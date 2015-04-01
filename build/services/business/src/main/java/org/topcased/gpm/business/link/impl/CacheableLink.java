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

import org.topcased.gpm.business.ContextLocator;
import org.topcased.gpm.business.fields.impl.CacheableValuesContainer;
import org.topcased.gpm.business.fields.impl.FieldsServiceImpl;
import org.topcased.gpm.business.sheet.impl.SheetServiceImpl;
import org.topcased.gpm.domain.link.Link;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * The Class CacheableLink.
 * 
 * @author llatil
 */
public class CacheableLink extends CacheableValuesContainer {

    /** serialVersionUID. */
    private static final long serialVersionUID = -7029110367938434878L;

    /** The origin id. */
    private String originId;

    /** The destination id. */
    private String destinationId;

    /** The origin ref. */
    private String originRef;

    /** The destination ref. */
    private String destinationRef;

    /**
     * Constructor for mutable / immutable switch
     */
    public CacheableLink() {
        super();
    }

    /**
     * Construct a new CacheableLink.
     * 
     * @param pLink
     *            Link entity
     * @param pLinkType
     *            Link type
     */
    public CacheableLink(Link pLink, CacheableLinkType pLinkType) {
        super(pLink, pLinkType);

        originId = pLink.getOrigin().getId();
        originRef = pLink.getOrigin().getFunctionalReference();

        destinationId = pLink.getDestination().getId();
        destinationRef = pLink.getDestination().getFunctionalReference();
    }

    /**
     * Construct a new CacheableLink.
     * 
     * @param pLinkType
     *            Link type
     * @param pOriginId
     *            The id of the origin
     * @param pOriginRef
     *            The reference of the origin
     * @param pDestId
     *            The id of the destination
     * @param pDestRef
     *            The reference of the destination
     */
    public CacheableLink(CacheableLinkType pLinkType, String pOriginId,
            String pOriginRef, String pDestId, String pDestRef) {
        super(pLinkType);
        originId = pOriginId;
        originRef = pOriginRef;
        destinationId = pDestId;
        destinationRef = pDestRef;

        FieldsServiceImpl lFieldsServiceImpl =
                (FieldsServiceImpl) ContextLocator.getContext().getBean(
                        "fieldsServiceImpl", FieldsServiceImpl.class);

        environmentNames =
                lFieldsServiceImpl.getCacheableValuesContainer(originId,
                        CacheProperties.IMMUTABLE).getEnvironmentNames();
    }

    /**
     * Construct a new Cacheable Link.
     * 
     * @param pLink
     *            The serializable link
     * @param pCacheableLinkType
     *            The link type
     */
    public CacheableLink(
            org.topcased.gpm.business.serialization.data.Link pLink,
            CacheableLinkType pCacheableLinkType) {
        super(pLink, pCacheableLinkType);

        originId = pLink.getOriginId();
        destinationId = pLink.getDestinationId();

        SheetServiceImpl lSheetServiceImpl =
                (SheetServiceImpl) ContextLocator.getContext().getBean(
                        "sheetServiceImpl", SheetServiceImpl.class);

        environmentNames =
                lSheetServiceImpl.getCachedValuesContainer(originId, 0).getEnvironmentNames();
    }

    /**
     * Gets the origin id.
     * 
     * @return the origin id
     */
    public String getOriginId() {
        return originId;
    }

    /**
     * Set the originId
     * 
     * @param pOriginId
     *            the new originId
     */
    public void setOriginId(String pOriginId) {
        originId = pOriginId;
    }

    /**
     * Gets the destination id.
     * 
     * @return the destination id
     */
    public String getDestinationId() {
        return destinationId;
    }

    /**
     * Set the destinationId
     * 
     * @param pDestinationId
     *            the new destinationId
     */
    public void setDestinationId(String pDestinationId) {
        destinationId = pDestinationId;
    }

    /**
     * Gets the origin ref.
     * 
     * @return the origin ref
     */
    public String getOriginRef() {
        return originRef;
    }

    /**
     * Set the originRef
     * 
     * @param pOriginRef
     *            the new originRef
     */
    public void setOriginRef(String pOriginRef) {
        originRef = pOriginRef;
    }

    /**
     * Gets the destination ref.
     * 
     * @return the destination ref
     */
    public String getDestinationRef() {
        return destinationRef;
    }

    /**
     * Set the destinationRef
     * 
     * @param pDestinationRef
     *            the new destinationRef
     */
    public void setDestinationRef(String pDestinationRef) {
        destinationRef = pDestinationRef;
    }

    /**
     * Marshal this object into the given
     * org.topcased.gpm.business.serialization.data.Link object.
     * 
     * @param pObject
     *            the serializable link
     */
    public void marshal(Object pObject) {

        super.marshal(pObject);
        org.topcased.gpm.business.serialization.data.Link lLink =
                (org.topcased.gpm.business.serialization.data.Link) pObject;

        lLink.setOriginId(getOriginId());
        lLink.setDestinationId(getDestinationId());
    }

    @Override
    public Object marshal() {
        org.topcased.gpm.business.serialization.data.Link lLink;
        lLink = new org.topcased.gpm.business.serialization.data.Link();
        marshal(lLink);
        return lLink;
    }
}
