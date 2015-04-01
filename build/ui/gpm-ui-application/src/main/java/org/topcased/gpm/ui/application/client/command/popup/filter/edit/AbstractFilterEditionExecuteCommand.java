/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.command.popup.filter.edit;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.ui.application.client.command.AbstractCommand;
import org.topcased.gpm.ui.application.client.popup.filter.edit.FilterEditionPopupPresenter;
import org.topcased.gpm.ui.facade.shared.filter.UiFilter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

/**
 * A command to get execute a filter in edition mode.
 * 
 * @author jlouisy
 */
public abstract class AbstractFilterEditionExecuteCommand extends
        AbstractCommand implements ClickHandler {

    /**
     * Create an AbstractFilterEditionExecuteCommand.
     * 
     * @param pEventBus
     *            The bus event.
     */
    public AbstractFilterEditionExecuteCommand(final EventBus pEventBus) {
        super(pEventBus);
    }

    /**
     * Filter execution.
     * 
     * @param pFilter
     *            the Filter to execute.
     */
    protected abstract void executeFilter(UiFilter pFilter);

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.event.dom.client.ClickHandler#onClick(com.google.gwt.event.dom.client.ClickEvent)
     */
    @Override
    public void onClick(final ClickEvent pEvent) {
        final FilterEditionPopupPresenter lFilterPopupPresenter =
                getPopupManager().getFilterEditionPopupPresenter();
        if (lFilterPopupPresenter.validateAllSteps()) {
            UiFilter lFilter = lFilterPopupPresenter.computeFilter();
            executeFilter(lFilter);
            lFilterPopupPresenter.unbind();
            //the reset has been done
            lFilterPopupPresenter.setResetNeeded(false);
        }
    }
}