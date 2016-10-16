package com.forisz.joystick.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;


public class Joystick extends View {

    public static final int JOYSTICK_MODE_NORMAL = 1;
    public static final int JOYSTICK_MODE_STATIC = 2;

    private static final int DEFAULT_JOYSTICK_MODE = JOYSTICK_MODE_NORMAL;
    private static final int DEFAULT_JOYSTICK_BACKGROUND_COLOR = Color.RED;


    public Joystick(Context context) {
        super(context);
        init(context, null);
    }

    public Joystick(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public Joystick(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Joystick(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    /**
     * Initialise Joystick
     * @param context
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs) {

    }
}
