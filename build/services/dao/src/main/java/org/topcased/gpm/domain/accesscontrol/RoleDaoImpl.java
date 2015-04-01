/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.domain.accesscontrol;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;

/**
 * Role DAO implementation
 * 
 * @author llatil
 */
public class RoleDaoImpl
        extends
        org.topcased.gpm.domain.AbstractDao<org.topcased.gpm.domain.accesscontrol.Role, java.lang.Long>
        implements org.topcased.gpm.domain.accesscontrol.RoleDao {

    private final static String ALL_ROLE_NAMES =
            "SELECT r.name FROM " + Role.class.getName() + " r "
                    + "WHERE r.businessProcess.name = :processName "
                    + "ORDER by r.id";

    /**
     * Constructor
     */
    public RoleDaoImpl() {
        super(org.topcased.gpm.domain.accesscontrol.Role.class);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.accesscontrol.RoleDaoBase#getAllRoleNames(java.lang.String)
     */
    public List<String> getAllRoleNames(final String pBusinessProcName) {
        final Query lQuery = createQuery(ALL_ROLE_NAMES);

        lQuery.setParameter("processName", pBusinessProcName);
        lQuery.setCacheable(true);
        return execute(lQuery, String.class);
    }

    private final static String ROLE_IS_USED =
            "SELECT rfp.id, u.id FROM " + RolesForProduct.class.getName()
                    + " rfp, " + EndUser.class.getName() + " u "
                    + "WHERE :role in elements(rfp.roles) "
                    + "OR :role in elements(u.adminRoles)";

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.accesscontrol.RoleDaoBase#isRoleUsed(org.topcased.gpm.domain.accesscontrol.Role)
     */
    public Boolean isRoleUsed(final Role pRole) {
        final Query lQuery = createQuery(ROLE_IS_USED);

        lQuery.setParameter("role", pRole);

        return hasResult(lQuery);
    }

    private final static String USER_ROLES_4_PRODUCTS_AND_INSTANCE =
            "SELECT distinct r FROM " + Role.class.getName() + " r, "
                    + EndUser.class.getName() + " u "
                    + "LEFT JOIN u.rolesForProducts as rpf "
                    + "LEFT JOIN rpf.product as p WHERE u.login = :login "
                    + "AND r.businessProcess.name = :processName "
                    + "AND (r in elements (u.adminRoles) "
                    + "OR (r in elements (rpf.roles) "
                    + "AND p.name = :productName)) " + "ORDER BY r.id";

    private final static String USER_ROLES_4_PRODUCTS_ONLY =
            "SELECT distinct r FROM " + Role.class.getName() + " r, "
                    + EndUser.class.getName() + " u "
                    + "LEFT JOIN u.rolesForProducts as rpf "
                    + "LEFT JOIN rpf.product as p WHERE u.login = :login "
                    + "AND r.businessProcess.name = :processName "
                    + "AND r in elements (rpf.roles) "
                    + "AND p.name = :productName " + "ORDER BY r.id";

    private final static String USER_ROLES_4_INSTANCE_ONLY =
            "SELECT distinct r FROM " + Role.class.getName() + " r, "
                    + EndUser.class.getName() + " u "
                    + "WHERE u.login = :login "
                    + "AND r.businessProcess.name = :processName "
                    + "AND r in elements (u.adminRoles) " + "ORDER BY r.id";

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.accesscontrol.RoleDaoBase#getRoleNames(java.lang.String,
     *      java.lang.String, java.lang.String, boolean, boolean)
     */
    public List<String> getRoleNames(final String pLogin,
            final String pProductName, final String pProcessName,
            final boolean pInstanceRole, final boolean pRoleOnProduct) {
        final List<String> lRolesNames = new LinkedList<String>();
        final Collection<Role> lRoles;

        if (StringUtils.isNotBlank(pProductName) && pRoleOnProduct) {
            if (pInstanceRole) {
                final Query lQuery =
                        createQuery(USER_ROLES_4_PRODUCTS_AND_INSTANCE);

                lQuery.setParameter("login", pLogin);
                lQuery.setParameter("processName", pProcessName);
                lQuery.setParameter("productName", pProductName);
                lQuery.setCacheable(true);
                lRoles = execute(lQuery);
            }
            else {
                final Query lQuery = createQuery(USER_ROLES_4_PRODUCTS_ONLY);

                lQuery.setParameter("login", pLogin);
                lQuery.setParameter("processName", pProcessName);
                lQuery.setParameter("productName", pProductName);
                lQuery.setCacheable(true);
                lRoles = execute(lQuery);
            }
        }
        else {
            if (pInstanceRole) {
                final Query lQuery = createQuery(USER_ROLES_4_INSTANCE_ONLY);

                lQuery.setParameter("login", pLogin);
                lQuery.setParameter("processName", pProcessName);
                lQuery.setCacheable(true);
                lRoles = execute(lQuery);
            }
            else {
                lRoles = Collections.emptyList();
            }
        }

        for (Role lRole : lRoles) {
            lRolesNames.add(lRole.getName());
        }

        return lRolesNames;
    }

    @Override
    public List<Role> getRoles(String pLogin, String pProductName,
            String pProcessName, boolean pInstanceRole, boolean pRoleOnProducts) {
        final List<Role> lRoles;

        if (StringUtils.isNotBlank(pProductName) && pRoleOnProducts) {
            if (pInstanceRole) {
                final Query lQuery =
                        createQuery(USER_ROLES_4_PRODUCTS_AND_INSTANCE);

                lQuery.setParameter("login", pLogin);
                lQuery.setParameter("processName", pProcessName);
                lQuery.setParameter("productName", pProductName);
                lQuery.setCacheable(true);
                lRoles = execute(lQuery);
            }
            else {
                final Query lQuery = createQuery(USER_ROLES_4_PRODUCTS_ONLY);

                lQuery.setParameter("login", pLogin);
                lQuery.setParameter("processName", pProcessName);
                lQuery.setParameter("productName", pProductName);
                lQuery.setCacheable(true);
                lRoles = execute(lQuery);
            }
        }
        else {
            if (pInstanceRole) {
                final Query lQuery = createQuery(USER_ROLES_4_INSTANCE_ONLY);

                lQuery.setParameter("login", pLogin);
                lQuery.setParameter("processName", pProcessName);
                lQuery.setCacheable(true);
                lRoles = execute(lQuery);
            }
            else {
                lRoles = Collections.emptyList();
            }
        }

        return lRoles;
    }

    private final static String ADMIN_ROLE =
            "SELECT r FROM " + Role.class.getName() + " r, "
                    + EndUser.class.getName() + " u "
                    + "LEFT JOIN u.rolesForProducts as rpf "
                    + "LEFT JOIN rpf.product as p WHERE r.name = 'admin' "
                    + "AND u.login = :login "
                    + "AND r.businessProcess.name = :processName "
                    + "AND (r in elements (u.adminRoles) "
                    + "OR (r in elements (rpf.roles) "
                    + "AND p.name = :productName)) " + "ORDER BY r.id";

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.accesscontrol.RoleDaoBase#hasAdminRole(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    public Boolean hasAdminRole(final String pUserLogin,
            final String pProductName, final String pProcessName) {
        final Query lQuery = createQuery(ADMIN_ROLE);

        lQuery.setParameter("login", pUserLogin);
        lQuery.setParameter("productName", pProductName);
        lQuery.setParameter("processName", pProcessName);

        return hasResult(lQuery);
    }

    private final static String HAS_ROLE =
            "SELECT r FROM " + Role.class.getName() + " r, "
                    + EndUser.class.getName() + " u "
                    + "LEFT JOIN u.rolesForProducts as rpf "
                    + "LEFT JOIN rpf.product as p WHERE r.name = :roleName "
                    + "AND u.login = :login "
                    + "AND r.businessProcess.name = :processName "
                    + "AND (r in elements (u.adminRoles) "
                    + "OR (r in elements (rpf.roles) "
                    + "AND p.name = :productName)) " + "ORDER BY r.id";

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.accesscontrol.RoleDaoBase#hasRole(java.lang.String,
     *      java.lang.String, java.lang.String, java.lang.String)
     */
    public Boolean hasRole(final String pLogin, final String pProcessName,
            final String pProductName, final String pRoleName) {
        final Query lQuery = createQuery(HAS_ROLE);

        lQuery.setParameter("login", pLogin);
        lQuery.setParameter("productName", pProductName);
        lQuery.setParameter("processName", pProcessName);
        lQuery.setParameter("roleName", pRoleName);

        return hasResult(lQuery);
    }

    private final static String DEFAULT_USER_ROLE =
            "SELECT r FROM " + Role.class.getName() + " r, "
                    + EndUser.class.getName() + " u "
                    + "LEFT JOIN u.rolesForProducts as rpf "
                    + "LEFT JOIN rpf.product as p WHERE u.login = :login "
                    + "AND r.businessProcess.name = :processName "
                    + "AND (r in elements (u.adminRoles) "
                    + "OR (r in elements (rpf.roles) "
                    + "AND p.name = :productName)) " + "ORDER BY r.id";

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.accesscontrol.RoleDaoBase#getDefaultRoleName(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    public String getDefaultRoleName(final String pLogin,
            final String pProcessName, final String pProductName) {
        final Query lQuery = createQuery(DEFAULT_USER_ROLE);

        lQuery.setParameter("login", pLogin);
        lQuery.setParameter("processName", pProcessName);
        lQuery.setParameter("productName", pProductName);

        final Role lDefaultRole = getFirst(lQuery);

        if (lDefaultRole == null) {
            return null;
        }
        else {
            return lDefaultRole.getName();
        }
    }

    private static final String IS_ROLE_EXISTS =
            "SELECT id FROM "
                    + Role.class.getName()
                    + " r "
                    + "WHERE r.name = :roleName AND r.businessProcess.name = :processName";

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.accesscontrol.RoleDaoBase#isRoleExists(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public Boolean isRoleExists(String pProcessName, String pRoleName) {
        final Query lQuery = createQuery(IS_ROLE_EXISTS);
        lQuery.setParameter("roleName", pRoleName);
        lQuery.setParameter("processName", pProcessName);
        return hasResult(lQuery);
    }

    /**
     * @see org.topcased.gpm.domain.accesscontrol.Role#getRole(java.lang.String,
     *      org.topcased.gpm.domain.businessProcess.BusinessProcess)
     */
    @SuppressWarnings("rawtypes")
    public org.topcased.gpm.domain.accesscontrol.Role getRole(
            final java.lang.String pName,
            final org.topcased.gpm.domain.businessProcess.BusinessProcess pBusinessProcess) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.accesscontrol.Role as role where role.name = :name and role.businessProcess.id = :businessProcessId";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("name", pName);
            lQueryObject.setParameter("businessProcessId",
                    pBusinessProcess.getId());
            java.util.List lResults = lQueryObject.list();
            org.topcased.gpm.domain.accesscontrol.Role lResult = null;
            if (lResults != null) {
                if (lResults.size() > 1) {
                    throw new org.springframework.dao.InvalidDataAccessResourceUsageException(
                            "More than one instance of 'org.topcased.gpm.domain.accesscontrol.Role"
                                    + "' was found when executing query --> '"
                                    + lQueryString + "'");
                }
                else if (lResults.size() == 1) {
                    lResult =
                            (org.topcased.gpm.domain.accesscontrol.Role) lResults.iterator().next();
                }
            }
            return lResult;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

}