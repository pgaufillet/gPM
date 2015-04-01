/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.common.container.sheet;

import java.util.List;

import org.topcased.gpm.ui.application.client.common.container.ContainerDisplay;
import org.topcased.gpm.ui.facade.shared.container.sheet.state.UiTransition;

/**
 * Display interface for the SheetView.
 * 
 * @author tpanuel
 */
public interface SheetDisplay extends ContainerDisplay {

    /**
     * Set sheet state.
     * 
     * @param pSheetState
     *            The sheet state.
     */
    public void setSheetState(String pSheetState);

    /**
     * Set the transition history for the sheet
     * 
     * @param pTransitions
     *            the transitions associated to the sheet
     */
    public void setTransitionHistory(List<UiTransition> pTransitions);
}