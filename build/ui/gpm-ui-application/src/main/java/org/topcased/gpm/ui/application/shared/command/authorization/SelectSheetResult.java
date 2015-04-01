/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael KARGBO (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.shared.command.authorization;

import org.topcased.gpm.ui.application.shared.command.sheet.GetSheetResult;

/**
 * Sheet preselection result
 * 
 * @author mkargbo
 */
public class SelectSheetResult extends AbstractConnectionResult {

    /** serialVersionUID */
    private static final long serialVersionUID = -949942437140029520L;

    private GetSheetResult result;

    /**
     * Default constructor
     */
    public SelectSheetResult() {

    }

    /**
     * Constructor
     * 
     * @param pResult
     *            Sheet as result
     */
    public SelectSheetResult(GetSheetResult pResult) {
        result = pResult;
    }

    public GetSheetResult getResult() {
        return result;
    }

    public void setResult(GetSheetResult pResult) {
        result = pResult;
    }
}