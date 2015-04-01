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
import org.topcased.gpm.ui.application.client.common.container.sheet.SheetPresenter;
import org.topcased.gpm.ui.application.client.event.LocalEvent;
import org.topcased.gpm.ui.application.shared.command.link.GetLinksAction;
import org.topcased.gpm.ui.component.client.container.GpmLinkGroupPanel;

import com.google.inject.Inject;

/**
 * LoadSheetLinkOnVisualizationCommand
 * 
 * @author nveillet
 */
public class LoadSheetLinkOnVisualizationCommand extends
        AbstractLoadLinkCommand {

    /**
     * Create an LoadLinkCommand.
     * 
     * @param pEventBus
     *            The bus event.
     */
    @Inject
    public LoadSheetLinkOnVisualizationCommand(EventBus pEventBus) {
        super(pEventBus);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.util.GpmBasicActionHandler#execute(java.lang.Object)
     */
    @Override
    public void execute(String pLinkTypeName) {
        SheetPresenter lSheetPresenter =
                Application.INJECTOR.getUserSpacePresenter().getCurrentProductWorkspace().getDetail().getCurrentContainer();

        GpmLinkGroupPanel lLinkGroup =
                lSheetPresenter.getDisplay().getLinkGroup(pLinkTypeName);

        if (lLinkGroup.isEmpty()) {
            String lProductName = getCurrentProductWorkspaceName();
            fireEvent(LocalEvent.LOAD_SHEET_LINKS.getType(lProductName),
                    new GetLinksAction(lProductName,
                            lSheetPresenter.getTabId(), pLinkTypeName,
                            DisplayMode.VISUALIZATION));
        }
        else {
            lLinkGroup.open();
        }
    }
}
