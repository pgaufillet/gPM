/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.command.connection;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.ui.application.client.command.AbstractCommand;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.client.main.MainPresenter;
import org.topcased.gpm.ui.application.shared.command.authorization.LogoutAction;
import org.topcased.gpm.ui.component.client.resources.i18n.Ui18n;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;

/**
 * Logout to the application
 * 
 * @author nveillet
 */
public class LogoutCommand extends AbstractCommand implements ClickHandler,
        CloseHandler<Window> {
    /**
     * Create a LogoutCommand.
     * 
     * @param pEventBus
     *            The bus event.
     */
    @Inject
    public LogoutCommand(final EventBus pEventBus) {
        super(pEventBus);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.event.dom.client.ClickHandler#onClick(com.google.gwt.event.dom.client.ClickEvent)
     */
    @Override
    public void onClick(final ClickEvent pEvent) {
        if (isOneOfWorkspaceElementsModified()) {
            fireEvent(GlobalEvent.LOGOUT.getType(), new LogoutAction(),
                    Ui18n.MESSAGES.confirmationLogout());
        }
        else {
            if (verifyAndDecrementOpenedTabs()) {
                fireEvent(GlobalEvent.LOGOUT.getType(), new LogoutAction());
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.event.logical.shared.CloseHandler#onClose(com.google.gwt.event.logical.shared.CloseEvent)
     */
    @Override
    public void onClose(final CloseEvent<Window> pEvent) {
        if (verifyAndDecrementOpenedTabs()) {
            fireEvent(GlobalEvent.LOGOUT.getType(), new LogoutAction());
        }
    }

    /**
     * Decrement number of opened tabs and verify if all tabs have been closed.
     * 
     * @return <tt>true</tt> if all tabs have been closed, <tt>false</tt>
     *         otherwise.
     */
    private boolean verifyAndDecrementOpenedTabs() {
        // Decrement number of opened tabs
        String lNumberTabs =
                Cookies.getCookie(MainPresenter.NUMBER_TABS + "_"
                        + MainPresenter.getSessionId());
        if (lNumberTabs != null && !lNumberTabs.equals("")) {
            Integer lCount = Integer.parseInt(lNumberTabs);
            if (--lCount > 0) {
                Cookies.setCookie(MainPresenter.NUMBER_TABS + "_"
                        + MainPresenter.getSessionId(), lCount + "");
                return false;
            }
        }
        Cookies.removeCookie(MainPresenter.NUMBER_TABS + "_"
                + MainPresenter.getSessionId());
        return true;
    }

    /**
     * Verify if the tab opened is the last
     * 
     * @return <tt>true</tt> if the tab is the last, <tt>false</tt> otherwise.
     */
    public boolean islastOpenedTab() {
        String lNumberTab =
                Cookies.getCookie(MainPresenter.NUMBER_TABS + "_"
                        + MainPresenter.getSessionId());
        if (lNumberTab != null && !lNumberTab.equals("")) {
            Integer lCount = Integer.parseInt(lNumberTab);
            return (lCount < 1);
        }
        else {
            return true;
        }
    }
}
