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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.exportation.ExportProperties;
import org.topcased.gpm.business.exportation.ExportProperties.ExportFlag;
import org.topcased.gpm.business.serialization.data.Category;
import org.topcased.gpm.business.serialization.data.CategoryValue;
import org.topcased.gpm.domain.dictionary.CategoryDao;

/**
 * Manager used to export dictionary categories.
 * 
 * @author tpanuel
 */
public class CategoryExportManager extends AbstractNamedElementExportManager<Category> {
    private CategoryDao categoryDao;

    /**
     * Setter for spring injection.
     * 
     * @param pCategoryDao
     *            The DAO.
     */
    public void setCategoryDao(final CategoryDao pCategoryDao) {
        categoryDao = pCategoryDao;
    }

    /**
     * Create a category export manager.
     */
    public CategoryExportManager() {
        super("categories");
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
        return categoryDao.categoriesIterator(authorizationServiceImpl.getProcessName(pRoleToken));
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.impl.AbstractExportManager#getExportFlag(org.topcased.gpm.business.exportation.ExportProperties)
     */
    @Override
    protected ExportFlag getExportFlag(final ExportProperties pExportProperties) {
        return pExportProperties.getCategoriesFlag();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.impl.AbstractExportManager#getExportedElement(java.lang.String,
     *      java.lang.String,
     *      org.topcased.gpm.business.exportation.ExportProperties)
     */
    @Override
    protected Category getExportedElement(final String pRoleToken,
            final String pElementId, final ExportProperties pExportProperties) {
        final org.topcased.gpm.domain.dictionary.Category lDomainCategory =
                categoryDao.load(pElementId);
        final Category lSerializable = new Category();
        final List<CategoryValue> lValues = new ArrayList<CategoryValue>();

        // Convert object
        lSerializable.setName(lDomainCategory.getName());
        lSerializable.setAccess(StringUtils.capitalize(lDomainCategory.getAccessLevel().getValue().toLowerCase()));

        // Fill values
        if (lDomainCategory.getCategoryValues() != null) {
            for (org.topcased.gpm.domain.dictionary.CategoryValue lValue : lDomainCategory.getCategoryValues()) {
                if (ReadProperties.getInstance().isObfDictionary()) {
                    List<CategoryValue> lObfuscatedValue =
                            obfuscateValue(lDomainCategory, lValue.getValue());
                    lValues.addAll(lObfuscatedValue);
                    for (CategoryValue lCategoryValue : lObfuscatedValue) {
                        ExportationData.getInstance().getCategoryValue().add(
                                lCategoryValue.getValue());
                    }
                }
                // No obfuscation
                else {
                    lValues.add(new CategoryValue(lValue.getValue()));
                }
            }
        }

        // If no value, list must be null
        if (lValues.isEmpty()) {
            lSerializable.setValues(null);
        }
        else {
            lSerializable.setValues(lValues);
        }

        categoryDao.evict(lDomainCategory);
        return lSerializable;
    }

    /**
     * Check if a category value is a product or an user and could be
     * obfuscated, otherwise this add the original value to the dictionary.
     * 
     * @param pDomainCategory
     *            the domain category
     * @param pValue
     *            the value of the field
     * @return a list of CategoryValue corresponding to a category
     */
    private List<CategoryValue> obfuscateValue(
            final org.topcased.gpm.domain.dictionary.Category pDomainCategory,
            final String pValue) {

        final List<CategoryValue> lCategoryValues =
                new ArrayList<CategoryValue>();

        final String lObfUserName =
                ExportationData.getInstance().getUserName().get(pValue);
        final String lObfProductName =
                ExportationData.getInstance().getProductNames().get(pValue);

        if (lObfUserName != null) {
            lCategoryValues.add(new CategoryValue(lObfUserName));
        }
        else if (lObfProductName != null) {
            lCategoryValues.add(new CategoryValue(lObfProductName));
        }
        else {
            lCategoryValues.addAll(obfuscateElement(pValue));
        }
        return lCategoryValues;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.impl.AbstractExportManager#isValidIdentifier(java.lang.String)
     */
    @Override
    protected boolean isValidIdentifier(final String pId) {
        return categoryDao.exist(pId);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.impl.AbstractExportManager#getProductNames(java.lang.String)
     */
    protected List<String> getProductNames(final String pElementId) {
        return Collections.emptyList();
    }
}