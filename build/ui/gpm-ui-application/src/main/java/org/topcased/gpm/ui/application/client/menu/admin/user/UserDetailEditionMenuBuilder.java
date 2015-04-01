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
import org.topcased.gpm.ui.application.client.command.admin.user.DeleteUserCommand;
import org.topcased.gpm.ui.application.client.command.admin.user.GetUserAffectationCommand;
import org.topcased.gpm.ui.application.client.command.admin.user.UpdateUserCommand;
import org.topcased.gpm.ui.application.client.menu.AbstractMenuBuilder;
import org.topcased.gpm.ui.application.shared.util.ExtendedActionType;
import org.topcased.gpm.ui.component.client.menu.GpmToolBar;
import org.topcased.gpm.ui.component.client.resources.ComponentResources;
import org.topcased.gpm.ui.facade.shared.action.UiAction;

import com.google.inject.Inject;

/**
 * A builder for the menu of the user creation detail panel.
 * 
 * @author nveillet
 */
public class UserDetailEditionMenuBuilder extends AbstractMenuBuilder {

    static {
        IMAGES.put(ActionName.USER_SAVE,
                ComponentResources.INSTANCE.images().sheetSave());
        IMAGES.put(ActionName.USER_AFFECTATION,
                ComponentResources.INSTANCE.images().sheetState());
        IMAGES.put(ActionName.USER_DELETE,
                ComponentResources.INSTANCE.images().sheetDelete());
    }

    /**
     * Create a builder for the menu of the product creation detail panel.
     * 
     * @param pUpdateCommand
     *            The update command.
     * @param pGetAffectionCommand
     *            The get affectation command.
     * @param pDeleteCommand
     *            The delete command.
     */
    @Inject
    public UserDetailEditionMenuBuilder(final UpdateUserCommand pUpdateCommand,
            final GetUserAffectationCommand pGetAffectionCommand,
            final DeleteUserCommand pDeleteCommand) {
        super();
        registerStandardCommand(ActionName.USER_SAVE, pUpdateCommand);
        registerStandardCommand(ActionName.USER_AFFECTATION,
                pGetAffectionCommand);
        registerStandardCommand(ActionName.USER_DELETE, pDeleteCommand);
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
            final List<UiAction> lToolBarB = new ArrayList<UiAction>();
            final List<UiAction> lToolBarC = new ArrayList<UiAction>();

            // Create the first tool bar
            lToolBarA.add(pActions.get(ActionName.USER_SAVE));

            // Create the second tool bar
            lToolBarB.add(pActions.get(ActionName.USER_AFFECTATION));

            // Create the third tool bar
            lToolBarC.add(pActions.get(ActionName.USER_DELETE));

            addToolBar(lToolBarA);
            addToolBar(lToolBarB);
            addToolBar(lToolBarC);
        }

        return toolBar;
    }
}