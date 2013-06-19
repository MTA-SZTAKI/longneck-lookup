package hu.sztaki.ilab.longneck.process.block;

import hu.sztaki.ilab.longneck.Field;
import hu.sztaki.ilab.longneck.Record;
import hu.sztaki.ilab.longneck.process.AbstractSourceInfoContainer;
import hu.sztaki.ilab.longneck.process.CheckError;
import hu.sztaki.ilab.longneck.process.VariableSpace;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.map.LRUMap;

/** Apply caching for translate block. 
 */
public class TranslateCache extends AbstractSourceInfoContainer implements AtomicBlock {

    private int size = 100; //default size of cache

    private Translate translate;

    @SuppressWarnings("unchecked") // unchecked warning supressed 
    /** key: block.applyTo[0]."["record.field.value"]" */
    private Map<String, Field> cache = 
        (Map<String, Field>) (Collections.synchronizedMap(new LRUMap(size)));

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Translate getTranslate() {
        return translate;
    }

    public void setTranslate(Translate translate) {
        this.translate = translate;
    }

    public synchronized Field getCacheElement(String key) {
        return cache.get(key);
    }

    public synchronized void putCacheElement(String key, Field value) {
        cache.put(key, value);
    }

    @Override
    public void apply(Record record, VariableSpace parentScope) throws CheckError {

        String applyToName = translate.getApplyTo().get(0); //currently only the first one is considered
        String examine = BlockUtils.getValue(translate.getFrom(), record, parentScope);
        String key  = asCacheKey(applyToName, examine);

        Field f = getCacheElement(key);
        if (f == null) {
            translate.apply(record, parentScope);
            putCacheElement(key, record.get(applyToName));
        }
        else {
            for (String fName : translate.getApplyTo()) {
                BlockUtils.setValue(fName, f.getValue(), record, parentScope);
            }
        }
    }

    @Override
    public void setApplyTo(List<String> fieldNames) {
        // NOPMD
    }

    private String asCacheKey(String applyTo, String fieldValue) {
        StringBuilder sb = new StringBuilder(applyTo);
        sb.append(".[");
        sb.append(fieldValue);
        sb.append("]");
        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    @Override
    public TranslateCache clone() {
        TranslateCache translateCache = (TranslateCache) super.clone();
        translateCache.cache = (Map<String, Field>) (Collections.synchronizedMap(new LRUMap(size)));
        translateCache.translate = translate.clone();
        return translateCache;
    }
}