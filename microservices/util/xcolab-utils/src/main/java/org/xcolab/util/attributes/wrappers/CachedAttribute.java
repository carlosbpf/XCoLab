package org.xcolab.util.attributes.wrappers;

import org.xcolab.util.attributes.AttributeGetter;

import java.util.HashMap;
import java.util.Map;

public class CachedAttribute<T> implements AttributeGetter<T> {

    private final AttributeGetter<T> wrappedAttributeGetter;

    private T cachedValue;
    private final Map<Long, T> cachedValuesByAdditionalId = new HashMap<>();

    public CachedAttribute(AttributeGetter<T> wrappedAttributeGetter) {
        this.wrappedAttributeGetter = wrappedAttributeGetter;
    }

    @Override
    public T get() {
        if (cachedValue == null) {
            cachedValue = wrappedAttributeGetter.get();
        }
        return cachedValue;
    }

    @Override
    public T get(long additionalId) {
        T value = cachedValuesByAdditionalId.get(additionalId);
        if (value == null) {
            value = wrappedAttributeGetter.get(additionalId);
            cachedValuesByAdditionalId.put(additionalId, value);
        }
        return value;
    }

    @Override
    public String name() {
        return wrappedAttributeGetter.name();
    }
}
