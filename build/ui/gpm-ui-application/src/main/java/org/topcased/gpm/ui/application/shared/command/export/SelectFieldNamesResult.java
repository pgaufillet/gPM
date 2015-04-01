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
import org.topcased.gpm.ui.facade.shared.export.UiExportableGroup;

/**
 * SelectFieldNamesResult
 * 
 * @author nveillet
 */
public class SelectFieldNamesResult extends AbstractExportResult {
    private static final long serialVersionUID = -76682710211546530L;

    private List<UiExportableGroup> exportableFields;

    private ExportFormat exportFormat;

    /**
     * Empty constructor for serialization.
     */
    public SelectFieldNamesResult() {
    }

    /**
     * Create SelectFieldNamesResult with export file
     * 
     * @param pExportableFields
     *            the field names
     * @param pExportFormat
     *            the export format
     */
    public SelectFieldNamesResult(List<UiExportableGroup> pExportableFields,
            ExportFormat pExportFormat) {
        super();
        exportableFields = pExportableFields;
        exportFormat = pExportFormat;
    }

    /**
     * get exportable fields
     * 
     * @return the exportable fields
     */
    public List<UiExportableGroup> getExportableFields() {
        return exportableFields;
    }

    /**
     * get the export format
     * 
     * @return the export format
     */
    public ExportFormat getExportFormat() {
        return exportFormat;
    }
}