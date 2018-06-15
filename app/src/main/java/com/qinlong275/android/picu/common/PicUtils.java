package com.qinlong275.android.picu.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import java.io.IOException;

public class PicUtils {

    //隐藏软键盘
    public static void toggleInput(Context context) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    //旋转图片
    public static Bitmap rotateBitmap(Bitmap b, String filepath) {
        int degrees = getExifOrientation(filepath);
        Log.d("xuanzhuan", String.valueOf(degrees));
        if (degrees != 0 && b != null) {
            Matrix m = new Matrix();
            m.setRotate((float) degrees, (float) b.getWidth() / 2.0F, (float) b.getHeight() / 2.0F);

            try {
                Bitmap b2 = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), m, true);
                if (b != b2) {
                    b.recycle();
                    b = b2;
                }
            } catch (OutOfMemoryError var5) {
                System.out.println("outOf");
            }
        }

        return b;
    }

    //判断拍摄的图片被旋转的角度
    public static int getExifOrientation(String filepath) {
        short degree = 0;
        ExifInterface exif = null;

        try {
            exif = new ExifInterface(filepath);
        } catch (IOException var4) {
            ;
        }

        if (exif != null) {
            int orientation = exif.getAttributeInt("Orientation", -1);
            if (orientation != -1) {
                switch (orientation) {
                    case 3:
                        degree = 180;
                    case 4:
                    case 5:
                    case 7:
                    default:
                        break;
                    case 6:
                        degree = 90;
                        break;
                    case 8:
                        degree = 270;
                }
            }
        }

        return degree;
    }

}
