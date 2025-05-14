package com.Ryoshi.RyoshiHub.popsauce.controller;

import com.Ryoshi.RyoshiHub.popsauce.entity.Player;
import com.Ryoshi.RyoshiHub.popsauce.entity.Setting;
import com.Ryoshi.RyoshiHub.popsauce.service.GameService;
import com.Ryoshi.RyoshiHub.popsauce.service.PictureService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
@RequestMapping("/popsauce")
@CrossOrigin
@RequiredArgsConstructor
public class BoardController {

    private final GameService gameService;
    private final PictureService pictureService;

    @PostMapping("/upload-own-picture")
    public String uploadOwnPicture(
            @RequestParam("file") MultipartFile file,
            @RequestParam("category") String category,
            @RequestParam("rightGuesses") String rightGuesses) throws IOException {

        if (file.isEmpty() || !"image/jpeg".equals(file.getContentType())) {
            return "index";
        }

        Path dirPath = Paths.get(PictureService.picturePath, category, rightGuesses);
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }

        String fileName = UUID.randomUUID() + ".jpg";
        Path filePath = dirPath.resolve(fileName);
        file.transferTo(filePath.toFile());

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
