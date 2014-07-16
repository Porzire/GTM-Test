package gtm.basic;

/**
 * Google n-gram corpus for GTM.
 * This class defines the public interface used in GTM. The concrete subclass contains the data
 * structure. GTM will only use {@code Corpus} to access the Google n-gram data.
 *
 * @author Jie Mei
 * @since  1.0
 */
public interface Corpus
{
    /**
     * Get the frequency for given gram in unigram corpus.
     *
     * @param gram  A gram.
     * @return The frequency in unigram corpus.
     */
    public long freq(String gram);

    /**
     * Get the total frequency for all trigram which
     * <ul>
     * <li> starting with <tt>gram1</tt> and ending with <tt>gram2</tt>
     * <li> starting with <tt>gram2</tt> and ending with <tt>gram1</tt>
     * </ul>
     *
     * @param gram1  A gram, has no particular order with gram2.
     * @param gram2  Another gram, has no particular order with gram1.
     * @return The total frequency of trigram with gram1 and gram2 at heading and tailing in trigram
     * corpus.
     */
    public long freq(String gram1, String gram2);

    /**
     * The maximum frequency in unigram corpus.
     */
    public long cMax();
}
