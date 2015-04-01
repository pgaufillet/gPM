/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien LOUISY (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.server.exportimport;

import java.io.OutputStream;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.sheet.export.service.SheetExportFormat;
import org.topcased.gpm.business.sheet.export.service.SheetExportService;
import org.topcased.gpm.business.sheet.service.SheetSummaryData;
import org.topcased.gpm.business.util.ExportParameter;
import org.topcased.gpm.ui.facade.server.FacadeCommand;

/**
 * ExportFilterResultFacadeCommand
 * 
 * @author jlouisy
 */
public class ExportFilterResultFacadeCommand implements FacadeCommand {

    private Map<ExportParameter, String> additionalParameters;

    private Context context;

    private SheetExportFormat exportFormat;

    private Map<String, String> labels;

    private String roleToken;

    private SheetExportService service;

    private Collection<SheetSummaryData> sheetSummaries;

    /**
     * ExportFilterResultFacadeCommand constructor
     * 
     * @param pService
     *            {@link SheetExportService}
     * @param pRoleToken
     *            Current user role token.
     * @param pLabels
     *            {@link Map} of label names to be used
     * @param pSheetSummaries
     *            Collection of {@link SheetSummaryData} (filter result)
     * @param pExportFormat
     *            Export format.
     * @param pAdditionalParameters
     *            The additional parameters
     * @param pContext
     *            the execution context
     */
    public ExportFilterResultFacadeCommand(SheetExportService pService,
            String pRoleToken, Map<String, String> pLabels,
            Collection<SheetSummaryData> pSheetSummaries,
            SheetExportFormat pExportFormat,
            Map<ExportParameter, String> pAdditionalParameters, Context pContext) {
        service = pService;
        roleToken = pRoleToken;
        labels = pLabels;
        sheetSummaries = pSheetSummaries;
        exportFormat = pExportFormat;
        additionalParameters = pAdditionalParameters;
        context = pContext;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.facade.server.FacadeCommand#execute(java.io.OutputStream)
     *      *
     */
    @Override
    public void execute(OutputStream pOutputStream) {

        if (additionalParameters != null) {
            for (Entry<ExportParameter, String> lEntry : additionalParameters.entrySet()) {
                context.put(lEntry.getKey().toString(), lEntry.getValue());
            }
        }

        service.exportSheetSummaries(roleToken, pOutputStream, labels,
                sheetSummaries, exportFormat, context);
    }
}
