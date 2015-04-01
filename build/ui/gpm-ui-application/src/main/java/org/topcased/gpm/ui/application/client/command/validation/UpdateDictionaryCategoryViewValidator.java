/***************************************************************
 * Copyright (c) 2011 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: phtsaan (Atos )
 ******************************************************************/
package org.topcased.gpm.ui.application.client.command.validation;

import net.customware.gwt.dispatch.shared.Action;

import org.topcased.gpm.ui.application.client.Application;
import org.topcased.gpm.ui.application.client.admin.dictionary.detail.DictionaryDetailPresenter;

/**
 * Validate if the category values are single.
 * 
 * @author phtsaan
 */
public final class UpdateDictionaryCategoryViewValidator implements
        ViewValidator {
    private final static UpdateDictionaryCategoryViewValidator INSTANCE =
            new UpdateDictionaryCategoryViewValidator();

    /**
     * Get the single instance.
     * 
     * @return The single instance of the UpdateDictionaryCategoryValidator.
     */
    public static UpdateDictionaryCategoryViewValidator getInstance() {
        return INSTANCE;
    }

    /**
     * Private constructor for singleton.
     */
    private UpdateDictionaryCategoryViewValidator() {

    }

    @Override
    public boolean isError() {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.command.validation.ViewValidator#validate(net.customware.gwt.dispatch.shared.Action)
     */
    @Override
    public String validate(Action<?> pAction) {
        DictionaryDetailPresenter lDictionaryDetail =
                Application.INJECTOR.getAdminPresenter().getDicoAdmin().getDictionaryDetail();

        final StringBuilder lValidMessage = new StringBuilder();

        String lMessage = lDictionaryDetail.validate();
        if (lMessage != null && !lMessage.isEmpty()) {
            lValidMessage.append(lMessage);
        }

        return lValidMessage.toString().trim();
    }

}
