package measures;


public abstract class Measure {

    abstract public double sim(long w1Freq, long w2Freq, long triFreq);
    
    public abstract void setConst(double... vals);

    // need to be override by subclass.
    public double getConst() { return -1; }
    
    @Override
    public String toString()
    {
        return this.getClass().getSimpleName();
    }
}