package gtm.test.util;


public class NGD
        extends Measure
{
    private long n;
    
    public NGD(long n)
    {
        this.n = n;
    }
    
    @Override
    public double sim(long w1Freq, long w2Freq, long triFreq)
    {
        double w1log = Math.log(w1Freq);
        double w2log = Math.log(w2Freq);
        double trilog = Math.log(triFreq);
        double max = (w1log > w2log ? w1log : w2log);
        double min = (w1log < w2log ? w1log : w2log);
        return (max - trilog) / (Math.log(n) - min);
    }
}
