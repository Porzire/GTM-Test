package gtm.test.util;

public class Timer
{
    private static long strTime;
    private static long endTime;

    public static void start()
    {
        strTime = System.currentTimeMillis();
    }

    public static void end()
    {
        endTime = System.currentTimeMillis();
    }
    
    public static double interval()
    {
        return (endTime - strTime) / 1000.0;
    }
}
