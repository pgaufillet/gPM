/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.exception;

/**
 * SchemaValidationException
 * 
 * @author nveillet
 */
public class SchemaValidationException extends GDMException {

    /** generated serialVersionUID */
    private static final long serialVersionUID = 8243153036653897189L;

    /**
     * Default constructor
     */
    public SchemaValidationException() {
    }

    /**
     * Create the schema validation exception from a message
     * 
     * @param pMessage
     *            The message
     */
    public SchemaValidationException(String pMessage) {
        super(pMessage);
    }

    /**
     * Create the schema validation exception from a cause
     * 
     * @param pCause
     *            The cause
     */
    public SchemaValidationException(Throwable pCause) {
        super(pCause);
    }

    /**
     * Create an schema validation exception from a message and a cause
     * 
     * @param pMessage
     *            The message
     * @param pCause
     *            The cause
     */
    public SchemaValidationException(String pMessage, Throwable pCause) {
        super(pMessage, pCause);
    }

}
