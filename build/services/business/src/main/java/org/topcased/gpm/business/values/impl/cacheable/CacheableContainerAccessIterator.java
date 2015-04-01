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
package org.topcased.gpm.business.values.impl.cacheable;

import java.util.Iterator;

import org.topcased.gpm.business.serialization.data.Field;
import org.topcased.gpm.business.values.field.BusinessField;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Iterator of the elements of a container access.
 * 
 * @author tpanuel
 */
public class CacheableContainerAccessIterator implements
        Iterator<BusinessField> {
    private final AbstractCacheableContainerAccess<?, ?> containerAccess;

    private final Iterator<? extends Field> fieldIterator;

    /**
     * Create an iterator of access on elements of a multiple field.
     * 
     * @param pContainerAccess
     *            Access on the values.
     */
    public CacheableContainerAccessIterator(
            final AbstractCacheableContainerAccess<?, ?> pContainerAccess) {
        containerAccess = pContainerAccess;
        fieldIterator = containerAccess.getType().getFields().iterator();
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
        return containerAccess.getField(fieldIterator.next().getLabelKey());
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