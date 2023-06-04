package com.biddingserver.auctionservice.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Bid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private Long bidAmount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "auction_id", nullable = false)
    private Auction auction;

    @Override
    public String toString() {
        return "Bid{" +
                "id=" + id +
                ", bidAmount=" + bidAmount +
                '}';
    }
}
