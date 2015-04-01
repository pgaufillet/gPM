/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/

package org.topcased.gpm.domain.accesscontrol;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.springframework.dao.DataAccessResourceFailureException;
import org.topcased.gpm.common.accesscontrol.Roles;
import org.topcased.gpm.domain.product.Product;

/**
 * EndUser DAO implementation
 * 
 * @author llatil
 */
public class EndUserDaoImpl
        extends
        org.topcased.gpm.domain.AbstractDao<org.topcased.gpm.domain.accesscontrol.EndUser, java.lang.String>
        implements org.topcased.gpm.domain.accesscontrol.EndUserDao {

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.accesscontrol.EndUserDaoBase#hasGlobalAdminRole(org.topcased.gpm.domain.accesscontrol.EndUser)
     */
    public Boolean hasGlobalAdminRole(final EndUser pEndUser) {
        final String lHqlQuery =
                "from EndUser user where user.id=:endUserId and"
                        + " exists(from Role role where role.name='admin'"
                        + " and role in elements(user.adminRoles))";
        Query lQuery = getSession().createQuery(lHqlQuery);
        lQuery.setParameter("endUserId", pEndUser.getId());
        return Boolean.valueOf(null != (EndUser) lQuery.uniqueResult());
    }

    /**
     * Contructor
     */
    public EndUserDaoImpl() {
        super(org.topcased.gpm.domain.accesscontrol.EndUser.class);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.accesscontrol.EndUserDaoBase#getUsers(java.lang.String,
     *      java.lang.String, java.lang.String[], int)
     */
    @SuppressWarnings("unchecked")
    public List<String> getUsers(final String pBusinessProcess,
            final String pRoleName, final String[] pProductNames,
            final int pRolesProps) {
        String lHqlQuery =
                "select distinct user.login from " + EndUser.class.getName()
                        + " user, " + Role.class.getName() + " role "
                        + " where role.name=:roleName and ";

        // Get role properties parameters
        boolean lIsInstanceRole = (Roles.INSTANCE_ROLE & pRolesProps) != 0;
        boolean lIsProductRole = (Roles.PRODUCT_ROLE & pRolesProps) != 0;
        boolean lIsRoleOnAllProducts =
                (Roles.ROLE_ON_ALL_PRODUCTS & pRolesProps) != 0;
        boolean lIsRoleOnOneProduct =
                (Roles.ROLE_ON_ONE_PRODUCT & pRolesProps) != 0;

        if (lIsInstanceRole) {
            /* Add users with role pRoleName on the whole instance. */
            lHqlQuery +=
                    " role.businessProcess.name = :processName and"
                            + " (role in elements(user.adminRoles) ";
        }

        if (pProductNames != null && pProductNames.length > 0 && lIsProductRole) {

            if (lIsInstanceRole) {
                lHqlQuery += " or ";
            }

            if (lIsRoleOnOneProduct) {
                /* Find users with role pRoleName on at least one product from pProductNames */
                lHqlQuery +=
                        " exists (from "
                                + RolesForProduct.class.getName()
                                + " roleFromProduct"
                                + " where roleFromProduct in elements(user.rolesForProducts)"
                                + " and roleFromProduct.product.name in (:productNames)"
                                + " and role in elements(roleFromProduct.roles))";
            }

            else if (lIsRoleOnAllProducts) {
                /* Find users with role pRoleName on each product */
                // Add successively query part for each of the products
                for (int i = 0; i < pProductNames.length - 1; i++) {
                    lHqlQuery +=
                            " exists (from "
                                    + RolesForProduct.class.getName()
                                    + " roleFromProduct"
                                    + " where roleFromProduct in elements(user.rolesForProducts)"
                                    + " and roleFromProduct.product.name = :productName_"
                                    + i
                                    + " and role in elements(roleFromProduct.roles))";
                    lHqlQuery += " and ";

                }
                lHqlQuery +=
                        " exists (from "
                                + RolesForProduct.class.getName()
                                + " roleFromProduct"
                                + " where roleFromProduct in elements(user.rolesForProducts)"
                                + " and roleFromProduct.product.name = :productName_"
                                + (pProductNames.length - 1)
                                + " and role in elements(roleFromProduct.roles))";

            }
        }
        lHqlQuery += ")";
        Query lQuery = getSession().createQuery(lHqlQuery);
        if (lIsInstanceRole) {
            lQuery.setParameter("processName", pBusinessProcess);
        }
        lQuery.setParameter("roleName", pRoleName);
        if (pProductNames != null) {
            if (lIsRoleOnOneProduct) {
                lQuery.setParameterList("productNames", pProductNames);
            }
            else if (lIsRoleOnAllProducts) {
                int i = 0;
                for (String lProductName : pProductNames) {
                    lQuery.setParameter("productName_" + i, lProductName);
                    i++;
                }
            }
        }
        return lQuery.list();
    }

    private final static String HAS_GLOBAL_ROLE =
            "select role.name from "
                    + EndUser.class.getName()
                    + " user inner join "
                    + " user.adminRoles as role"
                    + " where user.login=:login and role.businessProcess.name=:businessProcName";

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.accesscontrol.EndUserDaoBase#hasGlobalRole(java.lang.String,
     *      java.lang.String)
     */
    public Boolean hasGlobalRole(final String pLogin,
            final String pBusinessProcessName) {
        final Query lQuery = getSession().createQuery(HAS_GLOBAL_ROLE);

        lQuery.setParameter("login", pLogin);
        lQuery.setParameter("businessProcName", pBusinessProcessName);
        lQuery.setMaxResults(1);

        return lQuery.iterate().hasNext();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.accesscontrol.EndUserDaoBase#getProducts(org.topcased.gpm.domain.accesscontrol.EndUser,
     *      java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public List<Product> getProducts(final EndUser pUser,
            final String pBusinessProcessName) {
        Query lQuery;
        if (hasGlobalRole(pUser.getLogin(), pBusinessProcessName)) {
            String lQueryStr =
                    "from "
                            + Product.class.getName()
                            + " prod where prod.businessProcess.name=:businessProcName "
                            + " order by prod.name asc";

            lQuery = getSession().createQuery(lQueryStr);
            lQuery.setParameter("businessProcName", pBusinessProcessName);
        }
        else {
            String lQueryStr =
                    "select distinct prod from "
                            + EndUser.class.getName()
                            + " user inner join user.rolesForProducts as rfp"
                            + " inner join rfp.product prod"
                            + " where prod.businessProcess.name=:businessProcName"
                            + " and user.login=:login order by prod.name asc";

            lQuery = getSession().createQuery(lQueryStr);
            lQuery.setParameter("businessProcName", pBusinessProcessName);
            lQuery.setParameter("login", pUser.getLogin());
        }
        return lQuery.list();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.accesscontrol.EndUserDaoBase#getUser(java.lang.String,
     *      java.lang.Boolean)
     */
    @SuppressWarnings("rawtypes")
    public EndUser getUser(final String pLogin, final boolean pCaseSensitive) {

        String lLoginParameter = pLogin;
        String lLogin = "endUser.login";

        // Minimalize login list
        if (!pCaseSensitive) {
            lLoginParameter = StringUtils.lowerCase(lLoginParameter);
            lLogin = "lower(" + lLogin + ")";
        }

        String lHqlQuery = "from EndUser endUser where " + lLogin + " = :login";

        Query lQuery = getSession().createQuery(lHqlQuery);
        lQuery.setParameter("login", lLoginParameter);
        List lResults = lQuery.list();

        EndUser lResult = null;
        if (lResults != null) {
            if (lResults.size() > 1) {
                throw new org.springframework.dao.InvalidDataAccessResourceUsageException(
                        "More than one instance of 'org.topcased.gpm.domain.accesscontrol.EndUser"
                                + "' was found when executing query --> '"
                                + lQuery.getQueryString() + "'");
            }
            else if (lResults.size() == 1) {
                lResult = (EndUser) lResults.iterator().next();
            }
        }
        return lResult;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.accesscontrol.EndUserDaoBase#getUsersFromLogins(java.lang.String[],
     *      java.lang.Boolean)
     */
    @SuppressWarnings("unchecked")
    public List<EndUser> getUsersFromLogins(final String[] pLogins,
            boolean pCaseSensitive) {

        String[] lLoginsParameter = pLogins;
        String lLogin = "endUser.login";

        // Minimalize login list
        if (!pCaseSensitive) {
            for (int i = 0; i < lLoginsParameter.length; i++) {
                lLoginsParameter[i] =
                        StringUtils.lowerCase(lLoginsParameter[i]);
            }
            lLogin = "lower(" + lLogin + ")";
        }

        String lHqlQuery =
                "from EndUser endUser where " + lLogin + " in (:logins)";

        Query lQuery = getSession().createQuery(lHqlQuery);
        lQuery.setParameterList("logins", lLoginsParameter);
        return lQuery.list();
    }

    private static final String AVAILABLE_PRODUCT_NAMES_4_GLOBAL_ROLE =
            "SELECT p.name FROM " + Product.class.getName() + " p "
                    + "WHERE p.businessProcess.name = :processName "
                    + "ORDER BY p.name ASC";

    private static final String AVAILABLE_PRODUCT_NAMES_4_PRODUCT_ROLE =
            "SELECT distinct p.name FROM " + EndUser.class.getName() + " u "
                    + "INNER JOIN u.rolesForProducts as rfp "
                    + "INNER JOIN rfp.product as p "
                    + "WHERE p.businessProcess.name = :processName "
                    + "AND u.login = :login ORDER BY p.name ASC";

    private static final String AVAILABLE_PRODUCTS_4_GLOBAL_ROLE =
            "SELECT p.name, p.description FROM " + Product.class.getName()
                    + " p " + "WHERE p.businessProcess.name = :processName "
                    + "ORDER BY p.name ASC";

    private static final String AVAILABLE_PRODUCTS_4_PRODUCT_ROLE =
            "SELECT distinct p.name, p.description FROM "
                    + EndUser.class.getName() + " u "
                    + "INNER JOIN u.rolesForProducts as rfp "
                    + "INNER JOIN rfp.product as p "
                    + "WHERE p.businessProcess.name = :processName "
                    + "AND u.login = :login ORDER BY p.name ASC";

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.accesscontrol.EndUserDaoBase#getAvailableProductsName(java.lang.String,
     *      java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public List<String> getAvailableProductsName(final String pLogin,
            final String pProcessName) {
        final Query lQuery;

        if (hasGlobalRole(pLogin, pProcessName)) {
            lQuery =
                    getSession().createQuery(
                            AVAILABLE_PRODUCT_NAMES_4_GLOBAL_ROLE);
            lQuery.setParameter("processName", pProcessName);
        }
        else {
            lQuery =
                    getSession().createQuery(
                            AVAILABLE_PRODUCT_NAMES_4_PRODUCT_ROLE);
            lQuery.setParameter("processName", pProcessName);
            lQuery.setParameter("login", pLogin);
        }

        return lQuery.list();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.accesscontrol.EndUserDao#getAvailableProducts(java.lang.String,
     *      java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    @Override
    public List getAvailableProducts(final String pLogin,
            final String pProcessName) {
        final Query lQuery;

        if (hasGlobalRole(pLogin, pProcessName)) {
            lQuery = getSession().createQuery(AVAILABLE_PRODUCTS_4_GLOBAL_ROLE);
            lQuery.setParameter("processName", pProcessName);
        }
        else {
            lQuery =
                    getSession().createQuery(AVAILABLE_PRODUCTS_4_PRODUCT_ROLE);
            lQuery.setParameter("processName", pProcessName);
            lQuery.setParameter("login", pLogin);
        }

        return lQuery.list();
    }

    private static final String ALL_USERS_LOGIN =
            "SELECT login FROM " + EndUser.class.getName()
                    + " ORDER BY login ASC";

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.accesscontrol.EndUserDaoBase#getUserLogins()
     */
    @Override
    public List<String> getUserLogins() {
        return execute(createQuery(ALL_USERS_LOGIN), String.class);
    }

    private static final String ALL_USERS_ID =
            "SELECT id FROM " + EndUser.class.getName();

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.accesscontrol.EndUserDaoBase#usersIterator()
     */
    public Iterator<String> usersIterator() {
        return iterate(createQuery(ALL_USERS_ID), String.class);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.accesscontrol.EndUserDaoBase#getId(java.lang.String,
     *      java.lang.Boolean)
     */
    @Override
    public String getId(String pLogin, Boolean pIsCaseSensitive) {
        Query lQuery = getUserId(pLogin, pIsCaseSensitive);
        return (String) lQuery.uniqueResult();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.accesscontrol.EndUserDaoBase#isExists(java.lang.String,
     *      java.lang.Boolean)
     */
    @Override
    public Boolean isExists(String pLogin, Boolean pIsCaseSensitive) {
        Query lQuery = getUserId(pLogin, pIsCaseSensitive);
        return hasResult(lQuery);
    }

    /**
     * Query that retrieve the user identifier using its login.
     * <p>
     * Login case sensitive is handled.
     * </p>
     * 
     * @param pLogin
     *            Login of the user to search.
     * @param pIsCaseSensitive
     *            True to search with case sensitive option.
     * @return Technical identifier of the user.
     * @throws HibernateException
     *             Query error
     * @throws DataAccessResourceFailureException
     *             Query error
     * @throws IllegalStateException
     *             Query error
     */
    private Query getUserId(String pLogin, Boolean pIsCaseSensitive)
        throws HibernateException, DataAccessResourceFailureException,
        IllegalStateException {
        String lLoginParameter = pLogin;
        String lLogin = "user.login";

        // Minimize login
        if (!pIsCaseSensitive) {
            lLoginParameter = StringUtils.lowerCase(lLoginParameter);
            lLogin = "lower(" + lLogin + ")";
        }

        String lStringQuery =
                "SELECT user.id FROM " + EndUser.class.getName() + " user "
                        + "WHERE " + lLogin + " = :pLogin";

        Query lQuery = getSession().createQuery(lStringQuery);
        lQuery.setParameter("pLogin", lLoginParameter);
        return lQuery;
    }

    private static final String ID_FROM_LOGIN =
            "SELECT user.login" + " FROM " + EndUser.class.getName() + " user"
                    + " WHERE user.id = :pId";

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.accesscontrol.EndUserDaoBase#getLoginFromId(java.lang.String)
     */
    @Override
    public String getLoginFromId(String pId) {
        Query lQuery = getSession().createQuery(ID_FROM_LOGIN);
        lQuery.setParameter("pId", pId);
        return (String) lQuery.uniqueResult();
    }

    /**
     * @see org.topcased.gpm.domain.accesscontrol.EndUser#getProducts(org.topcased.gpm.domain.accesscontrol.EndUser)
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public java.util.List<org.topcased.gpm.domain.product.Product> getProducts(
            final org.topcased.gpm.domain.accesscontrol.EndUser pUser) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.accesscontrol.EndUser as endUser where endUser.user.id = :userId";
            org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("userId", pUser.getId());
            java.util.List lResults = lQueryObject.list();
            java.util.List<org.topcased.gpm.domain.product.Product> lResult =
                    null;
            if (lResults != null) {
                if (lResults.size() > 1) {
                    throw new org.springframework.dao.InvalidDataAccessResourceUsageException(
                            "More than one instance of 'java.util.List<org.topcased.gpm.domain.product.Product>"
                                    + "' was found when executing query --> '"
                                    + lQueryString + "'");
                }
                else if (lResults.size() == 1) {
                    lResult =
                            (java.util.List<org.topcased.gpm.domain.product.Product>) lResults.iterator().next();
                }
            }
            return lResult;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.topcased.gpm.domain.accesscontrol.EndUser#getAllAttrNames(org.topcased.gpm.domain.attributes.AttributesContainer)
     */
    @SuppressWarnings("rawtypes")
    public java.util.List getAllAttrNames(
            final org.topcased.gpm.domain.attributes.AttributesContainer pAttrContainer) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.accesscontrol.EndUser as endUser where endUser.attrContainer.id = :attrContainerId";
            org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("attrContainerId", pAttrContainer.getId());
            java.util.List lResults = lQueryObject.list();
            return lResults;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.topcased.gpm.domain.accesscontrol.EndUser#getAttributes(org.topcased.gpm.domain.attributes.AttributesContainer,
     *      java.lang.String[])
     */
    @SuppressWarnings("rawtypes")
    public java.util.List getAttributes(
            final org.topcased.gpm.domain.attributes.AttributesContainer pAttrContainer,
            final java.lang.String[] pAttrNames) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.accesscontrol.EndUser as endUser where endUser.attrContainer.id = :attrContainerId and endUser.attrNames = :attrNames";
            org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("attrContainerId", pAttrContainer.getId());
            lQueryObject.setParameterList("attrNames", pAttrNames);
            java.util.List lResults = lQueryObject.list();
            return lResults;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.topcased.gpm.domain.accesscontrol.EndUser#getAttribute(org.topcased.gpm.domain.attributes.AttributesContainer,
     *      java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public org.topcased.gpm.domain.attributes.Attribute getAttribute(
            final org.topcased.gpm.domain.attributes.AttributesContainer pAttrContainer,
            final java.lang.String pAttrName) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.accesscontrol.EndUser as endUser where endUser.attrContainer.id = :attrContainerId and endUser.attrName = :attrName";
            org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("attrContainerId", pAttrContainer.getId());
            lQueryObject.setParameter("attrName", pAttrName);
            java.util.List lResults = lQueryObject.list();
            org.topcased.gpm.domain.attributes.Attribute lResult = null;
            if (lResults != null) {
                if (lResults.size() > 1) {
                    throw new org.springframework.dao.InvalidDataAccessResourceUsageException(
                            "More than one instance of 'org.topcased.gpm.domain.attributes.Attribute"
                                    + "' was found when executing query --> '"
                                    + lQueryString + "'");
                }
                else if (lResults.size() == 1) {
                    lResult =
                            (org.topcased.gpm.domain.attributes.Attribute) lResults.iterator().next();
                }
            }
            return lResult;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }
    
    public void updateLastConnection(EndUser pEndUser) {
        pEndUser.setLastConnection(new Date());
    }

}