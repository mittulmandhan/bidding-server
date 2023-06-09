package com.biddingserver.auctionservice.utility;

import com.biddingserver.auctionservice.config.RabbitMQConfig;
import com.biddingserver.auctionservice.entity.Auction;
import com.biddingserver.auctionservice.event.AuctionWinnerMailEvent;
import com.biddingserver.auctionservice.repository.AuctionRepository;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class AuctionUtility {

    @Autowired
    private RabbitTemplate template;

    @Autowired
    private AuctionRepository auctionRepository;

    // Asynchronous method to stop all expired auctions
    // and send email to the winner
    @Async
    @Scheduled(fixedRate = 20*1000)
    public void closeAllExpiredAuctions() {
        // gets all the running auctions
        List<Auction> expiredAuctionsList = auctionRepository.findAllByStatus(AuctionStatus.RUNNING.toString());

        // filters out all the expired auctions and closes them
        expiredAuctionsList.stream().filter(auc -> isAuctionExpired(auc)).forEach(this::closeExpiredAuction);

    }

    // checks if an auction is expired
    private boolean isAuctionExpired(Auction auction) {
        Date currentTime = new Date();
        Date auctionExpirationTime = DateUtils.addMinutes(new Date(auction.getCreateDate()), auction.getDuration());
        return currentTime.after(auctionExpirationTime);
    }

    // changes status of auction from running to over
    // and sends adds necessary details in the messaging queue to initiate email
    private void closeExpiredAuction(Auction auction) {
        // marking auction status over
        auction.setStatus(AuctionStatus.OVER.toString());
        auction = auctionRepository.save(auction);

        if(auction.getWinnerEmail() == null)
            return;

        // prepare message to add in the queue
        AuctionWinnerMailEvent auctionWinnerMailEvent = new AuctionWinnerMailEvent();
        auctionWinnerMailEvent.setItemCode(auction.getItemCode());
        auctionWinnerMailEvent.setWinnerEmail(auction.getWinnerEmail());

        // add message in the queue
        template.convertAndSend(RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_KEY,
                auctionWinnerMailEvent);
    }

}
