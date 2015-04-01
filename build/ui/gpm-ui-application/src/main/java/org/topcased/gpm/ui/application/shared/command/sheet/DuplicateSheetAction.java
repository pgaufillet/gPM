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
 * DuplicateSheetAction
 * 
 * @author nveillet
 */
public class DuplicateSheetAction extends AbstractCommandAction<GetSheetResult> {
    /** serialVersionUID */
    private static final long serialVersionUID = -4323608498392921973L;

    private String sheetId;

    /**
     * Create action
     */
    public DuplicateSheetAction() {
    }

    /**
     * Create action with values
     * 
     * @param pProductName
     *            the product name
     * @param pSheetId
     *            the sheet identifier
     */
    public DuplicateSheetAction(String pProductName, String pSheetId) {
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
     * set sheet identifier
     * 
     * @param pSheetId
     *            the sheet identifier to set
     */
    public void setSheetId(String pSheetId) {
        sheetId = pSheetId;
    }
}