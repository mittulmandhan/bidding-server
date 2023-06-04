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

import java.util.Date;

@Service
public class BidServiceImpl implements BidService {

    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private UserRepository userRepository;

    // allows user to place bid by item code
    @Override
    public ResponseEntity<String> bidByItem(BidRequestDTO bidRequestDTO, Long itemCode, String userEmail) {
        // get the running auction on the item
        Auction auction = auctionRepository.findByItemCodeAndStatus(itemCode, AuctionStatus.RUNNING.toString());

        // if there is no running auction for the given item
        // or the auction is expired
        if(auction == null || isAuctionExpired(auction))
            return new ResponseEntity<>("Auction Not Found", HttpStatus.NOT_FOUND);

        // prepare Bid
        Bid bid = prepareBid(bidRequestDTO, auction, userEmail);

        // save Bid
        bid = bidRepository.save(bid);

        // if the bid been placed is acceptable
        // then save it in the corresponding auction
        if(isBidAcceptable(auction, bid)) {

            // save bid as highest
            auction.setHighestBid(bid);
            // save highest bidder's email
            auction.setWinnerEmail(bid.getUser().getEmail());

            // save the bid in the list of all the accepted bids placed against this auction
            auction.getBidList().add(bid);
            // save auction
            auction = auctionRepository.save(auction);

            return new ResponseEntity<>("Bid is Accepted", HttpStatus.CREATED);
        }

        return new ResponseEntity<>("Bid is Rejected", HttpStatus.NOT_ACCEPTABLE);
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
