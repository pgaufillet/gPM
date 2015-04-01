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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.topcased.gpm.util.lang.CopyUtils;

/**
 * An immutable list
 * 
 * @author tpanuel
 * @param <E>
 *            The type of the elements
 */
final public class ImmutableGpmList<E> implements List<E>, ImmutableGpmObject,
        Serializable {
    private static final long serialVersionUID = 7451766538112494131L;

    private static final RuntimeException SETTER_EXCEPTION =
            new RuntimeException("List is immutable");

    private final List<E> immutableList;

    /**
     * Construct an immutable list from a mutable one
     * 
     * @param pMutableList
     *            The mutable list
     */
    public ImmutableGpmList(List<E> pMutableList) {
        if (pMutableList.isEmpty()) {
            // Empty list is immutable tooF
            immutableList = Collections.emptyList();
        }
        else {
            immutableList = getEmptyClone(pMutableList);

            for (E lElement : pMutableList) {
                // All the element of the list must be immutable
                immutableList.add(CopyUtils.getImmutableCopy(lElement));
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.util.proxy.ImmutableGpmObject#getMutableCopy()
     */
    public Object getMutableCopy() {
        final List<E> lMutableList;

        if (immutableList.isEmpty()) {
            // Use linked list, because empty list is immutable
            lMutableList = new LinkedList<E>();
        }
        else {
            lMutableList = getEmptyClone(immutableList);

            for (E lElement : immutableList) {
                // All the element must becomes mutable
                lMutableList.add(CopyUtils.getMutableCopy(lElement));
            }
        }

        return lMutableList;
    }

    /**
     * Get an empty clone of a list
     * 
     * @param <E>
     *            The type of the element of the list
     * @param pList
     *            The list to clone
     * @return The cloned list
     */
    @SuppressWarnings("unchecked")
    private static <E> List<E> getEmptyClone(List<E> pList) {
        try {
            final List<E> lClonedList =
                    (List<E>) pList.getClass().getMethod("clone",
                            new Class[] {}).invoke(pList, new Object[] {});
            lClonedList.clear();
            return lClonedList;
        }
        catch (IllegalArgumentException e) {
            throw new RuntimeException(pList.getClass() + " must be cloneable");
        }
        catch (SecurityException e) {
            throw new RuntimeException(pList.getClass() + " must be cloneable");
        }
        catch (IllegalAccessException e) {
            throw new RuntimeException(pList.getClass() + " must be cloneable");
        }
        catch (InvocationTargetException e) {
            throw new RuntimeException(pList.getClass() + " must be cloneable");
        }
        catch (NoSuchMethodException e) {
            throw new RuntimeException(pList.getClass() + " must be cloneable");
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.List#add(java.lang.Object)
     */
    public boolean add(Object pArg0) {
        throw SETTER_EXCEPTION;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.List#add(int, java.lang.Object)
     */
    public void add(int pArg0, Object pArg1) {
        throw SETTER_EXCEPTION;

    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.List#addAll(java.util.Collection)
     */
    public boolean addAll(Collection<? extends E> pArg0) {
        throw SETTER_EXCEPTION;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.List#addAll(int, java.util.Collection)
     */
    public boolean addAll(int pArg0, Collection<? extends E> pArg1) {
        throw SETTER_EXCEPTION;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.List#clear()
     */
    public void clear() {
        throw SETTER_EXCEPTION;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.List#contains(java.lang.Object)
     */
    public boolean contains(Object pArg0) {
        return immutableList.contains(pArg0);
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.List#containsAll(java.util.Collection)
     */
    public boolean containsAll(Collection<?> pArg0) {
        return immutableList.containsAll(pArg0);
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.List#get(int)
     */
    public E get(int pArg0) {
        return immutableList.get(pArg0);
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.List#indexOf(java.lang.Object)
     */
    public int indexOf(Object pArg0) {
        return immutableList.indexOf(pArg0);
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.List#isEmpty()
     */
    public boolean isEmpty() {
        return immutableList.isEmpty();
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.List#iterator()
     */
    public Iterator<E> iterator() {
        return immutableList.iterator();
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.List#lastIndexOf(java.lang.Object)
     */
    public int lastIndexOf(Object pArg0) {
        return immutableList.lastIndexOf(pArg0);
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.List#listIterator()
     */
    public ListIterator<E> listIterator() {
        return immutableList.listIterator();
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.List#listIterator(int)
     */
    public ListIterator<E> listIterator(int pArg0) {
        return immutableList.listIterator(pArg0);
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.List#remove(java.lang.Object)
     */
    public boolean remove(Object pArg0) {
        throw SETTER_EXCEPTION;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.List#remove(int)
     */
    public E remove(int pArg0) {
        throw SETTER_EXCEPTION;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.List#removeAll(java.util.Collection)
     */
    public boolean removeAll(Collection<?> pArg0) {
        throw SETTER_EXCEPTION;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.List#retainAll(java.util.Collection)
     */
    public boolean retainAll(Collection<?> pArg0) {
        return immutableList.retainAll(pArg0);
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.List#set(int, java.lang.Object)
     */
    public E set(int pArg0, Object pArg1) {
        throw SETTER_EXCEPTION;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.List#size()
     */
    public int size() {
        return immutableList.size();
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.List#subList(int, int)
     */
    public List<E> subList(int pArg0, int pArg1) {
        return immutableList.subList(pArg0, pArg1);
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.List#toArray()
     */
    public Object[] toArray() {
        return immutableList.toArray();
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.List#toArray(T[])
     */
    public <T> T[] toArray(T[] pArg0) {
        return immutableList.toArray(pArg0);
    }
}
