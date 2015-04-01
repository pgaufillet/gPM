/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.popup.attribute;

import java.util.List;

import org.topcased.gpm.ui.application.client.popup.PopupDisplay;

/**
 * Display interface for the AttributesVisualizationView.
 * 
 * @author nveillet
 */
public interface AttributesVisualizationDisplay extends PopupDisplay {

    /**
     * Add a attribute
     * 
     * @param pName
     *            the name
     * @param pValues
     *            the values
     */
    public void addAttribute(String pName, List<String> pValues);

    /**
     * Clear the view
     */
    public void clear();
}
