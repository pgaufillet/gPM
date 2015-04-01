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

import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ScrollPanel;

/**
 * View for display an uploading.
 * 
 * @author tpanuel
 */
public class UploadMessageView extends PopupView implements
        UploadMessageDisplay {
    private final static double RATIO_WIDTH = 0.4;

    private final static double RATIO_HEIGHT = 0.15;

    private final HTML fileName;

    /**
     * Create a view to display a question.
     */
    public UploadMessageView() {
        // Cannot be close but modal
        super(false);
        setAutoHideEnabled(false);
        enableGlass();

        final ScrollPanel lPanel = new ScrollPanel();
        final DockPanel lDock = new DockPanel();
        final Image lImage = new Image(INSTANCE.images().loading());

        // The header title
        setHeaderText(CONSTANTS.uploadTitle());

        // The file name
        fileName = new HTML();
        fileName.addStyleName(INSTANCE.css().gpmBigBorder());

        // Add image
        lImage.addStyleName(INSTANCE.css().gpmBigBorder());
        lDock.addStyleName(INSTANCE.css().gpmBigBorder());
        lDock.add(lImage, DockPanel.WEST);
        lDock.add(fileName, DockPanel.CENTER);

        lPanel.addStyleName(INSTANCE.css().gpmSmallBorder());
        lPanel.setWidget(lDock);

        setContent(lPanel);

        setRatioSize(RATIO_WIDTH, RATIO_HEIGHT);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.message.UploadMessageDisplay#setFileName(java.lang.String)
     */
    @Override
    public void setFileName(final String pFileName) {
        fileName.setHTML(CONSTANTS.uploadText() + pFileName);
    }
}