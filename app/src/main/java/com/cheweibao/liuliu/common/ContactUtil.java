package com.cheweibao.liuliu.common;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;

import java.util.HashMap;

/**
 * Created by unknow on 2018/6/5.
 */

public class ContactUtil {
    /***
     * 判断权限
     *
     * @param context
     * @param data
     * @return
     */
    public static boolean getPermission(Context context, Intent data) {
        try {
            // 获取联系人id游标
            Cursor idCursor = context.getContentResolver().query(data.getData(), null, null, null, null);
            if (!idCursor.moveToFirst()) {
                if (!idCursor.isClosed()) idCursor.close();
                return false;
            }
            if (!idCursor.isClosed()) idCursor.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取单个联系的详细信息
     *
     * @param context
     * @param data    调用系统联系人返回的Intent
     */
    public static HashMap<String, String> getContactInfo(Context context, Intent data) {
        Cursor cursor = null;
        try {
            // 获取联系人id游标
            Cursor idCursor = context.getContentResolver().query(data.getData(), null, null, null, null);
            if (!idCursor.moveToFirst()) {
                return null;
            }
            // 获取联系人的id，用于筛选
            String contactId = idCursor.getString(idCursor.getColumnIndex(ContactsContract.Contacts._ID));
            if (!idCursor.isClosed()) idCursor.close();
            // 获取系统的所有联系人中筛选出id为“contactId”的所有信息
            cursor = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, ContactsContract.Data.CONTACT_ID + " = " + contactId, null, null);
            HashMap<String, String> params = new HashMap<>();
            // 遍历所有内容
            while (cursor.moveToNext()) {
                // 获取mimetype，通过mimetype来判断当前数据的类型
                String mimetype = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.MIMETYPE));
                switch (mimetype) {
                    // 联系人名称
                    case ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE:
                        String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME));
                        params.put("name", name);
                        break;
                    // 联系人电话号码，注意电话号码有多个且类型可能不同
                    case ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE:

                        int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                        int phoneTypeIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
                        int phoneType = cursor.getInt(phoneTypeIndex);
                        String number = cursor.getString(phoneIndex).trim();

                        params.put("mobile", number.replaceAll(" ", ""));
                        switch (phoneType) {
                            // 移动手机
                            case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                                String mobile = number;
                                break;
                            // 家用座机
                            case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                                String phone = number;
                                break;
                            // 家用传真
                            case ContactsContract.CommonDataKinds.Phone.TYPE_FAX_HOME:
                                String faxHome = number;
                                break;

                            default:
                                break;
                            // 一共有20种类型
                            // public static final int TYPE_HOME = 1;
                            // public static final int TYPE_MOBILE = 2;
                            // public static final int TYPE_WORK = 3;
                            // public static final int TYPE_FAX_WORK = 4;
                            // public static final int TYPE_FAX_HOME = 5;
                            // public static final int TYPE_PAGER = 6;
                            // public static final int TYPE_OTHER = 7;
                            // public static final int TYPE_CALLBACK = 8;
                            // public static final int TYPE_CAR = 9;
                            // public static final int TYPE_COMPANY_MAIN = 10;
                            // public static final int TYPE_ISDN = 11;
                            // public static final int TYPE_MAIN = 12;
                            // public static final int TYPE_OTHER_FAX = 13;
                            // public static final int TYPE_RADIO = 14;
                            // public static final int TYPE_TELEX = 15;
                            // public static final int TYPE_TTY_TDD = 16;
                            // public static final int TYPE_WORK_MOBILE = 17;
                            // public static final int TYPE_WORK_PAGER = 18;
                            // public static final int TYPE_ASSISTANT = 19;
                            // public static final int TYPE_MMS = 20;
                        }
                        break;
                    // 获取email
                    case ContactsContract.CommonDataKinds.Email.CONTENT_TYPE:
                        String email = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
                        params.put("email", email);
                        break;
                    // 获取备注
                    case ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE:
                        String note = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Note.NOTE));
                        params.put("momo", note);
                        break;
                    // 获取通讯地址
                    case ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE:

                        String street = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));

                        String poBox = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POBOX));

                        String city = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));

                        String country = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY));

                        String region = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.REGION));

                        String neighborhood = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.NEIGHBORHOOD));

                        String postcode = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE));

                        // 判断通讯地址类型
                        int structuredPostalIndexTypeIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.TYPE);
                        int structuredPostalIndexType = cursor.getInt(structuredPostalIndexTypeIndex);
                        switch (structuredPostalIndexType) {
                            // 家庭地址
                            case ContactsContract.CommonDataKinds.StructuredPostal.TYPE_HOME:
                                break;

                            // 单位地址
                            case ContactsContract.CommonDataKinds.StructuredPostal.TYPE_WORK:
                                break;

                            // 其他
                            case ContactsContract.CommonDataKinds.StructuredPostal.TYPE_OTHER:
                                break;

                            default:
                                break;
                        }
                        break;
                    default:
                        break;
                }
            }

            return params;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return null;
    }
}
