package com.biddingserver.auctionservice.controller;

import com.biddingserver.auctionservice.model.AuctionRequestDTO;
import com.biddingserver.auctionservice.model.AuctionResponseDto;
import com.biddingserver.auctionservice.model.BidRequestDTO;
import com.biddingserver.auctionservice.service.AuctionService;
import com.biddingserver.auctionservice.service.BidService;
import com.biddingserver.auctionservice.utility.AuctionStatus;
import com.biddingserver.auctionservice.utility.BidStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auction")
public class AuctionController {

    @Autowired
    private AuctionService auctionService;

    @Autowired
    private BidService bidService;

    // Allows admin to create and run the auction
    @PostMapping("/")
    public Long startAuction(@RequestBody AuctionRequestDTO auctionRequestDTO) {
        return auctionService.createAuction(auctionRequestDTO);
    }

    // To get the list auctions by their status
    @GetMapping("/")
    public List<AuctionResponseDto> getAuctionsByStatus(@RequestParam String status, @RequestParam Integer pageNumber, @RequestParam Integer pageSize) {
        AuctionStatus auctionStatus = null;
        try {
            // throws IllegalArgumentException if user has given invalid status
            auctionStatus = AuctionStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Possible Values for status : " + Arrays.stream(AuctionStatus.values()).map(s -> s.toString()).collect(Collectors.toList()).toString());
        }

        Pageable paged = PageRequest.of(pageNumber, pageSize);

        return auctionService.getAuctionsByStatus(auctionStatus.name(), paged);
    }

    // To make user bid in an auction using item code
    @PostMapping("/{itemCode}/bid")
    public ResponseEntity<String> bidByItem(@RequestBody BidRequestDTO bid, @PathVariable Long itemCode, @RequestHeader("user-email") String userEmail) {

        BidStatus bidStatus = bidService.bidByItem(bid, itemCode, userEmail);

        // if bid is accepted
        if(bidStatus.equals(BidStatus.BID_ACCEPTED))
            return new ResponseEntity<>("Bid is Accepted", HttpStatus.CREATED);
        // if bid is rejected
        else if (bidStatus.equals(BidStatus.BID_REJECTED))
            return new ResponseEntity<>("Bid is Rejected", HttpStatus.NOT_ACCEPTABLE);
        // if no auction is found for the given item code or the auction is expired
        return new ResponseEntity<>("Auction Not Found", HttpStatus.NOT_FOUND);
    }

}
