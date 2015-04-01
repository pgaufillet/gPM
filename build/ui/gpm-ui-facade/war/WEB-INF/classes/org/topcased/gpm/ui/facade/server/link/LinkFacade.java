/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.server.link;

import java.util.List;

import org.topcased.gpm.business.link.impl.CacheableLink;
import org.topcased.gpm.business.util.DisplayMode;
import org.topcased.gpm.business.util.Translation;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.shared.container.field.UiField;
import org.topcased.gpm.ui.facade.shared.container.link.UiLink;
import org.topcased.gpm.ui.facade.shared.filter.UiFilterContainerType;

/**
 * LinkFacade
 * 
 * @author nveillet
 */
public interface LinkFacade {

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
            String pOriginContainerId, String pDestinationContainerId);

    /**
     * Delete link in database
     * 
     * @param pSession
     *            Current user session
     * @param pLinkId
     *            Id of the link to delete
     */
    public void deleteLink(UiSession pSession, String pLinkId);

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
            String pFieldsContainerId);

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
            String pFieldsContainerId);

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
            UiSession pSession, String pLinkTypeName, String pValuesContainerId);

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
            String pOriginContainerId);

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
            String pLinkTypeName, DisplayMode pDisplayMode);

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
            List<UiField> pFieldsList);
    
    /**
     * Retrieves the updated link fields
     * @param pSession
     *            Current User session
     * @param pLinkId
     *            The Link id
     * @param pUiFields
     *            Updated Fields
     * @param pCurrentSheetDisplayMode
     *        current sheet display mode
     * @return CacheableLink The updated cacheable link
     */
    public CacheableLink getUpdatedCacheableLink(final UiSession pSession,
            final String pLinkId, final List<UiField> pUiFields,
            final DisplayMode pCurrentSheetDisplayMode);
}
