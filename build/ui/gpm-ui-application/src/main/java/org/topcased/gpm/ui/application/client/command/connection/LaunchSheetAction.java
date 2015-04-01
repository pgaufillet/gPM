/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael KARGBO (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.command.connection;

import org.topcased.gpm.ui.application.client.event.EmptyAction;
import org.topcased.gpm.ui.application.shared.command.sheet.GetSheetResult;

/**
 * Launch a sheet.
 * 
 * @author mkargbo
 */
public class LaunchSheetAction extends EmptyAction<LaunchSheetAction> {

    /** serialVersionUID */
    private static final long serialVersionUID = -4839729466523229743L;

    private GetSheetResult sheetResult;

    /**
     * Default constructor
     */
    public LaunchSheetAction() {

    }

    /**
     * Constructor with GetSheetVisualizationResult
     * 
     * @param pSheetResult
     *            Sheet to launch
     */
    public LaunchSheetAction(GetSheetResult pSheetResult) {
        sheetResult = pSheetResult;
    }

    public GetSheetResult getSheetResult() {
        return sheetResult;
    }

    public void setConnectResult(GetSheetResult pSheetResult) {
        sheetResult = pSheetResult;
    }
}