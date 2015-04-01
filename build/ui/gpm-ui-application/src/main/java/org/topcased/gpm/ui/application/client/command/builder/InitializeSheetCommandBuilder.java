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
import org.topcased.gpm.ui.application.client.command.user.sheet.InitializeSheetCommand;
import org.topcased.gpm.ui.facade.shared.action.UiAction;

import com.google.gwt.user.client.Command;
import com.google.inject.Inject;

/**
 * A command builder for initialize a sheet.
 * 
 * @author tpanuel
 */
public class InitializeSheetCommandBuilder extends
        AbstractDynamicCommandBuilder {
    /**
     * Create a command builder for initialize a sheet.
     */
    @Inject
    public InitializeSheetCommandBuilder() {
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.command.builder.AbstractDynamicCommandBuilder#generateCommand(org.topcased.gpm.ui.facade.shared.action.UiAction)
     */
    @Override
    public Command generateCommand(final UiAction pAction) {
        final InitializeSheetCommand lCommand =
                Application.INJECTOR.getInitializeSheetCommand();

        lCommand.setSheetType(pAction.getName());

        return lCommand;
    }
}