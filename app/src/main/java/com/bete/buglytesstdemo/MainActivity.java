package com.bete.buglytesstdemo;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.bete.buglytesstdemo.log.LogUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LogUtils.e("MainActivity is init ");


    }

    public void showErrorContent(View view) {
        int result = 20 / 1;
        LogUtils.e("  show excetion result  : " + result);
    }
}