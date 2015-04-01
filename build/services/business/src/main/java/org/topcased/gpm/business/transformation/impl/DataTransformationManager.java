/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.transformation.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.ServiceImplBase;
import org.topcased.gpm.business.exception.ConstraintException;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.facilities.DateDisplayHintData;
import org.topcased.gpm.business.facilities.GridDisplayHintData;
import org.topcased.gpm.business.fields.AttachedFieldModificationData;
import org.topcased.gpm.business.fields.ChoiceStringHintData;
import org.topcased.gpm.business.fields.FieldAccessData;
import org.topcased.gpm.business.fields.FieldAvailableValueData;
import org.topcased.gpm.business.fields.FieldData;
import org.topcased.gpm.business.fields.FieldValueData;
import org.topcased.gpm.business.fields.LineFieldData;
import org.topcased.gpm.business.fields.MultipleLineFieldData;
import org.topcased.gpm.business.fields.TextAreaSize;
import org.topcased.gpm.business.fields.impl.CacheableFieldsContainer;
import org.topcased.gpm.business.fields.impl.CacheableValuesContainer;
import org.topcased.gpm.business.product.impl.CacheableProductType;
import org.topcased.gpm.business.serialization.data.AttachedFieldValueData;
import org.topcased.gpm.business.serialization.data.ChoiceStringDisplayHint;
import org.topcased.gpm.business.serialization.data.DisplayHint;
import org.topcased.gpm.business.serialization.data.ExternDisplayHint;
import org.topcased.gpm.business.serialization.data.PointerFieldValueData;
import org.topcased.gpm.business.serialization.data.SimpleField;
import org.topcased.gpm.business.sheet.impl.CacheableSheet;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.business.util.GridObjectsUtil;
import org.topcased.gpm.domain.facilities.ChoiceFieldDisplayType;
import org.topcased.gpm.domain.facilities.GridDisplayHintType;
import org.topcased.gpm.domain.facilities.TextFieldDisplayType;
import org.topcased.gpm.domain.fields.ChoiceFieldType;

/**
 * Data transformation manager used by data transformation service
 * 
 * @author tpanuel
 */
public class DataTransformationManager extends ServiceImplBase {
    /**
     * Creates a MultipleLineFieldData. We need to create one
     * MultipleLineFieldData per top-level field defined in the sheet type.
     * 
     * @param pRoleToken
     *            Role session token.
     * @param pSheetType
     *            Sheet type
     * @param pSheet
     *            Sheet (can be null).
     * @param pField
     *            The field.
     * @param pContainer4Env
     *            Collection of Environment to use (for choice fields).
     * @return The MultipleLineFieldData.
     */
    public MultipleLineFieldData createMultipleLineFieldData(String pRoleToken,
            CacheableSheetType pSheetType, CacheableSheet pSheet,
            org.topcased.gpm.business.serialization.data.Field pField,
            String pContainer4Env) {

        String lStateName;
        if (null != pSheet) {
            lStateName = pSheet.getCurrentStateName();
        }
        else {
            // If no sheet exist yet, we use the initial state
            // (defined in the sheet type)
            lStateName = pSheetType.getInitialStateName();
        }
        return createMultipleLineFieldData(pRoleToken, pSheetType, pSheet,
                lStateName, pField, pContainer4Env);
    }

    /**
     * Creates the multiple line field data.
     * 
     * @param pRoleToken
     *            the role token
     * @param pType
     *            the type
     * @param pValuesContainer
     *            the values container
     * @param pField
     *            the field
     * @param pContainer4Env
     *            the environments
     * @return the multiple line field data
     */
    public MultipleLineFieldData createMultipleLineFieldData(String pRoleToken,
            CacheableFieldsContainer pType,
            CacheableValuesContainer pValuesContainer,
            org.topcased.gpm.business.serialization.data.Field pField,
            String pContainer4Env) {
        return createMultipleLineFieldData(pRoleToken, pType, pValuesContainer,
                null, pField, pContainer4Env);
    }

    /**
     * Creates a MultipleLineFieldData. We need to create one
     * MultipleLineFieldData per top-level field defined in the type.
     * 
     * @param pRoleToken
     *            Role session token.
     * @param pType
     *            Type of the element
     * @param pValuesContainer
     *            Values container (can be null).
     * @param pField
     *            The field.
     * @param pEnvironments
     *            Collection of Environments to use (for choice fields).
     * @param pStateName
     *            the state name
     * @return The MultipleLineFieldData.
     */
    private MultipleLineFieldData createMultipleLineFieldData(
            String pRoleToken, CacheableFieldsContainer pType,
            CacheableValuesContainer pValuesContainer, String pStateName,
            org.topcased.gpm.business.serialization.data.Field pField,
            String pContainer4Env) {
        if (pField == null) {
            throw new NullPointerException("Field is null");
        }

        String lValuesContainerId = null;
        if (pValuesContainer != null) {
            lValuesContainerId = pValuesContainer.getId();
        }

        FieldAccessData lFieldAccess =
                getAuthService().getFieldAccess(
                        pRoleToken,
                        getAuthService().getAccessControlContextData(
                                pRoleToken, pStateName, pType.getId(), null,
                                lValuesContainerId), pField.getId());
        boolean lIsMultiField =
                pField instanceof org.topcased.gpm.business.serialization.data.MultipleField;
        boolean lIsChoiceField =
                pField instanceof org.topcased.gpm.business.serialization.data.ChoiceField;

        String lDescription = pField.getDescription();
        if ((StringUtils.isNotBlank(pField.getDescription()))
                && (StringUtils.isNotBlank(pRoleToken))) {
            //For upward compatibility. Previous method signature doesn't have roletoken parameter.
            lDescription =
                    getI18nService().getValueForUser(pRoleToken,
                            pField.getDescription());
        }

        MultipleLineFieldData lMlfData;

        lMlfData =
                new MultipleLineFieldData(pField.getLabelKey(), /* ref */
                0, (!lIsChoiceField && pField.isMultivalued()), lIsMultiField,
                        lFieldAccess.getExportable(),
                        i18nService.getValueForUser(pRoleToken,
                                pField.getLabelKey()),
                        lFieldAccess.getConfidential(), lDescription,
                        pField.getId(), lFieldAccess.getUpdatable(),
                        pField.isPointerField(),
                        /* linefielddataArray */null);

        Object lValue = null;
        if (null != pValuesContainer) {
            lValue = pValuesContainer.getValue(pField.getLabelKey());
        }

        lMlfData.setLineFieldDatas(createLineFieldDatas(pRoleToken, pType,
                pValuesContainer, pStateName, pField, pContainer4Env, lValue,
                lFieldAccess, lIsMultiField, lIsChoiceField));
        return lMlfData;
    }

    /**
     * Creation of the line field data array for a field value / a list of field
     * values ( including pointer fields)
     * 
     * @param pRoleToken
     *            current session token
     * @param pType
     *            Cacheable fields container
     * @param pValuesContainer
     *            cacheable values container
     * @param pStateName
     *            current state name (for sheets)
     * @param pField
     *            current serialization field
     * @param pEnvironments
     *            list of environments for the current product
     * @param pValue
     *            current serialization field value (typed as Object)
     * @param pFieldAccess
     *            field access control
     * @param pIsMultipleField
     *            current field is a multiple field
     * @param pIsChoiceField
     *            current field is a choice field
     * @return an array of LineFieldData representing current field value/ field
     *         value list.
     */
    private LineFieldData[] createLineFieldDatas(String pRoleToken,
            CacheableFieldsContainer pType,
            CacheableValuesContainer pValuesContainer, String pStateName,
            org.topcased.gpm.business.serialization.data.Field pField,
            String pContainer4Env, Object pValue, FieldAccessData pFieldAccess,
            boolean pIsMultipleField, boolean pIsChoiceField) {

        if (pField.isPointerField()) {
            //pointer fields -> Create line field data for pointed field values
            return createLineFieldDatasForPointerField(pRoleToken, pType,
                    pValuesContainer, pStateName, pField, pContainer4Env,
                    pValue, pFieldAccess, pIsMultipleField, pIsChoiceField);
        }
        // else
        return createLineFieldDataArray(pRoleToken, pType, pValuesContainer,
                pStateName, pField, pContainer4Env, pValue, pFieldAccess,
                pIsMultipleField, pIsChoiceField, null);
    }

    /**
     * Creation of the line field data array for a field value / a list of field
     * values without pointer field (already replaced by pointed fields)
     * 
     * @param pRoleToken
     *            current session token
     * @param pType
     *            Cacheable fields container
     * @param pValuesContainer
     *            cacheable values container
     * @param pStateName
     *            current state name (for sheets)
     * @param pField
     *            current serialization field
     * @param pEnvironments
     *            list of environments for the current product
     * @param pValue
     *            current serialization field value (typed as Object)
     * @param pFieldAccess
     *            field access control
     * @param pIsMultipleField
     *            current field is a multiple field
     * @param pIsChoiceField
     *            current field is a choice field
     * @param pPointerFieldValueData
     *            pointer field value data corresponding to this field (if any)
     * @return an array of LineFieldData representing current field value/ field
     *         value list.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private LineFieldData[] createLineFieldDataArray(
            String pRoleToken,
            CacheableFieldsContainer pType,
            CacheableValuesContainer pValuesContainer,
            String pStateName,
            org.topcased.gpm.business.serialization.data.Field pField,
            String pContainer4Env,
            Object pValue,
            FieldAccessData pFieldAccess,
            boolean pIsMultipleField,
            boolean pIsChoiceField,
            org.topcased.gpm.business.fields.PointerFieldValueData pPointerFieldValueData) {
        LineFieldData[] lLineFieldDataArray = null;

        // MULTIPLE FIELD
        if (pIsMultipleField) {
            if (pValue instanceof List) { // multivalued multiple field -> LIST<MAP>
                lLineFieldDataArray =
                        new LineFieldData[((List<?>) pValue).size()];
                int i = 0;
                for (Map lSubValues : (List<Map>) pValue) {
                    lLineFieldDataArray[i] =
                            createLineFieldData(
                                    pRoleToken,
                                    pType,
                                    pValuesContainer,
                                    pStateName,
                                    lSubValues,
                                    (org.topcased.gpm.business.serialization.data.MultipleField) pField,
                                    pContainer4Env, pPointerFieldValueData);
                    lLineFieldDataArray[i].setRef(i + 1);
                    ++i;
                }
            }
            else {
                // mono valued multiple field  -> MAP
                Map lSubValues = (Map) pValue;
                lLineFieldDataArray = new LineFieldData[1];

                lLineFieldDataArray[0] =
                        createLineFieldData(
                                pRoleToken,
                                pType,
                                pValuesContainer,
                                pStateName,
                                lSubValues,
                                (org.topcased.gpm.business.serialization.data.MultipleField) pField,
                                pContainer4Env, pPointerFieldValueData);
            }
        }
        else {

            if (!pIsChoiceField && pValue instanceof List) {
                // Multivalued fields (except Choice fields)
                int lIndex = 0;
                lLineFieldDataArray = new LineFieldData[((List) pValue).size()];

                for (org.topcased.gpm.business.serialization.data.FieldValueData lValueData : (List<org.topcased.gpm.business.serialization.data.FieldValueData>) pValue) {
                    FieldData[] lFieldDataArray;
                    lFieldDataArray =
                            new FieldData[] { createFieldData(pRoleToken,
                                    pType, pValuesContainer, lValueData,
                                    pField, pFieldAccess, pContainer4Env,
                                    pPointerFieldValueData, false) };
                    lLineFieldDataArray[lIndex] =
                            new LineFieldData(lIndex + 1, lFieldDataArray);

                    lIndex++;
                }
            }
            else if (null != pValue) {
                // Not null value
                FieldData[] lFieldDataArray =
                        new FieldData[] { createFieldData(pRoleToken, pType,
                                pValuesContainer, pValue, pField, pFieldAccess,
                                pContainer4Env, pPointerFieldValueData, false) };

                lLineFieldDataArray =
                        new LineFieldData[] { new LineFieldData(1,
                                lFieldDataArray) };
            }
            else {
                // Null value
                FieldData[] lFieldDataArray =
                        new FieldData[] { createFieldData(pRoleToken, pType,
                                pValuesContainer, null /*pValue*/, pField,
                                pFieldAccess, pContainer4Env,
                                pPointerFieldValueData, false) };

                lLineFieldDataArray =
                        new LineFieldData[] { new LineFieldData(0,
                                lFieldDataArray) };
            }
        }
        return lLineFieldDataArray;
    }

    /**
     * Creates the line field data.
     * 
     * @param pRoleToken
     *            the role token
     * @param pType
     *            the type
     * @param pValuesContainer
     *            the values container
     * @param pStateName
     *            the state name
     * @param pValueObj
     *            the value object
     * @param pMultiField
     *            the multiple field
     * @param pContainer4Env
     *            Collection of environments
     * @param pPointerFieldValueData
     *            Pointer field value data associated to this field (if any)
     * @return the line field data
     */
    @SuppressWarnings("rawtypes")
	public LineFieldData createLineFieldData(
            String pRoleToken,
            CacheableFieldsContainer pType,
            CacheableValuesContainer pValuesContainer,
            String pStateName,
            Map pValueObj,
            org.topcased.gpm.business.serialization.data.MultipleField pMultiField,
            String pContainer4Env,
            org.topcased.gpm.business.fields.PointerFieldValueData pPointerFieldValueData) {
        final List<org.topcased.gpm.business.serialization.data.Field> lSubfields =
                pMultiField.getFields();
        FieldData[] lFieldDataArray = new FieldData[lSubfields.size()];
        String lValuesContainerId = null;
        if (pValuesContainer != null) {
            lValuesContainerId = pValuesContainer.getId();
        }

        int i = 0;
        for (org.topcased.gpm.business.serialization.data.Field lSubField : lSubfields) {
            Object lSubValueObj = null;
            if (null != pValueObj) {
                lSubValueObj = pValueObj.get(lSubField.getLabelKey());
            }

            FieldAccessData lFieldAccess =
                    getAuthService().getFieldAccess(
                            pRoleToken,
                            getAuthService().getAccessControlContextData(
                                    pRoleToken, pStateName, pType.getId(),
                                    null, lValuesContainerId),
                            lSubField.getId());

            // Add line field data.
            lFieldDataArray[i] =
                    createFieldData(pRoleToken, pType, pValuesContainer,
                            lSubValueObj, lSubField, lFieldAccess,
                            pContainer4Env, pPointerFieldValueData, false);
            i++;
        }
        return new LineFieldData(pValueObj == null ? 0 : 1, lFieldDataArray);
    }

    /**
     * Create a field data for a pointer field 1. Get pointed value 2. Create a
     * field data after having replaced pointer value by pointed value.
     * 
     * @param pRoleToken
     *            the role token
     * @param pType
     *            the type
     * @param pValuesContainer
     *            the values container
     * @param pValueObj
     *            the pointer field value object
     * @param pField
     *            the field
     * @param pFieldAccess
     *            the field access
     * @param pEnvironments
     *            the environments of current product
     * @return the field data for the pointer field
     */
    private FieldData createPointerFieldData(
            String pRoleToken,
            CacheableFieldsContainer pType,
            CacheableValuesContainer pValuesContainer,
            Object pValueObj,
            org.topcased.gpm.business.serialization.data.Field pField,
            FieldAccessData pFieldAccess,
            String pContainer4Env,
            org.topcased.gpm.business.fields.PointerFieldValueData pPointerFieldValueData) {
        // SEPARATE MANAGEMENT OF POINTER FIELDS : replace pointer value by pointed value.

        org.topcased.gpm.business.fields.PointerFieldValueData lPointerFieldValueData =
                null;
        //1. Get Pointed value(s)
        Object lPointedValue = pValueObj;
        if (pValueObj instanceof List) {
            // When pointer field references several values
            if (((List<?>) pValueObj).get(0) instanceof PointerFieldValueData) {
                throw new GDMException(
                        "A field inside a multiple field cannot be multivalued.");
            }
        }
        else if (pValueObj instanceof PointerFieldValueData) {
            //Pointer field has only one value
            PointerFieldValueData lPointerFieldValue =
                    (PointerFieldValueData) pValueObj;

            // Get pointed value
            lPointedValue =
                    fieldsManager.getPointedFieldValue(pRoleToken,
                            lPointerFieldValue.getName(),
                            lPointerFieldValue.getReferencedContainerId(),
                            lPointerFieldValue.getReferencedFieldLabel());

            if (pPointerFieldValueData == null) {
                lPointerFieldValueData =
                        new org.topcased.gpm.business.fields.PointerFieldValueData(
                                lPointerFieldValue.getReferencedContainerId(),
                                lPointerFieldValue.getReferencedFieldLabel());
            }
        }
        // 2. Call field data creation method after having replaced pointer value by pointed value

        if (pPointerFieldValueData != null) {
            lPointerFieldValueData = pPointerFieldValueData;
        }

        return createFieldData(pRoleToken, pType, pValuesContainer,
                lPointedValue, pField, pFieldAccess, pContainer4Env,
                lPointerFieldValueData, true);
    }

    /**
     * Creates the field data .
     * 
     * @param pRoleToken
     *            the role token
     * @param pType
     *            the type
     * @param pValuesContainer
     *            the values container
     * @param pValueObj
     *            the value obj
     * @param pField
     *            the field
     * @param pFieldAccess
     *            the field access
     * @param pEnv
     *            the env
     * @param pContainsPointedValue
     *            the pointer value has already been replaced by pointed value
     * @return the field data
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private FieldData createFieldData(
            String pRoleToken,
            CacheableFieldsContainer pType,
            CacheableValuesContainer pValuesContainer,
            Object pValueObj,
            org.topcased.gpm.business.serialization.data.Field pField,
            FieldAccessData pFieldAccess,
            String pContainer4Env,
            org.topcased.gpm.business.fields.PointerFieldValueData pPointerFieldValueData,
            boolean pContainsPointedValue) {
        if (pField.isPointerField() && !pContainsPointedValue
                && pValueObj != null) {
            return createPointerFieldData(pRoleToken, pType, pValuesContainer,
                    pValueObj, pField, pFieldAccess, pContainer4Env,
                    pPointerFieldValueData);
        }

        FieldData lFieldData = new FieldData();

        lFieldData.setLabelKey(pField.getLabelKey());
        if (StringUtils.isNotBlank(pField.getDescription())) {
            lFieldData.setDescription(getI18nService().getValueForUser(
                    pRoleToken, pField.getDescription()));
        }
        else {
            lFieldData.setDescription(pField.getDescription());
        }

        lFieldData.setI18nName(i18nService.getValueForUser(pRoleToken,
                pField.getLabelKey()));
        lFieldData.setFieldId(pField.getId());

        lFieldData.setMandatory(pFieldAccess.getMandatory());
        lFieldData.setConfidential(pFieldAccess.getConfidential());
        lFieldData.setUpdatable(pFieldAccess.getUpdatable());
        lFieldData.setExportable(pFieldAccess.getExportable());
        lFieldData.setPointerField(pField.isPointerField());
        lFieldData.setPointerFieldValue(pPointerFieldValueData);

        if (pField instanceof SimpleField) {
            lFieldData.setMaxSize(((SimpleField) pField).getSizeAsInt());
        }

        // Search external display hint and set associated display type
        org.topcased.gpm.business.serialization.data.DisplayHint lTmpDisplayHint =
                pType.getDisplayHint(pField.getLabelKey());
        if (lTmpDisplayHint instanceof org.topcased.gpm.business.serialization.data.ExternDisplayHint) {
            org.topcased.gpm.business.serialization.data.ExternDisplayHint lExternDisplayHint =
                    null;
            lExternDisplayHint = (ExternDisplayHint) lTmpDisplayHint;
            lFieldData.setDisplayType(lExternDisplayHint.getType());
        }

        if (pField instanceof org.topcased.gpm.business.serialization.data.ChoiceField) {
            org.topcased.gpm.business.serialization.data.ChoiceField lChoiceField =
                    (org.topcased.gpm.business.serialization.data.ChoiceField) pField;

            // Set field type
            if (lChoiceField.isMultivalued()) {
                lFieldData.setFieldType(ChoiceFieldType.CHOICE_MULTIPLE.getValue());
            }
            else {
                lFieldData.setFieldType(ChoiceFieldType.CHOICE_SINGLE.getValue());
            }

            // Set the display type
            String lDisplayType = StringUtils.EMPTY;
            DisplayHint lDisplayHint =
                    pType.getDisplayHint(pField.getLabelKey());
            if (lDisplayHint instanceof org.topcased.gpm.business.serialization.data.ChoiceDisplayHint) {
                org.topcased.gpm.business.serialization.data.ChoiceDisplayHint lChoiceDisplayHint;
                lChoiceDisplayHint =
                        (org.topcased.gpm.business.serialization.data.ChoiceDisplayHint) lDisplayHint;
                if (lChoiceDisplayHint != null) {
                    if (lChoiceDisplayHint.isList()) {
                        if (lChoiceField.isMultivalued()) {
                            lDisplayType =
                                    ChoiceFieldDisplayType.LIST.getValue();
                        }
                        else {
                            lDisplayType =
                                    ChoiceFieldDisplayType.COMBO.getValue();
                        }
                    }
                    else {
                        if (lChoiceField.isMultivalued()) {
                            lDisplayType =
                                    ChoiceFieldDisplayType.CHECKBOX.getValue();
                        }
                        else {
                            lDisplayType =
                                    ChoiceFieldDisplayType.RADIO.getValue();
                        }
                    }
                    if (StringUtils.isNotEmpty(lChoiceDisplayHint.getImageType())) {
                        lDisplayType = lChoiceDisplayHint.getImageType();
                    }
                }
            }
            else if (lDisplayHint instanceof org.topcased.gpm.business.serialization.data.ChoiceTreeDisplayHint) {
                lDisplayType = ChoiceFieldDisplayType.TREE.getValue();
            }
            lFieldData.setDisplayType(lDisplayType);

            // Set choice usable field values
            final String[] lAvailableValues =
                    getEnvironmentDao().getContainerCategoryValues(
                            pContainer4Env, lChoiceField.getCategoryName()).toArray(
                            new String[0]);

            lFieldData.setFieldAvailableValueData(new FieldAvailableValueData(
                    lAvailableValues));

            // Set default value
            lFieldData.setDefaultValue(lChoiceField.getDefaultValue());

            // Set actual values
            String[] lValues;
            if (null != pValueObj) {
                if (pValueObj instanceof org.topcased.gpm.business.serialization.data.FieldValueData) {
                    org.topcased.gpm.business.serialization.data.FieldValueData lValueData;
                    lValueData =
                            (org.topcased.gpm.business.serialization.data.FieldValueData) pValueObj;
                    lValues = new String[] { lValueData.getValue() };
                }
                else if (pValueObj instanceof List) {
                    List<org.topcased.gpm.business.serialization.data.FieldValueData> lValuesData;
                    lValuesData = (List) pValueObj;
                    lValues = new String[lValuesData.size()];

                    int i = 0;
                    for (org.topcased.gpm.business.serialization.data.FieldValueData lValueData : lValuesData) {
                        lValues[i++] = lValueData.getValue();
                    }
                }
                else {
                    throw new GDMException("Invalid object of class "
                            + pValueObj.getClass().getName());
                }
            }
            else {
                if (!ArrayUtils.isEmpty(lAvailableValues)
                        && StringUtils.isNotBlank(lChoiceField.getDefaultValue())
                        && Arrays.asList(lAvailableValues).contains(
                                lChoiceField.getDefaultValue())) {
                    lValues = new String[] { lChoiceField.getDefaultValue() };
                }
                else {
                    lValues = new String[0];
                }
            }
            lFieldData.setValues(new FieldValueData(lValues));
        }
        else if (pField instanceof org.topcased.gpm.business.serialization.data.SimpleField) {
            org.topcased.gpm.business.serialization.data.SimpleField lSimpleField;
            lSimpleField =
                    (org.topcased.gpm.business.serialization.data.SimpleField) pField;

            // Set field type
            lFieldData.setFieldType(lSimpleField.getValueType());

            // Set display type for String
            if (lSimpleField.getValueType().equalsIgnoreCase("string")) {

                // Set Max Size
                lFieldData.setMaxSize(lSimpleField.getSizeAsInt());

                org.topcased.gpm.business.serialization.data.DisplayHint lDisplayHint;
                lDisplayHint = pType.getDisplayHint(pField.getLabelKey());

                if (lDisplayHint instanceof org.topcased.gpm.business.serialization.data.TextDisplayHint) {
                    org.topcased.gpm.business.serialization.data.TextDisplayHint lTextDisplayHint;
                    lTextDisplayHint =
                            (org.topcased.gpm.business.serialization.data.TextDisplayHint) lDisplayHint;
                    lFieldData.setDisplayType(lTextDisplayHint.getDisplayType());
                    if (lTextDisplayHint.getHeight() != 0
                            && lTextDisplayHint.getWidth() != 0) {
                        lFieldData.setTextAreaSize(new TextAreaSize(
                                lTextDisplayHint.getWidth(),
                                lTextDisplayHint.getHeight()));
                    }
                }
                else if (lDisplayHint instanceof org.topcased.gpm.business.serialization.data.GridDisplayHint) {
                    setGridOption(lFieldData, lDisplayHint);
                }
                else if (lDisplayHint instanceof ChoiceStringDisplayHint) {
                    ChoiceStringDisplayHint lChoiceStringDisplayHint;
                    lChoiceStringDisplayHint =
                            (ChoiceStringDisplayHint) lDisplayHint;
                    lFieldData.setDisplayType(ChoiceStringDisplayHint.getHintType());
                    lFieldData.setChoiceStringOptions(new ChoiceStringHintData(
                            lChoiceStringDisplayHint.getExtensionPointName(),
                            lChoiceStringDisplayHint.isStrict()));
                }
                else if (lFieldData.getDisplayType() == null) {
                    lFieldData.setDisplayType(TextFieldDisplayType.SINGLE_LINE.getValue());
                }
            }

            // Set display type for Date
            if (lSimpleField.getValueType().equalsIgnoreCase("date")) {
                org.topcased.gpm.business.serialization.data.DisplayHint lDisplayHint;
                lDisplayHint = pType.getDisplayHint(pField.getLabelKey());

                if (lDisplayHint instanceof org.topcased.gpm.business.serialization.data.DateDisplayHint) {
                    org.topcased.gpm.business.serialization.data.DateDisplayHint lDateDisplayHint;
                    lDateDisplayHint =
                            (org.topcased.gpm.business.serialization.data.DateDisplayHint) lDisplayHint;

                    DateDisplayHintData lDisplayHintData =
                            new DateDisplayHintData(
                                    lDateDisplayHint.getFormat().toLowerCase(),
                                    lDateDisplayHint.hasTime());
                    lFieldData.setDateOptions(lDisplayHintData);
                }
            }
            // Set default value
            lFieldData.setDefaultValue(lSimpleField.getDefaultValue());

            String[] lValues;
            if (pValueObj instanceof org.topcased.gpm.business.serialization.data.FieldValueData) {
                org.topcased.gpm.business.serialization.data.FieldValueData lValueData;
                lValueData =
                        (org.topcased.gpm.business.serialization.data.FieldValueData) pValueObj;
                lValues = new String[] { lValueData.getValue() };
            }
            else if (pValueObj instanceof List) {
                lValues = new String[((List) pValueObj).size()];
                int i = 0;
                for (org.topcased.gpm.business.serialization.data.FieldValueData lValueData : (List<org.topcased.gpm.business.serialization.data.FieldValueData>) pValueObj) {
                    lValues[i] = lValueData.getValue();
                    i++;
                }
            }
            else {
                lValues = new String[] { lSimpleField.getDefaultValue() };
            }
            lFieldData.setValues(new FieldValueData(lValues));
        }
        else if (pField instanceof org.topcased.gpm.business.serialization.data.AttachedField) {
            // Set field type
            lFieldData.setFieldType("FILE");
            lFieldData.setDisplayType("FILE");

            AttachedFieldModificationData lFileValue;
            lFileValue = new AttachedFieldModificationData();

            if (pValueObj instanceof AttachedFieldValueData) {
                org.topcased.gpm.business.serialization.data.AttachedFieldValueData lValueData;
                lValueData =
                        (org.topcased.gpm.business.serialization.data.AttachedFieldValueData) pValueObj;
                if (null != lValueData.getNewContent()) {
                    lFileValue.setContent(lValueData.getNewContent());
                }
                lFileValue.setName(lValueData.getValue());
                lFileValue.setMimeType(lValueData.getMimeType());
                lFileValue.setId(lValueData.getId());
            }
            // FIXME WARNING : MULTIVALUED ATTACHED FIELDS ARE NOT TAKEN INTO ACCOUNT INSIDE MULTIPLE FIELDS .
            lFieldData.setFileValue(lFileValue);
        }
        else {
            throw new GDMException("Unhandled field type "
                    + pField.getClass().getName());
        }
        return lFieldData;
    }

    private void setGridOption(
            FieldData pFieldData,
            org.topcased.gpm.business.serialization.data.DisplayHint pDisplayHint) {
        org.topcased.gpm.business.serialization.data.GridDisplayHint lGridDisplayHint;
        lGridDisplayHint =
                (org.topcased.gpm.business.serialization.data.GridDisplayHint) pDisplayHint;
        pFieldData.setDisplayType(GridDisplayHintType.GRID.getValue());
        if (lGridDisplayHint.getHeight() != 0
                && lGridDisplayHint.getWidth() != 0) {
            pFieldData.setTextAreaSize(new TextAreaSize(
                    lGridDisplayHint.getWidth(), lGridDisplayHint.getHeight()));
        }

        GridDisplayHintData lGridDisplayHintData =
                GridObjectsUtil.createGridDisplayHintData(lGridDisplayHint);
        pFieldData.setGridOptions(lGridDisplayHintData);

        //Set the first initial value: 'line0'
        String lInitialValue =
                "line" + 0 + lGridDisplayHintData.getColumnSeparator();

        for (int i = 0; i < lGridDisplayHintData.getColumns().length; i++) {
            lInitialValue += "_";
            if (i == lGridDisplayHintData.getColumns().length - 1) {
                lInitialValue += "\n";
            }
            else {
                lInitialValue += lGridDisplayHintData.getColumnSeparator();
            }
        }
        pFieldData.setGridInitialItem(lInitialValue);
    }

    /**
     * Create a line field data array for a pointer field
     * 
     * @param pRoleToken
     *            current session token
     * @param pType
     *            cacheable fields container
     * @param pValuesContainer
     *            cacheable values container
     * @param pStateName
     *            current state name (for sheet)
     * @param pField
     *            current serialization field
     * @param pEnvironments
     *            list of environments present in the product
     * @param pValue
     *            current serialization field value (typed as Object)
     * @param pFieldAccess
     *            field access control
     * @param pIsMultipleField
     *            current field is a multiple field
     * @param pIsChoiceField
     *            current field is a choice field
     * @return an array of line field datas, with all values pointed
     */
    @SuppressWarnings("unchecked")
    private LineFieldData[] createLineFieldDatasForPointerField(
            String pRoleToken, CacheableFieldsContainer pType,
            CacheableValuesContainer pValuesContainer, String pStateName,
            org.topcased.gpm.business.serialization.data.Field pField,
            String pContainer4Env, Object pValue, FieldAccessData pFieldAccess,
            boolean pIsMultipleField, boolean pIsChoiceField) {
        if (!pField.isPointerField()) {
            throw new ConstraintException("Field '" + pField.getLabelKey()
                    + "' is not a pointer field.");
        }
        if ((pType instanceof CacheableProductType)) {
            throw new ConstraintException(
                    "Pointer field can only belong to a sheet type or a link type");
        }

        // Create empty Line array
        LineFieldData[] lLineFieldDataArray = new LineFieldData[0];
        if (pValue != null) {
            if (pValue instanceof List) {
                // When pointer field references several values
                List<PointerFieldValueData> lPointerFieldValues =
                        (List<PointerFieldValueData>) pValue;

                for (PointerFieldValueData lPointerFieldValue : lPointerFieldValues) {
                    // Get pointed value
                    Object lPointedValue =
                            fieldsManager.getPointedFieldValue(
                                    pRoleToken,
                                    lPointerFieldValue.getName(),
                                    lPointerFieldValue.getReferencedContainerId(),
                                    lPointerFieldValue.getReferencedFieldLabel());
                    org.topcased.gpm.business.fields.PointerFieldValueData lPointerFieldValueData =
                            new org.topcased.gpm.business.fields.PointerFieldValueData(
                                    lPointerFieldValue.getReferencedContainerId(),
                                    lPointerFieldValue.getReferencedFieldLabel());
                    // Add line field data array for pointed value to global pointer field value
                    lLineFieldDataArray =
                            (LineFieldData[]) ArrayUtils.addAll(
                                    createLineFieldDataArray(pRoleToken, pType,
                                            pValuesContainer, pStateName,
                                            pField, pContainer4Env,
                                            lPointedValue, pFieldAccess,
                                            pIsMultipleField, pIsChoiceField,
                                            lPointerFieldValueData),
                                    lLineFieldDataArray);
                }
            }
            else if (pValue instanceof PointerFieldValueData) {
                PointerFieldValueData lPointerFieldValue =
                        (PointerFieldValueData) pValue;

                // Get pointed value
                Object lPointedValue =
                        fieldsManager.getPointedFieldValue(pRoleToken,
                                lPointerFieldValue.getName(),
                                lPointerFieldValue.getReferencedContainerId(),
                                lPointerFieldValue.getReferencedFieldLabel());

                org.topcased.gpm.business.fields.PointerFieldValueData lPointerFieldValueData =
                        new org.topcased.gpm.business.fields.PointerFieldValueData(
                                lPointerFieldValue.getReferencedContainerId(),
                                lPointerFieldValue.getReferencedFieldLabel());

                // Build pointed line field data.
                lLineFieldDataArray =
                        createLineFieldDataArray(pRoleToken, pType,
                                pValuesContainer, pStateName, pField,
                                pContainer4Env, lPointedValue, pFieldAccess,
                                pIsMultipleField, pIsChoiceField,
                                lPointerFieldValueData);
            }
            else {
                throw new GDMException(
                        "Invalid pointer field value for field '"
                                + pField.getLabelKey() + "'.");
            }
        }
        return lLineFieldDataArray;
    }
}