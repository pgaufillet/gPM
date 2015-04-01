/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.popup.csv;

import org.topcased.gpm.ui.application.client.popup.PopupDisplay;

import com.google.gwt.event.dom.client.ClickHandler;

/**
 * Display interface for the CsvOptionSelectionView.
 * 
 * @author tpanuel
 */
public interface CsvOptionSelectionDisplay extends PopupDisplay {
    /**
     * Add an handler on the export button.
     * 
     * @param pHandler
     *            The handler.
     */
    public void addExportButtonHandler(ClickHandler pHandler);

    /**
     * Reset the view.
     */
    public void reset();

    /**
     * Get the escape character.
     * 
     * @return The escape character.
     */
    public String getEscapeCharacter();

    /**
     * Get the quote character.
     * 
     * @return The quote character.
     */
    public String getQuoteCharacter();

    /**
     * Get the separator character.
     * 
     * @return The separator character.
     */
    public String getSeparatorCharacter();
}