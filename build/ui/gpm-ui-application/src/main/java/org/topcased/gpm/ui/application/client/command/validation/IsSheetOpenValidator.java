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
import org.topcased.gpm.ui.application.client.user.detail.SheetEditionPresenter;
import org.topcased.gpm.ui.application.shared.command.sheet.GetSheetEditionAction;
import org.topcased.gpm.ui.application.shared.command.sheet.GetSheetVisualizationAction;
import org.topcased.gpm.ui.component.client.resources.i18n.Ui18n;

/**
 * Validate if a sheet is open on edition mode on a detail zone.
 * 
 * @author tpanuel
 */
public final class IsSheetOpenValidator implements ViewValidator {
    private final static IsSheetOpenValidator INSTANCE =
            new IsSheetOpenValidator();

    /**
     * Private constructor for singleton.
     */
    private IsSheetOpenValidator() {

    }

    /**
     * Get the single instance.
     * 
     * @return The single instance.
     */
    public final static IsSheetOpenValidator getInstance() {
        return INSTANCE;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.command.validation.ViewValidator#validate(net.customware.gwt.dispatch.shared.Action)
     */
    @Override
    public String validate(final Action<?> pAction) {
        final String lSheetId;

        if (pAction instanceof GetSheetVisualizationAction) {
            lSheetId = ((GetSheetVisualizationAction) pAction).getSheetId();
        }
        else if (pAction instanceof GetSheetEditionAction) {
            lSheetId = ((GetSheetEditionAction) pAction).getSheetId();
        }
        else {
            lSheetId = null;
        }

        final SheetPresenter lSheet =
                Application.INJECTOR.getUserSpacePresenter().getSheetPresenter(
                        lSheetId);

        if (lSheet instanceof SheetEditionPresenter) {
            return Ui18n.MESSAGES.errorSheetOpenAlreadyOpened();
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