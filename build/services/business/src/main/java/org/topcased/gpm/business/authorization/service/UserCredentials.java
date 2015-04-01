/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Damien Guérin (Atos Origin)
 ******************************************************************/

package org.topcased.gpm.business.authorization.service;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.topcased.gpm.domain.accesscontrol.EndUser;

/**
 * Interface user credentials
 * 
 * @author dguerin
 */
public interface UserCredentials {

    /**
     * Get information on a user.
     * 
     * @param pLoginName
     *            Login name of the user
     * @param pCaseSensitive
     *            Login case sensitive
     *            
     * @deprecated
     * @see UserCredentials#getUserInfos(String, boolean, boolean)
     * 
     * @return User info or null if login does not exist
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public EndUserData getUserInfos(String pLoginName, boolean pCaseSensitive);
    
    /**
     * Get information on a user.
     * 
     * @param pLoginName
     *            Login name of the user
     * @param pLightUserData
     *            This parameter has a great impact on performances. The impact
     *            will depend on the size of the array
     *            <ul>
     *            <li><b>true</b> : EndUserData without EndUserData attributes</li>
     *            <li><b>false</b> : complete EndUserData</li>
     *            </ul>
     * @param pCaseSensitive
     *            Login case sensitive
     * @return User info or null if login does not exist
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public EndUserData getUserInfos(String pLoginName, boolean pCaseSensitive, 
            boolean pLightUserData);

    /**
     * Validate user, pass
     * 
     * @param pUsername
     *            login name of the user
     * @param pPasswd
     *            User password
     * @param pCaseSensitive
     *            Login case sensitive
     * @return token used to identify the user.
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public EndUser validateLogin(String pUsername, String pPasswd,
            Boolean pCaseSensitive);
}
