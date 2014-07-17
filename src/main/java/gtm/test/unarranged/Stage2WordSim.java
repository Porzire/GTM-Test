package gtm.test.unarranged;

import java.io.File;
import java.io.IOException;

import org.textsim.exception.ProcessException;
import org.textsim.wordrt.proc.DefaultWordRtProcessor;

public class Stage2WordSim
        extends AbstractStageWordSim
{

    public Stage2WordSim(String uniFile, String triFile)
            throws IOException, ProcessException
    {
        super(new DefaultWordRtProcessor(new File(triFile)), uniFile);
    }
}