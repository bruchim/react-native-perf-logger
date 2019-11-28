package com.reactnativeperflogger;

import android.util.Log;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import java.util.ArrayList;
import java.util.function.Consumer;

public class PerfLoggerModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;
    private final ArrayList<Callback> callbackArrayList;

    static private PerfLoggerModule perfLoggerModuleInstance;
    public static PerfLoggerModule getInstance() {
        return perfLoggerModuleInstance;
    }

    public PerfLoggerModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        this.callbackArrayList = new ArrayList<>();
        perfLoggerModuleInstance = this;
    }


    @Override
    public String getName() {
        return "PerfLogger";
    }

    @ReactMethod
    public void sampleMethod(String stringArgument, int numberArgument, Callback callback) {
        callback.invoke("Received numberArgument: " + numberArgument + " stringArgument: " + stringArgument);
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
    public void getMarkersJSON(Promise promise) {
        promise.resolve(MarkersStore.getInstance().getPerfRecordsJSON());
    }


    public void invokeTTICompleted(){
        this.callbackArrayList.forEach(new Consumer<Callback>() {
            @Override
            public void accept(Callback callback) {
                callback.invoke();
            }
        });
    }
}
