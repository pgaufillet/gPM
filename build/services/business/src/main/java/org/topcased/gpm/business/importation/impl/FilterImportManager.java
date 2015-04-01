/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.importation.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.ServiceLocator;
import org.topcased.gpm.business.authorization.impl.filter.FilterAccessManager;
import org.topcased.gpm.business.exception.ImportException;
import org.topcased.gpm.business.exception.ImportException.ImportMessage;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.importation.ImportExecutionReport;
import org.topcased.gpm.business.importation.ImportProperties;
import org.topcased.gpm.business.importation.ImportProperties.ImportFlag;
import org.topcased.gpm.business.importation.impl.report.ElementType;
import org.topcased.gpm.business.search.impl.FilterCreator;
import org.topcased.gpm.business.search.impl.SearchServiceImpl;
import org.topcased.gpm.business.search.service.ExecutableFilterData;
import org.topcased.gpm.business.serialization.data.Filter;

/**
 * FilterImportManager handles filter importation.
 * <p>
 * There are tree kind of filters:
 * <ol>
 * <li>Filter defined for the whole instance: Only user who have global admin
 * role can import those filters.</li>
 * <li>Filter defined for one product: Only user who have role on the product
 * can import those filters.</li>
 * <li>Filter defined for a user: Only the filter's user can import those
 * filters.</li>
 * </ol>
 * </p>
 * <p>
 * A filter is identified by its name (label key) and optional attributes:
 * process name, product's name and user's login.
 * </p>
 * 
 * @author mkargbo
 */
public class FilterImportManager extends
        AbstractImportManager<Filter, ExecutableFilterData> {

    /** CANNOT_IMPORT_PRODUCT_FILTER */
    private static final String CANNOT_IMPORT_PRODUCT_FILTER =
            "Cannot import product filter.";

    /** CANNOT_IMPORT_USER_FILTER */
    private static final String CANNOT_IMPORT_USER_FILTER =
            "Cannot import user filter.";

    private SearchServiceImpl searchServiceImpl;

    private FilterAccessManager filterAccessManager;

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractImportManager#canImport(java.lang.String,
     *      java.lang.Object, java.lang.String,
     *      org.topcased.gpm.business.importation.ImportProperties,
     *      org.topcased.gpm.business.importation.ImportExecutionReport)
     */
    @Override
    protected boolean canImport(String pRoleToken, Filter pElement,
            String pElementId, ImportProperties pProperties,
            ImportExecutionReport pReport) throws ImportException {
        ExecutableFilterData lBusinessFilter =
                getBusinessObject(pRoleToken, pElement, pProperties);
        boolean lCanImport =
                filterAccessManager.canSaveFilter(pRoleToken, lBusinessFilter);
        if (!authorizationServiceImpl.hasGlobalAdminRole(pRoleToken)) {
            String lUserLogin =
                    lBusinessFilter.getFilterVisibilityConstraintData().getUserLogin();
            String lProductName =
                    lBusinessFilter.getFilterVisibilityConstraintData().getProductName();
            if (StringUtils.isNotBlank(lUserLogin)) {
                if (!lUserLogin.equals(authorizationServiceImpl.getLoginFromToken(pRoleToken))) {
                    onFailure(pElement, pProperties, pReport,
                            CANNOT_IMPORT_USER_FILTER);
                    lCanImport = false;
                }
            }
            if (StringUtils.isNotBlank(lProductName)) {
                if (!authorizationServiceImpl.hasRoleOnProduct(pRoleToken,
                        lProductName)) {
                    onFailure(pElement, pProperties, pReport,
                            CANNOT_IMPORT_PRODUCT_FILTER);
                    lCanImport = false;
                }
            }
        }
        return lCanImport;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractImportManager#createElement(java.lang.String,
     *      java.lang.Object,
     *      org.topcased.gpm.business.extensions.service.Context, String...)
     */
    @Override
    protected String createElement(String pRoleToken,
            ExecutableFilterData pBusinessElement, Context pContext,
            String... pAdditionalArguments) {
        String lId =
                searchServiceImpl.createExecutableFilter(pRoleToken,
                        pBusinessElement);
        return lId;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractImportManager#getBusinessObject(java.lang.String,
     *      java.lang.Object,
     *      org.topcased.gpm.business.importation.ImportProperties)
     */
    @Override
    protected ExecutableFilterData getBusinessObject(String pRoleToken,
            Filter pElement, ImportProperties pProperties) {
        FilterCreator lFilterCreator =
                new FilterCreator(
                        pRoleToken,
                        pElement,
                        authorizationServiceImpl.getProcessNameFromToken(pRoleToken),
                        ServiceLocator.instance());
        ExecutableFilterData lExecutableFilterData =
                lFilterCreator.createFilter();
        return lExecutableFilterData;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractImportManager#getImportFlag(org.topcased.gpm.business.importation.ImportProperties)
     */
    @Override
    protected ImportFlag getImportFlag(ImportProperties pProperties) {
        return pProperties.getFiltersFlag();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractImportManager#isElementExists(java.lang.String,
     *      java.lang.Object,
     *      org.topcased.gpm.business.importation.ImportProperties,
     *      ImportExecutionReport)
     */
    @Override
    protected String isElementExists(String pRoleToken, Filter pElement,
            ImportProperties pProperties, ImportExecutionReport pReport)
        throws ImportException {
        String lId = StringUtils.EMPTY;
        String lProcessName =
                authorizationServiceImpl.getProcessNameFromToken(pRoleToken);
        switch (getImportFlag(pProperties)) {
            case CREATE_ONLY:
                if (searchServiceImpl.isExists(lProcessName,
                        pElement.getProductName(), pElement.getUserLogin(),
                        pElement.getLabelKey())) {
                    throw new ImportException(ImportMessage.OBJECT_EXISTS,
                            pElement);
                }
                lId = StringUtils.EMPTY;
                break;
            case UPDATE_ONLY:
                if (!searchServiceImpl.isExists(lProcessName,
                        pElement.getProductName(), pElement.getUserLogin(),
                        pElement.getLabelKey())) {
                    throw new ImportException(ImportMessage.OBJECT_NOT_EXISTS,
                            pElement);
                }
                lId =
                        searchServiceImpl.getId(lProcessName,
                                pElement.getProductName(),
                                pElement.getUserLogin(), pElement.getLabelKey());
                break;

            case CREATE_OR_UPDATE:
            case ERASE:
                if (searchServiceImpl.isExists(lProcessName,
                        pElement.getProductName(), pElement.getUserLogin(),
                        pElement.getLabelKey())) {
                    lId =
                            searchServiceImpl.getId(lProcessName,
                                    pElement.getProductName(),
                                    pElement.getUserLogin(),
                                    pElement.getLabelKey());
                }
                break;
            default:
                lId = StringUtils.EMPTY;
        }
        return lId;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractImportManager#removeElement(java.lang.String,
     *      java.lang.String,
     *      org.topcased.gpm.business.extensions.service.Context, String...)
     */
    @Override
    protected void removeElement(String pRoleToken, String pElementId,
            Context pContext, String... pAdditionalArguments) {
        searchServiceImpl.removeExecutableFilter(pRoleToken, pElementId);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Get and add identifiers for sub-elements of the filter. (criteria, result
     * fields, sort fields)
     * </p>
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractImportManager#updateElement(java.lang.String,
     *      java.lang.Object, java.lang.String,
     *      org.topcased.gpm.business.extensions.service.Context, String...)
     */
    @Override
    protected void updateElement(String pRoleToken,
            ExecutableFilterData pBusinessElement, String pElementId,
            Context pContext, boolean pSheetsIgnoreVersion,
            String... pAdditionalArguments) {
        pBusinessElement.setId(pElementId);
        String lCriteriaComponentId =
                searchServiceImpl.getCriteriaComponentId(pElementId);
        String lResultFieldComponentId =
                searchServiceImpl.getResultFieldsComponentId(pElementId);
        String lSortFieldComponentId =
                searchServiceImpl.getSortFieldsComponentId(pElementId);
        if (pBusinessElement.getFilterData() != null) {
            pBusinessElement.getFilterData().setId(lCriteriaComponentId);
        }

        if (pBusinessElement.getResultSummaryData() != null) {
            pBusinessElement.getResultSummaryData().setId(
                    lResultFieldComponentId);
        }

        if (pBusinessElement.getResultSortingData() != null) {
            pBusinessElement.getResultSortingData().setId(lSortFieldComponentId);
        }
        searchServiceImpl.updateExecutableFilter(pRoleToken, pBusinessElement);
    }

    public void setSearchServiceImpl(SearchServiceImpl pSearchServiceImpl) {
        searchServiceImpl = pSearchServiceImpl;
    }

    public void setFilterAccessManager(FilterAccessManager pFilterAccessManager) {
        filterAccessManager = pFilterAccessManager;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.report.IImportTerminationHandler#getElementIdentifier(java.lang.Object)
     */
    public String getElementIdentifier(Filter pElement) {
        StringBuilder lIdentifier = new StringBuilder();
        lIdentifier.append(pElement.getLabelKey()).append(" (");
        lIdentifier.append(pElement.getProductName()).append("|").append(
                pElement.getUserLogin());
        lIdentifier.append(")");
        return lIdentifier.toString();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.report.IImportTerminationHandler#getElementType()
     */
    public ElementType getElementType() {
        return ElementType.FILTER;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractImportManager#getProductNames(java.lang.Object)
     */
    @Override
    protected List<String> getProductNames(final Filter pElement) {
        final String lProductName = pElement.getProductName();

        if (lProductName == null) {
            return Collections.emptyList();
        }
        else {
            return Collections.singletonList(lProductName);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractImportManager#skipRoleSelection(java.lang.String,
     *      java.lang.Object)
     */
    @Override
    protected boolean skipRoleSelection(final String pRoleToken,
            final Filter pElement) {
        return pElement.getUserLogin() != null
                && StringUtils.equals(
                        authorizationServiceImpl.getLogin(pRoleToken),
                        pElement.getUserLogin());
    }
}
