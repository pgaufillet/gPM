/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.command.admin.product.filter;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.business.util.FilterType;
import org.topcased.gpm.ui.application.client.command.AbstractCommand;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.shared.command.filter.ExecuteTableFilterAction;
import org.topcased.gpm.ui.component.client.button.GpmTextButton;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.inject.Inject;

/**
 * A command to execute a product table filter.
 * 
 * @author tpanuel
 */
public class ExecuteProductTableFilterCommand extends AbstractCommand implements
        Command, ClickHandler {
    /**
     * Create a ExecuteProductTableFilterCommand.
     * 
     * @param pEventBus
     *            The bus event.
     */
    @Inject
    public ExecuteProductTableFilterCommand(final EventBus pEventBus) {
        super(pEventBus);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.Command#execute()
     */
    @Override
    public void execute() {
        execute(getCurrentProductTableFilterId(), true);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.event.dom.client.ClickHandler#onClick(com.google.gwt.event.dom.client.ClickEvent)
     */
    @Override
    public void onClick(final ClickEvent pEvent) {
        execute(((GpmTextButton) pEvent.getSource()).getId(), false);
    }

    private void execute(final String pFilterId, final boolean pIsRefresh) {
        fireEvent(GlobalEvent.EXECUTE_PRODUCT_TABLE_FILTER.getType(),
                new ExecuteTableFilterAction(null, FilterType.PRODUCT,
                        pFilterId, pIsRefresh));
    }
}