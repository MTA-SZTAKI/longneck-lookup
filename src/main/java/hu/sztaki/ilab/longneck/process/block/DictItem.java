package hu.sztaki.ilab.longneck.process.block;

import java.util.Comparator;
import java.util.regex.Pattern;

/**
 * An element of a dictionary.
 * @author modras
 */
public class DictItem implements Cloneable {

    private KeyType keyType;

    private String value;

    public DictItem() {}

    public DictItem(String key, String value) {

        this.keyType = new KeyType(key);
        this.value = value;

    }

    public DictItem(KeyType keyType, String value) {

        if (keyType == null) {
            throw new IllegalArgumentException("KeyType cannot be null!");
        }
        this.keyType = keyType;
        this.value = value;
    }

    public KeyType getKeyType() {
        return keyType;
    }

    public void setKeyType(KeyType keyType) {
        this.keyType = keyType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKey() {
        return keyType.getKeyContent();
    }


    public Pattern getCompiledKey() {
        return keyType.getCompiledKey();
    }

    public static class DictItemKeyComparator implements Comparator<DictItem> {

        @Override
        public int compare(DictItem o1, DictItem o2) {
            return o1.getKey().compareTo(o2.getKey());
        }

    }

    @Override
    public DictItem clone() {
        try {
            DictItem copy = (DictItem) super.clone();
            copy.keyType = keyType.clone();
            return copy;

        } catch (CloneNotSupportedException ex) {
            throw new AssertionError(ex);
        }
    }
}
