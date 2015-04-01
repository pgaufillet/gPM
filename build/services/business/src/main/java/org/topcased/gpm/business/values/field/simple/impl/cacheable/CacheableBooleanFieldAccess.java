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

import org.topcased.gpm.business.serialization.data.FieldValueData;
import org.topcased.gpm.business.serialization.data.SimpleField;
import org.topcased.gpm.business.values.field.simple.BusinessBooleanField;
import org.topcased.gpm.business.values.impl.cacheable.ICacheableParentAccess;

/**
 * Access on a simple field of type Boolean.
 * 
 * @author tpanuel
 */
public class CacheableBooleanFieldAccess extends
        AbstractCacheableSimpleFieldAccess<Boolean> implements
        BusinessBooleanField {
    /**
     * Create an access on an empty simple field of type Boolean.
     * 
     * @param pSimpleField
     *            The simple field to access.
     * @param pParentAccess
     *            The parent access.
     */
    public CacheableBooleanFieldAccess(final SimpleField pSimpleField,
            final ICacheableParentAccess pParentAccess) {
        super(pSimpleField, pParentAccess);
    }

    /**
     * Create an access on a simple field of type Boolean.
     * 
     * @param pSimpleField
     *            The simple field to access.
     * @param pValue
     *            The field value data to access.
     * @param pParentAccess
     *            The parent access.
     */
    public CacheableBooleanFieldAccess(final SimpleField pSimpleField,
            final FieldValueData pValue,
            final ICacheableParentAccess pParentAccess) {
        super(pSimpleField, pValue, pParentAccess);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.impl.cacheable.AbstractCacheableSimpleFieldAccess#format(java.lang.Object)
     */
    @Override
    protected String format(final Boolean pValue) {
        if (pValue == null) {
            return null;
        }
        else {
            return pValue.toString();
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.impl.cacheable.AbstractCacheableSimpleFieldAccess#parse(java.lang.String)
     */
    @Override
    protected Boolean parse(final String pValue) {
        return Boolean.valueOf(pValue);
    }
}