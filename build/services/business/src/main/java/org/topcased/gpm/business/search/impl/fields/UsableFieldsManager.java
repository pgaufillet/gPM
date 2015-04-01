/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.search.impl.fields;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
//import org.apache.log4j.Logger;
import org.topcased.gpm.business.ServiceImplBase;
import org.topcased.gpm.business.authorization.impl.filter.FilterAccessControl;
import org.topcased.gpm.business.authorization.impl.filter.FilterAccessDefinition;
import org.topcased.gpm.business.authorization.impl.filter.FilterAccessDefinitionKey;
import org.topcased.gpm.business.cache.ICacheManager;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.exception.InvalidIdentifierException;
import org.topcased.gpm.business.exception.MethodNotImplementedException;
import org.topcased.gpm.business.fields.FieldType;
import org.topcased.gpm.business.fields.impl.CacheableFieldsContainer;
import org.topcased.gpm.business.fields.impl.FieldsPredicateFactory;
import org.topcased.gpm.business.fields.impl.IFieldsPredicate;
import org.topcased.gpm.business.link.impl.CacheableLinkType;
import org.topcased.gpm.business.link.impl.LinkDirection;
import org.topcased.gpm.business.product.impl.CacheableProductType;
import org.topcased.gpm.business.search.criterias.CriteriaData;
import org.topcased.gpm.business.search.criterias.CriteriaFieldData;
import org.topcased.gpm.business.search.criterias.OperationData;
import org.topcased.gpm.business.search.criterias.impl.FilterFieldsContainerInfo;
import org.topcased.gpm.business.search.criterias.impl.VirtualFieldData;
import org.topcased.gpm.business.search.impl.SearchUtils;
import org.topcased.gpm.business.search.result.sorter.SortingFieldData;
import org.topcased.gpm.business.search.service.ExecutableFilterData;
import org.topcased.gpm.business.search.service.SearchService;
import org.topcased.gpm.business.search.service.UsableFieldData;
import org.topcased.gpm.business.serialization.data.CategoryValue;
import org.topcased.gpm.business.serialization.data.ChoiceField;
import org.topcased.gpm.business.serialization.data.Field;
import org.topcased.gpm.business.serialization.data.LinkType;
import org.topcased.gpm.business.serialization.data.ProductType;
import org.topcased.gpm.business.serialization.data.SheetType;
import org.topcased.gpm.business.serialization.data.SimpleField;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.business.values.field.virtual.VirtualFieldType;
import org.topcased.gpm.common.fields.FieldsPredicateCondition;
import org.topcased.gpm.domain.link.LinkNavigation;
import org.topcased.gpm.util.bean.CacheProperties;
import org.topcased.gpm.util.bean.GpmPair;
import org.topcased.gpm.util.lang.CopyUtils;

/**
 * UsableFields manager.
 * <p>
 * Manage the usable fields:
 * <ul>
 * <li>create a usable fields from criterion label key</li>
 * </ul>
 * <p>
 * This manager cache the usable fields per fields container.<br />
 * The virtual fields have been the same for all fields container, then they
 * have been create by the manager.<br />
 * To build usable fields on several level, the manager needs to know the
 * relationship between linked fields containers.<br />
 * The possible values for some usable fields such as choice fields and some
 * virtual fields (PRODUCT_NAME, PRODUCT_HIERARCHY, SHEET_TYPE, SHEET_STATE) are
 * not filtered. The caller must clean values according to role token for
 * example. Pointer fields can not be use in filter.
 * 
 * @author mkargbo
 */
public class UsableFieldsManager extends ServiceImplBase implements
        ICacheManager<UsableFieldCacheKey, UsableFieldData> {

    /** LINK_VIRTUAL_FIELDS_NUMBER */
    private static final int LINK_VIRTUAL_FIELDS_NUMBER = 4;

    /** LINKED_SHEET_VIRTUAL_FIELDS_NUMBER */
    private static final int LINKED_SHEET_VIRTUAL_FIELDS_NUMBER = 4;

    /** SHEET_VIRTUAL_FIELDS_NUMBER */
    private static final int SHEET_VIRTUAL_FIELDS_NUMBER = 5;

//    private static final Logger LOGGER =
//            Logger.getLogger(UsableFieldsManager.class);

    /** The Constant PRODUCT_FIELD_PREFIX. */
    final public static String PRODUCT_FIELD_PREFIX = "$PRODUCT_FIELD_PREFIX";

    /** Default field hierarchy level is 1 (and not 0) */
    public static final int DEFAULT_HIERARCHY_LEVEL = 0;

    /** The name of the virtual fields used by sheets. */
    public static final Collection<String> SHEET_VIRTUAL_FIELDS;

    private static final Collection<String> LINKED_SHEET_VIRTUAL_FIELDS;

    /** The name of the virtual fields used by links. */
    public static final Collection<String> LINK_VIRTUAL_FIELDS;

    /** Number of virtual fields */
    private static final int VIRTUAL_FIELD_NUMBER = 10;

    /** The Constant NOT_SPECIFIED. */
    public static final String NOT_SPECIFIED = "$NOT_SPECIFIED";

    /** The Constant CURRENT_PRODUCT. */
    public static final String CURRENT_PRODUCT = "$CURRENT_PRODUCT";

    /** The Constant CURRENT_USER_LOGIN. */
    public static final String CURRENT_USER_LOGIN = "$CURRENT_USER_LOGIN";

    /** The Constant CURRENT_USER_NAME. */
    public static final String CURRENT_USER_NAME = "$CURRENT_USER_NAME";

    /** This month */
    public static final String CURRENT_MONTH =
            "filter.edition.criteria.period.thisMonth";

    /** The previous month */
    public static final String PREVIOUS_MONTH =
            "filter.edition.criteria.period.lastMonth";

    /** This week */
    public static final String CURRENT_WEEK =
            "filter.edition.criteria.period.thisWeek";

    /** The previous week */
    public static final String PREVIOUS_WEEK =
            "filter.edition.criteria.period.lastWeek";

    /** Predicate for pointer field */
    private static final IFieldsPredicate POINTER_FIELD_PREDICATE =
            FieldsPredicateFactory.getInstance(FieldsPredicateCondition.POINTER_FIELD);

    /** Virtual field data map per virtual field label key. */
    private static final Map<String, VirtualFieldData> VIRTUAL_FIELD_CACHE;

    static {
        Map<String, VirtualFieldData> lVirtualFieldCache =
                new HashMap<String, VirtualFieldData>(VIRTUAL_FIELD_NUMBER);
        lVirtualFieldCache.put(VirtualFieldType.$PRODUCT_NAME.name(),
                VirtualFieldData.PRODUCT_NAME_VIRTUAL_FIELD);
        lVirtualFieldCache.put(VirtualFieldType.$PRODUCT_DESCRIPTION.name(),
                VirtualFieldData.PRODUCT_DESCRIPTION_VIRTUAL_FIELD);
        lVirtualFieldCache.put(VirtualFieldType.$PRODUCT_HIERARCHY.name(),
                VirtualFieldData.PRODUCT_HIERARCHY_VIRTUAL_FIELD);
        lVirtualFieldCache.put(VirtualFieldType.$SHEET_REFERENCE.name(),
                VirtualFieldData.SHEET_REFERENCE_VIRTUAL_FIELD);
        lVirtualFieldCache.put(VirtualFieldType.$SHEET_STATE.name(),
                VirtualFieldData.SHEET_STATE_VIRTUAL_FIELD);
        lVirtualFieldCache.put(VirtualFieldType.$SHEET_TYPE.name(),
                VirtualFieldData.SHEET_TYPE_VIRTUAL_FIELD);
        lVirtualFieldCache.put(VirtualFieldType.$ORIGIN_SHEET_REF.name(),
                VirtualFieldData.ORIGIN_SHEET_REFERENCE_VIRTUAL_FIELD);
        lVirtualFieldCache.put(VirtualFieldType.$ORIGIN_PRODUCT.name(),
                VirtualFieldData.ORIGIN_PRODUCT_NAME_VIRTUAL_FIELD);
        lVirtualFieldCache.put(VirtualFieldType.$DEST_SHEET_REF.name(),
                VirtualFieldData.DEST_SHEET_REFERENCE_VIRTUAL_FIELD);
        lVirtualFieldCache.put(VirtualFieldType.$DEST_PRODUCT.name(),
                VirtualFieldData.DEST_PRODUCT_NAME_VIRTUAL_FIELD);
        VIRTUAL_FIELD_CACHE = CopyUtils.getImmutableCopy(lVirtualFieldCache);

        SHEET_VIRTUAL_FIELDS =
                new ArrayList<String>(SHEET_VIRTUAL_FIELDS_NUMBER);
        SHEET_VIRTUAL_FIELDS.add(VirtualFieldType.$SHEET_STATE.name());
        SHEET_VIRTUAL_FIELDS.add(VirtualFieldType.$SHEET_TYPE.name());
        SHEET_VIRTUAL_FIELDS.add(VirtualFieldType.$SHEET_REFERENCE.name());
        SHEET_VIRTUAL_FIELDS.add(VirtualFieldType.$PRODUCT_NAME.name());
        SHEET_VIRTUAL_FIELDS.add(VirtualFieldType.$PRODUCT_HIERARCHY.name());

        LINKED_SHEET_VIRTUAL_FIELDS =
                new ArrayList<String>(LINKED_SHEET_VIRTUAL_FIELDS_NUMBER);
        LINKED_SHEET_VIRTUAL_FIELDS.add(VirtualFieldType.$SHEET_STATE.name());
        LINKED_SHEET_VIRTUAL_FIELDS.add(VirtualFieldType.$SHEET_TYPE.name());
        LINKED_SHEET_VIRTUAL_FIELDS.add(VirtualFieldType.$SHEET_REFERENCE.name());
        LINKED_SHEET_VIRTUAL_FIELDS.add(VirtualFieldType.$PRODUCT_NAME.name());

        LINK_VIRTUAL_FIELDS = new ArrayList<String>(LINK_VIRTUAL_FIELDS_NUMBER);
        LINK_VIRTUAL_FIELDS.add(VirtualFieldType.$ORIGIN_SHEET_REF.name());
        LINK_VIRTUAL_FIELDS.add(VirtualFieldType.$ORIGIN_PRODUCT.name());
        LINK_VIRTUAL_FIELDS.add(VirtualFieldType.$DEST_SHEET_REF.name());
        LINK_VIRTUAL_FIELDS.add(VirtualFieldType.$DEST_PRODUCT.name());
    }

    /**
     * Usable fields data map (level 0) per fields container identifier and per
     * field label key.
     */
    private Map<UsableFieldCacheKey, Map<String, UsableFieldData>> cache =
            new ConcurrentHashMap<UsableFieldCacheKey, Map<String, UsableFieldData>>();

    /**
     * Link relation per link type identifier. The pair is 'origin' and
     * 'destination'
     */
    private Map<String, GpmPair<String, String>> linkRelationMap =
            new HashMap<String, GpmPair<String, String>>();

    /**
     * Get usable fields.
     * 
     * @param pFields
     *            Fields to use for creation.
     * @param pCacheableFieldsContainer
     *            Fields container
     * @return Map of usable fields (key-> fields label key)
     */
    private Map<String, UsableFieldData> getFieldsMap(List<Field> pFields,
            CacheableFieldsContainer pCacheableFieldsContainer) {
        Map<String, UsableFieldData> lFieldMap =
                new LinkedHashMap<String, UsableFieldData>();
        for (Field lField : pFields) {
            if (POINTER_FIELD_PREDICATE.isEnabled(lField, false)) {
                UsableFieldData lUsableFieldData =
                        createUsableFieldData(lField, pCacheableFieldsContainer);
                lFieldMap.put(lField.getLabelKey(), lUsableFieldData);
            }
        }
        return CopyUtils.getImmutableCopy(lFieldMap);
    }

    /**
     * Create the usable field.
     * <p>
     * Set size for SIMPLE_STRINg_FIELD.
     * 
     * @param pField
     *            Field to use for creation
     * @param pCacheableFieldsContainer
     *            Fields container.
     * @return Usable field.
     */
    private UsableFieldData createUsableFieldData(Field pField,
            CacheableFieldsContainer pCacheableFieldsContainer) {

        FieldType lFieldType = FieldType.valueOf(pField);

        UsableFieldData lUsableFieldData = new UsableFieldData();
        lUsableFieldData.setFieldId(pField.getId());
        lUsableFieldData.setMultivalued(pField.getMultivalued());
        lUsableFieldData.setId(SearchUtils.createUsableFieldDataId(pField.getLabelKey()));
        lUsableFieldData.setFieldName(pField.getLabelKey());
        lUsableFieldData.setFieldType(lFieldType);
        lUsableFieldData.setPossibleValues(new ArrayList<String>(0));

        if (FieldType.SIMPLE_STRING_FIELD.equals(lFieldType)) {
            SimpleField lSimpleField = (SimpleField) pField;
            lUsableFieldData.setFieldSize(lSimpleField.getSizeAsInt());
        }
        else if (FieldType.CHOICE_FIELD.equals(lFieldType)) {
            ChoiceField lChoiceField = (ChoiceField) pField;
            lUsableFieldData.setCategoryName(lChoiceField.getCategoryName());
        }

        if (StringUtils.isNotBlank(pField.getMultipleField())) {
            lUsableFieldData =
                    setMultipleUsableFieldId(pCacheableFieldsContainer, pField,
                            lUsableFieldData);
        }
        return lUsableFieldData;
    }

    /**
     * Sets the identifier of the usable field data associated to a multiple
     * field.
     * <p>
     * The multiple field is the parent of the given field.
     * <p>
     * Use {@link SearchUtils#createUsableFieldDataId(String)}
     * 
     * @param pCacheableFieldsContainer
     *            Fields container of the field.
     * @param pField
     *            The sub-field.
     * @param pUsableFieldData
     *            UsableFieldData to update
     * @throws InvalidIdentifierException
     *             If the multiple has not founded.
     */
    private UsableFieldData setMultipleUsableFieldId(
            final CacheableFieldsContainer pCacheableFieldsContainer,
            final org.topcased.gpm.business.serialization.data.Field pField,
            UsableFieldData pUsableFieldData) throws InvalidIdentifierException {
        Field lMultipleField =
                pCacheableFieldsContainer.getFieldFromId(pField.getMultipleField());
        if (lMultipleField == null) {
            throw new InvalidIdentifierException(
                    "Impossible to get the multiple field '"
                            + pField.getMultipleField()
                            + "', invalid identifier");
        }
        pUsableFieldData.setMultipleFieldId(lMultipleField.getId());
        String lMultipleUsableFieldId =
                SearchUtils.createUsableFieldDataId(lMultipleField.getLabelKey());
        pUsableFieldData.setMultipleField(lMultipleUsableFieldId);
        pUsableFieldData.setMultipleFieldMultivalued(lMultipleField.getMultivalued());
        return pUsableFieldData;
    }

    /**
     * Handle usable fields retrieving for multi-level criterion.
     * 
     * @param pCriterionLabelKey
     *            Label key of the criterion
     * @param pFieldsContainerId
     *            Identifier of the fields container (of field)
     * @return Usable field.
     * @throws InvalidIdentifierException
     * @throws RuntimeException
     */
    private List<FilterFieldsContainerInfo> handleMultiLevelCriterion(
            UsableFieldCacheKey pElementKey) throws InvalidIdentifierException,
        RuntimeException {

        String lMainFieldsContainerId =
                pElementKey.getCriterionFieldsContainerId();
        List<FilterFieldsContainerInfo> lParents =
                new ArrayList<FilterFieldsContainerInfo>(
                        pElementKey.getHierarchy().size());
        for (String lHierarchyElement : pElementKey.getHierarchy()) {
            FilterFieldsContainerInfo lFilterFieldsContainerInfo =
                    getSearchService().createFilterFieldsContainerInfo(
                            lHierarchyElement, null);

            //Update link direction for links
            if (linkRelationMap.containsKey(lHierarchyElement)) {
                GpmPair<String, String> lRelation =
                        linkRelationMap.get(lHierarchyElement);
                lFilterFieldsContainerInfo.setLinkDirection(LinkDirection.getFromLinkDirection(
                        lMainFieldsContainerId, lRelation.getFirst(),
                        lRelation.getSecond()));
            }
            else {
                lMainFieldsContainerId = lHierarchyElement;
            }
            lParents =
                    SearchUtils.createFieldsContainerHierarchy(lParents,
                            lFilterFieldsContainerInfo);
        }
        return lParents;
    }

    private List<String> getPossibleValues(UsableFieldCacheKey pElementKey,
            UsableFieldData pUsableFieldData) {
        List<String> lPossibleValues = Collections.emptyList();
        if (pUsableFieldData instanceof VirtualFieldData) {
            VirtualFieldData lVirtualFieldData =
                    (VirtualFieldData) pUsableFieldData;
            switch (lVirtualFieldData.getVirtualFieldType()) {
                case $PRODUCT_NAME:
                    lPossibleValues = getProductNamesPossibleValues();
                    break;
                case $PRODUCT_HIERARCHY:
                    lPossibleValues = getProductNamesPossibleValues();
                    break;
                case $SHEET_STATE:
                    lPossibleValues =
                            getSheetStatePossibleValues(
                                    pElementKey.getProcessName(),
                                    pElementKey.getFieldsContainerId());
                    break;
                case $SHEET_TYPE:
                    lPossibleValues =
                            getSheetTypePossibleValues(pElementKey.getProcessName());
                    break;
                default: //do nothing
            }
        }
        else {
            if (FieldType.CHOICE_FIELD.equals(pUsableFieldData.getFieldType())) {
                lPossibleValues =
                        getChoiceFieldPossibleValues(
                                pElementKey.getProcessName(), pUsableFieldData);
            }
        }
        return lPossibleValues;
    }

    private List<String> getChoiceFieldPossibleValues(String pProcessName,
            UsableFieldData pUsableFieldData) {
        List<String> lCatNames =
                Collections.singletonList(pUsableFieldData.getCategoryName());

        Map<String, List<CategoryValue>> lValuesMap =
                getEnvService().getCategoryValues(pProcessName, lCatNames);
        List<CategoryValue> lValues =
                lValuesMap.get(pUsableFieldData.getCategoryName());

        return SearchUtils.createPossibleValues(lValues);
    }

    private List<String> getProductNamesPossibleValues() {
        //Constructs values
        List<String> lPossibleValues = new ArrayList<String>(2);
        lPossibleValues.add(CURRENT_PRODUCT);
        lPossibleValues.add(NOT_SPECIFIED);
        return lPossibleValues;
    }

    private List<String> getSheetStatePossibleValues(String pProcessName,
            String pSheetTypeId) {
        if (StringUtils.isEmpty(pSheetTypeId)) {
            List<String> lPossibleValues = new ArrayList<String>();
            List<String> lSheetTypeIds =
                    getSheetTypeDao().getSheetTypesId(pProcessName);
            for (String lSheetTypeId : lSheetTypeIds) {
                lPossibleValues.addAll(doSheetStatePossibleValues(lSheetTypeId));
            }
            return lPossibleValues;
        }
        else {
            return doSheetStatePossibleValues(pSheetTypeId);
        }
    }

    /**
     * Get possible values for the $SHEET_STATE virtual fields.
     * <p>
     * Include NOT_SPECIFIED value in the result list.
     * 
     * @param pSheetTypeId
     *            Identifier of the sheet type
     * @return List of all states
     */
    private List<String> doSheetStatePossibleValues(String pSheetTypeId) {
        CacheableSheetType lCacheableSheetType =
                getSheetService().getCacheableSheetType(pSheetTypeId,
                        CacheProperties.IMMUTABLE);
        List<String> lStateNames =
                new ArrayList<String>(
                        lCacheableSheetType.getStateNames().size() + 1);
        lStateNames.add(NOT_SPECIFIED);
        lStateNames.addAll(lCacheableSheetType.getStateNames());
        return lStateNames;
    }

    private List<String> getSheetTypePossibleValues(String pProcessName) {
        List<String> lSheetTypeNames =
                getSheetTypeDao().getSheetTypeNames(pProcessName);
        return lSheetTypeNames;
    }

    private Map<String, UsableFieldData> getLinkFields(final String pRoleToken,
            final CacheableFieldsContainer pCacheableFieldsContainer,
            int pLevel, final List<FilterFieldsContainerInfo> pParents) {

        Map<String, UsableFieldData> lResult =
                new LinkedHashMap<String, UsableFieldData>();

        if (pLevel < getSearchService().getMaxFieldsDepth()) {
            pLevel = pLevel + 1;

            /* Ids of the possible sheet link types */
            List<String> lLinkTypeIds =
                    getLinkTypeDao().getLinkTypesId(
                            pCacheableFieldsContainer.getId(),
                            LinkNavigation.BIDIRECTIONAL_NAVIGATION);

            for (String lLinkTypeId : lLinkTypeIds) {
                //get link fields
                CacheableLinkType lCacheableLinkType =
                        getLinkService().getCacheableLinkType(lLinkTypeId,
                                CacheProperties.IMMUTABLE);

//                if (LOGGER.isDebugEnabled()) {
//                    LOGGER.debug("\t\t\t (Level " + pLevel + ")Link = "
//                            + lCacheableLinkType.getName());
//                }

                //Get filter access
                String lRoleName =
                        authorizationService.getRoleNameFromToken(pRoleToken);
                String lProcessName =
                        authorizationService.getProcessNameFromToken(pRoleToken);
                UsableFieldCacheKey lCacheKey =
                        new UsableFieldCacheKey(lRoleName, lProcessName,
                                lLinkTypeId, StringUtils.EMPTY);
                Map<String, UsableFieldData> lUsableFields =
                        new LinkedHashMap<String, UsableFieldData>();
                lUsableFields.putAll(doGetOrCreateElement(lCacheKey));
                if (MapUtils.isNotEmpty(lUsableFields)) {

                    //Link direction will be update later.
                    FilterFieldsContainerInfo lFilterFieldsContainerInfo =
                            getSearchService().createFilterFieldsContainerInfo(
                                    lCacheableLinkType.getId(), null);
                    lFilterFieldsContainerInfo.setLinkDirection(LinkDirection.getFromLinkDirection(
                            pCacheableFieldsContainer.getId(),
                            lCacheableLinkType.getOriginTypeId(),
                            lCacheableLinkType.getDestinationTypeId()));
                    List<FilterFieldsContainerInfo> lParents =
                            SearchUtils.createFieldsContainerHierarchy(
                                    pParents, lFilterFieldsContainerInfo);

                    for (UsableFieldData lUsableFieldData : lUsableFields.values()) {
                        UsableFieldData lUpdatedUsableFieldData =
                                updateUsableField(lCacheKey, lUsableFieldData,
                                        lParents);
                        lResult.put(lUpdatedUsableFieldData.getId(),
                                lUpdatedUsableFieldData);
                    }

                    //get linked sheet fields
                    String lTargetId;

                    if (lCacheableLinkType.getOriginTypeId().equals(
                            lCacheableLinkType.getDestinationTypeId())) {
                        lTargetId = lCacheableLinkType.getOriginTypeId();
                    }
                    else if (pCacheableFieldsContainer.getId().equals(
                            lCacheableLinkType.getOriginTypeId())) {
                        lTargetId = lCacheableLinkType.getDestinationTypeId();
                    }
                    else {
                        lTargetId = lCacheableLinkType.getOriginTypeId();
                    }

                    //Add linked sheet fields
                    CacheableSheetType lCacheableSheetType =
                            getSheetService().getCacheableSheetType(lTargetId,
                                    CacheProperties.IMMUTABLE);

                    FilterFieldsContainerInfo lFilterFieldsContainerInfo2 =
                            getSearchService().createFilterFieldsContainerInfo(
                                    lTargetId, null);
                    lParents =
                            SearchUtils.createFieldsContainerHierarchy(
                                    lParents, lFilterFieldsContainerInfo2);
                    lResult.putAll(getLinkedSheetFields(pRoleToken,
                            lCacheableSheetType, lParents));

                    if (pLevel < getSearchService().getMaxFieldsDepth()) {
                        lResult.putAll(getLinkFields(pRoleToken,
                                lCacheableSheetType, pLevel, lParents));
                    }
                }
            }
        }
        return lResult;
    }

    /**
     * Gets the linked sheet's usable fields.
     * <p>
     * Linked sheet's usable fields and virtual fields.
     * </p>
     * <p>
     * Linked sheet's virtual fields are 'type', 'state', 'reference' and
     * 'product name'
     * </p>
     * 
     * @param pProcessName
     *            Process name
     * @param pCacheableSheetType
     *            Sheet type
     * @param pParents
     *            Hierarchy
     * @return Linked sheet's usable fields.
     */
    private Map<String, UsableFieldData> getLinkedSheetFields(
            final String pRoleToken,
            final CacheableSheetType pCacheableSheetType,
            final List<FilterFieldsContainerInfo> pParents) {

        String lRoleName =
                authorizationService.getRoleNameFromToken(pRoleToken);
        String lProcessName =
                authorizationService.getProcessNameFromToken(pRoleToken);
        UsableFieldCacheKey lCacheKey =
                new UsableFieldCacheKey(lRoleName, lProcessName,
                        pCacheableSheetType.getId(), StringUtils.EMPTY);
        Map<String, UsableFieldData> lResult = Collections.emptyMap();
        Map<String, UsableFieldData> lUsableFields =
                new LinkedHashMap<String, UsableFieldData>();
        lUsableFields.putAll(doGetOrCreateElement(lCacheKey));

        if (MapUtils.isNotEmpty(lUsableFields)) {

            //Add virtual fields
            for (String lLabelkey : LINKED_SHEET_VIRTUAL_FIELDS) {
                lCacheKey.setCriterionLabelKey(lLabelkey);
                lCacheKey.setFieldLabelKey(lLabelkey);
                lUsableFields.put(lLabelkey, doGetElement(lCacheKey));
            }
            lResult =
                    new LinkedHashMap<String, UsableFieldData>(
                            lUsableFields.size());
            for (UsableFieldData lUsableFieldData : lUsableFields.values()) {
                UsableFieldData lUpdatedUsableFieldData =
                        updateUsableField(lCacheKey, lUsableFieldData, pParents);
                lResult.put(lUpdatedUsableFieldData.getId(),
                        lUpdatedUsableFieldData);
            }
        }
        return lResult;
    }

    /**
     * Update the usable field identifiers.
     * <p>
     * Identifiers of a usable fields are:
     * <ul>
     * <li>Usable field identifier attribute</li>
     * <li>Multiple field usable field identifier</li>
     * </ul>
     * </p>
     * 
     * @param pUsableFieldData
     *            Usable field to update
     * @param pParents
     *            Hierarchy of this usable field
     * @return Updated usable field.
     */
    private UsableFieldData updateUsableFieldDataId(
            final UsableFieldData pUsableFieldData,
            final List<FilterFieldsContainerInfo> pParents) {
        UsableFieldData lNewUsableFieldData = pUsableFieldData;
        lNewUsableFieldData.setId(SearchUtils.createUsableFieldDataId(pParents,
                pUsableFieldData.getFieldName()));
        if (StringUtils.isNotBlank(pUsableFieldData.getMultipleField())) {
            String lMultipleUsableFieldId =
                    SearchUtils.createUsableFieldDataId(
                            pParents,
                            SearchUtils.extractFieldLabelKey(pUsableFieldData.getMultipleField()));
            lNewUsableFieldData.setMultipleField(lMultipleUsableFieldId);
        }
        return lNewUsableFieldData;
    }

    /**
     * Retrieve a usable fields for a specified fields container.
     * <p>
     * If not found in the cache also creates usable fields for the specified
     * fields container identifier (containing in the cache key).
     * 
     * @param pElementKey
     *            Cache key
     * @return Usable fields
     */
    private Map<String, UsableFieldData> doGetOrCreateElement(
            UsableFieldCacheKey pElementKey) {
        if (cache.containsKey(pElementKey)) {
            return cache.get(pElementKey);
        }
        else {
            if (UsableFieldCacheKey.DEFAULT_ROLE.equals(pElementKey.getRoleName())) {
                return constructDefaultMap(pElementKey);
            }
            else {
                FilterAccessDefinitionKey lFilterAccessCacheKey =
                        new FilterAccessDefinitionKey(
                                pElementKey.getProcessName(),
                                pElementKey.getRoleName(),
                                pElementKey.getFieldsContainerId());
                FilterAccessDefinition lFilterAccessDefinition =
                        filterAccessManager.getElement(lFilterAccessCacheKey);
                Map<String, UsableFieldData> lResult;
                FilterAccessControl lTypeAccessControl =
                        lFilterAccessDefinition.getTypeAccessControl();
                if (lTypeAccessControl == null
                        || lTypeAccessControl.getEditable()) {
                    UsableFieldCacheKey lDefaultCacheKey =
                            UsableFieldCacheKey.getDefaultCacheKey(pElementKey);
                    Map<String, UsableFieldData> lFieldMap =
                            doGetOrCreateElement(lDefaultCacheKey);

                    if (MapUtils.isNotEmpty(lFieldMap)) {
                        lResult =
                                new LinkedHashMap<String, UsableFieldData>(
                                        lFieldMap.size());
                        for (Entry<String, UsableFieldData> lEntry : lFieldMap.entrySet()) {
                            //Apply access.
                            String lFieldLabelKey =
                                    lEntry.getValue().getFieldName();
                            FilterAccessControl lFilterAccessControl =
                                    lFilterAccessDefinition.getFieldsAccessControl().get(
                                            lFieldLabelKey);
                            if (lFilterAccessControl == null
                                    || lFilterAccessControl.getEditable()) {
                                lResult.put(lEntry.getKey(), lEntry.getValue());
                            }
                        }
                        cache.put(pElementKey, lResult);
                    }
                    else {
                        lResult = Collections.emptyMap();
                    }
                }
                else {
                    lResult = Collections.emptyMap();
                }
                return lResult;
            }
        }
    }

    private Map<String, UsableFieldData> constructDefaultMap(
            UsableFieldCacheKey pElementKey) {
        CacheableFieldsContainer lFieldsContainer =
                getCachedFieldsContainer(pElementKey.getFieldsContainerId(),
                        CACHE_IMMUTABLE_OBJECT);
        List<Field> lFields = SearchUtils.getFields(lFieldsContainer);
        Map<String, UsableFieldData> lFieldMap =
                getFieldsMap(lFields, lFieldsContainer);

        if (MapUtils.isNotEmpty(lFieldMap)) {
            cache.put(pElementKey, lFieldMap);
            if (lFieldsContainer instanceof CacheableLinkType) {
                CacheableLinkType lCacheableLinkType =
                        (CacheableLinkType) lFieldsContainer;
                //Handle link relation
                GpmPair<String, String> lLinkRelation =
                        new GpmPair<String, String>(
                                lCacheableLinkType.getOriginTypeId(),
                                lCacheableLinkType.getDestinationTypeId());
                linkRelationMap.put(lCacheableLinkType.getId(), lLinkRelation);
            }
        }
        return lFieldMap;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.cache.ICacheManager#depreciateAll()
     */
    public void depreciateAll() {
        cache.clear();
        linkRelationMap.clear();
        //Virtual fields never remove from cache.
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.cache.ICacheManager#depreciateElement(org.topcased.gpm.business.cache.CacheKey)
     */
    public void depreciateElement(UsableFieldCacheKey pElementKey) {
        cache.remove(pElementKey);
    }

    /**
     * Get usable field corresponding to the criterion.
     * <p>
     * Fields container identifier can be empty, then the usable field has been
     * searched in all fields container.
     * </p>
     * <p>
     * Usable fields contains all possible values, caller must filter those
     * values
     * </p>
     * 
     * @param pElementKey
     *            Cache key
     * @return Usable field data {@inheritDoc}
     * @see org.topcased.gpm.business.cache.ICacheManager#getElement(org.topcased.gpm.business.cache.CacheKey)
     */
    public UsableFieldData getElement(UsableFieldCacheKey pElementKey) {
        UsableFieldData lUsableFieldData = doGetElement(pElementKey);
        List<FilterFieldsContainerInfo> lParents = Collections.emptyList();

        if (pElementKey.isMultiLevelCriterion()) {
            lParents = handleMultiLevelCriterion(pElementKey);
        }

        if (lUsableFieldData != null) {
            lUsableFieldData =
                    updateUsableField(pElementKey, lUsableFieldData, lParents);
        }
        return lUsableFieldData;
    }

    /**
     * Update the usable field.
     * <ul>
     * <li>The fields container hierarchy</li>
     * <li>The possible values</li>
     * </ul>
     * 
     * @see UsableFieldsManager#updateUsableFieldDataId(UsableFieldData, List)
     * @param pElementKey
     *            Cache key (use to get process and fields container identifier)
     * @param pUsableFieldData
     *            Immutable usable field to update.
     * @param pParents
     *            Hierarchy of this usable field.
     * @return Updated usable field. It's mutable.
     */
    private UsableFieldData updateUsableField(UsableFieldCacheKey pElementKey,
            UsableFieldData pUsableFieldData,
            List<FilterFieldsContainerInfo> pParents) {
        pUsableFieldData = CopyUtils.getMutableCopy(pUsableFieldData);
        pUsableFieldData.setFieldsContainerHierarchy(pParents);
        List<String> lPossibleValues =
                getPossibleValues(pElementKey, pUsableFieldData);
        pUsableFieldData.setPossibleValues(lPossibleValues);
        return updateUsableFieldDataId(pUsableFieldData, pParents);
    }

    /**
     * Get all usable fields for the specified fields container.
     * <p>
     * The depth is setting by {@link SearchService#getMaxFieldsDepth()}
     * </p>
     * <ul>
     * <li>Sheet:
     * <ol>
     * <li>sheet's fields</li>
     * <li>sheet's virtual fields (type, state, reference, product name and
     * hierarchy)</li>
     * <li>product's fields</li>
     * <li>link's fields</li>
     * <li>linked sheet's fields and virtual fields (type, state, reference and
     * product name)</li>
     * </ol>
     * </li>
     * <li>Product: add only product's fields</li>
     * <li>Link:
     * <ol>
     * <li>link's fields</li>
     * <li>link's virtual fields (sheet as origin reference and product name,
     * sheet as destination reference and product name)</li>
     * </ol>
     * </li>
     * </ul>
     * <p>
     * Usable fields contains all possible values, caller must filter those
     * values
     * </p>
     * 
     * @param pRoleToken
     *            Role token
     * @param pCacheableFieldsContainer
     *            Fields container to use to get it's usable fields.
     * @return All usable fields for the specified fields container.
     */
    @SuppressWarnings("unchecked")
    public Map<String, UsableFieldData> getElements(String pRoleToken,
            CacheableFieldsContainer pCacheableFieldsContainer) {
        Map<String, UsableFieldData> lResult =
                new LinkedHashMap<String, UsableFieldData>();
        Map<String, UsableFieldData> lUsableFields =
                new LinkedHashMap<String, UsableFieldData>();
        final String lRoleName;

        if (authorizationService.isGlobalAdminRole(pRoleToken)
        		|| pCacheableFieldsContainer instanceof CacheableProductType) {
            lRoleName = UsableFieldCacheKey.DEFAULT_ROLE;
        }
        else {
            lRoleName = authorizationService.getRoleNameFromToken(pRoleToken);
        }

        String lProcessName =
                authorizationService.getProcessNameFromToken(pRoleToken);
        UsableFieldCacheKey lCacheKey =
                new UsableFieldCacheKey(lRoleName, lProcessName,
                        pCacheableFieldsContainer.getId(), StringUtils.EMPTY);

        lUsableFields.putAll(doGetOrCreateElement(lCacheKey));

        if (MapUtils.isNotEmpty(lUsableFields)) {
            //Update values
            for (Map.Entry<String, UsableFieldData> lEntry : lUsableFields.entrySet()) {
                UsableFieldData lUsableFieldData =
                        updateUsableField(lCacheKey, lEntry.getValue(),
                                Collections.EMPTY_LIST);

                lResult.put(lEntry.getKey(), lUsableFieldData);
            }
        }

        //Add virtual fields
        if (pCacheableFieldsContainer instanceof CacheableSheetType) {
            List<FilterFieldsContainerInfo> lParents =
                    new ArrayList<FilterFieldsContainerInfo>(0);
            for (String lLabelkey : SHEET_VIRTUAL_FIELDS) {
                lCacheKey.setCriterionLabelKey(lLabelkey);
                lCacheKey.setFieldLabelKey(lLabelkey);
                UsableFieldData lUsableFieldData = doGetElement(lCacheKey);
                lUsableFieldData =
                        updateUsableField(lCacheKey, lUsableFieldData, lParents);
                lResult.put(lUsableFieldData.getId(), lUsableFieldData);
            }
            //product fields
            String lId =
                    getBusinessProcess(lProcessName).getProductType().getId();
            List<FilterFieldsContainerInfo> lParent =
                    new ArrayList<FilterFieldsContainerInfo>(1);
            lParent.add(getSearchService().createFilterFieldsContainerInfo(lId,
                    null));
            Map<String, UsableFieldData> lFieldMap =
                    new LinkedHashMap<String, UsableFieldData>();
            UsableFieldCacheKey lCacheKeyProduct =
                    new UsableFieldCacheKey(lRoleName, lProcessName, lId,
                            StringUtils.EMPTY);

            lUsableFields = doGetOrCreateElement(lCacheKeyProduct);
            if (MapUtils.isNotEmpty(lUsableFields)) {

                for (UsableFieldData lUsableFieldData : lUsableFields.values()) {
                    UsableFieldData lUpdatedUsableFieldData =
                            updateUsableField(lCacheKeyProduct,
                                    lUsableFieldData, lParent);
                    lFieldMap.put(lUpdatedUsableFieldData.getId(),
                            lUpdatedUsableFieldData);
                }
                lResult.putAll(lFieldMap);
            }

            //Link and linked sheet fields
            lResult.putAll(getLinkFields(pRoleToken, pCacheableFieldsContainer,
                    DEFAULT_HIERARCHY_LEVEL,
                    new ArrayList<FilterFieldsContainerInfo>()));
        }
        else if (pCacheableFieldsContainer instanceof CacheableLinkType) {
            UsableFieldCacheKey lCacheKeyLink =
                    new UsableFieldCacheKey(lRoleName, lProcessName,
                            pCacheableFieldsContainer.getId(),
                            StringUtils.EMPTY);
            for (String lLabelkey : LINK_VIRTUAL_FIELDS) {
                lCacheKeyLink.setCriterionLabelKey(lLabelkey);
                lCacheKeyLink.setFieldLabelKey(lLabelkey);
                UsableFieldData lUsableFieldData = doGetElement(lCacheKeyLink);
                lUsableFieldData =
                        updateUsableField(lCacheKeyLink, lUsableFieldData,
                                Collections.EMPTY_LIST);
                lResult.put(lUsableFieldData.getId(), lUsableFieldData);
            }
        }
        else if (pCacheableFieldsContainer instanceof CacheableProductType) {
            UsableFieldCacheKey lCacheKeyProduct =
                    new UsableFieldCacheKey(lRoleName, lProcessName,
                            pCacheableFieldsContainer.getId(),
                            StringUtils.EMPTY);
            lCacheKeyProduct.setCriterionLabelKey(VirtualFieldType.$PRODUCT_DESCRIPTION.name());
            lCacheKeyProduct.setFieldLabelKey(VirtualFieldType.$PRODUCT_DESCRIPTION.name());
            UsableFieldData lUsableFieldData = doGetElement(lCacheKeyProduct);
            lUsableFieldData =
                    updateUsableField(lCacheKeyProduct, lUsableFieldData,
                            Collections.EMPTY_LIST);
            lResult.put(lUsableFieldData.getId(), lUsableFieldData);
        }

        return lResult;
    }

    /**
     * Gets a usable field or virtual field.
     * <p>
     * Get the usable field (and virtual fields) from the cache or create it.
     * </p>
     * <p>
     * If the cache key doesn't contains a fields container identifier, this
     * method looks for the usable field in the current Cache.
     * </p>
     * 
     * @param pElementKey
     *            Cache key
     * @return Usable field if found, null otherwise (maybe the cache does not
     *         contains all fields container and the usable field cannot be
     *         found in the 'no fields container identifier' case)
     */
    private UsableFieldData doGetElement(UsableFieldCacheKey pElementKey) {
        UsableFieldData lUsableFieldData = null;
        if (pElementKey.isVirtualField()) {
            lUsableFieldData =
                    VIRTUAL_FIELD_CACHE.get(pElementKey.getFieldLabelKey());
        }
        else if (StringUtils.isNotBlank(pElementKey.getFieldsContainerId())) {
            lUsableFieldData =
                    doGetOrCreateElement(pElementKey).get(
                            pElementKey.getFieldLabelKey());
        }
        else {//Looking on all fields container.
            //Complete cache with all fields container
            fillUsableFields();
            boolean lFound = false;
            Iterator<Map.Entry<UsableFieldCacheKey, Map<String, UsableFieldData>>> lIterator =
                    cache.entrySet().iterator();
            while (lIterator.hasNext() && !lFound) {
                Map.Entry<UsableFieldCacheKey, Map<String, UsableFieldData>> lEntry =
                        lIterator.next();
                if (lEntry.getValue().containsKey(
                        pElementKey.getFieldLabelKey())) {
                    lFound = true;
                    lUsableFieldData =
                            lEntry.getValue().get(
                                    pElementKey.getFieldLabelKey());
                }
            }
        }
        return lUsableFieldData;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.cache.ICacheManager#getElementVersion(org.topcased.gpm.business.cache.CacheKey)
     */
    public long getElementVersion(UsableFieldCacheKey pElementKey) {
        throw new MethodNotImplementedException("getElementVersion");
    }

    private void fillUsableFields() {
        //Looking for all types
        Collection<CacheableFieldsContainer> lTypes =
                new HashSet<CacheableFieldsContainer>();
        //Product types
        lTypes.addAll(fieldsContainerServiceImpl.getFieldsContainer(ProductType.class));
        lTypes.addAll(fieldsContainerServiceImpl.getFieldsContainer(SheetType.class));
        lTypes.addAll(fieldsContainerServiceImpl.getFieldsContainer(LinkType.class));
        for (CacheableFieldsContainer lType : lTypes) {
            UsableFieldCacheKey lCacheKey =
                    new UsableFieldCacheKey(StringUtils.EMPTY, lType.getId(),
                            StringUtils.EMPTY);
            doGetOrCreateElement(lCacheKey);
        }
    }

    /**
     * Search fields used by the filter in : Result summary, Result sorter and
     * Criteria.
     * 
     * @param pFilter
     *            The filter.
     * @return The used fields sort by fields container.
     */
    public Map<UsableTypeData, Set<UsableFieldData>> getUsedFields(
            final ExecutableFilterData pFilter) {
        final Map<UsableTypeData, Set<UsableFieldData>> lUsedFieldByType =
                new HashMap<UsableTypeData, Set<UsableFieldData>>();
        final Set<UsableFieldData> lUsedFields = new HashSet<UsableFieldData>();
        final Set<UsableTypeData> lTopLevelTypes =
                new HashSet<UsableTypeData>();

        // Search used fields on result summary
        if (pFilter.getResultSummaryData() != null) {
            for (UsableFieldData lFieldData : pFilter.getResultSummaryData().getUsableFieldDatas()) {
                lUsedFields.add(lFieldData);
            }
        }
        // Search used fields on sorting criteria
        if (pFilter.getResultSortingData() != null
                && pFilter.getResultSortingData().getSortingFieldDatas() != null) {
            for (SortingFieldData lFieldData : pFilter.getResultSortingData().getSortingFieldDatas()) {
                lUsedFields.add(lFieldData.getUsableFieldData());
            }
        }
        // Search used fields on criteria
        if (pFilter.getFilterData().getCriteriaData() != null) {
            lUsedFields.addAll(getUsedFields(pFilter.getFilterData().getCriteriaData()));
        }

        // Initialize set for top level types used by the filter
        for (String lUsedTypeId : pFilter.getFilterData().getFieldsContainerIds()) {
            final UsableTypeData lUsedType =
                    new UsableTypeData(getCachedFieldsContainer(lUsedTypeId,
                            CacheProperties.IMMUTABLE.getCacheFlags()));

            lUsedFieldByType.put(lUsedType, new HashSet<UsableFieldData>());
            lTopLevelTypes.add(lUsedType);
        }

        // Sort used fields by type
        for (UsableFieldData lUsedField : lUsedFields) {
            final List<FilterFieldsContainerInfo> lFullHierarchy =
                    lUsedField.getFieldsContainerHierarchy();

            if (lFullHierarchy == null || lFullHierarchy.isEmpty()) {
                // Mono level
                for (UsableTypeData lTopLevelType : lTopLevelTypes) {
                    lUsedFieldByType.get(lTopLevelType).add(lUsedField);
                }
            }
            else {
                final List<FilterFieldsContainerInfo> lCurrentHierarchy =
                        new ArrayList<FilterFieldsContainerInfo>();
                // Multi level
                Set<UsableFieldData> lLastLevelTypeFields = null;

                // Search last level field on hierarchy
                for (FilterFieldsContainerInfo lContainer : lFullHierarchy) {
                    // The hierarchy to access on the current container
                    lCurrentHierarchy.add(lContainer);

                    // Create the associate type data to access on the map
                    final UsableTypeData lContainerType =
                            new UsableTypeData(
                                    new ArrayList<FilterFieldsContainerInfo>(
                                            lCurrentHierarchy));

                    lLastLevelTypeFields = lUsedFieldByType.get(lContainerType);
                    // If present on the hierarchy, type is used by the filter
                    if (lLastLevelTypeFields == null) {
                        lLastLevelTypeFields = new HashSet<UsableFieldData>();
                        lUsedFieldByType.put(lContainerType,
                                lLastLevelTypeFields);
                    }
                }
                // Add field on last level fields
                if (lLastLevelTypeFields != null) {
                    lLastLevelTypeFields.add(lUsedField);
                }
            }
        }

        return lUsedFieldByType;
    }

    /**
     * Search the label of the fields used by a filter's criteria.
     * 
     * @param pElement
     *            The filter element.
     * @return The label of the used fields.
     */
    private Set<UsableFieldData> getUsedFields(final CriteriaData pCriteria) {
        final Set<UsableFieldData> lResults;

        if (pCriteria instanceof CriteriaFieldData) {
            lResults =
                    Collections.singleton(((CriteriaFieldData) pCriteria).getUsableFieldData());
        }
        else if (pCriteria instanceof OperationData) {
            lResults = new HashSet<UsableFieldData>();
            for (CriteriaData lSubCriteria : ((OperationData) pCriteria).getCriteriaDatas()) {
                lResults.addAll(getUsedFields(lSubCriteria));
            }
        }
        else {
            throw new GDMException("Unknow CriteriaData implementation : "
                    + pCriteria.getClass().getName());
        }

        return lResults;
    }
}
