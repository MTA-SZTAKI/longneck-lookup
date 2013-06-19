package hu.sztaki.ilab.longneck.process.constraint;

import hu.sztaki.ilab.longneck.Record;
import hu.sztaki.ilab.longneck.process.access.NoMoreRecordsException;
import hu.sztaki.ilab.longneck.process.access.Source;
import java.util.Set;
import java.util.TreeSet;

/**
 * Set of words from external source. E.g. xml file.
 * 
 * @author modras
 */
public class ExternalWordset implements Wordset {

    protected Set<String> words;
    protected Source source;
    protected String columnName;

    public void init() {
        readWords();
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    protected void readWords() {
        // On-demand read and construct words from source
        words = new TreeSet<String>();
        try {
            for (;;) {

                Record r = source.getRecord();
                words.add(r.get(getColumnName()).getValue());
            }
        } catch (NoMoreRecordsException e) {
            ;
        }
    }

    @Override
    public Set<String> getWords() {
        if (words == null) {
            readWords();
        }
        return words;
    }

    @Override
    public boolean contains(String s) {
        return getWords().contains((s == null) ? "" : s);
    }

    @Override
    public ExternalWordset clone() {
        try {

            ExternalWordset copy = (ExternalWordset) super.clone();
            copy.words = new TreeSet<String>(words);

            return copy;

        } catch (CloneNotSupportedException ex) {
            throw new AssertionError(ex);
        }
    }
}
