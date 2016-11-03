package com.guoxiaoxing.primary.window;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import com.guoxiaoxing.primary.R;

public class WindowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_window);

        Button button = new Button(WindowActivity.this);
        button.setText("Button");
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.type = WindowManager.LayoutParams.TYPE_APPLICATION;
        getWindowManager().addView(button, params);
    }


}
