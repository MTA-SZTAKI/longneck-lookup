package hu.sztaki.ilab.longneck.process.block;

import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * Class that represents a dictionary key with the attribute 'regexp', i.e.
 * <br>
 * {@code <key regexp="true">(.*)</key> }
 *
 * @author Bendig Loránd <lbendig@ilab.sztaki.hu>
 *
 */
public class KeyType implements Cloneable{

    private String keyContent;

    private boolean regexp;

    /** If the key is regexp then pattern have to be compiled */
    private Pattern compiledKey;

    public KeyType() {}

    public KeyType(String keyContent) {
        if (StringUtils.isEmpty(keyContent)) {
            throw new IllegalArgumentException("Invalid key: " + keyContent);
        }
            this.keyContent = keyContent;
    }

    public KeyType(String keyContent, boolean regexp) {
        this(keyContent);
        this.regexp = regexp;
        if (regexp) {
            this.compiledKey = Pattern.compile(keyContent);
        }
    }

    public String getKeyContent() {
        return keyContent;
    }

    public void setKeyContent(String keyContent) {
        this.keyContent = keyContent;
        if (regexp) {
            this.compiledKey = Pattern.compile(keyContent);
        }
    }

    public boolean isRegexp() {
        return regexp;
    }

    public void setRegexp(boolean regexp) {
        this.regexp = regexp;
    }

    public Pattern getCompiledKey() {
        return compiledKey;
    }

    @Override
    public KeyType clone() {
        try {

            KeyType copy = (KeyType) super.clone();
            copy.regexp = regexp;
            copy.setKeyContent(keyContent);

            return copy;

        } catch (CloneNotSupportedException ex) {
            throw new AssertionError(ex);
        }
    }
}
