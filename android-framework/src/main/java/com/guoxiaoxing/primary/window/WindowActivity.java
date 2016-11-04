package com.guoxiaoxing.primary.window;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.guoxiaoxing.primary.R;

public class WindowActivity extends AppCompatActivity {

    private WindowManager mWindowManager;

    private Button mButton;

    private WindowManager.LayoutParams mLayoutParams;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_window);
        setupView();
        addButton();
    }

    private void setupView() {
        mWindowManager = getWindowManager();
    }

    private void addButton() {
        mButton = new Button(this);
        mButton.setText("悬浮窗");
        mButton.setTextColor(Color.BLACK);

        mLayoutParams = new WindowManager.LayoutParams();

        mLayoutParams.width = 300;
        mLayoutParams.height = 150;

        mWindowManager.addView(mButton, mLayoutParams);

        mButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int rawX = (int) event.getRawX();
                int rawY = (int) event.getRawY();

                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        mLayoutParams.x = rawX;
                        mLayoutParams.y = rawY;
                        mWindowManager.updateViewLayout(mButton, mLayoutParams);
                        break;
                }
                return false;
            }
        });

    }
}
