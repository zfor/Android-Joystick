package com.forisz.joystick.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


public class Joystick extends View {
    private static final String TAG = Joystick.class.getSimpleName();

    ///////////////
    // BEHAVIORS //
    ///////////////
    /**
     * Normal joystick behavior. The inner circle returns to the center when the user releases the view
     */
    public static final int BEHAVIOR_NORMAL = 1;

    /**
     * Static joystick behavior. The inner circle stays in position when the user releases the view
     */
    public static final int BEHAVIOR_STATIC = 2;

    /////////////////
    // APPEARANCES //
    /////////////////
    /**
     * Draw a regular joystick with outer and inner circles and a line connecting them
     */
    public static final int APPEARANCE_NORMAL = 1;

    /**
     * Draw a joystic without the outer circle
     */
    public static final int APPEARANCE_NO_BASE = 2;

    /**
     * Draw a joystick with only the inner circle
     */
    public static final int APPEARANCE_INNER_CIRCLE_ONLY = 3;

    
    private static final int DEFAULT_MODE = BEHAVIOR_NORMAL;
    private static final int DEFAULT_APPEARANCE = APPEARANCE_NORMAL;
    private static final int DEFAULT_OUTER_CIRCLE_WIDTH = 5;
    private static final int DEFAULT_INNER_CIRCLE_RADIUS = 50;
    private static final int DEFAULT_INNER_CIRCLE_COLOR = Color.RED;
    private static final int DEFAULT_OUTER_CIRCLE_COLOR = Color.BLACK;
    private static final int DEFAULT_LINE_COLOR = Color.BLUE;
    private static final int DEFAULT_LINE_WIDTH = 2;
    private static final Paint.Style DEFAULT_INNER_CIRCLE_STYLE = Paint.Style.FILL;
    private static final Paint.Style DEFAULT_OUTER_CIRCLE_STYLE = Paint.Style.FILL;

    /**
     * Listener for handling joystick events
     */
    private OnJoysticEventListener listener;

    /**
     * Paint instance for drawing the inner circle of the joystick
     */
    private Paint innerCirclePaint;

    /**
     * Paint instance for drawing the outer circle of the joystick
     */
    private Paint outerCirclePaint;

    /**
     * Paint instance for drawing the line that connects the inner and outer circles of the joystick
     */
    private Paint linePaint;

    private int drawMode;
    private int behavior;
    private int innerCircleRadius;
    private int outerCircleRadius;
    private int lineWidth;

    private int innerCircleBorderWidth;
    private int outerCircleBorderWidth;



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
     *
     * @param context
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs) {

    }

    @Override
    public boolean isInEditMode() {
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    /**
     * Calculates the intersecting points of a line and a circle.
     * Used for preventing the outer circle from moving outside of the bounds of the outer circle
     * @param pointA First point of the line
     * @param pointB Second point of the line
     * @param center The center of the circle
     * @param radius The radius of the circle
     * @return The intersection of the circle and the line
     */
    private PointF getCircleLineIntersection(PointF pointA, PointF pointB, PointF center, float radius) {
        // Calculate pointB - pointA and center - pointA offsets on the X and Y axis
        float baX = pointB.x - pointA.x;
        float baY = pointB.y - pointA.y;
        float caX = center.x - pointA.x;
        float caY = center.y - pointA.y;

        float a = (baX * baX) + (baY * baY);
        float bBy2 = (baX * caX) + (baY * caY);
        float c = ((caX * caX) + (caY * caY)) - (radius * radius);

        float pBy2 = bBy2 / a;
        float q = c / a;

        float disc = (pBy2 * pBy2) - q;

        float abScalingFactor1 = -pBy2 + (float)Math.sqrt(disc);

        return new PointF(pointA.x - baX * abScalingFactor1, pointA.y - baY * abScalingFactor1);
    }


    public void setOnJoystickEventListener(OnJoysticEventListener listener) {
        this.listener = listener;
    }
    interface OnJoysticEventListener {
        void onMove();
        void onRelease();
        void onTouch();
    }

}
