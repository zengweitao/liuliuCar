package com.cheweibao.liuliu.common;

import android.content.Context;

import com.cheweibao.liuliu.data.AuditInformationInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by unknow on 2018/6/23.
 */

public class CacheData {
    public static final int recent_subject_list = 8;
    private static MyGlobal myglobal;

    public CacheData(Context mContext) {
        myglobal = (MyGlobal) mContext.getApplicationContext();
    }

    /**
     * 获取文件名
     *
     * @param uid       用户的id
     * @param studentId 文件唯一标示
     * @param type      :数据类型
     * @return
     */
    public static String getFileNameById(int type, String studentId) {
        File file = new File(myglobal.cache_path);
        if (!file.exists())
            file.mkdirs();
        String fileName = "cachedata_" + type + "_" + studentId + ".dat";
        return fileName;
    }


    /**
     * 存储缓存文件：
     */
    public static void saveRecentSubList(String studentId, AuditInformationInfo info) {
        int type = recent_subject_list;
        String fileName = myglobal.cache_path + File.separator + getFileNameById(type, studentId);
        File file = new File(fileName);
        try {
            if (!file.exists())
                file.createNewFile();
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName));
            oos.writeObject(info);
            oos.flush();
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取缓存文件：
     */

    public static AuditInformationInfo getRecentSubList(String studentId) {
        int type = recent_subject_list;
        AuditInformationInfo resultList = new AuditInformationInfo();
        String fileName = myglobal.cache_path + File.separator + getFileNameById(type, studentId);
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName));
            AuditInformationInfo list_ext = (AuditInformationInfo) ois.readObject();
            resultList = list_ext;
            ois.close();
        } catch (Exception e) {
            return resultList;
        }
        return resultList;
    }
}
