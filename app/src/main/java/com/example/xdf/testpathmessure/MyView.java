package com.example.xdf.testpathmessure;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by xdf on 2017/7/27.
 */

public class MyView extends View {
    Path path;
    Paint paint;
    private Bitmap bitmap;
    private float[] pos=new float[2];
    private float[] tan=new float[2];

    public MyView(Context context) {
        super(context);
    }

    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        path=new Path();
        paint=new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        paint.setAntiAlias(true);
        paint.setColor(Color.BLUE);
        BitmapFactory.Options op=new BitmapFactory.Options();
        op.inSampleSize=3;
        bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.ic_feiji,op);
        startAnim();
    }

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(getWidth() / 2, getHeight() / 2);
        path.addCircle(0,0,300,Path.Direction.CCW);
        PathMeasure pathMeasure=new PathMeasure(path,true);
        float distance=pathMeasure.getLength()*progress;
        boolean posTan = pathMeasure.getPosTan(distance, pos, tan);
        if(posTan){
            float degrees= (float) (Math.atan2(tan[1],tan[0])*180f/Math.PI);
            Matrix matrix=new Matrix();
            matrix.postRotate(degrees+90,bitmap.getWidth()/2,bitmap.getHeight()/2);
            matrix.postTranslate(pos[0]-bitmap.getWidth()/2,pos[1]-bitmap.getHeight()/2);

            canvas.drawBitmap(bitmap,matrix,paint);
        }
        canvas.drawPath(path,paint);
    }
    float progress;
    public void startAnim(){
        ValueAnimator va=ValueAnimator.ofFloat(0,1);
        va.setInterpolator(new LinearInterpolator());
        va.setRepeatCount(ValueAnimator.INFINITE);
        va.setDuration(2000);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                progress =(float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        va.start();
    }
}
