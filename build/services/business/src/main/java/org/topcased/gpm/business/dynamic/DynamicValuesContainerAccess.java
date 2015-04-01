/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.dynamic;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.topcased.gpm.business.dynamic.field.AbstractDynamicFieldAccess;
import org.topcased.gpm.business.dynamic.field.DynamicFieldAccessFactory;
import org.topcased.gpm.business.dynamic.field.multiple.DynamicMultipleFieldAccess;
import org.topcased.gpm.business.dynamic.field.multivalued.DynamicMultivaluedFieldAccess;
import org.topcased.gpm.business.exception.FieldValidationException;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.fields.impl.CacheableFieldsContainer;
import org.topcased.gpm.business.fields.impl.CacheableValuesContainer;
import org.topcased.gpm.business.serialization.data.Field;
import org.topcased.gpm.business.serialization.data.MultipleField;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.domain.dynamic.container.field.multivalued.DynamicMultivaluedField;
import org.topcased.gpm.domain.fields.ValuesContainer;

/**
 * Define all the field's accessors for a fields container
 * 
 * @author tpanuel
 */
public class DynamicValuesContainerAccess {
    public final static String VALUES_CONTAINER_4_CHOICE_FIELD =
            "VALUES_CONTAINER_4_CHOISE_FIELD";

    private final Map<String, AbstractDynamicFieldAccess<?>> accesses =
            new HashMap<String, AbstractDynamicFieldAccess<?>>();

    private final String typeId;

    private final String typeName;

    /**
     * Create field's accessors for a fields container
     * 
     * @param pType
     *            The values container
     * @param pIsForRevision
     *            If it's an accessor on a revision
     */
    public DynamicValuesContainerAccess(CacheableFieldsContainer pType,
            boolean pIsForRevision) {
        typeId = pType.getId();
        typeName = pType.getName();
        // Create accessor for all fields
        for (Field lField : pType.getTopLevelFields()) {
            accesses.put(lField.getLabelKey(),
                    DynamicFieldAccessFactory.getFieldAccessor(lField,
                            pIsForRevision));
        }
        // For sheets, create accessor for reference fields
        if (pType instanceof CacheableSheetType) {
            final MultipleField lMultipleField =
                    ((CacheableSheetType) pType).getReferenceField();

            accesses.put(lMultipleField.getLabelKey(),
                    DynamicFieldAccessFactory.getFieldAccessor(lMultipleField,
                            pIsForRevision));
        }
    }

    /**
     * Getter on the id of the type
     * 
     * @return The id of the type
     */
    public String getTypeId() {
        return typeId;
    }

    /**
     * Getter on the name of the type
     * 
     * @return The name of the type
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * Get a field value.
     * <p>
     * It searches a field value depending on the name of the field and its
     * container.
     * </p>
     * 
     * @param pFieldName
     *            The name of the field to access
     * @param pDomainContainer
     *            The container of the domain that own the field value
     * @return The field value or null if it was not found
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Object getFieldValue(String pFieldName,
            ValuesContainer pDomainContainer) {
        final Context lContext = Context.getEmptyContext();

        // Put top level values container on the context
        lContext.put(VALUES_CONTAINER_4_CHOICE_FIELD, pDomainContainer);

        final AbstractDynamicFieldAccess<?> lFieldAccess =
                accesses.get(pFieldName);

        // Found on top level fields
        if (lFieldAccess != null) {
            return lFieldAccess.getFieldValue(pDomainContainer, lContext);
        }

        // Search on sub fields
        for (AbstractDynamicFieldAccess<?> lEachFieldAccess : accesses.values()) {
            // Search on multiple fields
            if (lEachFieldAccess instanceof DynamicMultipleFieldAccess) {
                final AbstractDynamicFieldAccess<?> lSubFieldAccess =
                        ((DynamicMultipleFieldAccess) lEachFieldAccess).getAccesses().get(
                                pFieldName);

                // Search if it's a sub field
                if (lSubFieldAccess != null) {
                    return lSubFieldAccess.getFieldValue(pDomainContainer,
                            lContext);
                }
            }

            // Search on multiple multi valued fields
            if (lEachFieldAccess instanceof DynamicMultivaluedFieldAccess) {
                final AbstractDynamicFieldAccess<?> lMonoFieldAccess =
                        ((DynamicMultivaluedFieldAccess) lEachFieldAccess).getFieldAccess();

                if (lMonoFieldAccess instanceof DynamicMultipleFieldAccess) {
                    // Search if it's a sub field
                    final AbstractDynamicFieldAccess<?> lSubFieldAccess =
                            ((DynamicMultipleFieldAccess) lMonoFieldAccess).getAccesses().get(
                                    pFieldName);

                    if (lSubFieldAccess != null) {
                        // Compute value
                        List<Object> lValues = null;
                        final List<DynamicMultivaluedField> lSubElements =
                                (List<DynamicMultivaluedField>) lEachFieldAccess.getValue(pDomainContainer);

                        // Put all the sub-fields on a list
                        if (lSubElements != null && lSubElements.size() > 0) {
                            lValues = new LinkedList<Object>();
                            for (DynamicMultivaluedField lSubElement : lSubElements) {
                                final Object lValue =
                                        lSubFieldAccess.getFieldValue(
                                                lSubElement, lContext);

                                if (lValue instanceof List) {
                                    lValues.addAll((List) lValue);
                                }
                                else {
                                    lValues.add(lValue);
                                }
                            }
                        }

                        return lValues;
                    }
                }
            }
        }

        // Not found
        throw new GDMException("File not found " + pFieldName);
    }

    /**
     * Convert a field value on a String
     * 
     * @param pFieldName
     *            The name of the field to access
     * @param pDomainContainer
     *            The container of the domain that own the field value
     * @return The field value as a String
     */
    public String getFieldValueAsString(String pFieldName,
            ValuesContainer pDomainContainer) {
        final Context lContext = Context.getEmptyContext();

        // Put top level values container on the context
        lContext.put(VALUES_CONTAINER_4_CHOICE_FIELD, pDomainContainer);

        return accesses.get(pFieldName).getFieldValueAsString(pDomainContainer,
                lContext);
    }

    /**
     * Setter on a field value
     * 
     * @param pFieldName
     *            The name of the field to access
     * @param pDomainContainer
     *            The container of the domain that own the field value
     * @param pFieldValue
     *            The new field value
     */
    public void setFieldValue(String pFieldName,
            ValuesContainer pDomainContainer, Object pFieldValue) {
        final Context lContext = Context.getEmptyContext();

        // Put top level values container on the context
        lContext.put(VALUES_CONTAINER_4_CHOICE_FIELD, pDomainContainer);

        accesses.get(pFieldName).setFieldValue(pDomainContainer, pFieldValue,
                lContext);
    }

    /**
     * Update Domain container From Business container
     * 
     * @param pDomainContainer
     *            The Domain container
     * @param pBusinessContainer
     *            The Business container
     * @param pContext
     *            The context
     * @throws FieldValidationException
     *             If the field is not valid.
     */
    @SuppressWarnings("rawtypes")
    public void updateDomainFromBusiness(ValuesContainer pDomainContainer,
            CacheableValuesContainer pBusinessContainer, Context pContext)
        throws FieldValidationException {
        final Context lContext = Context.createContext(pContext);

        // Put top level values container on the context
        lContext.put(VALUES_CONTAINER_4_CHOICE_FIELD, pDomainContainer);

        // Update all fields
        for (AbstractDynamicFieldAccess lAccessor : accesses.values()) {
            lAccessor.setFieldValue(pDomainContainer,
                    pBusinessContainer.getValue(lAccessor.getFieldName()),
                    lContext);
        }
    }

    /**
     * Update Business container From Domain container
     * 
     * @param pBusinessContainer
     *            The Business container
     * @param pDomainContainer
     *            The Domain container
     * @param pContext
     *            The context
     */
    @SuppressWarnings("rawtypes")
    public void updateBusinessFromDomain(
            CacheableValuesContainer pBusinessContainer,
            ValuesContainer pDomainContainer, Context pContext) {
        final Context lContext = Context.createContext(pContext);

        // Put top level values container on the context
        lContext.put(VALUES_CONTAINER_4_CHOICE_FIELD, pDomainContainer);

        // Update all fields
        for (AbstractDynamicFieldAccess lAccessor : accesses.values()) {
            final Object lValue =
                    lAccessor.getFieldValue(pDomainContainer, lContext);

            // Only add not null value on the map
            if (lValue != null) {
                pBusinessContainer.getValuesMap().put(lAccessor.getFieldName(),
                        lValue);
            }
        }
    }
}
