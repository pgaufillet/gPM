/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.user.detail;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.ui.application.client.command.link.LoadSheetLinkOnEditionCommand;
import org.topcased.gpm.ui.application.client.command.user.sheet.OpenSheetOnEditionCommand;
import org.topcased.gpm.ui.application.client.command.user.sheet.OpenSheetOnVisualizationCommand;
import org.topcased.gpm.ui.application.client.common.container.sheet.SheetDisplay;
import org.topcased.gpm.ui.application.client.common.container.sheet.SheetPresenter;
import org.topcased.gpm.ui.application.client.menu.user.SheetDetailCreationMenuBuilder;

import com.google.inject.Inject;

/**
 * Presenter for the SheetView on creation mode.
 * 
 * @author tpanuel
 */
public class SheetCreationPresenter extends SheetPresenter {

    /**
     * Create a presenter for the SheetView on creation mode.
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
    public SheetCreationPresenter(final SheetDisplay pDisplay,
            final EventBus pEventBus,
            final SheetDetailCreationMenuBuilder pMenuBuilder,
            final OpenSheetOnVisualizationCommand pVisuCommand,
            final OpenSheetOnEditionCommand pEditCommand,
            final LoadSheetLinkOnEditionCommand pLoadLinkCommand) {
        super(pDisplay, pEventBus, pMenuBuilder, pVisuCommand, pEditCommand,
                pLoadLinkCommand);
    }
}