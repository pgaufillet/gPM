/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.server.link.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.topcased.gpm.business.authorization.service.AccessControlContextData;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.extensions.service.ExtensionPointParameters;
import org.topcased.gpm.business.fields.impl.CacheableValuesContainer;
import org.topcased.gpm.business.fieldscontainer.service.FieldsContainerService;
import org.topcased.gpm.business.link.impl.CacheableLink;
import org.topcased.gpm.business.link.impl.CacheableLinkType;
import org.topcased.gpm.business.product.impl.CacheableProduct;
import org.topcased.gpm.business.serialization.data.CategoryValue;
import org.topcased.gpm.business.serialization.data.LinkType;
import org.topcased.gpm.business.sheet.impl.CacheableSheet;
import org.topcased.gpm.business.util.DisplayMode;
import org.topcased.gpm.business.util.Translation;
import org.topcased.gpm.business.util.log.GPMActionLogConstants;
import org.topcased.gpm.business.values.ValuesAccessProperties;
import org.topcased.gpm.business.values.link.BusinessLink;
import org.topcased.gpm.business.values.link.impl.cacheable.CacheableLinkAccess;
import org.topcased.gpm.ui.facade.server.AbstractFacade;
import org.topcased.gpm.ui.facade.server.FacadeLocator;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.i18n.I18nFacade;
import org.topcased.gpm.ui.facade.server.i18n.I18nTranslationManager;
import org.topcased.gpm.ui.facade.server.link.LinkFacade;
import org.topcased.gpm.ui.facade.shared.container.field.UiField;
import org.topcased.gpm.ui.facade.shared.container.link.UiLink;
import org.topcased.gpm.ui.facade.shared.filter.UiFilterContainerType;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * LinkFacade
 * 
 * @author nveillet
 */
public class LinkFacadeImpl extends AbstractFacade implements LinkFacade {

    /**
     * Creates a link in database
     * 
     * @param pSession
     *            Current user session
     * @param pLinkTypeName
     *            Link type name
     * @param pOriginContainerId
     *            Origin container Id
     * @param pDestinationContainerId
     *            Destination container Id
     * @return created link
     */
    public UiLink createLink(UiSession pSession, String pLinkTypeName,
            String pOriginContainerId, String pDestinationContainerId) {

        String lRoleToken = pSession.getRoleToken();

        CacheableLinkType lCacheableLinkType =
                getLinkService().getLinkTypeByName(lRoleToken, pLinkTypeName,
                        CacheProperties.IMMUTABLE);

        CacheableValuesContainer lOriginContainer = 
                getFieldsContainerService().getValuesContainer(lRoleToken,
                        pOriginContainerId, CacheProperties.IMMUTABLE);
        
        CacheableValuesContainer lDestinationContainer = 
                getFieldsContainerService().getValuesContainer(lRoleToken,
                        pDestinationContainerId, CacheProperties.IMMUTABLE);
        
        CacheableLink lCacheableLink =
                new CacheableLink(lCacheableLinkType, pOriginContainerId,
                        lOriginContainer.getFunctionalReference(), pDestinationContainerId,
                        lDestinationContainer.getFunctionalReference());

        lCacheableLink =
                getLinkService().createLink(lRoleToken, lCacheableLink, true,
                        getContext(pSession));
        
        gpmLogger.mediumInfo(pSession.getParent().getLogin(), GPMActionLogConstants.SHEET_LINK, 
        		lOriginContainer.getProductName(), lDestinationContainer.getProductName(),
                lOriginContainer.getFunctionalReference(), lDestinationContainer.getFunctionalReference());

        Map<String, List<CategoryValue>> lCategoryCache =
                new HashMap<String, List<CategoryValue>>();

        return getUiLink(pSession, lCacheableLink, DisplayMode.EDITION, null,
                lCategoryCache);
    }

    /**
     * Delete link in database
     * 
     * @param pSession
     *            Current user session
     * @param pLinkId
     *            Id of the link to delete
     */
    public void deleteLink(UiSession pSession, String pLinkId) {
        getLinkService().deleteLink(pSession.getRoleToken(), pLinkId,
                getContext(pSession));
    }

    private CacheableLink fillCacheableLink(UiSession pSession, String pLinkId,
            List<UiField> pFieldsList) {

        String lRoleToken = pSession.getRoleToken();

        CacheableLink lCacheableLink =
                getLinkService().getLink(lRoleToken, pLinkId,
                        CacheProperties.MUTABLE);

        CacheProperties lProperties =
                new CacheProperties(false, new AccessControlContextData(
                        pSession.getProductName(), pSession.getRoleName(),
                        CacheProperties.ACCESS_CONTROL_NOT_USED,
                        lCacheableLink.getTypeId(),
                        CacheProperties.DEFAULT_ACCESS_CONTROL_USED,
                        lCacheableLink.getId()));

        CacheableLinkType lCacheableSheetType =
                getLinkService().getLinkTypeByName(lRoleToken,
                        lCacheableLink.getTypeName(), lProperties);

        BusinessLink lBusinessLink =
                new CacheableLinkAccess(lRoleToken, lCacheableSheetType,
                        lCacheableLink,
                        ValuesAccessProperties.NOT_CHECKED_READ_OR_WRITE);

        //Container attributes and fields
        initFieldsContainer(lBusinessLink, pFieldsList);

        return lCacheableLink;
    }

    /**
     * get execution context
     * 
     * @param pSession
     *            the session
     * @param pLink
     *            the link
     * @return the execution context
     */
    private Context getContext(UiSession pSession, BusinessLink pLink) {
        Context lContext = getContext(pSession);

        lContext.put(ExtensionPointParameters.FIELDS_CONTAINER_ID,
                pLink.getTypeId());
        lContext.put(ExtensionPointParameters.FIELDS_CONTAINER_NAME,
                pLink.getTypeName());

        return lContext;
    }

    /**
     * Get possible link type names to a given container type
     * 
     * @param pSession
     *            the session
     * @param pFieldsContainerId
     *            the fields container identifier
     * @return the possible link type names
     */
    public List<String> getCreatableLinkTypes(UiSession pSession,
            String pFieldsContainerId) {
        List<String> lLinksTypeIds =
                getSheetService().getPossibleLinkTypes(pSession.getRoleToken(),
                        pSession.getParent().getProcessName(),
                        pSession.getProductName(), pFieldsContainerId);

        List<String> lLinksTypeNames =
                new ArrayList<String>(lLinksTypeIds.size());
        for (String lLinksTypeId : lLinksTypeIds) {
            lLinksTypeNames.add(getLinkService().getLinkType(
                    pSession.getRoleToken(), lLinksTypeId,
                    CacheProperties.IMMUTABLE).getName());
        }

        Collections.sort(lLinksTypeNames);

        return lLinksTypeNames;
    }

    /**
     * Get deletable link type names to a given container type
     * 
     * @param pSession
     *            the session
     * @param pFieldsContainerId
     *            the fields container identifier
     * @return the deletable link type names
     */
    public List<String> getDeletableLinkTypes(UiSession pSession,
            String pFieldsContainerId) {

        List<CacheableLinkType> lLinkTypes =
                getFieldsContainerService().getFieldsContainer(
                        pSession.getRoleToken(),
                        LinkType.class,
                        FieldsContainerService.NOT_CONFIDENTIAL
                                | FieldsContainerService.DELETE);

        List<String> lLinksTypeNames = new ArrayList<String>();
        for (CacheableLinkType lLinkType : lLinkTypes) {
            if (lLinkType.getOriginTypeId().equals(pFieldsContainerId)
                    || lLinkType.getDestinationTypeId().equals(
                            pFieldsContainerId)) {
                lLinksTypeNames.add(lLinkType.getName());
            }
        }

        Collections.sort(lLinksTypeNames);

        return lLinksTypeNames;
    }

    /**
     * Get the destination container type of a link type
     * 
     * @param pSession
     *            the session
     * @param pLinkTypeName
     *            the link type name
     * @param pValuesContainerId
     *            the current values container identifier
     * @return the container type
     */
    public UiFilterContainerType getDestinationContainerType(
            UiSession pSession, String pLinkTypeName, String pValuesContainerId) {

        CacheableValuesContainer lValuesContainer =
                getFieldsContainerService().getValuesContainer(
                        pSession.getRoleToken(), pValuesContainerId,
                        CacheProperties.IMMUTABLE);

        CacheableLinkType lLinkTypeName =
                getLinkService().getLinkTypeByName(pSession.getRoleToken(),
                        pLinkTypeName, CacheProperties.IMMUTABLE);

        UiFilterContainerType lContainerType = new UiFilterContainerType();
        I18nFacade lI18nFacade = FacadeLocator.instance().getI18nFacade();
        if (!lLinkTypeName.getDestinationTypeName().equals(
                lValuesContainer.getTypeName())) {
            lContainerType.setId(lLinkTypeName.getDestinationTypeId());
            lContainerType.setName(lI18nFacade.getTranslation(
                    pSession.getParent(),
                    lLinkTypeName.getDestinationTypeName()));
        }
        else {
            lContainerType.setId(lLinkTypeName.getOriginTypeId());
            lContainerType.setName(lI18nFacade.getTranslation(
                    pSession.getParent(), lLinkTypeName.getOriginTypeName()));
        }

        return lContainerType;
    }

    /**
     * Get list of link groups. If container is a sheet, links are sorted by
     * filter. If container is a product, links are NOT sorted
     * 
     * @param pSession
     *            Current user session
     * @param pOriginContainerId
     *            Origin container Id
     * @return list of link groups
     */
    public List<Translation> getLinkGroups(UiSession pSession,
            String pOriginContainerId) {

        String lRoleToken = pSession.getRoleToken();

        List<Translation> lLinkGroups = new ArrayList<Translation>();

        CacheProperties lProperties =
                new CacheProperties(
                        false,
                        getFieldsContainerService().getFieldsContainerIdByValuesContainer(
                                pSession.getRoleToken(), pOriginContainerId));

        List<CacheableLinkType> lCacheableLinkTypes =
                getLinkService().getExistingLinkTypes(lRoleToken,
                        pOriginContainerId, lProperties);

        // Get translation manager
        I18nTranslationManager lTranslationManager =
                FacadeLocator.instance().getI18nFacade().getTranslationManager(
                        pSession.getParent().getLanguage());

        // Fill the list with the link type name
        for (CacheableLinkType lCacheableLinkType : lCacheableLinkTypes) {
            if (lCacheableLinkType != null) {
                lLinkGroups.add(new Translation(
                        lCacheableLinkType.getName(),
                        lTranslationManager.getTextTranslation(lCacheableLinkType.getName())));
            }
        }
        if (lLinkGroups.size() == 0) {
            lLinkGroups = null;
        }

        return lLinkGroups;
    }

    /**
     * Get list of links. If container is a sheet, links are sorted by filter.
     * If container is a product, links are NOT sorted
     * 
     * @param pSession
     *            Current user session
     * @param pOriginContainerId
     *            Origin container Id
     * @param pLinkTypeName
     *            link type name
     * @param pDisplayMode
     *            display mode
     * @return list of links of given type with given origin container
     */
    public List<UiLink> getLinks(UiSession pSession, String pOriginContainerId,
            String pLinkTypeName, DisplayMode pDisplayMode) {
        String lRoleToken = pSession.getRoleToken();
        CacheableValuesContainer lCacheableValuesContainer =
                getFieldsContainerService().getValuesContainer(lRoleToken,
                        pOriginContainerId, CacheProperties.IMMUTABLE);
        List<UiLink> lLinkList = new ArrayList<UiLink>();

        Map<String, List<CategoryValue>> lCategoryCache =
                new HashMap<String, List<CategoryValue>>();

        // if product, no sort
        if (lCacheableValuesContainer instanceof CacheableProduct) {
            List<CacheableLink> lCacheableLinkList =
                    getLinkService().getLinksByType(lRoleToken, pLinkTypeName,
                            CacheProperties.MUTABLE);

            for (CacheableLink lCacheableLink : lCacheableLinkList) {
                UiLink lLink =
                        getUiLink(pSession, lCacheableLink, pDisplayMode,
                                lCacheableValuesContainer.getTypeId(),
                                lCategoryCache);

                if (lLink != null) {
                    lLinkList.add(lLink);
                }
            }
        }
        // if sheet : sort by filter
        else if (lCacheableValuesContainer instanceof CacheableSheet) {
            CacheableSheet lCacheableSheet =
                    (CacheableSheet) lCacheableValuesContainer;
            String lLinkTypeId =
                    getLinkService().getLinkTypeByName(lRoleToken,
                            pLinkTypeName, CacheProperties.IMMUTABLE).getId();
            List<String> lLinksId =
                    getSheetService().getCacheableSheetLinksByType(
                            pSession.getParent().getProcessName(),
                            lCacheableSheet.getId(), lLinkTypeId).getLinksId();

            for (String lLinkId : lLinksId) {
                CacheableLink lCacheableLink =
                        getLinkService().getLink(lRoleToken, lLinkId,
                                CacheProperties.MUTABLE);
                UiLink lLink =
                        getUiLink(pSession, lCacheableLink, pDisplayMode,
                                lCacheableValuesContainer.getTypeId(),
                                lCategoryCache);

                if (lLink != null) {
                    lLinkList.add(lLink);
                }
            }
        }
        return lLinkList;
    }

    private UiLink getUiLink(final UiSession pSession,
            final CacheableLink pCacheableLink, final DisplayMode pDisplayMode,
            final String pVisibleTypeId,
            final Map<String, List<CategoryValue>> pCategoryCache) {
        String lRoleToken = pSession.getRoleToken();
        CacheProperties lProperties =
                new CacheProperties(false, new AccessControlContextData(
                        pSession.getProductName(), pSession.getRoleName(),
                        CacheProperties.DEFAULT_ACCESS_CONTROL_USED,
                        pCacheableLink.getTypeId(), pVisibleTypeId,
                        pCacheableLink.getId()));
        UiLink lLink = new UiLink();
        //Container attributes and fields
        CacheableLinkType lCacheableLinkType =
                getLinkService().getLinkType(lRoleToken,
                        pCacheableLink.getTypeId(), lProperties);
        BusinessLink lBusinessLink =
                new CacheableLinkAccess(lRoleToken, lCacheableLinkType,
                        pCacheableLink,
                        ValuesAccessProperties.NOT_CHECKED_READ_OR_WRITE);

        if (lBusinessLink.isConfidential()) {
            return null;
        }

        I18nTranslationManager lTranslationManager =
                FacadeLocator.instance().getI18nFacade().getTranslationManager(
                        pSession.getParent().getLanguage());

        Context lContext = getContext(pSession, lBusinessLink);

        initUiContainer(lLink, lBusinessLink, pSession, pDisplayMode,
                lTranslationManager, pCategoryCache, lContext);

        //Link attributes
        lLink.setDestinationId(lBusinessLink.getDestinationId());
        lLink.setDestinationReference(pCacheableLink.getDestinationRef());
        lLink.setOriginId(lBusinessLink.getOriginId());
        lLink.setOriginReference(pCacheableLink.getOriginRef());

        return lLink;
    }

    /**
     * Update link in database
     * 
     * @param pSession
     *            Current user session
     * @param pLinkId
     *            Id of link to update
     * @param pFieldsList
     *            List of modified fields
     */
    public void updateLink(UiSession pSession, String pLinkId,
            List<UiField> pFieldsList) {
        CacheableLink lCacheableLink =
                fillCacheableLink(pSession, pLinkId, pFieldsList);
        getLinkService().updateLink(pSession.getRoleToken(), lCacheableLink,
                getContext(pSession), false);
    }

    /**
     * Retrieves the Cacheable Link
     * @param pSession
     *            Current User session
     * @param pLinkId
     *            The Link id
     * @param pUiFields
     *            Updated Fields
     * @param pCurrentDisplayMode
     *            the current sheet display mode
     * @return CacheableLink The updated cacheable link
     */
    @Override
    public CacheableLink getUpdatedCacheableLink(final UiSession pSession,
            final String pLinkId, final List<UiField> pUiFields,
            final DisplayMode pCurrentDisplayMode) {
        CacheableLink lCacheableLink;
        switch (pCurrentDisplayMode) {
            case CREATION:
                lCacheableLink = new CacheableLink();
                lCacheableLink.setId(null);
                break;
            default:
                lCacheableLink =
                        fillCacheableLink(pSession, pLinkId, pUiFields);
                break;
        }
        return lCacheableLink;
    }
}
