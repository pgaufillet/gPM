/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Sophian Idjellidaine (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.serialization.data;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * Text display hint.
 * 
 * @author llatil
 */
@XStreamAlias("textDisplayHint")
public class TextDisplayHint extends DisplayHint {

    private static final long serialVersionUID = -3248312086641903999L;

    /** The width. */
    @XStreamAsAttribute
    private int width;

    /** The height. */
    @XStreamAsAttribute
    private int height;

    /** The multiline. */
    @XStreamAsAttribute
    private boolean multiline;

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
     * Specify if the text widget is multiline or not.
     * 
     * @return True for a multiline widget.
     */
    public boolean isMultiline() {
        return multiline;
    }

    /**
     * Get the displayType (url...)
     * 
     * @return the display type
     */
    public String getDisplayType() {
        if (null == displayType) {
            if (isMultiline()) {
                return "MULTI_LINE";
            }
            // else
            return "SINGLE_LINE";
        }
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
     * Sets the multiline.
     * 
     * @param pMultiline
     *            the new multiline
     */
    public void setMultiline(boolean pMultiline) {
        multiline = pMultiline;
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
