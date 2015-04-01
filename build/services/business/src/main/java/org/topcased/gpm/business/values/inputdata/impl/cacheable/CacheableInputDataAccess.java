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
package org.topcased.gpm.business.values.inputdata.impl.cacheable;

import java.util.ArrayList;
import java.util.List;

import org.topcased.gpm.business.fields.impl.CacheableInputData;
import org.topcased.gpm.business.fields.impl.CacheableInputDataType;
import org.topcased.gpm.business.serialization.data.DisplayGroup;
import org.topcased.gpm.business.values.ValuesAccessProperties;
import org.topcased.gpm.business.values.field.BusinessFieldGroup;
import org.topcased.gpm.business.values.field.impl.cacheable.CacheableFieldGroupAccess;
import org.topcased.gpm.business.values.impl.cacheable.AbstractCacheableContainerAccess;
import org.topcased.gpm.business.values.inputdata.BusinessInputData;

/**
 * Access on an input data.
 * 
 * @author tpanuel
 */
public class CacheableInputDataAccess
        extends
        AbstractCacheableContainerAccess<CacheableInputDataType, CacheableInputData>
        implements BusinessInputData {
    /**
     * Create an access on an input data.
     * 
     * @param pRoleToken
     *            The role token.
     * @param pInputDataType
     *            The input data type to access.
     * @param pInputData
     *            The input data to access.
     * @param pProperties
     *            The values access properties.
     */
    public CacheableInputDataAccess(final String pRoleToken,
            final CacheableInputDataType pInputDataType,
            final CacheableInputData pInputData,
            final ValuesAccessProperties pProperties) {
        super(pRoleToken, pInputDataType, pInputData, pProperties);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.inputdata.BusinessInputData#getFieldGroup(java.lang.String)
     */
    @Override
    public BusinessFieldGroup getFieldGroup(String pFieldGroupName) {
        return new CacheableFieldGroupAccess(getType().getDisplayGroup(
                pFieldGroupName));
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.inputdata.BusinessInputData#getFieldGroupNames()
     */
    @Override
    public List<String> getFieldGroupNames() {
        List<String> lGroupNames = new ArrayList<String>();
        for (DisplayGroup lGroup : getType().getDisplayGroups()) {
            lGroupNames.add(lGroup.getName());
        }
        return lGroupNames;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.inputdata.BusinessInputData#getFieldGroups()
     */
    @Override
    public List<BusinessFieldGroup> getFieldGroups() {
        List<BusinessFieldGroup> lGroups = new ArrayList<BusinessFieldGroup>();
        for (DisplayGroup lGroup : getType().getDisplayGroups()) {
            lGroups.add(new CacheableFieldGroupAccess(
                    getType().getDisplayGroup(lGroup.getName())));
        }
        return lGroups;
    }
}