/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.popup.report;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.ui.application.client.command.popup.ClosePopupAction;
import org.topcased.gpm.ui.application.client.command.popup.report.ExportSheetWithPdfModelCommand;
import org.topcased.gpm.ui.application.client.command.popup.report.ExportSheetWithXlsModelCommand;
import org.topcased.gpm.ui.application.client.command.popup.report.ExportSheetsWithPdfModelCommand;
import org.topcased.gpm.ui.application.client.command.popup.report.ExportSheetsWithXlsModelCommand;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.client.popup.PopupPresenter;
import org.topcased.gpm.ui.application.shared.command.export.SelectReportModelResult;

import com.google.inject.Inject;

/**
 * The presenter for the SelectReportView.
 * 
 * @author tpanuel
 */
public class SelectReportPresenter extends PopupPresenter<SelectReportDisplay> {
    private final ExportSheetWithPdfModelCommand sheetPdfCommand;

    private final ExportSheetWithXlsModelCommand sheetXlsCommand;

    private final ExportSheetsWithPdfModelCommand sheetsPdfCommand;

    private final ExportSheetsWithXlsModelCommand sheetsXlsCommand;

    /**
     * Create a presenter for the SelectReportView.
     * 
     * @param pDisplay
     *            The display interface.
     * @param pEventBus
     *            The event bus.
     * @param pSheetPdf
     *            The export PDF sheet command.
     * @param pSheetXls
     *            The export XLS sheet command.
     * @param pSheetsPdf
     *            The export PDF sheets command.
     * @param pSheetsXls
     *            The export XLS sheets command.
     */
    @Inject
    public SelectReportPresenter(final SelectReportDisplay pDisplay,
            final EventBus pEventBus,
            final ExportSheetWithPdfModelCommand pSheetPdf,
            final ExportSheetWithXlsModelCommand pSheetXls,
            final ExportSheetsWithPdfModelCommand pSheetsPdf,
            final ExportSheetsWithXlsModelCommand pSheetsXls) {
        super(pDisplay, pEventBus);
        sheetPdfCommand = pSheetPdf;
        sheetXlsCommand = pSheetXls;
        sheetsPdfCommand = pSheetsPdf;
        sheetsXlsCommand = pSheetsXls;
    }

    /**
     * Set the report names.
     * 
     * @param pResult
     *            The result.
     * @param pMultivalued
     *            If several sheet has been selected from the listing panel.
     */
    public void init(final SelectReportModelResult pResult,
            final boolean pMultivalued) {
        // Set the report names
        getDisplay().setReportNames(pResult.getReportModels());
        // Set the export button handler
        if (pMultivalued) {
            switch (pResult.getExportFormat()) {
                case PDF:
                    getDisplay().setExportButtonHandler(sheetPdfCommand);
                    break;
                case EXCEL:
                    getDisplay().setExportButtonHandler(sheetXlsCommand);
                    break;
                default:
                    // Nothing to do
            }
        }
        else {
            switch (pResult.getExportFormat()) {
                case PDF:
                    getDisplay().setExportButtonHandler(sheetsPdfCommand);
                    break;
                case EXCEL:
                    getDisplay().setExportButtonHandler(sheetsXlsCommand);
                    break;
                default:
                    // Nothing to do
            }
        }
    }

    /**
     * Get the selected report name.
     * 
     * @return The selected report name.
     */
    public String getSelectedReportName() {
        return getDisplay().getSelectedReportName();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.PopupPresenter#getClosePopupEvent()
     */
    @Override
    protected GlobalEvent<ClosePopupAction> getClosePopupEvent() {
        return GlobalEvent.CLOSE_REPORT_MODEL_POPUP;
    }
}