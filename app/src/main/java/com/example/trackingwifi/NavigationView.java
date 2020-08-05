package com.example.trackingwifi;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;

import java.util.List;

public class NavigationView extends View {
    int fps=200;
    private boolean showNetwork;
    private  ImageView thisisImg;
    float longitude=0;
    private float currentDegree = 0f;

    public int getFps() {
        return fps;
    }

    public boolean isShowNetwork() {
        return showNetwork;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setFps(int fps) {
        this.fps = fps;
    }

    public void setShowNetwork(boolean showNetwork) {
        this.showNetwork = showNetwork;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public NavigationView(Context context) {
        super(context);

    }

    public NavigationView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NavigationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

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
    public void setFrameRate(int fps) { this.fps = fps; }
    public int getFrameRate() { return this.fps; };
    public void setShowNetwork(boolean showNetwork, List<Pos> list){
        this.showNetwork=showNetwork;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Drawable drawable=getResources().getDrawable(R.drawable.navigation,null);
        Drawable drawable1=getResources().getDrawable(R.drawable.ic_wifi_black_48dp,null);

ImageView imageView=new ImageView(getContext());
imageView.setImageDrawable(drawable);
        Animation aniRotate = AnimationUtils.loadAnimation(getContext(),R.anim.rotate);
        imageView.startAnimation(aniRotate);
//        Drawable drawable2 = thisisImg.getDrawable();



        drawable1.setBounds(0,0,60,60);
        drawable.setBounds(0,0,300,300);
        int width = getWidth();
        int height = getHeight();

        int r = Math.min(width, height);
        int i = (r) /2;
        int j = r/2 - 1;


        Paint localPaint1 = new Paint();
        localPaint1.setColor(Color.BLACK);

        localPaint1.setStrokeWidth(7.0f);
        localPaint1.setAntiAlias(true);

        localPaint1.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(i, j, j-55, localPaint1);


        canvas.translate(160,160);

       imageView.draw(canvas);
       // canvas.translate(-(i-65),-(j-65));
        RotateAnimation ra = new RotateAnimation(
                currentDegree,
                -longitude,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);

        // how long the animation will take place
        ra.setDuration(210);

        // set the animation after the end of the reservation status
        ra.setFillAfter(true);

        // Start the animation
        imageView.startAnimation(ra);
        currentDegree = -longitude;
//        canvas.restore();
//Toast.makeText(getContext(),""+longitude,Toast.LENGTH_SHORT).show();

    }
}
