package com.example.background_image.Image.RemoveBackGround;

import com.google.gson.annotations.SerializedName;

public class RemoveImagebackground {
    @SerializedName("id")
    private int id;
    @SerializedName("image_data")
    private String imageData;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageData() {
        return imageData;
    }

    public void setImageData(String imageData) {
        this.imageData = imageData;
    }
}
