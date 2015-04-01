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
import org.topcased.gpm.business.importation.impl.report.ImportTermination;

/**
 * ElementReport the error induce by an element.
 * 
 * @author mkargbo
 */
public class ElementReport {

    /** The elementId. */
    private String elementId;

    /** The elementType */
    private ElementType elementType;

    /** The importResult */
    private ImportTermination importResult;

    /**
     * Default constructor
     */
    public ElementReport() {
    }

    /**
     * Element report constructor.
     * 
     * @param pElementId
     *            Identifier of the element
     * @param pElementType
     *            Type of the element.
     * @param pImportResult
     *            Result of the import.
     */
    public ElementReport(String pElementId, ElementType pElementType,
            ImportTermination pImportResult) {
        elementId = pElementId;
        elementType = pElementType;
        importResult = pImportResult;
    }

    /**
     * Element id getter.
     * 
     * @return the element id.
     */
    public String getElementId() {
        return elementId;
    }

    /**
     * The element id setter.
     * 
     * @param pElementId
     *            the element id.
     */
    public void setElementId(String pElementId) {
        elementId = pElementId;
    }

    /**
     * Element type getter.
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
     * Import result getter.
     * 
     * @return the import result.
     */
    public ImportTermination getImportResult() {
        return importResult;
    }

    /**
     * Import result setter.
     * 
     * @param pImportResult
     *            the import result.
     */
    public void setImportResult(ImportTermination pImportResult) {
        importResult = pImportResult;
    }
}