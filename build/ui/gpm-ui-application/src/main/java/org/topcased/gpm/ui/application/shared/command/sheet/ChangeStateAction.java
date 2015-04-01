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
 * ChangeStateAction
 * 
 * @author nveillet
 */
public class ChangeStateAction extends AbstractCommandAction<GetSheetResult> {

    /** serialVersionUID */
    private static final long serialVersionUID = -4595859815537101200L;

    private String sheetId;

    private String transitionName;

    /**
     * Create action
     */
    public ChangeStateAction() {
    }

    /**
     * Create action with values
     * 
     * @param pProductName
     *            the product name
     * @param pSheetId
     *            the sheet identifier
     * @param pTransitionName
     *            the transition name
     */
    public ChangeStateAction(String pProductName, String pSheetId,
            String pTransitionName) {
        super(pProductName);
        sheetId = pSheetId;
        transitionName = pTransitionName;
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
     * get transition name
     * 
     * @return the transition name
     */
    public String getTransitionName() {
        return transitionName;
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

    /**
     * set transition name
     * 
     * @param pTransitionName
     *            the transition name to set
     */
    public void setTransitionName(String pTransitionName) {
        transitionName = pTransitionName;
    }

}
