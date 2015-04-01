/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.popup.filter;

import java.util.List;

import org.topcased.gpm.business.util.search.FilterResult;
import org.topcased.gpm.ui.application.client.popup.PopupDisplay;

import com.google.gwt.event.dom.client.ClickHandler;

/**
 * Display interface for the FilterView.
 * 
 * @author tpanuel
 */
public interface FilterPopupDisplay extends PopupDisplay {
    /**
     * Initialize or reset the table.
     */
    public void initTable();

    /**
     * Add a column.
     * 
     * @param pColumnName
     *            The name of the column.
     */
    public void addValuesColumn(String pColumnName);

    /**
     * Set data.
     * 
     * @param pData
     *            The data.
     */
    public void setData(List<FilterResult> pData);

    /**
     * Set if the table is multivalued.
     * 
     * @param pMultivalued
     *            If the table is multivalued.
     */
    public void setMultivalued(boolean pMultivalued);

    /**
     * Set information for the validation button.
     * 
     * @param pButtonText
     *            The button text.
     * @param pClickHandler
     *            The click handler on the button.
     */
    public void setValidationButtonInfo(String pButtonText,
            ClickHandler pClickHandler);

    /**
     * Set information for the update filter button.
     * 
     * @param pClickHandler
     *            The click handler on the button.
     */
    public void setUpdateFilterButtonInfo(ClickHandler pClickHandler);

    /**
     * Get the selected elements id.
     * 
     * @return The selected elements id.
     */
    public List<String> getSelectedElementIds();
}