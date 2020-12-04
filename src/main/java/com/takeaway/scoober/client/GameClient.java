package com.takeaway.scoober.client;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.takeaway.scoober.domain.Player;

@FeignClient(name = GameClient.SERVICE_NAME, url = "${nextPlayerName.url}", fallback = GameClientFallback.class)
public interface GameClient {

	String SERVICE_NAME = "gameClient";

	@Cacheable("gameClientPlay")
	@GetMapping("${nextPlayerName.name}" + "/play")
	Player play(@RequestParam int number);

}
