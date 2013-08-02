package hu.sztaki.ilab.longneck.process.block;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * The dictionary is treated as a dictionary with regex key.
 * The key regex is matched against the translated string.
 * If key is not a regular expression, then it sees the containing of the string.
 * Otherwise, if it don't give a hit, return null.
 * 
 * Note: elements order is important! Return with the _first_ hit.
 * 
 * @author modras
 */
public class RegexpDictionary implements Dictionary {

    private Dictionary dictionary;

    private Map<String, DictItem> compiledDict; 

    public void setDictionary(Dictionary dictionary) {
        this.dictionary = dictionary;
        if (dictionary instanceof ExternalDictionary
          && ((ExternalDictionary) dictionary).isUseSourceOrder()) {
            compiledDict = new LinkedHashMap<String, DictItem>();
        }
        else {
            compiledDict = new HashMap<String, DictItem>();
        }

        for (DictItem di : dictionary.getItems()) {
            compiledDict.put(di.getKey(), di);
        }
    }

    public Dictionary getDictionary() {
        return this.dictionary;
    }

    protected Map<String, DictItem> getCompiledDict() {
        return compiledDict;
    }

    @Override
    public String translate(String s) {
        // Check for null value
        if (s == null) {
            return null;
        }

        // multi-pattern matching
        for (java.util.Map.Entry<String, DictItem> e : getCompiledDict().entrySet()) {

            DictItem compiledDictItem = e.getValue();
            Pattern compiledKey = compiledDictItem == null ? null : compiledDictItem.getCompiledKey();

            // if key is not a regexp
            if (compiledKey == null) {
                if (s.contains(e.getKey())) {
                    return compiledDictItem == null ? null : compiledDictItem.getValue();
                }
            }
            // s (string) match against regex
            else {
                if (Pattern.matches(compiledKey.pattern(), s)) {
                    return Pattern.compile(compiledKey.pattern()).matcher(s).replaceFirst(
                        compiledDictItem == null ? null : compiledDictItem.getValue()
                    );
                }
            }
        }
        return null;
    }

    @Override
    public Iterable<DictItem> getItems() {
        return getDictionary().getItems();
    }

    @Override
    public RegexpDictionary clone() {
        try {

            RegexpDictionary copy = (RegexpDictionary) super.clone();
            for (Map.Entry<String, DictItem> entry : compiledDict.entrySet()) {
                copy.compiledDict.put(entry.getKey(), entry.getValue().clone());
            }
            copy.dictionary = dictionary.clone();
            return copy;

        } catch (CloneNotSupportedException ex) {
            throw new AssertionError(ex);
        }
    }
}
