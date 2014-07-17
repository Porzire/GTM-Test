package gtm.test;

import gtm.test.approach.Approach;
import gtm.test.approach.Pair;
import gtm.test.approach.Pairs;
import gtm.test.measure.Measure;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;

public class Tester
{
    Approach approach;
    Measure measure;

    public Tester(Approach approach)
    {
        this.approach = approach;
    }

    /**
     * Set measure for relatedness computation.
     */
    public Tester setMeasure(Measure measure)
    {
        this.measure = measure;
        return this;
    }

    /**
     * Run the test without writing out output.
     * This method get rid of I/O, is used for testing pure computation time.
     */
    public void test(Pairs pairs)
    {
        for (Pair pair : pairs)
            measure.sim(approach.freq(pair.word1), approach.freq(pair.word2),
                    approach.freq(pair.word1, pair.word2));
    }

    /**
     * Run the test and write out result.
     * This method is used for debugging the output.
     */
    public void testAndWrite(Pairs pairs, Writer out)
            throws IOException
    {
        try (BufferedWriter bw = new BufferedWriter(out)) {
            for (Pair pair : pairs)
                bw.write(pair.word1 + "\t" + pair.word2 + "\t"
                        + measure.sim(
                                approach.freq(pair.word1),
                                approach.freq(pair.word2),
                                approach.freq(pair.word1, pair.word2))
                        + "\n");
        }
    }
}
