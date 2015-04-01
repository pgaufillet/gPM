/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 ******************************************************************/
package org.topcased.gpm.business.link.service;

import org.topcased.gpm.business.fields.MultipleLineFieldData;
import org.topcased.gpm.business.link.impl.CacheableLink;

/**
 * Old link data structure
 * 
 * @author generated
 * @deprecated
 * @since 1.8.3
 * @see CacheableLink
 */
public class LinkData implements java.io.Serializable {
    private static final long serialVersionUID = 2715214515592912650L;

    /**
     * Default constructor.
     */
    public LinkData() {
    }

    /**
     * Constructor taking all properties.
     */
    public LinkData(final String pId, final String pOriginRef,
            final String pOriginType, final String pOriginId,
            final String pDestinationId, final String pDestinationRef,
            final String pDestinationType, final String pLinkTypeName,
            final String pLinkTypeId, final String[] pEnvironmentNames,
            final MultipleLineFieldData[] pMultipleLineFieldDatas) {
        id = pId;
        originRef = pOriginRef;
        originType = pOriginType;
        originId = pOriginId;
        destinationId = pDestinationId;
        destinationRef = pDestinationRef;
        destinationType = pDestinationType;
        linkTypeName = pLinkTypeName;
        linkTypeId = pLinkTypeId;
        environmentNames = pEnvironmentNames;
        multipleLineFieldDatas = pMultipleLineFieldDatas;
    }

    /**
     * Copies constructor from other LinkData
     */
    public LinkData(LinkData pOtherBean) {
        if (pOtherBean != null) {
            id = pOtherBean.getId();
            originRef = pOtherBean.getOriginRef();
            originType = pOtherBean.getOriginType();
            originId = pOtherBean.getOriginId();
            destinationId = pOtherBean.getDestinationId();
            destinationRef = pOtherBean.getDestinationRef();
            destinationType = pOtherBean.getDestinationType();
            linkTypeName = pOtherBean.getLinkTypeName();
            linkTypeId = pOtherBean.getLinkTypeId();
            environmentNames = pOtherBean.getEnvironmentNames();
            multipleLineFieldDatas = pOtherBean.getMultipleLineFieldDatas();
        }
    }

    private java.lang.String id;

    /**
     * <p>
     * Identifier of the sheet link.
     * </p>
     */
    public String getId() {
        return id;
    }

    public void setId(final String pId) {
        id = pId;
    }

    private String originRef;

    /**
     * 
     */
    public String getOriginRef() {
        return originRef;
    }

    public void setOriginRef(final String pOriginRef) {
        originRef = pOriginRef;
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

    private String originId;

    /**
     * 
     */
    public String getOriginId() {
        return originId;
    }

    public void setOriginId(final String pOriginId) {
        originId = pOriginId;
    }

    private String destinationId;

    /**
     * 
     */
    public String getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(final String pDestinationId) {
        destinationId = pDestinationId;
    }

    private String destinationRef;

    /**
     * 
     */
    public String getDestinationRef() {
        return destinationRef;
    }

    public void setDestinationRef(final String pDestinationRef) {
        destinationRef = pDestinationRef;
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

    private String linkTypeName;

    /**
     * 
     */
    public String getLinkTypeName() {
        return linkTypeName;
    }

    public void setLinkTypeName(final String pLinkTypeName) {
        linkTypeName = pLinkTypeName;
    }

    private String linkTypeId;

    /**
     * 
     */
    public String getLinkTypeId() {
        return linkTypeId;
    }

    public void setLinkTypeId(final String pLinkTypeId) {
        linkTypeId = pLinkTypeId;
    }

    private String[] environmentNames;

    /**
     * 
     */
    public String[] getEnvironmentNames() {
        return environmentNames;
    }

    public void setEnvironmentNames(final String[] pEnvironmentNames) {
        environmentNames = pEnvironmentNames;
    }

    private MultipleLineFieldData[] multipleLineFieldDatas;

    /**
     * Get the multipleLineFieldDatas
     */
    public MultipleLineFieldData[] getMultipleLineFieldDatas() {
        return multipleLineFieldDatas;
    }

    /**
     * Set the multipleLineFieldDatas
     */
    public void setMultipleLineFieldDatas(
            final MultipleLineFieldData[] pMultipleLineFieldDatas) {
        multipleLineFieldDatas = pMultipleLineFieldDatas;
    }

}