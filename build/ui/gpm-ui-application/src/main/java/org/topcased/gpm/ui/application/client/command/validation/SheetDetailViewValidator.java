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
import org.topcased.gpm.ui.application.client.common.container.sheet.SheetPresenter;

/**
 * Validate the sheet detail view : validate all the fields.
 * 
 * @author tpanuel
 */
public final class SheetDetailViewValidator implements ViewValidator {
    private final static SheetDetailViewValidator INSTANCE =
            new SheetDetailViewValidator();

    /**
     * Private constructor for singleton.
     */
    private SheetDetailViewValidator() {

    }

    /**
     * Get the single instance.
     * 
     * @return The single instance.
     */
    public final static SheetDetailViewValidator getInstance() {
        return INSTANCE;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.command.validation.ViewValidator#validate(net.customware.gwt.dispatch.shared.Action)
     */
    @Override
    public String validate(final Action<?> pAction) {
        final SheetPresenter lCurrentSheet =
                Application.INJECTOR.getUserSpacePresenter().getCurrentProductWorkspace().getDetail().getCurrentContainer();
        final StringBuilder lValidMessage = new StringBuilder();

        for (String lMessage : lCurrentSheet.validate()) {
            if (lMessage != null && !lMessage.isEmpty()) {
                lValidMessage.append(lMessage);
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