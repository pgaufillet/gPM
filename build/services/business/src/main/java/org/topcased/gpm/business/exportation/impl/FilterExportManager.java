/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.exportation.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.exportation.ExportProperties;
import org.topcased.gpm.business.exportation.ExportProperties.ExportFlag;
import org.topcased.gpm.business.search.impl.SearchServiceImpl;
import org.topcased.gpm.business.serialization.data.Filter;
import org.topcased.gpm.domain.accesscontrol.EndUser;
import org.topcased.gpm.domain.product.Product;
import org.topcased.gpm.domain.search.FilterWithResultDao;

/**
 * Manager used to export filters.
 * 
 * @author tpanuel
 */
public class FilterExportManager extends AbstractExportManager<Filter> {
    private SearchServiceImpl searchServiceImpl;

    private FilterWithResultDao filterWithResultDao;

    /**
     * Setter for spring injection.
     * 
     * @param pSearchServiceImpl
     *            The service to inject.
     */
    public void setSearchServiceImpl(final SearchServiceImpl pSearchServiceImpl) {
        searchServiceImpl = pSearchServiceImpl;
    }

    /**
     * Setter for spring injection.
     * 
     * @param pFilterWithResultDao
     *            The DAO.
     */
    public void setFilterWithResultDao(
            final FilterWithResultDao pFilterWithResultDao) {
        filterWithResultDao = pFilterWithResultDao;
    }

    /**
     * Create a filter export manager.
     */
    public FilterExportManager() {
        super("filters");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.impl.AbstractExportManager#getAllElementsId(java.lang.String,
     *      org.topcased.gpm.business.exportation.ExportProperties)
     */
    @Override
    protected Iterator<String> getAllElementsId(final String pRoleToken,
            final ExportProperties pExportProperties) {
        final String[] lLimitedProductNames =
                pExportProperties.getLimitedProductsName();
        final String lProcessName =
                authorizationServiceImpl.getProcessName(pRoleToken);

        if (lLimitedProductNames == null || lLimitedProductNames.length == 0) {
            return filterWithResultDao.filtersIterator(lProcessName);
        }
        else {
            return filterWithResultDao.productFiltersIterator(lProcessName,
                    Arrays.asList(lLimitedProductNames));
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.impl.AbstractExportManager#getExportFlag(org.topcased.gpm.business.exportation.ExportProperties)
     */
    @Override
    protected ExportFlag getExportFlag(final ExportProperties pExportProperties) {
        return pExportProperties.getFiltersFlag();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.impl.AbstractExportManager#getExportedElement(java.lang.String,
     *      java.lang.String,
     *      org.topcased.gpm.business.exportation.ExportProperties)
     */
    @Override
    protected Filter getExportedElement(final String pRoleToken,
            final String pElementId, final ExportProperties pExportProperties) {
        return searchServiceImpl.getSerializableFilters(pRoleToken, pElementId);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.impl.AbstractExportManager#isValidIdentifier(java.lang.String)
     */
    @Override
    protected boolean isValidIdentifier(final String pId) {
        return filterWithResultDao.exist(pId);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.impl.AbstractExportManager#getProductNames(java.lang.String)
     */
    @Override
    protected List<String> getProductNames(final String pElementId) {
        final Product lProduct =
                filterWithResultDao.load(pElementId).getProduct();

        if (lProduct == null) {
            return Collections.emptyList();
        }
        else {
            return Collections.singletonList(lProduct.getName());
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.impl.AbstractExportManager#skipRoleSelection(java.lang.String,
     *      java.lang.String)
     */
    @Override
    protected boolean skipRoleSelection(final String pRoleToken,
            final String pElementId) {
        final EndUser lUser = filterWithResultDao.load(pElementId).getEndUser();

        return lUser != null
                && StringUtils.equals(
                        authorizationServiceImpl.getLogin(pRoleToken),
                        lUser.getLogin());
    }
}