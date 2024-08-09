package com.example.background_image.Image.BlackWhite;

import com.google.gson.annotations.SerializedName;

public class ResponseBlackWhite {
    @SerializedName("id")
    private Integer id;
    @SerializedName("image_id")
    private Integer imageId;
    @SerializedName("black_white_image")
    private String blackWhiteImage;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public String getBlackWhiteImage() {
        return blackWhiteImage;
    }

    public void setBlackWhiteImage(String blackWhiteImage) {
        this.blackWhiteImage = blackWhiteImage;
    }
}
