package com.Ryoshi.RyoshiHub.popsauce.service;

import com.Ryoshi.RyoshiHub.popsauce.entity.Picture;
import com.Ryoshi.RyoshiHub.popsauce.factory.ImageFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

@Service
public class PictureService {

    public static final String picturePath = System.getenv("PICTURE_PATH");

    public List<Picture> findAllByCategory(String category) {
        List<Picture> pictures = new ArrayList<>();

        File directory = new File(picturePath + category);
        if (directory.exists() && directory.isDirectory()) {
            List<File> directories = new ArrayList<>(Arrays.asList(Objects.requireNonNull(directory.listFiles())));

            for (File dir : directories) {
                List<File> files = ImageFactory.getFilesInFolder(dir);
                for (File file : files) {
                    try {
                        pictures.add(new Picture(Files.readAllBytes(file.toPath()), dir.getName(), category));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return pictures;
    }

    public List<String> getAllCategories() {
        List<String> categories = new ArrayList<>();
        File directory = new File(picturePath);
        if (directory.exists() && directory.isDirectory()) {
            List<File> directories = new ArrayList<>(Arrays.asList(Objects.requireNonNull(directory.listFiles())));

            for (File dir : directories) {
                categories.add(dir.getName());
            }
        }
        return categories;
    }

}
