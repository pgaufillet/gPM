/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.search.criterias.impl;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.fields.FieldsContainerType;
import org.topcased.gpm.business.link.impl.LinkDirection;

/**
 * FilterFieldsContainerInfo.
 * <p>
 * Useful information of a fields container associated to a virtual field.
 * <p>
 * Link direction is the direction of link type for this hierarchy element. It
 * corresponds to the 'to' part of the link type.<br />
 * <p>
 * e.g:
 * <p>
 * Hierarchy: A-B, A, A-D, D<br />
 * The link direction for 'A-B' is 'ORIGIN' because the next sheet type is the
 * origin of the link type.<br />
 * The link direction for 'A-D' is 'DESTINATION' because the next sheet type is
 * the destination of the link type.
 * </p>
 * <p>
 * Hierarchy: A-B, B, A-B, A<br />
 * The link direction for 'A-B' (first) is 'DESTINATION' because the next sheet
 * type 'B' is defined as destination of the link type.<br />
 * The link direction for 'A-B' (last) is 'ORIGIN' because the next sheet type
 * 'A' is defined as origin of the link type.
 * </p>
 * <p>
 * Hierarchy: A-A, A. The link direction for 'A-A' is 'UNDEFINED'.
 * </p>
 * <p>
 * The 'link direction' is never null. The null value is 'UNDEFINED'.
 * 
 * @author mkargbo
 */
public class FilterFieldsContainerInfo implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = -1126906765166793027L;

    /** Technical identifier of the fields container */
    private String id;

    /** Label key of the fields container */
    private String labelKey;

    /** Type of the fields container */
    private FieldsContainerType type;

    /** Link direction (only if type is LINK) */
    private LinkDirection linkDirection;

    /**
     * Creates a new FilterFieldsContainerInfo and sets:
     * <ul>
     * <li>id to a empty string
     * <li>label key to an empty string
     * <li>type to null
     * <li>link direction to 'UNDEFINED'
     * </ul>
     */
    public FilterFieldsContainerInfo() {
        id = StringUtils.EMPTY;
        labelKey = StringUtils.EMPTY;
        type = null;
        linkDirection = LinkDirection.UNDEFINED;
    }

    /**
     * Creates a new FilterFieldsContainerInfo.
     * <p>
     * Link direction is setting at null.
     * 
     * @param pId
     *            Technical identifier of the fields container
     * @param pLabelKey
     *            Label key of the fields container
     * @param pType
     *            Type of the fields container
     */
    public FilterFieldsContainerInfo(String pId, String pLabelKey,
            FieldsContainerType pType) {
        id = pId;
        labelKey = pLabelKey;
        type = pType;
        linkDirection = LinkDirection.UNDEFINED;
    }

    /**
     * Creates a new FilterFieldsContainerInfo.
     * 
     * @param pId
     *            Technical identifier of the fields container
     * @param pLabelKey
     *            Label key of the fields container
     * @param pType
     *            Type of the fields container
     * @param pLinkDirection
     *            Direction of the link.
     */
    public FilterFieldsContainerInfo(String pId, String pLabelKey,
            FieldsContainerType pType, LinkDirection pLinkDirection) {
        id = pId;
        labelKey = pLabelKey;
        type = pType;
        if (pLinkDirection == null) {
            linkDirection = LinkDirection.UNDEFINED;
        }
        else {
            linkDirection = pLinkDirection;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String pId) {
        id = pId;
    }

    public String getLabelKey() {
        return labelKey;
    }

    public void setLabelKey(String pLabelKey) {
        labelKey = pLabelKey;
    }

    public FieldsContainerType getType() {
        return type;
    }

    public void setType(FieldsContainerType pType) {
        type = pType;
    }

    public LinkDirection getLinkDirection() {
        return linkDirection;
    }

    /**
     * Sets the link direction
     * <p>
     * Sets 'UNDEFINED' value if the parameter is null.
     * 
     * @param pLinkDirection
     *            New link direction.
     */
    public void setLinkDirection(LinkDirection pLinkDirection) {
        if (pLinkDirection == null) {
            linkDirection = LinkDirection.UNDEFINED;
        }
        else {
            linkDirection = pLinkDirection;
        }
    }
}
