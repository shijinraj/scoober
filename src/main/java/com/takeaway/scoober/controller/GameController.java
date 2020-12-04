package com.takeaway.scoober.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.takeaway.scoober.domain.Player;
import com.takeaway.scoober.service.GameService;

import io.swagger.annotations.Api;
import springfox.documentation.annotations.ApiIgnore;

@Api(value = "REST APIs related for Game of Three - Coding Challenge -" + "${spring.application.name}")
@RestController
@RequestMapping("/api/game/")
public class GameController {

	@Autowired
	private GameService gameService;

	@GetMapping("${spring.application.name}" + "/manual")
	public List<Player> playManual(@RequestParam int number) {
		return gameService.playManual(number);
	}

	@GetMapping("${spring.application.name}" + "/automatic")
	public List<Player> playAutomatic() {
		return gameService.playAutomatic();
	}

	@ApiIgnore
	@GetMapping("${spring.application.name}" + "/play")
	public Player play(@RequestParam int number) {
		return gameService.play(number);
	}

}
