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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.topcased.gpm.business.util.action.ActionName;
import org.topcased.gpm.ui.application.client.command.user.sheet.DeleteSheetCommand;
import org.topcased.gpm.ui.application.client.command.user.sheet.DuplicateSheetCommand;
import org.topcased.gpm.ui.application.client.command.user.sheet.ExportSheetOnPdfCommand;
import org.topcased.gpm.ui.application.client.command.user.sheet.ExportSheetOnXlsCommand;
import org.topcased.gpm.ui.application.client.command.user.sheet.ExportSheetOnXmlCommand;
import org.topcased.gpm.ui.application.client.command.user.sheet.MailSheetCommand;
import org.topcased.gpm.ui.application.client.command.user.sheet.OpenSheetOnEditionCommand;
import org.topcased.gpm.ui.application.client.command.user.sheet.OpenSheetOnVisualizationCommand;
import org.topcased.gpm.ui.application.client.command.user.workspace.MaximizeSheetDetailCommand;
import org.topcased.gpm.ui.application.client.command.user.workspace.MinimizeSheetDetailCommand;
import org.topcased.gpm.ui.application.shared.util.ExtendedActionType;
import org.topcased.gpm.ui.facade.shared.action.UiAction;

import com.google.inject.Inject;

/**
 * A builder for the menu of the sheet visualization detail panel.
 * 
 * @author tpanuel
 */
public class SheetDetailVisualizationMenuBuilder extends
        AbstractSheetDetailMenuBuilder {
    static {
        IMAGES.put(ActionName.SHEET_REFRESH, INSTANCE.images().refresh());
        IMAGES.put(ActionName.SHEET_EDIT, INSTANCE.images().sheetEdit());
        IMAGES.put(ActionName.SHEET_DELETE, INSTANCE.images().sheetDelete());
        IMAGES.put(ActionName.SHEET_DUPLICATE,
                INSTANCE.images().sheetDuplicate());
        IMAGES.put(ActionName.SHEET_EXPORT, INSTANCE.images().sheetExport());
        IMAGES.put(ActionName.SHEET_MAIL, INSTANCE.images().email());
        IMAGES.put(ActionName.SHEET_LOCKED, INSTANCE.images().sheetLock());
    }

    /**
     * Create a builder for the menu of the sheet visualization detail panel.
     * 
     * @param pVisualizationCommand
     *            The visualization command.
     * @param pEdititionCommand
     *            The edition command.
     * @param pDeleteCommand
     *            The delete command.
     * @param pDuplicateCommand
     *            The duplicate command.
     * @param pExportPdf
     *            The export PDF command.
     * @param pExportXml
     *            The export XML command.
     * @param pExportXls
     *            The export XLS command.
     * @param pMailCommand
     *            The mail command.
     * @param pMinimizePanel
     *            The minimize panel command.
     * @param pMaximizePanel
     *            The maximize panel command.
     */
    @Inject
    public SheetDetailVisualizationMenuBuilder(
            final OpenSheetOnVisualizationCommand pVisualizationCommand,
            final OpenSheetOnEditionCommand pEdititionCommand,
            final DeleteSheetCommand pDeleteCommand,
            final DuplicateSheetCommand pDuplicateCommand,
            final ExportSheetOnPdfCommand pExportPdf,
            final ExportSheetOnXmlCommand pExportXml,
            final ExportSheetOnXlsCommand pExportXls,
            final MailSheetCommand pMailCommand,
            final MinimizeSheetDetailCommand pMinimizePanel,
            final MaximizeSheetDetailCommand pMaximizePanel) {
        super(pMinimizePanel, pMaximizePanel);
        pVisualizationCommand.forceRefresh();
        pEdititionCommand.forceRefresh();
        registerStandardCommand(ActionName.SHEET_REFRESH, pVisualizationCommand);
        registerStandardCommand(ActionName.SHEET_EDIT, pEdititionCommand);
        registerStandardCommand(ActionName.SHEET_DELETE, pDeleteCommand);
        registerStandardCommand(ActionName.SHEET_DUPLICATE, pDuplicateCommand);
        registerStandardCommand(
                ActionName.SHEET_EXPORT + ActionName.EXPORT_PDF, pExportPdf);
        registerStandardCommand(
                ActionName.SHEET_EXPORT + ActionName.EXPORT_XML, pExportXml);
        registerStandardCommand(
                ActionName.SHEET_EXPORT + ActionName.EXPORT_XLS, pExportXls);
        registerStandardCommand(ActionName.SHEET_MAIL, pMailCommand);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.menu.user.AbstractSheetDetailMenuBuilder#doBuildMenu(java.util.Map)
     */
    @Override
    protected void doBuildMenu(final Map<String, UiAction> pActions) {
        // Add actions buttons
        if (pActions != null) {
            final List<UiAction> lToolBarA = new ArrayList<UiAction>();
            final List<UiAction> lToolBarB = new ArrayList<UiAction>();
            final List<UiAction> lToolBarC = new ArrayList<UiAction>();

            // Create the first tool bar
            lToolBarA.add(pActions.get(ActionName.SHEET_REFRESH));

            // Create the second tool bar
            lToolBarB.add(pActions.get(ActionName.SHEET_LOCKED));
            lToolBarB.add(pActions.get(ActionName.SHEET_EDIT));
            lToolBarB.add(pActions.get(ActionName.SHEET_DELETE));

            // Create the third tool bar
            lToolBarC.add(pActions.get(ActionName.SHEET_DUPLICATE));
            lToolBarC.add(pActions.get(ActionName.SHEET_EXPORT));
            lToolBarC.add(pActions.get(ActionName.SHEET_MAIL));

            addToolBar(lToolBarA);
            addToolBar(lToolBarB);
            addToolBar(lToolBarC);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.menu.AbstractMenuBuilder#getExtendedActionType()
     */
    @Override
    protected ExtendedActionType getExtendedActionType() {
        return ExtendedActionType.SHEET_VISUALIZATION;
    }
}