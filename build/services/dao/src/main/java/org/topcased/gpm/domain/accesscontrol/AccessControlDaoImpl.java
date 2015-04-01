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

import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.topcased.gpm.domain.businessProcess.BusinessProcess;
import org.topcased.gpm.domain.constant.AccessControlConstant;
import org.topcased.gpm.domain.process.Node;
import org.topcased.gpm.domain.product.Product;

/**
 * @see org.topcased.gpm.domain.accesscontrol.AccessControl
 * @author llatil
 */
public class AccessControlDaoImpl
        extends
        org.topcased.gpm.domain.AbstractDao<org.topcased.gpm.domain.accesscontrol.AccessControl, java.lang.String>
        implements org.topcased.gpm.domain.accesscontrol.AccessControlDao {

    final static String QUERY_GET_ALL =
            "select ac from "
                    + AccessControl.class.getName()
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
                    + " type where "
                    + "(product is null or product.businessProcess.id=:instanceId)"
                    + " and (role is null or role.businessProcess.id=:instanceId) "
                    + " and (type is null or type.businessProcess.id=:instanceId)";

    /**
     * Create a DAO.
     */
    public AccessControlDaoImpl() {
        super(org.topcased.gpm.domain.accesscontrol.AccessControl.class);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.accesscontrol.AccessControlDaoBase#getAll(org.topcased.gpm.domain.businessProcess.BusinessProcess)
     */
    @SuppressWarnings("rawtypes")
    @Override
    public List getAll(final BusinessProcess pBusinessProcess) {
        Query lQuery = getSession().createQuery(QUERY_GET_ALL);
        lQuery.setParameter("instanceId", pBusinessProcess.getId());
        return lQuery.list();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.accesscontrol.AccessControlDaoBase#removeAllFromNode(org.topcased.gpm.domain.process.Node)
     */
    @SuppressWarnings("rawtypes")
    public Boolean removeAllFromNode(final Node pNode) {
        final Query lQuery =
                getSession().createQuery(
                        "from org.topcased.gpm.domain.accesscontrol.AccessControl "
                                + "where stateControl.id = :nodeId");
        lQuery.setParameter("nodeId", pNode.getId());
        Iterator lResult = lQuery.iterate();
        while (lResult.hasNext()) {
            getSession().delete(lResult.next());
        }
        return true;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.accesscontrol.AccessControlDaoBase#removeAllFromProduct(org.topcased.gpm.domain.product.Product)
     */
    @SuppressWarnings("rawtypes")
    public Boolean removeAllFromProduct(final Product pProduct) {
        final Query lQuery =
                getSession().createQuery(
                        "from org.topcased.gpm.domain.accesscontrol.AccessControl "
                                + "where productControl.id = :productId");
        lQuery.setParameter("productId", pProduct.getId());
        Iterator lResult = lQuery.iterate();
        while (lResult.hasNext()) {
            getSession().delete(lResult.next());
        }
        return true;
    }

    @SuppressWarnings("rawtypes")
    @Override
    /**
     * @see org.topcased.gpm.domain.accesscontrol.AccessControl#getAll(String)
     */
    public java.util.List getAll(final String pProcessName) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.accesscontrol.AccessControl as accessControl where accessControl.processName = :processName";
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
}