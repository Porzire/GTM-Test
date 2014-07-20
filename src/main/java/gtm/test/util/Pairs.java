package gtm.test.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Iterator;
import java.util.RandomAccess;

/**
 * This class maintains word pairs and operations for testing.
 * 
 * @author Jie Mei
 */
public class Pairs
        implements RandomAccess, Iterable<Pair>
{
    private String[][] pairs;

    /**
     * Construct the object with word pairs.
     * 
     * @param  pairs  The word pairs.
     */
    public Pairs(String[][] pairs)
    {
        this.pairs = pairs;
    }
    
    /**
     * Read pairs from files.
     * Each line in the input file should have the following format:
     * <pre>
     *     word <DELEMETER> word <NEWLINE>
     * </pre>
     * This method splits the words by the regular expression and reads specific
     * lines in the input file. If the request number of lines is negative or
     * beyond the total number of file lines, it reads all the pairs in the file.
     * 
     * @param  file         The input file.
     * @param  linesToRead  The number of lines to be read.
     * @param  regex        The regular expression of the delimiter.
     */
    public Pairs(File file, int linesToRead, String regex)
            throws IOException
    {
        int lines = 0;
        
        try (LineNumberReader lnr = new LineNumberReader(new FileReader(file))) {
            while (lnr.read() != -1)
                lnr.skip(Long.MAX_VALUE);
            lines += lnr.getLineNumber();
        }
        if (linesToRead < 0 || linesToRead > lines)
            linesToRead = lines;
        pairs = new String[linesToRead][2];
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            int i = 0;
            while (i < linesToRead)
                pairs[i++] = br.readLine().trim().split(regex);
        }
    }

    /**
     * Construct the object with specific number of pairs from file.
     * 
     * @param  file         The input file.
     * @param  linesToRead  The number of lines to be read.
     * @throws IOException
     */
    public Pairs(File file, int linesToRead)
            throws IOException
    {
        this(file, linesToRead, "\\s+");
    }

    /**
     * Construct the object with pairs from file.
     * 
     * @param  file         The input file.
     * @param  linesToRead  The number of lines to be read.
     * @throws IOException
     */
    public Pairs(File file)
            throws IOException
    {
        this(file, -1, "\\s+");
    }
    
    /**
     * Get the total number of pairs.
     * 
     * @return the total number of pairs.
     */
    public int size()
    {
        return pairs.length;
    }
    
    /**
     * Get pair by index.
     * 
     * @param  index  The index of the pair.
     * @return A word pair.
     */
    public Pair get(int index)
    {
        return new Pair(pairs[index]);
    }
    
    /**
     * Get a new {@code Pairs} that contains a subrange data of this pairs.
     * 
     * @param  strIndex  The start of the subrange.
     * @param  endIndex  The end of the subrange.
     * @return A new {@code Pairs}.
     */
    public Pairs subrange(int strIndex, int endIndex)
    {
        String[][] sub = new String[endIndex - strIndex][2];
        for (int i = 0; i < sub.length; i++)
            sub[i] = pairs[i + strIndex];
        return new Pairs(sub);
    }

    /**
     * Get a new {@code Pairs} that contains a subrange data of this pairs.
     * 
     * @param  size  The size of the subrange.
     * @return A new {@code Pairs}.
     */
    public Pairs subrange(int size)
    {
        return subrange(0, size);
    }

    /**
     * Returns an iterator over the elements in this list in proper sequence.
     * This iterator does NOT have concurrent feature.
     *
     * @return An iterator over the elements in this list in proper sequence 
     */
    @Override
    public Iterator<Pair> iterator()
    {
        return new PairIterator();
    }
    
    private class PairIterator
            implements Iterator<Pair>
    {
        private int next;

        private PairIterator()
        {
            next = 0;
        }

        @Override
        public boolean hasNext()
        {
            return pairs.length > next ? true : false;
        }

        @Override
        public Pair next()
        {
            return new Pair(pairs[next++]);
        }

        @Override
        public void remove()
        {
            // TODO Add concurrent feature.
        }
    }
}
