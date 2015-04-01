/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.command.connection;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.ui.application.client.command.AbstractCommand;
import org.topcased.gpm.ui.application.client.command.popup.ClosePopupAction;
import org.topcased.gpm.ui.application.client.command.validation.IsProductWorkspaceOpenValidator;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.shared.command.authorization.ConnectProductAction;
import org.topcased.gpm.ui.component.client.button.GpmTextButton;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.inject.Inject;

/**
 * A command to select a product
 * 
 * @author tpanuel
 */
public class SelectProductCommand extends AbstractCommand implements
        ClickHandler {
    /**
     * Create an SelectProductCommand.
     * 
     * @param pEventBus
     *            The bus event.
     */
    @Inject
    public SelectProductCommand(final EventBus pEventBus) {
        super(pEventBus);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.event.dom.client.ClickHandler#onClick(com.google.gwt.event.dom.client.ClickEvent)
     */
    @Override
    public void onClick(final ClickEvent pEvent) {
        execute(((GpmTextButton) pEvent.getSource()).getId());
    }

    /**
     * Execute the command.
     * 
     * @param pProductName
     *            The product name.
     */
    public void execute(final String pProductName) {
        fireEvent(GlobalEvent.CONNECTION.getType(), new ConnectProductAction(
                pProductName), IsProductWorkspaceOpenValidator.getInstance(),
                GlobalEvent.CLOSE_SELECT_PRODUCT_POPUP.getType(),
                new ClosePopupAction());
    }
}