/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.common.tab;

import net.customware.gwt.presenter.client.widget.WidgetDisplay;

/**
 * Display interface for the tab element.
 * 
 * @author tpanuel
 */
public interface TabElementDisplay extends WidgetDisplay {
    /**
     * Get the tab title.
     * 
     * @return The tab title.
     */
    public String getTabTitle();
}