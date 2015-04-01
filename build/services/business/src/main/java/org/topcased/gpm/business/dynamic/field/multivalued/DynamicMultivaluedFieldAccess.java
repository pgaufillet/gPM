/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.dynamic.field.multivalued;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.ContextLocator;
import org.topcased.gpm.business.dynamic.field.AbstractDynamicFieldAccess;
import org.topcased.gpm.business.dynamic.field.DynamicFieldAccessFactory;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.serialization.data.AttachedFieldValueData;
import org.topcased.gpm.business.serialization.data.Field;
import org.topcased.gpm.business.serialization.data.FieldValueData;
import org.topcased.gpm.business.serialization.data.PointerFieldValueData;
import org.topcased.gpm.domain.dynamic.DynamicObjectGenerator;
import org.topcased.gpm.domain.dynamic.container.field.multivalued.DynamicMultivaluedField;
import org.topcased.gpm.domain.dynamic.container.field.multivalued.DynamicMultivaluedFieldGeneratorFactory;
import org.topcased.gpm.domain.dynamic.container.field.multivalued.DynamicMultivaluedFieldRevisionGeneratorFactory;
import org.topcased.gpm.domain.fields.ValuesContainerDao;
import org.topcased.gpm.util.bean.GpmPair;
import org.topcased.gpm.util.iterator.GpmPairIterator;

/**
 * Access on a multi valued field
 * 
 * @author tpanuel
 */
public class DynamicMultivaluedFieldAccess extends
        AbstractDynamicFieldAccess<List<DynamicMultivaluedField>> {
    private final String fieldSeparator = "\n";

    private final AbstractDynamicFieldAccess<? extends Object> fieldAccess;

    private final DynamicObjectGenerator<DynamicMultivaluedField> generator;

    private ValuesContainerDao valuesContainerDao;

    /**
     * Create access on a multiple field
     * 
     * @param pField
     *            The multi valued field
     * @param pIsForRevision
     *            If it's an access on a revision
     */
    public DynamicMultivaluedFieldAccess(Field pField, boolean pIsForRevision) {
        super(pField, List.class);
        // Access on sub-fields
        fieldAccess =
                DynamicFieldAccessFactory.getSubFieldAccessor(pField,
                        pIsForRevision);
        // Generator of sub-fields
        if (pIsForRevision) {
            generator =
                    DynamicMultivaluedFieldRevisionGeneratorFactory.getInstance().getDynamicObjectGenerator(
                            pField.getId());
        }
        else {
            generator =
                    DynamicMultivaluedFieldGeneratorFactory.getInstance().getDynamicObjectGenerator(
                            pField.getId());
        }
        valuesContainerDao =
                (ValuesContainerDao) ContextLocator.getContext().getBean(
                        "valuesContainerDao");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.dynamic.field.AbstractDynamicFieldAccess#getFieldValue(java.lang.Object,
     *      org.topcased.gpm.business.extensions.service.Context)
     */
    @Override
    public Object getFieldValue(Object pDomainContainer, Context pContext) {
        final List<DynamicMultivaluedField> lSubElements =
                (List<DynamicMultivaluedField>) getValue(pDomainContainer);
        final List<Object> lValues = new ArrayList<Object>();

        // Put all the sub-fields on a list
        if (lSubElements != null) {
            for (DynamicMultivaluedField lSubElement : lSubElements) {
                lValues.add(fieldAccess.getFieldValue(lSubElement, pContext));
            }
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
        final Iterator<DynamicMultivaluedField> lSubElementIterator =
                ((List<DynamicMultivaluedField>) getValue(pDomainContainer)).iterator();

        // Concat all the sub-fields
        while (lSubElementIterator.hasNext()) {
            lStringBuffer.append(fieldAccess.getFieldValueAsString(
                    lSubElementIterator.next(), pContext));

            // Don't add separator at the end
            if (lSubElementIterator.hasNext()) {
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
    @Override
    public void setFieldValue(Object pDomainContainer, Object pFieldValue,
            Context pContext) {
        // If null, the field is not updated
        if (pFieldValue != null) {
            final List<DynamicMultivaluedField> lNewSubElements =
                    new LinkedList<DynamicMultivaluedField>();

            // If the list of values has been changed
            boolean lListChanged = false;

            // if the field value is empty
            if (isEmpty(pFieldValue)) {
                lListChanged = true;
            }
            else {
                final List<DynamicMultivaluedField> lOldSubElements =
                        (List<DynamicMultivaluedField>) getValue(pDomainContainer);
                final GpmPairIterator<DynamicMultivaluedField, Object> lValuesIterator =
                        new GpmPairIterator<DynamicMultivaluedField, Object>(
                                lOldSubElements, pFieldValue);

                // Update all the sub-fields
                while (lValuesIterator.hasNext()) {
                    final GpmPair<DynamicMultivaluedField, Object> lValues =
                            lValuesIterator.next();

                    if (lValues.getSecond() == null) {
                        if (lValues.getFirst() != null) {
                            // Remove sub field
                            valuesContainerDao.removeSubField(lValues.getFirst());
                            setValue(pDomainContainer, lNewSubElements);
                        }
                        // Value deleted of list -> change
                        lListChanged = true;
                    }
                    else {
                        // Update sub field
                        final DynamicMultivaluedField lSubElement;

                        if (lValues.getFirst() == null) {
                            lSubElement = generator.create();
                            // Value added on list -> change
                            lListChanged = true;
                        }
                        else {
                            lSubElement = lValues.getFirst();
                            // Only update -> No change
                        }

                        fieldAccess.setFieldValue(lSubElement,
                                lValues.getSecond(), pContext);

                        lNewSubElements.add(lSubElement);
                    }
                }
                // clear the field access local cache as all fields have been computed
                fieldAccess.clearLocalCache();
            }

            // Change the list only if necessary
            if (lListChanged) {
                setValue(pDomainContainer, lNewSubElements);
            }
        }
    }

    /**
     * Determine if a field value is empty
     * 
     * @param pFieldValue
     *            The field value
     * @return true is the field value is empty, false otherwise
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private boolean isEmpty(Object pFieldValue) {
        if (!(pFieldValue instanceof List)) {
            if (pFieldValue instanceof AttachedFieldValueData) {
                AttachedFieldValueData lFieldValueData =
                        (AttachedFieldValueData) pFieldValue;
                return StringUtils.isBlank(lFieldValueData.getFilename());
            }
            else if (pFieldValue instanceof HashMap) {
                HashMap<String, FieldValueData> lFieldValueData =
                        (HashMap<String, FieldValueData>) pFieldValue;
                return lFieldValueData.size() == 1
                        && isEmpty(new LinkedList(lFieldValueData.values()).getFirst());
            }
            else if (!(pFieldValue instanceof PointerFieldValueData)) {
                FieldValueData lFieldValueData = (FieldValueData) pFieldValue;
                return StringUtils.isEmpty(lFieldValueData.getValue());
            }
        }
        return false;
    }

    /**
     * Get the field access.
     * 
     * @return The fieldAccess.
     */
    public AbstractDynamicFieldAccess<? extends Object> getFieldAccess() {
        return fieldAccess;
    }
}
