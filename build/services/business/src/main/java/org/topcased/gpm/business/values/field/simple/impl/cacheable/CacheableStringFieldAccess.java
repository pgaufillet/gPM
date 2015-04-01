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
package org.topcased.gpm.business.values.field.simple.impl.cacheable;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.ServiceLocator;
import org.topcased.gpm.business.serialization.data.FieldValueData;
import org.topcased.gpm.business.serialization.data.SimpleField;
import org.topcased.gpm.business.values.field.simple.BusinessStringField;
import org.topcased.gpm.business.values.impl.cacheable.ICacheableParentAccess;

/**
 * Access on a simple field of type String.
 * 
 * @author tpanuel
 */
public class CacheableStringFieldAccess extends
        AbstractCacheableSimpleFieldAccess<String> implements
        BusinessStringField {
    /**
     * Create an access on a simple field of type String.
     * 
     * @param pSimpleField
     *            The simple field to access.
     * @param pValue
     *            The field value data to access.
     * @param pParentAccess
     *            The parent access.
     */
    public CacheableStringFieldAccess(final SimpleField pSimpleField,
            final FieldValueData pValue,
            final ICacheableParentAccess pParentAccess) {
        super(pSimpleField, pValue, pParentAccess);
    }

    /**
     * Create an access on an empty simple field of type String.
     * 
     * @param pSimpleField
     *            The simple field to access.
     * @param pParentAccess
     *            The parent access.
     */
    public CacheableStringFieldAccess(final SimpleField pSimpleField,
            final ICacheableParentAccess pParentAccess) {
        super(pSimpleField, pParentAccess);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.impl.cacheable.AbstractCacheableSimpleFieldAccess#format(java.lang.Object)
     */
    @Override
    protected String format(final String pValue) {
        return pValue;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessStringField#getInternalUrlSheetReference()
     */
    @Override
    public String getInternalUrlSheetReference() {
        if (StringUtils.isNotBlank(get())) {
            return ServiceLocator.instance().getSheetService().getSheetRefStringByKey(
                    getRoleToken(), get());
        }
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessStringField#getSize()
     */
    @Override
    public int getSize() {
        return getFieldType().getSizeAsInt();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.impl.cacheable.AbstractCacheableSimpleFieldAccess#parse(java.lang.String)
     */
    @Override
    protected String parse(final String pValue) {
        return pValue;
    }
}