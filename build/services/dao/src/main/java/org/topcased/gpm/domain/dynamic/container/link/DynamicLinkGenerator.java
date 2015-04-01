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

import org.topcased.gpm.domain.dynamic.container.DynamicValuesContainerGenerator;
import org.topcased.gpm.domain.link.Link;
import org.topcased.gpm.domain.link.LinkType;

/**
 * Generator of dynamic links
 * 
 * @author tpanuel
 */
public class DynamicLinkGenerator extends DynamicValuesContainerGenerator<Link> {
    private static final Source SOURCE =
            new Source(DynamicLinkGenerator.class.getName());

    /**
     * Create a dynamic links generator
     * 
     * @param pLinkType
     *            The type of the link
     */
    public DynamicLinkGenerator(LinkType pLinkType) {
        super(SOURCE, pLinkType, Link.class, false);
    }
}