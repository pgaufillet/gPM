/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/

package org.topcased.gpm.business.link.service;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.exception.ConstraintException;
import org.topcased.gpm.business.exception.FieldValidationException;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.exception.InvalidIdentifierException;
import org.topcased.gpm.business.exception.MandatoryValuesException;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.link.impl.CacheableLink;
import org.topcased.gpm.business.link.impl.CacheableLinkType;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * Public interface of the sheet service
 * 
 * @author llatil
 */
@Transactional(propagation = Propagation.REQUIRED)
public interface LinkService {

    /**
     * Get a link from its ID
     * 
     * @param pRoleToken
     *            Role session token
     * @param pLinkId
     *            Identifier of the sheet link.
     * @return The corresponding sheet link
     * @since 1.8.3
     * @deprecated
     * @see LinkService#getLink(String, String, CacheProperties)
     */
    public LinkData getLinkFromKey(String pRoleToken, String pLinkId);

    /**
     * Gets the links by type.
     * 
     * @param pRoleToken
     *            the role token
     * @param pLinkTypeName
     *            the link type name
     * @param pCacheProperties
     *            the cache properties
     * @return the links by type
     */
    @Transactional(readOnly = true)
    public List<CacheableLink> getLinksByType(final String pRoleToken,
            final String pLinkTypeName, CacheProperties pCacheProperties);

    /**
     * Get info on a sheet link type from a sheet link type identifier.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pProcessName
     *            Business process name
     * @param pLinkTypeName
     *            Name of sheet link type.
     * @return Information on the sheet link type.
     * @throws AuthorizationException
     *             Illegal access to a sheetType
     * @deprecated
     * @since 1.8.3
     * @see LinkService#getLinkTypeByName(String, String, CacheProperties)
     */
    @Transactional(readOnly = true)
    public LinkTypeData getLinkTypeByName(String pRoleToken,
            String pProcessName, String pLinkTypeName)
        throws AuthorizationException;

    /**
     * Get the link types usable on a given container type.
     * 
     * @param pRoleToken
     *            Role token
     * @param pTypeId
     *            The id of fields container that can be linked.
     * @return List of link types data. These link types are usable on the given
     *         fields container.
     * @throws AuthorizationException
     *             If the type is confidential
     */
    @Transactional(readOnly = true)
    public List<CacheableLinkType> getLinkTypes(String pRoleToken,
            String pTypeId) throws AuthorizationException;

    /**
     * Get types of links existing in a values container.
     * 
     * @param pRoleToken
     *            Role token
     * @param pValuesContainerId
     *            The identifier of the values container.
     * @param pProperties
     *            The properties of the cacheable object
     * @return List of link types.
     * @throws AuthorizationException an authorization exception
     */
    @Transactional(readOnly = true)
    public List<CacheableLinkType> getExistingLinkTypes(String pRoleToken,
            String pValuesContainerId, CacheProperties pProperties)
        throws AuthorizationException;

    /**
     * Get the links of a container
     * 
     * @param pRoleToken
     *            Role token
     * @param pContainerId
     *            Identifier of the container
     * @param pCacheProperties
     *            The properties of the cacheable object
     * @return List of links of the given container
     * @throws AuthorizationException
     *             The container is confidential
     */
    public List<CacheableLink> getLinks(final String pRoleToken,
            final String pContainerId, final CacheProperties pCacheProperties)
        throws AuthorizationException;

    /**
     * Create a new link between two sheets.
     * 
     * @param pRoleToken
     *            The role token.
     * @param pLinkTypeName
     *            Name of the link type.
     * @param pSourceId
     *            The source element id.
     * @param pDestId
     *            The destination element id.
     * @param pContext
     *            Execution context.
     * @return {@link LinkData}
     * @throws AuthorizationException
     *             Illegal access to a sheet
     * @deprecated
     * @since 1.8.1
     * @see LinkService#createLink(String, CacheableLink, Context)
     */
    public LinkData createSheetLink(final String pRoleToken,
            final String pLinkTypeName, final String pSourceId,
            final String pDestId, final Context pContext);

    /**
     * Create a new link between two sheets
     * 
     * @param pRoleToken
     *            The role token
     * @param pLinkTypeName
     *            Name of the link type.
     * @param pSourceId
     *            The source element id
     * @param pDestId
     *            The destination element id
     * @param pContext
     *            Execution context.
     * @return The associated cacheable link
     * @throws AuthorizationException
     *             Illegal access to a sheet
     * @throws FieldValidationException
     *             A field is not valid.
     * @since 1.8.1
     * @see LinkService#createLink(String, CacheableLink, Context)
     */
    public CacheableLink createCacheableSheetLink(final String pRoleToken,
            final String pLinkTypeName, final String pSourceId,
            final String pDestId, final Context pContext)
        throws AuthorizationException, FieldValidationException;

    /**
     * Create a new link.
     * 
     * @param pRoleToken
     *            Role token
     * @param pLink
     *            Link to create.
     * @param pCreateAutoPointers
     *            Determine if the pointer fields must be created automatically
     * @param pContext
     *            Execution context.
     * @return The created link
     * @throws AuthorizationException
     *             Illegal access to the link type
     * @throws MandatoryValuesException
     *             If a mandatory field is not filled.
     * @throws FieldValidationException
     *             A field is not valid.
     * @throws GDMException
     *             If the link type has not been found.
     * @throws ConstraintException
     *             If a link between origin and destination all ready exits.
     * @throws InvalidIdentifierException
     *             If the origin and destination are wrong
     */
    public CacheableLink createLink(final String pRoleToken,
            CacheableLink pLink, boolean pCreateAutoPointers,
            final Context pContext) throws AuthorizationException,
        MandatoryValuesException, FieldValidationException, GDMException,
        ConstraintException, InvalidIdentifierException;

    /**
     * Update an existing link content.
     * <p>
     * Update the attributes
     * 
     * @param pRoleToken
     *            Role session token
     * @param pCacheableLink
     *            The cacheable link
     * @param pContext
     *            Execution context.
     * @param pIgnoreVersion
     *            true to ignore the version of the link to update it
     * @throws AuthorizationException
     *             Illegal access to a sheet
     * @throws FieldValidationException
     *             A field is not valid.
     */
    public void updateLink(final String pRoleToken,
            final CacheableLink pCacheableLink, final Context pContext,
            final boolean pIgnoreVersion) throws AuthorizationException,
        FieldValidationException;

    /**
     * Update an existing link content.
     * <p>
     * Update the attributes
     * 
     * @param pRoleToken
     *            Role session token
     * @param pCacheableLink
     *            The cacheable link
     * @param pContext
     *            Execution context.
     * @throws AuthorizationException
     *             Illegal access to a sheet
     * @throws FieldValidationException
     *             A field is not valid.
     * @deprecated since version 3.1.0.2. Use
     *             {@link #updateLink(String, CacheableLink, Context, boolean)}
     *             instead.
     */
    public void updateLink(final String pRoleToken,
            final CacheableLink pCacheableLink, final Context pContext)
        throws AuthorizationException, FieldValidationException;

    /**
     * Update an existing link content.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pLink
     *            The serializable link
     * @param pContext
     *            Execution context.
     * @throws AuthorizationException
     *             Illegal access to a sheet
     */
    public void updateLink(final String pRoleToken,
            final org.topcased.gpm.business.serialization.data.Link pLink,
            final Context pContext);

    /**
     * Creates a new link type.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pBusinessProcName
     *            Name of the business process
     * @param pData
     *            Contains the data of the sheet link type to create.
     * @return Identifier of the newly created link type.
     * @deprecated
     * @since 1.8.3
     * @see LinkService#createLinkType(String, CacheableLinkType)
     */
    public String createLinkType(String pRoleToken, String pBusinessProcName,
            LinkTypeData pData);

    /**
     * Creates a new link type.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pLinkType
     *            Contains the data of the link type to create.
     * @return Identifier of the newly created link type.
     */
    public String createLinkType(String pRoleToken, CacheableLinkType pLinkType);

    /**
     * Delete a sheet link type in the database.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pBusinessProcName
     *            Name of the business process
     * @param pSheetLinkTypeName
     *            Name of the sheet link type to remove.
     * @param pDeleteSheetsLinks
     *            Force the deletion of all sheets links of this type
     * @throws AuthorizationException
     *             Illegal access to a sheetType
     */
    public void deleteLinkType(String pRoleToken, String pBusinessProcName,
            String pSheetLinkTypeName, boolean pDeleteSheetsLinks)
        throws AuthorizationException;

    /**
     * Delete a link between two elements.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pLinkId
     *            Identifier of the link to remove.
     * @param pContext
     *            Execution context.
     * @throws AuthorizationException
     *             Illegal access to a container
     */
    public void deleteLink(final String pRoleToken, final String pLinkId,
            final Context pContext) throws AuthorizationException;

    /**
     * Gets the serializable link.
     * 
     * @param pRoleToken
     *            The role token.
     * @param pLinkId
     *            the link id
     * @return the serializable link
     */
    public org.topcased.gpm.business.serialization.data.Link getSerializableLinkByKey(
            String pRoleToken, String pLinkId);

    /**
     * Get the serializable links of an element
     * 
     * @param pRoleToken
     *            Role session token
     * @param pContainerId
     *            Identifier of the element.
     * @return List of link data
     * @throws AuthorizationException
     *             Illegal access to the container
     */
    public List<org.topcased.gpm.business.serialization.data.Link> getSerializableLinks(
            final String pRoleToken, final String pContainerId)
        throws AuthorizationException;

    /**
     * Gets the serializable link type.
     * 
     * @param pRoleToken
     *            The role token.
     * @param pLinkTypeId
     *            the link type id
     * @return the serializable link type
     */
    public org.topcased.gpm.business.serialization.data.LinkType getSerializableLinkType(
            String pRoleToken, String pLinkTypeId);

    /**
     * Gets the cacheable link.
     * 
     * @param pRoleToken
     *            the role token
     * @param pLinkId
     *            the link data
     * @param pProperties
     *            the cache properties
     * @return the cacheable link
     */
    public CacheableLink getLink(String pRoleToken, String pLinkId,
            CacheProperties pProperties);

    /**
     * Get all the sheet link of a type associated to a sheet
     * 
     * @param pProcessName
     *            The name of the process
     * @param pSheetId
     *            The sheet type
     * @param pLinkTypeId
     *            The link type id
     * @param pUseFilter
     *            If the filter associated to the link type is used
     * @return A list of link id
     */
    public List<String> getSheetLinksByType(String pProcessName,
            String pSheetId, String pLinkTypeId, boolean pUseFilter);

    /**
     * Getter on the filter used to display the links associated to a sheet
     * 
     * @param pLinkTypeId
     *            The type of link used for display
     * @param pSheetTypeId
     *            the type of sheet
     * @return The name of the filter (null if no filter defined)
     */
    public String getFilterForLinksDisplaying(String pLinkTypeId,
            String pSheetTypeId);

    /**
     * Get the IDs of elements linked to current element with specific link
     * type.
     * 
     * @param pValuesContainerId
     *            Identifier of the current values container.
     * @param pLinkTypeName
     *            Name of the link type.
     * @return List of linked container IDs
     */
    public List<String> getLinkedElementsIds(final String pValuesContainerId,
            final String pLinkTypeName);

    /**
     * Gets the cacheable link type with its id
     * 
     * @param pRoleToken
     *            Role session token
     * @param pLinkTypeId
     *            The link type id
     * @param pProperties
     *            The properties of the cacheable object
     * @return The cacheable link type
     */
    public CacheableLinkType getLinkType(String pRoleToken, String pLinkTypeId,
            CacheProperties pProperties);

    /**
     * Gets the cacheable link type with its name
     * 
     * @param pRoleToken
     *            Role session token
     * @param pProcessName
     *            The name of the process
     * @param pLinkTypeName
     *            The name link type
     * @param pProperties
     *            The properties of the cacheable object
     * @return The cacheable link type
     * @since 1.8.3
     * @see LinkService#getLinkTypeByName(String, String, CacheProperties)
     */
    public CacheableLinkType getCacheableLinkTypeByName(String pRoleToken,
            String pProcessName, String pLinkTypeName,
            CacheProperties pProperties);

    /**
     * Gets the cacheable link type with its name
     * 
     * @param pRoleToken
     *            Role session token
     * @param pLinkTypeName
     *            The name link type
     * @param pProperties
     *            The properties of the cacheable object
     * @return The cacheable link type
     */
    public CacheableLinkType getLinkTypeByName(String pRoleToken,
            String pLinkTypeName, CacheProperties pProperties);
}
