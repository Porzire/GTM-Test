package gtm.test.unarranged;

import gtm.test.util.Dice;
import gtm.test.util.GTM;
import gtm.test.util.Jaccard;
import gtm.test.util.Measure;
import gtm.test.util.NGD;
import gtm.test.util.PMI;
import gtm.test.util.Simpon;

import java.io.IOException;

import org.textsim.exception.ProcessException;

public class Stage1Tester {
    
    public static float GB = 1024 * 1024 * 1024f;
    public static Runtime runtime = Runtime.getRuntime();

    public static void main(String[] args)
            throws IOException, ProcessException
    {
        String simType  = args[args.length - 6];
        int linesToRead = Integer.parseInt(args[args.length - 5]);
        String inFile   = args[args.length - 4];
        String uniFile  = args[args.length - 3];
        String triFile  = args[args.length - 2];
        String stg1Nm   = args[args.length - 1];

        System.out.println("------------------------------------------------------------------------------");
        // System.out.println("SimType:      " + simType);
        // System.out.println("Unigram File: " + uniFile);
        // System.out.println("Trigram File: " + triFile);
        // System.out.println("Input File:   " + inFile);
        // System.out.println("Output File:  " + stg1Nm);

        // TODO: set correct parameter to PMI, NGD, and GTM.
        Measure sim = null;
        if (simType.equals("Jacard"))
            sim = new Jaccard();
        else if (simType.equals("Simpson"))
            sim = new Simpon();
        else if (simType.equals("Dice"))
            sim = new Dice();
        else if (simType.equals("PMI"))
            sim = new PMI(0);
        else if (simType.equals("NGD"))
            sim = new NGD(0);
        else if (simType.equals("GTM"))
            sim = new GTM(0);
        runtime.gc();
        stage1Test(uniFile, triFile, inFile, stg1Nm, sim, linesToRead);
    }
    
    public static void stage1Test(
            String uniPath,
            String triPath,
            String inPath,
            String outPath,
            Measure sim,
            int linesToRead)
            throws IOException, ProcessException
    {
        System.out.print(sim + " (" + linesToRead + ", ");

        long strtTime, endTime;
        AbstractStageWordSim ws = new Stage1WordSim(uniPath, triPath, sim);
        ws.loadInput(inPath, linesToRead);

        strtTime = System.currentTimeMillis();
        ws.runAndWrite(outPath);
        endTime = System.currentTimeMillis();
        System.out.println((endTime - strtTime) / 1000.0 + ")");
    }
}
