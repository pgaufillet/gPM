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

import java.util.Map;

import org.topcased.gpm.business.util.ExportFormat;
import org.topcased.gpm.business.util.ExportParameter;
import org.topcased.gpm.ui.application.shared.command.AbstractCommandAction;

/**
 * ExportFilterResultAction
 * 
 * @author nveillet
 */
public class ExportFilterResultAction extends
        AbstractCommandAction<ExportFileResult> {
    /** serialVersionUID */
    private static final long serialVersionUID = 3598666658319625943L;

    private Map<ExportParameter, String> additionalParameters;

    private ExportFormat exportFormat;

    private String filterId;

    /**
     * create action
     */
    public ExportFilterResultAction() {
    }

    /**
     * Create action with values
     * 
     * @param pProductName
     *            the product name
     * @param pFilterId
     *            the filter id
     * @param pExportFormat
     *            the export format
     * @param pAdditionalParameters
     *            the additional parameters
     */
    public ExportFilterResultAction(String pProductName, String pFilterId,
            ExportFormat pExportFormat,
            Map<ExportParameter, String> pAdditionalParameters) {
        super(pProductName);
        exportFormat = pExportFormat;
        filterId = pFilterId;
        additionalParameters = pAdditionalParameters;
    }

    /**
     * get additionalParameters
     * 
     * @return the additionalParameters
     */
    public Map<ExportParameter, String> getAdditionalParameters() {
        return additionalParameters;
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
     * get filter id
     * 
     * @return the filter id
     */
    public String getFilterId() {
        return filterId;
    }

    /**
     * set additionalParameters
     * 
     * @param pAdditionalParameters
     *            the additionalParameters to set
     */
    public void setAdditionalParameters(
            Map<ExportParameter, String> pAdditionalParameters) {
        additionalParameters = pAdditionalParameters;
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
     * set filter id
     * 
     * @param pFilterId
     *            the filter id
     */
    public void setFilterId(String pFilterId) {
        filterId = pFilterId;
    }
}