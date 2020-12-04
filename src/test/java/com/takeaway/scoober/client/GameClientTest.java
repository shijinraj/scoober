package com.takeaway.scoober.client;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.takeaway.scoober.domain.Player;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Game Client Test")
class GameClientTest {
	
	@Autowired
	private GameClient gameClient;

	@Test
	@DisplayName("Test play with valid Number")
	void testPlay() {
		// Given , When
		Player player = gameClient.play(56);

		// Then
		assertEquals(player.getResultingNumber(), 19);
		assertEquals(player.getAddedNumber(), 1);
		assertEquals(player.getName(), "player2");
	}

}
