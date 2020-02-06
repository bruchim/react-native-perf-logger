package com.reactnativeperflogger;

import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;

public class AllRecordsExporter implements MarkersExporter<JSONArray> {
    private JSONArray array;

    @Override
    public void init() {
        array = new JSONArray();
    }

    @NonNull
    @Override
    public JSONArray finish() {
        return array;
    }

    @Override
    public void processRecord(@NonNull MarkerRecord record) {
        try {
            array.put(record.toJSON());
        } catch (JSONException e) {
            Log.w(RNPerfLogger.LOG_TAG, "Could not convert perf records to JSON", e);
        }

    }
}
