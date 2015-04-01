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
import org.topcased.gpm.ui.application.client.admin.product.navigation.ProductNavigationView;
import org.topcased.gpm.ui.application.client.command.AbstractCommand;
import org.topcased.gpm.ui.application.client.event.EmptyAction;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.shared.command.filter.ExecuteTreeFilterAction;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Command;
import com.google.inject.Inject;

/**
 * A command to execute a product tree filter.
 * 
 * @author tpanuel
 */
public class ExecuteProductTreeFilterCommand extends AbstractCommand implements
        ValueChangeHandler<String>, Command {
    /**
     * Create a ExecuteTreeFilterCommand.
     * 
     * @param pEventBus
     *            The bus event.
     */
    @Inject
    public ExecuteProductTreeFilterCommand(final EventBus pEventBus) {
        super(pEventBus);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.event.logical.shared.ValueChangeHandler#onValueChange(com.google.gwt.event.logical.shared.ValueChangeEvent)
     */
    @Override
    public void onValueChange(final ValueChangeEvent<String> pEvent) {
        execute(pEvent.getValue());
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.Command#execute()
     */
    @Override
    public void execute() {
        execute(getCurrentProductTreeFilterId());
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void execute(final String pFilterId) {
        if (pFilterId.equals(ProductNavigationView.EMPTY_ENTRY)) {
            fireEvent(GlobalEvent.REMOVE_PRODUCT_TREE_FILTER.getType(),
                    new EmptyAction());
        }
        else {
            fireEvent(GlobalEvent.EXECUTE_PRODUCT_TREE_FILTER.getType(),
                    new ExecuteTreeFilterAction(null, FilterType.PRODUCT,
                            pFilterId));
        }
    }
}