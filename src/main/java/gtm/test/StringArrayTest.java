package gtm.test;

import gtm.test.util.Constants;

import java.io.File;
import java.io.IOException;

import org.textsim.exception.ProcessException;

@SuppressWarnings("unused")
public class StringArrayTest
{
    private static final int[] cases = {1000, 10000, 100000, 500000, 1000000, 10000000 ,15000000 , 20000000};
    private static final float GB = 1024 * 1024 * 1024;

    private static final String S1_NAME = "stage1";
    private static final String S2_NAME = "stage2";
    private static final String UNI_SUFFIX = "uni";
    private static final String TRI_SUFFIX = "tri";
    
    public static void main(String[] args)
            throws IOException, ProcessException
    {
        File uniDir = Constants.uniDir;
        File triDir = Constants.triDir;
        File outDir = Constants.corpusDir;
        File pairFile = Constants.pairs1;
        File s1OutDir = new File(outDir, S1_NAME);
        File s1Uni = new File(s1OutDir, S1_NAME + "." + UNI_SUFFIX);
        File s1Tri = new File(s1OutDir, S1_NAME + "." + TRI_SUFFIX);
        File s2OutDir = new File(outDir, S2_NAME);
        File s2Uni = new File(s2OutDir, S2_NAME + "." + UNI_SUFFIX);
        File s2Tri = new File(s2OutDir, S2_NAME + "." + TRI_SUFFIX);

        gtm.test.stage1.ProposedApproach proposedApproach =
                new gtm.test.stage1.ProposedApproach(s1Uni, s1Tri);
        System.out.println("Proposed approach taken: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / GB + " GB.");
        gtm.test.stage1.DirectAccessApproach directApproach = 
                new gtm.test.stage1.DirectAccessApproach(proposedApproach);
        System.gc();
        System.out.println("Before destruct: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / GB + " GB.");
        float before = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / GB;
        directApproach = null;
        System.gc();
        System.out.println("After destruct: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / GB + " GB.");
        float after = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / GB;
        System.out.println("Memory taken: " + (before - after) + " GB.");
    }
}
