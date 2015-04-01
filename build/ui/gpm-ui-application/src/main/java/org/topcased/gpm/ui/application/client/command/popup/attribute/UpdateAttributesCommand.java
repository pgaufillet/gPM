/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.command.popup.attribute;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.ui.application.client.command.AbstractCommand;
import org.topcased.gpm.ui.application.client.command.popup.ClosePopupAction;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.client.popup.attribute.AttributesEditionPresenter;
import org.topcased.gpm.ui.application.shared.command.attribute.UpdateAttributesAction;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.inject.Inject;

/**
 * A command to update the user attribute.
 * 
 * @author nveillet
 */
public class UpdateAttributesCommand extends AbstractCommand implements
        ClickHandler {

    /**
     * Create an UpdateAttributesCommand.
     * 
     * @param pEventBus
     *            The bus event.
     */
    @Inject
    public UpdateAttributesCommand(EventBus pEventBus) {
        super(pEventBus);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.event.dom.client.ClickHandler#onClick(com.google.gwt.event.dom.client.ClickEvent)
     */
    @Override
    public void onClick(ClickEvent pEvent) {
        final AttributesEditionPresenter lAttributesPresenter =
                getPopupManager().getAttributesPopup();

        fireEvent(GlobalEvent.SAVE_ATTRIBUTES.getType(),
                new UpdateAttributesAction(
                        lAttributesPresenter.getProductName(),
                        lAttributesPresenter.getAttributeContainer(),
                        lAttributesPresenter.getElementId(),
                        lAttributesPresenter.getAttributes()),
                GlobalEvent.CLOSE_ATTRIBUTES_POPUP.getType(),
                new ClosePopupAction());
    }
}
