package com.ari.Imager.exception;

public class ImageNotFoundException extends RuntimeException{
    public ImageNotFoundException(Integer id){
        super("Image with Id: "+ id + " does not exist");
    }
}
