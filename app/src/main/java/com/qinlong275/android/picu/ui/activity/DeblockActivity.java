package com.qinlong275.android.picu.ui.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.czt.mp3recorder.MP3Recorder;
import com.qinlong275.android.picu.R;
import com.qinlong275.android.picu.bean.PicUItem;
import com.qinlong275.android.picu.common.CommonConst;
import com.qinlong275.android.picu.common.MicRecordingView;
import com.qinlong275.android.picu.ui.widget.DrawView;
import com.qinlong275.android.picu.ui.widget.TuyaView;
import com.tencent.aai.AAIClient;
import com.tencent.aai.audio.data.AudioRecordDataSource;
import com.tencent.aai.auth.AbsCredentialProvider;
import com.tencent.aai.auth.LocalCredentialProvider;
import com.tencent.aai.config.ClientConfiguration;
import com.tencent.aai.exception.ClientException;
import com.tencent.aai.exception.ServerException;
import com.tencent.aai.listener.AudioRecognizeResultListener;
import com.tencent.aai.listener.AudioRecognizeStateListener;
import com.tencent.aai.listener.AudioRecognizeTimeoutListener;
import com.tencent.aai.log.AAILogger;
import com.tencent.aai.model.AudioRecognizeRequest;
import com.tencent.aai.model.AudioRecognizeResult;
import com.tencent.aai.model.type.AudioRecognizeConfiguration;
import com.tencent.aai.model.type.AudioRecognizeTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class DeblockActivity extends BaseActivity implements DrawView.CirClickListener{

    @BindView(R.id.produce_close)
    Button mProduceClose;
    @BindView(R.id.top_bar)
    RelativeLayout mTopBar;
    @BindView(R.id.text_tit)
    TextView mTextTit;
    @BindView(R.id.icon_image)
    ImageView mIconImage;
    @BindView(R.id.user_show)
    CircleImageView mUserShow;
    @BindView(R.id.draw_image)
    RelativeLayout mDrawImage;
    @BindView(R.id.drawCirViewCon)
    RelativeLayout mDrawCirViewCon;
    @BindView(R.id.myreceivephotowatermark)
    ImageView mMyreceivephotowatermark;
    @BindView(R.id.pic_card)
    CardView mPicCard;
    @BindView(R.id.text_info)
    TextView mTextInfo;
    @BindView(R.id.show_index)
    RelativeLayout mShowIndex;
    @BindView(R.id.mic_record)
    Button mMicRecord;
    @BindView(R.id.micView)
    MicRecordingView mMicView;
    @BindView(R.id.text_bar)
    TextView mTextBar;
    @BindView(R.id.redraw)
    Button mRedraw;
    @BindView(R.id.draw_ok)
    Button mDrawOk;
    @BindView(R.id.close)
    Button mClose;
    @BindView(R.id.dialog_bg)
    ImageView mDialogBg;
    @BindView(R.id.try_again)
    Button mTryAgain;
    @BindView(R.id.alert_dialog)
    CardView mAlertDialog;
    @BindView(R.id.alert_view)
    RelativeLayout mAlertView;
    @BindView(R.id.lock_title)
    TextView mLockTitle;
    @BindView(R.id.secret_text)
    TextView mSecretText;
    @BindView(R.id.myshowplay)
    Button mMyshowplay;
    private Intent mIntent;

    private String mUnBlockType;      //1:语音  2：绘画    3：点击
    private String mSecretType;    //1:语音  2：文字

    private TuyaView mTuyaView;
    private int unLockTimes = 0;
    public static String TAG = "DeblockActivity";


    private MediaPlayer mMediaPlayer;

    private String recordPath;
    private File recordSaveDir;
    private String currentRecordPath;
    public static final int LockError = 1;
    public static final int LockoK = 2;

    private String mReceivedVoicepath;

    private boolean delockOK;
    String mPicuId;
    private PicUItem mPicUItem;
    private DrawView mDrawView;     //点击操作用的画板

    //语音识别
    private static final Logger logger = LoggerFactory.getLogger(MainActivity.class);
    LinkedHashMap<String, String> resMap = new LinkedHashMap<>();
    Handler handler;
    AAIClient aaiClient;
    AbsCredentialProvider credentialProvider;
    private final String PERFORMANCE_TAG = "PerformanceTag";
    int currentRequestId = 0;

    String mVoicekey="你好你好。";


    //收到的彩蛋要存下来，我收到的彩蛋页面会用

    //解锁成功的时候也要重置unLockTimes为0
    private Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LockError:
                    delockOK = false;
                    unLockTimes++;
                    mAlertView.setVisibility(View.VISIBLE);
                    mLockTitle.setText("失败" + unLockTimes + "次啦,找TA要点提示吧！");
                    mTryAgain.setBackgroundResource(R.drawable.btn_try_again);
                    mDialogBg.setBackgroundResource(R.drawable.bg_card_fail);

                    break;
                case LockoK:
                    delockOK = true;
                    unLockTimes = 0;
                    mAlertView.setVisibility(View.VISIBLE);
                    mLockTitle.setText("解锁成功");
                    mTryAgain.setBackgroundResource(R.drawable.btn_see_hidden_content);
                    mDialogBg.setBackgroundResource(R.drawable.bg_card_succeed);;
                    break;

            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deblock);
        ButterKnife.bind(this);

        handler = new Handler(getMainLooper());

        initAai();

        //初始化这个PicU的各种信息
        mIntent = getIntent();
        mPicuId=mIntent.getStringExtra("PicUId");

        //用mPicuId去拉取数据
        if (mPicuId.equals("3")){
            mPicUItem=new PicUItem("q","","a","","a",
                    "","1", "2","true");
        }else if (mPicuId.equals("13")){
            mPicUItem=new PicUItem("q","","a","","a",
                    "","2", "1","true");
        }else {
            mPicUItem=new PicUItem("q","","a","","a",
                    "","2", "3","true");
        }

        mUnBlockType = mPicUItem.getUnBlockType();
        mSecretType=mPicUItem.getSecretType();
//        mReceivedVoicepath=mPicUItem.getVoiceUrl();
        mReceivedVoicepath=Environment.getExternalStorageDirectory() +File.separator+"test.mp3";

        if (!(mUnBlockType.equals("1"))) initView();


        recordPath = Environment.getExternalStorageDirectory() + File.separator + "PicUfiles";
        recordSaveDir = new File(recordPath);
        if (!recordSaveDir.exists()) {
            recordSaveDir.mkdir();
        }
    }

    private void initAai(){

        // 用户配置：需要在控制台申请相关的账号;
        final int appid = CommonConst.appid;;
        final int projectId = CommonConst.projectId;
        final String secretId = CommonConst.secretId;
        final String secretKey = CommonConst.secretKey;

        AAILogger.info(logger, "config : appid={}, projectId={}, secretId={}, secretKey={}", appid, projectId, secretId, secretKey);

        // 签名鉴权类，sdk中给出了一个本地的鉴权类，但由于需要用户提供secretKey，这可能会导致一些安全上的问题，
        // 因此，请用户自行实现CredentialProvider接口
        credentialProvider = new LocalCredentialProvider(secretKey);

        // 用户配置
        ClientConfiguration.setServerProtocolHttps(false); // 是否启用https，默认启用
        ClientConfiguration.setMaxAudioRecognizeConcurrentNumber(2); // 语音识别的请求的最大并发数
        ClientConfiguration.setMaxRecognizeSliceConcurrentNumber(10); // 单个请求的分片最大并发数

        // 识别结果回调监听器
        final AudioRecognizeResultListener audioRecognizeResultlistener = new AudioRecognizeResultListener() {

            boolean dontHaveResult = true;

            /**
             * 返回分片的识别结果
             * @param request 相应的请求
             * @param result 识别结果
             * @param seq 该分片所在语音流的序号 (0, 1, 2...)
             */
            @Override
            public void onSliceSuccess(AudioRecognizeRequest request, AudioRecognizeResult result, int seq) {

                if (dontHaveResult && !TextUtils.isEmpty(result.getText())) {
                    dontHaveResult = false;
                    Date date=new Date();
                    DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
                    String time=format.format(date);
                    String message = String.format("voice flow order = %d, receive first response in %s, result is = %s", seq, time, result.getText());
                    Log.i(PERFORMANCE_TAG, message);
                }

                AAILogger.info(logger, "on slice success..");
                AAILogger.info(logger, "on slice success..");
                AAILogger.info(logger, "seq = {}, voiceid = {}, result = {}", seq, result.getVoiceId(), result.getText());
                resMap.put(String.valueOf(seq), result.getText());
                final String msg = buildMessage(resMap);
                AAILogger.info(logger, "msg="+msg);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG,msg);
                        mTextInfo.setText(msg);
                    }
                });

            }

            /**
             * 返回语音流的识别结果
             * @param request 相应的请求
             * @param result 识别结果
             * @param seq 该语音流的序号 (1, 2, 3...)
             */
            @Override
            public void onSegmentSuccess(AudioRecognizeRequest request, AudioRecognizeResult result, int seq) {
                dontHaveResult = true;
                AAILogger.info(logger, "on segment success");
                AAILogger.info(logger, "seq = {}, voiceid = {}, result = {}", seq, result.getVoiceId(), result.getText());
                resMap.put(String.valueOf(seq), result.getText());
                final String msg = buildMessage(resMap);
                AAILogger.info(logger, "msg="+msg);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG,msg);
                        mTextInfo.setText(msg);
                    }
                });
            }

            /**
             * 识别结束回调，返回所有的识别结果
             * @param request 相应的请求
             * @param result 识别结果
             */
            @Override
            public void onSuccess(AudioRecognizeRequest request, String result) {
                AAILogger.info(logger, "onSuccess..");
                AAILogger.info(logger, "result = {}", result);
            }

            /**
             * 识别失败
             * @param request 相应的请求
             * @param clientException 客户端异常
             * @param serverException 服务端异常
             */
            @Override
            public void onFailure(AudioRecognizeRequest request, final ClientException clientException, final ServerException serverException) {
                if (clientException!=null) {
                    AAILogger.info(logger, "onFailure..:"+clientException.toString());
                }
                if (serverException!=null) {
                    AAILogger.info(logger, "onFailure..:"+serverException.toString());
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        if (clientException!=null) {
                            mTextInfo.setText("识别状态：失败,  "+clientException.toString());
                        } else if (serverException!=null) {
                            mTextInfo.setText("识别状态：失败,  "+serverException.toString());
                        }
                    }
                });
            }
        };


        /**
         * 识别状态监听器
         */
        final AudioRecognizeStateListener audioRecognizeStateListener = new AudioRecognizeStateListener() {

            /**
             * 开始录音
             * @param request
             */
            @Override
            public void onStartRecord(AudioRecognizeRequest request) {
                currentRequestId = request.getRequestId();
                AAILogger.info(logger, "onStartRecord..");
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }

            /**
             * 结束录音
             * @param request
             */
            @Override
            public void onStopRecord(AudioRecognizeRequest request) {
                AAILogger.info(logger, "onStopRecord..");
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        // start.setEnabled(true);
                    }
                });
            }

            /**
             * 第seq个语音流开始识别
             * @param request
             * @param seq
             */
            @Override
            public void onVoiceFlowStartRecognize(AudioRecognizeRequest request, int seq) {




                AAILogger.info(logger, "onVoiceFlowStartRecognize.. seq = {}", seq);
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }

            /**
             * 第seq个语音流结束识别
             * @param request
             * @param seq
             */
            @Override
            public void onVoiceFlowFinishRecognize(AudioRecognizeRequest request, int seq) {

                Date date=new Date();
                DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
                String time=format.format(date);
                String message = String.format("voice flow order = %d, recognize finish in %s", seq, time);
                Log.i(PERFORMANCE_TAG, message);

                AAILogger.info(logger, "onVoiceFlowFinishRecognize.. seq = {}", seq);
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }

            /**
             * 第seq个语音流开始
             * @param request
             * @param seq
             */
            @Override
            public void onVoiceFlowStart(AudioRecognizeRequest request, int seq) {

                Date date=new Date();
                DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
                String time=format.format(date);
                String message = String.format("voice flow order = %d, start in %s", seq, time);
                Log.i(PERFORMANCE_TAG, message);

                AAILogger.info(logger, "onVoiceFlowStart.. seq = {}", seq);

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }

            /**
             * 第seq个语音流结束
             * @param request
             * @param seq
             */
            @Override
            public void onVoiceFlowFinish(AudioRecognizeRequest request, int seq) {

                Date date=new Date();
                DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
                String time=format.format(date);
                String message = String.format("voice flow order = %d, stop in %s", seq, time);
                Log.i(PERFORMANCE_TAG, message);

                AAILogger.info(logger, "onVoiceFlowFinish.. seq = {}", seq);
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }

            /**
             * 语音音量回调
             * @param request
             * @param volume
             */
            @Override
            public void onVoiceVolume(AudioRecognizeRequest request, final int volume) {
                AAILogger.info(logger, "onVoiceVolume..");
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }

        };

        /**
         * 识别超时监听器
         */
        final AudioRecognizeTimeoutListener audioRecognizeTimeoutListener = new AudioRecognizeTimeoutListener() {

            /**
             * 检测第一个语音流超时
             * @param request
             */
            @Override
            public void onFirstVoiceFlowTimeout(AudioRecognizeRequest request) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }

            /**
             * 检测下一个语音流超时
             * @param request
             */
            @Override
            public void onNextVoiceFlowTimeout(AudioRecognizeRequest request) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        };

        initMicRecord(audioRecognizeResultlistener, audioRecognizeStateListener, audioRecognizeTimeoutListener);

    }

    private String buildMessage(Map<String, String> msg) {

        StringBuffer stringBuffer = new StringBuffer();
        Iterator<Map.Entry<String, String>> iter = msg.entrySet().iterator();
        while (iter.hasNext()) {
            String value = iter.next().getValue();
            stringBuffer.append(value+"\r\n");
        }
        return stringBuffer.toString();
    }

    private void initView() {

        //加载头像
        if (mUnBlockType.equals("2")) {

            //画板在这里
            int screenWidth = 1000;
            int screenHeight = 1200;
            mTuyaView = new TuyaView(this, screenWidth, screenHeight);
            mTuyaView.requestFocus();
            mTuyaView.selectPaintSize(10);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            mDrawImage.addView(mTuyaView, params);
            mPicCard.setVisibility(View.VISIBLE);
            mTextTit.setVisibility(View.VISIBLE);
            mRedraw.setVisibility(View.VISIBLE);
            mDrawOk.setVisibility(View.VISIBLE);

            //根据收到的PicU的信息来填入提示信息
            mTextInfo.setText("啊哦，Ta没有留下提示哦");
            mTextBar.setVisibility(View.INVISIBLE);
            mMicRecord.setVisibility(View.INVISIBLE);
            mMyreceivephotowatermark.setImageResource(R.drawable.btn_watermark_sketch);
        } else {
            //点击出现圆圈，计算位置
            RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            param.addRule(RelativeLayout.CENTER_IN_PARENT);
            mDrawView = new DrawView(DeblockActivity.this, null,this);
            mDrawCirViewCon.addView(mDrawView, param);
            mDrawCirViewCon.setVisibility(View.VISIBLE);
            mTextTit.setVisibility(View.VISIBLE);
            mTextTit.setText("点击Ta选定的位置~");
            //根据收到的PicU的信息来填入提示信息
            mTextInfo.setText("啊哦，Ta没有留下提示哦");
            mMyreceivephotowatermark.setImageResource(R.drawable.btn_watermark_click);
            mTextBar.setVisibility(View.INVISIBLE);
            mMicRecord.setVisibility(View.INVISIBLE);
        }
    }

    private void initMediaPlayer() {

        try {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mMyshowplay.setBackgroundResource(R.drawable.btn_playing_audio);
                }
            });
            mMediaPlayer.setDataSource(mReceivedVoicepath);//指定音頻文件路徑
            mMediaPlayer.prepare();//讓MediaPlayer進入準備狀態
            Log.d(TAG, "准备播放器");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initMicRecord(final AudioRecognizeResultListener audioRecognizeResultlistener,final AudioRecognizeStateListener audioRecognizeStateListener,
                               final AudioRecognizeTimeoutListener audioRecognizeTimeoutListener) {
        mMicRecord.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        mMicView.start();
                        mMicView.setVisibility(View.VISIBLE);

                        AAILogger.info(logger, "the start button has clicked..");
                        resMap.clear();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                //   start.setEnabled(false);
                            }
                        });
                        AudioRecognizeRequest.Builder builder = new AudioRecognizeRequest.Builder();
                        //File file = new File(Environment.getExternalStorageDirectory()+"/tencent_aai____/audio", "1.pcm");

                        // 初始化识别请求
                        final AudioRecognizeRequest audioRecognizeRequest = builder
                                .pcmAudioDataSource(new AudioRecordDataSource()) // 设置数据源
                                //.templateName(templateName) // 设置模板
                                .template(new AudioRecognizeTemplate(1,0,0)) // 设置自定义模板
                                .build();

                        // 自定义识别配置
                        final AudioRecognizeConfiguration audioRecognizeConfiguration = new AudioRecognizeConfiguration.Builder()
                                .enableAudioStartTimeout(false) // 是否使能起点超时停止录音
                                .enableAudioEndTimeout(false) // 是否使能终点超时停止录音
                                .enableSilentDetect(true) // 是否使能静音检测，true表示不检查静音部分
                                .minAudioFlowSilenceTime(1000) // 语音流识别时的间隔时间
                                .maxAudioFlowSilenceTime(10000) // 语音终点超时时间
                                .maxAudioStartSilenceTime(10000) // 语音起点超时时间
                                .minVolumeCallbackTime(80) // 音量回调时间
                                .sensitive(2)
                                .build();

                        if (aaiClient==null) {
                            try {
                                aaiClient = new AAIClient(DeblockActivity.this, CommonConst.appid, CommonConst.projectId
                                        , CommonConst.secretId, credentialProvider);
                            } catch (ClientException e) {
                                e.printStackTrace();
                                AAILogger.info(logger, e.toString());
                            }
                        }
                        //currentRequestId = audioRecognizeRequest.getRequestId();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                aaiClient.startAudioRecognize(audioRecognizeRequest, audioRecognizeResultlistener,
                                        audioRecognizeStateListener, audioRecognizeTimeoutListener,
                                        audioRecognizeConfiguration);

                            }
                        }).start();
                        break;
                    case MotionEvent.ACTION_UP:

                        //停止语音识别
                        AAILogger.info(logger, "stop button is clicked..");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                boolean taskExist = false;
                                if (aaiClient!=null) {
                                    taskExist = aaiClient.stopAudioRecognize(currentRequestId);
                                }
                                if (!taskExist) {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {

                                        }
                                    });
                                }
                            }
                        }).start();
                        //


                        mMicView.stop();
                        mMicView.setVisibility(View.INVISIBLE);

                        //检测正确性
                        if (trimString(mTextInfo.getText().toString()).equals(mVoicekey)){
                            sendLockMessage(true);
                        }else {
                            sendLockMessage(false);
                        }
                        break;
                }
                return true;
            }
        });
    }

    private String trimString(String in){
        StringBuilder builder=new StringBuilder();
        char[] a=in.toCharArray();
        for(char c:a){
            if ((c=='，')||(c=='。')){

            }else {
                builder.append(c);
            }
        }
        return builder.toString();
    }

    private void sendLockMessage(boolean deLockok) {
        Message message = new Message();
        if (deLockok) {
            message.what = LockoK;
        } else {
            message.what = LockError;
        }
        myHandler.sendMessage(message);
    }


    @OnClick({R.id.produce_close, R.id.mic_record, R.id.redraw, R.id.draw_ok, R.id.close, R.id.try_again})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.produce_close:
                finish();
                break;
            case R.id.redraw:
                mTuyaView.redo();
                break;
            case R.id.draw_ok:
                //保存画板图片，去服务端进行对比，对结果进行弹窗show,错误的话可以删除
                mTuyaView.saveToSDCard("jiesuo");

                if (2 > 1) {
                    sendLockMessage(true);
                } else {
                    sendLockMessage(false);
                }
                break;
            case R.id.close:
                mAlertView.setVisibility(View.INVISIBLE);
                break;
            case R.id.try_again:
                mAlertView.setVisibility(View.INVISIBLE);
                if (delockOK) {
                    //显示出彩蛋，文字显示出来，音频显示播放按钮
                    if (mSecretType.equals("1")){
                        mMyshowplay.setVisibility(View.VISIBLE);
                    }else {
                        mSecretText.setVisibility(View.VISIBLE);
                        mSecretText.setText("隐藏的彩蛋信息呦~");
                    }
                } else {
                    mTextInfo.setText("请说出: "+mVoicekey);
                    mAlertView.setVisibility(View.INVISIBLE);
                    if (mTuyaView!=null){
                        mTuyaView.redo();
                    }
                }
                break;
        }
    }

    @OnClick(R.id.myshowplay)
    public void onViewClicked() {
        mMyshowplay.setBackgroundResource(R.drawable.ic_action_stop);
        //播放器
        initMediaPlayer();
        //开始播放
        if (!mMediaPlayer.isPlaying()) {
            mMediaPlayer.start();
        }else {
            if (mMediaPlayer.isPlaying()){
                mMediaPlayer.reset();
                initMediaPlayer();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
        }
    }

    @Override
    public void onClickCir(int x, int y) {
        //通过点击的位置x,y与密码中的x,y相比较看位置是否大致一致决定是否解锁成功
        //根据相应的算法来判断一个范围
        if (2 < 1) {
            pushDelay(true);

        } else {
            pushDelay(false);
        }
    }

    private void pushDelay(boolean isDelockOk){
        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sendLockMessage(true);
            }
        },250);
    }
}
