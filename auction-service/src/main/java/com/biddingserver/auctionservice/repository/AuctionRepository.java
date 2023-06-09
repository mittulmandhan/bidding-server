package com.biddingserver.auctionservice.repository;

import com.biddingserver.auctionservice.entity.Auction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuctionRepository extends PagingAndSortingRepository<Auction, Long> {
    Optional<Auction> findByItemCodeAndStatus(Long itemCode, String status);

    List<Auction> findAllByStatus(String auctionStatus);

    List<Auction> findAllByStatus(String auctionStatus, Pageable paged);
}
