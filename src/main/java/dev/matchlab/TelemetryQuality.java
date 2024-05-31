package dev.matchlab;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;

/** Reject invalid samples and conflicting duplicate timestamps before scoring runs. */
public final class TelemetryQuality {
    public List<TelemetryAnalyzer.Sample> checked(List<TelemetryAnalyzer.Sample> input) {
        if (input.isEmpty()) throw new IllegalArgumentException("telemetry samples required");
        var unique = new LinkedHashMap<String,TelemetryAnalyzer.Sample>();
        for (var sample : input) {
            if (sample.ms() < 0 || sample.cycle() < 0
                    || !finite(sample.x(),sample.y(),sample.targetX(),sample.targetY(),sample.leftPower(),sample.rightPower())
                    || Math.abs(sample.leftPower()) > 1 || Math.abs(sample.rightPower()) > 1) {
                throw new IllegalArgumentException("invalid telemetry sample");
            }
            String key = sample.cycle() + ":" + sample.ms();
            var old = unique.putIfAbsent(key,sample);
            if (old != null && !old.equals(sample)) throw new IllegalArgumentException("conflicting sample at " + key);
        }
        return unique.values().stream().sorted(Comparator.comparingLong(TelemetryAnalyzer.Sample::ms)
            .thenComparingInt(TelemetryAnalyzer.Sample::cycle)).toList();
    }
    private boolean finite(double... values) {
        for (double value : values) if (!Double.isFinite(value)) return false;
        return true;
    }
}
