package com.biddingserver.auctionservice.service;

import com.biddingserver.auctionservice.model.BidRequestDTO;
import com.biddingserver.auctionservice.utility.BidStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface BidService {
    BidStatus bidByItem(BidRequestDTO bid, Long itemCode, String userEmail);

}
