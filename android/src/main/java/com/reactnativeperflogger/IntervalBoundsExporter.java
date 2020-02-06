package com.reactnativeperflogger;

import android.util.ArrayMap;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;
import java.util.Map;

public class IntervalBoundsExporter implements MarkersExporter<JSONArray> {
    private Map<String, Interval> map;

    @Override
    public void init() {
        map = new ArrayMap<>();
    }

    @NonNull
    @Override
    public JSONArray finish() {
        JSONArray array = new JSONArray();
        Collection<Interval> intervals = map.values();

        try {
            for (Interval interval : intervals) {
                if (interval.endTime > 0) {
                    array.put(getIntervalBoundJSON(interval.name, interval.startTime, true));
                    array.put(getIntervalBoundJSON(interval.name, interval.endTime, false));
                }
            }
        } catch (JSONException e) {
            Log.w(RNPerfLogger.LOG_TAG, "Could not convert perf records to JSON", e);
        }

        return array;
    }

    @Override
    public void processRecord(@NonNull MarkerRecord record) {
        IntervalBound bound = intervalBound(record.getName(), record.getTag());
        if (bound != null) {
            if (bound.start) {
                map.put(bound.intervalName, new Interval(bound.intervalName, record.getTime()));
            } else {
                Interval interval = map.get(bound.intervalName);
                if (interval != null) {
                    interval.endTime = record.getTime();
                }
            }
        }
    }

    private class Interval {
        Interval(@NonNull String name, long startTime) {
            this.name = name;
            this.startTime = startTime;
        }

        String name;
        long startTime;
        long endTime;
    }

    private class IntervalBound {
        String intervalName;
        boolean start;

        IntervalBound(@NonNull String intervalName, boolean start) {
            this.intervalName = intervalName;
            this.start = start;
        }
    }

    @Nullable
    private IntervalBound intervalBound(@NonNull String markerName, @Nullable String tag) {
        markerName = markerName.toLowerCase();
        if (tag == null) {
            tag = "";
        } else if (tag.length() > 0) {
            tag = "[" + tag + "]";
        }


        String prefix = truncSuffixOrReturnNull(markerName, "_start");
        if (prefix != null) {
            return new IntervalBound(prefix + tag, true);
        }

        prefix = truncSuffixOrReturnNull(markerName, "_end");
        if (prefix != null) {
            return new IntervalBound(prefix + tag, false);
        }

        prefix = truncSuffixOrReturnNull(markerName, "_finish");
        if (prefix != null) {
            return new IntervalBound(prefix + tag, false);
        }

        return null;
    }

    @Nullable
    private String truncSuffixOrReturnNull(@NonNull String string, @NonNull String suffix) {
        return string.endsWith(suffix) ?
                string.substring(0, string.length() - suffix.length()) : null;
    }

    @NonNull
    private JSONObject getIntervalBoundJSON(@NonNull String name, long time, boolean start) throws JSONException {
        JSONObject result = new JSONObject();
        result.put("name", name);
        result.put("time", time);
        result.put("start", start);
        return result;
    }

}
