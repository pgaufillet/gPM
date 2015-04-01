/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.shared.exception;

import java.util.ArrayList;

import org.topcased.gpm.business.exception.GDMException;

/**
 * UiUnexpectedException
 * 
 * @author nveillet
 */
public class UiUnexpectedException extends RuntimeException {

    /** serialVersionUID */
    private static final long serialVersionUID = 797661274117462268L;

    private ArrayList<String> displayableStackTrace;

    /** default error user message */
    private static final String DEFAULT_UNEXPECTED_USER_ERROR_MSG =
            "An unexpected error Occured. Please contact your administrator.";

    /**
     * Empty constructor for serialization
     */
    protected UiUnexpectedException() {
        super(GDMException.DEFAULT_USER_ERROR_MSG);
        displayableStackTrace = new ArrayList<String>();
    }

    /**
     * Create new UiUnexpectedException from an throwable object
     * 
     * @param pThrowable
     *            the throwable object
     */
    public UiUnexpectedException(Throwable pThrowable) {
        this(DEFAULT_UNEXPECTED_USER_ERROR_MSG, pThrowable);
    }

    /**
     * Create new UiUnexpectedException from an throwable object with a user
     * message specified
     * 
     * @param pUserMessage
     *            user understandable message
     * @param pThrowable
     *            the throwable object
     */
    public UiUnexpectedException(String pUserMessage, Throwable pThrowable) {
        super(pUserMessage);

        displayableStackTrace = new ArrayList<String>();
        // stack trace first line must indicate the error type and message
        displayableStackTrace.add(createStackTraceHeaderLine("Error Message :"));
        displayableStackTrace.add(pThrowable.getMessage() == null ? pThrowable.getClass().getName()
                : pThrowable.getClass().getName() + ": "
                        + pThrowable.getMessage());
        displayableStackTrace.add(createStackTraceHeaderLine("Stack trace :"));
        setStackTrace(pThrowable.getStackTrace());
        computeStackTrace(pThrowable);
    }

    /**
     * Compute the displayable stack trace
     * 
     * @param pThrowable
     *            the throwable object
     */
    private void computeStackTrace(Throwable pThrowable) {
        for (StackTraceElement lStackTraceElement : pThrowable.getStackTrace()) {
            displayableStackTrace.add(lStackTraceElement.toString());
        }
        if (pThrowable.getCause() != null
                && !pThrowable.getCause().equals(pThrowable)) {
            displayableStackTrace.add(createStackTraceHeaderLine("Caused by :")
                    + pThrowable.getCause().getMessage());
            displayableStackTrace.add(createStackTraceHeaderLine("Nested stack trace :"));
            computeStackTrace(pThrowable.getCause());
        }
    }

    /**
     * get displayableStackTrace
     * 
     * @return the displayableStackTrace
     */
    public ArrayList<String> getDisplayableStackTrace() {
        return displayableStackTrace;
    }

    /**
     * private tool to create a highlight on a text in the stack trace popup
     * 
     * @param pText
     * @return
     */
    private String createStackTraceHeaderLine(String pText) {
        return "<p><b>" + pText + "</b></p>";
    }
}
