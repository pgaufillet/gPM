/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.config;

import net.customware.gwt.dispatch.client.DispatchAsync;
import net.customware.gwt.dispatch.client.service.DispatchService;
import net.customware.gwt.dispatch.client.service.DispatchServiceAsync;
import net.customware.gwt.dispatch.server.Dispatch;
import net.customware.gwt.dispatch.shared.Action;
import net.customware.gwt.dispatch.shared.Result;

import org.topcased.gpm.ui.application.client.Application;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Implementation of {@link DispatchAsync}, which provide client-side access to
 * the {@link Dispatch} class on the server-side.
 * <p>
 * Manipulates the loading panel that is displayed at execution call and hidden
 * at the callback.
 * </p>
 * 
 * @author mkargbo
 */
public class GpmDispatchAsync implements DispatchAsync {

    private static final DispatchServiceAsync REAL_SERVICE =
            GWT.create(DispatchService.class);

    /**
     * Default constructor.
     */
    public GpmDispatchAsync() {
    }

    /**
     * Execute the action.
     * 
     * @param <A>
     *            Action class to execute
     * @param <R>
     *            Associated result class
     * @param pAction
     *            Action to execute
     * @param pCallback
     *            Callback implementation
     * @param pWait
     *            Display the loading panel if true, nothing is displayed
     *            otherwise.
     */
    public <A extends Action<R>, R extends Result> void execute(
            final A pAction, final AsyncCallback<R> pCallback, boolean pWait) {
        if (pWait) {
            Application.INJECTOR.getMainPresenter().setLoadingPanelVisibility(
                    true);
        }
        REAL_SERVICE.execute(pAction, new AsyncCallback<Result>() {
            public void onFailure(Throwable pCaught) {
                Application.INJECTOR.getMainPresenter().setLoadingPanelVisibility(
                        false);
                pCallback.onFailure(pCaught);
            }

            @SuppressWarnings("unchecked")
            public void onSuccess(Result pResult) {
                Application.INJECTOR.getMainPresenter().setLoadingPanelVisibility(
                        false);
                pCallback.onSuccess((R) pResult);
            }
        });
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.client.DispatchAsync#execute(A,
     *      com.google.gwt.user.client.rpc.AsyncCallback)
     */
    public <A extends Action<R>, R extends Result> void execute(
            final A pAction, final AsyncCallback<R> pCallback) {
        execute(pAction, pCallback, true);
    }
}
