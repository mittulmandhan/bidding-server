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

    @Override
    public ResponseEntity<String> bidByItem(BidRequestDTO bidRequestDTO, Long itemCode, String userEmail) {
        Auction auction = auctionRepository.findByItemCodeAndStatus(itemCode, AuctionStatus.RUNNING.toString());

        if(auction == null || isAuctionExpired(auction))
            return new ResponseEntity<>("Auction Not Found", HttpStatus.NOT_FOUND);

        Bid bid = prepareBid(bidRequestDTO, auction, userEmail);
        bid = bidRepository.save(bid);

        if(isBidAcceptable(auction, bid)) {
            auction.setHighestBid(bid);
            auction.setWinnerEmail(bid.getUser().getEmail());
            auctionRepository.save(auction);
            return new ResponseEntity<>("Bid is Accepted", HttpStatus.CREATED);
        }
//        else if(auction.getHighestBid() != null && bid.getBidAmount() >= (auction.getHighestBid().getBidAmount() + auction.getStepRate())) {
//            auction.setHighestBid(bid);
//            auctionRepository.save(auction);
//            return new ResponseEntity<>("Bid is Accepted", HttpStatus.CREATED);
//        }



        return new ResponseEntity<>("Bid is Rejected", HttpStatus.NOT_ACCEPTABLE);
    }

    private boolean isAuctionExpired(Auction auction) {
        Date currentTime = new Date();
        Date auctionExpirationTime = DateUtils.addMinutes(new Date(auction.getCreateDate()), 10);

//        System.out.println("current time: " + currentTime);
//        System.out.println("auction creation time: " + new Date(auction.getCreateDate()));
//        System.out.println("auction expiration time: " + auctionExpirationTime);

        return currentTime.after(auctionExpirationTime);
    }

    // Checks if the bid must be accepted or not
    private boolean isBidAcceptable(Auction auction, Bid bid) {

        Bid HighestBid = auction.getHighestBid();

        if(HighestBid == null)
            return bid.getBidAmount() >=  auction.getBasePrice();

        // or if there exists a bid already then incoming bid must be greater than or equal to the sum of existing highest bid and step rate
        return bid.getBidAmount() >= (HighestBid.getBidAmount() + auction.getStepRate());
    }

    private Bid prepareBid(BidRequestDTO bidRequestDTO, Auction auction, String userEmail) {
        Bid bid = new Bid();
        User user = userRepository.findByEmail(userEmail);

        bid.setBidAmount(bidRequestDTO.getBidAmount());
        bid.setAuction(auction);
        bid.setUser(user);

        return bid;
    }
}
