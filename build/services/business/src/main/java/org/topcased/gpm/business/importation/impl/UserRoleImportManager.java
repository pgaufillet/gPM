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

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.exception.ImportException;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.importation.ImportExecutionReport;
import org.topcased.gpm.business.importation.ImportProperties;
import org.topcased.gpm.business.importation.ImportProperties.ImportFlag;
import org.topcased.gpm.business.importation.impl.report.ElementType;
import org.topcased.gpm.business.product.impl.ProductServiceImpl;
import org.topcased.gpm.business.serialization.data.UserRole;

/**
 * UserRoleImportManager: Handles user's role importation.
 * <p>
 * User's role is identified by 'login' and 'roleName' attributes.
 * </p>
 * <p>
 * Only administrator and users who have 'user.modify' admin access can import
 * the user's role.
 * </p>
 * <p>
 * User's role can be import if the user and role exists.
 * </p>
 * <p>
 * Its not possible to import user's role using the 'UPDATE_ONLY' flag, instead
 * you need to use 'ERASE' flag.
 * </p>
 * <p>
 * Additional user's role elements are the login, role and the product.(to
 * bypass that a user's role has no technical identifier)
 * </p>
 * 
 * @author mkargbo
 */
public class UserRoleImportManager extends
        AbstractImportManager<UserRole, UserRole> {

    /** ADDITIONAL_ELEMENTS_PRODUCT_INDEX */
    private static final int ADDITIONAL_ELEMENTS_PRODUCT_INDEX = 2;

    /** ADDITIONAL_ELEMENTS_ROLE_INDEX */
    private static final int ADDITIONAL_ELEMENTS_ROLE_INDEX = 1;

    /** ADDITIONAL_ELEMENTS_LOGIN_INDEX */
    private static final int ADDITIONAL_ELEMENTS_LOGIN_INDEX = 0;

    /** ADDITIONAL_ELEMENTS_NUMBER */
    private static final int ADDITIONAL_ELEMENTS_NUMBER = 3;

    private ProductServiceImpl productServiceImpl;

    /**
     * {@inheritDoc}
     * <p>
     * Test the user, role and product existence.
     * </p>
     * <p>
     * Only administrator and users who have 'user.modify' admin access can
     * import the user's role.
     * </p>
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractImportManager#canImport(java.lang.String,
     *      java.lang.Object, java.lang.String,
     *      org.topcased.gpm.business.importation.ImportProperties,
     *      org.topcased.gpm.business.importation.ImportExecutionReport)
     */
    @Override
    protected boolean canImport(String pRoleToken, UserRole pElement,
            String pElementId, ImportProperties pProperties,
            ImportExecutionReport pReport) throws ImportException {
        final boolean lCanImport;
        String lProcessName =
                authorizationServiceImpl.getProcessNameFromToken(pRoleToken);
        if (!authorizationServiceImpl.isUserExists(pElement.getLogin())
                || !authorizationServiceImpl.isRoleExists(
                        pElement.getRoleName(), lProcessName)) {
            lCanImport = false;
            onFailure(pElement, pProperties, pReport,
                    "Cannot assign role because login: '" + pElement.getLogin()
                            + "' or role '" + pElement.getRoleName()
                            + "' does not exists.");
        }
        else if (StringUtils.isNotBlank(pElement.getProductName())
                && !productServiceImpl.isProductExists(pRoleToken,
                        pElement.getProductName())) {
            lCanImport = false;
            onFailure(pElement, pProperties, pReport,
                    "Cannot assign role because product '"
                            + pElement.getProductName() + "' does not exist.");
        }
        else if (!authorizationServiceImpl.hasGlobalAdminRole(pRoleToken)
                && !authorizationServiceImpl.isUserUpdatable(pRoleToken,
                        pElement.getLogin())) {
            lCanImport = false;
            onFailure(pElement, pProperties, pReport, "No rights.");
        }
        else {
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
            UserRole pBusinessElement, Context pContext,
            String... pAdditionalArguments) {
        String lProductName = pBusinessElement.getProductName();
        String lProcessName =
                authorizationServiceImpl.getProcessNameFromToken(pRoleToken);
        if (StringUtils.isBlank(lProductName)) {
            authorizationServiceImpl.addRole(pRoleToken,
                    pBusinessElement.getLogin(), lProcessName,
                    pBusinessElement.getRoleName());
        }
        else {
            authorizationServiceImpl.addRole(pRoleToken,
                    pBusinessElement.getLogin(), lProcessName,
                    pBusinessElement.getRoleName(), lProductName);
        }
        return StringUtils.EMPTY;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractImportManager#getBusinessObject(java.lang.String,
     *      java.lang.Object,
     *      org.topcased.gpm.business.importation.ImportProperties)
     */
    @Override
    protected UserRole getBusinessObject(String pRoleToken, UserRole pElement,
            ImportProperties pProperties) {
        return pElement;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractImportManager#getImportFlag(org.topcased.gpm.business.importation.ImportProperties)
     */
    @Override
    protected ImportFlag getImportFlag(ImportProperties pProperties) {
        return pProperties.getUserRolesFlag();
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
    protected String isElementExists(String pRoleToken, UserRole pElement,
            ImportProperties pProperties, ImportExecutionReport pReport)
        throws ImportException {
        String lId = StringUtils.EMPTY;
        String lProductName = pElement.getProductName();
        switch (getImportFlag(pProperties)) {
            case CREATE_ONLY:
                if ((StringUtils.isBlank(lProductName))
                        && (authorizationServiceImpl.isAssignTo(
                                pElement.getLogin(), pElement.getRoleName()))) {
                    onFailure(
                            pElement,
                            pProperties,
                            pReport,
                            ImportException.ImportMessage.OBJECT_EXISTS.getValue());
                }
                else if (StringUtils.isNotBlank(lProductName)
                        && (authorizationServiceImpl.isAssignTo(
                                pElement.getLogin(), pElement.getRoleName(),
                                lProductName))) {
                    onFailure(
                            pElement,
                            pProperties,
                            pReport,
                            ImportException.ImportMessage.OBJECT_EXISTS.getValue());
                }
                break;
            case UPDATE_ONLY:
                throw new NotImplementedException(
                        "Cannot update a user's roles. You need to use ERASE mode.");
            default: //
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
        String lLogin = pAdditionalArguments[ADDITIONAL_ELEMENTS_LOGIN_INDEX];
        String lProcess =
                authorizationServiceImpl.getProcessNameFromToken(pRoleToken);
        authorizationServiceImpl.removeRoles(lLogin, lProcess);
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
    protected void updateElement(String pRoleToken, UserRole pBusinessElement,
            String pElementId, Context pContext, boolean pSheetsIgnoreVersion,
            String... pAdditionalArguments) {
        throw new NotImplementedException(
                "Cannot update a user's roles. You need to use ERASE mode.");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.report.IImportTerminationHandler#getElementIdentifier(java.lang.Object)
     */
    public String getElementIdentifier(UserRole pElement) {
        StringBuilder lBuilder = new StringBuilder();
        lBuilder.append(pElement.getLogin()).append("-").append(
                pElement.getRoleName());
        lBuilder.append("-").append(pElement.getProductName());
        return lBuilder.toString();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.report.IImportTerminationHandler#getElementType()
     */
    public ElementType getElementType() {
        return ElementType.USER_ROLE;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Additional elements are the login, role and the product.
     * </p>
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractImportManager#getAdditionalElements(java.lang.Object)
     */
    @Override
    protected String[] getAdditionalElements(UserRole pElement) {
        String[] lElements = new String[ADDITIONAL_ELEMENTS_NUMBER];
        lElements[ADDITIONAL_ELEMENTS_LOGIN_INDEX] = pElement.getLogin();
        lElements[ADDITIONAL_ELEMENTS_ROLE_INDEX] = pElement.getRoleName();
        lElements[ADDITIONAL_ELEMENTS_PRODUCT_INDEX] =
                pElement.getProductName();
        return lElements;
    }

    public void setProductServiceImpl(ProductServiceImpl pProductServiceImpl) {
        productServiceImpl = pProductServiceImpl;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractImportManager#getProductNames(java.lang.Object)
     */
    @Override
    protected List<String> getProductNames(final UserRole pElement) {
        return Collections.emptyList();
    }
}
