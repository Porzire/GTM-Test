package gtm.test;

import gtm.test.util.Constants;

import org.textsim.wordrt.preproc.WordrtPreproc;

public class GenerateOrigCorpusTest
{
    public static void main(String[] args)
            throws Exception
    {
        WordrtPreproc.preprocessCorpus(WordrtPreproc.BINARY,
                Constants.uniDir.getPath(), Constants.triDir.getPath(),
                "orig_out", "orig_temp", "orig_corpus", ".*");
    }
}
