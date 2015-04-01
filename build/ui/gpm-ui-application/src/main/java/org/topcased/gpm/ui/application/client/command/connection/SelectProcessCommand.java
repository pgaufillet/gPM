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

import java.util.HashMap;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.ui.application.client.command.AbstractCommand;
import org.topcased.gpm.ui.application.client.command.popup.ClosePopupAction;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.shared.command.authorization.LoginAction;
import org.topcased.gpm.ui.application.shared.util.PreSelectionEnum;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.inject.Inject;

/**
 * SelectProcessCommand
 * 
 * @author nveillet
 */
public class SelectProcessCommand extends AbstractCommand implements
        ClickHandler {
    /**
     * Create an SelectProcessCommand.
     * 
     * @param pEventBus
     *            The bus event.
     */
    @Inject
    public SelectProcessCommand(final EventBus pEventBus) {
        super(pEventBus);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.event.dom.client.ClickHandler#onClick(com.google.gwt.event.dom.client.ClickEvent)
     */
    @Override
    public void onClick(final ClickEvent pEvent) {
        LoginAction lLoginAction = new LoginAction(GWT.getHostPageBaseURL());

        // Set process name
        final HashMap<String, String> lParameters =
                new HashMap<String, String>();
        getPopupManager().getProcessSelectionPresenter().getDisplay().getSelectedProcess();

        lParameters.put(
                PreSelectionEnum.PROCESS.name(),
                getPopupManager().getProcessSelectionPresenter().getDisplay().getSelectedProcess());
        lLoginAction.setParameters(lParameters);

        // Set current locale
        lLoginAction.setLocaleName(LocaleInfo.getCurrentLocale().getLocaleName());
        lLoginAction.setAvailableLocale(LocaleInfo.getAvailableLocaleNames());

        fireEvent(GlobalEvent.CONNECTION.getType(), lLoginAction,
                GlobalEvent.CLOSE_SELECT_PROCESS_POPUP.getType(),
                new ClosePopupAction());
    }
}
