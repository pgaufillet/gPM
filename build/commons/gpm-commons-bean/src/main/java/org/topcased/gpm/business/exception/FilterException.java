/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.exception;

/**
 * Exception for provided by filters functionalities.
 * <p>
 * Add information about the filter (its name)
 * 
 * @author mkargbo
 */
public class FilterException extends GDMException {

    /** serialVersionUID */
    private static final long serialVersionUID = 5292738571969360271L;

    /** Exception message for query error */
    public static final String DEFAULT_MESSAGE_QUERY_ERROR =
            "Cannot execute filter query";

    /** Exception message while reading filter's results */
    public static final String DEFAULT_MESSAGE_RESULTS_READING =
            "Error while reading filter's results";

    private String filterName;

    /**
     * FilterException constructor
     * 
     * @param pFilterName
     *            Filter's name
     */
    public FilterException(String pFilterName) {
        super();
        filterName = pFilterName;
    }

    /**
     * FilterException constructor
     * 
     * @param pFilterName
     *            Filter's name
     * @param pMessage
     *            Exception message
     */
    public FilterException(String pFilterName, String pMessage) {
        super(pMessage);
        filterName = pFilterName;
    }

    /**
     * FilterException constructor
     * 
     * @param pFilterName
     *            Filter's name
     * @param pMessage
     *            Exception message
     * @param pCause
     *            Cause
     */
    public FilterException(String pFilterName, String pMessage, Throwable pCause) {
        super(pMessage, pCause);
        filterName = pFilterName;
    }

    public String getFilterName() {
        return filterName;
    }

    public void setFilterName(String pFilterName) {
        filterName = pFilterName;
    }
}
