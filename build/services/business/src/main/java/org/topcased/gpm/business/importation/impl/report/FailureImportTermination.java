/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.importation.impl.report;

/**
 * FailureImportTermination define a the failure termination for an importation.
 * <p>
 * Contains the error's message.
 * </p>
 * 
 * @author mkargbo
 */
public class FailureImportTermination extends ImportTermination {

    private String error;

    /**
     * Default constructor
     */
    public FailureImportTermination() {
        super();
    }

    /**
     * Failure import result constructor
     * 
     * @param pError
     *            Error's message.
     */
    public FailureImportTermination(String pError) {
        super(Termination.FAILURE);
        super.setMessage(pError);
        error = pError;

    }

    /**
     * Failure import result constructor
     * 
     * @param pError
     *            Error's message.
     * @param pLabelField
     *            The Label field in error.
     */
    public FailureImportTermination(String pError, String pLabelField) {
        super(Termination.FAILURE);
        super.setMessage(pError);
        super.setLabelField(pLabelField);
        error = pError;

    }

    public String getError() {
        return error;
    }

    public void setError(String pError) {
        error = pError;
    }
}
