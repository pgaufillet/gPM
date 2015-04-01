/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.authorization.service;

import java.util.Collection;
import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.topcased.gpm.business.authorization.impl.filter.FilterAccessContraint;
import org.topcased.gpm.business.authorization.impl.filter.FilterAccessControl;
import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.exception.InvalidIdentifierException;
import org.topcased.gpm.business.exception.InvalidNameException;
import org.topcased.gpm.business.exception.InvalidTokenException;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.fields.FieldAccessData;
import org.topcased.gpm.business.fields.impl.CacheableFieldsContainer;
import org.topcased.gpm.business.product.impl.CacheableProduct;
import org.topcased.gpm.business.product.impl.ProductTreeNode;
import org.topcased.gpm.business.search.service.FilterScope;
import org.topcased.gpm.business.serialization.data.FilterAccessCtl;
import org.topcased.gpm.business.serialization.data.Product;
import org.topcased.gpm.business.serialization.data.User;
import org.topcased.gpm.common.accesscontrol.Roles;
import org.topcased.gpm.domain.accesscontrol.EndUser;
import org.topcased.gpm.domain.accesscontrol.Role;

/**
 * Authorization service interface.
 * 
 * @author llatil
 */
@Transactional(propagation = Propagation.REQUIRED)
public interface AuthorizationService {

    /** Name of the static gPM admin role. */
    public static final String ADMIN_ROLE_NAME = "admin";

    /** Default Admin login. */
    public static final String ADMIN_LOGIN = "admin";

    /**
     * AuthorizationService.ROLE_ON_ONE_PRODUCT static value for method
     * getUsersWithRole
     */
    public static String ROLE_ON_ONE_PRODUCT = "ROLE_ON_ONE_PRODUCT";

    /**
     * AuthorizationService.ROLE_ON_ALL_PRODUCTS static value for method
     * getUsersWithRole
     */
    public static String ROLE_ON_ALL_PRODUCTS = "ROLE_ON_ALL_PRODUCTS";

    /**
     * Login a user, and begin a GDM session.
     * 
     * @param pUsername
     *            login name of the user
     * @param pPasswd
     *            User password
     * @return token used to identify the user, null otherwise
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public String login(String pUsername, String pPasswd);

    /**
     * Login a user, and begin a GDM session.
     * 
     * @param pUsername
     *            login name of the user
     * @param pPasswd
     *            User password
     * @param pSessionTimeMax
     *            The time max of session (-1 for permanent session)
     * @return token used to identify the user, null otherwise
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public String login(String pUsername, String pPasswd, long pSessionTimeMax);

    /**
     * Logout a logged on user. This closes the GDM session for this user.
     * 
     * @param pUserToken
     *            Token identifying the user session
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public void logout(String pUserToken);

    /**
     * Create a new user.
     * 
     * @param pToken
     *            Session token
     * @param pUserData
     *            Information on the user to create
     * @param pPasswd
     *            Password for this user.
     * @param pContext
     *            The context. Must contain the key "passwordEncoding" and a
     *            value from
     *            {@link org.topcased.gpm.business.authorization.service.PasswordEncoding}
     * @return User technical identifier
     * @throws InvalidNameException
     *             The login of the user data or the password is null.
     */
    public String createUser(String pToken, EndUserData pUserData,
            String pPasswd, Context pContext) throws InvalidNameException;

    /**
     * Create the administrator user. This method can be used only to create
     * initial admin user on a new gPM database. An exception is raised if the
     * admin user already exists.
     * 
     * @param pAdminPassword
     *            Password to set for the administrator
     * @return Admin user technical identifier
     * @throws AuthorizationException
     *             If the admin user already exists in database
     */
    public String createAdminUser(String pAdminPassword)
        throws AuthorizationException;

    /**
     * Update the information of an user.
     * 
     * @param pRoleToken
     *            Role session token.
     * @param pUserData
     *            Information on the user to create. The login value specified
     *            in this structure must already exist in the database.
     * @param pPasswd
     *            Password for this user. (use an empty string to keep it
     *            unchanged)
     * @param pContext
     *            The context
     * @return User technical identifier
     */
    public String updateUser(String pRoleToken, EndUserData pUserData,
            String pPasswd, Context pContext);

    /**
     * Create (or update) the information of an user.
     * 
     * @param pRoleToken
     *            Role session token.
     * @param pUserData
     *            Information on the user to create. The login value specified
     *            in this structure must already exist in the database.
     * @param pPasswd
     *            Password for this user. (use an empty string to keep it
     *            unchanged)
     * @param pContext
     *            The context. Must contain the key "passwordEncoding" and a
     *            value from
     *            {@link org.topcased.gpm.business.authorization.service.PasswordEncoding}
     * @return User technical identifier
     */
    public String createOrUpdateUser(String pRoleToken, EndUserData pUserData,
            String pPasswd, Context pContext);

    /**
     * Remove a user from the database.
     * 
     * @note Removing a user will prevent this user from login in the
     *       application, but if she is already logged in, her session remains
     *       valid (until logout).
     * @param pRoleToken
     *            Role session token.
     * @param pLogin
     *            Login name of the user to remove.
     */
    public void removeUser(String pRoleToken, String pLogin);

    /**
     * Get information on the currently logged user
     * 
     * @param pToken
     *            Token identifying the user or role session.
     *            
     * @deprecated
     * @see AuthorizationService#getLoggedUserData(String, boolean)
     *            
     * @return User info
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public EndUserData getLoggedUserData(String pToken);

    /**
     * Get information on the currently logged user
     * 
     * @param pUserToken
     *            Token identifying the user or role session.
     * @param pLightUserData
     *            This parameter has a great impact on performances. The impact
     *            will depend on the size of the array
     *            <ul>
     *            <li><b>true</b> : EndUserData without EndUserData attributes</li>
     *            <li><b>false</b> : complete EndUserData</li>
     *            </ul>
     *            
     * @return User info
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public EndUserData getLoggedUserData(String pUserToken, boolean pLightUserData);
    
    /**
     * Get a session attribute.
     * 
     * @param pUserToken
     *            User session token
     * @param pAttrName
     *            Attribute name to get
     * @return The value of the attribute, or null if undefined.
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Object getSessionAttribute(String pUserToken, String pAttrName);

    /**
     * Set a session attribute. The attribute value is stored in session only
     * (transient), and not persisted in the database.
     * 
     * @param pUserToken
     *            User session token
     * @param pAttrName
     *            Attribute name
     * @param pValue
     *            Attribute value
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public void setSessionAttribute(String pUserToken, String pAttrName,
            Object pValue);

    /**
     * Get information on a user.
     * 
     * @param pLoginName
     *            Login name of the user
     * @return User info or null if login does not exist
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public EndUserData getUserData(String pLoginName);

    /**
     * Get information on a user from this roleToken.
     * 
     * @param pRoleToken
     *            roleToken
     * @return User object.
     * @throws InvalidNameException
     *             The login is unknown.
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public org.topcased.gpm.business.serialization.data.User getUserFromRole(
            String pRoleToken);

    /**
     * Get the list of users with a given role (either application role or role
     * on product if a product is specified)
     * 
     * @param pBusinessProcessName
     *            processName
     * @param pRoleName
     *            roleName
     * @param pProductName
     *            productName (can be null)
     * @return a list of user logins.
     * @throws GDMException
     *             when roleName is invalid.
     */
    public List<String> getUsersWithRole(String pBusinessProcessName,
            String pRoleName, String pProductName) throws GDMException;

    /**
     * Get the list of users with a given role on a given list of products The
     * pRoleProperties parameters impacts the results :
     * <ul>
     * <li> {@link Roles#INSTANCE_ROLE} : get users with role roleName on
     * instance</li>
     * <li> {@link Roles#PRODUCT_ROLE} | {@link Roles#ROLE_ON_ONE_PRODUCT} : get
     * users with role roleName on at least one of the products from specified
     * product list</li>
     * <li> {@link Roles#PRODUCT_ROLE} | {@link Roles#ROLE_ON_ALL_PRODUCTS} : get
     * users with role roleName on each of the products from specified product
     * list</li>
     * <li> {@link Roles#INSTANCE_ROLE} | {@link Roles#PRODUCT_ROLE} |
     * {@link Roles#ROLE_ON_ONE_PRODUCT} : get users with role roleName on at
     * least one of the products from specified product list, or on instance</li>
     * <li> {@link Roles#INSTANCE_ROLE} | {@link Roles#PRODUCT_ROLE} |
     * {@link Roles#ROLE_ON_ALL_PRODUCTS} : get users with role roleName on each
     * of the products from specified product list, or on instance</li>
     * </ul>
     * 
     * @param pBusinessProcessName
     *            processName
     * @param pRoleName
     *            roleName
     * @param pProductNames
     *            A list of product names (can be null)
     * @param pRoleProperties
     *            integer that is a combination of following constants from
     *            class {@link Roles}
     * @return the list of user logins
     * @throws GDMException
     *             when roleName is invalid.
     */
    public List<String> getUsersWithRole(String pBusinessProcessName,
            String pRoleName, String[] pProductNames, int pRoleProperties)
        throws GDMException;

    /**
     * Get a list of users from their logins.
     * 
     * @param pLogins
     *            a list of logins
     * @param pCaseSensitive
     *            <ul>
     *            <li><b>true</b> : login policy is case sensitive</li>
     *            <li><b>false</b> : otherwise</li>
     *            </ul>
     * @param pLightEndUserData
     *            This parameter has a great impact on performances. The impact
     *            will depend on the size of the array
     *            <ul>
     *            <li><b>true</b> : EndUserData without EndUserData attributes</li>
     *            <li><b>false</b> : complete EndUserData</li>
     *            </ul>
     * @return a list of EndUserData or an empty list if there is no
     *         EndUserData.
     * @throws GDMException
     */
    public List<EndUserData> getUsersFromLogins(final List<String> pLogins,
            final boolean pCaseSensitive, final boolean pLightEndUserData)
        throws GDMException;

    /**
     * Get information on all users.
     * 
     * @return Array of user information for all users.
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public EndUserData[] getAllUserData();

    /**
     * Get information on all users. <br>
     * <br>
     * <b>Deprecated because</b> this method gets all informations for all users
     * that is very slow with a large amount of users <br>
     * use <i>getUsers()</i> method instead to get a fast loaded user list
     * 
     * @return List of user data for all users.
     */
    @Deprecated
    // XXX Do not remove in 3.0 as it is still used by webservices
    public Collection<User> getAllUsers();

    /**
     * Get information on a user.
     * 
     * @param pLoginName
     *            Login name of the user
     * @return User info
     */
    public User getUser(String pLoginName);

    /**
     * Get the user session token from a role session token
     * 
     * @param pRoleToken
     *            Role session
     * @return User session token
     */
    public String getUserSessionFromRoleSession(String pRoleToken);

    /**
     * Get logins of all users
     * 
     * @return List of user login
     */
    public List<String> getUserLogins();

    /**
     * Get a light list of users (for list display)
     * 
     * @return the list of all users with minimal informations (login, name,
     *         forname)
     */
    public List<User> getUsers();

    /**
     * Get the list of available business processes for the user
     * 
     * @param pUserToken
     *            Token identifying the user session
     * @return List of business processes
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Collection<String> getBusinessProcessNames(String pUserToken);

    /**
     * Get the list of product names for a given business process. This list
     * contains only products where the user has a role.
     * 
     * @param pUserToken
     *            Token identifying the user session
     * @param pBusinessProcess
     *            Business process the returned products belong to.
     * @return List of product data.
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<String> getProductNames(String pUserToken,
            String pBusinessProcess);

    /**
     * Get the list of product names for a given business process. This list
     * contains only products where the user has a role. Only name and
     * description are filled in return products
     * 
     * @param pUserToken
     *            Token identifying the user session
     * @param pBusinessProcess
     *            Business process the returned products belong to.
     * @return List of products.
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<CacheableProduct> getProductNamesAndDescriptions(
            String pUserToken, String pBusinessProcess);

    /**
     * Get the list of product names for a given business process.
     * 
     * @param pBusinessProcess
     *            Business process the returned products belong to.
     * @return List of product names.
     */
    public List<String> getAllProductNames(String pBusinessProcess);

    /**
     * Get the list of products available for a given business process. This
     * list contains only products where given user has a role.
     * 
     * @param pUserToken
     *            Token identifying the user session
     * @param pBusinessProcess
     *            Business process the returned products belong to.
     * @return List of product.
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Collection<Product> getSerializableProducts(String pUserToken,
            String pBusinessProcess);

    /**
     * Get the list of available ProductTreeNode for a given business process.
     * The list contains products where given user has a role, and also parent
     * node for which at least a child has a role.
     * 
     * @param pUserToken
     *            Token identifying the user session
     * @param pBusinessProcess
     *            Business process the returned products belong to.
     * @return List of Product tree node.
     */
    public List<ProductTreeNode> getProductsAsTree(String pUserToken,
            String pBusinessProcess);

    /**
     * Set the value of a user attribute
     * 
     * @param pEndUserData
     *            the user data
     * @param pAttributeName
     *            the attribute name
     * @param pValue
     *            the attribute value
     */
    public void setValueToUserAttribute(EndUserData pEndUserData,
            String pAttributeName, String pValue);

    /**
     * Get the value of a userAttribute from its name
     * 
     * @param pEndUserData
     *            the user data
     * @param pAttributeName
     *            the attribute name
     * @return the value of this attribute
     */
    public String getValueOfUserAttribute(EndUserData pEndUserData,
            String pAttributeName);

    /**
     * Get the list of user role names allowed for a given login
     * 
     * @param pUserToken
     *            Authorization token.
     * @param pBusinessProcessName
     *            Name of business process
     * @param pProductName
     *            Name of the product.
     * @return List of allowed role names.
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Collection<String> getRolesNames(String pUserToken,
            String pBusinessProcessName, String pProductName);

    /**
     * Get the list of user role names allowed for a given user
     * 
     * @param pUserToken
     *            Authorization token.
     * @param pBusinessProcessName
     *            Name of business process
     * @param pProductName
     *            Name of the product.
     * @param pRoleProperties
     *            Properties indicating if the role to find are on instance/on
     *            product/on both
     * @return List of allowed role names.
     */
    public Collection<String> getRolesNames(String pUserToken,
            String pBusinessProcessName, String pProductName,
            RoleProperties pRoleProperties);

    /**
     * Select an administrator role.
     * 
     * @param pUserToken
     *            Authorization token.
     * @return List of allowed admin role names.
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Collection<String> getAdminRolesNames(String pUserToken);

    /**
     * Select an user role.
     * 
     * @param pUserToken
     *            Token identifying the user session
     * @param pRoleName
     *            Role name
     * @param pProductName
     *            Product name, or null to select an admin role.
     * @param pBusinessProcessName
     *            Business process name
     * @return Role session token.
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public String selectRole(String pUserToken, String pRoleName,
            String pProductName, String pBusinessProcessName);

    /**
     * Closes the given role token session
     * 
     * @param pRoleToken
     *            the role
     */
    public void closeRoleSession(String pRoleToken);

    /**
     * Get the access control to a field of a sheet or link, according to the
     * user's role rights, and to the context.
     * 
     * @param pRoleToken
     *            Role session token.
     * @param pAccessControlContextData
     *            Context of access context recovery.
     * @param pFieldId
     *            Field definition identifier
     * @return The access rights to the Field, or access with field rights
     *         values (default values).
     * @throws InvalidTokenException
     *             If the token is not valid.
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public FieldAccessControlData getFieldAccessControl(
            final String pRoleToken,
            final AccessControlContextData pAccessControlContextData,
            final String pFieldId) throws InvalidTokenException;

    /**
     * Get the access control to a sheet, according to the user's role rights,
     * and to a given state of a sheet.
     * 
     * @param pRoleToken
     *            Role session token.
     * @param pAccessControlContextData
     *            Context of access context recovery.
     * @return The access rights to the SheetType instance, or null if no
     *         explicit authorization.
     * @throws InvalidTokenException
     *             If the token is not valid.
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public TypeAccessControlData getTypeAccessControl(String pRoleToken,
            final AccessControlContextData pAccessControlContextData)
        throws InvalidTokenException;

    /**
     * Get the access control to a sheet, according to the user's role rights,
     * and to a given state of a sheet.
     * 
     * @param pRoleToken
     *            Role session token.
     * @param pProcessName
     *            the business process name
     * @param pProductName
     *            the product name
     * @param pStateName
     *            Name of the sheet state.
     * @param pSheetTypeId
     *            Sheet type ident.
     * @return The access rights to the SheetType instance, or null if no
     *         explicit authorization.
     * @deprecated Since 1.7
     * @see AuthorizationService#getTypeAccessControl(String,
     *      AccessControlContextData)
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public TypeAccessControlData getTypeAccessControl(String pRoleToken,
            String pProcessName, String pProductName, String pStateName,
            String pSheetTypeId);

    /**
     * Gets the access control defined for a given sheet. Access control are
     * defined only for a type. This method verify if the given sheet id
     * references a sheet.
     * 
     * @see AuthorizationService#getTypeAccessControl(String,
     *      AccessControlContextData)
     * @param pRoleToken
     *            the role session token
     * @param pAccessControlContextData
     *            Context of access context recovery.
     * @param pSheetId
     *            the sheet identifier
     * @return The access rights to the type of this sheet instance, or null if
     *         it's not a sheet.
     * @throws InvalidTokenException
     *             If the role token is not valid.
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public TypeAccessControlData getSheetAccessControl(final String pRoleToken,
            final AccessControlContextData pAccessControlContextData,
            final String pSheetId) throws InvalidTokenException;

    /**
     * Gets the access control defined for a given sheet
     * 
     * @param pRoleToken
     *            the role session token
     * @param pSheetId
     *            the sheet identifier
     * @return The access rights to the type of this sheet instance, or null if
     *         no explicit authorization.
     * @deprecated Since 1.7
     * @see AuthorizationService#getSheetAccessControl(String,
     *      AccessControlContextData, String)
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public TypeAccessControlData getSheetAccessControl(String pRoleToken,
            String pSheetId);

    /**
     * Get the access control to a transition.
     * 
     * @param pRoleToken
     *            Role session token.
     * @param pAccessControlContextData
     *            Context of access context recovery.
     * @param pTransitionName
     *            Name of the transition.
     * @return access control to transition
     * @throws InvalidTokenException
     *             If token is not valid.
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public TransitionAccessControlData getTransitionAccessControl(
            String pRoleToken,
            AccessControlContextData pAccessControlContextData,
            String pTransitionName) throws InvalidTokenException;

    /**
     * Get the access control to an administration action.
     * 
     * @param pRoleToken
     *            Role session token.
     * @param pAccessControlContextData
     *            Context of access context recovery.
     * @param pActionKey
     *            Key of the action.
     * @return Admin Access Control Data
     * @throws IllegalArgumentException
     *             If pActionKey is blank.
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public AdminAccessControlData getAdminAccessControl(String pRoleToken,
            AccessControlContextData pAccessControlContextData,
            String pActionKey) throws IllegalArgumentException;

    /**
     * Check if an admin access is defined for role defined by roleToken on a
     * specified action on total instance.
     * 
     * @param pRoleToken
     *            Role session token.
     * @param pActionKey
     *            Key of the action.
     * @return true is an admin access is defined on instance for the given role
     *         on specified action.
     */
    public boolean isAdminAccessDefinedOnInstance(String pRoleToken,
            String pActionKey);

    /**
     * Check if an admin access is defined for role defined by roleToken on a
     * specified action on a specified product
     * 
     * @param pRoleToken
     *            Role session token.
     * @param pActionKey
     *            Key of the action.
     * @param pProductName
     *            Name of the product
     * @return true if an admin access is set on this product for the given role
     *         on specified action.
     */
    public boolean isAdminAccessDefinedOnProduct(String pRoleToken,
            String pActionKey, String pProductName);
    
    /**
     * Check if an admin access is defined for specified role on a
     * specified action on a specified product
     * 
     * @param pRoleToken
     *            Role session token.
     * @param pActionKey
     *            Key of the action.
     * @param pProductName
     *            Name of the product
     * @return true if an admin access is set on this product for the given role
     *         on specified action.
     */
    public boolean isAdminAccessDefinedOnProduct(String pRoleToken,
            String pActionKey, String pProductName, String pRole);

    /**
     * Get the access control to an application action.
     * 
     * @param pRoleToken
     *            Role session token.
     * @param pAccessControlContextData
     *            Context of access context recovery.
     * @param pActionKey
     *            Key of the action.
     * @return Control access data
     * @throws IllegalArgumentException
     *             If pActionKey is blank.
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public ActionAccessControlData getApplicationActionAccessControl(
            String pRoleToken,
            AccessControlContextData pAccessControlContextData,
            String pActionKey) throws IllegalArgumentException;

    /**
     * Get the access control to a list of application actions.
     * 
     * @param pRoleToken
     *            Role session token.
     * @param pAccessControlContextData
     *            Context of access context recovery.
     * @param pActionKeys
     *            List of action key.
     * @return List of control access data for all action keys (this list is
     *         ordered according to the pActionKeys list)
     * @throws IllegalArgumentException
     *             If pActionKey is blank.
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<ActionAccessControlData> getApplicationActionAccessControl(
            String pRoleToken,
            AccessControlContextData pAccessControlContextData,
            List<String> pActionKeys) throws IllegalArgumentException;

    /**
     * Get the access control to an application action.
     * <p>
     * Set fields container and sheet's state
     * 
     * @param pRoleToken
     *            Role session token.
     * @param pSheetId
     *            Identifier of the sheet. (or 0 if unknown)
     * @param pActionKey
     *            Key of the action.
     * @return Control access data
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public ActionAccessControlData getApplicationActionAccessControl(
            String pRoleToken, String pSheetId, String pActionKey);

    /**
     * Get the access control to a list of application actions.
     * <p>
     * Set fields container and sheet's state
     * 
     * @param pRoleToken
     *            Role session token.
     * @param pSheetId
     *            Identifier of the sheet. (or 0 if unknown)
     * @param pActionKeys
     *            List of action key.
     * @return List of control access data for all action keys (this list is
     *         ordered according to the pActionKeys list)Key of the action.
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<ActionAccessControlData> getApplicationActionAccessControl(
            String pRoleToken, String pSheetId, List<String> pActionKeys);

    /**
     * Add an administration role to a user. The role, and the related business
     * process must exist before calling this method.
     * 
     * @param pRoleToken
     *            Role session token.
     * @param pLoginName
     *            Login of the user.
     * @param pBusinessProcessName
     *            Business process name.
     * @param pRoleName
     *            Role to give to this user.
     */
    public void addRole(String pRoleToken, String pLoginName,
            String pBusinessProcessName, String pRoleName);

    /**
     * Add a role to a user for a specific product. The role, product, and the
     * related business process must exist before calling this method.
     * 
     * @param pRoleToken
     *            Role session token.
     * @param pLoginName
     *            Login of the user.
     * @param pBusinessProcessName
     *            Business process name.
     * @param pRoleName
     *            Role to give to this user.
     * @param pProductName
     *            Name of the product.
     */
    public void addRole(String pRoleToken, String pLoginName,
            String pBusinessProcessName, String pRoleName, String pProductName);

    /**
     * Create a new role for a given business process.
     * <p>
     * If the role already exists, this method returns without error.
     * 
     * @param pToken
     *            User or role session token (need global admin privilege)
     * @param pRoleName
     *            Name of the role to create
     * @param pBusinessProcessName
     *            Name of business process
     */
    public void createRole(String pToken, String pRoleName,
            String pBusinessProcessName);

    /**
     * Check if the given role session is administrator
     * 
     * @param pRoleToken
     *            Role token session
     * @return True if the role has administrator rights
     */
    public boolean hasAdminAccess(String pRoleToken);

    /**
     * Checks if the given role is an application admin role (ie not restricted
     * to a product)
     * 
     * @param pRoleToken
     *            the role token
     * @return true if the role token references a global admin role.
     */
    public boolean hasGlobalAdminRole(String pRoleToken);

    /**
     * Delete a role. The role to be deleted must already exist in the database.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pRoleName
     *            Name of the role to remove
     * @param pBusinessProcessName
     *            Name of business process
     */
    public void deleteRole(String pRoleToken, String pRoleName,
            String pBusinessProcessName);

    /**
     * Get all role names for a given business process.
     * 
     * @param pBusinessProcessName
     *            Business process name.
     * @return List of role names.
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Collection<String> getRolesNames(String pBusinessProcessName);

    /**
     * Get information on all roles defined for a given user
     * 
     * @param pLoginName
     *            Login name of the user
     * @param pBusinessProcessName
     *            Name of the business process
     * @return List of role data
     * @deprecated
     * @since 2.0
     * @see AuthorizationService#getRoles(String, String, String,
     *      RoleProperties)
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Collection<RoleData> getRoles(String pLoginName,
            String pBusinessProcessName);

    /**
     * Get the list of user role allowed for a given user
     * 
     * @param pUserToken
     *            Authorization token.
     * @param pBusinessProcessName
     *            Name of business process
     * @param pProductName
     *            Name of the product.
     * @param pRoleProperties
     *            Properties indicating if the role to find are on instance/on
     *            product/on both
     * @return List of allowed roles.
     */
    public Collection<RoleData> getRoles(String pUserToken,
            String pProductName, String pBusinessProcessName,
            RoleProperties pRoleProperties);

    /**
     * Get the role entity from role session token.
     * 
     * @param pRoleToken
     *            Role session token
     * @return Role or null if not found.
     * @throws InvalidTokenException
     *             The role token is invalid.
     */
    public Role getRole(String pRoleToken);

    /**
     * Get the role name from role session token.
     * 
     * @param pRoleToken
     *            Role session token
     * @return Role UUID.
     * @throws InvalidTokenException
     *             The role token is invalid.
     */
    public String getRoleNameFromToken(String pRoleToken);

    /**
     * Set all roles defined for a given user. The already existing roles of the
     * user are replaced by the roles of the list.
     * 
     * @param pRoleToken
     *            Role session token.
     * @param pLoginName
     *            Login name of the user
     * @param pBusinessProcessName
     *            Name of the business process
     * @param pNewRoles
     *            List of previous roles (used for extension point, nullable if not needed)
     * @param pNewRoles
     *            List of role to define for the user (replace all existing roles of the user)
     */
    public void setRoles(String pRoleToken, String pLoginName, String pBusinessProcessName,
    		Collection<RoleData> pOldRoles, Collection<RoleData> pNewRoles, Context pContext);

    /**
     * Add the defined role on the given product (or on instance if no product)
     * for a list of users.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pLoginNames
     *            Login names of the users
     * @param pRoleName
     *            Name of the role to set
     * @param pProductName
     *            Name of the product on which this role must be set (or null if
     *            instance role)
     * @param pBusinessProcessName
     *            Name of the business process
     */
    public void addRoleForUsers(String pRoleToken, String[] pLoginNames,
            String pRoleName, String pProductName, String pBusinessProcessName);

    /**
     * Remove the defined role on the given product (or on instance if no
     * product) for a list of users.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pLoginNames
     *            Login names of the users
     * @param pRoleName
     *            Name of the role to remove
     * @param pProductName
     *            Name of the product on which this role must be removed (or
     *            null if instance role)
     * @param pBusinessProcessName
     *            Name of the business process
     */
    public void removeRoleForUsers(String pRoleToken, String[] pLoginNames,
            String pRoleName, String pProductName, String pBusinessProcessName);

    /**
     * Define an access control on a field.
     * 
     * @param pAccessControl
     *            Define the access control for the field.
     */
    public void setFieldAccessControl(FieldAccessControlData pAccessControl);

    /**
     * Define an access control on a sheet type.
     * 
     * @param pAccessControl
     *            Define the access control for the sheet type.
     */
    public void setSheetTypeAccessControl(TypeAccessControlData pAccessControl);

    /**
     * Define an access control on a container type.
     * 
     * @param pAccessControlData
     *            Define the access control for the container type.
     */
    public void setTypeAccessControl(TypeAccessControlData pAccessControlData);

    /**
     * Define an access control on an application action.
     * 
     * @param pAccessControl
     *            Define the access control for the application action.
     */
    public void setApplicationActionAccessControl(
            ActionAccessControlData pAccessControl);

    /**
     * Define an admin access control on an administration action
     * 
     * @param pAccessControl
     *            Define the access control for the admin action
     */
    public void setAdminAccessControl(AdminAccessControlData pAccessControl);

    /**
     * Define an access control on a transition.
     * 
     * @param pAccessControl
     *            Define the access control for the transition.
     */
    public void setTransitionAccessControl(
            TransitionAccessControlData pAccessControl);

    /**
     * Check if a token is a valid role token
     * 
     * @param pToken
     *            the token to check
     */
    public void validateRoleToken(String pToken);

    /**
     * Check that the login/password combination is valid
     * 
     * @param pLogin
     *            The login
     * @param pPassword
     *            The password
     */
    public EndUser validateLogin(String pLogin, String pPassword);

    /**
     * Get the access control to a field of a sheet on a container, according to
     * the user's role rights, and to a given state of a sheet. The current
     * field access is specified so that it will not be overridden.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pStateName
     *            Name of the current sheet state.
     * @param pFieldId
     *            Field definition identifier
     * @param pContainerId
     *            Container definition identifier
     * @param pVisibleTypeId
     *            The sheet type in which the link field should be displayed or
     *            null
     * @return The access rigths to the field, or null if no explicit
     *         authorization.
     * @deprecated Since 1.7
     * @see AuthorizationService#getFieldAccess(String,
     *      AccessControlContextData, String)
     */
    public FieldAccessData getFieldAccess(String pRoleToken, String pStateName,
            String pFieldId, String pContainerId, String pVisibleTypeId);

    /**
     * Get the access control to a field of a sheet, according to the user's
     * role rights, and to a given state of a sheet.
     * 
     * @param pRoleToken
     *            Role token
     * @param pAccessControlContextData
     *            Context to get field access control
     * @param pFieldId
     *            Field UUID
     * @return The access rights to the Field, or null if no explicit
     *         authorization.
     * @throws InvalidTokenException
     *             The role token is blank.
     */
    public FieldAccessData getFieldAccess(String pRoleToken,
            AccessControlContextData pAccessControlContextData, String pFieldId)
        throws InvalidTokenException;

    /**
     * Apply field access controls on a fields container
     * <p>
     * For each field of the fields container, merge contextual access controls
     * with the default field attributes (mandatory, confidential, updatable,
     * exportable). If the access control contains extended attributes, they are
     * transmitted to the field.
     * 
     * @param pRoleToken
     *            current session token
     * @param pAccessControlContext
     *            Access control to apply
     * @param pFieldsContainer
     *            the fields container, modified by this method
     * @deprecated see getCacheableFieldsContainer
     */
    public void applyAccessControls(String pRoleToken,
            AccessControlContextData pAccessControlContext,
            CacheableFieldsContainer pFieldsContainer);

    /**
     * Apply field access controls on a field Access control attributes
     * (updatable/confidential/mandatory/exportable)
     * 
     * @param pRoleToken
     *            current session token
     * @param pAccessControlContextData
     *            access control context to apply
     * @param pField
     *            Field on which access control must be applied (modified by
     *            this method)
     * @deprecated see getCacheableFieldsContainer
     */
    public void applyAccessControls(final String pRoleToken,
            final AccessControlContextData pAccessControlContextData,
            org.topcased.gpm.business.serialization.data.Field pField);

    /**
     * Get the filter access control of a given type
     * 
     * @param pRoleToken
     *            The current role session token
     * @param pTypeId
     *            The id of the type to get the filter access control
     * @return The filter access control
     */
    public FilterAccessControl getFilterAccessControl(String pRoleToken,
            String pTypeId);

    /**
     * Get the filter access control of a given type and field
     * 
     * @param pRoleToken
     *            The current role session token
     * @param pTypeId
     *            The id of the type to get the filter access control
     * @param pFieldLabel
     *            The label of the field to get the filter access control
     * @return The filter access control
     */
    public FilterAccessControl getFilterAccessControl(String pRoleToken,
            String pTypeId, String pFieldLabel);

    /**
     * Delete <i><b>all</b></i> access controls defined for a given instance.
     * <p>
     * This method requires an administrator privilege.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pBusinessProcessName
     *            Instance (business process) name
     * @throws AuthorizationException
     *             If user has not global admin role
     * @throws InvalidNameException
     *             If business process does not exist
     */
    public void deleteAllAccessControls(String pRoleToken,
            String pBusinessProcessName);

    /**
     * Test if the session is a user session
     * 
     * @param pSessionToken
     *            A session token
     * @return If the session is a valid user token
     */
    public boolean isValidUserToken(String pSessionToken);

    /**
     * Test if the session is a role session
     * 
     * @param pSessionToken
     *            A session token
     * @return If the session is a valid role token
     */
    public boolean isValidRoleToken(String pSessionToken);

    /**
     * Check if the login management is case sensitive or not
     * 
     * @return true if the login management is case sensitive or not.
     */
    public boolean isLoginCaseSensitive();

    /**
     * Get the name of overridden role for a given role. An overridden role is a
     * similar role as others roles. This overridden role is characterized by a
     * container identifier and a user login.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pValuesContainerId
     *            The values container identifier
     * @return the overridden role name if it exists, else return the basic role
     *         name.
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public String getOverridenRoleName(String pRoleToken,
            String pValuesContainerId);

    /**
     * Set an overridden role. This method allow to set an overridden role for a
     * given container and a given user.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pValuesContainerId
     *            The values container identifier
     * @param pUserLogin
     *            Login of the user
     * @param pOldRoleName
     *            Role name whose we want to set overridden role
     * @param pNewRoleName
     *            Role name overridden the old role.
     */
    public void setOverriddenRole(String pRoleToken, String pValuesContainerId,
            String pUserLogin, String pOldRoleName, String pNewRoleName);

    /**
     * Delete an overridden role. This method allow to delete an overridden role
     * for a given container and a given user.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pValuesContainerId
     *            The values container identifier
     * @param pUserLogin
     *            Login of the user
     * @param pRoleName
     *            Role name to delete.
     */
    public void deleteOverriddenRole(String pRoleToken,
            String pValuesContainerId, String pUserLogin, String pRoleName);

    /**
     * Get an administrator user token.
     * <p>
     * This method should be used to have an 'administrator' privilege when
     * needed.
     * 
     * @return Administrator user token.
     */
    public String getAdminUserToken();

    /**
     * Get an administrator role token.
     * <p>
     * This method should be used to have an 'administrator' privilege when
     * needed.
     * 
     * @param pBusinessProcessName
     *            Name of the current gPM instance
     * @return Administrator role token.
     */
    public String getAdminRoleToken(String pBusinessProcessName);

    /**
     * The current user can use filters of the returned scopes.
     * 
     * @param pRoleToken
     *            The role token.
     * @return The executable filter scope.
     */
    public List<FilterScope> getExecutableFilterScope(final String pRoleToken);

    /**
     * The current user can saves filter on the returned scopes.
     * 
     * @param pRoleToken
     *            The role token.
     * @return The editable filter scope.
     */
    public List<FilterScope> getEditableFilterScope(final String pRoleToken);

    /**
     * Create the filter access constraint object.
     * 
     * @param pRoleToken
     *            Role token
     * @param pFilterAccessContraint
     *            Business object to create
     * @throws InvalidNameException
     *             If the constrains already exists.
     * @throws AuthorizationException
     *             Only global admin can create a filter access.
     */
    public void createFilterAccessConstraint(String pRoleToken,
            FilterAccessContraint pFilterAccessContraint)
        throws InvalidNameException, AuthorizationException;

    /**
     * Tests if the constraint exists.
     * 
     * @param pConstraintName
     *            Name of the constraint to test
     * @return True if exists, false otherwise
     */
    public boolean isFilterAccessConstraintExists(String pConstraintName);

    /**
     * Create the filter access.
     * <p>
     * Convert the business object to domain object and retrieve constraints.
     * 
     * @param pRoleToken
     *            Role token
     * @param pFilterAccessCtl
     *            filter access to create
     * @throws IllegalArgumentException
     *             if the visibility value does not correspond to an enumeration
     *             of FilterScope if the field name is setting and there is no
     *             type name.
     * @throws InvalidIdentifierException
     *             if the constraint specified in the business object does not
     *             exists.
     * @throws InvalidNameException
     *             If role name, type name or field name does not exist.
     * @throws AuthorizationException
     *             Only global admin can create a filter access.
     */
    public void createFilterAccess(String pRoleToken,
            FilterAccessCtl pFilterAccessCtl) throws InvalidNameException,
        IllegalArgumentException, AuthorizationException,
        InvalidIdentifierException;

    /**
     * Get the user login associated to a user token nor a role token.
     * 
     * @param pToken
     *            A user or a role token.
     * @return The current login.
     * @throws InvalidTokenException
     *             Exception if the token is invalid.
     */
    public String getLogin(String pToken) throws InvalidTokenException;

    /**
     * Get the process name associated to a role token.
     * 
     * @param pRoleToken
     *            A role token.
     * @return The current process name.
     * @throws InvalidTokenException
     *             Exception if the token is invalid or if it's a user token.
     */
    public String getProcessName(String pRoleToken)
        throws InvalidTokenException;

    /**
     * Get the product name associated to a role token.
     * 
     * @param pRoleToken
     *            A role token.
     * @return The current product name.
     * @throws InvalidTokenException
     *             Exception if the token is invalid or if it's a user token.
     */
    public String getProductName(String pRoleToken)
        throws InvalidTokenException;

    /**
     * Get application access control data for a Type ID
     * 
     * @param pRoleToken
     *            the user token
     * @param pContainerTypeId
     *            the type id
     * @param pActionKey
     *            the action key
     * @return the corresponding ActionAccessControlData
     */
    public ActionAccessControlData getApplicationActionAccessControlForType(
            String pRoleToken, String pContainerTypeId, String pActionKey);

    /**
     * Refresh the user session. after a HMI call for example.
     * 
     * @param pRoleToken
     *            The user token
     * @return If the session is valid
     */
    public boolean refreshSession(String pRoleToken);

    /**
     * Create a proxy 'Checked' on a Fields Link Container with an access
     * control
     * 
     * @param pRoleToken
     *            The session token
     * @param pAccessControlContext
     *            The access control context to apply
     * @param pFieldsContainer
     *            The initial Fields Container
     * @return The Values Fields with a proxy 'Checked' if the current user can
     *         access it, null if the links are confidential
     */
    public <FIELDS_CONTAINER extends CacheableFieldsContainer> FIELDS_CONTAINER getCheckedLinksFieldsContainer(
            String pRoleToken, AccessControlContextData pAccessControlContext,
            FIELDS_CONTAINER pFieldsContainer);

}
