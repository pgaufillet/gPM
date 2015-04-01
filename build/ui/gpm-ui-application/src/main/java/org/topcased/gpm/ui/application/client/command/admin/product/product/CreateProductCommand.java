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
import org.topcased.gpm.ui.application.client.command.validation.ProductDetailViewValidator;
import org.topcased.gpm.ui.application.client.common.container.product.ProductPresenter;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.shared.command.product.CreateProductAction;

import com.google.gwt.user.client.Command;
import com.google.inject.Inject;

/**
 * A command to create a product.
 * 
 * @author tpanuel
 */
public class CreateProductCommand extends AbstractCommand implements Command {
    /**
     * Create an CreateProductCommand.
     * 
     * @param pEventBus
     *            The bus event.
     */
    @Inject
    public CreateProductCommand(final EventBus pEventBus) {
        super(pEventBus);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.Command#execute()
     */
    @Override
    public void execute() {
        final ProductPresenter lProductPresenter = getCurrentProductPresenter();

        uploadFileAndFireEvent(
                GlobalEvent.LOAD_PRODUCT.getType(),
                new CreateProductAction(getCurrentProductId(),
                        lProductPresenter.getDisplay().getProductName(),
                        lProductPresenter.getUpdatedFields(),
                        lProductPresenter.getDisplay().getParentProducts(),
                        lProductPresenter.getDisplay().getChildProducts(),
                        lProductPresenter.getDisplay().getProductDescription()),
                ProductDetailViewValidator.getInstance(),
                lProductPresenter.getUploadRegisters());
    }

}