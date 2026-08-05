# FTC-MatchLab

A Java command-line toolkit for inspecting synthetic or recorded FTC-style telemetry. It parses CSV logs and calculates RMS path error, cycle consistency, left/right motor-command imbalance, autonomous completion reliability, and explainable threshold faults.

Run `mvn test`, then `mvn -q package && java -cp target/classes dev.matchlab.Main sample/telemetry.csv`. The sample is synthetic. Thresholds are general diagnostics, not evidence of use by a particular team or a substitute for robot-specific calibration.
