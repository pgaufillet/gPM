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
package org.topcased.gpm.business.values.sheet.impl.cacheable;

import java.util.ArrayList;
import java.util.List;

import org.topcased.gpm.business.serialization.data.DisplayGroup;
import org.topcased.gpm.business.sheet.impl.CacheableSheet;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.business.values.ValuesAccessProperties;
import org.topcased.gpm.business.values.field.BusinessFieldGroup;
import org.topcased.gpm.business.values.field.impl.cacheable.CacheableFieldGroupAccess;
import org.topcased.gpm.business.values.impl.cacheable.AbstractCacheableContainerAccess;
import org.topcased.gpm.business.values.sheet.BusinessSheet;

/**
 * Access on a sheet.
 * 
 * @author tpanuel
 */
public class CacheableSheetAccess extends
        AbstractCacheableContainerAccess<CacheableSheetType, CacheableSheet>
        implements BusinessSheet {
    /**
     * Create an access on a sheet.
     * 
     * @param pRoleToken
     *            The role token.
     * @param pSheetType
     *            The sheet type to access.
     * @param pSheet
     *            The sheet to access.
     * @param pProperties
     *            The values access properties.
     */
    public CacheableSheetAccess(final String pRoleToken,
            final CacheableSheetType pSheetType, final CacheableSheet pSheet,
            final ValuesAccessProperties pProperties) {
        super(pRoleToken, pSheetType, pSheet, pProperties);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.sheet.BusinessSheet#getFunctionalReference()
     */
    public String getFunctionalReference() {
        return read().getFunctionalReference();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.sheet.BusinessSheet#getProductName()
     */
    public String getProductName() {
        return read().getProductName();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.sheet.BusinessSheet#getState()
     */
    public String getState() {
        return read().getCurrentStateName();
    }

    public List<String> getFieldGroupNames() {
        List<String> lGroupNames = new ArrayList<String>();
        for (DisplayGroup lGroup : getType().getDisplayGroups()) {
            lGroupNames.add(lGroup.getName());
        }
        return lGroupNames;
    }

    public BusinessFieldGroup getFieldGroup(String pFieldGroupName) {
        return new CacheableFieldGroupAccess(getType().getDisplayGroup(
                pFieldGroupName));
    }
}