/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.server.exception;

import javax.servlet.ServletException;

/**
 * DownloadException
 * 
 * @author mkargbo
 */
public class DownloadException extends ServletException {

    /** serialVersionUID */
    private static final long serialVersionUID = -1222131757842154316L;

    public static final String MISSING_PARAMETER = "Missing parameter";

    public static final String PARAMETER_VALUE =
            "The following parameter's value is not handled: ";

    /**
     * default constructor
     */
    public DownloadException() {

    }

    /**
     * Constructor with message
     * 
     * @param pMessage
     *            Message of the exception
     */
    public DownloadException(final String pMessage) {
        super(pMessage);
    }
}
