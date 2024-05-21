# FTC-MatchLab

A Java command-line toolkit for inspecting synthetic or recorded FTC-style telemetry. It parses CSV logs; calculates RMS path error, cycle consistency, motor imbalance, reliability, and explainable faults; slices runs by time or cycle; compares candidate runs to a baseline; and formats Markdown reports.

Run `mvn test`, then `mvn -q package && java -cp target/classes dev.matchlab.Main sample/telemetry.csv`. The sample is synthetic. Thresholds are general diagnostics, not evidence of use by a particular team or a substitute for robot-specific calibration.
