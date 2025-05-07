package com.Ryoshi.RyoshiHub.popsauce.controller;

import com.Ryoshi.RyoshiHub.popsauce.entity.Player;
import com.Ryoshi.RyoshiHub.popsauce.entity.Setting;
import com.Ryoshi.RyoshiHub.popsauce.service.GameService;
import com.Ryoshi.RyoshiHub.popsauce.service.PictureService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/popsauce")
@CrossOrigin
@RequiredArgsConstructor
public class BoardController {

    private final GameService gameService;
    private final PictureService pictureService;

    @PostMapping("/upload-own-picture")
    public String uploadOwnPicture(
            @RequestParam("url") String url,
            @RequestParam("category") String category,
            @RequestParam("rightGuesses") String rightGuesses,
            @RequestParam("difficulty") String difficulty){
        //TODO
        return "popsauce/home";
    }

    @GetMapping("/create-game")
    public String getCreateGameWindow(Model model){
        model.addAttribute("settings", new Setting());
        model.addAttribute("categories", pictureService.getAllCategories());
        return "popsauce/create-game";
    }

    @GetMapping("/start-game/{code}")
    public String startGame(@PathVariable String code, Model model){
        model.addAttribute("game", gameService.getByCode(code));
        return "popsauce/in-game";
    }

    @GetMapping("")
    public String showHome(Model model){
        model.addAttribute("player",new Player());
        return "popsauce/home";
    }

}
