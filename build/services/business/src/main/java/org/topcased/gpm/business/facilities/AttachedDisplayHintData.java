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
package org.topcased.gpm.business.facilities;

/**
 * @author Atos
 */
public class AttachedDisplayHintData implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public AttachedDisplayHintData() {
    }

    /**
     * Constructor taking all properties.
     */
    public AttachedDisplayHintData(final int pWidth, final int pHeight,
            final String pDisplayType) {
        this.width = pWidth;
        this.height = pHeight;
        this.displayType = pDisplayType;
    }

    /**
     * Copies constructor from other AttachedDisplayHintData
     */
    public AttachedDisplayHintData(AttachedDisplayHintData pOtherBean) {
        if (pOtherBean != null) {
            this.width = pOtherBean.getWidth();
            this.height = pOtherBean.getHeight();
            this.displayType = pOtherBean.getDisplayType();
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

    private String displayType;

    /**
     * 
     */
    public String getDisplayType() {
        return this.displayType;
    }

    public void setDisplayType(String pDisplayType) {
        this.displayType = pDisplayType;
    }

}