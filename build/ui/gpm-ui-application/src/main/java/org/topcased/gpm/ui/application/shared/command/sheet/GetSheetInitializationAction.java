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
 * GetSheetInitializationAction
 * 
 * @author nveillet
 */
public class GetSheetInitializationAction extends
        AbstractCommandAction<GetSheetResult> {

    /** serialVersionUID */
    private static final long serialVersionUID = 5246138760125643450L;

    private String sheetId;

    private String sourceSheetId;

    /**
     * Create action
     */
    public GetSheetInitializationAction() {
    }

    /**
     * Create action with values
     * 
     * @param pProductName
     *            the product name
     * @param pSheetId
     *            the current sheet identifier
     * @param pSourceSheetId
     *            the source sheet identifier
     */
    public GetSheetInitializationAction(String pProductName, String pSheetId,
            String pSourceSheetId) {
        super(pProductName);
        sheetId = pSheetId;
        sourceSheetId = pSourceSheetId;
    }

    /**
     * get current sheet identifier
     * 
     * @return the current sheet identifier
     */
    public String getSheetId() {
        return sheetId;
    }

    /**
     * get source sheet identifier
     * 
     * @return the source sheet identifier
     */
    public String getSourceSheetId() {
        return sourceSheetId;
    }

    /**
     * set current sheet identifier
     * 
     * @param pSheetId
     *            the current sheet identifier to set
     */
    public void setSheetId(String pSheetId) {
        sheetId = pSheetId;
    }

    /**
     * set source sheet identifier
     * 
     * @param pSourceSheetId
     *            the source sheet identifier to set
     */
    public void setSourceSheetId(String pSourceSheetId) {
        sourceSheetId = pSourceSheetId;
    }
}
