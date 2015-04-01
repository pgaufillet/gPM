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
import org.topcased.gpm.ui.application.client.command.AbstractCommand;

import com.google.gwt.user.client.Command;
import com.google.inject.Inject;

/**
 * A command to update a dictionary category.
 * 
 * @author nveillet
 */
public class SortDictionaryCategoryCommand extends AbstractCommand implements
        Command {

    /**
     * Create an UpdateDictionaryCategoryCommand.
     * 
     * @param pEventBus
     *            The bus event.
     */
    @Inject
    public SortDictionaryCategoryCommand(EventBus pEventBus) {
        super(pEventBus);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.Command#execute()
     */
    @Override
    public void execute() {
        Application.INJECTOR.getAdminPresenter().getDicoAdmin().getDictionaryDetail().sortValues();
    }
}
