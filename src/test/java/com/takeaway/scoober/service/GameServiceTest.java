package com.takeaway.scoober.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.takeaway.scoober.client.GameClient;
import com.takeaway.scoober.domain.Player;

@ExtendWith(SpringExtension.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Game Service Test")
class GameServiceTest {

	@Mock
	private GameClient gameClient;

	@InjectMocks
	private GameService gameService = new GameServiceImpl();

	@Test
	@DisplayName("Test playManual with valid number")
	void testPlayManual() {
		// Given
		List<Player> playersExpected = new ArrayList<Player>();
		playersExpected.add(Player.builder().name("player1").resultingNumber(6).build());
		playersExpected.add(Player.builder().name("player2").resultingNumber(2).addedNumber(0).build());
		playersExpected.add(Player.builder().name("player1").resultingNumber(1).addedNumber(1).build());

		ReflectionTestUtils.setField(gameService, "playerName", "player1");
		when(gameClient.play(6)).thenReturn(Player.builder().name("player2").resultingNumber(2).addedNumber(0).build());
		// When
		List<Player> playersActual = gameService.playManual(6);

		// Then
		assertEquals(playersExpected, playersActual);
	}

	@Test
	@DisplayName("Test playManual with invalid number")
	void testPlayManualWithInavlidNumber() {
		// Given , When, Then
		IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
				() -> gameService.playManual(1));
		assertEquals("Number should be greater than or equal to 2", illegalArgumentException.getMessage());

	}

	@Test
	@DisplayName("Test test playAutomatic")
	void testPlayAutomatic() {

		// Given
		Player player = Player.builder().name("player2").resultingNumber(2).addedNumber(0).build();
		ReflectionTestUtils.setField(gameService, "playerName", "player1");
		when(gameClient.play(anyInt()))
				.thenReturn(Player.builder().name("player2").resultingNumber(2).addedNumber(0).build());

		List<Player> playersActual = gameService.playAutomatic();

		// Then
		Assertions.assertThat(playersActual).containsAnyOf(player);
		Assertions.assertThat(playersActual)
				.containsAnyOf(Player.builder().name("player1").resultingNumber(1).addedNumber(1).build());
	}

	@Test
	@DisplayName("Test play with Number less than 2")
	void testPlayWithNumberLessThanTwo() {
		// Given , When, Then
		ReflectionTestUtils.setField(gameService, "playerName", "player1");
		IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
				() -> gameService.play(1));
		assertEquals("Number should be greater than or equal to 2", illegalArgumentException.getMessage());
	}

	@Test
	@DisplayName("Test play with valid Number and player")
	void testPlay() {
		// Given , When
		ReflectionTestUtils.setField(gameService, "playerName", "player1");
		Player player = gameService.play(56);
		// Then
		assertEquals(player.getResultingNumber(), 19);
		assertEquals(player.getAddedNumber(), 1);
		assertEquals(player.getName(), "player1");
	}

}
