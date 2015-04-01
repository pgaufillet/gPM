/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 ******************************************************************/
package org.topcased.gpm.business.link.service;

import org.topcased.gpm.business.link.impl.CacheableLinkType;

/**
 * Old link type data structure
 * 
 * @author generated
 * @deprecated
 * @since 1.8.3
 * @see CacheableLinkType
 */
public class LinkTypeData implements java.io.Serializable {
    private static final long serialVersionUID = 5848649761493897793L;

    /**
     * Default constructor.
     */
    public LinkTypeData() {
    }

    /**
     * Constructor taking all properties.
     */
    public LinkTypeData(final String pId, final String pOriginType,
            final String pDestinationType, final int pLowBound,
            final int pHighBound, final String pName,
            final String pDescription, final boolean pUnidirectionalCreation,
            final boolean pUnidirectionalNavigation) {
        id = pId;
        originType = pOriginType;
        destinationType = pDestinationType;
        lowBound = pLowBound;
        highBound = pHighBound;
        name = pName;
        description = pDescription;
        unidirectionalCreation = pUnidirectionalCreation;
        unidirectionalNavigation = pUnidirectionalNavigation;
    }

    /**
     * Copies constructor from other LinkTypeData
     */
    public LinkTypeData(LinkTypeData pOtherBean) {
        if (pOtherBean != null) {
            id = pOtherBean.getId();
            originType = pOtherBean.getOriginType();
            destinationType = pOtherBean.getDestinationType();
            lowBound = pOtherBean.getLowBound();
            highBound = pOtherBean.getHighBound();
            name = pOtherBean.getName();
            description = pOtherBean.getDescription();
            unidirectionalCreation = pOtherBean.isUnidirectionalCreation();
            unidirectionalNavigation = pOtherBean.isUnidirectionalNavigation();
        }
    }

    private java.lang.String id;

    /**
     * 
     */
    public String getId() {
        return id;
    }

    public void setId(final String pId) {
        id = pId;
    }

    private String originType;

    /**
     * 
     */
    public String getOriginType() {
        return originType;
    }

    public void setOriginType(final String pOriginType) {
        originType = pOriginType;
    }

    private String destinationType;

    /**
     * 
     */
    public String getDestinationType() {
        return destinationType;
    }

    public void setDestinationType(final String pDestinationType) {
        destinationType = pDestinationType;
    }

    private int lowBound;

    /**
     * 
     */
    public int getLowBound() {
        return lowBound;
    }

    public void setLowBound(final int pLowBound) {
        lowBound = pLowBound;
    }

    private int highBound;

    /**
     * 
     */
    public int getHighBound() {
        return highBound;
    }

    public void setHighBound(final int pHighBound) {
        highBound = pHighBound;
    }

    private String name;

    /**
     * 
     */
    public String getName() {
        return name;
    }

    public void setName(final String pName) {
        name = pName;
    }

    private String description;

    /**
     * <p>
     * Translated value of description if exists. The description non translated
     * value otherwise.
     * </p>
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(final String pDescription) {
        description = pDescription;
    }

    private boolean unidirectionalCreation;

    /**
     * 
     */
    public boolean isUnidirectionalCreation() {
        return unidirectionalCreation;
    }

    public void setUnidirectionalCreation(final boolean pUnidirectionalCreation) {
        unidirectionalCreation = pUnidirectionalCreation;
    }

    private boolean unidirectionalNavigation;

    /**
     * 
     */
    public boolean isUnidirectionalNavigation() {
        return unidirectionalNavigation;
    }

    public void setUnidirectionalNavigation(
            final boolean pUnidirectionalNavigation) {
        unidirectionalNavigation = pUnidirectionalNavigation;
    }
}