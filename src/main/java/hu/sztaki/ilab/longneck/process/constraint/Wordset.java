package hu.sztaki.ilab.longneck.process.constraint;

import java.util.Set;

/**
 *
 * @author modras
 */
public interface Wordset extends Cloneable{
     /**
     * Check if the given string is represented in the wordset.
     * 
     * @param s the searching string.
     * @return true if the string is represented in the wordset, false otherwise.
     */
    public boolean contains(String s);

    /** Give back the whole wordset. */
    public Set<String> getWords();

    public Wordset clone();

}
