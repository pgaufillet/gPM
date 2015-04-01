/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.command.connection;

import java.util.ArrayList;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.ui.application.client.Application;
import org.topcased.gpm.ui.application.client.command.AbstractCommand;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.client.user.ProductWorkspacePresenter;
import org.topcased.gpm.ui.application.shared.command.authorization.ChangeRoleAction;
import org.topcased.gpm.ui.component.client.resources.i18n.Ui18n;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.inject.Inject;

/**
 * Command to change the role.
 * 
 * @author tpanuel
 */
public class ChangeRoleCommand extends AbstractCommand implements
        ValueChangeHandler<String> {
    /**
     * Command constructor
     * 
     * @param pEventBus
     *            Event bus
     */
    @Inject
    public ChangeRoleCommand(EventBus pEventBus) {
        super(pEventBus);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.event.logical.shared.ValueChangeHandler#onValueChange(com.google.gwt.event.logical.shared.ValueChangeEvent)
     */
    @Override
    public void onValueChange(final ValueChangeEvent<String> pEvent) {
        ProductWorkspacePresenter lCurrentProductWorkspace =
                Application.INJECTOR.getUserSpacePresenter().getCurrentProductWorkspace();

        ArrayList<String> lOpenedSheetIds =
                new ArrayList<String>(
                        lCurrentProductWorkspace.getDetail().getPresenterIds());

        fireEvent(GlobalEvent.CONNECTION.getType(), new ChangeRoleAction(
                getCurrentProductWorkspaceName(),
                lCurrentProductWorkspace.getSelectedRole(), lOpenedSheetIds),
                Ui18n.MESSAGES.confirmationChangeRole());

        // Cancel the user selection, user role will be updated when new user Workspace loads
        lCurrentProductWorkspace.rollBackSelectedRole();
    }
}