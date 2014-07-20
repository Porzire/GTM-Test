package gtm.test.stage1;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.textsim.util.FileUtil;
import org.textsim.util.Trigram;
import org.textsim.util.Unigram;
import org.textsim.wordrt.preproc.TrigramBinaryConvertion;
import org.textsim.wordrt.preproc.TrigramCountSummation;
import org.textsim.wordrt.preproc.TrigramExternalSort;
import org.textsim.wordrt.preproc.UnigramBinaryConversion;
import org.textsim.wordrt.preproc.UnigramPreprocess;

public class DataGenerator
        implements gtm.test.util.DataGenerator
{
    @Override
    public void gen(File uniDir, File triDir, File outDir, String name)
            throws Exception
    {
        long initTime, startTime, endTime;
        try {
            System.out.println();
            System.out.println("------------------------------------------------------------------------------");
            System.out.println("STAGE 1 CORPUS PREPROCESS");
            System.out.println();
            
            initTime = System.currentTimeMillis();
            
            String tempDir = new File(outDir, "temp").getPath();
            if (!outDir.exists())
                FileUtils.forceMkdir(outDir);
            if (!(new File(tempDir)).exists())
                FileUtils.forceMkdir(new File(tempDir));
            
            String unigramDir = uniDir.getAbsolutePath();
            String trigramDir = triDir.getAbsolutePath();
            
            /*STEP1 Unigram Pre-process */
            startTime = System.currentTimeMillis();
            System.out.print("STEP 1: Unigram Preprocess..");
            UnigramPreprocess.unigramGen(unigramDir, tempDir, ".*");
            endTime = System.currentTimeMillis();
            System.out.println("DONE!\n\tTime taken: " + ((endTime - startTime) / 1000.0) + " second.");
            /* STEP2 Trigram Count Summation */
            startTime = endTime;
            System.out.print("STEP 2: Trigram Count Summation..");
            TrigramCountSummation.TrigramGen(trigramDir, tempDir, ".*");
            endTime = System.currentTimeMillis();
            System.out.println("DONE!\n\tTime taken: " + ((endTime - startTime) / 1000.0) + " second.");
            /* STEP3 External sort */
            startTime = endTime;
            System.out.print("STEP 3 : External Sorting..");
            TrigramExternalSort.sortFile(tempDir);
            endTime = System.currentTimeMillis();
            System.out.println("DONE!\n\tTime Taken: " + ((endTime - startTime) / 1000.0) + " second.");
            startTime = endTime;
            /* STEP4 Frequency Print */
            System.out.print("STEP 4: Frequency Print..");
            FrequencyPrint.processT(tempDir);
            endTime = System.currentTimeMillis();
            System.out.println("DONE!\n\tTime taken: " + ((endTime - startTime) / 1000.0) + " second.");
            /* STEP5 Binary Conversion */
            startTime = System.currentTimeMillis();
            System.out.print("STEP 5: Binary Conversion...");
            UnigramBinaryConversion.process(tempDir);
            TrigramBinaryConvertion.process(tempDir);
            endTime = System.currentTimeMillis();
            System.out.println("DONE!\n\tTime taken: " + ((endTime - startTime) / 1000.0) + " second.");

            FileUtil.getFileInDirectoryWithSuffix(tempDir, UnigramBinaryConversion.SUFFIX).renameTo(
                    new File(outDir, name + '.' + Unigram.BINARY_SUFFIX));
            FileUtil.getFileInDirectoryWithSuffix(tempDir, TrigramBinaryConvertion.SUFFIX).renameTo(
                    new File(outDir, name + '.' + Trigram.BINARY_SUFFIX));

            endTime = System.currentTimeMillis();
            System.out.println("PREPROCESS Done! Total time taken: " + ((endTime - initTime) / 1000.0) + " second.");
            System.out.println("------------------------------------------------------------------------------");
            System.out.println();

        } catch (Exception e) {
            System.out.println();
            System.out.println("PREPROCESS fail");
            System.out.println("------------------------------------------------------------------------------");
            System.out.println();
            throw e;
        }
    }
}