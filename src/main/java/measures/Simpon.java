package measures;


public class Simpon
        extends Measure
{

    @Override
    public double sim(long w1Freq, long w2Freq, long triFreq)
    {
        return (double)triFreq / (w1Freq <= w2Freq ? w1Freq : w2Freq);
    }

    @Override
    public void setConst(double... vals) { }
}