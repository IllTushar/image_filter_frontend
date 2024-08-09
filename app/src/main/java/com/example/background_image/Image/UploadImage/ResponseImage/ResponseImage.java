package com.example.background_image.Image.UploadImage.ResponseImage;

import com.google.gson.annotations.SerializedName;

public class ResponseImage {

    @SerializedName("id")
    private Integer id;
    @SerializedName("image_data")
    private String imageData;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImageData() {
        return imageData;
    }

    public void setImageData(String imageData) {
        this.imageData = imageData;
    }
}
