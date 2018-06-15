package com.qinlong275.android.picu.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.qinlong275.android.picu.R;

public class ColorBarView extends LinearLayout implements View.OnClickListener{

    private Button mColor1;
    private Button mColor2;
    private Button mColor3;
    private Button mColor4;
    private Button mColor5;
    private Button mColor6;
    private Button mColor7;
    private Button back;
    private ColorChangeListener mListener;

    public ColorBarView(Context context,ColorChangeListener listener) {
        super(context);
        // 加载布局
        mListener=listener;
        LayoutInflater.from(context).inflate(R.layout.chose_color_bar,this);
        LayoutParams lp = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        this.setLayoutParams(lp);

        mColor1=(Button)findViewById(R.id.color_1);
        mColor2=(Button)findViewById(R.id.color_2);
        mColor3=(Button)findViewById(R.id.color_3);
        mColor4=(Button)findViewById(R.id.color_4);
        mColor5=(Button)findViewById(R.id.color_5);
        mColor6=(Button)findViewById(R.id.color_6);
        mColor7=(Button)findViewById(R.id.color_7);
        back=(Button)findViewById(R.id.back);

        mColor1.setOnClickListener(this);
        mColor2.setOnClickListener(this);
        mColor3.setOnClickListener(this);
        mColor4.setOnClickListener(this);
        mColor5.setOnClickListener(this);
        mColor6.setOnClickListener(this);
        mColor7.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.color_1:
                mListener.onStateChange(1);
                break;
            case R.id.color_2:
                mListener.onStateChange(2);
                break;
            case R.id.color_3:
                mListener.onStateChange(3);
                break;
            case R.id.color_4:
                mListener.onStateChange(4);
                break;
            case R.id.color_5:
                mListener.onStateChange(5);
                break;
            case R.id.color_6:
                mListener.onStateChange(6);
                break;
            case R.id.color_7:
                mListener.onStateChange(7);
                break;
            case R.id.back:
                mListener.onStateChange(8);
                break;

        }
    }

    public interface ColorChangeListener{
        void onStateChange(int which);
    }
}
