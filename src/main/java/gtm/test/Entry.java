package gtm.basic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

public class Entry
{
    private static float MB = 1024*1024f;
    
    private static String[] word1;
    private static String[] word2;

    public static void main(String[] args)
            throws IOException
    {
        test(args);
    }

    public static void test(String[] args)
            throws IOException
    {
        run(args[args.length - 4],
            args[args.length - 3],
            args[args.length - 2],
            Integer.parseInt(args[args.length - 1]));

        float size = size(args[0]) + size(args[1]);
        System.out.println("File size: " + size + " MB");
    }
    
    private static float size(String directory) {
        File[] files = new File(directory).listFiles(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isFile() && !f.isHidden()) {
                    return true;
                }
                return false;
            }
        });
        long size = 0;
        for (File file : files) {
            size += file.length();
        }
        return size / MB;
    }

    public static void run(String uniDir, String triDir, String inFile, int linesToRead)
            throws IOException
    {
        // Load corpus data into memory.
        Runtime runtime = Runtime.getRuntime();
        float str = (runtime.totalMemory() - runtime.freeMemory()) / MB;
        GTM gtm = new GTM(new ArrayCorpus(new File(uniDir), new File(triDir)));
        float end = (runtime.totalMemory() - runtime.freeMemory()) / MB;
        System.out.println("Memory used: " + (end - str) + " MB");

        // Read word pairs into memory.
        long init, term;
        System.out.println("Reading word pairs into memory:");
        init = System.currentTimeMillis();
        loadInput(inFile, linesToRead);
        term = System.currentTimeMillis();
        System.out.println("Time taken: " + (term - init) / 1000.0);

        // Generate Result.
        System.out.println("Compute similarities for " + word1.length + " pairs : ");
        init = System.currentTimeMillis();
        // double[] result = new double[word1.length];
        for (int i = 0; i < word1.length; i++) {
            // result[i] = gtm.wordrt(word1[i], word2[i]);
            gtm.wordrt(word1[i], word2[i]);
        }
        term = System.currentTimeMillis();
        System.out.println("Time taken: " + (term - init) / 1000.0);

        // Write out result.
//        System.out.println("Write out results : ");
//        init = System.currentTimeMillis();
//        BufferedWriter bw = null;
//        try {
//            bw = new BufferedWriter(new FileWriter(outFile));
//            for (int i = 0; i < word1.length; i++) {
//                bw.write(word1[i] + " " + word2[i] + " " + result[i] + "\n");
//            }
//        } finally {
//            if (bw != null) {
//                bw.close();
//            }
//        }
//        term = System.currentTimeMillis();
//        System.out.println("Time taken: " + (term - init) / 1000.0);
    }
    
    
    public static void loadInput(String inFile, int linesToRead)
            throws IOException
    {
        int lines = readLines(inFile);
        if (linesToRead != -1 && linesToRead < lines) {
            lines = linesToRead;
        }
        word1 = new String[lines];
        word2 = new String[lines];
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(new File(inFile)));
            String line = null;
            int i = 0;
            while ((line = br.readLine()) != null && i < linesToRead) {
                line = line.trim();
                String[] tokens = line.split("\\s+");
                word1[i] = tokens[0];
                word2[i] = tokens[1];
                i++;
            }
        } finally {
            if (br != null)
                br.close();
        }
    }
    
    private static int readLines(String inFile)
            throws IOException
    {
        int lines = 0;
        LineNumberReader lnr = null;
        try {
            lnr = new LineNumberReader(new FileReader(new File(inFile)));
            while (lnr.read() != -1) {
                lnr.skip(Long.MAX_VALUE);
            }
            lines += lnr.getLineNumber();
        } finally {
            if (lnr != null) {
                lnr.close();
            }
        }
        return lines;
    }
}
