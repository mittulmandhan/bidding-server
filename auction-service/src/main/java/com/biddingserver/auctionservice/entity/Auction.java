package com.biddingserver.auctionservice.entity;

import com.biddingserver.auctionservice.utility.AuctionStatus;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Data
@Entity
@ToString
public class Auction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private Long itemCode;

    private Long basePrice;

    private Long stepRate;

    private Integer duration;

    private String status;
}
