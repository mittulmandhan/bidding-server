package com.biddingserver.security.event.listener;

import com.biddingserver.security.config.RabbitMQConfig;
import com.biddingserver.security.event.AuctionWinnerMailEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class AuctionWinnerMailEventListener {

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void listener(AuctionWinnerMailEvent auctionWinnerMailEvent) {
        System.out.println(auctionWinnerMailEvent);
    }

}
