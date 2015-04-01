/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.values.impl;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.ServiceImplBase;
import org.topcased.gpm.business.exception.InstantiateException;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.extensions.service.ExtensionPointParameters;
import org.topcased.gpm.business.serialization.data.FieldMapping;
import org.topcased.gpm.business.serialization.data.TypeMapping;
import org.topcased.gpm.business.serialization.data.ValueMapping;
import org.topcased.gpm.business.values.BusinessContainer;
import org.topcased.gpm.business.values.field.BusinessField;
import org.topcased.gpm.business.values.field.multivalued.BusinessMultivaluedField;
import org.topcased.gpm.business.values.field.simple.BusinessAttachedField;
import org.topcased.gpm.business.values.field.simple.BusinessChoiceField;
import org.topcased.gpm.business.values.field.simple.BusinessPointerField;
import org.topcased.gpm.business.values.field.simple.BusinessSimpleField;
import org.topcased.gpm.business.values.service.ValuesService;
import org.topcased.gpm.domain.businessProcess.BusinessProcess;
import org.topcased.gpm.domain.extensions.ExtensionPoint;
import org.topcased.gpm.domain.fields.AttachedField;
import org.topcased.gpm.domain.fields.ChoiceField;
import org.topcased.gpm.domain.fields.Field;
import org.topcased.gpm.domain.fields.FieldsContainer;
import org.topcased.gpm.domain.fields.SimpleField;
import org.topcased.gpm.domain.mapping.FieldMap;
import org.topcased.gpm.domain.mapping.FieldMapDao;
import org.topcased.gpm.domain.mapping.TypeMap;
import org.topcased.gpm.domain.mapping.TypeMapDao;
import org.topcased.gpm.domain.mapping.ValueMap;
import org.topcased.gpm.domain.mapping.ValueMapDao;

/**
 * Values Service Implementation.
 * 
 * @author tpanuel
 */
public class ValuesServiceImpl extends ServiceImplBase implements ValuesService {
    private TypeMapDao typeMapDao;

    private FieldMapDao fieldMapDao;

    private ValueMapDao valueMapDao;

    private TypeMappingManager typeMappingManager;

    /**
     * Setter used by Spring for injection.
     * 
     * @param pTypeMapDao
     *            The new DAO.
     */
    public void setTypeMapDao(final TypeMapDao pTypeMapDao) {
        typeMapDao = pTypeMapDao;
    }

    /**
     * Setter used by Spring for injection.
     * 
     * @param pFieldMapDao
     *            The new DAO.
     */
    public void setFieldMapDao(final FieldMapDao pFieldMapDao) {
        fieldMapDao = pFieldMapDao;
    }

    /**
     * Setter used by Spring for injection.
     * 
     * @param pValueMapDao
     *            The new DAO.
     */
    public void setValueMapDao(final ValueMapDao pValueMapDao) {
        valueMapDao = pValueMapDao;
    }

    /**
     * Setter used by Spring for injection.
     * 
     * @param pTypeMappingManager
     *            The new Manager.
     */
    public void setTypeMappingManager(
            final TypeMappingManager pTypeMappingManager) {
        typeMappingManager = pTypeMappingManager;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.service.ValuesService#initializeValues(java.lang.String,
     *      org.topcased.gpm.business.values.BusinessContainer,
     *      org.topcased.gpm.business.values.BusinessContainer,
     *      org.topcased.gpm.business.extensions.service.Context)
     */
    public void initializeValues(final String pRoleToken,
            final BusinessContainer pOrigin,
            final BusinessContainer pDestination, final Context pContext) {
        // Load the mapping information
        final TypeMappingDefinition lMapping =
                typeMappingManager.getElement(new TypeMappingKey(
                        pOrigin.getBusinessProcessName(),
                        pOrigin.getTypeName(),
                        pDestination.getBusinessProcessName(),
                        pDestination.getTypeName()));

        // If a mapping has been defined
        if (lMapping != null && lMapping.getFieldMapping() != null) {
            for (Entry<String, String> lFieldMapping : lMapping.getFieldMapping().entrySet()) {
                final BusinessField lOriginField =
                        pOrigin.getField(lFieldMapping.getKey());
                final BusinessField lDestinationField =
                        pDestination.getField(lFieldMapping.getValue());
                final String lExtensionPointName =
                        lMapping.getExtensionPoints().get(
                                lFieldMapping.getKey());

                // If an extension point has been defined : execute it
                if (lExtensionPointName != null) {
                    final ExtensionPoint lExtensionPoint =
                            getExecutableExtensionPoint(
                                    getBusinessProcess(
                                            getBusinessProcessName(pRoleToken)).getId(),
                                    lExtensionPointName, pContext);

                    if (lExtensionPoint != null) {
                        final Context lContext =
                                Context.createContext(pContext);

                        // Fill the context
                        lContext.put(
                                ExtensionPointParameters.BUSINESS_CONTAINER,
                                pDestination);
                        lContext.put(ExtensionPointParameters.BUSINESS_FIELD,
                                lDestinationField);
                        lContext.put(
                                ExtensionPointParameters.SOURCE_BUSINESS_CONTAINER,
                                pOrigin);
                        lContext.put(
                                ExtensionPointParameters.SOURCE_BUSINESS_FIELD,
                                lOriginField);
                        // Launch the extension point
                        getExtensionsService().execute(pRoleToken,
                                lExtensionPoint, lContext);
                    }
                }
                // Else copy the value
                else {
                    final Map<String, String> lValueMapping =
                            lMapping.getValueMapping().get(
                                    lFieldMapping.getKey());

                    // Copy value of origin field on destination field
                    lDestinationField.copy(lOriginField);

                    // If a value mapping has been defined, the value can be changed
                    if (lValueMapping != null && !lValueMapping.isEmpty()) {
                        if (lDestinationField instanceof BusinessMultivaluedField) {
                            for (BusinessField lField : (BusinessMultivaluedField<?>) lDestinationField) {
                                modifyValueUsingMapping(lField, lValueMapping);
                            }
                        }
                        else {
                            modifyValueUsingMapping(lDestinationField,
                                    lValueMapping);
                        }
                    }
                }
            }
        }
    }

    private void modifyValueUsingMapping(final BusinessField pField,
            final Map<String, String> pValueMapping) {
        // For simple field, mapping is on the string value
        if (pField instanceof BusinessSimpleField) {
            final BusinessSimpleField<?> lSimpleField =
                    (BusinessSimpleField<?>) pField;
            final String lNewValue =
                    pValueMapping.get(lSimpleField.getAsString());

            if (lNewValue != null) {
                lSimpleField.setAsString(lNewValue);
            }
        }
        // For choice field, mapping is on the category value
        else if (pField instanceof BusinessChoiceField) {
            final BusinessChoiceField lChoiceField =
                    (BusinessChoiceField) pField;
            final String lNewValue =
                    pValueMapping.get(lChoiceField.getCategoryValue());

            if (lNewValue != null) {
                lChoiceField.setCategoryValue(lNewValue);
            }
        }
        // For attached field, mapping is on the file name
        else if (pField instanceof BusinessAttachedField) {
            final BusinessAttachedField lAttachedField =
                    (BusinessAttachedField) pField;
            final String lNewValue =
                    pValueMapping.get(lAttachedField.getFileName());

            if (lNewValue != null) {
                lAttachedField.setFileName(lNewValue);
            }
        }
        // For pointer field, mapping is on the pointed field label
        else if (pField instanceof BusinessPointerField) {
            final BusinessPointerField lPointerField =
                    (BusinessPointerField) pField;
            final String lNewValue =
                    pValueMapping.get(lPointerField.getPointedFieldName());

            if (lNewValue != null) {
                lPointerField.setPointedFieldName(lNewValue);
            }
        }
        // Else the value is not modified
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.service.ValuesService#createTypeMapping(java.lang.String,
     *      org.topcased.gpm.business.serialization.data.TypeMapping)
     */
    public void createTypeMapping(final String pRoleToken,
            final TypeMapping pTypeMapping) throws InstantiateException {
        final BusinessProcess lCurrentProcess =
                getBusinessProcess(getBusinessProcessName(pRoleToken));
        final String lOriginProcess;
        final String lDestinationProcess;

        // If the process name is not defined used the current one
        if (pTypeMapping.getOriginProcessName() == null) {
            lOriginProcess = lCurrentProcess.getName();
        }
        else {
            lOriginProcess = pTypeMapping.getOriginProcessName();
        }
        if (pTypeMapping.getDestinationProcessName() == null) {
            lDestinationProcess = lCurrentProcess.getName();
        }
        else {
            lDestinationProcess = pTypeMapping.getDestinationProcessName();
        }

        // Check origin or destination type, only if their are defined on the current process
        final FieldsContainer lOriginContainer =
                checkTypeExistence(lCurrentProcess, lOriginProcess,
                        pTypeMapping.getOriginTypeName(), pTypeMapping);
        final FieldsContainer lDestinationContainer =
                checkTypeExistence(lCurrentProcess, lDestinationProcess,
                        pTypeMapping.getDestinationTypeName(), pTypeMapping);

        TypeMap lTypeMap =
                typeMapDao.getTypeMapping(lOriginProcess,
                        pTypeMapping.getOriginTypeName(), lDestinationProcess,
                        pTypeMapping.getDestinationTypeName());

        if (lTypeMap == null) {
            // Create a new type mapping
            lTypeMap = TypeMap.newInstance();
            lTypeMap.setOriginProcessName(lOriginProcess);
            lTypeMap.setOriginTypeName(pTypeMapping.getOriginTypeName());
            lTypeMap.setDestinationProcessName(lDestinationProcess);
            lTypeMap.setDestinationTypeName(pTypeMapping.getDestinationTypeName());
            typeMapDao.create(lTypeMap);
        }
        else {
            // The field mapping is erased
            for (FieldMap lOldFieldMap : lTypeMap.getFieldMaps()) {
                fieldMapDao.remove(lOldFieldMap);
            }
            lTypeMap.getFieldMaps().clear();
        }

        // Save field mapping, if exists
        if (pTypeMapping.getFieldMapping() != null) {
            for (FieldMapping lFieldMapping : pTypeMapping.getFieldMapping()) {
                // Check if origin and destination field exists
                final Field lOriginField =
                        checkFieldExistence(lOriginContainer,
                                lFieldMapping.getOriginLabelKey(), pTypeMapping);
                final Field lDestinationField =
                        checkFieldExistence(lDestinationContainer,
                                lFieldMapping.getDestinationLabelKey(),
                                pTypeMapping);

                final FieldMap lFieldMap = FieldMap.newInstance();

                lFieldMap.setOriginLabelKey(lFieldMapping.getOriginLabelKey());
                lFieldMap.setDestinationLabelKey(lFieldMapping.getDestinationLabelKey());
                fieldMapDao.create(lFieldMap);
                lTypeMap.addToFieldMapList(lFieldMap);

                // Save value mapping, if exists
                if (lFieldMapping.getValueMapping() != null
                        && !lFieldMapping.getValueMapping().isEmpty()) {
                    // Check that the field type allow a value mapping
                    checkFieldTypeAllowValueMapping(lOriginField, pTypeMapping);
                    checkFieldTypeAllowValueMapping(lDestinationField,
                            pTypeMapping);

                    // It's not possible to define both a value mapping and an extension point
                    if (lFieldMapping.getExtensionPointName() != null) {
                        throw new InstantiateException(
                                "It's not possible to define both a mapping and an "
                                        + "extension point for the same field mapping.",
                                pTypeMapping);
                    }

                    for (ValueMapping lValueMapping : lFieldMapping.getValueMapping()) {
                        final ValueMap lValueMap = ValueMap.newInstance();

                        lValueMap.setOriginValue(lValueMapping.getOriginValue());
                        lValueMap.setDestinationValue(lValueMapping.getDestinationValue());
                        valueMapDao.create(lValueMap);
                        lFieldMap.addToValueMapList(lValueMap);
                    }
                }

                // Save extension point, if exists
                if (lFieldMapping.getExtensionPointName() != null) {
                    final ExtensionPoint lExtensionPoint =
                            getExecutableExtensionPoint(
                                    lCurrentProcess.getId(),
                                    lFieldMapping.getExtensionPointName(), null);

                    // Check if extension point exists
                    if (lExtensionPoint == null) {
                        throw new InstantiateException(
                                "The extension point named "
                                        + lFieldMapping.getExtensionPointName()
                                        + " does not exist.", pTypeMapping);
                    }
                    lFieldMap.setExtensionPoint(lExtensionPoint);
                }
            }
        }

        // Clean the cache
        typeMappingManager.depreciateElement(new TypeMappingKey(pTypeMapping));
    }

    private FieldsContainer checkTypeExistence(
            final BusinessProcess pCurrentProcess,
            final String pTypeProcessName, final String pTypeName,
            final TypeMapping pTypeMapping) {
        FieldsContainer lContainer = null;

        if (StringUtils.equals(pCurrentProcess.getName(), pTypeProcessName)) {
            lContainer =
                    getFieldsContainerDao().getFieldsContainer(pCurrentProcess,
                            pTypeName);
            if (lContainer == null) {
                throw new InstantiateException("The type " + pTypeName
                        + " of the business process "
                        + pCurrentProcess.getName() + " does not exist.",
                        pTypeMapping);
            }
        }

        return lContainer;
    }

    private Field checkFieldExistence(final FieldsContainer pContainer,
            final String pFieldLabel, final TypeMapping pTypeMapping) {
        Field lField = null;

        // No check if container is not defined
        if (pContainer != null) {
            final String[] lLabelParts = pFieldLabel.split("\\|");

            for (String lLabelPart : lLabelParts) {
                lField =
                        getFieldsContainerDao().getField(pContainer, lLabelPart);
                if (lField == null) {
                    throw new InstantiateException("The type "
                            + pContainer.getName()
                            + " of the business process "
                            + pContainer.getBusinessProcess().getName()
                            + " has no field named " + lLabelPart + ".",
                            pTypeMapping);
                }
            }
        }

        return lField;
    }

    private void checkFieldTypeAllowValueMapping(final Field pField,
            final TypeMapping pTypeMapping) {
        if (pField != null && !(pField instanceof SimpleField)
                && !(pField instanceof ChoiceField)
                && !(pField instanceof AttachedField)) {
            throw new InstantiateException(
                    "Value mapping is not available for field's type: "
                            + pField.getClass().getName(), pTypeMapping);
        }
    }
}