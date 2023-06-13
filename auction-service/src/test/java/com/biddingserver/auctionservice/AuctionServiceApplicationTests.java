package com.biddingserver.auctionservice;

import com.biddingserver.auctionservice.entity.Auction;
import com.biddingserver.auctionservice.entity.Bid;
import com.biddingserver.auctionservice.entity.User;
import com.biddingserver.auctionservice.model.AuctionRequestDTO;
import com.biddingserver.auctionservice.model.BidRequestDTO;
import com.biddingserver.auctionservice.repository.AuctionRepository;
import com.biddingserver.auctionservice.repository.BidRepository;
import com.biddingserver.auctionservice.repository.UserRepository;
import com.biddingserver.auctionservice.service.AuctionService;
import com.biddingserver.auctionservice.service.BidService;
import com.biddingserver.auctionservice.utility.AuctionStatus;
import org.apache.commons.lang.time.DateUtils;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
class AuctionServiceApplicationTests {

	@Autowired
	AuctionService auctionService;

	@Autowired
	BidService bidService;

	@MockBean
	AuctionRepository auctionRepository;

	@MockBean
	BidRepository bidRepository;

	@MockBean
	UserRepository userRepository;

	@Test
	public void createAuctionWhenItemHasNoRunningAuctionTest() {

		AuctionRequestDTO auctionRequestDTO = new AuctionRequestDTO();
		auctionRequestDTO.setItemCode(10L);
		auctionRequestDTO.setBasePrice(1000L);
		auctionRequestDTO.setDuration(10);
		auctionRequestDTO.setStepRate(250L);

		Auction auctionForRepository = new Auction();
		auctionForRepository.setItemCode(10L);
		auctionForRepository.setBasePrice(1000L);
		auctionForRepository.setDuration(10);
		auctionForRepository.setStepRate(250L);
		auctionForRepository.setStatus(AuctionStatus.RUNNING.toString());

		Auction responseAuction = new Auction();
		responseAuction.setId(1L);
		responseAuction.setItemCode(10L);
		responseAuction.setBasePrice(1000L);
		responseAuction.setDuration(10);
		responseAuction.setStepRate(250L);

		when(auctionRepository.save(auctionForRepository)).thenReturn(responseAuction);
		when(auctionRepository.findByItemCodeAndStatus(auctionRequestDTO.getItemCode(), AuctionStatus.RUNNING.toString())).thenReturn(Optional.empty());

		assertEquals(responseAuction.getId(), auctionService.createAuction(auctionRequestDTO));
	}

	@Test
	public void createAuctionWhenItemAlreadyHaveRunningAuctionTest() {

		AuctionRequestDTO auctionRequestDTO = new AuctionRequestDTO();
		auctionRequestDTO.setItemCode(10L);
		auctionRequestDTO.setBasePrice(1000L);
		auctionRequestDTO.setDuration(10);
		auctionRequestDTO.setStepRate(250L);

		Auction auctionForRepository = new Auction();
		auctionForRepository.setItemCode(10L);
		auctionForRepository.setBasePrice(1000L);
		auctionForRepository.setDuration(10);
		auctionForRepository.setStepRate(250L);
		auctionForRepository.setStatus(AuctionStatus.RUNNING.toString());

		Auction responseAuction = new Auction();
		responseAuction.setId(1L);
		responseAuction.setItemCode(10L);
		responseAuction.setBasePrice(1000L);
		responseAuction.setDuration(10);
		responseAuction.setStepRate(250L);

		when(auctionRepository.save(auctionForRepository)).thenReturn(responseAuction);
		when(auctionRepository.findByItemCodeAndStatus(auctionRequestDTO.getItemCode(), AuctionStatus.RUNNING.toString())).thenReturn(Optional.of(responseAuction));

		assertNull(auctionService.createAuction(auctionRequestDTO));
	}

	@Test
	public void userBiddingOnItemHavingRunningAuction() {
		User user = new User();
		user.setId(200L);
		user.setFirstName("Mittul");
		user.setLastName("Mandhan");
		user.setEmail("conanmittul@gmail.com");

		Bid highestBid = new Bid();
		highestBid.setId(99L);
		highestBid.setBidAmount(1000L);
		highestBid.setUser(user);


		Auction auction = new Auction();
		auction.setId(1L);
		auction.setStatus(AuctionStatus.RUNNING.toString());
		auction.setItemCode(10L);
		auction.setBasePrice(1000L);
		auction.setStepRate(250L);
		auction.setWinnerEmail("");
		auction.setCreateDate(new Date().getTime());

		highestBid.setAuction(auction);
		auction.setHighestBid(highestBid);

		Bid bid = new Bid();
		bid.setBidAmount(1250L);
		bid.setUser(user);
		bid.setAuction(auction);

		Bid responseBid = new Bid();
		responseBid.setId(100L);
		responseBid.setBidAmount(1250L);
		responseBid.setUser(user);
		responseBid.setAuction(auction);

		Auction responseAuction = new Auction();
		responseAuction.setId(1L);
		responseAuction.setStatus(AuctionStatus.RUNNING.toString());
		responseAuction.setItemCode(10L);
		responseAuction.setBasePrice(1000L);
		responseAuction.setStepRate(250L);
		responseAuction.setHighestBid(highestBid);
		responseAuction.setWinnerEmail("");
		responseAuction.setCreateDate(new Date().getTime());

		when(auctionRepository.findByItemCodeAndStatus(auction.getItemCode(), AuctionStatus.RUNNING.toString())).thenReturn(Optional.of(auction));
		when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
		when(bidRepository.save(bid)).thenReturn(responseBid);
		when(auctionRepository.setHighestBidOptimistic(responseBid.getId(), auction.getId(), responseBid.getUser().getEmail(), responseBid.getBidAmount())).thenReturn(1);

		assertEquals(new ResponseEntity<>("Bid is Accepted", HttpStatus.CREATED), bidService.bidByItem(new BidRequestDTO(bid.getBidAmount()), auction.getItemCode(), user.getEmail()));
	}

	@Test
	public void userBiddingFirstTimeOnItemHavingRunningAuction() {
		User user = new User();
		user.setId(200L);
		user.setFirstName("Mittul");
		user.setLastName("Mandhan");
		user.setEmail("conanmittul@gmail.com");

		Auction auction = new Auction();
		auction.setId(1L);
		auction.setStatus(AuctionStatus.RUNNING.toString());
		auction.setItemCode(10L);
		auction.setBasePrice(1000L);
		auction.setStepRate(250L);
		auction.setWinnerEmail("");
		auction.setCreateDate(new Date().getTime());
		auction.setHighestBid(null);

		Bid bid = new Bid();
		bid.setBidAmount(1250L);
		bid.setUser(user);
		bid.setAuction(auction);

		Bid responseBid = new Bid();
		responseBid.setId(100L);
		responseBid.setBidAmount(1250L);
		responseBid.setUser(user);
		responseBid.setAuction(auction);

		when(auctionRepository.findByItemCodeAndStatus(auction.getItemCode(), AuctionStatus.RUNNING.toString())).thenReturn(Optional.of(auction));
		when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
		when(bidRepository.save(bid)).thenReturn(responseBid);
		when(auctionRepository.setFirstBidOptimistic(responseBid.getId(), auction.getId(), responseBid.getUser().getEmail())).thenReturn(1);

		assertEquals(new ResponseEntity<>("Bid is Accepted", HttpStatus.CREATED), bidService.bidByItem(new BidRequestDTO(bid.getBidAmount()), auction.getItemCode(), user.getEmail()));
	}

	@Test
	public void userBiddingOnItemHavingNoRunningAuction() {
		when(auctionRepository.findByItemCodeAndStatus(10L, AuctionStatus.RUNNING.toString())).thenReturn(Optional.empty());

		assertEquals(new ResponseEntity<>("Auction Not Found", HttpStatus.NOT_FOUND), bidService.bidByItem(new BidRequestDTO(1200L), 10L, "conanmittul@gmail.com"));
	}


	@Test
	public void userBiddingOnRunningButExpiredAuction() {

		Auction auction = new Auction();
		auction.setId(1L);
		auction.setStatus(AuctionStatus.RUNNING.toString());
		auction.setItemCode(10L);
		auction.setBasePrice(1000L);
		auction.setHighestBid(null);
		auction.setWinnerEmail("");
		auction.setCreateDate(DateUtils.addMinutes(new Date(), -11).getTime());

		when(auctionRepository.findByItemCodeAndStatus(10L, AuctionStatus.RUNNING.toString())).thenReturn(Optional.of(auction));

		assertEquals(new ResponseEntity<>("Auction Not Found", HttpStatus.NOT_FOUND), bidService.bidByItem(new BidRequestDTO(1200L), 10L, "conanmittul@gmail.com"));
	}

}
