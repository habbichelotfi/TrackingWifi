package com.example.trackingwifi;

import android.content.Context;
import android.content.Intent;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import static android.view.MotionEvent.INVALID_POINTER_ID;


public class RadarView extends View {

    private final String LOG = "RadarView";
    private final int POINT_ARRAY_SIZE = 35;
private boolean showPoint=false;
    private int fps = 200;
    private Drawable mImage;

    private boolean showCircles = true;
private boolean showMetres=true;
private int rayon=15;
private int dis=0;
private String nameNetwork="";
private String encry="";
private String Adresse="";
private String name="";
private int channel=0;
private String mac="";
private int dbm=0;
private List<WifiInfo_> list=new ArrayList<>();
    private List<WifiInfo_> list1=new ArrayList<>();
    private List<Point> positions=new ArrayList<>();
private int longitude=0;
private boolean showNetwork=false;
    private static final int INVALID_POINTER_ID = -1;

    private float mPosX;
    private float mPosY;

    private float mLastTouchX;
    private float mLastTouchY;
    private int mActivePointerId = INVALID_POINTER_ID;

    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1.f;
    float alpha = 0;
    private Context context;
    Point latestPoint[] = new Point[POINT_ARRAY_SIZE];
    Paint latestPaint[] = new Paint[POINT_ARRAY_SIZE];

    public RadarView(Context context) {
        super(context);

    }

    public RadarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RadarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mImage = getResources().getDrawable(R.drawable.ic_wifi_black_48dp);

        this.context=context;
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());


        Paint localPaint = new Paint();

        localPaint.setColor(Color.BLUE);
        localPaint.setAntiAlias(true);

        localPaint.setStyle(Paint.Style.STROKE);
        localPaint.setStrokeWidth(2.0F);
        localPaint.setAlpha(0);

        int alpha_step = 255 / POINT_ARRAY_SIZE;
        for (int i=0; i < latestPaint.length; i++) {
            latestPaint[i] = new Paint(localPaint);
            latestPaint[i].setAlpha(255 - (i* alpha_step));
        }
    }


    android.os.Handler mHandler = new android.os.Handler();
    Runnable mTick = new Runnable() {
        @Override
        public void run() {
            invalidate();
            mHandler.postDelayed(this, 1000 / fps);
        }
    };


    public void startAnimation() {
        mHandler.removeCallbacks(mTick);
        mHandler.post(mTick);
    }

    public void stopAnimation() {
        mHandler.removeCallbacks(mTick);
    }

    public void setFrameRate(int fps) { this.fps = fps; }
    public int getFrameRate() { return this.fps; };
public void setShowPoints(boolean showPoint){this.showPoint=showPoint;}
    public void setShowCircles(boolean showCircles) { this.showCircles = showCircles; }
    public void setShowMetres(boolean showMetres) { this.showMetres = showMetres; }
public void setRayon(int rayon){this.rayon=rayon;}
public void setShowNetwork(boolean showNetwork,List<WifiInfo_> list){
    this.showNetwork=showNetwork;
this.list=list;
}




    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        mScaleDetector.onTouchEvent(ev);

        final int action = ev.getAction();
        mLastTouchX = ev.getX();
        mLastTouchY = ev.getY();


        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                final float x = ev.getX();
                final float y = ev.getY();

                mLastTouchX = x;
                mLastTouchY = y;
                mActivePointerId = ev.getPointerId(0);
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                final int pointerIndex = ev.findPointerIndex(mActivePointerId);
                final float x = ev.getX(pointerIndex);
                final float y = ev.getY(pointerIndex);

                // Only move if the ScaleGestureDetector isn't processing a gesture.
                if (!mScaleDetector.isInProgress()) {
                    final float dx = x - mLastTouchX;
                    final float dy = y - mLastTouchY;

                    mPosX += dx;
                    mPosY += dy;

                    invalidate();
                }

                mLastTouchX = x;
                mLastTouchY = y;

                break;
            }

            case MotionEvent.ACTION_UP: {
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_CANCEL: {
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_POINTER_UP: {
                final int pointerIndex = (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK)
                        >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                final int pointerId = ev.getPointerId(pointerIndex);
                if (pointerId == mActivePointerId) {
                    // This was our active pointer going up. Choose a new
                    // active pointer and adjust accordingly.
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mLastTouchX = ev.getX(newPointerIndex);
                    mLastTouchY = ev.getY(newPointerIndex);
                    mActivePointerId = ev.getPointerId(newPointerIndex);
                }
                break;
            }
        }

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

int r_1=100;int r_2=200;int r_3=300;
        int width = getWidth();
        int height = getHeight();

        int r = Math.min(width, height);


        //canvas.drawRect(0, 0, getWidth(), getHeight(), localPaint);

        int i = (r) /2;
        int j = r/2 - 1;
        int g=i;
        Paint localPaint = latestPaint[0];
        if(showPoint){
            Paint paint=new Paint();
            paint.setColor(Color.GREEN);
            canvas.drawCircle(i,j,10,paint);
        }
        if (showCircles) {

            //canvas.drawCircle(i, g, (float) (j*1.25), localPaint);

            Paint localPaint1 = new Paint();
            localPaint1.setColor(Color.rgb(0,191,255));

            localPaint1.setStrokeWidth(70.0f);
            localPaint1.setAntiAlias(true);

            localPaint1.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(i, g, j-35, localPaint1);
            canvas.drawCircle(i, g, (j * 3 / 4)-35, localPaint1);
            canvas.drawCircle(i, g, (j >> 1)-35, localPaint1);
            canvas.drawCircle(i, g, (j >> 2)-35 , localPaint1);
            if(showMetres){
                Paint paint=new Paint();
                paint.setColor(Color.GREEN);
                paint.setTextSize(40);
                canvas.drawText(rayon+"m",i,g+10,paint);
                canvas.drawText(rayon*2+"m",i,g-r_1,paint);
                canvas.drawText(rayon*3+"m",i,g-r_2,paint);
                canvas.drawText(rayon*4+"m",i,g-r_3,paint);

            }
        }// GREEN
        if(showNetwork){
            Paint paint=new Paint();
            paint.setColor(Color.GREEN);
            paint.setTextSize(30);
            for (int k=0;k<list.size();k++){
                dis=list.get(k).getDistance();
                nameNetwork=list.get(k).getName();
encry=list.get(k).getEncry();
channel=list.get(k).getChannel();
Adresse=list.get(k).getAddresse();
mac=list.get(k).getBssid();
dbm=list.get(k).getDmb();

                if (rayon==15){
                    if(dis<15 && dis>0){
                        int Min=i-30;
                        int Max=i-50;
                        int nombreAleatoirex = Min + (int)(Math.random() * ((Max - Min) + 1));
                         Min=g-10;
                         Max=g+10;
                        int nombreAleatoirey = Min + (int)(Math.random() * ((Max - Min) + 1));
                        //Toast.makeText(context,"jajaa1 ",Toast.LENGTH_SHORT).show();

                        mImage.setBounds(0,0,60,60);
canvas.translate(i-240,g-10);
positions.add(new Point(i-240,g-10));
                        mImage.draw(canvas);
                        canvas.translate(-(i-240),-(g-10));
                        //canvas.drawCircle(nombreAleatoirex,nombreAleatoirey,17,paint);
                        canvas.drawText(nameNetwork,i-240,g-30,paint);

                    }
                    if(dis<30 && dis>15){
                        int Min=i-15;
                        int Max=i-65;
                        int nombreAleatoirex = Min + (int)(Math.random() * ((Max - Min) + 1));
                        Min=g-r_2-70;
                        Max=g-r_2+70;
                        int nombreAleatoirey = Min + (int)(Math.random() * ((Max - Min) + 1));
                        //Toast.makeText(context,"jajaa2 ",Toast.LENGTH_LONG).show();

                        mImage.setBounds(0,0,60,60);
                        canvas.translate(i-40,r_1-10);
                        positions.add(new Point(i-40,r_1-10));

                        mImage.draw(canvas);
                        canvas.translate(-(i-40),-(r_1-10));

                        // canvas.drawCircle(nombreAleatoirex,nombreAleatoirey,17,paint);
                        canvas.drawText(nameNetwork,i-240,r_1-30,paint);

                    }
                    if(dis<45 && dis>30){
                        mImage.setBounds(0,0,60,60);
                        canvas.translate(i-40,r_2-10);
                        positions.add(new Point(i-40,r_2-10));

                        mImage.draw(canvas);
                        canvas.translate(-(i-40),-(r_2-10));

                        //canvas.drawCircle(i-40,r_2,17,paint);
                        canvas.drawText(nameNetwork,i-240,r_2-30,paint);

                        }

                    if(dis<60 && dis>45){
                        mImage.setBounds(0,0,60,60);
                        canvas.translate(i-250,r_3);
                        positions.add(new Point(i-240,r_3));

                        mImage.draw(canvas);
                        canvas.translate(-(i-250),-r_3);
                        canvas.drawText(nameNetwork,i-240,r_3-30,paint);

                    }
                }
               if (rayon==100){
                   if(dis<100 && dis>50){
                       mImage.setBounds(0,0,60,60);
                       mImage.draw(canvas);
                       canvas.drawText(nameNetwork,i-240,r_3-30,paint);

                       //canvas.drawCircle(i-40,r_3,17,paint);
                   }
               }
               if (rayon==150){

               }



            }

        }



        alpha -= 0.5;
        if (alpha < -360) alpha = 0;
        double angle = Math.toRadians(alpha);
        int offsetX =  (int) (i + (float)(i * Math.cos(angle)));
        int offsetY = (int) (i - (float)(i * Math.sin(angle)));

        latestPoint[0]= new Point(offsetX, offsetY);

        for (int x=POINT_ARRAY_SIZE-1; x > 0; x--) {
            latestPoint[x] = latestPoint[x-1];
        }



        int lines = 0;
        for (int x = 0; x < POINT_ARRAY_SIZE; x++) {
            Point point = latestPoint[x];
            if (point != null) {
                canvas.drawLine(i, i, point.x, point.y, latestPaint[x]);
            }
        }


        lines = 0;
        for (Point p : latestPoint) if (p != null) lines++;

        boolean debug = false;
        if (debug) {
            StringBuilder sb = new StringBuilder(" >> ");
            for (Point p : latestPoint) {
                if (p != null) sb.append(" (" + p.x + "x" + p.y + ")");
            }

            Log.d(LOG, sb.toString());
            //  " - R:" + r + ", i=" + i +
            //  " - Size: " + width + "x" + height +
            //  " - Angle: " + angle +
            //  " - Offset: " + offsetX + "," + offsetY);
        }


    }
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();

            // Don't let the object get too small or too large.
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));

            invalidate();
            return true;
        }
    }

}
