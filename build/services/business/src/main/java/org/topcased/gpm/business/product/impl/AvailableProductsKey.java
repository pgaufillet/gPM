/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.product.impl;

import org.topcased.gpm.business.cache.CacheKey;

/**
 * Key used to access on the cache of the AvailableProductsManager.
 * 
 * @author tpanuel
 */
public class AvailableProductsKey extends CacheKey {
    private static final long serialVersionUID = 5910689490101210187L;

    private final String processName;

    private final String login;

    /**
     * Create a key to access on the cache of the AvailableProductsManager. All
     * the fields are final.
     * 
     * @param pProcessName
     *            The name of the process.
     * @param pLogin
     *            The login of the user.
     */
    public AvailableProductsKey(final String pProcessName, final String pLogin) {
        super(pProcessName + '|' + pLogin);
        processName = pProcessName;
        login = pLogin;
    }

    /**
     * Get the process name.
     * 
     * @return The process name.
     */
    public String getProcessName() {
        return processName;
    }

    /**
     * Get the login.
     * 
     * @return The login.
     */
    public String getLogin() {
        return login;
    }
}