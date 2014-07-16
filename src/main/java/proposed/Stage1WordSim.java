package proposed;

import java.io.File;
import java.io.IOException;

import measures.Measure;

import org.textsim.exception.ProcessException;

public class Stage1WordSim
        extends AbstractStageWordSim
{

    public Stage1WordSim(String uniFile, String triFile, Measure sim) throws IOException, ProcessException {
        super(new Stage1WordRtProcessor(new File(uniFile), new File(triFile), sim), uniFile);
    }
}