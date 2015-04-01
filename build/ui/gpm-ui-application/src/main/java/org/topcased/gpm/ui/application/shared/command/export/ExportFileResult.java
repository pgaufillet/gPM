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

import org.topcased.gpm.business.util.ExportFormat;

/**
 * ExportFileResult
 * 
 * @author nveillet
 */
public class ExportFileResult extends AbstractExportResult {
    private static final long serialVersionUID = -1345889963898068788L;

    private String exportFileId;

    private ExportFormat format;

    /**
     * Empty constructor for serialization.
     */
    public ExportFileResult() {
    }

    /**
     * Create ExportFileResult with values
     * 
     * @param pExportFileId
     *            the export file identifier
     * @param pFormat
     *            Format of the export
     */
    public ExportFileResult(String pExportFileId, final ExportFormat pFormat) {
        super();
        exportFileId = pExportFileId;
        format = pFormat;
    }

    /**
     * get export file identifier
     * 
     * @return the export file identifier
     */
    public String getExportFileId() {
        return exportFileId;
    }

    public ExportFormat getFormat() {
        return format;
    }
}