package com.reactnativeperflogger;

public class ReactNativePerfLogger {
    private RNMarkersListener rnMarkersListener;

    ReactNativePerfLogger() {
        rnMarkersListener = new RNMarkersListener();
        rnMarkersListener.initialize();

    }


    private static ReactNativePerfLogger _plInstance;
    private static ReactNativePerfLogger getInstance() {
        if (_plInstance == null) {
            _plInstance = new ReactNativePerfLogger();
        }
        return _plInstance;
    }

    public static void Initialize() {
        long current = System.currentTimeMillis();
        ReactNativePerfLogger.getInstance();
        MarkersStore.getInstance().logCustomStartMarker("Application",null, current);
    }

}
