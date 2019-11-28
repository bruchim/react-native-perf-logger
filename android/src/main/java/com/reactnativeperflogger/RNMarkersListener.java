package com.reactnativeperflogger;

import android.view.View;
import android.view.ViewTreeObserver;
import com.facebook.react.bridge.ReactMarker;
import com.facebook.react.bridge.ReactMarkerConstants;
import com.facebook.react.uimanager.util.ReactFindViewUtil;


import javax.annotation.Nullable;

/**
 * A class to record the Perf metrics that are emitted by {@link ReactMarker.MarkerListener}
 */
public class RNMarkersListener {

    public void initialize() {
        addReactMarkerListener();
        addTTIEndListener();
    }

    /**
     * This is the main functionality of this file. It basically listens to all the events and stores
     * them
     */
    private void addReactMarkerListener() {
        ReactMarker.addListener(

                new ReactMarker.MarkerListener() {
                    @Override
                    public void logMarker(ReactMarkerConstants name, @Nullable String tag, int instanceKey) {
                        long current = System.currentTimeMillis();
                        MarkersStore.getInstance().logCustomMarker(name.toString(), tag, instanceKey, current);
                    }
                });
    }

    /**
     * Waits for Loading to complete, also called a Time-To-Interaction (TTI) event. To indicate TTI
     * completion, add a prop nativeID="tti_complete" to the component whose appearance indicates that
     * the initial TTI or loading is complete
     */
    private void addTTIEndListener() {
        ReactFindViewUtil.addViewListener(
                new ReactFindViewUtil.OnViewFoundListener() {
                    @Override
                    public String getNativeId() {
                        // This is the value of the nativeID property
                        return "tti_complete";
                    }

                    @Override
                    public void onViewFound(final View view) {
                        // Once we find the view, we also need to wait for it to be drawn
                        view.getViewTreeObserver()
                                // TODO (axe) Should be OnDrawListener instead of this
                                .addOnPreDrawListener(
                                        new ViewTreeObserver.OnPreDrawListener() {
                                            @Override
                                            public boolean onPreDraw() {
                                                view.getViewTreeObserver().removeOnPreDrawListener(this);
                                                long current = System.currentTimeMillis();
                                                MarkersStore.getInstance().logCustomMarker("TTI_COMPLETE", null, current);
                                                PerfLoggerModule.getInstance().invokeTTICompleted();
                                                return true;
                                            }
                                        });
                    }
                });
    }
}

