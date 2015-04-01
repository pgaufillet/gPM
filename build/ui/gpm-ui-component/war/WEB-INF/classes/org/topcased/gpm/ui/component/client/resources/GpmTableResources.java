/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.resources;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.CellTable.Resources;
import com.google.gwt.user.cellview.client.CellTable.Style;

/**
 * CSS resource for the gPM table.
 * 
 * @author tpanuel
 */
public class GpmTableResources implements Resources {
    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.cellview.client.CellTable.Resources#cellTableFooterBackground()
     */
    @Override
    public ImageResource cellTableFooterBackground() {
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.cellview.client.CellTable.Resources#cellTableHeaderBackground()
     */
    @Override
    public ImageResource cellTableHeaderBackground() {
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.cellview.client.CellTable.Resources#cellTableLoading()
     */
    @Override
    public ImageResource cellTableLoading() {
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.cellview.client.CellTable.Resources#cellTableSelectedBackground()
     */
    @Override
    public ImageResource cellTableSelectedBackground() {
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.cellview.client.CellTable.Resources#cellTableStyle()
     */
    @Override
    public Style cellTableStyle() {
        return ComponentResources.INSTANCE.css();
    }
}