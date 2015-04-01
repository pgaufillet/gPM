/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.domain.dynamic;

import java.util.HashMap;
import java.util.Map;

import org.topcased.gpm.domain.PersistentObject;

/**
 * Factory of DynamicObjectGenerator
 * 
 * @author tpanuel
 * @param <DYNAMIC_OBJECT>
 *            The super class of the generated objects
 * @param <TYPE>
 *            The type that describe the generated class
 */
public abstract class DynamicObjectGeneratorFactory<DYNAMIC_OBJECT extends PersistentObject, TYPE> {
    protected Map<String, DynamicObjectGenerator<DYNAMIC_OBJECT>> generatorsMap =
            new HashMap<String, DynamicObjectGenerator<DYNAMIC_OBJECT>>();

    /**
     * Initialize a generator for a specific type
     * 
     * @param pMappedObjectTypeId
     *            The id of the type : unique
     * @param pMappedObjectType
     *            The type describing the generated class
     * @return The computed generator associated to the type
     */
    public DynamicObjectGenerator<DYNAMIC_OBJECT> initDynamicObjectGenerator(
            String pMappedObjectTypeId, TYPE pMappedObjectType) {
        // If values container has been modified, 
        // it's possible to re initialize generator
        if (generatorsMap.get(pMappedObjectTypeId) != null) {
            cleanGenerator(pMappedObjectTypeId);
        }

        // Create a new generator
        final DynamicObjectGenerator<DYNAMIC_OBJECT> lGenerator =
                createDynamicObjectGenerator(pMappedObjectType);

        generatorsMap.put(pMappedObjectTypeId, lGenerator);
        DynamicClassRegister.registerDynamicClass(lGenerator.getGeneratedClass());

        return lGenerator;
    }

    /**
     * Getter on an already initialized generator
     * 
     * @param pMappedObjectTypeId
     *            The id of the type
     * @return The generator associated to the type
     */
    public DynamicObjectGenerator<DYNAMIC_OBJECT> getDynamicObjectGenerator(
            String pMappedObjectTypeId) {
        final DynamicObjectGenerator<DYNAMIC_OBJECT> lGenerator =
                generatorsMap.get(pMappedObjectTypeId);

        // Generator must be initialized first
        if (lGenerator == null) {
            throw new RuntimeException("Generator must be initialized first");
        }

        return lGenerator;
    }

    /**
     * Method used to define the way to create the generator from the type
     * definition
     * 
     * @param pMappedObjectType
     *            The type definition
     * @return The generator associated to the type
     */
    protected abstract DynamicObjectGenerator<DYNAMIC_OBJECT> createDynamicObjectGenerator(
            TYPE pMappedObjectType);

    /**
     * Clean the generator associate to a type
     * 
     * @param pTypeId
     *            The id of the type
     */
    public void cleanGenerator(String pTypeId) {
        final DynamicObjectGenerator<DYNAMIC_OBJECT> lGenerator =
                generatorsMap.remove(pTypeId);

        if (lGenerator != null) {
            lGenerator.cleanGenerator();
        }
    }
}
