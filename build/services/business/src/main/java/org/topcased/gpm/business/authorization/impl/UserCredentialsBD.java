/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Damien Guerin (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.authorization.impl;

import org.topcased.gpm.business.authorization.service.EndUserData;
import org.topcased.gpm.business.authorization.service.UserCredentials;
import org.topcased.gpm.domain.accesscontrol.EndUser;
import org.topcased.gpm.domain.accesscontrol.EndUserDao;

/**
 * UserCredentials implementation for BD.
 * 
 * @author dguerin
 */
public class UserCredentialsBD implements UserCredentials {

    /**
     * UserCredentialsBD constructor.
     */
    public UserCredentialsBD() {
    }

    /**
     * Get information on a user.
     * 
     * @param pLoginName
     *            Login name of the user
     * @param pCaseSensitive
     *            Login case sensitive
     *            
     * @deprecated
     * @see UserCredentialsBD#getUserInfos(String, boolean, boolean)
     *            
     * @return User info or null if login does not exist
     */
    @Deprecated
    public EndUserData getUserInfos(String pLoginName, boolean pCaseSensitive) {
        return getUserInfos(pLoginName, pCaseSensitive, false);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public EndUserData getUserInfos(String pLoginName, boolean pCaseSensitive,
            boolean pLightUserData) {
        EndUser lUser = endUserDao.getUser(pLoginName, pCaseSensitive);
        if (null == lUser) {
            return null;
        }
        return authorizationService.createEndUserData(lUser, pLightUserData);
    }

    /**
     * Validate user, pass.
     * 
     * @param pUsername
     *            login name of the user
     * @param pPasswd
     *            User password
     * @param pCaseSensitive
     *            Login case sensitive
     * @return token used to identify the user.
     */
    public EndUser validateLogin(String pUsername, String pPasswd,
            Boolean pCaseSensitive) {

        // get User in BD
        EndUser lUser = endUserDao.getUser(pUsername, pCaseSensitive);

        if (lUser == null) {
            return null;
        }

        if (isCorrectPassword(lUser.getPasswd(), pPasswd)) {
            return lUser;
        }
        // else
        return null;
    }

    /**
     * Checks if a given password corresponds to the correct encrypted password
     * in database.
     * 
     * @param pPassword
     *            the password (encrypted) in database
     * @param pToCheck
     *            the to check
     * @return true if correct, false otherwise.
     */
    private boolean isCorrectPassword(String pPassword, String pToCheck) {
        if (null == pPassword) {
            return false;
        }
        for (int lSalt = 0; lSalt < AuthorizationServiceImpl.MAX_SALT_VALUE; lSalt++) {
            if (pPassword.equals(authorizationService.encrypt(pToCheck, lSalt))) {
                return true;
            }
        }
        return false;
    }

    /** The authorization service. */
    private AuthorizationServiceImpl authorizationService;

    /**
     * Sets the authorization service impl.
     * 
     * @param pAuthorizationService
     *            the new authorization service impl
     */
    public final void setAuthorizationServiceImpl(
            AuthorizationServiceImpl pAuthorizationService) {
        authorizationService = pAuthorizationService;
    }

    private EndUserDao endUserDao;

    public final void setEndUserDao(EndUserDao pEndUserDao) {
        endUserDao = pEndUserDao;
    }
}
