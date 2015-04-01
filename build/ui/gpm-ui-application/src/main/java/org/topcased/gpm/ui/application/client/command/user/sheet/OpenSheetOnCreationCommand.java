/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.command.user.sheet;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.ui.application.client.command.AbstractCommand;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.shared.command.sheet.GetSheetCreationAction;

import com.google.gwt.user.client.Command;
import com.google.inject.Inject;

/**
 * A command to open a sheet on creation.
 * 
 * @author tpanuel
 */
public class OpenSheetOnCreationCommand extends AbstractCommand implements
        Command {
    private String sheetType;

    /**
     * Create an OpenSheetOnCreationCommand.
     * 
     * @param pEventBus
     *            The bus event.
     */
    @Inject
    public OpenSheetOnCreationCommand(final EventBus pEventBus) {
        super(pEventBus);
    }

    /**
     * Get the sheet type.
     * 
     * @return The sheet type.
     */
    public String getSheetType() {
        return sheetType;
    }

    /**
     * Set the sheet type.
     * 
     * @param pSheetType
     *            The sheet type.
     */
    public void setSheetType(final String pSheetType) {
        sheetType = pSheetType;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.Command#execute()
     */
    @Override
    public void execute() {
        fireEvent(GlobalEvent.LOAD_SHEET.getType(), new GetSheetCreationAction(
                getCurrentProductWorkspaceName(), sheetType));
    }
}