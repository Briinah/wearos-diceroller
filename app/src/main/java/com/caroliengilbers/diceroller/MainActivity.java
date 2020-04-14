package com.caroliengilbers.diceroller;

import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends WearableActivity {

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Enables Always-on
        setAmbientEnabled();

        mediaPlayer = MediaPlayer.create(this, R.raw.dice_roll);

        SnapScrollView snapView = findViewById(R.id.scrollView);
        snapView.getItems();

        String expr = "(?<=d)([0-9]+)(?=View)";
        final Pattern pattern = Pattern.compile(expr);

        for(int i = 0; i < snapView.items.size(); i++)
        {
            DynamicSquareLayout square = snapView.items.get(i);

            square.getChildAt(0).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mediaPlayer.start();
                    String name = v.getResources().getResourceName(v.getId());
                    Matcher matcher = pattern.matcher(name);
                    if(matcher.find())
                    {
                        int maxNumber = Integer.parseInt(matcher.group());
                        roll((TextView)v, maxNumber);
                    }
                }
            });
        }
    }


    private void roll(TextView view, int max){
        Random rand = new Random();
        int randomValue = rand.nextInt(max) + 1;

        final String value = String.valueOf(randomValue);
        final TextView viewToAnimate = view;

        Animation rotate = new RotateAnimation(0, 360, view.getPivotX(), view.getPivotY());
        rotate.setDuration(500);
        rotate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                viewToAnimate.setText(value);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        view.startAnimation(rotate);
    }

    @Override
    protected void onStop(){
        mediaPlayer.release();
        mediaPlayer = null;
        super.onStop();
    }
}
