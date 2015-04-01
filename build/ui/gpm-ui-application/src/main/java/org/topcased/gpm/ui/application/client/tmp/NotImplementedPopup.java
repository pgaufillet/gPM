/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.tmp;

import org.topcased.gpm.ui.component.client.popup.GpmPopupPanel;

import com.google.gwt.user.client.ui.HTML;

/**
 * A popup for not implemented command.
 * 
 * @author tpanuel
 */
public class NotImplementedPopup extends GpmPopupPanel {
    private final static double RATIO_WIDTH = 0.6;

    private final static double RATIO_HEIGHT = 0.6;

    public NotImplementedPopup(final String pMessage) {
        super(true);
        setHeader(new HTML("Command not implemented"));
        setContent(new HTML(pMessage));
        setRatioSize(RATIO_WIDTH, RATIO_HEIGHT);
        center();
    }
}