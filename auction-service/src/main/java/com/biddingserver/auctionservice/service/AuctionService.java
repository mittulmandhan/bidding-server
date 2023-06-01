package com.biddingserver.auctionservice.service;

import com.biddingserver.auctionservice.model.AuctionRequestDTO;

public interface AuctionService {
    Long createAuction(AuctionRequestDTO auctionRequestDTO);
}
