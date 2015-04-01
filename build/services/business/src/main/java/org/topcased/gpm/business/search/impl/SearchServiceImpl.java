/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Anne Haugommard(Atos Origin),
 * Yvan Ntsama (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.search.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
//import org.apache.log4j.Logger;
import org.topcased.gpm.business.ServiceImplBase;
import org.topcased.gpm.business.ServiceLocator;
import org.topcased.gpm.business.attributes.AttributeData;
import org.topcased.gpm.business.attributes.service.AttributesService;
import org.topcased.gpm.business.authorization.service.AccessControlContextData;
import org.topcased.gpm.business.authorization.service.TypeAccessControlData;
import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.exception.ConstraintException;
import org.topcased.gpm.business.exception.FilterException;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.exception.InvalidIdentifierException;
import org.topcased.gpm.business.exception.InvalidNameException;
import org.topcased.gpm.business.exportation.impl.ExportationData;
import org.topcased.gpm.business.exportation.impl.ReadProperties;
import org.topcased.gpm.business.fields.FieldType;
import org.topcased.gpm.business.fields.FieldsContainerType;
import org.topcased.gpm.business.fields.SummaryData;
import org.topcased.gpm.business.fields.impl.CacheableFieldsContainer;
import org.topcased.gpm.business.link.impl.CacheableLinkType;
import org.topcased.gpm.business.link.impl.LinkDirection;
import org.topcased.gpm.business.product.impl.CacheableProductType;
import org.topcased.gpm.business.scalar.BooleanValueData;
import org.topcased.gpm.business.scalar.DateValueData;
import org.topcased.gpm.business.scalar.IntegerValueData;
import org.topcased.gpm.business.scalar.RealValueData;
import org.topcased.gpm.business.scalar.ScalarValueData;
import org.topcased.gpm.business.scalar.StringValueData;
import org.topcased.gpm.business.search.converter.FilterUsageConverter;
import org.topcased.gpm.business.search.converter.OperatorConverter;
import org.topcased.gpm.business.search.criterias.CriteriaData;
import org.topcased.gpm.business.search.criterias.CriteriaFieldData;
import org.topcased.gpm.business.search.criterias.FilterData;
import org.topcased.gpm.business.search.criterias.FilterTypeData;
import org.topcased.gpm.business.search.criterias.OperationData;
import org.topcased.gpm.business.search.criterias.impl.FilterFieldsContainerInfo;
import org.topcased.gpm.business.search.criterias.impl.Operators;
import org.topcased.gpm.business.search.criterias.impl.VirtualFieldData;
import org.topcased.gpm.business.search.impl.fields.UsableFieldCacheKey;
import org.topcased.gpm.business.search.impl.fields.UsableFieldsManager;
import org.topcased.gpm.business.search.impl.filter.FilterDataKey;
import org.topcased.gpm.business.search.impl.query.FilterQueryConfigurator;
import org.topcased.gpm.business.search.impl.query.FilterResultIdIterator;
import org.topcased.gpm.business.search.impl.query.FilterResultIterator;
import org.topcased.gpm.business.search.result.sorter.ResultSortingData;
import org.topcased.gpm.business.search.result.sorter.SortingFieldData;
import org.topcased.gpm.business.search.result.summary.ResultSummaryData;
import org.topcased.gpm.business.search.service.ExecutableFilterData;
import org.topcased.gpm.business.search.service.FilterProductScope;
import org.topcased.gpm.business.search.service.FilterScope;
import org.topcased.gpm.business.search.service.FilterVisibilityConstraintData;
import org.topcased.gpm.business.search.service.SearchService;
import org.topcased.gpm.business.search.service.UsableFieldData;
import org.topcased.gpm.business.serialization.data.CriteriaGroup;
import org.topcased.gpm.business.serialization.data.Criterion;
import org.topcased.gpm.business.serialization.data.FieldResult;
import org.topcased.gpm.business.serialization.data.NamedElement;
import org.topcased.gpm.business.serialization.data.ProductFilter;
import org.topcased.gpm.business.serialization.data.ProductScope;
import org.topcased.gpm.business.serialization.data.ProductTypeRef;
import org.topcased.gpm.business.serialization.data.SheetFilter;
import org.topcased.gpm.business.serialization.data.SheetTypeRef;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.domain.accesscontrol.EndUser;
import org.topcased.gpm.domain.businessProcess.BusinessProcess;
import org.topcased.gpm.domain.fields.BooleanValue;
import org.topcased.gpm.domain.fields.BooleanValueDao;
import org.topcased.gpm.domain.fields.DateValue;
import org.topcased.gpm.domain.fields.DateValueDao;
import org.topcased.gpm.domain.fields.FieldsContainer;
import org.topcased.gpm.domain.fields.IntegerValue;
import org.topcased.gpm.domain.fields.IntegerValueDao;
import org.topcased.gpm.domain.fields.RealValue;
import org.topcased.gpm.domain.fields.RealValueDao;
import org.topcased.gpm.domain.fields.ScalarValue;
import org.topcased.gpm.domain.fields.StringValue;
import org.topcased.gpm.domain.fields.StringValueDao;
import org.topcased.gpm.domain.product.Product;
import org.topcased.gpm.domain.search.Criteria;
import org.topcased.gpm.domain.search.CriteriaDao;
import org.topcased.gpm.domain.search.FieldsContainerId;
import org.topcased.gpm.domain.search.Filter;
import org.topcased.gpm.domain.search.FilterComponentDao;
import org.topcased.gpm.domain.search.FilterDao;
import org.topcased.gpm.domain.search.FilterElement;
import org.topcased.gpm.domain.search.FilterField;
import org.topcased.gpm.domain.search.FilterFieldDao;
import org.topcased.gpm.domain.search.FilterType;
import org.topcased.gpm.domain.search.FilterUsage;
import org.topcased.gpm.domain.search.FilterWithResult;
import org.topcased.gpm.domain.search.FilterWithResultDao;
import org.topcased.gpm.domain.search.Operation;
import org.topcased.gpm.domain.search.OperationDao;
import org.topcased.gpm.domain.search.ProductScopeDao;
import org.topcased.gpm.domain.search.result.sorter.ResultField;
import org.topcased.gpm.domain.search.result.sorter.ResultFieldDao;
import org.topcased.gpm.domain.search.result.sorter.ResultSorter;
import org.topcased.gpm.domain.search.result.sorter.ResultSorterDao;
import org.topcased.gpm.domain.search.result.summary.ResultSummary;
import org.topcased.gpm.domain.search.result.summary.ResultSummaryDao;
import org.topcased.gpm.domain.search.result.summary.SummaryField;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * Class for the searching service
 * 
 * @author ahaugomm
 */
public class SearchServiceImpl extends ServiceImplBase implements SearchService {
    // The log4j logger object for this class.
//    private static final Logger LOGGER =
//            org.apache.log4j.Logger.getLogger(SearchServiceImpl.class);

    /** The result summary DAO */
    private ResultSummaryDao resultSummaryDao;

    /** The result sorter DAO */
    private ResultSorterDao resultSorterDao;

    /** The executable filter component DAO */
    private FilterComponentDao filterComponentDao;

    /** The executable filter DAO */
    private FilterWithResultDao filterWithResultDao;

    /** The product scope DAO */
    private ProductScopeDao productScopeDao;

    /** The filter DAO */
    private FilterDao filterDao;

    private FilterManager filterManager;

    private UsableFieldsManager usableFieldsManager;

    private int maxFieldsDepth;

    private boolean maxFieldsDepthGetted = false;

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.service.SearchService#checkVisibilityConstraints(java.lang.String,
     *      java.lang.String, java.lang.String,
     *      org.topcased.gpm.business.search.service.FilterVisibilityConstraintData)
     */
    @Override
    public boolean checkVisibilityConstraints(String pUserLogin,
            String pBusinessProcessName, String pProductName,
            FilterVisibilityConstraintData pFConstraint) {
        return checkValidAccess(pUserLogin, pBusinessProcessName,
                pFConstraint.getUserLogin(),
                pFConstraint.getBusinessProcessName());
    }

    private boolean checkValidAccess(String pUserLogin,
            String pBusinessProcessName, String pUserLoginConstraint,
            String pBusinessProcessNameConstraint) {

        // BusinessProcess ok
        boolean lBusinessProcessOk =
                StringUtils.isNotBlank(pBusinessProcessName)
                        && pBusinessProcessName.equals(pBusinessProcessNameConstraint);
        // user = null or user ok
        boolean lUserOk =
                StringUtils.isBlank(pUserLoginConstraint)
                        || pUserLoginConstraint.equals(pUserLogin);
        return lBusinessProcessOk && lUserOk;

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.service.SearchService#createExecutableFilter(java.lang.String,
     *      org.topcased.gpm.business.search.service.ExecutableFilterData)
     */
    @Override
    public String createExecutableFilter(final String pRoleToken,
            final ExecutableFilterData pFilter) throws AuthorizationException {
        // Check access right
        /*if (!filterAccessManager.canSaveFilter(pRoleToken, pFilter)) {
            throw new AuthorizationException(
                    "Unsufficient rights to create filter "
                            + pFilter.getLabelKey());
        }*/

        return createExecutableFilter(pFilter);
    }

    private String createExecutableFilter(
            final ExecutableFilterData pExecutableFilterData) {
        if (StringUtils.isBlank(pExecutableFilterData.getLabelKey())) {
            throw new GDMException(
                    "The name of the filter cannot be null or empty.");
        }

        final String lProcessName =
                pExecutableFilterData.getFilterVisibilityConstraintData().getBusinessProcessName();
        final String lProductName =
                pExecutableFilterData.getFilterVisibilityConstraintData().getProductName();
        final String lLogin =
                pExecutableFilterData.getFilterVisibilityConstraintData().getUserLogin();

        boolean lIsCaseSensitive =
                ServiceLocator.getNewInstance().getAuthorizationService().isLoginCaseSensitive();

        if (getFilterComponentDao().getFilterComponent(lProcessName,
                lProductName, lLogin, pExecutableFilterData.getLabelKey(),
                FilterWithResult.class, lIsCaseSensitive) != null) {
            throw new GDMException("A filter called '"
                    + pExecutableFilterData.getLabelKey()
                    + "' already exists with this name in this scope.");
        }

        final FilterWithResult lFilterWithResult =
                FilterWithResult.newInstance();

        // If Id has been already assign, take it
        if (StringUtils.isNotBlank(pExecutableFilterData.getId())) {
            lFilterWithResult.setId(pExecutableFilterData.getId());
        }

        lFilterWithResult.setName(pExecutableFilterData.getLabelKey());
        lFilterWithResult.setDescription(pExecutableFilterData.getDescription());
        lFilterWithResult.setBusinessProcess(getBusinessProcess(lProcessName));

        lFilterWithResult.setUsage(FilterUsage.fromString(pExecutableFilterData.getUsage()));
        lFilterWithResult.setHidden(pExecutableFilterData.isHidden());

        if (lProductName == null) {
            lFilterWithResult.setProduct(null);
        }
        else {
            lFilterWithResult.setProduct(getProduct(lProcessName, lProductName));
        }
        if (lLogin == null) {
            lFilterWithResult.setEndUser(null);
        }
        else {
            lFilterWithResult.setEndUser(getEndUserDao().getUser(lLogin,
                    authorizationService.isLoginCaseSensitive()));
        }

        lFilterWithResult.setFilter(createFilter(pExecutableFilterData.getFilterData()));
        if (pExecutableFilterData.getResultSortingData() != null) {
            lFilterWithResult.setResultSorter(
                    createResultSorter(pExecutableFilterData.getResultSortingData()));
        }
        lFilterWithResult.setResultSummary(
                createResultSummary(pExecutableFilterData.getResultSummaryData()));

        lFilterWithResult.setProductScopes(createProductScopes(pExecutableFilterData));

        getFilterWithResultDao().create(lFilterWithResult);

        return lFilterWithResult.getId();
    }

    private Set<org.topcased.gpm.domain.search.ProductScope> createProductScopes(
            final ExecutableFilterData pFilter) {
        final Set<org.topcased.gpm.domain.search.ProductScope> lProductScopes =
                new HashSet<org.topcased.gpm.domain.search.ProductScope>();

        if (pFilter.getFilterProductScopes() != null) {
            for (FilterProductScope lProductScope : pFilter.getFilterProductScopes()) {
                final org.topcased.gpm.domain.search.ProductScope lDomainProductScope =
                        org.topcased.gpm.domain.search.ProductScope.newInstance();

                lDomainProductScope.setProductName(lProductScope.getProductName());
                lDomainProductScope.setIncludeSubProducts(lProductScope.isIncludeSubProducts());
                productScopeDao.create(lDomainProductScope);
                lProductScopes.add(lDomainProductScope);
            }
        }

        return lProductScopes;
    }

    private ResultSummary createResultSummary(
            final ResultSummaryData pResultSummaryData) {
        final String lProcessName =
                pResultSummaryData.getFilterVisibilityConstraintData().getBusinessProcessName();
        final String lProductName =
                pResultSummaryData.getFilterVisibilityConstraintData().getProductName();
        final String lLogin =
                pResultSummaryData.getFilterVisibilityConstraintData().getUserLogin();

        boolean lIsCaseSensitive =
                ServiceLocator.getNewInstance().getAuthorizationService().isLoginCaseSensitive();

        if (getFilterComponentDao().getFilterComponent(lProcessName,
                lProductName, lLogin, pResultSummaryData.getLabelKey(),
                ResultSummary.class, lIsCaseSensitive) != null) {
            throw new InvalidIdentifierException("The result summary "
                    + pResultSummaryData.getLabelKey()
                    + " already exists in DB and cannot be created");
        }

        final ResultSummary lResultSummary = ResultSummary.newInstance();

        lResultSummary.setBusinessProcess(getBusinessProcess(lProcessName));

        if (lProductName == null) {
            lResultSummary.setProduct(null);
        }
        else {
            lResultSummary.setProduct(getProduct(lProcessName, lProductName));
        }
        if (lLogin == null) {
            lResultSummary.setEndUser(null);
        }
        else {
            lResultSummary.setEndUser(getEndUserDao().getUser(lLogin,
                    authorizationService.isLoginCaseSensitive()));
        }

        lResultSummary.setName(pResultSummaryData.getLabelKey());

        for (String lId : pResultSummaryData.getFieldsContainerIds()) {
            lResultSummary.addToFieldsContainerList(getFieldsContainer(lId));
        }

        for (UsableFieldData lUsableFieldData : pResultSummaryData.getUsableFieldDatas()) {
            SummaryField lSummaryField = SummaryField.newInstance();
            lSummaryField.setFilterField(createFilterField(lUsableFieldData));
            lSummaryField.setLabel(lUsableFieldData.getLabel());
            lResultSummary.addToSummaryFieldList(lSummaryField);
        }

        getResultSummaryDao().create(lResultSummary);

        return lResultSummary;
    }

    private ResultSorter createResultSorter(
            final ResultSortingData pResultSortingData) {
        final String lProcessName =
                pResultSortingData.getFilterVisibilityConstraintData().getBusinessProcessName();
        final String lProductName =
                pResultSortingData.getFilterVisibilityConstraintData().getProductName();
        final String lLogin =
                pResultSortingData.getFilterVisibilityConstraintData().getUserLogin();

        boolean lIsCaseSensitive =
                ServiceLocator.getNewInstance().getAuthorizationService().isLoginCaseSensitive();
        if (getFilterComponentDao().getFilterComponent(lProcessName,
                lProductName, lLogin, pResultSortingData.getLabelKey(),
                ResultSorter.class, lIsCaseSensitive) != null) {
            throw new InvalidIdentifierException("The result sorter "
                    + pResultSortingData.getLabelKey()
                    + " already exists in DB and cannot be created");
        }

        final ResultSorter lResultSorter = ResultSorter.newInstance();

        lResultSorter.setBusinessProcess(getBusinessProcess(lProcessName));

        if (lProductName == null) {
            lResultSorter.setProduct(null);
        }
        else {
            lResultSorter.setProduct(getProduct(lProcessName, lProductName));
        }
        if (lLogin == null) {
            lResultSorter.setEndUser(null);
        }
        else {
            lResultSorter.setEndUser(getEndUserDao().getUser(lLogin,
                    authorizationService.isLoginCaseSensitive()));
        }

        lResultSorter.setName(pResultSortingData.getLabelKey());

        for (String lId : pResultSortingData.getFieldsContainerIds()) {
            lResultSorter.addToFieldsContainerList(getFieldsContainer(lId));
        }
        if (pResultSortingData.getSortingFieldDatas() != null) {
            for (SortingFieldData lSortingFieldData : pResultSortingData.getSortingFieldDatas()) {
                ResultField lResultField = ResultField.newInstance();
                lResultField.setFilterField(
                        createFilterField(lSortingFieldData.getUsableFieldData()));
                Collection<String> lSortList =
                        SearchUtils.getSort(lSortingFieldData.getUsableFieldData());
                String lSort = lSortingFieldData.getOrder();
                if (!lSortList.contains(lSort)) {
                    lSort = SearchUtils.transformOrder(lSort);
                }
                lResultField.setSortOrder(lSortingFieldData.getOrder());
                lResultSorter.addToResultFieldList(lResultField);
            }
        }

        getResultSorterDao().create(lResultSorter);

        return lResultSorter;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.service.SearchService#executeFilterIdentifier(java.lang.String,
     *      java.lang.Class,
     *      org.topcased.gpm.business.search.service.ExecutableFilterData,
     *      org.topcased.gpm.business.search.service.FilterVisibilityConstraintData,
     *      org.topcased.gpm.business.search.impl.query.FilterQueryConfigurator)
     */
    @Override
    public FilterResultIdIterator executeFilterIdentifier(String pRoleToken,
            ExecutableFilterData pExecutableFilterData,
            FilterVisibilityConstraintData pFilterVisibilityConstraintData,
            FilterQueryConfigurator pFilterQueryConfigurator)
        throws AuthorizationException, FilterException {

        if (checkVisibilityConstraints(
                pFilterVisibilityConstraintData.getUserLogin(),
                pFilterVisibilityConstraintData.getBusinessProcessName(),
                pFilterVisibilityConstraintData.getProductName(),
                pExecutableFilterData.getFilterVisibilityConstraintData())) {
            return filterManager.executeFilterIdentifier(pRoleToken,
                    pExecutableFilterData, pFilterQueryConfigurator);
        }
        throw new AuthorizationException(
                "Unsufficient rights to execute the filter "
                        + pExecutableFilterData.getLabelKey());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.service.SearchService#executeFilter(java.lang.String,
     *      java.lang.Class,
     *      org.topcased.gpm.business.search.service.ExecutableFilterData,
     *      org.topcased.gpm.business.search.service.FilterVisibilityConstraintData,
     *      org.topcased.gpm.business.search.impl.query.FilterQueryConfigurator)
     */
    @Override
    public <S extends SummaryData> FilterResultIterator<S> executeFilter(
            String pRoleToken, ExecutableFilterData pExecutableFilterData,
            FilterVisibilityConstraintData pFilterVisibilityConstraintData,
            FilterQueryConfigurator pFilterQueryConfigurator) {
        if (checkVisibilityConstraints(
                pFilterVisibilityConstraintData.getUserLogin(),
                pFilterVisibilityConstraintData.getBusinessProcessName(),
                pFilterVisibilityConstraintData.getProductName(),
                pExecutableFilterData.getFilterVisibilityConstraintData())) {
            return filterManager.executeFilter(pRoleToken,
                    pExecutableFilterData, pFilterQueryConfigurator);
        }
        throw new AuthorizationException(
                "Unsufficient rights to execute the filter "
                        + pExecutableFilterData.getLabelKey());

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.service.SearchService#executeFilter(java.lang.String,
     *      java.lang.Class,
     *      org.topcased.gpm.business.search.service.ExecutableFilterData,
     *      org.topcased.gpm.business.search.service.FilterVisibilityConstraintData,
     *      org.topcased.gpm.business.search.impl.query.FilterQueryConfigurator,
     *      java.util.Collection)
     */
    @Override
    public <S extends SummaryData> FilterResultIterator<S> executeFilter(
            String pRoleToken, ExecutableFilterData pExecutableFilterData,
            FilterVisibilityConstraintData pFilterVisibilityConstraintData,
            FilterQueryConfigurator pFilterQueryConfigurator,
            Collection<String> pContainerIdentifiers) {

        if (checkVisibilityConstraints(
                pFilterVisibilityConstraintData.getUserLogin(),
                pFilterVisibilityConstraintData.getBusinessProcessName(),
                pFilterVisibilityConstraintData.getProductName(),
                pExecutableFilterData.getFilterVisibilityConstraintData())) {
            return filterManager.executeFilter(pRoleToken,
                    pExecutableFilterData, pFilterQueryConfigurator,
                    pContainerIdentifiers);
        }
        throw new AuthorizationException(
                "Unsufficient rights to execute the filter "
                        + pExecutableFilterData.getLabelKey());
    }

    /** {@inheritDoc} */
    @Override
    public ExecutableFilterData retrieveFilterDataFromName(String pRoleToken,
            String pFilterName,
            FilterVisibilityConstraintData pFilterVisibilityConstraintData,
            FilterScope pFilterScope) throws AuthorizationException {

        boolean lFound = false;
        ExecutableFilterData lFilterData = null;
        FilterVisibilityConstraintData lFilterVisibilityConstraintData = null;
        if (pFilterScope == null
                || FilterScope.USER_FILTER.equals(pFilterScope)) {
            lFilterVisibilityConstraintData =
                    new FilterVisibilityConstraintData();
            if (StringUtils.isBlank(pFilterVisibilityConstraintData.getBusinessProcessName())) {
                String lBusinessProcessName =
                        getBusinessProcessName(pRoleToken);
                lFilterVisibilityConstraintData.setBusinessProcessName(lBusinessProcessName);
            }
            if (StringUtils.isBlank(pFilterVisibilityConstraintData.getUserLogin())) {
                String lUserLogin =
                        authorizationService.getLoginFromToken(pRoleToken);
                lFilterVisibilityConstraintData.setUserLogin(lUserLogin);
            }

            if (StringUtils.isNotBlank(pFilterVisibilityConstraintData.getProductName())) {
                lFilterVisibilityConstraintData.setProductName(StringUtils.EMPTY);
            }

            lFilterData =
                    getExecutableFilterByName(
                            pRoleToken,
                            lFilterVisibilityConstraintData.getBusinessProcessName(),
                            lFilterVisibilityConstraintData.getProductName(),
                            lFilterVisibilityConstraintData.getUserLogin(),
                            pFilterName);
            if (lFilterData != null) {
                lFound = true;
            }
        }
        if (FilterScope.PRODUCT_FILTER.equals(pFilterScope)
                || (pFilterScope == null && !lFound)) {

            lFilterVisibilityConstraintData =
                    new FilterVisibilityConstraintData();
            if (StringUtils.isBlank(pFilterVisibilityConstraintData.getBusinessProcessName())) {
                String lBusinessProcessName =
                        getBusinessProcessName(pRoleToken);
                lFilterVisibilityConstraintData.setBusinessProcessName(lBusinessProcessName);
            }
            if (StringUtils.isNotBlank(pFilterVisibilityConstraintData.getUserLogin())) {
                lFilterVisibilityConstraintData.setUserLogin(StringUtils.EMPTY);
            }

            lFilterVisibilityConstraintData.setProductName(
                    pFilterVisibilityConstraintData.getProductName());

            lFilterData =
                    getExecutableFilterByName(
                            pRoleToken,
                            lFilterVisibilityConstraintData.getBusinessProcessName(),
                            lFilterVisibilityConstraintData.getProductName(),
                            lFilterVisibilityConstraintData.getUserLogin(),
                            pFilterName);
            if (lFilterData != null) {
                lFound = true;
            }
        }
        if (FilterScope.INSTANCE_FILTER.equals(pFilterScope)
                || (pFilterScope == null && !lFound)) {

            lFilterVisibilityConstraintData =
                    new FilterVisibilityConstraintData();
            if (StringUtils.isBlank(pFilterVisibilityConstraintData.getBusinessProcessName())) {
                String lBusinessProcessName =
                        getBusinessProcessName(pRoleToken);
                lFilterVisibilityConstraintData.setBusinessProcessName(lBusinessProcessName);
            }
            if (StringUtils.isNotBlank(pFilterVisibilityConstraintData.getUserLogin())) {
                lFilterVisibilityConstraintData.setUserLogin(StringUtils.EMPTY);
            }
            if (StringUtils.isNotBlank(pFilterVisibilityConstraintData.getProductName())) {
                lFilterVisibilityConstraintData.setProductName(StringUtils.EMPTY);
            }

            lFilterData =
                    getExecutableFilterByName(
                            pRoleToken,
                            lFilterVisibilityConstraintData.getBusinessProcessName(),
                            lFilterVisibilityConstraintData.getProductName(),
                            lFilterVisibilityConstraintData.getUserLogin(),
                            pFilterName);
            if (lFilterData != null) {
                lFound = true;
            }
        }

        if (!lFound) {
            throw new InvalidNameException(pFilterName);
        }
        return lFilterData;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.service.SearchService#removeExecutableFilter(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public boolean removeExecutableFilter(final String pRoleToken,
            final String pFilterId) throws AuthorizationException {
        final ExecutableFilterData lFilter =
                getExecutableFilter(pRoleToken, pFilterId);

        // Delete the filter only if exists
        if (lFilter != null) {
            // Check access right
            if (!filterAccessManager.canSaveFilter(pRoleToken, lFilter)) {
                throw new AuthorizationException(
                        "Unsufficient rights to remove filter "
                                + lFilter.getLabelKey());
            }

            // Remove all : the filter, the result sorter and the result summary
            getFilterWithResultDao().remove(lFilter.getId());
            getFilterDao().remove(lFilter.getFilterData().getId());
            if (lFilter.getResultSortingData() != null) {
                getResultSorterDao().remove(
                        lFilter.getResultSortingData().getId());
            }
            getResultSummaryDao().remove(lFilter.getResultSummaryData().getId());

            // Clear the filter cache
            clearFilterCaches(pFilterId);
        }

        return lFilter != null;
    }

    public FilterComponentDao getFilterComponentDao() {
        return filterComponentDao;
    }

    public FilterWithResultDao getFilterWithResultDao() {
        return filterWithResultDao;
    }

    private ResultSorterDao getResultSorterDao() {
        return resultSorterDao;
    }

    private ResultSummaryDao getResultSummaryDao() {
        return resultSummaryDao;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.service.SearchService#getVisibleFilterDatas(java.lang.String,
     *      java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public Collection<FilterData> getVisibleFilterDatas(String pRoleToken,
            String pBusinessProcessName, String pProductName, String pUserLogin) {
        Collection<FilterData> lFilterDatas = new ArrayList<FilterData>();
        List<Filter> lFilters =
                getFilterDao().getVisibleFilters(pBusinessProcessName,
                        pProductName, pUserLogin);
        for (Filter lFilter : lFilters) {
            lFilterDatas.add(filterDataManager.convertFilter(lFilter));
        }
        return lFilterDatas;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.service.SearchService#getVisibleExecutableFilterDatasByFilterType(java.lang.String,
     *      java.lang.String, java.lang.String, java.lang.String,
     *      org.topcased.gpm.business.search.criterias.FilterTypeData,
     *      java.lang.String)
     * @deprecated
     */
    @Override
    public Collection<ExecutableFilterData> getVisibleExecutableFilterDatasByFilterType(
            String pRoleToken, String pBusinessProcessName,
            String pProductName, String pUserLogin, FilterTypeData pFilterType,
            String pUsage) {

        return getVisibleExecutableFilter(pRoleToken,
                new FilterVisibilityConstraintData(pUserLogin,
                        pBusinessProcessName, pProductName), pFilterType,
                pUsage);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.service.SearchService#getVisibleExecutableFilter(java.lang.String,
     *      org.topcased.gpm.business.search.service.FilterVisibilityConstraintData,
     *      org.topcased.gpm.business.search.criterias.FilterTypeData,
     *      java.lang.String)
     */
    @Override
    public Collection<ExecutableFilterData> getVisibleExecutableFilter(
            final String pRoleToken,
            final FilterVisibilityConstraintData pFilterVisibilityConstraintData,
            final FilterTypeData pFilterType, final String pUsage) {
        FilterUsage lFilterUsage = null;
        FilterType lFilterType = null;

        if (pUsage != null) {
            lFilterUsage = FilterUsage.fromString(pUsage);
        }
        if (pFilterType != null) {
            lFilterType = pFilterType.toFilterType();
        }

        final List<FilterWithResult> lFilters =
                getFilterWithResultDao().getVisibleExecutableFilters(
                        pFilterVisibilityConstraintData.getBusinessProcessName(),
                        pFilterVisibilityConstraintData.getProductName(),
                        pFilterVisibilityConstraintData.getUserLogin(),
                        lFilterUsage, lFilterType,
                        getAuthService().hasAdminAccess(pRoleToken));
        final Collection<ExecutableFilterData> lFilterDataList =
                new ArrayList<ExecutableFilterData>(lFilters.size());

        for (FilterWithResult lFilter : lFilters) {
            lFilterDataList.add(getExecutableFilter(pRoleToken, lFilter.getId()));
        }

        return lFilterDataList;
    }

    /**
     * creates a field criteria from a criteria field data and a list of
     * fieldsContainers ids
     * 
     * @param pFieldsContainerIds
     *            the list of the ids of the fields container
     * @param pCriteriaFieldData
     *            the criteria field data
     * @return a field criteria
     */
    private Criteria createFieldCriteria(CriteriaFieldData pCriteriaFieldData) {
        Criteria lCriteria = Criteria.newInstance();
        lCriteria.setFilterField(createFilterField(pCriteriaFieldData.getUsableFieldData()));
        lCriteria.setOperator(pCriteriaFieldData.getOperator());
        lCriteria.setScalarValue(createNewScalarValue(pCriteriaFieldData.getScalarValueData()));
        lCriteria.setCaseSensitive(pCriteriaFieldData.getCaseSensitive());
        return lCriteria;
    }

    /**
     * create an operation from an operation data and fieldContainerIds
     * 
     * @param pFieldsContainerIds
     *            the list of ids of the fieldsContainers
     * @param pOperationData
     *            the operation data
     * @return an operation
     */
    private Operation createOperation(OperationData pOperationData) {
        Operation lOperation = Operation.newInstance();
        lOperation.setOp(pOperationData.getOperator());
        for (CriteriaData lCriteriaData : pOperationData.getCriteriaDatas()) {
            lOperation.addToFilterElementList(createFilterElement(lCriteriaData));
        }
        return lOperation;
    }

    /**
     * create a filter element from a CriteriaData and a list of ids of
     * fieldContainers
     * 
     * @param pCriteriaData
     *            the criteria data
     * @return the filter element
     * @throws GDMException
     *             If the filter element already exists / the criteria data is
     *             invalid (no CriteriaFieldData or no OperationData) / the
     *             scalar value of the criterion is invalid.
     */
    // TODO : declare in superclass
    public FilterElement createFilterElement(CriteriaData pCriteriaData) {

        if (pCriteriaData.getId() != null) {
            throw new GDMException("FilterElement " + pCriteriaData.getId()
                    + " already  exists in the DB.");
        }
        if (pCriteriaData instanceof CriteriaFieldData) {
            CriteriaFieldData lCriteriaFieldData =
                    (CriteriaFieldData) pCriteriaData;
            Criteria lFieldCrit = createFieldCriteria(lCriteriaFieldData);

            criteriaDao.create(lFieldCrit);

            return lFieldCrit;
        }
        else if (pCriteriaData instanceof OperationData) {
            OperationData lOperationData = (OperationData) pCriteriaData;
            Operation lOperation = createOperation(lOperationData);

            operationDao.create(lOperation);

            return lOperation;
        }
        else {
            throw new GDMException("Invalid criteriaData. ");
        }
    }

    /**
     * creates a scalar value from a scalarValueData
     * 
     * @param pScalarValueData
     *            the scalarValueData
     * @return a scalarValue
     */
    private ScalarValue createNewScalarValue(ScalarValueData pScalarValueData) {
        ScalarValue lResult;

        if (pScalarValueData == null) {
            return null;
        }
        if (pScalarValueData instanceof BooleanValueData) {
            BooleanValue lBooleanValue = BooleanValue.newInstance();
            lBooleanValue.setBoolValue(((BooleanValueData) pScalarValueData).getValue());
            booleanValueDao.create(lBooleanValue);
            lResult = lBooleanValue;
        }
        else if (pScalarValueData instanceof DateValueData) {
            DateValue lDateValue = DateValue.newInstance();
            lDateValue.setDateValue(((DateValueData) pScalarValueData).getValue());
            dateValueDao.create(lDateValue);
            lResult = lDateValue;
        }
        else if (pScalarValueData instanceof IntegerValueData) {
            IntegerValue lIntegerValue = IntegerValue.newInstance();
            lIntegerValue.setIntValue(((IntegerValueData) pScalarValueData).getValue());
            integerValueDao.create(lIntegerValue);
            lResult = lIntegerValue;
        }
        else if (pScalarValueData instanceof RealValueData) {
            RealValue lRealValue = RealValue.newInstance();
            lRealValue.setRealValue(((RealValueData) pScalarValueData).getValue());
            realValueDao.create(lRealValue);
            lResult = lRealValue;
        }
        else if (pScalarValueData instanceof StringValueData) {
            StringValue lStringValue = StringValue.newInstance();
            lStringValue.setStringValue(((StringValueData) pScalarValueData).getValue());
            stringValueDao.create(lStringValue);
            lResult = lStringValue;
        }
        else {
            throw new GDMException("Invalid ScalarValueData");
        }
        return lResult;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.service.SearchService#getExecutableFilter(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public ExecutableFilterData getExecutableFilter(final String pRoleToken,
            final String pFilterId) {
        final ExecutableFilterData lFilter =
                filterDataManager.getElement(new FilterDataKey(pFilterId));

        // Apply access control (not on product filters)
        if (!FilterTypeData.PRODUCT.equals(lFilter.getFilterData().getType())) {
        	filterAccessManager.applyAccessControl(pRoleToken, lFilter);
        }

        return lFilter;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.service.SearchService#getExecutableFilterByName(java.lang.String,
     *      java.lang.String, java.lang.String, java.lang.String,
     *      java.lang.String)
     */
    @Override
    public ExecutableFilterData getExecutableFilterByName(
            final String pRoleToken, final String pProcessName,
            final String pFilterProductName, final String pFilterUserLogin,
            final String pFilterName) {
        boolean lIsCaseSensitive =
                ServiceLocator.getNewInstance().getAuthorizationService().isLoginCaseSensitive();

        final FilterWithResult lExecutableFilter =
                (FilterWithResult) getFilterComponentDao().getFilterComponent(
                        pProcessName, pFilterProductName, pFilterUserLogin,
                        pFilterName, FilterWithResult.class, lIsCaseSensitive);

        if (lExecutableFilter == null) {
            return null;
        }
        else {
            return getExecutableFilter(pRoleToken, lExecutableFilter.getId());
        }
    }

    private Filter createFilter(final FilterData pFilterData) {
        final String lProcessName =
                pFilterData.getFilterVisibilityConstraintData().getBusinessProcessName();
        final String lProductName =
                pFilterData.getFilterVisibilityConstraintData().getProductName();
        final String lLogin =
                pFilterData.getFilterVisibilityConstraintData().getUserLogin();

        boolean lIsCaseSensitive =
                ServiceLocator.getNewInstance().getAuthorizationService().isLoginCaseSensitive();

        if (getFilterComponentDao().getFilterComponent(lProcessName,
                lProductName, lLogin, pFilterData.getLabelKey(), Filter.class,
                lIsCaseSensitive) != null) {
            final String lFilterLocalization;

            if (lLogin != null) {
                lFilterLocalization = "for user " + lLogin;
            }
            else if (lProductName != null) {
                lFilterLocalization = "on product " + lProductName;
            }
            else {
                lFilterLocalization = "on instance " + lProcessName;
            }
            throw new ConstraintException("A filter with name "
                    + pFilterData.getLabelKey() + lFilterLocalization
                    + " already exists in DB and cannot be created");
        }

        final Filter lFilter = Filter.newInstance();

        lFilter.setName(pFilterData.getLabelKey());
        lFilter.setModel(pFilterData.isIsFilterModel());
        lFilter.setType(FilterType.fromString(pFilterData.getType().toString()));
        lFilter.setBusinessProcess(getBusinessProcess(lProcessName));

        for (String lId : pFilterData.getFieldsContainerIds()) {
            lFilter.addToFieldsContainerList(getFieldsContainer(lId));
        }

        if (lLogin != null) {
            lFilter.setEndUser(getEndUserDao().getUser(lLogin,
                    authorizationService.isLoginCaseSensitive()));
        }
        else {
            lFilter.setEndUser(null);
        }
        if (lProductName == null) {
            lFilter.setProduct(null);
        }
        else {
            lFilter.setProduct(getProduct(lProcessName, lProductName));
        }

        if (pFilterData.getCriteriaData() != null) {
            lFilter.setFilterElement(createFilterElement(pFilterData.getCriteriaData()));
        }

        getFilterDao().create(lFilter);

        return lFilter;
    }

    private void updateFilter(FilterData pFilterData) {
        final Filter lFilter =
                (Filter) getFilterDao().load(pFilterData.getId());

        lFilter.setName(pFilterData.getLabelKey());
        lFilter.setModel(pFilterData.isIsFilterModel());

        /* UPDATE BUSINESS PROCESS */
        String lBusinessProcessName =
                pFilterData.getFilterVisibilityConstraintData().getBusinessProcessName();
        final BusinessProcess lBusinessProcess =
                getBusinessProcess(lBusinessProcessName);
        lFilter.setBusinessProcess(lBusinessProcess);

        /* UPDATE END USER */
        String lLogin =
                pFilterData.getFilterVisibilityConstraintData().getUserLogin();
        if (lLogin != null) {
            EndUser lUser =
                    getEndUserDao().getUser(lLogin,
                            authorizationService.isLoginCaseSensitive());
            lFilter.setEndUser(lUser);
        }
        else {
            lFilter.setEndUser(null);
        }

        /* UPDATE PRODUCT */
        final Product lProduct;
        if (pFilterData.getFilterVisibilityConstraintData().getProductName() == null) {
            lProduct = null;
        }
        else {
            lProduct =
                    getProduct(
                            lBusinessProcessName,
                            pFilterData.getFilterVisibilityConstraintData().getProductName());
        }

        lFilter.setProduct(lProduct);

        /* UPDATE FIELDS CONTAINER */

        // Remove previous fieldsContainer
        while (lFilter.getFieldsContainers().iterator().hasNext()) {
            FieldsContainer lFieldsContainer =
                    lFilter.getFieldsContainers().iterator().next();
            lFilter.removeFromFieldsContainerList(lFieldsContainer);
        }
        // Add new ones
        for (String lFieldsContainerId : pFilterData.getFieldsContainerIds()) {
            lFilter.addToFieldsContainerList(getFieldsContainer(lFieldsContainerId));
        }

        // CLear the filter element
        if (lFilter.getFilterElement() instanceof Operation) {
            final Operation lOperation = (Operation) lFilter.getFilterElement();

            lOperation.getFilterElements().clear();
        }
        // Re-create the filter element
        if (pFilterData.getCriteriaData() == null) {
            lFilter.setFilterElement(null);
        }
        else {
            lFilter.setFilterElement(createFilterElement(pFilterData.getCriteriaData()));
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.service.SearchService#updateExecutableFilter(java.lang.String,
     *      org.topcased.gpm.business.search.service.ExecutableFilterData)
     */
    @Override
    public void updateExecutableFilter(final String pRoleToken,
            final ExecutableFilterData pExecutableFilterData)
        throws AuthorizationException {
        final FilterWithResult lFilterWithResult =
                getFilterWithResultDao().load(pExecutableFilterData.getId());

        // Check if the filter exists
        /*if (!filterAccessManager.canSaveFilter(pRoleToken,
                pExecutableFilterData)) {
            throw new AuthorizationException(
                    "Unsufficient rights to save the filter");
        }*/

        final String lProcessName =
                pExecutableFilterData.getFilterVisibilityConstraintData().getBusinessProcessName();
        final String lLogin =
                pExecutableFilterData.getFilterVisibilityConstraintData().getUserLogin();
        final String lProductName =
                pExecutableFilterData.getFilterVisibilityConstraintData().getProductName();

        lFilterWithResult.setName(pExecutableFilterData.getLabelKey());
        lFilterWithResult.setDescription(pExecutableFilterData.getDescription());
        lFilterWithResult.setUsage(FilterUsage.fromString(pExecutableFilterData.getUsage()));
        lFilterWithResult.setHidden(pExecutableFilterData.isHidden());
        lFilterWithResult.setBusinessProcess(getBusinessProcess(lProcessName));

        if (lLogin != null) {
            EndUser lUser =
                    getEndUserDao().getUser(lLogin,
                            authorizationService.isLoginCaseSensitive());
            lFilterWithResult.setEndUser(lUser);
        }
        else {
            lFilterWithResult.setEndUser(null);
        }
        if (lProductName == null) {
            lFilterWithResult.setProduct(null);
        }
        else {
            lFilterWithResult.setProduct(getProduct(lProcessName, lProductName));
        }

        updateResultSummary(lFilterWithResult.getResultSummary(),
                pExecutableFilterData.getResultSummaryData());
        updateFilter(pExecutableFilterData.getFilterData());

        if (pExecutableFilterData.getResultSortingData() != null) {
            if (lFilterWithResult.getResultSorter() == null) {
                lFilterWithResult.setResultSorter(
                        createResultSorter(pExecutableFilterData.getResultSortingData()));
            }
            else {
                updateResultSorter(lFilterWithResult.getResultSorter(),
                        pExecutableFilterData.getResultSortingData());
            }
        }

        // Update Product Scope : remove ...
        for (org.topcased.gpm.domain.search.ProductScope lDomainScope :
                lFilterWithResult.getProductScopes()) {
            productScopeDao.remove(lDomainScope);
        }
        // .. and re create
        lFilterWithResult.setProductScopes(createProductScopes(pExecutableFilterData));

        // Clear the filter cache
        clearFilterCaches(lFilterWithResult.getId());
    }

    private void updateResultSummary(ResultSummary pResultSummary,
            ResultSummaryData pResultSummaryData) {
        /* UPDATE LABEL KEY */
        pResultSummary.setName(pResultSummaryData.getLabelKey());

        /* UPDATE BUSINESS PROCESS */
        String lBusinessProcessName =
                pResultSummaryData.getFilterVisibilityConstraintData().getBusinessProcessName();
        final BusinessProcess lBusinessProcess =
                getBusinessProcess(lBusinessProcessName);
        pResultSummary.setBusinessProcess(lBusinessProcess);

        /* UPDATE END USER */
        String lLogin =
                pResultSummaryData.getFilterVisibilityConstraintData().getUserLogin();
        if (lLogin != null) {
            EndUser lUser =
                    getEndUserDao().getUser(lLogin,
                            authorizationService.isLoginCaseSensitive());
            pResultSummary.setEndUser(lUser);
        }
        else {
            pResultSummary.setEndUser(null);
        }

        /* UPDATE PRODUCT */
        final Product lProduct;
        if (pResultSummaryData.getFilterVisibilityConstraintData().getProductName() == null) {
            lProduct = null;
        }
        else {
            lProduct = getProduct(
                lBusinessProcessName,
                pResultSummaryData.getFilterVisibilityConstraintData().getProductName());
        }
        pResultSummary.setProduct(lProduct);

        /* UPDATE FIELDS CONTAINER*/
        // Remove previous fieldsContainer
        while (pResultSummary.getFieldsContainers().iterator().hasNext()) {
            FieldsContainer lFieldsContainer =
                    pResultSummary.getFieldsContainers().iterator().next();
            pResultSummary.removeFromFieldsContainerList(lFieldsContainer);
        }
        // add new ones
        for (String lFieldsContainerId : pResultSummaryData.getFieldsContainerIds()) {
            pResultSummary.addToFieldsContainerList(getFieldsContainer(lFieldsContainerId));
        }

        /* UPDATE SUMMARY FIELDS */
        // remove old ones
        pResultSummary.getSummaryFields().clear();

        // create all new ones
        for (UsableFieldData lUsableFieldData : pResultSummaryData.getUsableFieldDatas()) {
            SummaryField lSummaryField = SummaryField.newInstance();
            lSummaryField.setFilterField(createFilterField(lUsableFieldData));
            lSummaryField.setLabel(lUsableFieldData.getLabel());
            pResultSummary.addToSummaryFieldList(lSummaryField);
        }
    }

    private void updateResultSorter(ResultSorter pResultSorter,
            ResultSortingData pResultSortingData) {
        /* UPDATE LABEL KEY */
        pResultSorter.setName(pResultSortingData.getLabelKey());

        /* UPDATE BUSINESS PROCESS */
        String lBusinessProcessName =
                pResultSortingData.getFilterVisibilityConstraintData().getBusinessProcessName();
        final BusinessProcess lBusinessProcess =
                getBusinessProcess(lBusinessProcessName);
        pResultSorter.setBusinessProcess(lBusinessProcess);

        /* UPDATE END USER */
        String lLogin =
                pResultSortingData.getFilterVisibilityConstraintData().getUserLogin();
        if (lLogin != null) {
            EndUser lUser =
                    getEndUserDao().getUser(lLogin,
                            authorizationService.isLoginCaseSensitive());
            pResultSorter.setEndUser(lUser);
        }
        else {
            pResultSorter.setEndUser(null);
        }

        /* UPDATE PRODUCT */
        final Product lProduct;
        if (pResultSortingData.getFilterVisibilityConstraintData().getProductName() == null) {
            lProduct = null;
        }
        else {
            lProduct = getProduct(
                    lBusinessProcessName,
                    pResultSortingData.getFilterVisibilityConstraintData().getProductName());
        }
        pResultSorter.setProduct(lProduct);

        /* UPDATE FIELDS CONTAINER*/
        // Remove previous fieldsContainer
        while (pResultSorter.getFieldsContainers().iterator().hasNext()) {
            FieldsContainer lFieldsContainer =
                    pResultSorter.getFieldsContainers().iterator().next();
            pResultSorter.removeFromFieldsContainerList(lFieldsContainer);
        }

        // Add new fieldsContainer
        for (String lFieldsContainerId : pResultSortingData.getFieldsContainerIds()) {
            pResultSorter.addToFieldsContainerList(getFieldsContainer(lFieldsContainerId));
        }

        /* UPDATE RESULT FIELDS */
        for (ResultField lResultField : pResultSorter.getResultFields()) {
            resultFieldDao.remove(lResultField);
        }

        pResultSorter.getResultFields().clear();
        for (SortingFieldData lSortingFieldData : pResultSortingData.getSortingFieldDatas()) {
            ResultField lResultField = ResultField.newInstance();
            lResultField.setFilterField(createFilterField(lSortingFieldData.getUsableFieldData()));
            lResultField.setSortOrder(lSortingFieldData.getOrder());
            pResultSorter.addToResultFieldList(lResultField);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.service.SearchService#getUsableFields(java.lang.String,
     *      java.lang.String[], java.lang.String)
     */
    @Override
    public Map<String, UsableFieldData> getUsableFields(String pRoleToken,
            String[] pFieldsContainerIds, String pBusinessProcessName) {

        final Map<String, UsableFieldData> lUsableFields;
        if (pFieldsContainerIds == null) {
            throw new GDMException(
                    "Impossible to get UsableFields when no fields container");
        }
//        if (LOGGER.isInfoEnabled()) {
//            LOGGER.info(MessageFormat.format(
//                    "getUsableFields() called for {0} container(s)",
//                    pFieldsContainerIds.length));
//        }

        if (pFieldsContainerIds.length == 0) {
            lUsableFields = new TreeMap<String, UsableFieldData>();
        }
        else if (pFieldsContainerIds.length == 1) {
            checkSimilarFieldsContainers(pFieldsContainerIds);
            lUsableFields =
                    getUsableFieldData(pRoleToken, pFieldsContainerIds[0]);

        }
        else {
            checkSimilarFieldsContainers(pFieldsContainerIds);
            Map<String, UsableFieldData> lUsableFieldDataList =
                    getUsableFieldData(pRoleToken, pFieldsContainerIds[0]);

            for (int i = 1; i < pFieldsContainerIds.length; i++) {
                String lContainerId = pFieldsContainerIds[i];
                Map<String, UsableFieldData> lCurrentUsableFields =
                        getUsableFieldData(pRoleToken, lContainerId);
                if (!lCurrentUsableFields.isEmpty()) {
                    lUsableFieldDataList =
                            mixUsableFieldData(lUsableFieldDataList,
                                    lCurrentUsableFields);
                }
            }
            lUsableFields = lUsableFieldDataList;
        }
        return lUsableFields;
    }

    private void checkSimilarFieldsContainers(String[] pFieldsContainerIds) {
        boolean lRet = true;
        CacheableFieldsContainer lFieldsContainer =
                getCachedFieldsContainer(pFieldsContainerIds[0],
                        CACHE_IMMUTABLE_OBJECT);
        if (lFieldsContainer instanceof CacheableSheetType) {
            lRet = getSheetTypeDao().checkSheetTypeIds(pFieldsContainerIds);
        }
        else if (lFieldsContainer instanceof CacheableProductType) {
            lRet = getProductTypeDao().checkProductTypeIds(pFieldsContainerIds);
        }
        if (!lRet) {
            throw new GDMException(
                    "invalid list of fieldsContainer (mixed product types / sheet types)");
        }
    }

    /**
     * Get all the usable fields for filter for a fieldsContainer.
     * <p>
     * If the role token is not for an 'ADMIN' user, the confidential fields
     * have not added.
     * </p>
     * 
     * @param pRoleToken
     *            the role token
     * @param pFieldsContainerId
     *            the id of the field container
     * @param pBusinessProcess
     *            the businessProcess
     * @return the collection of usable field data
     */
    private Map<String, UsableFieldData> getUsableFieldData(String pRoleToken,
            String pFieldsContainerId) {
        Map<String, UsableFieldData> lUsableFields =
                new LinkedHashMap<String, UsableFieldData>();

        CacheableFieldsContainer lCacheableFieldsContainer =
                getCachedFieldsContainer(pFieldsContainerId,
                        CACHE_IMMUTABLE_OBJECT);
        lUsableFields.putAll(usableFieldsManager.getElements(pRoleToken,
                lCacheableFieldsContainer));

//        if (LOGGER.isDebugEnabled()) {
//            LOGGER.debug("Usable field size = " + lUsableFields.size());
//        }
        return lUsableFields;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.service.SearchService#getCompatibleOperators(org.topcased.gpm.business.search.service.UsableFieldData)
     */
    @Override
    public Collection<String> getCompatibleOperators(
            UsableFieldData pUsableFieldData) {
        return Operators.getCompatibleOperators(pUsableFieldData.getFieldType());
    }

    /**
     * Mix usable field datas if compatible
     * 
     * @param pUsableFields1
     *            the first usable field data list
     * @param pUsableFields2
     *            the second usable field data list
     * @return a new list of usable field data containing only the common usable
     *         fields
     */
    private Map<String, UsableFieldData> mixUsableFieldData(
            Map<String, UsableFieldData> pUsableFields1,
            Map<String, UsableFieldData> pUsableFields2) {
        Map<String, UsableFieldData> lUsableFields =
                new LinkedHashMap<String, UsableFieldData>();

        for (UsableFieldData lUsableFieldData1 : pUsableFields1.values()) {
            UsableFieldData lUsableFieldData2 =
                    pUsableFields2.get(lUsableFieldData1.getId());

            if (lUsableFieldData2 != null) {
                // Check if the two usable fields have the same field type
                boolean lSameFieldType =
                        (lUsableFieldData1.getFieldType().equals(lUsableFieldData2.getFieldType()));
                boolean lCanBeUse = lSameFieldType
                    && !(lUsableFieldData1.getMultivalued() || lUsableFieldData2.getMultivalued());
                if (lCanBeUse) {

                    /*
                     * The final scalarValueDatas contain the union of
                     * scalarValueDatas from UsableFieldData1 and
                     * scalarValueDatas from UsableFieldData2
                     */
                    if (!CollectionUtils.isEmpty(lUsableFieldData1.getPossibleValues())) {
                        List<String> lPossibleValues = new ArrayList<String>();

                        // add all values from UsableFieldData1
                        CollectionUtils.addAll(
                                lPossibleValues,
                                lUsableFieldData1.getPossibleValues().iterator());

                        // add all values from UsableFieldData2 not
                        // contained in UsableFieldData1
                        if (!CollectionUtils.isEmpty(lUsableFieldData1.getPossibleValues())) {
                            for (String lPossibleValue : lUsableFieldData2.getPossibleValues()) {
                                if (!lPossibleValues.contains(lPossibleValue)) {
                                    lPossibleValues.add(lPossibleValue);
                                }
                            }
                        }
                        lUsableFieldData1.setPossibleValues(lPossibleValues);
                    }
                    else {
                        lUsableFieldData1.setPossibleValues(lUsableFieldData2.getPossibleValues());
                    }

                    // If all the usable fields are considered similar,
                    // one is added in the list
                    if (lUsableFieldData1.getId().equals(
                            lUsableFieldData2.getId())) {
                        lUsableFields.put(lUsableFieldData1.getId(),
                                lUsableFieldData1);
                    }
                    else {
                        if (StringUtils.isNotBlank(lUsableFieldData1.getMultipleField())) {
                            lUsableFields.remove(lUsableFieldData1.getMultipleField());
                        }
                    }
                }
            }
        }
        cleanMultipleFields(lUsableFields);
        return lUsableFields;
    }

    /**
     * Remove the multiple fields that not have children.
     * 
     * @param pUsableFieldDatas
     *            Usable fields data map to clean.
     */
    private void cleanMultipleFields(
            Map<String, UsableFieldData> pUsableFieldDatas) {
        Map<String, UsableFieldData> lMultipleFields =
                new HashMap<String, UsableFieldData>();
        List<String> lSubFields = new ArrayList<String>();
        //Get the multiple fields and the multiple fields of sub fields.
        for (Map.Entry<String, UsableFieldData> lEntry : pUsableFieldDatas.entrySet()) {
            if (lEntry.getValue().getFieldType().equals(
                    FieldType.MULTIPLE_FIELD)) {
                lMultipleFields.put(lEntry.getKey(), lEntry.getValue());
            }
            if (StringUtils.isNotBlank(lEntry.getValue().getMultipleField())) {
                lSubFields.add(lEntry.getValue().getMultipleField());
            }
        }

        //Clean usable field data.
        if (!lMultipleFields.isEmpty()) {
            if (lSubFields.isEmpty()) {
                //No multiple fields
                for (String lKey : lMultipleFields.keySet()) {
                    pUsableFieldDatas.remove(lKey);
                }
            }
            else {
                for (String lKey : lMultipleFields.keySet()) {
                    if (!lSubFields.contains(lKey)) {
                        pUsableFieldDatas.remove(lKey);
                    }
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.service.SearchService#setMaxFieldsDepth(int)
     */
    @Override
    public void setMaxFieldsDepth(int pMaxFieldsDepth) {
        maxFieldsDepth = pMaxFieldsDepth;
        maxFieldsDepthGetted = true;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.service.SearchService#getMaxFieldsDepth()
     */
    @Override
    public int getMaxFieldsDepth() {
        if (!maxFieldsDepthGetted) {
            AttributeData[] lAttributeDatas =
                    getAttributesService().getGlobalAttributes(
                            new String[] { AttributesService.FILTER_FIELDS_MAX_DEPTH });

            int lMaxFieldsDepth;
            if (lAttributeDatas[0] != null) {
                lMaxFieldsDepth =
                        Integer.parseInt(lAttributeDatas[0].getValues()[0]);
            }
            else {
                lMaxFieldsDepth =
                        Integer.parseInt(AttributesService.FILTER_FIELDS_MAX_DEPTH_DEFAULT_VALUE);
            }
            //Get the first value of the first attribute
            setMaxFieldsDepth(lMaxFieldsDepth);
        }

        return maxFieldsDepth;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.service.SearchService#getUsableFieldDataId(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    @Override
    public String getUsableFieldDataId(final String pRoleToken,
            final String pBusinessProcessName, final String pHierarchy) {
        int lLevel = 1;
        String[] lRestOfHierarchy = pHierarchy.split("\\|");
        if (lRestOfHierarchy.length > 1) {
            String[] lSubRestedHierarchy =
                    new String[lRestOfHierarchy.length - 1];
            System.arraycopy(lRestOfHierarchy, 1, lSubRestedHierarchy, 0,
                    lRestOfHierarchy.length - 1);
            return getUsableFieldDataId(pRoleToken, pBusinessProcessName,
                    lRestOfHierarchy[0], lSubRestedHierarchy, lLevel,
                    new ArrayList<FilterFieldsContainerInfo>());
        }
        // else
        return pHierarchy;
    }

    /**
     * Recursive method to create the usable field data id.
     * <p>
     * Recursive until the last before element.<br />
     * The last element is considered as field label key.<br />
     * The last before as field's container.<br />
     * The level is raising if the previous fields container was a SheetType.
     * 
     * @param pRoleToken
     *            Role token to retrieve fields container
     * @param pBusinessProcessName
     *            Business process name to retrieve fields container
     * @param pCurrentFieldsContainerName
     *            Current fields container name
     * @param pRestOfHierarchy
     *            Hierarchy without the current fields container name
     * @param pCurrentLevel
     *            Current level of the hierarchy.
     * @return The usable fields data id (@see
     *         {@link SearchUtil#createUsableFieldDataId(String, String)})
     */
    private String getUsableFieldDataId(final String pRoleToken,
            final String pBusinessProcessName,
            final String pCurrentFieldsContainerName,
            final String[] pRestOfHierarchy, int pCurrentLevel,
            List<FilterFieldsContainerInfo> pParents) {
        String lFieldsContainerId =
                fieldsContainerServiceImpl.getFieldsContainerId(pRoleToken,
                        pCurrentFieldsContainerName);
        //Verify authorization
        if (lFieldsContainerId != null) {
            AccessControlContextData lAccessControlContextData =
                    new AccessControlContextData();
            //No product for a fields container
            lAccessControlContextData.setProductName(null);
            //No state for a fields container
            lAccessControlContextData.setStateName(null);
            lAccessControlContextData.setRoleName(getAuthService().getRoleNameFromToken(
                    pRoleToken));
            lAccessControlContextData.setContainerTypeId(lFieldsContainerId);
            TypeAccessControlData lTypeAccessControlData =
                    authorizationService.getTypeAccessControl(pRoleToken,
                            lAccessControlContextData);
            if (lTypeAccessControlData.getConfidential()) {
                throw new AuthorizationException(
                        "Illegal access to the container type "
                                + lFieldsContainerId
                                + " : the access is confidential ");
            }
        }

        CacheableFieldsContainer lCacheableFieldsContainer =
                getCachedFieldsContainer(lFieldsContainerId,
                        CACHE_IMMUTABLE_OBJECT);

        //Unnecessary to set the link direction
        FilterFieldsContainerInfo lFilterFieldsContainerInfo =
                createFilterFieldsContainerInfo(lFieldsContainerId, null);
        pParents.add(lFilterFieldsContainerInfo);

        //The last value is a field label key
        boolean lLast = pRestOfHierarchy.length == 1;
        if (lLast) {
            return SearchUtils.createUsableFieldDataId(pParents,
                    pRestOfHierarchy[0]);
        }
        // else
        //Raise the level if the current fields container is a SheetType
        if (CacheableSheetType.class.isInstance(lCacheableFieldsContainer)) {
            pCurrentLevel++;
        }

        //Copy the rest of hierarchy, except the first. It is the new current fields container
        String[] lSubRestOfHierarchy = new String[pRestOfHierarchy.length - 1];
        System.arraycopy(pRestOfHierarchy, 1, lSubRestOfHierarchy, 0,
                pRestOfHierarchy.length - 1);
        return getUsableFieldDataId(pRoleToken, pBusinessProcessName,
                pRestOfHierarchy[0], lSubRestOfHierarchy, pCurrentLevel,
                pParents);
    }

    /**
     * Get an entity from the business data structure for FilterField /
     * UsableField
     * 
     * @param pUsableFieldData
     *            the Usable field data (business data structure)
     * @return a FilterField data structure corresponding to the same data
     */
    private FilterField createFilterField(UsableFieldData pUsableFieldData) {
        // Set field label key
        FilterField lFilterField = FilterField.newInstance();
        lFilterField.setLabelKey(pUsableFieldData.getFieldName());

        // Set virtualFieldType and fieldsContainerHierarchy only for Virtual fields
        if (pUsableFieldData instanceof VirtualFieldData) {
            VirtualFieldData lVirtualFieldData =
                    (VirtualFieldData) pUsableFieldData;
            lFilterField.setVirtualFieldType(lVirtualFieldData.getVirtualFieldType().getValue());
        }

        if (CollectionUtils.isNotEmpty(pUsableFieldData.getFieldsContainerHierarchy())) {
            SearchUtils.addFieldsContainerHierarchyToFilterField(
                    pUsableFieldData.getFieldsContainerHierarchy(),
                    lFilterField);
            lFilterField.setFieldLevel(pUsableFieldData.getLevel());
        }
        filterFieldDao.create(lFilterField);

        return lFilterField;
    }

    /**
     * Get a UsableFieldData (business data structure) from a FilterField
     * (entity)
     * 
     * @see SearchServiceImpl#createUsableFieldData(String, String,
     *      org.topcased.gpm.business.serialization.data.Field,
     *      CacheableFieldsContainer)
     * @param pRoleToken
     *            current user session token.
     * @param pFieldsContainerIds
     *            List of container ids concerned by current filter
     * @param pBusinessProcessName
     *            current business process name.
     * @param pFilterField
     *            filter field entity
     * @return corresponding UsableFieldData (either VirtualField or not)
     */

    /**
     * Update the usable field data.
     * <p>
     * Sets:
     * <ul>
     * <li>Id
     * <li>Field name (field label key)
     * <li>Field type (data type for the field)
     * <li>Display name (translation of the label key, field name)
     * <li>Scalar value to an array of zero elements. Updated by createXXX
     * methods.
     * </ul>
     * 
     * @param pRoleToken
     *            Current role token (for translation)
     * @param pUsableFieldData
     *            Usable field data to update
     * @param pLabelKey
     *            Field label key
     * @param pFieldType
     *            Data type of the virtual field (CHOICE_FIELD, ATTACHED, ... )
     */

    /**
     * Get CacheableSheetType without access control. It's not a method of the
     * interface, internal use only
     * 
     * @param pLinkTypeId
     *            The id of the link type
     * @param pProperties
     *            The cache properties
     * @return The cacheableLinkType
     * @throws IllegalArgumentException
     *             If not found.
     */
    private CacheableLinkType getCacheableLinkType(String pLinkTypeId,
            CacheProperties pProperties) throws IllegalArgumentException {
        CacheableLinkType lCachedLinkType =
                getCachedElement(CacheableLinkType.class, pLinkTypeId,
                        pProperties.getCacheFlags());

        if (null == lCachedLinkType) {
            org.topcased.gpm.domain.link.LinkType lLinkTypeEntity =
                    getLinkType(pLinkTypeId);
            lCachedLinkType = new CacheableLinkType(lLinkTypeEntity);
            addElementInCache(lCachedLinkType);
        }

        return lCachedLinkType;
    }

    /**
     * Converts a FieldsContainerId entity object of a FilterFieldsContainerInfo
     * value object.
     * <p>
     * For 'LINK' fields container, the 'link direction' attribute is setting by
     * using first following identifier of the fields container (in the
     * hierarchy). And previous if no next.
     * 
     * @param pPreviousFieldsContainerId
     *            Identifier of the previous fields container in the hierarchy.
     * @param pFieldsContainerId
     *            FieldsContainerId object to convert.
     * @param pNextFieldsContainerId
     *            Identifier of the next fields container in the hierarchy.
     * @return A FilterFieldsContainerInfo corresponding to the
     *         FieldsContainerId entity.
     */
    private FilterFieldsContainerInfo toFilterFieldsContainerInfo(
            String pPreviousFieldsContainerId,
            FieldsContainerId pFieldsContainerId, String pNextFieldsContainerId) {
        FilterFieldsContainerInfo lFilterFieldsContainerInfo =
                createFilterFieldsContainerInfo(pFieldsContainerId, null);
        //Set the link direction
        if (lFilterFieldsContainerInfo.getType().equals(
                FieldsContainerType.LINK)) {
            CacheableLinkType lCacheableLinkType =
                    getCacheableLinkType(lFilterFieldsContainerInfo.getId(),
                            CacheProperties.IMMUTABLE);

            if (StringUtils.isNotBlank(pNextFieldsContainerId)) {
                lFilterFieldsContainerInfo.setLinkDirection(LinkDirection.getToLinkDirection(
                        pNextFieldsContainerId,
                        lCacheableLinkType.getOriginTypeId(),
                        lCacheableLinkType.getDestinationTypeId()));
            }
            else if (StringUtils.isNotBlank(pPreviousFieldsContainerId)) {
                lFilterFieldsContainerInfo.setLinkDirection(LinkDirection.getFromLinkDirection(
                        pPreviousFieldsContainerId,
                        lCacheableLinkType.getOriginTypeId(),
                        lCacheableLinkType.getDestinationTypeId()));
            }
            //else UNDEFINED (the default value)
        }

        return lFilterFieldsContainerInfo;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.service.SearchService#toFilterFieldsContainerInfos(java.lang.String,
     *      java.util.Collection)
     */
    @Override
    public List<FilterFieldsContainerInfo> toFilterFieldsContainerInfos(
            String pFieldsContainerId,
            final Collection<FieldsContainerId> pVirtualFieldDataHierarchy) {
        List<FilterFieldsContainerInfo> lParents = Collections.emptyList();

        List<FieldsContainerId> lVirtualFieldDataHierarchy =
                new ArrayList<FieldsContainerId>(pVirtualFieldDataHierarchy);
        if (!pVirtualFieldDataHierarchy.isEmpty()) {
            lParents =
                    new ArrayList<FilterFieldsContainerInfo>(
                            pVirtualFieldDataHierarchy.size());
            //Get identifiers
            String lPreviousFieldsContainerId = pFieldsContainerId;
            String lNextFieldsContainerId = null;
            for (int i = 0; i < pVirtualFieldDataHierarchy.size(); i++) {
                FieldsContainerId lFieldsContainerId =
                        lVirtualFieldDataHierarchy.get(i);

                if (i < lVirtualFieldDataHierarchy.size() - 1) {
                    lNextFieldsContainerId =
                            lVirtualFieldDataHierarchy.get(i + 1).getIdentificator();
                }
                else {
                    lNextFieldsContainerId = StringUtils.EMPTY;
                }
                FilterFieldsContainerInfo lFilterFieldsContainerInfo =
                        toFilterFieldsContainerInfo(lPreviousFieldsContainerId,
                                lFieldsContainerId, lNextFieldsContainerId);
                lParents.add(lFilterFieldsContainerInfo);

                lPreviousFieldsContainerId =
                        lFieldsContainerId.getIdentificator();
            }
        }
        return lParents;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.service.SearchService#createFilterFieldsContainerInfo(java.lang.String)
     */
    @Override
    public FilterFieldsContainerInfo createFilterFieldsContainerInfo(
            final String pFieldsContainerId, final String pSheetTypeId)
        throws InvalidIdentifierException, RuntimeException {
        CacheableFieldsContainer lCacheableFieldsContainer =
                getCachedFieldsContainer(pFieldsContainerId,
                        CACHE_IMMUTABLE_OBJECT);
        CacheableSheetType lCacheableSheetType =
                getCachedElement(CacheableSheetType.class, pSheetTypeId,
                        CACHE_IMMUTABLE_OBJECT);
        FilterFieldsContainerInfo lFilterFieldsContainerInfo =
                createFilterFieldsContainerInfo(lCacheableFieldsContainer,
                        lCacheableSheetType);

        return lFilterFieldsContainerInfo;
    }

    /**
     * Use FieldsContainerId object
     * 
     * @see SearchService#createFilterFieldsContainerInfo(String)
     */
    private FilterFieldsContainerInfo createFilterFieldsContainerInfo(
            final FieldsContainerId pFieldsContainerId,
            final FieldsContainerId pSheetFieldsContainerId) {
        CacheableFieldsContainer lCacheableFieldsContainer =
                getCachedFieldsContainer(pFieldsContainerId.getIdentificator(),
                        CACHE_IMMUTABLE_OBJECT);
        CacheableSheetType lCacheableSheetType = null;

        if (pSheetFieldsContainerId != null) {
            try {
                lCacheableSheetType =
                        getCachedElement(CacheableSheetType.class,
                                pSheetFieldsContainerId.getIdentificator(),
                                CACHE_IMMUTABLE_OBJECT);
            }
            catch (ClassCastException e) {

            }
        }
        FilterFieldsContainerInfo lFilterFieldsContainerInfo =
                createFilterFieldsContainerInfo(lCacheableFieldsContainer,
                        lCacheableSheetType);
        return lFilterFieldsContainerInfo;
    }

    /**
     * Creates a FilterFieldsContainerInfo according to the fields container.
     * 
     * @param pCacheableFieldsContainer
     *            fields container
     * @return Created FilterFieldsContainerInfo.
     * @throws RuntimeException
     *             If cannot identify the type of the fields container (@link
     *             {@link FieldsContainerType#valueOf(CacheableFieldsContainer)}
     *             .
     */
    private FilterFieldsContainerInfo createFilterFieldsContainerInfo(
            final CacheableFieldsContainer pCacheableFieldsContainer,
            final CacheableSheetType pCacheableSheetType)
        throws RuntimeException {
        FilterFieldsContainerInfo lFilterFieldsContainerInfo =
                new FilterFieldsContainerInfo();
        lFilterFieldsContainerInfo.setId(pCacheableFieldsContainer.getId());
        lFilterFieldsContainerInfo.setLabelKey(pCacheableFieldsContainer.getName());
        FieldsContainerType lFieldsContainerType =
                FieldsContainerType.valueOf(pCacheableFieldsContainer);
        lFilterFieldsContainerInfo.setType(lFieldsContainerType);
        if (lFieldsContainerType.equals(FieldsContainerType.LINK)) {
            CacheableLinkType lCacheableLinkType =
                    (CacheableLinkType) pCacheableFieldsContainer;
            if (pCacheableSheetType != null) {
                lFilterFieldsContainerInfo.setLinkDirection(LinkDirection.getToLinkDirection(
                        pCacheableSheetType.getId(),
                        lCacheableLinkType.getOriginTypeId(),
                        lCacheableLinkType.getDestinationTypeId()));
            }
        }
        return lFilterFieldsContainerInfo;
    }

    /* THE DAO ELEMENTS */

    private OperationDao operationDao;

    private StringValueDao stringValueDao;

    private DateValueDao dateValueDao;

    private IntegerValueDao integerValueDao;

    private RealValueDao realValueDao;

    private BooleanValueDao booleanValueDao;

    private ResultFieldDao resultFieldDao;

    private CriteriaDao criteriaDao;

    private FilterFieldDao filterFieldDao;

    /**
     * get the filter dao
     * 
     * @return the filter dao
     */
    final protected FilterDao getFilterDao() {
        return filterDao;
    }

    /**
     * set the filterDao parameter
     * 
     * @param pFilterDao
     *            the parameter to set
     */
    public void setFilterDao(FilterDao pFilterDao) {
        filterDao = pFilterDao;
    }

    public void setCriteriaDao(CriteriaDao pCriteriaDao) {
        criteriaDao = pCriteriaDao;
    }

    public void setOperationDao(OperationDao pOperationDao) {
        operationDao = pOperationDao;
    }

    public void setBooleanValueDao(BooleanValueDao pBooleanValueDao) {
        booleanValueDao = pBooleanValueDao;
    }

    public void setDateValueDao(DateValueDao pDateValueDao) {
        dateValueDao = pDateValueDao;
    }

    public void setIntegerValueDao(IntegerValueDao pIntegerValueDao) {
        integerValueDao = pIntegerValueDao;
    }

    public void setRealValueDao(RealValueDao pRealValueDao) {
        realValueDao = pRealValueDao;
    }

    public void setStringValueDao(StringValueDao pStringValueDao) {
        stringValueDao = pStringValueDao;
    }

    public void setResultSorterDao(ResultSorterDao pResultSorterDao) {
        resultSorterDao = pResultSorterDao;
    }

    public void setResultSummaryDao(ResultSummaryDao pResultSummaryDao) {
        resultSummaryDao = pResultSummaryDao;
    }

    public void setFilterComponentDao(FilterComponentDao pFilterComponentDao) {
        filterComponentDao = pFilterComponentDao;
    }

    public void setFilterWithResultDao(FilterWithResultDao pFilterWithResultDao) {
        filterWithResultDao = pFilterWithResultDao;
    }

    public void setProductScopeDao(ProductScopeDao pProductScopeDao) {
        productScopeDao = pProductScopeDao;
    }

    public void setResultFieldDao(ResultFieldDao pResultFieldDao) {
        resultFieldDao = pResultFieldDao;
    }

    public void setFilterFieldDao(FilterFieldDao pFilterFieldDao) {
        filterFieldDao = pFilterFieldDao;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.service.SearchService#getFilterSummariesBySheetType(java.lang.String,
     *      java.lang.String, java.lang.String, java.lang.String,
     *      java.lang.String)
     * @deprecated
     */
    @Override
    public List<FilterData> getVisibleFilterDatasBySheetType(String pRoleToken,
            String pSheetTypeName, FilterScope pFilterScope) {

        String lProcessName =
                getAuthService().getProcessNameFromToken(pRoleToken);
        String lProductName =
                getAuthService().getProductNameFromSessionToken(pRoleToken);
        String lUserLogin = getAuthService().getLoginFromToken(pRoleToken);

        String lSheetTypeId =
                getSheetService().getCacheableSheetTypeByName(pRoleToken,
                        lProcessName, pSheetTypeName, CacheProperties.IMMUTABLE).getId();

        List<FilterData> lVisibleFilterDatas =
                new ArrayList<FilterData>(getVisibleFilterDatas(pRoleToken,
                        lProcessName, lProductName, lUserLogin));

        // Remove filters of other sheet types
        for (int i = lVisibleFilterDatas.size() - 1; i >= 0; i--) {
            String[] lFieldsContainerIds =
                    lVisibleFilterDatas.get(i).getFieldsContainerIds();

            if (lFieldsContainerIds.length != 1
                    || !lFieldsContainerIds[0].equals(lSheetTypeId)) {
                lVisibleFilterDatas.remove(i);
            }
        }

        // Remove filters of other scope
        if (pFilterScope != null) {
            for (int i = lVisibleFilterDatas.size() - 1; i >= 0; i--) {
                FilterVisibilityConstraintData lFilterVisibilityConstraintData =
                        lVisibleFilterDatas.get(i).getFilterVisibilityConstraintData();

                if ((pFilterScope.equals(FilterScope.INSTANCE_FILTER)
                        && (lFilterVisibilityConstraintData.getProductName() != null
                            || lFilterVisibilityConstraintData.getUserLogin() != null))
                    || (pFilterScope.equals(FilterScope.PRODUCT_FILTER)
                        && lFilterVisibilityConstraintData.getProductName() == null)
                    || (pFilterScope.equals(FilterScope.USER_FILTER)
                        && lFilterVisibilityConstraintData.getUserLogin() == null)) {
                    lVisibleFilterDatas.remove(i);
                }
            }
        }
        return lVisibleFilterDatas;
    }

    /**
     * Create a serializable filter.
     * 
     * @param pRoleToken
     *            The role token.
     * @param pFilterId
     *            The filter id.
     * @return The serializable filter.
     */
    // TODO : declare in interface
    public org.topcased.gpm.business.serialization.data.Filter getSerializableFilters(
            final String pRoleToken, final String pFilterId) {
        final org.topcased.gpm.business.serialization.data.Filter lFilter;
        final ExecutableFilterData lExecutableFilter = getExecutableFilter(pRoleToken, pFilterId);

        if (lExecutableFilter == null) {
            lFilter = null;
        }
        else {
            // Define filter type
            if (lExecutableFilter.getFilterData().getType().equals(
                    FilterTypeData.PRODUCT)) {
                lFilter = new ProductFilter();
            }
            else {
                lFilter = new SheetFilter();
            }

            // set informations filter
            //obfuscate filter name & description
            if (ReadProperties.getInstance().isObfFilters()) {
                long lDateInMillisecond = System.nanoTime();
                lFilter.setLabelKey("Filter" + lDateInMillisecond);
                lFilter.setDescription("Filter" + lDateInMillisecond
                        + "_description");
            }
            else {
                lFilter.setLabelKey(lExecutableFilter.getLabelKey());
                lFilter.setDescription(lExecutableFilter.getDescription());
            }

            lFilter.setFilterUsage(FilterUsageConverter.getGpmtoXml(lExecutableFilter.getUsage()));
            lFilter.setHidden(lExecutableFilter.isHidden());
            lFilter.setProductName(lExecutableFilter.getFilterVisibilityConstraintData().getProductName());

            if ((ExportationData.getInstance().getUserLogin().get(
                    lExecutableFilter.getFilterVisibilityConstraintData().getUserLogin()) != null)
                    && (ReadProperties.getInstance().isObfUsers())) {
                lFilter.setUserLogin(ExportationData.getInstance().getUserLogin().get(
                        lExecutableFilter.getFilterVisibilityConstraintData().getUserLogin()));
            }
            else {
                lFilter.setUserLogin(lExecutableFilter.getFilterVisibilityConstraintData().getUserLogin());
            }

            // set containers filter
            lFilter.setContainers(new ArrayList<NamedElement>());
            String[] lContainersIds =
                    lExecutableFilter.getFilterData().getFieldsContainerIds();
            NamedElement lContainerName;
            for (int i = 0; i < lContainersIds.length; i++) {
                if (lExecutableFilter.getFilterData().getType().equals(
                        FilterTypeData.PRODUCT)) {
                    lContainerName = new ProductTypeRef();
                }
                else {
                    lContainerName = new SheetTypeRef();
                }
                lContainerName.setName(getCachedFieldsContainer(
                        lContainersIds[i], CACHE_IMMUTABLE_OBJECT).getName());
                lFilter.getContainers().add(lContainerName);
            }

            // set criteria
            final ArrayList<CriteriaGroup> lCriteriaGroups =
                    new ArrayList<CriteriaGroup>();
            final CriteriaData lGlobalCriteria =
                    lExecutableFilter.getFilterData().getCriteriaData();

            if (lGlobalCriteria != null) {
                if (lGlobalCriteria instanceof OperationData
                        && ((OperationData) lGlobalCriteria).getOperator().equals(
                                Operators.OR)) {
                    for (CriteriaData lCriteriaField :
                            ((OperationData) lGlobalCriteria).getCriteriaDatas()) {
                        lCriteriaGroups.add(createCriteriaGroup(lCriteriaField));
                    }
                }
                else {
                    lCriteriaGroups.add(createCriteriaGroup(lGlobalCriteria));
                }
            }
            if (!lCriteriaGroups.isEmpty()) {
                lFilter.setCriteriaGroups(lCriteriaGroups);
            }

            // set result fields
            final List<FieldResult> lResultSummary =
                    new ArrayList<FieldResult>();

            for (UsableFieldData lUsableFieldData :
                    lExecutableFilter.getResultSummaryData().getUsableFieldDatas()) {
                FieldResult lFieldResult = new FieldResult();
                lFieldResult.setName(SearchUtils.getFieldName(lUsableFieldData));
                lFieldResult.setLabel(lUsableFieldData.getLabel());

                lResultSummary.add(lFieldResult);
            }

            if (lExecutableFilter.getResultSortingData() != null) {
                for (SortingFieldData lSortingFieldData :
                        lExecutableFilter.getResultSortingData().getSortingFieldDatas()) {
                    boolean lIsVisible = false;
                    String lFieldName =
                            SearchUtils.getFieldName(lSortingFieldData.getUsableFieldData());
                    for (FieldResult lFieldResult : lResultSummary) {
                        if (lFieldResult.getName().equals(lFieldName)) {
                            lIsVisible = true;
                            lFieldResult.setSort(lSortingFieldData.getOrder());
                        }
                    }
                    if (!lIsVisible) {
                        FieldResult lFieldResult = new FieldResult();
                        lFieldResult.setName(lFieldName);
                        lFieldResult.setLabel(lSortingFieldData.getUsableFieldData().getLabel());
                        lFieldResult.setSort(lSortingFieldData.getOrder());
                        lFieldResult.setDisplayed(false);

                        lResultSummary.add(lFieldResult);
                    }
                }
            }
            if (!lResultSummary.isEmpty()) {
                lFilter.setResultSummary(lResultSummary);
            }

            // set scope
            final ArrayList<ProductScope> lProductScopes =
                    new ArrayList<ProductScope>();

            for (FilterProductScope lFilterProductScope :
                    lExecutableFilter.getFilterProductScopes()) {
                final ProductScope lProductScope = new ProductScope();

                lProductScope.setName(lFilterProductScope.getProductName());
                lProductScope.setIncludeSubProducts(lFilterProductScope.isIncludeSubProducts());
                lProductScopes.add(lProductScope);
            }
            if (!lProductScopes.isEmpty()
                    && ReadProperties.getInstance().isObfProducts()) {
                for (ProductScope lProductSc : lProductScopes) {
                    final HashMap<String, String> lProductNames =
                            ExportationData.getInstance().getProductNames();
                    final String lProductName = lProductSc.getName();
                    if (lProductNames.get(lProductName) != null) {
                        lProductSc.setName(lProductNames.get(lProductName));
                    }
                }
                lFilter.setScope(lProductScopes);
            }

        }

        return lFilter;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.service.SearchService#getSerializableFilters()
     */
    @Override
    public List<org.topcased.gpm.business.serialization.data.Filter> getSerializableFilters(
            final String pRoleToken) {
        final List<org.topcased.gpm.business.serialization.data.Filter> lFilters =
                new ArrayList<org.topcased.gpm.business.serialization.data.Filter>();

        for (FilterWithResult lFilterEntity : filterWithResultDao.loadAll()) {
            final org.topcased.gpm.business.serialization.data.Filter lFilter =
                    getSerializableFilters(pRoleToken, lFilterEntity.getId());

            if (lFilter != null) {
                lFilters.add(lFilter);
            }
        }

        return lFilters;
    }

    private CriteriaGroup createCriteriaGroup(final CriteriaData pCriteria) {
        final CriteriaGroup lGroup = new CriteriaGroup();

        lGroup.setCriterionList(new LinkedList<Criterion>());
        if (pCriteria != null) {
            if (pCriteria instanceof CriteriaFieldData) {
                lGroup.getCriterionList().add(
                        createCriterion((CriteriaFieldData) pCriteria));
            }
            else if (pCriteria instanceof OperationData) {
                for (CriteriaData lCriteriaField : ((OperationData) pCriteria).getCriteriaDatas()) {
                    lGroup.getCriterionList().add(
                            createCriterion((CriteriaFieldData) lCriteriaField));
                }
            }
        }

        return lGroup;
    }

    private Criterion createCriterion(final CriteriaFieldData pCriteriaField) {
        // Get criteria field name
        final String lFieldName =
                SearchUtils.getFieldName(pCriteriaField.getUsableFieldData());

        // Get criteria operator
        final String lOperator =
                OperatorConverter.getGpmtoXml(pCriteriaField.getOperator());

        // Get criteria value
        final ScalarValueData lScalarValueData =
                pCriteriaField.getScalarValueData();
        String lValue = null;

        if (lScalarValueData instanceof StringValueData) {
            lValue = ((StringValueData) lScalarValueData).getValue();
        }
        else if (lScalarValueData instanceof IntegerValueData) {
            lValue =
                    String.valueOf(((IntegerValueData) lScalarValueData).getValue());
        }
        else if (lScalarValueData instanceof RealValueData) {
            lValue =
                    String.valueOf(((RealValueData) lScalarValueData).getValue());
        }
        else if (lScalarValueData instanceof BooleanValueData) {
            lValue =
                    String.valueOf(((BooleanValueData) lScalarValueData).getValue());
        }
        else if (lScalarValueData instanceof DateValueData) {
            Date lDate = ((DateValueData) lScalarValueData).getValue();
            if (lDate != null) {
                lValue = DateFormatUtils.ISO_DATE_FORMAT.format(lDate);
            }
        }

        return new Criterion(lFieldName, lOperator, lValue);
    }

    public FilterManager getFilterManager() {
        return filterManager;
    }

    public void setFilterManager(FilterManager pFilterManager) {
        filterManager = pFilterManager;
    }

    public UsableFieldsManager getUsableFieldsManager() {
        return usableFieldsManager;
    }

    public void setUsableFieldsManager(UsableFieldsManager pUsableFieldsManager) {
        usableFieldsManager = pUsableFieldsManager;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.service.SearchService#getUsableField(java.lang.String,
     *      java.lang.String, java.util.Collection)
     */
    public UsableFieldData getUsableField(String pProcessName,
            String pCriterionLabelKey, Collection<String> pFieldsContainerIds) {
        Iterator<String> lIterator = pFieldsContainerIds.iterator();
        final UsableFieldData lUsableFieldData;
        if (lIterator.hasNext()) {
            UsableFieldCacheKey lCacheKey =
                    new UsableFieldCacheKey(pProcessName, lIterator.next(),
                            pCriterionLabelKey);
            UsableFieldData lFirstUsableField =
                    usableFieldsManager.getElement(lCacheKey);
            if (lIterator.hasNext() && lFirstUsableField != null) {
                // More than 1 fields container identifier
                if (lFirstUsableField.getMultivalued()) {
                    lUsableFieldData = null;
                }
                else {
                    List<UsableFieldData> lUsableFieldDataList =
                            new ArrayList<UsableFieldData>();
                    while (lIterator.hasNext()) {
                        lCacheKey.setFieldsContainerId(lIterator.next());
                        UsableFieldData lUfd =
                                usableFieldsManager.getElement(lCacheKey);
                        if (lUfd != null) {
                            lUsableFieldDataList.add(lUfd);
                        }
                    }
                    if (lUsableFieldDataList.isEmpty()) {
                        lUsableFieldData = null;
                    }
                    else {
                        //mix
                        if (!CollectionUtils.isEmpty(
                                lFirstUsableField.getPossibleValues())) {
                            List<String> lPossibleValues =
                                    new ArrayList<String>();

                            // add all values from UsableFieldData1
                            CollectionUtils.addAll(
                                    lPossibleValues,
                                    lFirstUsableField.getPossibleValues().iterator());

                            for (UsableFieldData lData : lUsableFieldDataList) {
                                for (String lPossibleValue : lData.getPossibleValues()) {
                                    if (!lPossibleValues.contains(lPossibleValue)) {
                                        lPossibleValues.add(lPossibleValue);
                                    }
                                }
                            }
                            lFirstUsableField.setPossibleValues(lPossibleValues);
                        }
                        lUsableFieldData = lFirstUsableField;
                    }
                }
            }
            else {
                lUsableFieldData = lFirstUsableField;
            }
        }
        else {//Fields container id list is empty
            UsableFieldCacheKey lCacheKey =
                    new UsableFieldCacheKey(pProcessName, StringUtils.EMPTY,
                            pCriterionLabelKey);
            lUsableFieldData = usableFieldsManager.getElement(lCacheKey);
        }

        return lUsableFieldData;
    }

    /**
     * Clear usable field manager
     */
    public void clearUsableFieldManager() {
        usableFieldsManager.depreciateAll();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.service.SearchService#getResultsLimit()
     */
    public int getResultsLimit() {
        return filterManager.getMaxNbResults();
    }

    /**
     * Test the existence of the filter.
     * 
     * @param pProcessName
     *            Process name
     * @param pProductName
     *            (optional) Product's name
     * @param pUserLogin
     *            (optional) User's login
     * @param pFilterName
     *            Filter's name
     * @return True if the filter exists, false otherwise.
     */
    public boolean isExists(final String pProcessName,
            final String pProductName, final String pUserLogin,
            final String pFilterName) {
        return filterWithResultDao.isExists(pProcessName, pProductName,
                pUserLogin, pFilterName);
    }

    /**
     * Retrieve the filter's technical identifier.
     * 
     * @param pProcessName
     *            Process name
     * @param pProductName
     *            (optional) Product's name
     * @param pUserLogin
     *            (optional) User's login
     * @param pFilterName
     *            Filter's name
     * @return Filter's technical identifier.
     */
    public String getId(final String pProcessName, final String pProductName,
            final String pUserLogin, final String pFilterName) {
        return filterWithResultDao.getId(pProcessName, pProductName,
                pUserLogin, pFilterName);
    }

    /**
     * Retrieve the filter's criteria component technical identifier.
     * 
     * @param pFilterId
     *            Filter's identifier attached the criteria component.
     * @return Technical identifier of the criteria component.
     */
    public String getCriteriaComponentId(final String pFilterId) {
        return filterWithResultDao.getCriteriaId(pFilterId);
    }

    /**
     * Retrieve the filter's result fields component technical identifier.
     * 
     * @param pFilterId
     *            Filter's identfier attached the result fields component.
     * @return Technical identifier of the result fields component.
     */
    public String getResultFieldsComponentId(final String pFilterId) {
        return filterWithResultDao.getResultFieldsId(pFilterId);
    }

    /**
     * Retrieve the filter's sort fields component technical identifier.
     * 
     * @param pFilterId
     *            Filter's identfier attached the sort fields component.
     * @return Technical identifier of the sort fields component.
     */
    public String getSortFieldsComponentId(final String pFilterId) {
        return filterWithResultDao.getSortFieldsId(pFilterId);
    }
}
