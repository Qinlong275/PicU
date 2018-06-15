package com.qinlong275.android.picu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qinlong275.android.picu.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class BlockChoseActivity extends AppCompatActivity {

    @BindView(R.id.produce_close)
    Button mProduceClose;
    @BindView(R.id.top_bar)
    RelativeLayout mTopBar;
    @BindView(R.id.text_show)
    TextView mTextShow;
    @BindView(R.id.voice_block)
    CircleImageView mVoiceBlock;
    @BindView(R.id.paint_block)
    CircleImageView mPaintBlock;
    @BindView(R.id.point_click)
    CircleImageView mPointClick;
    @BindView(R.id.takephoto_block)
    CircleImageView mTakephotoBlock;

    public static String choseState="choseState";  //1:语音，2：绘画，3：点击，4：拍照

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.produce_close, R.id.voice_block, R.id.paint_block, R.id.point_click, R.id.takephoto_block})
    public void onViewClicked(View view) {
        Intent intent=new Intent(BlockChoseActivity.this,BlockProduceActivity.class);
        switch (view.getId()) {
            case R.id.produce_close:
                finish();
                break;
            case R.id.voice_block:
                intent.putExtra(choseState,1);
                startActivity(intent);
                break;
            case R.id.paint_block:
                intent.putExtra(choseState,2);
                startActivity(intent);
                break;
            case R.id.point_click:
                intent.putExtra(choseState,3);
                startActivity(intent);
                break;
            case R.id.takephoto_block:
                intent.putExtra(choseState,4);
                startActivity(intent);
                break;
        }
    }
}
