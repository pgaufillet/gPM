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

import static org.topcased.gpm.ui.component.client.resources.ComponentResources.I18N;
import static org.topcased.gpm.ui.component.client.resources.ComponentResources.INSTANCE;

import java.util.List;

import org.topcased.gpm.ui.application.client.common.tree.TreeProductItem;
import org.topcased.gpm.ui.application.client.common.tree.TreeProductManager;
import org.topcased.gpm.ui.application.client.popup.PopupView;
import org.topcased.gpm.ui.component.client.button.GpmTextButton;
import org.topcased.gpm.ui.component.client.layout.GpmLayoutPanelWithMenu;
import org.topcased.gpm.ui.component.client.layout.GpmTabLayoutPanel;
import org.topcased.gpm.ui.component.client.menu.GpmMenuTitle;
import org.topcased.gpm.ui.component.client.resources.i18n.Ui18n;
import org.topcased.gpm.ui.component.client.tree.GpmDynamicTree;
import org.topcased.gpm.ui.facade.shared.container.product.UiProduct;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * View for select a product on a popup.
 * 
 * @author tpanuel
 */
public class ProductSelectionView extends PopupView implements
        ProductSelectionDisplay {
    private final static double RATIO_WIDTH = 0.6;

    private final static double RATIO_HEIGHT = 0.6;

    private final FlowPanel productList;

    private final GpmDynamicTree<TreeProductItem> productHierarchy;

    /**
     * Create a product selection view.
     */
    public ProductSelectionView() {
        super(Ui18n.CONSTANTS.productSelectionTitle());
        productList = new FlowPanel();
        productList.addStyleName(INSTANCE.css().gpmTable());
        productHierarchy = new GpmDynamicTree<TreeProductItem>();

        final GpmTabLayoutPanel lTabs = new GpmTabLayoutPanel(false, true);
        lTabs.add(createTabContent(productList,
                Ui18n.CONSTANTS.productSelectionListTabSubTitle()),
                Ui18n.CONSTANTS.productSelectionListTabTitle(), null);
        lTabs.add(createTabContent(productHierarchy,
                Ui18n.CONSTANTS.productSelectionHierarchyTabSubTitle()),
                Ui18n.CONSTANTS.productSelectionHierarchyTabTitle(), null);

        setContent(lTabs);
        setRatioSize(RATIO_WIDTH, RATIO_HEIGHT);
    }

    private final static Widget createTabContent(final Widget pContent,
            final String pTitle) {
        final GpmLayoutPanelWithMenu lTabContent =
                new GpmLayoutPanelWithMenu(false);

        lTabContent.getMenu().addTitle(new GpmMenuTitle(pTitle, false));
        lTabContent.setContent(new ScrollPanel(pContent));

        return lTabContent;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.connection.ProductSelectionDisplay#setProductList(java.util.List,
     *      com.google.gwt.event.dom.client.ClickHandler)
     */
    @Override
    public void setProductList(final List<UiProduct> pProducts,
            final ClickHandler pHandler) {
        boolean lProcessOddEven = true;

        productList.clear();
        for (final UiProduct lP : pProducts) {
            final SimplePanel lPanel = new SimplePanel();
            final GpmTextButton lProduct =
                    new GpmTextButton(lP.getName(),
                            I18N.getConstant(lP.getName()));
            lProduct.setTitle(lP.getDescription());

            if (lProcessOddEven) {
                lPanel.addStyleName(INSTANCE.css().evenRow());
            }
            else {
                lPanel.addStyleName(INSTANCE.css().oddRow());
            }
            lProcessOddEven = !lProcessOddEven;
            lProduct.addClickHandler(pHandler);
            lPanel.setWidget(lProduct);
            productList.add(lPanel);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.connection.ProductSelectionDisplay#setProductHierarchy(java.util.List,
     *      org.topcased.gpm.ui.application.client.common.tree.TreeProductManager)
     */
    @Override
    public void setProductHierarchy(
            final List<TreeProductItem> pProductHierarchy,
            final TreeProductManager pManager) {
        productHierarchy.resetTree();
        productHierarchy.setDynamicTreeManager(pManager);
        productHierarchy.setRootItems(pProductHierarchy);
    }
}