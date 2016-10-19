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

        joy1 = (Joystick) findViewById(R.id.joystick);

        joy1.setOnJoystickEventListener(new Joystick.OnJoysticEventListener() {

            @Override
            public void onMove(int posX, int posY, int deg) {
                Log.d("MainActivity", "onMove");
            }

            @Override
            public void onRelease() {
                Log.d("MainActivity", "onRelease");
            }

            @Override
            public void onTouch() {
                Log.d("MainActivity", "onTouch");
            }
        });


        //Log.i("asd", Integer.toString(Joystick.DRAW_MODE_INNER_CIRCLE_ONLY));
    }
}
