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
package org.topcased.gpm.business.values.link;

import org.topcased.gpm.business.values.BusinessContainer;

/**
 * Interface used to access on a link.
 * 
 * @author tpanuel
 */
public interface BusinessLink extends BusinessContainer {
    /**
     * Get the id of the origin container.
     * 
     * @return The id of the origin container.
     */
    public String getOriginId();

    /**
     * Get the id of the destination container.
     * 
     * @return The id of the destination container.
     */
    public String getDestinationId();
}