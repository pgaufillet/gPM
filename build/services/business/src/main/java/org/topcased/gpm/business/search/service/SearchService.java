/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Anne Haugommard(Atos Origin), Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.search.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.topcased.gpm.business.attributes.service.AttributesService;
import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.exception.FilterException;
import org.topcased.gpm.business.exception.InvalidIdentifierException;
import org.topcased.gpm.business.fields.FieldsContainerType;
import org.topcased.gpm.business.fields.SummaryData;
import org.topcased.gpm.business.fields.impl.CacheableFieldsContainer;
import org.topcased.gpm.business.search.criterias.FilterData;
import org.topcased.gpm.business.search.criterias.FilterTypeData;
import org.topcased.gpm.business.search.criterias.impl.FilterFieldsContainerInfo;
import org.topcased.gpm.business.search.impl.query.FilterQueryConfigurator;
import org.topcased.gpm.business.search.impl.query.FilterResultIdIterator;
import org.topcased.gpm.business.search.impl.query.FilterResultIterator;
import org.topcased.gpm.domain.search.FieldsContainerId;

/**
 * Search service interface.
 * 
 * @author ahaugomm
 */
@Transactional(propagation = Propagation.REQUIRED)
public interface SearchService {

    public static String LIMIT_REACHED = "limitReached";

    /**
     * Checks the rights of the user.
     * 
     * @param pUserLogin
     *            The current user login.
     * @param pBusinessProcessName
     *            The name of the current business process.
     * @param pProductName
     *            The product name.
     * @param pFConstraint
     *            The filter visibility constraint.
     * @return True if the user in the current business process and product has
     *         sufficient rights for this filter visibility constraints.
     */
    public boolean checkVisibilityConstraints(String pUserLogin,
            String pBusinessProcessName, String pProductName,
            FilterVisibilityConstraintData pFConstraint);

    /**
     * Execute the filter. The result are the container's identifiers.
     * <p>
     * Check filter access control.
     * </p>
     * *
     * <p>
     * Check the visibility constraint of the filter: It depends of the filter
     * scope (PROCESS, PRODUCT, USER)
     * </p>
     * 
     * @param pRoleToken
     *            Role token
     * @param pExecutableFilterData
     *            Filter to execute
     * @param pFilterVisibilityConstraintData
     *            Visibility constraint of the filter
     * @param pFilterQueryConfigurator
     *            Configuration of the filter
     * @return Iterator for results (as String)
     * @throws FilterException
     *             Filter query cannot be execute or Filter results cannot be
     *             read.
     * @throws AuthorizationException
     *             If no rights to execute this filter
     */
    public FilterResultIdIterator executeFilterIdentifier(String pRoleToken,
            ExecutableFilterData pExecutableFilterData,
            FilterVisibilityConstraintData pFilterVisibilityConstraintData,
            FilterQueryConfigurator pFilterQueryConfigurator)
        throws AuthorizationException, FilterException;

    /**
     * Execute the filter. The type of results depends of the given class.
     * <p>
     * Check the visibility constraint of the filter: It depends of the filter
     * scope (PROCESS, PRODUCT, USER)
     * </p>
     * 
     * @param <S>
     *            Type of SummaryData (depends of the given ValuesContainer
     *            class)
     * @param pRoleToken
     *            Role token
     * @param pExecutableFilterData
     *            Filter to execute
     * @param pFilterVisibilityConstraintData
     *            Visibility constraint of the filter
     * @param pFilterQueryConfigurator
     *            Configuration of the filter
     * @return Iterator for results
     * @throws FilterException
     *             Filter query cannot be execute or Filter results cannot be
     *             read.
     * @throws AuthorizationException
     *             If no rights to execute this filter
     */
    public <S extends SummaryData> FilterResultIterator<S> executeFilter(
            String pRoleToken, ExecutableFilterData pExecutableFilterData,
            FilterVisibilityConstraintData pFilterVisibilityConstraintData,
            FilterQueryConfigurator pFilterQueryConfigurator)
        throws AuthorizationException, FilterException;

    /**
     * Execute the filter. The type of results depends of the given class.
     * <p>
     * This execution does not care about criteria. The selected containers are
     * only those containing in the specified list.
     * </p>
     * <p>
     * This execution is used to retrieve result elements and also to sort
     * containers that have been specified.
     * </p>
     * <p>
     * Check filter access control.
     * </p>
     * <p>
     * Check the visibility constraint of the filter: It depends of the filter
     * scope (PROCESS, PRODUCT, USER)
     * </p>
     * 
     * @param <S>
     *            Type of SummaryData (depends of the given ValuesContainer
     *            class)
     * @param pRoleToken
     *            Role token
     * @param pExecutableFilterData
     *            Filter to execute
     * @param pFilterVisibilityConstraintData
     *            Visibility constraint of the filter
     * @param pFilterQueryConfigurator
     *            Configuration of the filter
     * @param pContainerIdentifiers
     *            Identifiers of container that must be use in this Filter.
     * @return Iterator for results
     * @throws FilterException
     *             Filter query cannot be execute or Filter results cannot be
     *             read.
     * @throws AuthorizationException
     *             If no rights to execute this filter
     */
    public <S extends SummaryData> FilterResultIterator<S> executeFilter(
            String pRoleToken, ExecutableFilterData pExecutableFilterData,
            FilterVisibilityConstraintData pFilterVisibilityConstraintData,
            FilterQueryConfigurator pFilterQueryConfigurator,
            Collection<String> pContainerIdentifiers)
        throws AuthorizationException, FilterException;

    /**
     * Create an ExecutableFilter in database.
     * 
     * @param pRoleToken
     *            The role token.
     * @param pExecutableFilterData
     *            The executable filter data.
     * @return The id of the executable filter data.
     * @throws AuthorizationException
     *             The user cannot create the filter.
     */
    public String createExecutableFilter(String pRoleToken, ExecutableFilterData pExecutableFilterData)
        throws AuthorizationException;

    /**
     * Remove an executable filter from the database.
     * 
     * @param pRoleToken
     *            The role token.
     * @param pFilterId
     *            The id of the executable filter in the database.
     * @return If the filter has been deleted.
     * @throws AuthorizationException
     *             The filter cannot be delete.
     */
    public boolean removeExecutableFilter(String pRoleToken, String pFilterId)
        throws AuthorizationException;

    /**
     * Get all the FilterData objects visible for the given businessProcess,
     * product and user.
     * 
     * @param pUserToken
     *            the user token
     * @param pBusinessProcessName
     *            the current business process
     * @param pProductName
     *            the current product
     * @param pUserLogin
     *            the current user login
     * @return the visible filterDatas
     * @deprecated
     * @see SearchService#getVisibleExecutableFilter(String,
     *      FilterVisibilityConstraintData, FilterTypeData, String)
     * @since 1.8
     */
    public Collection<FilterData> getVisibleFilterDatas(String pUserToken,
            String pBusinessProcessName, String pProductName, String pUserLogin);

    /**
     * Get all the ExecutableFilterData objects of a certain type , visible for
     * the given businessProcess, product and user.
     * 
     * @param pRoleToken
     *            the role token
     * @param pBusinessProcessName
     *            the current business process
     * @param pProductName
     *            the current product
     * @param pUserLogin
     *            the current user login
     * @param pFilterType
     *            the type of filter we are looking for (QueryLL.SHEET_FILTER,
     *            QueryLL.PRODUCT_FILTER)
     * @param pUsage
     *            the filter usage (TREE_VIEW, TABLE_VIEW or BOTH_VIEWS)
     * @return the visible filterDatas
     * @deprecated
     * @see SearchService#getVisibleExecutableFilter(String,
     *      FilterVisibilityConstraintData, FilterTypeData, String)
     * @since 1.8
     */
    public Collection<ExecutableFilterData> getVisibleExecutableFilterDatasByFilterType(
            String pRoleToken, String pBusinessProcessName,
            String pProductName, String pUserLogin, FilterTypeData pFilterType,
            String pUsage);

    /**
     * Get all the ExecutableFilter objects that are visible for the given
     * constraints (businessProcess, product, user).
     * <p>
     * For an admin user, returns also filters that are hidden.
     * 
     * @param pRoleToken
     *            the role token
     * @param pFilterVisibilityConstraintData
     *            Constraints of the filters
     * @param pFilterType
     *            the type of filter we are looking for. Null for all types.
     * @param pUsage
     *            the filter usage
     * @return Executable filters that are visible.
     */
    public Collection<ExecutableFilterData> getVisibleExecutableFilter(
            String pRoleToken,
            FilterVisibilityConstraintData pFilterVisibilityConstraintData,
            FilterTypeData pFilterType, String pUsage);

    /**
     * Update an existing executable filter.
     * 
     * @param pRoleToken
     *            The role token.
     * @param pExecutableFilterData
     *            Contains the data of the executable filter.
     * @throws AuthorizationException
     *             The filter cannot be updated.
     */
    public void updateExecutableFilter(String pRoleToken,
            ExecutableFilterData pExecutableFilterData)
        throws AuthorizationException;

    /**
     * Gets an ExecutableFilterData from its Id.
     * 
     * @param pRoleToken
     *            The current role token.
     * @param pFilterId
     *            The id of the executable filter.
     * @return The executable filter data.
     */
    public ExecutableFilterData getExecutableFilter(String pRoleToken, String pFilterId);

    /**
     * Get an ExecutableFilterData from its name and visibility scope
     * 
     * @param pRoleToken
     *            the current role token
     * @param pProcessName
     *            the current business process
     * @param pFilterProductName
     *            the name of the product on which the filter applies (for a
     *            product filter) or null otherwise
     * @param pFilterUserLogin
     *            the user's login for a user filter, null otherwise
     * @param pFilterName
     *            the name of the executable filter
     * @return the executable filter data
     */
    public ExecutableFilterData getExecutableFilterByName(String pRoleToken,
            String pProcessName, String pFilterProductName,
            String pFilterUserLogin, String pFilterName);

    /**
     * Get all the usable fields for a set of fieldsContainers. <h4>Note:</h4>
     * <p>
     * For multi-container (more than one fields container identifier), a usable
     * field that represents a multi-valued field is not return. Null value is
     * return instead.
     * </p>
     * 
     * @param pRoleToken
     *            the role token
     * @param pFieldsContainerIds
     *            the identifiers of the field containers
     * @param pProcessName
     *            the businessProcess name (can be empty, not use)
     * @return the collection of usable field data
     */
    public Map<String, UsableFieldData> getUsableFields(String pRoleToken,
            String[] pFieldsContainerIds, String pProcessName);

    /**
     * Get the list of compatible operators to use in a criteria for a given
     * usable field data
     * 
     * @param pUsableFieldData
     *            the usable field data
     * @return the collection of operators
     */
    public Collection<String> getCompatibleOperators(
            UsableFieldData pUsableFieldData);

    /**
     * Set the maximum depth for usable field in filter criteria and display.
     * 
     * @param pMaxFieldsDepth
     *            New maximum depth value
     */
    public void setMaxFieldsDepth(int pMaxFieldsDepth);

    /**
     * Gets the maximum depth for usable field in filter criteria and display.
     * <p>
     * This value is defined by the instance as a global attribute.<br />
     * This method gets this attribute (from DB) if the value (stored as
     * instance attribute in SearchService) has not been set.
     * <p>
     * This value is only getting one time from the GlobalAttributes: at the
     * first call of this method.<br />
     * To dynamically change this attribute, the set method have to be used.
     * <p>
     * If it not defined, gets the default value (
     * {@link AttributesService#FILTER_FIELDS_MAX_DEPTH_DEFAULT_VALUE})
     * 
     * @return The maximum depth value for usable fields (in criteria and
     *         summary fields)
     */
    public int getMaxFieldsDepth();

    /**
     * Get the usable field data id from a field hierarchy.
     * <p>
     * A field hierarchy represents the path to a result field <br />
     * <blockquote>
     * <dl>
     * <dt>A path is like this:
     * <dd>
     * <code>LinkType_01|SheetType_02|LinkType_02|SheetType_03|Field label key</code>
     * <br />
     * <dt>represents
     * <dd>the <code>field label key</code> of the sheet type <code>SheetType_03'</code>
     * which is binding to the sheet type '<code>SheetType_02</code>' by the
     * link type '<code>LinkType_02</code>' and the sheet type '
     * <code>SheetType_02</code>' is binding to the current sheet by the link
     * type '<code>LinkType_01</code>'.
     * </dl>
     * <p>
     * 
     * <pre>
     * CurrentSheetType
     *          |
     *          |   LinkType_01
     *                  |
     *                  | SheetType_02
     *                          |
     *                          | LinkType_02
     *                                  |
     *                                  |   SheetType_03
     *                                          | Field label key
     * 
     * </pre>
     * 
     * </p>
     * </blockquote>
     * 
     * @see {@link SearchUtil#createUsableFieldDataId(String, String)} for the
     *      usable field data id description. <br/>
     * @param pRoleToken
     *            Role token to retrieve fields container. Used to retrieve the
     *            fields container id.
     * @param pBusinessProcessName
     *            Business process name to retrieve fields container. Used to
     *            retrieve the fields container id.
     * @param pHierarchy
     *            Hierarchy path to field (identified by its label key)
     * @return The usable fields data id. An empty string if the hierarchy
     *         contains one element.
     */
    public String getUsableFieldDataId(final String pRoleToken,
            final String pBusinessProcessName, final String pHierarchy);

    /**
     * Creates a FilterFieldsContainerInfo according to the fields container id.
     * <p>
     * Sets the link direction attribute according to the 'sheetTypeId'
     * parameter. {@link FilterFieldsContainerInfo} for the specification of the
     * 'link direction'.
     * 
     * @param pFieldsContainerId
     *            Technical identifier of a fields container.
     * @param pSheetTypeId
     *            Identifier of the sheet type which is the origin or
     *            destination of the link type. Can be null.<br />
     *            Use to set the 'link direction' attribute.
     * @return Created FilterFieldsContainerInfo if it founded.
     * @throws InvalidIdentifierException
     *             If the FieldsContainer doesn't exist.
     * @throws RuntimeException
     *             If cannot identify the type of the fields container (@link
     *             {@link FieldsContainerType#valueOf(CacheableFieldsContainer)}
     *             .
     */
    public FilterFieldsContainerInfo createFilterFieldsContainerInfo(
            final String pFieldsContainerId, final String pSheetTypeId)
        throws InvalidIdentifierException, RuntimeException;

    /**
     * Converts the list of the FieldsContainerId (entity) ancestors to
     * FilterFieldsContainerInfo.
     * 
     * @param pFieldsContainerId
     *            Identifier of the fields container of the associate field. The
     *            direct parent of the field.<br />
     *            This value used to set the 'link direction' attribute, it can
     *            be null.
     * @param pVirtualFieldDataHierarchy
     *            List of entity ancestors.
     * @return List of FilterFieldsContainerInfo ancestors.
     */
    public List<FilterFieldsContainerInfo> toFilterFieldsContainerInfos(
            String pFieldsContainerId,
            final Collection<FieldsContainerId> pVirtualFieldDataHierarchy);

    /**
     * Get all the FilterData objects for a certain sheet type , visible for the
     * given businessProcess, product and user.
     * 
     * @param pRoleToken
     *            The role token
     * @param pSheetTypeName
     *            The sheet type name
     * @param pFilterScope
     *            The FilterScope
     * @return the visible FilterData.
     * @deprecated
     * @see SearchService#getVisibleExecutableFilterDatas(String, String,
     *      String, String, String)
     * @since 1.8
     */
    public List<FilterData> getVisibleFilterDatasBySheetType(String pRoleToken,
            String pSheetTypeName, FilterScope pFilterScope);

    /**
     * Get all the serializable filters
     * 
     * @param pRoleToken
     *            The role token
     * @return The Filters
     */
    public List<org.topcased.gpm.business.serialization.data.Filter> getSerializableFilters(
            String pRoleToken);

    /**
     * Get usable field according to the criterion.
     * <p>
     * The identifiers of fields container can be empty, then usable field has
     * been searched in all fields container.
     * <h4>Note:</h4>
     * <p>
     * For multi-container (more than one fields container identifier), a usable
     * field that represents a multi-valued field is not return. Null value is
     * return instead.
     * </p>
     * 
     * @param pProcessName
     *            Process name
     * @param pCriterionLabelKey
     *            Label key of the criterion
     * @param pFieldsContainerIds
     *            Identifier of fields container containing the field.
     * @return Usable field or null if not find.
     */
    public UsableFieldData getUsableField(String pProcessName,
            String pCriterionLabelKey, Collection<String> pFieldsContainerIds);

    /**
     * Get filter's results number limits. (defined in properties file)
     * 
     * @return Number limit of results for a filter.
     */
    public int getResultsLimit();

    /**
     * Get a FilterData object from a filter name.
     * 
     * @param pRoleToken
     *            Role token
     * @param pFilterName
     *            Filter to execute
     * @param pFilterVisibilityConstraintData
     *            Visibility constraint of the filter
     * @param pFilterScope
     *            Identifiers of container that must be use in this Filter.
     * @return Iterator for results
     * @throws FilterException
     *             Filter query cannot be execute or Filter results cannot be
     *             read.
     * @throws AuthorizationException
     *             If no rights to execute this filter
     */
    public ExecutableFilterData retrieveFilterDataFromName(String pRoleToken,
            String pFilterName,
            FilterVisibilityConstraintData pFilterVisibilityConstraintData,
            FilterScope pFilterScope) throws AuthorizationException;
}
