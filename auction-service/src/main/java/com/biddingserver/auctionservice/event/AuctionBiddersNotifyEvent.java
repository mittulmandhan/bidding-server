package com.biddingserver.auctionservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AuctionBiddersNotifyEvent {
    Long itemCode;
    Long highestBidAmount;
    List<String> emails;
}
