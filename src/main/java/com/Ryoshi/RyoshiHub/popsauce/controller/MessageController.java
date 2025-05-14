package com.Ryoshi.RyoshiHub.popsauce.controller;

import com.Ryoshi.RyoshiHub.popsauce.entity.*;
import com.Ryoshi.RyoshiHub.popsauce.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Controller
@CrossOrigin
@RequiredArgsConstructor
public class MessageController {

    private final PictureService pictureService;
    private final GameService gameService;
    private final PlayerService playerService;

    @MessageMapping("/game.chat/{gameCode}")
    @SendTo("/start-game/game/{gameCode}")
    public Message chatMessage(@Payload Message message){
        return message;
    }

    @MessageMapping("/game.join/{gameCode}")
    @SendTo("/start-game/game/{gameCode}")
    public Message joinChat(@Payload Message message, SimpMessageHeaderAccessor headerAccessor){
        if (message.getMessageType() == MessageType.JOIN){
            headerAccessor.getSessionAttributes().put("username",message.getSender());
            headerAccessor.getSessionAttributes().put("gameCode",message.getGameCode());

            Game game = gameService.getByCode(message.getGameCode());
            Player newPlayer = new Player();
            newPlayer.setUsername(message.getSender());
            newPlayer.setPoints(0);
            if (playerService.findByUsername(message.getSender()) == null){
                playerService.save(newPlayer);
                gameService.addPlayerToGame(game, newPlayer);
            }
        }
        return message;
    }

    @MessageMapping("/game.start/{gameCode}")
    @SendTo("/start-game/game/{gameCode}")
    public Message gameGotStarted(@Payload Message message){
        Game game = gameService.getByCode(message.getGameCode());
        if (message.getSender().equals(game.getHost().getUsername())){
            gameService.startGameByCode(game.getCode());
            return message;
        }else {
            return null;
        }
    }

    @MessageMapping("/game.guess/{gameCode}")
    public Message guess(@Payload Message message) {
        Player player = playerService.findByUsername(message.getSender());
        Game game = gameService.getByCode(message.getGameCode());
        AtomicBoolean rightGuess = new AtomicBoolean(false);
        game.getPictures().get(game.getCurrentPicture()).getGuess().getGuesses().forEach(guess -> {
            if (message.getContent().equals(rightGuess)) {
                rightGuess.set(true);
            }
        });
        if (rightGuess.get()) {
            player.setPoints(player.getPoints() + 10);
            if (player.getPoints() >= 100){
                message.setMessageType(MessageType.END);
                message.setContent(message.getSender() + " won the Game");
                gameService.stopGameByCode(game.getCode());
                List<Player> allGamePlayer = gameService.getAllPlayersByGame(game);
                for (Player p: allGamePlayer) {
                    p.setPoints(0);
                }
            }
        }
        return message;
    }

    @MessageMapping("/game.playAgain/{gameCode}")
    @SendTo("/start-game/game/{gameCode}")
    public Message playAgain(@Payload Message message){

        Game game = gameService.getByCode(message.getGameCode());
        gameService.deleteAllGamePicturesByGame(game);

        List<Picture> pictures = pictureService.findAllByCategory(game.getSetting().getCategory());
        //Shuffle The list
        for (int i = 0;i < pictures.size();i++){
            Picture first = pictures.get(i);
            int random = (int) (Math.floor(Math.random()*pictures.size()));
            pictures.set(i,pictures.get(random));
            pictures.set(random,first);
        }
        //Insert List
        for (Picture picture : pictures) {
            gameService.addPictureToGame(game, picture);
        }

        game.setCurrentPicture(0);
        game.setCurrentTimer(game.getSetting().getGuessTimer()+game.getSetting().getResultTimer());

        gameService.startGameByCode(game.getCode());

        message.setMessageType(MessageType.PLAY_AGAIN);
        return message;
    }

}
