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
import org.topcased.gpm.ui.application.client.admin.dictionary.detail.DictionaryDetailPresenter;
import org.topcased.gpm.ui.application.client.command.AbstractCommand;
import org.topcased.gpm.ui.application.client.command.validation.UpdateDictionaryCategoryViewValidator;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.shared.command.dictionary.UpdateDictionaryCategoryValuesAction;

import com.google.gwt.user.client.Command;
import com.google.inject.Inject;

/**
 * A command to update a dictionary category.
 * 
 * @author nveillet
 * @author phtsaan
 */
public class UpdateDictionaryCategoryCommand extends AbstractCommand implements
        Command {

    /**
     * Create an UpdateDictionaryCategoryCommand.
     * 
     * @param pEventBus
     *            The bus event.
     */
    @Inject
    public UpdateDictionaryCategoryCommand(EventBus pEventBus) {
        super(pEventBus);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.Command#execute()
     */
    @Override
    public void execute() {
        DictionaryDetailPresenter lDictionaryDetail =
                Application.INJECTOR.getAdminPresenter().getDicoAdmin().getDictionaryDetail();

        if (lDictionaryDetail.hasChangedValues()) {
            fireEvent(GlobalEvent.SAVE_DICTIONARY_CATEGORY.getType(),
                    new UpdateDictionaryCategoryValuesAction(
                            lDictionaryDetail.getCategoryName(),
                            lDictionaryDetail.getCategoryValues()),
                    UpdateDictionaryCategoryViewValidator.getInstance());
        }
    }
}
