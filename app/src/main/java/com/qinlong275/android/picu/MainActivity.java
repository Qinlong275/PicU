package com.qinlong275.android.picu;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.czt.mp3recorder.MP3Recorder;
import com.qinlong275.android.picu.common.ColorBarView;
import com.qinlong275.android.picu.common.MicRecordingView;
import com.qinlong275.android.picu.common.PicUtils;
import com.qinlong275.android.picu.ui.activity.BlockChoseActivity;
import com.qinlong275.android.picu.ui.activity.SettingActivity;
import com.qinlong275.android.picu.ui.activity.myreceive;
import com.qinlong275.android.picu.ui.widget.BannerLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, ColorBarView.ColorChangeListener {

    private static final int TAKE_PHOTO = 1;
    private static final int CHOOSE_PHOTO = 2;
    @BindView(R.id.user_home)
    CircleImageView mUserHome;
    private ImageView mImageView;
    public Uri imageUri;
    private BannerLayout mBannerLayout;
    private CircleImageView addButton;
    private CircleImageView takePhoto;
    private CircleImageView openAlbum;
    private View mChosePage;
    private View mProducePage;
    private String picturePath;

    private Button mRecordButton;
    private Button mResetButton;
    private Button mRecordOkButton;
    private Button playMusic;
    private Button musicStop;
    private Button mChoseMic;
    private Button mChoseText;

    private Button mChoseTextColor;
    private Button mTextSize;
    private Button mTextPosition;
    private Button mProduceClose;
    private Button mTextOk;
    private Button mMicRecordOk;

    private FrameLayout mFrameLayout;
    private View mTextBar;
    private EditText mEditText;


    private MicRecordingView mRecordingView;

    private boolean start = true;
    private boolean isProduce = false;

    private static final String[] permissionsArray = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO};
    //还需申请的权限列表
    private List<String> permissionsList = new ArrayList<String>();
    //申请权限后的返回码
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 1;

    private MediaPlayer mMediaPlayer;
    public static String TAG = "mainActivity";

    private int mState = 0; //0：还没有选择彩蛋方式，1：语音，2：文字
    private int textSize = 0; //0:X,1:XX,2:XXX
    private int textPosition = 0; //0:左，1:中,2：右
    RelativeLayout.LayoutParams mLayoutParams;
    private View dialogView;
    private AlertDialog.Builder dialog;

    private Intent mIntent;     //彩蛋制作完成跳转
    private MP3Recorder mMP3Recorder;
    String recordPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        checkRequiredPermission(MainActivity.this);

        picturePath = Environment.getExternalStorageDirectory() + File.separator + "outout_image.jpg";
        mChosePage = (View) findViewById(R.id.chose_page);
        mProducePage = (View) findViewById(R.id.produce_page);
        mBannerLayout = (BannerLayout) findViewById(R.id.banner);
        addButton = (CircleImageView) findViewById(R.id.icon_add);
        takePhoto = (CircleImageView) findViewById(R.id.icon_take_photo);
        openAlbum = (CircleImageView) findViewById(R.id.icon_album);
        mImageView = (ImageView) findViewById(R.id.ic_picture);
        mRecordButton = (Button) findViewById(R.id.ic_record);
        mResetButton = (Button) findViewById(R.id.reset_mic);
        mChoseTextColor = (Button) findViewById(R.id.ic_chose_text_color);
        mRecordOkButton = (Button) findViewById(R.id.mic_record_ok);
        playMusic = (Button) findViewById(R.id.music_play);
        musicStop = (Button) findViewById(R.id.music_stop);
        mChoseMic = (Button) findViewById(R.id.chose_mic);
        mChoseText = (Button) findViewById(R.id.chose_text);
        mFrameLayout = (FrameLayout) findViewById(R.id.color_change_bar);
        mTextBar = (View) findViewById(R.id.text_toolbar);
        mEditText = (EditText) findViewById(R.id.text_input);
        mTextPosition = (Button) findViewById(R.id.text_position);
        mTextSize = (Button) findViewById(R.id.select_text_size);
        mProduceClose = (Button) findViewById(R.id.produce_close);
        mTextOk = (Button) findViewById(R.id.produce_text_ok);

        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //当actionId == XX_SEND 或者 XX_DONE时都触发
                //或者event.getKeyCode == ENTER 且 event.getAction == ACTION_DOWN时也触发
                //注意，这是一定要判断event != null。因为在某些输入法上会返回null。
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                    PicUtils.toggleInput(MainActivity.this);
                    mEditText.setEnabled(false);
                    mState = 2;
                }
                return false;
            }
        });

        mRecordingView = (MicRecordingView) findViewById(R.id.micView);

        openAlbum.setOnClickListener(this);
        takePhoto.setOnClickListener(this);
        addButton.setOnClickListener(this);
        mResetButton.setOnClickListener(this);
        mRecordOkButton.setOnClickListener(this);
        playMusic.setOnClickListener(this);
        musicStop.setOnClickListener(this);
        mChoseText.setOnClickListener(this);
        mChoseMic.setOnClickListener(this);
        mChoseTextColor.setOnClickListener(this);
        mProduceClose.setOnClickListener(this);
        mTextSize.setOnClickListener(this);
        mTextPosition.setOnClickListener(this);
        mTextOk.setOnClickListener(this);

        mRecordButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                Log.d(TAG, "touchRecordButton");
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mRecordingView.setVisibility(View.VISIBLE);
                        mRecordingView.start();
                        //获得系统当前时间，并以该时间作为文件名
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
                        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                        recordPath = Environment.getExternalStorageDirectory() + File.separator + formatter.format(curDate) + ".mp3";
                        File file = new File(recordPath);
                        mMP3Recorder = new MP3Recorder(file);
                        try {
                            mMP3Recorder.start();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (mMP3Recorder != null && mMP3Recorder.isRecording()) {
                            mMP3Recorder.setPause(false);
                            mMP3Recorder.stop();

                            initMediaPlayer();
                        }
                        mRecordingView.stop();
                        mRecordingView.setVisibility(View.INVISIBLE);
                        mResetButton.setVisibility(View.VISIBLE);
                        mRecordOkButton.setVisibility(View.VISIBLE);
                        playMusic.setVisibility(View.VISIBLE);
                        mRecordButton.setVisibility(View.INVISIBLE);
                        mState = 1;
                        break;
                }
                return true;
            }
        });

        mBannerLayout.initView();
        mBannerLayout.setOnBannerItemClickListener(new BannerLayout.OnBannerItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //动态添加的时候要根据position获得参数绑定点击事件

            }
        });
    }

    private void checkRequiredPermission(final Activity activity) {
        for (String permission : permissionsArray) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
            }
        }
        if (permissionsList.size() > 0) {
            ActivityCompat.requestPermissions(activity, permissionsList.toArray(new String[permissionsList.size()]), REQUEST_CODE_ASK_PERMISSIONS);
        }
    }

    private void initMediaPlayer() {

        try {
            mMediaPlayer=new MediaPlayer();
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    musicStop.setVisibility(View.INVISIBLE);
                    playMusic.setVisibility(View.VISIBLE);
                    mResetButton.setVisibility(View.VISIBLE);
                    mRecordOkButton.setVisibility(View.VISIBLE);
                }
            });
            mMediaPlayer.setDataSource(recordPath);//指定音頻文件路徑
            mMediaPlayer.prepare();//讓MediaPlayer進入準備狀態
            Log.d(TAG, "准备播放器");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    mProducePage.setVisibility(View.VISIBLE);
                    mChosePage.setVisibility(View.INVISIBLE);
                    isProduce = true;
                    try {
                        //將拍攝的照片顯示出來
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        mImageView.setImageBitmap(PicUtils.rotateBitmap(bitmap, picturePath));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    mProducePage.setVisibility(View.VISIBLE);
                    mChosePage.setVisibility(View.INVISIBLE);
                    isProduce = true;
                    //判斷手機系統版本號
                    if (Build.VERSION.SDK_INT >= 19) {
                        //4.4及以上系統使用這個方法處理圖片
                        handleImageOnKitkat(data);
                    } else {
                        //4.4及以下系統使用此方法處理圖片
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            case R.id.music_stop:
                mMediaPlayer.stop();
            default:
                break;
        }
    }


    @TargetApi(19)
    private void handleImageOnKitkat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            //如果是document類型的Uri。則通過document id 處理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];//解析出文字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public" +
                        "_downloads"), Long.valueOf(docId));
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //如果是content類型的Uri，則是用普通方式處理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //如果是file類型的Uri，直接獲取圖片路徑即可
            imagePath = uri.getPath();
        }
        displayImage(imagePath);//根據圖片路徑顯示圖片
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通過Uri和selection來獲取真實的圖片途徑
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            mImageView.setImageBitmap(bitmap);
        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }


    //展开工具栏
    private void open() {
        start = false;
        ObjectAnimator translationLeft = new ObjectAnimator().ofFloat(takePhoto, "translationX", 0, -220f);
        translationLeft.setDuration(800);
        translationLeft.start();
        ObjectAnimator translationRight = new ObjectAnimator().ofFloat(openAlbum, "translationX", 0, 220f);
        translationRight.setDuration(800);
        translationRight.start();
        ObjectAnimator re = ObjectAnimator.ofFloat(addButton, "rotation", 0f, 90f);
        AnimatorSet animatorSetsuofang = new AnimatorSet();//组合动画
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(addButton, "scaleX", 1, 0.8f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(addButton, "scaleY", 1, 0.8f);
        animatorSetsuofang.setDuration(800);
        animatorSetsuofang.play(scaleX).with(scaleY).with(re);
        animatorSetsuofang.start();
    }

    //合上工具栏
    private void close() {
        start = true;
        ObjectAnimator translationLeft = new ObjectAnimator().ofFloat(takePhoto, "translationX", -220, 0f);
        translationLeft.setDuration(800);
        translationLeft.start();
        ObjectAnimator translationRight = new ObjectAnimator().ofFloat(openAlbum, "translationX", 220, 0f);
        translationRight.setDuration(800);
        translationRight.start();
        ObjectAnimator re = ObjectAnimator.ofFloat(addButton, "rotation", 90f, 0f);
        AnimatorSet animatorSetsuofang = new AnimatorSet();//组合动画
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(addButton, "scaleX", 0.8f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(addButton, "scaleY", 0.8f, 1f);
        animatorSetsuofang.setDuration(800);
        animatorSetsuofang.play(scaleX).with(scaleY).with(re);
        animatorSetsuofang.start();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.icon_add:
                if (start) {
                    open();
                } else {
                    close();
                }
                break;
            case R.id.icon_take_photo:
                //創建File對象用於存儲拍照后的圖片
                File outputImage = new File(Environment.getExternalStorageDirectory(), "outout_image.jpg");
                try {
                    if (outputImage.exists()) {
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (Build.VERSION.SDK_INT >= 24) {
                    imageUri = FileProvider.getUriForFile(MainActivity.this,
                            "com.qinlong275.android.picu.fileprovider", outputImage);
                } else {
                    imageUri = Uri.fromFile(outputImage);
                }
                //啓動相機程序
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, TAKE_PHOTO);
                break;
            case R.id.icon_album:
                openAlbum();
                break;
            case R.id.reset_mic:
                //重新录制
                mRecordOkButton.setVisibility(View.INVISIBLE);
                mRecordButton.setVisibility(View.VISIBLE);
                mResetButton.setVisibility(View.INVISIBLE);
                playMusic.setVisibility(View.INVISIBLE);
                mState = 0;
                break;
            case R.id.music_play:
                //播放器
                initMediaPlayer();
                //开始播放
                playMusic.setVisibility(View.INVISIBLE);
                musicStop.setVisibility(View.VISIBLE);
                if (!mMediaPlayer.isPlaying()) {
                    mMediaPlayer.start();
                }

                mResetButton.setVisibility(View.INVISIBLE);
                mRecordOkButton.setVisibility(View.INVISIBLE);
                break;
            case R.id.music_stop:
                if (mMediaPlayer.isPlaying()){
                    mMediaPlayer.reset();
                    initMediaPlayer();
                }
                musicStop.setVisibility(View.INVISIBLE);
                playMusic.setVisibility(View.VISIBLE);
                mRecordOkButton.setVisibility(View.VISIBLE);
                mResetButton.setVisibility(View.VISIBLE);
                break;
            case R.id.ic_chose_text_color:
                mTextBar.setVisibility(View.INVISIBLE);
                mFrameLayout.addView(new ColorBarView(this, this));
            case R.id.chose_text:

                if (mState == 0) {
                    showTextBar();
                } else if (mState == 1) {
                    //弹窗提醒
                    showAlertDialog();
                }
                break;
            case R.id.chose_mic:
                if (mState == 0) {
                    showMicBar();
                } else if (mState == 2) {
                    //弹窗提醒
                    showAlertDialog();
                }
                break;
            case R.id.produce_close:
                //撤销此次制作
                init();
                isProduce = false;
                mProducePage.setVisibility(View.INVISIBLE);
                mChosePage.setVisibility(View.VISIBLE);
                mRecordButton.setVisibility(View.VISIBLE);
                playMusic.setVisibility(View.INVISIBLE);
                break;
            case R.id.select_text_size:
                Log.d(TAG, "textSize: " + textSize);
                switch (textSize) {
                    case 0:
                        mEditText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 32);
                        textSize++;
                        break;
                    case 1:
                        mEditText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 39);
                        textSize++;
                        break;
                    case 2:
                        mEditText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
                        textSize = 0;
                        break;
                }
                break;
            case R.id.text_position:

                switch (textPosition) {
                    case 0:
                        mLayoutParams = (RelativeLayout.LayoutParams) mEditText.getLayoutParams();
                        mLayoutParams.removeRule(RelativeLayout.CENTER_IN_PARENT);
                        mLayoutParams.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                        mLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                        mEditText.setLayoutParams(mLayoutParams);
                        textPosition++;
                        break;
                    case 1:
                        mLayoutParams = (RelativeLayout.LayoutParams) mEditText.getLayoutParams();
                        mLayoutParams.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
                        mLayoutParams.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                        mLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                        mEditText.setLayoutParams(mLayoutParams);
                        textPosition++;
                        break;
                    case 2:
                        mLayoutParams = (RelativeLayout.LayoutParams) mEditText.getLayoutParams();
                        mLayoutParams.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
                        mLayoutParams.removeRule(RelativeLayout.CENTER_IN_PARENT);
                        mLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                        mEditText.setLayoutParams(mLayoutParams);
                        textPosition = 0;
                        break;
                }
                break;
            case R.id.produce_text_ok:
                //完成跳转到添加解锁页面,文字彩蛋,传递存储参数


                mIntent = new Intent(this, BlockChoseActivity.class);
                startActivity(mIntent);
                break;
            case R.id.mic_record_ok:
                //完成跳转到添加解锁页面,语音彩蛋


                mIntent = new Intent(this, BlockChoseActivity.class);
                startActivity(mIntent);
                break;
            default:
                break;
        }
    }

    private void showAlertDialog() {
        LayoutInflater inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.alert_dialog, null);
        Button cancelBtn = (Button) dialogView.findViewById(R.id.alert_cancel);
        Button confirmBtn = (Button) dialogView.findViewById(R.id.alert_confirm);

        dialog = new AlertDialog.Builder(this);
        dialog.setView(dialogView);
        dialog.setCancelable(false);
        final AlertDialog mDialog = dialog.show();

        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
        WindowManager.LayoutParams p = mDialog.getWindow().getAttributes();  //获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.3);   //高度设置为屏幕的0.3
        p.width = (int) (d.getWidth() * 0.9);    //宽度设置为屏幕的0.5
        mDialog.getWindow().setAttributes(p);     //设置生效


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ViewGroup) dialogView.getParent()).removeView(dialogView);//去除子View
                mDialog.dismiss();
            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ViewGroup) dialogView.getParent()).removeView(dialogView);//去除子View
                mDialog.dismiss();
                //根据当前状态选择另外一种彩蛋方式
                switch (mState) {
                    case 1:
                        //转换到文字
                        showTextBar();
                        break;
                    case 2:
                        //转换到语音
                        showMicBar();
                        break;
                }
            }
        });

    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);//打開相冊
    }

    //xiugai
    @Override
    public void onBackPressed() {
        if (isProduce) {
            init();
            isProduce = false;
            mProducePage.setVisibility(View.INVISIBLE);
            mChosePage.setVisibility(View.VISIBLE);
            mRecordButton.setVisibility(View.VISIBLE);
            playMusic.setVisibility(View.INVISIBLE);
        } else {
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {

                    } else {
                        Toast.makeText(MainActivity.this, "权限被拒绝： " + permissions[i], Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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

    //回调彩蛋文字颜色改变
    @Override
    public void onStateChange(int which) {
        switch (which) {
            case 1:
                mEditText.setTextColor(getResources().getColor(R.color.textColor1));
                break;
            case 2:
                mEditText.setTextColor(getResources().getColor(R.color.textColor2));
                break;
            case 3:
                mEditText.setTextColor(getResources().getColor(R.color.textColor3));
                break;
            case 4:
                mEditText.setTextColor(getResources().getColor(R.color.textColor4));
                break;
            case 5:
                mEditText.setTextColor(getResources().getColor(R.color.textColor5));
                break;
            case 6:
                mEditText.setTextColor(getResources().getColor(R.color.textColor6));
                break;
            case 7:
                mEditText.setTextColor(getResources().getColor(R.color.textColor7));
                break;
            case 8:
                mFrameLayout.removeAllViews();
                mTextBar.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void init() {
        //produce页面全部重置,
        mState = 0;
        showMicBar();
    }

    private void showMicBar() {

        mState = 0;
        mLayoutParams = (RelativeLayout.LayoutParams) mEditText.getLayoutParams();
        mLayoutParams.removeRule(RelativeLayout.CENTER_IN_PARENT);
        mLayoutParams.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        mLayoutParams.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
        mLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        mEditText.setLayoutParams(mLayoutParams);

        mRecordButton.setVisibility(View.VISIBLE);
        mTextPosition.setVisibility(View.INVISIBLE);
        mTextSize.setVisibility(View.INVISIBLE);
        mChoseTextColor.setVisibility(View.INVISIBLE);
        mEditText.setVisibility(View.INVISIBLE);
        mEditText.setText("");
        mEditText.setTextColor(getResources().getColor(R.color.textColor4));
        mEditText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
        mChoseMic.setBackgroundResource(R.drawable.ic_action_mic_chose);
        mChoseText.setBackgroundResource(R.drawable.ic_action_text_normal);

        mFrameLayout.removeAllViews();
        mTextBar.setVisibility(View.VISIBLE);
        mTextOk.setVisibility(View.INVISIBLE);
    }

    private void showTextBar() {
        //删除录音文件
        mTextOk.setVisibility(View.VISIBLE);
        mState = 0;
        mEditText.setVisibility(View.VISIBLE);
        mEditText.setEnabled(true);
        mTextSize.setVisibility(View.VISIBLE);
        mChoseTextColor.setVisibility(View.VISIBLE);
        mTextPosition.setVisibility(View.VISIBLE);
        mRecordButton.setVisibility(View.INVISIBLE);
        mChoseMic.setBackgroundResource(R.drawable.ic_action_mic_normal);
        mChoseText.setBackgroundResource(R.drawable.ic_action_text_chose);
        mResetButton.setVisibility(View.INVISIBLE);
        mRecordOkButton.setVisibility(View.INVISIBLE);
        playMusic.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.user_home)
    public void onViewClicked() {
        Intent intent = new Intent(MainActivity.this, myreceive.class);
        startActivity(intent);
    }
}
