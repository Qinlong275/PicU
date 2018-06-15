package com.qinlong275.android.picu.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class DrawView extends View{
    //初始化圆的位置

    private int ringWidth=2;
    private int cirRadius=120;
    public float currentX=150;
    public float currentY=200;

    //创建画笔
    private Paint paint=new Paint();

    public DrawView(Context context) {
        this(context,null);
    }

    public DrawView(Context context, @Nullable AttributeSet attrs) {
        this(context,null,0);
    }

    public DrawView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //绘制内圆
        paint.setStrokeWidth(15);
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(currentX, currentY, cirRadius, paint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //getX获得点击x位置，getY获得点击Y的位置
        currentX= event.getX();
        currentY=event.getY();

        //得到点击的位置数据

        //重绘自身
        invalidate();
        //返回true自身消费
        return true;
    }
}
