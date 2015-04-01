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

import org.apache.commons.lang.StringUtils;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * A link maps as a SheetLink or a ProductLink in gPM and is used for XML
 * marshalling/unmarshalling of a links.
 * 
 * @author sidjelli
 */
public class Link extends ValuesContainerData {

    /** Generated serial UID. */
    private static final long serialVersionUID = -3656619001436999192L;

    /** The origin id. */
    @XStreamAsAttribute
    private String originId;

    /** The destination id. */
    @XStreamAsAttribute
    private String destinationId;

    /** The product origin. */
    @XStreamAsAttribute
    private String originProductName;

    /** The product dest. */
    @XStreamAsAttribute
    private String destinationProductName;

    /**
     * Gets the tag name.
     * 
     * @return the tag name
     */
    public String getTagName() {
        return null;
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
     * Sets the destination id.
     * 
     * @param pDestinationId
     *            the destination id
     */
    public void setDestinationId(String pDestinationId) {
        this.destinationId = pDestinationId;
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
     * Sets the origin id.
     * 
     * @param pOriginId
     *            the origin id
     */
    public void setOriginId(String pOriginId) {
        this.originId = pOriginId;
    }

    /**
     * Gets the product dest.
     * 
     * @return the product dest
     */
    public String getDestinationProductName() {
        return destinationProductName;
    }

    /**
     * Sets the product dest.
     * 
     * @param pDestinationProductName
     *            the product dest
     */
    public void setDestinationProductName(String pDestinationProductName) {
        destinationProductName = pDestinationProductName;
    }

    /**
     * Gets the product origin.
     * 
     * @return the product origin
     */
    public String getOriginProductName() {
        return originProductName;
    }

    /**
     * Sets the product origin.
     * 
     * @param pOriginProductName
     *            the product origin
     */
    public void setOriginProductName(String pOriginProductName) {
        originProductName = pOriginProductName;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object pOther) {

        if (pOther instanceof Link) {
            Link lOther = (Link) pOther;

            if (!super.equals(lOther)) {
                return false;
            }
            if (!StringUtils.equals(originId, lOther.originId)) {
                return false;
            }
            if (!StringUtils.equals(destinationId, lOther.destinationId)) {
                return false;
            }
            if (!StringUtils.equals(originProductName, lOther.originProductName)) {
                return false;
            }
            if (!StringUtils.equals(destinationProductName,
                    lOther.destinationProductName)) {
                return false;
            }

            return true;
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        // AttributesContainer hashcode is sufficient
        return super.hashCode();
    }
}
