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
 * @see org.topcased.gpm.domain.accesscontrol.AccessControl
 * @author Atos
 */
public interface AccessControlDao
        extends
        org.topcased.gpm.domain.IDao<org.topcased.gpm.domain.accesscontrol.AccessControl, java.lang.String> {
    /**
     * 
     */
    @SuppressWarnings("rawtypes")
    public java.util.List getAll(String pProcessName);

    /**
     * 
     */
    @SuppressWarnings("rawtypes")
    public java.util.List getAll(
            org.topcased.gpm.domain.businessProcess.BusinessProcess pInstance);

    /**
     * 
     */
    public java.lang.Boolean removeAllFromNode(
            org.topcased.gpm.domain.process.Node pNode);

    /**
     * 
     */
    public java.lang.Boolean removeAllFromProduct(
            org.topcased.gpm.domain.product.Product pProduct);

}