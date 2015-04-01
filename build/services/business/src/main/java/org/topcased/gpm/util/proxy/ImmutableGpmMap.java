/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel(Atos Origin)
 ******************************************************************/
package org.topcased.gpm.util.proxy;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.topcased.gpm.util.lang.CopyUtils;

/**
 * An immutable map
 * 
 * @author tpanuel
 * @param <K>
 *            The type of the key
 * @param <E>
 *            The type of the elements
 */
final public class ImmutableGpmMap<K, E> implements Map<K, E>,
        ImmutableGpmObject, Serializable {
    private static final long serialVersionUID = -7324268703841748257L;

    private static final RuntimeException SETTER_EXCEPTION =
            new RuntimeException("Map is immutable");

    private final Map<K, E> immutableMap;

    /**
     * Construct an immutable map from a mutable one
     * 
     * @param pMutableMap
     *            The mutable map
     */
    public ImmutableGpmMap(Map<K, E> pMutableMap) {
        if (pMutableMap.isEmpty()) {
            immutableMap = Collections.emptyMap();
        }
        else {
            immutableMap = getEmptyClone(pMutableMap);

            for (Entry<K, E> lEntry : pMutableMap.entrySet()) {
                // All the key and the element of the map must be immutable
                immutableMap.put(CopyUtils.getImmutableCopy(lEntry.getKey()),
                        CopyUtils.getImmutableCopy(lEntry.getValue()));
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.util.proxy.ImmutableGpmObject#getMutableCopy()
     */
    public Object getMutableCopy() {
        final Map<K, E> lMutableMap;

        if (immutableMap.isEmpty()) {
            // Use hash map, because empty map is immutable
            lMutableMap = new HashMap<K, E>();
        }
        else {
            lMutableMap = getEmptyClone(immutableMap);

            for (Entry<K, E> lEntry : immutableMap.entrySet()) {
                // All the key and element must becomes mutable
                lMutableMap.put(CopyUtils.getMutableCopy(lEntry.getKey()),
                        CopyUtils.getMutableCopy(lEntry.getValue()));
            }
        }
        // Keep the map mutable
        return lMutableMap;
    }

    /**
     * Get an empty clone of a map
     * 
     * @param <K>
     *            The type of the key of the map
     * @param <E>
     *            The type of the element of the map
     * @param pMap
     *            The map to clone
     * @return The cloned map
     */
    @SuppressWarnings("unchecked")
    private static <K, E> Map<K, E> getEmptyClone(Map<K, E> pMap) {
        try {
            final Map<K, E> lClonedMap =
                    (Map<K, E>) pMap.getClass().getMethod("clone",
                            new Class[] {}).invoke(pMap, new Object[] {});
            lClonedMap.clear();
            return lClonedMap;
        }
        catch (IllegalArgumentException e) {
            throw new RuntimeException(pMap.getClass() + " must be cloneable");
        }
        catch (SecurityException e) {
            throw new RuntimeException(pMap.getClass() + " must be cloneable");
        }
        catch (IllegalAccessException e) {
            throw new RuntimeException(pMap.getClass() + " must be cloneable");
        }
        catch (InvocationTargetException e) {
            throw new RuntimeException(pMap.getClass() + " must be cloneable");
        }
        catch (NoSuchMethodException e) {
            throw new RuntimeException(pMap.getClass() + " must be cloneable");
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.Map#clear()
     */
    public void clear() {
        throw SETTER_EXCEPTION;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.Map#containsKey(java.lang.Object)
     */
    public boolean containsKey(Object pKey) {
        return immutableMap.containsKey(pKey);
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.Map#containsValue(java.lang.Object)
     */
    public boolean containsValue(Object pValue) {
        return immutableMap.containsValue(pValue);
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.Map#entrySet()
     */
    public Set<java.util.Map.Entry<K, E>> entrySet() {
        return immutableMap.entrySet();
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.Map#get(java.lang.Object)
     */
    public E get(Object pKey) {
        return immutableMap.get(pKey);
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.Map#isEmpty()
     */
    public boolean isEmpty() {
        return immutableMap.isEmpty();
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.Map#keySet()
     */
    public Set<K> keySet() {
        return immutableMap.keySet();
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.Map#put(java.lang.Object, java.lang.Object)
     */
    public E put(K pKey, E pValue) {
        throw SETTER_EXCEPTION;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.Map#putAll(java.util.Map)
     */
    public void putAll(Map<? extends K, ? extends E> pMap) {
        throw SETTER_EXCEPTION;

    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.Map#remove(java.lang.Object)
     */
    public E remove(Object pKey) {
        throw SETTER_EXCEPTION;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.Map#size()
     */
    public int size() {
        return immutableMap.size();
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.Map#values()
     */
    public Collection<E> values() {
        return immutableMap.values();
    }
}
