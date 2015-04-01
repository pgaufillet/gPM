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
import org.topcased.gpm.ui.application.shared.command.filter.AbstractCommandFilterResult;
import org.topcased.gpm.ui.facade.shared.filter.UiFilter;

/**
 * ExecuteSheetInitializationFilterAction
 * 
 * @author nveillet
 */
public class ExecuteSheetInitializationFilterAction extends
        AbstractCommandAction<AbstractCommandFilterResult> {

    /** serialVersionUID */
    private static final long serialVersionUID = -5052899144628563476L;

    private UiFilter filter;

    private String sheetId;

    private String sheetTypeName;

    /**
     * Create action
     */
    public ExecuteSheetInitializationFilterAction() {
    }

    /**
     * Create action without specific filter
     * 
     * @param pProductName
     *            the product name
     * @param pSheetId
     *            the sheet identifier
     * @param pSheetTypeName
     *            the sheet type name
     */
    public ExecuteSheetInitializationFilterAction(String pProductName,
            String pSheetId, String pSheetTypeName) {
        super(pProductName);
        sheetId = pSheetId;
        sheetTypeName = pSheetTypeName;
    }

    /**
     * Create action with specific filter
     * 
     * @param pProductName
     *            the product name
     * @param pSheetId
     *            the sheet identifier
     * @param pSheetTypeName
     *            the sheet type name
     * @param pFilter
     *            the filter
     */
    public ExecuteSheetInitializationFilterAction(String pProductName,
            String pSheetId, String pSheetTypeName, UiFilter pFilter) {
        super(pProductName);
        sheetId = pSheetId;
        sheetTypeName = pSheetTypeName;
        filter = pFilter;
    }

    /**
     * get filter
     * 
     * @return the filter
     */
    public UiFilter getFilter() {
        return filter;
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
     * get sheet type name
     * 
     * @return the sheet type name
     */
    public String getSheetTypeName() {
        return sheetTypeName;
    }

    /**
     * set filter
     * 
     * @param pFilter
     *            the filter to set
     */
    public void setFilter(UiFilter pFilter) {
        filter = pFilter;
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
     * set sheet type name
     * 
     * @param pSheetTypeName
     *            the sheet type name to set
     */
    public void setSheetTypeName(String pSheetTypeName) {
        sheetTypeName = pSheetTypeName;
    }

}
