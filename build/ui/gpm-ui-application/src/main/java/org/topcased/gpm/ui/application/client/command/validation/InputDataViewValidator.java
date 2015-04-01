/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.command.validation;

import net.customware.gwt.dispatch.shared.Action;

import org.topcased.gpm.ui.application.client.Application;
import org.topcased.gpm.ui.application.client.popup.extended.InputDataPresenter;

/**
 * Validate the input data detail view : validate all the fields.
 * 
 * @author tpanuel
 */
public final class InputDataViewValidator implements ViewValidator {
    private final static InputDataViewValidator INSTANCE =
            new InputDataViewValidator();

    /**
     * Private constructor for singleton.
     */
    private InputDataViewValidator() {

    }

    /**
     * Get the single instance.
     * 
     * @return The single instance.
     */
    public final static InputDataViewValidator getInstance() {
        return INSTANCE;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.command.validation.ViewValidator#validate(net.customware.gwt.dispatch.shared.Action)
     */
    @Override
    public String validate(final Action<?> pAction) {
        final InputDataPresenter lInputData =
                Application.INJECTOR.getPopupManager().getInputDataPresenter();
        final StringBuilder lValidMessage = new StringBuilder();

        for (String lMessage : lInputData.getInputDataFieldManager().validate()) {
            if (lMessage != null && !lMessage.isEmpty()) {
                lValidMessage.append(lMessage).append("<br/>");
            }
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