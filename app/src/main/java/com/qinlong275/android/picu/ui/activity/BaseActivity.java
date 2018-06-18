package com.qinlong275.android.picu.ui.activity;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.qinlong275.android.picu.MyApplication;
import com.qinlong275.android.picu.R;
import com.qinlong275.android.picu.common.util.PicUtils;

import java.util.Map;

public class BaseActivity extends AppCompatActivity {

    private AlertDialog.Builder dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUpInvite(this);
    }

    protected void getUpInvite(final Context context) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        //无数据时直接返回
        if (!clipboard.hasPrimaryClip()) {
            return;
        }
        //如果是文本信息
        if (clipboard.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
            ClipData cdText = clipboard.getPrimaryClip();
            ClipData.Item item = cdText.getItemAt(0);
            //此处是TEXT文本信息
            String text=item.getText().toString();
            if ((text != null)&&(!((text).equals(MyApplication.getPicuKey())))) {
                MyApplication.setPicuKey(text);
                showAlertDia(context,text);
            }
        }
    }

    protected void showAlertDia(final Context mContext, final String text){
        dialog = new AlertDialog.Builder(this);
        dialog.setTitle("PickPickPicU");
        dialog.setMessage("您收到一条来自远方的PicU,点击确认查看");
        dialog.setCancelable(false);

        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //这里定义确认响应事件
                Intent intent=new Intent(mContext,DeblockActivity.class);
                intent.putExtra("PicUId",getPicUid(text));
                startActivity(intent);
            }
        });

        dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //这里定义否定响应事件

            }
        });
        dialog.show();
    }

    protected String getPicUid(String input){
        if (input!=null){
            String[] items=input.split("#");
            return items[1];
        }
        return "";
    }
}
