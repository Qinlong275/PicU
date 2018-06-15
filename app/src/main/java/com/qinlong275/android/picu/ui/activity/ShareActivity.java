package com.qinlong275.android.picu.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qinlong275.android.picu.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShareActivity extends AppCompatActivity {

    @BindView(R.id.produce_close)
    Button mProduceClose;
    @BindView(R.id.produce_text_ok)
    Button mProduceTextOk;
    @BindView(R.id.top_bar)
    RelativeLayout mTopBar;
    @BindView(R.id.title_show)
    TextView mTitleShow;
    @BindView(R.id.ic_picture)
    ImageView mIcPicture;
    @BindView(R.id.text_input)
    EditText mTextInput;
    @BindView(R.id.card_show)
    CardView mCardShow;
    @BindView(R.id.show_index)
    RelativeLayout mShowIndex;
    @BindView(R.id.check_box)
    CheckBox mCheckBox;
    @BindView(R.id.show_secret_text)
    Button mShowSecretText;
    @BindView(R.id.play_secret_voice)
    Button mPlaySecretVoice;
    @BindView(R.id.text_info)
    TextView mTextInfo;
    @BindView(R.id.text_show)
    TextView mTextShow;

    private boolean justShowOnce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        ButterKnife.bind(this);

        //获取数据判断设置的彩蛋样式
        //设置TextInfo的文字
    }

    @OnClick({R.id.produce_close, R.id.produce_text_ok, R.id.title_show, R.id.show_secret_text, R.id.play_secret_voice, R.id.text_info, R.id.check_box})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.produce_close:
                //取消分享，回到添加解锁方式页面，可能要将刚才制作好的进行回退
                finish();
                break;
            case R.id.produce_text_ok:
                //调用分享接口，分享给QQ好友
                if (mCheckBox.isChecked()) {
                    //选择了阅后即焚
                } else {

                }

                break;
            case R.id.title_show:
                break;
            case R.id.show_secret_text:
                //显示文字彩蛋设置的文字
                break;
            case R.id.play_secret_voice:
                //播放之前语音彩蛋设置的语音
                break;
            case R.id.text_info:
                break;
        }
    }

    @OnClick(R.id.text_show)
    public void onViewClicked() {
        //弹出一个窗口，解释阅后即焚的事项
    }
}
