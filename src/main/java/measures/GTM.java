package measures;


/**
 * The basic implementation of GTM in Java.
 * This class implements the GTM in a simple in-memory approach. It reads 
 * unigram and trigram into an array of strings.
 *
 * @author Jie Mei
 * @since  1.0 (5/22/2014)
 */
public class GTM
        extends Measure
{
    private static final double LOG_CONST = Math.log(1.01);
    
    private Long cMax = Long.MAX_VALUE;

    @Override
    public void setConst(double... vals)
    {
        this.cMax = (long)vals[0];
    }
    
    @Override
    public double getConst()
    {
        return cMax;
    }
    
    /**
     * Compute similarity between two given words.
     *
     * @param word1  A word, no particular order with word2.
     * @param word2  Another word, no particular order with word1.
     * @return The similarity between two words.
     */
    @Override
    public double sim(long freq1, long freq2, long triFreq)
    {
        long minFreq = (freq1 < freq2 ? freq1 : freq2);
        // Force computation!
        minFreq = (minFreq == 0 ? 1 : minFreq);
        double condition = ((double)triFreq * cMax * cMax) / (freq1 * freq2 * minFreq);
        double denominator =  -2 * Math.log((double)minFreq / cMax);
        if (condition > 1) {
            return Math.log(condition) / denominator;
        } else {
            return LOG_CONST / denominator;
        }
    }
}
