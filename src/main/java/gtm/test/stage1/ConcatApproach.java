package gtm.test.stage1;

import gnu.trove.map.hash.TLongLongHashMap;
import gnu.trove.map.hash.TObjectIntHashMap;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.textsim.exception.ProcessException;

public class ConcatApproach
        implements Approach {
    
    // Unigram Data structure.
    protected long cMax;
    protected TObjectIntHashMap<String> idMap;
    protected long[] freqs;
    // Trigram Data structure.
    private TLongLongHashMap coocMap;

    public ConcatApproach(File uniFile, File triFile)
            throws IOException, ProcessException {
        ProposedApproach pa = new ProposedApproach(uniFile, triFile);
        cMax = pa.cMax;
        idMap = pa.idMap;
        freqs = pa.freqs;
        Set<String> keys = idMap.keySet();
        for (String key1 : keys)
            for (String key2 : keys) {
                int id1 = idMap.get(key1);
                int id2 = idMap.get(key2);
                long count = pa.freq(id1, id2);
                if (count > 0) {
                    coocMap.put(encode(id1, id2), count);
                }
            }
    }
    
    private long encode(int a, int b) {
        return ((long)a << 32) + b;
    }

    @Override
    public long freq(String gram) {
        return freqs[idMap.get(gram)];
    }

    @Override
    public long freq(String gram1, String gram2) {
        return coocMap.get(encode(
                idMap.get(gram1), idMap.get(gram2)));
    }

    @Override
    public long cMax() {
        return cMax;
    }

}
