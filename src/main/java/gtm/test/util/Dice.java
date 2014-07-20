package gtm.test.util;


public class Dice
        extends Measure
{
    @Override
    public double sim(long w1Freq, long w2Freq, long triFreq)
    {
        return 2.0 * triFreq / (w1Freq + w2Freq);
    }
}