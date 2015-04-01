/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.fields.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.attributes.impl.CacheableAttributesContainer;
import org.topcased.gpm.business.dynamic.DynamicValuesContainerAccessFactory;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.fields.AttachedFieldModificationData;
import org.topcased.gpm.business.fields.FieldData;
import org.topcased.gpm.business.fields.LineFieldData;
import org.topcased.gpm.business.fields.MultipleLineFieldData;
import org.topcased.gpm.business.serialization.data.AttachedFieldValueData;
import org.topcased.gpm.business.serialization.data.FieldValueData;
import org.topcased.gpm.business.serialization.data.PointerFieldValueData;
import org.topcased.gpm.business.serialization.data.ValuesContainerData;
import org.topcased.gpm.domain.dictionary.Environment;
import org.topcased.gpm.domain.fields.ValuesContainer;
import org.topcased.gpm.domain.revision.Revision;

/**
 * The Class CacheableValuesContainer.
 * 
 * @author llatil
 */
public abstract class CacheableValuesContainer extends
        CacheableAttributesContainer {

    /** Generated serial UID. */
    private static final long serialVersionUID = 8282969004386623788L;

    /** Map field label -> value. */
    protected Map<String, Object> valuesMap = new Hashtable<String, Object>();

    /** Identifier of the type element of this container. */
    private String typeId;

    /** Name of the type of this container. */
    private String typeName;

    /** Functional reference of this element. */
    private String functionalReference;

    /** Name of the product containing this element. */
    private String productName;

    /** Version of this element. */
    private int version;

    /** List of environment names to use for this container. */
    protected List<String> environmentNames = Collections.emptyList();

    /** Extension points to exclude from execution (during import of data)*/
    private Set<String> extentionPointsToExclude;

    /**
     * Instantiates a new cacheable values container.
     */
    public CacheableValuesContainer() {
        super();
    }

    /**
     * Instantiates a new cacheable values container.
     * 
     * @param pValContainer
     *            the val container
     * @param pType
     *            the type
     */
    public CacheableValuesContainer(ValuesContainer pValContainer,
            CacheableFieldsContainer pType) {
        super(pValContainer.getId());

        typeName = pType.getName();
        typeId = pType.getId();

        functionalReference = pValContainer.getFunctionalReference();
        version = pValContainer.getVersion();

        if (pValContainer instanceof Revision) {
            DynamicValuesContainerAccessFactory.getInstance().getAccessorOnRevision(
                    typeId).updateBusinessFromDomain(this, pValContainer, null);
        }
        else {
            DynamicValuesContainerAccessFactory.getInstance().getAccessor(
                    typeId).updateBusinessFromDomain(this, pValContainer, null);
        }

        if (null != pValContainer.getEnvironments()
                && !pValContainer.getEnvironments().isEmpty()) {
            environmentNames =
                    new ArrayList<String>(
                            pValContainer.getEnvironments().size());
            for (Environment lEnv : pValContainer.getEnvironments()) {
                environmentNames.add(lEnv.getName());
            }
        }

        if (pValContainer.getProduct() != null) {
            productName = pValContainer.getProduct().getName();
        }
    }

    /**
     * Instantiates a new cacheable values container.
     * 
     * @param pType
     *            the type
     */
    public CacheableValuesContainer(CacheableFieldsContainer pType) {
        super();

        typeName = pType.getName();
        typeId = pType.getId();
    }

    /**
     * Instantiates a new cacheable values container from a serialization
     * object.
     * 
     * @param pValuesContainerData
     *            the serializable values container data
     * @param pType
     *            the type
     */
    public CacheableValuesContainer(ValuesContainerData pValuesContainerData,
            CacheableFieldsContainer pType) {
        super(pValuesContainerData.getId(), pValuesContainerData);

        initValues(pValuesContainerData.getFieldValues());

        typeName = pValuesContainerData.getType();
        typeId = pType.getId();

        functionalReference = pValuesContainerData.getReference();

        if (pValuesContainerData.getVersion() != null) {
            version = pValuesContainerData.getVersion();
        }
        else {
            version = 0;
        }
        productName = pValuesContainerData.getProductName();
    }

    /**
     * Inits the values.
     * 
     * @param pValuesList
     *            the values list
     */
    private final void initValues(
            Collection<? extends FieldValueData> pValuesList) {
        if (pValuesList != null) {

            // Add the all field values (including subfield values) in the list
            //valuesList.addAll(pValuesList);

            // Store the values in the valuesMap member for efficient retrieval.
            for (FieldValueData lCurrentValue : pValuesList) {
                if (lCurrentValue.getValue() != null
                        || (lCurrentValue instanceof AttachedFieldValueData)
                        || lCurrentValue instanceof PointerFieldValueData) {
                    putValueInMap(valuesMap, lCurrentValue);
                }
                else if (lCurrentValue.hasFieldValues()) {
                    final List<FieldValueData> lSubValues =
                            lCurrentValue.getFieldValues();

                    // Create a hashmap to store the subfield values
                    Map<String, Object> lCurrentSubfieldsMap =
                            new HashMap<String, Object>(lSubValues.size());
                    putValueInMap(valuesMap, lCurrentValue.getName(),
                            lCurrentSubfieldsMap);

                    for (FieldValueData lCurrentSubValue : lSubValues) {
                        putValueInMap(lCurrentSubfieldsMap, lCurrentSubValue);
                    }
                }
            }
        }
    }

    /**
     * Put a field value in a fields map.
     * 
     * @param pMap
     *            Map (field name -> value) where the value is added.
     * @param pFieldValueData
     *            Value data to put
     */
    protected void putValueInMap(Map<String, Object> pMap,
            FieldValueData pFieldValueData) {
        String lFieldName = pFieldValueData.getName();
        putValueInMap(pMap, lFieldName, pFieldValueData);
    }

    /**
     * Put a field value in a fields map.
     * 
     * @param pMap
     *            Map (field name -> value) where the value is added.
     * @param pFieldName
     *            Name of the field
     * @param pValue
     *            Field value
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected final void putValueInMap(Map<String, Object> pMap,
            String pFieldName, Object pValue) {
        // Values stored in map can be
        // - The FieldValueData, if the field has only one value
        // - or a list of FieldValueData
        Object lObjValue = pMap.get(pFieldName);
        if (null == lObjValue) {
            pMap.put(pFieldName, pValue);
        }
        else if (lObjValue instanceof List) {
            ((List) lObjValue).add(pValue);
        }
        else {
            List lList = new ArrayList();
            lList.add(lObjValue);
            lList.add(pValue);
            pMap.put(pFieldName, lList);
        }
    }

    /**
     * Put a new field value in the fields map.
     * 
     * @param pMap
     *            the map
     * @param pFieldValueDataList
     *            the field value data list
     */
    private void putValuesInMap(Map<String, Object> pMap,
            List<FieldValueData> pFieldValueDataList) {
        for (FieldValueData lFieldValue : pFieldValueDataList) {
            putValueInMap(pMap, lFieldValue);
        }
    }

    /**
     * Get the values of a field. This method may return:
     * <ul>
     * <li>null if field has no value
     * <li>a FieldValueData object if field has a single value
     * <li>a list of FieldValueData objects
     * <li>a map of subfields values (field key -> FieldValueData)
     * <li>a list of map of subfields values.
     * </ul>
     * for the last two cases, each subfield value can be:
     * <ul>
     * <li>a FieldValueData object for simple, attached file fields,
     * single-value choice field
     * <li>a list of FieldValueData objects for multi-valued choice field (in
     * this case this is a multi-valued choice field contained in a multiple
     * field).
     * </ul>
     * 
     * @param pFieldName
     *            Label key of the field
     * @return List containing the values of a field
     */
    public Object getValue(String pFieldName) {
        return valuesMap.get(pFieldName);
    }

    /**
     * Gets the value.
     * 
     * @param pParentFieldName
     *            the parent field name
     * @param pSubfieldName
     *            the subfield name
     * @return the value
     */
    @SuppressWarnings("unchecked")
    public FieldValueData getValue(String pParentFieldName, String pSubfieldName) {
        Object lParentValue = valuesMap.get(pParentFieldName);
        if (lParentValue instanceof Map) {
            Map<String, FieldValueData> lSubfields =
                    (Map<String, FieldValueData>) lParentValue;
            return lSubfields.get(pSubfieldName);
        }
        return null;
    }

    /**
     * Get the map of name -> values for this container.
     * 
     * @return Map associating the field names to their values
     */
    public Map<String, Object> getValuesMap() {
        return valuesMap;
    }

    /**
     * Get labels of all fields (top-level) valued in this container.
     * 
     * @return List of valued field labels
     */
    public Collection<String> getFieldLabels() {
        return valuesMap.keySet();
    }

    /**
     * Get the number of defined values for a given top-level field.
     * 
     * @param pFieldName
     *            the field name
     * @return Number of values for the field
     */
    @SuppressWarnings("rawtypes")
	int getValueCount(String pFieldName) {
        Object lObjValue = valuesMap.get(pFieldName);
        if (null == lObjValue) {
            return 0;
        }
        else if (lObjValue instanceof List) {
            return ((List) lObjValue).size();
        }
        else {
            return 1;
        }
    }

    /**
     * Get all first level field values
     * 
     * @return List of FieldValueData for all top level field values defined in
     *         this container
     */
    public List<? extends FieldValueData> getAllTopLevelValues() {
        final List<String> lFieldLabels =
                new ArrayList<String>(getFieldLabels());
        final List<FieldValueData> lResult = new ArrayList<FieldValueData>();

        Collections.sort(lFieldLabels, String.CASE_INSENSITIVE_ORDER);

        for (String lFieldLabel : lFieldLabels) {
            if (valuesMap.containsKey(lFieldLabel)) {
                lResult.addAll(getTopLevelValues(lFieldLabel,
                        valuesMap.get(lFieldLabel)));
            }
        }

        return lResult;
    }

    /**
     * Get first level field values for a field
     * 
     * @param pFieldLabel
     *            The field label
     * @param pValue
     *            The field value
     * @return List of FieldValueData for top level field values defined in this
     *         field
     */
    @SuppressWarnings("unchecked")
    protected List<FieldValueData> getTopLevelValues(String pFieldLabel,
            Object pValue) {
        final List<FieldValueData> lFieldValueDatas;

        if (pValue == null) {
            lFieldValueDatas = Collections.emptyList();
        }
        else if (pValue instanceof FieldValueData) {
            lFieldValueDatas =
                    Collections.singletonList((FieldValueData) pValue);
        }
        else if (pValue instanceof Map) {
            final FieldValueData lMultipleFieldValueData =
                    getMultipleTopLevelValues(pFieldLabel,
                            (Map<String, Object>) pValue);

            if (lMultipleFieldValueData.getFieldValues() == null
                    || lMultipleFieldValueData.getFieldValues().isEmpty()) {
                lFieldValueDatas = Collections.emptyList();
            }
            else {
                lFieldValueDatas =
                        Collections.singletonList(lMultipleFieldValueData);
            }
        }
        else if (pValue instanceof List) {
            final List<?> lSubValues = (List<?>) pValue;

            lFieldValueDatas = new LinkedList<FieldValueData>();
            for (Object lSubValue : lSubValues) {
                lFieldValueDatas.addAll(getTopLevelValues(pFieldLabel,
                        lSubValue));
            }
        }
        else {
            throw new GDMException(
                    "Invalid object on CacheableValuesContainer "
                            + pValue.getClass());
        }

        return lFieldValueDatas;
    }

    /**
     * Get sub values for a multiple field
     * 
     * @param pFieldLabel
     *            The multiple field label
     * @param pValue
     *            The multiple field value
     * @return FieldValueData containing all sub field values defined in this
     *         multiple field
     */
    protected FieldValueData getMultipleTopLevelValues(String pFieldLabel,
            Map<String, Object> pValue) {
        final FieldValueData lMultipleField = new FieldValueData(pFieldLabel);
        final List<String> lSubFieldLabels =
                new ArrayList<String>(pValue.keySet());

        Collections.sort(lSubFieldLabels, String.CASE_INSENSITIVE_ORDER);

        for (String lSubFieldLabel : lSubFieldLabels) {
            if (pValue.containsKey(lSubFieldLabel)) {
                lMultipleField.addFieldValues(getTopLevelValues(lSubFieldLabel,
                        pValue.get(lSubFieldLabel)));
            }
        }

        return lMultipleField;
    }

    /**
     * Get all field values defined for attached files fields.
     * 
     * @return List of field values defined for attached files fields
     */
    public List<? extends AttachedFieldValueData> getAllAttachedFileValues() {
        return getAllAttachedFileValues(valuesMap.values());
    }

    /**
     * Gets the all attached file values.
     * 
     * @param pValuesMap
     *            the values map
     * @return the all attached file values
     */
    @SuppressWarnings("unchecked")
    private List<? extends AttachedFieldValueData> getAllAttachedFileValues(
            final Collection<? extends Object> pValuesList) {
        List<AttachedFieldValueData> lAttachments =
                new ArrayList<AttachedFieldValueData>();

        List<Object> lValues = new ArrayList<Object>(pValuesList);
        for (Object lObjValue : lValues) {
            if (lObjValue instanceof AttachedFieldValueData) {
                lAttachments.add((AttachedFieldValueData) lObjValue);
            }
            else if (lObjValue instanceof Map<?, ?>) {
                Map<String, FieldValueData> lSubfieldValuesMap =
                        (Map<String, FieldValueData>) lObjValue;
                lAttachments.addAll(getAllAttachedFileValues(lSubfieldValuesMap.values()));
            }
            else if (lObjValue instanceof List<?>) {
                Collection<?> lObjValuesList = (Collection<?>) lObjValue;
                if (lObjValuesList.isEmpty()) {
                    continue;
                }
                lAttachments.addAll(getAllAttachedFileValues(lObjValuesList));
            }
        }
        return lAttachments;
    }

    /**
     * Marshal this values container in a given serializable data.
     * 
     * @param pObject
     *            Serializable filled by this method.
     */
    @Override
    public void marshal(Object pObject) {
        super.marshal(pObject);

        final ValuesContainerData lSerializedContainer =
                (ValuesContainerData) pObject;
        final List<? extends FieldValueData> lRootValues =
                getAllTopLevelValues();

        lSerializedContainer.setVersion(getVersion());
        lSerializedContainer.setId(getId());
        lSerializedContainer.setType(getTypeName());
        lSerializedContainer.setProductName(getProductName());
        if (!lRootValues.isEmpty()) {
            lSerializedContainer.addFieldValues(lRootValues);
        }
    }

    /**
     * Gets the type id.
     * 
     * @return the type id
     */
    public String getTypeId() {
        return typeId;
    }

    /**
     * Gets the type name.
     * 
     * @return the type name
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * Sets the type id.
     * 
     * @param pTypeId
     *            the new type id
     */
    public void setTypeId(String pTypeId) {
        typeId = pTypeId;
    }

    /**
     * Sets the type name.
     * 
     * @param pTypeName
     *            the new type name
     */
    public void setTypeName(String pTypeName) {
        typeName = pTypeName;
    }

    /**
     * Remove a fieldValue from the field list (from both valuesList and
     * valuesMap).
     * 
     * @param pMap
     *            The map
     * @param pFieldName
     *            the field name.
     * @param pFieldValue
     *            The field value.
     */
    @SuppressWarnings("unchecked")
    public void removeFieldValue(Map<String, ?> pMap, String pFieldName,
            String pFieldValue) {
        Iterator<String> lIterator = pMap.keySet().iterator();
        while (lIterator.hasNext()) {
            String lKey = lIterator.next();
            if (pMap.get(lKey) instanceof Map<?, ?>) {
                removeFieldValue((Map<String, ?>) pMap.get(lKey), pFieldName,
                        pFieldValue);
            }
            else if (pMap.get(lKey) instanceof List<?>) {
                Iterator<?> lIterator2 = ((List<?>) pMap.get(lKey)).iterator();
                while (lIterator2.hasNext()) {
                    Object lObject = lIterator2.next();
                    if (lObject instanceof Map<?, ?>) {
                        removeFieldValue((Map<String, ?>) lObject, pFieldName,
                                pFieldValue);
                    }
                    else if (lObject instanceof AttachedFieldValueData) {
                        AttachedFieldValueData lValueData =
                                (AttachedFieldValueData) lObject;
                        if (!StringUtils.isBlank(pFieldValue)
                                && lValueData.getValue().equals(pFieldValue)) {
                            lIterator2.remove();
                        }
                    }
                }
            }
            if (lKey.equals(pFieldName)) {
                Object lObject = pMap.get(lKey);
                if (lObject instanceof AttachedFieldValueData) {
                    AttachedFieldValueData lValueData =
                            (AttachedFieldValueData) lObject;
                    if (!StringUtils.isBlank(pFieldValue)
                            && lValueData.getValue().equals(pFieldValue)) {
                        lIterator.remove();
                    }
                }
            }
        }
    }

    /**
     * Remove a fieldValue from the field list (from both valuesList and
     * valuesMap).
     * 
     * @param pFieldName
     *            the field name.
     */
    public void removeFieldValue(String pFieldName) {
        if (valuesMap.containsKey(pFieldName)) {
            valuesMap.get(pFieldName);
            valuesMap.remove(pFieldName);
        }
    }

    /**
     * Remove a subfieldValue from the field list (from both valuesList and
     * valuesMap).
     * 
     * @param pFieldName
     *            the field name.
     * @param pSubFieldName
     *            the subfield name.
     */
    @SuppressWarnings("unchecked")
    public void removeSubFieldValue(String pFieldName, String pSubFieldName) {
        /*
         * The sub field is stored both in the valuesList and the valuesMap.
         * - Get the value from the valuesMap
         * - if NULL -> nothing to remove else remove from map
         * - if fieldValueData -> nothing to remove
         * - if list<fieldValueData> -> nothing to remove
         * - if map<string,?> -> remove subfield values from the map
         * - if list<map> -> remove all subfield values from each map.
         */
        Object lObjValue = valuesMap.get(pFieldName);
        if (lObjValue == null || lObjValue instanceof FieldValueData) {
            //nothing to remove
            return;
        }
        else if (lObjValue instanceof Map<?, ?>) {
            Map<String, FieldValueData> lMap =
                    ((Map<String, FieldValueData>) lObjValue);
            lMap.remove(pSubFieldName);
        }
        else if (lObjValue instanceof List<?>) {
            final List<?> lValuesList = (List<?>) lObjValue;
            if (lValuesList.size() > 0) {
                final Object lFirstValue = lValuesList.get(0);
                if (lFirstValue instanceof Map<?, ?>) {
                    for (Object lMap : lValuesList) {
                        ((Map<String, FieldValueData>) lMap).remove(pSubFieldName);
                    }
                }
                else if (lFirstValue instanceof FieldValueData) {
                    //nothing to remove
                    return;
                }
            }
        }
    }

    /**
     * Gets the functional reference.
     * 
     * @return the functional reference
     */
    public String getFunctionalReference() {
        return functionalReference;
    }

    /**
     * Sets the functional reference.
     * 
     * @param pRef
     *            the new functional reference
     */
    public void setFunctionalReference(String pRef) {
        functionalReference = pRef;
    }

    /**
     * Gets the product name.
     * 
     * @return the product name
     */
    public String getProductName() {
        return productName;
    }

    /**
     * Sets the product name.
     * 
     * @param pProductName
     *            the new product name
     */
    public void setProductName(String pProductName) {
        productName = pProductName;
    }

    /**
     * Gets the version.
     * 
     * @return the version
     */
    public int getVersion() {
        return version;
    }

    /**
     * Sets the version.
     * 
     * @param pVersion
     *            the new version
     */
    public void setVersion(int pVersion) {
        version = pVersion;
    }

    /**
     * Sets the values map.
     * 
     * @param pMap
     *            the map
     */
    public void setValuesMap(Map<String, Object> pMap) {
        valuesMap = pMap;
    }

    /**
     * Get a list of all fields which have a valued (possibly empty) in this
     * container.
     * 
     * @return List of field label keys
     */
    public Collection<String> getValuedFieldNames() {
        return new ArrayList<String>(valuesMap.keySet());
    }

    /**
     * Adds the multiple line field datas.
     * 
     * @param pMlfds
     *            the mlfds
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void addMultipleLineFieldDatas(MultipleLineFieldData[] pMlfds) {
        for (MultipleLineFieldData lMlfd : pMlfds) {
            /*
             * Each field can be : - multiple multivalued -> List<Map> -
             * multiple -> Map <String, Object (FieldValueData ou liste de
             * FieldValueData)> - multivalued -> List<FieldValueData> -
             * simple -> FieldValueData
             */
            Object lFieldValue = null;
            if (lMlfd.isMultiLined() && lMlfd.isMultiField()) {
                lFieldValue = new ArrayList<Map<String, Object>>();
            }
            else if (lMlfd.isMultiLined()) {
                lFieldValue = new ArrayList<FieldValueData>();
            }

            final LineFieldData[] lLineFieldData = lMlfd.getLineFieldDatas();
            if (lLineFieldData != null) {
                for (LineFieldData lFd : lLineFieldData) {
                    Map<String, Object> lMap = new HashMap<String, Object>();
                    for (FieldData lFieldData : lFd.getFieldDatas()) {
                        Object lValue = null;
                        if (lFieldData.getFileValue() != null
                                && lFieldData.getFileValue().getContent() != null) {
                            AttachedFieldModificationData lAttachedField =
                                    lFieldData.getFileValue();
                            AttachedFieldValueData lAttached =
                                    new AttachedFieldValueData(
                                            lFieldData.getLabelKey());
                            lAttached.setFilename(lAttachedField.getName());
                            lAttached.setMimeType(lAttachedField.getMimeType());
                            lAttached.setId(lAttachedField.getId());

                            // Attached field updated or created
                            lAttached.setNewContent(lAttachedField.getContent());

                            lValue = lAttached;
                        }
                        else if (lFieldData.getPointerFieldValue() != null) {
                            org.topcased.gpm.business.fields.PointerFieldValueData lPointerFieldValue =
                                    lFieldData.getPointerFieldValue();
                            PointerFieldValueData lPointerFieldSerialization =
                                    new PointerFieldValueData(
                                            lFieldData.getLabelKey());
                            lPointerFieldSerialization.setReferencedFieldLabel(lPointerFieldValue.getReferencedFieldLabel());
                            lPointerFieldSerialization.setReferencedContainerId(lPointerFieldValue.getReferencedContainerId());
                            lValue = lPointerFieldSerialization;
                        }
                        else if (lFieldData.getValues() != null) {
                            String[] lValues =
                                    lFieldData.getValues().getValues();

                            if (ArrayUtils.contains(lValues, null)) {
                                lValues =
                                        (String[]) ArrayUtils.removeElement(
                                                lValues, null);
                            }

                            if (lValues != null && lValues.length > 0) {
                                if (lValues.length == 1) {
                                    lValue =
                                            new org.topcased.gpm.business.serialization.data.FieldValueData(
                                                    lFieldData.getLabelKey(),
                                                    lValues[0]);
                                }
                                else {
                                    lValue =
                                            new ArrayList<org.topcased.gpm.business.serialization.data.FieldValueData>();
                                    for (int i = 0; i < lValues.length; i++) {
                                        ((List<org.topcased.gpm.business.serialization.data.FieldValueData>) lValue).add(new org.topcased.gpm.business.serialization.data.FieldValueData(
                                                lFieldData.getLabelKey(),
                                                lValues[i]));
                                    }
                                }
                            }
                        }
                        if (lMlfd.isMultiField()) {
                            lMap.put(lFieldData.getLabelKey(), lValue);
                        }
                        else if (lMlfd.isMultiLined()) {
                            ((List) lFieldValue).add(lValue);
                        }
                        else {
                            lFieldValue = lValue;
                        }
                    }
                    if (lMlfd.isMultiField() && lMlfd.isMultiLined()) {
                        ((List) lFieldValue).add(lMap);

                        //update valuesList
                        FieldValueData lValueD =
                                new FieldValueData(lMlfd.getLabelKey());
                        for (Object lValue : lMap.values()) {
                            if (lValue instanceof FieldValueData) {
                                lValueD.addFieldValue((FieldValueData) lValue);
                            }
                            if (lValue instanceof List<?>) {
                                lValueD.addFieldValues((List<FieldValueData>) lValue);
                            }
                        }
                    }
                    else if (lMlfd.isMultiField()) {
                        lFieldValue = lMap;

                        //update valuesList
                        FieldValueData lValueD =
                                new FieldValueData(lMlfd.getLabelKey());
                        for (Object lValue : lMap.values()) {
                            if (lValue instanceof FieldValueData) {
                                lValueD.addFieldValue((FieldValueData) lValue);
                            }
                            if (lValue instanceof List<?>) {
                                lValueD.addFieldValues((List<FieldValueData>) lValue);
                            }
                        }
                    }
                }
            }
            if (lFieldValue != null) {
                getValuesMap().put(lMlfd.getLabelKey(), lFieldValue);
            }
        }
    }

    /**
     * Add a value to a simple field.
     * 
     * @param pFieldValue
     *            Value to be added
     */
    public void addValue(FieldValueData pFieldValue) {
        if (null != pFieldValue) {
            putValueInMap(valuesMap, pFieldValue);
        }
    }

    /**
     * Add a list of values to a multiple field.
     * 
     * @param pMultipleFieldName
     *            Label of the multiple field
     * @param pFieldValues
     *            List of values to be added to the multiple field
     */
    public void addValue(String pMultipleFieldName,
            Collection<FieldValueData> pFieldValues) {
        if (pFieldValues != null) {
            // Create a hash map to store the subfield values
            Map<String, Object> lSubFieldsMap =
                    new HashMap<String, Object>(pFieldValues.size());

            for (FieldValueData lSubFieldValue : pFieldValues) {
                putValueInMap(lSubFieldsMap, lSubFieldValue);
            }
            // Add map of subfields values to the valuesMap, as value of the multiple field
            putValueInMap(valuesMap, pMultipleFieldName, lSubFieldsMap);
        }
    }

    /**
     * Add a list of values to a container.
     * 
     * @param pFieldValues
     *            List of values to be added
     */
    public void addValues(List<FieldValueData> pFieldValues) {
        if (pFieldValues != null) {
            putValuesInMap(valuesMap, pFieldValues);
        }
    }

    /**
     * Add a list of values to a multiple field.
     * 
     * @param pMultipleFieldName
     *            Label of the multiple field
     * @param pSubfieldValue
     *            Value of the subfield to be added to the multiple field
     */
    private void addValue(String pMultipleFieldName,
            FieldValueData pSubfieldValue) {
        // Create a hash map to store the subfield values
        Map<String, Object> lSubFieldsMap = new HashMap<String, Object>();
        putValueInMap(lSubFieldsMap, pSubfieldValue);

        // Add map of subfields values to the valuesMap, as value of the multiple field
        putValueInMap(valuesMap, pMultipleFieldName, lSubFieldsMap);
    }

    /**
     * Set value to a sub field of a multiple field, in all occurences of this
     * sub field value. Existing value is replaced if present
     * 
     * @param pMultipleFieldName
     *            name of the multiple field containing subField to set
     * @param pSubFieldValue
     *            Value to be set (name of the subField to set is already
     *            present in pSubFieldValue.getName() )
     */
    @SuppressWarnings("unchecked")
    public void setValue(String pMultipleFieldName,
            FieldValueData pSubFieldValue) {
        if (pSubFieldValue != null) {
            /*
             * The sub field is stored both in the valuesList and the valuesMap.
             * - Get the value from the valuesMap
             * - if NULL -> add new value
             * - if map<string,?> -> remove subfield values from the map
             * - if list<map> -> remove all subfield values from each map.
             */
            Object lObjValue = valuesMap.get(pMultipleFieldName);
            if (lObjValue == null) {
                //Add new value to multiple field
                addValue(pMultipleFieldName, pSubFieldValue);
            }
            else if (lObjValue instanceof Map<?, ?>) {
                Map<String, FieldValueData> lMap =
                        ((Map<String, FieldValueData>) lObjValue);
                lMap.remove(pSubFieldValue.getName());
                lMap.put(pSubFieldValue.getName(), pSubFieldValue);
            }
            else if (lObjValue instanceof List<?>) {
                List<?> lValuesList = (List<?>) lObjValue;
                if (lValuesList.size() > 0) {
                    if (lValuesList.get(0) instanceof Map<?, ?>) {
                        for (Object lMap : lValuesList) {
                            ((Map<String, FieldValueData>) lMap).remove(pSubFieldValue.getName());
                            ((Map<String, FieldValueData>) lMap).put(
                                    pSubFieldValue.getName(), pSubFieldValue);
                        }
                    }
                }
                else {
                    addValue(pMultipleFieldName, pSubFieldValue);
                }
            }
        }
    }

    /**
     * Set value to a simple field (replace existing value if present).
     * 
     * @param pFieldValue
     *            field value to set (name of the field is already present in
     *            pFieldValue.getName())
     */
    public void setValue(FieldValueData pFieldValue) {
        // Always remove the value from the map.
        valuesMap.remove(pFieldValue.getName());

        //Add new value into the map and list
        addValue(pFieldValue);
    }

    /**
     * Gets the environment names.
     * 
     * @return the environment names
     */
    public List<String> getEnvironmentNames() {
        return environmentNames;
    }

    /**
     * Sets the environment names.
     * 
     * @param pEnvironmentNames
     *            the new environment names
     */
    public void setEnvironmentNames(List<String> pEnvironmentNames) {
        environmentNames = pEnvironmentNames;
    }

    /**
     * Add a field value for this cacheableInputData (for a first level field
     * only)
     * 
     * @param pFieldValueData
     *            the field value to add
     */
    public void addFieldValueData(FieldValueData pFieldValueData) {
        putValueInMap(valuesMap, pFieldValueData);
    }

    /**
     * Add a field value in a subField map
     * 
     * @param pSubFieldsMap
     *            the subFieldsMap
     * @param pFieldValueData
     *            the field value to add
     */
    public void addFieldValueData(Map<String, Object> pSubFieldsMap,
            FieldValueData pFieldValueData) {
        putValueInMap(pSubFieldsMap, pFieldValueData);
    }

    /**
     * Add a sub field map
     * 
     * @param pFieldName
     *            the field's name
     * @param pSubFieldsMap
     *            the sub fields map
     */
    public void addSubFieldMap(String pFieldName,
            Map<String, Object> pSubFieldsMap) {
        putValueInMap(valuesMap, pFieldName, pSubFieldsMap);
    }

    /**
     * Get the list of extension points to exclude from execution while
     * importing a ValuesContainer
     * 
     * @return the list of extension points to exclude
     */
    public Set<String> getExtentionPointsToExclude() {
        return extentionPointsToExclude;
    }

    /**
     * Set a list of extension points to exclude from execution while importing
     * a ValuesContainer
     * 
     * @param pExtentionPointsToExclude
     *            the list of extension point to exclude from execution
     */
    public void setExtentionPointsToExclude(
            final Set<String> pExtentionPointsToExclude) {
        this.extentionPointsToExclude = pExtentionPointsToExclude;
    }
}
