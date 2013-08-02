package hu.sztaki.ilab.longneck.process.constraint;

import java.util.TreeSet;

/**
 * Set of words which is given inline in the lookup constraint.
 * 
 * Example:
 * <inline-wordset>
 *   <w>word1</w>
 *   <w>word2</w>
 *   ... 
 * </inline-wordset>
 * 
 * @author modras
 */
public class InlineWordset implements Wordset {

    protected TreeSet<String> words;

    @Override
    public TreeSet<String> getWords() {
        return words;
    }

    public void setWords(TreeSet<String> words) {
        this.words = words;
    }

    @Override
    public boolean contains(String s) {
        return words.contains((s == null) ? "" : s);
    }

    @Override
    public InlineWordset clone() {
        try {

            InlineWordset copy = (InlineWordset) super.clone();
            copy.words = new TreeSet<String>();
            copy.words.addAll(words);

            return copy;

        } catch (CloneNotSupportedException ex) {
            throw new AssertionError(ex);
        }
    }
}
