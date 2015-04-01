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

import static org.topcased.gpm.ui.component.client.resources.i18n.Ui18n.MESSAGES;
import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.business.util.FilterType;
import org.topcased.gpm.ui.application.client.command.AbstractCommand;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.shared.command.filter.DeleteFilterAction;
import org.topcased.gpm.ui.component.client.button.GpmImageButton;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.inject.Inject;

/**
 * A command to delete a product filter.
 * 
 * @author tpanuel
 */
public class DeleteProductTableFilterCommand extends AbstractCommand implements
        ClickHandler {
    /**
     * Create a DeleteProductTableFilterCommand.
     * 
     * @param pEventBus
     *            The bus event.
     */
    @Inject
    public DeleteProductTableFilterCommand(final EventBus pEventBus) {
        super(pEventBus);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.event.dom.client.ClickHandler#onClick(com.google.gwt.event.dom.client.ClickEvent)
     */
    @Override
    public void onClick(final ClickEvent pEvent) {
        fireEvent(GlobalEvent.DELETE_PRODUCT_FILTER.getType(),
                new DeleteFilterAction(null, FilterType.PRODUCT,
                        ((GpmImageButton) pEvent.getSource()).getId()),
                MESSAGES.confirmationFilterDeletion());
    }
}