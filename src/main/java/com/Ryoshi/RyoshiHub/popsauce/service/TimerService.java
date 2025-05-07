package com.Ryoshi.RyoshiHub.popsauce.service;

import com.Ryoshi.RyoshiHub.popsauce.controller.WebSocketMessageSender;
import com.Ryoshi.RyoshiHub.popsauce.entity.Game;
import com.Ryoshi.RyoshiHub.popsauce.entity.Message;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TimerService {

    private final GameService gameService;
    private final WebSocketMessageSender webSocketMessageSender;

    public TimerService(GameService gameService, WebSocketMessageSender webSocketMessageSender) {
        this.gameService = gameService;
        this.webSocketMessageSender = webSocketMessageSender;
    }

    @Scheduled(fixedRate = 1000) // Run every second
    public void decrementTimer() {
        List<Game> games = gameService.findAllByStarted();
        for (Game game:games) {
            if (game.getCurrentTimer()==0){
                int newPlace = game.getCurrentPicture() + 1;

                int amountOfPictures = game.getPictures().size();
                if (newPlace > amountOfPictures){
                    game.setCurrentPicture(0);
                }else {
                    game.setCurrentPicture(newPlace);
                }

                game.setCurrentTimer(game.getSetting().getGuessTimer()+game.getSetting().getResultTimer());
                webSocketMessageSender.sendNewPicture(game, new Message());
                webSocketMessageSender.sendNewTimer(game, new Message());
            }else {
                game.setCurrentTimer(game.getCurrentTimer()-1);
                webSocketMessageSender.sendNewTimer(game, new Message());
            }
        }
    }

}