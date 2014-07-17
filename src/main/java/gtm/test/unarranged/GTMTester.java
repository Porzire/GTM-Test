package gtm.test.unarranged;

import java.io.File;
import java.io.IOException;

import org.textsim.exception.ProcessException;
import org.textsim.textrt.preproc.SinglethreadTextRtPreprocessor;
import org.textsim.textrt.preproc.TextRtPreprocessor;
import org.textsim.textrt.preproc.tokenizer.PennTreeBankTokenizer;
import org.textsim.textrt.proc.singlethread.SinglethreadTextRtProcessor;
import org.textsim.textrt.proc.singlethread.TextRtProcessor;
import org.textsim.util.Unigram;
import org.textsim.util.token.DefaultTokenFilter;
import org.textsim.wordrt.preproc.WordrtPreproc;
import org.textsim.wordrt.proc.DefaultWordRtProcessor;
import org.textsim.wordrt.proc.WordRtProcessor;

import gnu.trove.map.hash.TObjectIntHashMap;


@SuppressWarnings("unused")
public class GTMTester {
	private static WordRtProcessor proc;
    private static TObjectIntHashMap<String> idMap;
    private static TextRtPreprocessor textpreproc;
    private static TextRtProcessor textproc;
    
    private static String[][] pairs = new String[][] {
            {"hello world", "hello god"}
    };
    
    public static void main(String[] args)
            throws ProcessException, IOException
    {
        initText(args[0], args[1]);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < pairs.length; i++) {
            double sim = textproc.sim(
                    ((SinglethreadTextRtPreprocessor)textpreproc).createSingleTextInstance(pairs[i][0]),
                    ((SinglethreadTextRtPreprocessor)textpreproc).createSingleTextInstance(pairs[i][1]));
            sb.append(pairs[i][0]).append(" ")
              .append(pairs[i][1]).append(" ")
              .append(sim).append(" ")
              .append((float)sim).append("\n");
        }
        System.out.println(sb.toString());
    }

    private static void initText(String uniFile, String triFile)
            throws IOException, ProcessException
    {
        textpreproc = new SinglethreadTextRtPreprocessor(uniFile, null, null, new PennTreeBankTokenizer(), new DefaultTokenFilter());
        textproc = new SinglethreadTextRtProcessor(triFile);
    }
    
    private static void init(File uniFile, File triFile)
            throws IOException, ProcessException
    {
        proc = new DefaultWordRtProcessor(triFile);
        idMap = Unigram.readIDMap(WordrtPreproc.BINARY, new File[]{uniFile});
    }

}
