package com.Ryoshi.RyoshiHub.popsauce.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Setting {

    public long id;
    public int guessTimer;
    public int resultTimer;
    public String category;

}
