package com.biddingserver.auctionservice.serviceimpl;

import com.biddingserver.auctionservice.config.RabbitMQConfig;
import com.biddingserver.auctionservice.entity.Auction;
import com.biddingserver.auctionservice.model.AuctionRequestDTO;
import com.biddingserver.auctionservice.event.AuctionWinnerMailEvent;
import com.biddingserver.auctionservice.repository.AuctionRepository;
import com.biddingserver.auctionservice.service.AuctionService;
import com.biddingserver.auctionservice.utility.AuctionStatus;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuctionServiceImpl implements AuctionService {

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private RabbitTemplate template;

    @Override
    public Long createAuction(AuctionRequestDTO auctionRequestDTO) {
        Auction auction = getAuctionFromAuctionRequestDTO(auctionRequestDTO);

        auction = auctionRepository.save(auction);

        AuctionWinnerMailEvent auctionWinnerMailEvent = new AuctionWinnerMailEvent();
        auctionWinnerMailEvent.setAuctionId(auction.getId());

        template.convertAndSend(RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_KEY,
                auctionWinnerMailEvent);

        return auction.getId();
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
