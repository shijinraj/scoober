package com.takeaway.scoober.util;

import java.util.Arrays;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.takeaway.scoober.domain.Player;

public class GameUtil {

	public static int[] allowedArray = new int[] { -1, 0, 1 };

	public static Player play(int number, String playerName) {

		Assert.isTrue(2 <= number, "Number should be greater than or equal to 2");
		Assert.isTrue(!StringUtils.isEmpty(playerName), "Player Name should not be null");

		Integer addedNumber = Arrays.stream(allowedArray).boxed().filter(value -> ((number + value) % 3 == 0))
				.findFirst().orElseThrow(() -> new IllegalArgumentException("Invalid number - " + number));

		Integer resultingNumber = (number + addedNumber) / 3;

		return Player.builder().name(playerName).addedNumber(addedNumber).resultingNumber(resultingNumber).build();

	}

}
