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

import java.util.List;

import net.customware.gwt.dispatch.shared.Action;

import org.topcased.gpm.ui.application.client.Application;
import org.topcased.gpm.ui.component.client.resources.i18n.Ui18n;

/**
 * Validate the select environment(s) popup view : at less one environment must
 * be selected.
 * 
 * @author nveillet
 */
public final class SelectEnvironmentsViewValidator implements ViewValidator {

    private final static SelectEnvironmentsViewValidator INSTANCE =
            new SelectEnvironmentsViewValidator();

    /**
     * Private constructor for singleton.
     */
    private SelectEnvironmentsViewValidator() {

    }

    /**
     * Get the single instance.
     * 
     * @return The single instance.
     */
    public final static SelectEnvironmentsViewValidator getInstance() {
        return INSTANCE;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.command.validation.ViewValidator#validate(net.customware.gwt.dispatch.shared.Action)
     */
    @Override
    public String validate(final Action<?> pAction) {
        final List<String> lSelectedEnvironments =
                Application.INJECTOR.getPopupManager().getSelectEnvironmentsPresenter().getDisplay().getSelectedEnvironments();

        if (lSelectedEnvironments != null && !lSelectedEnvironments.isEmpty()) {
            return null;
        }
        else {
            return Ui18n.MESSAGES.errorSelectEnvironmentSelectOneAtLeast();
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
