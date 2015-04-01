/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.command.popup.filter;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.business.util.FilterType;
import org.topcased.gpm.ui.application.client.command.AbstractCommand;
import org.topcased.gpm.ui.application.client.command.popup.ClosePopupAction;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.shared.command.filter.edit.SelectResultFieldAction;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.inject.Inject;

/**
 * A command to open product link deletion filter on the edition popup.
 * 
 * @author nveillet
 */
public class EditDeleteProductLinkFilterPopupCommand extends AbstractCommand
        implements ClickHandler {

    /**
     * Create an EditDeleteProductLinkFilterPopupCommand.
     * 
     * @param pEventBus
     *            The bus event.
     */
    @Inject
    public EditDeleteProductLinkFilterPopupCommand(final EventBus pEventBus) {
        super(pEventBus);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.event.dom.client.ClickHandler#onClick(com.google.gwt.event.dom.client.ClickEvent)
     */
    @Override
    public void onClick(final ClickEvent pEvent) {
        fireEvent(
                GlobalEvent.CLOSE_FILTER_POPUP.getType(),
                new ClosePopupAction(),
                GlobalEvent.EDIT_PRODUCT_LINK_CREATION_FILTER.getType(),
                new SelectResultFieldAction(
                        null,
                        FilterType.PRODUCT,
                        getPopupManager().getFilterPopupPresenter().getFilterId(),
                        true));
    }
}
