package com.takeaway.scoober;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.takeaway.scoober.domain.Player;
import com.takeaway.scoober.exception.ErrorDetails;
import com.takeaway.scoober.exception.ExceptionHandlerConstants;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(Lifecycle.PER_CLASS)
class ScooberPlayerApplicationTests {

	private static final String LOCAL_HOST = "http://localhost:";
	private static String BASE_URL = "/api/game/";

	private String PLAY_MANUAL_URL = null;
	private String PLAY_AUTOMATIC_URL = null;
	private String PLAY_URL = null;

	@Value("${spring.application.name}")
	private String playerName;

	@LocalServerPort
	int randomServerPort;

	@Autowired
	private TestRestTemplate restTemplate;

	@BeforeAll
	void setUpBeforeClass() throws Exception {
		BASE_URL += playerName;
		PLAY_MANUAL_URL = LOCAL_HOST + randomServerPort + BASE_URL + "/manual";
		PLAY_AUTOMATIC_URL = LOCAL_HOST + randomServerPort + BASE_URL + "/automatic";
		PLAY_URL = LOCAL_HOST + randomServerPort + BASE_URL + "/play";
	}

	@Test
	@DisplayName("Test playManual with valid number")
	void testPlayManual() {
		// Given
		List<Player> playersExpected = new ArrayList<Player>();
		playersExpected.add(Player.builder().name("player1").resultingNumber(56).build());
		playersExpected.add(Player.builder().name("player2").resultingNumber(19).addedNumber(1).build());
		playersExpected.add(Player.builder().name("player1").resultingNumber(6).addedNumber(-1).build());
		playersExpected.add(Player.builder().name("player2").resultingNumber(2).addedNumber(0).build());
		playersExpected.add(Player.builder().name("player1").resultingNumber(1).addedNumber(1).build());

		// When
		ResponseEntity<Player[]> playerResponse = restTemplate.getForEntity(PLAY_MANUAL_URL + "?number=56",
				Player[].class);

		// Then
		assertNotNull(playerResponse);
		assertNotNull(playerResponse.getBody());

		Assertions.assertThat(Arrays.asList(playerResponse.getBody()))
				.containsExactlyInAnyOrderElementsOf(playersExpected);

	}

	@Test
	@DisplayName("Test playManual with invalid number")
	void testPlayManualWithInvalid() {
		// Given
		ErrorDetails errorDetailsExpected = ErrorDetails.builder().type(HttpStatus.BAD_REQUEST.getReasonPhrase())
				.description(ExceptionHandlerConstants.INVALID_PARAMETER)
				.moreInfo("Number should be greater than or equal to 2").build();

		// When
		ResponseEntity<ErrorDetails> errorDetailsResponse = restTemplate.getForEntity(PLAY_MANUAL_URL + "?number=1",
				ErrorDetails.class);

		// Then
		assertNotNull(errorDetailsResponse);
		assertNotNull(errorDetailsResponse.getBody());

		ErrorDetails errorDetailsActual = errorDetailsResponse.getBody();

		Assertions.assertThat(errorDetailsActual).usingRecursiveComparison().ignoringFields("code")
				.isEqualTo(errorDetailsExpected);

	}

	@Test
	@DisplayName("Test playAutomatic")
	void testPlayAutomatic() {
		// When
		ResponseEntity<Player[]> playerResponse = restTemplate.getForEntity(PLAY_AUTOMATIC_URL, Player[].class);

		// Then
		assertNotNull(playerResponse);
		assertNotNull(playerResponse.getBody());

		List<Player> players = Arrays.asList(playerResponse.getBody());

		Collections.sort(players, Comparator.comparing(Player::getResultingNumber));

		assertEquals(1, players.get(0).getResultingNumber());

	}

	@Test
	@DisplayName("Test play with valid number")
	void testPlay() {

		// When
		ResponseEntity<Player> playerResponse = restTemplate.getForEntity(PLAY_URL + "?number=56", Player.class);

		// Then
		assertNotNull(playerResponse);
		assertNotNull(playerResponse.getBody());

		// Then
		assertNotNull(playerResponse);
		assertNotNull(playerResponse.getBody());

		Assertions.assertThat(playerResponse.getBody())
				.isEqualTo(Player.builder().name("player1").resultingNumber(19).addedNumber(1).build());

	}

	@Test
	@DisplayName("Test play with invalid number")
	void testPlayWithInvalidNumber() {

		// Given
		ErrorDetails errorDetailsExpected = ErrorDetails.builder().type(HttpStatus.BAD_REQUEST.getReasonPhrase())
				.description(ExceptionHandlerConstants.INVALID_PARAMETER)
				.moreInfo("Number should be greater than or equal to 2").build();

		// When
		ResponseEntity<ErrorDetails> errorDetailsResponse = restTemplate.getForEntity(PLAY_URL + "?number=1",
				ErrorDetails.class);

		// Then
		assertNotNull(errorDetailsResponse);
		assertNotNull(errorDetailsResponse.getBody());

		ErrorDetails errorDetailsActual = errorDetailsResponse.getBody();

		Assertions.assertThat(errorDetailsActual).usingRecursiveComparison().ignoringFields("code")
				.isEqualTo(errorDetailsExpected);
	}

}
