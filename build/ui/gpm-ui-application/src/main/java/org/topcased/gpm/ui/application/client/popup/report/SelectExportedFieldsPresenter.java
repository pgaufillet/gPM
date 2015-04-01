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

import java.util.List;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.ui.application.client.command.popup.ClosePopupAction;
import org.topcased.gpm.ui.application.client.command.popup.report.ExportSheetFieldsOnXlsCommand;
import org.topcased.gpm.ui.application.client.command.popup.report.ExportSheetFieldsOnXmlCommand;
import org.topcased.gpm.ui.application.client.command.popup.report.ExportSheetsFieldsOnXlsCommand;
import org.topcased.gpm.ui.application.client.command.popup.report.ExportSheetsFieldsOnXmlCommand;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.client.popup.PopupPresenter;
import org.topcased.gpm.ui.application.shared.command.export.SelectFieldNamesResult;
import org.topcased.gpm.ui.facade.shared.export.UiExportableGroup;

import com.google.inject.Inject;

/**
 * The presenter for the SelectExportFieldsView.
 * 
 * @author tpanuel
 */
public class SelectExportedFieldsPresenter extends
        PopupPresenter<SelectExportedFieldsDisplay> {
    private final ExportSheetFieldsOnXmlCommand sheetXmlCommand;

    private final ExportSheetFieldsOnXlsCommand sheetXlsCommand;

    private final ExportSheetsFieldsOnXmlCommand sheetsXmlCommand;

    private final ExportSheetsFieldsOnXlsCommand sheetsXlsCommand;

    /**
     * Create a presenter for the InputDataView.
     * 
     * @param pDisplay
     *            The display interface.
     * @param pEventBus
     *            The event bus.
     * @param pSheetXml
     *            The export PDF sheet command.
     * @param pSheetXls
     *            The export XLS sheet command.
     * @param pSheetsXml
     *            The export PDF sheets command.
     * @param pSheetsXls
     *            The export XLS sheets command.
     */
    @Inject
    public SelectExportedFieldsPresenter(
            final SelectExportedFieldsDisplay pDisplay,
            final EventBus pEventBus,
            final ExportSheetFieldsOnXmlCommand pSheetXml,
            final ExportSheetFieldsOnXlsCommand pSheetXls,
            final ExportSheetsFieldsOnXmlCommand pSheetsXml,
            final ExportSheetsFieldsOnXlsCommand pSheetsXls) {
        super(pDisplay, pEventBus);
        sheetXmlCommand = pSheetXml;
        sheetXlsCommand = pSheetXls;
        sheetsXmlCommand = pSheetsXml;
        sheetsXlsCommand = pSheetsXls;
    }

    /**
     * Set the field names.
     * 
     * @param pResult
     *            The result.
     * @param pMultivalued
     *            If several sheet has been selected from the listing panel.
     */
    public void init(final SelectFieldNamesResult pResult,
            final boolean pMultivalued) {
        // Set the exportable field names
        getDisplay().reset();
        for (UiExportableGroup lGroup : pResult.getExportableFields()) {
            getDisplay().addGroupField(lGroup.getName(),
                    lGroup.getExportableFields());
        }
        // Set the export button handler
        if (pMultivalued) {
            switch (pResult.getExportFormat()) {
                case XML:
                    getDisplay().setExportButtonHandler(sheetsXmlCommand);
                    break;
                case EXCEL:
                    getDisplay().setExportButtonHandler(sheetsXlsCommand);
                    break;
                default:
                    // Nothing to do
            }
        }
        else {
            switch (pResult.getExportFormat()) {
                case XML:
                    getDisplay().setExportButtonHandler(sheetXmlCommand);
                    break;
                case EXCEL:
                    getDisplay().setExportButtonHandler(sheetXlsCommand);
                    break;
                default:
                    // Nothing to do
            }
        }
    }

    /**
     * Get the selected field names.
     * 
     * @return The selected field names.
     */
    public List<String> getSelectedFieldNames() {
        return getDisplay().getSelectedFieldNames();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.PopupPresenter#getClosePopupEvent()
     */
    @Override
    protected GlobalEvent<ClosePopupAction> getClosePopupEvent() {
        return GlobalEvent.CLOSE_EXPORTED_FIELDS_SELECTION_POPUP;
    }
}