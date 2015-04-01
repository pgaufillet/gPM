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

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.ui.application.client.Application;
import org.topcased.gpm.ui.application.client.admin.dictionary.detail.EnvironmentDetailPresenter;
import org.topcased.gpm.ui.application.client.admin.dictionary.listing.EnvironmentListingPresenter;
import org.topcased.gpm.ui.application.client.command.AbstractCommand;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.shared.command.dictionary.UpdateEnvironmentCategoryValuesAction;

import com.google.gwt.user.client.Command;
import com.google.inject.Inject;

/**
 * A command to update a environment category.
 * 
 * @author nveillet
 */
public class UpdateEnvironmentCategoryCommand extends AbstractCommand implements
        Command {

    /**
     * Create an UpdateEnvironmentCategoryCommand.
     * 
     * @param pEventBus
     *            The bus event.
     */
    @Inject
    public UpdateEnvironmentCategoryCommand(EventBus pEventBus) {
        super(pEventBus);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.Command#execute()
     */
    @Override
    public void execute() {
        EnvironmentDetailPresenter lEnvironmentDetail =
                Application.INJECTOR.getAdminPresenter().getDicoAdmin().getEnvironmentDetail();

        if (lEnvironmentDetail.hasChangedValues()) {
            EnvironmentListingPresenter lEnvironmentListing =
                    Application.INJECTOR.getAdminPresenter().getDicoAdmin().getEnvironmentListing();

            fireEvent(GlobalEvent.SAVE_ENVIRONMENT_CATEGORY.getType(),
                    new UpdateEnvironmentCategoryValuesAction(
                            lEnvironmentListing.getEnvironmentName(),
                            lEnvironmentDetail.getCategoryName(),
                            lEnvironmentDetail.getCategoryValues()));
        }
    }
}
