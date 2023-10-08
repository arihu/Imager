package com.ari.Imager.repository;

import com.ari.Imager.image.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Integer> {

}
