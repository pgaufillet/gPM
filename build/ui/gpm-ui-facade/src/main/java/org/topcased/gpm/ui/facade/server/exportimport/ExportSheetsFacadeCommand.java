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
import java.util.List;
import java.util.Locale;

import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.report.ReportModelData;
import org.topcased.gpm.business.report.service.ReportingService;
import org.topcased.gpm.business.sheet.export.service.SheetExportFormat;
import org.topcased.gpm.business.sheet.export.service.SheetExportService;
import org.topcased.gpm.ui.facade.server.FacadeCommand;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;

/**
 * ExportSheetsFacadeCommand
 * 
 * @author jlouisy
 */
public class ExportSheetsFacadeCommand implements FacadeCommand {

    private Context context;

    private SheetExportFormat exportFormat;

    private List<String> fieldsToExportList;

    private Locale locale;

    private ReportModelData reportDataModel;

    private ReportingService reportingService;

    private UiSession session;

    private SheetExportService sheetExportService;

    private List<String> sheetIds;

    /**
     * ExportSheetsFacadeCommand constructor
     * 
     * @param pReportingService
     *            {@link ReportingService}
     * @param pSheetExportService
     *            {@link SheetExportService}
     * @param pSession
     *            Current user session
     * @param pSheetIds
     *            Sheet Ids to export
     * @param pExportFormat
     *            Export format
     * @param pLocale
     *            Language Locale
     * @param pReportModelData
     *            Report model (may be null)
     * @param pFieldsToExportList
     *            List of field names to export (may be null)
     * @param pContext
     *            the execution context
     */
    public ExportSheetsFacadeCommand(ReportingService pReportingService,
            SheetExportService pSheetExportService, UiSession pSession,
            List<String> pSheetIds, SheetExportFormat pExportFormat,
            Locale pLocale, ReportModelData pReportModelData,
            List<String> pFieldsToExportList, Context pContext) {
        reportingService = pReportingService;
        sheetExportService = pSheetExportService;
        session = pSession;
        sheetIds = pSheetIds;
        exportFormat = pExportFormat;
        locale = pLocale;
        reportDataModel = pReportModelData;
        fieldsToExportList = pFieldsToExportList;
        context = pContext;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.facade.server.FacadeCommand#execute(java.io.OutputStream)
     */
    @Override
    public void execute(OutputStream pOutputStream) {
        if (reportDataModel != null) {
            reportingService.generateReport(session.getRoleToken(),
                    session.getParent().getProcessName(), pOutputStream,
                    sheetIds, exportFormat, locale, reportDataModel, context);
        }
        else if (fieldsToExportList != null && !fieldsToExportList.isEmpty()) {
            sheetExportService.exportSheets(session.getRoleToken(),
                    pOutputStream, sheetIds, exportFormat, fieldsToExportList);
        }
        else {
            sheetExportService.exportSheets(session.getRoleToken(),
                    pOutputStream, sheetIds, exportFormat);
        }
    }

}
