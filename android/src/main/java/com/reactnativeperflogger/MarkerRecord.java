package com.reactnativeperflogger;

import android.os.Process;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

public class MarkerRecord {
    private final long time;
    private final String name;
    private final String tag;
    private final int instanceKey;
    private final int tid;
    private final int pid;

    @SuppressWarnings("unused")
    MarkerRecord(String name, String tag, int instanceKey) {
        time = System.currentTimeMillis();
        this.name = name;
        this.tag = tag;
        this.instanceKey = instanceKey;
        pid = Process.myPid();
        tid = Process.myTid();
    }

    MarkerRecord(String name, String tag, int instanceKey, long time) {
        this.time = time;
        this.name = name;
        this.tag = tag;
        this.instanceKey = instanceKey;
        pid = Process.myPid();
        tid = Process.myTid();
    }

    String getName() {
        return name;
    }

    long getTime() {
        return time;
    }

    String getTag() {
        return tag;
    }

    JSONObject toJSON() throws JSONException {
        JSONObject result = new JSONObject();
        result.put("time", time);
        result.put("name", name);
        result.put("tag", tag);
        result.put("instanceKey", instanceKey);
        result.put("pid", pid);
        result.put("tid", tid);
        return result;
    }

    @NonNull
    public String toString() {
        return TextUtils.join(
                ",",
                new String[]{
                        Long.toString(time),
                        name,
                        tag,
                        Integer.toString(instanceKey),
                        Integer.toString(tid),
                        Integer.toString(pid)
                });
    }
}