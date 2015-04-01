/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.popup.connection;

import java.util.ArrayList;
import java.util.List;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.ui.application.client.command.connection.SelectProductCommand;
import org.topcased.gpm.ui.application.client.command.popup.ClosePopupAction;
import org.topcased.gpm.ui.application.client.common.tree.TreeProductItem;
import org.topcased.gpm.ui.application.client.common.tree.TreeProductManager;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.client.popup.PopupPresenter;
import org.topcased.gpm.ui.application.shared.command.authorization.SelectProductResult;
import org.topcased.gpm.ui.facade.shared.container.product.UiProductHierarchy;

import com.google.inject.Inject;

/**
 * The presenter for the ProductSelectionView.
 * 
 * @author tpanuel
 */
public class ProductSelectionPresenter extends
        PopupPresenter<ProductSelectionDisplay> {
    private final SelectProductCommand selectProductCommand;

    /**
     * Create a presenter for the ProductSelectionView.
     * 
     * @param pDisplay
     *            The display interface.
     * @param pEventBus
     *            The event bus.
     * @param pSelectProductCommand
     *            The select product command.
     */
    @Inject
    public ProductSelectionPresenter(final ProductSelectionDisplay pDisplay,
            final EventBus pEventBus,
            final SelectProductCommand pSelectProductCommand) {
        super(pDisplay, pEventBus);
        selectProductCommand = pSelectProductCommand;
    }

    /**
     * Initialize the products
     * 
     * @param pResult
     *            The select product result.
     */
    public void initProducts(final SelectProductResult pResult) {
        final List<TreeProductItem> lRootItems =
                new ArrayList<TreeProductItem>();

        getDisplay().setProductList(pResult.getProductList(),
                selectProductCommand);
        for (final UiProductHierarchy lProductNode : pResult.getProductHierarchy()) {
            lRootItems.add(new TreeProductItem(lProductNode));
        }
        getDisplay().setProductHierarchy(lRootItems, new TreeProductManager() {
            @Override
            public void onSelection(final TreeProductItem pItem) {
                selectProductCommand.execute(pItem.getId());
            }
        });
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.PopupPresenter#getClosePopupEvent()
     */
    @Override
    protected GlobalEvent<ClosePopupAction> getClosePopupEvent() {
        return GlobalEvent.CLOSE_SELECT_PRODUCT_POPUP;
    }
}