package com.Ryoshi.RyoshiHub.popsauce.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

@Setter
@Getter
@AllArgsConstructor
public class Picture {
    private byte[] content;
    private String rightGuess;
    private String category;

    @Override
    public String toString() {
        return "Picture{" +
                "content=" + Arrays.toString(content) +
                ", rightGuess='" + rightGuess + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
