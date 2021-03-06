/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 ******************************************************************/
//
// Attention: Generated code! Do not modify by hand!
// Generated by: SpringDao.vsl in andromda-spring-cartridge.
//
package org.topcased.gpm.domain.mapping;

/**
 * @see org.topcased.gpm.domain.mapping.TypeMap
 * @author Atos
 */
public interface TypeMapDao
        extends
        org.topcased.gpm.domain.IDao<org.topcased.gpm.domain.mapping.TypeMap, java.lang.String> {
    /**
     * 
     */
    public org.topcased.gpm.domain.mapping.TypeMap getTypeMapping(
            java.lang.String pOriginProcessName,
            java.lang.String pOriginTypeName,
            java.lang.String pDestinationProcessName,
            java.lang.String pDestinationTypeName);

}