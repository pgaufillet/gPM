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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.topcased.gpm.business.util.action.ActionName;
import org.topcased.gpm.ui.application.client.command.builder.InitializeSheetCommandBuilder;
import org.topcased.gpm.ui.application.client.command.user.sheet.CreateSheetCommand;
import org.topcased.gpm.ui.application.client.command.user.workspace.MaximizeSheetDetailCommand;
import org.topcased.gpm.ui.application.client.command.user.workspace.MinimizeSheetDetailCommand;
import org.topcased.gpm.ui.application.shared.util.ExtendedActionType;
import org.topcased.gpm.ui.component.client.resources.ComponentResources;
import org.topcased.gpm.ui.facade.shared.action.UiAction;

import com.google.inject.Inject;

/**
 * A builder for the menu of the sheet creation detail panel.
 * 
 * @author tpanuel
 */
public class SheetDetailCreationMenuBuilder extends
        AbstractSheetDetailMenuBuilder {
    static {
        IMAGES.put(ActionName.SHEET_SAVE,
                ComponentResources.INSTANCE.images().sheetSave());
        IMAGES.put(ActionName.SHEET_INITIALIZE,
                ComponentResources.INSTANCE.images().sheetImport());
    }

    /**
     * Create a builder for the menu of the sheet creation detail panel.
     * 
     * @param pCreateCommand
     *            The creation command.
     * @param pInitializeCommandBuilder
     *            The initialization command builder.
     * @param pMinimizePanel
     *            The minimize panel command.
     * @param pMaximizePanel
     *            The maximize panel command.
     */
    @Inject
    public SheetDetailCreationMenuBuilder(
            final CreateSheetCommand pCreateCommand,
            final InitializeSheetCommandBuilder pInitializeCommandBuilder,
            final MinimizeSheetDetailCommand pMinimizePanel,
            final MaximizeSheetDetailCommand pMaximizePanel) {
        super(pMinimizePanel, pMaximizePanel);
        registerStandardCommand(ActionName.SHEET_SAVE, pCreateCommand);
        registerDynamicCommandBuilder(ActionName.SHEET_INITIALIZE,
                pInitializeCommandBuilder);
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

            // Create the first tool bar
            lToolBarA.add(pActions.get(ActionName.SHEET_SAVE));

            // Create the second tool bar
            lToolBarB.add(pActions.get(ActionName.SHEET_INITIALIZE));

            addToolBar(lToolBarA);
            addToolBar(lToolBarB);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.menu.AbstractMenuBuilder#getExtendedActionType()
     */
    @Override
    protected ExtendedActionType getExtendedActionType() {
        return ExtendedActionType.SHEET_CREATION;
    }
}