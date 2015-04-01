/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.menu.user;

import static org.topcased.gpm.ui.component.client.resources.ComponentResources.INSTANCE;
import static org.topcased.gpm.ui.component.client.resources.i18n.Ui18n.CONSTANTS;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.topcased.gpm.business.util.action.ActionName;
import org.topcased.gpm.ui.application.client.command.user.filter.EditSheetTableFilterCommand;
import org.topcased.gpm.ui.application.client.command.user.filter.ExecuteSheetTableFilterCommand;
import org.topcased.gpm.ui.application.client.command.user.filter.ExportSheetTableFilterOnCsvCommand;
import org.topcased.gpm.ui.application.client.command.user.filter.ExportSheetTableFilterOnPdfCommand;
import org.topcased.gpm.ui.application.client.command.user.filter.ExportSheetTableFilterOnXlsCommand;
import org.topcased.gpm.ui.application.client.command.user.filter.ExportSheetTableFilterOnXmlCommand;
import org.topcased.gpm.ui.application.client.command.user.sheets.DeleteSheetsCommand;
import org.topcased.gpm.ui.application.client.command.user.sheets.ExportSheetsOnPdfCommand;
import org.topcased.gpm.ui.application.client.command.user.sheets.ExportSheetsOnXlsCommand;
import org.topcased.gpm.ui.application.client.command.user.sheets.ExportSheetsOnXmlCommand;
import org.topcased.gpm.ui.application.client.command.user.sheets.MailSheetsCommand;
import org.topcased.gpm.ui.application.client.command.user.workspace.MaximizeSheetListingCommand;
import org.topcased.gpm.ui.application.client.command.user.workspace.MinimizeSheetListingCommand;
import org.topcased.gpm.ui.application.client.menu.AbstractMenuBuilder;
import org.topcased.gpm.ui.application.shared.util.ExtendedActionType;
import org.topcased.gpm.ui.component.client.menu.GpmToolBar;
import org.topcased.gpm.ui.facade.shared.action.UiAction;

import com.google.inject.Inject;

/**
 * A builder for the menu of the sheet listing panel.
 * 
 * @author tpanuel
 */
public class SheetListingMenuBuilder extends AbstractMenuBuilder {
    static {
        IMAGES.put(ActionName.FILTER_SHEET_REFRESH, INSTANCE.images().refresh());
        IMAGES.put(ActionName.FILTER_SHEET_EDIT, INSTANCE.images().filterEdit());
        IMAGES.put(ActionName.FILTER_SHEET_EXPORT,
                INSTANCE.images().filterExport());
        IMAGES.put(ActionName.SHEETS_DELETE, INSTANCE.images().sheetDelete());
        IMAGES.put(ActionName.SHEETS_EXPORT, INSTANCE.images().sheetExport());
        IMAGES.put(ActionName.SHEETS_MAIL, INSTANCE.images().email());
    }

    private final MinimizeSheetListingCommand minimizePanel;

    private final MaximizeSheetListingCommand maximizePanel;

    /**
     * Create a builder for the menu of the sheet listing panel.
     * 
     * @param pRefreshFilter
     *            The refresh filter command.
     * @param pEditFilter
     *            The edit filter command.
     * @param pExportFilterPdf
     *            The export filter PDF command.
     * @param pExportFilterXml
     *            The export filter XML command.
     * @param pExportFilterXls
     *            The export filter XLS command.
     * @param pExportFilterCsv
     *            The export filter CSV command.
     * @param pDeleteSheets
     *            The delete sheets command.
     * @param pExportSheetsPdf
     *            The export sheets PDF command.
     * @param pExportSheetsXml
     *            The export sheets XML command.
     * @param pExportSheetsXls
     *            The export sheets XLS command.
     * @param pMailSheets
     *            The mail sheets export.
     * @param pMinimizePanel
     *            The minimize panel command.
     * @param pMaximizePanel
     *            The maximize panel command.
     */
    @Inject
    public SheetListingMenuBuilder(
            final ExecuteSheetTableFilterCommand pRefreshFilter,
            final EditSheetTableFilterCommand pEditFilter,
            final ExportSheetTableFilterOnPdfCommand pExportFilterPdf,
            final ExportSheetTableFilterOnXmlCommand pExportFilterXml,
            final ExportSheetTableFilterOnXlsCommand pExportFilterXls,
            final ExportSheetTableFilterOnCsvCommand pExportFilterCsv,
            final DeleteSheetsCommand pDeleteSheets,
            final ExportSheetsOnPdfCommand pExportSheetsPdf,
            final ExportSheetsOnXmlCommand pExportSheetsXml,
            final ExportSheetsOnXlsCommand pExportSheetsXls,
            final MailSheetsCommand pMailSheets,
            final MinimizeSheetListingCommand pMinimizePanel,
            final MaximizeSheetListingCommand pMaximizePanel) {
        super();
        minimizePanel = pMinimizePanel;
        maximizePanel = pMaximizePanel;
        registerStandardCommand(ActionName.FILTER_SHEET_REFRESH, pRefreshFilter);
        registerStandardCommand(ActionName.FILTER_SHEET_EDIT, pEditFilter);
        registerStandardCommand(ActionName.FILTER_SHEET_EXPORT
                + ActionName.EXPORT_PDF, pExportFilterPdf);
        registerStandardCommand(ActionName.FILTER_SHEET_EXPORT
                + ActionName.EXPORT_XML, pExportFilterXml);
        registerStandardCommand(ActionName.FILTER_SHEET_EXPORT
                + ActionName.EXPORT_XLS, pExportFilterXls);
        registerStandardCommand(ActionName.FILTER_SHEET_EXPORT
                + ActionName.EXPORT_CSV, pExportFilterCsv);
        registerStandardCommand(ActionName.SHEETS_DELETE, pDeleteSheets);
        registerStandardCommand(ActionName.SHEETS_EXPORT
                + ActionName.EXPORT_PDF, pExportSheetsPdf);
        registerStandardCommand(ActionName.SHEETS_EXPORT
                + ActionName.EXPORT_XML, pExportSheetsXml);
        registerStandardCommand(ActionName.SHEETS_EXPORT
                + ActionName.EXPORT_XLS, pExportSheetsXls);
        registerStandardCommand(ActionName.SHEETS_MAIL, pMailSheets);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.menu.AbstractMenuBuilder#buildMenu(java.util.Map,
     *      java.util.List)
     */
    @Override
    public GpmToolBar buildMenu(final Map<String, UiAction> pActions,
            final List<UiAction> pExtendedActions) {
        // Clean tool bar
        resetToolBar();

        // Replace Standard actions with Extended Actions when corresponding
        mergeExtendedActionsWithActions(pActions, pExtendedActions);

        // Add actions buttons
        if (pActions != null) {
            final List<UiAction> lToolBarA = new ArrayList<UiAction>();
            final List<UiAction> lToolBarB = new ArrayList<UiAction>();
            final List<UiAction> lToolBarC = new ArrayList<UiAction>();

            // Create the first tool bar
            lToolBarA.add(pActions.get(ActionName.FILTER_SHEET_REFRESH));

            // Create the second tool bar
            lToolBarB.add(pActions.get(ActionName.FILTER_SHEET_EDIT));
            lToolBarB.add(pActions.get(ActionName.FILTER_SHEET_EXPORT));

            // Create the third tool bar
            lToolBarC.add(pActions.get(ActionName.SHEETS_DELETE));
            lToolBarC.add(pActions.get(ActionName.SHEETS_EXPORT));
            lToolBarC.add(pActions.get(ActionName.SHEETS_MAIL));

            addToolBar(lToolBarA);
            addToolBar(lToolBarB);
            addToolBar(lToolBarC);
        }

        // Add extended actions
        if (pExtendedActions != null && pExtendedActions.size() > 0) {
            addToolBar(pExtendedActions);
        }

        // Add minimize-maximize actions
        toolBar.addSeparatedToolBar();
        toolBar.addEntry(INSTANCE.images().minimizeUp(), CONSTANTS.minimize(),
                minimizePanel);
        toolBar.addEntry(INSTANCE.images().maximize(), CONSTANTS.maximize(),
                maximizePanel);

        return toolBar;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.menu.AbstractMenuBuilder#getExtendedActionType()
     */
    @Override
    protected ExtendedActionType getExtendedActionType() {
        return ExtendedActionType.SHEETS;
    }
}