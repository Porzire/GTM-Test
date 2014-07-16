package measures;


public class Jaccard
        extends Measure {

    @Override
    public double sim(long w1Freq, long w2Freq, long triFreq)
    {
        return (double)triFreq / (w1Freq + w2Freq - triFreq);
    }

    @Override
    public void setConst(double... vals) { }
}