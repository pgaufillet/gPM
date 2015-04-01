/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.importation;

import org.topcased.gpm.business.importation.impl.report.ElementType;

/**
 * GlobalErrorReport manages the error that is not attached to specific element
 * (global failure).
 * 
 * @author mkargbo
 */
public class GlobalErrorReport {

    /** The elementType. */
    private ElementType elementType;

    /** The error. */
    private String error;

    /**
     * Default constructor
     */
    public GlobalErrorReport() {
    }

    /**
     * Global error report constructor
     * 
     * @param pElementType
     *            Type of the element
     * @param pError
     *            Message of the error.
     */
    public GlobalErrorReport(ElementType pElementType, String pError) {
        elementType = pElementType;
        error = pError;
    }

    /**
     * The element type getter.
     * 
     * @return the element type.
     */
    public ElementType getElementType() {
        return elementType;
    }

    /**
     * The element type setter.
     * 
     * @param pElementType
     *            the element type.
     */
    public void setElementType(ElementType pElementType) {
        elementType = pElementType;
    }

    /**
     * The error getter.
     * 
     * @return the error.
     */
    public String getError() {
        return error;
    }

    /**
     * The error setter.
     * 
     * @param pError
     *            the error.
     */
    public void setError(String pError) {
        error = pError;
    }
}