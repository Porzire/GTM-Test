package proposed;

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
import org.textsim.wordrt.preproc.WordrtComputation;

public class CorpusGenerator {

    public static void main(String[] args)
            throws Exception
    {
        System.out.println();
        System.out.println("------------------------------------------------------------------------------");
        System.out.println("PREPROCESS INFO");
        System.out.println();

        String uniDir = args[args.length - 5];
        String triDir = args[args.length - 4];
        String outDir = args[args.length - 3];
        String stg1Nm = args[args.length - 2];
        String stg2Nm = args[args.length - 1];

        System.out.println("Unigram Dir:        " + uniDir);
        System.out.println("Trigram Dir:        " + triDir);
        System.out.println("Output Dir:         " + outDir);
        System.out.println("Stage1 Corpus Name: " + stg1Nm);
        System.out.println("Stage2 Corpus Name: " + stg2Nm);

        genStage1Corpus(uniDir, triDir, outDir, stg1Nm);
        genStage2Corpus(uniDir, triDir, outDir, stg2Nm);
    }

    public static void genStage1Corpus(
            String unigramDir,  // Unigram directory.
            String trigramDir,  // Trigram directory.
            String outputDir,   // Output directory.
            String name)        // Output file.
            throws Exception
    {
        String tempDir = "temp";
        unigramDir = new File(unigramDir).getAbsolutePath();
        trigramDir = new File(trigramDir).getAbsolutePath();
        long initTime, startTime, endTime;
        try {
            System.out.println();
            System.out.println("------------------------------------------------------------------------------");
            System.out.println("STAGE 2 CORPUS PREPROCESS");
            System.out.println();
            
            initTime = System.currentTimeMillis();
            
            if (!new File(tempDir).exists())
                FileUtils.forceMkdir(new File(tempDir));
            if (!new File(outputDir).exists())
                FileUtils.forceMkdir(new File(outputDir));
            
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
            /* STEP4 Word Relatedness Calculation */
            startTime = endTime;
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
                    new File(outputDir, name + '.' + Unigram.BINARY_SUFFIX));
            FileUtil.getFileInDirectoryWithSuffix(tempDir, TrigramBinaryConvertion.SUFFIX).renameTo(
                    new File(outputDir, name + '.' + Trigram.BINARY_SUFFIX));

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

    public static void genStage2Corpus(
            String unigramDir,  // Unigram directory.
            String trigramDir,  // Trigram directory.
            String outputDir,   // Output directory.
            String name)        // Output file.
            throws Exception
    {
        String tempDir = "temp";
        unigramDir = new File(unigramDir).getAbsolutePath();
        trigramDir = new File(trigramDir).getAbsolutePath();
        long initTime, startTime, endTime;
        try {
            System.out.println();
            System.out.println("------------------------------------------------------------------------------");
            System.out.println("STAGE 2 CORPUS PREPROCESS");
            System.out.println();
            
            initTime = System.currentTimeMillis();
            
            if (!new File(tempDir).exists())
                FileUtils.forceMkdir(new File(tempDir));
            if (!new File(outputDir).exists())
                FileUtils.forceMkdir(new File(outputDir));
            
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
            /* STEP4 Word Relatedness Calculation */
            startTime = endTime;
            System.out.print("STEP 4: Word Relateness Calculation..");
            WordrtComputation.processT(tempDir);
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
                    new File(outputDir, name + '.' + Unigram.BINARY_SUFFIX));
            FileUtil.getFileInDirectoryWithSuffix(tempDir, TrigramBinaryConvertion.SUFFIX).renameTo(
                    new File(outputDir, name + '.' + Trigram.BINARY_SUFFIX));

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
