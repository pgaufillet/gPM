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

import org.topcased.gpm.business.ContextLocator;
import org.topcased.gpm.business.dynamic.DynamicValuesContainerAccess;
import org.topcased.gpm.business.dynamic.field.AbstractDynamicFieldAccess;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.exception.InvalidFieldValueException;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.serialization.data.ChoiceField;
import org.topcased.gpm.business.serialization.data.FieldValueData;
import org.topcased.gpm.domain.dictionary.CategoryValue;
import org.topcased.gpm.domain.dictionary.EnvironmentDao;
import org.topcased.gpm.domain.fields.ValuesContainer;
import org.topcased.gpm.util.comparator.DataBaseValuesComparator;

/**
 * Access on a choice field
 * 
 * @author tpanuel
 */
public class DynamicChoiceFieldAccess extends
        AbstractDynamicFieldAccess<CategoryValue> {
    private final EnvironmentDao environmentDao;

    private final String categoryName;

    /**
     * Create access on a choice field
     * 
     * @param pField
     *            The choice field
     */
    public DynamicChoiceFieldAccess(ChoiceField pField) {
        super(pField, CategoryValue.class);
        categoryName = pField.getCategoryName();
        environmentDao =
                (EnvironmentDao) ContextLocator.getContext().getBean(
                        "environmentDao");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.dynamic.field.AbstractDynamicFieldAccess#getFieldValue(java.lang.Object,
     *      org.topcased.gpm.business.extensions.service.Context)
     */
    @Override
    public Object getFieldValue(Object pDomainContainer, Context pContext) {
        final CategoryValue lDomainValue = getValue(pDomainContainer);

        // If no value, return a null object
        // For memory space optimization
        if (lDomainValue == null) {
            return null;
        }
        else {
            final FieldValueData lBusinessValue =
                    new FieldValueData(getFieldName());

            lBusinessValue.setValue(lDomainValue.getValue());

            return lBusinessValue;
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
        final CategoryValue lDomainValue = getValue(pDomainContainer);

        if (lDomainValue == null) {
            return null;
        }
        else {
            return lDomainValue.getValue();
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
                final FieldValueData lBusinessValue =
                        (FieldValueData) pFieldValue;

                if (lBusinessValue.getValue() == null) {
                    // Empty field
                    setValue(pDomainContainer, null);
                }
                else {
                    final CategoryValue lOldDomainValue =
                            getValue(pDomainContainer);

                    // Update only if necessary
                    // New value or different value
                    if (lOldDomainValue == null
                            || !DataBaseValuesComparator.equals(
                                    lOldDomainValue.getValue(),
                                    lBusinessValue.getValue())) {
                        // The top level values container has been put on the context
                        final ValuesContainer lValuesContainer =
                                pContext.get(
                                        DynamicValuesContainerAccess.VALUES_CONTAINER_4_CHOICE_FIELD,
                                        ValuesContainer.class);
                        // The value must exist on the category
                        final CategoryValue lCategoryValue =
                                environmentDao.getContainerCategoryValue(
                                        lValuesContainer.getId(), categoryName,
                                        lBusinessValue.getValue());

                        // Value is not found in the category -> Invalid value
                        if (lCategoryValue == null) {
                            throw new InvalidFieldValueException(
                                    lValuesContainer.getDefinition().getName(),
                                    getFieldName(), lBusinessValue.getValue());
                        }

                        setValue(pDomainContainer, lCategoryValue);
                    }
                }
            }
            else {
                throw new GDMException("Invalid value type for choice field: "
                        + pFieldValue.getClass());
            }
        }
    }
}