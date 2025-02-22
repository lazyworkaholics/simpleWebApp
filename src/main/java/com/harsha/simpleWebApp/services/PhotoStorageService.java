package com.harsha.simpleWebApp.services;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;

@Service
public class PhotoStorageService {
    private final String storagePath = "/Users/pabbineedi_harsha/Desktop/RagnarPhotos/";
    public List<String> listFiles() {
        File folder = new File(storagePath);
        if (!folder.exists() || !folder.isDirectory()) {
            throw new RuntimeException("Storage directory does not exist: " + storagePath);
        }
        return listFilesRecursive(folder,null);
    }
    private List<String> listFilesRecursive(File folder, String parentFolderPath) {
        File[] files = folder.listFiles();
        if (files == null) {
            return List.of();
        }
        return Arrays.stream(files)
                .flatMap(file -> {
                    if (file.isFile()) {
                        if (parentFolderPath == null) {
                            return Arrays.stream(new String[]{file.getName()});
                        } else {
                            return Arrays.stream(new String[]{parentFolderPath+"/"+file.getName()});
                        }
                    } else if (file.isDirectory()) {
                        if (parentFolderPath == null) {
                            return listFilesRecursive(file, file.getName()).stream();
                        } else {
                            return listFilesRecursive(file, parentFolderPath+"/"+file.getName()).stream();
                        }
                    }
                    return Arrays.stream(new String[]{});
                })
                .filter(name -> name.toLowerCase().endsWith(".jpg")) // Ignore DS_Store
                .sorted()
                .collect(Collectors.toList());
    }
    public byte[] imageById(String id) {
        String fullPathString = storagePath + "/" + id;
        Path imagePath = new File(fullPathString).toPath();
        try {
//            byte[] imageBytes = Files.readAllBytes(imagePath);
//            return imageBytes;
            BufferedImage originalImage = ImageIO.read(imagePath.toFile());
            if (originalImage == null) {
                throw new IOException("Invalid image file: " + id);
            }
            int originalImageWidth = originalImage.getWidth();
            int originalImageHeight = originalImage.getHeight();
            BufferedImage resizedImage;
            Graphics2D g;
            if(originalImageWidth > originalImageHeight) {
                resizedImage = new BufferedImage(180, 120, BufferedImage.TYPE_INT_RGB);
                g = resizedImage.createGraphics();
                g.drawImage(originalImage, 0, 0, 180, 120, null);
            } else {
                resizedImage = new BufferedImage(120, 180, BufferedImage.TYPE_INT_RGB);
                g = resizedImage.createGraphics();
                g.drawImage(originalImage, 0, 0, 120, 180, null);
            }
            g.dispose();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(resizedImage, "jpg", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
