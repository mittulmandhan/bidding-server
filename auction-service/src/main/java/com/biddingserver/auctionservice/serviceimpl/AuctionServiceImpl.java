package com.biddingserver.auctionservice.serviceimpl;

import com.biddingserver.auctionservice.entity.Auction;
import com.biddingserver.auctionservice.entity.Bid;
import com.biddingserver.auctionservice.model.AuctionRequestDTO;
import com.biddingserver.auctionservice.model.AuctionResponseDto;
import com.biddingserver.auctionservice.repository.AuctionRepository;
import com.biddingserver.auctionservice.repository.BidRepository;
import com.biddingserver.auctionservice.service.AuctionService;
import com.biddingserver.auctionservice.utility.AuctionStatus;
import com.biddingserver.auctionservice.utility.AuctionUtility;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuctionServiceImpl implements AuctionService {

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private BidRepository bidRepository;

    @Override
    public Long createAuction(AuctionRequestDTO auctionRequestDTO) {
        Auction auction = getAuctionFromAuctionRequestDTO(auctionRequestDTO);

        auction = auctionRepository.save(auction);

//        auctionUtility.closeAuctionAfter(auction.getId(), auction.getDuration());

        return auction.getId();
    }

    @Override
    public List<AuctionResponseDto> getAuctionsByStatus(String status) {
        List<Auction> auctionList = auctionRepository.findAllByStatus(status);

        return auctionList.stream().map(this::prepareAuctionResponseDtoFromAuction).collect(Collectors.toList());
    }

    private AuctionResponseDto prepareAuctionResponseDtoFromAuction(Auction auction) {
        AuctionResponseDto auctionResponseDto = new AuctionResponseDto();
        Bid bid = null;

        if(auction.getHighestBidId() != null)
            bid = bidRepository.findById(auction.getHighestBidId()).get();

        if(bid != null)
            auctionResponseDto.setHighestBidAmount(bid.getBidAmount());
        else
            auctionResponseDto.setHighestBidAmount(null);

        auctionResponseDto.setItemCode(auction.getItemCode());
        auctionResponseDto.setStepRate(auction.getStepRate());

        return auctionResponseDto;
    }

    private Auction getAuctionFromAuctionRequestDTO(AuctionRequestDTO auctionRequestDTO) {
        Auction auction = new Auction();

        auction.setItemCode(auctionRequestDTO.getItemCode());
        auction.setBasePrice(auctionRequestDTO.getBasePrice());
        auction.setStepRate(auctionRequestDTO.getStepRate());
        auction.setDuration(auctionRequestDTO.getDuration());
        auction.setStatus(AuctionStatus.RUNNING.toString());
        return auction;
    }
}
