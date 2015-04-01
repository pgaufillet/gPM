/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.command.link;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.business.util.DisplayMode;
import org.topcased.gpm.ui.application.client.Application;
import org.topcased.gpm.ui.application.client.common.container.product.ProductPresenter;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.shared.command.link.GetLinksAction;
import org.topcased.gpm.ui.component.client.container.GpmLinkGroupPanel;

import com.google.inject.Inject;

/**
 * LoadProductLinkOnVisualizationCommand
 * 
 * @author nveillet
 */
public class LoadProductLinkOnVisualizationCommand extends
        AbstractLoadLinkCommand {

    /**
     * Create an LoadLinkCommand.
     * 
     * @param pEventBus
     *            The bus event.
     */
    @Inject
    public LoadProductLinkOnVisualizationCommand(EventBus pEventBus) {
        super(pEventBus);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.util.GpmBasicActionHandler#execute(java.lang.Object)
     */
    @Override
    public void execute(String pLinkTypeName) {
        ProductPresenter lProductPresenter =
                Application.INJECTOR.getAdminPresenter().getProductAdmin().getDetail().getCurrentContainer();

        GpmLinkGroupPanel lLinkGroup =
                lProductPresenter.getDisplay().getLinkGroup(pLinkTypeName);

        if (lLinkGroup.isEmpty()) {
            fireEvent(GlobalEvent.LOAD_PRODUCT_LINKS.getType(),
                    new GetLinksAction(null, lProductPresenter.getTabId(),
                            pLinkTypeName, DisplayMode.VISUALIZATION));
        }
        else {
            lLinkGroup.open();
        }
    }
}
