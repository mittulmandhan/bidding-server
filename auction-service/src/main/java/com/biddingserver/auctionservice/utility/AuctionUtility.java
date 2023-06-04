package com.biddingserver.auctionservice.utility;

import com.biddingserver.auctionservice.config.RabbitMQConfig;
import com.biddingserver.auctionservice.entity.Auction;
import com.biddingserver.auctionservice.event.AuctionBiddersNotifyEvent;
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
import java.util.stream.Collectors;

@Component
public class AuctionUtility {

    @Autowired
    private RabbitTemplate template;

    @Autowired
    private AuctionRepository auctionRepository;

    @Async
    @Scheduled(fixedRate = 20*1000)
    public void closeAllExpiredAuctions() {
        List<Auction> expiredAuctionsList = auctionRepository.findAllByStatus(AuctionStatus.RUNNING.toString());

        expiredAuctionsList.stream().forEach(a -> System.out.println(a));

        expiredAuctionsList.stream().filter(auc -> isAuctionExpired(auc)).forEach(auction -> closeExpiredAuction(auction));

    }

    @Async
    public void notifyUsersAboutHighestBid(Auction auction) {
        List<String> emails = auction.getBidList().stream().map(bid -> bid.getUser().getEmail()).distinct().collect(Collectors.toList());
        AuctionBiddersNotifyEvent auctionBiddersNotifyEvent = new AuctionBiddersNotifyEvent();
        auctionBiddersNotifyEvent.setItemCode(auction.getItemCode());
        auctionBiddersNotifyEvent.setHighestBidAmount(auction.getHighestBid().getBidAmount());
        auctionBiddersNotifyEvent.setEmails(emails);

        template.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_KEY,
                auctionBiddersNotifyEvent
        );
    }

    private boolean isAuctionExpired(Auction auction) {
        Date currentTime = new Date();
        Date auctionExpirationTime = DateUtils.addMinutes(new Date(auction.getCreateDate()), auction.getDuration());

//        System.out.println("current time: " + currentTime);
//        System.out.println("auction creation time: " + new Date(auction.getCreateDate()));
//        System.out.println("auction expiration time: " + auctionExpirationTime);

        return currentTime.after(auctionExpirationTime);
    }

    private void closeExpiredAuction(Auction auction) {
        // marking auction status over
        auction.setStatus(AuctionStatus.OVER.toString());
        auction = auctionRepository.save(auction);

        if(auction.getWinnerEmail() == null)
            return;

        AuctionWinnerMailEvent auctionWinnerMailEvent = new AuctionWinnerMailEvent();
        auctionWinnerMailEvent.setItemCode(auction.getItemCode());
        auctionWinnerMailEvent.setWinnerEmail(auction.getWinnerEmail());

        template.convertAndSend(RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_KEY,
                auctionWinnerMailEvent);
    }

//    private Auction markAuctionOver(Auction auction) {
//        auction.setStatus(AuctionStatus.OVER.toString());
//        return auctionRepository.save(auction);
//    }

}
