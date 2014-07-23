package gtm.test.util;


public class Simpson
        extends Measure
{
    @Override
    public double sim(long w1Freq, long w2Freq, long triFreq)
    {
        return (double)triFreq / (w1Freq <= w2Freq ? w1Freq : w2Freq);
    }
}