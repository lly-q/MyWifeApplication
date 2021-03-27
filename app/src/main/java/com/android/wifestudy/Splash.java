package com.android.wifestudy;
import android.animation.Animator;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Splash extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            setContentView(R.layout.activity_splash);
            TextView textview1 = findViewById(R.id.textView1);
            final Button button = findViewById(R.id.button);
            Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/dongman.ttf");
            textview1.setTypeface(typeface);
            button.setTypeface(typeface);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int cx = button.getWidth() / 2;
                    int cy = button.getHeight() / 2;
                    float radius = button.getWidth();
                    Animator anim = ViewAnimationUtils.createCircularReveal(button, cx, cy, radius, 0);
                    anim.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            Splash.super.onEnterAnimationComplete();

                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {
                        }
                    });
                    anim.start();
                    ClickToTurn(button);
                }
            });
        }
        public void ClickToTurn (View view ){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }


