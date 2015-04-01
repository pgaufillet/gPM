/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Sophian Idjellidaine (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.exception;

/**
 * Exception used in case of instantiation error.
 * 
 * @author llatil
 */
public class InstantiateException extends RuntimeException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -5146827828831095389L;

    /** The error message. */
    private String errorMessage;

    /** The object in error. */
    private Object objectInError;

    /**
     * Get the object related to this exception.
     * 
     * @return Object referenced in the exception
     */
    public Object getObjectInError() {
        return objectInError;
    }

    /**
     * The Constructor.
     * 
     * @param pErrorMessage
     *            Error message
     */
    public InstantiateException(String pErrorMessage) {
        super(pErrorMessage);
        errorMessage = pErrorMessage;
    }

    /**
     * Constructs a new instantiate exception.
     * 
     * @param pErrorMessage
     *            the error message
     * @param pObjectInError
     *            the object in error
     */
    public InstantiateException(String pErrorMessage, Object pObjectInError) {
        super(pErrorMessage);
        errorMessage = pErrorMessage;
        objectInError = pObjectInError;
    }

    /**
     * Constructs a new instantiate exception.
     * 
     * @param pErrorMessage
     *            the error message
     * @param pObjectInError
     *            the object in error
     * @param pCause
     *            Cause
     */
    public InstantiateException(String pErrorMessage, Object pObjectInError,
            Throwable pCause) {
        super(pErrorMessage, pCause);
        errorMessage = pErrorMessage;
        objectInError = pObjectInError;
    }

    /**
     * Gets the error message.
     * 
     * @return the error message
     */
    public String getErrorMessage() {
        return errorMessage;
    }
}
