package gtm.test;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.textsim.exception.ProcessException;

import gtm.test.util.Dice;
import gtm.test.util.GTM;
import gtm.test.util.Jaccard;
import gtm.test.util.Measure;
import gtm.test.util.NGD;
import gtm.test.util.PMI;
import gtm.test.util.Pairs;
import gtm.test.util.Simpson;

public class Main
{
//    private static final int[] cases = {100000, 500000, 1000000, 5000000, 10000000, 15000000, 20000000, 25000000, 30000000, 35000000};
    private static final int[] cases = {100, 200, 300, 400, 500, 600, 700, 800, 900, 1000};
    private static final float GB = 1024 * 1024 * 1024;

    public static void main(String[] args)
            throws Exception
    {
        File uniDir   = new File(args[args.length - 7]);
        File triDir   = new File(args[args.length - 6]);
        File s1Uni    = new File(args[args.length - 5]);
        File s1Tri    = new File(args[args.length - 4]);
        File s2Uni    = new File(args[args.length - 3]);
        File s2Tri    = new File(args[args.length - 2]);
        File pairFile = new File(args[args.length - 1]);

        System.out.println("================================== Test start ==================================");
        System.out.println("Arguments:");
        System.out.println("\tRaw Unigram directory: " + uniDir.getPath());
        System.out.println("\tRaw Trigram directory: " + triDir.getPath());
        System.out.println("\tStage1 Unigram :       " + s1Uni.getPath());
        System.out.println("\tStage1 Trigram :       " + s1Tri.getPath());
        System.out.println("\tStage2 Unigram :       " + s2Uni.getPath());
        System.out.println("\tStage2 Trigram :       " + s2Tri.getPath());
        System.out.println("\tPair file:             " + pairFile.getPath());

        // Test two stages.
        testStage1(uniDir, triDir, s1Uni, s1Tri, pairFile);
        testStage2(s2Uni, s2Tri, pairFile);

        System.out.println("=================================== Test end ===================================");
    }
    
    @SuppressWarnings("unused")
    private static void genData(File uniDir, File triDir, File outDir)
            throws Exception
    {
        System.out.println("------------------------------- Data Generation --------------------------------");
        // Print input files.
        System.out.println("Unigram files:");
        printFiles(uniDir.listFiles());
        System.out.println("Trigram files:");
        printFiles(triDir.listFiles());
        System.out.println("Output dir:\n\t" + outDir.getPath());

        // Generate data for proposed approach.
        new gtm.test.stage1.DataGenerator().gen(uniDir, triDir, new File(outDir, "stage1"), "stage1");
        new gtm.test.stage2.DataGenerator().gen(uniDir, triDir, new File(outDir, "stage2"), "stage2");
        
    }
    
    private static void testStage1(
            final File uniDir,
            final File triDir,
            final File preprocUni,
            final File preprocTri,
            final File pairFile)
            throws IOException, ProcessException
    {
        System.out.println("--------------------------------- Stage1 Test ----------------------------------");
        
        // Testing Requisites.
        System.out.print("Prepare testing... \r");
        List<Class<? extends gtm.test.stage1.Approach>> apps = Arrays.asList(
                gtm.test.stage1.ProposedApproach.class,
                gtm.test.stage1.StringArrayApproach.class,
                gtm.test.stage1.DirectAccessApproach.class
        );
        @SuppressWarnings("serial")
        HashMap<String, Measure> measures = new HashMap<String, Measure>() {{
                long cMax = new gtm.test.stage1.ProposedApproach(preprocUni, preprocTri).cMax();
                put("Jaccard", new Jaccard());
                put("Simpson", new Simpson());
                put("Dice   ", new Dice());
                put("PMI    ", new PMI(cMax));
                put("NGD    ", new NGD(cMax));
                put("GTM    ", new GTM(cMax));
        }};
        gtm.test.stage1.Tester tester = null;
        gtm.test.stage1.Approach approach = null;
        Runtime r = Runtime.getRuntime();
        Pairs pairs = new Pairs(pairFile);
        float before, after;
        System.out.print("                   \r");

        // Test all the approaches.
        for (Class<? extends gtm.test.stage1.Approach> app : apps) {
            System.out.print(app.getSimpleName() + ": Construct tester...\r");
            if (app == gtm.test.stage1.ProposedApproach.class) {
                continue;
                // approach = new gtm.test.stage1.ProposedApproach(preprocUni, preprocTri);
            } else if (app == gtm.test.stage1.StringArrayApproach.class) {
                approach = new gtm.test.stage1.StringArrayApproach(uniDir, triDir);
            } else if (app == gtm.test.stage1.DirectAccessApproach.class) {
                continue;
                // approach = new gtm.test.stage1.DirectAccessApproach(
                //         new gtm.test.stage1.ProposedApproach(preprocUni, preprocTri));
            }
            tester = new gtm.test.stage1.Tester(approach);
            // Test runtime time.
            System.out.print(app.getSimpleName() + ":                    \n\n\t");
            for (int c : cases)
                System.out.print(c + "\t");
            System.out.println();
            for (String name : measures.keySet()) {
                System.out.print(name + "\t");
                for (int c : cases) {
                    r.gc();
                    System.out.print(tester.setMeasure(measures.get(name)).test(pairs.subrange(c)) + "\t");
                }
                System.out.println();
            }
            // Test memory usage.
            r.gc();
            before = (r.totalMemory() - r.freeMemory()) / GB;
            approach = null;
            tester = null;
            r.gc();
            after = (r.totalMemory() - r.freeMemory()) / GB;
            System.out.println("Memory usage: " + (before - after) + " GB\n");
        }
    }

    private static void testStage2(
            final File preprocUni,
            final File preprocTri,
            final File pairFile)
            throws IOException, ProcessException
    {
        System.out.println("--------------------------------- Stage2 Test ----------------------------------");
        
        // Testing Requisites.
        gtm.test.stage2.Tester tester = new gtm.test.stage2.Tester(preprocUni, preprocTri);
        Pairs pairs = new Pairs(pairFile);
        Runtime r = Runtime.getRuntime();
        float before, after;
        // Test run time.
        for (int c : cases)
            System.out.print(c + "\t");
        System.out.println();
        for (int c : cases) {
            r.gc();
            System.out.print(tester.test(pairs.subrange(c)) + "\t");
        }
        System.out.println();
        // Test memory usage.
        r.gc();
        before = (r.totalMemory() - r.freeMemory()) / GB;
        tester = null;
        r.gc();
        after = (r.totalMemory() - r.freeMemory()) / GB;
        System.out.println("Memory usage: " + (before - after) + " GB\n");
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
