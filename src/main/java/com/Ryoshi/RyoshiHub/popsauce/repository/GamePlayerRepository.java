package com.Ryoshi.RyoshiHub.popsauce.repository;

import com.Ryoshi.RyoshiHub.popsauce.entity.Game;
import com.Ryoshi.RyoshiHub.popsauce.entity.GamePlayer;
import com.Ryoshi.RyoshiHub.popsauce.entity.Player;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GamePlayerRepository extends CrudRepository<GamePlayer, Long> {

    List<GamePlayer> findAllByGame(Game game);
    GamePlayer findByPlayer(Player player);
    GamePlayer findByPlayerAndGame(Player players, Game game);
    List<GamePlayer> findALlByPlayer(Player player);
}