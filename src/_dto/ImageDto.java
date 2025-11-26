package _dto;

import java.awt.*;

public class ImageDto {
    public final String uri;
    public final Image image;


    public ImageDto(String uri, Image image) {
        this.uri = uri;
        this.image = image;
    }
}
