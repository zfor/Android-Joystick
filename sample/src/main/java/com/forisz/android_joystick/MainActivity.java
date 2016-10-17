package com.forisz.android_joystick;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.forisz.joystick.view.Joystick;

public class MainActivity extends AppCompatActivity {

    Joystick joy1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Log.i("asd", Integer.toString(Joystick.DRAW_MODE_INNER_CIRCLE_ONLY));
    }
}
