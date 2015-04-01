/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.domain.dynamic.container;

import java.util.HashMap;
import java.util.Map;

import org.topcased.gpm.domain.dynamic.DynamicObjectGenerator;
import org.topcased.gpm.domain.dynamic.DynamicObjectGeneratorFactory;
import org.topcased.gpm.domain.fields.FieldsContainer;
import org.topcased.gpm.domain.fields.ValuesContainer;

/**
 * Factory of DynamicObjectGenerator
 * 
 * @author tpanuel
 * @param <DYNAMIC_OBJECT>
 *            The super class of the generated objects
 * @param <TYPE>
 *            The type that describe the generated class
 */
public abstract class DynamicValuesContainerGeneratorFactory<DYNAMIC_OBJECT extends ValuesContainer, TYPE extends FieldsContainer>
        extends DynamicObjectGeneratorFactory<DYNAMIC_OBJECT, TYPE> {
    /** Map<BusinessProcessName, Map<ValuesContainerName, ValuesContainerId>> */
    private Map<String, Map<String, String>> oldContainerIdByNameAndProcess =
            new HashMap<String, Map<String, String>>();

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.dynamic.DynamicObjectGeneratorFactory#initDynamicObjectGenerator(java.lang.String,
     *      java.lang.Object)
     */
    public DynamicObjectGenerator<DYNAMIC_OBJECT> initDynamicObjectGenerator(
            String pMappedObjectTypeId, TYPE pMappedObjectType) {
        // Destroy generator with same name but different id
        final String lContainerName = pMappedObjectType.getName();
        final String lProcessName =
                pMappedObjectType.getBusinessProcess().getName();
        Map<String, String> lOldContainerIdByName =
                oldContainerIdByNameAndProcess.get(lProcessName);

        if (lOldContainerIdByName == null) {
            // No generator for this business process already exists
            lOldContainerIdByName = new HashMap<String, String>();
            oldContainerIdByNameAndProcess.put(lProcessName,
                    lOldContainerIdByName);
        }
        else {
            final String lOldId = lOldContainerIdByName.get(lContainerName);

            // If a different container generator exists for the same name
            if (lOldId != null && !lOldId.equalsIgnoreCase(pMappedObjectTypeId)) {
                // Destroy and clean old generator
                cleanGenerator(lOldId);
            }
        }

        lOldContainerIdByName.put(lContainerName, pMappedObjectTypeId);

        return super.initDynamicObjectGenerator(pMappedObjectTypeId,
                pMappedObjectType);
    }

    /**
     * Reset DynamicValuesContainerGeneratorFactory by cleaning all
     * DynamicValuesContainerGenerator
     */
    public void reset() {
        for (DynamicObjectGenerator<DYNAMIC_OBJECT> lGenerator : generatorsMap.values()) {
            lGenerator.cleanGenerator();
        }
        generatorsMap.clear();
    }
}
