package com.takeaway.scoober.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.takeaway.scoober.domain.Player;
import com.takeaway.scoober.util.GameUtil;

/**
 * GameClientFallback service
 * 
 * @author Shijin Raj
 *
 */
@Component
public class GameClientFallback implements GameClient {

	@Value("${nextPlayerName.name}")
	private String nextPlayerName;

	@Override
	public Player play(int number) {
		Assert.isTrue(2 <= number, "Number should be greater than or equal to 2");
		return GameUtil.play(number, nextPlayerName);
	}

}
