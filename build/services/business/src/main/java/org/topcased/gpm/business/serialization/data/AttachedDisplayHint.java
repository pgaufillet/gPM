/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Anne Haugommard (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.serialization.data;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * Attached file display hint.
 * 
 * @author ahaugommard
 */
@XStreamAlias("attachedDisplayHint")
public class AttachedDisplayHint extends DisplayHint {

    /** serialVersionUID */
    private static final long serialVersionUID = 5879564919332930176L;

    /** The width. */
    @XStreamAsAttribute
    private int width;

    /** The height. */
    @XStreamAsAttribute
    private int height;

    /** The display type. */
    @XStreamAsAttribute
    private String displayType;

    /**
     * Get the preferred height for the widget displaying this field.
     * 
     * @return Height of the widget (in lines)
     */
    public int getHeight() {
        return height;
    }

    /**
     * Get the preferred width for the widget displaying this field.
     * 
     * @return Width of the widget
     */
    public int getWidth() {
        return width;
    }

    /**
     * Get the displayType (image...)
     * 
     * @return the display type
     */
    public String getDisplayType() {
        return displayType;
    }

    /**
     * Sets the width.
     * 
     * @param pWidth
     *            the new width
     */
    public void setWidth(int pWidth) {
        width = pWidth;
    }

    /**
     * Sets the height.
     * 
     * @param pHeight
     *            the new height
     */
    public void setHeight(int pHeight) {
        height = pHeight;
    }

    /**
     * Sets the display type.
     * 
     * @param pDisplayType
     *            the new display type
     */
    public void setDisplayType(String pDisplayType) {
        displayType = pDisplayType;
    }
}
