package com.biddingserver.auctionservice.utility;

import com.biddingserver.auctionservice.config.RabbitMQConfig;
import com.biddingserver.auctionservice.entity.Auction;
import com.biddingserver.auctionservice.event.AuctionWinnerMailEvent;
import com.biddingserver.auctionservice.repository.AuctionRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AuctionUtility {

    @Autowired
    private RabbitTemplate template;

    @Autowired
    private AuctionRepository auctionRepository;

    @Async
    synchronized public void closeAuctionAfter(Long auctionId, Integer auctionDuration) {

        try {
            wait(auctionDuration*1000*60);
        } catch (InterruptedException e) {
            System.out.println("closeAuction() has has interrupted");
        }

        Auction auction = auctionRepository.findById(auctionId).get();



        auction.setStatus(AuctionStatus.OVER.toString());

        auctionRepository.save(auction);

        AuctionWinnerMailEvent auctionWinnerMailEvent = new AuctionWinnerMailEvent();
        auctionWinnerMailEvent.setAuctionId(auctionId);

        template.convertAndSend(RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_KEY,
                auctionWinnerMailEvent);

    }

}
