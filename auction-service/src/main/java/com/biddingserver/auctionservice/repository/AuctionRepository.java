package com.biddingserver.auctionservice.repository;

import com.biddingserver.auctionservice.entity.Auction;
import com.biddingserver.auctionservice.entity.Bid;
import org.hibernate.annotations.SQLUpdate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuctionRepository extends PagingAndSortingRepository<Auction, Long> {
    Optional<Auction> findByItemCodeAndStatus(Long itemCode, String status);

    List<Auction> findAllByStatus(String status);

    List<Auction> findAllByStatus(String auctionStatus, Pageable paged);

    @Modifying
    @Query(value = "UPDATE auction a SET a.highest_bid_id = :highestBidId, a.winner_email = :winnerEmail WHERE a.id = :auctionId AND (SELECT b.bid_amount FROM bid b where b.id = a.highest_bid_id) < :bidAmount", nativeQuery = true)
    int setHighestBidOptimistic(@Param("highestBidId") Long highestBidId, @Param("auctionId") Long auctionId, @Param("winnerEmail") String winnerEmail, @Param("bidAmount") Long bidAmount);

    @Modifying
    @Query(value = "UPDATE auction a SET a.highest_bid_id = :highestBidId, a.winner_email = :winnerEmail WHERE a.highest_bid_id IS NULL AND a.id = :auctionId", nativeQuery = true)
    int setFirstBidOptimistic(@Param("highestBidId") Long highestBidId, @Param("auctionId") Long auctionId, @Param("winnerEmail") String winnerEmail);
}
