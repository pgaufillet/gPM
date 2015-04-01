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
package org.topcased.gpm.business.values.field.virtual.impl.cacheable;

import org.topcased.gpm.business.values.field.virtual.VirtualFieldType;
import org.topcased.gpm.business.values.impl.cacheable.AbstractCacheableContainerAccess;

/**
 * The virtual field on the type of the container.
 * 
 * @author tpanuel
 */
public class CacheableContainerTypeAccess
        extends
        AbstractCacheableVirtualFieldAccess<AbstractCacheableContainerAccess<?, ?>> {
    /**
     * Create a virtual access on the type of the container.
     * 
     * @param pContainerAccess
     *            The access on the container.
     */
    public CacheableContainerTypeAccess(
            final AbstractCacheableContainerAccess<?, ?> pContainerAccess) {
        super(VirtualFieldType.$SHEET_TYPE, pContainerAccess);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.virtual.BusinessVirtualField#getValue()
     */
    public String getValue() {
        return read().getTypeName();
    }
}