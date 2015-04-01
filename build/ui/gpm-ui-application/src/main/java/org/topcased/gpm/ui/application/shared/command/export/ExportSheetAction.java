/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.shared.command.export;

import java.util.List;

import org.topcased.gpm.business.util.ExportFormat;
import org.topcased.gpm.ui.application.shared.command.AbstractCommandAction;

/**
 * ExportSheetAction
 * 
 * @author nveillet
 */
public class ExportSheetAction extends
        AbstractCommandAction<AbstractExportResult> {

    /** serialVersionUID */
    private static final long serialVersionUID = 330380838577532531L;

    private ExportFormat exportFormat;

    private List<String> fieldNames;

    private String locale;

    private String reportModelName;

    private List<String> sheetIds;

    /**
     * Create action
     */
    public ExportSheetAction() {
    }

    /**
     * Create action with values
     * 
     * @param pProductName
     *            the product name
     * @param pSheetIds
     *            the sheet identifiers
     * @param pExportFormat
     *            the export format
     * @param pReportModelName
     *            the report model name
     * @param pFieldNames
     *            the field names
     * @param pLocale
     *            the user locale
     */
    public ExportSheetAction(String pProductName, List<String> pSheetIds,
            ExportFormat pExportFormat, String pReportModelName,
            List<String> pFieldNames, String pLocale) {
        super(pProductName);
        sheetIds = pSheetIds;
        exportFormat = pExportFormat;
        reportModelName = pReportModelName;
        fieldNames = pFieldNames;
        locale = pLocale;
    }

    /**
     * get export format
     * 
     * @return the export format
     */
    public ExportFormat getExportFormat() {
        return exportFormat;
    }

    /**
     * get field names
     * 
     * @return the field names
     */
    public List<String> getFieldNames() {
        return fieldNames;
    }

    /**
     * get locale
     * 
     * @return the locale
     */
    public String getLocale() {
        return locale;
    }

    /**
     * get report model name
     * 
     * @return the report model name
     */
    public String getReportModelName() {
        return reportModelName;
    }

    /**
     * get sheet identifiers
     * 
     * @return the sheet identifiers
     */
    public List<String> getSheetIds() {
        return sheetIds;
    }

    /**
     * set export format
     * 
     * @param pExportFormat
     *            the export format to set
     */
    public void setExportFormat(ExportFormat pExportFormat) {
        exportFormat = pExportFormat;
    }

    /**
     * set field names
     * 
     * @param pFieldNames
     *            the field names to set
     */
    public void setFieldNames(List<String> pFieldNames) {
        fieldNames = pFieldNames;
    }

    /**
     * set locale
     * 
     * @param pLocale
     *            the locale to set
     */
    public void setLocale(String pLocale) {
        locale = pLocale;
    }

    /**
     * set report model name
     * 
     * @param pReportModelName
     *            the report model name to set
     */
    public void setReportModelName(String pReportModelName) {
        reportModelName = pReportModelName;
    }

    /**
     * set sheet identifiers
     * 
     * @param pSheetIds
     *            the sheet identifiers to set
     */
    public void setSheetIds(List<String> pSheetIds) {
        sheetIds = pSheetIds;
    }

}
