package com.example.visualizertest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

//import static com.example.s4.MainActivity.loadBitmapFromView;
//import  com.example.s4.MusicWave;

public class MusicWave extends View {
    private int size = 4;
    private byte[] mBytes;
    private float[] mPoints;
    private Rect mRect = new Rect();
    private Paint mForePaint = new Paint();



    public MusicWave(Context context) {
        super(context);
        init();
    }

    public MusicWave(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MusicWave(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mBytes = null;
        mForePaint.setStrokeWidth(2f);
        mForePaint.setAntiAlias(true);
        mForePaint.setColor(Color.rgb(0, 128, 255));
    }

    public void updateVisualizer(byte[] bytes) {
        mBytes = bytes;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBytes == null) {
            return;
        }
        if (mPoints == null || mPoints.length < mBytes.length * size) {
            mPoints = new float[mBytes.length * size];
        }
        mRect.set(0, 0, getWidth(), getHeight());

        for (int i = 0; i < mBytes.length - 1; i++) {
            mPoints[i * size] = mRect.width() * i / (mBytes.length - 1);
            mPoints[i * size + 1] = mRect.height() / 2
                    + ((byte) (mBytes[i] + 128)) * (mRect.height() / 2) / 128;
            mPoints[i * size + 2] = mRect.width() * (i + 1) / (mBytes.length - 1);
            mPoints[i * size + 3] = mRect.height() / 2
                    - ((byte) (mBytes[i] + 128)) * (mRect.height() / 2) / 128;
        }
        canvas.drawLines(mPoints, mForePaint);
    }
}