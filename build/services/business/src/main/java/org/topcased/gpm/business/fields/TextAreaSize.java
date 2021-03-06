/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 ******************************************************************/
//
// Attention: Generated code! Do not modify by hand!
// Generated by: ValueObject.vsl in andromda-java-cartridge.
//
package org.topcased.gpm.business.fields;

/**
 * @author Atos
 */
public class TextAreaSize implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public TextAreaSize() {
    }

    /**
     * Constructor taking all properties.
     */
    public TextAreaSize(final int pWidth, final int pHeight) {
        this.width = pWidth;
        this.height = pHeight;
    }

    /**
     * Copies constructor from other TextAreaSize
     */
    public TextAreaSize(TextAreaSize pOtherBean) {
        if (pOtherBean != null) {
            this.width = pOtherBean.getWidth();
            this.height = pOtherBean.getHeight();
        }
    }

    private int width;

    /**
     * 
     */
    public int getWidth() {
        return this.width;
    }

    public void setWidth(int pWidth) {
        this.width = pWidth;
    }

    private int height;

    /**
     * 
     */
    public int getHeight() {
        return this.height;
    }

    public void setHeight(int pHeight) {
        this.height = pHeight;
    }

}