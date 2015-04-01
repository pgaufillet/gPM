/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.values.service;

import org.topcased.gpm.business.exception.InstantiateException;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.serialization.data.TypeMapping;
import org.topcased.gpm.business.values.BusinessContainer;

/**
 * Values Service.
 * 
 * @author tpanuel
 */
public interface ValuesService {
    /**
     * The destination container's values are initialized with the values of the
     * origin one. The mapping defined during the instantiation is used.
     * 
     * @param pRoleToken
     *            The role token.
     * @param pOrigin
     *            The origin container.
     * @param pDestination
     *            The destination container.
     * @param pContext
     *            The context.
     */
    public void initializeValues(String pRoleToken, BusinessContainer pOrigin,
            BusinessContainer pDestination, Context pContext);

    /**
     * Create a type mapping. If a type mapping already exists, it's erased.
     * 
     * @param pRoleToken
     *            The role token.
     * @param pTypeMapping
     *            The type mapping to create.
     * @throws InstantiateException
     *             The mapping cannot be created : invalid content.
     */
    public void createTypeMapping(String pRoleToken, TypeMapping pTypeMapping)
        throws InstantiateException;
}