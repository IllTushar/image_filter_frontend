package com.example.background_image.Api;

import com.example.background_image.Image.UploadImage.ResponseImage.ResponseImage;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {
    @Multipart
    @POST("image/upload-image/")
    Call<ResponseImage> uploadImage(@Part MultipartBody.Part image);
}
