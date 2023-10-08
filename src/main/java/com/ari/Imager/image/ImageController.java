package com.ari.Imager.image;

import com.ari.Imager.auth.AuthenticationService;
import com.ari.Imager.exception.ImageNotFoundException;
import com.ari.Imager.repository.ImageRepository;
import com.ari.Imager.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/image")
@RequiredArgsConstructor
public class ImageController {

    @Autowired
    private ImageRepository imageRepository;
    private final AuthenticationService service;

    @PostMapping
    Image newImage(@RequestBody Image newImage) {
        User authenticatedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        newImage.setUserId(authenticatedUser.getUserId());
        return imageRepository.save(newImage);
    }

    @GetMapping
    List<Image> getAllImages() {
        return imageRepository.findAll();
    }

    @GetMapping("/{id}")
    Image getImageById(@PathVariable Integer id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new ImageNotFoundException(id));
    }

    @PutMapping("/{id}")
    Image updateImage(@RequestBody Image newImage, @PathVariable Integer id) {
        User authenticatedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return imageRepository.findById(id)
                .map(image -> {
                    // Check if the authenticated user's userId matches the image's userId
                    if (authenticatedUser.getUserId().equals(image.getUserId())) {
                        // Only allow the update if the userIds match
                        image.setTitle(newImage.getTitle());
                        image.setImg(newImage.getImg());
                        image.setDescription(newImage.getDescription());
                        return imageRepository.save(image);
                    } else {
                        // If userIds don't match, throw an exception or return an appropriate response
                        throw new AccessDeniedException("You are not authorized to update this image.");
                    }
                }).orElseThrow(() -> new ImageNotFoundException(id));
    }

    @DeleteMapping("/{id}")
    String deleteImage(@PathVariable Integer id){
        User authenticatedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!imageRepository.existsById(id)){
            throw new ImageNotFoundException(id);
        }
        Image imageToDelete = imageRepository.findById(id)
                .orElseThrow(() -> new ImageNotFoundException(id));

        if (authenticatedUser.getUserId().equals(imageToDelete.getUserId())) {
            imageRepository.deleteById(id);
            return "Image with id " + id + " has been deleted.";
        } else {
            // If userIds don't match, throw an exception or return an appropriate response
            throw new AccessDeniedException("You are not authorized to delete this image.");
        }
    }


}
