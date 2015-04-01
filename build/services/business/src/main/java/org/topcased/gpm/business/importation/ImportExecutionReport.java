/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin), Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.importation;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.importation.impl.report.ElementType;
import org.topcased.gpm.business.importation.impl.report.ImportTermination;

/**
 * Define the result of an import.
 * <p>
 * A report contains:
 * <ul>
 * <li>List of the elements that have been imported and the import result
 * 'SUCCESS' or 'FAILURE'.</li>
 * <li>List of the errors occurred during the importation.</li>
 * </ul>
 * </p>
 * 
 * @author tpanuel
 */
public class ImportExecutionReport {

    private List<String> sheetIdList;

    /** The list reports. */
    private List<ElementReport> reports;

    /** The list errors */
    private List<GlobalErrorReport> errors;

    /**
     * Default constructor.
     */
    public ImportExecutionReport() {
        reports = new ArrayList<ElementReport>();
        errors = new ArrayList<GlobalErrorReport>();
    }

    /**
     * Add an element's report.
     * 
     * @param pElementId
     *            Identifier of the element.
     * @param pElementType
     *            Type of the element
     * @param pImportResult
     *            Result of the import.
     */
    public void add(final String pElementId, final ElementType pElementType,
            final ImportTermination pImportResult) {
        ElementReport lReport =
                new ElementReport(pElementId, pElementType, pImportResult);
        reports.add(lReport);
    }

    /**
     * Append a list of the element's reports
     * 
     * @param pReports
     *            Reports to append.
     */
    public void addReports(List<ElementReport> pReports) {
        reports.addAll(pReports);
    }

    public List<ElementReport> getReports() {
        return reports;
    }

    /**
     * Add an error report.
     * 
     * @param pElementType
     *            Type of the element raising this error.
     * @param pError
     *            Message of the error.
     */
    public void add(final ElementType pElementType, final String pError) {
        GlobalErrorReport lErrorReport =
                new GlobalErrorReport(pElementType, pError);
        errors.add(lErrorReport);
    }

    /**
     * Append a list of errors.
     * 
     * @param pErrors
     *            Errors to append.
     */
    public void addErrors(List<GlobalErrorReport> pErrors) {
        errors.addAll(pErrors);
    }

    public List<GlobalErrorReport> getErrors() {
        return errors;
    }

    /**
     * Set reports.
     * 
     * @param pReports
     *            the reports to set.
     */
    public void setReports(List<ElementReport> pReports) {
        reports = pReports;
    }

    /**
     * Set errors.
     * 
     * @param pErrors
     *            the errors to set.
     */
    public void setErrors(List<GlobalErrorReport> pErrors) {
        errors = pErrors;
    }

    /**
     * Get the element report associate to a specific element.
     * 
     * @param pElementId
     *            The element id.
     * @return The element report. Can be null.
     */
    public ElementReport getElementReport(final String pElementId) {
        for (ElementReport lElementReport : reports) {
            if (StringUtils.equals(pElementId, lElementReport.getElementId())) {
                return lElementReport;
            }
        }
        return null;
    }

    /**
     * Get the element reports corresponding to the specified type.
     * 
     * @param pElementType
     *            Type of the element to retrieve.
     * @return List of elements according to the type.
     */
    public List<ElementReport> getElementReport(final ElementType pElementType) {
        final List<ElementReport> lElements = new ArrayList<ElementReport>();
        for (ElementReport lElementReport : reports) {
            if (lElementReport.getElementType().equals(pElementType)) {
                lElements.add(lElementReport);
            }
        }
        return lElements;
    }

    public List<String> getSheetIdList() {
        if (sheetIdList == null) {
            sheetIdList = new ArrayList<String>();
        }
        return sheetIdList;
    }

    public void setSheetIdList(List<String> pSheetIdList) {
        this.sheetIdList = pSheetIdList;
    }
}