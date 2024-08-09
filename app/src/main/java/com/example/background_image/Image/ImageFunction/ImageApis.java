package com.example.background_image.Image.ImageFunction;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatButton;

import com.example.background_image.Api.ApiInterface;
import com.example.background_image.Api.RetrofitClient;
import com.example.background_image.Cache.LocalCache;
import com.example.background_image.Image.BlackWhite.ResponseBlackWhite;
import com.example.background_image.Image.RemoveBackGround.RemoveImagebackground;
import com.example.background_image.Image.UploadImage.ResponseImage.ResponseImage;
import com.example.background_image.R;
import com.example.background_image.utils;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageApis {
    Context context;
    utils util;
    LocalCache cache;

    public ImageApis(Context context) {
        this.context = context;
        util = new utils(context);
        cache = new LocalCache(context);
    }

    // Method to get file path from URI
    public String getRealPathFromURI(Uri uri) {
        String path = null;
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            path = cursor.getString(columnIndex);
            cursor.close();
        }
        return path;
    }

    public void uploadImage(File file) {
        util.showDialog("Please wait");

        // Check if the file is a PNG
        String mimeType = getMimeType(file.getPath());
        if (!"image/png".equals(mimeType)) {
            util.toast(false, "Invalid file type! Please upload a PNG image.");
            util.dismissDialog();
            return;
        }

        // Create a RequestBody for the file with the correct MIME type
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/png"), file);

        // Create MultipartBody.Part using the file request-body, file name, and part name
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        // Create an instance of ApiService
        ApiInterface service = RetrofitClient.getRetrofit().create(ApiInterface.class);

        // Make the network call
        Call<ResponseImage> call = service.uploadImage(body);

        call.enqueue(new Callback<ResponseImage>() {
            @Override
            public void onResponse(Call<ResponseImage> call, Response<ResponseImage> response) {
                util.dismissDialog();

                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        util.toast(true, "Image Uploaded Successfully!!");
                        if (response.body().getImageData().toString() != null) {
                            cache.saveId(response.body().getId());
                            showDialog(response.body().getImageData().toString());
                        }


                    } else {
                        util.toast(false, "Status Code:" + response.code());
                    }
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("Upload Error", "Response Code: " + response.code() + ", Error: " + errorBody);
                        util.toast(false, "Error: " + errorBody);
                    } catch (Exception e) {
                        Log.e("Upload Error", "Failed to read error body", e);
                        util.toast(false, "Failed to upload image: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseImage> call, Throwable t) {
                util.dismissDialog();
                util.toast(false, "Failed: " + t.getMessage());
                Log.e("Upload Error", "Request failed", t);
            }
        });
    }

    @SuppressLint("MissingInflatedId")
    private void showDialog(String base64Image) {
        // Create a dialog instance
        Dialog dialog = new Dialog(context, R.style.CustomDialogTheme);

        // Inflate the custom layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.custom_dailog, null);

        // Set the custom layout as the dialog content
        dialog.setContentView(dialogView);

        // Find views in the custom layout
        ImageView responseImage = dialogView.findViewById(R.id.responseImage);
        ImageView close = dialogView.findViewById(R.id.close);
        AppCompatButton removeBG = dialogView.findViewById(R.id.removeBG);
        AppCompatButton blackWhite = dialogView.findViewById(R.id.blackWhite);
        displayBase64Image(base64Image, responseImage);

        // Close DialogBox
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        // Transparent Image..
        removeBG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cache.getId() != null) {
                    removeBackground(cache.getId(), responseImage);
                } else {
                    util.toast(false, "Image ID is not found!!");
                }
            }
        });
        // BlackWhiteImage
        blackWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cache.getId() != null) {
                    blackWhiteImageFunction(cache.getId(), responseImage);
                } else {
                    util.toast(false, "Image ID is not found!!");
                }
            }
        });

        // Prevent the dialog from being canceled by clicking outside
        dialog.setCancelable(false);

        // Optionally, prevent the dialog from being canceled by pressing the back button
        dialog.setCanceledOnTouchOutside(false);
        // Show the dialog
        dialog.show();
    }

    private void removeBackground(Integer id, ImageView responseImage) {
        util.showDialog("Please wait...");
        ApiInterface api = RetrofitClient.getRetrofit().create(ApiInterface.class);
        Call<RemoveImagebackground> call = api.removeBackground(id);
        call.enqueue(new Callback<RemoveImagebackground>() {
            @Override
            public void onResponse(Call<RemoveImagebackground> call, Response<RemoveImagebackground> response) {
                util.dismissDialog();
                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        if (response.body().getImageData().toString() != null) {
                            util.toast(true, "Background Remove Successful!!");
                            displayBase64Image(response.body().getImageData().toString(), responseImage);
                        }
                    } else {
                        util.toast(false, "Status Code: " + response.code());
                    }
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("Upload Error", "Response Code: " + response.code() + ", Error: " + errorBody);
                        util.toast(false, "Error: " + errorBody);
                    } catch (Exception e) {
                        Log.e("Upload Error", "Failed to read error body", e);
                        util.toast(false, "Failed to upload image: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<RemoveImagebackground> call, Throwable t) {
                util.dismissDialog();
                util.toast(false, "Failed: " + t.getMessage());
            }
        });
    }

    private void blackWhiteImageFunction(Integer id, ImageView responseImage) {
        util.showDialog("Please wait..");
        ApiInterface api = RetrofitClient.getRetrofit().create(ApiInterface.class);
        Call<ResponseBlackWhite> call = api.blackWhiteImage(id);
        call.enqueue(new Callback<ResponseBlackWhite>() {
            @Override
            public void onResponse(Call<ResponseBlackWhite> call, Response<ResponseBlackWhite> response) {
                util.dismissDialog();
                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        if (response.body().getBlackWhiteImage().toString().trim() != null) {
                            util.toast(true, "Black-White Image!!");
                            displayBase64Image(response.body().getBlackWhiteImage().toString(), responseImage);
                        }
                    } else {
                        util.toast(false, "Status Code: " + response.code());
                    }
                } else {

                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("Upload Error", "Response Code: " + response.code() + ", Error: " + errorBody);
                        util.toast(false, "Error: " + errorBody);
                    } catch (Exception e) {
                        Log.e("Upload Error", "Failed to read error body", e);
                        util.toast(false, "Failed to upload image: " + e.getMessage());
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBlackWhite> call, Throwable t) {
                util.dismissDialog();
                util.toast(false, "Failed: " + t.getMessage());
            }
        });
    }

    // Method to get MIME type from file path
    private String getMimeType(String filePath) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(filePath);
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }

    public void displayBase64Image(String base64Image, ImageView imageView) {
        // Decode the Base64 string to a byte array
        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);

        // Convert the byte array into a bitmap
        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        // Set the bitmap to the ImageView
        imageView.setImageBitmap(decodedBitmap);
    }

}
