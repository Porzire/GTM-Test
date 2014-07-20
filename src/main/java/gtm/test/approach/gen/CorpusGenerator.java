package gtm.test.approach.gen;

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

    public static void gen(
            int stage,
            String unigramDir,  // Unigram directory.
            String trigramDir,  // Trigram directory.
            String outputDir,   // Output directory.
            String name)        // Output file.
            throws Exception
    {
        unigramDir = new File(unigramDir).getAbsolutePath();
        trigramDir = new File(trigramDir).getAbsolutePath();
        long initTime, startTime, endTime;
        try {
            System.out.println();
            System.out.println("------------------------------------------------------------------------------");
            System.out.println("STAGE " + stage + " CORPUS PREPROCESS");
            System.out.println();
            
            initTime = System.currentTimeMillis();
            
            String tempDir = new File(outputDir, "temp").getPath();
            if (!new File(outputDir).exists())
                FileUtils.forceMkdir(new File(outputDir));
            if (!(new File(tempDir)).exists())
                FileUtils.forceMkdir(new File(tempDir));
            
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
            if (stage == 1) {
                /* STEP4 Frequency Print */
                System.out.print("STEP 4: Frequency Print..");
                FrequencyPrint.processT(tempDir);
            } else if (stage == 2) {
                /* STEP4 Word Relatedness Calculation */
                System.out.print("STEP 4: Word Relateness Calculation..");
                WordrtComputation.processT(tempDir);
            } else {
                throw new RuntimeException("Invalid stage number: " + stage);
            }
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

    public static void main(String[] args)
            throws Exception
    {
        System.out.println();
        System.out.println("------------------------------------------------------------------------------");
        System.out.println("PREPROCESS INFO");
        System.out.println();

//        String uniDir = args[args.length - 5];
//        String triDir = args[args.length - 4];
//        String outDir = args[args.length - 3];
//        String stg1Nm = args[args.length - 2];
//        String stg2Nm = args[args.length - 1];
//
        String uniDir     = args[args.length - 5];
        String triDir     = args[args.length - 4];
        String stg1Nm     = args[args.length - 2];
        String stg1OutDir = args[args.length - 3];
        String stg2Nm     = args[args.length - 1];
        String stg2OutDir = args[args.length - 3];

        System.out.println("Unigram Dir:\t " + uniDir);
        System.out.println("Trigram Dir:\t " + triDir);
        System.out.println("Stage 1:");
        System.out.println("\tCorpus Name: " + stg1Nm);
        System.out.println("\tOutput Dir:  " + stg1OutDir);
        System.out.println("Stage 2:");
        System.out.println("\tCorpus Name: " + stg2Nm);
        System.out.println("\tOutput Dir:  " + stg2OutDir);

        gen(1, uniDir, triDir, stg1OutDir, stg1Nm);
        gen(2, uniDir, triDir, stg1OutDir, stg1Nm);
    }
}
