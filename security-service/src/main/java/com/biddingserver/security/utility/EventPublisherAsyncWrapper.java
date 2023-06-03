package com.biddingserver.security.utility;

import com.biddingserver.security.entity.User;
import com.biddingserver.security.event.AddContactNumberEvent;
import com.biddingserver.security.event.RegistrationCompleteEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class EventPublisherAsyncWrapper {

    @Autowired
    private ApplicationEventPublisher publisher;

    @Async
    public void publishRegistrationCompleteEvent(User user, String applicationUrl) {
        publisher.publishEvent(new RegistrationCompleteEvent(
                user,
                applicationUrl
        ));
    }

    @Async
    public void publishAddContactNumberEvent(User user) {
        publisher.publishEvent(new AddContactNumberEvent(user));
    }

}
