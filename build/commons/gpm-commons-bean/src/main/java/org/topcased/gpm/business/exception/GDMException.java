/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Thomas Szadel
 * (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.exception;

/**
 * Base class for all GDM exceptions. This exception may contain a nested
 * exception (the actual exception causing the problem). This nested exception
 * can be retrieved through getCause() method
 * 
 * @author llatil
 */
public class GDMException extends RuntimeException {

    /** Automatically generated serial. */
    private static final long serialVersionUID = -5286020573617767861L;

    /** The exception class name. */
    private String exceptionClassName;

    /** Message to be displayed to the user */
    private String userMessage;

    /** Default user message */
    public static final String DEFAULT_USER_ERROR_MSG =
            "An error Occured. If the issue persists, please contact your administrator.";

    /**
     * Create a new GDM exception object.
     */
    public GDMException() {
        super();
        exceptionClassName = getClass().getName();
        userMessage = DEFAULT_USER_ERROR_MSG;
    }

    /**
     * Create a new GDM exception object.
     * 
     * @param pMessage
     *            Message to report in the exception
     */
    public GDMException(String pMessage) {
        super(pMessage);
        exceptionClassName = getClass().getName();
        userMessage = DEFAULT_USER_ERROR_MSG;
    }

    /**
     * Create a new GDM exception object.
     * 
     * @param pTechnicalMessage
     *            Message to report in the exception
     * @param pUserMessage
     *            Message to be displayed to the final user
     */
    public GDMException(String pTechnicalMessage, String pUserMessage) {
        super(pTechnicalMessage);
        exceptionClassName = getClass().getName();
        userMessage = pUserMessage;
    }

    /**
     * Create a new GDM exception object containing a nested exception.
     * <p>
     * This constructor should be used when the problem to report is caused by
     * an internal exception (catched in the code). In this case, the original
     * exception should be passed in as the <code>cause</code> parameter in
     * order to be preserved in this exception
     * 
     * @param pCause
     *            Original exception object wrapped in this exception
     * @see Throwable.getCause()
     */
    public GDMException(Throwable pCause) {
        super(pCause);
        exceptionClassName = getClass().getName();
        userMessage = DEFAULT_USER_ERROR_MSG;
    }

    /**
     * Create a new GDM exception object containing a nested exception
     * <p>
     * This constructor should be used when the problem to report is caused by
     * an internal exception (catched in the code). In this case, the original
     * exception should be passed in as the <code>cause</code> parameter in
     * order to be preserved in this exception
     * 
     * @param pMessage
     *            Message to report in the exception
     * @param pCause
     *            Original exception object wrapped in this exception
     */
    public GDMException(String pMessage, Throwable pCause) {
        super(pMessage, pCause);
        exceptionClassName = getClass().getName();
        userMessage = DEFAULT_USER_ERROR_MSG;
    }

    /**
     * Get the content of this exception as a string.
     * 
     * @return Human readable message of the exception.
     */
    public String toString() {
        String lStrMessage = super.toString();

        if (getCause() != null) {
            lStrMessage +=
                    "\nCaused by:" + getCause().toString()
                            + "\nNested stack trace:"
                            + getCause().getStackTrace();
        }
        return lStrMessage;
    }

    /**
     * Gets the exception class name.
     * 
     * @return the exception class name
     */
    public String getExceptionClassName() {
        return exceptionClassName;
    }

    /**
     * Sets the exception class name.
     * 
     * @param pExceptionClassName
     *            the new exception class name
     */
    public void setExceptionClassName(String pExceptionClassName) {
        exceptionClassName = pExceptionClassName;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(String pUserMessage) {
        this.userMessage = pUserMessage;
    }
}
