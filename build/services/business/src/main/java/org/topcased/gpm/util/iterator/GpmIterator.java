/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.util.iterator;

import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.topcased.gpm.business.exception.MethodNotImplementedException;

/**
 * @author tpanuel
 * @param <E>
 */
public class GpmIterator<E> implements Iterator<E> {
    final private Iterator<E> iterator;

    /**
     * @param pElementToIterate
     */
    @SuppressWarnings("unchecked")
    public GpmIterator(Object pElementToIterate) {
        if (pElementToIterate == null) {
            iterator = null;
        }
        else if (pElementToIterate instanceof Iterable<?>) {
            iterator = ((Iterable<E>) pElementToIterate).iterator();
        }
        else {
            iterator =
                    (Iterator<E>) Collections.singleton(pElementToIterate).iterator();
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.Iterator#hasNext()
     */
    public boolean hasNext() {
        if (iterator == null) {
            return false;
        }
        // else
        return iterator.hasNext();
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.Iterator#next()
     */
    public E next() {
        if (iterator == null) {
            throw new NoSuchElementException();
        }
        // else
        return (E) iterator.next();
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.Iterator#remove()
     */
    public void remove() {
        throw new MethodNotImplementedException(
                "Cannot remove an element of a " + getClass().getName());
    }
}
