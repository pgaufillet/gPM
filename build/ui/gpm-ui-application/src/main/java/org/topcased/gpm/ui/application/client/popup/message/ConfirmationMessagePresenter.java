/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.popup.message;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.ui.application.client.command.popup.ClosePopupAction;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.client.popup.PopupPresenter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.inject.Inject;

/**
 * The presenter for the ConfirmationMessageView.
 * 
 * @author tpanuel
 */
public class ConfirmationMessagePresenter extends
        PopupPresenter<ConfirmationMessageDisplay> {
    /**
     * Create a presenter for the ConfirmationMessageView.
     * 
     * @param pDisplay
     *            The display interface.
     * @param pEventBus
     *            The event bus.
     */
    @Inject
    public ConfirmationMessagePresenter(
            final ConfirmationMessageDisplay pDisplay, final EventBus pEventBus) {
        super(pDisplay, pEventBus);
    }

    /**
     * Display a question.
     * 
     * @param pQuestion
     *            The question.
     * @param pYesHandler
     *            The handler on the yes button.
     */
    public void displayQuestion(final String pQuestion,
            final ClickHandler pYesHandler) {
        getDisplay().setQuestionMessage(pQuestion);
        getDisplay().getYesButton().addClickHandler(pYesHandler);
        bind();
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.presenter.client.BasicPresenter#onBind()
     */
    @Override
    protected void onBind() {
        getDisplay().getYesButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent pEvent) {
                unbind();
            }
        });
        getDisplay().getNoButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent pEvent) {
                unbind();
            }
        });
        super.onBind();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.PopupPresenter#getClosePopupEvent()
     */
    @Override
    protected GlobalEvent<ClosePopupAction> getClosePopupEvent() {
        return null;
    }
}