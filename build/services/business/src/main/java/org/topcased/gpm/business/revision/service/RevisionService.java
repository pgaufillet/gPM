/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Magali Franchet (Neo-Soft on behalf of Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.revision.service;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.exception.FieldValidationException;
import org.topcased.gpm.business.exception.InvalidIdentifierException;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.revision.RevisionData;
import org.topcased.gpm.business.revision.RevisionDifferencesData;
import org.topcased.gpm.business.revision.RevisionSummaryData;
import org.topcased.gpm.business.revision.impl.CacheableRevision;
import org.topcased.gpm.business.sheet.impl.CacheableSheet;

/**
 * Revision Service
 * 
 * @author mfranche
 */
@Transactional(propagation = Propagation.REQUIRED)
public interface RevisionService {

    /** Name of the extended attribute */
    public static String REVISION_ENABLED_ATTRIBUTE_NAME = "revisionSupport";

    public static String SHEET_STATE_ATTRIBUTE_NAME = "sheetState";

    /**
     * Create a new revision on container pContainerId with a revision pLabel.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pContainerId
     *            Container identifier
     * @param pLabel
     *            The revision label
     * @return The revision id
     * @throws AuthorizationException
     *             Illegal access to the revision
     */
    public String createRevision(String pRoleToken, String pContainerId,
            String pLabel) throws AuthorizationException;

    /**
     * Create a revision on pContainerId with a revisionData structure
     * 
     * @param pRoleToken
     *            Role session token
     * @param pContainerId
     *            Container identifier
     * @param pRevisionData
     *            The filled revision data structure
     * @return The revision id
     * @throws AuthorizationException
     *             Illegal access to the revision
     */
    public String createRevision(String pRoleToken, String pContainerId,
            final RevisionData pRevisionData) throws AuthorizationException;

    /**
     * Create a new revision
     * 
     * @param pRoleToken
     *            The role token
     * @param pRevisedContainerId
     *            The id of the revised container
     * @param pCacheableRevision
     *            The revision
     * @param pCtx
     *            The context
     * @return The id of then new revision
     * @throws FieldValidationException
     *             A field is not valid.
     */
    public String createRevision(final String pRoleToken,
            final String pRevisedContainerId,
            final CacheableRevision pCacheableRevision, final Context pCtx)
        throws FieldValidationException;

    /**
     * Get a revision content
     * 
     * @param pRoleToken
     *            Role session token
     * @param pContainerId
     *            Container identifier
     * @param pRevisionId
     *            Revision identifier
     * @throws AuthorizationException
     *             Illegal access to the revision
     * @return The revision data structure
     */
    public RevisionData getRevisionData(String pRoleToken, String pContainerId,
            String pRevisionId) throws AuthorizationException;

    /**
     * Get a revision content as a serializable RevisionData
     * 
     * @param pRoleToken
     *            Role session token
     * @param pContainerId
     *            Container identifier
     * @param pRevisionId
     *            Revision identifier
     * @throws AuthorizationException
     *             Illegal access to the revision
     * @return The serializable revision data structure
     */
    public org.topcased.gpm.business.serialization.data.RevisionData getRevision(
            String pRoleToken, String pContainerId, String pRevisionId)
        throws AuthorizationException;

    /**
     * Get properties of a revision having label pLabel on container
     * pContainerId
     * 
     * @param pRoleToken
     *            Role session token
     * @param pContainerId
     *            Container identifier
     * @param pLabel
     *            Revision label
     * @return The revision data structure
     * @throws AuthorizationException
     *             Illegal access to the revision
     * @throws InvalidIdentifierException
     *             If the revision's label doesn't exist.
     */
    public RevisionData getRevisionDataByLabel(String pRoleToken,
            String pContainerId, String pLabel) throws AuthorizationException;

    /**
     * Get cacheable revision for a sheet
     * 
     * @param pRoleToken
     *            The role token
     * @param pSheetId
     *            The id of the revised sheet
     * @param pRevisionId
     *            The id of teh revision
     * @return The cacheable revision
     */
    public CacheableRevision getCacheableRevision(String pRoleToken,
            String pSheetId, String pRevisionId);

    /**
     * Get a sheet in a specific revision identified by a revision Id and a
     * container Id
     * 
     * @param pRoleToken
     *            The role token
     * @param pContainerId
     *            Container identifier
     * @param pRevisionId
     *            Revision identifier
     * @return The cacheable sheet structure
     * @throws AuthorizationException
     *             Illegal access to the revision
     */
    public CacheableSheet getCacheableSheetInRevision(String pRoleToken,
            String pContainerId, String pRevisionId)
        throws AuthorizationException;

    /**
     * Get a sheet in a specific revision identified by a revision Id and a
     * container Id
     * 
     * @param pRoleToken
     *            The role token
     * @param pContainerId
     *            Container identifier
     * @param pRevisionId
     *            Revision identifier
     * @return The serializable sheet structure
     * @throws AuthorizationException
     *             Illegal access to the revision
     */
    public org.topcased.gpm.business.serialization.data.SheetData getSerializableSheetInRevision(
            String pRoleToken, String pContainerId, String pRevisionId)
        throws AuthorizationException;

    /**
     * Delete the latest revision on container pContainerId
     * 
     * @param pRoleToken
     *            Role session token
     * @param pContainerId
     *            Container identifier
     * @throws AuthorizationException
     *             Illegal access to the revision
     */
    public void deleteRevision(String pRoleToken, String pContainerId)
        throws AuthorizationException;

    /**
     * Get the number of revisions on the container
     * 
     * @param pRoleToken
     *            Role session token
     * @param pContainerId
     *            Container identifier
     * @return Number of revisions
     */
    public int getRevisionsCount(String pRoleToken, String pContainerId);

    /**
     * Get a summary of revisions created on a container
     * 
     * @param pRoleToken
     *            The role token
     * @param pContainerId
     *            Container identifier
     * @return Collection containing a summary of each revision
     * @throws AuthorizationException
     *             Illegal access to the revision
     */
    public List<RevisionSummaryData> getRevisionsSummary(String pRoleToken,
            String pContainerId) throws AuthorizationException;

    /**
     * Get a list of n last revision labels created on a container. The returned
     * list is ordered by descendant labels (First label corresponds to the last
     * revision, etc ...) If there are less revisions than pLastItems, then the
     * returned list size will be the the number of revisions.
     * 
     * @param pRoleToken
     *            The role token
     * @param pContainerId
     *            Container identifier
     * @param pLastItems
     *            Number of return revisions labels
     * @return Collection containing label of each revision
     * @throws AuthorizationException
     *             Illegal access to the revision
     */
    public List<String> getRevisionLabels(String pRoleToken,
            String pContainerId, int pLastItems) throws AuthorizationException;

    /**
     * Compare two revisions on container pContainerId
     * 
     * @param pRoleToken
     *            Role session token
     * @param pContainerId
     *            Container identifier
     * @param pRevisionFirstId
     *            Revision 1 to be compared identifier
     * @param pRevisionSndId
     *            Revision 2 to be compared identifier
     * @return Structure containing the differences between the two revisions
     * @throws AuthorizationException
     *             Illegal access to the revision
     */
    public RevisionDifferencesData getDifferencesBetweenRevisions(
            String pRoleToken, String pContainerId, String pRevisionFirstId,
            String pRevisionSndId) throws AuthorizationException;

    /**
     * Update the container structure with the content of a revision
     * 
     * @param pRoleToken
     *            Role session token
     * @param pContainerId
     *            Container identifier
     * @param pRevisionId
     *            Revision identifier
     * @throws AuthorizationException
     *             Illegal access to the revision
     */
    public void updateContainerFromRevision(String pRoleToken,
            String pContainerId, String pRevisionId)
        throws AuthorizationException;

    /**
     * Get a revision identifier from a revision label
     * 
     * @param pRoleToken
     *            The role token
     * @param pContainerId
     *            Container identifier
     * @param pLabel
     *            Revision label
     * @return The revision identifier
     * @throws AuthorizationException
     *             Illegal access to the revision
     */
    public String getRevisionIdFromRevisionLabel(String pRoleToken,
            String pContainerId, String pLabel) throws AuthorizationException;
}
