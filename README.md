# FTC matchlab

a Java command-line tool for comparing robot telemetry runs: path error, cycle consistency, motor imbalance, and reliability. it reads CSV and writes markdown reports.

```sh
mvn test
mvn -q package
java -cp target/classes dev.matchlab.Main sample/telemetry.csv
```

the included telemetry is synthetic. the thresholds are general diagnostics, not robot-specific calibration or measured results from a competition.

## Implementation details

`CycleTiming` derives observed cycle durations from telemetry timestamps and summarizes mean duration and sample variation independently of the sampling frequency.

## Time-weighted session diagnostics

[TelemetryQuality](src/main/java/dev/matchlab/TelemetryQuality.java) validates finite coordinates and bounded motor power, removes exact duplicate observations, and rejects conflicting observations at the same cycle/timestamp. [TelemetrySession](src/main/java/dev/matchlab/TelemetrySession.java) integrates squared path error over observed intervals and measures command saturation, per-cycle coverage, and excluded gaps.

[SessionReport](src/main/java/dev/matchlab/SessionReport.java) adds a second Markdown report to the command-line output. Long gaps and cycle boundaries do not count as measured motion. A cycle with zero observed duration is insufficient for a time-weighted estimate. Existing point-sample diagnostics remain available for comparison.

Tests cover unequal sampling intervals, duplicate/conflicting samples, missing intervals, saturation, finite inputs, and report generation. No physical-robot run is bundled or implied.
