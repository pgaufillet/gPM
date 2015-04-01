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
import org.topcased.gpm.ui.application.client.admin.product.detail.ProductCreationPresenter;
import org.topcased.gpm.ui.application.client.common.container.product.ProductPresenter;

/**
 * Validate the product detail view : validate all the fields and the product
 * name on creation.
 * 
 * @author tpanuel
 */
public final class ProductDetailViewValidator implements ViewValidator {
    private final static ProductDetailViewValidator INSTANCE =
            new ProductDetailViewValidator();

    /**
     * Private constructor for singleton.
     */
    private ProductDetailViewValidator() {

    }

    /**
     * Get the single instance.
     * 
     * @return The single instance.
     */
    public final static ProductDetailViewValidator getInstance() {
        return INSTANCE;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.command.validation.ViewValidator#validate(net.customware.gwt.dispatch.shared.Action)
     */
    @Override
    public String validate(final Action<?> pAction) {
        final ProductPresenter lCurrentProduct =
                Application.INJECTOR.getAdminPresenter().getProductAdmin().getDetail().getCurrentContainer();
        final StringBuilder lValidMessage = new StringBuilder();

        // Check name for creation only
        if (lCurrentProduct instanceof ProductCreationPresenter) {
            String lErrMsg = lCurrentProduct.getDisplay().validate();
            if (lErrMsg != null) {
                lValidMessage.append(lErrMsg).append("<br/>");
            }
        }
        // Check fields
        for (String lMessage : lCurrentProduct.validate()) {
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