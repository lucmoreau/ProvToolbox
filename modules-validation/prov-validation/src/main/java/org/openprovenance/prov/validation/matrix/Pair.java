package org.openprovenance.prov.validation.matrix;

/**
 * A Pair is a container for two coordinates, the "first" and the "second" one.
 */
final public class Pair //implements Serializable
{
    
    private final int first;
    private final int second;
      int value;

    /**
     * Constructor for Pair.
     * @param first must be different from null
     * @param second must be different from null
     */
    public Pair(int first, int second)
    {
        this.first = first;
        this.second = second;
        this.value=-1;
    }

    public Pair(int first, int second, int value)
    {
        this.first = first;
        this.second = second;
        this.value=value;
    }

    /**
     * Returns the first.
     * @return int
     */
    public int getFirst()
    {
        return first;
    }

    /**
     * Returns the second.
     * @return int
     */
    public int getSecond()
    {
        return second;
    }

    public int getValue()
    {
        return value;
    }


    /**
     * 
     */
    public boolean equals(Object obj)
    {
        if (obj == this)
	    {
		return true;
	    }
        if (!(obj instanceof Pair))
	    {
		return false;
	    }
        Pair other = (Pair) obj;
        return first==other.first && second==other.second;
    }

    /*
     * @see java.lang.Integer#hashCode()
     * KKB, 17.3.04: hashCode calculated after "Effective Java", Item 8
     */
    public int hashCode()
    {
        int result = 17;
        result = 37 * result + first;
        result = 37 * result + second;
        return result;
    }

    /**
     * @see java.lang.Integer#toString()
     */
    public String toString()
    {
        return "(" + first + ", " + second + ", " + value + ")";
    }

}
