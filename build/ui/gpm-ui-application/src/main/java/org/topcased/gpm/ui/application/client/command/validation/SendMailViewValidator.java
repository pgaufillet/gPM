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
import org.topcased.gpm.ui.application.client.popup.mail.SendMailDisplay;

/**
 * Validate the send mail popup view : All fields must be filled.
 * 
 * @author nveillet
 */
public final class SendMailViewValidator implements ViewValidator {

    private final static SendMailViewValidator INSTANCE =
            new SendMailViewValidator();

    /**
     * Get the single instance.
     * 
     * @return The single instance.
     */
    public final static SendMailViewValidator getInstance() {
        return INSTANCE;
    }

    /**
     * Private constructor for singleton.
     */
    private SendMailViewValidator() {

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

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.command.validation.ViewValidator#validate(net.customware.gwt.dispatch.shared.Action)
     */
    @Override
    public String validate(Action<?> pAction) {
        SendMailDisplay lSendMailDisplay =
                Application.INJECTOR.getPopupManager().getSendMailPresenter().getDisplay();
        return lSendMailDisplay.validate();
    }
}
