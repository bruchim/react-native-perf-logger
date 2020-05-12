package com.reactnativeperflogger;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PerfLoggerModule extends ReactContextBaseJavaModule implements TTIEndListener {
    private final RNPerfLogger logger;
    private final ArrayList<Callback> callbackArrayList;

    PerfLoggerModule(@NonNull ReactApplicationContext reactContext, @NonNull RNPerfLogger logger) {
        super(reactContext);
        this.logger = logger;
        this.callbackArrayList = new ArrayList<>();
        this.logger.getTTIEndNotifier().addListener(this);
    }

    @NonNull
    @Override
    public String getName() {
        return "PerfLogger";
    }

    @ReactMethod
    public void registerTTINativeIds(ReadableArray ids) {
        logger.registerTTINativeIds(ids);
    }

    @ReactMethod
    public void registerTTICompletedListener(Callback callback) {
        this.callbackArrayList.add(callback);
    }

    @ReactMethod
    public void unregisterTTICompletedListener(Callback callback) {
        this.callbackArrayList.remove(callback);
    }

    @ReactMethod
    public void getAllMarkers(Promise promise) {
        promise.resolve(buildJSONString(logger.getAllRecordsJSON()));
    }

    @ReactMethod
    public void getIntervalBounds(Promise promise) {
        promise.resolve(buildJSONString(logger.getIntervalRecordsJson()));
    }

    @NonNull
    private String buildJSONString(@NonNull JSONArray array) {
        JSONObject object = new JSONObject();
        try {
            object.put("data", array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }

    @Override
    public void ttiEnded(String id, long time) {
        for (Callback callback : this.callbackArrayList) {
            callback.invoke(id, String.valueOf(time));
        }
    }

    @ReactMethod
    public void stopAndClear() {
        logger.stopListening();
        logger.clear();
    }
}
