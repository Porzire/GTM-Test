package gtm.test.stage1;

import org.textsim.exception.ProcessException;
import org.textsim.util.BinaryFileFastRecordReader;
import org.textsim.util.Unigram;
import org.textsim.wordrt.preproc.WordrtPreproc;

import gnu.trove.map.hash.TObjectIntHashMap;

import java.io.File;
import java.io.IOException;
import java.nio.MappedByteBuffer;

public class ProposedApproach
        implements Approach
{
    // Unigram Data structure.
    protected long cMax;
    protected TObjectIntHashMap<String> idMap;
    protected long[] freqs;
    // Trigram Data structure.
    protected int[]     tListStr;  // The start index of trigram sublist (inclusive)
    protected int[]     tListEnd;  // The end index of trigram sublist (exclusive)
    protected int[]     tListID;   // The ID of the third word in trigram
    protected double[]  rt;        // The word relatedness of the trigram
    protected int       uNum;      // The number of unigrams.
                                   // This variable is the length when construct tListStr and tListEnd.
                                   // Therefore the index of the arrays represents the ID of the unigram.
    protected int       tNum;      // The number of unordered trigram pairs
                                   // This variable is the length when construct rt and tListID.
                                   // The index can be looked up in the tListStr and tListEnd.

    /**
     * Construct the object with pre-processed binary trigram file.
     *
     * @param binTrigramFile  the pre-processed binary trigram file.
     *
     * @throws IOException  when I/O error occurs.
     * @throws ProcessException 
     */
    public ProposedApproach(File uniFile, File triFile)
            throws IOException, ProcessException
    {
        // Load unigram data.
        Unigram.Data unigramData = Unigram.read(WordrtPreproc.BINARY, new File[] {uniFile});
        idMap = unigramData.unigramIDMap;
        freqs = unigramData.unigramCount;
        cMax = 0;
        for (long freq : freqs) {
            cMax = (freq > cMax ? freq : cMax);
        }
        // Load trigram data.
		BinaryFileFastRecordReader fastReader = new BinaryFileFastRecordReader(triFile);
        try {
            fastReader = new BinaryFileFastRecordReader(triFile);
    		MappedByteBuffer readerBuffer = fastReader.getBuffer();
    		long recordSize = readerBuffer.getLong();
    	    this.tNum = readerBuffer.getInt();
    	    this.uNum = readerBuffer.getInt();
        	this.tListStr = new int[this.uNum+1];
            this.tListEnd = new int[this.uNum+1];
            this.tListID  = new int[this.tNum];
            this.rt       = new double[this.tNum];
    		while (true) {
    			try {
    				//get next record length
    				recordSize = readerBuffer.getLong();
    				//check if EOF is reached
    				if (recordSize == 0) {
    					break;
    				} else if (readerBuffer.remaining() < recordSize) {
    					//check if buffer remaining size is bigger than record length
    					//refill buffer and check if buffer can be refill
    					if(! fastReader.refillBuffer())
    						break;
    					readerBuffer = fastReader.getBuffer();
    				}
    				//read record
    				int word1ID = readerBuffer.getInt();
    				int start = readerBuffer.getInt();
    				int end = readerBuffer.getInt();
    				this.tListStr[word1ID] = start;
    	    		this.tListEnd[word1ID] = end;
    	    		for (int i = start; i < end; i++) {
    	    			this.tListID[i] = readerBuffer.getInt();
    	    			this.rt[i] = readerBuffer.getDouble();
    	    		}
    			} catch (IOException e) {
    			}
    		}
        } finally {
            if (fastReader != null)
                fastReader.close();
        }
    }

    @Override
    public long freq(String gram)
    {
        return freqs[idMap.get(gram)];
    }

    @Override
    public long freq(String gram1, String gram2)
    {
        return freq(idMap.get(gram1), idMap.get(gram2));
    }
    
    protected long freq(int id1, int id2)
    {
        if (id1 > id2) {
            int temp = id1; id1 = id2; id2 = temp;
        }
        // Binary search
        int indStr = tListStr[id1],
            indEnd = tListEnd[id1],
            indMid = 0;
        while (indStr <= indEnd) {
        	indMid = (indStr + indEnd) / 2;
        	if (tListID[indMid] == id2) {
            	return (long)(rt[indMid] * 1000000);
        	}
            else if (tListID[indMid] > id2)
                indEnd = indMid - 1;
            else
                indStr = indMid + 1;
        }
        return 0;
    }
    
    @Override
    public long cMax()
    {
        return cMax;
    }
}