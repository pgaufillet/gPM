/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.menu;

import org.topcased.gpm.ui.component.client.resources.ComponentResources;

import com.google.gwt.user.client.ui.HTML;

/**
 * @author tpanuel
 */
public class GpmMenuTitle extends HTML {
    public final static String EMPTY = "&nbsp;";

    /**
     * Create an empty title for the a menu.
     * 
     * @param pMainTitle
     *            If it's the main title.
     */
    public GpmMenuTitle(final boolean pMainTitle) {
        this(EMPTY, pMainTitle);
    }

    /**
     * Create a title for the a menu.
     * 
     * @param pTitle
     *            The title.
     * @param pMainTitle
     *            If it's the main title.
     */
    public GpmMenuTitle(final String pTitle, final boolean pMainTitle) {
        super(pTitle);
        if (pMainTitle) {
            setStyleName(ComponentResources.INSTANCE.css().gpmMenuTitle());
        }
        else {
            setStyleName(ComponentResources.INSTANCE.css().gpmMenuSubTitle());
        }
    }
}