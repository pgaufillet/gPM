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

import org.topcased.gpm.ui.application.client.Application;
import org.topcased.gpm.ui.application.client.command.AbstractCommand;
import org.topcased.gpm.ui.application.client.popup.filter.edit.FilterEditionPopupPresenter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.inject.Inject;

/**
 * A command to get next step in filter edition.
 * 
 * @author jlouisy
 */
public class FilterEditionPopupNextCommand extends AbstractCommand implements
        ClickHandler {
    /**
     * Create an AddSheetLinkPopupCommand.
     * 
     * @param pEventBus
     *            The bus event.
     */
    @Inject
    public FilterEditionPopupNextCommand(final EventBus pEventBus) {
        super(pEventBus);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.event.dom.client.ClickHandler#onClick(com.google.gwt.event.dom.client.ClickEvent)
     */
    @Override
    public void onClick(final ClickEvent pEvent) {
        final FilterEditionPopupPresenter lFilterPopupPresenter =
                getPopupManager().getFilterEditionPopupPresenter();
        if (lFilterPopupPresenter.validateStep()) {
            lFilterPopupPresenter.setCurrentStep(lFilterPopupPresenter.getNextStep());
            lFilterPopupPresenter.fireAction();
        }
        else if (lFilterPopupPresenter.hasContainerListChanged()) {
            Application.INJECTOR.getConfirmationMessagePresenter().displayQuestion(
                    "Selected containers list has changed. Filter will be reset.",
                    new ClickHandler() {

                        @Override
                        public void onClick(ClickEvent pEvent) {
                            lFilterPopupPresenter.resetFilter();
                            lFilterPopupPresenter.setCurrentStep(lFilterPopupPresenter.getNextStep());
                            //Parameter is true in order to reset fields
                            lFilterPopupPresenter.setResetNeeded(true);
                            lFilterPopupPresenter.fireAction();
                        }
                    });
        }
    }
}