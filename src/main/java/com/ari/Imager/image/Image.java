package com.ari.Imager.image;

import com.ari.Imager.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "image")
public class Image {

    @Id
    @GeneratedValue
    private Integer imageId;
    private String title;
    //base 64 representation of image
    private String img;
    private String description;
    private Integer userId;

}
