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

import java.text.ParseException;
import java.util.Date;

import org.topcased.gpm.business.serialization.data.FieldValueData;
import org.topcased.gpm.business.serialization.data.SimpleField;
import org.topcased.gpm.business.values.field.simple.BusinessDateField;
import org.topcased.gpm.business.values.impl.cacheable.ICacheableParentAccess;
import org.topcased.gpm.domain.util.FieldsUtil;

/**
 * Access on a simple field of type Date.
 * 
 * @author tpanuel
 */
public class CacheableDateFieldAccess extends
        AbstractCacheableSimpleFieldAccess<Date> implements BusinessDateField {
    /**
     * Create an access on an empty simple field of type Date.
     * 
     * @param pSimpleField
     *            The simple field to access.
     * @param pParentAccess
     *            The parent access.
     */
    public CacheableDateFieldAccess(final SimpleField pSimpleField,
            final ICacheableParentAccess pParentAccess) {
        super(pSimpleField, pParentAccess);
    }

    /**
     * Create an access on a simple field of type Date.
     * 
     * @param pSimpleField
     *            The simple field to access.
     * @param pValue
     *            The field value data to access.
     * @param pParentAccess
     *            The parent access.
     */
    public CacheableDateFieldAccess(final SimpleField pSimpleField,
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
    protected String format(final Date pValue) {
        if (pValue == null) {
            return null;
        }
        else {
            return FieldsUtil.formatDate(pValue);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.impl.cacheable.AbstractCacheableSimpleFieldAccess#parse(java.lang.String)
     */
    @Override
    protected Date parse(final String pValue) {
        Date lDateValue = null;

        if (pValue != null) {
            // Value is parsed
            try {
                lDateValue = FieldsUtil.parseDate(pValue);
            }
            catch (ParseException e) {
                // Null, if no value or bad format
            }
        }

        return lDateValue;
    }
}