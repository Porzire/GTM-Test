package gtm.test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.textsim.exception.ProcessException;

import gtm.test.approach.Approach;
import gtm.test.approach.Pairs;
import gtm.test.approach.ProposedApproach;
import gtm.test.measure.Jaccard;

public class CorrectnessTest
{
    public static void main(String[] args)
            throws IOException, ProcessException
    {
        System.out.println("Start testing...");
        long initTime = System.currentTimeMillis();
        
        long strTime, endTime;

        System.out.print("Build data structure ");
        strTime = System.currentTimeMillis();
        Approach approach = new ProposedApproach(Constants.stage1Uni, Constants.stage1Tri);
        endTime = System.currentTimeMillis();
        System.out.println("takes " + (endTime - strTime) / 1000.0 + " s.");
        System.out.println("cMax: " + approach.cMax());

        System.out.print("Load pairs ");
        strTime = System.currentTimeMillis();
        Pairs pairs = new Pairs(Constants.pairs2);
        endTime = System.currentTimeMillis();
        System.out.print(pairs.size() + " ");
        System.out.println("takes " + (endTime - strTime) / 1000.0 + " s.");

        System.out.print("Create tester ");
        strTime = System.currentTimeMillis();
        Stage1Tester tester = new Stage1Tester(approach).setMeasure(new Jaccard());
        endTime = System.currentTimeMillis();
        System.out.println("takes " + (endTime - strTime) / 1000.0 + " s.");

        System.out.print("Pure test ");
        strTime = System.currentTimeMillis();
        tester.test(pairs);
        endTime = System.currentTimeMillis();
        System.out.println("takes " + (endTime - strTime) / 1000.0 + " s.");

        System.out.print("Test and write ");
        strTime = System.currentTimeMillis();
        tester.testAndWrite(pairs, new FileWriter(new File(Constants.resourcesDir, "test1.out")));
        endTime = System.currentTimeMillis();
        System.out.println("takes " + (endTime - strTime) / 1000.0 + " s.");

        long termTime = System.currentTimeMillis();
        System.out.println("Overall time taken: " + (termTime - initTime) / 1000.0 + "s.");
    }
}
