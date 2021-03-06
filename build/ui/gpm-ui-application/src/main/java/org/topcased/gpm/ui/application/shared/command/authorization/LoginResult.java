/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael KARGBO (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.shared.command.authorization;

/**
 * LoginResult
 * 
 * @author mkargbo
 */
public class LoginResult extends AbstractConnectionResult {
    /** serialVersionUID */
    private static final long serialVersionUID = -6484480550902275618L;

    private boolean logged;

    /**
     * Default constructor
     */
    public LoginResult() {

    }

    /**
     * Constructor
     * 
     * @param pLogged
     *            True if log-in succeed, false otherwise.
     */
    public LoginResult(boolean pLogged) {
        logged = pLogged;
    }

    public boolean isLogged() {
        return logged;
    }

    public void setLogged(boolean pLogged) {
        logged = pLogged;
    }
}