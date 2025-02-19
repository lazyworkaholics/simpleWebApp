package com.harsha.simpleWebApp.services;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class PhotoStorageService {
    private final String storagePath = "/Users/pabbineedi_harsha/Desktop/RagnarPhotos/1stMonthShoot";
    public List<String> listFiles() {
        File folder = new File(storagePath);
        if (!folder.exists() || !folder.isDirectory()) {
            throw new RuntimeException("Storage directory does not exist: " + storagePath);
        }
        return Arrays.stream(folder.listFiles())
                .map(file -> file.getName())
                .sorted()
                .collect(Collectors.toList());
    }
}
