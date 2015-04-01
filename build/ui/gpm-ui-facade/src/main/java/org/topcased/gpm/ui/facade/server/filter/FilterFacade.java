/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.server.filter;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.topcased.gpm.business.search.service.ExecutableFilterData;
import org.topcased.gpm.business.search.service.FilterScope;
import org.topcased.gpm.business.util.FilterType;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.shared.exception.EmptyResultFieldException;
import org.topcased.gpm.ui.facade.shared.exception.NotExistFilterException;
import org.topcased.gpm.ui.facade.shared.exception.NotSpecifiedCriteriaException;
import org.topcased.gpm.ui.facade.shared.exception.NotSpecifiedScopeException;
import org.topcased.gpm.ui.facade.shared.extendedaction.UiFilterEAResult;
import org.topcased.gpm.ui.facade.shared.filter.UiFilter;
import org.topcased.gpm.ui.facade.shared.filter.UiFilterContainerHierarchy;
import org.topcased.gpm.ui.facade.shared.filter.UiFilterContainerType;
import org.topcased.gpm.ui.facade.shared.filter.UiFilterScope;
import org.topcased.gpm.ui.facade.shared.filter.UiFilterVisibility;
import org.topcased.gpm.ui.facade.shared.filter.field.UiFilterUsableField;
import org.topcased.gpm.ui.facade.shared.filter.result.table.UiFilterTableResult;
import org.topcased.gpm.ui.facade.shared.filter.result.tree.UiFilterTreeResult;
import org.topcased.gpm.ui.facade.shared.filter.summary.UiFilterSummaries;

/**
 * FilterFacade
 * 
 * @author nveillet
 */
public interface FilterFacade {

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
    public void addToCache(final UiSession pSession,
            final FilterType pFilterType, final UiFilter pFilter);

    /**
     * Clear a filter from cache
     * 
     * @param pSession
     *            Current user session
     * @param pFilterType
     *            the filter type
     */
    public void clearCache(final UiSession pSession,
            final FilterType pFilterType);

    /**
     * Delete a filter
     * 
     * @param pSession
     *            the session
     * @param pFilterId
     *            the filter identifier
     */
    public void deleteFilter(final UiSession pSession, final String pFilterId);

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
            final String pFilterSorterName, final Collection<String> pSheetIds);

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
        NotSpecifiedCriteriaException;

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
        EmptyResultFieldException, NotSpecifiedCriteriaException;

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
        NotSpecifiedCriteriaException;

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
        EmptyResultFieldException, NotSpecifiedCriteriaException;

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
        NotSpecifiedCriteriaException;

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
        EmptyResultFieldException, NotSpecifiedCriteriaException;

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
        EmptyResultFieldException, NotSpecifiedCriteriaException;

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
        NotSpecifiedCriteriaException;

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
        NotSpecifiedCriteriaException;

    /**
     * Get usable products.
     * 
     * @param pSession
     *            Current user session.
     * @return List of product names.
     */
    public List<UiFilterScope> getAvailableProductScope(final UiSession pSession);

    /**
     * Get available visibilities for a filter.
     * 
     * @param pSession Current user session.
     * @param pFilterType the filter type.
     * @return List of visibilities.
     */
    public List<UiFilterVisibility> getAvailableVisibilities(
            final UiSession pSession, FilterType pFilterType);

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
            final String pCategoryName, List<UiFilterScope> pProductScope);

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
            final String pFilterId);

    /**
     * get a filter
     * 
     * @param pSession
     *            the session
     * @param pFilterId
     *            the filter identifier
     * @return the filter
     */
    public UiFilter getFilter(final UiSession pSession, final String pFilterId);

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
            final FilterType pFilterType);

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
            final FilterType pFilterType);

    /**
     * get max fields depth.
     * 
     * @return max fields depth.
     */
    public int getMaxFieldsDepth();

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
            final UiSession pSession, final FilterType pFilterType);

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
            final UiSession pSession, final List<String> pContainerTypeId);

    /**
     * Create or Update filter.
     * 
     * @param pSession
     *            Current user session.
     * @param pFilter
     *            Filter to save.
     * @return Id of saved filter in database.
     */
    public String saveFilter(final UiSession pSession, final UiFilter pFilter);
}
