/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.search.impl.filter;

import java.util.ArrayList;
import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.cache.AbstractCacheManager;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.link.impl.LinkDirection;
import org.topcased.gpm.business.search.criterias.CriteriaData;
import org.topcased.gpm.business.search.criterias.CriteriaFieldData;
import org.topcased.gpm.business.search.criterias.FilterData;
import org.topcased.gpm.business.search.criterias.FilterTypeData;
import org.topcased.gpm.business.search.criterias.OperationData;
import org.topcased.gpm.business.search.criterias.impl.FilterFieldsContainerInfo;
import org.topcased.gpm.business.search.impl.SearchUtils;
import org.topcased.gpm.business.search.result.sorter.ResultSortingData;
import org.topcased.gpm.business.search.result.sorter.SortingFieldData;
import org.topcased.gpm.business.search.result.summary.ResultSummaryData;
import org.topcased.gpm.business.search.service.ExecutableFilterData;
import org.topcased.gpm.business.search.service.FilterProductScope;
import org.topcased.gpm.business.search.service.FilterVisibilityConstraintData;
import org.topcased.gpm.business.search.service.UsableFieldData;
import org.topcased.gpm.domain.accesscontrol.EndUser;
import org.topcased.gpm.domain.businessProcess.BusinessProcess;
import org.topcased.gpm.domain.fields.FieldsContainer;
import org.topcased.gpm.domain.product.Product;
import org.topcased.gpm.domain.search.Criteria;
import org.topcased.gpm.domain.search.Filter;
import org.topcased.gpm.domain.search.FilterElement;
import org.topcased.gpm.domain.search.FilterField;
import org.topcased.gpm.domain.search.FilterWithResult;
import org.topcased.gpm.domain.search.FilterWithResultDao;
import org.topcased.gpm.domain.search.Operation;
import org.topcased.gpm.domain.search.ProductScope;
import org.topcased.gpm.domain.search.result.sorter.ResultField;
import org.topcased.gpm.domain.search.result.sorter.ResultSorter;
import org.topcased.gpm.domain.search.result.summary.ResultSummary;
import org.topcased.gpm.domain.search.result.summary.SummaryField;
import org.topcased.gpm.util.lang.CopyUtils;

/**
 * Used to managed the filter data. For each filter, the cache contains an
 * ExecutableFilterData.
 * 
 * @author tpanuel
 */
public class FilterDataManager extends
        AbstractCacheManager<FilterDataKey, ExecutableFilterData> {
    private FilterWithResultDao filterWithResultDao;

    private Cache filterDataCache;

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.cache.AbstractCacheManager#getCache()
     */
    @Override
    protected Cache getCache() {
        return filterDataCache;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.cache.AbstractCacheManager#getElement(org.topcased.gpm.business.cache.CacheKey)
     */
    @Override
    public ExecutableFilterData getElement(final FilterDataKey pElementKey) {
        // Use deep clone to fix problem with mutable/immutable and ScalarValueData 
        final Element lCachedElement = getCache().get(pElementKey);
        final ExecutableFilterData lValue;

        if (lCachedElement == null) {
            lValue = load(pElementKey);
            getCache().put(new Element(pElementKey, lValue));
        }
        else {
            lValue = (ExecutableFilterData) lCachedElement.getValue();
        }

        return CopyUtils.deepClone(lValue);
    }

    /**
     * Setter on cache used for Spring injection.
     * 
     * @param pCache
     *            The cache.
     */
    public void setFilterDataCache(final Cache pCache) {
        filterDataCache = pCache;
    }

    /**
     * Setter on DAO used for Spring injection.
     * 
     * @param pFilterWithResultDao
     *            The DAO.
     */
    public void setFilterWithResultDao(
            final FilterWithResultDao pFilterWithResultDao) {
        filterWithResultDao = pFilterWithResultDao;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.cache.AbstractCacheManager#load(java.io.Serializable)
     */
    @Override
    protected ExecutableFilterData load(final FilterDataKey pElementKey) {
        final FilterWithResult lDomainFilterWithResult =
                (FilterWithResult) filterWithResultDao.load(pElementKey.getFilterId());
        final FilterData lFilterData =
                convertFilter(lDomainFilterWithResult.getFilter());
        final ResultSummaryData lResultSummary =
                convertResultSummary(lDomainFilterWithResult.getResultSummary());
        final ResultSortingData lResultSorting;
        if (lDomainFilterWithResult.getResultSorter() == null) {
            lResultSorting = null;
        }
        else {
            lResultSorting =
                    convertResultSorting(lDomainFilterWithResult.getResultSorter());
        }
        final FilterVisibilityConstraintData lVisibilityConstraint =
                convertFilterVisibilityConstraint(
                        lDomainFilterWithResult.getEndUser(),
                        lDomainFilterWithResult.getBusinessProcess(),
                        lDomainFilterWithResult.getProduct());
        final ArrayList<FilterProductScope> lProductScopes =
                new ArrayList<FilterProductScope>();

        for (ProductScope lDomainScope : lDomainFilterWithResult.getProductScopes()) {
            lProductScopes.add(new FilterProductScope(
                    lDomainScope.getProductName(),
                    lDomainScope.isIncludeSubProducts()));
        }

        return new ExecutableFilterData(
                lDomainFilterWithResult.getId(),
                lDomainFilterWithResult.getName(),
                lDomainFilterWithResult.getDescription(),
                lDomainFilterWithResult.getUsage().getValue(),
                lDomainFilterWithResult.isHidden(),
                lResultSummary,
                lResultSorting,
                lFilterData,
                lVisibilityConstraint,
                lProductScopes.toArray(new FilterProductScope[lProductScopes.size()]));
    }

    /**
     * Convert a domain filter to a business one.
     * 
     * @param pDomainFilter
     *            The domain filter.
     * @return The business filter.
     */
    public FilterData convertFilter(final Filter pDomainFilter) {
        final ArrayList<String> lTypeIds =
                new ArrayList<String>(
                        pDomainFilter.getFieldsContainers().size());

        for (FieldsContainer lFieldsContainer : pDomainFilter.getFieldsContainers()) {
            lTypeIds.add(lFieldsContainer.getId());
        }

        final FilterTypeData lFilterType =
                FilterTypeData.fromString(pDomainFilter.getType().toString());
        final CriteriaData lCriteria =
                convertCriteria(pDomainFilter.getBusinessProcess().getName(),
                        lTypeIds, pDomainFilter.getFilterElement());
        final FilterVisibilityConstraintData lVisibilityConstraint =
                convertFilterVisibilityConstraint(pDomainFilter.getEndUser(),
                        pDomainFilter.getBusinessProcess(),
                        pDomainFilter.getProduct());

        return new FilterData(pDomainFilter.getId(), pDomainFilter.getName(),
                pDomainFilter.isModel(),
                lTypeIds.toArray(new String[lTypeIds.size()]), lFilterType,
                lCriteria, lVisibilityConstraint);
    }

    /**
     * Convert a domain criteria to a business one.
     * 
     * @param pProcessName
     *            The name of the business process.
     * @param pTypeIds
     *            The types used by the parent filter.
     * @param pDomainCriteria
     *            The domain criteria.
     * @return The business criteria.
     */
    public CriteriaData convertCriteria(final String pProcessName,
            final List<String> pTypeIds, final FilterElement pDomainCriteria) {
        if (pDomainCriteria == null) {
            return null;
        }
        else if (pDomainCriteria instanceof Operation) {
            return convertOperation(pProcessName, pTypeIds,
                    (Operation) pDomainCriteria);
        }
        else if (pDomainCriteria instanceof Criteria) {
            final Criteria lCriteria = (Criteria) pDomainCriteria;
            //The ancestors (without the direct parent)
            final List<FilterFieldsContainerInfo> lParents =
                    new ArrayList<FilterFieldsContainerInfo>();

            if (CollectionUtils.isNotEmpty(
                    lCriteria.getFilterField().getFieldsContainerHierarchy())) {
                lParents.addAll(getSearchService().toFilterFieldsContainerInfos(
                        StringUtils.EMPTY,
                        lCriteria.getFilterField().getFieldsContainerHierarchy()));
            }

            final UsableFieldData lUsableFieldData =
                    getSearchService().getUsableField(
                            pProcessName,
                            SearchUtils.createUsableFieldDataId(lParents,
                                    lCriteria.getFilterField().getLabelKey()),
                                    pTypeIds);
            // Bug 550: mend the lUsableFieldData link direction
            List<FilterFieldsContainerInfo> lInfos =
                lUsableFieldData.getFieldsContainerHierarchy();
            if (lParents.size() == lInfos.size()) {
                for (int i = 0; i<lInfos.size(); i++) {
                    if (lInfos.get(i).getLinkDirection() == LinkDirection.UNDEFINED) {
                        lInfos.get(i).setLinkDirection(lParents.get(i).getLinkDirection());
                    }
                }
            }

            return new CriteriaFieldData(lCriteria.getOperator(),
                    lCriteria.getCaseSensitive(),
                    getFieldsManager().createScalarValueData(
                            lCriteria.getScalarValue()), lUsableFieldData);
        }
        else {
            throw new GDMException("Invalid criteria type.");
        }
    }

    /**
     * Convert a domain operation to a business one.
     * 
     * @param pProcessName
     *            The name of the business process.
     * @param pTypeIds
     *            The types used by the parent filter.
     * @param pDomainOperation
     *            The domain operation.
     * @return The business operation.
     */
    public OperationData convertOperation(final String pProcessName,
            final List<String> pTypeIds, final Operation pDomainOperation) {
        final ArrayList<CriteriaData> lCriterias =
                new ArrayList<CriteriaData>(
                        pDomainOperation.getFilterElements().size());

        for (FilterElement lFilterElement : pDomainOperation.getFilterElements()) {
            lCriterias.add(convertCriteria(pProcessName, pTypeIds,
                    lFilterElement));
        }

        return new OperationData(pDomainOperation.getOp(),
                lCriterias.toArray(new CriteriaData[lCriterias.size()]));
    }

    /**
     * Convert a domain result summary to a business one.
     * 
     * @param pDomainResultSummary
     *            The domain result summary.
     * @return The business result summary.
     */
    public ResultSummaryData convertResultSummary(
            final ResultSummary pDomainResultSummary) {
        final ArrayList<UsableFieldData> lUsableFields =
                new ArrayList<UsableFieldData>(
                        pDomainResultSummary.getSummaryFields().size());
        final ArrayList<String> lTypeIds =
                new ArrayList<String>(
                        pDomainResultSummary.getFieldsContainers().size());

        for (FieldsContainer lFieldsContainer : pDomainResultSummary.getFieldsContainers()) {
            lTypeIds.add(lFieldsContainer.getId());
        }

        for (SummaryField lSummaryField : pDomainResultSummary.getSummaryFields()) {
            final UsableFieldData lUsableField =
                    getSearchService().getUsableField(
                            pDomainResultSummary.getBusinessProcess().getName(),
                            getUsableFieldDataId(lSummaryField.getFilterField()),
                            lTypeIds);
            if(lUsableField != null){
	            lUsableField.setLabel(lSummaryField.getLabel());
	            lUsableFields.add(lUsableField);
            }
        }

        final FilterVisibilityConstraintData lVisibilityConstraint =
                convertFilterVisibilityConstraint(
                        pDomainResultSummary.getEndUser(),
                        pDomainResultSummary.getBusinessProcess(),
                        pDomainResultSummary.getProduct());

        return new ResultSummaryData(
                pDomainResultSummary.getId(),
                pDomainResultSummary.getName(),
                lTypeIds.toArray(new String[lTypeIds.size()]),
                lUsableFields.toArray(new UsableFieldData[lUsableFields.size()]),
                lVisibilityConstraint);
    }

    /**
     * Convert a domain result sorter to a business one.
     * 
     * @param pDomainResultSorter
     *            The domain result sorter.
     * @return The business result sorter.
     */
    public ResultSortingData convertResultSorting(
            final ResultSorter pDomainResultSorter) {

        final ResultSortingData lResultSortingData;
        if (pDomainResultSorter.getResultFields().isEmpty()) {
            lResultSortingData = null;
        }
        else {
            final ArrayList<SortingFieldData> lSortingFields =
                    new ArrayList<SortingFieldData>(
                            pDomainResultSorter.getResultFields().size());
            final ArrayList<String> lTypeIds =
                    new ArrayList<String>(
                            pDomainResultSorter.getFieldsContainers().size());

            for (FieldsContainer lFieldsContainer : pDomainResultSorter.getFieldsContainers()) {
                lTypeIds.add(lFieldsContainer.getId());
            }

            for (ResultField lResultField : pDomainResultSorter.getResultFields()) {
                final UsableFieldData lUsableField =
                        getSearchService().getUsableField(
                                pDomainResultSorter.getBusinessProcess().getName(),
                                getUsableFieldDataId(lResultField.getFilterField()),
                                lTypeIds);
                final SortingFieldData lSortingField = new SortingFieldData();

                lSortingField.setId(lResultField.getId());
                lSortingField.setOrder(lResultField.getSortOrder());
                lSortingField.setUsableFieldData(lUsableField);
                lSortingFields.add(lSortingField);
            }

            final FilterVisibilityConstraintData lVisibilityConstraint =
                    convertFilterVisibilityConstraint(
                            pDomainResultSorter.getEndUser(),
                            pDomainResultSorter.getBusinessProcess(),
                            pDomainResultSorter.getProduct());

            lResultSortingData =
                    new ResultSortingData(
                            pDomainResultSorter.getId(),
                            pDomainResultSorter.getName(),
                            lTypeIds.toArray(new String[lTypeIds.size()]),
                            lSortingFields.toArray(new SortingFieldData[lSortingFields.size()]),
                            lVisibilityConstraint);
        }
        return lResultSortingData;
    }

    private String getUsableFieldDataId(FilterField pFilterField) {
        if (CollectionUtils.isEmpty(pFilterField.getFieldsContainerHierarchy())) {
            return SearchUtils.createUsableFieldDataId(pFilterField.getLabelKey());
        }
        else {
            return SearchUtils.createUsableFieldDataId(
                    getSearchService().toFilterFieldsContainerInfos(
                            StringUtils.EMPTY,
                            pFilterField.getFieldsContainerHierarchy()),
                    pFilterField.getLabelKey());
        }
    }

    /**
     * Convert domain informations to a business filter visibility constraint.
     * 
     * @param pDomainUser
     *            The domain user.
     * @param pDomainProcess
     *            The domain process.
     * @param pDomainProduct
     *            The domain product.
     * @return The business filter visibility constraint.
     */
    public FilterVisibilityConstraintData convertFilterVisibilityConstraint(
            final EndUser pDomainUser, final BusinessProcess pDomainProcess,
            final Product pDomainProduct) {
        final FilterVisibilityConstraintData lFilterVisibilityConstraint =
                new FilterVisibilityConstraintData();

        if (pDomainUser == null) {
            lFilterVisibilityConstraint.setUserLogin(null);
        }
        else {
            lFilterVisibilityConstraint.setUserLogin(pDomainUser.getLogin());
        }
        if (pDomainProcess == null) {
            lFilterVisibilityConstraint.setBusinessProcessName(null);
        }
        else {
            lFilterVisibilityConstraint.setBusinessProcessName(pDomainProcess.getName());
        }
        if (pDomainProduct == null) {
            lFilterVisibilityConstraint.setProductName(null);
        }
        else {
            lFilterVisibilityConstraint.setProductName(pDomainProduct.getName());
        }

        return lFilterVisibilityConstraint;
    }
}
