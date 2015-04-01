/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.shared.command.sheet;

import org.topcased.gpm.ui.application.shared.command.AbstractCommandAction;

/**
 * GetSheetEditionAction
 * 
 * @author nveillet
 */
public class GetSheetEditionAction extends
        AbstractCommandAction<GetSheetResult> {

    /** serialVersionUID */
    private static final long serialVersionUID = -1471420311714298358L;

    private boolean forceRefresh;

    private String sheetId;

    /**
     * create action
     */
    public GetSheetEditionAction() {
        super();
    }

    /**
     * create action with values
     * 
     * @param pProductName
     *            the product name
     * @param pSheetId
     *            the sheet identifier
     */
    public GetSheetEditionAction(String pProductName, String pSheetId) {
        super(pProductName);
        sheetId = pSheetId;
    }

    /**
     * get sheet identifier
     * 
     * @return the sheet identifier
     */
    public String getSheetId() {
        return sheetId;
    }

    /**
     * get forceRefresh
     * 
     * @return the forceRefresh
     */
    public boolean isForceRefresh() {
        return forceRefresh;
    }

    /**
     * set force refresh
     * 
     * @param pForceRefresh
     *            the force refresh to set
     */
    public void setForceRefresh(boolean pForceRefresh) {
        forceRefresh = pForceRefresh;
    }

    /**
     * set sheet identifier
     * 
     * @param pSheetId
     *            the sheet identifier to set
     */
    public void setSheetId(String pSheetId) {
        sheetId = pSheetId;
    }
}
