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

import static org.topcased.gpm.ui.component.client.resources.i18n.Ui18n.CONSTANTS;

import java.util.List;

import org.topcased.gpm.ui.application.client.popup.PopupView;
import org.topcased.gpm.ui.component.client.container.GpmDisclosurePanel;
import org.topcased.gpm.ui.component.client.resources.ComponentResources;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ScrollPanel;

/**
 * View for display an error.
 * 
 * @author tpanuel
 */
public class ErrorMessageView extends PopupView implements ErrorMessageDisplay {
    private final static double RATIO_WIDTH = 0.4;

    private final static double RATIO_HEIGHT = 0.2;

    private final static double RATIO_WIDTH_WITH_STACK = 0.5;

    private final static double RATIO_HEIGHT_WITH_STACK = 0.4;

    private final HTML errorMessage;

    private final GpmDisclosurePanel stackTrace;

    private final HTML stackTraceText;

    private final Button okButton;

    /**
     * Create a send sheet(s) by mail view.
     */
    public ErrorMessageView() {
        super(CONSTANTS.errorTitle());
        final ScrollPanel lPanel = new ScrollPanel();
        final DockPanel lDock = new DockPanel();
        final Image lImage =
                new Image(ComponentResources.INSTANCE.images().error());

        // The error message
        errorMessage = new HTML();
        errorMessage.addStyleName(ComponentResources.INSTANCE.css().gpmBigBorder());

        // The stack trace
        stackTraceText = new HTML();
        stackTrace = new GpmDisclosurePanel();
        stackTrace.setContent(stackTraceText);
        stackTrace.setButtonText(CONSTANTS.errorStackTrace());
        stackTrace.close();
        stackTrace.setVisible(false);

        // Add image
        lImage.addStyleName(ComponentResources.INSTANCE.css().gpmBigBorder());
        lDock.addStyleName(ComponentResources.INSTANCE.css().gpmBigBorder());
        lDock.add(lImage, DockPanel.WEST);
        lDock.add(stackTrace, DockPanel.SOUTH);
        lDock.add(errorMessage, DockPanel.CENTER);

        lPanel.addStyleName(ComponentResources.INSTANCE.css().gpmSmallBorder());
        lPanel.setWidget(lDock);

        setContent(lPanel);

        okButton = addButton(CONSTANTS.ok());

        setRatioSize(RATIO_WIDTH, RATIO_HEIGHT);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.message.ErrorMessageDisplay#setErrorMessage(java.lang.String)
     */
    @Override
    public void setErrorMessage(final String pErrorMessage) {
        errorMessage.setHTML(pErrorMessage);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.message.ErrorMessageDisplay#setStackTrace(java.lang.StackTraceElement[])
     */
    @Override
    public void setStackTrace(final List<String> pStackTrace) {
        final StringBuilder lMessage = new StringBuilder();

        for (String lElement : pStackTrace) {
            lMessage.append(lElement).append("\n");
        }
        stackTraceText.setHTML(lMessage.toString());
        stackTrace.setVisible(true);
        setRatioSize(RATIO_WIDTH_WITH_STACK, RATIO_HEIGHT_WITH_STACK);
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