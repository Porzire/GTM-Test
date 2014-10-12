package gtm.test.stage1;

import gnu.trove.map.hash.TIntLongHashMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.map.hash.TObjectIntHashMap;

import java.io.File;
import java.io.IOException;
import java.nio.MappedByteBuffer;

import org.textsim.exception.ProcessException;
import org.textsim.util.BinaryFileFastRecordReader;
import org.textsim.util.Unigram;
import org.textsim.wordrt.preproc.WordrtPreproc;

public class NestedHashApproach
        implements Approach
{
    // Unigram Data structure.
    protected long cMax;
    protected TObjectIntHashMap<String> idMap;
    protected long[] freqs;
    // Trigram Data structure.
    protected TIntObjectHashMap<TIntLongHashMap> rtMap;
    
    public NestedHashApproach(File uniFile, File triFile)
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
    	this.rtMap = new TIntObjectHashMap<TIntLongHashMap>();
        try {
            fastReader = new BinaryFileFastRecordReader(triFile);
            MappedByteBuffer readerBuffer = fastReader.getBuffer();
            long recordSize = readerBuffer.getLong();
		    readerBuffer.getInt();  // Omit the total trigram count.
		    readerBuffer.getInt();  // Omit the total unigram count.
			while(true){
				try {
					//get next record length
					recordSize = readerBuffer.getLong();
					//check if EOF is reached
					if(recordSize == 0){
						break;
					}else if(readerBuffer.remaining() < recordSize){
						//check if buffer remaining size is bigger than record length
						//refill buffer and check if buffer can be refill
						if(! fastReader.refillBuffer()){
							break;
						}
						readerBuffer = fastReader.getBuffer();
					}
					//read record
					int w1ID = readerBuffer.getInt();
					int start = readerBuffer.getInt();
					int end = readerBuffer.getInt();
					TIntLongHashMap w3Map = new TIntLongHashMap();
		    		for (int i = start; i < end; i++) {
		    			w3Map.put(readerBuffer.getInt(), (long)readerBuffer.getDouble());
		    		}
		    		// NOTE: Use this data only for testing!
		    		this.rtMap.put(w1ID, w3Map);
				} catch (IOException e) {
				}
			}
        } finally {
            if (fastReader != null)
                fastReader.close();
        }
    }

    @Override
    public long freq(String gram) {
        return freqs[idMap.get(gram)];
    }

    @Override
    public long freq(String gram1, String gram2)
    {
        int id1 = idMap.get(gram1);
        int id2 = idMap.get(gram1);
    	if (id1 > id2) {
            int temp = id1; id1 = id2; id2 = temp;
        } else if (id1 == id2) {
            return 1;
        }
        try {
            return rtMap.get(id1).get(id2);
    	} catch (NullPointerException e) {
    		return 0;
        }
    }

    @Override
    public long cMax() {
        return cMax;
    }

}
