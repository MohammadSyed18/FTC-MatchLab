# FTC MatchLab

A Java command-line tool for comparing robot telemetry runs: path error, cycle consistency, motor imbalance, and reliability. It reads CSV and writes Markdown reports.

```sh
mvn test
mvn -q package
java -cp target/classes dev.matchlab.Main sample/telemetry.csv
```

The included telemetry is synthetic. The thresholds are general diagnostics, not robot-specific calibration or measured results from a competition.
