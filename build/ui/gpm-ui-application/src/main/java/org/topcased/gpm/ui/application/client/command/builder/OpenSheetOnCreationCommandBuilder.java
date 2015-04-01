/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.command.builder;

import org.topcased.gpm.ui.application.client.Application;
import org.topcased.gpm.ui.application.client.command.user.sheet.OpenSheetOnCreationCommand;
import org.topcased.gpm.ui.facade.shared.action.UiAction;

import com.google.gwt.user.client.Command;
import com.google.inject.Inject;

/**
 * A command builder for open sheet on creation mode.
 * 
 * @author tpanuel
 */
public class OpenSheetOnCreationCommandBuilder extends
        AbstractDynamicCommandBuilder {
    /**
     * Create a command builder for open sheet on creation mode.
     */
    @Inject
    public OpenSheetOnCreationCommandBuilder() {
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.command.builder.AbstractDynamicCommandBuilder#generateCommand(org.topcased.gpm.ui.facade.shared.action.UiAction)
     */
    @Override
    public Command generateCommand(final UiAction pAction) {
        final OpenSheetOnCreationCommand lCommand =
                Application.INJECTOR.getOpenSheetOnCreationCommand();

        lCommand.setSheetType(pAction.getName());

        return lCommand;
    }
}