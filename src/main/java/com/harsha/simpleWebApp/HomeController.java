package com.harsha.simpleWebApp;
import com.harsha.simpleWebApp.services.PhotoStorageService;
import jakarta.annotation.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
public class HomeController {
    private final PhotoStorageService photoStorageService;
    public HomeController(PhotoStorageService photoStorageService) {
        this.photoStorageService = photoStorageService;
    }
    @RequestMapping("/")
    public List<String> greet() {
        List<String> returnObject = photoStorageService.listFiles();
        return returnObject;
    }
    @RequestMapping("/about")
    public String about() {
        return "This is about page";
    }
    @RequestMapping("/image={imagePath}")
    public ResponseEntity<byte[]> getImage(@PathVariable("imagePath") String id) {
        byte[] image = photoStorageService.imageById(id);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
    }
}
