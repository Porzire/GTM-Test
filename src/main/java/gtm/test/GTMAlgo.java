package gtm.test;

/**
 * The basic implementation of GTM in Java.
 * This class implements the GTM in a simple in-memory approach. It reads 
 * unigram and trigram into an array of strings.
 *
 * @author Jie Mei
 * @since  1.0 (5/22/2014)
 */
public class GTMAlgo
{
    private static final double LOG_CONST = Math.log(1.01);

    private Corpus corpus;
    
    public GTMAlgo(Corpus corpus)
    {
        this.corpus = corpus;
    }
    
    /**
     * Compute similarity between two given words.
     *
     * @param word1  A word, no particular order with word2.
     * @param word2  Another word, no particular order with word1.
     * @return The similarity between two words.
     */
    public double wordrt(String word1, String word2)
    {
        long freq = this.corpus.freq(word1, word2);
        if (freq == 0) {
            return 0;
        }
        long cMax = this.corpus.cMax();
        long freq1 = this.corpus.freq(word1);
        long freq2 = this.corpus.freq(word2);
        long minFreq = (freq1 < freq2 ? freq1 : freq2);
        double condition = (freq * cMax * cMax) / (freq1 * freq2 * minFreq);
        double denominator =  -2 * Math.log((double)minFreq / cMax);
        if (condition > 1) {
            return condition / denominator;
        } else {
            return LOG_CONST / denominator;
        }
    }
}
