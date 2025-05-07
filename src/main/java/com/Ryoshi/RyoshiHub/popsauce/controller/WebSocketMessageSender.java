package com.Ryoshi.RyoshiHub.popsauce.controller;

import com.Ryoshi.RyoshiHub.popsauce.entity.*;
import com.Ryoshi.RyoshiHub.popsauce.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class WebSocketMessageSender {

    private final SimpMessageSendingOperations messagingTemplate;
    private final PlayerService playerService;
    private final SettingService settingService;
    private final GameService gameService;

    public void sendNewTimer(Game game, Message message){
        message.setGameCode(game.getCode());
        message.setSender(game.getCode());
        message.setMessageType(MessageType.TIME);
        message.setContent(String.valueOf(game.getCurrentTimer()));
        messagingTemplate.convertAndSend("/start-game/game/"+game.getCode(),message);
    }
    public void sendNewPicture(Game game, Message message) {
        message.setGameCode(game.getCode());
        message.setSender(game.getCode());
        message.setMessageType(MessageType.PICTURE);
        message.setContent(game.getPictures().get(game.getCurrentPicture()));
        messagingTemplate.convertAndSend("/start-game/game/"+game.getCode(),message);
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) Objects.requireNonNull(headerAccessor.getSessionAttributes()).get("username");
        String gameCode = (String) headerAccessor.getSessionAttributes().get("gameCode");

        System.out.println("Someone Disconnected");
        System.out.println(username);
        if (username != null) {
            System.out.println(username + " left the game");
            var chatMessage = Message.builder()
                    .gameCode(gameCode)
                    .messageType(MessageType.LEAVE)
                    .sender(username)
                    .build();

            Game game = gameService.getByCode(gameCode);
            Player player = playerService.findByUsername(username);

            if (!gameService.getAllPlayersByGame(game).isEmpty() && game.getHost().getUsername().equals(player.getUsername())){
                game.setHost(gameService.getAllPlayersByGame(game).get(0));
                messagingTemplate.convertAndSend("/start-game/game/"+game.getCode(), chatMessage);
            }

            if (gameService.getAllPlayersByGame(game).isEmpty()){
                gameService.deleteAllGamePicturesByGame(game);
                gameService.delete(game);
                settingService.deleteById(game.getSetting().getId());
            }

            playerService.deleteByUsername(player.getUsername());
        }
    }

}
