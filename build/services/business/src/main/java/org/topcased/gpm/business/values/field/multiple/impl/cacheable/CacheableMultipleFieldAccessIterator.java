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
package org.topcased.gpm.business.values.field.multiple.impl.cacheable;

import java.util.Iterator;

import org.topcased.gpm.business.serialization.data.Field;
import org.topcased.gpm.business.values.field.BusinessField;

/**
 * Iterator of the elements of a multiple field access.
 * 
 * @author tpanuel
 */
public class CacheableMultipleFieldAccessIterator implements
        Iterator<BusinessField> {
    private final CacheableMultipleFieldAccess multipleValuesAccess;

    private final Iterator<Field> fieldIterator;

    /**
     * Create an iterator of access on elements of a multiple field.
     * 
     * @param pMulipleValuesAccess
     *            Access on the values.
     */
    public CacheableMultipleFieldAccessIterator(
            final CacheableMultipleFieldAccess pMulipleValuesAccess) {
        multipleValuesAccess = pMulipleValuesAccess;
        fieldIterator =
                multipleValuesAccess.getFieldType().getFields().iterator();
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.Iterator#hasNext()
     */
    public boolean hasNext() {
        return fieldIterator.hasNext();
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.Iterator#next()
     */
    public BusinessField next() {
        return multipleValuesAccess.getField(fieldIterator.next().getLabelKey());
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.Iterator#remove()
     */
    public void remove() {
        fieldIterator.remove();
    }
}