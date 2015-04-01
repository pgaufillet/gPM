/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Florian ROSIER (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.field;

/**
 * IHasSize defines the behavior of fields that you can set size.
 * 
 * @author frosier
 */
public interface IHasSize {

    /**
     * Set the height.
     * 
     * @param pHeight
     *            The height.
     */
    public void setHeight(final String pHeight);

    /**
     * Set the width.
     * 
     * @param pWidth
     *            The width.
     */
    public void setWidth(final String pWidth);
}
