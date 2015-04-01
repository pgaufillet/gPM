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

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.topcased.gpm.domain.constant.AccessControlConstant;

/**
 * @see org.topcased.gpm.domain.accesscontrol.AdminAccessControl
 * @author mfranchet
 */
public class AdminAccessControlDaoImpl
        extends
        org.topcased.gpm.domain.AbstractDao<org.topcased.gpm.domain.accesscontrol.AdminAccessControl, java.lang.String>
        implements org.topcased.gpm.domain.accesscontrol.AdminAccessControlDao {

    static final String QUERY_GET_APPLICABLES_SELECT =
            "select ac from "
                    + AdminAccessControl.class.getName()
                    + " ac left join ac."
                    + AccessControlConstant.DB_ATTRIBUTE_PRODUCTCONTROL.getAsString()
                    + " product"
                    + " left join ac."
                    + AccessControlConstant.DB_ATTRIBUTE_ROLECONTROL.getAsString()
                    + " role";

    static final String QUERY_GET_APPLICABLES_WHERE_WITH_PRODUCT =
            " where product.name=:productName ";

    static final String QUERY_GET_APPLICABLES_WHERE_WITHOUT_PRODUCT =
            " where product is null ";

    static final String QUERY_GET_APPLICABLES_WHERE =
            " and (role is null or role.name=:roleName) "
                    + " and ac.actionKey=:action";

    /**
     * Create a DAO
     */
    public AdminAccessControlDaoImpl() {
        super(org.topcased.gpm.domain.accesscontrol.AdminAccessControl.class);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.accesscontrol.AdminAccessControlDaoBase#getApplicables(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public List getApplicables(final String pProductName, final String pRoleName, final String pActionKey) {
        Query lQuery = null;

        if (StringUtils.isEmpty(pProductName)) {
            StringBuffer lQueryString = new StringBuffer();
            lQueryString.append(QUERY_GET_APPLICABLES_SELECT);
            lQueryString.append(QUERY_GET_APPLICABLES_WHERE_WITHOUT_PRODUCT);
            lQueryString.append(QUERY_GET_APPLICABLES_WHERE);

            lQuery = getSession().createQuery(lQueryString.toString());
            lQuery.setParameter("roleName", pRoleName);
            lQuery.setParameter("action", pActionKey);
        }
        else {
            StringBuffer lQueryString = new StringBuffer();
            lQueryString.append(QUERY_GET_APPLICABLES_SELECT);
            lQueryString.append(QUERY_GET_APPLICABLES_WHERE_WITH_PRODUCT);
            lQueryString.append(QUERY_GET_APPLICABLES_WHERE);

            lQuery = getSession().createQuery(lQueryString.toString());
            lQuery.setParameter("productName", pProductName);
            lQuery.setParameter("roleName", pRoleName);
            lQuery.setParameter("action", pActionKey);
        }

        return lQuery.list();
    }

    /**
     * @see org.topcased.gpm.domain.accesscontrol.AdminAccessControl#getAll(String)
     */
    @SuppressWarnings("rawtypes")
    public java.util.List getAll(final String pProcessName) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.accesscontrol.AdminAccessControl as adminAccessControl where adminAccessControl.processName = :processName";
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
     * @see org.topcased.gpm.domain.accesscontrol.AdminAccessControl#getAll(org.topcased.gpm.domain.businessProcess.BusinessProcess)
     */
    @SuppressWarnings("rawtypes")
    public java.util.List getAll(
            final org.topcased.gpm.domain.businessProcess.BusinessProcess pInstance) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.accesscontrol.AdminAccessControl as adminAccessControl where adminAccessControl.instance.id = :instanceId";
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
     * @see org.topcased.gpm.domain.accesscontrol.AdminAccessControl#removeAllFromNode(org.topcased.gpm.domain.process.Node)
     */
    @SuppressWarnings("rawtypes")
    public java.lang.Boolean removeAllFromNode(
            final org.topcased.gpm.domain.process.Node pNode) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.accesscontrol.AdminAccessControl as adminAccessControl where adminAccessControl.node.id = :nodeId";
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
     * @see org.topcased.gpm.domain.accesscontrol.AdminAccessControl#removeAllFromProduct(org.topcased.gpm.domain.product.Product)
     */
    @SuppressWarnings("rawtypes")
    public java.lang.Boolean removeAllFromProduct(
            final org.topcased.gpm.domain.product.Product pProduct) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.accesscontrol.AdminAccessControl as adminAccessControl where adminAccessControl.product.id = :productId";
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