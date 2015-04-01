/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.common.container.product;

import java.util.ArrayList;
import java.util.List;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.business.util.Translation;
import org.topcased.gpm.business.values.field.BusinessField;
import org.topcased.gpm.ui.application.client.command.OpenCloseWorkspacePanelAction;
import org.topcased.gpm.ui.application.client.command.admin.product.product.OpenProductOnEditionCommand;
import org.topcased.gpm.ui.application.client.command.admin.product.product.OpenProductOnVisualizationCommand;
import org.topcased.gpm.ui.application.client.command.link.AbstractLoadLinkCommand;
import org.topcased.gpm.ui.application.client.common.container.ContainerPresenter;
import org.topcased.gpm.ui.application.client.event.ActionEventHandler;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.client.menu.admin.product.AbstractProductDetailMenuBuilder;
import org.topcased.gpm.ui.application.shared.command.container.GetContainerResult;
import org.topcased.gpm.ui.application.shared.command.product.GetProductCreationResult;
import org.topcased.gpm.ui.application.shared.command.product.GetProductEditionResult;
import org.topcased.gpm.ui.application.shared.command.product.GetProductResult;
import org.topcased.gpm.ui.application.shared.command.product.GetProductVisualizationResult;
import org.topcased.gpm.ui.facade.shared.container.product.UiProduct;

import com.google.gwt.event.shared.GwtEvent.Type;

/**
 * The presenter for display a product.
 * 
 * @author tpanuel
 */
public abstract class ProductPresenter extends
        ContainerPresenter<ProductDisplay> {
    private final AbstractProductDetailMenuBuilder menuBuilder;

    /**
     * Create a presenter for the ProductView.
     * 
     * @param pDisplay
     *            The display interface.
     * @param pEventBus
     *            The event bus.
     * @param pMenuBuilder
     *            The menu builder.
     * @param pVisuCommand
     *            The visualization command.
     * @param pEditCommand
     *            The edition command.
     * @param pLoadLinkCommand
     *            The load link command.
     */
    public ProductPresenter(final ProductDisplay pDisplay,
            final EventBus pEventBus,
            final AbstractProductDetailMenuBuilder pMenuBuilder,
            final OpenProductOnVisualizationCommand pVisuCommand,
            final OpenProductOnEditionCommand pEditCommand,
            final AbstractLoadLinkCommand pLoadLinkCommand) {
        super(pDisplay, pEventBus, pVisuCommand, pEditCommand, pLoadLinkCommand);
        menuBuilder = pMenuBuilder;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.common.container.ContainerPresenter#initContainer(org.topcased.gpm.ui.application.shared.command.container.GetContainerResult)
     */
    @Override
    public void initContainer(final GetContainerResult<?> pResult) {
        final GetProductResult lProductResult = (GetProductResult) pResult;
        final UiProduct lProduct = lProductResult.getContainer();

        // Initialize the properties group
        if (lProductResult instanceof GetProductCreationResult) {
            getDisplay().initPropertiesGroup(
                    true,
                    true,
                    ((GetProductCreationResult) lProductResult).getProductNames(),
                    lProductResult.getContainer().getParents(),
                    lProductResult.getContainer().getChildren(),
                    lProductResult.getContainer().getDescription());
        }
        else if (lProductResult instanceof GetProductVisualizationResult) {
            getDisplay().initPropertiesGroup(false, false, null,
                    lProductResult.getContainer().getParents(),
                    lProductResult.getContainer().getChildren(),
                    lProductResult.getContainer().getDescription());
        }
        else if (lProductResult instanceof GetProductEditionResult) {
        	GetProductEditionResult lProductEditionResult = (GetProductEditionResult) lProductResult;
            getDisplay().initPropertiesGroup(
                    false,
                    lProductEditionResult.isAdmin(),
                    ((GetProductEditionResult) lProductResult).getProductNames(),
                    lProductResult.getContainer().getParents(),
                    lProductResult.getContainer().getChildren(),
                    lProductResult.getContainer().getDescription());
        }

        // Get fields
        final List<String> lFieldNames = new ArrayList<String>();

        for (BusinessField lField : lProduct) {
            lFieldNames.add(lField.getFieldName());
        }

        // Get links
        final List<Translation> lLinkGroups;

        if (pResult instanceof GetProductEditionResult) {
            lLinkGroups = ((GetProductEditionResult) pResult).getLinkGroups();
        }
        else if (pResult instanceof GetProductVisualizationResult) {
            lLinkGroups =
                    ((GetProductVisualizationResult) pResult).getLinkGroups();
        }
        else {
            lLinkGroups = null;
        }

        // Initialize product with id, name, display groups and link groups
        init(lProduct.getId(), lProduct.getName(), lProduct,
                lProduct.getFieldGroups(), lLinkGroups, null);

        // Display tool bar
        getDisplay().setToolBar(
                menuBuilder.buildMenu(lProductResult.getActions(), null));
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.common.container.ContainerPresenter#getOpenCloseWorkspacePanelActionType()
     */
    @Override
    protected Type<ActionEventHandler<OpenCloseWorkspacePanelAction>> getOpenCloseWorkspacePanelActionType() {
        return GlobalEvent.OPEN_CLOSE_PRODUCT_WORKSPACE.getType();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.common.AbstractPresenter#onUnbind()
     */
    @Override
    protected void onUnbind() {
        // Nothing to unbind
    }

    /**
     * Only validate product's name using view validator
     * 
     * @return an error message or null if product name was valid
     */
    public String validateName() {
        return getDisplay().validate();
    }

    public String getProductDescription() {
        return getDisplay().getProductDescription();
    }
}