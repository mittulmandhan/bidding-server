package com.biddingserver.auctionservice.controller;

import com.biddingserver.auctionservice.model.AuctionRequestDTO;
import com.biddingserver.auctionservice.model.AuctionResponseDto;
import com.biddingserver.auctionservice.model.BidRequestDTO;
import com.biddingserver.auctionservice.service.AuctionService;
import com.biddingserver.auctionservice.service.BidService;
import com.biddingserver.auctionservice.utility.AuctionStatus;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/")
    public Long startAuction(@RequestBody AuctionRequestDTO auctionRequestDTO) {
        Long auctionId = auctionService.createAuction(auctionRequestDTO);
        return auctionId;
    }

    @GetMapping("/")
    public List<AuctionResponseDto> getAuctionsByStatus(@RequestParam String status) {
        if(!isStatusValid(status)) {
            return null;
        }

        return auctionService.getAuctionsByStatus(status.toUpperCase());
    }

    @PostMapping("/{itemCode}/bid")
    public ResponseEntity<String> bidByItem(@RequestBody BidRequestDTO bid, @PathVariable Long itemCode, @RequestHeader("user-email") String userEmail) {
        return bidService.bidByItem(bid, itemCode, userEmail);
    }

    private boolean isStatusValid(String status) {
        String s = status.toUpperCase();
        return s.equals(AuctionStatus.RUNNING.toString()) || s.equals(AuctionStatus.OVER.toString());
    }

}
