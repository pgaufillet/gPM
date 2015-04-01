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
import org.topcased.gpm.business.values.field.simple.BusinessRealField;
import org.topcased.gpm.business.values.impl.cacheable.ICacheableParentAccess;

/**
 * Access on a simple field of type Real.
 * 
 * @author tpanuel
 */
public class CacheableRealFieldAccess extends
        AbstractCacheableSimpleFieldAccess<Double> implements BusinessRealField {
    /**
     * Create an access on an empty simple field of type Real.
     * 
     * @param pSimpleField
     *            The simple field to access.
     * @param pParentAccess
     *            The parent access.
     */
    public CacheableRealFieldAccess(final SimpleField pSimpleField,
            final ICacheableParentAccess pParentAccess) {
        super(pSimpleField, pParentAccess);
    }

    /**
     * Create an access on a simple field of type Real.
     * 
     * @param pSimpleField
     *            The simple field to access.
     * @param pValue
     *            The field value data to access.
     * @param pParentAccess
     *            The parent access.
     */
    public CacheableRealFieldAccess(final SimpleField pSimpleField,
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
    protected String format(final Double pValue) {
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
    protected Double parse(final String pValue) {
        Double lRealValue = null;

        if (pValue != null) {
            // Value is parsed
            try {
                lRealValue = Double.valueOf(pValue);
            }
            catch (NumberFormatException e) {
                // Null, if no value or bad format
            }
        }

        return lRealValue;
    }
}