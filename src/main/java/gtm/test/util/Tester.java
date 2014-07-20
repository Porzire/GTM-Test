package gtm.test.util;

import java.io.IOException;
import java.io.Writer;

/**
 * Tester.
 */
public abstract class Tester
{
    /**
     * Run the test without writing out output.
     * This method get rid of I/O, is used for testing pure computation time.
     */
    public abstract void test(Pairs pairs);

    /**
     * Run the test and write out result.
     * This method is used for debugging the output.
     */
    public abstract void testAndWrite(Pairs pairs, Writer out)
            throws IOException;
}
