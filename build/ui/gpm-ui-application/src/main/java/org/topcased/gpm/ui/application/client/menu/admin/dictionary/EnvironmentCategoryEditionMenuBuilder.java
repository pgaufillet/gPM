/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.menu.admin.dictionary;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.topcased.gpm.business.util.action.ActionName;
import org.topcased.gpm.ui.application.client.command.admin.dictionary.UpdateEnvironmentCategoryCommand;
import org.topcased.gpm.ui.application.client.menu.AbstractMenuBuilder;
import org.topcased.gpm.ui.application.shared.util.ExtendedActionType;
import org.topcased.gpm.ui.component.client.menu.GpmToolBar;
import org.topcased.gpm.ui.component.client.resources.ComponentResources;
import org.topcased.gpm.ui.facade.shared.action.UiAction;

import com.google.inject.Inject;

/**
 * A builder for the main menu of the environment administration.
 * 
 * @author nveillet
 */
public class EnvironmentCategoryEditionMenuBuilder extends AbstractMenuBuilder {

    static {
        IMAGES.put(ActionName.ENVIRONMENT_CATEGORY_SAVE,
                ComponentResources.INSTANCE.images().sheetSave());
    }

    /**
     * Create a builder for the menu of the environment detail administration
     * panel.
     * 
     * @param pUpdateCommand
     *            The update command.
     */
    @Inject
    public EnvironmentCategoryEditionMenuBuilder(
            final UpdateEnvironmentCategoryCommand pUpdateCommand) {
        super();
        registerStandardCommand(ActionName.ENVIRONMENT_CATEGORY_SAVE,
                pUpdateCommand);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.menu.AbstractMenuBuilder#getExtendedActionType()
     */
    @Override
    protected ExtendedActionType getExtendedActionType() {
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.menu.AbstractMenuBuilder#buildMenu(java.util.Map,
     *      java.util.List)
     */
    @Override
    public GpmToolBar buildMenu(Map<String, UiAction> pActions,
            List<UiAction> pExtendedActions) {
        // Clean tool bar
        resetToolBar();

        // Add actions
        if (pActions != null) {
            final List<UiAction> lToolBarA = new ArrayList<UiAction>();

            // Create the first tool bar
            lToolBarA.add(pActions.get(ActionName.ENVIRONMENT_CATEGORY_SAVE));

            addToolBar(lToolBarA);
        }

        return toolBar;
    }
}
