package gtm.test.stage2;

import gnu.trove.map.hash.TObjectIntHashMap;
import gtm.test.util.Pair;
import gtm.test.util.Pairs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.Writer;

import org.textsim.exception.ProcessException;
import org.textsim.util.Unigram;
import org.textsim.wordrt.preproc.WordrtPreproc;
import org.textsim.wordrt.proc.DefaultWordRtProcessor;
import org.textsim.wordrt.proc.WordRtProcessor;

/**
 * Tester for stage 2 of the proposed approach.
 *
 * @author Jie Mei
 */
public class Tester
        extends gtm.test.util.Tester
{
    private WordRtProcessor processor;
    private TObjectIntHashMap<String> idMap;

    public Tester(File uniFile, File triFile)
            throws IOException, ProcessException
    {
        processor = new DefaultWordRtProcessor(triFile);
        idMap = Unigram.readIDMap(WordrtPreproc.BINARY, new File[]{uniFile});
    }

    @Override
    public void test(Pairs pairs)
    {
        for (Pair pair : pairs)
            processor.sim(idMap.get(pair.word1), idMap.get(pair.word2));
    }

    @Override
    public void testAndWrite(Pairs pairs, Writer out)
            throws IOException
    {
        try (
            BufferedWriter bw = new BufferedWriter(out);
        ){
            for (Pair pair : pairs) {
                double sim = processor.sim(idMap.get(pair.word1), idMap.get(pair.word2));
                bw.write(pair.word1 + "\t" + pair.word2 + "\t" + sim + "\n");
            }
        }
    }
}
