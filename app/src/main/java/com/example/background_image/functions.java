package com.example.background_image;

import android.content.Context;
import android.content.Intent;

public class functions {
    Context context;

    public functions(Context context) {
        this.context = context;
    }

    public void intent(Class<?> targetClass) {
        context.startActivity(new Intent(context, targetClass));
    }
}
