/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.shared.command.extendedaction;

import java.util.List;

import org.topcased.gpm.ui.application.shared.command.sheet.GetSheetResult;

/**
 * GetSheetsExtendedActionResult
 * 
 * @author nveillet
 */
public class GetSheetsExtendedActionResult extends
        AbstractExecuteExtendedActionResult {

    /** serialVersionUID */
    private static final long serialVersionUID = -869470914203017124L;

    private List<GetSheetResult> sheetResults;

    /**
     * Empty constructor for serialization.
     */
    public GetSheetsExtendedActionResult() {
    }

    /**
     * Create GetSheetsExtendedActionResult with values
     * 
     * @param pSheetResults
     *            the sheet results
     */
    public GetSheetsExtendedActionResult(List<GetSheetResult> pSheetResults) {
        super();
        sheetResults = pSheetResults;
    }

    /**
     * get sheet results
     * 
     * @return the sheet results
     */
    public List<GetSheetResult> getSheetResults() {
        return sheetResults;
    }
}
