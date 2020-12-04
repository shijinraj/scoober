package com.takeaway.scoober.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.takeaway.scoober.client.GameClient;
import com.takeaway.scoober.domain.Player;
import com.takeaway.scoober.util.GameUtil;

@Service
public class GameServiceImpl implements GameService {

	@Value("${spring.application.name}")
	private String playerName;

	@Autowired
	private GameClient gameClient;

	@Cacheable("playManual")
	@Override
	public List<Player> playManual(int number) {
		Assert.isTrue(number >= 2, "Number should be greater than or equal to 2");

		List<Player> players = new ArrayList<>();
		players.add(Player.builder().name(playerName).resultingNumber(number).build());

		do {
			// Call opposite player.play
			Player playerOutput = gameClient.play(number);
			players.add(playerOutput);
			number = playerOutput.getResultingNumber();

			if (number == 1) {
				break;
			}

			/// Call current player.play
			playerOutput = play(number);
			players.add(playerOutput);
			number = playerOutput.getResultingNumber();

		} while (number != 1);

		return players;
	}

	@Override
	public List<Player> playAutomatic() {
		return playManual(2 + Math.abs(new Random().nextInt(1000)));// random number within 1002

	}

	@Cacheable("play")
	@Override
	public Player play(int number) {

		return GameUtil.play(number, playerName);

	}

}
