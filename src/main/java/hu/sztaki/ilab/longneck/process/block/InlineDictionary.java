package hu.sztaki.ilab.longneck.process.block;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Inline dictionary, set of pair of words.
 * <br>
 * Example:
 * <inline-dictionary>
 *   <di>
 *     <key>hu</key>
 *     <val>Hungarian</val>
 *   </di>
 *   <di>
 *     <key>en</key>
 *     <val>English</val>
 *   </di>
 *   <di>
 *     <key>de</key>
 *     <val>German</val>
 *   </di>
 *   <di>
 *     <key>fr</key>
 *     <val>French</val>
 *   </di>
 * </inline-dictionary>
 * 
 * @author modras
 */
public class InlineDictionary implements Dictionary {

    private Map<String, DictItem> itemMap;

    public InlineDictionary () {
        itemMap = new TreeMap<String,DictItem>();
    }

    public Map<String, DictItem> getItemMap() {
        return itemMap;
    }

    public void setItemMap(Map<String, DictItem> itemMap) {
        this.itemMap = itemMap;
    }

    public Collection<DictItem> getItemsCopy() {
        TreeSet<DictItem> ret = new TreeSet<DictItem>(new DictItem.DictItemKeyComparator());
        for (Entry<String, DictItem> e : itemMap.entrySet()) {
            ret.add(e.getValue());
        }
        return ret;
    }

    @Override
    public Iterable<DictItem> getItems() {
        return getItemsCopy();
    }

    public void addItem(DictItem item) {
        this.itemMap.put(item.getKey(), item);
    }

    @Override
    public String translate(String s) {
        DictItem dictItem = getItemMap().get(s);
        if (dictItem == null) {
            return null;
        }
        return dictItem.getValue();
    }

    @Override
    public InlineDictionary clone() {
        try {
            InlineDictionary copy = (InlineDictionary) super.clone();
            copy.itemMap = new HashMap<String, DictItem>(itemMap.size());

            for (Map.Entry<String, DictItem> entry : itemMap.entrySet()) {
                copy.itemMap.put(entry.getKey(), entry.getValue().clone());
            }
            return copy;

        } catch (CloneNotSupportedException ex) {
            throw new AssertionError(ex);
        }
    }
}
