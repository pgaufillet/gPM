/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/

package org.topcased.gpm.util.bean;

import org.topcased.gpm.business.ServiceImplBase;
import org.topcased.gpm.business.authorization.service.AccessControlContextData;

/**
 * The Class CacheableGpmObjectProperties is a bean used to set options on
 * cacheable loading
 * 
 * @author tpanuel
 */
public final class CacheProperties {
    /**
     * Flag for not using a criteria on the access rights verification
     */
    public static final String ACCESS_CONTROL_NOT_USED = null;

    /**
     * Flag for using the default value of a criteria on the access rights
     * verification
     */
    public static final String DEFAULT_ACCESS_CONTROL_USED =
            "$#{USE_DEFAULT_ACCESS_CONTROL}#";

    /**
     * A mutable cacheable without specific access control
     */
    public static final CacheProperties MUTABLE = new CacheProperties(true);

    /**
     * An immutable cacheable without specific access control
     */
    public static final CacheProperties IMMUTABLE = new CacheProperties(false);

    /** The cache flags */
    private int cacheFlags;

    /**
     * Specific access control context can be null. If null for
     * CacheableValuesContainer, compute it from the the container's values. If
     * null for CacheableFieldsContainer, no access control applied.
     */
    private AccessControlContextData specificAccessControl;

    /**
     * Default constructor: by default cacheable object are mutable
     */
    public CacheProperties() {
        this(true);
    }

    /**
     * Constructor defining if the cacheable object is mutable
     * 
     * @param pMutable
     *            If the cacheable is mutable
     */
    public CacheProperties(boolean pMutable) {
        if (pMutable) {
            cacheFlags = ServiceImplBase.CACHE_MUTABLE_OBJECT;
        }
        else {
            cacheFlags = ServiceImplBase.CACHE_IMMUTABLE_OBJECT;
        }
        specificAccessControl = null;
    }

    /**
     * Constructor defining if the cacheable object is mutable and defining the
     * specific visible type id used for the access rights (all other access
     * rights criterion will use default values)
     * 
     * @param pMutable
     *            If the cacheable is mutable
     * @param pSpecificVisibleTypeId
     *            The specific visible type id
     */
    public CacheProperties(boolean pMutable, String pSpecificVisibleTypeId) {
        this(pMutable, pSpecificVisibleTypeId, DEFAULT_ACCESS_CONTROL_USED);
    }

    /**
     * Constructor defining if the cacheable object is mutable and defining the
     * specific visible type id and the specific state name used for the access
     * rights (all other access rights criterion will use default values)
     * 
     * @param pMutable
     *            If the cacheable is mutable
     * @param pSpecificVisibleTypeId
     *            The specific visible type id
     * @param pSpecificStateName
     *            The specificstate name
     */
    public CacheProperties(boolean pMutable, String pSpecificVisibleTypeId,
            String pSpecificStateName) {
        this(pMutable, new AccessControlContextData(
                DEFAULT_ACCESS_CONTROL_USED, DEFAULT_ACCESS_CONTROL_USED,
                pSpecificStateName, DEFAULT_ACCESS_CONTROL_USED,
                pSpecificVisibleTypeId, DEFAULT_ACCESS_CONTROL_USED));
    }

    /**
     * Constructor defining all the fields
     * 
     * @param pMutable
     *            If the cacheable is mutable
     * @param pSpecificAccessControl
     *            Specific access control (can be null)
     */
    public CacheProperties(boolean pMutable,
            AccessControlContextData pSpecificAccessControl) {
        this(pMutable);
        specificAccessControl = pSpecificAccessControl;
    }

    /**
     * Get the mutable
     * 
     * @return the mutable
     */
    public boolean isMutable() {
        return (cacheFlags & ServiceImplBase.CACHE_MUTABLE_OBJECT) != 0;
    }

    /**
     * Get the specific access control
     * 
     * @return the specific access control
     */
    public AccessControlContextData getSpecificAccessControl() {
        return specificAccessControl;
    }

    public final int getCacheFlags() {
        return new Integer(cacheFlags);
    }

    /**
     * Set the CACHE_EVICT_ENTITY property.
     */
    public void setCacheEvictProperty() {
        cacheFlags = cacheFlags | ServiceImplBase.CACHE_EVICT_ENTITY;
    }
}
