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
import org.topcased.gpm.ui.application.client.admin.dictionary.detail.EnvironmentCreationDetailPresenter;
import org.topcased.gpm.ui.application.client.command.AbstractCommand;
import org.topcased.gpm.ui.application.client.command.validation.EnvironmentCreationDetailViewValidator;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.shared.command.dictionary.CreateEnvironmentAction;

import com.google.gwt.user.client.Command;
import com.google.inject.Inject;

/**
 * A command to update a environment category.
 * 
 * @author nveillet
 */
public class CreateEnvironmentCommand extends AbstractCommand implements
        Command {

    /**
     * Create an UpdateEnvironmentCategoryCommand.
     * 
     * @param pEventBus
     *            The bus event.
     */
    @Inject
    public CreateEnvironmentCommand(EventBus pEventBus) {
        super(pEventBus);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.Command#execute()
     */
    @Override
    public void execute() {
        EnvironmentCreationDetailPresenter lEnvCreationDetail =
                Application.INJECTOR.getAdminPresenter().getDicoAdmin().getEnvironmentCreationDetail();

        fireEvent(GlobalEvent.CREATE_ENVIRONMENT.getType(),
                new CreateEnvironmentAction(
                        lEnvCreationDetail.getEnvironmentName(),
                        lEnvCreationDetail.getEnvironmentPublic()),
                EnvironmentCreationDetailViewValidator.getInstance());
    }
}
