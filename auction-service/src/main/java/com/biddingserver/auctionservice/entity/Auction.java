package com.biddingserver.auctionservice.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Auction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private Long itemCode;

    private Long basePrice;

    private Long stepRate;

    private Integer duration;
}
