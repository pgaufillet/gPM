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
import org.topcased.gpm.business.values.sheet.impl.cacheable.CacheableSheetAccess;

/**
 * The virtual field on the functional reference of a sheet.
 * 
 * @author tpanuel
 */
public class CacheableSheetReferenceAccess extends
        AbstractCacheableVirtualFieldAccess<CacheableSheetAccess> {
    /**
     * Create a virtual access on the functional reference of a sheet.
     * 
     * @param pSheetAccess
     *            The access on the sheet.
     */
    public CacheableSheetReferenceAccess(final CacheableSheetAccess pSheetAccess) {
        super(VirtualFieldType.$SHEET_REFERENCE, pSheetAccess);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.virtual.BusinessVirtualField#getValue()
     */
    public String getValue() {
        return read().getFunctionalReference();
    }
}