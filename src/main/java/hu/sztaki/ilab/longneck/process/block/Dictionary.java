package hu.sztaki.ilab.longneck.process.block;

/**
 * Interface for dictionary, set of key-value pair.
 * 
 * @author modras
 */
public interface Dictionary extends Cloneable {
    /** Translate the given string from the dictionary if it is contain the string. */
    public String translate(String s);

    public Iterable<DictItem> getItems();

    public Dictionary clone();
}
