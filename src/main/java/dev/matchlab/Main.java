package dev.matchlab;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length != 1) throw new IllegalArgumentException("usage: matchlab <telemetry.csv>");
        var analyzer = new TelemetryAnalyzer();
        var samples = analyzer.parse(Path.of(args[0]));
        System.out.print(new ReportFormatter().markdown(analyzer.analyze(samples)));
        System.out.print(new SessionReport().markdown(new TelemetrySession().analyze(samples,500)));
    }
}
