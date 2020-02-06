package com.reactnativeperflogger;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.ReactMarker;
import com.facebook.react.bridge.ReactMarkerConstants;

import org.json.JSONArray;

import javax.annotation.Nullable;

public class RNPerfLogger implements ReactMarker.MarkerListener {
    static final String LOG_TAG = "RNPerfLogger";

    @NonNull
    private final MarkersStore store;

    @NonNull
    private final TTIEndNotifier ttiEndNotifier;

    public RNPerfLogger() {
        long time = System.currentTimeMillis();
        store = new MarkersStore();
        ReactMarker.addListener(this);
        ttiEndNotifier = new TTIEndNotifier(this);
        logCustomStartMarker("Application", null, time);
    }

    @SuppressWarnings("WeakerAccess")
    public TTIEndNotifier getTTIEndNotifier() {
        return ttiEndNotifier;
    }

    @SuppressWarnings("unused")
    public void logCustomStartMarker(String name, String tag) {
        logCustomStartMarker(name, tag, System.currentTimeMillis());
    }

    @SuppressWarnings("unused")
    public void logCustomEndMarker(String name, String tag) {
        logCustomEndMarker(name, tag, System.currentTimeMillis());
    }

    @SuppressWarnings("WeakerAccess")
    public void logCustomStartMarker(String name, String tag, Long time) {
        logCustomMarker("@" + name + "_START", tag, time);
    }

    @SuppressWarnings("WeakerAccess")
    public void logCustomEndMarker(String name, String tag, Long time) {
        logCustomMarker("@" + name + "_END", tag, time);
    }

    @SuppressWarnings("unused")
    public void logCustomEventMarker(String name, String tag) {
        logCustomEventMarker(name, tag, System.currentTimeMillis());
    }

    @SuppressWarnings("WeakerAccess")
    public void logCustomEventMarker(String name, String tag, Long time) {
        logCustomStartMarker(name, tag, time);
        logCustomEndMarker(name, tag, time + 1); // TODO: ???
    }

    @SuppressWarnings("WeakerAccess")
    public void logCustomMarker(String name, String tag, Long time) {
        logCustomMarker(name, tag, -1, time);
    }

    @SuppressWarnings("WeakerAccess")
    public void logCustomMarker(String name, String tag, int instanceKey, Long time) {
        store.add(name, tag, instanceKey, time);
    }

    @NonNull
    JSONArray getAllRecordsJSON() {
        return store.export(new AllRecordsExporter());
    }

    @NonNull
    JSONArray getIntervalRecordsJson() {
        return store.export(new IntervalBoundsExporter());
    }

    @SuppressWarnings("WeakerAccess")
    public void stopListening() {
        ReactMarker.removeListener(this);
        ttiEndNotifier.removeAllListeners();
    }

    @SuppressWarnings("WeakerAccess")
    public void clear() {
        store.clear();
    }

    @Override
    public void logMarker(ReactMarkerConstants name, @Nullable String tag, int instanceKey) {
        logCustomMarker(name.toString(), tag, instanceKey, System.currentTimeMillis());
    }
}
