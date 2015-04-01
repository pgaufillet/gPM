/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.domain.dynamic.container.link;

import org.topcased.gpm.domain.dynamic.DynamicObjectGenerator;
import org.topcased.gpm.domain.dynamic.container.DynamicValuesContainerGeneratorFactory;
import org.topcased.gpm.domain.link.Link;
import org.topcased.gpm.domain.link.LinkType;

/**
 * Factory of DynamicLinkGenerator
 * 
 * @author tpanuel
 */
public final class DynamicLinkGeneratorFactory extends
        DynamicValuesContainerGeneratorFactory<Link, LinkType> {
    private final static DynamicLinkGeneratorFactory INSTANCE =
            new DynamicLinkGeneratorFactory();

    private DynamicLinkGeneratorFactory() {
    }

    /**
     * DynamicLinkGeneratorFactory is a singleton
     * 
     * @return The instance
     */
    public final static DynamicLinkGeneratorFactory getInstance() {
        return INSTANCE;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.dynamic.DynamicObjectGeneratorFactory#createDynamicObjectGenerator(java.lang.Object)
     */
    @Override
    protected DynamicObjectGenerator<Link> createDynamicObjectGenerator(
            LinkType pMappedObjectType) {
        return new DynamicLinkGenerator(pMappedObjectType);
    }
}