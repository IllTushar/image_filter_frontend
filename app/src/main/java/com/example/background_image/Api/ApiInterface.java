package com.example.background_image.Api;

import com.example.background_image.Image.BlackWhite.ResponseBlackWhite;
import com.example.background_image.Image.RemoveBackGround.RemoveImagebackground;
import com.example.background_image.Image.UploadImage.ResponseImage.ResponseImage;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiInterface {
    @Multipart
    @POST("image/upload-image/")
    Call<ResponseImage> uploadImage(@Part MultipartBody.Part image);

    @POST("image/filter/black-white/{image_id}")
    Call<ResponseBlackWhite> blackWhiteImage(
            @Path("image_id") int image_id
    );

    @GET("image/remove-bg/{image_id}")
    Call<RemoveImagebackground> removeBackground(
            @Path("image_id") int image_id
    );

}
