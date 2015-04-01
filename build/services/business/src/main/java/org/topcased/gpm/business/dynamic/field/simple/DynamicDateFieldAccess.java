/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.dynamic.field.simple;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.dynamic.field.AbstractDynamicFieldAccess;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.serialization.data.FieldValueData;
import org.topcased.gpm.business.serialization.data.SimpleField;
import org.topcased.gpm.domain.util.FieldsUtil;
import org.topcased.gpm.util.comparator.DataBaseValuesComparator;

/**
 * Access on a date simple field field
 * 
 * @author tpanuel
 */
public class DynamicDateFieldAccess extends AbstractDynamicFieldAccess<Date> {
    /**
     * Create access on a date simple field
     * 
     * @param pField
     *            The simple field
     */
    public DynamicDateFieldAccess(SimpleField pField) {
        super(pField, Date.class);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.dynamic.field.AbstractDynamicFieldAccess#getFieldValue(java.lang.Object,
     *      org.topcased.gpm.business.extensions.service.Context)
     */
    @Override
    public Object getFieldValue(Object pDomainContainer, Context pContext) {
        final Date lDomainValue = getValue(pDomainContainer);

        // If no value, return a null object
        // For memory space optimization
        if (lDomainValue == null) {
            return null;
        }
        else {
            return new FieldValueData(getFieldName(),
                    FieldsUtil.formatDate(lDomainValue));
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.dynamic.field.AbstractDynamicFieldAccess#getFieldValueAsString(java.lang.Object,
     *      org.topcased.gpm.business.extensions.service.Context)
     */
    @Override
    public String getFieldValueAsString(Object pDomainContainer,
            Context pContext) {
        final Date lDomainValue = getValue(pDomainContainer);

        if (lDomainValue == null) {
            return StringUtils.EMPTY;
        }
        else {
            return FieldsUtil.formatDate(lDomainValue);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.dynamic.field.AbstractDynamicFieldAccess#setFieldValue(java.lang.Object,
     *      java.lang.Object,
     *      org.topcased.gpm.business.extensions.service.Context)
     */
    @Override
    public void setFieldValue(Object pDomainContainer, Object pFieldValue,
            Context pContext) {
        // If null, the sub-fields is not updated
        if (pFieldValue != null) {
            // Value must be a FieldValueData
            if (pFieldValue instanceof FieldValueData) {
                final String lBusinessValue =
                        ((FieldValueData) pFieldValue).getValue();
                final String lOldValue =
                        getFieldValueAsString(pDomainContainer, pContext);

                // Update only if different values
                if (!DataBaseValuesComparator.equals(lOldValue, lBusinessValue)) {
                    try {
                        setValue(pDomainContainer,
                                FieldsUtil.parseDate(lBusinessValue));
                    }
                    catch (ParseException e) {
                        throw new GDMException("Invalid date format: "
                                + lBusinessValue);
                    }
                }
            }
            else {
                throw new GDMException("Invalid value type for simple field: "
                        + pFieldValue.getClass());
            }
        }
    }
}