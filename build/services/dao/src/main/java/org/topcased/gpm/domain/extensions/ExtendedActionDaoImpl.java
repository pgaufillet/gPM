/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 ******************************************************************/
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 */
package org.topcased.gpm.domain.extensions;

import java.util.List;

import org.hibernate.Query;
import org.topcased.gpm.common.extensions.GUIContext;
import org.topcased.gpm.domain.businessProcess.BusinessProcess;

/**
 * @see org.topcased.gpm.domain.extensions.ExtendedAction
 * @author ahaugommard
 */
public class ExtendedActionDaoImpl
        extends
        org.topcased.gpm.domain.AbstractDao<org.topcased.gpm.domain.extensions.ExtendedAction, java.lang.String>
        implements org.topcased.gpm.domain.extensions.ExtendedActionDao {

    private static final String GET_EXTENDED_ACTIONS_QUERY_STRING =
            "from ExtendedAction as extendedAction "
                    + "where extendedAction.extensionsContainer.id = :extensionsContainerId "
                    + " and (exists (from Context context"
                    + " where context in elements(extendedAction.contexts)"
                    + " and context.guiContext in (:contexts) )) order by extendedAction.actionOrder asc";

    /**
     * Constructor
     */
    public ExtendedActionDaoImpl() {
        super(org.topcased.gpm.domain.extensions.ExtendedAction.class);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.extensions.ExtendedActionDaoBase#getExtendedActions(java.lang.String,
     *      org.topcased.gpm.common.extensions.GUIContext)
     */
    @SuppressWarnings("unchecked")
    public List<ExtendedAction> getExtendedActions(
            final String pExtensionsContainerId,
            final List<GUIContext> pContexts) {
        final Query lQuery =
                getSession(false).createQuery(GET_EXTENDED_ACTIONS_QUERY_STRING);
        lQuery.setParameter("extensionsContainerId", pExtensionsContainerId);
        lQuery.setParameterList("contexts", pContexts);
        return lQuery.list();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.extensions.ExtendedActionDaoBase#getExtendedAction(java.lang.String,
     *      java.lang.String)
     */
    public ExtendedAction getExtendedAction(
            final String pExtensionsContainerId,
            final String pExtendedActionName) {
        final String lStringQuery =
                " from ExtendedAction as extendedAction where "
                        + "extendedAction.extensionsContainer.id = :extensionsContainerId "
                        + "and extendedAction.name = :name order by extendedAction.actionOrder asc";

        final Query lQuery = getSession(false).createQuery(lStringQuery);
        lQuery.setParameter("extensionsContainerId", pExtensionsContainerId);
        lQuery.setParameter("name", pExtendedActionName);
        return (ExtendedAction) lQuery.uniqueResult();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.extensions.ExtendedActionDaoBase#getExtendedActions(java.lang.String,
     *      org.topcased.gpm.common.extensions.GUIContext)
     */
    @SuppressWarnings("unchecked")
    public List<ExtendedAction> getAllExtendedActions(
            final BusinessProcess pProcess) {
        final String lStringQuery =
                "from ExtendedAction as extendedAction "
                        + "where extendedAction.extensionsContainer.businessProcess = :process order by extendedAction.actionOrder asc";

        final Query lQuery = getSession(false).createQuery(lStringQuery);
        lQuery.setParameter("processId", pProcess.getId());
        return lQuery.list();
    }
}
