package dev.matchlab;

import java.util.Locale;

public final class SessionReport {
    public String markdown(TelemetrySession.Summary summary) {
        var out = new StringBuilder("# Time-weighted telemetry\n\n");
        out.append("Observed intervals: ").append(summary.observedMillis()).append(" ms; excluded gaps: ")
            .append(summary.excludedGapMillis()).append(" ms; duplicate samples removed: ")
            .append(summary.duplicateSamplesRemoved()).append(".\n\n");
        out.append("| Cycle | Samples | Observed ms | Gap ms | RMS error | Peak error | Saturation |\n")
            .append("| --- | ---: | ---: | ---: | ---: | ---: | ---: |\n");
        for (var cycle : summary.cycles()) {
            out.append(String.format(Locale.ROOT,"| %d | %d | %d | %d | %.4f | %.4f | %.1f%% |%n",
                cycle.cycle(),cycle.samples(),cycle.observedMillis(),cycle.excludedGapMillis(),
                cycle.rmsError(),cycle.maxError(),100*cycle.saturationFraction()));
        }
        return out.append("\nIntervals across cycle boundaries and long telemetry gaps are excluded. Zero observed duration is insufficient for a time-weighted estimate.\n").toString();
    }
}
