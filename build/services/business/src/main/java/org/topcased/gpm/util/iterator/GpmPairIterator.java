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

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.topcased.gpm.business.exception.MethodNotImplementedException;
import org.topcased.gpm.util.bean.GpmPair;

/**
 * @author tpanuel
 */
public class GpmPairIterator<F, S> implements Iterator<GpmPair<F, S>> {
    final private Iterator<F> iterator1;

    final private Iterator<S> iterator2;

    /**
     * @param pObject
     */
    public GpmPairIterator(Object pFirstElementToIterate,
            Object pSecondElementToIterate) {
        iterator1 = new GpmIterator<F>(pFirstElementToIterate);
        iterator2 = new GpmIterator<S>(pSecondElementToIterate);
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.Iterator#hasNext()
     */
    public boolean hasNext() {
        return iterator1.hasNext() || iterator2.hasNext();
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.Iterator#next()
     */
    public GpmPair<F, S> next() {
        if (hasNext()) {
            final GpmPair<F, S> lNext = new GpmPair<F, S>();

            if (iterator1.hasNext()) {
                lNext.setFirst(iterator1.next());
            }
            if (iterator2.hasNext()) {
                lNext.setSecond(iterator2.next());
            }
            return lNext;
        }
        // else
        throw new NoSuchElementException();
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
