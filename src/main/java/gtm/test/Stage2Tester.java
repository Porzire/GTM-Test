package gtm.test;

import gnu.trove.map.hash.TObjectIntHashMap;
import gtm.test.approach.Pairs;

import java.io.File;
import java.io.IOException;
import java.io.Writer;

import org.textsim.util.Unigram;
import org.textsim.wordrt.preproc.WordrtPreproc;
import org.textsim.wordrt.proc.DefaultWordRtProcessor;
import org.textsim.wordrt.proc.WordRtProcessor;

/**
 * Tester for stage 2 of the proposed approach.
 *
 * @author Jie Mei
 */
public class Stage2Tester
        extends Tester
{
    private WordRtProcessor processor;
    private TObjectIntHashMap<String> idMap;

    public Stage2Tester(String uniFile, File triFile)
    {
        processor = new DefaultWordRtProcessor(triFile);
        idMap = Unigram.readIDMap(WordrtPreproc.BINARY, new File[]{uniFile});
    }

    @Override
    public void test(Pairs pairs)
    {
        for (Pair pair : pairs)
            processor.sim(idMap.get(pairs.word1), idMap.get(pairs.word2));
    }

    @Override
    public void testAndWrite(Pairs pairs, Writer out)
            throws IOException
    {
        try (
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outFile)));
        ){
            for (int i = 0; i < word1.length; i++) {
                double sim = processor.sim(idMap.get(word1[i]), idMap.get(word2[i]));
                bw.write(word1[i] + "\t" + word2[i] + "\t" + sim + "\n");
            }
        }
    }
}
