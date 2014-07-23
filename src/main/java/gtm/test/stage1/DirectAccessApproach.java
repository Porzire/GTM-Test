package gtm.test.stage1;

import gnu.trove.map.hash.TObjectIntHashMap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class DirectAccessApproach
        implements Approach, Serializable
{
    private static final long serialVersionUID = 7060334221436462563L;

    /**
     * The co-occurrence map size. Only SIZE x SIZE of the map is implemented
     * for the limitation of the memory.
     */
    int size;

    private TObjectIntHashMap<String> idMap;
    long[] freqs;
    long[][] cooccurrence;
    long cMax;


    /**
     * Read object from serialized file.
     * 
     * @param  serFile  Serialized file.
     * @return A StringArrayApproach object.
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static DirectAccessApproach read(File serFile)
            throws FileNotFoundException, IOException, ClassNotFoundException
    {
        try (
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(serFile));
        ){
            return (DirectAccessApproach) ois.readObject();
        }
    }

    public DirectAccessApproach(ProposedApproach proposed, int size)
    {
        this.size = size;
        idMap = new TObjectIntHashMap<String>();
        for (String gram : idMap.keySet())
            idMap.put(gram, proposed.idMap.get(gram));
        freqs = new long[proposed.freqs.length];
        System.arraycopy(proposed.freqs, 0, freqs, 0, proposed.freqs.length);
        cMax = proposed.cMax;
        cooccurrence = new long[size][size];
        for (int i = 1; i < size; i++)
            for (int j = 1; j < size; j++)
                cooccurrence[i][j] = proposed.freq(i, j);
    }
    
    public DirectAccessApproach(ProposedApproach proposed)
    {
        this(proposed, 100000);
    }

    @Override
    public long freq(String gram) {
        return freqs[idMap.get(gram)];
    }

    @Override
    public long freq(String gram1, String gram2) {
        int id1 = idMap.get(gram1);
        int id2 = idMap.get(gram2);
        if (id1 >= size || id2 >= size)
            return 1;  // Force computation!
        else
            return cooccurrence[id1][id2];
    }

    @Override
    public long cMax() {
        return cMax;
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