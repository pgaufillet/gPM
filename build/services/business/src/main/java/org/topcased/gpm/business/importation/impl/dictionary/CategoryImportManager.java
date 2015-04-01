/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.importation.impl.dictionary;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.dictionary.CategoryAccessData;
import org.topcased.gpm.business.dictionary.CategoryData;
import org.topcased.gpm.business.environment.impl.EnvironmentServiceImpl;
import org.topcased.gpm.business.exception.ImportException;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.importation.ImportExecutionReport;
import org.topcased.gpm.business.importation.ImportProperties;
import org.topcased.gpm.business.importation.ImportProperties.ImportFlag;
import org.topcased.gpm.business.importation.impl.AbstractImportManager;
import org.topcased.gpm.business.importation.impl.report.ElementType;
import org.topcased.gpm.business.serialization.data.Category;

/**
 * CategoryImportManager: handles category importation.
 * <p>
 * A category is identified by its identifier (business and dao object) and by
 * its name (serialization object).
 * </p>
 * <p>
 * A category can be import by an administrator user and users who have
 * 'dictionary.modify' admin access.<br />
 * Only administrator can import 'PROCESS' category.
 * </p>
 * <p>
 * At least a category can be update or delete (ERASE) only if a choice field
 * does not use it.
 * </p>
 * 
 * @author mkargbo
 */
public class CategoryImportManager extends
        AbstractImportManager<Category, CategoryData> {

    private EnvironmentServiceImpl environmentServiceImpl;

    /**
     * {@inheritDoc}
     * <p>
     * Only user's who can modify the dictionary can import a category.
     * <ul>
     * <li>Administrator user</li>
     * <li>User who have admin access 'dictionary.modify'.</li>
     * </ul>
     * </p>
     * <p>
     * The be import a category must not be use by a choice field.
     * </p>
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractImportManager#canImport(java.lang.String,
     *      java.lang.Object, java.lang.String,
     *      org.topcased.gpm.business.importation.ImportProperties,
     *      org.topcased.gpm.business.importation.ImportExecutionReport)
     */
    @Override
    protected boolean canImport(String pRoleToken, Category pElement,
            String pElementId, ImportProperties pProperties,
            ImportExecutionReport pReport) throws ImportException {
        CategoryAccessData lAccess = null;
        if (pElement.getAccess() != null) {
            lAccess =
                    CategoryAccessData.fromString(pElement.getAccess().toUpperCase());
        }
        return environmentServiceImpl.isDictionaryCategoryUpdatable(pRoleToken,
                lAccess);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractImportManager#createElement(java.lang.String,
     *      java.lang.Object,
     *      org.topcased.gpm.business.extensions.service.Context,
     *      java.lang.String[])
     */
    @Override
    protected String createElement(String pRoleToken,
            CategoryData pBusinessElement, Context pContext,
            String... pAdditionalArguments) {
        environmentServiceImpl.setDictionaryCategory(pRoleToken,
                pBusinessElement);
        return environmentServiceImpl.getCategoryId(pBusinessElement.getLabelKey());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractImportManager#getBusinessObject(java.lang.String,
     *      java.lang.Object,
     *      org.topcased.gpm.business.importation.ImportProperties)
     */
    @Override
    protected CategoryData getBusinessObject(String pRoleToken,
            Category pElement, ImportProperties pProperties) {
        CategoryData lCategoryData =
                environmentServiceImpl.getCategory(pRoleToken, pElement);
        return lCategoryData;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractImportManager#getImportFlag(org.topcased.gpm.business.importation.ImportProperties)
     */
    @Override
    protected ImportFlag getImportFlag(ImportProperties pProperties) {
        return pProperties.getCategoriesFlag();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractImportManager#isElementExists(java.lang.String,
     *      java.lang.Object,
     *      org.topcased.gpm.business.importation.ImportProperties,
     *      org.topcased.gpm.business.importation.ImportExecutionReport)
     */
    @Override
    protected String isElementExists(String pRoleToken, Category pElement,
            ImportProperties pProperties, ImportExecutionReport pReport)
        throws ImportException {
        String lId = StringUtils.EMPTY;
        switch (getImportFlag(pProperties)) {
            case CREATE_ONLY:
                if (environmentServiceImpl.isCategoryExists(pElement.getName())) {
                    onFailure(
                            pElement,
                            pProperties,
                            pReport,
                            ImportException.ImportMessage.OBJECT_EXISTS.getValue());
                }
                else {
                    lId =
                            environmentServiceImpl.getCategoryId(pElement.getName());
                }
                break;
            case UPDATE_ONLY:
                if (!environmentServiceImpl.isCategoryExists(pElement.getName())) {
                    onFailure(
                            pElement,
                            pProperties,
                            pReport,
                            ImportException.ImportMessage.OBJECT_NOT_EXISTS.getValue());
                }
                else {
                    lId =
                            environmentServiceImpl.getCategoryId(pElement.getName());
                }
                break;
            case CREATE_OR_UPDATE:
            case ERASE:
                if (environmentServiceImpl.isCategoryExists(pElement.getName())) {
                    lId =
                            environmentServiceImpl.getCategoryId(pElement.getName());
                }
                break;
            default://

        }
        return lId;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractImportManager#removeElement(java.lang.String,
     *      java.lang.String,
     *      org.topcased.gpm.business.extensions.service.Context,
     *      java.lang.String[])
     */
    @Override
    protected void removeElement(String pRoleToken, String pElementId,
            Context pContext, String... pAdditionalArguments) {
        //Never remove a category
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractImportManager#updateElement(java.lang.String,
     *      java.lang.Object, java.lang.String,
     *      org.topcased.gpm.business.extensions.service.Context,
     *      java.lang.String[])
     */
    @Override
    protected void updateElement(String pRoleToken,
            CategoryData pBusinessElement, String pElementId, Context pContext,
            boolean pSheetsIgnoreVersion, String... pAdditionalArguments) {
        environmentServiceImpl.updateDictionaryCategory(pRoleToken,
                pBusinessElement);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.report.IImportTerminationHandler#getElementIdentifier(java.lang.Object)
     */
    public String getElementIdentifier(Category pElement) {
        return pElement.getName();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.report.IImportTerminationHandler#getElementType()
     */
    public ElementType getElementType() {
        return ElementType.CATEGORY;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Get the category name.
     * </p>
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractImportManager#getAdditionalElements(java.lang.Object)
     */
    @Override
    protected String[] getAdditionalElements(Category pElement) {
        return new String[] { pElement.getName() };
    }

    public void setEnvironmentServiceImpl(
            EnvironmentServiceImpl pEnvironmentServiceImpl) {
        environmentServiceImpl = pEnvironmentServiceImpl;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractImportManager#getProductNames(java.lang.Object)
     */
    @Override
    protected List<String> getProductNames(final Category pElement) {
        return Collections.emptyList();
    }
}