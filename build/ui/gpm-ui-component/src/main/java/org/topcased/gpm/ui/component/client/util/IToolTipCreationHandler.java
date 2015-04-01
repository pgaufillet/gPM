/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien Eballard (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.util;

import org.topcased.gpm.ui.component.client.popup.GpmToolTipPanel;

/**
 * Interface for toolTip creation.
 * 
 * @author jeballar
 */
public interface IToolTipCreationHandler {

    /**
     * Fills the tooltip content before displaying it.
     * 
     * @param pToolTip
     *            The tooltip to fill
     */
    public void fillPopupContent(GpmToolTipPanel pToolTip);
}
