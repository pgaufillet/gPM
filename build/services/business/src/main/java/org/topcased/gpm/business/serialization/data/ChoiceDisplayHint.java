/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Sophian Idjellidaine (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.serialization.data;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * Choice display hint.
 * 
 * @author llatil
 */
@XStreamAlias("choiceDisplayHint")
public class ChoiceDisplayHint extends DisplayHint {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 560802317084041717L;

    /** The list. */
    @XStreamAsAttribute
    private boolean list;

    /** The image type. */
    @XStreamAsAttribute
    private String imageType;

    /**
     * Specify the kind of widget to use to display this field.
     * 
     * @return True if the choice widget should be displayed as a list (or
     *         combobox)
     */
    public boolean isList() {
        return list;
    }

    /**
     * Specify the kind of widget to use to display this field.
     * 
     * @param pList
     *            True if the choice widget should be displayed as a list (or
     *            combobox)
     */
    public void setList(boolean pList) {
        list = pList;
    }

    /**
     * Get the displayType (LIST, IMAGE...)
     * 
     * @return the display type
     */
    public String getImageType() {
        return imageType;
    }

    /**
     * Specify the type of image to use to display this field.
     * 
     * @param pImageType
     *            IMAGE to display just image or IMAGE_TEXT to display image and
     *            text.
     */
    public void setImageType(String pImageType) {
        imageType = pImageType;
    }

}
