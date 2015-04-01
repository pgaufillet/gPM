/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.search.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
//import org.apache.log4j.Logger;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.fields.impl.CacheableFieldsContainer;
import org.topcased.gpm.business.scalar.BooleanValueData;
import org.topcased.gpm.business.scalar.DateValueData;
import org.topcased.gpm.business.scalar.IntegerValueData;
import org.topcased.gpm.business.scalar.RealValueData;
import org.topcased.gpm.business.scalar.ScalarValueData;
import org.topcased.gpm.business.scalar.StringValueData;
import org.topcased.gpm.business.search.criterias.impl.FilterFieldsContainerInfo;
import org.topcased.gpm.business.search.criterias.impl.Operators;
import org.topcased.gpm.business.search.criterias.impl.VirtualFieldData;
import org.topcased.gpm.business.search.impl.fields.UsableFieldsManager;
import org.topcased.gpm.business.search.service.UsableFieldData;
import org.topcased.gpm.business.serialization.data.CategoryValue;
import org.topcased.gpm.business.serialization.data.DisplayGroup;
import org.topcased.gpm.business.serialization.data.Field;
import org.topcased.gpm.business.serialization.data.FieldRef;
import org.topcased.gpm.business.serialization.data.MultipleField;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.domain.search.FieldsContainerId;
import org.topcased.gpm.domain.search.FilterField;

/**
 * Functional for search service.
 * 
 * @author mkargbo
 */
public final class SearchUtils {

//    private static final Logger LOGGER = Logger.getLogger(SearchUtils.class);

    /**
     * Hierarchy separator.
     * <p>
     * Note: This attribute is used to split usable field identifier. If it is
     * modified, you must update the regular expression for all methods using
     * this attribute to split identifier.
     */
    public static final String HIERARCHY_SEPARATOR = "|";

    /**
     * Creates the identifier of an Usable field data.
     * <p>
     * Examples:<br />
     * <code>
     *  ParentId1|ParentId2|ParentId3|FieldLabelKey
     * </code>
     * <p>
     * If no parents:<br />
     * <code>
     *  FieldLabelKey
     * </code>
     * 
     * @param pParents
     *            List of parent's.
     * @param pFieldLabelKey
     *            Label key of the field
     * @return The identifier of an usable field data.
     */
    public static String createUsableFieldDataId(
            final List<FilterFieldsContainerInfo> pParents,
            final String pFieldLabelKey) {
        StringBuilder lFieldName = new StringBuilder();
        for (FilterFieldsContainerInfo lParent : pParents) {
            lFieldName.append(lParent.getId()).append(HIERARCHY_SEPARATOR);
        }
        lFieldName.append(pFieldLabelKey);

        return lFieldName.toString();
    }

    /**
     * Creates the identifier of an Usable field data.
     * <p>
     * The field have no parents.
     * 
     * @see SearchUtil#createUsableFieldDataId(List, String)
     * @param pFieldLabelKey
     *            Field label key
     * @return The identifier of an usable field data.
     */
    public static String createUsableFieldDataId(final String pFieldLabelKey) {
        List<FilterFieldsContainerInfo> lEmptyList = Collections.emptyList();
        return createUsableFieldDataId(lEmptyList, pFieldLabelKey);
    }

    /**
     * Get the direct parent identifier from parent's hierarchy.
     * <p>
     * The direct parent is the last one.
     * 
     * @param pVirtualFieldDataHierarchy
     *            Ancestors hierarchy.
     * @return Identifier of the last element. Empty string if the Collection is
     *         empty.
     */
    public static String extractFieldsContainerId(
            final Collection<FieldsContainerId> pVirtualFieldDataHierarchy) {
        String lFieldsContainerId = StringUtils.EMPTY;
        if (!pVirtualFieldDataHierarchy.isEmpty()) {
            lFieldsContainerId =
                    new ArrayList<FieldsContainerId>(pVirtualFieldDataHierarchy).get(
                            pVirtualFieldDataHierarchy.size() - 1).getIdentificator();
        }

        return lFieldsContainerId;
    }

    /**
     * Get the direct parent identifier from parent's hierarchy.
     * <p>
     * The direct parent is the last one.
     * 
     * @param pVirtualFieldDataHierarchy
     *            Ancestors hierarchy.
     * @return Identifier of the last element. Empty string if the Collection is
     *         empty.
     */
    public static String extractFieldsContainerId(
            final List<FilterFieldsContainerInfo> pVirtualFieldDataHierarchy) {
        String lFieldsContainerId = StringUtils.EMPTY;
        if (!pVirtualFieldDataHierarchy.isEmpty()) {
            lFieldsContainerId =
                    pVirtualFieldDataHierarchy.get(
                            pVirtualFieldDataHierarchy.size() - 1).getId();
        }
        return lFieldsContainerId;
    }

    /**
     * Get the label key of the field from this usable field identifier
     * 
     * @param pUsableFieldId
     *            Identifier of the usable field containing the label key.
     * @return The extracted label key.
     */
    public static String extractFieldLabelKey(String pUsableFieldId) {
        String[] lSplitedId = pUsableFieldId.split("\\" + HIERARCHY_SEPARATOR);
        return lSplitedId[lSplitedId.length - 1];
    }

    /**
     * Creates the ancestors hierarchy.
     * <p>
     * Includes the direct parent to ancestors list.<br />
     * The direct parent is the latest element of the list.
     * 
     * @param pParent
     *            Direct parent
     * @param pFilterFieldsContainerInfo
     *            Ancestors info.
     * @return Ancestor's info list. (include the direct parent)
     */
    public static List<FilterFieldsContainerInfo> createFieldsContainerHierarchy(
            final List<FilterFieldsContainerInfo> pParent,
            final FilterFieldsContainerInfo pFilterFieldsContainerInfo) {
        List<FilterFieldsContainerInfo> lParents =
                new ArrayList<FilterFieldsContainerInfo>(pParent.size() + 1);
        lParents.addAll(pParent);
        lParents.add(pFilterFieldsContainerInfo);
        return lParents;
    }

    /**
     * Creates the ancestors hierarchy.
     * <p>
     * Creates the FieldsContainerId entity object.
     * 
     * @param pFieldsContainerHierarchy
     *            Info of fields container.
     * @return List of FieldsContainerId entity object.
     */
    public static Collection<FieldsContainerId> createFieldsContainerHierarchy(
            final List<FilterFieldsContainerInfo> pFieldsContainerHierarchy) {
        Collection<FieldsContainerId> lFieldscontainerIds =
                new ArrayList<FieldsContainerId>();
        for (FilterFieldsContainerInfo lFieldsContainerInfo : pFieldsContainerHierarchy) {
            FieldsContainerId lFieldsContainerIdEntity =
                    FieldsContainerId.newInstance();
            lFieldsContainerIdEntity.setIdentificator(lFieldsContainerInfo.getId());
            lFieldscontainerIds.add(lFieldsContainerIdEntity);
        }
        return lFieldscontainerIds;
    }

    /**
     * Add ancestor hierarchy (as FieldsContainerId entity objects)to Filter
     * field
     * 
     * @param pFieldsContainerHierarchy
     *            Id of fields container.
     * @param pFilterField
     *            the filter field
     */
    public static void addFieldsContainerHierarchyToFilterField(
            final List<FilterFieldsContainerInfo> pFieldsContainerHierarchy,
            FilterField pFilterField) {
        for (FilterFieldsContainerInfo lFieldsContainerInfo : pFieldsContainerHierarchy) {
            FieldsContainerId lFieldsContainerIdEntity =
                    FieldsContainerId.newInstance();
            lFieldsContainerIdEntity.setIdentificator(lFieldsContainerInfo.getId());
            pFilterField.addToFieldsContainerIdList(lFieldsContainerIdEntity);
        }
    }

    /**
     * Creates the table of possible values. The values are the the value of the
     * categories. The first value is NOT_SPECIFIED
     * {@link UsableFieldsManager#NOT_SPECIFIED}
     * 
     * @param pCategoryValues
     *            Categories containing the values.
     * @return Values of the categorieS.
     */
    public static List<String> createPossibleValues(
            Collection<CategoryValue> pCategoryValues) {
        List<String> lPossibleValues =
                new ArrayList<String>(pCategoryValues.size() + 1);
        lPossibleValues.add(UsableFieldsManager.NOT_SPECIFIED);
        for (CategoryValue lCategoryValue : pCategoryValues) {
            lPossibleValues.add(lCategoryValue.getValue());
        }
        return lPossibleValues;
    }

    /**
     * Get the fields (and sub fields of multiple fields) of the cacheable
     * fields container ordered by display group.<br />
     * The display group definition defines the fields order.<br />
     * <p>
     * If the fields container has no groups, gets directly all fields. If
     * cannot retrieve a field, do nothing.
     * <p>
     * <b>Note</b>: Add the fields of the reference if its a sheet type.<br />
     * 
     * @param pCacheableFieldsContainer
     *            Cacheable fields container containing the fields
     * @return Fields ordered by their display group.
     */
    public static List<Field> getFields(
            final CacheableFieldsContainer pCacheableFieldsContainer) {
        List<Field> lFields = new ArrayList<Field>();
        if (CacheableSheetType.class.isInstance(pCacheableFieldsContainer)) {
            CacheableSheetType lCacheableSheetType =
                    (CacheableSheetType) pCacheableFieldsContainer;
            lFields.add(lCacheableSheetType.getReferenceField());
            lFields.addAll(lCacheableSheetType.getReferenceField().getFields());
        }

        if (pCacheableFieldsContainer.getDisplayGroups().isEmpty()) {
            lFields.addAll(pCacheableFieldsContainer.getAllFields());
        }
        else {
            for (DisplayGroup lDisplayGroup : pCacheableFieldsContainer.getDisplayGroups()) {
                for (FieldRef lFieldRef : lDisplayGroup.getFields()) {
                    Field lField =
                            pCacheableFieldsContainer.getFieldFromLabel(lFieldRef.getName());
                    if (lField != null) {
                        lFields.add(lField);
                        if (MultipleField.class.isInstance(lField)) {
                            MultipleField lMultipleField =
                                    (MultipleField) lField;
                            lFields.addAll(lMultipleField.getFields());
                        }
                    }
                }
            }
        }
        return lFields;
    }

    /**
     * Convert the value to a ScalarValueData. The value can be
     * <ul>
     * <li>String
     * <li>Double
     * <li>Boolean
     * <li>Integer
     * <li>Date
     * </ul>
     * 
     * @param pValue
     *            Value to transform or null.
     * @return The scalar value or null if the value is null.
     */
    public static ScalarValueData getScalarValue(Object pValue) {
        if (pValue == null) {
            return null;
        }

        if (pValue instanceof String) {
            return new StringValueData((String) pValue);
        }
        else if (pValue instanceof Double) {
            return new RealValueData((Double) pValue);
        }
        else if (pValue instanceof Boolean) {
            return new BooleanValueData((Boolean) pValue);
        }
        else if (pValue instanceof Integer) {
            return new IntegerValueData((Integer) pValue);
        }
        else if (pValue instanceof Date) {
            return new DateValueData((Date) pValue);
        }
        else {
            throw new GDMException("The scalarValueData class '"
                    + pValue.getClass() + "' is not valid. Valid class are '"
                    + String.class + "', '" + Double.class + "', '"
                    + Boolean.class + "'; '" + Integer.class + "' and '"
                    + Date.class + "'");
        }
    }

    /**
     * Gets only the fields container hierarchy, excludes the fields name.<br />
     * eg:<br />
     * <code>id1|id2|field1</code>&nbsp;->&nbsp;<code>id1|id2</code>
     * 
     * @param pUsableFieldId
     *            Identifier of usable field to process.
     * @return List of identifier of the fields container.
     */
    public static List<String> getFieldsContainerHierarchy(
            String[] pUsableFieldId) {
        List<String> lColumnsId = Arrays.asList(pUsableFieldId);
        return lColumnsId.subList(0, lColumnsId.size() - 1);
    }

    /**
     * Get the field name with the container names hierarchy<br />
     * eg:<br />
     * <code>name1|name2|field1</code>&nbsp;->&nbsp;<code>name1|name2</code>
     * 
     * @param pUsableFieldData
     *            The usable field data
     * @return The field name with hierarchy
     */
    public static String getFieldName(UsableFieldData pUsableFieldData) {
        StringBuilder lFieldName = new StringBuilder();

        if (CollectionUtils.isNotEmpty(pUsableFieldData.getFieldsContainerHierarchy())) {
            for (FilterFieldsContainerInfo lParent : pUsableFieldData.getFieldsContainerHierarchy()) {
                lFieldName.append(lParent.getLabelKey());
                lFieldName.append(HIERARCHY_SEPARATOR);
            }
        }
        lFieldName.append(pUsableFieldData.getFieldName());

        return lFieldName.toString();
    }

    /**
     * Transform the DEF_ASC order to ASC order and the DEF_DESC order to DESC
     * order.
     * 
     * @param pOrder
     *            the order to be modified
     * @return ASC or DESC.
     */
    public static String transformOrder(final String pOrder) {
        final String lOperator;
        if (Operators.DEF_ASC.equalsIgnoreCase(pOrder)) {
            lOperator = Operators.ASC;
        }
        else if (Operators.DEF_DESC.equalsIgnoreCase(pOrder)) {
            lOperator = Operators.DESC;
        }
        else {
            throw new IllegalArgumentException(pOrder);
        }
//        if (LOGGER.isInfoEnabled()) {
//            LOGGER.info("Order operator '" + pOrder
//                    + "' is not available, it has been changed into '"
//                    + lOperator + "' operator");
//        }
        return lOperator;
    }

    /**
     * Get the sort operator that have been compatible with the specified usable
     * field (or virtual field).
     * 
     * @param pUsableFieldData
     *            Usable field
     * @return Compatible sort operators.
     */
    public static Collection<String> getSort(
            final UsableFieldData pUsableFieldData) {
        if (pUsableFieldData instanceof VirtualFieldData) {
            return Operators.getSort(((VirtualFieldData) pUsableFieldData).getVirtualFieldType());
        }
        else {
            return Operators.getSort(pUsableFieldData.getFieldType());
        }
    }
}
