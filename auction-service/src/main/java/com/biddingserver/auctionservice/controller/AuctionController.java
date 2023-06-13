package com.biddingserver.auctionservice.controller;

import com.biddingserver.auctionservice.model.AuctionRequestDTO;
import com.biddingserver.auctionservice.model.AuctionResponseDto;
import com.biddingserver.auctionservice.model.BidRequestDTO;
import com.biddingserver.auctionservice.service.AuctionService;
import com.biddingserver.auctionservice.service.BidService;
import com.biddingserver.auctionservice.utility.AuctionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            throw new IllegalArgumentException("Possible Values for status : " + AuctionStatus.values());
        }

        Pageable paged = PageRequest.of(pageNumber, pageSize);

        return auctionService.getAuctionsByStatus(auctionStatus.name(), paged);
    }

    // To make user bid in an auction using item code
    @PostMapping("/{itemCode}/bid")
    public ResponseEntity<String> bidByItem(@RequestBody BidRequestDTO bid, @PathVariable Long itemCode, @RequestHeader("user-email") String userEmail) {
        return bidService.bidByItem(bid, itemCode, userEmail);
    }

}
