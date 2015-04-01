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

import java.util.List;

import org.topcased.gpm.ui.application.shared.command.AbstractCommandAction;
import org.topcased.gpm.ui.application.shared.command.filter.AbstractCommandFilterResult;

/**
 * DeleteSheetsAction
 * 
 * @author nveillet
 */
public class DeleteSheetsAction extends
        AbstractCommandAction<AbstractCommandFilterResult> {

    /** serialVersionUID */
    private static final long serialVersionUID = -4241677043672552377L;

    private String filterId;

    private List<String> sheetIds;

    /**
     * Create action
     */
    public DeleteSheetsAction() {
    }

    /**
     * Create action with values
     * 
     * @param pProductName
     *            the product name
     * @param pSheetIds
     *            the sheets identifiers
     * @param pFilterId
     *            the filter identifier
     */
    public DeleteSheetsAction(String pProductName, List<String> pSheetIds,
            String pFilterId) {
        super(pProductName);
        sheetIds = pSheetIds;
        filterId = pFilterId;
    }

    /**
     * get filter identifier
     * 
     * @return the filter identifier
     */
    public String getFilterId() {
        return filterId;
    }

    /**
     * get sheets identifiers
     * 
     * @return the sheets identifiers
     */
    public List<String> getSheetIds() {
        return sheetIds;
    }

    /**
     * set filter identifier
     * 
     * @param pFilterId
     *            the filter identifier to set
     */
    public void setFilterId(String pFilterId) {
        filterId = pFilterId;
    }

    /**
     * set sheets identifiers
     * 
     * @param pSheetIds
     *            the sheets identifiers to set
     */
    public void setSheetIds(List<String> pSheetIds) {
        sheetIds = pSheetIds;
    }
}
