/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.authorization.impl;

/**
 * Abstract class AbstractContext
 * 
 * @author tpanuel
 */
public abstract class AbstractContext {
    private String token;

    /**
     * Refresh a session if valid, synchronized with isValid method
     * 
     * @return If the session is valid
     */
    abstract public boolean refresh();

    /**
     * Test is a session is valid, synchronized with refresh method
     * 
     * @return If the session is valid
     */
    abstract public boolean isValid();

    /**
     * Invalid a context -> priority on all the other action
     */
    abstract public void invalid();

    /**
     * A context must be associated to a session
     * 
     * @param pToken
     *            The token associated to the session
     */
    public AbstractContext(String pToken) {
        token = pToken;
    }

    /**
     * Get the token
     * 
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * Set the token
     * 
     * @param pToken
     *            the new token
     */
    public void setToken(String pToken) {
        token = pToken;
    }
}
