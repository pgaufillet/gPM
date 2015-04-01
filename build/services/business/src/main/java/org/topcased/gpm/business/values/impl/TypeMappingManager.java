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

import java.util.HashMap;
import java.util.Map;

import net.sf.ehcache.Cache;

import org.topcased.gpm.business.cache.AbstractCacheManager;
import org.topcased.gpm.domain.mapping.FieldMap;
import org.topcased.gpm.domain.mapping.TypeMap;
import org.topcased.gpm.domain.mapping.TypeMapDao;
import org.topcased.gpm.domain.mapping.ValueMap;

/**
 * Used to managed the filter data. For each type couple, the cache contains an
 * ExecutableFilterData.
 * 
 * @author tpanuel
 */
public class TypeMappingManager extends
        AbstractCacheManager<TypeMappingKey, TypeMappingDefinition> {
    private Cache typeMappingCache;

    private TypeMapDao typeMapDao;

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.cache.AbstractCacheManager#getCache()
     */
    @Override
    protected Cache getCache() {
        return typeMappingCache;
    }

    /**
     * Setter on cache used for Spring injection.
     * 
     * @param pCache
     *            The cache.
     */
    public void setTypeMappingCache(final Cache pCache) {
        typeMappingCache = pCache;
    }

    /**
     * Setter used by Spring for injection.
     * 
     * @param pTypeMapDao
     *            The new DAO.
     */
    public void setTypeMapDao(final TypeMapDao pTypeMapDao) {
        typeMapDao = pTypeMapDao;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.cache.AbstractCacheManager#load(java.io.Serializable)
     */
    @Override
    protected TypeMappingDefinition load(final TypeMappingKey pElementKey) {
        final TypeMappingDefinition lTypeMapping = new TypeMappingDefinition();
        final TypeMap lDomainTypeMapping =
                typeMapDao.getTypeMapping(pElementKey.getOriginProcessName(),
                        pElementKey.getOriginTypeName(),
                        pElementKey.getDestinationProcessName(),
                        pElementKey.getDestinationTypeName());

        // If a mapping has been defined
        if (lDomainTypeMapping != null
                && lDomainTypeMapping.getFieldMaps() != null) {
            for (FieldMap lDomainFieldMapping : lDomainTypeMapping.getFieldMaps()) {
                lTypeMapping.getFieldMapping().put(
                        lDomainFieldMapping.getOriginLabelKey(),
                        lDomainFieldMapping.getDestinationLabelKey());
                // If a value mapping has been defined
                if (lDomainFieldMapping.getValueMaps() != null
                        && !lDomainFieldMapping.getValueMaps().isEmpty()) {
                    final Map<String, String> lValues =
                            new HashMap<String, String>();

                    for (ValueMap lDomainValueMapping : lDomainFieldMapping.getValueMaps()) {
                        lValues.put(lDomainValueMapping.getOriginValue(),
                                lDomainValueMapping.getDestinationValue());
                    }
                    lTypeMapping.getValueMapping().put(
                            lDomainFieldMapping.getOriginLabelKey(), lValues);
                }
                // If an extension point has been defined
                if (lDomainFieldMapping.getExtensionPoint() != null) {
                    lTypeMapping.getExtensionPoints().put(
                            lDomainFieldMapping.getOriginLabelKey(),
                            lDomainFieldMapping.getExtensionPoint().getName());
                }
            }
        }

        return lTypeMapping;
    }
}