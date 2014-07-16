package proposed;

import gnu.trove.map.hash.TObjectIntHashMap;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;

import org.textsim.exception.ProcessException;
import org.textsim.util.Unigram;
import org.textsim.wordrt.preproc.WordrtPreproc;
import org.textsim.wordrt.proc.WordRtProcessor;

abstract class AbstractStageWordSim
{
    private WordRtProcessor proc;
    private TObjectIntHashMap<String> idMap;
    private String[] word1;
    private String[] word2;

    public AbstractStageWordSim(WordRtProcessor proc, String uniFile)
            throws IOException, ProcessException
    {
        this.proc = proc;
        idMap = Unigram.readIDMap(WordrtPreproc.BINARY, new File[]{new File(uniFile)});
    }
    
    // Load word pairs from input file.
    public void loadInput(String inFile, int linesToRead)
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
    
    // Run test.
    public void run()
    {
        for (int i = 0; i < word1.length; i++) {
            proc.sim(idMap.get(word1[i]), idMap.get(word2[i]));
        }
    }

    // Run test and write the result to the output file.
    public void runAndWrite(String outFile)
            throws IOException
    {
        BufferedWriter bw = null;
        try {
             bw = new BufferedWriter(new FileWriter(new File(outFile)));
            for (int i = 0; i < word1.length; i++) {
                double sim = proc.sim(idMap.get(word1[i]), idMap.get(word2[i]));
                bw.write(word1[i] + "\t" + word2[i] + "\t" + sim + "\n");
            }
        } finally {
            if (bw != null)
                bw.close();
        }
    }

    // Read the total number of lines in the input file.
    private int readLines(String inFile)
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
