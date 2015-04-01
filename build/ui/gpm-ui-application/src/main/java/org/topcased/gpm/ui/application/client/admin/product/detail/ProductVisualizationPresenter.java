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

import org.topcased.gpm.ui.application.client.command.admin.product.product.OpenProductOnEditionCommand;
import org.topcased.gpm.ui.application.client.command.admin.product.product.OpenProductOnVisualizationCommand;
import org.topcased.gpm.ui.application.client.command.link.LoadProductLinkOnVisualizationCommand;
import org.topcased.gpm.ui.application.client.common.container.product.ProductDisplay;
import org.topcased.gpm.ui.application.client.common.container.product.ProductPresenter;
import org.topcased.gpm.ui.application.client.menu.admin.product.ProductDetailVisualizationMenuBuilder;

import com.google.inject.Inject;

/**
 * Presenter for the ProductView on visualization mode.
 * 
 * @author tpanuel
 */
public class ProductVisualizationPresenter extends ProductPresenter {
    /**
     * Create a presenter for the ProductView on visualization mode.
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
    @Inject
    public ProductVisualizationPresenter(final ProductDisplay pDisplay,
            final EventBus pEventBus,
            final ProductDetailVisualizationMenuBuilder pMenuBuilder,
            final OpenProductOnVisualizationCommand pVisuCommand,
            final OpenProductOnEditionCommand pEditCommand,
            final LoadProductLinkOnVisualizationCommand pLoadLinkCommand) {
        super(pDisplay, pEventBus, pMenuBuilder, pVisuCommand, pEditCommand,
                pLoadLinkCommand);
    }
}