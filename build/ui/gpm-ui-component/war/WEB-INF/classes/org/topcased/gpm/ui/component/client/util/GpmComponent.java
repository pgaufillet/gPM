/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.util;

import com.google.gwt.user.client.ui.Widget;

/**
 * Interface that has the same utility as Component but withtout performance
 * problem.
 * 
 * @author tpanuel
 * @param <W>The type of Widget.
 */
public interface GpmComponent<W extends Widget> {
    /**
     * Get widget.
     * 
     * @return The widget.
     */
    public W getWidget();
}
