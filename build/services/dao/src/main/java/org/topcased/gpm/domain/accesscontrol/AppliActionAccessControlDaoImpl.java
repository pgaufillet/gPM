/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 ******************************************************************/
package org.topcased.gpm.domain.accesscontrol;

import java.util.List;

import org.hibernate.Query;
import org.topcased.gpm.domain.constant.AccessControlConstant;

/**
 * @see org.topcased.gpm.domain.accesscontrol.AppliActionAccessControl
 * @author llatil
 */
public class AppliActionAccessControlDaoImpl
        extends
        org.topcased.gpm.domain.AbstractDao<org.topcased.gpm.domain.accesscontrol.AppliActionAccessControl, java.lang.String>
        implements
        org.topcased.gpm.domain.accesscontrol.AppliActionAccessControlDao {
    static final String QUERY_GET_APPLICABLES =
            "select ac from "
                    + AppliActionAccessControl.class.getName()
                    + " ac left join ac."
                    + AccessControlConstant.DB_ATTRIBUTE_PRODUCTCONTROL.getAsString()
                    + " product"
                    + " left join ac."
                    + AccessControlConstant.DB_ATTRIBUTE_STATECONTROL.getAsString()
                    + " state"
                    + " left join ac."
                    + AccessControlConstant.DB_ATTRIBUTE_ROLECONTROL.getAsString()
                    + " role"
                    + " left join ac."
                    + AccessControlConstant.DB_ATTRIBUTE_TYPECONTROL.getAsString()
                    + " type"
                    + " where (product is null or product.name=:productName)"
                    + " and (state is null or state.name=:stateName)"
                    + " and (role is null or role.name=:roleName) "
                    + " and (type is null or type.id=:typeId) "
                    + " and ac.actionKey=:action";

    /**
     * Create a DAO.
     */
    public AppliActionAccessControlDaoImpl() {
        super(
                org.topcased.gpm.domain.accesscontrol.AppliActionAccessControl.class);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.accesscontrol.AppliActionAccessControlDaoBase#getApplicables(java.lang.String,
     *      java.lang.String, java.lang.String, java.lang.String,
     *      java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    @Override
    public List getApplicables(final String pProductName,
            final String pStateName, final String pRoleName,
            final String pTypeId, final String pActionKey) {
        final Query lQuery = getSession().createQuery(QUERY_GET_APPLICABLES);
        lQuery.setParameter("productName", pProductName);
        lQuery.setParameter("stateName", pStateName);
        lQuery.setParameter("roleName", pRoleName);
        lQuery.setParameter("typeId", pTypeId);
        lQuery.setParameter("action", pActionKey);

        return lQuery.list();
    }

    /**
     * @see org.topcased.gpm.domain.accesscontrol.AppliActionAccessControl#getApplicables(java.lang.String,
     *      java.lang.String, java.lang.String, java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public java.util.List getApplicables(final java.lang.String pProductName,
            final java.lang.String pStateName,
            final java.lang.String pRoleName, final java.lang.String pActionKey) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.accesscontrol.AppliActionAccessControl as appliActionAccessControl where appliActionAccessControl.productName = :productName and appliActionAccessControl.stateName = :stateName and appliActionAccessControl.roleName = :roleName and appliActionAccessControl.actionKey = :actionKey";
            org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("productName", pProductName);
            lQueryObject.setParameter("stateName", pStateName);
            lQueryObject.setParameter("roleName", pRoleName);
            lQueryObject.setParameter("actionKey", pActionKey);
            java.util.List lResults = lQueryObject.list();
            java.util.List lResult = null;
            if (lResults != null) {
                if (lResults.size() > 1) {
                    throw new org.springframework.dao.InvalidDataAccessResourceUsageException(
                            "More than one instance of 'java.util.List"
                                    + "' was found when executing query --> '"
                                    + lQueryString + "'");
                }
                else if (lResults.size() == 1) {
                    lResult = (java.util.List) lResults.iterator().next();
                }
            }
            return lResult;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.topcased.gpm.domain.accesscontrol.AppliActionAccessControl#getAll(String)
     */
    @SuppressWarnings("rawtypes")
    public java.util.List getAll(final String pProcessName) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.accesscontrol.AppliActionAccessControl as appliActionAccessControl where appliActionAccessControl.processName = :processName";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("processName", pProcessName);
            java.util.List lResults = lQueryObject.list();
            java.util.List lResult = null;
            if (lResults != null) {
                if (lResults.size() > 1) {
                    throw new org.springframework.dao.InvalidDataAccessResourceUsageException(
                            "More than one instance of 'java.util.List"
                                    + "' was found when executing query --> '"
                                    + lQueryString + "'");
                }
                else if (lResults.size() == 1) {
                    lResult = (java.util.List) lResults.iterator().next();
                }
            }
            return lResult;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.topcased.gpm.domain.accesscontrol.AppliActionAccessControl#getAll(org.topcased.gpm.domain.businessProcess.BusinessProcess)
     */
    @SuppressWarnings("rawtypes")
    public java.util.List getAll(
            final org.topcased.gpm.domain.businessProcess.BusinessProcess pInstance) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.accesscontrol.AppliActionAccessControl as appliActionAccessControl where appliActionAccessControl.instance.id = :instanceId";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("instanceId", pInstance.getId());
            java.util.List lResults = lQueryObject.list();
            java.util.List lResult = null;
            if (lResults != null) {
                if (lResults.size() > 1) {
                    throw new org.springframework.dao.InvalidDataAccessResourceUsageException(
                            "More than one instance of 'java.util.List"
                                    + "' was found when executing query --> '"
                                    + lQueryString + "'");
                }
                else if (lResults.size() == 1) {
                    lResult = (java.util.List) lResults.iterator().next();
                }
            }
            return lResult;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.topcased.gpm.domain.accesscontrol.AppliActionAccessControl#removeAllFromNode(org.topcased.gpm.domain.process.Node)
     */
    @SuppressWarnings("rawtypes")
    public java.lang.Boolean removeAllFromNode(
            final org.topcased.gpm.domain.process.Node pNode) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.accesscontrol.AppliActionAccessControl as appliActionAccessControl where appliActionAccessControl.node.id = :nodeId";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("nodeId", pNode.getId());
            java.util.List lResults = lQueryObject.list();
            java.lang.Boolean lResult = null;
            if (lResults != null) {
                if (lResults.size() > 1) {
                    throw new org.springframework.dao.InvalidDataAccessResourceUsageException(
                            "More than one instance of 'java.lang.Boolean"
                                    + "' was found when executing query --> '"
                                    + lQueryString + "'");
                }
                else if (lResults.size() == 1) {
                    lResult = (java.lang.Boolean) lResults.iterator().next();
                }
            }
            return lResult;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.topcased.gpm.domain.accesscontrol.AppliActionAccessControl#removeAllFromProduct(org.topcased.gpm.domain.product.Product)
     */
    @SuppressWarnings("rawtypes")
    public java.lang.Boolean removeAllFromProduct(
            final org.topcased.gpm.domain.product.Product pProduct) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.accesscontrol.AppliActionAccessControl as appliActionAccessControl where appliActionAccessControl.product.id = :productId";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("productId", pProduct.getId());
            java.util.List lResults = lQueryObject.list();
            java.lang.Boolean lResult = null;
            if (lResults != null) {
                if (lResults.size() > 1) {
                    throw new org.springframework.dao.InvalidDataAccessResourceUsageException(
                            "More than one instance of 'java.lang.Boolean"
                                    + "' was found when executing query --> '"
                                    + lQueryString + "'");
                }
                else if (lResults.size() == 1) {
                    lResult = (java.lang.Boolean) lResults.iterator().next();
                }
            }
            return lResult;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

}