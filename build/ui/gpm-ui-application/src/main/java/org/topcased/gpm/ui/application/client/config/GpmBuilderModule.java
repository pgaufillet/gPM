/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.config;

import org.topcased.gpm.ui.application.client.command.builder.AddProductLinkCommandBuilder;
import org.topcased.gpm.ui.application.client.command.builder.AddSheetLinkCommandBuilder;
import org.topcased.gpm.ui.application.client.command.builder.ChangeSheetStateCommandBuilder;
import org.topcased.gpm.ui.application.client.command.builder.DeleteProductLinkCommandBuilder;
import org.topcased.gpm.ui.application.client.command.builder.DeleteSheetLinkCommandBuilder;
import org.topcased.gpm.ui.application.client.command.builder.InitializeSheetCommandBuilder;
import org.topcased.gpm.ui.application.client.command.builder.OpenProductOnCreationCommandBuilder;
import org.topcased.gpm.ui.application.client.command.builder.OpenSheetOnCreationCommandBuilder;
import org.topcased.gpm.ui.application.client.menu.admin.product.ProductAdminMenuBuilder;
import org.topcased.gpm.ui.application.client.menu.admin.product.ProductDetailCreationMenuBuilder;
import org.topcased.gpm.ui.application.client.menu.admin.product.ProductDetailEditionMenuBuilder;
import org.topcased.gpm.ui.application.client.menu.admin.product.ProductDetailVisualizationMenuBuilder;
import org.topcased.gpm.ui.application.client.menu.admin.product.ProductListingMenuBuilder;
import org.topcased.gpm.ui.application.client.menu.admin.product.ProductNavigationTableMenuBuilder;
import org.topcased.gpm.ui.application.client.menu.admin.product.ProductNavigationTreeMenuBuilder;
import org.topcased.gpm.ui.application.client.menu.user.ProductWorkspaceMenuBuilder;
import org.topcased.gpm.ui.application.client.menu.user.SheetDetailCreationMenuBuilder;
import org.topcased.gpm.ui.application.client.menu.user.SheetDetailEditionMenuBuilder;
import org.topcased.gpm.ui.application.client.menu.user.SheetDetailVisualizationMenuBuilder;
import org.topcased.gpm.ui.application.client.menu.user.SheetListingMenuBuilder;
import org.topcased.gpm.ui.application.client.menu.user.SheetNavigationTableMenuBuilder;
import org.topcased.gpm.ui.application.client.menu.user.SheetNavigationTreeMenuBuilder;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;

/**
 * Module with injection of all the menu builder.
 * 
 * @author tpanuel
 */
public class GpmBuilderModule extends AbstractGinModule {
    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.inject.client.AbstractGinModule#configure()
     */
    @Override
    protected void configure() {
        // Menu builder
        bind(ProductWorkspaceMenuBuilder.class);
        bind(SheetNavigationTableMenuBuilder.class);
        bind(SheetNavigationTreeMenuBuilder.class);
        bind(SheetListingMenuBuilder.class);
        bind(SheetDetailVisualizationMenuBuilder.class);
        bind(SheetDetailEditionMenuBuilder.class);
        bind(SheetDetailCreationMenuBuilder.class);
        bind(ProductAdminMenuBuilder.class).in(Singleton.class);
        bind(ProductNavigationTableMenuBuilder.class).in(Singleton.class);
        bind(ProductNavigationTreeMenuBuilder.class).in(Singleton.class);
        bind(ProductListingMenuBuilder.class).in(Singleton.class);
        bind(ProductDetailVisualizationMenuBuilder.class).in(Singleton.class);
        bind(ProductDetailEditionMenuBuilder.class).in(Singleton.class);
        bind(ProductDetailCreationMenuBuilder.class).in(Singleton.class);

        // Command builder
        bind(OpenSheetOnCreationCommandBuilder.class).in(Singleton.class);
        bind(InitializeSheetCommandBuilder.class).in(Singleton.class);
        bind(AddSheetLinkCommandBuilder.class).in(Singleton.class);
        bind(DeleteSheetLinkCommandBuilder.class).in(Singleton.class);
        bind(ChangeSheetStateCommandBuilder.class).in(Singleton.class);
        bind(OpenProductOnCreationCommandBuilder.class).in(Singleton.class);
        bind(AddProductLinkCommandBuilder.class).in(Singleton.class);
        bind(DeleteProductLinkCommandBuilder.class).in(Singleton.class);
    }
}