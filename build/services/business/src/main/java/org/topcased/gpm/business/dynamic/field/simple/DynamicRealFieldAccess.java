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

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.dynamic.field.AbstractDynamicFieldAccess;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.serialization.data.FieldValueData;
import org.topcased.gpm.business.serialization.data.SimpleField;
import org.topcased.gpm.util.comparator.DataBaseValuesComparator;

/**
 * Access on a real simple field field
 * 
 * @author tpanuel
 */
public class DynamicRealFieldAccess extends AbstractDynamicFieldAccess<Double> {
    // FIXME: On IE, the Dojo toolkit may send a "null" value for an
    // empty real or integer field. We catch the problem here (not the
    // very best way to handle this problem, ok).
    private final static String DOJO_NULL_STRING = "null";

    /**
     * Create access on a real simple field
     * 
     * @param pField
     *            The simple field
     */
    public DynamicRealFieldAccess(SimpleField pField) {
        super(pField, Double.class);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.dynamic.field.AbstractDynamicFieldAccess#getFieldValue(java.lang.Object,
     *      org.topcased.gpm.business.extensions.service.Context)
     */
    @Override
    public Object getFieldValue(Object pDomainContainer, Context pContext) {
        final Double lDomainValue = getValue(pDomainContainer);

        // If no value, return a null object
        // For memory space optimization
        if (lDomainValue == null) {
            return null;
        }
        else {
            return new FieldValueData(getFieldName(),
                    String.valueOf(lDomainValue));
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
        final Double lDomainValue = getValue(pDomainContainer);

        if (lDomainValue == null) {
            return StringUtils.EMPTY;
        }
        else {
            return lDomainValue.toString();
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
                    if (StringUtils.isBlank(lBusinessValue)
                            || lBusinessValue.equals(DOJO_NULL_STRING)) {
                        setValue(pDomainContainer, null);
                    }
                    else {
                        setValue(pDomainContainer,
                                Double.parseDouble(lBusinessValue));
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