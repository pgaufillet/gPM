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
import org.topcased.gpm.ui.application.client.admin.dictionary.DictionaryAdminPresenter;
import org.topcased.gpm.ui.application.client.command.AbstractCommand;
import org.topcased.gpm.ui.application.client.event.EmptyAction;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;

import com.google.gwt.user.client.Command;
import com.google.inject.Inject;

/**
 * A command to open a environment in creation mode.
 * 
 * @author nveillet
 */
public class OpenEnvironmentOnCreationCommand extends AbstractCommand implements
        Command {

    /**
     * Create an OpenEnvironmentOnCreationCommand.
     * 
     * @param pEventBus
     *            The bus event.
     */
    @Inject
    public OpenEnvironmentOnCreationCommand(EventBus pEventBus) {
        super(pEventBus);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.Command#execute()
     */
    @SuppressWarnings("rawtypes")
	@Override
    public void execute() {
        DictionaryAdminPresenter lDicoAdmin =
                Application.INJECTOR.getAdminPresenter().getDicoAdmin();

        String lMessage = null;
        if (lDicoAdmin.getDictionaryDetail().hasChangedValues()
                || lDicoAdmin.getEnvironmentDetail().hasChangedValues()) {
            lMessage = MESSAGES.confirmationCategoryChanged();
        }

        fireEvent(GlobalEvent.LOAD_NEW_ENVIRONMENT.getType(),
                new EmptyAction<EmptyAction>(), lMessage);
    }
}
