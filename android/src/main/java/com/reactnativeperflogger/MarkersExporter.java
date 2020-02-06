package com.reactnativeperflogger;

import androidx.annotation.NonNull;

interface MarkersExporter<T> {
    void init();

    @NonNull
    T finish();

    void processRecord(@NonNull MarkerRecord record);
}
