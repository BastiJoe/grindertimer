package com.grindertimer.grindertimer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                Intent send = new Intent(getApplicationContext(),Home.class);
                startActivity(send);
                overridePendingTransition(R.anim.switcher_bottom_up, R.anim.fragment_exit);
                finish();
            }
        }, 1000);
    }
}
