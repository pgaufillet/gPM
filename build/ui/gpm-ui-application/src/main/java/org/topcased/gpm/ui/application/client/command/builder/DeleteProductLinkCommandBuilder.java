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
import org.topcased.gpm.ui.application.client.command.admin.product.product.DeleteProductLinkCommand;
import org.topcased.gpm.ui.facade.shared.action.UiAction;

import com.google.gwt.user.client.Command;
import com.google.inject.Inject;

/**
 * A command builder for delete a product link.
 * 
 * @author tpanuel
 */
public class DeleteProductLinkCommandBuilder extends
        AbstractDynamicCommandBuilder {
    /**
     * Create a command builder for delete a product link.
     */
    @Inject
    public DeleteProductLinkCommandBuilder() {
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.command.builder.AbstractDynamicCommandBuilder#generateCommand(org.topcased.gpm.ui.facade.shared.action.UiAction)
     */
    @Override
    public Command generateCommand(final UiAction pAction) {
        final DeleteProductLinkCommand lCommand =
                Application.INJECTOR.getDeleteProductLinkCommand();

        lCommand.setLinkType(pAction.getName());

        return lCommand;
    }
}