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

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.exportation.ExportProperties;
import org.topcased.gpm.business.exportation.ExportProperties.ExportFlag;
import org.topcased.gpm.business.serialization.data.UserRole;
import org.topcased.gpm.domain.accesscontrol.EndUser;
import org.topcased.gpm.domain.accesscontrol.EndUserDao;
import org.topcased.gpm.domain.accesscontrol.Role;
import org.topcased.gpm.domain.accesscontrol.RolesForProduct;

/**
 * Manager used to export user roles.
 * 
 * @author tpanuel
 */
public class UserRoleExportManager extends
        AbstractExportManager<Collection<UserRole>> {
    private EndUserDao endUserDao;

    /**
     * Setter for spring injection.
     * 
     * @param pEndUserDao
     *            The DAO.
     */
    public void setEndUserDao(final EndUserDao pEndUserDao) {
        endUserDao = pEndUserDao;
    }

    /**
     * Create a user role export manager.
     */
    public UserRoleExportManager() {
        super("userRoles");
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
        return endUserDao.usersIterator();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.impl.AbstractExportManager#getExportFlag(org.topcased.gpm.business.exportation.ExportProperties)
     */
    @Override
    protected ExportFlag getExportFlag(final ExportProperties pExportProperties) {
        return pExportProperties.getUserRolesFlag();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.impl.AbstractExportManager#getExportedElement(java.lang.String,
     *      java.lang.String,
     *      org.topcased.gpm.business.exportation.ExportProperties)
     */
    @Override
    protected Collection<UserRole> getExportedElement(final String pRoleToken,
            final String pElementId, final ExportProperties pExportProperties) {
        final Collection<UserRole> lUserRoles = new LinkedList<UserRole>();
        final EndUser lEndUser = endUserDao.load(pElementId);
        final String[] lLimitedProductNames =
                pExportProperties.getLimitedProductsName();
        final boolean lCheckProduct =
                lLimitedProductNames != null && lLimitedProductNames.length > 0;

        // Export global roles
        // Global roles are not exported if products name are limited
        String lEndUserLogin = lEndUser.getLogin();
        if (!lCheckProduct && lEndUser.getAdminRoles() != null) {
            for (Role lGlobalRole : lEndUser.getAdminRoles()) {
                final UserRole lUserRole = new UserRole();

                lUserRole.setLogin(getUserRole(lEndUserLogin));
                lUserRole.setRoleName(lGlobalRole.getName());
                lUserRole.setProductName(null);
                lUserRoles.add(lUserRole);
            }
        }

        // Export roles by product
        if (lEndUser.getRolesForProducts() != null) {
            for (RolesForProduct lRole : lEndUser.getRolesForProducts()) {
                final String lProductName = lRole.getProduct().getName();
                boolean lProductCanBeExported = true;

                // Limit the exported of roles to specific products 
                if (lCheckProduct) {
                    lProductCanBeExported = false;
                    for (String lLimitedProductName : lLimitedProductNames) {
                        if (StringUtils.equals(lLimitedProductName,
                                lProductName)) {
                            lProductCanBeExported = true;
                            break;
                        }
                    }
                }

                // Export all the roles for the product
                if (lProductCanBeExported) {
                    for (Role lProductRole : lRole.getRoles()) {
                        final UserRole lUserRole = new UserRole();

                        lUserRole.setLogin(getUserRole(lEndUserLogin));
                        lUserRole.setRoleName(lProductRole.getName());
                        lUserRole.setProductName(getProductName(lProductName));
                        lUserRoles.add(lUserRole);
                    }
                }
            }
        }

        endUserDao.evict(lEndUser);

        return lUserRoles;
    }

    /**
     * Determine the product according to the product name given with management
     * of obfuscation
     * 
     * @param pProductName
     *            the product name
     * @return the product name to use
     */
    private String getProductName(final String pProductName) {
        String lProductName = pProductName;
        if (ReadProperties.getInstance().isObfUsers()) {
            // Obfuscated product name
            final String lObfProductName =
                    ExportationData.getInstance().getProductNames().get(
                            pProductName);
            if ((lObfProductName != null)) {
                lProductName = lObfProductName;
            }
        }
        return lProductName;
    }

    /**
     * Determine the end user login to use according to the end user login given with
     * management of obfuscation
     * 
     * @param pEndUserLogin
     *            the login of the end user
     * @return the user login to use
     */
    private String getUserRole(final String pEndUserLogin) {
        String lEndUserLogin = pEndUserLogin;
        if (ReadProperties.getInstance().isObfUsers()
                && !ExportationConstants.ADMIN.equals(pEndUserLogin)) {
            lEndUserLogin =
                    ExportationData.getInstance().getUserLogin().get(
                            pEndUserLogin);
        }
        return lEndUserLogin;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.impl.AbstractExportManager#isValidIdentifier(java.lang.String)
     */
    @Override
    protected boolean isValidIdentifier(final String pId) {
        return endUserDao.exist(pId);
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