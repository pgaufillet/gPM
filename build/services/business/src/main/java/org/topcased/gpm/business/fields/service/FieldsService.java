/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.fields.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.facilities.DisplayGroupData;
import org.topcased.gpm.business.fields.AttachedFieldData;
import org.topcased.gpm.business.fields.ChoiceFieldData;
import org.topcased.gpm.business.fields.FieldTypeData;
import org.topcased.gpm.business.fields.MultipleFieldData;
import org.topcased.gpm.business.fields.SimpleFieldData;
import org.topcased.gpm.business.fields.impl.CacheableFieldsContainer;
import org.topcased.gpm.business.fields.impl.CacheableInputData;
import org.topcased.gpm.business.fields.impl.CacheableInputDataType;
import org.topcased.gpm.business.fields.impl.CacheableValuesContainer;
import org.topcased.gpm.business.serialization.data.InputData;
import org.topcased.gpm.business.serialization.data.InputDataType;
import org.topcased.gpm.business.values.field.BusinessField;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * Fields service
 * 
 * @author llatil
 */
@Transactional(propagation = Propagation.REQUIRED)
public interface FieldsService {

    /** Key of the 'reference' field. */
    public static final String REFERENCE_FIELD_NAME = "__Reference";

    /**
     * Default separator char. for reference field. It is used as separator for
     * the reference sub-fields
     */
    public static final String REFERENCE_FIELD_SEPARATOR = "-";

    /**
     * Default separator char. for multiple field. It is used as separator for
     * the sub-fields.
     */
    public static final String DEFAULT_FIELD_SEPARATOR = ":";

    /** Field label for sheet state */
    public static final String PROCESS_INSTANCE_LABEL =
            "$PROCESS_INSTANCE_LABEL";

    public static final String TRANSITION_NAME = "$TRANSITION_NAME";

    public static final String TRANSITION_DATE = "$TRANSITION_DATE";

    public static final String TRANSITION_ORIGIN_STATE =
            "$TRANSITION_ORIGIN_STATE";

    public static final String TRANSITION_DESTINATION_STATE =
            "$TRANSITION_DESTINATION_STATE";

    public static final String TRANSITION_USER_LOGIN = "$TRANSITION_USER_LOGIN";

    public static final String TRANSITION_USER_NAME = "$TRANSITION_USER_NAME";

    /**
     * Get information on a field.
     * 
     * @param pRoleToken
     *            The role session token.
     * @param pContainerId
     *            The identifier of the fields container containing the field
     * @param pFieldName
     *            Name of the field to retrieve
     * @return Field type data
     */
    public FieldTypeData getField(String pRoleToken, String pContainerId,
            String pFieldName);

    /**
     * Get information on a field retrieved using its unique identifier.
     * 
     * @param pRoleToken
     *            The role session token.
     * @param pFieldId
     *            The identifier of the field
     * @return Field type data
     */
    public FieldTypeData getField(String pRoleToken, String pFieldId);

    /**
     * Get names of all fields defined in a field container.
     * 
     * @param pRoleToken
     *            Role session token.
     * @param pContainerId
     *            Identifier of the fields container
     * @return Field type data
     */
    public String[] getFieldNames(String pRoleToken, String pContainerId);

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
    public void deleteField(String pRoleToken, String pContainerId,
            String pFieldName, boolean pDeleteValues);

    /**
     * Create (or update) a simple field definition
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
            SimpleFieldData pSimpleFieldData);

    /**
     * Create (or update) a choice field definition
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
            ChoiceFieldData pChoiceFieldData);

    /**
     * Create (or update) an attached file field definition
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
            AttachedFieldData pFieldData);

    /**
     * Create (or update) a multiple field definition
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
            MultipleFieldData pMultipleFieldData);

    /**
     * Add a field in a display group.
     * 
     * @param pRoleToken
     *            the role token
     * @param pDisplayGroupData
     *            the display group data
     * @param pFieldName
     *            the name of the field
     */
    public void addToDisplayGroupData(String pRoleToken,
            DisplayGroupData pDisplayGroupData, String pFieldName);

    /**
     * Get a InputDataType from its ID.
     * 
     * @param pRoleToken
     *            the role token
     * @param pInputDataTypeId
     *            Identifier of the InputDataType to get
     * @return a InputDataType
     */
    public InputDataType getInputDataType(String pRoleToken,
            String pInputDataTypeId);

    /**
     * Get a InputDataType from its name
     * 
     * @param pRoleToken
     *            the role token
     * @param pInputDataTypeName
     *            the InputDataTypeName
     * @param pBusinessProcessName
     *            the process name
     * @return the corresponding InputDataType.
     */
    public InputDataType getInputDataType(String pRoleToken,
            String pInputDataTypeName, String pBusinessProcessName);

    /**
     * Create a InputData from its type
     * 
     * @param pRoleToken
     *            the role token
     * @param pBusinessProcessName
     *            the process name
     * @param pInputDataType
     *            the InputDataType
     * @return the newly created InputData
     */
    public InputData createInputData(String pRoleToken,
            String pBusinessProcessName, InputDataType pInputDataType);

    /**
     * Create an InputDataType(except fields)
     * 
     * @param pRoleToken
     *            the roleToken
     * @param pProcessName
     *            the process name
     * @param pData
     *            the input data type
     * @return Identifier of the created input data type
     */
    public String createInputDataType(String pRoleToken, String pProcessName,
            org.topcased.gpm.business.serialization.data.InputDataType pData);

    /**
     * Create a cacheableInputDataType
     * 
     * @param pRoleToken
     *            the roleToken
     * @param pInputDataTypeName
     *            the input data type name
     * @param pBusinessProcessName
     *            the process name
     * @return CacheableInputData
     * @see FieldsService#getCacheableInputDataType(String, String, String,
     *      CacheProperties)
     */
    public CacheableInputDataType getCacheableInputDataType(String pRoleToken,
            String pInputDataTypeName, String pBusinessProcessName);

    /**
     * Get a CacheableInputData from its id
     * 
     * @param pRoleToken
     *            the roleToken
     * @param pInputDataTypeId
     *            the input data type id
     * @param pProperties
     *            the cache properties
     * @return the CacheableInputData
     */
    public CacheableInputDataType getCacheableInputDataType(String pRoleToken,
            String pInputDataTypeId, CacheProperties pProperties);

    /**
     * Get a CacheableInputData from its name
     * 
     * @param pRoleToken
     *            the roleToken
     * @param pInputDataTypeName
     *            the input data type name
     * @param pBusinessProcessName
     *            the process name
     * @param pProperties
     *            the cache properties
     * @return the CacheableInputData
     */
    public CacheableInputDataType getCacheableInputDataTypeByName(
            String pRoleToken, String pInputDataTypeName,
            String pBusinessProcessName, CacheProperties pProperties);

    /**
     * Remove a InputDataType
     * 
     * @param pRoleToken
     *            the roleToken
     * @param pInputDataTypeId
     *            the identifier of inputDataType to remove
     */
    public void removeInputDataType(String pRoleToken, String pInputDataTypeId);

    /**
     * Get an empty CacheableInputData modeled after the given inputData type.
     * Fields are filled with their default value (if any).
     * 
     * @param pRoleToken
     *            The roleToken
     * @param pCacheableInputDataType
     *            The InputData type
     * @param pContext
     *            Execution context.
     * @return An empty CacheableInputData.
     */
    public CacheableInputData getInputDataModel(String pRoleToken,
            CacheableInputDataType pCacheableInputDataType, Context pContext);

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
    public void checkMandatoryFields(String pRoleToken,
            CacheableValuesContainer pCacheableValuesContainer,
            CacheableFieldsContainer pCacheableFieldsContainer);

    /**
     * Get a list of all acceptable values for a 'choiceString' field.
     * <p>
     * The field passed as parameter for this method must be a String field, and
     * should have a 'choiceString' display hint. The user can enhance the
     * default context.
     * <p>
     * Default context:
     * <ul>
     * <li>typeName: Name of the choice string field container</li>
     * <li>typeId: Id of the choice string field container</li>
     * <li>fieldName: The choice string field name</li>
     * <li>fieldId: The choice string field id</li>
     * </ul>
     * 
     * @param pRoleToken
     *            Role session token
     * @param pTypeId
     *            Identifier of the values container
     * @param pFieldId
     *            Field identifier.
     * @param pContext
     *            Context of the extension action to get choice string value.
     *            Can be null.
     * @return List of values
     * @deprecated use
     *             {@link FieldsService#getChoiceStringData(String, String, String, Context)}
     *             instead
     * @since 2.0.4
     */
    public List<String> getChoiceStringList(String pRoleToken, String pTypeId,
            String pFieldId, Context pContext);

    /**
     * Get the data used by a 'choiceString' field.
     * <p>
     * Data of a 'choiceString' field are :
     * <ul>
     * <li>a list of values (key to use:
     * ExtensionPointParameters.CHOICES_RESULT.getParameterName())
     * <li>an optional default selected value (key to use:
     * ExtensionPointParameters.DEFAULT_VALUE_CHOICES_RESULT.getParameterName())
     * </ul>
     * <p>
     * The field passed as parameter for this method must be a String field, and
     * should have a 'choiceString' display hint. The user can enhance the
     * default context.
     * <p>
     * Default context:
     * <ul>
     * <li>typeName: Name of the choice string field container</li>
     * <li>typeId: Id of the choice string field container</li>
     * <li>fieldName: The choice string field name</li>
     * <li>fieldId: The choice string field id</li>
     * </ul>
     * 
     * @param pRoleToken
     *            Role session token
     * @param pTypeId
     *            Identifier of the values container
     * @param pFieldId
     *            Field identifier.
     * @param pContext
     *            Context of the extension action to get choice string value.
     *            Can be null.
     * @return List of values
     */
    public HashMap<String, Object> getChoiceStringData(String pRoleToken,
            String pTypeId, String pFieldId, Context pContext);

    /**
     * Gets a cached fields container from its identifier.
     * <p>
     * If the object is not present in the cache, it is read from the DB (and
     * stored in the cache).
     * 
     * @param pRoleToken
     *            Role token.
     * @param pFieldsContainerId
     *            Fields container identifier.
     * @return A cached fields container or null if not found
     * @throws AuthorizationException
     *             If the fields container is confidential for the given role.
     * @see FieldsService#getCacheableFieldsContainer(String, String,
     *      CacheProperties)
     */
    public CacheableFieldsContainer getCacheableFieldsContainer(
            String pRoleToken, String pFieldsContainerId)
        throws AuthorizationException;

    /**
     * Gets a cached fields container from its identifier.
     * <p>
     * If the object is not present in the cache, it is read from the DB (and
     * stored in the cache).
     * 
     * @param pRoleToken
     *            Role token.
     * @param pFieldsContainerId
     *            Fields container identifier.
     * @param pProperties
     *            the cache properties
     * @return A cached fields container or null if not found
     * @throws AuthorizationException
     *             If the fields container is confidential for the given role.
     */
    public CacheableFieldsContainer getCacheableFieldsContainer(
            String pRoleToken, String pFieldsContainerId,
            CacheProperties pProperties) throws AuthorizationException;

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
    public BusinessField getPointedField(final String pRoleToken,
            final String pPointerFieldLabel,
            final String pPointedValuesContainerId,
            final String pPointedFieldLabel);

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
            String pReferencedContainerId, String pReferencedFieldLabel);

}
