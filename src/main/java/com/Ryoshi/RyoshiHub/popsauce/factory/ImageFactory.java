package com.Ryoshi.RyoshiHub.popsauce.factory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageFactory {

    public static List<File> getFilesInFolder(final File folder) {
        List<File> pathOfFiles = new ArrayList<>();
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                pathOfFiles.addAll(getFilesInFolder(fileEntry));
            } else {
                pathOfFiles.add(fileEntry);
            }
        }
        return pathOfFiles;
    }

}
