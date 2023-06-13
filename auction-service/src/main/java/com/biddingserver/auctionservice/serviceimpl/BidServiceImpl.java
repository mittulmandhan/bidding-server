package com.biddingserver.auctionservice.serviceimpl;

import com.biddingserver.auctionservice.entity.Auction;
import com.biddingserver.auctionservice.entity.Bid;
import com.biddingserver.auctionservice.entity.User;
import com.biddingserver.auctionservice.model.BidRequestDTO;
import com.biddingserver.auctionservice.repository.AuctionRepository;
import com.biddingserver.auctionservice.repository.BidRepository;
import com.biddingserver.auctionservice.repository.UserRepository;
import com.biddingserver.auctionservice.service.BidService;
import com.biddingserver.auctionservice.utility.AuctionStatus;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.OptimisticLockException;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.Optional;

@Service
public class BidServiceImpl implements BidService {

    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private UserRepository userRepository;

    // allows user to place bid by item code
    @Transactional
    @Override
    public ResponseEntity<String> bidByItem(BidRequestDTO bidRequestDTO, Long itemCode, String userEmail) {

        Optional<Auction> runningAuction = auctionRepository.findByItemCodeAndStatus(itemCode, AuctionStatus.RUNNING.toString());

        if(runningAuction.isEmpty())
            return new ResponseEntity<>("Auction Not Found", HttpStatus.NOT_FOUND);

        // get the running auction on the item
        Auction auction = runningAuction.get();

        // if there is no running auction for the given item
        // or the auction is expired
        if(isAuctionExpired(auction))
            return new ResponseEntity<>("Auction Not Found", HttpStatus.NOT_FOUND);

        // prepare Bid
        Bid bid = prepareBid(bidRequestDTO, auction, userEmail);

        // save Bid
        bid = bidRepository.save(bid);

        if(isAuctionAlmostExpired(auction)) {
            auction.setDuration(auction.getDuration()*2);
            auctionRepository.save(auction);
        }

        // if the bid been placed is acceptable
        // then save it in the corresponding auction
        if (isBidAcceptable(auction, bid)) {
            // save auction
            //update auction a set a.highest_bid = bid.getHighestBid() where a.highest_bid < bid.getHighestBid(); this method will return int (number of updated records
            if ((auction.getHighestBid() == null && auctionRepository.setFirstBidOptimistic(bid.getId(), auction.getId(), bid.getUser().getEmail()) == 0)
            || (auction.getHighestBid() != null && auctionRepository.setHighestBidOptimistic(bid.getId(), auction.getId(), bid.getUser().getEmail(), bid.getBidAmount()) == 0))
                throw new OptimisticLockException();

            return new ResponseEntity<>("Bid is Accepted", HttpStatus.CREATED);
        }

        return new ResponseEntity<>("Bid is Rejected", HttpStatus.NOT_ACCEPTABLE);
    }

    private boolean isAuctionAlmostExpired(Auction auction) {
        Date currentTime = new Date();
        long totalDuration = auction.getDuration()*60L*1000L;
        int threshold = 90;
        Date thresholdTime = DateUtils.addMilliseconds(new Date(auction.getCreateDate()), (int) ((totalDuration/100)*threshold));
        return currentTime.after(thresholdTime);
    }

    // checks if the auction is expired
    private boolean isAuctionExpired(Auction auction) {
        Date currentTime = new Date();
        Date auctionExpirationTime = DateUtils.addMinutes(new Date(auction.getCreateDate()), 10);
        return currentTime.after(auctionExpirationTime);
    }

    // Checks if the bid is acceptable or not
    private boolean isBidAcceptable(Auction auction, Bid bid) {

        Bid HighestBid = auction.getHighestBid();

        // if there is no bid placed against this auction
        // then the bid is acceptable if it is greater than or equal to base price
        if(HighestBid == null)
            return bid.getBidAmount() >=  auction.getBasePrice();

        // or if there exists a bid already then incoming bid must be greater than or equal to the sum of existing highest bid and step rate
        return bid.getBidAmount() >= (HighestBid.getBidAmount() + auction.getStepRate());
    }

    // prepare Bid from given parameters
    private Bid prepareBid(BidRequestDTO bidRequestDTO, Auction auction, String userEmail) {
        Bid bid = new Bid();
        User user = userRepository.findByEmail(userEmail);

        bid.setBidAmount(bidRequestDTO.getBidAmount());
        bid.setAuction(auction);
        bid.setUser(user);

        return bid;
    }
}
