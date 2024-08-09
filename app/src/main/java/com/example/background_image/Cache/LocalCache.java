package com.example.background_image.Cache;

import android.content.Context;
import android.content.SharedPreferences;

public class LocalCache {
    private static final String PREF_NAME = "LocalCache";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public LocalCache(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // Save ID to SharedPreferences
    public void saveId(int id) {
        editor.putInt("image_id", id);
        editor.apply(); // or editor.commit();
    }

    public Integer getId() {
        return sharedPreferences.getInt("image_id", -1); // or provide a default value
    }
}
