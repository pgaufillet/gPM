/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.fieldscontainer.service;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.topcased.gpm.business.exception.InvalidTokenException;
import org.topcased.gpm.business.fields.impl.CacheableFieldsContainer;
import org.topcased.gpm.business.fields.impl.CacheableValuesContainer;
import org.topcased.gpm.business.serialization.data.FieldsContainer;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * FieldsContainerService
 * 
 * @author mkargbo
 */
@Transactional(propagation = Propagation.REQUIRED)
public interface FieldsContainerService {

    /** CREATE_SHIFT */
    public static final int CREATE_SHIFT = 0;

    /** UPDATE_SHIFT */
    public static final int UPDATE_SHIFT = 1;

    /** DELETE_SHIFT */
    public static final int DELETE_SHIFT = 2;

    /** CONFIDENTIAL_SHIFT */
    public static final int NOT_CONFIDENTIAL_SHIFT = 3;

    public static final long CREATE = 1 << CREATE_SHIFT;

    public static final long UPDATE = 1 << UPDATE_SHIFT;

    public static final long DELETE = 1 << DELETE_SHIFT;

    public static final long NOT_CONFIDENTIAL = 1 << NOT_CONFIDENTIAL_SHIFT;

    /** FIELD_MANDATORY_SHIFT */
    public static final int FIELD_MANDATORY_SHIFT = 0;

    /** FIELD_UPDATE_SHIFT */
    public static final int FIELD_UPDATE_SHIFT = 1;

    /** FIELD_EXPORT_SHIFT */
    public static final int FIELD_EXPORT_SHIFT = 2;

    /** CONFIDENTIAL_SHIFT */
    public static final int FIELD_NOT_CONFIDENTIAL_SHIFT = 3;

    public static final int FIELD_NOT_MANDATORY_SHIFT = 4;

    /** Mandatory property of a field. */
    public static final long FIELD_MANDATORY = 1 << FIELD_MANDATORY_SHIFT;

    /** Update property of a field. */
    public static final long FIELD_UPDATE = 1 << FIELD_UPDATE_SHIFT;

    /** Export property of a field. */
    public static final long FIELD_EXPORT = 1 << FIELD_EXPORT_SHIFT;

    /** Not confidential property of a field. */
    public static final long FIELD_NOT_CONFIDENTIAL =
            1 << FIELD_NOT_CONFIDENTIAL_SHIFT;

    public static final long FIELD_NOT_MANDATORY =
            1 << FIELD_NOT_MANDATORY_SHIFT;

    /**
     * Get informations of all fields container for the specified type.
     * <p>
     * Retrieve the fields containers that respect the type access flag.
     * <p>
     * If a flag is not defined, the access property is not use.
     * <p>
     * Eg: Get all not confidential and sheet types that can be create.
     * 
     * <pre>
     * getFieldsContainerInfo(roleToken, SheetType.class, NOT_CONFIDENTIAL | CREATE)
     * </pre>
     * 
     * Note: If a type access is not defined, the access control property will
     * be ignore.<br />
     * e.g: If the type 'DELETE' is not set, it doesn't means that the property
     * is 'NOT_DELETE'. It means that the property will be ignore.
     * 
     * @param pRoleToken
     *            Role token
     * @param pClazz
     *            Type of fields container to retrieve, for all fields container
     * @param pTypeAccess
     *            Type access flag. If a flag is not
     * @param <TYPE>
     *            Fields container object's type
     * @return Fields container information.
     */
    public <TYPE extends CacheableFieldsContainer> List<TYPE> getFieldsContainer(
            final String pRoleToken,
            final Class<? extends FieldsContainer> pClazz,
            final long pTypeAccess);

    /**
     * Retrieve identifier of all values container of the specified fields
     * container.
     * 
     * @param pRoleToken
     *            Role token
     * @param pFieldsContainerId
     *            Identifier of the fields container
     * @return Identifier of values containers.
     */
    public List<String> getValuesContainerId(String pRoleToken,
            String pFieldsContainerId);

    /**
     * Get values container from its identifier.
     * 
     * @param pRoleToken
     *            Role token
     * @param pValuesContainerId
     *            Values container's identifier
     * @param pCacheProperties
     *            Cache properties
     * @return Values container
     */
    public CacheableValuesContainer getValuesContainer(String pRoleToken,
            String pValuesContainerId, CacheProperties pCacheProperties);

    /**
     * Get the Identifier of the container by its name.
     * 
     * @param pRoleToken
     *            Role token
     * @param pFieldsContainerName
     *            Name of Fields container
     * @return The fields container identifier.
     * @throws InvalidTokenException
     *             If the role token is invalid
     */
    public String getFieldsContainerId(String pRoleToken,
            String pFieldsContainerName) throws InvalidTokenException;

    /**
     * Get the Identifier of the fields container by the identifier of a values
     * container.
     * 
     * @param pRoleToken
     *            The role token
     * @param pValuesContainerId
     *            The values container identifier
     * @return The fields container identifier.
     * @throws InvalidTokenException
     *             If the role token is invalid
     */
    public String getFieldsContainerIdByValuesContainer(String pRoleToken,
            String pValuesContainerId) throws InvalidTokenException;

    /**
     * Initialize mapping for a new fields container, must be called when new
     * fields container is created
     * 
     * @param pId
     *            Id of the new fields container
     */
    public void initDynamicMapping(String pId);

    /**
     * Test values container existence.
     * 
     * @param pValuesContainerId
     *            Identifier of the values container to test.
     * @return True if the values container exists (and identifier is not
     *         blank), false otherwise
     */
    public boolean isValuesContainerExists(final String pValuesContainerId);
}
