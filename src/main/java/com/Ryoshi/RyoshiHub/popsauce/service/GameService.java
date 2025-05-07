package com.Ryoshi.RyoshiHub.popsauce.service;

import com.Ryoshi.RyoshiHub.popsauce.entity.Game;
import com.Ryoshi.RyoshiHub.popsauce.entity.Picture;
import com.Ryoshi.RyoshiHub.popsauce.entity.Player;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class GameService {

    private final HashMap<String, Game> gameMap;
    private final List<Game> startedGames;

    public GameService() {
        this.gameMap = new HashMap<>();
        this.startedGames = new ArrayList<>();
    }

    public Game getByCode(String code) {
        return gameMap.get(code);
    }

    public List<Game> findAllByStarted() {
        return startedGames;
    }

    public void startGameByCode(String code) {
        startedGames.add(gameMap.get(code));
    }

    public void stopGameByCode(String code) {
        startedGames.remove(gameMap.get(code));
    }

    public boolean isStarted(Game game) {
        return startedGames.contains(game);
    }

    public void save(Game game) {
        gameMap.put(game.getCode(), game);
    }

    public boolean containsCode(String code) {
        return gameMap.containsKey(code);
    }

    public void delete(Game game) {
        gameMap.remove(game.getCode());
    }

    public List<Player> getAllPlayersByGame(Game game) {
        return gameMap.get(game.getCode()).getPlayers();
    }

    public void addPlayerToGame(Game game, Player player) {
        gameMap.get(game.getCode()).getPlayers().add(player);
    }

    public List<Picture> getAllPicturesByGame(Game game) {
        return gameMap.get(game.getCode()).getPictures();
    }

    public void addPictureToGame(Game game, Picture picture) {
        gameMap.get(game.getCode()).getPictures().add(picture);
    }

    public Picture getCurrentPictureOfGame(Game game) {
        return game.getPictures().get(game.getCurrentPicture());
    }

    public void deleteAllGamePicturesByGame(Game game) {
        gameMap.get(game.getCode()).getPictures().clear();
    }
}
