package hu.sztaki.ilab.longneck.process.constraint;

import java.util.Set;

/**
 *
 * @author modras
 */
public interface Wordset extends Cloneable{
     /**
     * Return if the given string is represented in the wordset.
     * 
     * @param s the searching string.
     * @return true if the string is represent in the wordset, false otherwise.
     */
    public boolean contains(String s);

    /** Give back the whole worldset. */
    public Set<String> getWords();

    public Wordset clone();

}
