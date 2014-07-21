package gtm.test.stage1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;

/**
 * String Array implementation of {@code Corpus}.
 * This class uses arrays to store the corpus data. The search algorithm uses binary search in
 * iterative paradigm.
 * <p>
 * Note that the index uses integer which is enough for Google Web-1T n-gram corpus (1.0). When
 * applying this class to larger corpus, change them long if the number of records exceeds the
 * integer range, which is likely to happen in trigram corpus.
 *
 * @author Jie Mei
 */
public class StringArrayApproach
        implements Approach, Serializable
{
    private static final long serialVersionUID = -988281163471228506L;

    /**
     * The unigram data.
     * Each line is a unigram record in Google n-gram corpus.
     */
    private String[] unigram; 

    /**
     * The trigram data.
     * Each line is a trigram record in Google n-gram corpus.
     */
    private String[] trigram;

    /**
     * The maximum frequency in unigram corpus.
     */
    private long cMax;

    /**
     * Read object from serialized file.
     * 
     * @param  serFile  Serialized file.
     * @return A StringArrayApproach object.
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static StringArrayApproach read(File serFile)
            throws FileNotFoundException, IOException, ClassNotFoundException
    {
        try (
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(serFile));
        ){
            return (StringArrayApproach) ois.readObject();
        }
    }

    /**
     * Constructs the object with input files.
     *
     * @param  uniDir  The unigram directory.
     * @param  triDir  The trigram directory.
     */
    public StringArrayApproach(File[] uniFiles, File[] triFiles)
            throws IOException
    {
        long init, term;
        // Read unigram and trigram into memory.
        System.out.println("Loading unigram corpus:");
        init = System.currentTimeMillis();
        this.unigram = read(uniFiles);
        term = System.currentTimeMillis();
        System.out.println("Time taken: " + (term - init) / 1000.0);
        System.out.println("Loading trigram corpus:");
        init = System.currentTimeMillis();
        this.trigram = read(triFiles);
        term = System.currentTimeMillis();
        System.out.println("Time taken: " + (term - init) / 1000.0);
        // Find cMax in unigram.
        System.out.print("Searching cMax: ");
        init = System.currentTimeMillis();
        this.cMax = 0;
        for (String line : this.unigram) {
            long freq = getFreq(line);
            this.cMax = (this.cMax > freq ? this.cMax : freq);
        }
        term = System.currentTimeMillis();
        System.out.println("Done!");
        System.out.println("Time taken: " + (term - init) / 1000.0);
    }
    
    public StringArrayApproach(File uniDir, File triDir)
            throws IOException
    {
        this(listFiles(uniDir), listFiles(triDir));
    }

    /**
     * Load n-gram data from directory into array.
     *
     * @param directory  The n-gram directory.
     */
    private static File[] listFiles(File directory)
            throws IOException
    {
        // Sort files by name.
        File[] files = directory.listFiles(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isFile() && !f.isHidden())
                    return true;
                else
                    return false;
            }
        });
        return files;
    }

    /*
     * This method applies binary search in the iterative paradigm.
     * 
     * @see gtm.basic.Corpus#freq(java.lang.String)
     */
    @Override
    public long freq(String gram)
    {
        int str = 0;
        int end = this.unigram.length - 1;
        while (str <= end) {
            int mid = (str + end) / 2;
            if (getGram(this.unigram[mid]).compareTo(gram) == 0)
                return getFreq(this.unigram[mid]);
            else if (getGram(this.unigram[mid]).compareTo(gram) > 0)
              end = mid - 1;
            else
              str = mid + 1;
        }
        // Return 0 if given gram does not exists in the unigram corpus.
        return 0;
    }

    /*
     * @see gtm.basic.Corpus#freq(java.lang.String, java.lang.String)
     */
    @Override
    public long freq(String gram1, String gram2)
    {
        return patternFreq(gram1, gram2) + patternFreq(gram2, gram1);
    }

    /**
     * Get the total frequency of the trigram pattern with specific heading and tailing gram.
     *
     * @param  gram1  The starting gram in the trigram.
     * @param  gram3  The starting gram in the trigram.
     * @return the total frequency of the trigram pattern with specific heading and tailing gram.
     */
    private long patternFreq(String gram1, String gram3)
    {
        long freq = 0;
        int[] range = startWith(gram1);
        if (range[0] == -1 || range[1] == -1)
            return 0;
        for (int i = range[0]; i <= range[1]; i++) {
            String line = this.trigram[i];
            if (getGrams(line)[2].equals(gram3))
                freq += getFreq(line);
        }
        return freq;
    }

    /**
     * Get the trigram data range starting with specific gram.
     * This method applies binary search in the iterative paradigm.
     *
     * @param gram  The starting gram in the trigram..
     * @return An integer array with two elements represent the starting and ending index in the
     * unigram corpus respectively.
     */
    private int[] startWith(String gram)
    {
        int[] range = {-1, -1};
        int[] temp = null;
        int str = 0;
        int end = this.trigram.length - 1;
        while (str <= end) {
            int mid = (str + end) / 2;
            String[] grams = getGrams(this.trigram[mid]);
            if (grams[0].compareTo(gram) == 0) {
                // Store str and end when first access, to avoid duplicate search steps.
                if (temp == null)
                    temp = new int[]{str, end};
                // Find the lower bound of the gram range.
                if (range[0] == -1) {
                    if (mid == 0 || getGrams(this.trigram[mid - 1])[0].compareTo(gram) < 0) {
                        range[0] = mid;
                        str = temp[0];
                        end = temp[1];
                    } else {
                        end = mid - 1;
                    }
                // Find the upper bound of the gram range.
                } else {
                    if (mid == this.trigram.length - 1 || getGrams(this.trigram[mid + 1])[0].compareTo(gram) > 0) {
                        range[1] = mid;
                        break;
                    } else {
                        str = mid + 1;
                    }
                }
            } else if (grams[0].compareTo(gram) < 0) {
                str = mid + 1;
            } else {
                end = mid - 1;
            }
        }
        return range;
    }

    /*
     * @see gtm.basic.Corpus#cMax()
     */
    @Override
    public long cMax()
    {
        return this.cMax;
    }

    /**
     * Get the n-gram in the data line.
     *
     * @param line  A line in corpus.
     * @return The n-gram in the line.
     */
    private String getGram(String line)
    {
        return line.split("\t")[0];
    }

    /**
     * Get the n-gram in the data line.
     *
     * @param line  A line in corpus.
     * @return An array of 1-grams in the line.
     */
    private String[] getGrams(String line)
    {
        return getGram(line).split(" ");
    }

    /**
     * Get the frequency in the data line.
     *
     * @param line  A line in corpus.
     * @return The n-gram in the line.
     */
    private long getFreq(String line)
    {
        return Long.parseLong(line.split("\t")[1]);
    }
    
    private static String[] read(File[] files)
            throws IOException
    {
        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File f1, File f2) {
                return f1.getName().compareTo(f2.getName());
            }
        });
        // Get total line number.
        int total = files.length;
        int count = 0;
        // Java only support array with 2^32 elements!
        int lines = 0;
        for (File file : files) {
            System.out.print("[" + (count++) + "/" + total + "] read count form " + file.getName() + " ...\n");
            try (LineNumberReader lnr = new LineNumberReader(new FileReader(file))) {
                while (lnr.read() != -1)
                    lnr.skip(Long.MAX_VALUE);
                lines += lnr.getLineNumber();
            }
        }
        // Read contents into array.
        count = 0;
        String[] data = new String[lines];
        int curr = 0;
        for (File file : files) {
            System.out.print("[" + (count++) + "/" + total + "] read content from " + file.getName() + " ...\n");
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line = null;
                while ((line = br.readLine()) != null)
                    data[curr++] = line;
            }
        }
        return data;
    }
    
    public void write(File out)
            throws IOException
    {
        try (
            ObjectOutputStream fos = new ObjectOutputStream(new FileOutputStream(out));
        ){
            fos.writeObject(this);
        }
    }
}