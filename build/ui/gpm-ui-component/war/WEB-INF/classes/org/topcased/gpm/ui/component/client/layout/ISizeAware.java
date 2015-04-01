/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: ROSIER Florian (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.layout;

import com.google.gwt.user.client.ui.Widget;

/**
 * A SizeAware component represents an object that knows its dimensions, height
 * and width.
 * <p>
 * It permits to impose Layout components to describe their behavior as regards
 * their size. <br />
 * A minimum size, implemented by getMinHeight() and getMinWidth() allows the
 * component to keep those dimensions in case of resizing.
 * </p>
 * 
 * @author frr
 */
public interface ISizeAware {

    /**
     * Get the height of the component.
     * 
     * @return The height.
     */
    public int getHeight();

    /**
     * Get the width of the component.
     * 
     * @return The width.
     */
    public int getWidth();

    /**
     * Get the minimum height of the component.
     * 
     * @return The minimum height.
     */
    public int getMinHeight();

    /**
     * Get the minimum width of the component.
     * 
     * @return The minimum width.
     */
    public int getMinWidth();

    /**
     * Return the widget of the class (to bypass "Widget" GWT class which is not
     * an interface)
     * 
     * @return The widget represented by the class
     */
    public Widget asWidget();

}
