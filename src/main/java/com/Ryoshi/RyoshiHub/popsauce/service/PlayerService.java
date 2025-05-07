package com.Ryoshi.RyoshiHub.popsauce.service;

import com.Ryoshi.RyoshiHub.popsauce.entity.Player;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class PlayerService {

    private final HashMap<String, Player> playerMap;

    public PlayerService() {
        this.playerMap = new HashMap<>();
    }

    public Player findByUsername(String username) {
        return playerMap.get(username);
    }

    public void save(Player player) {
        playerMap.put(player.getUsername(), player);
    }

    public void deleteByUsername(String username) {
        playerMap.remove(username);
    }

}
