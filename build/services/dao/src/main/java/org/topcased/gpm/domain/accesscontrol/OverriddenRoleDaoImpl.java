/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 ******************************************************************/

package org.topcased.gpm.domain.accesscontrol;

import java.util.List;

import org.hibernate.Query;

/**
 * OverriddenRole DAO implementration
 * 
 * @author ogehin
 */
public class OverriddenRoleDaoImpl
        extends
        org.topcased.gpm.domain.AbstractDao<org.topcased.gpm.domain.accesscontrol.OverriddenRole, java.lang.String>
        implements org.topcased.gpm.domain.accesscontrol.OverriddenRoleDao {

    /**
     * Constructor
     */
    public OverriddenRoleDaoImpl() {
        super(org.topcased.gpm.domain.accesscontrol.OverriddenRole.class);
    }

    /**
     * Get the overridden role name.
     * 
     * @param pContainerId
     *            Container identifier
     * @param pUserLogin
     *            User login
     * @param pOldRole
     *            old role name
     * @return the overridden role name.
     */
    public String getOverriddenRoleName(final String pContainerId,
            final String pUserLogin, final String pOldRole) {
        final Query lQuery =
                getSession(false).createQuery(
                        "select newRole.name "
                                + " from org.topcased.gpm.domain.accesscontrol.OverriddenRole as overriddenRole"
                                + " left join overriddenRole.newRole as newRole"
                                + " where overriddenRole.endUser.login = :userLogin"
                                + " and overriddenRole.oldRole.name = :oldRole"
                                + " and overriddenRole.valuesContainer.id = :containerId");

        lQuery.setParameter("userLogin", pUserLogin);
        lQuery.setParameter("containerId", pContainerId);
        lQuery.setParameter("oldRole", pOldRole);

        return (String) lQuery.uniqueResult();
    }

    /**
     * Get the overridden role entity.
     * 
     * @param pContainerId
     *            Container identifier
     * @param pUserLogin
     *            User login
     * @param pOldRole
     *            old role name
     * @return the overridden role entity.
     */
    public OverriddenRole getOverriddenRole(final String pContainerId,
            final String pUserLogin, final String pOldRole) {
        final Query lQuery =
                getSession(false).createQuery(
                        "select overriddenRole "
                                + " from org.topcased.gpm.domain.accesscontrol.OverriddenRole as overriddenRole"
                                + " where overriddenRole.endUser.login = :userLogin"
                                + " and overriddenRole.oldRole.name = :oldRole"
                                + " and overriddenRole.valuesContainer.id = :containerId");

        lQuery.setParameter("userLogin", pUserLogin);
        lQuery.setParameter("containerId", pContainerId);
        lQuery.setParameter("oldRole", pOldRole);

        return (OverriddenRole) lQuery.uniqueResult();
    }

    /**
     * Get the overridden role entities list for an expected user.
     * 
     * @param pUserLogin
     *            User login
     * @return the overridden role entities list.
     */
    @SuppressWarnings("unchecked")
    public List<OverriddenRole> getOverriddenRolesFromUserLogin(
            final String pUserLogin) {
        final Query lQuery =
                getSession(false).createQuery(
                        "select overriddenRole "
                                + " from org.topcased.gpm.domain.accesscontrol.OverriddenRole as overriddenRole"
                                + " where overriddenRole.endUser.login = :userLogin");
        lQuery.setParameter("userLogin", pUserLogin);
        return (List<OverriddenRole>) lQuery.list();
    }

    /**
     * Get the overridden role entities list for an expected container.
     * 
     * @param pContainerId
     *            Container identifier
     * @return the overridden role entities list.
     */
    @SuppressWarnings("unchecked")
    public List<OverriddenRole> getOverriddenRolesFromContainerId(
            final String pContainerId) {
        final Query lQuery =
                getSession(false).createQuery(
                        "select overriddenRole "
                                + " from org.topcased.gpm.domain.accesscontrol.OverriddenRole as overriddenRole"
                                + " where overriddenRole.valuesContainer.id = :containerId");
        lQuery.setParameter("containerId", pContainerId);
        return (List<OverriddenRole>) lQuery.list();
    }

    /**
     * Get the overridden role entities list for an expected role.
     * 
     * @param pRoleName
     *            The role name
     * @return the overridden role entities list.
     */
    @SuppressWarnings("unchecked")
    public List<OverriddenRole> getOverriddenRolesFromRoleName(String pRoleName) {
        final Query lQuery =
                getSession(false).createQuery(
                        "select overriddenRole "
                                + "from org.topcased.gpm.domain.accesscontrol.OverriddenRole "
                                + "as overriddenRole "
                                + "where overriddenRole.newRole.name = :roleName "
                                + "or overriddenRole.oldRole.name = :roleName");
        lQuery.setParameter("roleName", pRoleName);
        return (List<OverriddenRole>) lQuery.list();
    }
}