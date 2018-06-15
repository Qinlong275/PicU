package com.qinlong275.android.picu.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qinlong275.android.picu.R;
import com.qinlong275.android.picu.common.PicUtils;
import com.qinlong275.android.picu.ui.widget.DrawView;
import com.qinlong275.android.picu.ui.widget.TuyaView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BlockProduceActivity extends AppCompatActivity {

    @BindView(R.id.produce_close)
    Button mProduceClose;
    @BindView(R.id.produce_text_ok)
    Button mProduceTextOk;
    @BindView(R.id.top_bar)
    RelativeLayout mTopBar;
    @BindView(R.id.tit_show)
    Button mTitShow;
    @BindView(R.id.icon_image)
    ImageView mIconImage;
    @BindView(R.id.pic_card)
    CardView mPicCard;
    @BindView(R.id.text_icon)
    TextView mTextIcon;
    @BindView(R.id.voice_intext)
    EditText mVoiceIntext;
    @BindView(R.id.voice_text)
    LinearLayout mVoiceText;
    @BindView(R.id.draw_image)
    RelativeLayout mDrawImage;
    @BindView(R.id.pic_redo)
    Button mPicRedo;
    @BindView(R.id.drawCirViewCon)
    RelativeLayout mDrawCirViewCon;
    @BindView(R.id.input_text)
    TextView mInputText;
    @BindView(R.id.line)
    TextView mLine;

    private DrawView mDrawView;
    private int choseState;     //选择的解锁方式
    private TuyaView mTuyaView;
    private View dialogView;
    private AlertDialog.Builder dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_produce);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        choseState = intent.getIntExtra(BlockChoseActivity.choseState, 1);

        initView();

    }

    private void initView() {
        switch (choseState) {
            case 1:
                mTitShow.setBackgroundResource(R.drawable.icon_voice);
                mVoiceText.setVisibility(View.VISIBLE);
                mInputText.setVisibility(View.GONE);
                mPicCard.setVisibility(View.INVISIBLE);
                mLine.setVisibility(View.INVISIBLE);
                break;
            case 2:
                //画板在这里
                int screenWidth = 1000;
                int screenHeight = 1200;
                mTuyaView = new TuyaView(this, screenWidth, screenHeight);
                mTuyaView.requestFocus();
                mTuyaView.selectPaintSize(26);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                params.addRule(RelativeLayout.CENTER_IN_PARENT);
                mDrawImage.addView(mTuyaView, params);
                mPicRedo.setVisibility(View.VISIBLE);
                mTitShow.setBackgroundResource(R.drawable.icon_sketch);
                mPicCard.setVisibility(View.VISIBLE);
                break;
            case 3:
                //点击出现圆圈，计算位置
                RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                param.addRule(RelativeLayout.CENTER_IN_PARENT);
                mDrawView = new DrawView(BlockProduceActivity.this, null);
                mDrawCirViewCon.addView(mDrawView, param);
                mDrawCirViewCon.setVisibility(View.VISIBLE);
                mTitShow.setBackgroundResource(R.drawable.icon_click);
                mPicCard.setVisibility(View.VISIBLE);
                break;
            case 4:
                mTitShow.setBackgroundResource(R.drawable.icon_camera);
                mVoiceText.setVisibility(View.VISIBLE);
                mTextIcon.setVisibility(View.INVISIBLE);
                mPicCard.setVisibility(View.INVISIBLE);
                break;
        }
    }

    @OnClick({R.id.produce_close, R.id.produce_text_ok, R.id.tit_show, R.id.input_text, R.id.voice_intext})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.produce_close:
                finish();
                break;
            case R.id.produce_text_ok:
                //分享给朋友,根据类型存储交换相应的信息
                switch (choseState) {
                    case 1:

                        break;
                    case 2:
                        //保存画板图片
                        mTuyaView.saveToSDCard();
                        break;
                    case 3:

                        break;
                    case 4:

                        break;
                }
                Intent intent = new Intent(BlockProduceActivity.this, ShareActivity.class);
                startActivity(intent);
                break;
            case R.id.input_text:
                //防止软键盘顶起的问题，采用弹出dialog
                LayoutInflater inflater = getLayoutInflater();
                dialogView = inflater.inflate(R.layout.input_dialog_view, null);
                dialog = new AlertDialog.Builder(BlockProduceActivity.this);
                dialog.setTitle("给出你的提示");
                dialog.setView(dialogView);
                dialog.setCancelable(false);

                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //这里定义确认响应事件
                        mInputText.setText(((EditText) dialogView.findViewById(R.id.editText)).getText());
                        PicUtils.toggleInput(BlockProduceActivity.this);
                    }
                });

                dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //这里定义否定响应事件
                        PicUtils.toggleInput(BlockProduceActivity.this);
                    }
                });
                dialog.show();
                break;
            case R.id.voice_intext:
                mVoiceIntext.setOnEditorActionListener(new TextView.OnEditorActionListener() {

                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEND
                                || actionId == EditorInfo.IME_ACTION_DONE
                                || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                            PicUtils.toggleInput(BlockProduceActivity.this);
                            //设置相关数据内容，想听到的语音
                        }
                        return false;
                    }
                });
                break;
        }
    }

    @OnClick(R.id.pic_redo)
    public void onViewClicked() {
        mTuyaView.redo();
    }
}
