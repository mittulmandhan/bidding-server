package com.biddingserver.auctionservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


// This will be pushed in the queue to announce user
// Details in this event class will be used to
// send mail to the user who won the auction

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AuctionWinnerMailEvent {

    private Long itemCode;
    private String winnerEmail;

}
