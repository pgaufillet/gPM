/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.command.admin.dictionary;

import static org.topcased.gpm.ui.component.client.resources.i18n.Ui18n.MESSAGES;
import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.ui.application.client.Application;
import org.topcased.gpm.ui.application.client.admin.dictionary.listing.EnvironmentListingPresenter;
import org.topcased.gpm.ui.application.client.command.AbstractCommand;
import org.topcased.gpm.ui.application.client.event.EmptyAction;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.shared.command.dictionary.GetEnvironmentCategoryValuesAction;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.inject.Inject;

/**
 * A command to open a dictionary category.
 * 
 * @author nveillet
 */
public class OpenEnvironmentCategoryCommand extends AbstractCommand implements
        ChangeHandler {

    /**
     * Create an OpenEnvironmentCategoryCommand.
     * 
     * @param pEventBus
     *            The bus event.
     */
    @Inject
    public OpenEnvironmentCategoryCommand(EventBus pEventBus) {
        super(pEventBus);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.event.dom.client.ChangeHandler#onChange(com.google.gwt.event.dom.client.ChangeEvent)
     */
    @SuppressWarnings("rawtypes")
	@Override
    public void onChange(ChangeEvent pEvent) {
        EnvironmentListingPresenter lEnvironmentListing =
                Application.INJECTOR.getAdminPresenter().getDicoAdmin().getEnvironmentListing();

        String lCategory = lEnvironmentListing.getSelectedCategory();

        String lMessage = null;
        if (Application.INJECTOR.getAdminPresenter().getDicoAdmin().getEnvironmentDetail().hasChangedValues()) {
            lMessage = MESSAGES.confirmationCategoryChanged();
        }

        if (lCategory.isEmpty()) {
            fireEvent(GlobalEvent.HIDE_CATEGORY.getType(),
                    new EmptyAction<EmptyAction>(), lMessage);
        }
        else {
            fireEvent(
                    GlobalEvent.LOAD_ENVIRONMENT_CATEGORY.getType(),
                    new GetEnvironmentCategoryValuesAction(
                            lEnvironmentListing.getEnvironmentName(), lCategory),
                    lMessage);
        }
    }
}
