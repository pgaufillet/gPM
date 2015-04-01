/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/

package org.topcased.gpm.business.sheet.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.topcased.gpm.business.ServiceLocator;
import org.topcased.gpm.business.fields.service.FieldsService;
import org.topcased.gpm.business.lifecycle.service.LifeCycleService;
import org.topcased.gpm.business.serialization.data.MultipleField;
import org.topcased.gpm.business.serialization.data.State;
import org.topcased.gpm.business.sheet.service.SheetTypeData;
import org.topcased.gpm.domain.process.Node;

/**
 * The Class CacheableSheetType
 * 
 * @author llatil
 */
public class CacheableSheetType extends
        org.topcased.gpm.business.fields.impl.CacheableFieldsContainer {
    /** Generated UID */
    private static final long serialVersionUID = 2430832801326913012L;

    /** Reference field definition. */
    private MultipleField referenceField;

    /** Name of the initial state in the lifecycle. */
    private String initialStateName;

    /** State names of all states define in the lifecycle of the sheet type */
    private Collection<String> stateNames;

    /** If the type can be selected */
    private boolean selectable;

    /**
     * Empty constructor for mutable/immutable transformation
     */
    public CacheableSheetType() {
        super();
    }

    /**
     * Constructs a new cacheable sheet type
     * 
     * @param pSheetType
     *            the sheet type
     * @param pDisplayGrpEntities
     *            the display groups entities
     */
    public CacheableSheetType(
            org.topcased.gpm.domain.sheet.SheetType pSheetType,
            List<? extends org.topcased.gpm.domain.facilities.DisplayGroup> pDisplayGrpEntities) {
        super(pSheetType);

        selectable = pSheetType.isSelectable();

        // Remove the reference field from the fields map.
        fieldsKeyMap.remove(FieldsService.REFERENCE_FIELD_NAME);

        setDisplayGroups(pDisplayGrpEntities);

        if (null != pSheetType.getReferenceField()) {
            referenceField =
                    (MultipleField) createSerializableField(pSheetType.getReferenceField());
        }
        else {
            referenceField = null;
        }
        final LifeCycleService lLifeCycleService =
                ServiceLocator.instance().getLifeCycleService();
        initialStateName =
                lLifeCycleService.getInitialStateName(pSheetType.getId());

        final Set<Node> lNodes = lLifeCycleService.getNodes(pSheetType);
        final int lStatesCount = lLifeCycleService.getNodes(pSheetType).size();
        if (lStatesCount > 0) {
            stateNames = new ArrayList<String>(lStatesCount);
            for (Node lState : lNodes) {
                stateNames.add(lState.getName());
            }
        }
        else {
            stateNames = Collections.emptyList();
        }
    }

    /**
     * Get the initial life cycle state name for this sheet type
     * 
     * @return Initial state name
     */
    public String getInitialStateName() {
        return initialStateName;
    }

    /**
     * Set the initial life cycle state name for this sheet type
     * 
     * @param pInitialStateName
     *            The new state name
     */
    public void setInitialStateName(String pInitialStateName) {
        initialStateName = pInitialStateName;
    }

    /**
     * Get the reference field
     * 
     * @return the reference field
     */
    public MultipleField getReferenceField() {
        return referenceField;
    }

    /**
     * Set the reference field
     * 
     * @param pReferenceField
     *            the reference field
     */
    public void setReferenceField(MultipleField pReferenceField) {
        referenceField = pReferenceField;
    }

    /**
     * Get the name of all states for this sheet type
     * 
     * @return Collection of state names
     */
    public Collection<String> getStateNames() {
        return stateNames;
    }

    /**
     * Set the name of all states for this sheet type
     * 
     * @param pStateNames
     *            The new collection of state names
     */
    public void setStateNames(Collection<String> pStateNames) {
        stateNames = pStateNames;
    }

    /**
     * Get if the type can be selected
     * 
     * @return If the type can be selected
     */
    public boolean isSelectable() {
        return selectable;
    }

    /**
     * Set if the type can be selected
     * 
     * @param pSelectable
     *            If the type can be selected
     */
    public void setSelectable(boolean pSelectable) {
        selectable = pSelectable;
    }

    /**
     * Get a SheetTypeData object built from this type
     * 
     * @return New SheetTypeData
     */
    public SheetTypeData toSheetTypeData() {
        return new SheetTypeData(getId(), getName(), getDescription(),
                selectable);
    }

    /**
     * Marshal this object into the given SheetType object
     * 
     * @param pSerializedSheetType
     *            the serialized sheet type
     */
    public void marshal(
            org.topcased.gpm.business.serialization.data.SheetType pSerializedSheetType) {
        super.marshal(pSerializedSheetType);
        pSerializedSheetType.setReferenceField(getReferenceField());
        pSerializedSheetType.setId(getId());

        List<State> lStates = new ArrayList<State>();
        for (String lStateName : getStateNames()) {
            State lState = new State();
            lState.setName(lStateName);
            lStates.add(lState);
        }
        pSerializedSheetType.setStates(lStates);
    }
}
