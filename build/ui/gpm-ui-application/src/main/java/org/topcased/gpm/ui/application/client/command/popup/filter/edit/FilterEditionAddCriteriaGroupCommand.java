/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien LOUISY (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.command.popup.filter.edit;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.ui.application.client.command.AbstractCommand;
import org.topcased.gpm.ui.application.client.popup.filter.edit.FilterEditionSelectCriteriaGroupPopupPresenter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.inject.Inject;

/**
 * A command to add a criteria group.
 * 
 * @author jlouisy
 */
public class FilterEditionAddCriteriaGroupCommand extends AbstractCommand
        implements ClickHandler {
    /**
     * Create an FilterEditionAddCriteriaGroupCommand.
     * 
     * @param pEventBus
     *            The bus event.
     */
    @Inject
    public FilterEditionAddCriteriaGroupCommand(final EventBus pEventBus) {
        super(pEventBus);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.event.dom.client.ClickHandler#onClick(com.google.gwt.event.dom.client.ClickEvent)
     */
    @Override
    public void onClick(final ClickEvent pEvent) {
        FilterEditionSelectCriteriaGroupPopupPresenter lGroupPopupPresenter =
                getPopupManager().getFilterEditionPopupPresenter().getFilterEditionSelectCriteriaGroupPopupPresenter();
        AddCriterionAction lAddCriterionAction =
                lGroupPopupPresenter.getAddCriterionAction();
        getPopupManager().getFilterEditionPopupPresenter().fireAddCriterionAction(
                lAddCriterionAction);
    }
}