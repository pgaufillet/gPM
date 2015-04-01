/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.event;

import java.util.ArrayList;
import java.util.List;

import net.customware.gwt.dispatch.shared.Action;
import net.customware.gwt.dispatch.shared.Result;

import org.topcased.gpm.ui.application.client.command.validation.ViewValidator;

import com.google.gwt.event.shared.GwtEvent;

/**
 * An event handler that can launch an async command.
 * 
 * @author tpanuel
 * @param <A>
 *            The action type.
 * @param <R>
 *            The result type.
 */
public class ActionEvent<A extends Action<R>, R extends Result> extends
        GwtEvent<ActionEventHandler<R>> {
    private final Type<ActionEventHandler<R>> type;

    private final A action;

    private final List<ViewValidator> viewValidators;

    private ActionEvent<?, ?> nextEvent;

    private String confirmationMessage;

    private R result;

    /**
     * Create an AbstractUiEvent.
     * 
     * @param pType
     *            The type of event.
     * @param pAction
     *            The action.
     */
    public ActionEvent(final Type<ActionEventHandler<R>> pType, final A pAction) {
        type = pType;
        action = pAction;
        viewValidators = new ArrayList<ViewValidator>();
    }

    /**
     * Create an AbstractUiEvent.
     * 
     * @param pType
     *            The type of event.
     * @param pAction
     *            The action.
     * @param pViewValidators
     *            The view valdiators.
     */
    public ActionEvent(final Type<ActionEventHandler<R>> pType,
            final A pAction, final List<ViewValidator> pViewValidators) {
        type = pType;
        action = pAction;
        viewValidators = pViewValidators;
    }

    /**
     * Get the action.
     * 
     * @return The action.
     */
    public A getAction() {
        return action;
    }

    /**
     * Get the result.
     * 
     * @return The result.
     */
    public R getResult() {
        return result;
    }

    /**
     * Set the result.
     * 
     * @param pResult
     *            The result.
     */
    public void setResult(final R pResult) {
        result = pResult;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.event.shared.GwtEvent#dispatch(com.google.gwt.event.shared.EventHandler)
     */
    @Override
    protected void dispatch(final ActionEventHandler<R> pHandler) {
        pHandler.execute(result);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.event.shared.GwtEvent#getAssociatedType()
     */
    @Override
    public Type<ActionEventHandler<R>> getAssociatedType() {
        return type;
    }

    /**
     * Get the view validators.
     * 
     * @return The view validators.
     */
    public List<ViewValidator> getViewValidators() {
        return viewValidators;
    }

    /**
     * Get the next event.
     * 
     * @return The next event.
     */
    public ActionEvent<?, ?> getNextEvent() {
        return nextEvent;
    }

    /**
     * Set the next event.
     * 
     * @param pNextEvent
     *            The next event.
     */
    public void setNextEvent(final ActionEvent<?, ?> pNextEvent) {
        nextEvent = pNextEvent;
    }

    /**
     * Get the confirmation message.
     * 
     * @return The confirmation message.
     */
    public String getConfirmationMessage() {
        return confirmationMessage;
    }

    /**
     * Set the confirmation message.
     * 
     * @param pConfirmationMessage
     *            The confirmation message.
     */
    public void setConfirmationMessage(String pConfirmationMessage) {
        confirmationMessage = pConfirmationMessage;
    }
}