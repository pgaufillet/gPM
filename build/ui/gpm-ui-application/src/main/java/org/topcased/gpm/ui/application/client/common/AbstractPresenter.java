/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.common;

import net.customware.gwt.dispatch.shared.Action;
import net.customware.gwt.dispatch.shared.Result;
import net.customware.gwt.presenter.client.EventBus;
import net.customware.gwt.presenter.client.place.Place;
import net.customware.gwt.presenter.client.place.PlaceRequest;
import net.customware.gwt.presenter.client.widget.WidgetDisplay;
import net.customware.gwt.presenter.client.widget.WidgetPresenter;

import org.topcased.gpm.ui.application.client.command.validation.ViewValidator;
import org.topcased.gpm.ui.application.client.event.ActionEvent;
import org.topcased.gpm.ui.application.client.event.ActionEventHandler;
import org.topcased.gpm.ui.application.client.util.CollectionUtil;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent.Type;

/**
 * An abstract presenter.
 * 
 * @author tpanuel
 * @param <D>
 *            The type of display.
 */
public abstract class AbstractPresenter<D extends WidgetDisplay> extends
        WidgetPresenter<D> {

    /**
     * Create an abstract presenter.
     * 
     * @param pDisplay
     *            The display interface.
     * @param pEventBus
     *            the event bus.
     */
    public AbstractPresenter(final D pDisplay, final EventBus pEventBus) {
        super(pDisplay, pEventBus);
    }

    /**
     * Fire an event.
     * 
     * @param <A>
     *            The type of action.
     * @param <R>
     *            The type of result.
     * @param pType
     *            The type of event.
     * @param pAction
     *            The action.
     */
    public <A extends Action<R>, R extends Result> void fireEvent(
            final Type<ActionEventHandler<R>> pType, final A pAction) {
        eventBus.fireEvent(new ActionEvent<A, R>(pType, pAction));
    }

    /**
     * Fire an event.
     * 
     * @param <A>
     *            The type of action.
     * @param <R>
     *            The type of result.
     * @param pType
     *            The type of event.
     * @param pAction
     *            The action.
     * @param pConfirmationMessage
     *            The confirmation message asked to the user before executing
     */
    public <A extends Action<R>, R extends Result> void fireEvent(
            final Type<ActionEventHandler<R>> pType, final A pAction,
            String pConfirmationMessage) {
        final ActionEvent<A, R> lAction = new ActionEvent<A, R>(pType, pAction);

        lAction.setConfirmationMessage(pConfirmationMessage);
        eventBus.fireEvent(lAction);
    }

    /**
     * Fire an event.
     * 
     * @param <A>
     *            The type of action.
     * @param <R>
     *            The type of result.
     * @param pType
     *            The type of event.
     * @param pAction
     *            The action.
     * @param pViewValidator
     *            The view validator.
     */
    public <A extends Action<R>, R extends Result> void fireEvent(
            final Type<ActionEventHandler<R>> pType, final A pAction,
            final ViewValidator pViewValidator) {
        eventBus.fireEvent(new ActionEvent<A, R>(pType, pAction,
                CollectionUtil.singleton(pViewValidator)));
    }

    /**
     * Add an event handler.
     * 
     * @param <H>
     *            The type of handler.
     * @param pType
     *            The type of event.
     * @param pHandler
     *            The handler.
     */
    public <H extends EventHandler> void addEventHandler(final Type<H> pType,
            final H pHandler) {
        registerHandler(eventBus.addHandler(pType, pHandler));
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.presenter.client.BasicPresenter#getPlace()
     */
    @Override
    public Place getPlace() {
        // No place management
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.presenter.client.BasicPresenter#onPlaceRequest(net.customware.gwt.presenter.client.place.PlaceRequest)
     */
    @Override
    protected void onPlaceRequest(final PlaceRequest pRequest) {
        // No place management
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.presenter.client.Presenter#revealDisplay()
     */
    @Override
    public void revealDisplay() {
        // No place management
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.presenter.client.Presenter#refreshDisplay()
     */
    @Override
    public void refreshDisplay() {
        // No action
    }
}