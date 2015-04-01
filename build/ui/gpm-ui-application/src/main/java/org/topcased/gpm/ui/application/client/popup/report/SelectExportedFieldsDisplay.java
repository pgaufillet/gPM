/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.popup.report;

import java.util.List;

import org.topcased.gpm.ui.application.client.popup.PopupDisplay;
import org.topcased.gpm.ui.facade.shared.export.UiExportableField;

import com.google.gwt.event.dom.client.ClickHandler;

/**
 * Display interface for the SelectExportedFieldsView.
 * 
 * @author tpanuel
 */
public interface SelectExportedFieldsDisplay extends PopupDisplay {
    /**
     * Get the selected field names.
     * 
     * @return The selected field names.
     */
    public List<String> getSelectedFieldNames();

    /**
     * Add a group of exportable field.
     * 
     * @param pGroupName
     *            The group name.
     * @param pFields
     *            The fields.
     */
    public void addGroupField(String pGroupName, List<UiExportableField> pFields);

    /**
     * Reset the popup.
     */
    public void reset();

    /**
     * Set the export button handler.
     * 
     * @param pHandler
     *            The handler.
     */
    public void setExportButtonHandler(ClickHandler pHandler);
}