package gtm.test.util;

import java.io.File;

public class Constants
{
    public static final File resourcesDir = new File("etc");

    // Processed corpus.
    public static final File preprocDir = new File(resourcesDir, "preproc");
    public static final File corpusDir = new File(preprocDir, "test");
    public static final File stage1Uni = new File(corpusDir, "stage1/stage1.uni");
    public static final File stage1Tri = new File(corpusDir, "stage1/stage1.tri");
    public static final File stage2Uni = new File(corpusDir, "stage2/stage2.uni");
    public static final File stage2Tri = new File(corpusDir, "stage2/stage2.tri");
    public static final File aresCorpusDir = new File(preprocDir, "ares");
    public static final File aresUni = new File(aresCorpusDir, "corpus.uni");
    public static final File aresTri = new File(aresCorpusDir, "corpus.tri");

    // Google web 1T n-gram corpus.
    public static final File googleNgramDir = new File(resourcesDir, "Google_Web_1T_5-gram");
    public static final File uniDir = new File(googleNgramDir, "1gm");
    public static final File triDir = new File(googleNgramDir, "3gms");

    // Word pairs.
    public static final File pairsDir = new File(resourcesDir, "pairs");
    public static final File pairs1 = new File(pairsDir, "word-pair.txt");
    public static final File pairs2 = new File(pairsDir, "word-pair1.txt");
    public static final File pairs3 = new File(pairsDir, "word-pair2.txt");
}
