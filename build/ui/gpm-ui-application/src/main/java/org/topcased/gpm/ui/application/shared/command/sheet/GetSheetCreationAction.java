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
 * GetSheetCreationAction
 * 
 * @author nveillet
 */
public class GetSheetCreationAction extends
        AbstractCommandAction<GetSheetResult> {

    /** serialVersionUID */
    private static final long serialVersionUID = 1236826450839489947L;

    private String sheetTypeName;

    /**
     * create action
     */
    public GetSheetCreationAction() {
        super();
    }

    /**
     * create action with sheet type name
     * 
     * @param pProductName
     *            the product name
     * @param pSheetTypeName
     *            the sheet type name
     */
    public GetSheetCreationAction(String pProductName, String pSheetTypeName) {
        super(pProductName);
        sheetTypeName = pSheetTypeName;
    }

    /**
     * get sheet type name
     * 
     * @return the sheet type name
     */
    public String getSheetTypeName() {
        return sheetTypeName;
    }

    /**
     * set sheet type name
     * 
     * @param pSheetTypeName
     *            the sheet type name to set
     */
    public void setSheetTypeName(String pSheetTypeName) {
        sheetTypeName = pSheetTypeName;
    }
}
