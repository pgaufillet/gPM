/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.command.admin.product.product;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.ui.application.client.command.AbstractCommand;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.shared.command.product.SelectEnvironmentsAction;

import com.google.gwt.user.client.Command;
import com.google.inject.Inject;

/**
 * A command to open a product on creation.
 * 
 * @author tpanuel
 */
public class OpenProductOnCreationCommand extends AbstractCommand implements
        Command {
    private String productType;

    /**
     * Create an OpenProductOnCreationCommand.
     * 
     * @param pEventBus
     *            The bus event.
     */
    @Inject
    public OpenProductOnCreationCommand(final EventBus pEventBus) {
        super(pEventBus);
    }

    /**
     * Get the product type.
     * 
     * @return The product type.
     */
    public String getProductType() {
        return productType;
    }

    /**
     * Set the product type.
     * 
     * @param pProductType
     *            The product type.
     */
    public void setProductType(final String pProductType) {
        productType = pProductType;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.Command#execute()
     */
    @Override
    public void execute() {
        fireEvent(GlobalEvent.OPEN_SELECT_ENV_POPUP.getType(),
                new SelectEnvironmentsAction(productType));
    }
}