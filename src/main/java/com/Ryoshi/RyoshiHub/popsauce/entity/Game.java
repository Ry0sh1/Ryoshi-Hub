package com.Ryoshi.RyoshiHub.popsauce.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class Game {

    private String code;
    private Player host;
    private int currentTimer;
    private int currentPicture;
    private Setting setting;
    private List<Picture> pictures;
    private List<Player> players;

    public Game() {
        pictures = new ArrayList<>();
        players = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Game{" +
                "code='" + code + '\'' +
                ", host=" + host +
                ", currentTimer=" + currentTimer +
                ", currentPicture=" + currentPicture +
                ", setting=" + setting +
                ", pictures=" + pictures +
                ", players=" + players +
                '}';
    }
}
