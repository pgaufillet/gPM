/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.popup.message;

import static org.topcased.gpm.ui.component.client.resources.i18n.Ui18n.CONSTANTS;
import static org.topcased.gpm.ui.component.client.resources.i18n.Ui18n.MESSAGES;

import org.topcased.gpm.ui.application.client.popup.PopupView;
import org.topcased.gpm.ui.component.client.resources.ComponentResources;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ScrollPanel;

/**
 * View for display an error session.
 * 
 * @author nveillet
 */
public class ErrorSessionMessageView extends PopupView implements
        ErrorSessionMessageDisplay {
    private final static double RATIO_WIDTH = 0.4;

    private final static double RATIO_HEIGHT = 0.2;

    private final HTML errorMessage;

    private final Button logoutButton;

    private final Button cancelButton;

    /**
     * Create a send sheet(s) by mail view.
     */
    public ErrorSessionMessageView() {
        super(false);
        setAutoHideEnabled(false);

        enableGlass();

        setHeaderText(CONSTANTS.errorSessionTitle());

        final ScrollPanel lPanel = new ScrollPanel();
        final DockPanel lDock = new DockPanel();
        final Image lImage =
                new Image(ComponentResources.INSTANCE.images().error());

        // The error message
        errorMessage = new HTML(MESSAGES.errorSession());
        errorMessage.addStyleName(ComponentResources.INSTANCE.css().gpmBigBorder());

        // Add image
        lImage.addStyleName(ComponentResources.INSTANCE.css().gpmBigBorder());
        lDock.addStyleName(ComponentResources.INSTANCE.css().gpmBigBorder());
        lDock.add(lImage, DockPanel.WEST);
        lDock.add(errorMessage, DockPanel.CENTER);

        lPanel.addStyleName(ComponentResources.INSTANCE.css().gpmSmallBorder());
        lPanel.setWidget(lDock);

        setContent(lPanel);

        logoutButton = addButton(CONSTANTS.errorSessionButton());
        cancelButton = addButton(CONSTANTS.errorSessionCancelButton());

        setRatioSize(RATIO_WIDTH, RATIO_HEIGHT);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.message.ErrorSessionMessageDisplay#getLogoutButton()
     */
    @Override
    public HasClickHandlers getLogoutButton() {
        return logoutButton;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.message.ErrorSessionMessageDisplay#getCancelButton()
     */
    @Override
    public HasClickHandlers getCancelButton() {
        return cancelButton;
    }
}