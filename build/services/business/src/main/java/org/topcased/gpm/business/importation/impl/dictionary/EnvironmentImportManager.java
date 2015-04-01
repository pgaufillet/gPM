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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.dictionary.CategoryValueData;
import org.topcased.gpm.business.dictionary.EnvironmentData;
import org.topcased.gpm.business.environment.impl.EnvironmentServiceImpl;
import org.topcased.gpm.business.exception.ImportException;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.importation.ImportExecutionReport;
import org.topcased.gpm.business.importation.ImportProperties;
import org.topcased.gpm.business.importation.ImportProperties.ImportFlag;
import org.topcased.gpm.business.importation.impl.AbstractImportManager;
import org.topcased.gpm.business.importation.impl.report.ElementType;
import org.topcased.gpm.business.serialization.data.Category;
import org.topcased.gpm.business.serialization.data.CategoryValue;
import org.topcased.gpm.business.serialization.data.Environment;

/**
 * EnvironmentImportManager: Handles the environment's importation.
 * <p>
 * An environment is identified by its technical identifier (business and dao
 * objects) and by its name for serialize objects.
 * </p>
 * <p>
 * An environment can be import by:
 * <ul>
 * <li>Global administrator only for no public environment.</li>
 * <li>Uses who have admin access 'environment.create' and 'environment.modify'</li>
 * <li>User connected to a product and this product is associated to the
 * environment and has an admin access.</li>
 * </ul>
 * </p>
 * <p>
 * At least an environment can be update or delete (ERASE) only if a product
 * does not use it.
 * </p>
 * <p>
 * Environment's categories values have be consider as additional elements of an
 * environment and are imported in a second time (additionalImport).
 * </p>
 * <p>
 * Additional argument is the name of the environment.
 * </p>
 * 
 * @author mkargbo
 */
public class EnvironmentImportManager extends
        AbstractImportManager<Environment, EnvironmentData> {

    /** ADDITIONAL_ELEMENT_NAME_INDEX */
    private static final int ADDITIONAL_ELEMENT_NAME_INDEX = 0;

    private static final Boolean IS_PUBLIC_DEFAULT_VALUE = Boolean.TRUE;

    private EnvironmentServiceImpl environmentServiceImpl;

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractImportManager#canImport(java.lang.String,
     *      java.lang.Object, java.lang.String,
     *      org.topcased.gpm.business.importation.ImportProperties,
     *      org.topcased.gpm.business.importation.ImportExecutionReport)
     */
    @Override
    protected boolean canImport(String pRoleToken, Environment pElement,
            String pElementId, ImportProperties pProperties,
            ImportExecutionReport pReport) throws ImportException {
        boolean lCanImport = false;
        switch (getImportFlag(pProperties)) {
            case CREATE_ONLY:
                lCanImport =
                        environmentServiceImpl.isEnvironmentCreatable(pRoleToken);
                break;
            case UPDATE_ONLY:
                lCanImport =
                        !environmentServiceImpl.isEnvironmentUsed(pRoleToken,
                                pElement.getName())
                                && environmentServiceImpl.isEnvironmentUpdatable(
                                        pRoleToken, pElement.getName(),
                                        pElement.getIsPublic());
                break;
            case CREATE_OR_UPDATE:
            case ERASE:
                if (StringUtils.isNotBlank(pElementId)) {
                    lCanImport =
                            !environmentServiceImpl.isEnvironmentUsed(
                                    pRoleToken, pElement.getName())
                                    && environmentServiceImpl.isEnvironmentCreatable(pRoleToken);
                }
                else {
                    lCanImport =
                            environmentServiceImpl.isEnvironmentCreatable(pRoleToken);
                }
                break;
            default:
                lCanImport = true;
        }
        return lCanImport;
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
            EnvironmentData pBusinessElement, Context pContext,
            String... pAdditionalArguments) {
        String lProcessName =
                authorizationServiceImpl.getProcessNameFromToken(pRoleToken);
        environmentServiceImpl.createEnvironment(pRoleToken, lProcessName,
                pBusinessElement.getLabelKey(), pBusinessElement.isIsPublic());
        String lId =
                environmentServiceImpl.getEnvironmentId(pRoleToken,
                        pBusinessElement.getLabelKey());
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
    protected EnvironmentData getBusinessObject(String pRoleToken,
            Environment pElement, ImportProperties pProperties) {
        EnvironmentData lEnvironment = new EnvironmentData();
        lEnvironment.setLabelKey(pElement.getName());
        if (pElement.getIsPublic() != null) {
            lEnvironment.setIsPublic(pElement.getIsPublic());
        }
        else {
            lEnvironment.setIsPublic(IS_PUBLIC_DEFAULT_VALUE);
        }

        String lProcessName =
                authorizationServiceImpl.getProcessNameFromToken(pRoleToken);
        lEnvironment.setBusinessProcess(lProcessName);
        return lEnvironment;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractImportManager#getImportFlag(org.topcased.gpm.business.importation.ImportProperties)
     */
    @Override
    protected ImportFlag getImportFlag(ImportProperties pProperties) {
        return pProperties.getEnvironmentsFlag();
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
    protected String isElementExists(String pRoleToken, Environment pElement,
            ImportProperties pProperties, ImportExecutionReport pReport)
        throws ImportException {
        String lId = StringUtils.EMPTY;
        switch (getImportFlag(pProperties)) {
            case CREATE_ONLY:
                if (environmentServiceImpl.isEnvironmentExists(pRoleToken,
                        pElement.getName())) {
                    onFailure(
                            pElement,
                            pProperties,
                            pReport,
                            ImportException.ImportMessage.OBJECT_EXISTS.getValue());
                }
                else {
                    lId =
                            environmentServiceImpl.getEnvironmentId(pRoleToken,
                                    pElement.getName());
                }
                break;
            case UPDATE_ONLY:
                if (!environmentServiceImpl.isEnvironmentExists(pRoleToken,
                        pElement.getName())) {
                    onFailure(
                            pElement,
                            pProperties,
                            pReport,
                            ImportException.ImportMessage.OBJECT_NOT_EXISTS.getValue());
                }
                else {
                    lId =
                            environmentServiceImpl.getEnvironmentId(pRoleToken,
                                    pElement.getName());
                }
                break;
            case CREATE_OR_UPDATE:
            case ERASE:
                if (environmentServiceImpl.isEnvironmentExists(pRoleToken,
                        pElement.getName())) {
                    lId =
                            environmentServiceImpl.getEnvironmentId(pRoleToken,
                                    pElement.getName());
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
        String lName = pAdditionalArguments[ADDITIONAL_ELEMENT_NAME_INDEX];
        String lProcessName =
                authorizationServiceImpl.getProcessNameFromToken(pRoleToken);
        environmentServiceImpl.deleteEnvironment(pRoleToken, lProcessName,
                lName);
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
            EnvironmentData pBusinessElement, String pElementId,
            Context pContext, boolean pSheetsIgnoreVersion,
            String... pAdditionalArguments) {
        pBusinessElement.setId(pElementId);
        environmentServiceImpl.updateEnvironment(pRoleToken, pBusinessElement);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.report.IImportTerminationHandler#getElementIdentifier(java.lang.Object)
     */
    public String getElementIdentifier(Environment pElement) {
        return pElement.getName();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.report.IImportTerminationHandler#getElementType()
     */
    public ElementType getElementType() {
        return ElementType.ENVIRONMENT;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Only one additional element is set: Name of the environment.
     * </p>
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractImportManager#getAdditionalElements(java.lang.Object)
     */
    @Override
    protected String[] getAdditionalElements(Environment pElement) {
        return new String[] { pElement.getName() };
    }

    /**
     * {@inheritDoc}
     * <p>
     * Import the categories values.
     * </p>
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractImportManager#doAdditionalImport(java.lang.String,
     *      java.lang.Object, java.lang.String,
     *      org.topcased.gpm.business.extensions.service.Context)
     */
    @Override
    protected void doAdditionalImport(String pRoleToken, Environment pElement,
            String pElementId, Context pContext) {
        //Set categories
        String lProcessName =
                authorizationServiceImpl.getProcessNameFromToken(pRoleToken);
        EnvironmentData lEnvironment =
                environmentServiceImpl.getEnvironmentByName(pRoleToken,
                        lProcessName, pElement.getName());
        if (CollectionUtils.isNotEmpty(pElement.getCategories())) {
            for (Category lCategory : pElement.getCategories()) {
                lEnvironment.setCategoryKey(lCategory.getName());
                CategoryValueData[] lCategoryValues =
                        new CategoryValueData[lCategory.getValues().size()];

                // Retrieving the category values
                int i = 0;
                for (CategoryValue lCategoryValue : lCategory.getValues()) {
                    CategoryValueData lCategoryValueData =
                            new CategoryValueData();
                    lCategoryValueData.setValue(lCategoryValue.getValue());
                    lCategoryValues[i] = lCategoryValueData;
                    i++;
                }
                lEnvironment.setCategoryValueDatas(lCategoryValues);

                // Create the category with these associated values
                environmentServiceImpl.setEnvironmentCategories(pRoleToken,
                        lEnvironment);
            }
        }
    }

    public void setEnvironmentServiceImpl(
            EnvironmentServiceImpl pEnvironmentServiceImpl) {
        environmentServiceImpl = pEnvironmentServiceImpl;
    }

    /**
     * The get the product names associate to an element id.
     * 
     * @param pElement
     *            The element.
     * @return The product name.
     */
    @Override
    protected List<String> getProductNames(final Environment pElement) {
        return Collections.emptyList();
    }
}