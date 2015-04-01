/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.server.filter.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.StringEscapeUtils;
import org.topcased.gpm.business.ServiceLocator;
import org.topcased.gpm.business.authorization.impl.filter.FilterAccessContraint;
import org.topcased.gpm.business.authorization.impl.filter.FilterExecutionReport;
import org.topcased.gpm.business.authorization.service.AuthorizationService;
import org.topcased.gpm.business.exception.InstantiateException;
import org.topcased.gpm.business.fields.FieldSummaryData;
import org.topcased.gpm.business.fields.FieldsContainerType;
import org.topcased.gpm.business.fields.SummaryData;
import org.topcased.gpm.business.fields.impl.CacheableFieldsContainer;
import org.topcased.gpm.business.fields.impl.CacheableValuesContainer;
import org.topcased.gpm.business.fieldscontainer.service.FieldsContainerService;
import org.topcased.gpm.business.link.impl.CacheableLinkType;
import org.topcased.gpm.business.link.impl.LinkDirection;
import org.topcased.gpm.business.product.impl.CacheableProduct;
import org.topcased.gpm.business.product.service.ProductSummaryData;
import org.topcased.gpm.business.scalar.BooleanValueData;
import org.topcased.gpm.business.scalar.DateValueData;
import org.topcased.gpm.business.scalar.IntegerValueData;
import org.topcased.gpm.business.scalar.RealValueData;
import org.topcased.gpm.business.scalar.ScalarValueData;
import org.topcased.gpm.business.scalar.StringValueData;
import org.topcased.gpm.business.search.criterias.CriteriaData;
import org.topcased.gpm.business.search.criterias.CriteriaFieldData;
import org.topcased.gpm.business.search.criterias.FilterData;
import org.topcased.gpm.business.search.criterias.FilterTypeData;
import org.topcased.gpm.business.search.criterias.OperationData;
import org.topcased.gpm.business.search.criterias.impl.FilterFieldsContainerInfo;
import org.topcased.gpm.business.search.criterias.impl.Operators;
import org.topcased.gpm.business.search.criterias.impl.VirtualFieldData;
import org.topcased.gpm.business.search.impl.SearchUtils;
import org.topcased.gpm.business.search.impl.fields.UsableFieldsManager;
import org.topcased.gpm.business.search.impl.fields.UsableTypeData;
import org.topcased.gpm.business.search.impl.query.FilterQueryConfigurator;
import org.topcased.gpm.business.search.impl.query.FilterResultIterator;
import org.topcased.gpm.business.search.result.sorter.ResultSortingData;
import org.topcased.gpm.business.search.result.sorter.SortingFieldData;
import org.topcased.gpm.business.search.result.summary.ResultSummaryData;
import org.topcased.gpm.business.search.service.ExecutableFilterData;
import org.topcased.gpm.business.search.service.FilterProductScope;
import org.topcased.gpm.business.search.service.FilterScope;
import org.topcased.gpm.business.search.service.FilterVisibilityConstraintData;
import org.topcased.gpm.business.search.service.UsableFieldData;
import org.topcased.gpm.business.serialization.data.CategoryValue;
import org.topcased.gpm.business.serialization.data.FieldsContainer;
import org.topcased.gpm.business.serialization.data.LinkType;
import org.topcased.gpm.business.serialization.data.ProductType;
import org.topcased.gpm.business.serialization.data.SheetType;
import org.topcased.gpm.business.sheet.service.SheetSummaryData;
import org.topcased.gpm.business.util.FieldType;
import org.topcased.gpm.business.util.FilterType;
import org.topcased.gpm.business.util.action.AdministrationAction;
import org.topcased.gpm.business.util.log.GPMActionLogConstants;
import org.topcased.gpm.business.util.search.FilterResult;
import org.topcased.gpm.business.util.search.FilterResultId;
import org.topcased.gpm.business.values.field.virtual.VirtualFieldType;
import org.topcased.gpm.common.valuesContainer.LockType;
import org.topcased.gpm.ui.facade.server.AbstractFacade;
import org.topcased.gpm.ui.facade.server.FacadeLocator;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.cache.FilterCache;
import org.topcased.gpm.ui.facade.server.filter.FilterFacade;
import org.topcased.gpm.ui.facade.server.i18n.I18nFacade;
import org.topcased.gpm.ui.facade.server.i18n.I18nTranslationManager;
import org.topcased.gpm.ui.facade.shared.exception.EmptyResultFieldException;
import org.topcased.gpm.ui.facade.shared.exception.NotExistFilterException;
import org.topcased.gpm.ui.facade.shared.exception.NotSpecifiedCriteriaException;
import org.topcased.gpm.ui.facade.shared.exception.NotSpecifiedScopeException;
import org.topcased.gpm.ui.facade.shared.extendedaction.UiFilterEAResult;
import org.topcased.gpm.ui.facade.shared.filter.UiFilter;
import org.topcased.gpm.ui.facade.shared.filter.UiFilterContainerHierarchy;
import org.topcased.gpm.ui.facade.shared.filter.UiFilterContainerType;
import org.topcased.gpm.ui.facade.shared.filter.UiFilterScope;
import org.topcased.gpm.ui.facade.shared.filter.UiFilterUsage;
import org.topcased.gpm.ui.facade.shared.filter.UiFilterVisibility;
import org.topcased.gpm.ui.facade.shared.filter.field.UiFilterFieldName;
import org.topcased.gpm.ui.facade.shared.filter.field.UiFilterFieldNameType;
import org.topcased.gpm.ui.facade.shared.filter.field.UiFilterUsableField;
import org.topcased.gpm.ui.facade.shared.filter.field.criteria.UiFilterCriteriaGroup;
import org.topcased.gpm.ui.facade.shared.filter.field.criteria.UiFilterCriterion;
import org.topcased.gpm.ui.facade.shared.filter.field.criteria.UiFilterOperator;
import org.topcased.gpm.ui.facade.shared.filter.field.result.UiFilterResultField;
import org.topcased.gpm.ui.facade.shared.filter.field.sort.UiFilterSorting;
import org.topcased.gpm.ui.facade.shared.filter.field.sort.UiFilterSortingField;
import org.topcased.gpm.ui.facade.shared.filter.result.UiFilterExecutionReport;
import org.topcased.gpm.ui.facade.shared.filter.result.table.UiFilterTableResult;
import org.topcased.gpm.ui.facade.shared.filter.result.tree.AbstractUiFilterTreeResultNode;
import org.topcased.gpm.ui.facade.shared.filter.result.tree.UiFilterTreeResult;
import org.topcased.gpm.ui.facade.shared.filter.result.tree.UiFilterTreeResultLeaf;
import org.topcased.gpm.ui.facade.shared.filter.result.tree.UiFilterTreeResultNode;
import org.topcased.gpm.ui.facade.shared.filter.summary.UiFilterSummaries;
import org.topcased.gpm.ui.facade.shared.filter.summary.UiFilterSummary;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * FilterFacade
 * 
 * @author nveillet
 */
public class FilterFacadeImpl extends AbstractFacade implements FilterFacade {

    private static final String FILTER_INITIALIZATION_SUFFIX = "_initFilter";
    private static final String FILTER_LINK_CREATION_SUFFIX =  "_linkCreationFilter";
    private static final String FILTER_LINK_DELETION_SUFFIX = "_linkDeletionFilter";

    /**
     * Add a filter to cache
     * 
     * @param pSession
     *            Current user session
     * @param pFilterType
     *            the filter type
     * @param pFilter
     *            the product
     */
    public void addToCache(final UiSession pSession, final FilterType pFilterType, final UiFilter pFilter) {
        // Set temporary Id
        if (pFilter.getId() == null) {
            pFilter.setId(UUID.randomUUID().toString());
        }
        FilterCache lFilterCache = getUserCacheManager().getUserCache(pSession.getParent()).getFilterCache();
        
        switch (pFilterType) {
            case SHEET:
                lFilterCache.getSheetFilters().put(pSession.getProductName(), pFilter);
                break;
            case PRODUCT:
                lFilterCache.setProductFilter(pFilter);
                break;
            default:
                lFilterCache.setPopupFilter(pFilter);
                break;
        }
    }

    private void addUiFilterUsableField(
            I18nTranslationManager pTranslationManager,
            final String pKey,
            final Map<String, UsableFieldData> pUsableFields,
            final Map<String, UiFilterUsableField> pResultMap) {

        if (!pResultMap.containsKey(pKey)) {
            UsableFieldData lUsableFieldData = pUsableFields.get(pKey);
            UiFilterUsableField lFilterUsableField = new UiFilterUsableField();
            lFilterUsableField.setCategoryName(lUsableFieldData.getCategoryName());
            lFilterUsableField.setId(lUsableFieldData.getFieldId());
            lFilterUsableField.setName(lUsableFieldData.getFieldName());
            lFilterUsableField.setTranslatedName(pTranslationManager.getTextTranslation(lUsableFieldData.getFieldName()));
            lFilterUsableField.setFieldType(convertFieldType(lUsableFieldData.getFieldType()));

            if (lUsableFieldData instanceof VirtualFieldData) {
                lFilterUsableField.setVirtualField(true);
                lFilterUsableField.setVirtualPossibleValues(new ArrayList<String>(
                        lUsableFieldData.getPossibleValues()));
            }

            if (lUsableFieldData.getMultipleField() != null
                    && !lUsableFieldData.getMultipleField().isEmpty()) {

                UiFilterUsableField lParentField =
                        pResultMap.get(lUsableFieldData.getMultipleField());

                if (lParentField == null
                        && pUsableFields.containsKey(lUsableFieldData.getMultipleField())) {
                    addUiFilterUsableField(pTranslationManager,
                            lUsableFieldData.getMultipleField(), pUsableFields,
                            pResultMap);
                    lParentField = pResultMap.get(lUsableFieldData.getMultipleField());
                }

                if (lParentField != null) {
                    if (lParentField.getSubFields() == null) {
                        lParentField.setSubFields(new ArrayList<UiFilterUsableField>());
                    }
                    lParentField.getSubFields().add(lFilterUsableField);
                    lFilterUsableField.setParentFieldName(lParentField.getName());
                }
            }
            else {
                pResultMap.put(pKey, lFilterUsableField);
            }
        }
    }

    /**
     * Check the filter information before execution
     * 
     * @param pFilter
     *            the filter
     * @throws NotExistFilterException
     *             Not exist filter exception
     * @throws NotSpecifiedCriteriaException
     *             Not specified scope exception
     * @throws EmptyResultFieldException
     *             Empty result field exception
     * @throws NotSpecifiedScopeException
     *             Not specified criteria exception
     */
    private void checkFilter(UiFilter pFilter)
        throws NotSpecifiedScopeException, EmptyResultFieldException,
        NotSpecifiedCriteriaException, NotExistFilterException {

        // Check Filter
        if (pFilter == null || pFilter.getId() == null
                || pFilter.getId().isEmpty()) {
            throw new NotExistFilterException();
        }

        // Check Scope
        if (pFilter.getScopes() != null) {
            for (UiFilterScope lScope : pFilter.getScopes()) {
                if (UsableFieldsManager.NOT_SPECIFIED.equals(lScope.getProductName().getValue())
                        || lScope.getProductName().getValue().isEmpty()) {
                    throw new NotSpecifiedScopeException(pFilter.getId());
                }
            }
        }

        // Check Result Fields
        if (pFilter.getResultFields() == null
                || pFilter.getResultFields().isEmpty()) {
            throw new EmptyResultFieldException(pFilter.getId());
        }

        // Check Criteria
        if (pFilter.getCriteriaGroups() != null) {
            for (UiFilterCriteriaGroup lCriteriaGroup : pFilter.getCriteriaGroups()) {
                for (UiFilterCriterion lCriterion : lCriteriaGroup.getCriteria()) {
                    if (UsableFieldsManager.NOT_SPECIFIED.equals(lCriterion.getValue())
                            || lCriterion.getValue() == null
                            || (lCriterion.getValue().toString().isEmpty() && !FieldType.STRING.equals(lCriterion.getFieldType()))) {
                        throw new NotSpecifiedCriteriaException(pFilter.getId());
                    }
                }
            }
        }
    }

    /**
     * Clear a filter from cache
     * 
     * @param pSession
     *            Current user session
     * @param pFilterType
     *            the filter type
     */
    public void clearCache(final UiSession pSession,
            final FilterType pFilterType) {
        FilterCache lFilterCache =
                getUserCacheManager().getUserCache(pSession.getParent()).getFilterCache();
        switch (pFilterType) {
            case SHEET:
                lFilterCache.getSheetFilters().remove(pSession.getProductName());
                break;
            case PRODUCT:
                lFilterCache.setProductFilter(null);
                break;
            default:
                lFilterCache.setPopupFilter(null);
                break;
        }
    }

    /**
     * Convert a criteria group from UI for business
     * 
     * @param pSession
     *            the session
     * @param pFilterCriteriaGroup
     *            the criteria group from UI
     * @return the criteria data for business
     */
    private CriteriaData convertCriteriaData(final UiSession pSession,
            final UiFilterCriteriaGroup pFilterCriteriaGroup,
            List<UiFilterContainerType> pUiContainerTypes) {

        OperationData lAndOperation = new OperationData();
        lAndOperation.setOperator(Operators.AND);
        List<CriteriaData> lCriterias = new ArrayList<CriteriaData>();

        for (UiFilterCriterion lFilterCriterion : pFilterCriteriaGroup.getCriteria()) {

            LinkedList<UiFilterFieldName> lName = lFilterCriterion.getName();
            UsableFieldData lUsableFieldData =
            		getSearchService().getUsableField(
            				pSession.getParent().getProcessName(),
            				lName.getLast().getName(),
            				getContainerIds(pSession, lName, pUiContainerTypes));
            lUsableFieldData.setFieldsContainerHierarchy(convertFieldHierarchy(
            		pSession, lFilterCriterion.getName()));
            lUsableFieldData.setId(SearchUtils.createUsableFieldDataId(
            		lUsableFieldData.getFieldsContainerHierarchy(), lUsableFieldData.getLabel()));

            ScalarValueData lScalarValueData = null;
            Object lValue = lFilterCriterion.getValue();

            if (lValue instanceof String) {
                lScalarValueData = new StringValueData((String) lValue);
            }
            else if (lValue instanceof Double) {
                lScalarValueData = new RealValueData((Double) lValue);
            }
            else if (lValue instanceof Integer) {
                lScalarValueData = new IntegerValueData((Integer) lValue);
            }
            else if (lValue instanceof Boolean) {
                lScalarValueData = new BooleanValueData((Boolean) lValue);
            }
            else if (lValue instanceof Date) {
                lScalarValueData = new DateValueData((Date) lValue);
            }

            String lOperator;
            switch (lFilterCriterion.getOperator()) {
                case EQUAL:
                    lOperator = Operators.EQ;
                    break;
                case NOT_EQUAL:
                    lOperator = Operators.NEQ;
                    break;
                case GREATER:
                    lOperator = Operators.GT;
                    break;
                case GREATER_OR_EQUALS:
                    lOperator = Operators.GE;
                    break;
                case LESS:
                    lOperator = Operators.LT;
                    break;
                case LESS_OR_EQUALS:
                    lOperator = Operators.LE;
                    break;
                case LIKE:
                    lOperator = Operators.LIKE;
                    break;
                case NOT_LIKE:
                    lOperator = Operators.NOT_LIKE;
                    break;
                case SINCE:
                    lOperator = Operators.SINCE;
                    break;
                case OTHER:
                    lOperator = Operators.OTHER;
                    break;
                default:
                    throw new InstantiateException("Invalid operator '"
                            + lFilterCriterion.getOperator() + "' for field '"
                            + lFilterCriterion.getName().getLast().getName()
                            + "'", lFilterCriterion);
            }

            CriteriaData lCritData =
                    new CriteriaFieldData(lOperator,
                            lFilterCriterion.isCaseSensitive(),
                            lScalarValueData, lUsableFieldData);

            if (pFilterCriteriaGroup.getCriteria().size() == 1) {
                return lCritData;
            }
            lCriterias.add(lCritData);

        }
        lAndOperation.setCriteriaDatas(lCriterias.toArray(new CriteriaData[0]));
        return lAndOperation;
    }

    /**
     * Convert a criteria group from business for UI
     * 
     * @param pCriteria
     *            the criteria group from business
     * @param pCategoryValues
     *            the categories values map
     * @return the criteria group for UI
     */
    private UiFilterCriteriaGroup convertCriteriaGroup(
            final I18nTranslationManager pTranslationManager,
            final CriteriaData pCriteria,
            final Map<String, List<String>> pCategoryValues) {
        final UiFilterCriteriaGroup lCriteriaGroup =
                new UiFilterCriteriaGroup(new ArrayList<UiFilterCriterion>());
        if (pCriteria != null) {
            if (pCriteria instanceof CriteriaFieldData) {
                lCriteriaGroup.getCriteria().add(
                        convertCriterion(pTranslationManager,
                                (CriteriaFieldData) pCriteria, pCategoryValues));
            }
            else if (pCriteria instanceof OperationData) {
                for (CriteriaData lCriteriaField : ((OperationData) pCriteria).getCriteriaDatas()) {
                    lCriteriaGroup.getCriteria().add(
                            convertCriterion(pTranslationManager,
                                    (CriteriaFieldData) lCriteriaField,
                                    pCategoryValues));
                }
            }
        }

        return lCriteriaGroup;
    }

    /**
     * Convert a criterion from business for UI
     * 
     * @param pTranslationManager
     *            translation manager
     * @param pCriterion
     *            the criterion from business
     * @param pCategoryValues
     *            the categories values map
     * @return the criterion for UI
     */
    private UiFilterCriterion convertCriterion(
            final I18nTranslationManager pTranslationManager,
            final CriteriaFieldData pCriterion,
            final Map<String, List<String>> pCategoryValues) {
        // Get criteria field name
        final LinkedList<UiFilterFieldName> lFieldName =
                getFilterFieldName(pTranslationManager,
                        pCriterion.getUsableFieldData());

        // Get criteria operator
        final UiFilterOperator lOperator =
                convertFilterOperator(pCriterion.getOperator());

        // Get criteria value
        ScalarValueData lScalarValueData = pCriterion.getScalarValueData();

        Serializable lValue = null;

        if (lScalarValueData instanceof StringValueData) {
            lValue = ((StringValueData) lScalarValueData).getValue();
        }
        else if (lScalarValueData instanceof IntegerValueData) {
            lValue = ((IntegerValueData) lScalarValueData).getValue();
        }
        else if (lScalarValueData instanceof RealValueData) {
            lValue = ((RealValueData) lScalarValueData).getValue();
        }
        else if (lScalarValueData instanceof BooleanValueData) {
            lValue = ((BooleanValueData) lScalarValueData).getValue();
        }
        else if (lScalarValueData instanceof DateValueData) {
            lValue = ((DateValueData) lScalarValueData).getValue();
        }

        String lCategoryName =
                pCriterion.getUsableFieldData().getCategoryName();
        if (VirtualFieldType.isExist(lFieldName.getLast().getName())) {
            lCategoryName = lFieldName.getLast().getName();
            pCategoryValues.put(lCategoryName, new ArrayList<String>(
                    pCriterion.getUsableFieldData().getPossibleValues()));
        }

        return new UiFilterCriterion(
                pCriterion.getUsableFieldData().getFieldId(),
                lFieldName,
                lOperator,
                lValue,
                pCriterion.getCaseSensitive(),
                convertFieldType(pCriterion.getUsableFieldData().getFieldType()),
                lCategoryName);
    }

    private List<FilterFieldsContainerInfo> convertFieldHierarchy(
            UiSession pSession, LinkedList<UiFilterFieldName> pName) {
        ArrayList<FilterFieldsContainerInfo> lFilterFieldsContainerInfoList =
                new ArrayList<FilterFieldsContainerInfo>();
        for (int i = 0; i < pName.size(); i++) {
            UiFilterFieldName lFieldName = pName.get(i);
            String lId =
                    getFieldsContainerService().getFieldsContainerId(
                            pSession.getRoleToken(), lFieldName.getName());
            FieldsContainerType lType = null;
            LinkDirection lLinkDirection = LinkDirection.UNDEFINED;
            switch (lFieldName.getType()) {
                case SHEET:
                    lType = FieldsContainerType.SHEET;
                    break;
                case PRODUCT:
                    lType = FieldsContainerType.PRODUCT;
                    break;
                case LINK:
                    lType = FieldsContainerType.LINK;

                    CacheableLinkType lLink =
                            getLinkService().getLinkType(
                                    pSession.getRoleToken(), lId,
                                    CacheProperties.IMMUTABLE);

                    if (i + 1 < pName.size()
                            && !lLink.getOriginTypeId().equals(
                                    lLink.getDestinationTypeId())) {

                        String lNextContainerId =
                                getFieldsContainerService().getFieldsContainerId(
                                        pSession.getRoleToken(),
                                        pName.get(i + 1).getName());
                        if (lNextContainerId != null) {
                            if (lLink.getOriginTypeId().equals(lNextContainerId)) {
                                lLinkDirection = LinkDirection.ORIGIN;
                            }
                            else if (lLink.getDestinationTypeId().equals(
                                    lNextContainerId)) {
                                lLinkDirection = LinkDirection.DESTINATION;
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
            if (lId != null) {
                lFilterFieldsContainerInfoList.add(new FilterFieldsContainerInfo(
                        lId, lFieldName.getName(), lType, lLinkDirection));
            }
        }
        return lFilterFieldsContainerInfoList;
    }

    /**
     * convert fieldType
     * 
     * @param pFieldType
     *            field type to convert
     * @return field type
     */
    private FieldType convertFieldType(
            org.topcased.gpm.business.fields.FieldType pFieldType) {
        FieldType lResult = null;
        switch (pFieldType) {
            case ATTACHED_FIELD:
                lResult = FieldType.ATTACHED;
                break;
            case CHOICE_FIELD:
                lResult = FieldType.CHOICE;
                break;
            case MULTIPLE_FIELD:
                lResult = FieldType.MULTIPLE;
                break;
            case SIMPLE_BOOLEAN_FIELD:
                lResult = FieldType.BOOLEAN;
                break;
            case SIMPLE_DATE_FIELD:
                lResult = FieldType.DATE;
                break;
            case SIMPLE_INTEGER_FIELD:
                lResult = FieldType.INTEGER;
                break;
            case SIMPLE_REAL_FIELD:
                lResult = FieldType.REAL;
                break;
            case SIMPLE_STRING_FIELD:
                lResult = FieldType.STRING;
                break;
            default:
                break;
        }
        return lResult;
    }

    /**
     * Convert a filter from business for UI
     * 
     * @param pSession
     *            the session
     * @param pExecutableFilterData
     *            the filter from business
     * @return the filter for UI
     */
    private UiFilter convertFilter(final UiSession pSession,
            final ExecutableFilterData pExecutableFilterData) {

        if (pExecutableFilterData == null) {
            return null;
        }

        I18nTranslationManager lTranslationManager =
                FacadeLocator.instance().getI18nFacade().getTranslationManager(
                        pSession.getParent().getLanguage());

        final UiFilter lFilter = new UiFilter();
        lFilter.setId(pExecutableFilterData.getId());
        lFilter.setName(pExecutableFilterData.getLabelKey());
        lFilter.setDescription(pExecutableFilterData.getDescription());
        lFilter.setHidden(pExecutableFilterData.isHidden());
        lFilter.setEditable(pExecutableFilterData.isEditable());

        // Convert containers      
        lFilter.setContainerTypes(new ArrayList<UiFilterContainerType>());
        I18nFacade lI18nFacade = FacadeLocator.instance().getI18nFacade();
        for (String lContainerId : pExecutableFilterData.getFilterData().getFieldsContainerIds()) {
            final CacheableFieldsContainer lCacheableFieldsContainer;
            switch (pExecutableFilterData.getFilterData().getType()) {
                case PRODUCT:
                    lCacheableFieldsContainer =
                            getProductService().getCacheableProductType(
                                    pSession.getRoleToken(), lContainerId,
                                    CacheProperties.IMMUTABLE);
                    break;
                default:
                    lCacheableFieldsContainer =
                            getSheetService().getCacheableSheetType(
                                    pSession.getRoleToken(), lContainerId,
                                    CacheProperties.IMMUTABLE);
                    break;
            }

            lFilter.getContainerTypes().add(
                    new UiFilterContainerType(
                            lCacheableFieldsContainer.getId(),
                            lI18nFacade.getTranslation(pSession.getParent(),
                                    lCacheableFieldsContainer.getName())));
        }

        // Convert scopes
        lFilter.setScopes(new ArrayList<UiFilterScope>());
        for (FilterProductScope lScope : pExecutableFilterData.getFilterProductScopes()) {
            lFilter.getScopes().add(
                    new UiFilterScope(lI18nFacade.getTranslation(
                            pSession.getParent(), lScope.getProductName()),
                            lScope.isIncludeSubProducts()));
        }

        // Convert result fields
        lFilter.setResultFields(new ArrayList<UiFilterResultField>());
        for (UsableFieldData lUsableField : pExecutableFilterData.getResultSummaryData().getUsableFieldDatas()) {
            lFilter.getResultFields().add(
                    new UiFilterResultField(lUsableField.getLabel(),
                            getFilterFieldName(lTranslationManager,
                                    lUsableField)));
        }

        Map<String, List<String>> lCategoryValues =
                new HashMap<String, List<String>>();

        // Convert criteria fields
        lFilter.setCriteriaGroups(new ArrayList<UiFilterCriteriaGroup>());
        final CriteriaData lGlobalCriteria =
                pExecutableFilterData.getFilterData().getCriteriaData();
        if (lGlobalCriteria != null) {
            if (lGlobalCriteria instanceof OperationData
                    && ((OperationData) lGlobalCriteria).getOperator().equals(
                            Operators.OR)) {
                for (CriteriaData lCriteriaField : ((OperationData) lGlobalCriteria).getCriteriaDatas()) {
                    lFilter.getCriteriaGroups().add(
                            convertCriteriaGroup(lTranslationManager,
                                    lCriteriaField, lCategoryValues));
                }
            }
            else {
                lFilter.getCriteriaGroups().add(
                        convertCriteriaGroup(lTranslationManager,
                                lGlobalCriteria, lCategoryValues));
            }
        }

        // Convert sorting fields
        List<UiFilterSortingField> lSortingFields =
                new ArrayList<UiFilterSortingField>();
        lFilter.setSortingFields(lSortingFields);
        if (pExecutableFilterData.getResultSortingData() != null
                && pExecutableFilterData.getResultSortingData().getSortingFieldDatas() != null) {
            for (SortingFieldData lSortingFieldData : pExecutableFilterData.getResultSortingData().getSortingFieldDatas()) {
                boolean lVirtualField = false;
                if (lSortingFieldData.getUsableFieldData() instanceof VirtualFieldData) {
                    lVirtualField = true;
                }
                lSortingFields.add(new UiFilterSortingField(
                        lSortingFieldData.getUsableFieldData().getFieldId(),
                        getFilterFieldName(lTranslationManager,
                                lSortingFieldData.getUsableFieldData()),
                        convertSortingOrder(lSortingFieldData.getOrder()),
                        convertFieldType(lSortingFieldData.getUsableFieldData().getFieldType()),
                        lVirtualField));
            }
        }

        // Get category values.
        if (lFilter.getCriteriaGroups() != null) {
            for (UiFilterCriteriaGroup lCriteriaGroup : lFilter.getCriteriaGroups()) {
                for (UiFilterCriterion lCriterion : lCriteriaGroup.getCriteria()) {
                    if (lCriterion.getCategoryName() != null
                            && !lCategoryValues.containsKey(lCriterion.getCategoryName())) {
                        lCategoryValues.put(lCriterion.getCategoryName(),
                                getCategoryValues(pSession,
                                        lCriterion.getCategoryName(),
                                        lFilter.getScopes()));
                    }
                }
            }
        }
        lFilter.setCategoryValues(lCategoryValues);

        lFilter.setUsage(UiFilterUsage.valueOf(pExecutableFilterData.getUsage()));
        lFilter.setVisibility(getFilterVisibility(pExecutableFilterData.getFilterVisibilityConstraintData()));
        lFilter.setFilterType(FilterType.valueOf(pExecutableFilterData.getFilterData().getType().toString()));

        lFilter.setBusinessFilterDataId(pExecutableFilterData.getFilterData().getId());

        return lFilter;
    }

    /**
     * Convert a filter from UI for business
     * 
     * @param pSession
     *            the session
     * @param pFilter
     *            the filter from UI
     * @return the filter for business
     */
    private ExecutableFilterData convertFilter(final UiSession pSession,
            final UiFilter pFilter) {

        if (pFilter == null) {
            return null;
        }

        // FilterVisibilityConstraintData
        FilterVisibilityConstraintData lFilterVisibilityConstraintData =
                new FilterVisibilityConstraintData();
        lFilterVisibilityConstraintData.setBusinessProcessName(pSession.getParent().getProcessName());
        if (UiFilterVisibility.PRODUCT.equals(pFilter.getVisibility())) {
            lFilterVisibilityConstraintData.setProductName(pSession.getProductName());
        }
        else if (UiFilterVisibility.USER.equals(pFilter.getVisibility())) {
            lFilterVisibilityConstraintData.setUserLogin(pSession.getParent().getLogin());
        }

        List<String> lFieldsContainerIds = new ArrayList<String>();
        for (UiFilterContainerType lContainerType : pFilter.getContainerTypes()) {
            lFieldsContainerIds.add(lContainerType.getId());
        }

        // ResultSummaryData
        int lResultFieldCount = 0;
        if (pFilter.getResultFields() != null) {
            lResultFieldCount = pFilter.getResultFields().size();
        }
        UsableFieldData[] lResultFieldDatas =
                new UsableFieldData[lResultFieldCount];
        for (int i = 0; i < lResultFieldCount; i++) {
            LinkedList<UiFilterFieldName> lName =
                    pFilter.getResultFields().get(i).getName();
            UsableFieldData lUsableField =
                    getSearchService().getUsableField(
                            pSession.getParent().getProcessName(),
                            lName.getLast().getName(),
                            getContainerIds(pSession, lName,
                                    pFilter.getContainerTypes()));
            lUsableField.setFieldsContainerHierarchy(convertFieldHierarchy(
                    pSession, pFilter.getResultFields().get(i).getName()));
            lUsableField.setLabel(pFilter.getResultFields().get(i).getLabel());
            lUsableField.setId(SearchUtils.createUsableFieldDataId(
                    lUsableField.getFieldsContainerHierarchy(),
                    lName.getLast().getName()));
            lResultFieldDatas[i] = lUsableField;
        }

        ResultSummaryData lResultSummaryData = new ResultSummaryData();
        lResultSummaryData.setLabelKey(pFilter.getName());
        lResultSummaryData.setFieldsContainerIds(lFieldsContainerIds.toArray(new String[0]));
        lResultSummaryData.setUsableFieldDatas(lResultFieldDatas);
        lResultSummaryData.setFilterVisibilityConstraintData(lFilterVisibilityConstraintData);

        // ResultSortingData
        ResultSortingData lResultSortingData = null;
        int lSortingFieldCount = 0;
        SortingFieldData[] lSortingFieldDatas = null;
        if (pFilter.getSortingFields() != null
                && !pFilter.getSortingFields().isEmpty()) {
            lSortingFieldCount = pFilter.getSortingFields().size();
            lSortingFieldDatas = new SortingFieldData[lSortingFieldCount];
            for (int i = 0; i < lSortingFieldCount; i++) {
                UiFilterSortingField lFilterSortingField =
                        pFilter.getSortingFields().get(i);
                LinkedList<UiFilterFieldName> lName =
                        lFilterSortingField.getName();
                UsableFieldData lUsableFieldData =
                        getSearchService().getUsableField(
                                pSession.getParent().getProcessName(),
                                lName.getLast().getName(),
                                getContainerIds(pSession, lName,
                                        pFilter.getContainerTypes()));
                lUsableFieldData.setFieldsContainerHierarchy(convertFieldHierarchy(
                        pSession, lFilterSortingField.getName()));
                lUsableFieldData.setId(SearchUtils.createUsableFieldDataId(
                        lUsableFieldData.getFieldsContainerHierarchy(),
                        lName.getLast().getName()));
                String lOrder;
                switch (lFilterSortingField.getOrder()) {
                    case ASCENDANT:
                        lOrder = Operators.ASC;
                        break;
                    case DESCENDANT:
                        lOrder = Operators.DESC;
                        break;
                    case ASCENDANT_DEFINED:
                        lOrder = Operators.DEF_ASC;
                        break;
                    case DESCENDANT_DEFINED:
                        lOrder = Operators.DEF_DESC;
                        break;
                    default:
                        lOrder = null;
                        break;
                }
                lSortingFieldDatas[i] =
                        new SortingFieldData(lFilterSortingField.getId(),
                                lOrder, lUsableFieldData);
            }

            lResultSortingData = new ResultSortingData();
            lResultSortingData.setLabelKey(pFilter.getName());
            lResultSortingData.setFieldsContainerIds(lFieldsContainerIds.toArray(new String[0]));
            lResultSortingData.setFilterVisibilityConstraintData(lFilterVisibilityConstraintData);
            lResultSortingData.setSortingFieldDatas(lSortingFieldDatas);
        }

        // Criteria
        List<UiFilterCriteriaGroup> lFilterCriteriaGroups =
                pFilter.getCriteriaGroups();
        CriteriaData lCriteriaData = null;
        if (lFilterCriteriaGroups != null && !lFilterCriteriaGroups.isEmpty()) {
            if (lFilterCriteriaGroups.size() == 1) {
                lCriteriaData =
                        convertCriteriaData(pSession,
                                lFilterCriteriaGroups.get(0),
                                pFilter.getContainerTypes());
            }
            else {
                OperationData lOrOperation = new OperationData();
                lOrOperation.setOperator(Operators.OR);
                List<CriteriaData> lCriterias = new ArrayList<CriteriaData>();
                for (UiFilterCriteriaGroup lFilterCriteriaGroup : lFilterCriteriaGroups) {
                    lCriterias.add(convertCriteriaData(pSession,
                            lFilterCriteriaGroup, pFilter.getContainerTypes()));
                }
                lOrOperation.setCriteriaDatas(lCriterias.toArray(new CriteriaData[0]));
                lCriteriaData = lOrOperation;
            }
        }

        // FilterData
        FilterData lFilterData = new FilterData();
        lFilterData.setId(pFilter.getBusinessFilterDataId());
        lFilterData.setLabelKey(pFilter.getName());
        lFilterData.setCriteriaData(lCriteriaData);
        lFilterData.setFieldsContainerIds(lFieldsContainerIds.toArray(new String[0]));
        lFilterData.setFilterVisibilityConstraintData(lFilterVisibilityConstraintData);
        lFilterData.setType(FilterTypeData.valueOf(pFilter.getFilterType().toString()));

        // FilterProductScopes
        int lProductScopeCount = 0;
        if (pFilter.getScopes() != null) {
            lProductScopeCount = pFilter.getScopes().size();
        }
        FilterProductScope[] lFilterProductScopes =
                new FilterProductScope[lProductScopeCount];
        for (int i = 0; i < lProductScopeCount; i++) {
            UiFilterScope lScope = pFilter.getScopes().get(i);
            lFilterProductScopes[i] =
                    new FilterProductScope(lScope.getProductName().getValue(),
                            lScope.isIncludeSubProduct());
        }

        // ExecutableFilterData
        return new ExecutableFilterData(pFilter.getId(), pFilter.getName(),
                pFilter.getDescription(), pFilter.getUsage().name(),
                pFilter.getHidden(), lResultSummaryData, lResultSortingData,
                lFilterData, lFilterVisibilityConstraintData,
                lFilterProductScopes);
    }

    /**
     * Convert a filter execution report from business to a filter execution
     * report for UI
     * 
     * @param pExecutionReport
     *            the filter execution report from business
     * @return the filter execution report for UI
     */
    private UiFilterExecutionReport convertFilterExecutionReport(
            final FilterExecutionReport pExecutionReport) {
        UiFilterExecutionReport lExecutionReport =
                new UiFilterExecutionReport();

        // Non executable products
        lExecutionReport.setNonExecutableProducts(new HashSet<String>(
                pExecutionReport.getNonExecutableProducts()));

        // Executable products
        HashMap<String, HashSet<String>> lExecutableProducts =
                new HashMap<String, HashSet<String>>();
        for (Entry<String, Set<String>> lEntry : pExecutionReport.getExecutableProducts().entrySet()) {
            lExecutableProducts.put(lEntry.getKey(), new HashSet<String>(
                    lEntry.getValue()));
        }
        lExecutionReport.setExecutableProducts(lExecutableProducts);

        // Additional constraints
        final HashMap<String, HashMap<String, HashSet<String>>> lAdditionalConstraints =
                new HashMap<String, HashMap<String, HashSet<String>>>();
        final Map<UsableTypeData, Map<String, Set<FilterAccessContraint>>> lBusinessAdditionalConstraints =
                pExecutionReport.getAdditionalConstraints();

        for (UsableTypeData lType : lBusinessAdditionalConstraints.keySet()) {
            final String lTypeName = lType.getFullName();
            final HashMap<String, HashSet<String>> lTypeMap =
                    new HashMap<String, HashSet<String>>();

            for (String lRole : lBusinessAdditionalConstraints.get(lType).keySet()) {
                final HashSet<String> lRoleSet = new HashSet<String>();

                for (FilterAccessContraint lConstraint : lBusinessAdditionalConstraints.get(
                        lType).get(lRole)) {
                    String lConstraintValue = lConstraint.getName();

                    if (lConstraint.getDescription() != null) {
                        lConstraintValue +=
                                " - " + lConstraint.getDescription();
                    }
                    lRoleSet.add(lConstraintValue);
                }
                if (!lRoleSet.isEmpty()) {
                    lTypeMap.put(lRole, lRoleSet);
                }
            }
            if (!lTypeMap.isEmpty()) {
                lAdditionalConstraints.put(lTypeName, lTypeMap);
            }
        }
        lExecutionReport.setAdditionalConstraints(lAdditionalConstraints);

        return lExecutionReport;
    }

    /**
     * Convert a operator from business for UI
     * 
     * @param pOperator
     *            the operator from business
     * @return the operator for UI
     */
    private UiFilterOperator convertFilterOperator(final String pOperator) {
        UiFilterOperator lOperator = null;
        if (Operators.EQ.equals(pOperator)) {
            lOperator = UiFilterOperator.EQUAL;
        }
        else if (Operators.NEQ.equals(pOperator)) {
            lOperator = UiFilterOperator.NOT_EQUAL;
        }
        else if (Operators.GT.equals(pOperator)) {
            lOperator = UiFilterOperator.GREATER;
        }
        else if (Operators.GE.equals(pOperator)) {
            lOperator = UiFilterOperator.GREATER_OR_EQUALS;
        }
        else if (Operators.LT.equals(pOperator)) {
            lOperator = UiFilterOperator.LESS;
        }
        else if (Operators.LE.equals(pOperator)) {
            lOperator = UiFilterOperator.LESS_OR_EQUALS;
        }
        else if (Operators.LIKE.equals(pOperator)) {
            lOperator = UiFilterOperator.LIKE;
        }
        else if (Operators.NOT_LIKE.equals(pOperator)) {
            lOperator = UiFilterOperator.NOT_LIKE;
        }
        else if (Operators.SINCE.equals(pOperator)) {
            lOperator = UiFilterOperator.SINCE;
        }
        else if (Operators.OTHER.equals(pOperator)) {
            lOperator = UiFilterOperator.OTHER;
        }
        return lOperator;
    }

    /**
     * Convert a filter result from business to a table filter result for UI
     * 
     * @param pSession
     *            the session
     * @param pFilterResultIterator
     *            the filter result from business
     * @param pResultFields
     *            the filter result fields
     * @return the table filter result for UI
     */
    private UiFilterTableResult convertFilterTableResult(
            final UiSession pSession,
            final FilterResultIterator<SummaryData> pFilterResultIterator,
            final List<UiFilterResultField> pResultFields,
            final String pFilterId) {
        // Convert filter result
        final List<String> lColumnNames = new ArrayList<String>();

        for (UiFilterResultField lResultField : pResultFields) {
            lColumnNames.add(StringEscapeUtils.escapeHtml(getFieldResultLabel(pSession, lResultField)));
        }

        final List<FilterResult> lResultValues = new ArrayList<FilterResult>();
        final String lCurrentLogin = pSession.getParent().getLogin();
        final boolean lGlobalAdmin =
                getAuthorizationService().hasGlobalAdminRole(
                        pSession.getRoleToken());

        while (pFilterResultIterator.hasNext()) {
            final SummaryData lSummaryData = pFilterResultIterator.next();
            final List<String> lValues = new ArrayList<String>();
            String lVisuLocker = null;
            String lEditLocker = null;

            // Add field values
            for (FieldSummaryData lFieldSummaryData : lSummaryData.getFieldSummaryDatas()) {
                lValues.add(lFieldSummaryData.getValue());
            }
            if (lSummaryData.getLock() != null) {
                final String lOwner = lSummaryData.getLock().getOwner();

                if (!lCurrentLogin.equals(lOwner)) {
                    if (LockType.READ_WRITE.equals(lSummaryData.getLock().getType())) {
                        lVisuLocker = lOwner;
                        lEditLocker = lOwner;
                    }
                    else if (LockType.WRITE.equals(lSummaryData.getLock().getType())) {
                        lEditLocker = lOwner;
                    }
                }
            }
            lResultValues.add(new FilterResult(new FilterResultId(
                    lSummaryData.getId(), lVisuLocker, lEditLocker,
                    lGlobalAdmin), lValues));
        }

        UiFilterExecutionReport lFilterExecutionReport =
                convertFilterExecutionReport(pFilterResultIterator.getExecutionReport());

        return new UiFilterTableResult(lColumnNames, lResultValues,
                lFilterExecutionReport, pFilterId);
    }

    /**
     * Convert a filter result from business to a tree filter result for UI
     * 
     * @param pSession
     *            the session
     * @param pFilterResultIterator
     *            the filter result from business
     * @param pResultFields
     *            the filter result fields
     * @return the tree filter result for UI
     */
    private UiFilterTreeResult convertFilterTreeResult(
            final UiSession pSession,
            final FilterResultIterator<SummaryData> pFilterResultIterator,
            final List<UiFilterResultField> pResultFields) {

        UiFilterTreeResult lFilterTreeResult =
                new UiFilterTreeResult(new ArrayList<UiFilterTreeResultNode>(),
                        true);

        while (pFilterResultIterator.hasNext()) {
            SummaryData lSummaryData = pFilterResultIterator.next();

            AbstractUiFilterTreeResultNode lParentNode = lFilterTreeResult;
            boolean lPassedReference = false;

            for (int i = 0; i < lSummaryData.getFieldSummaryDatas().length; i++) {
                FieldSummaryData lFieldSummaryData =
                        lSummaryData.getFieldSummaryDatas()[i];

                if ((lSummaryData instanceof SheetSummaryData && lFieldSummaryData.getLabelKey().equals(
                        VirtualFieldType.$SHEET_REFERENCE.name()))
                        || (lSummaryData instanceof ProductSummaryData && lFieldSummaryData.getLabelKey().equals(
                                VirtualFieldType.$PRODUCT_NAME.name()))) {
                    lPassedReference = true;
                    UiFilterTreeResultLeaf lLeaf =
                            new UiFilterTreeResultLeaf(lSummaryData.getId(),
                                    lFieldSummaryData.getValue(), null);
                    lParentNode.getResultNodes().add(lLeaf);
                    lParentNode = lLeaf;
                }
                else if (lPassedReference) {
                    UiFilterTreeResultLeaf lLeaf =
                            ((UiFilterTreeResultLeaf) lParentNode);

                    String lDescription = lFieldSummaryData.getValue();
                    if (lLeaf.getDescription() != null) {
                        lDescription =
                                lLeaf.getDescription() + "|" + lDescription;
                    }
                    lLeaf.setDescription(lDescription);
                }
                else {
                    UiFilterTreeResultNode lNode =
                            getFilterTreeResultNode(lParentNode,
                                    lFieldSummaryData.getValue());
                    if (lNode == null) {

                        lNode =
                                new UiFilterTreeResultNode(
                                        StringEscapeUtils.escapeHtml(lFieldSummaryData.getValue()),
                                        StringEscapeUtils.escapeHtml(getFieldResultLabel(
                                                pSession, pResultFields.get(i))),
                                        new ArrayList<UiFilterTreeResultNode>());
                        lParentNode.getResultNodes().add(lNode);
                    }
                    lParentNode = lNode;
                }
            }

            if (!lPassedReference) {
                String lReference;
                if (lSummaryData instanceof SheetSummaryData) {
                    lReference =
                            ((SheetSummaryData) lSummaryData).getSheetReference();
                }
                else {
                    lReference = ((ProductSummaryData) lSummaryData).getName();
                }

                lParentNode.getResultNodes().add(
                        new UiFilterTreeResultLeaf(lSummaryData.getId(),
                                lReference, null));
            }
        }

        return lFilterTreeResult;
    }

    /**
     * Convert a sorting order from business for UI
     * 
     * @param pOrder
     *            the sorting order from business
     * @return the sorting order for UI
     */
    private UiFilterSorting convertSortingOrder(final String pOrder) {
        UiFilterSorting lOrder = null;
        if (Operators.ASC.equals(pOrder)) {
            lOrder = UiFilterSorting.ASCENDANT;
        }
        else if (Operators.DESC.equals(pOrder)) {
            lOrder = UiFilterSorting.DESCENDANT;
        }
        else if (Operators.DEF_ASC.equals(pOrder)) {
            lOrder = UiFilterSorting.ASCENDANT_DEFINED;
        }
        else if (Operators.DEF_DESC.equals(pOrder)) {
            lOrder = UiFilterSorting.DESCENDANT_DEFINED;
        }
        return lOrder;
    }

    /**
     * Delete a filter
     * 
     * @param pSession
     *            the session
     * @param pFilterId
     *            the filter identifier
     */
    public void deleteFilter(final UiSession pSession, final String pFilterId) {
        getSearchService().removeExecutableFilter(pSession.getRoleToken(),
                pFilterId);
    }

    /**
     * execute a filter
     * 
     * @param pSession
     *            the session
     * @param pFilter
     *            the filter
     * @return the filter result
     */
    private FilterResultIterator<SummaryData> executeFilter(
            final UiSession pSession, final ExecutableFilterData pFilter) {
        return getSearchService().executeFilter(
                pSession.getRoleToken(),
                pFilter,
                new FilterVisibilityConstraintData(
                        pSession.getParent().getLogin(),
                        pSession.getParent().getProcessName(),
                        pSession.getProductName()),
                new FilterQueryConfigurator());
    }

    /**
     * Execute filter for extended action result
     * 
     * @param pSession
     *            The session
     * @param pFilterProductName
     *            the filter product name
     * @param pFilterSummaryName
     *            the filter summary name
     * @param pResultScope
     *            the filter scope
     * @param pFilterSorterName
     *            the filter sorter name
     * @param pSheetIds
     *            the sheet identifiers
     * @return The filter extended action result
     */
    public UiFilterEAResult executeFilterExtendedAction(
            final UiSession pSession, final String pFilterProductName,
            final String pFilterSummaryName, final FilterScope pResultScope,
            final String pFilterSorterName, final Collection<String> pSheetIds) {

        // Get Columns of filter result
        String lFilterProductName = null;
        String lFilterUserLogin = null;
        switch (pResultScope) {
            case PRODUCT_FILTER:
                lFilterProductName = pFilterProductName;
                break;
            case USER_FILTER:
                lFilterUserLogin = pSession.getParent().getLogin();
                break;
            default:
                break;
        }
        List<UiFilterResultField> lResultFields =
                convertFilter(
                        pSession,
                        getSearchService().getExecutableFilterByName(
                                pSession.getRoleToken(),
                                pSession.getParent().getProcessName(),
                                lFilterProductName, lFilterUserLogin,
                                pFilterSummaryName)).getResultFields();

        // Get result
        final ExecutableFilterData lExecutableFilterData =
                getExecutableFilterDataByName(pSession, pFilterSorterName);

        FilterResultIterator<SummaryData> lFilterResultIterator =
                getSearchService().executeFilter(
                        pSession.getRoleToken(),
                        lExecutableFilterData,
                        new FilterVisibilityConstraintData(
                                pSession.getParent().getLogin(),
                                pSession.getParent().getProcessName(),
                                pSession.getProductName()),
                        new FilterQueryConfigurator(), pSheetIds);

        try {
            UiFilter lFilter = convertFilter(pSession, lExecutableFilterData);
            lFilter.setResultFields(lResultFields);

            return new UiFilterEAResult(convertFilterTableResult(pSession,
                    lFilterResultIterator, lFilter.getResultFields(),
                    lFilter.getId()), lFilter);
        }
        finally {
            lFilterResultIterator.close();
        }
    }

    /**
     * Execute a filter for the link creation
     * 
     * @param pSession
     *            the session
     * @param pValuesContainerId
     *            the current container identifier
     * @param pLinkTypeName
     *            the link type name
     * @return the filter result
     * @throws NotExistFilterException
     *             Not exist filter exception
     * @throws NotSpecifiedCriteriaException
     *             Not specified scope exception
     * @throws EmptyResultFieldException
     *             Empty result field exception
     * @throws NotSpecifiedScopeException
     *             Not specified criteria exception
     */
    public UiFilterTableResult executeFilterLinkCreation(
            final UiSession pSession, final String pValuesContainerId,
            final String pLinkTypeName) throws NotExistFilterException,
        NotSpecifiedScopeException, EmptyResultFieldException,
        NotSpecifiedCriteriaException {

        // Clear the popup filter cache
        clearCache(pSession, FilterType.OTHER);

        CacheableValuesContainer lValuesContainer =
                getFieldsContainerService().getValuesContainer(
                        pSession.getRoleToken(), pValuesContainerId,
                        CacheProperties.IMMUTABLE);

        String lFilterName =
                getLinkFilterName(pSession, pLinkTypeName,
                        lValuesContainer.getTypeName());

        ExecutableFilterData lExecutableFilterData =
                getExecutableFilterDataByName(pSession, lFilterName
                        + FILTER_LINK_CREATION_SUFFIX);

        // If not specific creation link filter defined, try without creation suffix 
        if (lExecutableFilterData == null) {
            lExecutableFilterData =
                    getExecutableFilterDataByName(pSession, lFilterName);
        }

        final UiFilter lFilter = convertFilter(pSession, lExecutableFilterData);

        // Check filter
        checkFilter(lFilter);

        return executeFilterLinkCreation(pSession, pValuesContainerId,
                pLinkTypeName, lFilter, lExecutableFilterData);
    }

    /**
     * Execute a filter for the link deletion
     * 
     * @param pSession
     *            the session
     * @param pValuesContainerId
     *            the current container identifier
     * @param pLinkTypeName
     *            the link type name
     * @param pFilter
     *            the filter
     * @return the filter result
     * @throws NotExistFilterException
     *             Not exist filter exception
     * @throws NotSpecifiedCriteriaException
     *             Not specified scope exception
     * @throws EmptyResultFieldException
     *             Empty result field exception
     * @throws NotSpecifiedScopeException
     *             Not specified criteria exception
     */
    public UiFilterTableResult executeFilterLinkCreation(
            final UiSession pSession, final String pValuesContainerId,
            final String pLinkTypeName, final UiFilter pFilter)
        throws NotExistFilterException, NotSpecifiedScopeException,
        EmptyResultFieldException, NotSpecifiedCriteriaException {

        // Merge between filter in cache or in base and pFilter
        UiFilter lFilter =
                mergeFilters(getFilter(pSession, pFilter.getId()), pFilter);

        // Add filter in cache
        addToCache(pSession, FilterType.OTHER, lFilter);

        // Check filter
        checkFilter(lFilter);

        return executeFilterLinkCreation(pSession, pValuesContainerId,
                pLinkTypeName, lFilter, convertFilter(pSession, lFilter));
    }

    /**
     * Execute a filter for the link creation
     * 
     * @param pSession
     *            the session
     * @param pValuesContainerId
     *            the current container identifier
     * @param pLinkTypeName
     *            the link type name
     * @param pFilter
     *            the UI filter
     * @param pExecutableFilterData
     *            the business filter
     * @return the filter result
     */
    private UiFilterTableResult executeFilterLinkCreation(
            final UiSession pSession, final String pValuesContainerId,
            final String pLinkTypeName, final UiFilter pFilter,
            final ExecutableFilterData pExecutableFilterData) {

        // Execute the filter
        FilterResultIterator<SummaryData> lFilterResultIterator =
                executeFilter(pSession, pExecutableFilterData);
        UiFilterTableResult lFilterResult;

        try {
            lFilterResult =
                    convertFilterTableResult(pSession, lFilterResultIterator,
                            pFilter.getResultFields(), pFilter.getId());
        }
        finally {
            lFilterResultIterator.close();
        }

        // remove already linked container
        List<String> lLinkedElementIds =
                getLinkService().getLinkedElementsIds(pValuesContainerId,
                        pLinkTypeName);
        lLinkedElementIds.add(pValuesContainerId);
        for (int i = lFilterResult.getResultValues().size() - 1; i >= 0; i--) {
            if (lLinkedElementIds.contains(lFilterResult.getResultValues().get(
                    i).getFilterResultId().getId())) {
                lFilterResult.getResultValues().remove(i);
            }
        }

        return lFilterResult;
    }

    /**
     * Execute a filter for the link deletion
     * 
     * @param pSession
     *            the session
     * @param pValuesContainerId
     *            the current container identifier
     * @param pLinkTypeName
     *            the link type name
     * @return the filter result
     * @throws NotExistFilterException
     *             Not exist filter exception
     * @throws NotSpecifiedCriteriaException
     *             Not specified scope exception
     * @throws EmptyResultFieldException
     *             Empty result field exception
     * @throws NotSpecifiedScopeException
     *             Not specified criteria exception
     */
    public UiFilterTableResult executeFilterLinkDeletion(
            final UiSession pSession, final String pValuesContainerId,
            final String pLinkTypeName) throws NotExistFilterException,
        NotSpecifiedScopeException, EmptyResultFieldException,
        NotSpecifiedCriteriaException {

        // Clear the popup filter cache
        clearCache(pSession, FilterType.OTHER);

        CacheableValuesContainer lValuesContainer =
                getFieldsContainerService().getValuesContainer(
                        pSession.getRoleToken(), pValuesContainerId,
                        CacheProperties.IMMUTABLE);

        String lFilterName =
                getLinkFilterName(pSession, pLinkTypeName,
                        lValuesContainer.getTypeName());

        ExecutableFilterData lExecutableFilterData =
                getExecutableFilterDataByName(pSession, lFilterName
                        + FILTER_LINK_DELETION_SUFFIX);

        // If not specific deletion link filter defined, try without deletion suffix 
        if (lExecutableFilterData == null) {
            lExecutableFilterData =
                    getExecutableFilterDataByName(pSession, lFilterName);
        }

        final UiFilter lFilter = convertFilter(pSession, lExecutableFilterData);

        // Check filter
        checkFilter(lFilter);

        return executeFilterLinkDeletion(pSession, pValuesContainerId,
                pLinkTypeName, lFilter, lExecutableFilterData);
    }

    /**
     * Execute a filter for the link deletion
     * 
     * @param pSession
     *            the session
     * @param pValuesContainerId
     *            the current container identifier
     * @param pLinkTypeName
     *            the link type name
     * @param pFilter
     *            the filter
     * @return the filter result
     * @throws NotExistFilterException
     *             Not exist filter exception
     * @throws NotSpecifiedCriteriaException
     *             Not specified scope exception
     * @throws EmptyResultFieldException
     *             Empty result field exception
     * @throws NotSpecifiedScopeException
     *             Not specified criteria exception
     */
    public UiFilterTableResult executeFilterLinkDeletion(
            final UiSession pSession, final String pValuesContainerId,
            final String pLinkTypeName, final UiFilter pFilter)
        throws NotExistFilterException, NotSpecifiedScopeException,
        EmptyResultFieldException, NotSpecifiedCriteriaException {

        // Merge between filter in cache or in base and pFilter
        UiFilter lFilter =
                mergeFilters(getFilter(pSession, pFilter.getId()), pFilter);

        // Add filter in cache
        addToCache(pSession, FilterType.OTHER, lFilter);

        // Check filter
        checkFilter(lFilter);

        return executeFilterLinkDeletion(pSession, pValuesContainerId,
                pLinkTypeName, pFilter, convertFilter(pSession, lFilter));
    }

    /**
     * Execute a filter for the link deletion
     * 
     * @param pSession
     *            the session
     * @param pValuesContainerId
     *            the current container identifier
     * @param pLinkTypeName
     *            the link type name
     * @param pFilter
     *            the UI filter
     * @param pExecutableFilterData
     *            the business filter
     * @return the filter result
     */
    private UiFilterTableResult executeFilterLinkDeletion(
            final UiSession pSession, final String pValuesContainerId,
            final String pLinkTypeName, final UiFilter pFilter,
            ExecutableFilterData pExecutableFilterData) {

        // Execute the filter
        FilterResultIterator<SummaryData> lFilterResultIterator =
                executeFilter(pSession, pExecutableFilterData);
        UiFilterTableResult lFilterResult;

        try {
            lFilterResult =
                    convertFilterTableResult(pSession, lFilterResultIterator,
                            pFilter.getResultFields(), pFilter.getId());
        }
        finally {
            lFilterResultIterator.close();
        }

        // remove already linked container
        List<String> lLinkedElementIds =
                getLinkService().getLinkedElementsIds(pValuesContainerId,
                        pLinkTypeName);
        for (int i = lFilterResult.getResultValues().size() - 1; i >= 0; i--) {
            if (!lLinkedElementIds.contains(lFilterResult.getResultValues().get(
                    i).getFilterResultId().getId())) {
                lFilterResult.getResultValues().remove(i);
            }
        }

        return lFilterResult;
    }

    /**
     * Execute a filter for the sheet initialization
     * 
     * @param pSession
     *            the session
     * @param pSheetId
     *            the current sheet identifier
     * @param pSheetTypeName
     *            the destination sheet type name
     * @return the filter result
     * @throws NotExistFilterException
     *             Not exist filter exception
     * @throws NotSpecifiedCriteriaException
     *             Not specified scope exception
     * @throws EmptyResultFieldException
     *             Empty result field exception
     * @throws NotSpecifiedScopeException
     *             Not specified criteria exception
     */
    public UiFilterTableResult executeFilterSheetInitialization(
            final UiSession pSession, final String pSheetId,
            final String pSheetTypeName) throws NotExistFilterException,
        NotSpecifiedScopeException, EmptyResultFieldException,
        NotSpecifiedCriteriaException {

        // Clear the popup filter cache
        clearCache(pSession, FilterType.OTHER);

        String lCurrentSheetTypeName =
                getUserCacheManager().getUserCache(pSession.getParent()).getSheetCache().get(
                        pSheetId).getTypeName();

        String lFilterName =
                lCurrentSheetTypeName + "_" + pSheetTypeName
                        + FILTER_INITIALIZATION_SUFFIX;

        ExecutableFilterData lExecutableFilterData =
                getExecutableFilterDataByName(pSession, lFilterName);

        final UiFilter lFilter = convertFilter(pSession, lExecutableFilterData);

        // Check filter
        checkFilter(lFilter);

        FilterResultIterator<SummaryData> lFilterResultIterator =
                executeFilter(pSession, lExecutableFilterData);

        try {
            return convertFilterTableResult(pSession, lFilterResultIterator,
                    lFilter.getResultFields(), lFilter.getId());
        }
        finally {
            lFilterResultIterator.close();
        }

    }

    /**
     * Execute a filter for the sheet initialization
     * 
     * @param pSession
     *            the session
     * @param pFilter
     *            the filter
     * @return the filter result
     * @throws NotExistFilterException
     *             Not exist filter exception
     * @throws NotSpecifiedCriteriaException
     *             Not specified scope exception
     * @throws EmptyResultFieldException
     *             Empty result field exception
     * @throws NotSpecifiedScopeException
     *             Not specified criteria exception
     */
    public UiFilterTableResult executeFilterSheetInitialization(
            final UiSession pSession, final UiFilter pFilter)
        throws NotExistFilterException, NotSpecifiedScopeException,
        EmptyResultFieldException, NotSpecifiedCriteriaException {

        // Merge between filter in cache or in base and pFilter
        UiFilter lFilter =
                mergeFilters(getFilter(pSession, pFilter.getId()), pFilter);

        // Add filter in cache
        addToCache(pSession, FilterType.OTHER, lFilter);

        // Check filter
        checkFilter(lFilter);

        // Execute the filter
        FilterResultIterator<SummaryData> lFilterResultIterator =
                executeFilter(pSession, convertFilter(pSession, lFilter));

        try {
            return convertFilterTableResult(pSession, lFilterResultIterator,
                    lFilter.getResultFields(), lFilter.getId());
        }
        finally {
            lFilterResultIterator.close();
        }
    }

    /**
     * Execute an existing filter table
     * 
     * @param pSession
     *            the session
     * @param pFilterId
     *            the filter identifier
     * @param pUseCache
     *            if method must use the filter cache
     * @return The filter table result
     * @throws NotExistFilterException
     *             Not exist filter exception
     * @throws NotSpecifiedCriteriaException
     *             Not specified scope exception
     * @throws EmptyResultFieldException
     *             Empty result field exception
     * @throws NotSpecifiedScopeException
     *             Not specified criteria exception
     */
    public UiFilterTableResult executeFilterTable(final UiSession pSession,
            final String pFilterId, final boolean pUseCache)
        throws NotExistFilterException, NotSpecifiedScopeException,
        EmptyResultFieldException, NotSpecifiedCriteriaException {

        long lTime = System.currentTimeMillis();
        
        UiFilter lFilter = null;
        ExecutableFilterData lExecutableFilterData = null;

        if (pUseCache) {
            lFilter = getFromCache(pSession, pFilterId);
            lExecutableFilterData = convertFilter(pSession, lFilter);
        }

        if (lFilter == null && lExecutableFilterData == null) {
            lExecutableFilterData =
                    getExecutableFilterData(pSession, pFilterId);
            lFilter = convertFilter(pSession, lExecutableFilterData);
        }

        // Check filter
        checkFilter(lFilter);

        FilterResultIterator<SummaryData> lFilterResultIterator =
                executeFilter(pSession, lExecutableFilterData);
        
        gpmLogger.mediumInfo(pSession.getParent().getLogin(), GPMActionLogConstants.FILTER_EXECUTION, 
                lExecutableFilterData.getLabelKey(), (System.currentTimeMillis() - lTime) + "");

        try {
            if (!pUseCache) {
                clearCache(pSession, lFilter.getFilterType());
            }

            return convertFilterTableResult(pSession, lFilterResultIterator,
                    lFilter.getResultFields(), lFilter.getId());
        }
        finally {
            lFilterResultIterator.close();
        }
    }

    /**
     * Execute an existing filter table (editing mode)
     * 
     * @param pSession
     *            the session
     * @param pFilter
     *            the filter
     * @return The filter table result
     * @throws NotExistFilterException
     *             Not exist filter exception
     * @throws NotSpecifiedCriteriaException
     *             Not specified scope exception
     * @throws EmptyResultFieldException
     *             Empty result field exception
     * @throws NotSpecifiedScopeException
     *             Not specified criteria exception
     */
    public UiFilterTableResult executeFilterTable(final UiSession pSession,
            final UiFilter pFilter) throws NotExistFilterException,
        NotSpecifiedScopeException, EmptyResultFieldException,
        NotSpecifiedCriteriaException {

        long lTime = System.currentTimeMillis();
        
        ExecutableFilterData lExecutableFilterData = null;
        // Merge between filter in cache or in base and pFilter
        UiFilter lFilter =
                mergeFilters(getFilter(pSession, pFilter.getId()), pFilter);

        // Add filter in cache
        addToCache(pSession, lFilter.getFilterType(), lFilter);

        // Check filter
        checkFilter(lFilter);

        lExecutableFilterData = convertFilter(pSession, lFilter);
        // Execute the filter
        FilterResultIterator<SummaryData> lFilterResultIterator =
                executeFilter(pSession, lExecutableFilterData);

        gpmLogger.mediumInfo(pSession.getParent().getLogin(), GPMActionLogConstants.FILTER_EXECUTION, 
                lExecutableFilterData.getLabelKey(), (System.currentTimeMillis() - lTime) + "");
        
        try {
            return convertFilterTableResult(pSession, lFilterResultIterator,
                    lFilter.getResultFields(), lFilter.getId());
        }
        finally {
            lFilterResultIterator.close();
        }
    }

    /**
     * Execute a filter tree
     * 
     * @param pSession
     *            the session
     * @param pFilterId
     *            the filter identifier
     * @return The filter tree result
     * @throws NotExistFilterException
     *             Not exist filter exception
     * @throws NotSpecifiedCriteriaException
     *             Not specified scope exception
     * @throws EmptyResultFieldException
     *             Empty result field exception
     * @throws NotSpecifiedScopeException
     *             Not specified criteria exception
     */
    public UiFilterTreeResult executeFilterTree(final UiSession pSession,
            final String pFilterId) throws NotExistFilterException,
        NotSpecifiedScopeException, EmptyResultFieldException,
        NotSpecifiedCriteriaException {

        final ExecutableFilterData lExecutableFilterData =
                getExecutableFilterData(pSession, pFilterId);

        final UiFilter lFilter = convertFilter(pSession, lExecutableFilterData);

        // Check filter
        checkFilter(lFilter);

        FilterResultIterator<SummaryData> lFilterResultIterator =
                executeFilter(pSession, lExecutableFilterData);

        try {
            clearCache(pSession, lFilter.getFilterType());

            UiFilterTreeResult lFilterTreeResult =
                    convertFilterTreeResult(pSession, lFilterResultIterator,
                            lFilter.getResultFields());

            lFilterTreeResult.setEditable(lExecutableFilterData.isEditable());

            return lFilterTreeResult;
        }
        finally {
            lFilterResultIterator.close();
        }
    }

    /**
     * Get usable products.
     * 
     * @param pSession
     *            Current user session.
     * @return List of product names.
     */
    public List<UiFilterScope> getAvailableProductScope(final UiSession pSession) {

        List<String> lProducts =
                FacadeLocator.instance().getAuthorizationFacade().getAvailableProducts(
                        pSession.getParent());
        List<UiFilterScope> lResult = new ArrayList<UiFilterScope>();
        I18nFacade lI18nFacade = FacadeLocator.instance().getI18nFacade();
        for (String lProduct : lProducts) {
            lResult.add(new UiFilterScope(lI18nFacade.getTranslation(
                    pSession.getParent(), lProduct), false));
        }
        lResult.add(new UiFilterScope(lI18nFacade.getTranslation(
                pSession.getParent(), UsableFieldsManager.CURRENT_PRODUCT),
                false));
        lResult.add(new UiFilterScope(lI18nFacade.getTranslation(
                pSession.getParent(), UsableFieldsManager.NOT_SPECIFIED), false));
        return lResult;
    }

    /**
     * Get available visibilities for a filter.
     * 
     * @param pSession Current user session.
     * @param pFilterType the filter type.
     * @return List of visibilities.
     */
    public List<UiFilterVisibility> getAvailableVisibilities(
            final UiSession pSession, FilterType pFilterType) {
        List<FilterScope> lScopes =
                getAuthorizationService().getEditableFilterScope(
                        pSession.getRoleToken());
        List<UiFilterVisibility> lResult = new ArrayList<UiFilterVisibility>();
        for (FilterScope lFilterScope : lScopes) {
            UiFilterVisibility lFilterVisibility = null;
            switch (lFilterScope) {
                case USER_FILTER:
                    lFilterVisibility = UiFilterVisibility.USER;
                    break;
                case PRODUCT_FILTER:
                    if (FilterType.PRODUCT.equals(pFilterType)) {
                    	continue;
                    }
                    lFilterVisibility = UiFilterVisibility.PRODUCT;
                    break;
                default:
                    lFilterVisibility = UiFilterVisibility.INSTANCE;
                    break;
            }
            lResult.add(lFilterVisibility);
        }
        return lResult;
    }

    /**
     * Get category values.
     * 
     * @param pSession
     *            Current user session.
     * @param pCategoryName
     *            Category name.
     * @param pProductScope
     *            Filter scopes.
     * @return List of category values.
     */
    public List<String> getCategoryValues(final UiSession pSession,
            final String pCategoryName, List<UiFilterScope> pProductScope) {

        List<String> lEnvironmentList = new ArrayList<String>();

        List<String> lResultList = new ArrayList<String>();
        lResultList.add(UsableFieldsManager.NOT_SPECIFIED);

        if (pProductScope == null
                || pProductScope.isEmpty()
                || (pProductScope.size() == 1 && UsableFieldsManager.NOT_SPECIFIED.equals(pProductScope.get(
                        0).getProductName().getValue()))) {
            lResultList.addAll(FacadeLocator.instance().getDictionaryFacade().getDictionaryCategoryValues(
                    pSession, pCategoryName));
            return lResultList;
        }

        for (UiFilterScope lFilterScope : pProductScope) {

            String lProductName = null;

            if (UsableFieldsManager.CURRENT_PRODUCT.equals(lFilterScope.getProductName().getValue())) {
                lProductName = pSession.getProductName();
            }
            else if (!UsableFieldsManager.NOT_SPECIFIED.equals(lFilterScope.getProductName().getValue())) {
                lProductName = lFilterScope.getProductName().getValue();
            }

            if (lProductName != null) {
                String lProductId =
                        getProductService().getProductId(
                                pSession.getRoleToken(), lProductName);
                CacheableProduct lProduct =
                        getProductService().getCacheableProduct(
                                pSession.getRoleToken(), lProductId,
                                CacheProperties.IMMUTABLE);

                lEnvironmentList.addAll(lProduct.getEnvironmentNames());
            }
        }

        List<String> lCategoryList = new ArrayList<String>();
        lCategoryList.add(pCategoryName);

        Map<String, List<CategoryValue>> lCategoryValues =
                getEnvironmentService().getCategoryValues(
                        pSession.getRoleToken(),
                        pSession.getParent().getProcessName(),
                        lEnvironmentList, lCategoryList);
        if (lCategoryValues != null
                && lCategoryValues.containsKey(pCategoryName)) {
            for (CategoryValue lCategoryValue : lCategoryValues.get(pCategoryName)) {
                lResultList.add(lCategoryValue.getValue());
            }
        }
        return lResultList;
    }

    private List<String> getContainerIds(UiSession pSession,
            List<UiFilterFieldName> pFieldName,
            List<UiFilterContainerType> pContainerTypes) {
        List<String> lIdList = new ArrayList<String>();
        int i = 0;
        boolean lFound = false;
        while (i < pFieldName.size() && !lFound) {
            UiFilterFieldNameType lType = pFieldName.get(i).getType();
            switch (lType) {
                case FIELD:
                case MULTIPLE:
                    lFound = true;
                    if (i == 0) {
                        // Take root container(s)
                        for (UiFilterContainerType lContainerType : pContainerTypes) {
                            lIdList.add(lContainerType.getId());
                        }
                    }
                    else {
                        lIdList.add(getFieldsContainerService().getFieldsContainerId(
                                pSession.getRoleToken(),
                                pFieldName.get(i - 1).getName()));
                    }
                    break;
                default:
                    i++;
                    break;
            }
        }
        return lIdList;
    }

    /**
     * get an executable filter
     * 
     * @param pSession
     *            the session
     * @param pFilterId
     *            the filter identifier
     * @return the executable filter
     */
    public ExecutableFilterData getExecutableFilter(final UiSession pSession,
            final String pFilterId) {
        UiFilter lFilter = getFromCache(pSession, pFilterId);
        ExecutableFilterData lExecutableFilter;
        if (lFilter != null) {
            lExecutableFilter = convertFilter(pSession, lFilter);
        }
        else {
            lExecutableFilter = getExecutableFilterData(pSession, pFilterId);
        }
        return lExecutableFilter;
    }

    /**
     * get executable filter data from business
     * 
     * @param pSession
     *            the session
     * @param pFilterId
     *            the filter identifier
     * @return the executable filter data from business
     */
    private ExecutableFilterData getExecutableFilterData(
            final UiSession pSession, final String pFilterId) {
        if (pFilterId != null) {
            return getSearchService().getExecutableFilter(pSession.getRoleToken(), pFilterId);
        }
        return null;
    }

    /**
     * get executable filter data from business by its name.
     * <p>
     * By preference, a user filter will be returned, if none succeeds, a
     * product filter, and in the last case an instance filter
     * </p>
     * 
     * @param pSession
     *            the session
     * @param pFilterName
     *            the filter name
     * @return the executable filter data from business
     */
    private ExecutableFilterData getExecutableFilterDataByName(
            final UiSession pSession, final String pFilterName) {

        // Try to get user filter
        ExecutableFilterData lExecutableFilterData =
                getSearchService().getExecutableFilterByName(
                        pSession.getRoleToken(),
                        pSession.getParent().getProcessName(),
                        pSession.getProductName(),
                        pSession.getParent().getLogin(), pFilterName);

        // Try to get product filter
        if (lExecutableFilterData == null) {
            lExecutableFilterData =
                    getSearchService().getExecutableFilterByName(
                            pSession.getRoleToken(),
                            pSession.getParent().getProcessName(),
                            pSession.getProductName(), null, pFilterName);
        }

        // Try to get instance filter
        if (lExecutableFilterData == null) {
            lExecutableFilterData =
                    getSearchService().getExecutableFilterByName(
                            pSession.getRoleToken(),
                            pSession.getParent().getProcessName(), null, null,
                            pFilterName);
        }

        return lExecutableFilterData;
    }

    /**
     * get the label defined by the user or the field name translation of a
     * filter field result
     * 
     * @param pSession
     *            the session
     * @param pResultField
     *            the filter field result
     * @return the field label
     */
    private String getFieldResultLabel(final UiSession pSession,
            final UiFilterResultField pResultField) {
        String lLabel = pResultField.getLabel();
        if (lLabel == null) {
            lLabel = pResultField.getName().getLast().getName();
            lLabel =
                    getI18nService().getValueForUser(pSession.getRoleToken(),
                            lLabel);
        }
        return lLabel;
    }

    /**
     * get a filter
     * 
     * @param pSession
     *            the session
     * @param pFilterId
     *            the filter identifier
     * @return the filter
     */
    public UiFilter getFilter(final UiSession pSession, final String pFilterId) {
        UiFilter lFilter = null;
        if (pFilterId != null) {
            lFilter = getFromCache(pSession, pFilterId);
            if (lFilter == null) {
                lFilter =
                        convertFilter(pSession, getExecutableFilterData(
                                pSession, pFilterId));
            }
        }
        return lFilter;
    }

    /**
     * get the filter field name for UI by a usable field from business
     * 
     * @param pUsableFieldData
     *            the usable field from business
     * @return the filter field name for UI
     */
    private LinkedList<UiFilterFieldName> getFilterFieldName(
            I18nTranslationManager pTranslationManager,
            final UsableFieldData pUsableFieldData) {
        final LinkedList<UiFilterFieldName> lFilterFieldName =
                new LinkedList<UiFilterFieldName>();

        // type hierarchy
        for (FilterFieldsContainerInfo lContainerInfo : pUsableFieldData.getFieldsContainerHierarchy()) {
            lFilterFieldName.add(new UiFilterFieldName(
                    lContainerInfo.getLabelKey(),
                    pTranslationManager.getTextTranslation(lContainerInfo.getLabelKey()),
                    UiFilterFieldNameType.valueOf(lContainerInfo.getType().toString())));
        }
        // multiple field
        String lMultipleField = pUsableFieldData.getMultipleField();
        if (lMultipleField != null) {
            int lSplitIndex = lMultipleField.lastIndexOf("|");
            String lMultipleFieldName;
            if (lSplitIndex == -1) {
                lMultipleFieldName = lMultipleField;
            }
            else {
                lMultipleFieldName = lMultipleField.substring(lSplitIndex + 1);
            }
            lFilterFieldName.add(new UiFilterFieldName(lMultipleFieldName,
                    pTranslationManager.getTextTranslation(lMultipleFieldName),
                    UiFilterFieldNameType.MULTIPLE));
        }

        // field
        lFilterFieldName.add(new UiFilterFieldName(
                pUsableFieldData.getFieldName(),
                pTranslationManager.getTextTranslation(pUsableFieldData.getFieldName()),
                UiFilterFieldNameType.FIELD));

        return lFilterFieldName;
    }

    /**
     * get all filter summaries
     * 
     * @param pSession
     *            the session
     * @param pFilterType
     *            the filter type
     * @return the filter summaries
     */
    public UiFilterSummaries getFilters(final UiSession pSession,
            final FilterType pFilterType) {

        // Create filter summaries
        UiFilterSummaries lFilterSummaries =
                new UiFilterSummaries(new ArrayList<UiFilterSummary>(),
                        new ArrayList<UiFilterSummary>(),
                        new ArrayList<UiFilterSummary>(),
                        new ArrayList<UiFilterSummary>());

        // get filters
        AuthorizationService lAuthorisationService =
                ServiceLocator.instance().getAuthorizationService();
        String lLogin = pSession.getParent().getLogin();
        if (!lAuthorisationService.isLoginCaseSensitive()) {
            lLogin =
                    lAuthorisationService.getLoggedUserData(
                            pSession.getRoleToken()).getLogin();
        }
        Collection<ExecutableFilterData> lFilters =
                getSearchService().getVisibleExecutableFilter(
                        pSession.getRoleToken(),
                        new FilterVisibilityConstraintData(lLogin,
                                pSession.getParent().getProcessName(),
                                pSession.getProductName()),
                        FilterTypeData.fromString(pFilterType.toString()), null);

        // Get translation manager
        I18nTranslationManager lTranslationManager =
                FacadeLocator.instance().getI18nFacade().getTranslationManager(
                        pSession.getParent().getLanguage());

        boolean lFiltersEditable = true;
        boolean lFiltersDeletable = true;
        if (FilterType.PRODUCT.equals(pFilterType)) {
            lFiltersEditable =
                    FacadeLocator.instance().getAuthorizationFacade().hasAnyAdminAccess(
                            pSession,
                            AdministrationAction.PRODUCT_SEARCH_NEW_EDIT);
            lFiltersDeletable =
                    FacadeLocator.instance().getAuthorizationFacade().hasAnyAdminAccess(
                            pSession,
                            AdministrationAction.PRODUCT_SEARCH_DELETE);
        }

        for (ExecutableFilterData lFilter : lFilters) {
            boolean lFilterEditable = lFiltersEditable && lFilter.isEditable();
            boolean lFilterDeletable = lFiltersDeletable && lFilter.isEditable();

            // Return only executable or editable filters
            if (lFilter.isExecutable() || lFilterEditable || lFilterDeletable) {
                UiFilterUsage lUsage = UiFilterUsage.valueOf(lFilter.getUsage());

                // Create filter summary
                UiFilterSummary lFilterSummary =
                        new UiFilterSummary(
                                lFilter.getId(),
                                lTranslationManager.getTextTranslation(StringEscapeUtils.escapeHtml(lFilter.getLabelKey())),
                                lTranslationManager.getTextTranslation(lFilter.getDescription()),
                                getFilterVisibility(lFilter.getFilterVisibilityConstraintData()),
                                lFilterEditable, lFilter.isExecutable(),
                                lFilterDeletable);

                // Add table filter in filter summaries
                if (UiFilterUsage.TABLE_VIEW.equals(lUsage)
                        || UiFilterUsage.BOTH_VIEWS.equals(lUsage)) {
                    switch (lFilterSummary.getVisibility()) {
                        case INSTANCE:
                            lFilterSummaries.getTableProcessFilters().add(
                                    lFilterSummary);
                            break;
                        case PRODUCT:
                            lFilterSummaries.getTableProductFilters().add(
                                    lFilterSummary);
                            break;
                        default:
                            lFilterSummaries.getTableUserFilters().add(
                                    lFilterSummary);
                            break;
                    }
                }

                // Add tree filter in filter summaries
                if (UiFilterUsage.TREE_VIEW.equals(lUsage)
                        || UiFilterUsage.BOTH_VIEWS.equals(lUsage)) {
                    lFilterSummaries.getTreeFilters().add(lFilterSummary);
                }
            }
        }

        return lFilterSummaries;
    }

    /**
     * get a result node by the value
     * 
     * @param pParentNode
     *            the result node parent
     * @param pNodeValue
     *            the child result node value to find
     * @return the result node
     */
    private UiFilterTreeResultNode getFilterTreeResultNode(
            final AbstractUiFilterTreeResultNode pParentNode,
            final String pNodeValue) {
        for (UiFilterTreeResultNode lResultNode : pParentNode.getResultNodes()) {
            if ((pNodeValue != null && pNodeValue.equals(lResultNode.getValue()))
                    || ((pNodeValue == null || " ".equals(pNodeValue)) && (lResultNode.getValue() == null || " ".equals(lResultNode.getValue())))) {
                return lResultNode;
            }
        }
        return null;
    }

    /**
     * get the filter visibility from business constraints
     * 
     * @param pFilterVisibilityConstraintData
     *            the business constraints
     * @return the filter visibility
     */
    private UiFilterVisibility getFilterVisibility(
            final FilterVisibilityConstraintData pFilterVisibilityConstraintData) {
        if (pFilterVisibilityConstraintData.getUserLogin() != null) {
            return UiFilterVisibility.USER;
        }
        else if (pFilterVisibilityConstraintData.getProductName() != null) {
            return UiFilterVisibility.PRODUCT;
        }
        else {
            return UiFilterVisibility.INSTANCE;
        }
    }

    /**
     * Get a filter from cache
     * 
     * @param pSession
     *            the session
     * @param pFilterId
     *            the filter id
     * @return filter from cache
     */
    private UiFilter getFromCache(final UiSession pSession,
            final String pFilterId) {
        FilterCache lFilterCache =
                getUserCacheManager().getUserCache(pSession.getParent()).getFilterCache();
        return lFilterCache.getFilter(pSession.getProductName(), pFilterId);
    }

    /**
     * Get container hierarchy.
     * 
     * @param pSession
     *            Current user session.
     * @param pContainerId
     *            Selected container identifier.
     * @param pMaxFieldDepth
     *            Max field depth.
     * @param pAlreadyGetContainers
     *            Container of the hierarchy already recovered.
     * @param pFilterType
     *            Filter type.
     */
    private void getHierarchy(
            final UiSession pSession,
            I18nTranslationManager pTranslationManager,
            final String pContainerId,
            final int pMaxFieldDepth,
            final Map<String, UiFilterContainerHierarchy> pAlreadyGetContainers,
            final FilterType pFilterType) {

        if (!(pAlreadyGetContainers.keySet().contains(pContainerId) && pAlreadyGetContainers.get(
                pContainerId).getChildren().size() != 0)
                && pMaxFieldDepth >= 0) {
            UiFilterContainerHierarchy lFilterContainerHierarchy =
                    new UiFilterContainerHierarchy();
            lFilterContainerHierarchy.setContainerId(pContainerId);
            String lContainerName = null;
            if (FilterType.SHEET.equals(pFilterType)) {
                lContainerName =
                        getSheetService().getCacheableSheetType(
                                pSession.getRoleToken(), pContainerId,
                                CacheProperties.IMMUTABLE).getName();
                lFilterContainerHierarchy.setContainerType(UiFilterFieldNameType.SHEET);
            }
            else if (FilterType.PRODUCT.equals(pFilterType)) {
                lContainerName =
                        getProductService().getCacheableProductType(
                                pSession.getRoleToken(), pContainerId,
                                CacheProperties.IMMUTABLE).getName();
                lFilterContainerHierarchy.setContainerType(UiFilterFieldNameType.PRODUCT);
            }
            lFilterContainerHierarchy.setContainerName(lContainerName);
            lFilterContainerHierarchy.setContainerTranslatedName(pTranslationManager.getTextTranslation(lContainerName));

            if (pMaxFieldDepth > 0) {

                String lAdminRoleToken =
                        getAuthorizationService().getAdminRoleToken(
                                pSession.getParent().getProcessName());

                List<CacheableLinkType> lCacheableLinkTypes =
                        getLinkService().getLinkTypes(lAdminRoleToken,
                                pContainerId);

                for (CacheableLinkType lCacheableLinkType : lCacheableLinkTypes) {
                    if (getAuthorizationService().getFilterAccessControl(
                            pSession.getRoleToken(), lCacheableLinkType.getId()).getEditable()) {
                        String lId = lCacheableLinkType.getId();
                        if (!pAlreadyGetContainers.keySet().contains(lId)) {
                            String lName = lCacheableLinkType.getName();
                            pAlreadyGetContainers.put(
                                    lCacheableLinkType.getId(),
                                    new UiFilterContainerHierarchy(
                                            lId,
                                            lName,
                                            pTranslationManager.getTextTranslation(lName),
                                            null, UiFilterFieldNameType.LINK));
                        }
                        String lDestinationId =
                                lCacheableLinkType.getDestinationTypeId();
                        if (pContainerId.equals(lDestinationId)
                                && !pContainerId.equals(lCacheableLinkType.getOriginTypeId())) {
                            lDestinationId =
                                    lCacheableLinkType.getOriginTypeId();
                        }
                        String[] lChild = new String[] { lId, lDestinationId };
                        if (!lFilterContainerHierarchy.getChildren().contains(
                                lChild)) {
                            lFilterContainerHierarchy.getChildren().add(lChild);
                        }
                        getHierarchy(pSession, pTranslationManager,
                                lDestinationId, pMaxFieldDepth - 1,
                                pAlreadyGetContainers, pFilterType);
                    }
                }
            }

            pAlreadyGetContainers.put(pContainerId, lFilterContainerHierarchy);
        }
    }

    /**
     * Get container hierarchy.
     * 
     * @param pSession
     *            Current user session.
     * @param pContainerIds
     *            Selected container Ids.
     * @param pFilterType
     *            Filter type.
     * @return Containers hierarchy.
     */
    public Map<String, UiFilterContainerHierarchy> getHierarchyContainers(
            final UiSession pSession,
            final List<UiFilterContainerType> pContainerIds,
            final FilterType pFilterType) {

        I18nTranslationManager lTranslationManager =
                FacadeLocator.instance().getI18nFacade().getTranslationManager(
                        pSession.getParent().getLanguage());

        Map<String, UiFilterContainerHierarchy> lHierarchyMap =
                new HashMap<String, UiFilterContainerHierarchy>();

        if (pContainerIds.size() > 1) {
            for (UiFilterContainerType lFilterContainerType : pContainerIds) {
                getHierarchy(pSession, lTranslationManager,
                        lFilterContainerType.getId(), 0, lHierarchyMap,
                        pFilterType);
            }
        }
        else {
            int lMaxFieldsDepth = getSearchService().getMaxFieldsDepth();

            getHierarchy(pSession, lTranslationManager,
                    pContainerIds.get(0).getId(), lMaxFieldsDepth,
                    lHierarchyMap, pFilterType);
        }

        // Add product types in hierarchy
        if (FilterType.SHEET.equals(pFilterType)) {
            List<CacheableFieldsContainer> lProductTypes =
                    getFieldsContainerService().getFieldsContainer(
                            pSession.getRoleToken(), ProductType.class,
                            FieldsContainerService.NOT_CONFIDENTIAL);

            for (CacheableFieldsContainer lCacheableFieldsContainer : lProductTypes) {
                UiFilterContainerHierarchy lProductTypeHierarchy =
                        new UiFilterContainerHierarchy(
                                lCacheableFieldsContainer.getId(),
                                lCacheableFieldsContainer.getName(),
                                lTranslationManager.getTextTranslation(lCacheableFieldsContainer.getName()),
                                null, UiFilterFieldNameType.PRODUCT);
                for (UiFilterContainerHierarchy lContainerHierarchy : lHierarchyMap.values()) {
                    if (lContainerHierarchy.getChildren() != null) {
                        lContainerHierarchy.getChildren().add(
                                new String[] {
                                              lProductTypeHierarchy.getContainerId(),
                                              null });
                    }
                }
                lHierarchyMap.put(lProductTypeHierarchy.getContainerId(),
                        lProductTypeHierarchy);
            }
        }

        return lHierarchyMap;
    }

    /**
     * get the filter name used for link creation and deletion
     * 
     * @param pLinkTypeName
     *            the link type name
     * @param pCurrentContainerType
     *            the current container type
     * @return the filter name
     */
    private String getLinkFilterName(final UiSession pSession,
            final String pLinkTypeName, final String pCurrentContainerType) {
        CacheableLinkType lLinkType =
                getLinkService().getLinkTypeByName(pSession.getRoleToken(),
                        pLinkTypeName, CacheProperties.IMMUTABLE);

        String lOriginType = lLinkType.getOriginTypeName();
        String lDestinationType = lLinkType.getDestinationTypeName();
        if (lDestinationType.equals(pCurrentContainerType)) {
            lOriginType = lDestinationType;
            lDestinationType = lLinkType.getOriginTypeName();
        }

        return lLinkType.getName() + "_" + lOriginType + "_" + lDestinationType;
    }

    /**
     * get max fields depth.
     * 
     * @return max fields depth.
     */
    public int getMaxFieldsDepth() {
        return getSearchService().getMaxFieldsDepth();
    }

    /**
     * Get searcheable containers for given filter type.
     * 
     * @param pSession
     *            Current user session.
     * @param pFilterType
     *            Filter type Id.
     * @return List of searcheable container types.
     */
    public List<UiFilterContainerType> getSearcheableContainers(
            final UiSession pSession, final FilterType pFilterType) {

        Class<? extends FieldsContainer> lFilterClass = null;
        if (FilterType.PRODUCT.equals(pFilterType)) {
            lFilterClass = ProductType.class;
        }
        else if (FilterType.LINK.equals(pFilterType)) {
            lFilterClass = LinkType.class;
        }
        else if (FilterType.SHEET.equals(pFilterType)) {
            lFilterClass = SheetType.class;
        }
        
        long lNeedConfidential = 0;
        if (pFilterType != FilterType.PRODUCT) {
        	lNeedConfidential = FieldsContainerService.NOT_CONFIDENTIAL;
        }
        List<CacheableFieldsContainer> lFieldsContainers =
                getFieldsContainerService().getFieldsContainer(
                        pSession.getRoleToken(), lFilterClass, lNeedConfidential);

        List<UiFilterContainerType> lResult =
                new ArrayList<UiFilterContainerType>();
        I18nFacade lI18nFacade = FacadeLocator.instance().getI18nFacade();
        for (CacheableFieldsContainer lFieldsContainer : lFieldsContainers) {
            lResult.add(new UiFilterContainerType(lFieldsContainer.getId(),
                    lI18nFacade.getTranslation(pSession.getParent(),
                            lFieldsContainer.getName())));

        }
        return lResult;
    }

    /**
     * Get usable fields for the given container
     * 
     * @param pSession
     *            Current user session.
     * @param pContainerTypeId
     *            Container type Id.
     * @return List of usable fields.
     */
    public Map<String, UiFilterUsableField> getUsableFields(
            final UiSession pSession, final List<String> pContainerTypeId) {

        I18nTranslationManager lTranslationManager =
                FacadeLocator.instance().getI18nFacade().getTranslationManager(
                        pSession.getParent().getLanguage());

        //Get Maximum Fields Depth for restoration
        int lMaxFieldsDepth = getSearchService().getMaxFieldsDepth();

        LinkedHashMap<String, UsableFieldData> lUsableFields =
                new LinkedHashMap<String, UsableFieldData>(
                        getSearchService().getUsableFields(
                                pSession.getRoleToken(),
                                pContainerTypeId.toArray(new String[0]),
                                pSession.getParent().getProcessName()));

        LinkedHashMap<String, UiFilterUsableField> lResultMap =
                new LinkedHashMap<String, UiFilterUsableField>();

        for (String lFieldKey : lUsableFields.keySet()) {
            UsableFieldData lUsableFieldData = lUsableFields.get(lFieldKey);
            //Keep only first level of hierarchy
            if (lUsableFieldData.getFieldsContainerHierarchy().isEmpty()) {
                addUiFilterUsableField(lTranslationManager, lFieldKey,
                        lUsableFields, lResultMap);
            }
        }

        getSearchService().setMaxFieldsDepth(lMaxFieldsDepth);

        return lResultMap;
    }

    /**
     * Merge two filters
     * 
     * @param pOriginalFilter
     *            the original filter (from cache or database)
     * @param pUpdatedFilter
     *            the updated filter (from UI)
     * @return the filter merged
     */
    private UiFilter mergeFilters(final UiFilter pOriginalFilter,
            final UiFilter pUpdatedFilter) {
        // If the original filter is null (ex: at the filter creation),
        // return the updated filter
        if (pOriginalFilter == null) {
            return pUpdatedFilter;
        }

        UiFilter lFilter = new UiFilter();

        // Set not updatable properties
        lFilter.setFilterType(pOriginalFilter.getFilterType());
        lFilter.setId(pOriginalFilter.getId());
        lFilter.setBusinessFilterDataId(pOriginalFilter.getBusinessFilterDataId());

        // Set container types
        List<UiFilterContainerType> lContainerTypes =
                pOriginalFilter.getContainerTypes();
        if (pUpdatedFilter.getContainerTypes() != null) {
            lContainerTypes = pUpdatedFilter.getContainerTypes();
        }
        lFilter.setContainerTypes(lContainerTypes);

        // Set scopes
        List<UiFilterScope> lScopes = pOriginalFilter.getScopes();
        if (pUpdatedFilter.getScopes() != null) {
            lScopes = pUpdatedFilter.getScopes();
        }
        lFilter.setScopes(lScopes);

        // Set result fields
        List<UiFilterResultField> lResultFields =
                pOriginalFilter.getResultFields();
        if (pUpdatedFilter.getResultFields() != null) {
            lResultFields = pUpdatedFilter.getResultFields();
        }
        lFilter.setResultFields(lResultFields);

        // Set criteria
        List<UiFilterCriteriaGroup> lCriteriaGroups =
                pOriginalFilter.getCriteriaGroups();
        if (pUpdatedFilter.getCriteriaGroups() != null) {
            lCriteriaGroups = pUpdatedFilter.getCriteriaGroups();
        }
        //If CriteriaGroup is null and the reset is needed it means that 
        //the container list has changed and no criteria has been selected
        else if (pUpdatedFilter.isResetNeeded()) {
            //No criteria has been selected in the new filter
            lCriteriaGroups = new ArrayList<UiFilterCriteriaGroup>();
        }
        lFilter.setCriteriaGroups(lCriteriaGroups);

        // Set sorting fields
        List<UiFilterSortingField> lSortingFields =
                pOriginalFilter.getSortingFields();
        if (pUpdatedFilter.getSortingFields() != null) {
            lSortingFields = pUpdatedFilter.getSortingFields();
        }
        //If sortingFields is null and the reset is needed it means that 
        //the container list has changed and no criteria has been selected
        else if (pUpdatedFilter.isResetNeeded()) {
            //No sorting field has been selected in the new filter
            lSortingFields = new ArrayList<UiFilterSortingField>();
        }
        lFilter.setSortingFields(lSortingFields);

        // Set Name
        String lName = pOriginalFilter.getName();
        if (pUpdatedFilter.getName() != null) {
            lName = pUpdatedFilter.getName();
        }
        lFilter.setName(lName);

        // Set Description
        String lDescription = pOriginalFilter.getDescription();
        if (pUpdatedFilter.getDescription() != null) {
            lDescription = pUpdatedFilter.getDescription();
        }
        lFilter.setDescription(lDescription);

        // Set usage
        UiFilterUsage lUsage = pOriginalFilter.getUsage();
        if (pUpdatedFilter.getUsage() != null) {
            lUsage = pUpdatedFilter.getUsage();
        }
        lFilter.setUsage(lUsage);

        // Set visibility
        UiFilterVisibility lVisibility = pOriginalFilter.getVisibility();
        if (pUpdatedFilter.getVisibility() != null) {
            lVisibility = pUpdatedFilter.getVisibility();
        }
        lFilter.setVisibility(lVisibility);

        // Set Hidden properties
        Boolean lHidden = pOriginalFilter.getHidden();
        if (pUpdatedFilter.getHidden() != null) {
            lHidden = pUpdatedFilter.getHidden();
        }
        lFilter.setHidden(lHidden);

        // Set Editable access
        lFilter.setEditable(pOriginalFilter.isEditable()
                || pUpdatedFilter.isEditable());

        // Set category values
        Map<String, List<String>> lCategoryValues =
                pOriginalFilter.getCategoryValues();
        if (pUpdatedFilter.getCategoryValues() != null
                && !pUpdatedFilter.getCategoryValues().isEmpty()) {
            lCategoryValues = pUpdatedFilter.getCategoryValues();
        }
        lFilter.setCategoryValues(lCategoryValues);

        return lFilter;
    }

    /**
     * Create or Update filter.
     * 
     * @param pSession
     *            Current user session.
     * @param pFilter
     *            Filter to save.
     * @return Id of saved filter in database.
     */
    public String saveFilter(final UiSession pSession, final UiFilter pFilter) {

        String lFilterId = pFilter.getId();

        // Get filter from cache/DB and merge it with pFilter 
        UiFilter lFilter = mergeFilters(getFilter(pSession, lFilterId), pFilter);

        boolean lCreation = false;

        // If the business FilterData Id is null,
        // the filter not exist in DB so it's a creation
        if (lFilter.getBusinessFilterDataId() == null) {
            lCreation = true;
        }
        else {
            // Get business filter
        	ExecutableFilterData lExecutableFilterData = getExecutableFilterData(pSession, lFilterId);
        	// Create new filter when change name or visibility
        	if (!lFilter.getName().equals(lExecutableFilterData.getLabelKey())|| !lFilter.getVisibility().equals(
        			getFilterVisibility(lExecutableFilterData.getFilterVisibilityConstraintData()))) {
        		lCreation = true;
        	}
        }

        // Create filter
        if (lCreation) {
            lFilter.setId(null);
            lFilter.setBusinessFilterDataId(null);
            lFilterId = getSearchService().createExecutableFilter(
                            pSession.getRoleToken(),
                            convertFilter(pSession, lFilter));
        }
        
        // Update filter
        else {
            getSearchService().updateExecutableFilter(pSession.getRoleToken(), convertFilter(pSession, lFilter));
        }

        return lFilterId;
    }
}
