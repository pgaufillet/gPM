/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.command.popup.product;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.ui.application.client.command.AbstractCommand;
import org.topcased.gpm.ui.application.client.command.popup.ClosePopupAction;
import org.topcased.gpm.ui.application.client.command.validation.SelectEnvironmentsViewValidator;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.client.popup.product.SelectEnvironmentsPresenter;
import org.topcased.gpm.ui.application.shared.command.product.GetProductCreationAction;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.inject.Inject;

/**
 * A command to select the environment for product creation.
 * 
 * @author nveillet
 */
public class SelectEnvironmentsCommand extends AbstractCommand implements
        ClickHandler {

    /**
     * Create an SelectEnvironmentsCommand.
     * 
     * @param pEventBus
     *            The bus event.
     */
    @Inject
    public SelectEnvironmentsCommand(final EventBus pEventBus) {
        super(pEventBus);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.event.dom.client.ClickHandler#onClick(com.google.gwt.event.dom.client.ClickEvent)
     */
    @Override
    public void onClick(ClickEvent pEvent) {
        final SelectEnvironmentsPresenter lSelectEnvironmentsPresenter =
                getPopupManager().getSelectEnvironmentsPresenter();

        fireEvent(
                GlobalEvent.LOAD_PRODUCT.getType(),
                new GetProductCreationAction(
                        lSelectEnvironmentsPresenter.getProductTypeName(),
                        lSelectEnvironmentsPresenter.getDisplay().getSelectedEnvironments()),
                SelectEnvironmentsViewValidator.getInstance(),
                GlobalEvent.CLOSE_SELECT_ENV_POPUP.getType(),
                new ClosePopupAction());
    }
}
