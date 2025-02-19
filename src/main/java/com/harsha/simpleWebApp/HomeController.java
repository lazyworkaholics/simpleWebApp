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

//@RestController
//public class HomeController {
//
//    private final PhotoStorageService photoStorageService;
//    public HomeController(PhotoStorageService photoStorageService) {
//        this.photoStorageService = photoStorageService;
//    }
//
//    @RequestMapping("/")
//    public List<String> greet() {
//        return photoStorageService.listFiles();
//    }
//
//    @RequestMapping("/about")
//    public String about() {
//        return "This is about page";
//    }
//}

@RestController
public class HomeController {

    private final PhotoStorageService photoStorageService;
    public HomeController(PhotoStorageService photoStorageService) {
        this.photoStorageService = photoStorageService;
    }

    // Directory where thumbnails are stored locally on your computer
    private final String thumbnailsDirectory = "/Users/pabbineedi_harsha/Desktop/RagnarPhotos/1stMonthShoot";

    // Method to list all images (you can also use a dynamic list here if needed)
    @GetMapping("/harsha")
    public String listImages(Model model) {
        System.out.println("1");
        File folder = new File(thumbnailsDirectory);
        System.out.println("2");
        File[] imageFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".png"));
        System.out.println("3");
        // Check if the directory contains images and pass them to the model
        if (imageFiles != null && imageFiles.length > 0) {
            System.out.println("images available");
            model.addAttribute("images", imageFiles);
        } else {
            System.out.println("no images");
            model.addAttribute("images", new File[0]); // Empty array if no images found
        }

        return "harsha";  // Thymeleaf template
    }

    @GetMapping("/harshaimages")
    public List<String> greet() {
        return photoStorageService.listFiles();
    }

    // Method to serve image thumbnails from local storage
    @GetMapping("{imageName}")
    @ResponseBody
    public ResponseEntity<Resource> getThumbnail(@PathVariable String imageName) throws MalformedURLException {
        // Construct the file path for the thumbnail from local storage
        Path path = Paths.get(thumbnailsDirectory + imageName);
        UrlResource resource = new UrlResource(path.toUri());
        if (resource.exists() || resource.isReadable()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)  // Adjust this based on the image type (JPEG, PNG, etc.)
                    .body((Resource) resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
