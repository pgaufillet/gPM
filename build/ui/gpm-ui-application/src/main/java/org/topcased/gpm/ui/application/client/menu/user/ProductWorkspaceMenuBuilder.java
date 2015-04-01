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
import org.topcased.gpm.ui.application.client.command.builder.OpenSheetOnCreationCommandBuilder;
import org.topcased.gpm.ui.application.client.command.user.sheets.CloseSheetsCommand;
import org.topcased.gpm.ui.application.client.menu.AbstractMenuBuilder;
import org.topcased.gpm.ui.application.shared.util.ExtendedActionType;
import org.topcased.gpm.ui.component.client.menu.GpmToolBar;
import org.topcased.gpm.ui.component.client.resources.ComponentResources;
import org.topcased.gpm.ui.facade.shared.action.UiAction;

import com.google.inject.Inject;

/**
 * A builder for the menu of the product workspace.
 * 
 * @author tpanuel
 */
public class ProductWorkspaceMenuBuilder extends AbstractMenuBuilder {
    static {
        IMAGES.put(ActionName.SHEET_CREATION,
                ComponentResources.INSTANCE.images().sheetAdd());
        IMAGES.put(ActionName.SHEETS_CLOSE,
                ComponentResources.INSTANCE.images().sheetsClose());
    }

    /**
     * Create a builder for the menu of the product workspace.
     * 
     * @param pCreateCommandBuilder
     *            Command builder for creation command.
     */
    @Inject
    public ProductWorkspaceMenuBuilder(
            final OpenSheetOnCreationCommandBuilder pCreateCommandBuilder,
            final CloseSheetsCommand pCloseSheetsCommand) {
        super();
        registerDynamicCommandBuilder(ActionName.SHEET_CREATION,
                pCreateCommandBuilder);
        registerStandardCommand(ActionName.SHEETS_CLOSE, pCloseSheetsCommand);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.menu.AbstractMenuBuilder#buildMenu(java.util.Map,
     *      java.util.List)
     */
    @Override
    public final GpmToolBar buildMenu(final Map<String, UiAction> pActions,
            final List<UiAction> pExtendedActions) {
        // Clean tool bar
        resetToolBar();

        // Replace Standard actions with Extended Actions when corresponding
        mergeExtendedActionsWithActions(pActions, pExtendedActions);

        // Add actions
        if (pActions != null) {
            final List<UiAction> lToolBarA = new ArrayList<UiAction>();

            // Create the first tool bar
            lToolBarA.add(pActions.get(ActionName.SHEET_CREATION));

            addToolBar(lToolBarA);
        }

        // Add extended actions
        if (pExtendedActions != null && pExtendedActions.size() > 0) {
            addToolBar(pExtendedActions);
        }

        // Add close all sheets action
        if (pActions != null) {
            final List<UiAction> lToolBarA = new ArrayList<UiAction>();

            // Create the first tool bar
            lToolBarA.add(pActions.get(ActionName.SHEETS_CLOSE));

            addToolBar(lToolBarA);
        }

        return toolBar;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.menu.AbstractMenuBuilder#getExtendedActionType()
     */
    @Override
    protected ExtendedActionType getExtendedActionType() {
        return ExtendedActionType.GLOBAL;
    }
}