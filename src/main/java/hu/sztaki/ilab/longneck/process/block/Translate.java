package hu.sztaki.ilab.longneck.process.block;

import hu.sztaki.ilab.longneck.Record;
import hu.sztaki.ilab.longneck.process.*;
import hu.sztaki.ilab.longneck.process.constraint.CheckResult;

/**
 * Translate the string using the given dictionary.
 * 
 * @author Molnár Péter <molnarp@ilab.sztaki.hu>
 */
public class Translate extends AbstractAtomicBlock {

    private Dictionary dictionary;
    private String from;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public Dictionary getDictionary() {
        return dictionary;
    }

    public void setDictionary(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    @Override
    public void apply(Record record, VariableSpace parentScope) throws CheckError { 
        String val = dictionary.translate(BlockUtils.getValue(from, record, parentScope));
        // if dictionary not contains, then raise a CheckError exeption.
        if (val == null) {
            throw new CheckError(
                new CheckResult(this, false, from,
                    BlockUtils.getValue(from, record, parentScope),
                    "Not found in translation dictionary."));
        }
        for (String fName : applyTo) {
            BlockUtils.setValue(fName, val, record, parentScope);
        }
    }

    @Override
    public Translate clone() {

        Translate copy = (Translate) super.clone();
        copy.dictionary = dictionary.clone();
        return copy;
    }
}
