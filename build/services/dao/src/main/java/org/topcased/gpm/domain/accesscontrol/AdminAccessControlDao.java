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
package org.topcased.gpm.domain.accesscontrol;

/**
 * @see org.topcased.gpm.domain.accesscontrol.AdminAccessControl
 * @author Atos
 */
public interface AdminAccessControlDao
        extends
        org.topcased.gpm.domain.IDao<org.topcased.gpm.domain.accesscontrol.AdminAccessControl, java.lang.String> {
    /**
     * 
     */
    @SuppressWarnings("rawtypes")
    public java.util.List getApplicables(java.lang.String pProductName,
            java.lang.String pRoleName, java.lang.String pActionKey);

}