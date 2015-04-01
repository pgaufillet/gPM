/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.command.admin.product.products;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.ui.application.client.command.AbstractCommand;
import org.topcased.gpm.ui.application.client.command.validation.ProductListingViewValidator;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.shared.command.export.ExportProductAction;

import com.google.gwt.user.client.Command;
import com.google.inject.Inject;

/**
 * A command to export a list of products on XML format.
 * 
 * @author tpanuel
 */
public class ExportProductsOnXmlCommand extends AbstractCommand implements
        Command {
    /**
     * Create an ExportProductsOnXmlCommand.
     * 
     * @param pEventBus
     *            The bus event.
     */
    @Inject
    public ExportProductsOnXmlCommand(final EventBus pEventBus) {
        super(pEventBus);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.Command#execute()
     */
    @Override
    public void execute() {
        fireEvent(GlobalEvent.EXPORT_PRODUCTS.getType(),
                new ExportProductAction(getCurrentProductIds()),
                ProductListingViewValidator.getInstance());
    }
}