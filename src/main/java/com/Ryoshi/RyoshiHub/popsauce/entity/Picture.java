package com.Ryoshi.RyoshiHub.popsauce.entity;

import com.Ryoshi.RyoshiHub.popsauce.model.Guess;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class Picture {
    private byte[] content;
    private String uuid;
    private String category;
    private Guess guess;
}
