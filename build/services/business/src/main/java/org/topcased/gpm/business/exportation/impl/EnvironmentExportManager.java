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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.topcased.gpm.business.exportation.ExportProperties;
import org.topcased.gpm.business.exportation.ExportProperties.ExportFlag;
import org.topcased.gpm.business.serialization.data.Category;
import org.topcased.gpm.business.serialization.data.CategoryValue;
import org.topcased.gpm.business.serialization.data.Environment;
import org.topcased.gpm.domain.dictionary.CategoryValueInfo;
import org.topcased.gpm.domain.dictionary.EnvironmentDao;

/**
 * Manager used to export environment.
 * 
 * @author tpanuel
 */
public class EnvironmentExportManager extends
        AbstractNamedElementExportManager<Environment> {
    private EnvironmentDao environmentDao;

    /**
     * Setter for spring injection.
     * 
     * @param pEnvironmentDao
     *            The DAO.
     */
    public void setEnvironmentDao(final EnvironmentDao pEnvironmentDao) {
        environmentDao = pEnvironmentDao;
    }

    /**
     * Create a environment export manager.
     */
    public EnvironmentExportManager() {
        super("environments");
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
        return environmentDao.environmentsIterator(authorizationServiceImpl.getProcessName(pRoleToken));
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.impl.AbstractExportManager#getExportFlag(org.topcased.gpm.business.exportation.ExportProperties)
     */
    @Override
    protected ExportFlag getExportFlag(final ExportProperties pExportProperties) {
        return pExportProperties.getEnvironmentsFlag();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.impl.AbstractExportManager#getExportedElement(java.lang.String,
     *      java.lang.String,
     *      org.topcased.gpm.business.exportation.ExportProperties)
     */
    @Override
    protected Environment getExportedElement(final String pRoleToken,
            final String pElementId, final ExportProperties pExportProperties) {
        final String lProcessName =
                authorizationServiceImpl.getProcessName(pRoleToken);
        final org.topcased.gpm.domain.dictionary.Environment lDomainEnvironment =
                environmentDao.load(pElementId);
        final Environment lEnvironment = new Environment();
        final Map<String, Category> lCategoriesMap =
                new HashMap<String, Category>();

        lEnvironment.setName(lDomainEnvironment.getName());
        lEnvironment.setIsPublic(lDomainEnvironment.isIsPublic());
        // Fill values
        for (CategoryValueInfo lValueInfo : environmentDao.getCategoryValues(
                lProcessName, lDomainEnvironment.getName(), null)) {
            final String lCategoryName = lValueInfo.getCategoryName();
            final String lCategoryValue = lValueInfo.getCategoryValue();
            Category lCategory = lCategoriesMap.get(lCategoryName);

            // Create category if it does not exist
            if (lCategory == null) {
                lCategory = createCategory(lCategoryName);
                lCategoriesMap.put(lCategoryName, lCategory);
            }

            lCategory.getValues().addAll(
                    getCategoryToExport(lCategoryValue, lCategory));
        }

        // If no value, list must be null
        if (lCategoriesMap.isEmpty()) {
            lEnvironment.setCategories(null);
        }
        else {
            lEnvironment.setCategories(new ArrayList<Category>(
                    lCategoriesMap.values()));
        }

        return lEnvironment;
    }

    /**
     * Get the category to export, obfuscated or not
     * 
     * @param lCategoryValue
     *            the category value
     * @param lCategory
     *            the category
     */
    private List<CategoryValue> getCategoryToExport(
            final String lCategoryValue, Category lCategory) {
        List<CategoryValue> lCategoryValues = new ArrayList<CategoryValue>();
        // Obfuscation of the category
        if (ReadProperties.getInstance().isObfEnvironments()) {
            lCategoryValues.addAll(obfuscateValue(lCategory, lCategoryValue));
        }
        // No obfuscation
        else {
            lCategoryValues.add(new CategoryValue(lCategoryValue));
        }
        return lCategoryValues;
    }

    /**
     * Create a category
     * 
     * @param pCategoryName
     *            the category name
     * @return a new category
     */
    private Category createCategory(final String pCategoryName) {
        final Category lCategory = new Category();
        lCategory.setName(pCategoryName);
        lCategory.setAccess(null);
        lCategory.setValues(new ArrayList<CategoryValue>());
        return lCategory;
    }

    /**
     * Obfuscate a value : change original users and products by the obfuscated
     * values, and create new products for some obfuscated objects
     * 
     * @param pDomainCategory
     *            the category
     * @param pValue
     *            the value
     * @return a CategoryValue list
     */
    private List<CategoryValue> obfuscateValue(final Category pDomainCategory,
            final String pValue) {

        final List<CategoryValue> lCategoryValues =
                new ArrayList<CategoryValue>();

        if (ExportationConstants.USER.equals(pDomainCategory.getName())) {
            String lValue = pValue;
            if ((getObfUserName(pValue) != null)) {
                lValue = getObfUserName(pValue);
            }
            lCategoryValues.add(new CategoryValue(lValue));
        }
        else if ((getObfProductName(pValue) != null)) {
            lCategoryValues.add(new CategoryValue(getObfProductName(pValue)));
        }
        else {
            lCategoryValues.addAll(obfuscateElement(pValue));
        }

        return lCategoryValues;
    }

    /**
     * Get the obfuscated value of the product
     * 
     * @param pValue
     *            the String corresponding to the original product
     * @return the obfuscated product
     */
    private String getObfProductName(String pValue) {
        return ExportationData.getInstance().getProductNames().get(pValue);
    }

    /**
     * Get the obfuscated value of the user
     * 
     * @param pValue
     *            the String corresponding to the original username
     * @return the obfuscated username
     */
    private String getObfUserName(String pValue) {
        return ExportationData.getInstance().getUserName().get(pValue);

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.impl.AbstractExportManager#isValidIdentifier(java.lang.String)
     */
    @Override
    protected boolean isValidIdentifier(final String pId) {
        return environmentDao.exist(pId);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.impl.AbstractExportManager#getProductNames(java.lang.String)
     */
    protected List<String> getProductNames(final String pElementId) {
        return environmentDao.getEnvironmentProductNames(pElementId);
    }
}
