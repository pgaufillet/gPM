/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.fields.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.topcased.gpm.business.ServiceImplBase;
import org.topcased.gpm.business.authorization.service.AccessControlContextData;
import org.topcased.gpm.business.authorization.service.TypeAccessControlData;
import org.topcased.gpm.business.cache.CacheableFactory;
import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.exception.InvalidIdentifierException;
import org.topcased.gpm.business.exception.InvalidNameException;
import org.topcased.gpm.business.exception.MandatoryValuesException;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.extensions.service.ContextBase;
import org.topcased.gpm.business.extensions.service.ExtensionPointNames;
import org.topcased.gpm.business.extensions.service.ExtensionPointParameters;
import org.topcased.gpm.business.facilities.DisplayGroupData;
import org.topcased.gpm.business.fields.AttachedFieldData;
import org.topcased.gpm.business.fields.ChoiceFieldData;
import org.topcased.gpm.business.fields.FieldAccessData;
import org.topcased.gpm.business.fields.FieldSummaryData;
import org.topcased.gpm.business.fields.FieldTypeData;
import org.topcased.gpm.business.fields.FieldsManager;
import org.topcased.gpm.business.fields.MultipleFieldData;
import org.topcased.gpm.business.fields.SimpleFieldData;
import org.topcased.gpm.business.fields.ValueType;
import org.topcased.gpm.business.fields.service.FieldsService;
import org.topcased.gpm.business.product.impl.CacheableProduct;
import org.topcased.gpm.business.serialization.data.ChoiceStringDisplayHint;
import org.topcased.gpm.business.serialization.data.DisplayHint;
import org.topcased.gpm.business.serialization.data.FieldValueData;
import org.topcased.gpm.business.serialization.data.InputData;
import org.topcased.gpm.business.values.field.BusinessField;
import org.topcased.gpm.domain.accesscontrol.FieldAccessControlDao;
import org.topcased.gpm.domain.businessProcess.BusinessProcess;
import org.topcased.gpm.domain.dictionary.CategoryValue;
import org.topcased.gpm.domain.extensions.ExtendedAction;
import org.topcased.gpm.domain.extensions.ExtensionPoint;
import org.topcased.gpm.domain.facilities.DisplayGroupDao;
import org.topcased.gpm.domain.facilities.FieldGroupDao;
import org.topcased.gpm.domain.fields.AttachedField;
import org.topcased.gpm.domain.fields.ChoiceField;
import org.topcased.gpm.domain.fields.Field;
import org.topcased.gpm.domain.fields.FieldDao;
import org.topcased.gpm.domain.fields.FieldsContainer;
import org.topcased.gpm.domain.fields.InputDataType;
import org.topcased.gpm.domain.fields.InputDataTypeDao;
import org.topcased.gpm.domain.fields.MultipleField;
import org.topcased.gpm.domain.fields.ScalarValue;
import org.topcased.gpm.domain.fields.SimpleField;
import org.topcased.gpm.util.bean.CacheProperties;
import org.topcased.gpm.util.session.GpmSessionFactory;

/**
 * Implementation of the fields service.
 * 
 * @author llatil
 */
public class FieldsServiceImpl extends ServiceImplBase implements FieldsService {

    private CacheableInputDataTypeFactory inputDataTypeFactory =
            new CacheableInputDataTypeFactory();

    /**
     * Default constructor.
     * <p>
     * This constructor should not be invoked directly. Use the service locator
     * or Spring injectors to get a reference to the sheet service.
     */
    public FieldsServiceImpl() {
        registerFactories(inputDataTypeFactory);
    }

    /**
     * Get information on a field.
     * 
     * @param pRoleToken
     *            The role session token.
     * @param pFieldsContainerId
     *            The identifier of the fields container containing the field
     * @param pFieldName
     *            Name of the field to retrieve
     * @return Field type data
     */
    @Override
    public FieldTypeData getField(String pRoleToken, String pFieldsContainerId,
            String pFieldName) {
        FieldsContainer lFieldsContainer;

        try {
            lFieldsContainer =
                    (FieldsContainer) getFieldsContainerDao().load(
                            pFieldsContainerId);
        }
        catch (ClassCastException e) {
            throw new InvalidIdentifierException(pFieldsContainerId);
        }
        if (null == lFieldsContainer) {
            throw new InvalidIdentifierException(pFieldsContainerId);
        }

        Field lField = fieldsManager.getField(lFieldsContainer, pFieldName);
        return createFieldTypeData(pRoleToken, lField);
    }

    /**
     * Get information on a field retrieved using its unique identifier.
     * 
     * @param pRoleToken
     *            The role session token.
     * @param pFieldId
     *            The identifier of the field
     * @return Field type data
     */
    @Override
    public FieldTypeData getField(String pRoleToken, String pFieldId) {
        Field lField;

        try {
            lField = (Field) getFieldDao().load(pFieldId);
        }
        catch (ClassCastException e) {
            throw new InvalidIdentifierException(pFieldId);
        }
        return createFieldTypeData(pRoleToken, lField);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.fields.service.FieldsService#getFieldNames(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public String[] getFieldNames(String pRoleToken, String pContainerId) {
        return getFieldsContainerDao().getFieldNames(pContainerId);
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
     * @param pDeleteValues
     *            Force deletion of field values
     */
    @Override
    public void deleteField(String pRoleToken, String pContainerId,
            String pFieldName, boolean pDeleteValues) {
        fieldsManager.deleteField(pRoleToken, pContainerId, pFieldName);
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
    @Override
    public String createSimpleField(String pRoleToken, String pContainerId,
            SimpleFieldData pSimpleFieldData) {
        return fieldsManager.createSimpleField(pRoleToken, pContainerId,
                pSimpleFieldData);
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
    @Override
    public String createChoiceField(String pRoleToken, String pContainerId,
            ChoiceFieldData pChoiceFieldData) {
        return fieldsManager.createChoiceField(pRoleToken, pContainerId,
                pChoiceFieldData);
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
    @Override
    public String createAttachedField(String pRoleToken, String pContainerId,
            AttachedFieldData pFieldData) {
        return fieldsManager.createAttachedField(pRoleToken, pContainerId,
                pFieldData);
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
    @Override
    public String createMultipleField(String pRoleToken, String pContainerId,
            MultipleFieldData pMultipleFieldData) {
        return fieldsManager.createMultipleField(pRoleToken, pContainerId,
                pMultipleFieldData);
    }

    /**
     * Create a field type data from a field.
     * 
     * @param pField
     *            Field
     * @return The FieldTypeData. This object is dynamically typed according to
     *         the given field type (SimpleFieldData, ChoiceFieldData,
     *         MultipleFieldData)
     */
    private FieldTypeData createFieldTypeData(String pRoleToken, Field pField) {
        FieldTypeData lFieldTypeData = null;

        if (pField instanceof SimpleField) {
            SimpleField lSimpleField = (SimpleField) pField;
            SimpleFieldData lSimpleFieldData = new SimpleFieldData();

            ScalarValue lDefaultValue = lSimpleField.getDefaultValue();

            lSimpleFieldData.setValueType(ValueType.fromString(lSimpleField.getType().toString()));
            if (null != lDefaultValue) {
                lSimpleFieldData.setDefaultValue(lDefaultValue.getAsString());
            }
            lFieldTypeData = lSimpleFieldData;
        }
        else if (pField instanceof AttachedField) {
            lFieldTypeData = new AttachedFieldData();
        }
        else if (pField instanceof ChoiceField) {
            ChoiceField lChoiceField = (ChoiceField) pField;
            ChoiceFieldData lChoiceFieldData = new ChoiceFieldData();
            lChoiceFieldData.setCategoryName(lChoiceField.getCategory().getName());

            lFieldTypeData = lChoiceFieldData;
            CategoryValue lDefaultValue = lChoiceField.getDefaultValue();
            if (null != lDefaultValue) {
                lChoiceFieldData.setDefaultValue(lDefaultValue.getValue());
            }
            lChoiceFieldData.setValuesSeparator(lChoiceField.getValuesSeparator());
        }
        else if (pField instanceof MultipleField) {
            MultipleField lMultiField = (MultipleField) pField;
            MultipleFieldData lMultiFieldData = new MultipleFieldData();
            lMultiFieldData.setFieldSeparator(lMultiField.getFieldSeparator());

            String[] lSubFields = new String[lMultiField.getFields().size()];
            int i = 0;
            for (Field lSubField : lMultiField.getFields()) {
                lSubFields[i++] = lSubField.getLabelKey();
            }
            lMultiFieldData.setSubfields(lSubFields);
            lFieldTypeData = lMultiFieldData;
        }
        else {
            throw new RuntimeException("Unsupported field type '"
                    + pField.getClass().getName() + "'");
        }

        // Fill others attributes
        lFieldTypeData.setId(pField.getId());

        lFieldTypeData.setLabelKey(pField.getLabelKey());

        if (StringUtils.isNotBlank(pField.getDescription())) {
            lFieldTypeData.setDescription(getI18nService().getValueForUser(
                    pRoleToken, pField.getDescription()));
        }
        else {
            lFieldTypeData.setDescription(pField.getDescription());
        }

        lFieldTypeData.setMultipleValueSupport(pField.isMultiValued());

        // Create the default field access
        FieldAccessData lDefaultAccess = new FieldAccessData();
        lDefaultAccess.setConfidential(pField.isConfidential());
        lDefaultAccess.setExportable(pField.isExportable());
        lDefaultAccess.setMandatory(pField.isMandatory());
        lDefaultAccess.setUpdatable(pField.isUpdatable());
        lFieldTypeData.setDefaultAccess(lDefaultAccess);

        return lFieldTypeData;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.fields.service.FieldsService
     *      #addToDisplayGroupData(org.topcased.gpm.business.facilities.DisplayGroupData,
     *      org.topcased.gpm.business.fields.FieldData)
     */
    @Override
    public void addToDisplayGroupData(String pRoleToken,
            DisplayGroupData pDisplayGroupData, String pFieldName) {
        FieldSummaryData[] lSummaries = null;
        int lLength;

        if (null == pDisplayGroupData.getFieldSummaryDatas()) {
            lSummaries = new FieldSummaryData[1];
            lLength = 0;
        }
        else {
            lLength = pDisplayGroupData.getFieldSummaryDatas().length;

            lSummaries = new FieldSummaryData[lLength + 1];
            for (int i = 0; i < lLength; i++) {
                lSummaries[i] = pDisplayGroupData.getFieldSummaryDatas()[i];
            }
        }
        Field lField =
                fieldsManager.getField(
                        getFieldsContainer(pDisplayGroupData.getContainerId()),
                        pFieldName);
        lSummaries[lLength] =
                new FieldSummaryData(lField.getLabelKey(),
                        getI18nService().getValueForUser(pRoleToken,
                                lField.getLabelKey()), null,
                        FieldsManager.getFieldType(lField));
        pDisplayGroupData.setFieldSummaryDatas(lSummaries);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.fields.service.FieldsService#getInputDataType(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public org.topcased.gpm.business.serialization.data.InputDataType getInputDataType(
            String pRoleToken, String pInputDataTypeId) {
        CacheableInputDataType lCachedInputDataType =
                getCachedElement(CacheableInputDataType.class,
                        pInputDataTypeId, CACHE_IMMUTABLE_OBJECT);
        if (null == lCachedInputDataType) {
            InputDataType lInputDataType = getInputDataType(pInputDataTypeId);
            List<org.topcased.gpm.domain.facilities.DisplayGroup> lDisplayGroups;
            lDisplayGroups =
                    getDisplayGroupDao().getDisplayGroup(lInputDataType);
            lCachedInputDataType =
                    new CacheableInputDataType(lInputDataType, lDisplayGroups);
            addElementInCache(lCachedInputDataType);
        }
        org.topcased.gpm.business.serialization.data.InputDataType lInputDataTypeSerialization =
                new org.topcased.gpm.business.serialization.data.InputDataType();
        lCachedInputDataType.marshal(lInputDataTypeSerialization);
        return lInputDataTypeSerialization;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.fields.service.FieldsService#getCacheableInputDataType(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    @Override
    public CacheableInputDataType getCacheableInputDataType(String pRoleToken,
            String pInputDataTypeName, String pBusinessProcessName) {
        return getCacheableInputDataTypeByName(pRoleToken, pInputDataTypeName,
                pBusinessProcessName, CacheProperties.IMMUTABLE);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.fields.service.FieldsService#getCacheableInputDataType(java.lang.String,
     *      java.lang.String, org.topcased.gpm.util.bean.CacheProperties)
     */
    @Override
    public CacheableInputDataType getCacheableInputDataType(String pRoleToken,
            String pInputDataTypeId, CacheProperties pProperties) {
        CacheableInputDataType lCachedInputDataType =
                getCachedElement(CacheableInputDataType.class,
                        pInputDataTypeId, pProperties.getCacheFlags());

        if (null == lCachedInputDataType) {
            InputDataType lInputDataType = getInputDataType(pInputDataTypeId);
            List<org.topcased.gpm.domain.facilities.DisplayGroup> lDisplayGroups;
            lDisplayGroups =
                    getDisplayGroupDao().getDisplayGroup(lInputDataType);
            lCachedInputDataType =
                    new CacheableInputDataType(lInputDataType, lDisplayGroups);
            // Add displayGroups and displayHints
            addElementInCache(lCachedInputDataType);
        }

        if (pProperties.getSpecificAccessControl() == null) {
            return lCachedInputDataType;
        }
        // else
        return authorizationService.getCheckedFieldsContainer(pRoleToken,
                pProperties.getSpecificAccessControl(), lCachedInputDataType);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.fields.service.FieldsService#getCacheableInputDataTypeByName(java.lang.String,
     *      java.lang.String, java.lang.String,
     *      org.topcased.gpm.util.bean.CacheProperties)
     */
    @Override
    public CacheableInputDataType getCacheableInputDataTypeByName(
            String pRoleToken, String pInputDataTypeName,
            String pBusinessProcessName, CacheProperties pProperties) {
        final String lInputDateTypeId =
                fieldsContainerServiceImpl.getFieldsContainerId(pRoleToken,
                        pInputDataTypeName);
        if (null == lInputDateTypeId) {
            return null;
        }
        // else
        return getCacheableInputDataType(pRoleToken, lInputDateTypeId,
                pProperties);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.fields.service.FieldsService#getInputDataType(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    @Override
    public org.topcased.gpm.business.serialization.data.InputDataType getInputDataType(
            String pRoleToken, String pInputDataTypeName,
            String pBusinessProcessName) {

        CacheableInputDataType lCachedInputDataType =
                getCacheableInputDataType(pRoleToken, pInputDataTypeName,
                        pBusinessProcessName);
        if (lCachedInputDataType == null) {
            return null;
        }
        org.topcased.gpm.business.serialization.data.InputDataType lInputDataTypeSerialization =
                new org.topcased.gpm.business.serialization.data.InputDataType();
        lCachedInputDataType.marshal(lInputDataTypeSerialization);
        return lInputDataTypeSerialization;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.fields.service.FieldsService#createInputDataType(java.lang.String,
     *      java.lang.String,
     *      org.topcased.gpm.business.serialization.data.InputDataType)
     */
    @Override
    public String createInputDataType(String pRoleToken, String pProcessName,
            org.topcased.gpm.business.serialization.data.InputDataType pData) {
        if (pData == null) {
            throw new GDMException("Null input data type cannot be created.");
        }
        if (null == pData.getName()) {
            throw new InvalidNameException("Name is null");
        }
        String lId =
                getFieldsContainerDao().getFieldsContainerId(pData.getName(),
                        pProcessName);
        if (lId != null) {
            throw new GDMException("inputDataType '" + pData.getName()
                    + "' already exist.");
        }

        BusinessProcess lBusinessProc = getBusinessProcess(pProcessName);
        InputDataType lInputDataType = InputDataType.newInstance();

        lInputDataType.setName(pData.getName());
        lInputDataType.setBusinessProcess(lBusinessProc);
        lInputDataType.setDescription(pData.getDescription());
        getInputDataTypeDao().create(lInputDataType);
        return lInputDataType.getId();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.fields.service.FieldsService#removeInputDataType(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public void removeInputDataType(String pRoleToken, String pInputDataTypeId) {
        //        getInputDataType(pInputDataTypeId).getExtendedAction().setInputDataType(null);
        final String lQueryStr =
                "from " + ExtendedAction.class.getName()
                        + " ea where ea.inputDataType.id = :idTypeId";
        Query lQuery =
                GpmSessionFactory.getHibernateSession().createQuery(lQueryStr);
        lQuery.setParameter("idTypeId", pInputDataTypeId);
        ExtendedAction lEA = (ExtendedAction) lQuery.uniqueResult();
        if (lEA != null) {
            lEA.setInputDataType(null);
        }
        getFieldGroupDao().deleteGroups(getInputDataType(pInputDataTypeId));
        getInputDataTypeDao().remove(pInputDataTypeId);
        removeElementFromCache(pInputDataTypeId);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.fields.service.FieldsService#createInputData(java.lang.String,
     *      java.lang.String,
     *      org.topcased.gpm.business.serialization.data.InputDataType)
     */
    @Override
    public InputData createInputData(
            String pRoleToken,
            String pProcessName,
            org.topcased.gpm.business.serialization.data.InputDataType pInputDataType) {

        CacheableInputDataType lCacheableInputDataType =
                getCacheableInputDataType(pRoleToken, pInputDataType.getName(),
                        pProcessName);

        CacheableInputData lCacheableInputData =
                getInputDataModel(pRoleToken, lCacheableInputDataType, null);
        InputData lInputData = new InputData();
        lCacheableInputData.marshal(lInputData);
        return lInputData;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.fields.service.FieldsService#getInputDataModel(java.lang.String,
     *      org.topcased.gpm.business.fields.impl.CacheableInputDataType,
     *      org.topcased.gpm.business.extensions.service.Context)
     */
    @Override
    public CacheableInputData getInputDataModel(String pRoleToken,
            CacheableInputDataType pCacheableInputDataType, Context pContext) {
        final String lCurrentProductName =
                authorizationService.getProductNameFromSessionToken(pRoleToken);
        final String lProcessName =
                authorizationService.getProcessNameFromToken(pRoleToken);
        final CacheableProduct lCurrentProduct =
                getProductService().getCacheableProductByName(
                        lCurrentProductName, lProcessName,
                        CacheProperties.IMMUTABLE);

        CacheableInputData lInputData =
                (CacheableInputData) fieldsContainerServiceImpl.getValuesContainerModel(
                        pRoleToken, pCacheableInputDataType,
                        lCurrentProductName,
                        lCurrentProduct.getEnvironmentNames());

        // Extension point postGetSheetModel or postGetModel
        final ExtensionPoint lPostGetModel =
                getExecutableExtensionPoint(pCacheableInputDataType.getId(),
                        ExtensionPointNames.POST_GET_MODEL, pContext);

        if (lPostGetModel != null) {
            final ContextBase lContext = new ContextBase(pContext);
            lContext.put(ExtensionPointParameters.INPUT_DATA, lInputData);

            getExtensionsService().execute(pRoleToken, lPostGetModel, lContext);
        }

        return lInputData;
    }

    /**
     * Check for all mandatory fields of a fields container. This method throws
     * a MandatoryValuesException if any mandatory field is unvalued.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pCacheableValuesContainer
     *            values container
     * @param pCacheableFieldsContainer
     *            fields container
     */
    @Override
    public void checkMandatoryFields(String pRoleToken,
            CacheableValuesContainer pCacheableValuesContainer,
            CacheableFieldsContainer pCacheableFieldsContainer) {
        Collection<MandatoryValuesException.FieldRef> lMissingFieldsRef = null;

        Collection<? extends org.topcased.gpm.business.serialization.data.Field> lToplevelFields =
                pCacheableFieldsContainer.getFields();
        for (org.topcased.gpm.business.serialization.data.Field lField : lToplevelFields) {
            if (lField.getMandatory()) {
                Object lValue =
                        pCacheableValuesContainer.getValue(lField.getLabelKey());
                if (lValue == null
                        || (lValue instanceof FieldValueData && StringUtils.isBlank(((FieldValueData) lValue).getValue()))) {
                    if (null == lMissingFieldsRef) {
                        lMissingFieldsRef =
                                new ArrayList<MandatoryValuesException.FieldRef>();
                    }
                    lMissingFieldsRef.add(new MandatoryValuesException.FieldRef(
                            lField.getLabelKey(),
                            getI18nService().getValueForUser(pRoleToken,
                                    lField.getLabelKey())));
                }
            }
            if (lField.getMultiple()) {
                for (org.topcased.gpm.business.serialization.data.Field lSubField : ((org.topcased.gpm.business.serialization.data.MultipleField) lField).getFields()) {
                    if (lSubField.getMandatory()) {
                        FieldValueData lValue =
                                pCacheableValuesContainer.getValue(
                                        lField.getLabelKey(),
                                        lSubField.getLabelKey());
                        if (lValue == null
                                || StringUtils.isBlank(lValue.getValue())) {
                            if (null == lMissingFieldsRef) {
                                lMissingFieldsRef =
                                        new ArrayList<MandatoryValuesException.FieldRef>();
                            }
                            lMissingFieldsRef.add(new MandatoryValuesException.FieldRef(
                                    lSubField.getLabelKey(),
                                    getI18nService().getValueForUser(
                                            pRoleToken, lField.getLabelKey())));
                        }
                    }
                }
            }
        }
        if (lMissingFieldsRef != null) {
            throw new MandatoryValuesException(lMissingFieldsRef);
        }
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<String> getChoiceStringList(String pRoleToken, String pTypeId,
            String pFieldId, Context pContext) {
        return (List<String>) getChoiceStringData(pRoleToken, pTypeId,
                pFieldId, pContext).get(
                ExtensionPointParameters.CHOICES_RESULT.getParameterName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HashMap<String, Object> getChoiceStringData(String pRoleToken,
            String pTypeId, String pFieldId, Context pContext) {
        CacheableFieldsContainer lFieldsContainer =
                getCachedFieldsContainer(pTypeId, CACHE_IMMUTABLE_OBJECT);
        org.topcased.gpm.business.serialization.data.Field lField =
                lFieldsContainer.getFieldFromId(pFieldId);
        org.topcased.gpm.business.serialization.data.SimpleField lSimpleField;

        try {
            lSimpleField =
                    (org.topcased.gpm.business.serialization.data.SimpleField) lField;
        }
        catch (ClassCastException e) {
            throw new InvalidIdentifierException(pFieldId,
                    "Field is not a simple field");
        }

        if (!lSimpleField.getValueType().equals("STRING")) {
            throw new InvalidIdentifierException(pFieldId,
                    "Field must be a String field");
        }

        DisplayHint lDisplayHint =
                lFieldsContainer.getDisplayHint(lField.getLabelKey());
        if (null == lDisplayHint) {
            HashMap<String, Object> lChoiceStringData =
                    new HashMap<String, Object>(2);
            lChoiceStringData.put(
                    ExtensionPointParameters.CHOICES_RESULT.getParameterName(),
                    Collections.emptyList());
            lChoiceStringData.put(
                    ExtensionPointParameters.CHOICES_RESULT_DEFAULT_VALUE.getParameterName(),
                    null);
            return lChoiceStringData;
        }
        ChoiceStringDisplayHint lChoiceStringHint;
        try {
            lChoiceStringHint = (ChoiceStringDisplayHint) lDisplayHint;
        }
        catch (ClassCastException e) {
            throw new InvalidIdentifierException(pFieldId,
                    "Field does not use a ChoiceString display hint");
        }
        return getChoiceStringData(pRoleToken,
                lChoiceStringHint.getExtensionPointName(), pFieldId,
                lFieldsContainer, pContext);
    }

    private HashMap<String, Object> getChoiceStringData(String pRoleToken,
            String pExtensionPointName, String pFieldId,
            CacheableFieldsContainer pFieldsContainer, Context pContext) {
        String lFieldName =
                pFieldsContainer.getFieldFromId(pFieldId).getLabelKey();
        Context lLocalCtx = new ContextBase(pContext);

        // Add type info to the context
        lLocalCtx.put(ExtensionPointParameters.FIELDS_CONTAINER_NAME,
                pFieldsContainer.getName());
        lLocalCtx.put(ExtensionPointParameters.FIELDS_CONTAINER_ID,
                pFieldsContainer.getId());

        // Add the field label
        lLocalCtx.put(ExtensionPointParameters.FIELD_NAME, lFieldName);
        lLocalCtx.put(ExtensionPointParameters.FIELD_ID, pFieldId);

        // Add result parameter
        lLocalCtx.put(ExtensionPointParameters.CHOICES_RESULT.getParameterName());
        lLocalCtx.put(ExtensionPointParameters.CHOICES_RESULT_DEFAULT_VALUE.getParameterName());

        getExtensionsService().execute(pRoleToken, pFieldsContainer.getId(),
                pExtensionPointName, lLocalCtx);

        // Get Results

        HashMap<String, Object> lChoiceStringData =
                new HashMap<String, Object>(2);

        List<String> lListStrings;
        Object lListRawResult =
                lLocalCtx.get(ExtensionPointParameters.CHOICES_RESULT.getParameterName());
        if (lListRawResult instanceof Collection<?>) {
            // If the result is a list, convert all elements to string
            lListStrings =
                    new ArrayList<String>(
                            ((Collection<?>) lListRawResult).size());
            for (Object lCurrent : (Collection<?>) lListRawResult) {
                lListStrings.add(lCurrent.toString());
            }
        }
        else if (lListRawResult instanceof Object[]) {
            lListStrings =
                    new ArrayList<String>(((Object[]) lListRawResult).length);
            for (Object lCurrentObj : (Object[]) lListRawResult) {
                lListStrings.add(lCurrentObj.toString());
            }
        }
        // If result is a string object, create a singleton list.
        else if (lListRawResult instanceof String) {
            lListStrings = Collections.singletonList((String) lListRawResult);
        }
        // Any other returned type is not supported, and result is considered as empty.
        else {
            lListStrings = Collections.emptyList();
        }

        Object lDefaultRawResult =
                lLocalCtx.get(ExtensionPointParameters.CHOICES_RESULT_DEFAULT_VALUE.getParameterName());
        String lDefaultString = null;
        if (lDefaultRawResult instanceof String) {
            lDefaultString = (String) lDefaultRawResult;
        }

        lChoiceStringData.put(
                ExtensionPointParameters.CHOICES_RESULT.getParameterName(),
                lListStrings);
        lChoiceStringData.put(
                ExtensionPointParameters.CHOICES_RESULT_DEFAULT_VALUE.getParameterName(),
                lDefaultString);

        return lChoiceStringData;
    }

    /**
     * Gets the input data type.
     * 
     * @param pId
     *            the id
     * @return the input data type
     */
    private InputDataType getInputDataType(String pId) {
        return (InputDataType) getInputDataTypeDao().load(pId);
    }

    /** The input data type dao. */
    private InputDataTypeDao inputDataTypeDao;

    /**
     * getInputDataTypeDao.
     * 
     * @return the InputDataTypeDao
     */
    public InputDataTypeDao getInputDataTypeDao() {
        return inputDataTypeDao;
    }

    /**
     * setInputDataTypeDao.
     * 
     * @param pInputDataTypeDao
     *            the InputDataTypeDao to set
     */
    public void setInputDataTypeDao(InputDataTypeDao pInputDataTypeDao) {
        inputDataTypeDao = pInputDataTypeDao;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.fields.service.FieldsService#getCacheableFieldsContainer(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public CacheableFieldsContainer getCacheableFieldsContainer(
            String pRoleToken, String pFieldsContainerId)
        throws AuthorizationException {
        return getCacheableFieldsContainer(pRoleToken, pFieldsContainerId,
                CacheProperties.IMMUTABLE);
    }

    /**
     * Gets a cached fields container from its identifier
     * 
     * @param pFieldsContainerId
     *            Fields container identifier
     * @return A cached fields container
     */
    @Override
    public CacheableFieldsContainer getCacheableFieldsContainer(
            String pRoleToken, String pFieldsContainerId,
            CacheProperties pProperties) throws AuthorizationException {
        //Verify authorization
        AccessControlContextData lAccessControlContextData =
                new AccessControlContextData();
        //No product for a fields container
        lAccessControlContextData.setProductName(null);
        //No state for a fields container
        lAccessControlContextData.setStateName(null);
        lAccessControlContextData.setRoleName(getAuthService().getRoleNameFromToken(
                pRoleToken));
        lAccessControlContextData.setContainerTypeId(null);
        TypeAccessControlData lTypeAccessControlData =
                authorizationService.getTypeAccessControl(pRoleToken,
                        lAccessControlContextData);
        if (lTypeAccessControlData.getConfidential()) {
            throw new AuthorizationException(
                    "Illegal access to the container type "
                            + pFieldsContainerId
                            + " : the access is confidential ");
        }
        return getCacheableFieldsContainer(pFieldsContainerId, pProperties);
    }

    /**
     * @param pFieldsContainerId
     * @param pProperties
     * @return
     */
    public CacheableFieldsContainer getCacheableFieldsContainer(
            String pFieldsContainerId, CacheProperties pProperties) {
        return getCachedFieldsContainer(pFieldsContainerId,
                pProperties.getCacheFlags());
    }

    /**
     * Get a cacheable values container.
     * <p>
     * Note: If the object is not present in the cache, it is read from the DB
     * (and stored in the cache).
     * 
     * @param pElemId
     *            Identifier of the element
     * @param pCacheProperties
     *            The cache properties
     * @return The cached element, or null if not found
     */
    public final CacheableValuesContainer getCacheableValuesContainer(
            String pElemId, CacheProperties pCacheProperties) {
        return getCachedValuesContainer(pElemId,
                pCacheProperties.getCacheFlags());
    }

    /** The field dao. */
    private FieldDao fieldDao;

    /**
     * Gets the field dao.
     * 
     * @return the field dao
     */
    public final FieldDao getFieldDao() {
        return fieldDao;
    }

    /**
     * Sets the field dao.
     * 
     * @param pFieldDao
     *            the new field dao
     */
    public final void setFieldDao(FieldDao pFieldDao) {
        fieldDao = pFieldDao;
    }

    /** The display group dao. */
    private DisplayGroupDao displayGroupDao;

    /**
     * getDisplayGroupDao.
     * 
     * @return the DisplayGroupDao
     */
    public DisplayGroupDao getDisplayGroupDao() {
        return displayGroupDao;
    }

    /**
     * setDisplayGroupDao.
     * 
     * @param pDisplayGroupDao
     *            the DisplayGroupDao to set
     */
    public void setDisplayGroupDao(DisplayGroupDao pDisplayGroupDao) {
        displayGroupDao = pDisplayGroupDao;
    }

    /** The fieldGroupDao */
    private FieldGroupDao fieldGroupDao;

    /**
     * Get the fieldGroupDao return the fieldGroupDao
     */
    public FieldGroupDao getFieldGroupDao() {
        return fieldGroupDao;
    }

    /**
     * Set the fieldGroupDao
     * 
     * @param pFieldGroupDao
     *            the fieldGroupdao to set.
     */
    public void setFieldGroupDao(FieldGroupDao pFieldGroupDao) {
        fieldGroupDao = pFieldGroupDao;
    }

    /** The field access control dao. */
    private FieldAccessControlDao fieldAccessControlDao;

    /**
     * getFieldAccessControlDao
     * 
     * @return the FieldAccessControlDao
     */
    public FieldAccessControlDao getFieldAccessControlDao() {
        return fieldAccessControlDao;
    }

    /**
     * setFieldAccessControlDao
     * 
     * @param pFieldAccessControlDao
     *            the FieldAccessControlDao to set
     */
    public void setFieldAccessControlDao(
            FieldAccessControlDao pFieldAccessControlDao) {
        fieldAccessControlDao = pFieldAccessControlDao;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.fields.service.FieldsService#getPointedField(java.lang.String,
     *      java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public BusinessField getPointedField(String pRoleToken,
            String pPointerFieldLabel, String pPointedValuesContainerId,
            String pPointedFieldLabel) {
        return fieldsManager.getPointedField(pRoleToken, pPointerFieldLabel,
                pPointedValuesContainerId, pPointedFieldLabel);
    }

    private class CacheableInputDataTypeFactory implements CacheableFactory {
        public Object createCacheableObject(Object pEntityObject) {
            InputDataType lInputDataType = (InputDataType) pEntityObject;
            List<org.topcased.gpm.domain.facilities.DisplayGroup> lDisplayGroups;
            lDisplayGroups =
                    getDisplayGroupDao().getDisplayGroup(lInputDataType);

            return new CacheableInputDataType(lInputDataType, lDisplayGroups);
        }

        public Class<?>[] getSupportedClasses() {
            return new Class<?>[] { InputDataType.class };
        }
    }
    
    
    /**
     * {@inheritDoc}
     */
    public Object getPointedFieldValue(String pRoleToken, String pFieldLabel,
            String pReferencedContainerId, String pReferencedFieldLabel) {
        return fieldsManager.getPointedFieldValue(pRoleToken, pFieldLabel,
                pReferencedContainerId, pReferencedFieldLabel);
    }
}
