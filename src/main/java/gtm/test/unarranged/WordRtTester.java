package gtm.test.unarranged;

import gtm.test.util.GTM;

import java.io.IOException;

import org.textsim.exception.ProcessException;

public class WordRtTester {
    
    public static float GB = 1024 * 1024 * 1024f;
    public static Runtime runtime = Runtime.getRuntime();

    public static void main(String[] args)
            throws IOException, ProcessException
    {
        System.out.println();
        System.out.println("------------------------------------------------------------------------------");
        System.out.println("PREPROCESS INFO");
        System.out.println();

        int linesToRead    = Integer.parseInt(args[args.length - 8]);
        String inFile      = args[args.length - 7];
        String uniFile1    = args[args.length - 6];
        String triFile1    = args[args.length - 5];
        String stg1Nm      = args[args.length - 4];
        String uniFile2    = args[args.length - 3];
        String triFile2    = args[args.length - 2];
        String stg2Nm      = args[args.length - 1];

        System.out.println();
        System.out.println("------------------------------------------------------------------------------");
        System.out.println(linesToRead + " Test");
        System.out.println();
        System.out.println("Stage1");
        System.out.println();
        System.out.println("Unigram File: " + uniFile1);
        System.out.println("Trigram File: " + triFile1);
        System.out.println("Input File:   " + inFile);
        System.out.println("Output File:  " + stg1Nm);
        System.out.println();
        System.out.println("Stage2");
        System.out.println();
        System.out.println("Unigram File: " + uniFile2);
        System.out.println("Trigram File: " + triFile2);
        System.out.println("Input File:   " + inFile);
        System.out.println("Output File:  " + stg2Nm);

        runtime.gc();
        stage1Test(uniFile1, triFile1, inFile, stg1Nm, linesToRead);
        runtime.gc();
        stage2Test(uniFile2, triFile2, inFile, stg2Nm, linesToRead);

        System.out.println("------------------------------------------------------------------------------");
        System.out.println();
    }
    
    public static void stage1Test(
            String uniPath,
            String triPath,
            String inPath,
            String outPath,
            int linesToRead)
            throws IOException, ProcessException
    {
        System.out.println();
        System.out.println("------------------------------------------------------------------------------");
        System.out.println("STAGE 1 " + linesToRead + " TEST");
        System.out.println();

        long strtTime, endTime;
        float strtMemo, endMemo;
        System.out.println("Build Data Structure");
        strtMemo = (runtime.totalMemory() - runtime.freeMemory()) / GB;
        strtTime = System.currentTimeMillis();
        // TODO: set correct parameter.
        AbstractStageWordSim ws = new Stage1WordSim(uniPath, triPath, new GTM(0));
        endTime = System.currentTimeMillis();
        endMemo = (runtime.totalMemory() - runtime.freeMemory()) / GB;
        // System.out.println("Time taken: " + (endTime - strtTime) / 1000.0 + "s");
        System.out.println("Memory taken: " + (endMemo - strtMemo) + "G");
        System.out.println();
        
        ws.loadInput(inPath, linesToRead);

        System.out.println("Run test");
        strtMemo = (runtime.totalMemory() - runtime.freeMemory()) / GB;
        strtTime = System.currentTimeMillis();
        // ws.runAndWrite(outPath);
        ws.run();
        endTime = System.currentTimeMillis();
        endMemo = (runtime.totalMemory() - runtime.freeMemory()) / GB;
        System.out.println("Time taken: " + (endTime - strtTime) / 1000.0 + "s");
        // System.out.println("Memory taken: " + (endMemo - strtMemo) / 1000.0 + "G");

        System.out.println();
        System.out.println("Done!");
    }
    
    public static void stage2Test(
            String uniPath,
            String triPath,
            String inPath,
            String outPath,
            int linesToRead)
            throws IOException, ProcessException
    {
        System.out.println();
        System.out.println("------------------------------------------------------------------------------");
        System.out.println("STAGE 2 " + linesToRead + " TEST");
        System.out.println();

        long strtTime, endTime;
        float strtMemo, endMemo;
        System.out.println("Build Data Structure");
        strtMemo = (runtime.totalMemory() - runtime.freeMemory()) / GB;
        strtTime = System.currentTimeMillis();
        AbstractStageWordSim ws = new Stage2WordSim(uniPath, triPath);
        endTime = System.currentTimeMillis();
        endMemo = (runtime.totalMemory() - runtime.freeMemory()) / GB;
        // System.out.println("Time taken: " + (endTime - strtTime) / 1000.0 + "s");
        System.out.println("Memory taken: " + (endMemo - strtMemo) + "G");
        System.out.println();
        
        ws.loadInput(inPath, linesToRead);

        System.out.println("Run test");
        strtMemo = (runtime.totalMemory() - runtime.freeMemory()) / GB;
        strtTime = System.currentTimeMillis();
        // ws.runAndWrite(outPath);
        ws.run();
        endTime = System.currentTimeMillis();
        endMemo = (runtime.totalMemory() - runtime.freeMemory()) / GB;
        System.out.println("Time taken: " + (endTime - strtTime) / 1000.0 + "s");
        // System.out.println("Memory taken: " + (endMemo - strtMemo) / 1000.0 + "G");

        System.out.println();
        System.out.println("Done!");
    }
}
