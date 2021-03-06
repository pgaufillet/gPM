/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien Eballard (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.popup;

/**
 * GpmToolTipPanel
 * 
 * @author jeballar
 */
public final class GpmToolTipPanel extends GpmToolTipPopupPanel {

    /**
     * Constructor
     */
    public GpmToolTipPanel() {
        super();
        setAutoHideEnabled(true);
        setGlassEnabled(false);
        setModal(false);
    }
}
