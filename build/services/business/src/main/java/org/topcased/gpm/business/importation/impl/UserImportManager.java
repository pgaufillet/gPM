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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.attributes.impl.AttributesServiceImpl;
import org.topcased.gpm.business.authorization.service.AuthorizationService;
import org.topcased.gpm.business.authorization.service.EndUserData;
import org.topcased.gpm.business.authorization.service.PasswordEncoding;
import org.topcased.gpm.business.authorization.service.UserAttributesData;
import org.topcased.gpm.business.exception.ImportException;
import org.topcased.gpm.business.exception.ImportException.ImportMessage;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.importation.ImportExecutionReport;
import org.topcased.gpm.business.importation.ImportProperties;
import org.topcased.gpm.business.importation.ImportProperties.ImportFlag;
import org.topcased.gpm.business.importation.impl.report.ElementType;
import org.topcased.gpm.business.serialization.data.Attribute;
import org.topcased.gpm.business.serialization.data.User;
import org.topcased.gpm.domain.accesscontrol.EndUser;
import org.topcased.gpm.domain.accesscontrol.EndUserDao;
import org.topcased.gpm.domain.accesscontrol.Role;

/**
 * UserImportManager handles user's importation.
 * <p>
 * The user's object identifier is its login.
 * </p>
 * <p>
 * Can import specification:
 * <ol>
 * <li>Global administrator can import users using of import mode.</li>
 * <li>On CREATE_ONLY mode, only users who have 'user.create' admin access can
 * import users.</li>
 * <li>On UPDATE_ONLY mode, only users who have 'user.update' admin access can
 * import users.</li>
 * <li>On ERASE mode, only users who have 'user.create' and 'user.delete' admin
 * access can import users.</li>
 * </ol>
 * </p>
 * <p>
 * For the importation, the context contains the 'passwordEncoding' element.
 * </p>
 * <p>
 * Additionally import the user's attributes.
 * </p>
 * 
 * @author mkargbo
 */
public class UserImportManager extends AbstractImportManager<User, EndUserData> {
    private EndUserDao endUserDao;

    private AttributesServiceImpl attributesServiceImpl;

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractImportManager#canImport(java.lang.String,
     *      java.lang.Object, java.lang.String,
     *      org.topcased.gpm.business.importation.ImportProperties,
     *      org.topcased.gpm.business.importation.ImportExecutionReport)
     */
    @Override
    protected boolean canImport(String pRoleToken, User pElement,
            String pElementId, ImportProperties pProperties,
            ImportExecutionReport pReport) throws ImportException {
        boolean lCanImport = false;
        switch (getImportFlag(pProperties)) {
            case CREATE_ONLY:
                lCanImport =
                        authorizationServiceImpl.isUserCreatable(pRoleToken);
                break;
            case UPDATE_ONLY:
                lCanImport =
                        authorizationServiceImpl.isUserUpdatable(pRoleToken,
                                pElement.getLogin());
                break;
            case ERASE:
                lCanImport =
                        authorizationServiceImpl.isUserDeletable(pRoleToken)
                                && authorizationServiceImpl.isUserCreatable(pRoleToken);
                break;
            case CREATE_OR_UPDATE:
                if (StringUtils.isBlank(pElementId)) {
                    lCanImport =
                            authorizationServiceImpl.isUserCreatable(pRoleToken);
                }
                else {
                    lCanImport =
                            authorizationServiceImpl.isUserUpdatable(
                                    pRoleToken, pElement.getLogin());
                }
                break;
            default:
                lCanImport = false;
        }
        if (!lCanImport) {
            onFailure(
                    pElement,
                    pProperties,
                    pReport,
                    ImportException.ImportMessage.CANNOT_IMPORT_ELEMENT.getValue());
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
            EndUserData pBusinessElement, Context pContext,
            String... pAdditionalArguments) {
        if (StringUtils.equals(pBusinessElement.getLogin(),
                AuthorizationService.ADMIN_LOGIN)) {
            // Administrator always exists, update it
            final String lAdminId =
                    endUserDao.getId(AuthorizationService.ADMIN_LOGIN, true);

            updateElement(pRoleToken, pBusinessElement, lAdminId, pContext,
                    false, pAdditionalArguments);

            return lAdminId;
        }
        else {
            return authorizationServiceImpl.createUser(pRoleToken,
                    pBusinessElement, pAdditionalArguments[0], pContext);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractImportManager#getBusinessObject(java.lang.String,
     *      java.lang.Object,
     *      org.topcased.gpm.business.importation.ImportProperties)
     */
    @Override
    protected EndUserData getBusinessObject(String pRoleToken, User pElement,
            ImportProperties pProperties) {
        EndUserData lUserData = new EndUserData();
        lUserData.setLogin(pElement.getLogin());
        lUserData.setForname(pElement.getForname());
        lUserData.setName(pElement.getName());
        lUserData.setMailAddr(pElement.getMail());
        List<UserAttributesData> lUserAttributesDataList =
                new ArrayList<UserAttributesData>();
        if (pElement.getAttributes() != null) {
            for (Attribute lAttribute : pElement.getAttributes()) {
                lUserAttributesDataList.add(new UserAttributesData(
                        lAttribute.getName(), lAttribute.getValue()));
            }
            lUserData.setUserAttributes(lUserAttributesDataList.toArray(new UserAttributesData[0]));
        }
        return lUserData;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractImportManager#getImportFlag(org.topcased.gpm.business.importation.ImportProperties)
     */
    @Override
    protected ImportFlag getImportFlag(ImportProperties pProperties) {
        return pProperties.getUsersFlag();
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
    protected String isElementExists(String pRoleToken, User pElement,
            ImportProperties pProperties, ImportExecutionReport pReport)
        throws ImportException {
        String lId = StringUtils.EMPTY;
        switch (getImportFlag(pProperties)) {
            case CREATE_ONLY:
                if (authorizationServiceImpl.isUserExists(pElement.getLogin())) {
                    throw new ImportException(ImportMessage.OBJECT_EXISTS,
                            pElement);
                }
                break;
            case UPDATE_ONLY:
                if (!authorizationServiceImpl.isUserExists(pElement.getLogin())) {
                    throw new ImportException(ImportMessage.OBJECT_NOT_EXISTS,
                            pElement);
                }
                else {
                    lId =
                            authorizationServiceImpl.getUserId(pElement.getLogin());
                }
                break;
            case CREATE_OR_UPDATE:
            case ERASE:
                if (authorizationServiceImpl.isUserExists(pElement.getLogin())) {
                    lId =
                            authorizationServiceImpl.getUserId(pElement.getLogin());
                }
                break;
            default:
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
        final EndUser lAdmin = endUserDao.load(pElementId);

        if (StringUtils.equals(lAdmin.getLogin(),
                AuthorizationService.ADMIN_LOGIN)) {
            // Administrator user is not removed, only clear
            lAdmin.setEmail(null);
            lAdmin.setForname(null);
            lAdmin.getAttributes().clear();
            // Remove all global role except
            Role lAdminRole = null;
            for (Role lRole : lAdmin.getAdminRoles()) {
                if (StringUtils.equals(lRole.getName(),
                        AuthorizationService.ADMIN_ROLE_NAME)) {
                    lAdminRole = lRole;
                    break;
                }
            }
            lAdmin.getAdminRoles().clear();
            if (lAdminRole != null) {
                lAdmin.getAdminRoles().add(lAdminRole);
            }
            // Remove all role for product
            lAdmin.getRolesForProducts().clear();
            // Remove all overridden role
            authorizationServiceImpl.deleteOverriddenRolesFromLogin(lAdmin.getLogin());
        }
        else {
            // Remove non Administrator user
            authorizationServiceImpl.removeUser(pRoleToken, lAdmin.getLogin());
        }
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
            EndUserData pBusinessElement, String pElementId, Context pContext,
            boolean pSheetsIgnoreVersion, String... pAdditionalArguments) {
        String lPassword = pAdditionalArguments[0];
        authorizationServiceImpl.updateUser(pRoleToken, pBusinessElement,
                lPassword, pContext);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Import user's attributes.
     * </p>
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractImportManager#doAdditionalImport(java.lang.String,
     *      java.lang.Object, java.lang.String,
     *      org.topcased.gpm.business.extensions.service.Context)
     */
    @Override
    protected void doAdditionalImport(String pRoleToken, User pElement,
            String pElementId, Context pContext) {
        attributesServiceImpl.importAttributes(pRoleToken, pElementId,
                pElement.getAttributes(), pContext);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractImportManager#fillContext(java.lang.Object,
     *      org.topcased.gpm.business.extensions.service.Context)
     */
    @Override
    protected void fillContext(User pElement, final Context pContext) {
        if (pContext.contains(Context.USER_PASSWORD_ENCODING)) {
            if (pElement.getPasswordEncoding() == null) {
                pContext.set(Context.USER_PASSWORD_ENCODING,
                        PasswordEncoding.CLEAR);
            }
            else {
                pContext.set(Context.USER_PASSWORD_ENCODING,
                        PasswordEncoding.get(pElement.getPasswordEncoding()));
            }
        }
        else {
            if (pElement.getPasswordEncoding() == null) {
                pContext.put(Context.USER_PASSWORD_ENCODING,
                        PasswordEncoding.CLEAR);
            }
            else {
                pContext.put(Context.USER_PASSWORD_ENCODING,
                        PasswordEncoding.get(pElement.getPasswordEncoding()));
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractImportManager#getAdditionalElements(java.lang.Object)
     */
    @Override
    protected String[] getAdditionalElements(User pElement) {
        return new String[] { pElement.getPassword() };
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.report.IImportTerminationHandler#getElementIdentifier(java.lang.Object)
     */
    public String getElementIdentifier(User pElement) {
        return pElement.getLogin();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.report.IImportTerminationHandler#getElementType()
     */
    public ElementType getElementType() {
        return ElementType.USER;
    }

    public void setAttributesServiceImpl(
            AttributesServiceImpl pAttributesServiceImpl) {
        attributesServiceImpl = pAttributesServiceImpl;
    }

    public void setEndUserDao(final EndUserDao pEndUserDao) {
        endUserDao = pEndUserDao;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractImportManager#getProductNames(java.lang.Object)
     */
    @Override
    protected List<String> getProductNames(final User pElement) {
        return Collections.emptyList();
    }
}
