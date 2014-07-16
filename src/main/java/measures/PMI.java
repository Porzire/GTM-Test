package measures;


public class PMI
        extends Measure
{
    private long n = 0;

    @Override
    public double sim(long w1Freq, long w2Freq, long triFreq)
    {
        return Math.log(((double)triFreq / n) / (((double)w1Freq / n) * ((double)w2Freq / n)))/ Math.log(2);
    }

    @Override
    public void setConst(double... vals)
    {
        this.n = (long) vals[0];
    }
}