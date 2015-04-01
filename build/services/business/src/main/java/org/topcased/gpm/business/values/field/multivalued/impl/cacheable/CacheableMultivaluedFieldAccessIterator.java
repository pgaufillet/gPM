/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 *
 ******************************************************************/
package org.topcased.gpm.business.values.field.multivalued.impl.cacheable;

import java.util.Iterator;

import org.topcased.gpm.business.values.field.BusinessField;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Iterator of the elements of multi valued field access.
 * 
 * @author tpanuel
 * @param <A>
 *            The type of field access.
 */
public class CacheableMultivaluedFieldAccessIterator<A extends BusinessField>
        implements Iterator<A> {
    private final CacheableMultivaluedFieldAccess<A, ?> values;

    private int nbElement;

    private int currentElement;

    /**
     * Create an iterator of access on elements of a multi valued field.
     * 
     * @param pValuesAccess
     *            Access on the values.
     */
    public CacheableMultivaluedFieldAccessIterator(
            final CacheableMultivaluedFieldAccess<A, ?> pValuesAccess) {
        values = pValuesAccess;
        if (values == null) {
            nbElement = 0;
        }
        else {
            nbElement = values.size();
        }
        currentElement = 0;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.Iterator#hasNext()
     */
    public boolean hasNext() {
        return currentElement < nbElement;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.Iterator#next()
     */
    public A next() {
        final A lNextValue = values.get(currentElement);

        currentElement++;

        return lNextValue;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.Iterator#remove()
     */
    public void remove() {
        throw new NotImplementedException();
    }
}