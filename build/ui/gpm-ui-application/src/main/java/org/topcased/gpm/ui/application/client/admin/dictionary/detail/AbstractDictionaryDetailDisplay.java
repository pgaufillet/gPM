/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.admin.dictionary.detail;

import net.customware.gwt.presenter.client.widget.WidgetDisplay;

import org.topcased.gpm.ui.component.client.layout.IResizableLayoutPanel;
import org.topcased.gpm.ui.component.client.menu.GpmToolBar;

/**
 * Display interface for the AbstractDictionaryDetailView.
 * 
 * @author nveillet
 */
public interface AbstractDictionaryDetailDisplay extends WidgetDisplay,
        IResizableLayoutPanel {

    /**
     * Clear the view
     */
    public void clear();

    /**
     * Set the category name
     * 
     * @param pName
     *            the name
     */
    void setName(String pName);

    /**
     * Set the tool bar.
     * 
     * @param pToolBar
     *            The tool bar.
     */
    public void setToolBar(final GpmToolBar pToolBar);
}
