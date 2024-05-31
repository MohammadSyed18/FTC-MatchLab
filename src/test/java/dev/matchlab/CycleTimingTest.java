package dev.matchlab;
import static org.junit.jupiter.api.Assertions.*;import org.junit.jupiter.api.Test;import java.util.*;
class CycleTimingTest {
 TelemetryAnalyzer.Sample sample(long ms,int cycle){return new TelemetryAnalyzer.Sample(ms,0,0,0,0,0,0,cycle,true);}
 @Test void durationsUseTimeNotSamplingFrequency(){var result=new CycleTiming().summarize(List.of(sample(1000,1),sample(0,1),sample(500,1),sample(2000,2),sample(4000,2)));assertEquals(1500,result.meanMillis());assertEquals(707.10678,result.standardDeviationMillis(),.001);}
}
