package hu.sztaki.ilab.longneck.process.block;

import hu.sztaki.ilab.longneck.Field;
import hu.sztaki.ilab.longneck.Record;
import hu.sztaki.ilab.longneck.process.access.NoMoreRecordsException;
import hu.sztaki.ilab.longneck.process.access.Source;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;

/**
 *  A dictionary, set of word pair from external source like xml file.
 *
 * @author modras
 */
public class ExternalDictionary implements Dictionary {

    private Source source;

    private Map<String, DictItem> itemMap;

    protected String keyColumnName;

    protected String keyTypeColumnName;

    protected String valueColumnName;

    /** If true use the source order as is instead of DictItem key order. */
    private boolean useSourceOrder;

    public void init() {
        readItems();
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public String getKeyColumnName() {
        return keyColumnName;
    }

    public void setKeyColumnName(String keyColumnName) {
        this.keyColumnName = keyColumnName;
    }

    public String getKeyTypeColumnName() {
        return keyTypeColumnName;
    }

    public void setKeyTypeColumnName(String keyTypeColumnName) {
        this.keyTypeColumnName = keyTypeColumnName;
    }

    public String getValueColumnName() {
        return valueColumnName;
    }

    public void setValueColumnName(String valueColumnName) {
        this.valueColumnName = valueColumnName;
    }

    public boolean isUseSourceOrder() {
        return useSourceOrder;
    }

    public void setUseSourceOrder(boolean useSourceOrder) {
        this.useSourceOrder = useSourceOrder;
    }

    public ExternalDictionary () { }

    private void readItems() {
        // read and construct items on-demand from source:
        itemMap = useSourceOrder ? new LinkedHashMap<String, DictItem>()
            : new TreeMap<String, DictItem>();
        try {
            for (;;) {
                Record r = source.getRecord();
                String key = r.get(getKeyColumnName()).getValue();
                String value = r.get(getValueColumnName()).getValue();

                boolean isRegexp = false;
                if (!StringUtils.isEmpty(getKeyTypeColumnName())) {
                    Field regexpTypeField = r.get(getKeyTypeColumnName());
                    if (regexpTypeField != null) {
                        isRegexp = Boolean.parseBoolean(regexpTypeField.getValue());
                    }
                }
                itemMap.put(key, new DictItem(new KeyType(key, isRegexp), value));
            }
        }
        catch (NoMoreRecordsException e) {
        ;
        }
    }

    public synchronized Map<String, DictItem> getItemMap() {
        if (itemMap == null) {
            readItems();
        }
        return itemMap;
    }

    public Collection<DictItem> getItemsCopy() {

        Set<DictItem> ret = useSourceOrder ? new LinkedHashSet<DictItem>() : 
            new TreeSet<DictItem>(new DictItem.DictItemKeyComparator());
        for (Entry<String, DictItem> e : getItemMap().entrySet()) {
            ret.add(e.getValue());
        }
        return ret;
    }

    @Override
    public Iterable<DictItem> getItems() {
        return getItemsCopy();
    }

    protected synchronized void addItem(DictItem item) {
        this.getItemMap().put(item.getKey(), item);
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
    public ExternalDictionary clone() {
        try {

            ExternalDictionary copy = (ExternalDictionary) super.clone();
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
