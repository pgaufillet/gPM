/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.authorization.impl.filter;

import org.topcased.gpm.business.cache.CacheKey;

/**
 * Key used to access on the cache of the FilterAccessManager.
 * 
 * @author tpanuel
 */
public class FilterAccessDefinitionKey extends CacheKey {
    private static final long serialVersionUID = -146983732766963890L;

    private final String processName;

    private final String roleName;

    private final String typeId;

    /**
     * Create a key to access on the cache of the FilterAccessManager. All the
     * fields are final.
     * 
     * @param pProcessName
     *            The process name.
     * @param pRoleName
     *            The role name.
     * @param pTypeId
     *            The id of the type.
     */
    public FilterAccessDefinitionKey(final String pProcessName,
            final String pRoleName, final String pTypeId) {
        super(pProcessName + '|' + pRoleName + '|' + pTypeId);
        processName = pProcessName;
        roleName = pRoleName;
        typeId = pTypeId;
    }

    /**
     * Get the process name.
     * 
     * @return The process name.
     */
    public String getProcessName() {
        return processName;
    }

    /**
     * Get the role name.
     * 
     * @return The role name.
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * Get the type id.
     * 
     * @return The type id.
     */
    public String getTypeId() {
        return typeId;
    }
}