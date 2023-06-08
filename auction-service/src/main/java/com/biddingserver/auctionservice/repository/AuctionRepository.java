package com.biddingserver.auctionservice.repository;

import com.biddingserver.auctionservice.entity.Auction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Long> {
    Optional<Auction> findByItemCodeAndStatus(Long itemCode, String status);

    List<Auction> findAllByStatus(String auctionStatus);
}
