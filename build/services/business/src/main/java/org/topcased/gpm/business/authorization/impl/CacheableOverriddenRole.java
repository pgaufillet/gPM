/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Olivier Gehin (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.authorization.impl;

import java.util.HashMap;
import java.util.Map;

import org.topcased.gpm.business.CacheableGpmObject;

/**
 * Cacheable object used to cache overriden role. CacheableOverriddenRole
 * 
 * @author ogehin
 */
public class CacheableOverriddenRole extends CacheableGpmObject {

    /** serialVersionUID */
    private static final long serialVersionUID = -1473493348229863390L;

    /** Map Overridden role key -> overriden role name */
    private Map<String, String> overriddenRoleMap;

    /** Prefix used to ensure unicity of cacheable identifier */
    public final static String PREFIX = "OverriddenRole";

    /**
     * Getters of the map
     * 
     * @return the map linking overridenRole key and overridden role name.
     */
    public Map<String, String> getOverriddenRoleMap() {
        return overriddenRoleMap;
    }

    /**
     * Setters of the map
     * 
     * @param pOverriddenRoleMap
     *            The new map linking overridenRole key and overridden role
     *            name.
     */
    public void setOverriddenRoleMap(Map<String, String> pOverriddenRoleMap) {
        overriddenRoleMap = pOverriddenRoleMap;
    }

    /**
     * Get the cached overridden role name
     * 
     * @param pRoleName
     *            The key
     * @return the role name
     */
    public String getOverriddenRoleName(String pRoleName) {
        return overriddenRoleMap.get(pRoleName);
    }

    /**
     * Add a new overridden role name on the map
     * 
     * @param pRoleName
     *            The key of the new overridenRole to add.
     * @param pOverriddenRoleName
     *            The name of the new overriddenRole to add.
     */
    public void putOverriddenRoleInMap(String pRoleName,
            String pOverriddenRoleName) {
        overriddenRoleMap.put(pRoleName, pOverriddenRoleName);
    }

    /**
     * Constructor
     * 
     * @param pContainerID the container ID
     * @param pUserLogin the user login
     */
    public CacheableOverriddenRole(String pContainerID, String pUserLogin) {
        super(PREFIX + pContainerID + pUserLogin);
        overriddenRoleMap = new HashMap<String, String>();
    }

    /**
     * Constructor for mutable/immutable switch
     */
    public CacheableOverriddenRole() {
        super();
    }

}
