package com.cheweibao.liuliu.data;

import android.content.Intent;

/**
 * Created by LuckyMan on 2017/9/8.
 */

public class MessageEvent {
    public String msg;
    public Intent store;

    public MessageEvent() {
        msg = "";
        store = new Intent();
    }

    public boolean isMsgOf(String msg){
        if (msg.equals(this.msg)) return true;
        else return false;
    }

    public MessageEvent(String msg) {
        this.msg = msg;
        store = new Intent();
    }
    public void putExtra(String key, int value){
        store.putExtra(key, value);
    }
    public int getIntExtra(String key, int def){
        return store.getIntExtra(key, def);
    }
    public void putExtra(String key, String value){
        store.putExtra(key, value);
    }
    public String getStringExtra(String key){
        return store.getStringExtra(key);
    }
    public void putExtra(String key, double value){
        store.putExtra(key, value);
    }
    public double getDoubleExtra(String key, double def){
        return store.getDoubleExtra(key, def);
    }
    public void putExtra(String key, boolean value){
        store.putExtra(key, value);
    }
    public boolean getBooleanExtra(String key, boolean def){
        return store.getBooleanExtra(key, def);
    }
    public void putExtra(String key, long value){
        store.putExtra(key, value);
    }
    public long getLongExtra(String key, long def){
        return store.getLongExtra(key, def);
    }
    public void setStore(Intent store){
        this.store = store;
    }
    public Intent getStore(){
        return store;
    }

}
