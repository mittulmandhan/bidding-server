package com.biddingserver.security.event;

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

}
