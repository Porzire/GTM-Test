package gtm.test.util;

import java.io.File;

public interface DataGenerator
{
    /**
     * Generate data file.
     * 
     * @param uniDir  Raw unigram directory.
     * @param triDir  Raw trigram directory.
     * @param outDir  Output directory.
     * @param name    The output file name.
     */
    public void gen(File uniDir, File triDir, File outDir, String name)
            throws Exception;
}
