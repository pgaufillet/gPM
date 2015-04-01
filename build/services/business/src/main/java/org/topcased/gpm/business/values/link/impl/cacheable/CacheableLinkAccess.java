/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 *
 ******************************************************************/
package org.topcased.gpm.business.values.link.impl.cacheable;

import org.topcased.gpm.business.link.impl.CacheableLink;
import org.topcased.gpm.business.link.impl.CacheableLinkType;
import org.topcased.gpm.business.values.ValuesAccessProperties;
import org.topcased.gpm.business.values.impl.cacheable.AbstractCacheableContainerAccess;
import org.topcased.gpm.business.values.link.BusinessLink;

/**
 * Access on a link.
 * 
 * @author tpanuel
 */
public class CacheableLinkAccess extends
        AbstractCacheableContainerAccess<CacheableLinkType, CacheableLink>
        implements BusinessLink {
    /**
     * Create an access on a link.
     * 
     * @param pRoleToken
     *            The role token.
     * @param pLinkType
     *            The link type to access.
     * @param pLink
     *            The link to access.
     * @param pProperties
     *            The values access properties.
     */
    public CacheableLinkAccess(final String pRoleToken,
            final CacheableLinkType pLinkType, final CacheableLink pLink,
            final ValuesAccessProperties pProperties) {
        super(pRoleToken, pLinkType, pLink, pProperties);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.link.BusinessLink#getOriginId()
     */
    public String getOriginId() {
        return read().getOriginId();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.link.BusinessLink#getDestinationId()
     */
    public String getDestinationId() {
        return read().getDestinationId();
    }
}