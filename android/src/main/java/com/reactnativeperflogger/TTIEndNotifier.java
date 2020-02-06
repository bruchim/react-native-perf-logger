package com.reactnativeperflogger;

import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;

import com.facebook.react.uimanager.util.ReactFindViewUtil;

import java.util.ArrayList;
import java.util.List;

class TTIEndNotifier {
    private final RNPerfLogger logger;
    private final List<TTIEndListener> listeners;

    TTIEndNotifier(@NonNull RNPerfLogger logger) {
        this.logger = logger;
        listeners = new ArrayList<>();
        addTTIEndListener();
    }

    @SuppressWarnings("WeakerAccess")
    synchronized public void addListener(@NonNull TTIEndListener listener) {
        listeners.add(listener);
    }

    @SuppressWarnings("unused")
    synchronized public void removeListener(@NonNull TTIEndListener listener) {
        listeners.remove(listener);
    }

    @SuppressWarnings("WeakerAccess")
    synchronized public void removeAllListeners() {
        listeners.clear();
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
                                                logger.logCustomMarker("TTI_COMPLETE", null, System.currentTimeMillis());
                                                notifyListeners();
                                                return true;
                                            }
                                        });
                    }
                });
    }

    synchronized private void notifyListeners() {
        for (TTIEndListener listener : listeners) {
            listener.ttiEnded();
        }
    }
}
