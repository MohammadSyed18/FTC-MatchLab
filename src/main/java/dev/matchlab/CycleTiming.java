package dev.matchlab;

import java.util.*;

public final class CycleTiming {
    public record Cycle(int id, long startMillis, long endMillis, int samples, boolean complete) {
        public long durationMillis() { return endMillis-startMillis; }
    }
    public record Summary(List<Cycle> cycles, double meanMillis, double standardDeviationMillis) {}
    public Summary summarize(List<TelemetryAnalyzer.Sample> samples) {
        var grouped=new TreeMap<Integer,List<TelemetryAnalyzer.Sample>>();
        for(var sample:samples) {
            if(sample.ms()<0)throw new IllegalArgumentException("negative telemetry timestamp");
            grouped.computeIfAbsent(sample.cycle(),ignored->new ArrayList<>()).add(sample);
        }
        var cycles=new ArrayList<Cycle>();
        for(var entry:grouped.entrySet()) {
            long start=entry.getValue().stream().mapToLong(TelemetryAnalyzer.Sample::ms).min().orElseThrow();
            long end=entry.getValue().stream().mapToLong(TelemetryAnalyzer.Sample::ms).max().orElseThrow();
            cycles.add(new Cycle(entry.getKey(),start,end,entry.getValue().size(),entry.getValue().stream().anyMatch(TelemetryAnalyzer.Sample::autoComplete)));
        }
        double mean=0,m2=0;int count=0;
        for(var cycle:cycles){double delta=cycle.durationMillis()-mean;mean+=delta/++count;m2+=delta*(cycle.durationMillis()-mean);}
        return new Summary(List.copyOf(cycles),mean,count>1?Math.sqrt(m2/(count-1)):0);
    }
}
