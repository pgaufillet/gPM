/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 *
 ******************************************************************/

package org.topcased.gpm.business.sheet.impl;

import java.util.List;

import org.topcased.gpm.business.CacheableGpmObject;

/**
 * The Class CacheableSheetLinks
 * 
 * @author tpanuel
 */
public class CacheableSheetLinksByType extends CacheableGpmObject {
    private static final long serialVersionUID = -475277189215284553L;

    private String sheetId;

    private String linkTypeId;

    private List<String> linksId;

    /**
     * Empty constructor for mutable / immutable switch
     */
    public CacheableSheetLinksByType() {
        super();
    }

    /**
     * Constructs a new cacheable sheet
     * 
     * @param pSheetId
     *            The sheet id
     * @param pLinkTypeId
     *            The link type id
     * @param pLinksId
     *            The links id
     */
    public CacheableSheetLinksByType(String pSheetId, String pLinkTypeId,
            List<String> pLinksId) {
        setId(getCachedElementId(pSheetId, pLinkTypeId));
        sheetId = pSheetId;
        linkTypeId = pLinkTypeId;
        linksId = pLinksId;
    }

    /**
     * Getter on the sheet id
     * 
     * @return A sheet id
     */
    public String getSheetId() {
        return sheetId;
    }

    /**
     * Set the sheetId
     * 
     * @param pSheetId
     *            the new sheetId
     */
    public void setSheetId(String pSheetId) {
        sheetId = pSheetId;
    }

    /**
     * Set the linkTypeId
     * 
     * @param pLinkTypeId
     *            the new linkTypeId
     */
    public void setLinkTypeId(String pLinkTypeId) {
        linkTypeId = pLinkTypeId;
    }

    /**
     * Getter on the link type id
     * 
     * @return A link type id
     */
    public String getLinkTypeId() {
        return linkTypeId;
    }

    /**
     * Getter on the list of links id
     * 
     * @return A list of link id
     */
    public List<String> getLinksId() {
        return linksId;
    }

    /**
     * Set the linksId
     * 
     * @param pLinksId
     *            the new linksId
     */
    public void setLinksId(List<String> pLinksId) {
        linksId = pLinksId;
    }

    /**
     * Marshal
     * 
     * @param pContent
     *            Serializable content
     */
    public void marshal(Object pContent) {
        throw new RuntimeException("marshal() cannot be used on '"
                + getClass().getName() + "' class");
    }

    /**
     * Compute an id used to find the CacheableSheetLinks associated to a sheet
     * 
     * @param pSheetId
     *            The id of the sheet
     * @param pLinkTypeId
     *            The link type id
     * @return The cacheable id
     */
    public static String getCachedElementId(String pSheetId, String pLinkTypeId) {
        return pSheetId + pLinkTypeId;
    }
}
