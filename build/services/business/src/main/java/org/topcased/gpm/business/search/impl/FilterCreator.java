/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Sophian Idjellidaine (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.search.impl;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.ServiceLocator;
import org.topcased.gpm.business.exception.InstantiateException;
import org.topcased.gpm.business.fields.FieldType;
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
import org.topcased.gpm.business.search.impl.fields.UsableFieldsManager;
import org.topcased.gpm.business.search.result.sorter.ResultSortingData;
import org.topcased.gpm.business.search.result.sorter.SortingFieldData;
import org.topcased.gpm.business.search.result.summary.ResultSummaryData;
import org.topcased.gpm.business.search.service.ExecutableFilterData;
import org.topcased.gpm.business.search.service.FilterProductScope;
import org.topcased.gpm.business.search.service.FilterVisibilityConstraintData;
import org.topcased.gpm.business.search.service.SearchService;
import org.topcased.gpm.business.search.service.UsableFieldData;
import org.topcased.gpm.business.serialization.data.CriteriaGroup;
import org.topcased.gpm.business.serialization.data.Criterion;
import org.topcased.gpm.business.serialization.data.FieldResult;
import org.topcased.gpm.business.serialization.data.Filter;
import org.topcased.gpm.business.serialization.data.LinkFilter;
import org.topcased.gpm.business.serialization.data.LinkTypeRef;
import org.topcased.gpm.business.serialization.data.NamedElement;
import org.topcased.gpm.business.serialization.data.ProductFilter;
import org.topcased.gpm.business.serialization.data.ProductScope;
import org.topcased.gpm.business.serialization.data.ProductTypeRef;
import org.topcased.gpm.business.serialization.data.SheetFilter;
import org.topcased.gpm.business.serialization.data.SheetTypeRef;
import org.topcased.gpm.domain.util.FieldsUtil;

/**
 * Helper class used to create a search filter
 * 
 * @author llatil
 */
public class FilterCreator {
    /**
     *
     */
    public static final String VIRTUAL_FIELD = "VIRTUAL_FIELD";

    /** Ascending sort attribute for a filter result */
    public static final String SORT_ASC = "asc";

    /** Descending sort attribute for a filter result */
    public static final String SORT_DESC = "desc";

    /** No sorting attribute for a filter result */
    public static final String SORT_NONE = "none";

    public static final String SORT_DEF_ASC = "def_asc";

    public static final String SORT_DEF_DESC = "def_desc";

    /** AND Criterium operator */
    public static final String AND = "and";

    /** OR Criterium operator */
    public static final String OR = "or";

    /** EQUAL Criterium operator */
    public static final String EQ = "=";

    public static final Operators OPERATORS = new Operators();

    private final Filter filter;

    private String[] containersId;

    private final String processName;

    private final ServiceLocator serviceLocator;

    private final String roleToken;

    private FilterVisibilityConstraintData visibilityData;

    private final SearchService searchService;

    private static final List<String> dateCriteria =
            Arrays.asList(UsableFieldsManager.CURRENT_WEEK,
                    UsableFieldsManager.PREVIOUS_WEEK,
                    UsableFieldsManager.CURRENT_MONTH,
                    UsableFieldsManager.PREVIOUS_MONTH);

    public FilterCreator(String pRoleToken, Filter pFilter,
            String pProcessName, ServiceLocator pServiceLocator) {
        filter = pFilter;
        processName = pProcessName;
        roleToken = pRoleToken;

        serviceLocator = pServiceLocator;
        searchService = serviceLocator.getSearchService();
    }

    public ExecutableFilterData createFilter() {
        checkFilter();

        // Create the fields container id array.
        containersId = createContainersId(filter.getContainers(), processName);

        visibilityData = createVisibilityConstraint();

        ResultSummaryData lResultSummaryData = createResultSummaryData();
        ResultSortingData lResultSortingData = createResultSortingData();
        FilterData lFilterData = createFilterData();

        ExecutableFilterData lExecFilterData = new ExecutableFilterData();
        lExecFilterData.setLabelKey(filter.getLabelKey());
        lExecFilterData.setDescription(filter.getDescription());
        lExecFilterData.setFilterVisibilityConstraintData(visibilityData);

        if (filter.getHidden() != null) {
            lExecFilterData.setHidden(filter.getHidden().booleanValue());
        }

        lExecFilterData.setResultSummaryData(lResultSummaryData);
        lExecFilterData.setResultSortingData(lResultSortingData);

        lExecFilterData.setFilterData(lFilterData);

        lExecFilterData.setFilterProductScopes(createProductScope());

        String lUsage = "BOTH_VIEWS";

        if (null != filter.getFilterUsage()) {
            String lFilterUsage = filter.getFilterUsage();
            if (lFilterUsage.equals("tree")) {
                lUsage = "TREE_VIEW";
            }
            else if (lFilterUsage.equals("table")) {
                lUsage = "TABLE_VIEW";
            }
        }
        lExecFilterData.setUsage(lUsage);

        return lExecFilterData;
    }

    private FilterVisibilityConstraintData createVisibilityConstraint() {
        FilterVisibilityConstraintData lVisibilityConstraint;
        lVisibilityConstraint = new FilterVisibilityConstraintData();
        lVisibilityConstraint.setBusinessProcessName(processName);
        lVisibilityConstraint.setUserLogin(filter.getUserLogin());
        lVisibilityConstraint.setProductName(filter.getProductName());

        return lVisibilityConstraint;
    }

    private void checkFilter() {
        if (StringUtils.isBlank(filter.getLabelKey())) {
            throw new InstantiateException("A filter must have a labelKey",
                    filter);
        }

        if (filter.getContainers() == null
                || filter.getContainers().size() == 0) {
            throw new InstantiateException(
                    "A filter must specify a containers list", filter);
        }
        if (filter.getHidden() != null
                && filter.getHidden().booleanValue()
                && (filter.getUserLogin() != null || filter.getProductName() != null)) {
            throw new InstantiateException(
                    "Only instance filters (not associated to a user or a product) "
                            + "can be defined as 'hidden'", filter);
        }
    }

    /**
     * Get the containers id
     * 
     * @param pContainers
     *            The container list to found
     * @param pProcessName
     *            The process name
     * @return Container ids tab
     */
    public String[] createContainersId(List<NamedElement> pContainers,
            String pProcessName) {
        final Class<? extends NamedElement> lTypeRefClass;

        // A filter type can only be applied on a specific type of container 
        if (filter instanceof SheetFilter) {
            lTypeRefClass = SheetTypeRef.class;
        }
        else if (filter instanceof LinkFilter) {
            lTypeRefClass = LinkTypeRef.class;
        }
        else if (filter instanceof ProductFilter) {
            lTypeRefClass = ProductTypeRef.class;
        }
        else {
            // For report, all container available
            lTypeRefClass = NamedElement.class;
        }

        final String[] lIds = new String[pContainers.size()];
        int i = 0;

        for (NamedElement lFieldContainer : pContainers) {
            if (!lTypeRefClass.isAssignableFrom(lFieldContainer.getClass())) {
                throw new InstantiateException("Filter type "
                        + filter.getClass().getName()
                        + " cannot be applied on container type "
                        + lFieldContainer.getClass().getName(), filter);
            }
            lIds[i] =
                    serviceLocator.getFieldsContainerService().getFieldsContainerId(
                            roleToken, lFieldContainer.getName());
            i++;
        }

        return lIds;
    }

    private ResultSummaryData createResultSummaryData() {
        ResultSummaryData lResultSummaryData = new ResultSummaryData();
        lResultSummaryData.setFilterVisibilityConstraintData(visibilityData);
        lResultSummaryData.setLabelKey(filter.getLabelKey());
        lResultSummaryData.setFieldsContainerIds(containersId);

        Collection<UsableFieldData> lResultsFieldsArray;
        lResultsFieldsArray = new ArrayList<UsableFieldData>();

        for (FieldResult lFieldResult : filter.getResultSummary()) {
            if (lFieldResult.getDisplayed() == null
                    || lFieldResult.getDisplayed().booleanValue()) {
                String lUsableFieldDataId =
                        searchService.getUsableFieldDataId(roleToken,
                                processName, lFieldResult.getName());
                UsableFieldData lUsableFieldData =
                        searchService.getUsableField(processName,
                                lUsableFieldDataId, Arrays.asList(containersId));
                if (null == lUsableFieldData) {
                    String lMsg =
                            MessageFormat.format(
                                    "Invalid field name ''{0}'' for results in filter ''{1}''",
                                    lFieldResult.getName(),
                                    filter.getLabelKey());
                    throw new InstantiateException(lMsg, lFieldResult);
                }

                if (lFieldResult.getLabel() != null) {
                    lUsableFieldData.setLabel(lFieldResult.getLabel());
                }
                lResultsFieldsArray.add(lUsableFieldData);
            }
        }

        UsableFieldData[] lResultsFieldsTab =
                new UsableFieldData[lResultsFieldsArray.size()];
        int i = 0;
        for (UsableFieldData lUsableFieldData : lResultsFieldsArray) {
            lResultsFieldsTab[i] = lUsableFieldData;
            i++;
        }
        lResultSummaryData.setUsableFieldDatas(lResultsFieldsTab);

        return lResultSummaryData;
    }

    private ResultSortingData createResultSortingData() {
        ResultSortingData lResultSortingData = new ResultSortingData();

        lResultSortingData.setLabelKey(filter.getLabelKey());
        lResultSortingData.setFieldsContainerIds(containersId);
        lResultSortingData.setFilterVisibilityConstraintData(visibilityData);

        SortingFieldData[] lSortingFieldsDataArray;
        lSortingFieldsDataArray = new SortingFieldData[0];

        for (FieldResult lFieldResult : filter.getResultSummary()) {
            if (lFieldResult.getSort() != null
                    && !(lFieldResult.getSort().equals(SORT_NONE))) {
                final SortingFieldData lSortFieldData = new SortingFieldData();

                // Get the UsableFieldData from the list according to containers
                // idents.
                String lUsableFieldDataId;
                lUsableFieldDataId =
                        searchService.getUsableFieldDataId(roleToken,
                                processName, lFieldResult.getName());
                UsableFieldData lUsableFieldData =
                        searchService.getUsableField(processName,
                                lUsableFieldDataId, Arrays.asList(containersId));
                if (null == lUsableFieldData) {
                    String lMsg =
                            MessageFormat.format(
                                    "Invalid field name ''{0}'' for sorting in filter ''{1}''",
                                    lFieldResult.getName(),
                                    filter.getLabelKey());
                    throw new InstantiateException(lMsg, lFieldResult);
                }

                Collection<String> lSortList =
                        SearchUtils.getSort(lUsableFieldData);
                String lSort = lFieldResult.getSort();
                if (StringUtils.isNotBlank(lSort) && !lSortList.contains(lSort)) {
                    throw new InstantiateException("Invalid sort operator '"
                            + lFieldResult.getSort() + "' for field '"
                            + lFieldResult.getName() + "'", lFieldResult);
                }
                lSortFieldData.setOrder(lSort);
                lSortFieldData.setUsableFieldData(lUsableFieldData);

                lSortingFieldsDataArray =
                        (SortingFieldData[]) ArrayUtils.add(
                                lSortingFieldsDataArray, lSortFieldData);
            }
        }
        lResultSortingData.setSortingFieldDatas(lSortingFieldsDataArray);
        return lResultSortingData;
    }

    private FilterData createFilterData() {
        FilterData lFilterData = new FilterData();

        lFilterData.setLabelKey(filter.getLabelKey());

        if (filter instanceof ProductFilter) {
            lFilterData.setType(FilterTypeData.PRODUCT);
        }
        else if (filter instanceof SheetFilter) {
            lFilterData.setType(FilterTypeData.SHEET);
        }
        else {
            lFilterData.setType(FilterTypeData.LINK);
        }

        lFilterData.setFieldsContainerIds(containersId);
        lFilterData.setFilterVisibilityConstraintData(visibilityData);
        lFilterData.setCriteriaData(createCriteriaGroups());

        return lFilterData;
    }

    private FilterProductScope[] createProductScope() {
        if (filter.getScope() == null || filter.getScope().isEmpty()) {
            return new FilterProductScope[] {};
        }
        else {
            final ArrayList<FilterProductScope> lScopes =
                    new ArrayList<FilterProductScope>(filter.getScope().size());

            for (ProductScope lScope : filter.getScope()) {
                lScopes.add(new FilterProductScope(lScope.getName(),
                        lScope.isIncludeSubProducts()));
            }

            return lScopes.toArray(new FilterProductScope[filter.getScope().size()]);
        }
    }

    private CriteriaData createCriteriaGroups() {
        if (filter.getCriteriaGroups() == null
                || filter.getCriteriaGroups().isEmpty()) {
            return null;
        }
        else if (filter.getCriteriaGroups().size() == 1) {
            return createCriteriaGroup(filter.getCriteriaGroups().get(0));
        }
        else {
            final OperationData lCrit = createOrOperator();

            for (CriteriaGroup lGroup : filter.getCriteriaGroups()) {
                appendCriteria(lCrit, createCriteriaGroup(lGroup));
            }

            return lCrit;
        }
    }

    private CriteriaData createCriteriaGroup(CriteriaGroup pGroup) {
        OperationData lGroupCritData = createAndOperator();

        for (Criterion lCrit : pGroup.getCriterionList()) {
            String lUsableFieldDataId =
                    serviceLocator.getSearchService().getUsableFieldDataId(
                            roleToken, processName, lCrit.getFieldKey());
            UsableFieldData lUsableFieldData =
                    searchService.getUsableField(processName,
                            lUsableFieldDataId, Arrays.asList(containersId));

            if (null == lUsableFieldData) {
                String lMsg =
                        MessageFormat.format(
                                "Invalid field name ''{0}'' for criterion in filter ''{1}''",
                                lCrit.getFieldKey(), filter.getLabelKey());
                throw new InstantiateException(lMsg, lCrit);
            }

            ScalarValueData lScalarValueData = null;

            try {
                lScalarValueData =
                        createScalarValueData(lUsableFieldData.getFieldType(),
                                lCrit.getValue());
            }
            catch (InstantiateException e) {
                throw new InstantiateException("Invalid value '"
                        + lCrit.getValue() + "' for field '"
                        + lCrit.getFieldKey() + "'", lCrit, e);
            }

            String lRealOp = OPERATORS.get(lCrit.getOperator());
            Collection<String> lCompatibleOperators =
                    searchService.getCompatibleOperators(lUsableFieldData);
            if ((null == lRealOp) || (!lCompatibleOperators.contains(lRealOp))) {
                throw new InstantiateException("Invalid operator '"
                        + lCrit.getOperator() + "' for field '"
                        + lCrit.getFieldKey() + "'", lCrit);
            }

            // Get case sensitivity
            Boolean lCaseSensitive = lCrit.getCaseSensitive();
            if (lCaseSensitive == null) {
                lCaseSensitive = Boolean.FALSE;
            }

            CriteriaData lCritData =
                    new CriteriaFieldData(lRealOp, lCaseSensitive,
                            lScalarValueData, lUsableFieldData);

            appendCriteria(lGroupCritData, lCritData);
        }

        if (lGroupCritData.getCriteriaDatas() == null
                || lGroupCritData.getCriteriaDatas().length == 0) {
            return null;
        }
        else if (lGroupCritData.getCriteriaDatas().length == 1) {
            return lGroupCritData.getCriteriaDatas()[0];
        }
        else {
            return lGroupCritData;
        }
    }

    public static ScalarValueData createScalarValueData(FieldType pType,
            String pValueStr) {
        if (pValueStr == null) {
            return null;
        }
        else if (FieldType.SIMPLE_STRING_FIELD.equals(pType)
                || FieldType.CHOICE_FIELD.equals(pType)
                || FieldType.ATTACHED_FIELD.equals(pType)) {
            return new StringValueData(pValueStr);
        }
        else if (FieldType.SIMPLE_REAL_FIELD.equals(pType)) {
            return new RealValueData(java.lang.Double.valueOf(pValueStr));
        }
        else if (FieldType.SIMPLE_INTEGER_FIELD.equals(pType)) {
            return new IntegerValueData(Integer.valueOf(pValueStr));
        }
        else if (FieldType.SIMPLE_BOOLEAN_FIELD.equals(pType)) {
            return new BooleanValueData(Boolean.valueOf(pValueStr));
        }
        else if (FieldType.SIMPLE_DATE_FIELD.equals(pType)) {
            try {
                // Otherwise verify if the value is a filter number (like the criteria since 10 days) or 
                // a string like 
                if (pValueStr.matches("\\d*")) {
                    return new IntegerValueData(Integer.valueOf(pValueStr));
                }
                else if (dateCriteria.contains(pValueStr)) {
                    return new StringValueData(pValueStr);
                }
                return new DateValueData(FieldsUtil.parseDate(pValueStr));
            }
            catch (ParseException e) {
                throw new InstantiateException("Invalid date '" + pValueStr
                        + "'");
            }
        }
        else {
            throw new InstantiateException("Unsupported type '" + pType + "'");
        }
    }

    private OperationData createAndOperator() {
        OperationData lAndResult = new OperationData();
        lAndResult.setOperator(AND);
        return lAndResult;
    }

    private OperationData createOrOperator() {
        OperationData lOrResult = new OperationData();
        lOrResult.setOperator(OR);
        return lOrResult;
    }

    private void appendCriteria(OperationData pParentOperator,
            CriteriaData pCriter) {
        CriteriaData[] lCritArray = pParentOperator.getCriteriaDatas();
        if (null == lCritArray) {
            lCritArray = new CriteriaData[0];
        }
        lCritArray = (CriteriaData[]) ArrayUtils.add(lCritArray, pCriter);

        pParentOperator.setCriteriaDatas(lCritArray);
    }

    static final public class Operators extends HashMap<String, String> {
        /** serialVersionUID */
        private static final long serialVersionUID = 8690894483116296176L;

        Operators() {
            // Initialize the map used to convert the operators from the XML
            // representation to their actual value for the API.
            // Note: < and > characters are not easily usable in XML,
            // so we have to kludge a bit here.
            put("=", "=");
            put("!=", "<>");
            put("like", "like");
            put("notLike", "not like");
            put("greaterThan", ">");
            put("greaterOrEqual", ">=");
            put("lessThan", "<");
            put("lessOrEqual", "<=");
            put("since", "since");
            put("other", "other");
        }

        public String put(String pXmlOp, String pApiOp) {
            return super.put(pXmlOp.toLowerCase(), pApiOp);
        }

        public String get(String pXmlOp) {
            return super.get(pXmlOp.toLowerCase());
        }
    }
}