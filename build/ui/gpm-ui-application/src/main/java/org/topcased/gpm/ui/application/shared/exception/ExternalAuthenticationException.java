/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.shared.exception;

import net.customware.gwt.dispatch.shared.ActionException;

/**
 * Exception thrown by external authentication
 * 
 * @author mkargbo
 */
public class ExternalAuthenticationException extends ActionException {

    /** serialVersionUID */
    private static final long serialVersionUID = -4853474461120050310L;

    private static final String USER_MESSAGE_FIRST_PART =
            "Unable to authenticate user in the application. ";

    public static final String MISSING_USER_ID =
            "Missing parameter( user identifier )";

    /**
     * Default constructor
     */
    public ExternalAuthenticationException() {
        super(USER_MESSAGE_FIRST_PART);
    }

    /**
     * Constructor with message
     * 
     * @param pMessage
     *            Message of the exception
     */
    public ExternalAuthenticationException(final String pMessage) {
        super(USER_MESSAGE_FIRST_PART + pMessage);
    }
}