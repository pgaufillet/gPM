/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.shared.extendedaction;

import org.topcased.gpm.ui.facade.shared.container.sheet.UiSheet;

/**
 * UiSheetCreationEAResult
 * 
 * @author nveillet
 */
public class UiSheetCreationEAResult extends AbstractUiExtendedActionResult {

    private UiSheet sheet;

    private String sheetTypeName;

    /**
     * Constructor with the sheet type name to create
     * 
     * @param pSheetTypeName
     *            the sheet type name
     */
    public UiSheetCreationEAResult(String pSheetTypeName) {
        super();
        sheetTypeName = pSheetTypeName;
    }

    /**
     * Constructor with the specific sheet to create
     * 
     * @param pSheet
     *            the sheet
     */
    public UiSheetCreationEAResult(UiSheet pSheet) {
        super();
        sheet = pSheet;
    }

    /**
     * get sheet
     * 
     * @return the sheet
     */
    public UiSheet getSheet() {
        return sheet;
    }

    /**
     * get sheetTypeName
     * 
     * @return the sheetTypeName
     */
    public String getSheetTypeName() {
        return sheetTypeName;
    }

    /**
     * set sheet
     * 
     * @param pSheet
     *            the sheet to set
     */
    public void setSheet(UiSheet pSheet) {
        sheet = pSheet;
    }

    /**
     * set sheetTypeName
     * 
     * @param pSheetTypeName
     *            the sheetTypeName to set
     */
    public void setSheetTypeName(String pSheetTypeName) {
        sheetTypeName = pSheetTypeName;
    }
}
