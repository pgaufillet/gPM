/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.command.validation;

import net.customware.gwt.dispatch.shared.Action;

import org.topcased.gpm.ui.application.client.Application;
import org.topcased.gpm.ui.application.client.admin.dictionary.detail.EnvironmentCreationDetailPresenter;

/**
 * Validate the environment creation detail view : validate all the fields.
 * 
 * @author nveillet
 */
public final class EnvironmentCreationDetailViewValidator implements
        ViewValidator {

    private final static EnvironmentCreationDetailViewValidator INSTANCE =
            new EnvironmentCreationDetailViewValidator();

    /**
     * Private constructor for singleton.
     */
    private EnvironmentCreationDetailViewValidator() {

    }

    /**
     * Get the single instance.
     * 
     * @return The single instance.
     */
    public final static EnvironmentCreationDetailViewValidator getInstance() {
        return INSTANCE;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.command.validation.ViewValidator#validate(net.customware.gwt.dispatch.shared.Action)
     */
    @Override
    public String validate(final Action<?> pAction) {
        EnvironmentCreationDetailPresenter lCreationDetailPresenter =
                Application.INJECTOR.getAdminPresenter().getDicoAdmin().getEnvironmentCreationDetail();

        final StringBuilder lValidMessage = new StringBuilder();

        String lMessage = lCreationDetailPresenter.validate();
        if (lMessage != null && !lMessage.isEmpty()) {
            lValidMessage.append(lMessage);
        }

        return lValidMessage.toString().trim();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.command.validation.ViewValidator#isError()
     */
    @Override
    public boolean isError() {
        return true;
    }
}