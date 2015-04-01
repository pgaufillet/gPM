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

/**
 * FilterExecutionException
 * 
 * @author nveillet
 */
public abstract class FilterExecutionException extends Exception {

    /** serialVersionUID */
    private static final long serialVersionUID = 5359926336391273714L;

    private String filterId;

    /**
     * Constructor
     */
    public FilterExecutionException() {
        super();
    }

    /**
     * Constructor
     * 
     * @param pFilterId
     *            the filter identifier
     */
    public FilterExecutionException(String pFilterId) {
        super();
        filterId = pFilterId;
    }

    /**
     * get filter identifier
     * 
     * @return the filter identifier
     */
    public String getFilterId() {
        return filterId;
    }

    /**
     * set filter identifier
     * 
     * @param pFilterId
     *            the filter identifier to set
     */
    public void setFilterId(String pFilterId) {
        filterId = pFilterId;
    }
}
