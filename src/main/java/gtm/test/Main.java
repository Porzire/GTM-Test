package gtm.test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.textsim.exception.ProcessException;

import gtm.test.approach.Pairs;
import gtm.test.approach.ProposedApproach;
import gtm.test.measure.Jaccard;

public class Main
{
    public static void main(String[] args)
            throws IOException, ProcessException
    {
        System.out.print("Start testing...");
        long initTime = System.currentTimeMillis();

        new Stage1Tester(new ProposedApproach(Constants.stage1Uni, Constants.stage1Tri))
                .setMeasure(new Jaccard())
                .testAndWrite(new Pairs(Constants.pairs2), new FileWriter(new File(Constants.resourcesDir, "test1.out")));

        long termTime = System.currentTimeMillis();
        System.out.println("done! Time taken: " + (termTime - initTime) / 1000.0 + "s.");
    }
}
