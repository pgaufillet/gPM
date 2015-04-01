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
import org.topcased.gpm.ui.application.shared.command.authorization.ConnectProductAction;
import org.topcased.gpm.ui.component.client.resources.i18n.Ui18n;

/**
 * Validate if a product workspace is open on the user space.
 * 
 * @author tpanuel
 */
public final class IsProductWorkspaceOpenValidator implements ViewValidator {
    private final static IsProductWorkspaceOpenValidator INSTANCE =
            new IsProductWorkspaceOpenValidator();

    /**
     * Private constructor for singleton.
     */
    private IsProductWorkspaceOpenValidator() {

    }

    /**
     * Get the single instance.
     * 
     * @return The single instance.
     */
    public final static IsProductWorkspaceOpenValidator getInstance() {
        return INSTANCE;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.command.validation.ViewValidator#validate(net.customware.gwt.dispatch.shared.Action)
     */
    @Override
    public String validate(final Action<?> pAction) {
        final String lProductName =
                ((ConnectProductAction) pAction).getProductName();

        if (Application.INJECTOR.getUserSpacePresenter().containTab(
                lProductName)) {
            Application.INJECTOR.getUserSpacePresenter().selectTab(lProductName);

            return Ui18n.MESSAGES.errorProductWorkspaceOpenAlreadyOpened();
        }
        else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.command.validation.ViewValidator#isError()
     */
    @Override
    public boolean isError() {
        return false;
    }
}