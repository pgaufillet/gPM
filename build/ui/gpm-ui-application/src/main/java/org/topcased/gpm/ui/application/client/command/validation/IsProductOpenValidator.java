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
import org.topcased.gpm.ui.application.client.admin.product.detail.ProductEditionPresenter;
import org.topcased.gpm.ui.application.client.common.container.product.ProductPresenter;
import org.topcased.gpm.ui.application.shared.command.product.GetProductEditionAction;
import org.topcased.gpm.ui.application.shared.command.product.GetProductVisualizationAction;
import org.topcased.gpm.ui.component.client.resources.i18n.Ui18n;

/**
 * Validate if a product is open on edition mode on a detail zone.
 * 
 * @author tpanuel
 */
public final class IsProductOpenValidator implements ViewValidator {
    private final static IsProductOpenValidator INSTANCE =
            new IsProductOpenValidator();

    /**
     * Private constructor for singleton.
     */
    private IsProductOpenValidator() {

    }

    /**
     * Get the single instance.
     * 
     * @return The single instance.
     */
    public final static IsProductOpenValidator getInstance() {
        return INSTANCE;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.command.validation.ViewValidator#validate(net.customware.gwt.dispatch.shared.Action)
     */
    @Override
    public String validate(final Action<?> pAction) {
        final String lProductId;

        if (pAction instanceof GetProductVisualizationAction) {
            lProductId =
                    ((GetProductVisualizationAction) pAction).getProductId();
        }
        else if (pAction instanceof GetProductEditionAction) {
            lProductId = ((GetProductEditionAction) pAction).getProductId();
        }
        else {
            lProductId = null;
        }

        final ProductPresenter lProduct =
                Application.INJECTOR.getAdminPresenter().getProductAdmin().getDetail().getProductPresenter(
                        lProductId);

        if (lProduct instanceof ProductEditionPresenter) {
            return Ui18n.MESSAGES.errorProductOpenAlreadyOpened();
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