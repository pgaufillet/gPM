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

import java.util.List;

import net.customware.gwt.dispatch.client.DispatchAsync;
import net.customware.gwt.dispatch.shared.Result;
import net.customware.gwt.presenter.client.DefaultEventBus;

import org.topcased.gpm.ui.application.client.Application;
import org.topcased.gpm.ui.application.client.command.validation.ViewValidator;
import org.topcased.gpm.ui.component.client.resources.i18n.Ui18n;
import org.topcased.gpm.ui.facade.shared.exception.UiBusinessException;
import org.topcased.gpm.ui.facade.shared.exception.UiException;
import org.topcased.gpm.ui.facade.shared.exception.UiExtensionException;
import org.topcased.gpm.ui.facade.shared.exception.UiSessionException;
import org.topcased.gpm.ui.facade.shared.exception.UiUnexpectedException;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.inject.Inject;

/**
 * An event bus for actions.
 * 
 * @author tpanuel
 */
public class ActionEventBus extends DefaultEventBus {
    private final DispatchAsync dispatchAsync;

    /**
     * Create an event bus for actions.
     * 
     * @param pDispatchAsync
     *            the asynchronous dispatcher
     */
    @Inject
    public ActionEventBus(final DispatchAsync pDispatchAsync) {
        super();
        dispatchAsync = pDispatchAsync;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.event.shared.HandlerManager#fireEvent(com.google.gwt.event.shared.GwtEvent)
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void fireEvent(final GwtEvent<?> pEvent) {
        if (pEvent instanceof ActionEvent) {
            final ActionEvent lActionEvent = (ActionEvent) pEvent;
            boolean lValid = true;

            // Validate the view before launching the action
            for (ViewValidator lValidator : (List<ViewValidator>) lActionEvent.getViewValidators()) {
                String lError = null;
                if (lValidator != null) {
                    lError = lValidator.validate(lActionEvent.getAction());
                }

                if (lError != null && !lError.isEmpty()) {
                    if (lValidator.isError()) {
                        Application.INJECTOR.getErrorMessagePresenter().displayMessage(
                                lError);
                    }
                    else {
                        Application.INJECTOR.getInfoMessagePresenter().displayMessage(
                                lError);
                    }
                    lValid = false;
                }
            }

            if (lValid) {
                // Display the confirmation message, if exists
                if (lActionEvent.getConfirmationMessage() == null) {
                    // Execute the action
                    executeAction(pEvent, lActionEvent);
                }
                else {
                    // Execute the action, only if yes has been selected
                    Application.INJECTOR.getConfirmationMessagePresenter().displayQuestion(
                            lActionEvent.getConfirmationMessage(),
                            new ClickHandler() {
                                @Override
                                public void onClick(final ClickEvent pClickEvent) {
                                    // Execute the action
                                    executeAction(pEvent, lActionEvent);
                                }
                            });
                }
            }
        }
        // Standard action
        else {
            super.fireEvent(pEvent);
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void executeAction(final GwtEvent<?> pEvent,
            final ActionEvent pActionEvent) {
        if (pActionEvent.getAction() instanceof EmptyAction) {
            // The result is the action - no server call
            publishEvent(pActionEvent, (EmptyAction) pActionEvent.getAction());
        }
        else {
            // Execute a server call
            dispatchAsync.execute(((ActionEvent) pEvent).getAction(),
                    new AsyncCallback<Result>() {
                        @Override
                        public void onSuccess(final Result pResult) {
                            publishEvent(pActionEvent, pResult);
                        }

                        @Override
                        public void onFailure(final Throwable pCaught) {
                            if (pCaught instanceof UiSessionException) {
                                Application.INJECTOR.getErrorSessionMessagePresenter().bind();
                            }
                            else if (pCaught instanceof UiBusinessException) {
                                Application.INJECTOR.getErrorMessagePresenter().displayMessage(
                                        ((UiBusinessException) pCaught).getMessage());
                            }
                            else if (pCaught instanceof UiExtensionException) {
                                Application.INJECTOR.getErrorMessagePresenter().displayMessage(
                                        ((UiExtensionException) pCaught).getMessage());
                            }
                            else if (pCaught instanceof UiException) {
                                Application.INJECTOR.getErrorMessagePresenter().displayMessage(
                                        ((UiException) pCaught).getMessage());
                            }
                            else if (pCaught instanceof UiUnexpectedException) {
                                final UiUnexpectedException lException =
                                        (UiUnexpectedException) pCaught;
                                Application.INJECTOR.getErrorMessagePresenter().displayException(
                                        lException.getMessage(),
                                        lException.getDisplayableStackTrace());
                            }
                            else if (pCaught instanceof InvocationException) {
                                Application.INJECTOR.getErrorMessagePresenter().displayMessage(
                                        Ui18n.CONSTANTS.errorInvocationException());
                            }
                            else {
                                final UiUnexpectedException lException =
                                        new UiUnexpectedException(pCaught);
                                Application.INJECTOR.getErrorMessagePresenter().displayException(
                                        lException.getMessage(),
                                        lException.getDisplayableStackTrace());
                            }
                        }
                    });
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void publishEvent(final ActionEvent pEvent, final Result pResult) {
        pEvent.setResult(pResult);
        // Use this to call on async callback
        super.fireEvent(pEvent);
        // Fire the next event, if exists
        if (pEvent.getNextEvent() != null) {
            fireEvent(pEvent.getNextEvent());
        }
    }
}