package com.Ryoshi.RyoshiHub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RyoshiHubApplication {

	public static void main(String[] args) {
		SpringApplication.run(RyoshiHubApplication.class, args);
	}

}


/*
TODO: Add read Me
TODO: Change Service name on Server
Game I plan to program:
	* Qix
	* Pong

Plans for future:
	* Tron Online Multiplayer
	* Pong Online Multiplayer
	* Leaderboards
 */