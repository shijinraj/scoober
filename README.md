# Takeaway - Game of Three - Coding Challenge
## Goal
*   The Goal is to implement a game with two independent units – the players –
    communicating with each other using an API.
## Description
When a player starts, it incepts a random (whole) number and sends it to the second
player as an approach of starting the game. The receiving player can now always choose
between adding one of {-1, 0, 1} to get to a number that is divisible by 3. Divide it by three. The
resulting whole number is then sent back to the original sender.

## Setup
*   JDK 8 or higher version
*   Maven 3.5.X
*   Run application using command line from the **project directory** - mvn clean install
*   Run **Player1** application - mvn spring-boot:run
*   **player1** Swagger URL - http://localhost:8080/swagger-ui/index.html
*   Run **Player2** application using command line from the **project target** directory - java -jar scoober-0.0.1-SNAPSHOT.jar --spring.config.location=classpath:/player2/
*   **player2** Swagger URL - http://localhost:9000/swagger-ui/index.html

## 1. Play Manual:
*  Player1 API - HTTP : GET - api/game/player1/manual?number=56
*  Player2 API - HTTP : GET - api/game/player2/manual?number=1001
*  Request Parameter - number
*  HTTP response :
```json
[
  {
    "addedNumber": 0,
    "name": "string", -- player name 
    "resultingNumber": 0
  }
]
```
*   only **resultingNumber** value present for the first player and **addedNumber** will be null.
*   player with **resultingNumber** JSON value as **1** will be the **Winner**
*   For example: 
```json
[
  {
    "name": "player1",
    "addedNumber": null, -- addedNumber will be null for first player
    "resultingNumber": 56
  },
  {
    "name": "player2",
    "addedNumber": 1,
    "resultingNumber": 19
  },
  {
    "name": "player1",
    "addedNumber": -1,
    "resultingNumber": 6
  },
  {
    "name": "player2",
    "addedNumber": 0,
    "resultingNumber": 2
  },
  {
    "name": "player1",
    "addedNumber": 1,
    "resultingNumber": 1 -- player1 is the winner!!!
  }
]
```
## 2. Play Automatic:
*  Player1 API - HTTP : GET - api/game/player1/automatic
*  Player2 API - HTTP : GET - api/game/player2/automatic
*  HTTP response :
```json
[
  {
    "addedNumber": 0,
    "name": "string", -- player name 
    "resultingNumber": 0
  }
]
```
*   only **resultingNumber** value present for the first player and **addedNumber** will be null.
*   player with **resultingNumber** JSON value as **1** will be the **Winner**
*   For example: 
```json
[
  {
    "name": "player1",
    "addedNumber": null,-- addedNumber will be null for first player
    "resultingNumber": 6
  },
  {
    "name": "player2",
    "addedNumber": 0,
    "resultingNumber": 2
  },
  {
    "name": "player1",
    "addedNumber": 1,
    "resultingNumber": 1 -- player1 is the winner!!!
  }
]
```
## Application Features
*   Spring Boot project with Spring Dev tools, Spring Cache, Spring cloud open feign,  JUnit 5 and Swagger
*   Centralised Exception handling using @RestControllerAdvice - please refer the package com.takeaway.scoober.exception
*   Exception example
```json
{
  "error_code": 172789177, --unique identifier for the exception
  "error_type": "Bad Request",
  "error_description": "Request has invalid parameters",
  "more_info": "Number should be greater than or equal to 2"
}
```
*   Unit Tests and Integration Test cases are available
*   Cached service calls
*   player1 and player2 runs on its own (independent programs, two browsers)
*   Easily configured single spring boot application for player1 (application.yml) and player2 (/player2/application.yml)
```yaml
    server:
      port: $applicationPort -- player1 port is 8080 and player2 port is 9000
    spring:
      application:
        name: $applicationName -- player1 or player2
    nextPlayerName:
      name: $nextPlayerName -- player1 or player2
      url:  $nextPlayerNameURL -- player1 URL is localhost:8080/api/game/ and player2 URL is localhost:9000/api/game/ 
    feign:
      hystrix: 
        enabled: true -- spring cloud openfeign Fallback handling
```
*   player1 and player2 Communication via an REST
*   A player may not be available when the other one starts - spring cloud openfeign Fallback addresses this problem

 

## Assumption
*   input number is positive integer greater than or equal to 2
*   adding one number range is {-1, 0, 1}
*   random number is calculated in a range of 1000 for for Automatic play