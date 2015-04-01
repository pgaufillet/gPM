/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Thomas Szadel
 * (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.exception;

import java.text.MessageFormat;

/**
 * This exception is thrown when a token (role, user...) is invalid or the
 * associated session has expired
 * 
 * @author tszadel
 */
public class InvalidTokenException extends GDMException {

    /** Generated Serial Version. */
    private static final long serialVersionUID = 7617411207698040408L;

    /**
     * Constructs a new invalid token exception.
     * 
     * @param pMessage
     *            Exception message
     */
    public InvalidTokenException(String pMessage) {
        super(pMessage);
    }

    /**
     * Constructs a new InvalidTokenException.
     * <p>
     * The exception message can be tailored through the pMessage pattern. The
     * pMessage pattern is processed through a MessageFormat with the invalid
     * name as {0} argument.
     * 
     * @param pInvalidToken
     *            Invalid token
     * @param pMessage
     *            Message pattern (ex: "The session token {0} is invalid")
     * @see MessageFormat
     */
    public InvalidTokenException(String pInvalidToken, String pMessage) {
        super(MessageFormat.format(pMessage, pInvalidToken));
    }

}
