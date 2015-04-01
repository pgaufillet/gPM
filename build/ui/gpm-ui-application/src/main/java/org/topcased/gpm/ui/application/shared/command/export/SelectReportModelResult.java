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
import org.topcased.gpm.business.util.Translation;

/**
 * SelectReportModelResult
 * 
 * @author nveillet
 */
public class SelectReportModelResult extends AbstractExportResult {

    private static final long serialVersionUID = 1416582297475328509L;

    private ExportFormat exportFormat;

    private List<Translation> reportModelNames;

    /**
     * Empty constructor for serialization.
     */
    public SelectReportModelResult() {
    }

    /**
     * Create SelectReportModelResult with export file
     * 
     * @param pAvailableReportModels
     *            the report model names
     * @param pExportFormat
     *            the export format
     */
    public SelectReportModelResult(List<Translation> pAvailableReportModels,
            ExportFormat pExportFormat) {
        super();
        reportModelNames = pAvailableReportModels;
        exportFormat = pExportFormat;
    }

    /**
     * get the export format
     * 
     * @return the export format
     */
    public ExportFormat getExportFormat() {
        return exportFormat;
    }

    /**
     * get report models
     * 
     * @return the report models
     */
    public List<Translation> getReportModels() {
        return reportModelNames;
    }
}