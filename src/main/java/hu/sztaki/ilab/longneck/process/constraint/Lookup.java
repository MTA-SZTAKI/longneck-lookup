package hu.sztaki.ilab.longneck.process.constraint;

import hu.sztaki.ilab.longneck.Record;
import hu.sztaki.ilab.longneck.process.VariableSpace;
import hu.sztaki.ilab.longneck.util.BlockUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * Look up if the given wordset contain the apply-to filed(s) or not.
 * 
 * @author Molnár Péter <molnarp@ilab.sztaki.hu>
 */
public class Lookup extends AbstractAtomicConstraint {

    protected Wordset words;

    public Wordset getWords() {
        return words;
    }

    public void setWords(Wordset words) {
        this.words = words;
    }

    @Override
    public CheckResult check(Record record, VariableSpace scope)  {
        List<CheckResult> results = new ArrayList<CheckResult>();

        for (String fName : applyTo) {
            if (words.contains(BlockUtils.getValue(fName, record, scope))) {
                results.add(new CheckResult(
                    this, true, fName,
                    BlockUtils.getValue(fName, record, scope),
                    "Found in lookup wordset."));
            } else {
                results.add(new CheckResult(
                    this, false, fName,
                    BlockUtils.getValue(fName, record, scope),
                    "Not found in lookup wordset."));

                return new CheckResult(this, false, null, null, null, results);
            }
        }

        return new CheckResult(this, true, null, null, null, results);
    }

    @Override
    public Lookup clone() {
        Lookup copy = (Lookup) super.clone();
        copy.words = words.clone();

        return copy;
    }
}
