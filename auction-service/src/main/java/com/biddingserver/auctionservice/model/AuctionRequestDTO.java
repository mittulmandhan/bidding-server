package com.biddingserver.auctionservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuctionRequestDTO {

    private Long itemCode;

    private Long basePrice;

    private Long stepRate;

    private Integer duration;

    public String toString() {
        return "{" + this.getItemCode() + ", " + this.getBasePrice() + ", " + this.getStepRate() + ", " + this.getDuration() + "}";
    }

}
