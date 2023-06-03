package com.biddingserver.auctionservice.service;

import com.biddingserver.auctionservice.model.BidRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface BidService {
    ResponseEntity<String> bidByItem(BidRequestDTO bid, Long itemCode, String userEmail);

}
