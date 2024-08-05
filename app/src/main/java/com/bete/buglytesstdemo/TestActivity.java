package com.bete.buglytesstdemo;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.Toast;

import com.tencent.rfix.lib.dev.AbsRFixDevActivity;

public class TestActivity extends AbsRFixDevActivity {
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);


        Toast.makeText(this, "onCreate", Toast.LENGTH_LONG).show();
//        SystemClock.sleep(50000000);
//        startActivities( new Intent(this,MainActivity.class));

    }
}
