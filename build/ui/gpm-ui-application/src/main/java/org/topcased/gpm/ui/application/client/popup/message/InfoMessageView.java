/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.popup.message;

import static org.topcased.gpm.ui.component.client.resources.ComponentResources.INSTANCE;
import static org.topcased.gpm.ui.component.client.resources.i18n.Ui18n.CONSTANTS;

import org.topcased.gpm.ui.application.client.popup.PopupView;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ScrollPanel;

/**
 * View for display an info.
 * 
 * @author tpanuel
 */
public class InfoMessageView extends PopupView implements InfoMessageDisplay {
    private final static double RATIO_WIDTH = 0.4;

    private final static double RATIO_HEIGHT = 0.2;

    private final HTML infoMessage;

    private final Button okButton;

    /**
     * Create a send sheet(s) by mail view.
     */
    public InfoMessageView() {
        super(CONSTANTS.infoTitle());
        final ScrollPanel lPanel = new ScrollPanel();
        final DockPanel lDock = new DockPanel();
        final Image lImage = new Image(INSTANCE.images().info());

        // The error message
        infoMessage = new HTML();
        infoMessage.addStyleName(INSTANCE.css().gpmBigBorder());

        // Add image
        lImage.addStyleName(INSTANCE.css().gpmBigBorder());
        lDock.addStyleName(INSTANCE.css().gpmBigBorder());
        lDock.add(lImage, DockPanel.WEST);
        lDock.add(infoMessage, DockPanel.CENTER);

        lPanel.addStyleName(INSTANCE.css().gpmSmallBorder());
        lPanel.setWidget(lDock);

        setContent(lPanel);

        okButton = addButton(CONSTANTS.ok());

        setRatioSize(RATIO_WIDTH, RATIO_HEIGHT);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.message.InfoMessageDisplay#setInfoMessage(java.lang.String)
     */
    @Override
    public void setInfoMessage(final String pInfoMessage) {
        infoMessage.setHTML(pInfoMessage);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.message.ErrorMessageDisplay#getOkButton()
     */
    @Override
    public HasClickHandlers getOkButton() {
        return okButton;
    }
}