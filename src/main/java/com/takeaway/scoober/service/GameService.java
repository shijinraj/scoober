package com.takeaway.scoober.service;

import java.util.List;

import com.takeaway.scoober.domain.Player;

public interface GameService {
	
	List<Player> playManual(int number);
	
	List<Player> playAutomatic();
	
	Player play(int number);

}
