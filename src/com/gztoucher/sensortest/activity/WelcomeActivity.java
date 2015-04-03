package com.gztoucher.sensortest.activity;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import com.gztoucher.sensortest.R;

/**
 * @author ¿Ó”ÒΩ≠[QQ:1023694760]
 * @version 2015/4/3
 *          Created by IntelliJ IDEA 14.1
 */
public class WelcomeActivity extends Activity implements Runnable {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        new Handler().postDelayed(this, 3000L);
    }

    @Override
    public void run() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
