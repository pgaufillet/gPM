/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.authorization.impl.filter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.ServiceImplBase;
import org.topcased.gpm.business.cache.ICacheManager;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.exception.InvalidIdentifierException;
import org.topcased.gpm.business.exception.InvalidNameException;
import org.topcased.gpm.business.fields.impl.CacheableFieldsContainer;
import org.topcased.gpm.business.link.impl.CacheableLinkType;
import org.topcased.gpm.business.product.impl.ProductHierarchyKey;
import org.topcased.gpm.business.scalar.MultiStringValueData;
import org.topcased.gpm.business.search.criterias.CriteriaData;
import org.topcased.gpm.business.search.criterias.CriteriaFieldData;
import org.topcased.gpm.business.search.criterias.FilterTypeData;
import org.topcased.gpm.business.search.criterias.OperationData;
import org.topcased.gpm.business.search.criterias.impl.Operators;
import org.topcased.gpm.business.search.criterias.impl.VirtualFieldData;
import org.topcased.gpm.business.search.impl.fields.UsableFieldsManager;
import org.topcased.gpm.business.search.impl.fields.UsableTypeData;
import org.topcased.gpm.business.search.service.ExecutableFilterData;
import org.topcased.gpm.business.search.service.FilterProductScope;
import org.topcased.gpm.business.search.service.FilterScope;
import org.topcased.gpm.business.search.service.UsableFieldData;
import org.topcased.gpm.business.serialization.data.Field;
import org.topcased.gpm.business.serialization.data.FilterAccessConstraintName;
import org.topcased.gpm.business.serialization.data.FilterAccessCtl;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.business.util.action.AdministrationAction;
import org.topcased.gpm.domain.accesscontrol.FilterAccess;
import org.topcased.gpm.domain.accesscontrol.FilterAccessConstraint;
import org.topcased.gpm.domain.accesscontrol.FilterAccessConstraintDao;
import org.topcased.gpm.domain.accesscontrol.FilterAccessDao;
import org.topcased.gpm.domain.accesscontrol.RoleDao;
import org.topcased.gpm.domain.search.FilterElement;
import org.topcased.gpm.util.bean.CacheProperties;
import org.topcased.gpm.util.lang.CopyUtils;

/**
 * Used to managed the access control on filter. Use a cache for store the
 * access control definition.
 * 
 * @author tpanuel
 */
public class FilterAccessManager extends ServiceImplBase implements
        ICacheManager<FilterAccessDefinitionKey, FilterAccessDefinition> {
    private final Map<String, FilterAccessDefinition> cache =
            new ConcurrentHashMap<String, FilterAccessDefinition>();

    private FilterAccessDao filterAccessDao;

    private RoleDao roleDao;

    /**
     * Setter on DAO for Spring injection.
     * 
     * @param pFilterAccessDao
     *            The DAO.
     */
    public void setFilterAccessDao(final FilterAccessDao pFilterAccessDao) {
        filterAccessDao = pFilterAccessDao;
    }

    /**
     * Setter on DAO for Spring injection.
     * 
     * @param pRoleDao
     *            The DAO.
     */
    public void setRoleDao(final RoleDao pRoleDao) {
        roleDao = pRoleDao;
    }

    private FilterAccessConstraintDao filterAccessConstraintDao;

    public void setFilterAccessConstraintDao(
            FilterAccessConstraintDao pFilterAccessConstraintDao) {
        filterAccessConstraintDao = pFilterAccessConstraintDao;
    }

    /**
     * Apply access control on a filter. No restriction for administrator.
     * 
     * @param pRoleToken
     *            The role token.
     * @param pFilter
     *            The filter.
     */
    public void applyAccessControl(final String pRoleToken,
            final ExecutableFilterData pFilter) {
        // No restriction for administrator
        if (getAuthService().isGlobalAdminRole(pRoleToken)) {
            pFilter.setExecutable(true);
            pFilter.setEditable(true);
        }
        else {
            final String lProcessName = getAuthService().getProcessNameFromToken(pRoleToken);
            final String lRoleName = getAuthService().getRoleNameFromToken(pRoleToken);
            final Map<UsableTypeData, Set<UsableFieldData>> lUsedFieldLabels =
                    getSearchService().getUsableFieldsManager().getUsedFields(pFilter);

            // Compute access control on the filter
            // By default, no restriction
            boolean lExecutable = true;
            boolean lEditable = true;

            // For each type
            for (Entry<UsableTypeData, Set<UsableFieldData>> lEntry : lUsedFieldLabels.entrySet()) {
                final FilterAccessDefinition lAccessDefinition = getElement(new FilterAccessDefinitionKey(
                		lProcessName, lRoleName, lEntry.getKey().getId()));
                
                // Search an access control defined on type
                final FilterAccessControl lTypeAccess = lAccessDefinition.getTypeAccessControl();

                if (lTypeAccess != null) {
                    lExecutable = lExecutable && lTypeAccess.getExecutable();
                    lEditable = lEditable && lTypeAccess.getEditable();
                }

                // Search access control defined on all type's fields
                for (UsableFieldData lUsedField : lEntry.getValue()) {
                    final FilterAccessControl lFieldAccess =
                    		lAccessDefinition.getFieldsAccessControl().get(lUsedField.getFieldName());

                    if (lFieldAccess != null) {
                        lExecutable = lExecutable && lFieldAccess.getExecutable();
                        lEditable = lEditable && lFieldAccess.getEditable();
                    }
                }
            }

            pFilter.setExecutable(lExecutable);
            pFilter.setEditable(lEditable);
        }
    }

    /**
     * Test if the filter can be saved.
     * 
     * @param pRoleToken
     *            The role token.
     * @param pFilter
     *            The filter.
     * @return If the filter can be saved.
     */
    public boolean canSaveFilter(final String pRoleToken,
            final ExecutableFilterData pFilter) {
        if (getAuthService().isGlobalAdminRole(pRoleToken) ||
        		(FilterTypeData.PRODUCT.equals(pFilter.getFilterData().getType())
        				&& pFilter.getFilterVisibilityConstraintData().getUserLogin() != null)) {
            return true;
        }
        else {
            // Apply access control on filter
            applyAccessControl(pRoleToken, pFilter);

            // Search access on scope
            final FilterAccessControl lScopeAccess;

            if (pFilter.getFilterVisibilityConstraintData().getProductName() != null) {
                lScopeAccess =
                        filterAccessManager.getAccessOnFilterScope(pRoleToken,
                                FilterScope.PRODUCT_FILTER);
            }
            else if (pFilter.getFilterVisibilityConstraintData().getUserLogin() != null) {
                lScopeAccess =
                        filterAccessManager.getAccessOnFilterScope(pRoleToken,
                                FilterScope.USER_FILTER);
            }
            else {
                lScopeAccess =
                        filterAccessManager.getAccessOnFilterScope(pRoleToken,
                                FilterScope.INSTANCE_FILTER);
            }

            return pFilter.isEditable() && lScopeAccess.getEditable();
        }
    }

    /**
     * Get access for a specific filter scope.
     * 
     * @param pRoleToken
     *            The role token.
     * @param pScope
     *            The scope.
     * @return The access.
     */
    public FilterAccessControl getAccessOnFilterScope(final String pRoleToken,
            final FilterScope pScope) {
        final FilterAccessControl lAccess = new FilterAccessControl();

        // All rights for administrator
        if (!getAuthService().isGlobalAdminRole(pRoleToken)) {
            final String lRoleName =
                    getAuthService().getRoleNameFromToken(pRoleToken);
            final FilterAccess lFilterAccess =
                    filterAccessDao.getFilterAccessOnVisibility(lRoleName,
                            pScope.name());

            if (lFilterAccess == null) {
                // No access control defined, apply default ones
                switch (pScope) {
                    case INSTANCE_FILTER:
                        // For instance filter : only global admin role
                        // and admin access set on global instance can delete.
                        lAccess.setEditable(getAuthService().isAdminAccessDefinedOnInstance(
                                pRoleToken,
                                AdministrationAction.PRODUCT_SEARCH_DELETE.getActionKey()));
                        break;
                    case PRODUCT_FILTER:
                        // For product filter : only global admin role
                        // and admin access set on product can delete.
                        lAccess.setEditable(getAuthService().isAdminAccessDefinedOnProduct(
                                pRoleToken,
                                AdministrationAction.PRODUCT_SEARCH_DELETE.getActionKey(),
                                getAuthService().getProductNameFromSessionToken(
                                        pRoleToken)));
                        break;
                    case USER_FILTER:
                    default:
                        // No restriction
                }
            }
            else {
                lAccess.setExecutable(lFilterAccess.getExecutable());
                lAccess.setEditable(lFilterAccess.getEditable());
            }
        }

        return lAccess;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.cache.ICacheManager#depreciateAll()
     */
    public void depreciateAll() {
        cache.clear();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.cache.ICacheManager#depreciateElement(org.topcased.gpm.business.cache.CacheKey)
     */
    public void depreciateElement(final FilterAccessDefinitionKey pElementKey) {
        cache.remove(pElementKey.getKeyValue());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.cache.ICacheManager#getElement(org.topcased.gpm.business.cache.CacheKey)
     */
    public FilterAccessDefinition getElement(
            final FilterAccessDefinitionKey pElementKey) {
        FilterAccessDefinition lAccessDefinition =
                cache.get(pElementKey.getKeyValue());

        // Load the access definition from the data base
        if (lAccessDefinition == null) {
            final String lRoleName = pElementKey.getRoleName();
            final CacheableFieldsContainer lContainer =
                    getCachedFieldsContainer(pElementKey.getTypeId(),
                            CacheProperties.IMMUTABLE.getCacheFlags());

            // New Access Definition
            lAccessDefinition = new FilterAccessDefinition();

            // Load access on type
            final FilterAccess lTypeAccess =
                    filterAccessDao.getFilterAccessOnType(lRoleName,
                            lContainer.getName());

            lAccessDefinition.setTypeAccessControl(convertFilterAccess(
                    pElementKey.getProcessName(), lTypeAccess));

            // Get field labels
            final Set<String> lFieldLabels = new HashSet<String>();

            // Add standard fields
            for (Field lField : lContainer.getAllFields()) {
                lFieldLabels.add(lField.getLabelKey());
            }
            // Add virtual fields
            if (lContainer instanceof CacheableSheetType) {
                lFieldLabels.addAll(UsableFieldsManager.SHEET_VIRTUAL_FIELDS);
            }
            else if (lContainer instanceof CacheableLinkType) {
                lFieldLabels.addAll(UsableFieldsManager.LINK_VIRTUAL_FIELDS);
            }

            // Load access on fields
            for (String lFieldLabel : lFieldLabels) {
                final FilterAccess lFieldAccess =
                        filterAccessDao.getFilterAccessOnField(lRoleName,
                                lContainer.getName(), lFieldLabel);

                if (lFieldAccess != null) {
                    lAccessDefinition.getFieldsAccessControl().put(
                            lFieldLabel,
                            convertFilterAccess(pElementKey.getProcessName(),
                                    lFieldAccess));
                }
            }

            lAccessDefinition = CopyUtils.deepClone(lAccessDefinition);

            // Store the access definition in cache
            cache.put(pElementKey.getKeyValue(), lAccessDefinition);
        }

        //Use deep clone to fix problem with mutable/immutable and ScalarValueData
        return CopyUtils.deepClone(lAccessDefinition);
    }

    /**
     * Convert a domain filter access to a business one.
     * 
     * @param pProcessName
     *            The business process name.
     * @param pDomainFilterAccess
     *            The domain filter access.
     * @return The business filter access.
     */
    private FilterAccessControl convertFilterAccess(final String pProcessName,
            final FilterAccess pDomainFilterAccess) {
        final FilterAccessControl lFilterAccess = new FilterAccessControl();

        if (pDomainFilterAccess != null) {
            if (pDomainFilterAccess.getExecutable() != null) {
                lFilterAccess.setExecutable(pDomainFilterAccess.getExecutable());
            }
            if (pDomainFilterAccess.getEditable() != null) {
                lFilterAccess.setEditable(pDomainFilterAccess.getEditable());
            }
            for (FilterAccessConstraint lAdditionalConstraint :
                    pDomainFilterAccess.getConstraints()) {
                lFilterAccess.getAdditionalConstraints().add(
                        convertFilterConstraint(pProcessName,
                                lAdditionalConstraint));
            }
        }

        return lFilterAccess;
    }

    /**
     * Convert a domain filter constraint to a business one.
     * 
     * @param pProcessName
     *            The business process name.
     * @param pDomainFilterConstraint
     *            The domain filter constraint.
     * @return The business filter constraint.
     */
    @SuppressWarnings("unchecked")
    private FilterAccessContraint convertFilterConstraint(
            final String pProcessName,
            final FilterAccessConstraint pDomainFilterConstraint) {
        final FilterAccessContraint lConstraint = new FilterAccessContraint();

        lConstraint.setName(pDomainFilterConstraint.getName());
        lConstraint.setDescription(pDomainFilterConstraint.getDescription());
        lConstraint.setConstraints(filterDataManager.convertCriteria(
                pProcessName, Collections.EMPTY_LIST,
                pDomainFilterConstraint.getFilterElement()));

        return lConstraint;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.cache.ICacheManager#getElementVersion(org.topcased.gpm.business.cache.CacheKey)
     */
    public long getElementVersion(final FilterAccessDefinitionKey pElementKey) {
        throw new NotImplementedException("No version for filter access cache.");
    }

    /**
     * Convert business constraint to domain constraint.
     * <p>
     * The domain is created only if the business object contains constraints.
     * 
     * @param pBusinessConstraint
     *            Business constraint to convert
     * @return Domain representation of the business constraint.
     * @throws GDMException
     *             If the filter element already exists / the criteria data is
     *             invalid (no CriteriaFieldData or no OperationData) / the
     *             scalar value of the criterion is invalid.
     */
    private FilterAccessConstraint toDomainConstaint(
            final FilterAccessContraint pBusinessConstraint) {
        final FilterAccessConstraint lDomainConstraint;
        if ((pBusinessConstraint != null)
                && (pBusinessConstraint.getConstraints() != null)) {
            lDomainConstraint = FilterAccessConstraint.newInstance();
            lDomainConstraint.setName(pBusinessConstraint.getName());
            lDomainConstraint.setDescription(pBusinessConstraint.getDescription());
            FilterElement lFilterElement =
                    getSearchService().createFilterElement(
                            pBusinessConstraint.getConstraints());
            lDomainConstraint.setFilterElement(lFilterElement);
        }
        else {
            lDomainConstraint = null;
        }
        return lDomainConstraint;
    }

    /**
     * Create the filter access constraint object.
     * 
     * @param pFilterAccessContraint
     *            Business object to create
     * @throws InvalidNameException
     *             If the constrains already exists.
     */
    public void createFilterAccessConstraint(
            FilterAccessContraint pFilterAccessContraint)
        throws InvalidNameException {
        // Test existence
        if (isFilterAccessConstraintExists(pFilterAccessContraint.getName())) {
            throw new InvalidNameException(pFilterAccessContraint.getName(),
                    "A filter access constraint named {0} already exists.");
        }
        else {
            filterAccessConstraintDao.create(toDomainConstaint(pFilterAccessContraint));
        }
    }

    /**
     * Tests if the constraint exists.
     * 
     * @param pConstraintName
     *            Name of the constraint to test
     * @return True if exists, false otherwise
     */
    public boolean isFilterAccessConstraintExists(String pConstraintName) {
        return filterAccessConstraintDao.isConstraintExists(pConstraintName);
    }

    /**
     * Convert business (XML filter access object) to domain object. For the
     * visibility the domain object contains a value of FilterScope enumeration.
     * Each constrains are loaded and added to the domain object.
     * 
     * @param pBusinessAccess
     *            Object to convert
     * @return Domain object representing domain view of the business object.
     * @throws IllegalArgumentException
     *             if the visibility value does not correspond to an enumeration
     *             of FilterScope
     * @throws InvalidIdentifierException
     *             if the constraint specified in the business object does not
     *             exists.
     */
    private FilterAccess toDomainAccess(final FilterAccessCtl pBusinessAccess)
        throws IllegalArgumentException, InvalidIdentifierException {
        final FilterAccess lDomainAccess;
        if (pBusinessAccess != null) {
            lDomainAccess = FilterAccess.newInstance();
            lDomainAccess.setRoleName(pBusinessAccess.getRoleName());
            lDomainAccess.setTypeName(pBusinessAccess.getTypeName());
            lDomainAccess.setFieldName(pBusinessAccess.getFieldName());
            if (StringUtils.isNotBlank(pBusinessAccess.getVisibility())) {
                lDomainAccess.setVisibility(FilterScope.toFilterScope(
                        pBusinessAccess.getVisibility()).name());
            }
            lDomainAccess.setExecutable(pBusinessAccess.getExecutable());
            lDomainAccess.setEditable(pBusinessAccess.getEditable());

            //Get the constraint
            if (CollectionUtils.isNotEmpty(pBusinessAccess.getConstraints())) {
                for (FilterAccessConstraintName lConstraintName :
                        pBusinessAccess.getConstraints()) {
                    FilterAccessConstraint lConstraint =
                            filterAccessConstraintDao.load(lConstraintName.getConstraintName());
                    lDomainAccess.addToFilterAccessConstraintList(lConstraint);
                }
            }
        }
        else {
            lDomainAccess = null;
        }
        return lDomainAccess;
    }

    /**
     * Create the filter access.
     * <p>
     * Convert the business object to domain object and retrieve constraints.
     * 
     * @param pFilterAccessCtl
     *            filter access to create
     * @throws IllegalArgumentException
     *             if the visibility value does not correspond to an enumeration
     *             of FilterScope
     * @throws InvalidIdentifierException
     *             if the constraint specified in the business object does not
     *             exists.
     */
    public void createFilterAccess(FilterAccessCtl pFilterAccessCtl) {
        filterAccessDao.create(toDomainAccess(pFilterAccessCtl));
        // Reset cache
        depreciateAll();
    }

    /**
     * Build the execution report for a filter.
     * 
     * @param pRoleToken
     *            The role token.
     * @param pFilter
     *            The filter.
     * @return The execution report.
     */
    public FilterAdditionalConstraints getAdditionalConstraints(
            final String pRoleToken, final ExecutableFilterData pFilter) {
        final FilterExecutionReport lReport = new FilterExecutionReport();
        final String lProcessName =
                getAuthService().getProcessNameFromToken(pRoleToken);
        final String lLogin = getAuthService().getLoginFromToken(pRoleToken);
        final Map<UsableTypeData, Set<UsableFieldData>> lUsedFieldLabels =
                getSearchService().getUsableFieldsManager().getUsedFields(
                        pFilter);
        final Set<String> lProductNames =
                getFilterProducts(pRoleToken, pFilter);

        // Search the role that can be used for the different products
        for (String lProductName : lProductNames) {

            List<String> lRolesForProduct =
                    roleDao.getRoleNames(lLogin, lProductName, lProcessName,
                            true, true);

            // No role -> no execution available for this product
            if (lRolesForProduct.isEmpty()) {
                lReport.getNonExecutableProducts().add(lProductName);
            }
            else {
                for (String lUsableRole : lRolesForProduct) {
                    Set<String> lProductByRole =
                            lReport.getExecutableProducts().get(lUsableRole);

                    if (lProductByRole == null) {
                        lProductByRole = new HashSet<String>();
                        lReport.getExecutableProducts().put(lUsableRole,
                                lProductByRole);
                    }
                    lProductByRole.add(lProductName);
                }
            }
        }

        // For each type
        for (UsableTypeData lUsedType : lUsedFieldLabels.keySet()) {
            final Map<String, Set<FilterAccessContraint>> lTypeConstraintsByRole =
                    new HashMap<String, Set<FilterAccessContraint>>();

            // For each role used to execute the filter
            for (String lRoleName : lReport.getExecutableProducts().keySet()) {
                final FilterAccessDefinition lAccessDefinition =
                        getElement(new FilterAccessDefinitionKey(lProcessName,
                                lRoleName, lUsedType.getId()));
                // Load all addition constraints
                final Set<FilterAccessContraint> lAdditionalConstraints =
                        new HashSet<FilterAccessContraint>();
                // Search an access control defined on type
                final FilterAccessControl lTypeAccess =
                        lAccessDefinition.getTypeAccessControl();

                // Only take constraints into account if current role has execution access
                if (lTypeAccess == null || lTypeAccess.getExecutable()) {

                    // Add constraints for type
                    if (lTypeAccess != null) {
                        lAdditionalConstraints.addAll(lTypeAccess.getAdditionalConstraints());
                    }

                    // Search access control defined on all type's fields
                    for (UsableFieldData lUsedField : lUsedFieldLabels.get(lUsedType)) {
                        final FilterAccessControl lFieldAccess =
                                lAccessDefinition.getFieldsAccessControl().get(
                                        lUsedField.getFieldName());

                        // Add constraints for field
                        if (lFieldAccess != null) {
                            lAdditionalConstraints.addAll(lFieldAccess.getAdditionalConstraints());
                        }
                    }
                    lTypeConstraintsByRole.put(lRoleName,
                            lAdditionalConstraints);
                }
            }
            if (MapUtils.isNotEmpty(lTypeConstraintsByRole)) {
                lReport.getAdditionalConstraints().put(lUsedType,
                        lTypeConstraintsByRole);
            }
        }

        return new FilterAdditionalConstraints(lReport,
                buildAdditionalConstraints(lReport));
    }

    /**
     * Get all the products used by a filter.
     * 
     * @param pRoleToken
     *            The role token.
     * @param pFilter
     *            The filter.
     * @return The used products.
     */
    private Set<String> getFilterProducts(final String pRoleToken,
            final ExecutableFilterData pFilter) {
        final String lProcessName =
                getAuthService().getProcessNameFromToken(pRoleToken);
        final Set<String> lProductNames = new HashSet<String>();

        // Search all the products used by the filter
        if (pFilter.getFilterProductScopes() == null
                || pFilter.getFilterProductScopes().length == 0) {
            // No scope defined = UsableFieldsManager.NOT_SPECIFIED 
            lProductNames.addAll(getProductDao().getProductNames(lProcessName,
                    false));
        }
        else {
            for (FilterProductScope lScope : pFilter.getFilterProductScopes()) {
                if (lScope.getProductName().equals(
                        UsableFieldsManager.NOT_SPECIFIED)) {
                    // All the products
                    lProductNames.addAll(getProductDao().getProductNames(
                            lProcessName, false));
                }
                else {
                    final String lProductName;

                    if (lScope.getProductName().equals(
                            UsableFieldsManager.CURRENT_PRODUCT)) {
                        // Current product
                        lProductName =
                                getAuthService().getProductNameFromSessionToken(
                                        pRoleToken);
                    }
                    else {
                        lProductName = lScope.getProductName();
                    }
                    lProductNames.add(lProductName);
                    // All sub product (all levels)
                    if (lScope.isIncludeSubProducts()) {
                        lProductNames.addAll(productHierarchyManager.getElement(
                                new ProductHierarchyKey(lProcessName,
                                        lProductName)).getAllSubProductNames());
                    }
                }
            }
        }

        return lProductNames;
    }

    /**
     * Get the additional constraints to add during the filter execution.
     * 
     * @param pExecutionReport
     *            The filter execution report.
     * @return The additional constraints sort by used type 'full' id.
     */
    private Map<String, CriteriaData> buildAdditionalConstraints(
            final FilterExecutionReport pExecutionReport) {
        // Contains constraint to used for each filter's type
        final Map<String, CriteriaData> lConstraintsByType =
                new HashMap<String, CriteriaData>();

        // Search the additional constraints to add for all types used by the filters
        for (UsableTypeData lType : pExecutionReport.getAdditionalConstraints().keySet()) {
            lConstraintsByType.put(lType.getFullId(),
                    buildAdditionalConstraints(lType, pExecutionReport));
        }

        return lConstraintsByType;
    }

    /**
     * Build additional constraints to apply on a filter for a specific type.
     * This constraint apply restrictions for products scope and access
     * controls.
     * 
     * @param UsableTypeData
     *            The specific type.
     * @param pExecutionReport
     *            The execution report.
     * @return The criteria to add on a filter. Null if no restriction.
     */
    @SuppressWarnings("unchecked")
    private CriteriaData buildAdditionalConstraints(final UsableTypeData pType,
            final FilterExecutionReport pExecutionReport) {
        final List<CriteriaData> lCriterionByProducts =
                new ArrayList<CriteriaData>();
        // Get the constraints to apply on the type
        final Map<String, Set<FilterAccessContraint>> lTypeConstraints =
                pExecutionReport.getAdditionalConstraints().get(pType);
        final List<String> lListProducts = new ArrayList<String>();
        if (lTypeConstraints != null) {
            // The additional constraints are grouped by role -> by product
            for (Entry<String, Set<FilterAccessContraint>> lEntry : lTypeConstraints.entrySet()) {
                // The product for which the constraints are applied
                final Set<String> lProductNames =
                        pExecutionReport.getExecutableProducts().get(
                                lEntry.getKey());
                lProductNames.removeAll(lListProducts);
                if (lProductNames != null && !lProductNames.isEmpty()) {
                    final List<CriteriaData> lProductCriteria =
                            new ArrayList<CriteriaData>();

                    // Constraint for product scope
                    final VirtualFieldData lProductVirtualField;

                    switch (pType.getType()) {
                        case SHEET:
                        case PRODUCT:
                            // Use the sheet's product or the product
                            lProductVirtualField = CopyUtils.getMutableCopy(
                                    VirtualFieldData.PRODUCT_NAME_VIRTUAL_FIELD);
                            break;
                        case LINK:
                            // Use the product of one of the linked sheet
                            switch (pType.getLinkDirection()) {
                                case DESTINATION:
                                    lProductVirtualField = CopyUtils.getMutableCopy(
                                            VirtualFieldData.ORIGIN_PRODUCT_NAME_VIRTUAL_FIELD);
                                    break;
                                default:
                                    lProductVirtualField = CopyUtils.getMutableCopy(
                                            VirtualFieldData.DEST_PRODUCT_NAME_VIRTUAL_FIELD);
                            }
                            break;
                        default:
                            throw new NotImplementedException("Invalid type "
                                    + pType.getType());
                    }
                    if (lProductVirtualField != null) {
                        lProductVirtualField.setFieldsContainerHierarchy(Collections.EMPTY_LIST);
                        lProductCriteria.add(new CriteriaFieldData(
                                Operators.IN, true, new MultiStringValueData(
                                        lProductNames), lProductVirtualField));
                    }

                    // The additional constraints for the current group of product
                    for (FilterAccessContraint lConstraint : lEntry.getValue()) {
                        lProductCriteria.add(lConstraint.getConstraints());
                    }

                    if (!lProductCriteria.isEmpty()) {
                        // All the criteria must be respected : operator AND
                        lCriterionByProducts.add(linkCriteria(Operators.AND,
                                lProductCriteria));
                    }
                    lListProducts.addAll(lProductNames);
                }
            }
        }

        // The product must be at less on one of the product group : operator OR
        return linkCriteria(Operators.OR, lCriterionByProducts);
    }

    /**
     * Link a group of criterion with an operator AND or OR.
     * 
     * @param pOperator
     *            The operator used to link criteria.
     * @param pSubCriteria
     *            The criteria to link.
     * @return The global criteria. Can be null.
     */
    private CriteriaData linkCriteria(final String pOperator,
            final List<CriteriaData> pSubCriteria) {
        int lNbCriteria = pSubCriteria.size();

        switch (lNbCriteria) {
            case 0:
                return null;
            case 1:
                return pSubCriteria.get(0);
            default:
                return new OperationData(pOperator,
                        pSubCriteria.toArray(new CriteriaData[lNbCriteria]));
        }
    }
}
