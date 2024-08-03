package com.example.background_image;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class Animation {
    public static void animation(String textToAnimate, TextView title) {
        ValueAnimator animator = ValueAnimator.ofInt(0, textToAnimate.length());
        animator.setDuration(2000); // duration of the animation
        animator.addUpdateListener(animation -> {
            int animatedValue = (int) animation.getAnimatedValue();
            title.setText(textToAnimate.subSequence(0, animatedValue));
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(@NonNull Animator animation) {

            }

            @Override
            public void onAnimationEnd(@NonNull Animator animation) {

                animation.setStartDelay(1000);
                animator.start();
            }

            @Override
            public void onAnimationCancel(@NonNull Animator animation) {

            }

            @Override
            public void onAnimationRepeat(@NonNull Animator animation) {
            }
        });
        animator.start();
    }
}
