package com.reactnativeperflogger;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class MarkersStore {

    private static final String TAG = "reactnativeperflogger";

    private static MarkersStore _sInstance;
    public static MarkersStore getInstance() {
        if (_sInstance == null) {
            _sInstance = new MarkersStore();
        }
        return _sInstance;
    }

    private final List<MarkerRecord> mMarkersRecords = new LinkedList<>();

    public void logCustomStartMarker(String name, String tag) {
        logCustomStartMarker(name, tag, System.currentTimeMillis());
    }
    public void logCustomEndMarker(String name, String tag) {
        logCustomEndMarker(name, tag, System.currentTimeMillis());
    }

    public void logCustomStartMarker(String name, String tag, Long time) {
        logCustomMarker("@" + name + "_START", tag, time);
    }
    public void logCustomEndMarker(String name, String tag, Long time) {
        logCustomMarker("@" + name + "_END", tag, time);
    }

    public void logCustomEventMarker(String name, String tag) {
        logCustomEventMarker(name, tag, System.currentTimeMillis());
    }
    public void logCustomEventMarker(String name, String tag, Long time) {
        logCustomStartMarker(name, tag, time);
        logCustomEndMarker(name, tag, time + 1);
    }
    public void logCustomMarker(String name, String tag, Long time) {
        mMarkersRecords.add(new MarkerRecord(name, tag, -1, time));
    }
    public void logCustomMarker(String name, String tag, int instanceKey, Long time) {
        mMarkersRecords.add(new MarkerRecord(name, tag, instanceKey, time));
    }

    public String getPerfRecordsJSON() {
        JSONObject result = new JSONObject();
        try {
            if (mMarkersRecords != null) {
                JSONArray jsonRecords = new JSONArray();
                for (MarkerRecord record : mMarkersRecords) {
                    jsonRecords.put(record.toJSON());
                }
                result.put("data", jsonRecords);
            }

            return result.toString();
        } catch (JSONException e) {
            Log.w(TAG, "Could not convert perf records to JSON", e);
            return "{}";
        }
    }
}
