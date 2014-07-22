package gtm.test.stage1;

import gtm.test.util.Measure;
import gtm.test.util.Pair;
import gtm.test.util.Pairs;
import gtm.test.util.Timer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;

public class Tester
        extends gtm.test.util.Tester
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

    @Override
    public double test(Pairs pairs)
    {
        Timer.start();
        for (Pair pair : pairs)
            measure.sim(approach.freq(pair.word1), approach.freq(pair.word2),
                    approach.freq(pair.word1, pair.word2));
        Timer.end();
        return Timer.interval();
    }

    @Override
    public double testAndWrite(Pairs pairs, Writer out)
            throws IOException
    {
        Timer.start();
        try (BufferedWriter bw = new BufferedWriter(out)) {
            for (Pair pair : pairs)
                bw.write(pair.word1 + "\t" + pair.word2 + "\t"
                        + measure.sim(
                                approach.freq(pair.word1),
                                approach.freq(pair.word2),
                                approach.freq(pair.word1, pair.word2))
                        + "\n");
        }
        Timer.end();
        return Timer.interval();
    }
}
