/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.shared.command.authorization;

import java.util.List;

import org.topcased.gpm.ui.application.shared.command.AbstractCommandAction;

/**
 * DisconnectProductAction
 * 
 * @author nveillet
 */
public class DisconnectProductAction extends
        AbstractCommandAction<DisconnectProductResult> {

    /** serialVersionUID */
    private static final long serialVersionUID = -841486887206319932L;

    private List<String> openedSheetIds;

    /**
     * create action
     */
    public DisconnectProductAction() {
    }

    /**
     * create action with values
     * 
     * @param pProductName
     *            the product name
     * @param pOpenedSheetIds
     *            the opened sheet identifiers
     */
    public DisconnectProductAction(String pProductName,
            List<String> pOpenedSheetIds) {
        super(pProductName);
        openedSheetIds = pOpenedSheetIds;
    }

    /**
     * get opened sheet identifiers
     * 
     * @return the opened sheet identifiers
     */
    public List<String> getOpenedSheetIds() {
        return openedSheetIds;
    }

    /**
     * set opened sheet identifiers
     * 
     * @param pOpenedSheetIds
     *            the opened sheet identifiers to set
     */
    public void setOpenedSheetIds(List<String> pOpenedSheetIds) {
        openedSheetIds = pOpenedSheetIds;
    }
}
