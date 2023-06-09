package com.biddingserver.auctionservice.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.List;

@Entity
@Setter
@Getter
@EqualsAndHashCode
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

    @OneToOne
    private Bid highestBid;

    private String winnerEmail;
}
