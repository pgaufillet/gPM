/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/

package org.topcased.gpm.business.link.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.fields.impl.CacheableFieldsContainer;
import org.topcased.gpm.business.serialization.data.DisplayGroup;
import org.topcased.gpm.business.serialization.data.Field;
import org.topcased.gpm.business.serialization.data.FieldRef;
import org.topcased.gpm.business.serialization.data.LinkType;

/**
 * The Class CacheableLinkType.
 * 
 * @author llatil
 */
public class CacheableLinkType extends CacheableFieldsContainer {

    /** serialVersionUID. */
    private static final long serialVersionUID = -1721934233658558031L;

    private static final String DEFAULT_DISPLAY_GROUP = "link.fields.title";

    /** The value for unlimited bound. */
    private static final int UNLIMITED_BOUND = -1;

    /** For links that can be created only from source type. */
    private boolean unidirectionalForCreation;

    /** For links that are visible only from source type. */
    private boolean unidirectionalForNavigation;

    /** Low bound value for the link. */
    private int lowBound = UNLIMITED_BOUND;

    /** High bound value for the link. */
    private int highBound = UNLIMITED_BOUND;

    /** Id of the origin type. */
    private String originTypeId;

    /** Id of the destination type. */
    private String destinationTypeId;

    /** Name of the origin type. */
    private String originTypeName;

    /** Name of the destination type. */
    private String destinationTypeName;

    /**
     * Default constructor for mutable /immutable switch
     */
    public CacheableLinkType() {
        super();
    }

    /**
     * Constructs a new cacheable link type.
     * 
     * @param pLinkTypeEntity
     *            the link type entity
     */
    public CacheableLinkType(
            org.topcased.gpm.domain.link.LinkType pLinkTypeEntity) {
        super(pLinkTypeEntity);

        originTypeId = pLinkTypeEntity.getOriginType().getId();
        destinationTypeId = pLinkTypeEntity.getDestType().getId();
        originTypeName = pLinkTypeEntity.getOriginType().getName();
        destinationTypeName = pLinkTypeEntity.getDestType().getName();

        unidirectionalForCreation = pLinkTypeEntity.isUnidirectionalCreation();
        unidirectionalForNavigation =
                pLinkTypeEntity.isUnidirectionalNavigation();

        if (pLinkTypeEntity.getHighBound() != null) {
            highBound = pLinkTypeEntity.getHighBound();
        }
        if (pLinkTypeEntity.getLowBound() != null) {
            lowBound = pLinkTypeEntity.getLowBound();
        }
        setDefaultLinkDisplayGroup();
    }

    /**
     * Check if this link has a defined low bound.
     * 
     * @return true if this link has a defined low bound.
     */
    public boolean hasLowBound() {
        return lowBound != UNLIMITED_BOUND;
    }

    /**
     * Check if this link has a defined high bound.
     * 
     * @return true if this link has a defined high bound.
     */
    public boolean hasHighBound() {
        return highBound != UNLIMITED_BOUND;
    }

    /**
     * Gets the low bound.
     * 
     * @return the low bound
     */
    public int getLowBound() {
        return lowBound;
    }

    /**
     * Set the lowBound
     * 
     * @param pLowBound
     *            the new lowBound
     */
    public void setLowBound(int pLowBound) {
        lowBound = pLowBound;
    }

    /**
     * Gets the high bound.
     * 
     * @return the high bound
     */
    public int getHighBound() {
        return highBound;
    }

    /**
     * Set the highBound
     * 
     * @param pHighBound
     *            the new highBound
     */
    public void setHighBound(int pHighBound) {
        highBound = pHighBound;
    }

    /**
     * Checks if is unidirectional for creation.
     * 
     * @return true, if is unidirectional for creation
     */
    public boolean isUnidirectionalForCreation() {
        return unidirectionalForCreation;
    }

    /**
     * Set the unidirectionalForCreation
     * 
     * @param pUnidirectionalForCreation
     *            the new unidirectionalForCreation
     */
    public void setUnidirectionalForCreation(boolean pUnidirectionalForCreation) {
        unidirectionalForCreation = pUnidirectionalForCreation;
    }

    /**
     * Checks if is unidirectional for navigation.
     * 
     * @return true, if is unidirectional for navigation
     */
    public boolean isUnidirectionalForNavigation() {
        return unidirectionalForNavigation;
    }

    /**
     * Set the unidirectionalForNavigation
     * 
     * @param pUnidirectionalForNavigation
     *            the new unidirectionalForNavigation
     */
    public void setUnidirectionalForNavigation(
            boolean pUnidirectionalForNavigation) {
        unidirectionalForNavigation = pUnidirectionalForNavigation;
    }

    /**
     * Gets the origin type id.
     * 
     * @return the origin type id
     */
    public String getOriginTypeId() {
        return originTypeId;
    }

    /**
     * Set the originTypeId
     * 
     * @param pOriginTypeId
     *            the new originTypeId
     */
    public void setOriginTypeId(String pOriginTypeId) {
        originTypeId = pOriginTypeId;
    }

    /**
     * Gets the destination type id.
     * 
     * @return the destination type id
     */
    public String getDestinationTypeId() {
        return destinationTypeId;
    }

    /**
     * Set the destinationTypeId
     * 
     * @param pDestinationTypeId
     *            the new destinationTypeId
     */
    public void setDestinationTypeId(String pDestinationTypeId) {
        destinationTypeId = pDestinationTypeId;
    }

    /**
     * Gets the origin type name.
     * 
     * @return the origin type name
     */
    public String getOriginTypeName() {
        return originTypeName;
    }

    /**
     * Set the originTypeName
     * 
     * @param pOriginTypeName
     *            the new originTypeName
     */
    public void setOriginTypeName(String pOriginTypeName) {
        originTypeName = pOriginTypeName;
    }

    /**
     * Gets the destination type name.
     * 
     * @return the destination type name
     */
    public String getDestinationTypeName() {
        return destinationTypeName;
    }

    /**
     * Set the destinationTypeName
     * 
     * @param pDestinationTypeName
     *            the new destinationTypeName
     */
    public void setDestinationTypeName(String pDestinationTypeName) {
        destinationTypeName = pDestinationTypeName;
    }

    /**
     * Marshal this object into the given LinkType object.
     * 
     * @param pSerializedLinkType
     *            the serialized link type
     */
    public void marshal(LinkType pSerializedLinkType) {
        super.marshal(pSerializedLinkType);
        pSerializedLinkType.setOriginType(getOriginTypeName());
        pSerializedLinkType.setDestinationType(getDestinationTypeName());
    }

    private void setDefaultLinkDisplayGroup() {
        Map<String, DisplayGroup> lDisplayGroupsMap =
                new LinkedHashMap<String, DisplayGroup>();
        org.topcased.gpm.business.serialization.data.DisplayGroup lDisplayGroup;
        lDisplayGroup =
                new org.topcased.gpm.business.serialization.data.DisplayGroup();
        lDisplayGroup.setName(DEFAULT_DISPLAY_GROUP);
        lDisplayGroup.setDescription(StringUtils.EMPTY);

        List<FieldRef> lFieldsRef = new ArrayList<FieldRef>();
        for (Field lField : getAllFields()) {
            lFieldsRef.add(new FieldRef(lField.getLabelKey()));
        }

        Comparator<FieldRef> lComparator = new Comparator<FieldRef>() {
            public int compare(FieldRef pO1, FieldRef pO2) {
                return pO1.getName().compareTo(pO2.getName());
            }

        };
        Collections.sort(lFieldsRef, lComparator);
        lDisplayGroup.setFields(lFieldsRef);

        lDisplayGroupsMap.put(lDisplayGroup.getName(), lDisplayGroup);
        setDisplayGroupsMap(lDisplayGroupsMap);
    }
}
