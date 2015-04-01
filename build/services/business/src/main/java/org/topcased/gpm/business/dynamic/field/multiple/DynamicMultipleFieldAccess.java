/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.dynamic.field.multiple;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.topcased.gpm.business.dynamic.field.AbstractDynamicFieldAccess;
import org.topcased.gpm.business.dynamic.field.DynamicFieldAccessFactory;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.serialization.data.Field;
import org.topcased.gpm.business.serialization.data.MultipleField;

/**
 * Access on a multiple field
 * 
 * @author tpanuel
 */
public class DynamicMultipleFieldAccess extends
        AbstractDynamicFieldAccess<Object> {
    private final Map<String, AbstractDynamicFieldAccess<? extends Object>> accesses =
            new LinkedHashMap<String, AbstractDynamicFieldAccess<? extends Object>>();

    private final String fieldSeparator;

    /**
     * Create access on a multiple field
     * 
     * @param pField
     *            The multiple field
     * @param pIsForRevision
     *            If it's an access on a revision
     */
    public DynamicMultipleFieldAccess(MultipleField pField,
            boolean pIsForRevision) {
        super(pField, null);
        // One access by sub-field
        for (Field lSubField : pField.getFields()) {
            accesses.put(lSubField.getLabelKey(),
                    DynamicFieldAccessFactory.getFieldAccessor(lSubField,
                            pIsForRevision));
        }
        fieldSeparator = pField.getFieldSeparator();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.dynamic.field.AbstractDynamicFieldAccess#getFieldValue(java.lang.Object,
     *      org.topcased.gpm.business.extensions.service.Context)
     */
    @Override
    public Object getFieldValue(Object pDomainContainer, Context pContext) {
        final Map<String, Object> lValues = new HashMap<String, Object>();

        // Put all the sub-fields on a map
        for (AbstractDynamicFieldAccess<? extends Object> lAccessor : accesses.values()) {
            lValues.put(lAccessor.getFieldName(), lAccessor.getFieldValue(
                    pDomainContainer, pContext));
        }

        // If no value, return a null object
        // For memory space optimization
        if (lValues.isEmpty()) {
            return null;
        }
        else {
            return lValues;
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
        final StringBuffer lStringBuffer = new StringBuffer();
        final Iterator<AbstractDynamicFieldAccess<? extends Object>> lAccessorIterator =
                accesses.values().iterator();

        // Concat all the sub-fields
        while (lAccessorIterator.hasNext()) {
            lStringBuffer.append(lAccessorIterator.next().getFieldValueAsString(
                    pDomainContainer, pContext));
            // Don't add separator at the end
            if (lAccessorIterator.hasNext()) {
                lStringBuffer.append(fieldSeparator);
            }
        }

        return lStringBuffer.toString();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.dynamic.field.AbstractDynamicFieldAccess#setFieldValue(java.lang.Object,
     *      java.lang.Object,
     *      org.topcased.gpm.business.extensions.service.Context)
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void setFieldValue(Object pDomainContainer, Object pFieldValue,
            Context pContext) {
        // If null, the sub-fields are not updated
        if (pFieldValue != null) {
            // Values must be a Map
            if (pFieldValue instanceof Map) {
                final Map<String, Object> lValues =
                        (Map<String, Object>) pFieldValue;

                // Update the values of all the sub-fields
                for (AbstractDynamicFieldAccess lAccessor : accesses.values()) {
                    lAccessor.setFieldValue(pDomainContainer,
                            lValues.get(lAccessor.getFieldName()), pContext);
                }
            }
            else {
                throw new GDMException(
                        "Invalid value type for multiple field: "
                                + pFieldValue.getClass());
            }
        }
    }

    /**
     * Get accesses on the sub fields.
     * 
     * @return Accesses on the sub fields.
     */
    public Map<String, AbstractDynamicFieldAccess<? extends Object>> getAccesses() {
        return accesses;
    }
}