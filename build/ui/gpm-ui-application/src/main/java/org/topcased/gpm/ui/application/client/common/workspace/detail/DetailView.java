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
import org.topcased.gpm.ui.application.client.common.tab.TabLayoutView;

/**
 * View for a set of container displayed on the user space.
 * 
 * @author tpanuel
 * @param <SD>
 *            The type of display of sub element.
 */
public class DetailView<SD extends ContainerDisplay> extends TabLayoutView<SD>
        implements DetailDisplay<SD> {
    /**
     * Create a detail view for containers.
     */
    public DetailView() {
        super(false);
    }
}