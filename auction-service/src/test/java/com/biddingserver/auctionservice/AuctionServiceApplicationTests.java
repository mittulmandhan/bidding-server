package com.biddingserver.auctionservice;

import com.biddingserver.auctionservice.entity.Auction;
import com.biddingserver.auctionservice.model.AuctionRequestDTO;
import com.biddingserver.auctionservice.repository.AuctionRepository;
import com.biddingserver.auctionservice.service.AuctionService;
import com.biddingserver.auctionservice.utility.AuctionStatus;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
class AuctionServiceApplicationTests {

	@Autowired
	AuctionService auctionService;

	@MockBean
	AuctionRepository auctionRepository;

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

		assertEquals(null, auctionService.createAuction(auctionRequestDTO));
	}







}
