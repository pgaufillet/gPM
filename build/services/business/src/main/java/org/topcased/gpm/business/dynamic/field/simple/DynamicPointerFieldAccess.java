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
import org.topcased.gpm.business.ContextLocator;
import org.topcased.gpm.business.dynamic.DynamicValuesContainerAccess;
import org.topcased.gpm.business.dynamic.field.AbstractDynamicFieldAccess;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.serialization.data.Field;
import org.topcased.gpm.business.serialization.data.PointerFieldValueData;
import org.topcased.gpm.business.sheet.impl.SheetServiceImpl;
import org.topcased.gpm.domain.fields.PointerFieldValue;
import org.topcased.gpm.domain.fields.ValuesContainer;
import org.topcased.gpm.util.comparator.DataBaseValuesComparator;

/**
 * Access on a pointer field
 * 
 * @author tpanuel
 */
public class DynamicPointerFieldAccess extends
        AbstractDynamicFieldAccess<PointerFieldValue> {
    private final SheetServiceImpl sheetService;

    /**
     * Create access on a pointer field
     * 
     * @param pField
     *            The simple field
     */
    public DynamicPointerFieldAccess(Field pField) {
        super(pField, PointerFieldValue.class);
        sheetService =
                (SheetServiceImpl) ContextLocator.getContext().getBean(
                        "sheetServiceImpl");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.dynamic.field.AbstractDynamicFieldAccess#getFieldValue(java.lang.Object,
     *      org.topcased.gpm.business.extensions.service.Context)
     */
    @Override
    public Object getFieldValue(Object pDomainContainer, Context pContext) {
        final PointerFieldValue lDomainValue = getValue(pDomainContainer);

        // Null attached field if the no referenced id or no field label
        // For memory space optimization
        if (lDomainValue == null
                || lDomainValue.getReferencedContainerId() == null
                || lDomainValue.getReferencedFieldLabel() == null) {
            return null;
        }
        else {
            final PointerFieldValueData lBusinessValue =
                    new PointerFieldValueData(getFieldName());

            lBusinessValue.setReferencedContainerId(lDomainValue.getReferencedContainerId());
            lBusinessValue.setReferencedFieldLabel(lDomainValue.getReferencedFieldLabel());

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
        final PointerFieldValue lDomainValue = getValue(pDomainContainer);

        if (lDomainValue == null) {
            return StringUtils.EMPTY;
        }
        else {
            return lDomainValue.getReferencedContainerId() + " "
                    + lDomainValue.getReferencedFieldLabel();
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
            // Value must be a PointerFieldValueData
            if (pFieldValue instanceof PointerFieldValueData) {
                final PointerFieldValueData lBusinessValue =
                        (PointerFieldValueData) pFieldValue;

                // Delete pointer field value if field label is not defined
                if (lBusinessValue.getReferencedFieldLabel() == null) {
                    setValue(pDomainContainer, null);
                }
                else {
                    final String lBusinessReferencedContainerId =
                            getReferencedContainerId(lBusinessValue, pContext);
                    PointerFieldValue lDomainValue = getValue(pDomainContainer);

                    // If no object, create a new one
                    if (lDomainValue == null) {
                        lDomainValue = PointerFieldValue.newInstance();
                        setValue(pDomainContainer, lDomainValue);
                    }

                    // Update the values of the domain object, only if different values
                    if (!DataBaseValuesComparator.equals(
                            lDomainValue.getReferencedContainerId(),
                            lBusinessReferencedContainerId)) {
                        lDomainValue.setReferencedContainerId(lBusinessReferencedContainerId);
                    }
                    if (!DataBaseValuesComparator.equals(
                            lDomainValue.getReferencedFieldLabel(),
                            lBusinessValue.getReferencedFieldLabel())) {
                        lDomainValue.setReferencedFieldLabel(lBusinessValue.getReferencedFieldLabel());
                    }
                }
            }
            else {
                throw new GDMException("Invalid value type for simple field: "
                        + pFieldValue.getClass());
            }
        }
    }

    private String getReferencedContainerId(PointerFieldValueData pValue,
            Context pContext) {
        // Values container is on the context
        final ValuesContainer lDomainContainer =
                pContext.get(
                        DynamicValuesContainerAccess.VALUES_CONTAINER_4_CHOICE_FIELD,
                        ValuesContainer.class);
        final String lReferencedId;

        // Either referenced container functional REFERENCE and PRODUCT are filled
        if (pValue.getReferencedContainerRef() != null
                && pValue.getReferencedProduct() != null) {
            lReferencedId =
                    sheetService.getSheetIdByReference(
                            lDomainContainer.getDefinition().getBusinessProcess().getName(),
                            pValue.getReferencedProduct(),
                            pValue.getReferencedContainerRef());
        }
        // Or referenced container ID
        else {
            lReferencedId = pValue.getReferencedContainerId();
        }

        // If the id of the reference is not found -> exception
        if (lReferencedId == null) {
            if (pValue.getReferencedContainerRef() != null) {
                throw new GDMException("Invalid pointer field value "
                        + pValue.getName() + " : referencedContainerRef '"
                        + pValue.getReferencedContainerRef()
                        + "' cannot be found in product '"
                        + pValue.getReferencedProduct() + "'.");
            }
            else {
                throw new GDMException("Invalid pointer field value "
                        + pValue.getName() + " : referencedContainerId '"
                        + pValue.getReferencedContainerId()
                        + "' cannot be found.");
            }
        }

        return lReferencedId;
    }
}