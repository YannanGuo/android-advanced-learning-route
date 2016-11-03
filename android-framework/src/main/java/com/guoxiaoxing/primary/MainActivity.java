package com.guoxiaoxing.primary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.guoxiaoxing.primary.activity.lifecycle.LifecycleActivity;
import com.guoxiaoxing.primary.window.WindowActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_activity_lifecycle).setOnClickListener(this);
        findViewById(R.id.btn_window).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_activity_lifecycle:
                startActivity(new Intent(MainActivity.this, LifecycleActivity.class));
                break;
            case R.id.btn_window:
                startActivity(new Intent(MainActivity.this, WindowActivity.class));
                break;
            default:
                break;
        }
    }
}
