package com.biddingserver.security.event.listener;

import com.biddingserver.security.config.RabbitMQConfig;
import com.biddingserver.security.event.AuctionBiddersNotifyEvent;
import com.biddingserver.security.event.AuctionWinnerMailEvent;
import com.biddingserver.security.service.MailSenderService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuctionEventListener {

    @Autowired
    private MailSenderService mailSenderService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void notifyBidderListener(AuctionBiddersNotifyEvent auctionBiddersNotifyEvent) {
        auctionBiddersNotifyEvent.getEmails().stream().forEach(emailId -> mailSenderService.send(emailId, "Update on Highest Bid placed on" + auctionBiddersNotifyEvent.getItemCode(), "Dear User\nThis is to update you highest bid placed on " + auctionBiddersNotifyEvent.getItemCode() + " is now " + auctionBiddersNotifyEvent.getHighestBidAmount()));
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void winnerEmailListener(AuctionWinnerMailEvent auctionWinnerMailEvent) {
        System.out.println(auctionWinnerMailEvent);
        mailSenderService.send(auctionWinnerMailEvent.getWinnerEmail(), "Congratulations! You Won the Auction", "Hi \n Congratulations! You are the winner of the auction for item: " + auctionWinnerMailEvent.getItemCode());
    }

}
