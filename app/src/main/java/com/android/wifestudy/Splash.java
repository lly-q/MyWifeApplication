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

import static android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;

public class Splash extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN| SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            setContentView(R.layout.activity_splash);
            //设置标题
            TextView textview1 = findViewById(R.id.textView1);
            //设置按钮
            final Button button = findViewById(R.id.button);
            Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/dongman.ttf");
            textview1.setTypeface(typeface);
            button.setTypeface(typeface);
            //设置动画
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
        //设置点击事件
        public void ClickToTurn (View view ){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }


