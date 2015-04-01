/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.admin.dictionary.listing;

import net.customware.gwt.presenter.client.widget.WidgetDisplay;

import org.topcased.gpm.ui.component.client.container.field.GpmListBox;
import org.topcased.gpm.ui.component.client.layout.IResizableLayoutPanel;
import org.topcased.gpm.ui.component.client.menu.GpmToolBar;

import com.google.gwt.event.dom.client.ChangeHandler;

/**
 * Display interface for the AbstractDictionaryListingView.
 * 
 * @author nveillet
 */
public interface AbstractDictionaryListingDisplay extends WidgetDisplay,
        IResizableLayoutPanel {

    /**
     * Clear the view
     */
    public void clear();

    /**
     * Get the category
     * 
     * @return the category
     */
    public GpmListBox getCategory();

    /**
     * Set the open category handler
     * 
     * @param pOpenCategory
     *            The handler to open a category.
     */
    void setCategoryHandler(ChangeHandler pOpenCategory);

    /**
     * Set the tool bar.
     * 
     * @param pToolBar
     *            The tool bar.
     */
    public void setToolBar(final GpmToolBar pToolBar);
}
