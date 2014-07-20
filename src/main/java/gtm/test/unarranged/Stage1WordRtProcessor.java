package gtm.test.unarranged;

import org.textsim.exception.ProcessException;
import org.textsim.util.BinaryFileFastRecordReader;
import org.textsim.util.Unigram;
import org.textsim.wordrt.preproc.WordrtPreproc;
import org.textsim.wordrt.proc.AbstractWordRtProcessor;

import gtm.test.util.Measure;

import java.io.File;
import java.io.IOException;
import java.nio.MappedByteBuffer;

public class Stage1WordRtProcessor
        extends AbstractWordRtProcessor {

    private long[]    freqs;
    private Measure   sim;
    private int[]     tListStr;  // The start index of trigram sublist (inclusive)
    private int[]     tListEnd;  // The end index of trigram sublist (exclusive)
    private int[]     tListID;   // The ID of the third word in trigram
    private double[]  rt;        // The word relatedness of the trigram
    private int       uNum;      // The number of unigrams.
                                 // This variable is the length when construct tListStr and tListEnd.
                                 // Therefore the index of the arrrays represents the ID of the unigram.
    private int       tNum;      // The number of unordered trigram pairs
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
    public Stage1WordRtProcessor(File uniFile, File binTrigramFile, Measure sim)
            throws IOException, ProcessException {
    	
		BinaryFileFastRecordReader fastReader = new BinaryFileFastRecordReader(binTrigramFile);
        try {
            fastReader = new BinaryFileFastRecordReader(binTrigramFile);
    		MappedByteBuffer readerBuffer = fastReader.getBuffer();
    		long recordSize = readerBuffer.getLong();
    	    this.tNum = readerBuffer.getInt();
    	    this.uNum = readerBuffer.getInt();
        	this.tListStr = new int[this.uNum+1];
            this.tListEnd = new int[this.uNum+1];
            this.tListID  = new int[this.tNum];
            this.rt       = new double[this.tNum];
            this.sim = sim;
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
    					if(! fastReader.refillBuffer()) {
    						break;
    					}
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
            close(fastReader);
        }
        freqs = Unigram.readCounts(WordrtPreproc.BINARY, new File[]{uniFile});
        long cMax = 0;
        for (long freq : freqs) {
            cMax = (freq > cMax ? freq : cMax);
        }
    }
    
    private void close(BinaryFileFastRecordReader bffrr)
            throws IOException {

        if (bffrr != null)
            bffrr.close();
    }

	/*
	 * Look-up the relatedness of two given words/grams from in-memory data.
	 * 
	 * @see org.textsim.wordrt.proc.AbstractWordRtProcessor#sim(int, int)
	 */
	@Override
    public double sim(int word1, int word2) {
        
        if (word1 > word2) {
            int temp = word1;
            word1 = word2;
            word2 = temp;
        } else if (word1 == word2) {
            return 1;
        }
        // Binary search
        int indStr = tListStr[word1],
            indEnd = tListEnd[word1],
            indMid = 0;
        while (indStr <= indEnd) {
        	indMid = (indStr + indEnd) / 2;
        	if (tListID[indMid] == word2) {
            	double triFreq = rt[indMid];
            	return sim.sim(freqs[word1], freqs[word2], (long)triFreq);
        	}
            else if (tListID[indMid] > word2)
                indEnd = indMid - 1;
            else
                indStr = indMid + 1;
        }
        return 0;
    }
}
