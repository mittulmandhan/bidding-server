package com.biddingserver.security.event;

import com.biddingserver.security.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class AddContactNumberEvent extends ApplicationEvent {

    User user;

    public AddContactNumberEvent(User user) {
        super(user);
        this.user = user;
    }
}
