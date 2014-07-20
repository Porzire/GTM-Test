package gtm.test.stage1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import org.textsim.exception.ProcessException;
import org.textsim.util.FileUtil;
import org.textsim.util.IOUtil;
import org.textsim.wordrt.preproc.TrigramCountSummation;
import org.textsim.wordrt.preproc.TrigramExternalSort;
import org.textsim.wordrt.preproc.UnigramPreprocess;

/**
 * Forth step of pre-process to output two words with their relatedness.
 * <p>
 * <ol>
 * <li>read the output of step 3 which has sorted words id and their trigram frequency.
 * <li>read each word and its count from the Unigram file and store them into an array.
 * <li>add the frequency of two same entries in the output of step 3.
 * <li>compute the relatedness of words and store them in a file.
 * </ol>
 * The output does not have repeated entries, the pattern is:
 * <pre>
 * word1id  word2id relatedness 
 * </pre></p>
 *
 * @author  <a href="mailto:mei.jie@hotmail.com">Jie Mei</a>
 */
public class  FrequencyPrint
{
    /**
     * The suffix {@code String} of the output data file.
     */
    public static final String DATA_SUFFIX = "wcd";

    /**
     * The suffix {@code String} of the output count file.
     */
    public static final String COUNT_SUFFIX = "wcc";

    /**
     * {@code WordrtComputation} reads the output of {@code TrigramExternalSort} in the directory, compute and output to
     * the same directory.
     * 
     * @param  tempDir    The pathname {@code String} of the processing temporary directory.
     * @param  outputDir  The pathname {@code String} of the output directory.
     *
     * @throws IOException
     * @throws ProcessException 
     */
    public static void processT(String tempDir)
            throws FileNotFoundException, NoSuchElementException, IOException, ProcessException
    {
        File trigramSortedInterDataFile = FileUtil.getFileInDirectoryWithSuffix(tempDir, TrigramExternalSort.DATA_SUFFIX);
        File unigramDataFile = FileUtil.getFileInDirectoryWithSuffix(tempDir, UnigramPreprocess.DATA_SUFFIX);
        String trigramDataFilePathname = tempDir + File.separator + FileUtil.getFilenameWithoutSuffix(trigramSortedInterDataFile) + '.' + DATA_SUFFIX;
        String trigramCountFilePathname = tempDir + File.separator + FileUtil.getFilenameWithoutSuffix(trigramSortedInterDataFile) + '.' + COUNT_SUFFIX;
    
        try (
            BufferedReader trigramSortedInterData = new BufferedReader(new FileReader(trigramSortedInterDataFile));
            BufferedWriter trigramData = new BufferedWriter(new FileWriter(trigramDataFilePathname));
            BufferedWriter trigramCount = new BufferedWriter(new FileWriter(trigramCountFilePathname));
        ){
            String unigramDataName = FileUtil.getFilenameWithoutSuffix(unigramDataFile.getCanonicalPath(), UnigramPreprocess.DATA_SUFFIX);
            String trigramDataName = FileUtil.getFilenameWithoutSuffix(trigramDataFilePathname, TrigramCountSummation.DATA_SUFFIX);
            // Add header to the files
            IOUtil.writePreprocTrigramHeader(trigramData, new File(unigramDataName), new File(trigramDataName), "data");
            IOUtil.writePreprocTrigramHeader(trigramCount, new File(unigramDataName), new File(trigramDataName), "count");
            
            int gram1ID = 0, gram2ID = 0, prevGram1ID = 0, prevGram2ID = 0;
            long freq = 0;
            boolean isFirstGram = true;
    
            for (String inputLine; (inputLine = trigramSortedInterData.readLine()) != null; ) {
                StringTokenizer line = new StringTokenizer(inputLine);
    
                gram1ID = Integer.parseInt(line.nextToken());
                gram2ID = Integer.parseInt(line.nextToken());
    
                if (isFirstGram) {
                    prevGram1ID = gram1ID;
                    prevGram2ID = gram2ID;
                    freq = Long.parseLong(line.nextToken());
                    trigramData.write(Integer.toString(gram1ID));
                    isFirstGram = false;
                } else if (gram1ID == prevGram1ID && gram2ID == prevGram2ID) {
                    freq += Long.parseLong(line.nextToken());
                } else {
                    // Use frequency instead of relatedness.
                    if (freq > 0)
                        trigramData.write("\t" + prevGram2ID + "\t" + freq);
                    
                    if (prevGram1ID != gram1ID)
                        trigramData.write("\n" + Integer.toString(gram1ID));
                    
                    prevGram1ID = gram1ID;
                    prevGram2ID = gram2ID;
                    freq = Long.parseLong(line.nextToken());
                }
            }
            if (prevGram1ID != gram1ID)
                trigramData.write("\n" + gram1ID);
    
            // Use frequency instead of relatedness.
            if (freq > 0)
                trigramData.write("\t" + prevGram2ID + "\t" + freq);
        }
    }
}
