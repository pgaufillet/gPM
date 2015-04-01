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

import java.util.List;

import org.topcased.gpm.business.util.DisplayMode;

/**
 * UiSheetsEAResult
 * 
 * @author nveillet
 */
public class UiSheetsEAResult extends AbstractUiExtendedActionResult {

    private DisplayMode displayMode;

    private List<String> sheetIds;

    /**
     * Constructor with values
     * 
     * @param pSheetIds
     *            the sheet identifiers
     * @param pDisplayMode
     *            the display mode
     */
    public UiSheetsEAResult(List<String> pSheetIds, DisplayMode pDisplayMode) {
        super();
        sheetIds = pSheetIds;
        displayMode = pDisplayMode;
    }

    /**
     * get display mode
     * 
     * @return the display mode
     */
    public DisplayMode getDisplayMode() {
        return displayMode;
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
     * set display mode
     * 
     * @param pDisplayMode
     *            the display mode to set
     */
    public void setDisplayMode(DisplayMode pDisplayMode) {
        displayMode = pDisplayMode;
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
