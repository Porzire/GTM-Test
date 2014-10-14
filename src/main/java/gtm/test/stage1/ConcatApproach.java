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
        int size = idMap.keySet().size();
        coocMap = new TLongLongHashMap((int)(size * size * 0.01));
        long starTime = System.currentTimeMillis();
        for (int id1 = 0; id1 < size; id1++) {
            int str = pa.tListStr[id1];
            int end = pa.tListEnd[id1];
            for (int id2 = str; id2 < end; id2++)
                coocMap.put(encode(id1, id2), pa.freq(id1, id2));
            if (id1 % 1000 == 0)
                System.out.print("[" + id1 + "/" + size + "] " +
                        new java.text.DecimalFormat("#.##").format((float)id1/size*100) + "% done " + coocMap.size() + " entries added. Time taken " +
                        (System.currentTimeMillis() - starTime) / 1000 + " s        \r");
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
