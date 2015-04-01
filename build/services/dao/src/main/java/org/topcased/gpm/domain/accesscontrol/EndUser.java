/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 ******************************************************************/

package org.topcased.gpm.domain.accesscontrol;

/**
 * @author Atos
 */

@javax.persistence.Entity
@javax.persistence.Table(name = "END_USER")
@javax.persistence.Inheritance(strategy = javax.persistence.InheritanceType.JOINED)
@org.hibernate.annotations.Entity(dynamicInsert = false, dynamicUpdate = false)
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class EndUser extends
        org.topcased.gpm.domain.attributes.AttributesContainer {
    private static final long serialVersionUID = 6737183340414977827L;

    protected java.lang.String login;

    /**
     * 
     */
    @javax.persistence.Column(name = "LOGIN", length = 100, nullable = false, unique = false)
    @org.hibernate.annotations.Type(type = "java.lang.String")
    @org.hibernate.annotations.Index(name = "end_user_login_idx")
    public java.lang.String getLogin() {
        return this.login;
    }

    public void setLogin(java.lang.String pLogin) {
        this.login = pLogin;
    }

    protected java.lang.String passwd;

    /**
     * 
     */
    @javax.persistence.Column(name = "PASSWD", length = 100, nullable = false, unique = false)
    @org.hibernate.annotations.Type(type = "java.lang.String")
    public java.lang.String getPasswd() {
        return this.passwd;
    }

    public void setPasswd(java.lang.String pPasswd) {
        this.passwd = pPasswd;
    }

    protected java.lang.String name;

    /**
     * 
     */
    @javax.persistence.Column(name = "NAME", length = 100, unique = false)
    @org.hibernate.annotations.Type(type = "java.lang.String")
    public java.lang.String getName() {
        return this.name;
    }

    public void setName(java.lang.String pName) {
        this.name = pName;
    }

    protected java.lang.String email;

    /**
     * 
     */
    @javax.persistence.Column(name = "EMAIL", length = 100, unique = false)
    @org.hibernate.annotations.Type(type = "java.lang.String")
    public java.lang.String getEmail() {
        return this.email;
    }

    public void setEmail(java.lang.String pEmail) {
        this.email = pEmail;
    }

    protected java.lang.String forname;

    /**
     * 
     */
    @javax.persistence.Column(name = "FORNAME", length = 100, unique = false)
    @org.hibernate.annotations.Type(type = "java.lang.String")
    public java.lang.String getForname() {
        return this.forname;
    }

    public void setForname(java.lang.String pForname) {
        this.forname = pForname;
    }

    protected java.lang.String id;

    /**
     * 
     */

    protected java.util.Date lastConnection;

    /**
     * 
     */
    @javax.persistence.Column(name = "LAST_CONNECTION", unique = false)
    @org.hibernate.annotations.Type(type = "java.util.Date")
    public java.util.Date getLastConnection() {
        return this.lastConnection;
    }

    public void setLastConnection(java.util.Date pLastConnection) {
        this.lastConnection = pLastConnection;
    }
    
    protected java.util.Set<org.topcased.gpm.domain.accesscontrol.Role> roleList =
            new java.util.LinkedHashSet<org.topcased.gpm.domain.accesscontrol.Role>();

    /**
     * 
     */
    @javax.persistence.ManyToMany(fetch = javax.persistence.FetchType.LAZY, targetEntity = org.topcased.gpm.domain.accesscontrol.Role.class)
    @javax.persistence.JoinTable(name = "ADMIN_ROLES2END_USERS", joinColumns = @javax.persistence.JoinColumn(name = "END_USERS_FK"), inverseJoinColumns = @javax.persistence.JoinColumn(name = "ADMIN_ROLES_FK"))
    @org.hibernate.annotations.OrderBy(clause = "END_USERS_FK")
    @org.hibernate.annotations.ForeignKey(inverseName = "ROLE_END_USERS_FKC", name = "END_USER_ADMIN_ROLES_FKC")
    public java.util.Set<org.topcased.gpm.domain.accesscontrol.Role> getAdminRoles() {
        return this.roleList;
    }

    public void setAdminRoles(
            java.util.Set<org.topcased.gpm.domain.accesscontrol.Role> pAdminRoles) {
        this.roleList = pAdminRoles;
    }

    /**
     * Add a org.topcased.gpm.domain.accesscontrol.Role.
     * 
     * @param pAdminRoles
     *            the entity to add.
     */
    public void addToRoleList(
            org.topcased.gpm.domain.accesscontrol.Role pAdminRoles) {
        if (this.roleList == null) {
            this.roleList =
                    new java.util.LinkedHashSet<org.topcased.gpm.domain.accesscontrol.Role>();
        }
        this.roleList.add(pAdminRoles);
    }

    /**
     * Remove a org.topcased.gpm.domain.accesscontrol.Role.
     * 
     * @param pAdminRoles
     *            the entity to remove.
     */
    public void removeFromRoleList(
            org.topcased.gpm.domain.accesscontrol.Role pAdminRoles) {
        if (this.roleList != null) {
            this.roleList.remove(pAdminRoles);
        }
    }

    protected java.util.Set<org.topcased.gpm.domain.accesscontrol.RolesForProduct> rolesForProductList =
            new java.util.LinkedHashSet<org.topcased.gpm.domain.accesscontrol.RolesForProduct>();

    /**
     * 
     */
    @javax.persistence.OneToMany(cascade = { javax.persistence.CascadeType.ALL }, fetch = javax.persistence.FetchType.LAZY, targetEntity = org.topcased.gpm.domain.accesscontrol.RolesForProduct.class)
    @javax.persistence.JoinColumn(name = "END_USER_FK")
    @org.hibernate.annotations.OrderBy(clause = "END_USER_FK")
    @org.hibernate.annotations.ForeignKey(name = "END_USER_ROLES_FOR_PRODUCTS_FK")
    public java.util.Set<org.topcased.gpm.domain.accesscontrol.RolesForProduct> getRolesForProducts() {
        return this.rolesForProductList;
    }

    public void setRolesForProducts(
            java.util.Set<org.topcased.gpm.domain.accesscontrol.RolesForProduct> pRolesForProducts) {
        this.rolesForProductList = pRolesForProducts;
    }

    /**
     * Add a org.topcased.gpm.domain.accesscontrol.RolesForProduct.
     * 
     * @param pRolesForProducts
     *            the entity to add.
     */
    public void addToRolesForProductList(
            org.topcased.gpm.domain.accesscontrol.RolesForProduct pRolesForProducts) {
        if (this.rolesForProductList == null) {
            this.rolesForProductList =
                    new java.util.LinkedHashSet<org.topcased.gpm.domain.accesscontrol.RolesForProduct>();
        }
        this.rolesForProductList.add(pRolesForProducts);
    }

    /**
     * Remove a org.topcased.gpm.domain.accesscontrol.RolesForProduct.
     * 
     * @param pRolesForProducts
     *            the entity to remove.
     */
    public void removeFromRolesForProductList(
            org.topcased.gpm.domain.accesscontrol.RolesForProduct pRolesForProducts) {
        if (this.rolesForProductList != null) {
            this.rolesForProductList.remove(pRolesForProducts);
        }
    }

    protected java.util.Set<org.topcased.gpm.domain.search.FilterComponent> filterComponentList =
            new java.util.LinkedHashSet<org.topcased.gpm.domain.search.FilterComponent>();

    /**
     * 
     */
    @javax.persistence.OneToMany(cascade = { javax.persistence.CascadeType.ALL }, mappedBy = "endUser", fetch = javax.persistence.FetchType.LAZY, targetEntity = org.topcased.gpm.domain.search.FilterComponent.class)
    @org.hibernate.annotations.OrderBy(clause = "END_USER_FK")
    @org.hibernate.annotations.ForeignKey(name = "END_USER_FILTER_COMPONENTS_FKC")
    public java.util.Set<org.topcased.gpm.domain.search.FilterComponent> getFilterComponents() {
        return this.filterComponentList;
    }

    public void setFilterComponents(
            java.util.Set<org.topcased.gpm.domain.search.FilterComponent> pFilterComponents) {
        this.filterComponentList = pFilterComponents;
    }

    /**
     * Add a org.topcased.gpm.domain.search.FilterComponent.
     * 
     * @param pFilterComponents
     *            the entity to add.
     */
    public void addToFilterComponentList(
            org.topcased.gpm.domain.search.FilterComponent pFilterComponents) {
        if (this.filterComponentList == null) {
            this.filterComponentList =
                    new java.util.LinkedHashSet<org.topcased.gpm.domain.search.FilterComponent>();
        }
        this.filterComponentList.add(pFilterComponents);
    }

    /**
     * Remove a org.topcased.gpm.domain.search.FilterComponent.
     * 
     * @param pFilterComponents
     *            the entity to remove.
     */
    public void removeFromFilterComponentList(
            org.topcased.gpm.domain.search.FilterComponent pFilterComponents) {
        if (this.filterComponentList != null) {
            this.filterComponentList.remove(pFilterComponents);
        }
    }

    /**
     * Returns <code>true</code> if the argument is an EndUser instance and all
     * identifiers for this entity equal the identifiers of the argument entity.
     * The <code>equals</code> method of the parent entity will also need to
     * return <code>true</code>. Returns <code>false</code> otherwise.
     * 
     * @see org.topcased.gpm.domain.attributes.AttributesContainer#equals(Object)
     */
    @javax.persistence.Transient
    public boolean equals(Object pObject) {
        if (this == pObject) {
            return true;
        }
        if (!(pObject instanceof EndUser)) {
            return false;
        }
        final EndUser lEndUser = (EndUser) pObject;
        if (this.getId() == null || lEndUser.getId() == null
                || !lEndUser.getId().equals(getId())) {
            return false;
        }
        return true;
    }

    /**
     * Returns a hash code based on this entity's identifiers and the hash code
     * of the parent entity.
     * 
     * @return a hash code value for this object.
     * @see org.topcased.gpm.domain.attributes.AttributesContainer#hashCode()
     */
    @javax.persistence.Transient
    public int hashCode() {
        int lHashCode = super.hashCode();
        int lId = 0;
        if (id != null) {
            lId = id.hashCode();
        }
        lHashCode = HASHCODE_CONSTANT * lHashCode + lId;

        return lHashCode;
    }

    /**
     * Constructs new instances of
     * {@link org.topcased.gpm.domain.accesscontrol.EndUser}.
     * 
     * @return a new instance of
     *         {@link org.topcased.gpm.domain.accesscontrol.EndUser}
     */
    public static org.topcased.gpm.domain.accesscontrol.EndUser newInstance() {
        return new org.topcased.gpm.domain.accesscontrol.EndUser();
    }
}