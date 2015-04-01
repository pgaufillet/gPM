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
import org.topcased.gpm.business.values.field.simple.BusinessIntegerField;
import org.topcased.gpm.business.values.impl.cacheable.ICacheableParentAccess;

/**
 * Access on a simple field of type Integer.
 * 
 * @author tpanuel
 */
public class CacheableIntegerFieldAccess extends
        AbstractCacheableSimpleFieldAccess<Integer> implements
        BusinessIntegerField {
    /**
     * Create an access on an empty simple field of type Integer.
     * 
     * @param pSimpleField
     *            The simple field to access.
     * @param pParentAccess
     *            The parent access.
     */
    public CacheableIntegerFieldAccess(final SimpleField pSimpleField,
            final ICacheableParentAccess pParentAccess) {
        super(pSimpleField, pParentAccess);
    }

    /**
     * Create an access on a simple field of type Integer.
     * 
     * @param pSimpleField
     *            The simple field to access.
     * @param pValue
     *            The field value data to access.
     * @param pParentAccess
     *            The parent access.
     */
    public CacheableIntegerFieldAccess(final SimpleField pSimpleField,
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
    protected String format(final Integer pValue) {
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
    protected Integer parse(final String pValue) {
        Integer lIntValue = null;

        if (pValue != null) {
            // Value is parsed
            try {
                lIntValue = Integer.valueOf(pValue);
            }
            catch (NumberFormatException e) {
                // Null, if no value or bad format
            }
        }

        return lIntValue;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessIntegerField#increment()
     */
    public void increment() {
        final Integer lCurrentValue = get();

        if (lCurrentValue == null) {
            final Integer lDefaultValue =
                    parse(getFieldType().getDefaultValue());

            if (lDefaultValue == null) {
                set(1);
            }
            else {
                set(lDefaultValue + 1);
            }
        }
        else {
            set(lCurrentValue + 1);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessIntegerField#decrement()
     */
    public void decrement() {
        final Integer lCurrentValue = get();

        if (lCurrentValue == null) {
            final Integer lDefaultValue =
                    parse(getFieldType().getDefaultValue());

            if (lDefaultValue == null) {
                set(0);
            }
            else {
                set(lDefaultValue);
            }
        }
        else {
            set(lCurrentValue - 1);
        }
    }
}