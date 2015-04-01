/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 *
 ******************************************************************/

package org.topcased.gpm.business.sheet.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.topcased.gpm.business.ContextLocator;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.fields.impl.CacheableValuesContainer;
import org.topcased.gpm.business.fields.service.FieldsService;
import org.topcased.gpm.business.product.impl.CacheableProduct;
import org.topcased.gpm.business.product.impl.ProductServiceImpl;
import org.topcased.gpm.business.serialization.data.FieldValueData;
import org.topcased.gpm.business.serialization.data.RuleData;
import org.topcased.gpm.business.serialization.data.SheetData;
import org.topcased.gpm.business.serialization.data.TransitionHistoryData;
import org.topcased.gpm.domain.sheet.Sheet;
import org.topcased.gpm.domain.sheet.SheetHistory;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * The Class CacheableSheet.
 * 
 * @author llatil
 */
public class CacheableSheet extends CacheableValuesContainer {

    /** serialVersionUID. */
    private static final long serialVersionUID = 1755518066062308847L;

    /** Current state name in lifecycle. */
    private String currentStateName;

    /** The transition history. */
    private List<TransitionHistoryData> transitionsHistory;

    /** The rules defined in import */
    private List<RuleData> rules;

    /**
     * Constructs a new cacheable sheet.
     */
    public CacheableSheet() {
    }

    /**
     * Create a new cacheable sheet data.
     * 
     * @param pSheet
     *            Sheet entity
     * @param pStateName
     *            Name of the current state of this sheet in its lifecycle
     * @param pSheetType
     *            Sheet type definition
     */
    public CacheableSheet(Sheet pSheet, String pStateName,
            CacheableSheetType pSheetType) {
        super(pSheet, pSheetType);

        currentStateName = pStateName;
        setTransitionHistory(pSheet.getSheetHistories());
    }

    /**
     * Create a new cacheable sheet data.
     * 
     * @param pSheet
     *            Serializable sheet
     * @param pType
     *            Type of the sheet
     */
    public CacheableSheet(
            org.topcased.gpm.business.serialization.data.SheetData pSheet,
            CacheableSheetType pType) {
        super(pSheet, pType);

        initReferenceValues(pSheet);
        currentStateName = pSheet.getState();
        if (!pSheet.getRules().isEmpty()) {
            rules = pSheet.getRules();
        }

        ProductServiceImpl lProductServiceImpl =
                (ProductServiceImpl) ContextLocator.getContext().getBean(
                        "productServiceImpl", ProductServiceImpl.class);
        CacheableProduct lCacheableProduct =
                lProductServiceImpl.getCacheableProductByName(
                        pSheet.getProductName(),
                        pType.getBusinessProcessName(),
                        CacheProperties.IMMUTABLE);
        // test if the product exist
        if (lCacheableProduct != null) {
            environmentNames = lCacheableProduct.getEnvironmentNames();
        }
        else {
            // raise an exception in case the production doesn't exist
            throw new GDMException("Unknown product '"
                    + pSheet.getProductName() + " '");
        }
    }

    /**
     * Inits the reference values.
     * 
     * @param pSheet
     *            the sheet
     */
    private void initReferenceValues(SheetData pSheet) {
        final List<FieldValueData> lReferenceValues =
                pSheet.getReferenceValues();
        if (null == lReferenceValues) {
            return;
        }

        // Create a hashmap to store the reference values
        Map<String, Object> lReferenceSubfieldsMap =
                new HashMap<String, Object>();
        // Fill the map with reference values
        for (FieldValueData lCurrentSubValue : lReferenceValues) {
            putValueInMap(lReferenceSubfieldsMap, lCurrentSubValue);
        }
        putValueInMap(valuesMap, FieldsService.REFERENCE_FIELD_NAME,
                lReferenceSubfieldsMap);
    }

    /**
     * Sets the current state name.
     * 
     * @param pCurrentStateName
     *            the new current state name
     */
    public void setCurrentStateName(String pCurrentStateName) {
        currentStateName = pCurrentStateName;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.fields.impl.CacheableValuesContainer#getFieldLabels()
     */
    @Override
    public Collection<String> getFieldLabels() {
        final Set<String> lLabels = new HashSet<String>(super.getFieldLabels());

        lLabels.remove(FieldsService.REFERENCE_FIELD_NAME);

        return lLabels;
    }

    /**
     * Gets the reference values
     * 
     * @return The reference values
     */
    private List<FieldValueData> getReferenceValues() {
        final List<FieldValueData> lReferences =
                getTopLevelValues(FieldsService.REFERENCE_FIELD_NAME,
                        valuesMap.get(FieldsService.REFERENCE_FIELD_NAME));

        if (lReferences.isEmpty()) {
            return Collections.emptyList();
        }
        else {
            if (lReferences.size() == 1) {
                return lReferences.get(0).getFieldValues();
            }
            else {
                throw new GDMException(
                        "Reference field has invalid content (multivalued)");
            }
        }
    }

    /**
     * Marshal the sheet content into the given sheet data object.
     * 
     * @param pSerializedSheet
     *            the serialized sheet filled by this method.
     */
    public void marshal(
            org.topcased.gpm.business.serialization.data.SheetData pSerializedSheet) {
        super.marshal(pSerializedSheet);

        pSerializedSheet.setState(getCurrentStateName());
        pSerializedSheet.setReference(getFunctionalReference());

        final List<FieldValueData> lRefValues = getReferenceValues();

        if (lRefValues != null && !lRefValues.isEmpty()) {
            pSerializedSheet.setReferenceValues(lRefValues);
        }

        if (getTransitionsHistory() == null
                || getTransitionsHistory().isEmpty()) {
            pSerializedSheet.setTransitionsHistory(null);
        }
        else {
            pSerializedSheet.setTransitionsHistory(getTransitionsHistory());
        }
    }

    @Override
    public void marshal(Object pSerializedContainer) {
        marshal((SheetData) pSerializedContainer);
    }

    /**
     * Sets the transition history.
     * 
     * @param pTransitionsEntities
     *            the new transition history
     */
    private void setTransitionHistory(
            Collection<? extends SheetHistory> pTransitionsEntities) {
        if (!pTransitionsEntities.isEmpty()) {
            transitionsHistory = new ArrayList<TransitionHistoryData>();
            for (SheetHistory lCurrentTransitionEntity : pTransitionsEntities) {
                TransitionHistoryData lTransitionData;
                lTransitionData = new TransitionHistoryData();
                lTransitionData.setOriginState(lCurrentTransitionEntity.getOriginState());
                lTransitionData.setDestinationState(lCurrentTransitionEntity.getDestinationState());
                lTransitionData.setLogin(lCurrentTransitionEntity.getLoginName());

                if (lCurrentTransitionEntity.getChangeDate() == null) {
                    lTransitionData.setTransitionDate(new Date());
                }
                else {
                    lTransitionData.setTransitionDate(lCurrentTransitionEntity.getChangeDate());
                }
                lTransitionData.setTransitionName(lCurrentTransitionEntity.getTransitionName());
                transitionsHistory.add(lTransitionData);
            }
        }
    }

    /**
     * Marshal the sheet content and returns the created sheet data object.
     * 
     * @return The newly created sheet data serialization object.
     */
    @Override
    public Object marshal() {
        final SheetData lSheetData = new SheetData();

        marshal(lSheetData);

        return lSheetData;
    }

    /**
     * Gets the current state name.
     * 
     * @return the current state name
     */
    public String getCurrentStateName() {
        return currentStateName;
    }

    /**
     * Gets the transition history.
     * 
     * @return the transition history
     */
    public List<TransitionHistoryData> getTransitionsHistory() {
        if (null == transitionsHistory) {
            return Collections.emptyList();
        }
        return transitionsHistory;
    }

    /**
     * Sets the transition history.
     * 
     * @param pTransitionHistory
     *            the new transition history
     */
    public void setTransitionsHistory(
            List<TransitionHistoryData> pTransitionHistory) {
        transitionsHistory = pTransitionHistory;
    }

    /**
     * Get the rule list
     * 
     * @return The rule list
     */
    public List<RuleData> getRules() {
        if (rules == null) {
            rules = new ArrayList<RuleData>();
        }
        return rules;
    }

}
