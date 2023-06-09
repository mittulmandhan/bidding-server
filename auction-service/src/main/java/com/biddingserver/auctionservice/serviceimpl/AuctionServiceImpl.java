package com.biddingserver.auctionservice.serviceimpl;

import com.biddingserver.auctionservice.entity.Auction;
import com.biddingserver.auctionservice.entity.Bid;
import com.biddingserver.auctionservice.model.AuctionRequestDTO;
import com.biddingserver.auctionservice.model.AuctionResponseDto;
import com.biddingserver.auctionservice.repository.AuctionRepository;
import com.biddingserver.auctionservice.service.AuctionService;
import com.biddingserver.auctionservice.utility.AuctionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuctionServiceImpl implements AuctionService {

    @Autowired
    private AuctionRepository auctionRepository;

    @Override
    public Long createAuction(AuctionRequestDTO auctionRequestDTO) {

        // if auction is already running for the given item code then don't run auction
        if(itemHasRunningAuctionAlready(auctionRequestDTO.getItemCode()))
            return null;

        // convert AuctionRequestDTO to Auction
        Auction auction = prepareAuctionFromAuctionRequestDTO(auctionRequestDTO);

        // save auction
        auction = auctionRepository.save(auction);

        // return id if all goes well
        return auction.getId();
    }

    // checks if the item already has a running auction
    private boolean itemHasRunningAuctionAlready(Long itemCode) {
        return auctionRepository.findByItemCodeAndStatus(itemCode, AuctionStatus.RUNNING.toString()).isPresent();
    }

    // gets list of auctions by status
    @Override
    public List<AuctionResponseDto> getAuctionsByStatus(String status, Pageable paged) {
        List<Auction> auctionList = auctionRepository.findAllByStatus(status, paged);

        // map all Auction objects to AuctionResponseDto
        return auctionList.stream().map(this::prepareAuctionResponseDtoFromAuction).collect(Collectors.toList());
    }

    // map Auction objects to AuctionResponseDto
    private AuctionResponseDto prepareAuctionResponseDtoFromAuction(Auction auction) {
        AuctionResponseDto auctionResponseDto = new AuctionResponseDto();
        Bid bid = auction.getHighestBid();

        // if there's a bid placed against this auction
        if(bid != null)
            auctionResponseDto.setHighestBidAmount(bid.getBidAmount());
        // if there's no bid placed against this auction
        else
            auctionResponseDto.setHighestBidAmount(null);

        auctionResponseDto.setItemCode(auction.getItemCode());
        auctionResponseDto.setStepRate(auction.getStepRate());

        return auctionResponseDto;
    }

    // Map AuctionRequestDTO to Auction
    private Auction prepareAuctionFromAuctionRequestDTO(AuctionRequestDTO auctionRequestDTO) {
        Auction auction = new Auction();

        auction.setItemCode(auctionRequestDTO.getItemCode());
        auction.setBasePrice(auctionRequestDTO.getBasePrice());
        auction.setStepRate(auctionRequestDTO.getStepRate());
        auction.setDuration(auctionRequestDTO.getDuration());
        // initially the auction will be in running state
        auction.setStatus(AuctionStatus.RUNNING.toString());

        return auction;
    }
}
