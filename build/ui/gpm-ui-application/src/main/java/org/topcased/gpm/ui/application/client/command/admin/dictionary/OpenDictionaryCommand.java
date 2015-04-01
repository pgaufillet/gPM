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
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.shared.command.dictionary.GetDictionaryCategoriesAction;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.inject.Inject;

/**
 * A command to open a dictionary.
 * 
 * @author nveillet
 */
public class OpenDictionaryCommand extends AbstractCommand implements
        ClickHandler {

    /**
     * Create an OpenDictionaryCommand.
     * 
     * @param pEventBus
     *            The bus event.
     */
    @Inject
    public OpenDictionaryCommand(EventBus pEventBus) {
        super(pEventBus);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.event.dom.client.ClickHandler#onClick(com.google.gwt.event.dom.client.ClickEvent)
     */
    @Override
    public void onClick(ClickEvent pEvent) {
        DictionaryAdminPresenter lDicoAdmin =
                Application.INJECTOR.getAdminPresenter().getDicoAdmin();

        String lMessage = null;
        if (lDicoAdmin.getDictionaryDetail().hasChangedValues()
                || lDicoAdmin.getEnvironmentDetail().hasChangedValues()) {
            lMessage = MESSAGES.confirmationCategoryChanged();
        }

        fireEvent(GlobalEvent.LOAD_DICTIONARY.getType(),
                new GetDictionaryCategoriesAction(), lMessage);
    }
}
