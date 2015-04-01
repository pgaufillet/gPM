/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.values.impl;

import org.topcased.gpm.business.cache.CacheKey;
import org.topcased.gpm.business.serialization.data.TypeMapping;

/**
 * Key used to access on the cache of the TypeMappingManager.
 * 
 * @author tpanuel
 */
public class TypeMappingKey extends CacheKey {
    private static final long serialVersionUID = -146983732766963890L;

    private final String originProcessName;

    private final String originTypeName;

    private final String destinationProcessName;

    private final String destinationTypeName;

    /**
     * Create a key used to identify an element of the TypeMappingManager.
     * 
     * @param pOriginProcessName
     *            The origin process name.
     * @param pOriginTypeName
     *            The origin type name.
     * @param pDestinationProcessName
     *            The destination process name.
     * @param pDestinationTypeName
     *            The destination type name.
     */
    public TypeMappingKey(final String pOriginProcessName,
            final String pOriginTypeName, final String pDestinationProcessName,
            final String pDestinationTypeName) {
        super(pOriginProcessName + '|' + pOriginTypeName + '|'
                + pDestinationProcessName + '|' + pDestinationTypeName);
        originProcessName = pOriginProcessName;
        originTypeName = pOriginTypeName;
        destinationProcessName = pDestinationProcessName;
        destinationTypeName = pDestinationTypeName;
    }

    /**
     * Create a key used to identify an element of the TypeMappingManager.
     * 
     * @param pTypeMapping
     *            The type mapping.
     */
    public TypeMappingKey(final TypeMapping pTypeMapping) {
        this(pTypeMapping.getOriginProcessName(),
                pTypeMapping.getOriginTypeName(),
                pTypeMapping.getDestinationProcessName(),
                pTypeMapping.getDestinationTypeName());
    }

    /**
     * Get the origin process name.
     * 
     * @return The origin process name.
     */
    public String getOriginProcessName() {
        return originProcessName;
    }

    /**
     * Get the origin type name.
     * 
     * @return The origin type name.
     */
    public String getOriginTypeName() {
        return originTypeName;
    }

    /**
     * Get the destination process name.
     * 
     * @return The destination process name.
     */
    public String getDestinationProcessName() {
        return destinationProcessName;
    }

    /**
     * Get the destination type name.
     * 
     * @return The destination type name.
     */
    public String getDestinationTypeName() {
        return destinationTypeName;
    }
}