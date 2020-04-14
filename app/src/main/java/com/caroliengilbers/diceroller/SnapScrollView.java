package com.caroliengilbers.diceroller;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;

public class SnapScrollView extends ScrollView {

    public ArrayList<DynamicSquareLayout> items = null;
    private GestureDetector gestureDetector;
    private int activeItem = 0;

    private int prevScrollY = 0;


    public SnapScrollView(Context context) {
        super(context);
    }

    public SnapScrollView(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    public SnapScrollView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
    }

    public void getItems()
    {
        items = new ArrayList<>();

        LinearLayout layout = findViewById(R.id.diceLayout);

        for(int i = 0; i < layout.getChildCount(); i++){
            DynamicSquareLayout item = (DynamicSquareLayout)layout.getChildAt(i);
            items.add(item);
        }

        final int itemNumber = items.size();

        setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                // if the user swipes
                if(gestureDetector.onTouchEvent(event)){
                    return true;
                }
                else if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    int scrollY = getScrollY();
                    Log.i("INFO", "scroll: " + scrollY);
                    int featureHeight = v.getMeasuredHeight(); // only works with square items
                    //activeItem = ((scrollY + featureHeight / 2))/featureHeight;
                    //Log.i("INFO", "item: " + activeItem);
                    if(scrollY > prevScrollY + 10)
                    {
                        activeItem ++;
                    }
                    if(scrollY < prevScrollY - 10)
                    {
                        activeItem--;
                    }
                    activeItem = Math.max(0, Math.min(itemNumber - 1, activeItem));
                    int scrollTo = activeItem * featureHeight;
                    smoothScrollTo(0, scrollTo);
                    v.performClick();

                    prevScrollY = scrollY;
                    return true;
                }
                else {
                    return false;
                }
            }
        });

        gestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener());
    }
}
