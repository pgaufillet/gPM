/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.common.workspace.detail;

import org.topcased.gpm.ui.application.client.common.container.ContainerDisplay;
import org.topcased.gpm.ui.application.client.common.tab.TabLayoutDisplay;
import org.topcased.gpm.ui.component.client.layout.IResizableLayoutPanel;

/**
 * Display interface for the DetailView.
 * 
 * @author tpanuel
 * @param <SD>
 *            The type of display of sub element.
 */
public interface DetailDisplay<SD extends ContainerDisplay> extends
        TabLayoutDisplay<SD>, IResizableLayoutPanel {
}