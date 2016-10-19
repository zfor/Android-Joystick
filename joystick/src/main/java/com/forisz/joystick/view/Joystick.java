package com.forisz.joystick.view;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.forisz.joystick.R;


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
    private static final int DEFAULT_APPEARANCE = APPEARANCE_NORMAL;
    private static final int DEFAULT_BEHAVIOR = BEHAVIOR_NORMAL;
    private static final int DEFAULT_OUTER_CIRCLE_WIDTH = 5;

    /**
     * Default radius of the inner circle in display-independent pixels
     * 0 means auto
     */
    private static final int DEFAULT_INNER_CIRCLE_RADIUS = 0;

    /**
     * Default radius of the outer circle in display-independent pixels
     * 0 means auto
     */
    private static final int DEFAULT_OUTER_CIRCLE_RADIUS = 0;

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

    private int appearance;
    private int behavior;
    private int innerCircleRadius;
    private int outerCircleRadius;
    private int lineWidth;
    private int innerCircleBorderWidth;
    private int outerCircleBorderWidth;

    /**
     * The center of the view on the X axis (also the center of the outer circle)
     */
    private int centerX;

    /**
     * The center of the view on the Y axis (also the center of the outer circle)
     */
    private int centerY;

    /**
     * The center of the inner circle on the X axis
     */
    private int joyX;

    /**
     * The center of the inner circle on the Y axis
     */
    private int joyY;

    private ValueAnimator animation;


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
        TypedArray attributes = null;
        int innerCircleColor;
        int outerCircleColor;
        int lineColor;
        try {
            attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.Joystick, 0, 0);

            appearance = attributes.getInt(R.styleable.Joystick_appearance, DEFAULT_APPEARANCE);
            behavior = attributes.getInt(R.styleable.Joystick_behavior, DEFAULT_BEHAVIOR);
            innerCircleRadius = attributes.getDimensionPixelSize(R.styleable.Joystick_inner_circle_radius, DEFAULT_INNER_CIRCLE_RADIUS);
            outerCircleRadius = attributes.getDimensionPixelSize(R.styleable.Joystick_outer_circle_radius, DEFAULT_OUTER_CIRCLE_RADIUS);
            innerCircleColor = attributes.getColor(R.styleable.Joystick_inner_circle_color, DEFAULT_INNER_CIRCLE_COLOR);
            outerCircleColor = attributes.getColor(R.styleable.Joystick_outer_circle_color, DEFAULT_OUTER_CIRCLE_COLOR);

        } finally {
            if (attributes != null) {
                attributes.recycle();
            }
        }

        // Set up Paint instances for onDraw

        outerCirclePaint = new Paint();
        outerCirclePaint.setStyle(Paint.Style.FILL);
        outerCirclePaint.setColor(outerCircleColor);
        outerCirclePaint.setAntiAlias(true);

        innerCirclePaint = new Paint();
        innerCirclePaint.setStyle(Paint.Style.FILL);
        innerCirclePaint.setColor(innerCircleColor);
        innerCirclePaint.setAntiAlias(true);

        linePaint = new Paint();
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(Color.BLACK);
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(5f);

    }

    @Override
    public boolean isInEditMode() {
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw the outer circle
        canvas.drawCircle(centerX, centerY, outerCircleRadius, outerCirclePaint);
        // Draw the line connecting the inner and outer circles
     //   canvas.drawLine(joyX, joyY, centerX, centerY, linePaint);
        // Draw the inner circle
        canvas.drawCircle(joyX, joyY, innerCircleRadius, innerCirclePaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        centerX = w / 2;
        centerY = h / 2;
        joyX = centerX;
        joyY = centerY;

        outerCircleRadius = (w < h ? w : h) / 2;
        innerCircleRadius = (int) (0.4 * outerCircleRadius);

        outerCircleRadius -= innerCircleRadius;


        this.invalidate();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                handleActionDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                handleActionMove(event);
                break;
            case MotionEvent.ACTION_UP:
                handleActionUp(event);
                break;
        }
        return true;
    }

    private void handleActionDown(MotionEvent event) {
        if (listener != null) {
            listener.onTouch();
        }
        handleActionMove(event);
    }

    private void handleActionMove(MotionEvent event) {
        final PointF pointer = new PointF(event.getX(0), event.getY(0));
        final PointF center = new PointF(centerX, centerY);

        // Prevent the inner circle from leaving the outer circle's boundaries.
        PointF joy = null;
        if (getDistance(pointer, center) > outerCircleRadius) {
            joy = getCircleLineIntersection(pointer, center, center, outerCircleRadius);
        } else {
            joy = getCircleLineIntersection(pointer, center, center, getDistance(pointer, center));
        }
        joyX = (int) joy.x;
        joyY = (int) joy.y;

        // Let's translate the x and y position into the [-1, 1] range and call the callback with it;
        if (listener != null) {
            float x = joy.x / outerCircleRadius - 1;
            float y = -(joy.y / outerCircleRadius - 1);
            double deg = Math.toDegrees(Math.atan2(y, x));

            if (deg < 0) deg = Math.abs(deg) + 180;

            //listener.onMove(x, y, (float) deg);
        }
        Log.d(TAG, String.format("X: %d; Y: %d;", joyX, joyY));
        this.invalidate();
    }

    private float getDistance(PointF a, PointF b) {
        return (float)Math.sqrt(Math.pow(b.x - a.x, 2) + Math.pow(b.y - a.y , 2));
    }

    private void handleActionUp(MotionEvent event) {
        if (behavior == BEHAVIOR_NORMAL) {
            joyX = centerX;
            joyY = centerY;
            invalidate();
        } else {
            // STATIC

        }
    }

    /**
     * Calculates the intersecting points of a line and a circle.
     * Used for preventing the outer circle from moving outside of the bounds of the outer circle
     *
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

        float abScalingFactor1 = -pBy2 + (float) Math.sqrt(disc);

        return new PointF(pointA.x - baX * abScalingFactor1, pointA.y - baY * abScalingFactor1);
    }


    public void setOnJoystickEventListener(OnJoysticEventListener listener) {
        this.listener = listener;
    }

    public interface OnJoysticEventListener {
        /**
         * Called when the joystick button is moved by the user
         */
        void onMove(int posX, int posY, int deg);

        /**
         * Called when the user releases the joystick
         */
        void onRelease();

        /**
         * Called when the user touched the joystick.
         * Immediately followed by at least one onMove call!
         */
        void onTouch();
    }

}
