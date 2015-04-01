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
 * @see org.topcased.gpm.domain.accesscontrol.FieldAccessControl
 * @author llatil
 * @author mfranchet
 */
public class FieldAccessControlDaoImpl
        extends
        org.topcased.gpm.domain.AbstractDao<org.topcased.gpm.domain.accesscontrol.FieldAccessControl, java.lang.String>
        implements org.topcased.gpm.domain.accesscontrol.FieldAccessControlDao {

    // First part of the HQL request
    static final String QUERY_GET_APPLICABLES_FIRST_PART =
            "select fac from "
                    + FieldAccessControl.class.getName()
                    + " fac left join fac."
                    + AccessControlConstant.DB_ATTRIBUTE_PRODUCTCONTROL.getAsString()
                    + " product"
                    + " left join fac."
                    + AccessControlConstant.DB_ATTRIBUTE_STATECONTROL.getAsString()
                    + " state"
                    + " left join fac."
                    + AccessControlConstant.DB_ATTRIBUTE_ROLECONTROL.getAsString()
                    + " role"
                    + " left join fac."
                    + AccessControlConstant.DB_ATTRIBUTE_TYPECONTROL.getAsString()
                    + " type"
                    + " where (product is null or product.name=:productName)"
                    + " and (state is null or state.name=:stateName)"
                    + " and (role is null or role.name=:roleName) "
                    + " and (fac.visibleTypeId is null or fac.visibleTypeId=:visibleTypeId)";

    // Part of the HQL request with the check type
    static final String QUERY_GET_APPLICABLES_CHECK_TYPE =
            " and (type is null or (type.id=:typeId and "
                    + " fac."
                    + AccessControlConstant.DB_ATTRIBUTE_FIELD_FIELDCONTROL.getAsString()
                    + ".container.id=:typeId)) ";

    // Part of the HQL request without the check type
    static final String QUERY_GET_APPLICABLES_WITHOUT_CHECK_TYPE =
            " and (type is null or type.id=:typeId)";

    // Last part of the HQL request
    static final String QUERY_GET_APPLICABLES_LAST_PART =
            " and fac."
                    + AccessControlConstant.DB_ATTRIBUTE_FIELD_FIELDCONTROL.getAsString()
                    + ".id=:fieldId ";

    /**
     * Constructor
     */
    public FieldAccessControlDaoImpl() {
        super(org.topcased.gpm.domain.accesscontrol.FieldAccessControl.class);
    }

    /**
     * Note : If the pCheckType parameter is set to true, then the HQL request
     * will not check if the type of the field corresponds to the container.
     * (For instance, this check is not useful when a fieldAccess has been set
     * on a linkField on the origin or destination sheet type). {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.accesscontrol.FieldAccessControlDaoBase#getApplicables(java.lang.String,
     *      java.lang.String, java.lang.String, java.lang.String,
     *      java.lang.String, boolean)
     */
    @SuppressWarnings("rawtypes")
    public List getApplicables(final String pProductName,
            final String pStateName, final String pRoleName,
            final String pTypeId, final String pFieldId,
            final String pVisibleTypeId, final boolean pCheckType) {
        assert (pCheckType);
        final Query lQuery =
                getSession().createQuery(getQueryString(pCheckType));

        lQuery.setParameter("productName", pProductName);
        lQuery.setParameter("stateName", pStateName);
        lQuery.setParameter("roleName", pRoleName);
        lQuery.setParameter("typeId", pTypeId);
        lQuery.setParameter("fieldId", pFieldId);
        lQuery.setParameter("visibleTypeId", pVisibleTypeId);

        return lQuery.list();
    }

    /**
     * Get the HQL query string
     * 
     * @param pCheckType
     *            Boolean indicating if the field type will be checked in the
     *            request
     * @return the query string
     */
    protected String getQueryString(boolean pCheckType) {
        final StringBuilder lQueryString = new StringBuilder();

        lQueryString.append(QUERY_GET_APPLICABLES_FIRST_PART);

        if (pCheckType) {
            lQueryString.append(QUERY_GET_APPLICABLES_CHECK_TYPE);
        }
        else {
            lQueryString.append(QUERY_GET_APPLICABLES_WITHOUT_CHECK_TYPE);
        }
        lQueryString.append(QUERY_GET_APPLICABLES_LAST_PART);
        return lQueryString.toString();
    }

    /**
     * @see org.topcased.gpm.domain.accesscontrol.FieldAccessControl#getAll(String)
     */
    @SuppressWarnings("rawtypes")
    public java.util.List getAll(final String pProcessName) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.accesscontrol.FieldAccessControl as fieldAccessControl where fieldAccessControl.processName = :processName";
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
     * @see org.topcased.gpm.domain.accesscontrol.FieldAccessControl#getAll(org.topcased.gpm.domain.businessProcess.BusinessProcess)
     */
    @SuppressWarnings("rawtypes")
    public java.util.List getAll(
            final org.topcased.gpm.domain.businessProcess.BusinessProcess pInstance) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.accesscontrol.FieldAccessControl as fieldAccessControl where fieldAccessControl.instance.id = :instanceId";
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
     * @see org.topcased.gpm.domain.accesscontrol.FieldAccessControl#removeAllFromNode(org.topcased.gpm.domain.process.Node)
     */
    @SuppressWarnings("rawtypes")
    public java.lang.Boolean removeAllFromNode(
            final org.topcased.gpm.domain.process.Node pNode) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.accesscontrol.FieldAccessControl as fieldAccessControl where fieldAccessControl.node.id = :nodeId";
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
     * @see org.topcased.gpm.domain.accesscontrol.FieldAccessControl#removeAllFromProduct(org.topcased.gpm.domain.product.Product)
     */
    @SuppressWarnings("rawtypes")
    public java.lang.Boolean removeAllFromProduct(
            final org.topcased.gpm.domain.product.Product pProduct) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.accesscontrol.FieldAccessControl as fieldAccessControl where fieldAccessControl.product.id = :productId";
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