package com.biddingserver.auctionservice.controller;

import com.biddingserver.auctionservice.model.AuctionRequestDTO;
import com.biddingserver.auctionservice.model.BidRequestDTO;
import com.biddingserver.auctionservice.service.AuctionService;
import com.biddingserver.auctionservice.utility.AuctionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auction")
public class AuctionController {

    @Autowired
    AuctionService auctionService;

    @PostMapping("/")
    public Long startAuction(@RequestBody AuctionRequestDTO auctionRequestDTO) {
        Long auctionId = auctionService.createAuction(auctionRequestDTO);
        return auctionId;
    }

    @GetMapping("/")
    public String getAuctionsByStatus(@RequestParam String status) {
        if(!isStatusValid(status)) {
            return "Invalid Status";
        }
        AuctionStatus auctionStatus = AuctionStatus.valueOf(status.toUpperCase());

        if(auctionStatus.equals(AuctionStatus.RUNNING)) {
            return "Running Auctions";
        }
        return "Over Auctions";
    }

    @PostMapping("/{itemCode}/bid")
    public String bidByItem(@RequestBody BidRequestDTO bid, @PathVariable Long itemCode) {
        return itemCode + " " + bid.getBidAmount();
    }

    private boolean isStatusValid(String status) {
        String s = status.toUpperCase();
        return s.equals(AuctionStatus.RUNNING.toString()) || s.equals(AuctionStatus.OVER.toString());
    }

}
