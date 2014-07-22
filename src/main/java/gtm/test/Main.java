package gtm.test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.textsim.exception.ProcessException;

import gtm.test.util.Constants;
import gtm.test.util.GTM;
import gtm.test.util.Jaccard;
import gtm.test.util.Measure;
import gtm.test.util.Pairs;

public class Main
{
    private static final int[] cases = {1000, 10000, 100000, 1000000, 10000000 };

    public static void main(String[] args)
            throws Exception
    {
        genData(new File(args[args.length - 1]), new File(args[args.length - 2]), new File(args[args.length - 3]));
        // testStage1();
        // testStage2();
    }
    
    private static void genData(File uniDir, File triDir, File outDir)
            throws Exception
    {
        // Print input files.
        System.out.println("Unigram files:");
        printFiles(uniDir.listFiles());
        System.out.println("Trigram files:");
        printFiles(triDir.listFiles());
        System.out.println("Output dir:\n\t" + outDir.getPath());
        // Generate data for proposed approach.
        // new gtm.test.stage1.DataGenerator().gen(uniDir, triDir, new File(outDir, "stage1"), "stage1");
        // new gtm.test.stage2.DataGenerator().gen(uniDir, triDir, new File(outDir, "stage2"), "stage2");
        // Generate data for string array approach.
        new gtm.test.stage1.StringArrayApproach(uniDir, triDir).write(new File(outDir, "stringArray"));
    }
    
    private static void testStage1()
            throws IOException, ProcessException
    {
        System.out.print("Start testing...");
        long initTime = System.currentTimeMillis();

        gtm.test.stage1.ProposedApproach proposedApproach =
                // new gtm.test.stage1.ProposedApproach(Constants.stage1Uni, Constants.stage1Tri);
                new gtm.test.stage1.ProposedApproach(Constants.aresUni, Constants.aresTri);
        gtm.test.stage1.Tester tester =
                new gtm.test.stage1.Tester(proposedApproach);
        
        tester.setMeasure(new Jaccard())
              .testAndWrite(new Pairs(Constants.pairs3), new FileWriter(new File(Constants.resourcesDir, "test1.jaccard")));
        tester.setMeasure(new GTM(proposedApproach.cMax()))
              .testAndWrite(new Pairs(Constants.pairs3), new FileWriter(new File(Constants.resourcesDir, "test1.gtm")));

        long termTime = System.currentTimeMillis();
        System.out.println("done! Time taken: " + (termTime - initTime) / 1000.0 + "s.");
    }
    
    public static void testStage1(File pairFile)
            throws IOException, ProcessException
    {

        System.out.println("Proposed Approach:\n");

        gtm.test.stage1.ProposedApproach proposedApproach =
                new gtm.test.stage1.ProposedApproach(Constants.stage1Uni, Constants.stage1Tri);
        gtm.test.stage1.Tester tester =
                new gtm.test.stage1.Tester(proposedApproach);
        Pairs pairs = new Pairs(pairFile);
        
        testStage1(tester, pairs, new Jaccard());
        testStage1(tester, pairs, new GTM(proposedApproach.cMax()));
    }
    
    public static void testStage1(gtm.test.stage1.Tester tester, Pairs pairs, Measure measure)
    {
        for (int c : cases)
            System.out.print(tester.setMeasure(measure).test(pairs.subrange(c)) + "\t");
        System.out.println();
    }

    private static void testStage2()
            throws IOException, ProcessException
    {
        System.out.print("Start testing...");
        long initTime = System.currentTimeMillis();

        gtm.test.stage2.Tester tester =
                // new gtm.test.stage2.Tester(Constants.stage2Uni, Constants.stage2Tri);
                new gtm.test.stage2.Tester(Constants.aresUni, Constants.aresTri);
        
        tester.testAndWrite(new Pairs(Constants.pairs3), new FileWriter(new File(Constants.resourcesDir, "test2.gtm")));

        long termTime = System.currentTimeMillis();
        System.out.println("done! Time taken: " + (termTime - initTime) / 1000.0 + "s.");
    }
    
    private static void printFiles(File... files)
    {
        List<File> list = Arrays.asList(files);
        Collections.sort(list, new Comparator<File>() {
            @Override
            public int compare(File f1, File f2) {
                String s1 = f1.getPath();
                String s2 = f2.getPath();
                int res = String.CASE_INSENSITIVE_ORDER.compare(s1, s2);
                return (res == 0 ? s1.compareTo(s2) : res);
            }
        });
        for (File file : files)
            System.out.println("\t" + file.getPath());
    }
}