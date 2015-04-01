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
 * Exception raised in case of invalid identifier
 * 
 * @author llatil
 */
public class InvalidIdentifierException extends GDMException {
    private static final long serialVersionUID = 9083406643374719617L;

    private String identifier;

    /** Default user message */
    private static final String DEFAULT_INVALID_IDENT_ERROR_MSG =
            "A required element could not be retrieved, it may have been deleted. "
                    + "If you think this is an error please contact an administrator.";

    /**
     * Create a new invalid identifier exception
     * 
     * @param pIdent
     *            Invalid identifier.
     */
    public InvalidIdentifierException(final String pIdent) {
        super("Identifier " + pIdent + " invalid.");
        identifier = pIdent;
        setUserMessage(DEFAULT_INVALID_IDENT_ERROR_MSG);
    }

    /**
     * Create a new invalid identifier exception
     * 
     * @param pIdent
     *            Invalid identifier.
     * @param pCause
     *            The exception cause.
     */
    public InvalidIdentifierException(final String pIdent,
            final Throwable pCause) {
        super("Identifier " + pIdent + " invalid.", pCause);
        identifier = pIdent;
        setUserMessage(DEFAULT_INVALID_IDENT_ERROR_MSG);
    }

    /**
     * Constructs a new InvalidIdentifierException.
     * <p>
     * The exception message can be tailored through the pMessage pattern. The
     * pMessage pattern is processed through a MessageFormat with the invalid
     * name as {0} argument.
     * 
     * @param pInvalidIdent
     *            Identifier
     * @param pMessage
     *            Message pattern (ex: "The identifier {0} is invalid")
     * @see MessageFormat
     */
    public InvalidIdentifierException(String pInvalidIdent, String pMessage) {
        super(MessageFormat.format(pMessage, pInvalidIdent));
        identifier = pInvalidIdent;
        setUserMessage(DEFAULT_INVALID_IDENT_ERROR_MSG);
    }

    /**
     * Get the invalid identifier
     * 
     * @return Identifier
     */
    public String getIdentifier() {
        return identifier;
    }
}
