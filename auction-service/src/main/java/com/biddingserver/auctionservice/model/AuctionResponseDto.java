package com.biddingserver.auctionservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AuctionResponseDto {
    Long itemCode;
    Long highestBidAmount;
    Long stepRate;
}
