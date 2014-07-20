package gtm.test.util;

import java.io.File;

public class Constants
{
    public static final File resourcesDir = new File("etc");

    public static final File corpusDir = new File(resourcesDir, "corpus");
    public static final File stage1Uni = new File(corpusDir, "Stage1.uni");
    public static final File stage1Tri = new File(corpusDir, "Stage1.tri");
    public static final File stage2Uni = new File(corpusDir, "Stage2.uni");
    public static final File stage2Tri = new File(corpusDir, "Stage2.tri");

    public static final File googleNgramDir = new File(resourcesDir, "Google_Web_1T_5-gram");
    public static final File uniDir = new File(googleNgramDir, "1gm");
    public static final File triDir = new File(googleNgramDir, "3gms");

    public static final File pairsDir = new File(resourcesDir, "pairs");
    public static final File pairs1 = new File(pairsDir, "word-pair.txt");
    public static final File pairs2 = new File(pairsDir, "word-pair1.txt");
}
