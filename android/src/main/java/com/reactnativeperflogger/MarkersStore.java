package com.reactnativeperflogger;

import androidx.annotation.NonNull;

import java.util.LinkedList;
import java.util.List;

class MarkersStore {
    private final List<MarkerRecord> records;

    MarkersStore() {
        records = new LinkedList<>();
    }

    synchronized void add(String name, String tag, int instanceKey, Long time) {
        records.add(new MarkerRecord(name, tag, instanceKey, time));
    }

    synchronized <T> T export(@NonNull MarkersExporter<T> exporter) {
        exporter.init();
        for (MarkerRecord record : records) {
            exporter.processRecord(record);
        }
        return exporter.finish();
    }

    synchronized void clear() {
        records.clear();
    }
}
