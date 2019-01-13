package com.cheweibao.liuliu.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by myx on 2018/3/23.
 */

public class TimeFormatUtils {
    /**
     * 将毫秒值转换为日期
     *
     * @param @param  ms
     * @param @return
     * @return
     * @描述: TODO
     * @作者： Ace
     * @创建时间： 2014-11-13 下午5:57:58
     */
    public static String msToDate(String ms) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            long now = Long.parseLong(ms);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(now);
            return formatter.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String msToDateAccurateToMonth(String ms) {
        DateFormat formatter = new SimpleDateFormat("yyyy年MM月");
        try {
            long now = Long.parseLong(ms);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(now);
            return formatter.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String msToDateAccurateToDay(String ms) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            long now = Long.parseLong(ms);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(now);
            return formatter.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String msToDateAccurateToDay(long ms) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(ms);
            return formatter.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String msToDateAccurateToDayCN(long ms) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(ms);
            return formatter.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String msToDateAccurateToYD(long ms) {
        DateFormat formatter = new SimpleDateFormat("MM月dd日");
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(ms);
            return formatter.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String msToDateAccurateToSecond(String ms) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            long now = Long.parseLong(ms);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(now);
            return formatter.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static int msToHour(String ms) {
        try {
            long now = Long.parseLong(ms);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(now);
            return calendar.get(Calendar.HOUR_OF_DAY);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static int msToDayOfWeek(String ms) {
        try {
            long now = Long.parseLong(ms);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(now);
            return calendar.get(Calendar.DAY_OF_WEEK);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static int parseInt(String str) {
        try {
            int num = Integer.parseInt(str);
            return num;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    /**
     * @return 2 //把String转化为double
     */

    public static double convertToDouble(String number, double defaultValue) {
        if (TextUtils.isEmpty(number)) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(number);
        } catch (Exception e) {
            return defaultValue;
        }

    }
    /**
     * @param d
     * @return 2位小数的double
     */
    public static String doubleToShowString(double d) {
        BigDecimal b = new BigDecimal(d);
        return b.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    }
    /**
     * @param d
     * @return 无小数的double
     */
    public static String doubleToIntString(double d) {
        BigDecimal b = new BigDecimal(d);
        return b.setScale(0, BigDecimal.ROUND_HALF_UP).toString();
    }
    /**
     * 判断字符串是否全部为字母
     *
     * @param
     * @return
     */
    public static final boolean judgeLetter(String str) {
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            int temp = chars[i];
            if ((temp < 97 || temp > 122) && (temp < 65 || temp > 90)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字符串是否全部为数字
     *
     * @param
     * @return
     */
    public static final boolean judgeNumber(String str) {
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            int temp = chars[i];
            if (temp < 48 || temp > 57) {
                return false;
            }
        }
        return true;
    }

    static DecimalFormat format = new DecimalFormat(",###,##0.00");
    static DecimalFormat format1 = new DecimalFormat(",###,##0.##");
    static DecimalFormat format2 = new DecimalFormat(",###,###");

    public static String parseMoney(String str) {
        if (TextUtils.isEmpty(str)) {
            return "0.00";
        }
        return format.format(Double.parseDouble(str));
    }

    public static String parseMoney(double d) {
        return format.format(d);
    }


    public static String parseMoneyShort(String str) {
        if (TextUtils.isEmpty(str)) {
            return "0";
        }
        return format1.format(Double.parseDouble(str));
    }


    public static String parseMoneyNoZero(String str) {
        if (TextUtils.isEmpty(str)) {
            return "0.00";
        }
        return format2.format(Float.parseFloat(str));
    }


    public static boolean isIntergerStr(String str) {
        Pattern pattern = Pattern.compile("^(0|[1-9][0-9]*)$");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }


    public final static String MD5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //umeng
    public static String getDeviceInfo(Context context) {
        try {
            org.json.JSONObject json = new org.json.JSONObject();
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);

            String device_id = tm.getDeviceId();

            android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);

            String mac = wifi.getConnectionInfo().getMacAddress();
            json.put("mac", mac);

            if (TextUtils.isEmpty(device_id)) {
                device_id = mac;
            }

            if (TextUtils.isEmpty(device_id)) {
                device_id = android.provider.Settings.Secure.getString(
                        context.getContentResolver(),
                        android.provider.Settings.Secure.ANDROID_ID);
            }

            json.put("device_id", device_id);

            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //ForReapal
    public static String getDeviceInfoForReapal(Context context) {
        try {
            String json = "";
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);

            String device_id = tm.getDeviceId();

            android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);

            String mac = wifi.getConnectionInfo().getMacAddress();
            json+= mac;

            if (TextUtils.isEmpty(device_id)) {
                device_id = mac;
            }

            if (TextUtils.isEmpty(device_id)) {
                device_id = android.provider.Settings.Secure.getString(
                        context.getContentResolver(),
                        android.provider.Settings.Secure.ANDROID_ID);
            }

            json += device_id;

            return json;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getDeviceDeviceUID(Context context) {
        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String device = deviceUuid.toString();
        return device;
    }

    /**
     * 是否有网络
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return false;
        } else {
            // 打印所有的网络状态
            NetworkInfo[] infos = cm.getAllNetworkInfo();
            if (infos != null) {
                for (int i = 0; i < infos.length; i++) {
                    // Log.d(TAG, "isNetworkAvailable - info: " +
                    // infos[i].toString());
                    if (infos[i].getState() == NetworkInfo.State.CONNECTED) {
                        //Log.d(TAG, "isNetworkAvailable -  I " + i);
                    }
                }
            }

            // 如果仅仅是用来判断网络连接　　　　　　
            // 则可以使用 cm.getActiveNetworkInfo().isAvailable();
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null) {
               /* Log.d(TAG,
                        "isNetworkAvailable - 是否有网络： "
                                + networkInfo.isAvailable());*/
            } else {
                //Log.d(TAG, "isNetworkAvailable - 完成没有网络！");
                return false;
            }

            // 1、判断是否有3G网络
            if (networkInfo != null
                    && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                //Log.d(TAG, "isNetworkAvailable - 有3G网络");
                return true;
            } else {
                //Log.d(TAG, "isNetworkAvailable - 没有3G网络");
            }

            // 2、判断是否有wifi连接
            if (networkInfo != null
                    && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                //Log.d(TAG, "isNetworkAvailable - 有wifi连接");
                return true;
            } else {
                //Log.d(TAG, "isNetworkAvailable - 没有wifi连接");
            }
        }
        return false;
    }
}
