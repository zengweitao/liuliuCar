package com.cheweibao.liuliu.common;

import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by sjk on 2017/1/3.
 */

public class ToastUtil {
    private static Toast toast;

    public static void showToast(String text) {
        if (toast == null) {
            toast = Toast.makeText(MyGlobal.context, text, Toast.LENGTH_SHORT);
        } else {
            toast.setText(text);
        }
        toast.show();
    }

    public static void showLongToast(String text) {
        if (toast == null) {
            toast = Toast.makeText(MyGlobal.context, text, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else {
            toast.setText(text);
            toast.setGravity(Gravity.CENTER, 0, 0);
        }
        toast.show();
    }
}
