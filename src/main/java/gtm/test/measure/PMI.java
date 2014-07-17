package gtm.test.measure;


public class PMI
        extends Measure
{
    private long n;
    
    public PMI(long n)
    {
        this.n = n;
    }

    @Override
    public double sim(long w1Freq, long w2Freq, long triFreq)
    {
        return Math.log(((double)triFreq / n) / (((double)w1Freq / n) * ((double)w2Freq / n)))/ Math.log(2);
    }
}