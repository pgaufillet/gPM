/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.admin.product.detail;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.ui.application.client.Application;
import org.topcased.gpm.ui.application.client.command.CloseTabAction;
import org.topcased.gpm.ui.application.client.command.OpenCloseWorkspacePanelAction;
import org.topcased.gpm.ui.application.client.common.container.ContainerPresenter;
import org.topcased.gpm.ui.application.client.common.container.product.ProductDisplay;
import org.topcased.gpm.ui.application.client.common.container.product.ProductPresenter;
import org.topcased.gpm.ui.application.client.common.tab.TabElementPresenter;
import org.topcased.gpm.ui.application.client.common.workspace.detail.DetailPresenter;
import org.topcased.gpm.ui.application.client.event.ActionEventHandler;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.shared.command.link.GetLinksResult;
import org.topcased.gpm.ui.application.shared.command.product.CloseProductAction;
import org.topcased.gpm.ui.application.shared.command.product.GetProductResult;
import org.topcased.gpm.ui.component.client.resources.i18n.Ui18n;

import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.inject.Inject;

/**
 * The presenter for the ProductDetailView.
 * 
 * @author tpanuel
 */
public class ProductDetailPresenter extends
        DetailPresenter<ProductPresenter, ProductDetailDisplay, ProductDisplay> {

    /**
     * Create a presenter for the ProductDetailView.
     * 
     * @param pDisplay
     *            The display interface.
     * @param pEventBus
     *            The event bus.
     */
    @Inject
    public ProductDetailPresenter(final ProductDetailDisplay pDisplay,
            final EventBus pEventBus) {
        super(pDisplay, pEventBus);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.presenter.client.BasicPresenter#onBind()
     */
    protected void onBind() {
        // Add handler to open a product
        addEventHandler(GlobalEvent.LOAD_PRODUCT.getType(),
                new ActionEventHandler<GetProductResult>() {
                    @Override
                    public void execute(final GetProductResult pResult) {
                        addContainer(pResult);
                    }
                });
        // Add handler for close tab
        addEventHandler(GlobalEvent.CLOSE_PRODUCT.getType(),
                new ActionEventHandler<CloseTabAction>() {
                    @Override
                    public void execute(final CloseTabAction pResult) {
                        removeTab(pResult.getTabId());
                        if (pResult.isSendServer()) {
                            fireEvent(GlobalEvent.CLOSE_CONTAINER.getType(),
                                    new CloseProductAction(pResult.getTabId()));
                        }
                    }
                });
        // Add handler for get links
        addEventHandler(GlobalEvent.LOAD_PRODUCT_LINKS.getType(),
                new ActionEventHandler<GetLinksResult>() {
                    @Override
                    public void execute(final GetLinksResult pResult) {
                        getCurrentContainer().initLinks(
                                pResult.getValuesContainerId(),
                                pResult.getLinkTypeName(), pResult.getLinks());
                    }
                });
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.common.workspace.detail.DetailPresenter#getCreationPresenter()
     */
    @Override
    protected ProductPresenter getCreationPresenter() {
        return Application.INJECTOR.getProductCreationPresenter();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.common.workspace.detail.DetailPresenter#getVisualizationPresenter()
     */
    @Override
    protected ProductPresenter getVisualizationPresenter() {
        return Application.INJECTOR.getProductVisualizationPresenter();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.common.workspace.detail.DetailPresenter#getEditionPresenter()
     */
    @Override
    protected ProductPresenter getEditionPresenter() {
        return Application.INJECTOR.getProductEditionPresenter();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.common.workspace.detail.DetailPresenter#getCloseTabType()
     */
    @Override
    protected Type<ActionEventHandler<CloseTabAction>> getCloseTabType() {
        return GlobalEvent.CLOSE_PRODUCT.getType();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.common.workspace.detail.DetailPresenter#getOpenCloseWorkspaceType()
     */
    @Override
    protected Type<ActionEventHandler<OpenCloseWorkspacePanelAction>> getOpenCloseWorkspaceType() {
        return GlobalEvent.OPEN_CLOSE_PRODUCT_WORKSPACE.getType();
    }

    /**
     * Get a product presenter.
     * 
     * @param pProductId
     *            The product id.
     * @return The product presenter.
     */
    public ProductPresenter getProductPresenter(final String pProductId) {
        for (TabElementPresenter<ProductDisplay> lProduct : getPresenters()) {
            if (lProduct.getTabId().equals(pProductId)) {
                selectTab(pProductId);

                return (ProductPresenter) lProduct;
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.common.workspace.detail.DetailPresenter#needCloseConfirmation(java.lang.String)
     */
    @Override
    protected String needCloseConfirmation(String pTabId) {
        if (getPresenterById(pTabId) instanceof ProductEditionPresenter
                || getPresenterById(pTabId) instanceof ProductCreationPresenter) {
            ContainerPresenter<?> lContainer =
                    (ContainerPresenter<?>) getPresenterById(pTabId);
            if (lContainer.getUpdatedFields().size() != 0
                    || lContainer.getUpdatedLinkFields().size() != 0) {
                return Ui18n.MESSAGES.confirmationCloseProduct();
            }
        }
        return null;
    }
}