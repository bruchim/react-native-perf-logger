package com.reactnativeperflogger;

import android.os.Process;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class MarkerRecord {
    private final long mTime;
    private final String mName;
    private final String mTag;
    private final int mInstanceKey;
    private final int mTid;
    private final int mPid;

    MarkerRecord(String name, String tag, int instanceKey) {
        mTime = System.currentTimeMillis();
        mName = name;
        mTag = tag;
        mInstanceKey = instanceKey;
        mPid = Process.myPid();
        mTid = Process.myTid();
    }

    MarkerRecord(String name, String tag, int instanceKey, long time) {
        mTime = time;
        mName = name;
        mTag = tag;
        mInstanceKey = instanceKey;
        mPid = Process.myPid();
        mTid = Process.myTid();
    }

    public JSONObject toJSON() {
        JSONObject result = new JSONObject();
        try {
            result.put("time", mTime);
            result.put("name", mName);
            result.put("tag", mTag);
            result.put("instanceKey", mInstanceKey);
            result.put("pid", mPid);
            result.put("tid", mTid);
            return result;
        } catch (JSONException e) {
            return new JSONObject();
        }
    }

    public String toString() {
        return TextUtils.join(
                ",",
                new String[] {
                        Long.toString(mTime),
                        mName,
                        mTag,
                        Integer.toString(mInstanceKey),
                        Integer.toString(mTid),
                        Integer.toString(mPid)
                });
    }
}