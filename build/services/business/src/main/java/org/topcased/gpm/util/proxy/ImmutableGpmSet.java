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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.topcased.gpm.util.lang.CopyUtils;

/**
 * An immutable set
 * 
 * @author tpanuel
 * @param <E>
 *            The type of the elements
 */
final public class ImmutableGpmSet<E> implements Set<E>, ImmutableGpmObject,
        Serializable {
    private static final long serialVersionUID = -4074295417149705195L;

    private static final RuntimeException SETTER_EXCEPTION =
            new RuntimeException("Set is immutable");

    private final Set<E> immutableSet;

    /**
     * Construct an immutable set from a mutable one
     * 
     * @param pMutableSet
     *            The mutable set
     */
    public ImmutableGpmSet(Set<E> pMutableSet) {
        if (pMutableSet.isEmpty()) {
            immutableSet = Collections.emptySet();
        }
        else {
            immutableSet = getEmptyClone(pMutableSet);

            for (E lElement : pMutableSet) {
                // All the element of the set must be immutable
                immutableSet.add(CopyUtils.getImmutableCopy(lElement));
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.util.proxy.ImmutableGpmObject#getMutableCopy()
     */
    public Object getMutableCopy() {
        final Set<E> lMutableSet;

        if (immutableSet.isEmpty()) {
            // Use hash set, because empty set is immutable
            lMutableSet = new HashSet<E>();
        }
        else {
            lMutableSet = getEmptyClone(immutableSet);
            for (E lElement : immutableSet) {
                // All the element must becomes mutable
                lMutableSet.add(CopyUtils.getMutableCopy(lElement));
            }
        }
        return lMutableSet;
    }

    /**
     * Get an empty clone of a set
     * 
     * @param <E>
     *            The type of the element of the set
     * @param pSet
     *            The set to clone
     * @return The cloned set
     */
    @SuppressWarnings("unchecked")
    private static <E> Set<E> getEmptyClone(Set<E> pSet) {
        try {
            final Set<E> lClonedSet =
                    (Set<E>) pSet.getClass().getMethod("clone", new Class[] {}).invoke(
                            pSet, new Object[] {});
            lClonedSet.clear();
            return lClonedSet;
        }
        catch (IllegalArgumentException e) {
            throw new RuntimeException(pSet.getClass() + " must be cloneable");
        }
        catch (SecurityException e) {
            throw new RuntimeException(pSet.getClass() + " must be cloneable");
        }
        catch (IllegalAccessException e) {
            throw new RuntimeException(pSet.getClass() + " must be cloneable");
        }
        catch (InvocationTargetException e) {
            throw new RuntimeException(pSet.getClass() + " must be cloneable");
        }
        catch (NoSuchMethodException e) {
            throw new RuntimeException(pSet.getClass() + " must be cloneable");
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.Set#add(java.lang.Object)
     */
    public boolean add(E pArg0) {
        throw SETTER_EXCEPTION;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.Set#addAll(java.util.Collection)
     */
    public boolean addAll(Collection<? extends E> pArg0) {
        throw SETTER_EXCEPTION;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.Set#clear()
     */
    public void clear() {
        throw SETTER_EXCEPTION;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.Set#contains(java.lang.Object)
     */
    public boolean contains(Object pArg0) {
        return immutableSet.contains(pArg0);
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.Set#containsAll(java.util.Collection)
     */
    public boolean containsAll(Collection<?> pArg0) {
        return immutableSet.containsAll(pArg0);
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.Set#isEmpty()
     */
    public boolean isEmpty() {
        return immutableSet.isEmpty();
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.Set#iterator()
     */
    public Iterator<E> iterator() {
        return immutableSet.iterator();
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.Set#remove(java.lang.Object)
     */
    public boolean remove(Object pArg0) {
        throw SETTER_EXCEPTION;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.Set#removeAll(java.util.Collection)
     */
    public boolean removeAll(Collection<?> pArg0) {
        throw SETTER_EXCEPTION;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.Set#retainAll(java.util.Collection)
     */
    public boolean retainAll(Collection<?> pArg0) {
        return immutableSet.removeAll(pArg0);
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.Set#size()
     */
    public int size() {
        return immutableSet.size();
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.Set#toArray()
     */
    public Object[] toArray() {
        return immutableSet.toArray();
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.Set#toArray(T[])
     */
    public <T> T[] toArray(T[] pArg0) {
        return immutableSet.toArray(pArg0);
    }
}
