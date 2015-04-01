/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.shared.command.mail;

import java.util.List;

import org.topcased.gpm.ui.application.shared.command.AbstractCommandAction;

/**
 * GetMailingInfoAction
 * 
 * @author nveillet
 */
public class GetMailingInfoAction extends
        AbstractCommandAction<GetMailingInfoResult> {

    /** serialVersionUID */
    private static final long serialVersionUID = 6099643709972105075L;

    private List<String> sheetIds;

    /**
     * create action
     */
    public GetMailingInfoAction() {
    }

    /**
     * create action with values
     * 
     * @param pProductName
     *            the product name
     * @param pSheetIds
     *            the sheet identifiers
     */
    public GetMailingInfoAction(String pProductName, List<String> pSheetIds) {
        super(pProductName);
        sheetIds = pSheetIds;
    }

    /**
     * get sheet identifiers
     * 
     * @return the sheet identifiers
     */
    public List<String> getSheetIds() {
        return sheetIds;
    }

    /**
     * set sheet identifiers
     * 
     * @param pSheetIds
     *            the sheet identifiers to set
     */
    public void setSheetIds(List<String> pSheetIds) {
        sheetIds = pSheetIds;
    }
}
