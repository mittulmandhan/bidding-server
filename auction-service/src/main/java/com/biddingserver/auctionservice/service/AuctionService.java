package com.biddingserver.auctionservice.service;

import com.biddingserver.auctionservice.model.AuctionRequestDTO;
import com.biddingserver.auctionservice.model.AuctionResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AuctionService {
    Long createAuction(AuctionRequestDTO auctionRequestDTO);

    List<AuctionResponseDto> getAuctionsByStatus(String upperCase, Pageable paged);
}
