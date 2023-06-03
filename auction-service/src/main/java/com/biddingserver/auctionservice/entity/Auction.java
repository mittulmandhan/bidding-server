package com.biddingserver.auctionservice.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Data
@Entity
@ToString
@EntityListeners(AuditingEntityListener.class)
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

    @CreatedDate
    private Long createDate;

    private Long highestBidId;

    private String winnerEmail;
}
