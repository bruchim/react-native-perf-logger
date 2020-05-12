package com.reactnativeperflogger;

import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.uimanager.util.ReactFindViewUtil;

import java.util.ArrayList;
import java.util.List;

class TTIEndNotifier {
    private final RNPerfLogger logger;
    private final List<TTIEndListener> listeners;
    private final List<ReactFindViewUtil.OnViewFoundListener> onViewFoundListeners;
    private boolean notified;

    TTIEndNotifier(@NonNull RNPerfLogger logger) {
        this.logger = logger;
        listeners = new ArrayList<>();
        onViewFoundListeners = new ArrayList<>();
    }

    void registerTTINativeIds(@NonNull ReadableArray ids) {
        notified = false;
        onViewFoundListeners.clear();
        for (int i = 0; i < ids.size(); i++) {
            String id = ids.getString(i);
            if (id != null) {
                onViewFoundListeners.add(addTTIEndListener(id));
            }
        }
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

    private void removeViewFoundListeners() {
        for (int i = 0; i < onViewFoundListeners.size(); i++) {
            ReactFindViewUtil.OnViewFoundListener listener = onViewFoundListeners.get(i);
            ReactFindViewUtil.removeViewListener(listener);
        }
        onViewFoundListeners.clear();
    }

    /**
     * Waits for Loading to complete, also called a Time-To-Interaction (TTI) event. To indicate TTI
     * completion, add a prop nativeID="tti_complete" to the component whose appearance indicates that
     * the initial TTI or loading is complete
     */
    private ReactFindViewUtil.OnViewFoundListener addTTIEndListener(@NonNull final String id) {
        ReactFindViewUtil.OnViewFoundListener listener = new ReactFindViewUtil.OnViewFoundListener() {
            @Override
            public String getNativeId() {
                // This is the value of the nativeID property
                return id;//"tti_complete";
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
                                        if (!notified) {
                                            notified = true;
                                            long time = System.currentTimeMillis();
                                            view.getViewTreeObserver().removeOnPreDrawListener(this);
                                            removeViewFoundListeners();
                                            logger.logCustomMarker(id, null, time);
                                            notifyListeners(id, time);
                                        }
                                        return true;
                                    }
                                });
            }
        };
        ReactFindViewUtil.addViewListener(listener);
        return listener;
    }

    synchronized private void notifyListeners(String id, long time) {
        for (TTIEndListener listener : listeners) {
            listener.ttiEnded(id, time);
        }
    }
}
