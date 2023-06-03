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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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

        if(auction == null)
            return new ResponseEntity<>("Auction Not Found", HttpStatus.NOT_FOUND);

        Bid bid = prepareBid(bidRequestDTO, auction, userEmail);

        if(isBidAcceptable(auction, bid)) {
            auction.setHighestBid(bid);
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

    // Checks if the bid must be accepted or not
    private boolean isBidAcceptable(Auction auction, Bid bid) {
        // check if this is the first bid then must be greater or equal to base price
        boolean result = auction.getHighestBid() == null && bid.getBidAmount() >=  auction.getBasePrice();

        // or if there exists a bid already then incoming bid must be greater than or equal to the sum of existing highest bid and step rate
        result = result || auction.getHighestBid() != null && bid.getBidAmount() >= (auction.getHighestBid().getBidAmount() + auction.getStepRate());


        return result;
    }

    private Bid prepareBid(BidRequestDTO bidRequestDTO, Auction auction, String userEmail) {
        Bid bid = new Bid();
        User user = userRepository.findByEmail(userEmail);

        bid.setBidAmount(bidRequestDTO.getBidAmount());
        bid.setAuction(auction);
        bid.setUser(user);

        bid = bidRepository.save(bid);

        return bid;
    }
}
