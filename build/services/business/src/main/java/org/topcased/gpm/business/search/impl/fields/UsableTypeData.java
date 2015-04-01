/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.search.impl.fields;

import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.fields.FieldsContainerType;
import org.topcased.gpm.business.fields.impl.CacheableFieldsContainer;
import org.topcased.gpm.business.link.impl.CacheableLinkType;
import org.topcased.gpm.business.link.impl.LinkDirection;
import org.topcased.gpm.business.product.impl.CacheableProductType;
import org.topcased.gpm.business.search.criterias.impl.FilterFieldsContainerInfo;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;

/**
 * UsableTypeData class.
 * 
 * @author tpanuel
 */
public class UsableTypeData implements Serializable {
    /** serialVersionUID */
    private static final long serialVersionUID = 4447934861541119182L;

    private String id;

    private String fullId;

    private String name;

    private String fullName;

    private List<FilterFieldsContainerInfo> hierarchy;

    private FieldsContainerType type;

    /** Link direction, only for links */
    private LinkDirection linkDirection;

    /**
     * Empty constructor
     */
    public UsableTypeData() {

    }

    /**
     * For containers used on top level by filters.
     * 
     * @param pCacheableFieldsContainer
     *            The fields container.
     */
    public UsableTypeData(
            final CacheableFieldsContainer pCacheableFieldsContainer) {
        id = pCacheableFieldsContainer.getId();
        fullId = pCacheableFieldsContainer.getId();
        name = pCacheableFieldsContainer.getName();
        fullName = pCacheableFieldsContainer.getName();
        hierarchy = Collections.emptyList();
        if (pCacheableFieldsContainer instanceof CacheableSheetType) {
            type = FieldsContainerType.SHEET;
        }
        else if (pCacheableFieldsContainer instanceof CacheableLinkType) {
            type = FieldsContainerType.LINK;
        }
        else if (pCacheableFieldsContainer instanceof CacheableProductType) {
            type = FieldsContainerType.PRODUCT;
        }
        else {
            throw new IllegalArgumentException("Unknow fields container : "
                    + pCacheableFieldsContainer.getClass().getName());
        }
        linkDirection = LinkDirection.UNDEFINED;
    }

    /**
     * For container used on multi level filters.
     * 
     * @param pHierarchy
     *            The container hierarchy. Cannot be null or empty.
     */
    public UsableTypeData(final List<FilterFieldsContainerInfo> pHierarchy) {
        if (pHierarchy == null || pHierarchy.isEmpty()) {
            throw new IllegalArgumentException(
                    "The container hierarchy cannot be empty.");
        }

        final Iterator<FilterFieldsContainerInfo> lIterator =
                pHierarchy.iterator();

        fullId = StringUtils.EMPTY;
        fullName = StringUtils.EMPTY;

        while (lIterator.hasNext()) {
            final FilterFieldsContainerInfo lContainer = lIterator.next();

            fullId += lContainer.getId();
            fullName += lContainer.getLabelKey();
            if (lIterator.hasNext()) {
                fullId += '|';
                fullName += '|';
            }
            else {
                id = lContainer.getId();
                name = lContainer.getLabelKey();
                type = lContainer.getType();
                linkDirection = lContainer.getLinkDirection();
            }
        }

        hierarchy = pHierarchy;

    }

    /**
     * Get the type's id.
     * 
     * @return The id.
     */
    public String getId() {
        return id;
    }

    /**
     * Get the full id, describing the hierarchy. Id are separated by |.
     * 
     * @return The full id.
     */
    public String getFullId() {
        return fullId;
    }

    /**
     * Get the type's name.
     * 
     * @return The name.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the full name, describing the hierarchy. Names are separated by |.
     * 
     * @return The full name.
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Get the hierarchy. Empty for top level containers.
     * 
     * @return The hierarchy.
     */
    public List<FilterFieldsContainerInfo> getHierarchy() {
        return hierarchy;
    }

    /**
     * Get the type of fields container (sheet, link or product).
     * 
     * @return The type of fields container.
     */
    public FieldsContainerType getType() {
        return type;
    }

    /**
     * Get the link direction.
     * 
     * @return The link direction.
     */
    public LinkDirection getLinkDirection() {
        return linkDirection;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object pOther) {
        if (!(pOther instanceof UsableTypeData)) {
            return false;
        }
        else {
            // Same usable type if same full id
            return getFullId().equals(((UsableTypeData) pOther).getFullId());
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        if (getFullId() == null) {
            return 0;
        }
        else {
            return getFullId().hashCode();
        }
    }

    /**
     * Set id
     * 
     * @param pId
     *            the id to set
     */
    public void setId(String pId) {
        id = pId;
    }

    /**
     * Set fullId
     * 
     * @param pFullId
     *            the fullId to set
     */
    public void setFullId(String pFullId) {
        fullId = pFullId;
    }

    /**
     * Set name
     * 
     * @param pName
     *            the name to set
     */
    public void setName(String pName) {
        name = pName;
    }

    /**
     * Set fullName
     * 
     * @param pFullName
     *            the fullName to set
     */
    public void setFullName(String pFullName) {
        fullName = pFullName;
    }

    /**
     * Set hierarchy
     * 
     * @param pHierarchy
     *            the hierarchy to set
     */
    public void setHierarchy(List<FilterFieldsContainerInfo> pHierarchy) {
        hierarchy = pHierarchy;
    }

    /**
     * Set type
     * 
     * @param pType
     *            the type to set
     */
    public void setType(FieldsContainerType pType) {
        type = pType;
    }

    /**
     * Set linkDirection
     * 
     * @param pLinkDirection
     *            the linkDirection to set
     */
    public void setLinkDirection(LinkDirection pLinkDirection) {
        linkDirection = pLinkDirection;
    }

}