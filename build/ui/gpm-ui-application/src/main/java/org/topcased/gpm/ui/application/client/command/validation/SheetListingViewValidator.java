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

import static org.topcased.gpm.ui.component.client.resources.i18n.Ui18n.MESSAGES;

import java.util.List;

import net.customware.gwt.dispatch.shared.Action;

import org.topcased.gpm.ui.application.client.Application;

/**
 * Validate the sheet listing view : at less one sheet must be selected.
 * 
 * @author tpanuel
 */
public final class SheetListingViewValidator implements ViewValidator {
    private final static SheetListingViewValidator INSTANCE =
            new SheetListingViewValidator();

    /**
     * Private constructor for singleton.
     */
    private SheetListingViewValidator() {

    }

    /**
     * Get the single instance.
     * 
     * @return The single instance.
     */
    public final static SheetListingViewValidator getInstance() {
        return INSTANCE;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.command.validation.ViewValidator#validate(net.customware.gwt.dispatch.shared.Action)
     */
    @Override
    public String validate(final Action<?> pAction) {
        final List<String> lSheetIds =
                Application.INJECTOR.getUserSpacePresenter().getCurrentProductWorkspace().getListing().getSelectedElementIds();

        if (lSheetIds != null && !lSheetIds.isEmpty()) {
            return null;
        }
        else {
            return MESSAGES.errorSheetNeedSelection(1);
        }
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