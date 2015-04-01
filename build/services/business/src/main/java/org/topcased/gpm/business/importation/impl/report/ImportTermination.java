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
 * ImportTermination defined the termination of an importation
 * 
 * @author mkargbo
 */
public class ImportTermination {

    private Termination result;

    private String message;

    private String labelField;

    /**
     * Default constructor
     */
    public ImportTermination() {
    }

    /**
     * Import result constructor
     * 
     * @param pResult
     *            Result of importation.
     */
    public ImportTermination(Termination pResult) {
        result = pResult;
    }

    public Termination getResult() {
        return result;
    }

    public void setResult(Termination pResult) {
        result = pResult;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String pMessage) {
        this.message = pMessage;
    }

    public String getLabelField() {
        return labelField;
    }

    public void setLabelField(String pLabelField) {
        this.labelField = pLabelField;
    }

}
