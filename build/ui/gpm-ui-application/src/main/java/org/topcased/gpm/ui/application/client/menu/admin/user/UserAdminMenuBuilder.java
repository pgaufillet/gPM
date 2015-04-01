/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.menu.admin.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.topcased.gpm.business.util.action.ActionName;
import org.topcased.gpm.ui.application.client.command.admin.user.OpenUserOnCreationCommand;
import org.topcased.gpm.ui.application.client.menu.AbstractMenuBuilder;
import org.topcased.gpm.ui.application.shared.util.ExtendedActionType;
import org.topcased.gpm.ui.component.client.menu.GpmToolBar;
import org.topcased.gpm.ui.component.client.resources.ComponentResources;
import org.topcased.gpm.ui.facade.shared.action.UiAction;

import com.google.inject.Inject;

/**
 * A builder for the main menu of the user administration.
 * 
 * @author nveillet
 */
public class UserAdminMenuBuilder extends AbstractMenuBuilder {

    static {
        IMAGES.put(ActionName.USER_CREATION,
                ComponentResources.INSTANCE.images().sheetAdd());
    }

    /**
     * Create a builder for the menu of the product creation detail panel.
     * 
     * @param pCreateCommand
     *            The creation command.
     */
    @Inject
    public UserAdminMenuBuilder(final OpenUserOnCreationCommand pCreateCommand) {
        super();
        registerStandardCommand(ActionName.USER_CREATION, pCreateCommand);
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
            lToolBarA.add(pActions.get(ActionName.USER_CREATION));

            addToolBar(lToolBarA);
        }

        return toolBar;
    }
}