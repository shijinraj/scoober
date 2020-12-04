package com.takeaway.scoober.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

import com.takeaway.scoober.domain.Player;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Testable
@DisplayName("GameUtil Test")
class GameUtilTest {

	@Test
	@DisplayName("Test play with Number less than 2")
	void testPlayWithNumberLessThanTwo() {
		// Given , When, Then
		IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
				() -> GameUtil.play(1, null));
		assertEquals("Number should be greater than or equal to 2", illegalArgumentException.getMessage());
	}

	@Test
	@DisplayName("Test play with Null Player Name")
	void testPlayWithNullPlayerName() {
		// Given , When, Then
		IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
				() -> GameUtil.play(2, null));
		assertEquals("Player Name should not be null", illegalArgumentException.getMessage());
	}

	@Test
	@DisplayName("Test play with valid Number and player")
	void testPlay() {
		// Given , When
		Player player = GameUtil.play(56, "player1");
		// Then
		assertEquals(player.getResultingNumber(), 19);
		assertEquals(player.getAddedNumber(), 1);
		assertEquals(player.getName(), "player1");
	}

}
