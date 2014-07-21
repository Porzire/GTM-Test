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
    private static final int SIZE = 100000;

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

    public DirectAccessApproach(ProposedApproach proposed)
    {
        idMap = proposed.idMap;
        freqs = proposed.freqs;
        cMax = proposed.cMax;
        for (int i = 0; i <= SIZE; i++)
            for (int j = 0; j <= SIZE; j++)
                cooccurrence[i][j] = proposed.freq(idMap.get(i), idMap.get(j));
    }

    @Override
    public long freq(String gram) {
        return freqs[idMap.get(gram)];
    }

    @Override
    public long freq(String gram1, String gram2) {
        return cooccurrence[idMap.get(gram1)][idMap.get(gram2)];
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