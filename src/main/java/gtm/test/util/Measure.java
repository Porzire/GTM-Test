package gtm.test.util;


public abstract class Measure
{

    /**
     * Compute the relatedness of two words with their co-occurrence statistics.
     * 
     * @param  w1Freq   Occurrence statistic of one given word.
     * @param  w2Freq   Occurrence statistic of another given word.
     * @param  triFreq  Co-occurrence statistic of the given two words.
     * @return The relatedness of two words.
     */
    abstract public double sim(long w1Freq, long w2Freq, long triFreq);
    
    /**
     * Get the measure name.
     * 
     * @see java.lang.Object#toString()
     * @return The measure name.
     */
    @Override
    public String toString()
    {
        return this.getClass().getSimpleName();
    }
}