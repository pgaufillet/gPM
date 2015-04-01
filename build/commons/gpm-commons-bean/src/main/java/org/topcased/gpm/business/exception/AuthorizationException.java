/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Thomas Szadel
 * (Atos Origin), Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.exception;

import org.apache.commons.lang.StringUtils;

/**
 * AuthorizationException
 * 
 * @author ???
 */
public class AuthorizationException extends BusinessException {
    /** Name of the element that is not authorized. */
    private String elementName = StringUtils.EMPTY;

    /** Default user message */
    private static final String DEFAULT_AUTHORIZATION_ERROR_MSG =
            "You don't have the required authorization to perform this action.";

    /**
     * Automatically generated serial.
     */
    private static final long serialVersionUID = 7553273191262000011L;

    /**
     * AuthorizationException
     * 
     * @param pMessage
     *            the message
     */
    public AuthorizationException(String pMessage) {
        super(pMessage);
        setUserMessage(DEFAULT_AUTHORIZATION_ERROR_MSG);

    }

    /**
     * AuthorizationException
     * 
     * @param pElementName
     *            Name of the element that is not authorized.
     * @param pMessage
     *            Message of the exception
     */
    public AuthorizationException(String pElementName, String pMessage) {
        super(pMessage);
        elementName = pElementName;
        setUserMessage(DEFAULT_AUTHORIZATION_ERROR_MSG);
    }

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String pElementName) {
        elementName = pElementName;
    }
}
