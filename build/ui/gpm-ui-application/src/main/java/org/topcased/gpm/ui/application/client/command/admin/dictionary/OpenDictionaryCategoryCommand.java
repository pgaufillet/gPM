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
import org.topcased.gpm.ui.application.client.command.AbstractCommand;
import org.topcased.gpm.ui.application.client.event.EmptyAction;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.shared.command.dictionary.GetDictionaryCategoryValuesAction;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.inject.Inject;

/**
 * A command to open a dictionary category.
 * 
 * @author nveillet
 */
public class OpenDictionaryCategoryCommand extends AbstractCommand implements
        ChangeHandler {

    /**
     * Create an OpenDictionaryCategoryCommand.
     * 
     * @param pEventBus
     *            The bus event.
     */
    @Inject
    public OpenDictionaryCategoryCommand(EventBus pEventBus) {
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
        String lCategory =
                Application.INJECTOR.getAdminPresenter().getDicoAdmin().getDictionaryListing().getSelectedCategory();

        String lMessage = null;
        if (Application.INJECTOR.getAdminPresenter().getDicoAdmin().getDictionaryDetail().hasChangedValues()) {
            lMessage = MESSAGES.confirmationCategoryChanged();
        }

        if (lCategory.isEmpty()) {
            fireEvent(GlobalEvent.HIDE_CATEGORY.getType(),
                    new EmptyAction<EmptyAction>(), lMessage);
        }
        else {
            fireEvent(GlobalEvent.LOAD_DICTIONARY_CATEGORY.getType(),
                    new GetDictionaryCategoryValuesAction(lCategory), lMessage);
        }
    }
}
