package dev.matchlab;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/** Time-weighted metrics avoid treating a burst of samples as extra elapsed time. */
public final class TelemetrySession {
    public record CycleStats(int cycle, int samples, long observedMillis, long excludedGapMillis,
                             double rmsError, double maxError, double saturationFraction) {}
    public record Summary(List<CycleStats> cycles, long observedMillis, long excludedGapMillis,
                          double timeWeightedRmsError, int duplicateSamplesRemoved) {}

    public Summary analyze(List<TelemetryAnalyzer.Sample> input,long maximumGapMillis) {
        if (maximumGapMillis < 1) throw new IllegalArgumentException("maximum gap must be positive");
        var checked = new TelemetryQuality().checked(input);
        var groups = new TreeMap<Integer,List<TelemetryAnalyzer.Sample>>();
        for (var sample : checked) groups.computeIfAbsent(sample.cycle(),ignored -> new ArrayList<>()).add(sample);
        var cycles = new ArrayList<CycleStats>();
        long totalTime = 0, totalGaps = 0;
        double totalArea = 0;
        for (var entry : groups.entrySet()) {
            var samples = entry.getValue();
            long time = 0, gaps = 0;
            double errorArea = 0, saturatedArea = 0, maximum = 0;
            for (int i = 0; i < samples.size(); i++) {
                var current = samples.get(i);
                double error = error(current);
                maximum = Math.max(maximum,error);
                if (i == 0) continue;
                var previous = samples.get(i - 1);
                long duration = current.ms() - previous.ms();
                if (duration > maximumGapMillis) { gaps += duration; continue; }
                time += duration;
                errorArea += duration * (error * error + Math.pow(error(previous),2)) / 2;
                saturatedArea += duration * (saturated(previous) + saturated(current)) / 2;
            }
            cycles.add(new CycleStats(entry.getKey(),samples.size(),time,gaps,
                time == 0 ? 0 : Math.sqrt(errorArea/time),maximum,time == 0 ? 0 : saturatedArea/time));
            totalTime += time; totalGaps += gaps; totalArea += errorArea;
        }
        return new Summary(List.copyOf(cycles),totalTime,totalGaps,
            totalTime == 0 ? 0 : Math.sqrt(totalArea/totalTime),input.size()-checked.size());
    }
    private double error(TelemetryAnalyzer.Sample sample) {
        return Math.hypot(sample.x()-sample.targetX(),sample.y()-sample.targetY());
    }
    private double saturated(TelemetryAnalyzer.Sample sample) {
        return Math.max(Math.abs(sample.leftPower()),Math.abs(sample.rightPower())) >= .98 ? 1 : 0;
    }
}
