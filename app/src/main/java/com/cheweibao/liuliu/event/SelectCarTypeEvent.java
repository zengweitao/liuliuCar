package com.cheweibao.liuliu.event;

import android.content.Intent;

/**
 * Created by user on 2018/4/20.
 */

public class SelectCarTypeEvent {

        public String msg;
        public Intent store;

        public SelectCarTypeEvent() {
            msg = "";
            store = new Intent();
        }

        public boolean isMsgOf(String msg){
            if (msg.equals(this.msg)) return true;
            else return false;
        }

        public SelectCarTypeEvent(String msg) {
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
