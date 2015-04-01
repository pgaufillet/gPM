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

import static org.topcased.gpm.ui.component.client.resources.i18n.Ui18n.MESSAGES;
import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.ui.application.client.command.AbstractCommand;
import org.topcased.gpm.ui.application.client.command.CloseTabAction;
import org.topcased.gpm.ui.application.client.command.validation.ProductListingViewValidator;
import org.topcased.gpm.ui.application.client.event.ActionEvent;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.client.util.CollectionUtil;
import org.topcased.gpm.ui.application.shared.command.product.DeleteProductsAction;

import com.google.gwt.user.client.Command;
import com.google.inject.Inject;

/**
 * A command to delete a list of products selected on the listing view.
 * 
 * @author tpanuel
 */
public class DeleteProductsCommand extends AbstractCommand implements Command {
    /**
     * Create an DeleteProductsCommand.
     * 
     * @param pEventBus
     *            The bus event.
     */
    @Inject
    public DeleteProductsCommand(final EventBus pEventBus) {
        super(pEventBus);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.Command#execute()
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void execute() {
        // The filter is executed again because sheets have been deleted
        final ActionEvent lMainAction =
                new ActionEvent(
                        GlobalEvent.EXECUTE_PRODUCT_TABLE_FILTER.getType(),
                        new DeleteProductsAction(getCurrentProductIds(),
                                getCurrentProductTableFilterId()),
                        CollectionUtil.singleton(ProductListingViewValidator.getInstance()));
        ActionEvent lPreviousAction = lMainAction;

        // Close all the selected sheet on a same
        for (String lDeletedProductId : getCurrentProductIds()) {
            final ActionEvent lCloseAction =
                    new ActionEvent(GlobalEvent.CLOSE_PRODUCT.getType(),
                            new CloseTabAction(lDeletedProductId, false));

            lPreviousAction.setNextEvent(lCloseAction);
            lPreviousAction = lCloseAction;
        }

        // A confirmation is need
        lMainAction.setConfirmationMessage(MESSAGES.confirmationProductDeletion(getCurrentProductIds().size()));
        eventBus.fireEvent(lMainAction);
    }
}