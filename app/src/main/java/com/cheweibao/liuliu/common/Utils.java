package com.cheweibao.liuliu.common;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.MediaRecorder;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.ColorInt;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.alibaba.fastjson.JSONObject;
import com.cheweibao.liuliu.R;
import com.cheweibao.liuliu.data.UserInfo;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    static public final int SLIDE_CYCLE_S = 3000;

    static public final String PREF_KEY = "cheweibao";


    public final static String IT_KEY_1 = "IT_KEY_1";
    public final static String IT_KEY_2 = "IT_KEY_2";
    public final static String IT_KEY_3 = "IT_KEY_3";


    /**
     * getScreenDensity
     * get density of LCD screen
     *
     * @param context - Context object
     * @return int - screen height
     */
    public static float getScreenDensity(Context context) {

        DisplayMetrics dMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dMetrics);

        return dMetrics.density;
    }

    //Get ImageFile's Rotation
    public synchronized static int GetExifOrientation(String filepath) {
        int degree = 0;
        ExifInterface exif = null;

        try {
            exif = new ExifInterface(filepath);
        } catch (IOException e) {
            Log.e("FlipBook", "cannot read exif");
            e.printStackTrace();
        }

        if (exif != null) {
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);

            if (orientation != -1) {
                // We only recognize a subset of orientation tag values.
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                }
            }
        }

        return degree;
    }

    //Get Bitmap By degrees
    public synchronized static Bitmap GetRotatedBitmap(Bitmap bitmap, int degrees) {

        if (degrees != 0 && bitmap != null) {
            Matrix m = new Matrix();
            m.setRotate(degrees, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);
            try {
                Bitmap b2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
                if (bitmap != b2) {
                    bitmap.recycle();
                    bitmap = b2;
                }
            } catch (OutOfMemoryError ex) {
                // We have no memory to rotate. Return the original bitmap.
            }
        }
        return bitmap;
    }


    //Get Bitmap From FilePath
    public synchronized static Bitmap getSafeDecodeBitmapAndAdjustXYWithRGB_565(
            String strFilePath, int maxSize, float viewX, float viewY) {
        try {
            // Max image size
            int IMAGE_MAX_SIZE = maxSize;

            File file = new File(strFilePath);
            if (file.exists() == false) {
                // DEBUG.SHOW_ERROR(TAG,
                // "[ImageDownloader] SafeDecodeBitmapFile : File does not exist !!");
                return null;
            }

            BitmapFactory.Options bfo = new BitmapFactory.Options();
            bfo.inPreferredConfig = Bitmap.Config.RGB_565;
            //bfo.inJustDecodeBounds = true;

            BitmapFactory.decodeFile(strFilePath, bfo);

            if (IMAGE_MAX_SIZE > 0)
                if (bfo.outHeight * bfo.outWidth >= IMAGE_MAX_SIZE
                        * IMAGE_MAX_SIZE) {
                    bfo.inSampleSize = (int) Math.pow(
                            2,
                            (int) Math.round(Math.log(IMAGE_MAX_SIZE
                                    / (double) Math.max(bfo.outHeight,
                                    bfo.outWidth))
                                    / Math.log(0.5)));
                }
            bfo.inJustDecodeBounds = false;
            bfo.inPurgeable = true;
            bfo.inDither = true;

            final Bitmap _bmpOriginal = BitmapFactory.decodeFile(strFilePath, bfo);

            int degree = GetExifOrientation(strFilePath);

            float _fSrcWidth = _bmpOriginal.getWidth();
            float _fSrcHeight = _bmpOriginal.getHeight();

            float _fSrcAspect = _fSrcHeight / _fSrcWidth;
            float _fDstAspect = viewY / viewX;
            if (_fSrcAspect > _fDstAspect) {
                return GetRotatedBitmap(zoomBitmapByHeight(_bmpOriginal, (int) viewY), degree);
            } else {
                return GetRotatedBitmap(zoomBitmapByWidth(_bmpOriginal, (int) viewX), degree);
            }
        } catch (OutOfMemoryError ex) {
            ex.printStackTrace();
            return null;
        }
    }

    //Bitmap zooms by Width
    public static final Bitmap zoomBitmapByWidth(Bitmap bm, int newWidth) {

        int width = bm.getWidth();
        int height = bm.getHeight();

        if (width >= newWidth)
            return bm;

        float aspect = (float) width / height;
        float scaleWidth = newWidth;
        float scaleHeight = scaleWidth / aspect;        // yeah!

        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth / width, scaleHeight / height);
        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        /*bm.recycle();*/
        if (resizedBitmap != bm) {
            bm.recycle();
            bm = null;
        }
        return resizedBitmap;
    }

    //Bitmap zooms by Height
    public static final Bitmap zoomBitmapByHeight(Bitmap bm, int newHeight) {

        try {
            int width = bm.getWidth();
            int height = bm.getHeight();

            if (height >= newHeight)
                return bm;

            float aspect = (float) width / height;
            float scaleHeight = newHeight;
            float scaleWidth = scaleHeight * aspect; // yeah!

            // create a matrix for the manipulation
            Matrix matrix = new Matrix();
            // resize the bit map
            matrix.postScale(scaleWidth / width, scaleHeight / height);
            // recreate the new Bitmap
            Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
                    matrix, true);
            /* bm.recycle(); */
            if (resizedBitmap != bm) {
                bm.recycle();
                bm = null;
            }
            return resizedBitmap;
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    //Get Bitmap From File
    public synchronized static Bitmap getBitmapWithRectWithRotation(String strFilePath, int x, int y, int nDstWidth, int nDstHeight, int rotation, int maxSize) {

        try {
            // Max image size
            int IMAGE_MAX_SIZE = maxSize;

            File file = new File(strFilePath);
            if (file.exists() == false) {
                return null;
            }

            //get Bitamp Width, Height
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(strFilePath, options);
            int m_nOrgBitmapW = options.outWidth;
            int m_nOrgBitmapH = options.outHeight;

            //if rotation is 90 or 270, exchange Width and Height
            if (rotation == 90
                    || rotation == 270) {
                int tmpVal = m_nOrgBitmapW;
                m_nOrgBitmapW = m_nOrgBitmapH;
                m_nOrgBitmapH = tmpVal;
            }

            BitmapFactory.Options bfo = new BitmapFactory.Options();
            bfo.inJustDecodeBounds = true;

            BitmapFactory.decodeFile(strFilePath, bfo);

            if (IMAGE_MAX_SIZE > 0)
                if (bfo.outHeight * bfo.outWidth >= IMAGE_MAX_SIZE * IMAGE_MAX_SIZE) {
                    bfo.inSampleSize = (int) Math.pow(2, (int) Math.round(Math.log(IMAGE_MAX_SIZE
                            / (double) Math.max(bfo.outHeight, bfo.outWidth)) / Math.log(0.5)));
                }
            bfo.inJustDecodeBounds = false;
            bfo.inPurgeable = true;
            bfo.inDither = true;

            Bitmap bitmap = BitmapFactory.decodeFile(strFilePath, bfo);
            if (bitmap == null)
                return null;

            bitmap = GetRotatedBitmap(bitmap, rotation);
            float m_fBitHRate = (float) bitmap.getHeight() / (float) m_nOrgBitmapH;
            //float m_fBitWRate = (float)m_nOrgBitmapW / (float)m_nBitmapW;

            if (bitmap == null)
                return null;

            int cropX = Math.max((int) ((float) x * (float) m_fBitHRate), 0);
            int cropY = Math.max((int) ((float) y * (float) m_fBitHRate), 0);
            int cropW = Math.min((int) ((float) nDstWidth * (float) m_fBitHRate), (bitmap.getWidth() - cropX));
            int cropH = Math.min((int) ((float) nDstHeight * (float) m_fBitHRate), (bitmap.getHeight() - cropY));

            Bitmap bmpResult = Bitmap.createBitmap(bitmap, cropX, cropY, cropW, cropH);

            if (bitmap != bmpResult) {
                bitmap.recycle();
                bitmap = null;
            }

            return bmpResult;
        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return null;
    }

    //Save Bitmap To File
    public static String saveBitmap2File(Bitmap bmp, String strTargetFile,
                                         Bitmap.CompressFormat compressFormat) {

        File file = new File(strTargetFile);
        try {
            FileOutputStream out = new FileOutputStream(file);
            // MyMedia.getViewBitmap(mCapturedImageLayout).compress(CompressFormat.JPEG,100,
            // out);
            bmp.compress(compressFormat, 100, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        String imagePath = file.getAbsolutePath();

        return imagePath;
    }

    private static Bitmap compressImage(Bitmap image) {
//        int zoomW = 100;
//        int zoomH = 100;
//        int zoom = 100;
//        zoomH = 840 * 100 / image.getWidth();
//        zoomW = 640 * 100 / image.getHeight();
//        if (image.getWidth() > 840 || image.getHeight() > 640) {
//            zoom = Math.min(zoomH, zoomW);
//            if (zoom == 0) zoom = 1;
//        }
//
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        image.compress(Bitmap.CompressFormat.JPEG, zoom, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
////		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
////		int options = 100;
////		while ( baos.toByteArray().length / 1024>1024) {  //循环判断如果压缩后图片是否大于100MB,大于继续压缩
////			baos.reset();//重置baos即清空baos
////			image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
////			options -= 10;//每次都减少5
////		}
//        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
//        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return image;
    }

    private static Bitmap imageZoom(Bitmap image) {
        Bitmap bitmap = image;
        //图片允许最大空间   单位：KB
        double maxSize = 300.00;//500
        //将bitmap放至数组中，意在bitmap的大小（与实际读取的原文件要大）
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        //将字节换成KB
        double mid = b.length / 1024;
        //判断bitmap占用空间是否大于允许最大空间  如果大于则压缩 小于则不压缩
        if (mid > maxSize) {
            //获取bitmap大小 是允许最大大小的多少倍
            double i = mid / maxSize;
            //开始压缩  此处用到平方根 将宽带和高度压缩掉对应的平方根倍 （1.保持刻度和高度和原bitmap比率一致，压缩后也达到了最大大小占用空间的大小）
            bitmap = zoomImage(image, image.getWidth() / Math.sqrt(i),
                    image.getHeight() / Math.sqrt(i));
        }
        return bitmap;
    }

    /***
     * 图片的缩放方法
     *
     * @param bgimage
     *            ：源图片资源
     * @param newWidth
     *            ：缩放后宽度
     * @param newHeight
     *            ：缩放后高度
     * @return
     */
    public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
                                   double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                (int) height, matrix, true);
        return bitmap;
    }

    private static Bitmap getComoressedImage(String path) {
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        return imageZoom(bitmap);//压缩好比例大小后再进行质量压缩
    }

    public static boolean compressBitmapFile(String path, String target) {
        return saveBitmapToFile(getComoressedImage(path), target);
    }

    public static boolean saveBitmapToFile(Bitmap photo, String path) {
        OutputStream fOut = null;
        File f = new File(path);
        if (f.exists()) f.delete();

        try {
            fOut = new FileOutputStream(path);
            photo.compress(CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    ////////////////////////////////

    static public String getStrFromObj(Object obj) {

        String ret = "";
        if (obj != null) {
            try {
                ret = obj.toString();
            } catch (NumberFormatException nfe) {
                ret = "";
            } catch (Exception e) {
                ret = "";
            }
        }
        return ret;
    }

    static public int getIntFromString(String str) {
        int ret = 0;
        if (str != null) {
            try {
                ret = Integer.parseInt(str);
            } catch (NumberFormatException nfe) {
                ret = 0;
            }
        }
        return ret;
    }

    static public int getIntFromObj(Object obj) {
        int ret = 0;
        if (obj == null) return ret;
        if (obj != null) {
            try {
                ret = Integer.parseInt(obj.toString());
            } catch (NumberFormatException nfe) {
                ret = 0;
            }
        }
        return ret;
    }

    static public long getLongFromObj(Object obj) {
        long ret = 0;
        if (obj == null) return ret;
        if (obj != null) {
            try {
                ret = Long.parseLong(obj.toString());
            } catch (NumberFormatException nfe) {
                ret = 0;
            }
        }
        return ret;
    }

    static public String getStringFromObj(Object obj) {
        String ret = "";
        if (obj == null) return "";
        try {
            ret = obj.toString();
        } catch (Exception e) {
            ret = "";
        }
        return ret;
    }

    public static String TimeStampDate(Object timestampObj, String format) {
        String timestampString = getStringFromObj(timestampObj);
        if (timestampString.trim().equals("")) return "";
        Long timestamp = Long.parseLong(timestampString);
        String date = new SimpleDateFormat(format).format(new Date(timestamp));
        return date;
    }

    public static String TimeStampDate(String timestampString, String format) {
        if (isEmpty(timestampString)) return "";
        Long timestamp = Long.parseLong(timestampString);
        String date = new SimpleDateFormat(format).format(new Date(timestamp));
        return date;
    }

    public static Date TimeStamp2Date(String timestampString) {
        if (isEmpty(timestampString)) return null;
        Timestamp ts = new Timestamp(getLongFromString(timestampString));
        Date date = new Date();
        try {
            date = ts;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return date;
    }

    public static String getFormatDate(String dateString, String formatType) {
        if (isEmpty(dateString)) return null;
        Date date = getDateFromString(dateString);
        String formatedDateString = "";
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(formatType);
            formatedDateString = formatter.format(date);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return formatedDateString;
    }

    public static String getFormatDate1(String dateString, String formatType) {
        if (isEmpty(dateString)) return null;
        Date date = getDateFromString1(dateString);
        String formatedDateString = "";
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(formatType);
            formatedDateString = formatter.format(date);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return formatedDateString;
    }

    public static String getChinaFormatYearMonth(String dateString, String curDateString, String formatType) {

        String resultString = "";
        try {
            SimpleDateFormat format = new SimpleDateFormat(formatType);
            Date date = format.parse(dateString);
            Date curDate = format.parse(curDateString);
            Calendar cal = Calendar.getInstance();
            Calendar curCal = Calendar.getInstance();
            cal.setTime(date);
            curCal.setTime(curDate);
            if (cal.get(Calendar.YEAR) == curCal.get(Calendar.YEAR)) {
                if (cal.get(Calendar.MONTH) == curCal.get(Calendar.MONTH))
                    resultString = "本月";
                else
                    resultString = (cal.get(Calendar.MONTH) + 1) + "月";
            } else {
                resultString = getFormatDate(dateString, "yyyy年M月");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultString;
    }

    public static long getTodayTimestamp() {
        long timestamp = 0;

        //DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = new Date();
            Timestamp ts = Timestamp.valueOf(sdf.format(date) + " 00:00:00");
            timestamp = ts.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return timestamp;
    }

    public static long getNowTimestamp() {
        long timestamp = 0;

        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = new Date();
            Timestamp ts = Timestamp.valueOf(sdf.format(date));
            timestamp = ts.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return timestamp;
    }

    static public long getLongFromString(String str) {
        long ret = 0;
        if (str != null) {
            try {
                ret = Long.parseLong(str);
            } catch (NumberFormatException nfe) {
                ret = 0;
            }
        }
        return ret;
    }

    static public float getFloatFromString(String str) {
        float ret = 0;
        if (str != null) {
            try {
                ret = Float.parseFloat(str);
            } catch (NumberFormatException nfe) {
                ret = 0;
            }
        }
        return ret;
    }

    static public double getDoubleFromString(String str) {
        double ret = 0;
        if (str != null) {
            try {
                ret = Double.parseDouble(str);
            } catch (NumberFormatException nfe) {
                ret = 0;
            }
        }
        return ret;
    }

    static public long getHexFromString(String str) {
        long ret = 0;
        if (str != null) {
            try {
                ret = Long.decode("0x" + str).intValue();
            } catch (NumberFormatException nfe) {
                ret = 0;
            }
        }
        return ret;
    }

    static public String getString(double value, int digit) {
        DecimalFormat df = new DecimalFormat("##########.########");
        String str1 = df.format(value);
        int pos = str1.indexOf(".");
        if (pos < 0) return str1;
        String back = str1.substring(pos + 1);
        if (getLongFromString(back) == 0) {
            String str2 = "";
            if (digit > 0) str2 = ".";
            for (int i = 0; i < digit; i++) str2 += "0";
            str1 = str1.substring(0, pos) + str2;
        } else {
            if (str1.length() > pos + digit + 1) {
                str1 = str1.substring(0, Math.min(pos + digit + 1, str1.length()));
            }
        }
        return str1;
    }

    public static int getResourceId(Context context, String pVariableName, String pResourcename, String pPackageName) {
        if (context == null) return -1;
        try {
            return context.getResources().getIdentifier(pVariableName, pResourcename, pPackageName);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    static public void DeleteRecursive(File fileOrDirectory, boolean root) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                DeleteRecursive(child, false);

        if (!root) {
//	    	if(!fileOrDirectory.getName().equals("dating_back.jpg")){
            fileOrDirectory.delete();
//	    	}
        }
    }

    static public void DeleteFile(String filePath) {
        if (isEmpty(filePath)) return;

        try {
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
        }
    }

    static public boolean checkAlreadyDowned(String path) {
        File file = new File(path);
        if (file.exists()) {
            return true;
        }
        return false;
    }

    static public boolean checkDirExist(String path) {
        File dir = new File(path);
        if (dir.exists() && dir.isDirectory()) {
            return true;
        } else return false;
    }

    static public void copy(String src, String dst) throws IOException {
        File src_file = new File(src);
        File dst_file = new File(dst);
        if (dst_file.exists()) dst_file.delete();

        InputStream in = new FileInputStream(src_file);
        OutputStream out = new FileOutputStream(dst_file);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    static public void move(String src, String dst) throws IOException {
        File src_file = new File(src);
        File dst_file = new File(dst);

        if (dst_file.exists()) dst_file.delete();
        if (src_file.exists())
            src_file.renameTo(dst_file);
    }

    static public void gotoHandler(int type, int state, String content, Handler h) {
        Message msg = h.obtainMessage();
        Bundle b = new Bundle();
        b.putInt("type", type);
        b.putInt("state", state);
        b.putString("content", content);
        msg.setData(b);
        h.sendMessage(msg);
    }

    static public int dp2Pixel(Context context, float dips) {
        if (context == null) return 0;
        return (int) (dips * context.getResources().getDisplayMetrics().density + 0.5f);
    }

    public static long daysBetween(Date dateEarly, Date dateLater) {
        return (dateLater.getTime() - dateEarly.getTime()) / (24 * 60 * 60 * 1000);
    }

    public static long secondsBetween(Date dateEarly, Date dateLater) {
        return (dateLater.getTime() - dateEarly.getTime()) / 1000;
    }

    public static Date getDateFromString(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(dateString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return convertedDate;
    }

    public static Date getDateFromString1(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(dateString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return convertedDate;
    }

    public static Date getTimeFromString1(String timeString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(timeString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return convertedDate;
    }

    public static String getCustomFormatDate(String strMilliSeconds) {
        long milliSeconds = getLongFromString(strMilliSeconds);
        Date newDate = new Date(milliSeconds);

        String interval = null;

        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date();

        long time = (curDate.getTime() - newDate.getTime()) / 1000;

        if (time < 86400) {
            sd = new SimpleDateFormat("HH:mm");
            interval = sd.format(newDate);
        } else if (time < 86400 * 365) {
            sd = new SimpleDateFormat("M月d曰");
            interval = sd.format(newDate);
        } else {
            sd = new SimpleDateFormat("yyyy-MM-dd");
            interval = sd.format(newDate);
        }

        return interval;
    }

    /**
     * format = "yyyy-MM-dd HH:mm:ss"
     *
     * @param formattedDate : 今天HH:mm, 昨天HH:mm, MM:dd HH:mm
     * @return
     */
    public static String getCustomFormatDate2(String formattedDate) {

        String formatDate = "";
        if (formattedDate == null || formattedDate.toString().equals("")) {
            formatDate = "";
            return formatDate;
        }
        Calendar cal = Calendar.getInstance();
        Calendar curCal = Calendar.getInstance();
        cal.setTime(Utils.getDateFromString(formattedDate));
        if (cal.get(Calendar.YEAR) == curCal.get(Calendar.YEAR)
                && cal.get(Calendar.MONTH) == curCal.get(Calendar.MONTH)
                && cal.get(Calendar.DAY_OF_MONTH) == curCal.get(Calendar.DAY_OF_MONTH)) {
            formatDate = "今天" + formattedDate.substring(11, 16);
        } else {
            cal.add(Calendar.DAY_OF_MONTH, 1);
            if (cal.get(Calendar.YEAR) == curCal.get(Calendar.YEAR)
                    && cal.get(Calendar.MONTH) == curCal.get(Calendar.MONTH)
                    && cal.get(Calendar.DAY_OF_MONTH) == curCal.get(Calendar.DAY_OF_MONTH)) {
                formatDate = "昨天" + formattedDate.substring(11, 16);
            } else {
                formatDate = formattedDate.substring(5, 16);
            }
        }
        return formatDate;
    }

    /**
     * Get formatted time string
     *
     * @param minutes : minute
     * @return : formatted string (ex 600 : 10小时, 1440 : 1天)
     */
    public static String getTimeStr(String minutes) {
        int minVal = Utils.getIntFromString(minutes);
        String retStr = "";
        int temp = 0;
        temp = minVal / (60 * 24);
        if (temp > 1) {
            retStr = temp + "天";
        }
        temp = minVal % (60 * 24);
        temp = temp / 60;
        if (temp > 1) {
            retStr += temp + "小时";
        }
        if (minVal % 60 > 0) {
            retStr += (minVal % 60) + "分钟";
        }
        return retStr;
    }

    public static long getTimeDiff(String big, String small) {
        long timeDiff = 0L;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            long preTime = dateFormat.parse(small).getTime();
            long curTime = dateFormat.parse(big).getTime();
            timeDiff = Math.abs(curTime - preTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeDiff;
    }

    static public boolean isValidDate(int year, int month, int day) {

        boolean bResult = true;

        int[] end = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

        if ((year % 4) == 0) end[1] = 29;

        if (day > end[month - 1])
            bResult = false;

        return bResult;
    }

    /**
     * 是否连接上网
     *
     * @param context
     * @return
     */
    public static boolean canConnect(Context context, boolean wifiOnly) {
        if (context == null) return false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        boolean canConnect = false;

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            if (networkInfo.isConnected()) {
                if ((networkInfo.getType() == ConnectivityManager.TYPE_WIFI) ||
                        (networkInfo.getType() != ConnectivityManager.TYPE_WIFI && !wifiOnly)) {
                    canConnect = true;
                }
            }
        }

        return canConnect;
    }

    static public void displayMemoryUsage(String message) {
        int usedKBytes = (int) (Debug.getNativeHeapAllocatedSize() / 1024L);
        // String usedMegsString = String.format("%s - usedMemory = Memory Used: %d KB", message, usedKBytes);
        // Log.w("微三云", usedMegsString);
    }

    public static boolean isValidEmail(String emailAddress) {
        return emailAddress.contains(" ") == false && emailAddress.matches(".+@.+\\.[a-z]+");
    }

    public static void setCachePath(Context context) {
        if (context == null) return;
        MyGlobal myglobal = (MyGlobal) context.getApplicationContext();

        String extpath = getCardDirectory(context);
        if (extpath == null || extpath.equals("")) {
            extpath = context.getFilesDir().getAbsolutePath();
        }

        File dir = new File(extpath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        myglobal.cache_path = extpath + "/cheweibao/";
        dir = new File(myglobal.cache_path);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        myglobal.temp_path = myglobal.cache_path + "temp/";
        dir = new File(myglobal.temp_path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public static String getCardDirectory(Context context) {
        String path = "";
        /*String tf_path = getMicroSDCardDirectory();
        if(tf_path==null || tf_path.length() == 0 || !tf_path.startsWith("/storage/")){*/
        File SDCardRoot = Environment.getExternalStorageDirectory().getAbsoluteFile();
        if (SDCardRoot.exists()) {
            path = SDCardRoot.getAbsolutePath();
        } else {
            File extFolder = context.getExternalFilesDir(null);
            if (extFolder != null) { // none of the external card (internal path)
                if (extFolder.exists()) {
                    path = Environment.getExternalStorageDirectory().getAbsolutePath();
                } else {
                    if (extFolder.mkdirs()) //  available (external path)
                        path = Environment.getExternalStorageDirectory().getAbsolutePath();
                }
            }
        }
        /*}
        else{
			path = tf_path;
		}*/
        return path;
    }

    public static String getMicroSDCardDirectory() {
        InputStreamReader inputStream = null;
        BufferedReader reader = null;
        try {
            Process dfProcess = Runtime.getRuntime().exec("df");
            String string = null;
            String[] column;
            final int indexOfFileSystem = 0;
            final int indexOfSize = 1;
            inputStream = new InputStreamReader(dfProcess.getInputStream());
            reader = new BufferedReader(inputStream);
            long startTime = System.currentTimeMillis();
            long endTime = startTime;
            while (true) {
                if (!reader.ready()) {
                    endTime = System.currentTimeMillis();
                    if (endTime - startTime <= 100) continue;
                    break;
                }
                string = reader.readLine();
                if (string == null) break;
                column = string.split("[ \t]+");
                if (column.length < 4) continue;
                if (column[indexOfSize].toUpperCase().contains("G") == false) continue;
                if (isAvailableFileSystem(column[indexOfFileSystem]) == true)
                    return column[indexOfFileSystem];
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                inputStream = null;
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private static boolean isAvailableFileSystem(String fileSystemName) {
        final String[] unAvailableFileSystemList = {"/dev", "/mnt/asec", "/mnt/obb", "/system", "/data", "/cache", "/efs"};
        for (String name : unAvailableFileSystemList) {
            if (fileSystemName.contains(name) == true) return false;
        }
        if (Environment.getExternalStorageDirectory().getAbsolutePath().equals(fileSystemName) == true)
            return false;
        return true;
    }

    public static String getSavePath(Context context) {
        if (context == null) return "";
        MyGlobal myglobal = (MyGlobal) context.getApplicationContext();

        String path = myglobal.cache_path + "file_recv/";
        File dir = new File(path);
        if (!dir.exists()) dir.mkdirs();
        return path;
    }

    private List<File> getListFiles(File parentDir) {
        ArrayList<File> inFiles = new ArrayList<File>();
        File[] files = parentDir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                inFiles.addAll(getListFiles(file));
            } else {
                inFiles.add(file);
            }
        }
        return inFiles;
    }

    public static void setDisPlayMetrics(Context context, Activity activity) {
        if (context == null) return;
        MyGlobal myglobal = (MyGlobal) context.getApplicationContext();
        DisplayMetrics dMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dMetrics);

        myglobal.SCR_WIDTH = Math.min(dMetrics.widthPixels, dMetrics.heightPixels);
        myglobal.SCR_HEIGHT = Math.max(dMetrics.widthPixels, dMetrics.heightPixels);
        myglobal.SCR_DENSITY = dMetrics.density;
    }

    public static void setupUnits(Context context) {
        if (context == null) return;
    }

    public static void setupUnits(Context context, Activity activity) {
        if (context == null) return;
        setDisPlayMetrics(context, activity);
    }

//	public static void saveBitmapToFile(Bitmap photo, String path){
//    	OutputStream fOut = null;
//        File f = new File(path);
//        if(f.exists()) f.delete();
//
//        try {
//			fOut = new FileOutputStream(path);
//			photo.compress(CompressFormat.JPEG, 70, fOut);
//            fOut.flush();
//            fOut.close();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//    }

    public static boolean isEmpty(String s) {
        if (null == s)
            return true;
        if (s.length() == 0)
            return true;
        if (s.trim().length() == 0)
            return true;
        return false;
    }

    // 校验Tag Alias 只能是数字,英文字母和中文
    public static boolean isValidTagAndAlias(String s) {
        Pattern p = Pattern.compile("^[\u4E00-\u9FA50-9a-zA-Z_-]{0,}$");
        Matcher m = p.matcher(s);
        return m.matches();
    }

    // 取得AppKey
    public static String getAppKey(Context context) {
        if (context == null) return "";
        Bundle metaData = null;
        String appKey = null;
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            if (null != ai)
                metaData = ai.metaData;
            if (null != metaData) {
                appKey = metaData.getString("JPUSH_APPKEY");
                if ((null == appKey) || appKey.length() != 24) {
                    appKey = null;
                }
            }
        } catch (NameNotFoundException e) {

        }
        return appKey;
    }

    // 取得版本号
    public static String GetVersion(Context context) {
        if (context == null) return "";
        try {
            PackageInfo manager = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            return manager.versionName;
        } catch (NameNotFoundException e) {
            return "Unknown";
        }
    }

    public static int GetVersionCode(Context context) {
        if (context == null) return 0;
        try {
            PackageInfo manager = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            return manager.versionCode;
        } catch (NameNotFoundException e) {
            return 0;
        }
    }

    // 显示Toast
    public static void showToast(final String toast, final Context context) {
        if (context == null) return;
        Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
    }

    // 显示Toast
    public static void showToast(final String toast, final Context context, int duration) {
        if (context == null) return;
        Toast.makeText(context, toast, duration).show();
    }

    public static void showToast(final int strResId, final Context context) {
        if (context == null) return;
        try {
            Toast.makeText(context, strResId, Toast.LENGTH_SHORT).show();
        } catch (NotFoundException e) {

        }
    }

    public static void showDelayAlert(final String toast, final Context context, final int delay) {
        if (context == null) return;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                showAlertView(context, "提示", toast);
            }
        }, delay);
    }

    public static void showAlertView(Context context, String title, String content) {
        if (context == null) return;
        Activity mActivity = (Activity) context;
        while (mActivity.getParent() != null) {
            mActivity = mActivity.getParent();
        }
        try {
            new AlertDialog.Builder(mActivity)
                    .setTitle(title)
                    .setMessage(content)
                    .setCancelable(false)
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    }).create().show();
        } catch (Exception e) { // android.view.WindowManager$BadTokenException错误
            return;
        }
    }

    public static void showAlertView(Context context, String title, int contentResId) {
        if (context == null) return;
        Activity mActivity = (Activity) context;
        while (mActivity.getParent() != null) {
            mActivity = mActivity.getParent();
        }
        try {
            new AlertDialog.Builder(mActivity)
                    .setTitle(title)
                    .setMessage(contentResId)
                    .setCancelable(false)
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    }).create().show();
        } catch (Exception e) { // android.view.WindowManager$BadTokenException错误
            return;
        }
    }

    public static void showAlertView(Context context, String title, String content, String okString, DialogInterface.OnClickListener okListener) {
        if (context == null) return;
        Activity mActivity = (Activity) context;
        while (mActivity.getParent() != null) {
            mActivity = mActivity.getParent();
        }
        try {
            new AlertDialog.Builder(mActivity)
                    .setTitle(title)
                    .setMessage(content)
                    .setCancelable(false)
                    .setPositiveButton(okString, okListener).create().show();
        } catch (Exception e) { // android.view.WindowManager$BadTokenException错误
        }
    }

    public static void showQuestionView(Context context, String title, String content, String okString, String cancelString, DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener cancelListener) {
        if (context == null) return;
        Activity mActivity = (Activity) context;
        while (mActivity.getParent() != null) {
            mActivity = mActivity.getParent();
        }
        if (okListener == null) {
            okListener = new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    return;
                }
            };
        }
        if (cancelListener == null) {
            cancelListener = new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    return;
                }
            };
        }
        try {
            new AlertDialog.Builder(mActivity)
                    .setTitle(title)
                    .setMessage(content)
                    .setCancelable(false)
                    .setNegativeButton(cancelString, cancelListener)
                    .setPositiveButton(okString, okListener).create().show();
        } catch (Exception e) { // android.view.WindowManager$BadTokenException错误

        }
    }

    public static void showQuestionView(Context context, String title, int contentResId, String okString, String cancelString, DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener cancelListener) {
        if (context == null) return;
        Activity mActivity = (Activity) context;
        while (mActivity.getParent() != null) {
            mActivity = mActivity.getParent();
        }
        try {
            if (okListener == null) {
                okListener = new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                };
            }
            if (cancelListener == null) {
                cancelListener = new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                };
            }
            new AlertDialog.Builder(mActivity)
                    .setTitle(title)
                    .setMessage(contentResId)
                    .setCancelable(false)
                    .setNegativeButton(cancelString, cancelListener)
                    .setPositiveButton(okString, okListener).create().show();
        } catch (Exception e) { // android.view.WindowManager$BadTokenException错误
            return;
        }
    }

    static public Bitmap getBitmapFromImageFile(String strLocalFilePath, int width, int height) {
        Bitmap bitmap = null;

        File file = new File(strLocalFilePath);
        if (file.exists() == false) {
            return null;
        }

        if (width == 0 && height == 0) {
            try {
                bitmap = BitmapFactory.decodeFile(strLocalFilePath);
            } catch (OutOfMemoryError e) {
                bitmap = null;
                System.gc();
            }
        } else {
            try {
                BitmapFactory.Options o = new BitmapFactory.Options();
                o.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(new FileInputStream(file), null, o);

                //Find the correct scale value. It should be the power of 2.
                int scale = 1;
                while (o.outWidth / scale / 2 >= width && o.outHeight / scale / 2 >= height)
                    scale *= 2;

                //Decode with inSampleSize
                BitmapFactory.Options o2 = new BitmapFactory.Options();
                o2.inSampleSize = scale;
                o2.inPreferredConfig = Bitmap.Config.ARGB_8888;
                bitmap = BitmapFactory.decodeStream(new FileInputStream(file), null, o2);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (OutOfMemoryError e) {
                bitmap = null;
                System.gc();
            }
        }
        return bitmap;
    }

    public static boolean isValid(String str) {
        if (TextUtils.isEmpty(str)) return false;
        else return true;
    }

    public static boolean isLinkTypeValid(String str) {
        if (str == null || str.equals("") || getIntFromString(str) == 0) return false;
        else return true;
    }

    public static String randomString(final int length) {
        char[] chars = "abcdefghijklmnopqrstuvwxyz1234567980".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();
    }

    public static void moveToFront(Context context) {
        if (context == null) return;
        if (Build.VERSION.SDK_INT >= 11) { // honeycomb
            final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            final List<RunningTaskInfo> recentTasks = activityManager.getRunningTasks(Integer.MAX_VALUE);

            for (int i = 0; i < recentTasks.size(); i++) {
                // bring to front
                if (recentTasks.get(i).baseActivity.toShortString().indexOf(context.getPackageName()) > -1) {
                    activityManager.moveTaskToFront(recentTasks.get(i).id, ActivityManager.MOVE_TASK_WITH_HOME);
                }
            }
        }
    }

    public static void postRefreshComplete(final PullToRefreshListView lvList) {
        Handler handler3 = new Handler();
        handler3.postDelayed(new Runnable() {
            public void run() {
                lvList.onRefreshComplete();
            }
        }, 500);
    }

    public static void postRefreshComplete(final PullToRefreshScrollView svScrollView) {
        Handler handler3 = new Handler();
        handler3.postDelayed(new Runnable() {
            public void run() {
                svScrollView.onRefreshComplete();
            }
        }, 500);
    }

    public static void postRefreshComplete(final PullToRefreshGridView svScrollView) {
        Handler handler3 = new Handler();
        handler3.postDelayed(new Runnable() {
            public void run() {
                svScrollView.onRefreshComplete();
            }
        }, 500);
    }

    public static boolean isPackageInstalled(Context context, String packagename) {
        if (context == null) return false;
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    public static void galleryAddPic(Context context, String path) {
        if (context == null) return;
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(path);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    public static boolean getBooleanPreferences(Context context, String name) {
        if (context == null) return false;
        int mode = Activity.MODE_PRIVATE;

        SharedPreferences mySharedPreferences = context.getSharedPreferences(PREF_KEY, mode);
        if (mySharedPreferences != null) {
            return mySharedPreferences.getBoolean(name, false);
        }
        return false;
    }

    public static boolean getBooleanPreferences1(Context context, String name) {
        if (context == null) return true;
        int mode = Activity.MODE_PRIVATE;

        SharedPreferences mySharedPreferences = context.getSharedPreferences(PREF_KEY, mode);
        if (mySharedPreferences != null) {
            return mySharedPreferences.getBoolean(name, true);
        }
        return true;
    }

    public static void putBooleanPreferences(Context context, String name, boolean value) {
        if (context == null) return;
        int mode = Activity.MODE_PRIVATE;

        SharedPreferences mySharedPreferences = context.getSharedPreferences(PREF_KEY, mode);
        if (mySharedPreferences != null) {
            SharedPreferences.Editor editor = mySharedPreferences.edit();
            editor.putBoolean(name, value);
            editor.commit();
        }
    }

    public static String getStringPreferences(Context context, String name) {
        if (context == null) return "";
        int mode = Activity.MODE_PRIVATE;

        SharedPreferences mySharedPreferences = context.getSharedPreferences(PREF_KEY, mode);
        if (mySharedPreferences != null) {
            return mySharedPreferences.getString(name, "");
        }
        return "";
    }

    public static void putStringPreferences(Context context, String name, String value) {
        if (context == null) return;
        int mode = Activity.MODE_PRIVATE;

        SharedPreferences mySharedPreferences = context.getSharedPreferences(PREF_KEY, mode);
        if (mySharedPreferences != null) {
            SharedPreferences.Editor editor = mySharedPreferences.edit();
            editor.putString(name, value);
            editor.commit();
        }
    }

    public static int getIntPreferences(Context context, String name) {
        if (context == null) return 0;
        int mode = Activity.MODE_PRIVATE;

        SharedPreferences mySharedPreferences = context.getSharedPreferences(PREF_KEY, mode);
        if (mySharedPreferences != null) {
            return mySharedPreferences.getInt(name, 0);
        }
        return 0;
    }

    public static void putIntPreferences(Context context, String name, int value) {
        if (context == null) return;
        int mode = Activity.MODE_PRIVATE;

        SharedPreferences mySharedPreferences = context.getSharedPreferences(PREF_KEY, mode);
        if (mySharedPreferences != null) {
            SharedPreferences.Editor editor = mySharedPreferences.edit();
            editor.putInt(name, value);
            editor.commit();
        }
    }

    // 获取AppKey
    public static String getMetaValue(Context context, String metaKey) {
        if (context == null) return "";
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.getString(metaKey);
            }
        } catch (NameNotFoundException e) {

        }
        return apiKey;
    }

    /**
     * 对double数据进行取精度.
     *
     * @param value        double数据.
     * @param scale        精度位数(保留的小数位数).
     * @param roundingMode 精度取值方式.
     * @return 精度计算后的数据.
     */
    public static double round(double value, int scale, int roundingMode) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(scale, roundingMode);
        double d = bd.doubleValue();
        bd = null;
        return d;
    }

    /**
     * double 相加
     *
     * @param d1
     * @param d2
     * @return
     */
    public static double sum(double d1, double d2) {
        BigDecimal bd1 = new BigDecimal(Double.toString(d1));
        BigDecimal bd2 = new BigDecimal(Double.toString(d2));
        return bd1.add(bd2).doubleValue();
    }

    /**
     * double 相减
     *
     * @param d1
     * @param d2
     * @return
     */
    public static double sub(double d1, double d2) {
        BigDecimal bd1 = new BigDecimal(Double.toString(d1));
        BigDecimal bd2 = new BigDecimal(Double.toString(d2));
        return bd1.subtract(bd2).doubleValue();
    }

    /**
     * double 乘法
     *
     * @param d1
     * @param d2
     * @return
     */
    public static double mul(double d1, double d2) {
        BigDecimal bd1 = new BigDecimal(Double.toString(d1));
        BigDecimal bd2 = new BigDecimal(Double.toString(d2));
        return bd1.multiply(bd2).doubleValue();
    }


    /**
     * double 除法
     *
     * @param d1
     * @param d2
     * @param scale 四舍五入 小数点位数
     * @return
     */
    public static double div(double d1, double d2, int scale) {
        //  当然在此之前，你要判断分母是否为0，
        //  为0你可以根据实际需求做相应的处理

        BigDecimal bd1 = new BigDecimal(Double.toString(d1));
        BigDecimal bd2 = new BigDecimal(Double.toString(d2));
        //return bd1.divide(bd2,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
        return bd1.divide(bd2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static boolean isNetworkAvailable(Context context) {
        if (context == null) return false;

        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 手机号验证
     *
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isMobile(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        // p = Pattern.compile("^[1][3,4,5,8][0-9]{9}$"); // 验证手机号
        p = Pattern.compile("^(1)\\d{10}$");
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

    /**
     * 电话号码验证
     *
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isPhone(String str) {
        Pattern p1 = null, p2 = null;
        Matcher m = null;
        boolean b = false;
        p1 = Pattern.compile("^[0][1-9]{2,3}-[0-9]{5,10}$");  // 验证带区号的
        p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$");         // 验证没有区号的
        if (str.length() > 9) {
            m = p1.matcher(str);
            b = m.matches();
        } else {
            m = p2.matcher(str);
            b = m.matches();
        }
        return b;
    }

    /**
     * 验证邮箱地址是否正确
     *
     * @param email
     * @return
     */
    public static boolean checkEmail(String email) {
        boolean flag = false;
        try {
            String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }

        return flag;
    }

    /**
     * 验证手机号码
     *
     * @param mobiles
     * @return [0-9]{5,9}
     */
    public static boolean isMobileNO(String mobiles) {
        boolean flag = false;
        try {
            Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
            // Pattern p = Pattern.compile("^(1)\\d{10}$");
            Matcher m = p.matcher(mobiles);
            flag = m.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 验证手机号码格式
     */
    public static boolean checkMobileNO(String mobiles) {
        /*
            移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		    联通：130、131、132、152、155、156、185、186
		    电信：133、153、180、189、（1349卫通）
		 new: 170  171  173   175    176   177   178
		    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
	    */
        String telRegex = "([1][3578][0123456789]\\d{8})|([1][4][579]\\d{8})|([1][7][8156703]\\d{8})|([1][8][8156703]\\d{8})";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }

    public static double fn_rad(double d) {
        return div(mul(d, Math.PI), 180.0, 8);
    }// 2点间算法

    public static double P2PDistance(double lat1, double lng1, double lat2, double lng2) {// 纬度1,经度1 ~ 纬度2,经度2
        double EARTH_RADIUS = 6378.137;
        double radLat1 = fn_rad(lat1);
        double radLat2 = fn_rad(lat2);
        double a = sub(radLat1, radLat2);
        double b = sub(fn_rad(lng1), fn_rad(lng2));
        double s = mul(2, Math.asin(Math.sqrt(Math.pow(Math.sin(div(a, 2, 8)), 2) + mul(mul(Math.cos(radLat1), Math.cos(radLat2)), Math.pow(Math.sin(div(b, 2, 8)), 2)))));
        s = mul(s, EARTH_RADIUS);
        s = div(Math.round(s * 10000), 10000, 8);
        return s;
    }


    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * Get current format date string
     *
     * @param strFormat: format String (ex, "yyyy-MM-dd HH:mm:ss")
     * @return String retStr: format String
     */
    public static String getCurFormattedDate(String strFormat) {
        String retStr = "";
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format = null;
        try {
            format = new SimpleDateFormat(strFormat);
            retStr = format.format(cal.getTime());
        } catch (Exception e) {
        }
        return retStr;
    }

    /**
     * Get format date string with calendar
     *
     * @param strFormat:format string (ex, "yyyy-MM-dd HH:mm:ss")
     * @param cal:             given calendar
     * @return String retStr:format date String
     */
    public static String getCurFormattedDate2(String strFormat, Calendar cal) {
        String retStr = "";
        if (strFormat == null || cal == null || strFormat.trim().equals("")) return retStr;
        SimpleDateFormat format = null;
        try {
            format = new SimpleDateFormat(strFormat);
            retStr = format.format(cal.getTime());
        } catch (Exception e) {
        }
        return retStr;
    }

    // KeyBoard show/hide
    public static void hideKeyboard(Context ctx, EditText editText) {
        if (editText == null)
            return;
        InputMethodManager mgr = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    // KeyBoard hide
    public static void hideKeyboard(Context ctx) {
        InputMethodManager mgr = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(((Activity) ctx).getWindow().getDecorView().getWindowToken(), 0);
    }

    public static void showKeyboard(Context ctx, EditText editText) {
        if (editText == null)
            return;
        /*
        InputMethodManager mgr = (InputMethodManager)ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
		mgr.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
		*/
        InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);

        editText.setCursorVisible(true);
    }

    public static void setEditMaxLength(EditText et, int length) {
        if (et != null && length > 0) {
            InputFilter[] filters = {new InputFilter.LengthFilter(length)};
            et.setFilters(filters);
        }
    }

    public static void scrollListViewTo(final ListView lv, int pos) {
        if (lv == null || pos < 0) return;
        final int curPos;
        Adapter adp = lv.getAdapter();
        if (adp.getCount() < pos + 1) {
            curPos = adp.getCount() - 1;
        } else {
            curPos = pos;
        }
        lv.postDelayed(new Runnable() {
            @Override
            public void run() {
                lv.setSelection(curPos);
            }
        }, 300);
    }

    public static void registerToMediaScanner(Context ctx, String newFilePath) {
        if (newFilePath == null || newFilePath.length() < 4)
            return;
        ContentValues values = new ContentValues();
        if (newFilePath.indexOf(".jpg") != -1 || newFilePath.indexOf(".png") != -1 || newFilePath.indexOf(".jpeg") != -1) {
            values.put(MediaStore.Images.Media.DATA, newFilePath);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            ctx.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        } else if (newFilePath.indexOf(".mp4") != -1) {
            ThumbnailUtils.createVideoThumbnail(newFilePath, MediaStore.Images.Thumbnails.MINI_KIND);
            values.put(MediaStore.Video.Media.DATA, newFilePath);
            values.put(MediaStore.Video.Media.MIME_TYPE, "video/3gp");
            ctx.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
        }
    }

    public static String getFileName(String path) {
        if (path == null || path.trim().equals("")) return "";
        try {
            String[] subs = path.split("\\/");
            if (subs.length < 2)
                return "";

            String name = subs[subs.length - 1];
            return name;
        } catch (Exception e) {
            return "";
        }
    }

    public static String getExtName(String path) {
        String extension = "";
        int i = path.lastIndexOf('.');
        int p = Math.max(path.lastIndexOf('/'), path.lastIndexOf('\\'));

        if (i > p) {
            extension = path.substring(i + 1);
        }
        return extension;
    }

    public static long calcCacheCapacity(Context context, File dir) {
        long ret = 0;
        if (dir == null)
            dir = StorageUtils.getCacheDirectory(context);

        File[] children = dir.listFiles();
        try {
            for (int i = 0; i < children.length; i++)
                if (children[i].isDirectory())
                    ret += calcCacheCapacity(context, children[i]);
                else {
                    ret += children[i].length();
                }
        } catch (Exception e) {
        }
        return ret;
    }

    public static void savePrefer(Context context, String key, String value) {

        int mode = Activity.MODE_PRIVATE;

        if (key == null || key.trim().equals("") || value == null)
            return;
        SharedPreferences mySharedPreferences = context.getSharedPreferences(PREF_KEY, mode);
        if (mySharedPreferences != null) {

            SharedPreferences.Editor editor = mySharedPreferences.edit();
            editor.putString(key, value);
            editor.commit();
        }
    }

    public static String readPrefer(Context context, String key) {

        int mode = Activity.MODE_PRIVATE;
        String ret = "";
        SharedPreferences mySharedPreferences = context.getSharedPreferences(PREF_KEY, mode);
        if (mySharedPreferences != null) {
            ret = mySharedPreferences.getString(key, "");
        }
        return ret;
    }

    /*
     *  获取字段的高度。
     */
    public static int getHeightOfMultiLineText(String text, int textSize, int maxWidth) {
        TextPaint paint = new TextPaint();
        paint.setTextSize(textSize);
        int index = 0;
        int lineCount = 0;
        while (index < text.length()) {
            index += paint.breakText(text, index, text.length(), true, maxWidth, null);
            lineCount++;
        }

        Rect bounds = new Rect();
        paint.getTextBounds("Yy", 0, 2, bounds);
        // obtain space between lines
        double lineSpacing = Math.max(0, ((lineCount - 1) * bounds.height() * 0.25));

        return (int) Math.floor(lineSpacing + lineCount * bounds.height());
    }

    /*
     *  获取字段的几个行。
     */
    public static int getLinesOfMultiLineText(String text, int textSize, int maxWidth) {
        TextPaint paint = new TextPaint();
        paint.setTextSize(textSize);
        int index = 0;
        int lineCount = 0;
        String[] splitStrings = text.split("\n");
        for (int i = 0; i < splitStrings.length; i++) {
            text = splitStrings[i];
            index = 0;
            while (index < text.length()) {
                index += paint.breakText(text, index, text.length(), true, maxWidth, null);
                lineCount++;
            }
        }
        return lineCount;
    }

    // 从Activity 获取 rootView
    public static View getRootView(Activity context) {
        if (context == null) return null;
        return ((ViewGroup) context.findViewById(android.R.id.content)).getChildAt(0);
    }

    public static void nullViewDrawablesRecursive(View view) {
        if (view != null) {
            try {
                ViewGroup viewGroup = (ViewGroup) view;

                int childCount = viewGroup.getChildCount();
                for (int index = 0; index < childCount; index++) {
                    View child = viewGroup.getChildAt(index);
                    nullViewDrawablesRecursive(child);
                }
            } catch (Exception e) {
            }
            nullViewDrawable(view);
        }
    }

    @SuppressWarnings("deprecation")
    public static void nullViewDrawable(View view) {
        try {
            Drawable d = view.getBackground();
            if (d != null) {
                d.setCallback(null);
            }
            d = null;
            int sdk = Build.VERSION.SDK_INT;
            if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                view.setBackgroundDrawable(null);
            } else {
                view.setBackground(null);
            }
        } catch (Exception e) {
        }

        try {
            if (view instanceof ImageView) {
                ImageView imageView = (ImageView) view;
                Drawable d = imageView.getDrawable();
                if (d != null) d.setCallback(null);
                imageView.setImageDrawable(null);
                d = null;
            }
        } catch (Exception e) {
        }
    }

    public static boolean screenShot(String dstFilePath, String dstFileName, View contentView) {

        if (contentView == null) {
            return false;
        }
        File folder = new File(dstFilePath);
        if (!folder.exists()) {
            if (!folder.mkdir()) {
                return false;
            }
        }
        contentView.setDrawingCacheEnabled(true);
        Bitmap bitmap = contentView.getDrawingCache();
        File file = new File(dstFilePath, dstFileName);

        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream ostream = new FileOutputStream(file);
            bitmap.compress(CompressFormat.PNG, 100, ostream);
            ostream.flush();
            ostream.close();
            contentView.setDrawingCacheEnabled(false);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            contentView.setDrawingCacheEnabled(false);
            return false;
        }
    }

    /**
     * 检查录音权限
     */
    public static void verifyRecordAuth() throws Exception {
        MediaRecorder mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        String dir = Environment.getExternalStorageDirectory() + "/Recording";
        File file = null;
        if (file == null) {
            File newRootDir = new File(dir);
            if (!newRootDir.exists()) {
                newRootDir.mkdir();
            }
            file = File.createTempFile("CR-", ".amr", newRootDir);
        }
        mediaRecorder.setOutputFile(file.getAbsolutePath());
        mediaRecorder.prepare();
        mediaRecorder.start();
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
        Utils.DeleteFile(file.getAbsolutePath());
    }

    /**
     * 获取机器号
     *
     * @param context
     * @return 机器号
     */
    public static String getDeviceId(Context context) {
        if (context == null) return "";
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = telephonyManager.getDeviceId();
        return deviceId;
    }

    public static void gotoIntentBrowser(Context mContext, String url) {

        if (mContext == null || url == null || url.trim().equals("")) return;
        Uri uri = Uri.parse(url.toString());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        mContext.startActivity(intent);
    }

    public static void gotoIntentCallPhone(Context mContext, String phoneUrl) {

        if (mContext == null || phoneUrl == null || phoneUrl.trim().equals("")) return;

        String uri = "tel:" + phoneUrl;
        if (phoneUrl.startsWith("tel:")) {
            uri = phoneUrl;
        } else {
            uri = "tel:" + phoneUrl;
        }
        Intent it = new Intent(Intent.ACTION_CALL);
        it.setData(Uri.parse(uri));

        PackageManager pm = mContext.getPackageManager();
        boolean permissionActionCall = (PackageManager.PERMISSION_GRANTED == pm.checkPermission("android.permission.ACTION_CALL", "com.cheweibao.liuliu"));
        if (permissionActionCall) {
            mContext.startActivity(it);
        } else {
            showToast("木有这个权限", mContext);
        }


    }

    public static void gotoIntentCallEmail(Context mContext, String emailUrl, String subject, String text) {

        if (mContext == null || emailUrl == null || emailUrl.trim().equals("")) return;

        if (!emailUrl.startsWith("mailto:")) {
            emailUrl = "mailto:" + emailUrl;
        }

        subject = subject == null ? mContext.getString(R.string.app_name) : subject;
        text = text == null ? "" : text;

        Intent data = new Intent(Intent.ACTION_SENDTO);
        data.setData(Uri.parse(emailUrl));
        data.putExtra(Intent.EXTRA_SUBJECT, subject);
        data.putExtra(Intent.EXTRA_TEXT, text);
        mContext.startActivity(data);
    }

    // 获取MD5加密
    public static String getMD5(String info) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(info.getBytes("UTF-8"));
            byte[] encryption = md5.digest();

            StringBuffer strBuf = new StringBuffer();
            for (int i = 0; i < encryption.length; i++) {
                if (Integer.toHexString(0xff & encryption[i]).length() == 1) {
                    strBuf.append("0").append(Integer.toHexString(0xff & encryption[i]));
                } else {
                    strBuf.append(Integer.toHexString(0xff & encryption[i]));
                }
            }

            return strBuf.toString();
        } catch (NoSuchAlgorithmException e) {
            return "";
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    //检查是否在后台
    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    /*Default Prefrence Management*/
    public static String readDefPref(Context mContext, String key) {
        if (mContext == null || !Utils.isValid(key)) return "";
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        return preferences.getString(key, "");
    }

    public static void saveDefPref(Context mContext, String key, String value) {
        if (mContext == null || !Utils.isValid(key) || value == null) return;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }
    /*Default Prefrence Management End*/

	/*解析方法*/

    /**
     * 方法名称:transStringToMap
     * 传入参数:mapString
     * 返回值:Map
     */
    public static Map transStringToMap(String mapString, String itemToken, String keyToken) {
        if (!Utils.isValid(mapString) || !Utils.isValid(itemToken) || !Utils.isValid(keyToken))
            return null;
        Map map = new HashMap();
        StringTokenizer items;
        for (StringTokenizer entrys = new StringTokenizer(mapString, itemToken); entrys.hasMoreTokens();
             map.put(items.nextToken(), items.hasMoreTokens() ? ((Object) (items.nextToken())) : null))
            items = new StringTokenizer(entrys.nextToken(), keyToken);
        return map;
    }


    /**
     * Check NewVersion
     *
     * @param context : 应用环境
     * @return int: 0- no new version, 1- upgrade, 2- first install
     */
    public static int isStartNewVersion(Context context) {

        int retVal = 0;
        PackageInfo pInfo;
        String curVersion = "";
        int curVerCode = 0;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            curVersion = pInfo.versionName;
            curVerCode = pInfo.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            retVal = 2;
        }

        String preVersion = Utils.readPrefer(context, "zhs_pre_version_name");
        int preVerCode = Utils.getIntFromString(Utils.readPrefer(context, "zhs_pre_version_code"));

        if (preVersion.equals("") || preVerCode == 0) retVal = 2; // fisr install
        else {
            if (preVersion.equals(curVersion)) {
                if (preVerCode == curVerCode) {
                    retVal = 0;
                } else { // new version start
                    retVal = 1;
                }
            } else { // new version start
                retVal = 1;
            }
        }
        return retVal;
    }

    public static void saveCurrentVersion(Context context) {

        PackageInfo pInfo;
        String curVersion = "";
        int curVerCode = 0;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            curVersion = pInfo.versionName;
            curVerCode = pInfo.versionCode;

            Utils.savePrefer(context, "zhs_pre_version_name", curVersion);
            Utils.savePrefer(context, "zhs_pre_version_code", "" + curVerCode);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static String getChatDateTime(String time_str) {
        if (time_str.length() <= 10) time_str += " 00:00:00";
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);

        Date newDate = (Date) sd.parse(time_str, pos);

        return getChatDateTime(newDate);
    }

    public static String getChatDateTime(Date newDate) {
        String interval = null;

        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date();

        long time = (curDate.getTime() - newDate.getTime()) / 1000;

        if (time < 60) {
            interval = "刚刚";
        } else if (time < 3600 && time >= 60) {
            interval = String.format("%d分钟前", (int) (time / 60));
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(curDate);
            int year1 = cal.get(Calendar.YEAR);
            int month1 = cal.get(Calendar.MONTH);
            int day1 = cal.get(Calendar.DAY_OF_MONTH);

            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(newDate);
            int year2 = cal2.get(Calendar.YEAR);
            int month2 = cal2.get(Calendar.MONTH);
            int day2 = cal2.get(Calendar.DAY_OF_MONTH);

            if (year1 == year2 && month1 == month2 && day1 == day2) {
                sd = new SimpleDateFormat("HH:mm");
                interval = sd.format(newDate);
            } else if (year1 == year2) {
                sd = new SimpleDateFormat("M月d曰 HH:mm");
                interval = sd.format(newDate);
            } else {
                sd = new SimpleDateFormat("yyyy年M月d日");
                interval = sd.format(newDate);
            }
        }

        return interval;
    }

    public static String getChatDateTime2(String time_str) {
        if (time_str.length() <= 10) time_str += " 00:00:00";
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);

        Date newDate = (Date) sd.parse(time_str, pos);

        return getChatDateTime2(newDate);
    }

    public static String getChatDateTime2(Date newDate) {
        String interval = null;

        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date();

        long time = (curDate.getTime() - newDate.getTime()) / 1000;

        if (time < 60) {
            interval = "刚刚";
        } else if (time < 3600 && time >= 60) {
            interval = String.format("%d分钟前", (int) (time / 60));
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(curDate);
            int year1 = cal.get(Calendar.YEAR);
            int month1 = cal.get(Calendar.MONTH);
            int day1 = cal.get(Calendar.DAY_OF_MONTH);

            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(newDate);
            int year2 = cal2.get(Calendar.YEAR);
            int month2 = cal2.get(Calendar.MONTH);
            int day2 = cal2.get(Calendar.DAY_OF_MONTH);

            if (year1 == year2 && month1 == month2 && day1 == day2) {
                sd = new SimpleDateFormat("HH:mm");
                interval = sd.format(newDate);
            } else if (year1 == year2) {
                sd = new SimpleDateFormat("M月d曰");
                interval = sd.format(newDate);
            } else {
                sd = new SimpleDateFormat("yyyy年M月d日");
                interval = sd.format(newDate);
            }
        }

        return interval;
    }

    public static String getUpdateDateTime(String time_str) {
        if (time_str.length() <= 10) time_str += " 00:00:00";
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);

        Date newDate = (Date) sd.parse(time_str, pos);

        return getUpdateDateTime(newDate);
    }

    public static String getUpdateDateTime(Date newDate) {
        String interval = null;

        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date();

        long time = (curDate.getTime() - newDate.getTime()) / 1000;

        if (time < 60) {
            interval = "刚刚";
        } else if (time < 3600 && time >= 60) {
            interval = String.format("%d分钟前", (int) (time / 60));
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(curDate);
            int year1 = cal.get(Calendar.YEAR);
            int month1 = cal.get(Calendar.MONTH);
            int day1 = cal.get(Calendar.DAY_OF_MONTH);

            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(newDate);
            int year2 = cal2.get(Calendar.YEAR);
            int month2 = cal2.get(Calendar.MONTH);
            int day2 = cal2.get(Calendar.DAY_OF_MONTH);

            if (year1 == year2 && month1 == month2 && day1 == day2) {
                interval = String.format("%d小时前", (int) (time / 60 / 60));
            } else if (year1 == year2 && month1 == month2) {
                interval = String.format("%d天前", day1 - day2);
            } else if (year1 == year2) {
                interval = String.format("%d个月前", month1 - month2);
            } else {
                interval = String.format("%d年前", year1 - year2);
            }
        }

        return interval;
    }

    public static String getImageRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static String getVideoRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Video.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static double getImageRate(String path) {
        File file = new File(path);
        if (file.exists() == true) {
            try {
                BitmapFactory.Options o = new BitmapFactory.Options();
                o.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(new FileInputStream(file), null, o);

                return (double) (o.outHeight / (double) o.outWidth);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (OutOfMemoryError e) {
                System.gc();
            }

        }
        return 0;
    }

    public static void setDialogCloseState(DialogInterface dialog, boolean canClose) {
        try {
            Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
            field.setAccessible(true);
            field.set(dialog, Boolean.valueOf(canClose));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static boolean compressImage(File imgFile) {

        final int LIMIT_SIZE = 1200;//Uint KB.
        if (imgFile == null) return false;
        int file_size = Integer.parseInt(String.valueOf(imgFile.length() / 1024)); // file_size = percent

        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getPath(), options);

            if (LIMIT_SIZE >= file_size) {
                file_size = 100;
            } else if (100 * LIMIT_SIZE / file_size == 0) {
                file_size = 1;
            } else {
                file_size = 100 * LIMIT_SIZE / file_size;
            }

            FileOutputStream out = null;
            if (file_size < 100) {
                try {
                    out = new FileOutputStream(imgFile);
                    bitmap.compress(CompressFormat.JPEG, file_size, out);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return false;
                    }
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    public static boolean compressImage(Bitmap bitmap, File imgFile) {

        final int LIMIT_SIZE = 1200;//Uint KB.
        if (imgFile == null) return false;
        if (bitmap == null) return false;
        int file_size = getBitmapSize(bitmap) / (1024 * 15); // file_size = percent

        if (LIMIT_SIZE >= file_size) {
            file_size = 100;
        } else if (100 * LIMIT_SIZE / file_size == 0) {
            file_size = 1;
        } else {
            file_size = 100 * LIMIT_SIZE / file_size;
        }

        FileOutputStream out = null;
        if (file_size < 100) {
            try {
                out = new FileOutputStream(imgFile);
                bitmap.compress(CompressFormat.JPEG, file_size, out);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
        return true;
    }

    public static int getBitmapSize(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {    //API 19
            return bitmap.getAllocationByteCount();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {//API 12
            return bitmap.getByteCount();
        }
        return bitmap.getRowBytes() * bitmap.getHeight();                //earlier version
    }

    public static int compareVersion(String version1, String version2) {
        return compare(version1, 0, version2, 0);
    }

    public static int compare(String version1, int st1, String version2, int st2) {
        int ssa = 1;
        int ssb = 1;
        if (st1 == version1.length() || st1 == version1.length() + 1) {
            ssa = 0;
            if (st2 == version2.length() || st2 == version2.length() + 1) {
                return 0;
            }
        }
        if (st2 == version2.length() || st2 == version2.length() + 1) {
            ssb = 0;
            if (st1 == version1.length() || st1 == version1.length() + 1) {
                return 0;
            }
        }
        int i = st1;
        int a = 0;
        if (ssa != 0) {
            String s1 = "";
            for (; i < version1.length(); i++) {
                if (version1.charAt(i) == '.')
                    break;
                else
                    s1 = s1 + version1.charAt(i);
            }
            a = Integer.valueOf(s1);
        }
        int j = st2;
        int b = 0;
        if (ssb != 0) {
            String s2 = "";
            for (; j < version2.length(); j++) {
                if (version2.charAt(j) == '.')
                    break;
                else
                    s2 = s2 + version2.charAt(j);
            }
            b = Integer.valueOf(s2);
        }
        if (a > b)
            return 1;
        else {
            if (a < b)
                return -1;
            else {
                if (ssa == 0 || ssb == 0) return 0;
                else return compare(version1, i + 1, version2, j + 1);
            }
        }
    }

    public static String getEncriptPhone(String phone) {
        if (phone == null) return "";

        if (phone.length() > 9) {
            return phone.substring(0, 3) + "****" + phone.substring(phone.length() - 4);
        } else {
            return phone;
        }
    }

    /**
     * Get Age
     *
     * @param birthDay : format "yyyy-MM-dd"
     * @return int : age
     */
    public static int getAge(String birthDay) {
        int age = 0;
        Date date = new Date();
        SimpleDateFormat format = null;
        try {
            format = new SimpleDateFormat("yyyy-MM-dd");
            Date birthDate = format.parse(birthDay);
            age = (int) ((date.getTime() - birthDate.getTime()) / (24 * 60 * 60 * 1000));
            age = age / 365;
        } catch (Exception e) {
        }
        return age;
    }

    public static long getTimeFromString(String strDate) {
        long time = 0;
        Date date = new Date();
        SimpleDateFormat format = null;
        try {
            format = new SimpleDateFormat("yyyy-MM-dd");
            date = format.parse(strDate);
            time = (long) date.getTime();
        } catch (Exception e) {
        }

        return time;
    }

    public static String getDateYMD(long milliSeconds) {
        // Create a DateFormatter object for displaying date in specified format.
        //SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public static int getDateY(long milliSeconds) {
        // Create a DateFormatter object for displaying date in specified format.
        //SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return getIntFromString(formatter.format(calendar.getTime()));
    }

    public static int getDateM(long milliSeconds) {
        // Create a DateFormatter object for displaying date in specified format.
        //SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat formatter = new SimpleDateFormat("MM");

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return getIntFromString(formatter.format(calendar.getTime()));
    }

    public static int getDateD(long milliSeconds) {
        // Create a DateFormatter object for displaying date in specified format.
        //SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat formatter = new SimpleDateFormat("dd");

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return getIntFromString(formatter.format(calendar.getTime()));
    }

    /////////////////////////////////// user info start ////////////////////////////////////////
    //Set UserInfo
    public static boolean setUserInfo(Context context, String jString) {

        try {
            SharedPreferences _pref = context.getSharedPreferences(PREF_KEY, Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = _pref.edit();
            editor.putString("USER_INFO_JSON_STR", jString);
            editor.commit();
            return true;
        } catch (Exception e) {

        }
        return false;
    }

    public static UserInfo getUserInfo(Context context) {

        UserInfo info = null;
        try {
            SharedPreferences _pref = context.getSharedPreferences(PREF_KEY, Activity.MODE_PRIVATE);
            String strInfo = _pref.getString("USER_INFO_JSON_STR", "");
            JSONObject jObj = (JSONObject) JSONObject.parse(strInfo);
            info = new UserInfo(jObj);
        } catch (Exception e) {

        }
        return info;
    }

    public static boolean setUserInfo(Context context, String key, String value) {

        if (key == null || key.equals("")) return false;
        if (value == null || value.equals("")) return false;
        try {
            SharedPreferences _pref = context.getSharedPreferences(PREF_KEY, Activity.MODE_PRIVATE);
            String strInfo = _pref.getString("USER_INFO_JSON_STR", "");
            JSONObject jObj = (JSONObject) JSONObject.parse(strInfo);
            jObj.put(key, value);
            setUserInfo(context, jObj.toString());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void deleteUserinfo(Context context) {
        SharedPreferences _pref = context.getSharedPreferences(PREF_KEY, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = _pref.edit();
        editor.putString("USER_INFO_JSON_STR", "");
        editor.commit();
    }

    /**
     * set Current Location(Lat, Lon)
     *
     * @param context :
     * @return void
     */
    public static void setUserLocation(Context context, float lat, float lon) {

        try {
            SharedPreferences _pref = context.getSharedPreferences(
                    PREF_KEY, Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = _pref.edit();
            editor.putFloat("userLat", lat);
            editor.putFloat("userLon", lon);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static double getUserLat(Context context) {
        SharedPreferences _pref = context.getSharedPreferences(
                PREF_KEY, Activity.MODE_PRIVATE);

        return _pref.getFloat("userLat", 0);
    }

    public static double getUserLon(Context context) {
        SharedPreferences _pref = context.getSharedPreferences(
                PREF_KEY, Activity.MODE_PRIVATE);

        return _pref.getFloat("userLon", 0);
    }

    public static void setGetuiClientId(Context context, String clientId) {
        try {
            SharedPreferences _pref = context.getSharedPreferences(
                    PREF_KEY, Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = _pref.edit();
            editor.putString("getuiId", clientId);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getGetuiClientId(Context context) {
        SharedPreferences _pref = context.getSharedPreferences(
                PREF_KEY, Activity.MODE_PRIVATE);

        return _pref.getString("getuiId", "");
    }

    public static void setLoginType(Context context, int type) {
        try {
            SharedPreferences _pref = context.getSharedPreferences(
                    PREF_KEY, Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = _pref.edit();
            editor.putInt("LOGIN_TYPE", type);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getLoginType(Context context) {
        SharedPreferences _pref = context.getSharedPreferences(
                PREF_KEY, Activity.MODE_PRIVATE);

        return _pref.getInt("LOGIN_TYPE", 0);
    }

    public static void setLoginMobile(Context context, String mobile) {
        try {
            SharedPreferences _pref = context.getSharedPreferences(
                    PREF_KEY, Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = _pref.edit();
            editor.putString("LOGIN_MOBILE", mobile);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getLoginMobile(Context context) {
        SharedPreferences _pref = context.getSharedPreferences(
                PREF_KEY, Activity.MODE_PRIVATE);

        return _pref.getString("LOGIN_MOBILE", "");
    }

    public static void setLoginPwd(Context context, String pwd) {
        try {
            SharedPreferences _pref = context.getSharedPreferences(
                    PREF_KEY, Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = _pref.edit();
            editor.putString("LOGIN_PWD", pwd);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getLoginPwd(Context context) {
        SharedPreferences _pref = context.getSharedPreferences(
                PREF_KEY, Activity.MODE_PRIVATE);

        return _pref.getString("LOGIN_PWD", "");
    }

    /////////////////////////////////// user info end ////////////////////////////////////////

    /////////////////////////////////// book and video kind list start ////////////////////////////////////////////
    public static boolean setBookKind(Context context, String jString) {

        try {
            SharedPreferences _pref = context.getSharedPreferences(PREF_KEY, Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = _pref.edit();
            editor.putString("BOOK_KIND_JSON_STR", jString);
            editor.commit();
            return true;
        } catch (Exception e) {

        }
        return false;
    }


    public static boolean moveToFrontFromBackGround(Context context, boolean isMove) {
        boolean isAppRunning = false;
        if (Build.VERSION.SDK_INT >= 11) { // honeycomb
            final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            final List<RunningTaskInfo> recentTasks = activityManager.getRunningTasks(Integer.MAX_VALUE);

            for (int i = 0; i < recentTasks.size(); i++) {
                // bring to front
                if (recentTasks.get(i).baseActivity.toShortString().indexOf(context.getPackageName()) > -1) {
                    if (isMove)
                        activityManager.moveTaskToFront(recentTasks.get(i).id, ActivityManager.MOVE_TASK_WITH_HOME);
                    isAppRunning = true;
                }
            }
        }
        return isAppRunning;
    }

    public static String getFormatedPrice(String org) {
        if (TextUtils.isEmpty(org)) return "0";
        try {
            if (org.endsWith("00000000.00"))
                return org.substring(0, org.length() - 11) + "亿";
            else if (org.endsWith("0000000.00"))
                return org.substring(0, org.length() - 10) + "千万";
            else if (org.endsWith("000000.00"))
                return org.substring(0, org.length() - 9) + "百万";
            else if (org.endsWith("0000.00"))
                return org.substring(0, org.length() - 7) + "万";
//			else if (org.endsWith("000.00"))
//				return org.substring(0, org.length() - 3) + "千";
            else
                return org;
        } catch (Exception e) {
            return "0";
        }
    }

    public static void setIsLoad(Context context, boolean isload) {
        try {
            SharedPreferences _pref = context.getSharedPreferences(
                    PREF_KEY, Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = _pref.edit();
            editor.putBoolean("IS_LOAD", isload);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean IsLoad(Context context) {
        SharedPreferences _pref = context.getSharedPreferences(
                PREF_KEY, Activity.MODE_PRIVATE);

        return _pref.getBoolean("IS_LOAD", false);
    }

    public static String replaceWithBlank(String str) {

        String newStr1 = str.replaceAll(";|；", "；\n");
        return newStr1;
    }

    public static void setListViewHeight(ListView listView) {
        //获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {   //listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);  //计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight();  //统计所有子项的总高度
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        //listView.getDividerHeight()获取子项间分隔符占用的高度
        //params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    public static void setGridViewHeight(GridView gridView) {
        ListAdapter adapter = gridView.getAdapter();
        if (adapter == null) {
            return;
        }
        int totalHeigt = 0;
        double itemCount = adapter.getCount();
        if (itemCount <= 0) {
            return;
        }
        double horizentalNumger = gridView.getNumColumns();
        int verticalNumber = (int) Math.ceil(itemCount / horizentalNumger);
        View item = adapter.getView(0, null, gridView);
        item.measure(0, 0);
        int itemHeight = item.getMeasuredHeight();
        int space = gridView.getVerticalSpacing();
        totalHeigt = verticalNumber * itemHeight + (verticalNumber - 1) * space;
        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeigt;
        gridView.setLayoutParams(params);
    }

    /**
     * 对内存进行计算和单位的转换
     *
     * @param size
     * @return
     * @throws Exception
     */

    public static String getFormatSize(double size) throws Exception {

        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "Byte";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }

    public static String getPrice(String money) {
        return money + "元";
    }

    /**
     * 禁止EditText输入特殊字符
     *
     * @param editText
     */
    public static void setEditTextInhibitInputSpeChat(EditText editText) {

        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String speChat = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
                Pattern pattern = Pattern.compile(speChat);
                Matcher matcher = pattern.matcher(source.toString());
                if (matcher.find()) return "";
                else return null;
            }
        };
        editText.setFilters(new InputFilter[]{filter});
    }

    /**
     * 读取assets下的txt文件，返回utf-8 String
     *
     * @param context
     * @param fileName 不包括后缀
     * @return
     */
    public static String readAssetsTxt(Context context, String fileName) {
        try {
            //Return an AssetManager instance for your application's package
            InputStream is = context.getAssets().open(fileName + ".txt");
            int size = is.available();
            // Read the entire asset into a local byte buffer.
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            // Convert the buffer into a string.
            String text = new String(buffer, "utf-8");
            // Finally stick the string into the text view.
            return text;
        } catch (IOException e) {
            // Should never happen!
//            throw new RuntimeException(e);
            e.printStackTrace();
        }
        return "读取错误，请检查文件名";
    }

    public static String replaceBlank(String src) {
        String dest = "";
        if (src != null) {
            Pattern pattern = Pattern.compile("\\\\|\t|\r|\n|\\s*");
            Matcher matcher = pattern.matcher(src);
            dest = matcher.replaceAll("");
        }
        return dest;
    }
}
