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

import org.topcased.gpm.ui.facade.shared.action.UiAction;

import com.google.gwt.user.client.Command;

/**
 * A command builder for dynamic action.
 * 
 * @author tpanuel
 */
public abstract class AbstractDynamicCommandBuilder {
    /**
     * Generate a command from a dynamic action.
     * 
     * @param pAction
     *            The dynamic action.
     * @return The command.
     */
    abstract public Command generateCommand(final UiAction pAction);
}