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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.topcased.gpm.business.authorization.service.PasswordEncoding;
import org.topcased.gpm.business.exportation.ExportProperties;
import org.topcased.gpm.business.exportation.ExportProperties.ExportFlag;
import org.topcased.gpm.business.serialization.data.Attribute;
import org.topcased.gpm.business.serialization.data.AttributeValue;
import org.topcased.gpm.business.serialization.data.User;
import org.topcased.gpm.domain.accesscontrol.EndUser;
import org.topcased.gpm.domain.accesscontrol.EndUserDao;

/**
 * Manager used to export users. Use the login has id.
 * 
 * @author tpanuel
 */
public class UserExportManager extends AbstractExportManager<User> {
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
     * Create a user export manager.
     */
    public UserExportManager() {
        super("users");
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
        return pExportProperties.getUsersFlag();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.impl.AbstractExportManager#getExportedElement(java.lang.String,
     *      java.lang.String,
     *      org.topcased.gpm.business.exportation.ExportProperties)
     */
    @Override
    protected User getExportedElement(final String pRoleToken,
            final String pElementId, final ExportProperties pExportProperties) {
        final EndUser lEndUser = endUserDao.load(pElementId);
        final User lUser = new User();

        // Obfuscated export
        if (ReadProperties.getInstance().isObfUsers()) {
            if (ExportationConstants.ADMIN.equals(lEndUser.getLogin())) {
                // Set the admin user
                // The admin user is not obfuscated
                setAdminUser(lEndUser, lUser);
            }
            else {
                // Obfuscate an user
                obfuscateUser(lEndUser, lUser);
            }
            ExportationData.getInstance().incrementUserCounter();
        }
        // Export without obfuscation
        else {
            lUser.setLogin(lEndUser.getLogin());
            lUser.setName(lEndUser.getName());
            lUser.setForname(lEndUser.getForname());
            lUser.setMail(lEndUser.getEmail());
            // Export encoded password
            lUser.setPassword(lEndUser.getPasswd());
            lUser.setPasswordEncoding(PasswordEncoding.MD5.getValue());
        }

        // Roles are not exported here
        lUser.setRoles(null);

        // Attributes
        if (lEndUser.getAttributes() == null
                || lEndUser.getAttributes().isEmpty()) {
            lUser.setAttributes(null);
        }
        else {
            final List<Attribute> lAttributes = new ArrayList<Attribute>();

            for (org.topcased.gpm.domain.attributes.Attribute lAttributeEntity : lEndUser.getAttributes()) {
                if (lAttributeEntity.getAttributeValues() != null) {
                    final List<AttributeValue> lAttributeValues =
                            new ArrayList<AttributeValue>();

                    for (org.topcased.gpm.domain.attributes.AttributeValue lValue : lAttributeEntity.getAttributeValues()) {
                        lAttributeValues.add(new AttributeValue(
                                lValue.getValue()));
                    }
                    lAttributes.add(new Attribute(lAttributeEntity.getName(),
                            lAttributeValues));
                }
            }
            lUser.setAttributes(lAttributes);
        }

        endUserDao.evict(lEndUser);

        return lUser;
    }

    /**
     * Obfuscate the user and add original/obfuscated values to the maps
     * 
     * @param pEndUser
     *            the EndUser
     * @param pUser
     *            the user exported
     */
    private void obfuscateUser(final EndUser pEndUser, final User pUser) {
        int lUserCounter = ExportationData.getInstance().getUserCounter();
        // Get the obfuscated login
        String lObfLogin = ExportationConstants.USER_LOGIN + lUserCounter;
        pUser.setLogin(lObfLogin);
        // Get the obfuscated user name
        String lObfUserName = ExportationConstants.USER + lUserCounter;
        pUser.setName(lObfUserName);
        // Set the others properties
        pUser.setForname("");
        pUser.setMail(lObfLogin + ExportationConstants.MAIL_PART1
                + ExportationConstants.MAIL_PART2);
        pUser.setPassword(ExportationConstants.ADMIN_MD5_PASSWORD);
        pUser.setPasswordEncoding(PasswordEncoding.MD5.getValue());

        // Update the map of obfuscated user and login in order to retrieve the user afterwards.
        HashMap<String, String> lUserLogin =
                ExportationData.getInstance().getUserLogin();
        HashMap<String, String> lUserName =
                ExportationData.getInstance().getUserName();

        if (!(lUserLogin.containsValue(lObfLogin) && lUserName.containsValue(lObfUserName))) {
            lUserLogin.put(pEndUser.getLogin(), lObfLogin);
            lUserName.put(pEndUser.getName(), lObfUserName);
        }
    }

    /**
     * Set the administrator user in the correspondence map
     * 
     * @param pEndUser
     *            the EndUser
     * @param pUser
     *            the user exported
     */
    private void setAdminUser(final EndUser pEndUser, final User pUser) {
        pUser.setLogin(pEndUser.getLogin());
        pUser.setName(ExportationConstants.GPM_ADMINISTRATOR);
        pUser.setForname("");
        pUser.setMail(ExportationConstants.ADMIN_MAIL);
        pUser.setPassword(ExportationConstants.ADMIN_MD5_PASSWORD);
        pUser.setPasswordEncoding(PasswordEncoding.MD5.getValue());
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