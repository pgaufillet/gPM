/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.fields;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.ServiceImplBase;
import org.topcased.gpm.business.dynamic.DynamicValuesContainerAccessFactory;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.exception.InvalidFieldValueException;
import org.topcased.gpm.business.exception.InvalidNameException;
import org.topcased.gpm.business.exception.InvalidValueException;
import org.topcased.gpm.business.exception.MandatoryValuesException;
import org.topcased.gpm.business.exception.UnmodifiableElementException;
import org.topcased.gpm.business.fields.impl.CacheableFieldsContainer;
import org.topcased.gpm.business.fields.impl.CacheableValuesContainer;
import org.topcased.gpm.business.product.impl.CacheableProduct;
import org.topcased.gpm.business.scalar.BooleanValueData;
import org.topcased.gpm.business.scalar.DateValueData;
import org.topcased.gpm.business.scalar.IntegerValueData;
import org.topcased.gpm.business.scalar.RealValueData;
import org.topcased.gpm.business.scalar.ScalarValueData;
import org.topcased.gpm.business.scalar.StringValueData;
import org.topcased.gpm.business.search.impl.fields.UsableFieldsManager;
import org.topcased.gpm.business.serialization.data.AttachedFieldValueData;
import org.topcased.gpm.business.serialization.data.FieldValueData;
import org.topcased.gpm.business.serialization.data.PointerFieldValueData;
import org.topcased.gpm.business.sheet.impl.CacheableSheet;
import org.topcased.gpm.business.values.field.BusinessField;
import org.topcased.gpm.business.values.field.impl.pointed.AbstractBusinessPointedField;
import org.topcased.gpm.business.values.field.multivalued.impl.pointed.BusinessMultivaluedPointedField;
import org.topcased.gpm.business.values.field.simple.impl.pointed.BusinessAttachedPointedField;
import org.topcased.gpm.business.values.field.simple.impl.pointed.BusinessBooleanPointedField;
import org.topcased.gpm.business.values.field.simple.impl.pointed.BusinessChoicePointedField;
import org.topcased.gpm.business.values.field.simple.impl.pointed.BusinessDatePointedField;
import org.topcased.gpm.business.values.field.simple.impl.pointed.BusinessIntegerPointedField;
import org.topcased.gpm.business.values.field.simple.impl.pointed.BusinessPointerPointedField;
import org.topcased.gpm.business.values.field.simple.impl.pointed.BusinessRealPointedField;
import org.topcased.gpm.business.values.field.simple.impl.pointed.BusinessStringPointedField;
import org.topcased.gpm.business.values.field.virtual.VirtualFieldType;
import org.topcased.gpm.business.values.field.virtual.impl.pointed.BusinessVirtualPointedField;
import org.topcased.gpm.domain.dictionary.Category;
import org.topcased.gpm.domain.dictionary.CategoryDao;
import org.topcased.gpm.domain.dictionary.CategoryValue;
import org.topcased.gpm.domain.dictionary.CategoryValueDao;
import org.topcased.gpm.domain.dictionary.Dictionary;
import org.topcased.gpm.domain.facilities.FieldGroup;
import org.topcased.gpm.domain.facilities.FieldGroupDao;
import org.topcased.gpm.domain.fields.AttachedField;
import org.topcased.gpm.domain.fields.AttachedFieldDao;
import org.topcased.gpm.domain.fields.AttachedFieldValueDao;
import org.topcased.gpm.domain.fields.BooleanValue;
import org.topcased.gpm.domain.fields.BooleanValueDao;
import org.topcased.gpm.domain.fields.ChoiceField;
import org.topcased.gpm.domain.fields.ChoiceFieldDao;
import org.topcased.gpm.domain.fields.DateValue;
import org.topcased.gpm.domain.fields.DateValueDao;
import org.topcased.gpm.domain.fields.Field;
import org.topcased.gpm.domain.fields.FieldDao;
import org.topcased.gpm.domain.fields.FieldType;
import org.topcased.gpm.domain.fields.FieldsContainer;
import org.topcased.gpm.domain.fields.IntegerValue;
import org.topcased.gpm.domain.fields.IntegerValueDao;
import org.topcased.gpm.domain.fields.MultipleField;
import org.topcased.gpm.domain.fields.MultipleFieldDao;
import org.topcased.gpm.domain.fields.PointerFieldAttributes;
import org.topcased.gpm.domain.fields.PointerFieldAttributesDao;
import org.topcased.gpm.domain.fields.RealValue;
import org.topcased.gpm.domain.fields.RealValueDao;
import org.topcased.gpm.domain.fields.ScalarValue;
import org.topcased.gpm.domain.fields.ScalarValueDao;
import org.topcased.gpm.domain.fields.SimpleField;
import org.topcased.gpm.domain.fields.SimpleFieldDao;
import org.topcased.gpm.domain.fields.StringValue;
import org.topcased.gpm.domain.fields.StringValueDao;
import org.topcased.gpm.domain.fields.ValuesContainer;
import org.topcased.gpm.domain.util.FieldsUtil;
import org.topcased.gpm.domain.util.IdentityFieldVisitor;
import org.topcased.gpm.util.iterator.GpmIterator;
import org.topcased.gpm.util.resources.IResourcesLoader;
import org.topcased.gpm.util.validator.MandatoryFieldsValidator;

/**
 * Manage the fields & values containers.
 * 
 * @author llatil
 */
public class FieldsManager extends ServiceImplBase {
    public static final String GPM_IDS = "GPM_IDS";

    /** The Constant DEFAULT_MANDATORY_ACCESS. */
    public static final Boolean DEFAULT_MANDATORY_ACCESS = Boolean.FALSE;

    /** The Constant DEFAULT_UPDATABLE_ACCESS. */
    public static final Boolean DEFAULT_UPDATABLE_ACCESS = Boolean.TRUE;

    /** The Constant DEFAULT_EXPORTABLE_ACCESS. */
    public static final Boolean DEFAULT_EXPORTABLE_ACCESS = Boolean.TRUE;

    /** The Constant DEFAULT_CONFIDENTIAL_ACCESS. */
    public static final Boolean DEFAULT_CONFIDENTIAL_ACCESS = Boolean.FALSE;

    /**
     * Obtain the pointed field value from the pointer field label, and
     * references to pointed field.
     * 
     * @param pRoleToken
     *            session token
     * @param pFieldLabel
     *            pointer field label
     * @param pReferencedContainerId
     *            ID of pointed container
     * @param pReferencedFieldLabel
     *            pointed field label
     * @return the pointed field value object
     */
    public Object getPointedFieldValue(String pRoleToken, String pFieldLabel,
            String pReferencedContainerId, String pReferencedFieldLabel) {
        Object lValue;

        //Get pointed container in cache
        CacheableValuesContainer lCacheablePointedContainer =
                getCachedElement(CacheableValuesContainer.class,
                        pReferencedContainerId, CACHE_MUTABLE_OBJECT);

        if (isPointedFieldVirtual(pReferencedFieldLabel)) {
            // If the values container is in cache, use it.
            if (lCacheablePointedContainer != null) {
                lValue =
                        getVirtualPointedFieldValue(pReferencedFieldLabel,
                                pFieldLabel, lCacheablePointedContainer);
            }
            else {
                lValue =
                        getVirtualPointedFieldValue(pFieldLabel,
                                pReferencedContainerId, pReferencedFieldLabel);
            }
        }
        else {
            // If the values container is in cache, use it.
            if (lCacheablePointedContainer != null) {
                // Get parent field and subfield pointed value
                CacheableFieldsContainer lPointedFieldsContainer =
                        getCachedFieldsContainer(
                                lCacheablePointedContainer.getTypeId(),
                                CACHE_IMMUTABLE_OBJECT);
                lValue =
                        getPointedFieldValue(pReferencedFieldLabel,
                                pFieldLabel, lCacheablePointedContainer,
                                lPointedFieldsContainer);
            }
            else {
                final ValuesContainer lContainer =
                        getValuesContainerDao().load(pReferencedContainerId);
                lValue =
                        DynamicValuesContainerAccessFactory.getInstance().getAccessor(
                                lContainer.getDefinition().getId()).getFieldValue(
                                pReferencedFieldLabel, lContainer);
            }
        }

        return lValue;
    }

    /**
     * Check if referenced field label matches a virtual field.
     * 
     * @param pReferencedFieldLabel
     *            pointed field label
     * @return boolean
     */
    protected boolean isPointedFieldVirtual(String pReferencedFieldLabel) {
        boolean lIsVirtualField = false;
        if (pReferencedFieldLabel.startsWith("$")) {
            lIsVirtualField = pReferencedFieldLabel.equals(VirtualFieldType.$SHEET_STATE.getValue())
                || pReferencedFieldLabel.equals(VirtualFieldType.$SHEET_REFERENCE.getValue())
                || pReferencedFieldLabel.equals(VirtualFieldType.$SHEET_TYPE.getValue())
                || pReferencedFieldLabel.equals(VirtualFieldType.$PRODUCT_NAME.getValue())
                || pReferencedFieldLabel.equals(VirtualFieldType.$PRODUCT_DESCRIPTION.getValue());
            if (!lIsVirtualField) {
                throw new GDMException("Incorrect virtual pointed field value "
                        + pReferencedFieldLabel);
            }
        }
        return lIsVirtualField;
    }

    /**
     * Get the pointed field value for virtual fields using cache
     * 
     * @param pFieldLabel
     *            pointer field name
     * @param pReferencedFieldLabel
     *            virtual pointed field name
     * @param pPointedContainer
     *            pointed container
     * @return pointed field value
     */
    protected Object getVirtualPointedFieldValue(String pFieldLabel,
            String pReferencedFieldLabel,
            CacheableValuesContainer pPointedContainer) {
        String lStringValue = null;
        if (pReferencedFieldLabel.equals(VirtualFieldType.$SHEET_STATE.getValue())) {
            if (pPointedContainer instanceof CacheableSheet) {
                lStringValue =
                        ((CacheableSheet) pPointedContainer).getCurrentStateName();
            }
        }
        else if (pReferencedFieldLabel.equals(VirtualFieldType.$SHEET_REFERENCE.getValue())) {
            lStringValue = pPointedContainer.getFunctionalReference();
        }
        else if (pReferencedFieldLabel.equals(VirtualFieldType.$SHEET_TYPE.getValue())) {
            lStringValue = pPointedContainer.getTypeName();
        }
        else if (pReferencedFieldLabel.equals(VirtualFieldType.$PRODUCT_NAME.getValue())) {
            lStringValue = pPointedContainer.getProductName();
        }
        else if (pReferencedFieldLabel.equals(VirtualFieldType.$PRODUCT_DESCRIPTION.getValue())) {
            lStringValue =
                    ((CacheableProduct) pPointedContainer).getDescription();
        }

        org.topcased.gpm.business.serialization.data.FieldValueData lFieldValue =
                new org.topcased.gpm.business.serialization.data.FieldValueData(
                        pFieldLabel, lStringValue);

        return lFieldValue;
    }

    /**
     * Get the pointed field value for virtual fields without cache
     * 
     * @param pFieldLabel
     *            pointer field label
     * @param pReferencedContainerId
     *            pointed container id
     * @param pReferencedFieldLabel
     *            virtual field name
     * @return pointed field value
     */
    protected Object getVirtualPointedFieldValue(String pFieldLabel,
            String pReferencedContainerId, String pReferencedFieldLabel) {
        String lStringValue = null;
        if (pReferencedFieldLabel.equals(VirtualFieldType.$SHEET_STATE.getValue())) {
            lStringValue = sheetDao.getStateName(pReferencedContainerId);
        }
        else if (pReferencedFieldLabel.equals(VirtualFieldType.$SHEET_REFERENCE.getValue())) {
            lStringValue =
                    getValuesContainerDao().getFunctionalReference(
                            pReferencedContainerId);
        }
        else if (pReferencedFieldLabel.equals(VirtualFieldType.$SHEET_TYPE.getValue())) {
            lStringValue =
                    getValuesContainerDao().getTypeName(pReferencedContainerId);
        }
        else if (pReferencedFieldLabel.equals(VirtualFieldType.$PRODUCT_NAME.getValue())) {
            lStringValue = sheetDao.getProductName(pReferencedContainerId);
        }

        org.topcased.gpm.business.serialization.data.FieldValueData lFieldValue =
                new org.topcased.gpm.business.serialization.data.FieldValueData(
                        pFieldLabel, lStringValue);

        return lFieldValue;
    }

    /**
     * Get the pointed field value for non virtual fields
     * 
     * @param pReferencedFieldLabel
     *            pointed field label
     * @param pFieldLabel
     *            pointer field label
     * @param pPointedContainer
     *            pointed container
     * @param pPointedFieldsContainer
     *            pointed fields container
     * @return pointed field value
     */
    @SuppressWarnings("unchecked")
    protected Object getPointedFieldValue(String pReferencedFieldLabel,
            String pFieldLabel, CacheableValuesContainer pPointedContainer,
            CacheableFieldsContainer pPointedFieldsContainer) {
        Object lValue;
        org.topcased.gpm.business.serialization.data.Field lPointedField =
                pPointedFieldsContainer.getFieldFromLabel(pReferencedFieldLabel);

        if (lPointedField.getMultipleField() != null) {
            String lParentFieldLabel =
                    pPointedFieldsContainer.getFieldFromId(
                            lPointedField.getMultipleField()).getLabelKey();
            Object lParentValue = pPointedContainer.getValue(lParentFieldLabel);

            if (lParentValue instanceof Map) {
                // Get pointed value
                lValue =
                        ((Map<String, Object>) lParentValue).get(pReferencedFieldLabel);
            }
            else if (lParentValue instanceof List) {
                List<Map<String, Object>> lParentValueList =
                        (List<Map<String, Object>>) lParentValue;
                List<Object> lValueList = new LinkedList<Object>();
                for (Map<String, Object> lParentValueMap : lParentValueList) {
                    lValueList.add(lParentValueMap.get(pReferencedFieldLabel));
                }
                lValue = lValueList;
            }
            else {
                // If value not found, set to null
                lValue = null;
            }
        }
        else {
            lValue = pPointedContainer.getValue(pReferencedFieldLabel);
        }
        if (lValue instanceof org.topcased.gpm.business.serialization.data.FieldValueData) {
            org.topcased.gpm.business.serialization.data.FieldValueData lFieldValue =
                    (org.topcased.gpm.business.serialization.data.FieldValueData) lValue;
            lFieldValue.setName(pFieldLabel);
            return lFieldValue;
        }
        else if (lValue instanceof List) {
            List<org.topcased.gpm.business.serialization.data.FieldValueData> lPointedFieldValues =
                    (List<org.topcased.gpm.business.serialization.data.FieldValueData>) lValue;
            for (org.topcased.gpm.business.serialization.data.FieldValueData lPointedFieldValue :
                lPointedFieldValues) {
                if (lPointedFieldValue != null) {
                    lPointedFieldValue.setName(pFieldLabel);
                }
            }
            return lPointedFieldValues;
        }
        return lValue;
    }

    /**
     * Get a pointed field
     * 
     * @param pRoleToken
     *            the role token
     * @param pPointerFieldLabel
     *            the pointer field name
     * @param pPointedValuesContainerId
     *            the pointed values container identifier
     * @param pPointedFieldLabel
     *            the pointed field name
     * @return the pointed field
     */
    public BusinessField getPointedField(String pRoleToken,
            String pPointerFieldLabel, String pPointedValuesContainerId,
            String pPointedFieldLabel) {

        Object lValue;

        //Get pointed container in cache
        CacheableValuesContainer lCacheablePointedContainer =
                getCachedElement(CacheableValuesContainer.class,
                        pPointedValuesContainerId, CACHE_MUTABLE_OBJECT);

        String lPointedFieldsContainerId = null;
        CacheableFieldsContainer lPointedFieldsContainer = null;

        if (isPointedFieldVirtual(pPointedFieldLabel)) {
            // If the values container is in cache, use it.
            if (lCacheablePointedContainer != null) {
                lValue =
                        getVirtualPointedFieldValue(pPointerFieldLabel,
                                pPointedFieldLabel, lCacheablePointedContainer);
            }
            else {
                lValue =
                        getVirtualPointedFieldValue(pPointerFieldLabel,
                                pPointedValuesContainerId, pPointedFieldLabel);
            }

            String lStringValue = StringUtils.EMPTY;
            if (lValue instanceof FieldValueData) {
                lStringValue = ((FieldValueData) lValue).getValue();
            }
            return new BusinessVirtualPointedField(pPointedFieldLabel,
                    lStringValue);
        }
        else {
            // If the values container is in cache, use it.
            if (lCacheablePointedContainer != null) {
                // Get parent field and subfield pointed value
                lPointedFieldsContainer =
                        getCachedFieldsContainer(
                                lCacheablePointedContainer.getTypeId(),
                                CACHE_IMMUTABLE_OBJECT);
                lValue =
                        getPointedFieldValue(pPointedFieldLabel,
                                pPointerFieldLabel, lCacheablePointedContainer,
                                lPointedFieldsContainer);
            }
            else {
                final ValuesContainer lContainer =
                        getValuesContainerDao().load(pPointedValuesContainerId);
                lPointedFieldsContainerId = lContainer.getDefinition().getId();
                lValue =
                        DynamicValuesContainerAccessFactory.getInstance().getAccessor(
                                lPointedFieldsContainerId).getFieldValue(
                                pPointedFieldLabel, lContainer);
            }
        }

        if (lPointedFieldsContainer == null) {
            if (lPointedFieldsContainerId == null) {
                lPointedFieldsContainerId =
                    getValuesContainerDao().load(pPointedValuesContainerId).getDefinition().getId();
            }
            lPointedFieldsContainer =
                    getCachedFieldsContainer(lPointedFieldsContainerId,
                            CACHE_IMMUTABLE_OBJECT);
        }

        org.topcased.gpm.business.serialization.data.Field lField =
                lPointedFieldsContainer.getFieldFromLabel(pPointedFieldLabel);

        if (lField == null) {
            return null;
        }
        return createBusinessPointedField(pRoleToken, lField, lValue, false);
    }

    @SuppressWarnings("unchecked")
    private AbstractBusinessPointedField createBusinessPointedField(
            final String pRoleToken,
            final org.topcased.gpm.business.serialization.data.Field pField,
            final Object pValue, final boolean pInMultivalued) {

        AbstractBusinessPointedField lBusinessField = null;
        String lFieldName = pField.getLabelKey();
        String lFieldDescription = pField.getDescription();

        boolean lIsChoiceField = false;
        //if the pointed field is a choice field and if the value to inject is a list
        if (pValue instanceof List<?>
                && pField instanceof org.topcased.gpm.business.serialization.data.ChoiceField) {
            lIsChoiceField = true;
        }
        // If there is a choice field, we need to go on level down
        // to created a pointed field for each pointed value
        if ((pField.isMultivalued() && !pInMultivalued) || lIsChoiceField) {
            List<Object> lValues = new ArrayList<Object>();
            if (pValue instanceof List<?>) {
                lValues = (List<Object>) pValue;
            }
            else {
                lValues.add(pValue);
            }

            List<BusinessField> lFields = new ArrayList<BusinessField>();
            for (Object lValue : lValues) {
                lFields.add(createBusinessPointedField(pRoleToken, pField,
                        lValue, true));
            }
            lBusinessField =
                    new BusinessMultivaluedPointedField(lFieldName,
                            lFieldDescription, lFields);
        }
        else {
            if (pField instanceof org.topcased.gpm.business.serialization.data.SimpleField) {
                org.topcased.gpm.business.serialization.data.SimpleField lSimpleField =
                        (org.topcased.gpm.business.serialization.data.SimpleField) pField;

                String lValue = StringUtils.EMPTY;
                if (pValue instanceof FieldValueData) {
                    lValue = ((FieldValueData) pValue).getValue();
                }

                if (ValueType.INTEGER.getValue().equals(
                        lSimpleField.getValueType())) {
                    lBusinessField =
                            new BusinessIntegerPointedField(lFieldName,
                                    lFieldDescription, Integer.valueOf(lValue));
                }
                else if (ValueType.REAL.getValue().equals(
                        lSimpleField.getValueType())) {
                    lBusinessField =
                            new BusinessRealPointedField(lFieldName,
                                    lFieldDescription, Double.valueOf(lValue));
                }
                else if (ValueType.BOOLEAN.getValue().equals(
                        lSimpleField.getValueType())) {
                    lBusinessField =
                            new BusinessBooleanPointedField(lFieldName,
                                    lFieldDescription, Boolean.valueOf(lValue));
                }
                else if (ValueType.DATE.getValue().equals(
                        lSimpleField.getValueType())) {
                    try {
                        lBusinessField =
                                new BusinessDatePointedField(lFieldName,
                                        lFieldDescription,
                                        FieldsUtil.parseDate(lValue));
                    }
                    catch (ParseException e) {
                        throw new GDMException(e);
                    }
                }
                else {
                    String lInternalUrlSheetReference = null;
                    if (getSheetDao().exist(lValue)) {
                        lInternalUrlSheetReference =
                                getSheetService().getSheetRefStringByKey(
                                        pRoleToken, lValue);
                    }

                    lBusinessField =
                            new BusinessStringPointedField(lFieldName,
                                    lFieldDescription, lValue,
                                    lInternalUrlSheetReference);
                }
            }
            else if (pField instanceof org.topcased.gpm.business.serialization.data.ChoiceField) {
                String lValue = StringUtils.EMPTY;
                if (pValue instanceof FieldValueData) {
                    lValue = ((FieldValueData) pValue).getValue();
                }
                lBusinessField =
                        new BusinessChoicePointedField(lFieldName,
                                lFieldDescription, lValue);
            }
            else if (pField instanceof org.topcased.gpm.business.serialization.data.AttachedField) {
                String lId = StringUtils.EMPTY;
                String lFileName = StringUtils.EMPTY;
                String lMimeType = StringUtils.EMPTY;
                if (pValue instanceof AttachedFieldValueData) {
                    lId = ((AttachedFieldValueData) pValue).getId();
                    lFileName = ((AttachedFieldValueData) pValue).getFilename();
                    lMimeType = ((AttachedFieldValueData) pValue).getMimeType();
                }
                lBusinessField =
                        new BusinessAttachedPointedField(pRoleToken,
                                lFieldName, lFieldDescription, lId, lFileName,
                                lMimeType);
            }
            else if (pField instanceof org.topcased.gpm.business.serialization.data.PointerField) {
                String lReferencedContainerId = StringUtils.EMPTY;
                String lReferencedFieldLabel = StringUtils.EMPTY;
                if (pValue instanceof PointerFieldValueData) {
                    lReferencedContainerId =
                            ((PointerFieldValueData) pValue).getReferencedContainerId();
                    lReferencedFieldLabel =
                            ((PointerFieldValueData) pValue).getReferencedFieldLabel();
                }
                lBusinessField =
                        new BusinessPointerPointedField(pRoleToken, lFieldName,
                                lFieldDescription, lReferencedContainerId,
                                lReferencedFieldLabel);
            }
        }

        return lBusinessField;
    }

    /**
     * /** Get a field of a container by name.
     * 
     * @param pFieldsContainer
     *            Container of the field
     * @param pFieldName
     *            Name of the field to get
     * @return The field object
     * @throws InvalidNameException
     *             if the field is not found.
     */
    public Field getField(FieldsContainer pFieldsContainer, String pFieldName)
        throws InvalidNameException {

        if (null == pFieldName) {
            throw new GDMException("Field name is null");
        }

        String lFieldName = pFieldName;
        if (pFieldName.startsWith(UsableFieldsManager.PRODUCT_FIELD_PREFIX)) {
            lFieldName =
                    pFieldName.substring(UsableFieldsManager.PRODUCT_FIELD_PREFIX.length());
        }

        // Get the model field.
        Field lField =
                getFieldsContainerDao().getField(pFieldsContainer, lFieldName);
        if (null == lField) {
            throw new InvalidNameException(lFieldName,
                    "Field name ''{0}'' invalid");
        }
        return IdentityFieldVisitor.getIdentity(lField);
    }

    /**
     * Duplicate all attached field values found inside these multiple line
     * field datas
     * 
     * @param pRoleToken
     *            the current session token
     * @param pValuesContainer
     *            the array of multiple line field datas
     */
    @SuppressWarnings("unchecked")
    public void duplicateAttachedFieldValues(String pRoleToken,
            CacheableValuesContainer pValuesContainer) {
        // Iterate field
        for (Object lMultiValuedValue : pValuesContainer.getValuesMap().values()) {
            // Used if the field is multi valued
            final GpmIterator<Object> lValueIterator =
                    new GpmIterator<Object>(lMultiValuedValue);

            // Field is multi value 
            while (lValueIterator.hasNext()) {
                final Object lValue = lValueIterator.next();

                // Field is an attached field
                if (lValue instanceof AttachedFieldValueData) {
                    final AttachedFieldValueData lAttachedFieldValueData =
                            (AttachedFieldValueData) lValue;

                    if (lAttachedFieldValueData.getId() != null) {
                        lAttachedFieldValueData.setNewContent(
                                getSheetService().getAttachedFileContent(
                                        pRoleToken, lAttachedFieldValueData.getId()));
                        lAttachedFieldValueData.setId(null);
                    }
                }
                // Field is a multiple field
                else if (lValue instanceof Map) {
                    final Map<String, Object> lMultipleField =
                            (Map<String, Object>) lValue;

                    // Iterate on sub fields
                    for (Object lMultiValuedSubValue : lMultipleField.values()) {
                        // Used if the subfield is multi valued
                        final GpmIterator<Object> lSubValueIterator =
                                new GpmIterator<Object>(lMultiValuedSubValue);

                        // Sub field is multi value
                        while (lSubValueIterator.hasNext()) {
                            final Object lSubValue = lSubValueIterator.next();

                            // Sub field is an attached field
                            if (lSubValue instanceof AttachedFieldValueData) {
                                final AttachedFieldValueData lAttachedFieldSubValueData =
                                        (AttachedFieldValueData) lSubValue;

                                if (lAttachedFieldSubValueData.getId() != null) {
                                    lAttachedFieldSubValueData.setNewContent(
                                            getSheetService().getAttachedFileContent(
                                                    pRoleToken,
                                                    lAttachedFieldSubValueData.getId()));
                                    lAttachedFieldSubValueData.setId(null);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Get a field by its identifier.
     * 
     * @param pFieldId
     *            Identifier of the field
     * @return Field object
     */
    public Field getField(String pFieldId) {
        return IdentityFieldVisitor.getIdentity((Field) (getFieldDao().load(pFieldId)));
    }

    /**
     * Gets a field contained in a fields container from its name.
     * 
     * @param pClazz
     *            Actual class of the container
     * @param pContainer
     *            The fields container.
     * @param pFieldName
     *            Name of the field
     * @return The field object (typed according to clazz parameter), or null if
     *         not found.
     */
    @SuppressWarnings("unchecked")
    private final <C extends Field> C getField(Class<C> pClazz,
            FieldsContainer pContainer, String pFieldName) {
        Field lField = getFieldsContainerDao().getField(pContainer, pFieldName);
        if (null != lField) {
            return (C) IdentityFieldVisitor.getIdentity(lField);
        }
        return null;
    }

    /**
     * Delete a field.
     * 
     * @param pRoleToken
     *            The role session token.
     * @param pContainerId
     *            The fields container id where the simple field must be
     *            created.
     * @param pFieldName
     *            Name of the field to delete
     */
    public void deleteField(String pRoleToken, String pContainerId,
            String pFieldName) {
        FieldsContainer lContainer = getFieldsContainer(pContainerId);
        Field lField;

        lField = getField(Field.class, lContainer, pFieldName);
        if (null == lField) {
            throw new InvalidNameException(pFieldName);
        }

        deleteField(lField);

        removeElementFromCache(pContainerId, true);
    }

    /**
     * Delete a field in database.
     * 
     * @param pField
     *            Field to delete.
     */
    @SuppressWarnings("unchecked")
    private void deleteField(Field pField) {
        // Delete subfields (in multiple field)
        if (pField instanceof MultipleField) {
            MultipleField lMultiField = (MultipleField) pField;

            lMultiField.getFields().clear();

            for (Field lSubField : lMultiField.getFields()) {
                deleteField(lSubField);
            }
        }

        // Remove this field from its container.
        FieldsContainer lFieldsContainer = pField.getContainer();
        if (null != lFieldsContainer) {
            lFieldsContainer.getFields().remove(pField);
        }

        // Remove the field from its groups
        List<FieldGroup> lGroups = getFieldGroupDao().getGroups(pField);
        for (FieldGroup lGroup : lGroups) {
            lGroup.getFields().remove(pField);
        }
        getFieldDao().remove(pField);
    }

    /**
     * Create (or update) a simple field definition.
     * 
     * @param pRoleToken
     *            The role session token.
     * @param pContainerId
     *            The fields container id where the simple field must be
     *            created.
     * @param pSimpleFieldData
     *            Value object containing the attribute for the field to be
     *            created.
     * @return Identifier of the field
     */
    public String createSimpleField(String pRoleToken, String pContainerId,
            SimpleFieldData pSimpleFieldData) {
        FieldsContainer lContainer = getFieldsContainer(pContainerId);
        SimpleField lSimpleField;

        try {
            lSimpleField =
                    getField(SimpleField.class, lContainer,
                            pSimpleFieldData.getLabelKey());
        }
        catch (ClassCastException e) {
            throw new UnmodifiableElementException(
                    pSimpleFieldData.getLabelKey());
        }

        if (null != lSimpleField) {
            // Update existing field

            // Check that the current type is the same as new one.
            String lTypeName = lSimpleField.getType().toString();
            if (!lTypeName.equals(pSimpleFieldData.getValueType().toString())) {
                throw new UnmodifiableElementException(
                        pSimpleFieldData.getLabelKey(),
                        "Cannot change the type of a SimpleField.");
            }

            fillField(lSimpleField, pSimpleFieldData);

            lSimpleField.setMaxSize(pSimpleFieldData.getMaxSize());

            String lStrDefaultValue = pSimpleFieldData.getDefaultValue();
            ScalarValue lPrevDefaultValue = lSimpleField.getDefaultValue();

            if (null == lStrDefaultValue) {
                // Remove the default value (if any)
                if (null != lPrevDefaultValue) {
                    getScalarValueDao().remove(lPrevDefaultValue);
                    lSimpleField.setDefaultValue(null);
                }
            }
            else {
                if (null != lPrevDefaultValue) {
                    // Set the default value (if different from the current one)

                    if (!lPrevDefaultValue.getAsString().equals(
                            lStrDefaultValue)) {
                        getScalarValueDao().remove(lPrevDefaultValue);
                    }
                }
                ScalarValue lValue =
                        createOrUpdateScalarValue(lSimpleField, null,
                                lStrDefaultValue);
                lSimpleField.setDefaultValue(lValue);
            }
        }
        else {
            // Create a new field
            lSimpleField = SimpleField.newInstance();

            lSimpleField.setContainer(lContainer);
            // Define the type of this simple field.
            lSimpleField.setType(FieldType.fromString(pSimpleFieldData.getValueType().toString()));
            fillField(lSimpleField, pSimpleFieldData);

            lSimpleField.setMaxSize(pSimpleFieldData.getMaxSize());

            String lStrDefaultValue = pSimpleFieldData.getDefaultValue();

            if (null != lStrDefaultValue) {
                ScalarValue lValue =
                        createOrUpdateScalarValue(lSimpleField, null,
                                lStrDefaultValue);
                lSimpleField.setDefaultValue(lValue);
            }
            getSimpleFieldDao().create(lSimpleField);
            lContainer.addToFieldList(lSimpleField);
        }
        removeElementFromCache(pContainerId, true);
        return lSimpleField.getId();
    }

    /**
     * Create or update a scalar value
     * 
     * @param pField
     *            Simple field (used to know the type of the value, and the
     *            actual implementation class to create)
     * @param pOldScalarValue
     *            The old scalar value : null if need to create a new one
     * @param pValue
     *            Value to set in the scalar value
     * @return The created or updated scalar value
     */
    private ScalarValue createOrUpdateScalarValue(SimpleField pField,
            ScalarValue pOldScalarValue, String pValue) {
        final ScalarValue lNewScalarValue;

        if (pOldScalarValue == null || pValue == null) {
            final FieldType lFieldType = pField.getType();

            if (lFieldType == FieldType.BOOLEAN) {
                lNewScalarValue = BooleanValue.newInstance();
            }
            else if (lFieldType == FieldType.INTEGER) {
                // FIXME: On IE, the Dojo toolkit may send a "null" value for an
                // empty real or integer field. We catch the problem here (not the
                // very best way to handle this problem, ok).
                if ("null".equals(pValue)) {
                    pValue = null;
                }
                lNewScalarValue = IntegerValue.newInstance();
            }
            else if (lFieldType == FieldType.REAL) {
                if ("null".equals(pValue)) {
                    pValue = null;
                }
                lNewScalarValue = RealValue.newInstance();
            }
            else if (lFieldType == FieldType.STRING) {
                lNewScalarValue = StringValue.newInstance();
            }
            else if (lFieldType == FieldType.DATE) {
                lNewScalarValue = DateValue.newInstance();
            }
            else {
                throw new RuntimeException("Field type "
                        + lFieldType.toString() + " not supported");
            }
        }
        else {
            lNewScalarValue = pOldScalarValue;
        }

        if (pValue != null) {
            try {
                lNewScalarValue.setAsString(pValue);
            }
            catch (NumberFormatException e) {
                throw new InvalidFieldValueException(pField.getLabelKey(),
                        pValue);
            }
        }
        // Else keep the scalar value object 'null'

        return lNewScalarValue;
    }

    /**
     * Create (or update) a choice field definition.
     * 
     * @param pRoleToken
     *            The role session token.
     * @param pContainerId
     *            The fields container id where the simple field must be
     *            created.
     * @param pChoiceFieldData
     *            Value object containing the attribute for the field to be
     *            created.
     * @return Identifier of the field
     */
    public String createChoiceField(String pRoleToken, String pContainerId,
            ChoiceFieldData pChoiceFieldData) {
        FieldsContainer lContainer = getFieldsContainer(pContainerId);
        ChoiceField lField;
        boolean lUpdate = true;

        try {
            lField =
                    getField(ChoiceField.class, lContainer,
                            pChoiceFieldData.getLabelKey());
        }
        catch (ClassCastException e) {
            throw new UnmodifiableElementException(
                    pChoiceFieldData.getLabelKey());
        }

        Dictionary lDict = lContainer.getBusinessProcess().getDictionary();
        Category lCat =
                getCategoryDao().getCategory(lDict,
                        pChoiceFieldData.getCategoryName());

        if (null == lCat) {
            throw new InvalidValueException(pChoiceFieldData.getCategoryName(),
                    "Invalid category value ''{0}''.");
        }

        if (null == lField) {

            // Create a new field
            lField = ChoiceField.newInstance();
            lUpdate = false;
        }

        // Fill field attrs
        fillField(lField, pChoiceFieldData);
        lField.setCategory(lCat);
        lField.setValuesSeparator(pChoiceFieldData.getValuesSeparator());
        CategoryValue lDefaultCatValue = null;

        // Set the default value (if specified)
        String lDefaultValue = pChoiceFieldData.getDefaultValue();
        if (!StringUtils.isEmpty(lDefaultValue)) {
            lDefaultCatValue = getCategoryValueDao().get(lCat, lDefaultValue);
            if (null == lDefaultCatValue) {
                throw new InvalidValueException(lDefaultValue,
                        "Invalid default value ''{0}''.");
            }
        }
        lField.setDefaultValue(lDefaultCatValue);

        if (!lUpdate) {
            lField.setContainer(lContainer);
            getChoiceFieldDao().create(lField);
            lContainer.addToFieldList(lField);
        }
        removeElementFromCache(pContainerId, true);
        return lField.getId();
    }

    /**
     * Create (or update) an attached file field definition.
     * 
     * @param pRoleToken
     *            The role session token.
     * @param pContainerId
     *            The fields container id where the simple field must be
     *            created.
     * @param pFieldData
     *            Value object containing the attribute for the field to be
     *            created.
     * @return Identifier of the field
     */
    public String createAttachedField(String pRoleToken, String pContainerId,
            FieldTypeData pFieldData) {
        FieldsContainer lContainer = getFieldsContainer(pContainerId);
        boolean lUpdate = true;

        AttachedField lField =
                getField(AttachedField.class, lContainer,
                        pFieldData.getLabelKey());
        if (null == lField) {
            lField = AttachedField.newInstance();
            lUpdate = false;
        }
        fillField(lField, pFieldData);

        if (!lUpdate) {
            lField.setContainer(lContainer);
            getAttachedFieldDao().create(lField);
            lContainer.addToFieldList(lField);
        }
        removeElementFromCache(pContainerId, true);
        return lField.getId();
    }

    /**
     * Create (or update) a multiple field definition.
     * 
     * @param pRoleToken
     *            The role session token.
     * @param pContainerId
     *            The fields container id where the simple field must be
     *            created.
     * @param pMultipleFieldData
     *            Value object containing the attribute for the field to be
     *            created.
     * @return Identifier of the field
     */
    public String createMultipleField(String pRoleToken, String pContainerId,
            MultipleFieldData pMultipleFieldData) {
        FieldsContainer lContainer = getFieldsContainer(pContainerId);

        MultipleField lMultipleField;

        try {
            lMultipleField =
                    getField(MultipleField.class, lContainer,
                            pMultipleFieldData.getLabelKey());
        }
        catch (ClassCastException e) {
            throw new UnmodifiableElementException(
                    pMultipleFieldData.getLabelKey());
        }
        if (null == lMultipleField) {
            lMultipleField = MultipleField.newInstance();
            lMultipleField.setContainer(lContainer);
            fillField(lMultipleField, pMultipleFieldData);
            lMultipleField.setFieldSeparator(pMultipleFieldData.getFieldSeparator());
            getMultipleFieldDao().create(lMultipleField);
            lContainer.addToFieldList(lMultipleField);
        }
        else {
            // First clear the subfields list
            lMultipleField.getFields().clear();

            fillField(lMultipleField, pMultipleFieldData);
            lMultipleField.setFieldSeparator(pMultipleFieldData.getFieldSeparator());

            // All previous subfields not present in the new multiple filed
            // definition must
            // be marked as 'non sub-fields'.
            for (Field lSubfield : lMultipleField.getSubfields()) {
                if (!ArrayUtils.contains(pMultipleFieldData.getSubfields(),
                        lSubfield.getLabelKey())) {
                    lSubfield.setSubfield(false);
                }
            }
        }

        // Associate the subfields
        for (String lSubfieldName : pMultipleFieldData.getSubfields()) {
            Field lSubfield = getField(Field.class, lContainer, lSubfieldName);
            if (null == lSubfield) {
                throw new InvalidNameException(lSubfieldName);
            }

            // Mark fields as subfields
            if (!lSubfield.isSubfield()) {
                lSubfield.setSubfield(true);
            }
            lMultipleField.addToFieldList(lSubfield);
        }

        removeElementFromCache(pContainerId, true);
        return lMultipleField.getId();
    }

    /**
     * Initialize a Field content from a FieldTypeData definition.
     * 
     * @param pField
     *            Field to initialize
     * @param pFieldData
     *            FieldTypeData definition
     */
    private void fillField(Field pField, final FieldTypeData pFieldData) {
        pField.setLabelKey(pFieldData.getLabelKey());

        pField.setMultiValued(pFieldData.isMultipleValueSupport());
        pField.setDescription(pFieldData.getDescription());

        FieldAccessData lDefaultAccess = pFieldData.getDefaultAccess();

        if (null == lDefaultAccess) {
            // If no default access specified, use default values.
            pField.setMandatory(DEFAULT_MANDATORY_ACCESS);
            pField.setConfidential(DEFAULT_CONFIDENTIAL_ACCESS);
            pField.setUpdatable(DEFAULT_UPDATABLE_ACCESS);
            pField.setExportable(DEFAULT_EXPORTABLE_ACCESS);
        }
        else {
            pField.setMandatory((Boolean) ObjectUtils.defaultIfNull(
                    lDefaultAccess.getMandatory(), DEFAULT_MANDATORY_ACCESS));
            pField.setConfidential((Boolean) ObjectUtils.defaultIfNull(
                    lDefaultAccess.getConfidential(),
                    DEFAULT_CONFIDENTIAL_ACCESS));
            pField.setUpdatable((Boolean) ObjectUtils.defaultIfNull(
                    lDefaultAccess.getUpdatable(), DEFAULT_UPDATABLE_ACCESS));
            pField.setExportable((Boolean) ObjectUtils.defaultIfNull(
                    lDefaultAccess.getExportable(), DEFAULT_EXPORTABLE_ACCESS));
        }

        // If pointer field, add pointerField attributes to field
        pField.setPointerField(pFieldData.isPointerField());
        createOrUpdatePointerFieldAttributes(pField, pFieldData);

        pField.setLineSeparator(",");
    }

    private void createOrUpdatePointerFieldAttributes(Field pField,
            final FieldTypeData pFieldData) {
        if (pFieldData.isPointerField()
                && pFieldData.getPointerFieldData() != null) {

            // Create or pointer field attributes
            PointerFieldAttributes lPointerFieldAttributes =
                    pField.getPointerFieldAttributes();
            boolean lIsUpdate = true;
            if (lPointerFieldAttributes == null) {
                lPointerFieldAttributes = PointerFieldAttributes.newInstance();
                pField.setPointerFieldAttributes(lPointerFieldAttributes);
                lIsUpdate = false;
            }
            lPointerFieldAttributes.setReferencedFieldLabel(
                    pFieldData.getPointerFieldData().getReferencedFieldLabel());
            lPointerFieldAttributes.setReferencedLinkType(
                    pFieldData.getPointerFieldData().getReferencedLinkType());
            if (!lIsUpdate) {
                getPointerFieldAttributesDao().create(lPointerFieldAttributes);
            }
        }
    }

    /**
     * Create a scalar value data from a scalar value.
     * 
     * @param pScalarValue
     *            the scalar value
     * @return the corresponding scalar value data
     */
    public ScalarValueData createScalarValueData(ScalarValue pScalarValue) {
        if (pScalarValue == null) {
            return null;
        }
        if (pScalarValue instanceof StringValue) {
            StringValue lStringValue = (StringValue) pScalarValue;
            StringValueData lStringValueData =
                    new StringValueData(lStringValue.getStringValue());

            return lStringValueData;
        }
        else if (pScalarValue instanceof RealValue) {
            RealValue lRealValue = (RealValue) pScalarValue;
            RealValueData lRealValueData =
                    new RealValueData(lRealValue.getRealValue());

            return lRealValueData;
        }
        else if (pScalarValue instanceof DateValue) {
            DateValue lDateValue = (DateValue) pScalarValue;
            DateValueData lDateValueData =
                    new DateValueData(lDateValue.getDateValue());

            return lDateValueData;
        }
        else if (pScalarValue instanceof BooleanValue) {
            BooleanValue lBooleanValue = (BooleanValue) pScalarValue;
            BooleanValueData lBooleanValueData =
                    new BooleanValueData(lBooleanValue.getBoolValue());

            return lBooleanValueData;
        }
        else if (pScalarValue instanceof IntegerValue) {
            IntegerValue lIntegerValue = (IntegerValue) pScalarValue;
            IntegerValueData lIntegerValueData =
                    new IntegerValueData(lIntegerValue.getIntValue());

            return lIntegerValueData;
        }
        else {
            throw new GDMException("This scalarValue is not supported :"
                    + pScalarValue.getAsString());
        }
    }

    /**
     * get the field type of a field.
     * 
     * @param pField
     *            the field
     * @return the field type
     */
    public static String getFieldType(Field pField) {
        if (pField instanceof AttachedField) {
            return FieldTypes.ATTACHED_FIELD;
        }
        else if (pField instanceof ChoiceField) {
            return FieldTypes.CHOICE_FIELD;
        }
        else if (pField instanceof SimpleField) {
            SimpleField lSimpleField = (SimpleField) pField;
            if (lSimpleField.getType() == FieldType.BOOLEAN) {
                return FieldTypes.SIMPLE_BOOLEAN_FIELD;
            }
            else if (lSimpleField.getType() == FieldType.DATE) {
                return FieldTypes.SIMPLE_DATE_FIELD;
            }
            else if (lSimpleField.getType() == FieldType.INTEGER) {
                return FieldTypes.SIMPLE_INTEGER_FIELD;
            }
            else if (lSimpleField.getType() == FieldType.REAL) {
                return FieldTypes.SIMPLE_REAL_FIELD;
            }
            else if (lSimpleField.getType() == FieldType.STRING) {
                return FieldTypes.SIMPLE_STRING_FIELD;
            }
        }
        else if (pField instanceof MultipleField) {
            return FieldTypes.MULTIPLE_FIELD;
        }
        throw new GDMException("Unexpected type '"
                + pField.getClass().getName() + "' for field '"
                + pField.getLabelKey() + "'.");
    }

    /** The multiple field dao. */
    private MultipleFieldDao multipleFieldDao;

    /**
     * Sets the reference to <code>multipleField</code>'s DAO.
     * 
     * @param pMultipleFieldDao
     *            the multiple field dao
     */
    public void setMultipleFieldDao(MultipleFieldDao pMultipleFieldDao) {
        multipleFieldDao = pMultipleFieldDao;
    }

    /**
     * Gets the reference to <code>multipleField</code>'s DAO.
     * 
     * @return <code>multipleField</code>'s DAO.
     */
    protected MultipleFieldDao getMultipleFieldDao() {
        return multipleFieldDao;
    }

    /** The field dao. */
    private FieldDao fieldDao;

    /** The attached field dao. */
    private AttachedFieldDao attachedFieldDao;

    /** The attached field value dao. */
    private AttachedFieldValueDao attachedFieldValueDao;

    /**
     * Get the attached field dao.
     * 
     * @return Returns the attachedFieldDao.
     */
    protected AttachedFieldDao getAttachedFieldDao() {
        return attachedFieldDao;
    }

    /**
     * set the attached field dao.
     * 
     * @param pAttachedFieldDao
     *            The attachedFieldDao to set.
     */
    public void setAttachedFieldDao(AttachedFieldDao pAttachedFieldDao) {
        attachedFieldDao = pAttachedFieldDao;
    }

    /**
     * Get the attachedFieldValueDao.
     * 
     * @return Returns the attachedFieldValueDao.
     */
    protected AttachedFieldValueDao getAttachedFieldValueDao() {
        return attachedFieldValueDao;
    }

    /**
     * Set the attachedFieldValueDao.
     * 
     * @param pAttachedFieldValueDao
     *            The attachedFieldValueDao to set.
     */
    public void setAttachedFieldValueDao(
            AttachedFieldValueDao pAttachedFieldValueDao) {
        attachedFieldValueDao = pAttachedFieldValueDao;
    }

    /** The simple field dao. */
    private SimpleFieldDao simpleFieldDao;

    /**
     * Gets the simple field dao.
     * 
     * @return the simple field dao
     */
    public SimpleFieldDao getSimpleFieldDao() {
        return simpleFieldDao;
    }

    /**
     * Sets the simple field dao.
     * 
     * @param pSimpleFieldDao
     *            the simple field dao
     */
    public void setSimpleFieldDao(SimpleFieldDao pSimpleFieldDao) {
        simpleFieldDao = pSimpleFieldDao;
    }

    /** The choice field dao. */
    private ChoiceFieldDao choiceFieldDao;

    /**
     * Gets the choice field dao.
     * 
     * @return the choice field dao
     */
    public ChoiceFieldDao getChoiceFieldDao() {
        return choiceFieldDao;
    }

    /**
     * Sets the choice field dao.
     * 
     * @param pChoiceFieldDao
     *            the choice field dao
     */
    public void setChoiceFieldDao(ChoiceFieldDao pChoiceFieldDao) {
        choiceFieldDao = pChoiceFieldDao;
    }

    /** The category dao. */
    private CategoryDao categoryDao;

    /**
     * Gets the category dao.
     * 
     * @return the category dao
     */
    public CategoryDao getCategoryDao() {
        return categoryDao;
    }

    /**
     * Sets the category dao.
     * 
     * @param pCategoryDao
     *            the category dao
     */
    public void setCategoryDao(CategoryDao pCategoryDao) {
        categoryDao = pCategoryDao;
    }

    /**
     * Gets the field dao.
     * 
     * @return the field dao
     */
    public FieldDao getFieldDao() {
        return fieldDao;
    }

    /**
     * Sets the field dao.
     * 
     * @param pFieldDao
     *            the field dao
     */
    public void setFieldDao(FieldDao pFieldDao) {
        fieldDao = pFieldDao;
    }

    /** The category value dao. */
    private CategoryValueDao categoryValueDao;

    /**
     * Gets the category value dao.
     * 
     * @return the category value dao
     */
    public CategoryValueDao getCategoryValueDao() {
        return categoryValueDao;
    }

    /**
     * Sets the category value dao.
     * 
     * @param pCategoryValueDao
     *            the category value dao
     */
    public void setCategoryValueDao(CategoryValueDao pCategoryValueDao) {
        categoryValueDao = pCategoryValueDao;
    }

    /** The string value dao. */
    private StringValueDao stringValueDao;

    /**
     * getStringValueDao.
     * 
     * @return the StringValueDao
     */
    public StringValueDao getStringValueDao() {
        return stringValueDao;
    }

    /**
     * setStringValueDao.
     * 
     * @param pStringValueDao
     *            the StringValueDao to set
     */
    public void setStringValueDao(StringValueDao pStringValueDao) {
        stringValueDao = pStringValueDao;
    }

    /** The real value dao. */
    private RealValueDao realValueDao;

    /**
     * getRealValueDao.
     * 
     * @return the RealValueDao
     */
    public RealValueDao getRealValueDao() {
        return realValueDao;
    }

    /**
     * setRealValueDao.
     * 
     * @param pRealValueDao
     *            the RealValueDao to set
     */
    public void setRealValueDao(RealValueDao pRealValueDao) {
        realValueDao = pRealValueDao;
    }

    /** The boolean value dao. */
    private BooleanValueDao booleanValueDao;

    /**
     * getBooleanValueDao.
     * 
     * @return the BooleanValueDao
     */
    public BooleanValueDao getBooleanValueDao() {
        return booleanValueDao;
    }

    /**
     * setBooleanValueDao.
     * 
     * @param pBooleanValueDao
     *            the BooleanValueDao to set
     */
    public void setBooleanValueDao(BooleanValueDao pBooleanValueDao) {
        booleanValueDao = pBooleanValueDao;
    }

    /** The integer value dao. */
    private IntegerValueDao integerValueDao;

    /**
     * getIntegerValueDao.
     * 
     * @return the IntegerValueDao
     */
    public IntegerValueDao getIntegerValueDao() {
        return integerValueDao;
    }

    /**
     * setIntegerValueDao.
     * 
     * @param pIntegerValueDao
     *            the IntegerValueDao to set
     */
    public void setIntegerValueDao(IntegerValueDao pIntegerValueDao) {
        integerValueDao = pIntegerValueDao;
    }

    /** The date value dao. */
    private DateValueDao dateValueDao;

    /**
     * getDateValueDao.
     * 
     * @return the DateValueDao
     */
    public DateValueDao getDateValueDao() {
        return dateValueDao;
    }

    /**
     * setDateValueDao.
     * 
     * @param pDateValueDao
     *            the DateValueDao to set
     */
    public void setDateValueDao(DateValueDao pDateValueDao) {
        dateValueDao = pDateValueDao;
    }

    /** The scalar value dao. */
    private ScalarValueDao scalarValueDao;

    /**
     * Gets the scalar value dao.
     * 
     * @return the scalar value dao
     */
    public ScalarValueDao getScalarValueDao() {
        return scalarValueDao;
    }

    /**
     * Sets the scalar value dao.
     * 
     * @param pScalarValueDao
     *            the scalar value dao
     */
    public void setScalarValueDao(ScalarValueDao pScalarValueDao) {
        scalarValueDao = pScalarValueDao;
    }

    /** The field group dao. */
    private FieldGroupDao fieldGroupDao;

    /**
     * Gets the field group dao.
     * 
     * @return the field group dao
     */
    public FieldGroupDao getFieldGroupDao() {
        return fieldGroupDao;
    }

    /**
     * Sets the field group dao.
     * 
     * @param pFieldGroupDao
     *            the field group dao
     */
    public void setFieldGroupDao(FieldGroupDao pFieldGroupDao) {
        fieldGroupDao = pFieldGroupDao;
    }

    private PointerFieldAttributesDao pointerFieldAttributesDao;

    public PointerFieldAttributesDao getPointerFieldAttributesDao() {
        return pointerFieldAttributesDao;
    }

    public void setPointerFieldAttributesDao(
            PointerFieldAttributesDao pPointerFieldAttributesDao) {
        this.pointerFieldAttributesDao = pPointerFieldAttributesDao;
    }

    /**
     * Fill attached files content.
     * 
     * @param pContainer
     *            the sheet
     */
    public void fillAttachedFilesContent(CacheableValuesContainer pContainer) {
        for (AttachedFieldValueData lValue : pContainer.getAllAttachedFileValues()) {
            fillAttachedContent(lValue);
        }
    }

    /**
     * Fill attached content.
     * 
     * @param pField
     *            the field
     */
    private void fillAttachedContent(AttachedFieldValueData pField) {

        InputStream lFileStream = resourcesLoader.getAsStream(pField.getFilename());
        if (null == lFileStream) {
            throw new GDMException("Cannot read resource / file '" + pField.getFilename() + "'");
        }
        
        try {
        	InputStream lStream = new BufferedInputStream(lFileStream);
        	try {
                long lLength = resourcesLoader.getLength(pField.getFilename());
        		if (lLength < 0) { // Length could not be sorted out -> use the memory-hungry ByteArrayOutputStream
        			ByteArrayOutputStream lContentOutput = new ByteArrayOutputStream();
       				IOUtils.copy(lStream, lContentOutput);
        			pField.setNewContent(lContentOutput.toByteArray());
        		} else { // This is a real file -> optimize memory usage (until real streams are used)
        			final byte[] lContents = new byte[(int) lLength];
                    final int lSize = lContents.length;
                    int c;
                    for (int i=0; i<lSize; i++) {
                    	c = lStream.read();
                    	if (c == -1) {
                    		throw new GDMException("Could not upload file " + pField.getFilename() + ": unexpected end of file.");
                    	}
                    	lContents[i] = (byte) c;
                    }

        			pField.setNewContent(lContents);
            	}
        	}

        	finally {
        		if (lStream != null) {
        			// close input stream after use
        			lStream.close();
        		}
        	}
        }
        catch (IOException e) {
        	throw new GDMException("Cannot read file content '" + pField.getFilename() + "'", e);
        }
    }

    private IResourcesLoader resourcesLoader;

    public final IResourcesLoader getResourcesLoader() {
        return resourcesLoader;
    }

    public final void setResourcesLoader(IResourcesLoader pResourcesLoader) {
        resourcesLoader = pResourcesLoader;
    }

    /**
     * Throw a mandatory exception of the list of missing file is not empty
     * 
     * @param pRoleToken
     *            The role token
     * @param pFieldsContainer
     *            The field container describing the type
     * @param pValuesContainer
     *            The values container to check
     * @throws MandatoryValuesException
     */
    public void checkMandoryFields(String pRoleToken,
            CacheableFieldsContainer pFieldsContainer,
            CacheableValuesContainer pValuesContainer) {
        final Set<String> lMissingFields =
                MandatoryFieldsValidator.getEmptyMandatoryFields(
                        pFieldsContainer, pValuesContainer);

        if (!lMissingFields.isEmpty()) {
            final Collection<MandatoryValuesException.FieldRef> lExcFields =
                    new ArrayList<MandatoryValuesException.FieldRef>(
                            lMissingFields.size());

            for (String lFieldLabelKey : lMissingFields) {
                lExcFields.add(new MandatoryValuesException.FieldRef(
                        lFieldLabelKey, getI18nService().getValueForUser(
                                pRoleToken, lFieldLabelKey)));
            }

            throw new MandatoryValuesException(lExcFields);
        }
    }
}
