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

import org.hibernate.Query;
import org.topcased.gpm.common.extensions.GUIContext;

/**
 * @see org.topcased.gpm.domain.extensions.Context
 * @author ahaugommard
 */
public class ContextDaoImpl
        extends
        org.topcased.gpm.domain.AbstractDao<org.topcased.gpm.domain.extensions.Context, java.lang.String>
        implements org.topcased.gpm.domain.extensions.ContextDao {

    /**
     * Construct
     */
    public ContextDaoImpl() {
        super(org.topcased.gpm.domain.extensions.Context.class);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.extensions.ContextDaoBase#getContext(org.topcased.gpm.common.extensions.GUIContext)
     */
    public Context getContext(final GUIContext pGuiContext) {
        final String lStringQuery =
                "from Context context where context.guiContext = :guiContext";

        final Query lQuery = getSession().createQuery(lStringQuery);
        lQuery.setParameter("guiContext", pGuiContext);
        return (Context) lQuery.uniqueResult();
    }
}