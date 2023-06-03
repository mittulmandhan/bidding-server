package com.biddingserver.security.event.listener;

import com.biddingserver.security.config.RabbitMQConfig;
import com.biddingserver.security.event.AuctionWinnerMailEvent;
import com.biddingserver.security.service.MailSenderService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuctionWinnerMailEventListener {

    @Autowired
    private MailSenderService mailSenderService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void listener(AuctionWinnerMailEvent auctionWinnerMailEvent) {
        System.out.println(auctionWinnerMailEvent);
        mailSenderService.send(auctionWinnerMailEvent.getWinnerEmail(), "Congratulations! You Won the Auction", "Hi \n Congratulations! You are the winner of the auction for item: " + auctionWinnerMailEvent.getItemCode());
    }

}
