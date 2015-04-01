/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Anne Haugommard (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.fields.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.topcased.gpm.business.serialization.data.DisplayGroup;
import org.topcased.gpm.business.serialization.data.DisplayHint;
import org.topcased.gpm.business.serialization.data.Field;
import org.topcased.gpm.business.serialization.data.InputDataType;

/**
 * CacheableInputDataType.
 * 
 * @author ahaugomm
 */
@SuppressWarnings("serial")
public class CacheableInputDataType extends CacheableFieldsContainer {
    /**
     * Constructor for mutable / immutable switch
     */
    public CacheableInputDataType() {
        super();
    }

    /**
     * Constructs a new cacheable input data type.
     * 
     * @param pInputDataTypeEntity
     *            the input data type
     * @param pDisplayGroupsEntities
     *            Display groups
     */
    public CacheableInputDataType(
            org.topcased.gpm.domain.fields.InputDataType pInputDataTypeEntity,
            List<? extends org.topcased.gpm.domain.facilities.DisplayGroup> pDisplayGroupsEntities) {
        super(pInputDataTypeEntity);
        setDisplayGroups(pDisplayGroupsEntities);
    }

    /**
     * Initialize the CacheableInputDataType from the InputDataType.
     * 
     * @param pInputDataType
     *            the input data type.
     */
    public void init(
            org.topcased.gpm.business.serialization.data.InputDataType pInputDataType) {
        setDescription(pInputDataType.getDescription());
        setName(pInputDataType.getName());
        Map<String, DisplayHint> lDisplayHintsMap =
                new HashMap<String, DisplayHint>();
        for (DisplayHint lDisplayHint : pInputDataType.getDisplayHints()) {
            lDisplayHintsMap.put(lDisplayHint.getLabelKey(), lDisplayHint);
        }
        setDisplayHintsMap(lDisplayHintsMap);

        Map<String, DisplayGroup> lDisplayGroupsMap =
                new HashMap<String, DisplayGroup>();
        for (DisplayGroup lDisplayGroup : pInputDataType.getDisplayGroups()) {
            lDisplayGroupsMap.put(lDisplayGroup.getName(), lDisplayGroup);
        }
        setDisplayGroupsMap(lDisplayGroupsMap);

        Map<String, Field> lFieldsIdMap = new HashMap<String, Field>();
        Map<String, Field> lFieldsKeyMap = new HashMap<String, Field>();
        for (Field lField : pInputDataType.getFields()) {
            lFieldsIdMap.put(lField.getId(), lField);
            lFieldsKeyMap.put(lField.getLabelKey(), lField);
        }
        setFieldsIdMap(lFieldsIdMap);
        setFieldsKeyMap(lFieldsKeyMap);
    }

    /**
     * Marshal this object into the given InputDataType object.
     * 
     * @param pInputDataType
     *            the input data type
     */
    public void marshal(InputDataType pInputDataType) {
        super.marshal(pInputDataType);
        pInputDataType.setId(getId());
    }
}
