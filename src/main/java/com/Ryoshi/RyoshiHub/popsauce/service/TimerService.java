package com.Ryoshi.RyoshiHub.popsauce.service;


import com.Ryoshi.RyoshiHub.popsauce.controller.MessageController;
import com.Ryoshi.RyoshiHub.popsauce.controller.WebSocketMessageSender;
import com.Ryoshi.RyoshiHub.popsauce.entity.Game;
import com.Ryoshi.RyoshiHub.popsauce.entity.GamePicture;
import com.Ryoshi.RyoshiHub.popsauce.entity.Message;
import com.Ryoshi.RyoshiHub.popsauce.repository.GamePictureRepository;
import com.Ryoshi.RyoshiHub.popsauce.repository.GameRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TimerService {

    private final GameRepository gameRepository;
    private final GamePictureRepository gamePictureRepository;
    private final WebSocketMessageSender webSocketMessageSender;

    public TimerService(GameRepository gameRepository,
                        GamePictureRepository gamePictureRepository, MessageController messageController, WebSocketMessageSender webSocketMessageSender) {
        this.gameRepository = gameRepository;
        this.gamePictureRepository = gamePictureRepository;
        this.webSocketMessageSender = webSocketMessageSender;
    }

    @Scheduled(fixedRate = 1000) // Run every second
    public void decrementTimer() {
        List<Game> games = gameRepository.findAllByStarted(true);
        for (Game game:games) {
            if (game.getCurrentTimer()==0){
                GamePicture picture = gamePictureRepository.findByGameAndPicture(game, game.getCurrentPicture()).orElseThrow();
                int newPlace = picture.getPlace() + 1;
                int amountOfPictures = gamePictureRepository.findAllByGame(game).size();
                if (newPlace<amountOfPictures){
                    game.setCurrentPicture(gamePictureRepository.findByGameAndPlace(game,newPlace).orElseThrow().getPicture());
                }else {
                    game.setCurrentPicture(gamePictureRepository.findByGameAndPlace(game,0).orElseThrow().getPicture());
                }
                gameRepository.save(game);
                game.setCurrentTimer(game.getSetting().getGuessTimer()+game.getSetting().getResultTimer());
                webSocketMessageSender.sendNewPicture(game,new Message());
                webSocketMessageSender.sendNewTimer(game, new Message());
            }else {
                game.setCurrentTimer(game.getCurrentTimer()-1);
                gameRepository.save(game);
                webSocketMessageSender.sendNewTimer(game, new Message());
            }
            gameRepository.save(game);
        }
    }

}