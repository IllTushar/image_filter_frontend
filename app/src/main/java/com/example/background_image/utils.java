package com.example.background_image;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class utils {

    Context context;

    public utils(Context context) {
        this.context = context;
    }

    public void toast(boolean status, String message) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.toast_layout, null);
        // Customize the toast layout based on the status
        ImageView toastIcon = layout.findViewById(R.id.toast_icon);
        TextView toastText = layout.findViewById(R.id.toast_text);
        if (!status) {
            toastIcon.setImageResource(R.drawable.wrong); // Set your error icon
            toastText.setTextColor(Color.RED);
        } else {
            toastIcon.setImageResource(R.drawable.check); // Set your success icon
            toastText.setTextColor(Color.GREEN);
        }
        toastText.setText(message);

        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}
