/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.dynamic;

import java.util.HashMap;
import java.util.Map;

import org.topcased.gpm.business.ContextLocator;
import org.topcased.gpm.business.fields.impl.FieldsServiceImpl;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * Update values of object generated dynamically
 * 
 * @author tpanuel
 */
public final class DynamicValuesContainerAccessFactory {
    private final static DynamicValuesContainerAccessFactory INSTANCE =
            new DynamicValuesContainerAccessFactory();

    private final Map<String, DynamicValuesContainerAccess> accessorsMap;

    private final Map<String, DynamicValuesContainerAccess> revisionAccessorsMap;

    private final FieldsServiceImpl fieldsService;

    private DynamicValuesContainerAccessFactory() {
        accessorsMap = new HashMap<String, DynamicValuesContainerAccess>();
        revisionAccessorsMap =
                new HashMap<String, DynamicValuesContainerAccess>();
        fieldsService =
                (FieldsServiceImpl) ContextLocator.getContext().getBean(
                        "fieldsServiceImpl", FieldsServiceImpl.class);
    }

    /**
     * Access to singleton
     * 
     * @return The DynamicValuesUpdater
     */
    public final static DynamicValuesContainerAccessFactory getInstance() {
        return INSTANCE;
    }

    /**
     * Get access for a specific type (not a revision)
     * 
     * @param pTypeId
     *            Id of the values contaienr's type
     * @return Access for the type
     */
    public DynamicValuesContainerAccess getAccessor(String pTypeId) {
        DynamicValuesContainerAccess lAccessor = accessorsMap.get(pTypeId);

        if (lAccessor == null) {
            lAccessor =
                    new DynamicValuesContainerAccess(
                            fieldsService.getCacheableFieldsContainer(pTypeId,
                                    CacheProperties.IMMUTABLE), false);
            accessorsMap.put(pTypeId, lAccessor);
        }

        return lAccessor;
    }

    /**
     * Get access for a revision of a specific type
     * 
     * @param pRevisedTypeId
     *            Id of the revised values contaienr's type
     * @return Access for the revision of a type
     */
    public DynamicValuesContainerAccess getAccessorOnRevision(
            String pRevisedTypeId) {
        DynamicValuesContainerAccess lRevisionAccessor =
                revisionAccessorsMap.get(pRevisedTypeId);

        if (lRevisionAccessor == null) {
            lRevisionAccessor =
                    new DynamicValuesContainerAccess(
                            fieldsService.getCacheableFieldsContainer(
                                    pRevisedTypeId, CacheProperties.IMMUTABLE),
                            true);
            revisionAccessorsMap.put(pRevisedTypeId, lRevisionAccessor);
        }

        return lRevisionAccessor;
    }
}