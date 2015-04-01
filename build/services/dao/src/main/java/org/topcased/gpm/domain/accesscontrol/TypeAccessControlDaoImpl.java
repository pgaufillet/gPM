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
package org.topcased.gpm.domain.accesscontrol;

import java.util.List;

import org.hibernate.Query;
import org.topcased.gpm.domain.constant.AccessControlConstant;

/**
 * @see org.topcased.gpm.domain.accesscontrol.TypeAccessControl
 * @author mkargbo
 */
public class TypeAccessControlDaoImpl
        extends
        org.topcased.gpm.domain.AbstractDao<org.topcased.gpm.domain.accesscontrol.TypeAccessControl, java.lang.String>
        implements org.topcased.gpm.domain.accesscontrol.TypeAccessControlDao {

    static final String QUERY_GET_APPLICABLES =
            "select tac from "
                    + TypeAccessControl.class.getName()
                    + " tac left join tac."
                    + AccessControlConstant.DB_ATTRIBUTE_PRODUCTCONTROL.getAsString()
                    + " product"
                    + " left join tac."
                    + AccessControlConstant.DB_ATTRIBUTE_STATECONTROL.getAsString()
                    + " state"
                    + " left join tac."
                    + AccessControlConstant.DB_ATTRIBUTE_ROLECONTROL.getAsString()
                    + " role"
                    + " left join tac."
                    + AccessControlConstant.DB_ATTRIBUTE_TYPECONTROL.getAsString()
                    + " type"
                    + " where (product is null or product.name=:productName)"
                    + " and (state is null or state.name=:stateName)"
                    + " and (role is null or role.name=:roleName) "
                    + " and (type is null or type.id=:typeId)";

    /**
     * Constructor
     */
    public TypeAccessControlDaoImpl() {
        super(org.topcased.gpm.domain.accesscontrol.TypeAccessControl.class);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.accesscontrol.TypeAccessControlDaoBase#getApplicables(java.lang.String,
     *      java.lang.String, java.lang.String, java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public List getApplicables(String pProductName, String pStateName,
            String pRoleName, String pTypeId) {
        final Query lQuery = getSession().createQuery(QUERY_GET_APPLICABLES);
        lQuery.setParameter("productName", pProductName);
        lQuery.setParameter("stateName", pStateName);
        lQuery.setParameter("roleName", pRoleName);
        lQuery.setParameter("typeId", pTypeId);

        return lQuery.list();
    }

    /**
     * @see org.topcased.gpm.domain.accesscontrol.TypeAccessControl#getAll(String)
     */
    @SuppressWarnings("rawtypes")
    public java.util.List getAll(final String pProcessName) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.accesscontrol.TypeAccessControl as typeAccessControl where typeAccessControl.processName = :processName";
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
     * @see org.topcased.gpm.domain.accesscontrol.TypeAccessControl#getAll(org.topcased.gpm.domain.businessProcess.BusinessProcess)
     */
    @SuppressWarnings("rawtypes")
    public java.util.List getAll(
            final org.topcased.gpm.domain.businessProcess.BusinessProcess pInstance) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.accesscontrol.TypeAccessControl as typeAccessControl where typeAccessControl.instance.id = :instanceId";
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
     * @see org.topcased.gpm.domain.accesscontrol.TypeAccessControl#removeAllFromNode(org.topcased.gpm.domain.process.Node)
     */
    @SuppressWarnings("rawtypes")
    public java.lang.Boolean removeAllFromNode(
            final org.topcased.gpm.domain.process.Node pNode) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.accesscontrol.TypeAccessControl as typeAccessControl where typeAccessControl.node.id = :nodeId";
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
     * @see org.topcased.gpm.domain.accesscontrol.TypeAccessControl#removeAllFromProduct(org.topcased.gpm.domain.product.Product)
     */
    @SuppressWarnings("rawtypes")
    public java.lang.Boolean removeAllFromProduct(
            final org.topcased.gpm.domain.product.Product pProduct) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.accesscontrol.TypeAccessControl as typeAccessControl where typeAccessControl.product.id = :productId";
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