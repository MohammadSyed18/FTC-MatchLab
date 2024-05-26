# FTC matchlab

a Java command-line tool for comparing robot telemetry runs: path error, cycle consistency, motor imbalance, and reliability. it reads CSV and writes markdown reports.

```sh
mvn test
mvn -q package
java -cp target/classes dev.matchlab.Main sample/telemetry.csv
```

the included telemetry is synthetic. the thresholds are general diagnostics, not robot-specific calibration or measured results from a competition.
