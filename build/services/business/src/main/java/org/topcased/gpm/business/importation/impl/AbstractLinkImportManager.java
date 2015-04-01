/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.importation.impl;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.exception.ImportException;
import org.topcased.gpm.business.exception.ImportException.ImportMessage;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.importation.ImportExecutionReport;
import org.topcased.gpm.business.importation.ImportProperties;
import org.topcased.gpm.business.importation.ImportProperties.ImportFlag;
import org.topcased.gpm.business.link.impl.CacheableLink;
import org.topcased.gpm.business.link.impl.CacheableLinkType;
import org.topcased.gpm.business.link.impl.LinkServiceImpl;
import org.topcased.gpm.business.serialization.data.Link;

/**
 * LinkImportManager handles link importation.
 * 
 * @param <SL>
 *            Concrete class of link to import.
 * @author mkargbo
 */
public abstract class AbstractLinkImportManager<SL extends Link>
        extends
        AbstractValuesContainerImportManager<SL, CacheableLink, CacheableLinkType> {

    /** LINKED_ID_NOT_EXISTS */
    private static final String LINKED_ID_NOT_EXISTS =
            "Origin or destination does not exist for this link.";

    protected LinkServiceImpl linkServiceImpl;

    /**
     * {@inheritDoc}
     * <p>
     * Cannot import a link if its origin and/or its destination does no exits.
     * </p>
     * 
     * @throws ImportException
     * @see org.topcased.gpm.business.importation.impl.AbstractImportManager#canImport(java.lang.String,
     *      java.lang.Object,
     *      org.topcased.gpm.business.importation.ImportProperties)
     */
    @Override
    protected boolean canImport(final String pRoleToken, final SL pElement,
            final String pElementId, final ImportProperties pProperties,
            final ImportExecutionReport pReport) throws ImportException {
        final boolean lCanImport =
                super.canImport(pRoleToken, pElement, pElementId, pProperties,
                        pReport);

        final CacheableLinkType lLinkType =
                getBusinessTypeObject(pRoleToken, pElement);
        final boolean lLinkedExists;
        //Looking with identifiers.
        if (StringUtils.isBlank(pElement.getOriginId())
                || StringUtils.isBlank(pElement.getDestinationId())) {
            lLinkedExists =
                    canImportWithoutIdentifiers(pRoleToken, pElement,
                            lLinkType, pProperties, pReport);
        }
        else if (!(fieldsContainerServiceImpl.isValuesContainerExists(pElement.getOriginId()))
                || !(fieldsContainerServiceImpl.isValuesContainerExists(pElement.getDestinationId()))) {
            onFailure(pElement, pProperties, pReport, LINKED_ID_NOT_EXISTS);
            lLinkedExists = false;
        }
        else {
            lLinkedExists = true;
        }
        return lLinkedExists && lCanImport;
    }

    /**
     * Test the link to import using its functional reference.
     * <p>
     * Depends of the concrete link object.
     * </p>
     * 
     * @param pRoleToken
     *            Role token
     * @param pLink
     *            Link to import
     * @param pLinkType
     *            Type of the link to import.
     * @param pProperties
     *            TODO
     * @param pReport
     *            Report to fill if the link cannot be import.
     * @return True if the link can be import, false otherwise.
     * @throws ImportException
     *             On import fails (cannot import).
     */
    protected abstract boolean canImportWithoutIdentifiers(
            final String pRoleToken, final SL pLink,
            final CacheableLinkType pLinkType, ImportProperties pProperties,
            final ImportExecutionReport pReport) throws ImportException;

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractImportManager#createElement(java.lang.String,
     *      java.lang.Object,
     *      org.topcased.gpm.business.extensions.service.Context, String...)
     */
    @Override
    protected String createElement(final String pRoleToken,
            final CacheableLink pBusinessElement, final Context pContext,
            final String... pAdditionalArguments) {
        final CacheableLink lCreatedLink =
                linkServiceImpl.createLink(pRoleToken, pBusinessElement, false,
                        pContext);
        return lCreatedLink.getId();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Create auto pointers
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractImportManager#createErasedElement(java.lang.String,
     *      java.lang.Object,
     *      org.topcased.gpm.business.extensions.service.Context, String...)
     */
    @Override
    protected String createErasedElement(final String pRoleToken,
            final CacheableLink pBusinessElement, final Context pContext,
            final String... pAdditionalArguments) {
        final CacheableLink lCreatedLink =
                linkServiceImpl.createLink(pRoleToken, pBusinessElement, true,
                        pContext);
        return lCreatedLink.getId();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractImportManager#isElementExists(java.lang.String,
     *      java.lang.Object,
     *      org.topcased.gpm.business.importation.ImportProperties,
     *      ImportExecutionReport)
     */
    @Override
    protected String isElementExists(final String pRoleToken, final SL pElement,
            final ImportProperties pProperties, final ImportExecutionReport pReport)
        throws ImportException {
        String lId = StringUtils.EMPTY;
        switch (getImportFlag(pProperties)) {
            case CREATE_ONLY:
                if (linkServiceImpl.isLinkExists(pElement)
                        || isLinkExists(pRoleToken, pElement)) {
                    throw new ImportException(ImportMessage.OBJECT_EXISTS,
                            pElement);
                }
                break;
            case UPDATE_ONLY:
                if (!linkServiceImpl.isLinkExists(pElement)
                        && !isLinkExists(pRoleToken, pElement)) {
                    throw new ImportException(ImportMessage.OBJECT_NOT_EXISTS,
                            pElement);
                }
                if (StringUtils.isBlank(pElement.getId())) {
                    lId =
                            linkServiceImpl.getId(pElement.getType(),
                                    pElement.getOriginId(),
                                    pElement.getDestinationId());
                    if (StringUtils.isBlank(lId)) {
                        lId = getLinkIdentifier(pRoleToken, pElement);
                    }
                }
                break;
            case CREATE_OR_UPDATE:
            case ERASE:
                if (fieldsContainerServiceImpl.isValuesContainerExists(pElement.getId())) {
                    lId = pElement.getId();
                }
                else if (linkServiceImpl.isLinkExists(pElement)) {
                    lId =
                            linkServiceImpl.getId(pElement.getType(),
                                    pElement.getOriginId(),
                                    pElement.getDestinationId());
                }
                else if (isLinkExists(pRoleToken, pElement)) {
                    lId = getLinkIdentifier(pRoleToken, pElement);
                }
                else {
                    lId = StringUtils.EMPTY;
                }
                break;
            default:
        }
        return lId;
    }

    /**
     * Test the link existence using link's functional reference.
     * <p>
     * Depends of the concrete link.
     * </p>
     * 
     * @param pRoleToken
     *            Role token
     * @param pLink
     *            Link to test.
     * @return True if the link exists, false otherwise.
     */
    protected abstract boolean isLinkExists(final String pRoleToken,
            final SL pLink);

    /**
     * Retrieve the link identifier using the link functional reference.
     * <p>
     * Depends of the concrete link.
     * </p>
     * 
     * @param pRoleToken
     *            Role token
     * @param pLink
     *            Link to retrieve.
     * @return Identifier of the link if found, blank otherwise.
     */
    protected abstract String getLinkIdentifier(final String pRoleToken,
            final SL pLink);

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractImportManager#removeElement(java.lang.String,
     *      java.lang.String,
     *      org.topcased.gpm.business.extensions.service.Context, String...)
     */
    @Override
    protected void removeElement(final String pRoleToken, final String pElementId,
            final Context pContext, final String... pAdditionalArguments) {
        linkServiceImpl.deleteLink(pRoleToken, pElementId, pContext);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractImportManager#updateElement(java.lang.String,
     *      java.lang.Object, java.lang.String,
     *      org.topcased.gpm.business.extensions.service.Context, String...)
     */
    @Override
    protected void updateElement(final String pRoleToken,
            final CacheableLink pBusinessElement, final String pElementId,
            final Context pContext, final boolean pIgnoreVersion,
            final String... pAdditionalArguments) {
        pBusinessElement.setId(pElementId);
        linkServiceImpl.updateLink(pRoleToken, pBusinessElement, pContext,
                pIgnoreVersion);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractImportManager#getCanImportErrorMessage()
     */
    @Override
    protected String getCanImportErrorMessage() {
        return ImportException.ImportMessage.LINK_LINKED_NOT_EXISTS.getValue();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractValuesContainerImportManager#getBusinessTypeObject(java.lang.String,
     *      org.topcased.gpm.business.serialization.data.ValuesContainerData)
     */
    @Override
    protected CacheableLinkType getBusinessTypeObject(final String pRoleToken,
            final SL pElement) {
        final CacheableLinkType lLinkType =
                linkServiceImpl.getCacheableLinkTypeByName(pRoleToken,
                        pElement.getType());
        return lLinkType;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Fill the 'originId' and 'destinationId' attributes.
     * </p>
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractValuesContainerImportManager#getBusinessObject(java.lang.String,
     *      org.topcased.gpm.business.serialization.data.ValuesContainerData,
     *      org.topcased.gpm.business.importation.ImportProperties)
     */
    @Override
    protected CacheableLink getBusinessObject(final String pRoleToken, final SL pElement,
            final ImportProperties pProperties) {
        //Origin and destination identifiers
        if (StringUtils.isBlank(pElement.getOriginId())) {
            final String lOriginId = getLinkedIdentifier(pRoleToken, pElement, true);
            pElement.setOriginId(lOriginId);
        }
        if (StringUtils.isBlank(pElement.getDestinationId())) {
            final String lDestinationId =
                    getLinkedIdentifier(pRoleToken, pElement, false);
            pElement.setDestinationId(lDestinationId);
        }
        return super.getBusinessObject(pRoleToken, pElement, pProperties);
    }

    /**
     * Retrieve the linked element's identifier.
     * 
     * @param pRoleToken
     *            Role token
     * @param pElement
     *            Link to consider.
     * @param pOrigin
     *            True if the linked origin element must threat, false to threat
     *            the destination.
     * @return Origin or destination linked element's identifier.
     */
    protected abstract String getLinkedIdentifier(final String pRoleToken,
            final SL pElement, boolean pOrigin);

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractValuesContainerImportManager#doGetBusinessObject(java.lang.String,
     *      org.topcased.gpm.business.serialization.data.ValuesContainerData,
     *      org.topcased.gpm.business.fields.impl.CacheableFieldsContainer,
     *      org.topcased.gpm.business.importation.ImportProperties)
     */
    @Override
    protected CacheableLink doGetBusinessObject(final String pRoleToken, final SL pElement,
            final CacheableLinkType pBusinessType, final ImportProperties pProperties) {
        CacheableLink lCacheableLink = new CacheableLink(pElement, pBusinessType);
        if (pElement.getExtensionPointsToExclude() != null) {
            lCacheableLink.setExtentionPointsToExclude(
                    pElement.getExtensionPointsToExclude().retrieveCommandsToExclude());
        }
        return lCacheableLink;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.impl.AbstractImportManager#getImportFlag(org.topcased.gpm.business.importation.ImportProperties)
     */
    protected abstract ImportFlag getImportFlag(ImportProperties pProperties);

    public void setLinkServiceImpl(final LinkServiceImpl pLinkServiceImpl) {
        linkServiceImpl = pLinkServiceImpl;
    }
}