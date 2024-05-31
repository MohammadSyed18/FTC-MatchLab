package dev.matchlab;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class TelemetrySessionTest {
    private TelemetryAnalyzer.Sample sample(long time,double x) {
        return new TelemetryAnalyzer.Sample(time,x,0,0,0,1,.5,1,false);
    }
    @Test void weightsActualTimeAndExcludesLongGaps() {
        var a=sample(0,0);var b=sample(100,2);var c=sample(1100,2);
        var summary=new TelemetrySession().analyze(List.of(c,b,a,b),200);
        assertEquals(100,summary.observedMillis());
        assertEquals(1000,summary.excludedGapMillis());
        assertEquals(Math.sqrt(2),summary.timeWeightedRmsError(),1e-8);
        assertEquals(1,summary.duplicateSamplesRemoved());
        assertEquals(1,summary.cycles().getFirst().saturationFraction(),1e-8);
        assertTrue(new SessionReport().markdown(summary).contains("1000"));
    }
    @Test void rejectsConflictsAndNonfiniteSamples() {
        assertThrows(IllegalArgumentException.class,()->new TelemetryQuality().checked(List.of(sample(0,1),sample(0,2))));
        assertThrows(IllegalArgumentException.class,()->new TelemetryQuality().checked(List.of(sample(0,Double.NaN))));
    }
}
