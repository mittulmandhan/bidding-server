package com.biddingserver.auctionservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AuctionWinnerMailEvent {

    private Long auctionId;
    private String winnerEmail;

}
