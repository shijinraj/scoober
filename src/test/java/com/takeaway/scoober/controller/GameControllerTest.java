package com.takeaway.scoober.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.NestedServletException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.takeaway.scoober.domain.Player;
import com.takeaway.scoober.service.GameService;

@AutoConfigureMockMvc
@ContextConfiguration(classes = { GameController.class, GameService.class })
@WebMvcTest
@TestInstance(Lifecycle.PER_CLASS)
class GameControllerTest {

	@Value("${spring.application.name}")
	private String playerName;

	private static String BASE_URL = "/api/game/";

	private static final String UTF_8 = "utf-8";

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private GameService gameService;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeAll
	void setUpBeforeClass() throws Exception {
		BASE_URL += playerName;
	}

	@Test
	@DisplayName("Test playManual with valid number")
	void testPlayManual() throws JsonProcessingException, Exception {

		// Given
		List<Player> playersExpected = new ArrayList<Player>();
		playersExpected.add(Player.builder().name("player1").resultingNumber(6).build());
		playersExpected.add(Player.builder().name("player2").resultingNumber(2).addedNumber(0).build());
		playersExpected.add(Player.builder().name("player1").resultingNumber(1).addedNumber(1).build());

		when(gameService.playManual(anyInt())).thenReturn(playersExpected);

		// When & Then
		mockMvc.perform(get(BASE_URL + "/manual?number=56").characterEncoding(UTF_8)).andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string(containsString(objectMapper.writeValueAsString(playersExpected))));

	}

	@Test
	@DisplayName("Test playManual with invalid number")
	void testPlayManualWithInvalidNumber() throws JsonProcessingException, Exception {

		// Given
		when(gameService.playManual(1)).thenThrow(IllegalArgumentException.class);

		// When, Then
		NestedServletException nestedServletException = assertThrows(NestedServletException.class,
				() -> mockMvc.perform(get(BASE_URL + "/manual?number=1").characterEncoding(UTF_8)));

		Assertions.assertThat(nestedServletException.getCause()).isExactlyInstanceOf(IllegalArgumentException.class);

	}

	@Test
	@DisplayName("Test testPlayAutomatic")
	void testPlayAutomatic() throws JsonProcessingException, Exception {
		// Given
		List<Player> playersExpected = new ArrayList<Player>();
		playersExpected.add(Player.builder().name("player1").resultingNumber(6).build());
		playersExpected.add(Player.builder().name("player2").resultingNumber(2).addedNumber(0).build());
		playersExpected.add(Player.builder().name("player1").resultingNumber(1).addedNumber(1).build());

		when(gameService.playAutomatic()).thenReturn(playersExpected);

		// When & Then
		mockMvc.perform(get(BASE_URL + "/automatic").characterEncoding(UTF_8)).andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString(objectMapper.writeValueAsString(playersExpected))));
	}

	@Test
	@DisplayName("Test testPlay")
	void testPlay() throws JsonProcessingException, Exception {
		// Given
		Player playerExpected = Player.builder().name("player2").resultingNumber(19).addedNumber(1).build();
		when(gameService.play(anyInt())).thenReturn(playerExpected);

		// When & Then
		mockMvc.perform(get(BASE_URL + "/play?number=56").characterEncoding(UTF_8)).andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string(containsString(objectMapper.writeValueAsString(playerExpected))));
	}

	@Test
	@DisplayName("Test testPlay with invalid Number")
	void testPlayWithInvalidNumber() throws JsonProcessingException, Exception {
		// Given
		when(gameService.play(1)).thenThrow(IllegalArgumentException.class);

		// When & Then
		// When, Then
		NestedServletException nestedServletException = assertThrows(NestedServletException.class,
				() -> mockMvc.perform(get(BASE_URL + "/play?number=1").characterEncoding(UTF_8)));

		Assertions.assertThat(nestedServletException.getCause()).isExactlyInstanceOf(IllegalArgumentException.class);
	}

}
