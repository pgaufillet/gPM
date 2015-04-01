/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.authorization.impl;

import static org.topcased.gpm.domain.constant.AccessControlConstant.ACTION_DEFAULT_NOROLE_CONFIDENTIAL;
import static org.topcased.gpm.domain.constant.AccessControlConstant.ACTION_DEFAULT_NOROLE_ENABLED;
import static org.topcased.gpm.domain.constant.AccessControlConstant.TRANSITION_DEFAULT_ALLOWED;
import static org.topcased.gpm.domain.constant.AccessControlConstant.TYPE_DEFAULT_CONFIDENTIAL;
import static org.topcased.gpm.domain.constant.AccessControlConstant.TYPE_DEFAULT_CREATABLE;
import static org.topcased.gpm.domain.constant.AccessControlConstant.TYPE_DEFAULT_DELETABLE;
import static org.topcased.gpm.domain.constant.AccessControlConstant.TYPE_DEFAULT_UPDATABLE;
import static org.topcased.gpm.util.bean.CacheProperties.ACCESS_CONTROL_NOT_USED;
import static org.topcased.gpm.util.bean.CacheProperties.DEFAULT_ACCESS_CONTROL_USED;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.topcased.gpm.business.ServiceImplBase;
import org.topcased.gpm.business.attributes.AttributeData;
import org.topcased.gpm.business.attributes.impl.AttributesUtils;
import org.topcased.gpm.business.attributes.impl.CacheableAttributesContainer;
import org.topcased.gpm.business.attributes.service.AttributesService;
import org.topcased.gpm.business.authorization.impl.filter.FilterAccessContraint;
import org.topcased.gpm.business.authorization.impl.filter.FilterAccessControl;
import org.topcased.gpm.business.authorization.impl.filter.FilterAccessDefinition;
import org.topcased.gpm.business.authorization.impl.filter.FilterAccessDefinitionKey;
import org.topcased.gpm.business.authorization.service.AccessControlContextData;
import org.topcased.gpm.business.authorization.service.AccessControlData;
import org.topcased.gpm.business.authorization.service.ActionAccessControlData;
import org.topcased.gpm.business.authorization.service.AdminAccessControlData;
import org.topcased.gpm.business.authorization.service.AuthorizationService;
import org.topcased.gpm.business.authorization.service.EndUserData;
import org.topcased.gpm.business.authorization.service.FieldAccessControlData;
import org.topcased.gpm.business.authorization.service.PasswordEncoding;
import org.topcased.gpm.business.authorization.service.RoleData;
import org.topcased.gpm.business.authorization.service.RoleProperties;
import org.topcased.gpm.business.authorization.service.TransitionAccessControlData;
import org.topcased.gpm.business.authorization.service.TypeAccessControlData;
import org.topcased.gpm.business.authorization.service.UserAttributesData;
import org.topcased.gpm.business.authorization.service.UserCredentials;
import org.topcased.gpm.business.events.LogoutEvent;
import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.exception.ConstraintException;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.exception.InvalidIdentifierException;
import org.topcased.gpm.business.exception.InvalidNameException;
import org.topcased.gpm.business.exception.InvalidTokenException;
import org.topcased.gpm.business.exception.InvalidValueException;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.extensions.service.ContextBase;
import org.topcased.gpm.business.fields.FieldAccessData;
import org.topcased.gpm.business.fields.impl.CacheableFieldsContainer;
import org.topcased.gpm.business.fields.impl.CacheableValuesContainer;
import org.topcased.gpm.business.product.impl.AvailableProductsKey;
import org.topcased.gpm.business.product.impl.CacheableProduct;
import org.topcased.gpm.business.product.impl.ProductTreeNode;
import org.topcased.gpm.business.product.service.ProductData;
import org.topcased.gpm.business.search.impl.fields.UsableFieldsManager;
import org.topcased.gpm.business.search.service.FilterScope;
import org.topcased.gpm.business.serialization.data.FilterAccessCtl;
import org.topcased.gpm.business.serialization.data.MultipleField;
import org.topcased.gpm.business.serialization.data.User;
import org.topcased.gpm.business.sheet.impl.CacheableSheet;
import org.topcased.gpm.business.util.action.AdministrationAction;
import org.topcased.gpm.common.accesscontrol.Roles;
import org.topcased.gpm.domain.accesscontrol.AccessControl;
import org.topcased.gpm.domain.accesscontrol.AdminAccessControl;
import org.topcased.gpm.domain.accesscontrol.AdminAccessControlDao;
import org.topcased.gpm.domain.accesscontrol.AppliActionAccessControl;
import org.topcased.gpm.domain.accesscontrol.AppliActionAccessControlDao;
import org.topcased.gpm.domain.accesscontrol.EndUser;
import org.topcased.gpm.domain.accesscontrol.FieldAccessControl;
import org.topcased.gpm.domain.accesscontrol.FieldAccessControlDao;
import org.topcased.gpm.domain.accesscontrol.OverriddenRole;
import org.topcased.gpm.domain.accesscontrol.OverriddenRoleDao;
import org.topcased.gpm.domain.accesscontrol.Role;
import org.topcased.gpm.domain.accesscontrol.RoleDao;
import org.topcased.gpm.domain.accesscontrol.RolesForProduct;
import org.topcased.gpm.domain.accesscontrol.RolesForProductDao;
import org.topcased.gpm.domain.accesscontrol.TransitionAccessControl;
import org.topcased.gpm.domain.accesscontrol.TransitionAccessControlDao;
import org.topcased.gpm.domain.accesscontrol.TypeAccessControl;
import org.topcased.gpm.domain.accesscontrol.TypeAccessControlDao;
import org.topcased.gpm.domain.attributes.AttributeValue;
import org.topcased.gpm.domain.attributes.AttributesContainer;
import org.topcased.gpm.domain.attributes.AttributesContainerDao;
import org.topcased.gpm.domain.businessProcess.BusinessProcess;
import org.topcased.gpm.domain.constant.AccessControlConstant;
import org.topcased.gpm.domain.dictionary.Environment;
import org.topcased.gpm.domain.fields.Field;
import org.topcased.gpm.domain.fields.FieldDao;
import org.topcased.gpm.domain.fields.FieldsContainer;
import org.topcased.gpm.domain.fields.ValuesContainer;
import org.topcased.gpm.domain.process.Node;
import org.topcased.gpm.domain.product.Product;
import org.topcased.gpm.util.bean.CacheProperties;
import org.topcased.gpm.util.lang.CopyUtils;
import org.topcased.gpm.util.proxy.CheckedObjectGenerator;
import org.topcased.gpm.util.session.GpmSessionFactory;

import sun.misc.BASE64Encoder;

/**
 * Authorization service implementation.
 * 
 * @author llatil
 */
public final class AuthorizationServiceImpl extends ServiceImplBase implements
        AuthorizationService {

    /** The max value of the 'salt' attribute (for encryption). */
    public static final int MAX_SALT_VALUE = 255;

    /** Name of the administrator user. */
    private static final String ADMIN_NAME = "gPM Administrator";

    /** User sessions. */
    private final UserSessions userSessions = new UserSessions();

    /** Role sessions. */
    private final RoleSessions roleSessions = new RoleSessions();

    /** User credentials access. */
    private List<UserCredentials> userCredentialsList;

    /**
     * User credentials definition (the string defines all the
     * userCredentialManagerNames, separated by a comma).
     */
    private String userCredentialsMgrDefinition;

    /**
     * Administrator user token.
     * <p>
     * This token is used to get 'administrator' privileges when needed.
     */
    private String globalAdminUserToken;

    /**
     * Administrator role tokens.
     * <p>
     * This map stores admin role tokens for each gPM instance. <br>
     * (instance name -> admin role token)
     */
    private Map<String, String> globalAdminRoleTokens;

    /** Random generator used for 'salt' value. */
    private Random saltGenerator = new Random();

    /** The login case sensitive. */
    private boolean loginCaseSensitive = false;

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#login(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public String login(String pLoginName, String pPasswd) {
        return login(pLoginName, pPasswd, sessionMaxTime);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#validateLogin(java.lang.String,
     *      java.lang.String, boolean)
     */
    @Override
    public EndUser validateLogin(String pLogin, String pPassword) {
        EndUser lUser = null;

        if (isExternalAuthentication()) {
            // In case of external auth., we simply check that the user exists.
            lUser = getUserFromLogin(pLogin);
        }
        else {
            for (UserCredentials lUserCredentials : userCredentialsList) {
                lUser =
                        lUserCredentials.validateLogin(pLogin, pPassword,
                                isLoginCaseSensitive());
                if (lUser != null) {
                    break;
                }
            }
        }
        return lUser;
    }

    /**
     * set userCredentialsMgrDefinition.
     * 
     * @param pUserCredentialsMgrDefinition
     *            the userCredentialsMgrDefinition to set
     */
    public void setUserCredentialsMgrDefinition(
            String pUserCredentialsMgrDefinition) {
        userCredentialsMgrDefinition = pUserCredentialsMgrDefinition;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#login(java.lang.String,
     *      java.lang.String, long)
     */
    @Override
    public String login(String pLoginName, String pPasswd, long pSessionMaxTime) {

        EndUser lUser = validateLogin(pLoginName, pPasswd);

        if (lUser != null) {
        	getEndUserDao().updateLastConnection(lUser);
            return userSessions.create(lUser, pSessionMaxTime);
        }
        return null;
    }

    /**
     * Checks if is external authentication.
     * 
     * @return true, if is external authentication
     */
    private boolean isExternalAuthentication() {
        // Get authentication attribute
        final String[] lAttributeNames = { AttributesService.AUTHENTICATION };
        final AttributeData[] lAttributes;

        lAttributes =
                getAttributesService().getGlobalAttributes(lAttributeNames);

        // If the authentication type is not specified, let's assume an internal auth.
        if (lAttributes == null || lAttributes[0] == null) {
            return false;
        }
        // else

        // Check the authentication type attribute has one value. If not,
        // assume an internal auth
        if (lAttributes.length != 1 || lAttributes[0] == null
                || lAttributes[0].getValues() == null
                || lAttributes[0].getValues().length != 1) {
            return false;
        }
        // else
        String lAuthenticationType = lAttributes[0].getValues()[0];

        // External authentication
        if (AttributesService.EXTERNAL_AUTHENTICATION.equals(lAuthenticationType)) {
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#logout(java.lang.String)
     */
    @Override
    public void logout(String pUserToken) {
        final List<RoleContext> lAssociatedRoles = new ArrayList<RoleContext>();
        // Close the user session
        mainContext.publishEvent(new LogoutEvent(this,
                userSessions.remove(pUserToken)));

        // Close the associated role sessions
        for (RoleContext lRole : roleSessions.getAll()) {
            if (lRole.getUserToken().equals(pUserToken)) {
                lAssociatedRoles.add(lRole);
            }
        }
        for (RoleContext lInvalidRole : lAssociatedRoles) {
            closeRoleSession(lInvalidRole.getToken());
        }
    }

    /**
     * Check if a user is creatable by current role token (A user can be created
     * either from the global admin role, or from an admin access defined on
     * instance).
     * 
     * @param pRoleToken
     *            Role session token
     * @return true, if the user is creatable
     */
    // TODO declare in interface
    public boolean isUserCreatable(String pRoleToken) {
        boolean lHasGlobalAdminRole = hasGlobalAdminRole(pRoleToken);

        if (lHasGlobalAdminRole) {
            return true;
        }

        boolean lIsAdminAccessOnInstance =
                isAdminAccessDefinedOnInstance(pRoleToken,
                        AdministrationAction.USER_CREATE.getActionKey());

        return lIsAdminAccessOnInstance;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#createUser(java.lang.String,
     *      org.topcased.gpm.business.authorization.service.EndUserData,
     *      java.lang.String,
     *      org.topcased.gpm.business.extensions.service.Context)
     */
    @Override
    public String createUser(String pToken, EndUserData pUserData,
            String pPasswd, Context pCtx) throws InvalidNameException {

        if (!isUserCreatable(pToken)) {
            throw new AuthorizationException(
                    "Unsufficient rights to create user.");
        }

        return createUser(pUserData, pPasswd, pCtx);
    }

    /**
     * Creates the user.
     * 
     * @param pUserData
     *            the user data
     * @param pPasswd
     *            the passwd
     * @param pContext
     *            the context (not null)
     * @return the string
     */
    private String createUser(EndUserData pUserData, String pPasswd,
            Context pContext) {
        if (StringUtils.isBlank(pUserData.getLogin())) {
            throw new InvalidValueException("UserData 'login' is null");
        }

        // Get user with case insensitive
        EndUser lUser = getEndUserDao().getUser(pUserData.getLogin(), false);
        if (null != lUser) {
            throw new InvalidValueException(pUserData.getLogin(), "Login '"
                    + pUserData.getLogin() + "' already exists");
        }
        if (StringUtils.isBlank(pPasswd)) {
            throw new InvalidValueException(InvalidNameException.PWD_EMPTY_KEY);
        }

        lUser = EndUser.newInstance();
        fillEndUser(lUser, pUserData);

        // set the encrypted password
        String lEncryptedPassword;
        if (PasswordEncoding.CLEAR.equals(pContext.get("passwordEncoding",
                PasswordEncoding.class))
                || pContext.get("passwordEncoding", PasswordEncoding.class) == null) {
            lEncryptedPassword = encrypt(pPasswd);
        }
        else if (PasswordEncoding.MD5.equals(pContext.get("passwordEncoding",
                PasswordEncoding.class))) {
            lEncryptedPassword = pPasswd;
        }
        else {
            throw new InvalidValueException(pContext.get("passwordEncoding",
                    PasswordEncoding.class).getValue());
        }
        lUser.setPasswd(lEncryptedPassword);

        getEndUserDao().create(lUser);

        setUserAttributes(lUser, pUserData);
        return lUser.getId();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#createAdminUser(java.lang.String)
     */
    @Override
    public String createAdminUser(String pAdminPwd) {
        if (null != getUserData(ADMIN_LOGIN)) {
            throw new AuthorizationException(
                    "Administrator user 'admin' already exists.");
        }

        EndUserData lAdminData = new EndUserData();

        lAdminData.setLogin(ADMIN_LOGIN);
        lAdminData.setName(ADMIN_NAME);

        Context lContext = ContextBase.getEmptyContext();
        lContext.put("passwordEncoding", PasswordEncoding.CLEAR);

        return createUser(lAdminData, pAdminPwd, lContext);
    }

    /**
     * Can current role update user : A user can be updated by global admin
     * role, or by admin access on instance, or by admin access on current
     * product, or if modified user is current user.
     * 
     * @param pRoleToken
     *            role session token
     * @param pUserLogin
     *            user login to be updaed
     * @return true if user is updatable
     */
    // TODO declare in interface
    public boolean isUserUpdatable(String pRoleToken, String pUserLogin) {

        boolean lIsAdmin = hasAdminAccess(pRoleToken);
        if (lIsAdmin) {
            return true;
        }

        boolean lIsAdminAccessOnInstance =
                isAdminAccessDefinedOnInstance(pRoleToken,
                        AdministrationAction.USER_MODIFY.getActionKey());
        if (lIsAdminAccessOnInstance) {
            return true;
        }

        if (getLoginFromToken(pRoleToken).equals(pUserLogin)
                && isAdminAccessDefinedOnInstance(pRoleToken,
                        AdministrationAction.USER_MODIFY_CURRENT.getActionKey())) {
            return true;
        }

        String lCurrentProductName = getProductNameFromSessionToken(pRoleToken);
        boolean lIsAdminAccessOnProduct =
                isAdminAccessDefinedOnProduct(pRoleToken,
                        AdministrationAction.USER_MODIFY.getActionKey(),
                        lCurrentProductName);
        if (lIsAdminAccessOnProduct) {
            return true;
        }

        return getUserFromToken(pRoleToken).getLogin().equals(pUserLogin);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#updateUser(java.lang.String,
     *      org.topcased.gpm.business.authorization.service.EndUserData,
     *      java.lang.String,
     *      org.topcased.gpm.business.extensions.service.Context)
     */
    @Override
    public String updateUser(String pRoleToken, EndUserData pUserData,
            String pPasswd, Context pContext) throws InvalidNameException {
        if (!isUserUpdatable(pRoleToken, pUserData.getLogin())) {
            throw new AuthorizationException(
                    "Not enough privileges for user update");
        }

        EndUser lUser =
                getEndUserDao().getUser(pUserData.getLogin(),
                        isLoginCaseSensitive());

        if (null == lUser) {
            throw new InvalidNameException(pUserData.getLogin(), "Login '"
                    + pUserData.getLogin() + "' does not exist");
        }

        fillEndUser(lUser, pUserData);
        setUserAttributes(lUser, pUserData);

        if (!StringUtils.isBlank(pPasswd)) {
            String lEncryptedPassword;
            if (PasswordEncoding.CLEAR.equals(pContext.get("passwordEncoding",
                    PasswordEncoding.class))
                    || pContext.get("passwordEncoding", PasswordEncoding.class) == null) {
                lEncryptedPassword = encrypt(pPasswd);
            }
            else if (PasswordEncoding.MD5.equals(pContext.get(
                    "passwordEncoding", PasswordEncoding.class))) {
                lEncryptedPassword = pPasswd;
            }
            else {
                throw new InvalidValueException(pContext.get(
                        "passwordEncoding", PasswordEncoding.class).getValue());
            }
            lUser.setPasswd(lEncryptedPassword);
        }
        return lUser.getId();
    }

    /**
     * Encrypt a string with SHA-1.
     * 
     * @param pText
     *            a String
     * @param pSalt
     *            the salt value
     * @return the corresponding encrypted String
     */
    // TODO declare in interface
    public String encrypt(String pText, int pSalt) {
        MessageDigest lMessageDigest = null;

        // concat the initial string with a random Salt value
        String lSaltedText = pText + pSalt;

        // gets Secure Hash Algorithm
        try {
            lMessageDigest = MessageDigest.getInstance("SHA");
        }
        catch (NoSuchAlgorithmException e) {
            throw new GDMException("no SHA-1 support on this VM");
        }

        lMessageDigest.update(lSaltedText.getBytes());

        byte[] lRaw = lMessageDigest.digest();
        String lEncrypted = (new BASE64Encoder()).encode(lRaw);

        return lEncrypted;
    }

    /**
     * Encrypt a character string with SHA-1 with a random 'salt' value added
     * (between 0 and MAX_SALT_VALUE).
     * 
     * @param pText
     *            a String
     * @return the corresponding encrypted String
     */
    private String encrypt(String pText) {
        // choose a random salt value between 0 and MAX_SALT_VALUE (exclusive)
        return encrypt(pText, saltGenerator.nextInt(MAX_SALT_VALUE));
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#createOrUpdateUser(java.lang.String,
     *      org.topcased.gpm.business.authorization.service.EndUserData,
     *      java.lang.String,
     *      org.topcased.gpm.business.extensions.service.Context)
     */
    @Override
    public String createOrUpdateUser(String pRoleToken, EndUserData pUserData,
            String pPasswd, Context pContext) {
        EndUser lUser =
                getEndUserDao().getUser(pUserData.getLogin(),
                        isLoginCaseSensitive());

        if (null == lUser) {
            return createUser(pRoleToken, pUserData, pPasswd, pContext);
        }
        // else
        return updateUser(pRoleToken, pUserData, pPasswd, pContext);
    }

    /**
     * Check if a user can be removed by current role token (A user can be
     * removed either from the global admin role, or from an admin access
     * defined on instance).
     * 
     * @param pRoleToken
     *            Role session token
     * @return true, if the user can be removed
     */
    // TODO declare in interface
    public boolean isUserDeletable(String pRoleToken) {
        boolean lHasGlobalAdminRole = hasGlobalAdminRole(pRoleToken);

        if (lHasGlobalAdminRole) {
            return true;
        }

        boolean lIsAdminAccessOnInstance =
                isAdminAccessDefinedOnInstance(pRoleToken,
                        AdministrationAction.USER_DELETE.getActionKey());

        return lIsAdminAccessOnInstance;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#removeUser(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public void removeUser(String pRoleToken, String pLogin)
        throws InvalidNameException, AuthorizationException {

        if (!isUserDeletable(pRoleToken)) {
            throw new AuthorizationException(
                    "Unsufficient rights to delete user.");
        }

        EndUser lUser = getUserFromLogin(pLogin);

        // Remove all OverriddenRoles for the user
        authorizationService.deleteOverriddenRolesFromLogin(pLogin);

        getEndUserDao().remove(lUser);
        // Note: RolesForProduct & UserAttributes attached on this
        // user are removed automatically.
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#getBusinessProcessNames(java.lang.String)
     */
    @Override
    public Collection<String> getBusinessProcessNames(String pUserToken)
        throws InvalidTokenException {
        // The blank user token is tested in the method getUserFromToken.
        SortedSet<String> lNames = new TreeSet<String>();
        EndUser lEndUser = getUserFromToken(pUserToken);

        // Get business processes for 'normal' users
        for (RolesForProduct lRfp : lEndUser.getRolesForProducts()) {
            for (Role lRole : lRfp.getRoles()) {
                BusinessProcess lBizProc = lRole.getBusinessProcess();
                lNames.add(lBizProc.getName());
            }
        }

        // Get business processes for 'admin' users
        for (Role lAdminRole : lEndUser.getAdminRoles()) {
            BusinessProcess lBizProc = lAdminRole.getBusinessProcess();
            lNames.add(lBizProc.getName());
        }

        return lNames;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#getProductNames(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public List<String> getProductNames(String pUserToken,
            String pBusinessProcess) throws InvalidTokenException {
        return availableProductsManager.getProductNames(new AvailableProductsKey(
                pBusinessProcess, getLoginFromToken(pUserToken)));
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#getProductNamesAndDescriptions(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public List<CacheableProduct> getProductNamesAndDescriptions(
            String pUserToken, String pBusinessProcess)
        throws InvalidTokenException {
        return availableProductsManager.getElement(new AvailableProductsKey(
                pBusinessProcess, getLoginFromToken(pUserToken)));
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#getAllProductNames(java.lang.String)
     */
    @Override
    public List<String> getAllProductNames(String pBusinessProcess) {
        return getProductDao().getProductNames(pBusinessProcess, false);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#getSerializableProducts(java.lang.String,
     *      java.lang.String)
     */
    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Collection<org.topcased.gpm.business.serialization.data.Product> getSerializableProducts(
            String pUserToken, String pBusinessProcess) {
        EndUser lEndUser = getUserFromToken(pUserToken);
        Collection<Product> lProducts =
                getProductsForUser(pUserToken, pBusinessProcess, lEndUser);
        List<org.topcased.gpm.business.serialization.data.Product> lSerializableProducts =
                new ArrayList<org.topcased.gpm.business.serialization.data.Product>();

        for (Product lProduct : lProducts) {
            CacheableProduct lCacheableProduct =
                    getProductService().getCacheableProduct(lProduct.getId(),
                            CacheProperties.IMMUTABLE);
            org.topcased.gpm.business.serialization.data.Product lSerializableProduct =
                    new org.topcased.gpm.business.serialization.data.Product();
            lCacheableProduct.marshal(lSerializableProduct);
            lSerializableProducts.add(lSerializableProduct);
        }

        return lSerializableProducts;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#getLoggedUserData(java.lang.String)
     * 
     * @deprecated
     * @see AuthorizationService#getLoggedUserData(String, boolean)
     */
    @Override
    public EndUserData getLoggedUserData(String pUserToken)
        throws InvalidTokenException {
        return getLoggedUserData(pUserToken, false);
    }
    
    /**
     * {@inheritDoc}
     */
    public EndUserData getLoggedUserData(String pUserToken, boolean pLightUserData) 
    throws InvalidTokenException {
        EndUser lUser = getUserFromToken(pUserToken);
        return createEndUserData(lUser, pLightUserData);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#getUserSessionFromRoleSession(java.lang.String)
     */
    @Override
    public String getUserSessionFromRoleSession(String pRoleToken)
        throws InvalidTokenException {
        // The blank role token is tested in the method getContext.
        return roleSessions.getContext(pRoleToken).getUserToken();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#getSessionAttribute(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public Object getSessionAttribute(String pUserToken, String pAttrName)
        throws InvalidTokenException, InvalidNameException {
        // The blank role tokens are tested in the method getContext.
        return userSessions.getContext(pUserToken).getAttribute(pAttrName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#setSessionAttribute(java.lang.String,
     *      java.lang.String, java.lang.Object)
     */
    @Override
    public void setSessionAttribute(String pUserToken, String pAttrName,
            Object pValue) throws InvalidTokenException, InvalidNameException {
        userSessions.getContext(pUserToken).setAttribute(pAttrName, pValue);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#getUserData(java.lang.String)
     */
    @Override
    public EndUserData getUserData(String pLoginName)
        throws InvalidNameException {
        boolean lCaseSensitive = false;

        if (!ADMIN_LOGIN.equals(pLoginName)) {
            lCaseSensitive = isLoginCaseSensitive();
        }
        EndUserData lUser = null;
        for (UserCredentials lUserCredentials : userCredentialsList) {
            if (lUserCredentials != null) {
                EndUserData lUserTmp =
                        lUserCredentials.getUserInfos(pLoginName,
                                lCaseSensitive);
                if (lUserTmp != null && lUserTmp.getId() != null) {
                    lUser =
                            lUserCredentials.getUserInfos(pLoginName,
                                    lCaseSensitive);
                }
            }
        }
        return lUser;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#getUser(java.lang.String)
     */
    @Override
    public User getUser(String pLoginName) {
        //Build user data  from user credentials
        User lUser =
                getSerializableUserFromEndUserData(getUserData(pLoginName));

        // add roles and attributes from DAO user :
        EndUser lUserDB = getEndUserDao().getUser(pLoginName, false);

        if (lUserDB != null) {
            // Fill user roles
            List<org.topcased.gpm.business.serialization.data.Role> lRoles =
                    new ArrayList<org.topcased.gpm.business.serialization.data.Role>();
            // Add instance roles
            for (Role lRole : lUserDB.getAdminRoles()) {
                org.topcased.gpm.business.serialization.data.Role lUserRole =
                        new org.topcased.gpm.business.serialization.data.Role();
                lUserRole.setName(lRole.getName());
                lRoles.add(lUserRole);
            }
            // add roles for products
            if (lUserDB.getRolesForProducts() != null) {
                for (RolesForProduct lRoleForProduct : lUserDB.getRolesForProducts()) {
                    if (lRoleForProduct != null
                            && lRoleForProduct.getRoles() != null) {
                        String lProductName =
                                lRoleForProduct.getProduct().getName();
                        for (Role lRole : lRoleForProduct.getRoles()) {
                            org.topcased.gpm.business.serialization.data.Role lUserRole =
                                    new org.topcased.gpm.business.serialization.data.Role();
                            lUserRole.setName(lRole.getName());
                            lUserRole.setProductName(lProductName);
                            lRoles.add(lUserRole);
                        }
                    }
                }
            }
            lUser.setRoles(lRoles);

            // Add User attributes
            if (lUserDB.getAttributes() != null) {
                List<org.topcased.gpm.business.serialization.data.Attribute> lAttributes =
                        new ArrayList<org.topcased.gpm.business.serialization.data.Attribute>();
                for (org.topcased.gpm.domain.attributes.Attribute lAttributeEntity : lUserDB.getAttributes()) {
                    if (lAttributeEntity.getAttributeValues() != null) {
                        List<org.topcased.gpm.business.serialization.data.AttributeValue> lAttributeValues =
                                new ArrayList<org.topcased.gpm.business.serialization.data.AttributeValue>();
                        for (AttributeValue lAttributeValueEntity : lAttributeEntity.getAttributeValues()) {
                            lAttributeValues.add(new org.topcased.gpm.business.serialization.data.AttributeValue(
                                    lAttributeValueEntity.getValue()));
                        }
                        lAttributes.add(new org.topcased.gpm.business.serialization.data.Attribute(
                                lAttributeEntity.getName(), lAttributeValues));
                    }
                }
                lUser.setAttributes(lAttributes);
            }
        }
        return lUser;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#getUserFromRole(java.lang.String)
     */
    @Override
    public org.topcased.gpm.business.serialization.data.User getUserFromRole(
            String pRoleToken) throws InvalidNameException {
        EndUser lUser =
                getUserFromToken(getUserSessionFromRoleSession(pRoleToken));
        if (null == lUser) {
            return null;
        }
        // Get information on user, using user credentials.
        org.topcased.gpm.business.serialization.data.User lRes =
                getUser(lUser.getLogin());
        org.topcased.gpm.business.serialization.data.Role lRole =
                getSerializableRole(pRoleToken);
        List<org.topcased.gpm.business.serialization.data.Role> lRoles =
                new ArrayList<org.topcased.gpm.business.serialization.data.Role>();
        lRoles.add(lRole);
        lRes.setRoles(lRoles);
        return lRes;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#getUsersWithRole(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    @Override
    public List<String> getUsersWithRole(String pBusinessProcessName,
            String pRoleName, String pProductName) throws GDMException {
        // Get users with specified role name either on instance or on specified product
        return getUsersWithRole(pBusinessProcessName, pRoleName,
                new String[] { pProductName }, Roles.INSTANCE_ROLE
                        | Roles.PRODUCT_ROLE | Roles.ROLE_ON_ONE_PRODUCT);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#getUsersWithRole(java.lang.String,
     *      java.lang.String, java.lang.String[], int)
     */
    @Override
    public List<String> getUsersWithRole(String pBusinessProcessName,
            String pRoleName, String[] pProductNames, int pRoleProps)
        throws GDMException {

        Collection<String> lRoles = getRolesNames(pBusinessProcessName);

        if (lRoles != null && !lRoles.contains(pRoleName)) {
            throw new GDMException("Invalid roleName '" + pRoleName
                    + "' for Business process '" + pBusinessProcessName + "'.");
        }

        return getEndUserDao().getUsers(pBusinessProcessName, pRoleName,
                pProductNames, pRoleProps);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#getAllUserData()
     */
    @Override
    @SuppressWarnings("unchecked")
    public EndUserData[] getAllUserData() {
        // Get all users logins.
        // TODO: We must replace this code with a query method in DAO.
        final String lQueryStr =
                "select user.login from " + EndUser.class.getName()
                        + " user order by user.login";
        Query lQuery =
                GpmSessionFactory.getHibernateSession().createQuery(lQueryStr);
        List<String> lLogins = lQuery.list();
        EndUserData[] lUserData = new EndUserData[lLogins.size()];
        int i = 0;

        for (String lLogin : lLogins) {
            lUserData[i++] = getUserData(lLogin);
        }
        return lUserData;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#getAllUsers()
     */
    @Override
    public Collection<User> getAllUsers() {
        // Get all users from database
        Collection<EndUser> lEndUsers = getEndUserDao().loadAll();
        if (lEndUsers == null) {
            return null;
        }
        // For each user from Database, create the corresponding Serialization data structure.
        Collection<User> lUsers = new ArrayList<User>(lEndUsers.size());
        for (EndUser lEndUser : lEndUsers) {
            // Add current user in list (user created from user credentials)
            lUsers.add(getUser(lEndUser.getLogin()));
        }
        return lUsers;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#getUserLogins()
     */
    @Override
    public List<String> getUserLogins() {
        return getEndUserDao().getUserLogins();
    }

    /**
     * gets a light list of users (for list display)
     * 
     * @return the list of all users with minimal informations (login, name,
     *         forename, mail)
     */
    @Override
    public List<User> getUsers() {
        List<User> lResultUserList = new ArrayList<User>();
        Collection<EndUser> lLightListFroDAO = getEndUserDao().loadAll();
        for (EndUser lEndUser : lLightListFroDAO) {
            User lAUser = new User();
            lAUser.setLogin(lEndUser.getLogin());
            lAUser.setName(lEndUser.getName());
            lAUser.setForname(lEndUser.getForname());
            lAUser.setMail(lEndUser.getEmail());
            lResultUserList.add(lAUser);
        }

        return lResultUserList;
    }

    /**
     * Create the serialization structure from the Data object.
     * 
     * @param pEndUser
     *            the end user
     * @return the User data (serialization).
     */
    private User getSerializableUserFromEndUserData(EndUserData pEndUser) {
        if (pEndUser == null) {
            return null;
        }
        User lUser = new User();
        lUser.setLogin(pEndUser.getLogin());
        lUser.setName(pEndUser.getName());
        lUser.setForname(pEndUser.getForname());
        lUser.setMail(pEndUser.getMailAddr());

        return lUser;
    }

    /**
     * Create a EndUserData object from a EndUser.
     * 
     * @param pUser
     *            End user from DB
     * @return EndUserData
     */
    // TODO declare in interface
    private EndUserData createEndUserData(EndUser pUser) {
        EndUserData lUserData = createLightEndUserData(pUser);

        AttributesService lAttrService = getAttributesService();
        AttributeData[] lAttrsData = lAttrService.getAll(pUser.getId());

        if (ArrayUtils.isEmpty(lAttrsData)) {
            lUserData.setUserAttributes(null);
        }
        else {
            lUserData.setUserAttributes(new UserAttributesData[lAttrsData.length]);
            for (int i = 0; i < lAttrsData.length; i++) {
                lUserData.getUserAttributes()[i] =
                        new UserAttributesData(lAttrsData[i].getName(),
                                lAttrsData[i].getValues()[0]);
            }
        }
        return lUserData;
    }
    
    /**
     * Create a EndUserData object from an EndUser
     * 
     * This light version of EndUserData have a significant impact on
     * performance.
     * 
     * @param pEndUser
     *            End user from DB
     * 
     * @param pLightUserData
     *            This parameter has a great impact on performances. The impact
     *            will depend on the size of the array
     *            <ul>
     *            <li><b>true</b> : EndUserData without EndUserData attributes</li>
     *            <li><b>false</b> : complete EndUserData</li>
     *            </ul>
     *            
     * @return an EndUserData object
     */
    protected EndUserData createEndUserData(EndUser pEndUser, Boolean pLightUserData) {
        if (pLightUserData) {
            return createLightEndUserData(pEndUser);
        }
        else {
            return createEndUserData(pEndUser);
        }
    }

    /**
     * Create a EndUserData object from an EndUser without usersAttributes.
     * EndUserData will contain only :
     * <ul>
     * <li>Login</li>
     * <li>Name</li>
     * <li>Forname</li>
     * <li>Email</li>
     * <li>Id</li>
     * </ul>
     * This light version of EndUserData have a significant impact on
     * performance.
     * 
     * @param pUser
     *            End user from DB
     * @return an EndUserData object <b>without</b> its attributes
     */
    private EndUserData createLightEndUserData(EndUser pUser) {
        EndUserData lUserData = new EndUserData();

        lUserData.setLogin(pUser.getLogin());
        lUserData.setName(pUser.getName());
        lUserData.setForname(pUser.getForname());
        lUserData.setMailAddr(pUser.getEmail());
        lUserData.setId(pUser.getId());
        return lUserData;
    }

    /**
     * Fill a EndUser object from a EndUSerData.
     * 
     * @param pUser
     *            User object to fill
     * @param pUserData
     *            EndUserData to use.
     * @throws GDMException
     *             Data is incomplete.
     */
    private void fillEndUser(EndUser pUser, final EndUserData pUserData) {
        pUser.setEmail(pUserData.getMailAddr());
        pUser.setLogin(pUserData.getLogin());
        pUser.setForname(pUserData.getForname());
        pUser.setName(pUserData.getName());
    }

    /**
     * Set (or update) the attributes of a user.
     * 
     * @param pUser
     *            User DAO object
     * @param pUserData
     *            User data
     */
    private void setUserAttributes(EndUser pUser, final EndUserData pUserData) {

        // If the list of attributes to set is empty, simply remove
        // all existing attributes.
        if (ArrayUtils.isEmpty(pUserData.getUserAttributes())) {
            getAttributesService().removeAll(pUser.getId());
            return;
        }

        AttributeData[] lAttrsData =
                getAttributesService().getAll(pUser.getId());
        // list of index of updated attributes
        ArrayList<Integer> lUpdatedAttributes = new ArrayList<Integer>();

        for (AttributeData lAttrData : lAttrsData) {
            boolean lAttributeFound = false;
            for (int i = 0; !lAttributeFound
                    && (i < pUserData.getUserAttributes().length); i++) {
                // if pUser.getUserAttributes() contains an attribute with
                // the same name : updates its value
                String lAttrName = pUserData.getUserAttributes()[i].getName();
                String lNewAttrValue =
                        pUserData.getUserAttributes()[i].getValue();

                if (lAttrData.getName().equals(lAttrName)) {
                    lAttributeFound = true;
                    lUpdatedAttributes.add(i);
                    if (lAttrData.getValues() == null
                            || StringUtils.isEmpty(lAttrData.getValues()[0])
                            || !lAttrData.getValues()[0].equals(lNewAttrValue)) {
                        lAttrData.setValues(new String[] { lNewAttrValue });
                    }
                }
            }

            // If the attribute is not present in the new list, it must be
            // deleted
            if (!lAttributeFound) {
                lAttrData.setValues(null);
            }
        }

        // add new attributes
        for (int i = 0; i < pUserData.getUserAttributes().length; i++) {
            if (!lUpdatedAttributes.contains(i)) {
                String lAttrName = pUserData.getUserAttributes()[i].getName();
                String lNewAttrValue =
                        pUserData.getUserAttributes()[i].getValue();

                AttributeData lAddedAttrData =
                        new AttributeData(lAttrName,
                                new String[] { lNewAttrValue });

                lAttrsData =
                        (AttributeData[]) ArrayUtils.add(lAttrsData,
                                lAddedAttrData);
            }
        }
        getAttributesService().set(pUser.getId(), lAttrsData);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#getRolesNames(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    @Override
    public Collection<String> getRolesNames(String pUserToken,
            String pBusinessProcessName, String pProductName)
        throws InvalidTokenException {
        // Return both instance and product roles
        RoleProperties lRoleProperties = new RoleProperties(true, true);
        return getRolesNames(pUserToken, pBusinessProcessName, pProductName,
                lRoleProperties);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#getRolesNames(java.lang.String,
     *      java.lang.String, java.lang.String,
     *      org.topcased.gpm.business.authorization.service.RoleProperties)
     */
    @Override
    public Collection<String> getRolesNames(String pUserToken,
            String pBusinessProcessName, String pProductName,
            RoleProperties pRoleProperties) {
        return getRoleDao().getRoleNames(getLoginFromToken(pUserToken),
                pProductName, pBusinessProcessName,
                pRoleProperties.getInstanceRole(),
                pRoleProperties.getProductRole());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#getAdminRolesNames(java.lang.String)
     */
    @Override
    public Collection<String> getAdminRolesNames(String pUserToken)
        throws InvalidTokenException {
        // The blank token is tested in the method getUserFromToken
        EndUser lUser = getUserFromToken(pUserToken);

        Set<String> lRolesNames = new HashSet<String>();
        for (Role lAdminRole : lUser.getAdminRoles()) {
            lRolesNames.add(lAdminRole.getName());
        }
        return lRolesNames;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#selectRole(java.lang.String,
     *      java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public String selectRole(String pUserToken, String pRoleName,
            String pProductName, String pBusinessProcessName)
        throws InvalidTokenException, AuthorizationException {
        // The blank token is tested in the method getUserFromToken
        EndUser lUser = getUserFromToken(pUserToken);
        Product lProduct = null;

        if (!StringUtils.isBlank(pProductName)) {
            lProduct =
                    getProductDao().getProduct(pBusinessProcessName,
                            pProductName);

            if (lProduct == null) {
                throw new AuthorizationException("Product '" + pProductName
                        + "' not exist on business process '"
                        + pBusinessProcessName + "'");
            }

            for (RolesForProduct lRfp : lUser.getRolesForProducts()) {
                if (lRfp.getProduct().getName().equals(pProductName)) {
                    for (Role lRole : lRfp.getRoles()) {
                        if (lRole.getName().equals(pRoleName)) {
                            return roleSessions.create(
                                    userSessions.getContext(pUserToken), lUser,
                                    lRole, lProduct);
                        }
                    }
                }
            }
        }

        // Admin roles
        for (Role lAdminRole : lUser.getAdminRoles()) {
            if (lAdminRole.getBusinessProcess().getName().equals(
                    pBusinessProcessName)
                    && lAdminRole.getName().equals(pRoleName)) {
                return roleSessions.create(userSessions.getContext(pUserToken),
                        lUser, lAdminRole, lProduct);
            }
        }

        throw new AuthorizationException("Role '" + pRoleName
                + "' not authorized for business process '"
                + pBusinessProcessName + "'");

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService
     *      #closeRoleSession(java.lang.String)
     */
    @Override
    public void closeRoleSession(String pRoleToken) {
        mainContext.publishEvent(new LogoutEvent(this,
                roleSessions.remove(pRoleToken)));
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#getFieldAccessControl(java.lang.String,
     *      org.topcased.gpm.business.authorization.service.AccessControlContextData,
     *      java.lang.String)
     */
    @Override
    @SuppressWarnings("unchecked")
    public FieldAccessControlData getFieldAccessControl(
            final String pRoleToken,
            final AccessControlContextData pAccessControlContextData,
            final String pFieldId) throws InvalidTokenException {
        if (pAccessControlContextData == null) {
            throw new IllegalArgumentException("Access control context is null");
        }
        if (StringUtils.isBlank(pFieldId)) {
            throw new IllegalArgumentException("Field id is blank");
        }

        // Valid access control : fill default values and override role
        final AccessControlContextData lValidAccessControl =
                validateAccessControlContextData(pRoleToken,
                        pAccessControlContextData, null);
        final String lCacheKey =
                computeAccessControlCacheKey(lValidAccessControl,
                        "FieldAccessCtl|", "_field|", pFieldId);

        // Try to get the access control from the cache.
        Element lElem = accessControlCache.get(lCacheKey);

        FieldAccessControlData lFacd = null;

        if (null != lElem) {
//            if (staticLOGGER.isDebugEnabled()) {
//                staticLOGGER.debug("Cache hit for key=" + lCacheKey);
//                staticLOGGER.debug("Current cache stats: "
//                        + accessControlCache.getStatistics());
//            }
            lFacd = (FieldAccessControlData) lElem.getValue();
        }
        else {
            Collection<FieldAccessControl> lApplicables;
//            if (staticLOGGER.isDebugEnabled()) {
//                staticLOGGER.debug("Access control context values");
//                staticLOGGER.debug("Product name = "
//                        + lValidAccessControl.getProductName());
//                staticLOGGER.debug("State name = "
//                        + lValidAccessControl.getStateName());
//                staticLOGGER.debug("Rolen name = "
//                        + lValidAccessControl.getRoleName());
//                staticLOGGER.debug("container id = "
//                        + lValidAccessControl.getContainerTypeId());
//            }
            lApplicables =
                    getFieldAccessControlDao().getApplicables(
                            lValidAccessControl.getProductName(),
                            lValidAccessControl.getStateName(),
                            lValidAccessControl.getRoleName(),
                            lValidAccessControl.getContainerTypeId(), pFieldId,
                            lValidAccessControl.getVisibleTypeId(), true);
            if (lApplicables.size() > 1) {
                lApplicables =
                        AuthorizationUtils.getSortedAccessControls(lApplicables);
            }

            lFacd = new FieldAccessControlData();

            // The blank token is tested in the method getRole
            fillAccessControlData(lFacd, lValidAccessControl,
                    getBusinessProcessName(pRoleToken));

            FieldAccessData lFieldAccess = new FieldAccessData();

            Field lField = fieldsManager.getField(pFieldId);

            // Set the global field attributes.
            lFieldAccess.setConfidential(lField.isConfidential());
            lFieldAccess.setUpdatable(lField.isUpdatable());
            lFieldAccess.setMandatory(lField.isMandatory());
            lFieldAccess.setExportable(lField.isExportable());

//            if (staticLOGGER.isDebugEnabled()) {
//                staticLOGGER.debug("Applicables numbers : "
//                        + lApplicables.size());
//            }
            for (FieldAccessControl lFac : lApplicables) {
                if (null != lFac.getConfidential()) {
                    lFieldAccess.setConfidential(lFac.getConfidential());
//                    if (staticLOGGER.isDebugEnabled()) {
//                        staticLOGGER.debug("Set confidential as "
//                                + lFac.getConfidential());
//                    }
                }
                if (null != lFac.getUpdatable()) {
                    lFieldAccess.setUpdatable(lFac.getUpdatable());
                }
                if (null != lFac.getMandatory()) {
                    lFieldAccess.setMandatory(lFac.getMandatory());
                }
                if (null != lFac.getExportable()) {
                    lFieldAccess.setExportable(lFac.getExportable());
                }
                setExtendedAttributes(lFacd, lFac);
            }
            lFacd.setAccess(lFieldAccess);
            lFacd.setFieldName(lField.getLabelKey());

            putInCache(lCacheKey, lFacd);

//            if (staticLOGGER.isDebugEnabled()) {
//                staticLOGGER.debug("Cache miss for key=" + lCacheKey);
//                staticLOGGER.debug("Current cache stats: "
//                        + accessControlCache.getStatistics());
//            }
        }
        return lFacd;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#getFieldAccess(java.lang.String,
     *      java.lang.String, java.lang.String, java.lang.String,
     *      java.lang.String)
     * @deprecated Since 1.7
     * @see AuthorizationService#getFieldAccess(String,
     *      AccessControlContextData, String)
     */
    @Override
    public FieldAccessData getFieldAccess(String pRoleToken, String pStateName,
            String pFieldId, String pContainerId, String pVisibleTypeId) {
        return getFieldAccess(pRoleToken, getAccessControlContextData(
                pRoleToken, pStateName, pContainerId, pVisibleTypeId, null),
                pFieldId);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#getFieldAccess(java.lang.String,
     *      org.topcased.gpm.business.authorization.service.AccessControlContextData,
     *      java.lang.String)
     */
    @Override
    @SuppressWarnings("unchecked")
    public FieldAccessData getFieldAccess(String pRoleToken,
            AccessControlContextData pAccessControlContextData, String pFieldId)
        throws InvalidTokenException {
        // Valid access control : fill default values and override role
        final AccessControlContextData lValidAccessControl =
                validateAccessControlContextData(pRoleToken,
                        pAccessControlContextData, null);
        final String lCacheKey =
                computeAccessControlCacheKey(lValidAccessControl,
                        "FieldAccess|", "_field|", pFieldId);

        String lEffectiveVisibleTypeId = lValidAccessControl.getVisibleTypeId();
        if (StringUtils.isBlank(lEffectiveVisibleTypeId)) {
            lEffectiveVisibleTypeId = null;
        }

        // Try to get the access control from the cache.
        Element lElem = accessControlCache.get(lCacheKey);
        FieldAccessData lFad;

        if (null != lElem) {
//            if (staticLOGGER.isDebugEnabled()) {
//                staticLOGGER.debug("Cache hit for key=" + lCacheKey);
//
//                staticLOGGER.debug("Current cache stats: "
//                        + accessControlCache.getStatistics());
//            }
            lFad = (FieldAccessData) lElem.getValue();
        }
        else {
            Field lField = fieldsManager.getField(pFieldId);

            Collection<FieldAccessControl> lApplicables;
//            if (staticLOGGER.isDebugEnabled()) {
//                staticLOGGER.debug("(getFieldAccess) Access control context values");
//                staticLOGGER.debug("Product name = "
//                        + lValidAccessControl.getProductName());
//                staticLOGGER.debug("State name = "
//                        + lValidAccessControl.getStateName());
//                staticLOGGER.debug("Role name = "
//                        + lValidAccessControl.getRoleName());
//                staticLOGGER.debug("container id = "
//                        + lValidAccessControl.getContainerTypeId());
//            }
            lApplicables =
                    getFieldAccessControlDao().getApplicables(
                            lValidAccessControl.getProductName(),
                            lValidAccessControl.getStateName(),
                            lValidAccessControl.getRoleName(),
                            lValidAccessControl.getContainerTypeId(),
                            lField.getId(), lEffectiveVisibleTypeId, true);
            if (lApplicables.size() > 1) {
                lApplicables =
                        AuthorizationUtils.getSortedAccessControls(lApplicables);
            }

//            if (staticLOGGER.isDebugEnabled()) {
//                staticLOGGER.debug("(Field Access) Applicables found #: "
//                        + lApplicables.size());
//                staticLOGGER.debug("(Field Access) Field name "
//                        + lField.getLabelKey() + ", confidential value is "
//                        + lField.isConfidential());
//            }

            lFad = new FieldAccessData();

            // Set the global field attributes.
            lFad.setConfidential(lField.isConfidential());
            lFad.setUpdatable(lField.isUpdatable());
            lFad.setMandatory(lField.isMandatory());
            lFad.setExportable(lField.isExportable());

            //Set the field attributes according to access controls hierarchy
            for (FieldAccessControl lFac : lApplicables) {
                if (null != lFac.getConfidential()) {
                    lFad.setConfidential(lFac.getConfidential());
                }
                if (null != lFac.getUpdatable()) {
                    lFad.setUpdatable(lFac.getUpdatable());
                }
                if (null != lFac.getMandatory()) {
                    lFad.setMandatory(lFac.getMandatory());
                }
                if (null != lFac.getExportable()) {
                    lFad.setExportable(lFac.getExportable());
                }
            }
            putInCache(lCacheKey, lFad);

//            if (staticLOGGER.isDebugEnabled()) {
//                staticLOGGER.debug("Cache miss for key=" + lCacheKey);
//
//                staticLOGGER.debug("Current cache stats: "
//                        + accessControlCache.getStatistics());
//            }
        }
        return new FieldAccessData(lFad);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#applyAccessControls(java.lang.String,
     *      org.topcased.gpm.business.authorization.service.AccessControlContextData,
     *      org.topcased.gpm.business.fields.impl.CacheableFieldsContainer)
     */
    @Override
    public final void applyAccessControls(final String pRoleToken,
            final AccessControlContextData pAccessControlContextData,
            CacheableFieldsContainer pCacheableFieldsContainer) {

        // Fill access control context
        String lProductName = getProductNameFromSessionToken(pRoleToken);
        pAccessControlContextData.setProductName(lProductName);
        pAccessControlContextData.setContainerTypeId(pCacheableFieldsContainer.getId());
        pAccessControlContextData.setRoleName(getRoleNameFromToken(pRoleToken));

        // Fill cacheable container attributes with type access control attributes
        TypeAccessControlData lTypeAccessControlData =
                getTypeAccessControl(pRoleToken, pAccessControlContextData);
        pCacheableFieldsContainer.setConfidential(lTypeAccessControlData.getConfidential());
        pCacheableFieldsContainer.setCreatable(lTypeAccessControlData.getCreatable());
        pCacheableFieldsContainer.setDeletable(lTypeAccessControlData.getDeletable());
        pCacheableFieldsContainer.setUpdatable(lTypeAccessControlData.getUpdatable());
        pCacheableFieldsContainer.addAttributes(AttributesUtils.transformIntoSerializationAttributes(lTypeAccessControlData.getExtendedAttributes()));

        //For each field, find and apply field Access controls.
        for (org.topcased.gpm.business.serialization.data.Field lField : pCacheableFieldsContainer.getFields()) {
            applyAccessControls(pRoleToken, pAccessControlContextData, lField);
        }

    }

    /**
     * Create a proxy 'Checked' on a Fields Container before applying access
     * control.
     * 
     * @param pRoleToken
     *            The session token
     * @param pAccessControlContext
     *            The access control context to apply
     * @param pFieldsContainer
     *            The initial Fields Container
     * @return The Values Fields with a proxy 'Checked'
     */
    // TODO declare in interface
    public <FIELDS_CONTAINER extends CacheableFieldsContainer> FIELDS_CONTAINER getCheckedFieldsContainer(
            String pRoleToken, AccessControlContextData pAccessControlContext,
            FIELDS_CONTAINER pFieldsContainer) {
        // Create a proxy on the value container, for keeping this data safe without full copy
        final FIELDS_CONTAINER lCheckedFieldsContainer =
                CheckedObjectGenerator.create(pFieldsContainer);
        final AccessControlContextData lValidContextData =
                validateAccessControlContextData(pRoleToken,
                        pAccessControlContext, pFieldsContainer);
        final TypeAccessControlData lTypeAccessControlData =
                getTypeAccessControl(pRoleToken, lValidContextData);

        lCheckedFieldsContainer.setConfidential(lTypeAccessControlData.getConfidential());
        lCheckedFieldsContainer.setCreatable(lTypeAccessControlData.getCreatable());
        lCheckedFieldsContainer.setDeletable(lTypeAccessControlData.getDeletable());
        lCheckedFieldsContainer.setUpdatable(lTypeAccessControlData.getUpdatable());

        // Copy the attributes of the fields container (no check is need)
        lCheckedFieldsContainer.addAttributes(pFieldsContainer.getAllAttributes());
        // Set field access controls attributes to field
        lCheckedFieldsContainer.addAttributes(AttributesUtils.transformIntoSerializationAttributes(lTypeAccessControlData.getExtendedAttributes()));

        final Collection<? extends org.topcased.gpm.business.serialization.data.Field> lNotCheckedFields =
                pFieldsContainer.getFields();

        // The three following stuctures must be re build for the proxy
        final Set<org.topcased.gpm.business.serialization.data.Field> lCheckedTopLevelFields =
                lCheckedFieldsContainer.getTopLevelFields();
        final Map<String, org.topcased.gpm.business.serialization.data.Field> lCheckedFieldsId =
                lCheckedFieldsContainer.getFieldsIdMap();
        final Map<String, org.topcased.gpm.business.serialization.data.Field> lCheckedFieldsKey =
                lCheckedFieldsContainer.getFieldsKeyMap();

        // Each field of the initial container is checked
        for (org.topcased.gpm.business.serialization.data.Field lNotCheckedField : lNotCheckedFields) {
            final org.topcased.gpm.business.serialization.data.Field lCheckedField =
                    getCheckedField(pRoleToken, lValidContextData,
                            lNotCheckedField);

            // Top level field
            lCheckedTopLevelFields.add(lCheckedField);
            // Map <Id, Field>
            lCheckedFieldsId.put(lCheckedField.getId(), lCheckedField);
            // Map <LebelKey, Field>
            lCheckedFieldsKey.put(lCheckedField.getLabelKey(), lCheckedField);
        }

        return lCheckedFieldsContainer;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService.getCheckedLinksFieldsContainer
     *      (String, AccessControlContextData, FIELDS_CONTAINER)
     */
    public <FIELDS_CONTAINER extends CacheableFieldsContainer> FIELDS_CONTAINER getCheckedLinksFieldsContainer(
            String pRoleToken, AccessControlContextData pAccessControlContext,
            FIELDS_CONTAINER pFieldsContainer) {

        FIELDS_CONTAINER lCheckFields =
                getCheckedFieldsContainer(pRoleToken, pAccessControlContext,
                        pFieldsContainer);
        //If the links within the container are confidential, then the user wouldn't be able to see them 
        if (lCheckFields.getConfidential()) {
            lCheckFields = null;
        }
        return lCheckFields;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#applyAccessControls(java.lang.String,
     *      org.topcased.gpm.business.authorization.service.AccessControlContextData,
     *      org.topcased.gpm.business.serialization.data.Field)
     */
    @Override
    public final void applyAccessControls(final String pRoleToken,
            final AccessControlContextData pAccessControlContextData,
            org.topcased.gpm.business.serialization.data.Field pField) {
        FieldAccessControlData lFacd =
                getFieldAccessControl(pRoleToken, pAccessControlContextData,
                        pField.getId());
        pField.setConfidential(lFacd.getAccess().getConfidential());
        pField.setExportable(lFacd.getAccess().getExportable());
        pField.setMandatory(lFacd.getAccess().getMandatory());
        pField.setUpdatable(lFacd.getAccess().getUpdatable());

        // Set field access controls attributes to field
        pField.setAttributes(AttributesUtils.transformIntoSerializationAttributes(lFacd.getExtendedAttributes()));
    }

    /**
     * Create a proxy 'Checked' on a Field before applying access control.
     * 
     * @param pRoleToken
     *            The session token
     * @param pAccessControlContextData
     *            The access control context to apply
     * @param pField
     *            The initial Field
     * @return The Field with a proxy 'Checked'
     */
    private <FIELD extends org.topcased.gpm.business.serialization.data.Field> FIELD getCheckedField(
            String pRoleToken,
            AccessControlContextData pAccessControlContextData, FIELD pField) {
        // Create a proxy on the field, for keeping this data safe without full copy
        final FIELD lCheckedField = CheckedObjectGenerator.create(pField);
        final FieldAccessControlData lAccessControlData =
                getFieldAccessControl(pRoleToken, pAccessControlContextData,
                        pField.getId());
        final FieldAccessData lAccessControl = lAccessControlData.getAccess();

        lCheckedField.setConfidential(lAccessControl.getConfidential());
        lCheckedField.setExportable(lAccessControl.getExportable());
        lCheckedField.setMandatory(lAccessControl.getMandatory());
        lCheckedField.setUpdatable(lAccessControl.getUpdatable());

        // Set field access controls attributes to field
        lCheckedField.setAttributes(AttributesUtils.transformIntoSerializationAttributes(lAccessControlData.getExtendedAttributes()));

        // For multiple fields, sub fields are checked too
        if (pField instanceof MultipleField) {
            final MultipleField lMultipleField = (MultipleField) pField;

            if (lMultipleField.getFields() != null) {
                final List<org.topcased.gpm.business.serialization.data.Field> lCheckedSubFields =
                        new ArrayList<org.topcased.gpm.business.serialization.data.Field>();

                for (org.topcased.gpm.business.serialization.data.Field lSubField : lMultipleField.getFields()) {
                    lCheckedSubFields.add(getCheckedField(pRoleToken,
                            pAccessControlContextData, lSubField));
                }
                ((MultipleField) lCheckedField).setFields(lCheckedSubFields);
            }
        }
        return lCheckedField;
    }

    /**
     * Create a proxy 'Checked' on a Values Container before applying access
     * control.
     * <p>
     * Retrieve the fields that respect the field access flag.
     * <p>
     * If a flag is not defined, the access property is not use.
     * <p>
     * Eg: Get fields that are not confidential and can be export.
     * 
     * <pre>
     * getCheckedValuesContainer(roleToken, accessControlContext,
     *         cacheableFieldsContainer, cacheableValuesContaineer, NOT_CONFIDENTIAL
     *                 | FIELD_EXPORT)
     * </pre>
     * 
     * Note: If a field access is not defined, the access control property will
     * be ignore.<br />
     * e.g: If the field access 'EXPORT' is not set, it doesn't means that the
     * property is 'NOT_EXPORT'. It means that the property will be ignore.
     * 
     * @param pRoleToken
     *            The session token
     * @param pAccessControlContext
     *            The access control context to apply
     * @param pFieldsContainer
     *            The Field Container associated to the values Container
     * @param pValuesContainer
     *            The initial Values Container
     * @param pFieldAccess
     *            Field access to use
     * @return The Values Container with a proxy 'Checked'
     */
    // TODO declare in interface
    public <VALUES_CONTAINER extends CacheableValuesContainer> VALUES_CONTAINER getCheckedValuesContainer(
            String pRoleToken, AccessControlContextData pAccessControlContext,
            CacheableFieldsContainer pFieldsContainer,
            VALUES_CONTAINER pValuesContainer, final long pFieldAccess) {
        // Create a proxy on the value container, for keeping this data safe without full copy
        final VALUES_CONTAINER lCheckedValuesContainer =
                CheckedObjectGenerator.create(pValuesContainer);
        final AccessControlContextData lValidContextData =
                validateAccessControlContextData(pRoleToken,
                        pAccessControlContext, pValuesContainer);
        final Collection<? extends org.topcased.gpm.business.serialization.data.Field> lAllFields =
                pFieldsContainer.getFields();
        // The map could contain confidential values
        final Map<String, Object> lNotCheckedMap =
                pValuesContainer.getValuesMap();
        // Map of the proxy: empty
        final Map<String, Object> lCheckedMap =
                lCheckedValuesContainer.getValuesMap();

        final FieldAccess lFieldAccess =
                FieldAccess.getFieldAccess(pFieldAccess);

        // Copy the values of the initial Values container to the Checked proxy
        for (org.topcased.gpm.business.serialization.data.Field lField : lAllFields) {
            // Copy only fields according to the access
            if (hasAccess(pRoleToken, lValidContextData, lFieldAccess, lField)) {
                final String lFieldLabelKey = lField.getLabelKey();
                if (lField instanceof MultipleField) {
                    MultipleField lMultipleField = (MultipleField) lField;
                    if (lMultipleField.isMultivalued()) {
                        final Object lChecked =
                                handleMultipleMultivaluedField(pRoleToken,
                                        lValidContextData, lNotCheckedMap,
                                        lFieldAccess, lMultipleField);
                        if (lChecked != null) {
                            lCheckedMap.put(lFieldLabelKey, lChecked);
                        }
                    }
                    else {
                        final Map<String, Object> lChecked =
                                handleMultipleField(pRoleToken,
                                        lValidContextData, lNotCheckedMap,
                                        lFieldAccess, lMultipleField);
                        if (MapUtils.isNotEmpty(lChecked)) {
                            lCheckedMap.put(lFieldLabelKey, lChecked);
                        }
                    }
                }
                else {
                    final Object lNotCheckedValue =
                            lNotCheckedMap.get(lFieldLabelKey);
                    // Hash tables doesn't accept null elements
                    if (lNotCheckedValue != null) {
                        lCheckedMap.put(lFieldLabelKey, lNotCheckedValue);
                    }
                }
            }
        }
        return lCheckedValuesContainer;
    }

    /**
     * Handle multiple field access checking.
     * <p>
     * Check the sub-field access.
     * </p>
     * 
     * @param pRoleToken
     *            Role token
     * @param pValidContextData
     *            Access control
     * @param pNotCheckedMap
     *            Not checked fields (all)
     * @param pFieldAccess
     *            Access to consider
     * @param pField
     *            Multiple field.
     * @return Checked fields.
     * @throws InvalidTokenException
     *             If the token is invalid
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> handleMultipleField(String pRoleToken,
            final AccessControlContextData pValidContextData,
            final Map<String, Object> pNotCheckedMap,
            final FieldAccess pFieldAccess, MultipleField pField)
        throws InvalidTokenException {
        Map<String, Object> lNotCheckedSubFieldMap =
                (Map<String, Object>) pNotCheckedMap.get(pField.getLabelKey());
        Map<String, Object> lCheckedSubFields =
                checkSubField(pRoleToken, pValidContextData, pFieldAccess,
                        pField, lNotCheckedSubFieldMap);
        return lCheckedSubFields;
    }

    /**
     * Check the sub-fields access.
     * 
     * @param pRoleToken
     *            Role token
     * @param pValidContextData
     *            Access control
     * @param pNotCheckedMap
     *            Not checked fields (all)
     * @param pFieldAccess
     *            Access to consider
     * @param pField
     *            Multiple field.
     * @param pNotCheckedSubField
     *            Not checked fields.
     * @return Checked fields.
     * @throws InvalidTokenException
     *             If the roletoken is invalid.
     */
    private Map<String, Object> checkSubField(String pRoleToken,
            final AccessControlContextData pValidContextData,
            final FieldAccess pFieldAccess, MultipleField pField,
            Map<String, Object> pNotCheckedSubField)
        throws InvalidTokenException {
        final Map<String, Object> lCheckedSubFields =
                new HashMap<String, Object>();
        if (MapUtils.isNotEmpty(pNotCheckedSubField)) {
            for (org.topcased.gpm.business.serialization.data.Field lSubField : pField.getFields()) {
                if (hasAccess(pRoleToken, pValidContextData, pFieldAccess,
                        lSubField)) {
                    Object lNotCheckedValue =
                            pNotCheckedSubField.get(lSubField.getLabelKey());
                    lCheckedSubFields.put(lSubField.getLabelKey(),
                            lNotCheckedValue);
                }
            }
        }
        return lCheckedSubFields;
    }

    /**
     * Handle multiple-multivalued field access checking.
     * <p>
     * Check the sub-field access.
     * </p>
     * 
     * @param pRoleToken
     *            Role token
     * @param pValidContextData
     *            Access control
     * @param pNotCheckedMap
     *            Not checked fields (all)
     * @param pFieldAccess
     *            Access to consider
     * @param pField
     *            Multiple field.
     * @return Checked fields. (List or Map), Null if the value.
     * @throws InvalidTokenException
     */
    @SuppressWarnings("unchecked")
    private Object handleMultipleMultivaluedField(String pRoleToken,
            final AccessControlContextData pValidContextData,
            final Map<String, Object> pNotCheckedMap,
            final FieldAccess pFieldAccess, MultipleField pField)
        throws InvalidTokenException {
        final Object lCheckedValues;
        Object lNotCheckedValues = pNotCheckedMap.get(pField.getLabelKey());
        //Contains several values (lines)
        if (lNotCheckedValues instanceof List) {
            final List<Map<String, Object>> lCheckedLines =
                    new ArrayList<Map<String, Object>>();
            final List<Map<String, Object>> lNotCheckedLines =
                    (List<Map<String, Object>>) pNotCheckedMap.get(pField.getLabelKey());
            if (CollectionUtils.isNotEmpty(lNotCheckedLines)) {
                for (Map<String, Object> lNotCheckedLine : lNotCheckedLines) {
                    Map<String, Object> lCheckedValue =
                            checkSubField(pRoleToken, pValidContextData,
                                    pFieldAccess, pField, lNotCheckedLine);
                    if (MapUtils.isNotEmpty(lCheckedValue)) {
                        lCheckedLines.add(lCheckedValue);
                    }
                }
            }
            if (!CollectionUtils.isEmpty(lCheckedLines)) {
                lCheckedValues = lCheckedLines;
            }
            else {
                lCheckedValues = null;
            }
        }
        //Contains only one line.
        else {
            Map<String, Object> lCheckedLine =
                    handleMultipleField(pRoleToken, pValidContextData,
                            pNotCheckedMap, pFieldAccess, pField);
            if (MapUtils.isNotEmpty(lCheckedLine)) {
                lCheckedValues = lCheckedLine;
            }
            else {
                lCheckedValues = null;
            }
        }
        return lCheckedValues;
    }

    /**
     * Test the field access.
     * <p>
     * Test the field access according to the specified access.
     * </p>
     * 
     * @param pRoleToken
     *            Role token
     * @param pValidContextData
     *            Access control
     * @param pFieldAccess
     *            Access of the field
     * @param pField
     *            Field to test
     * @return Are the field's access according to the specified access rights.
     * @throws InvalidTokenException
     *             If the token is invalid.
     */
    private boolean hasAccess(String pRoleToken,
            final AccessControlContextData pValidContextData,
            final FieldAccess pFieldAccess,
            org.topcased.gpm.business.serialization.data.Field pField)
        throws InvalidTokenException {
        boolean lFieldAccessFlag = true;
        if (!hasGlobalAdminRole(pRoleToken)) {
            FieldAccessData lFieldAccessData =
                    getFieldAccess(pRoleToken, pValidContextData,
                            pField.getId());
            if (lFieldAccessData != null) {
                //The flag is false if the given flag defines the field access AND this 
                //field access doesn't
                //correspond to the access control.
                if (pFieldAccess.isNotConfidential()
                        && (lFieldAccessData.getConfidential().compareTo(
                                !pFieldAccess.isNotConfidential()) != 0)) {
                    lFieldAccessFlag = false;
                }

                if (pFieldAccess.isMandatory()
                        && (lFieldAccessData.getMandatory().compareTo(
                                pFieldAccess.isMandatory()) != 0)) {
                    lFieldAccessFlag = false;
                }

                if (pFieldAccess.isNotMandatory()
                        && (lFieldAccessData.getMandatory().compareTo(
                                !pFieldAccess.isNotMandatory()) != 0)) {
                    lFieldAccessFlag = false;
                }

                if (pFieldAccess.isUpdatable()
                        && (lFieldAccessData.getUpdatable().compareTo(
                                pFieldAccess.isUpdatable()) != 0)) {
                    lFieldAccessFlag = false;
                }
                if (pFieldAccess.isExportable()
                        && (lFieldAccessData.getExportable().compareTo(
                                pFieldAccess.isExportable()) != 0)) {
                    lFieldAccessFlag = false;
                }
            }
        }
        return lFieldAccessFlag;
    }

    /**
     * Compute a validate access control context data.
     * 
     * @param pRoleToken
     *            The role token
     * @param pAccessControlContextData
     *            The context data to validate
     * @param pContainer
     *            An attribute container
     * @return The new context data
     */
    private AccessControlContextData validateAccessControlContextData(
            String pRoleToken,
            AccessControlContextData pAccessControlContextData,
            CacheableAttributesContainer pContainer) {
        final AccessControlContextData lValidContextData =
                new AccessControlContextData(pAccessControlContextData);

        // If no role token -> skip validation
        if (StringUtils.isBlank(pRoleToken)) {
            return lValidContextData;
        }

        String lUserRoleName = getRoleNameFromToken(pRoleToken);

        // Use the id of the container like default criteria
        if (StringUtils.equals(lValidContextData.getContainerTypeId(),
                DEFAULT_ACCESS_CONTROL_USED)) {
            if (pContainer == null) {
                lValidContextData.setContainerTypeId(ACCESS_CONTROL_NOT_USED);
            }
            else {
                // For a values container used the associated type (fields container)
                if (pContainer instanceof CacheableValuesContainer) {
                    lValidContextData.setContainerTypeId(((CacheableValuesContainer) pContainer).getTypeId());
                }
                else {
                    lValidContextData.setContainerTypeId(pContainer.getId());
                }
            }
        }

        // Use product associated to the session like default criteria
        if (StringUtils.equals(lValidContextData.getProductName(),
                DEFAULT_ACCESS_CONTROL_USED)) {
            lValidContextData.setProductName(getProductNameFromSessionToken(pRoleToken));
        }

        // If the container is a values container, use this id for override role
        if (StringUtils.equals(lValidContextData.getValuesContainerId(),
                DEFAULT_ACCESS_CONTROL_USED)) {
            if (pContainer instanceof CacheableValuesContainer) {
                lValidContextData.setValuesContainerId(pContainer.getId());
            }
            else {
                lValidContextData.setValuesContainerId(ACCESS_CONTROL_NOT_USED);
            }
        }

        // Overridde the role if the values container is defined
        if (!StringUtils.equals(lValidContextData.getValuesContainerId(),
                ACCESS_CONTROL_NOT_USED)
                && StringUtils.isNotBlank(lValidContextData.getValuesContainerId())) {
            lValidContextData.setRoleName(getOverridenRoleName(pRoleToken,
                    lValidContextData.getValuesContainerId()));
        }
        // Use user role like default criteria
        else if (StringUtils.equals(lValidContextData.getRoleName(),
                DEFAULT_ACCESS_CONTROL_USED)) {
            lValidContextData.setRoleName(lUserRoleName);
        }
        // Only administrator can use another role
        //else if (!(hasAdminAccess(pRoleToken) || lUserRoleName.equals(lValidContextData.getRoleName()))) {
        //    throw new InvalidTokenException(
        //            "Only admin can use another role to check access rights");
        //}

        // Only sheets can have a state
        if (StringUtils.equals(lValidContextData.getStateName(),
                DEFAULT_ACCESS_CONTROL_USED)) {
            // Use the sheet state if it exists
            if (pContainer instanceof CacheableSheet) {
                lValidContextData.setStateName(((CacheableSheet) pContainer).getCurrentStateName());
            }
            // No use criteria if the container is not a sheet
            else {
                lValidContextData.setStateName(ACCESS_CONTROL_NOT_USED);
            }
        }
        // If no specified, no criteria on the visible type
        if (StringUtils.equals(lValidContextData.getVisibleTypeId(),
                DEFAULT_ACCESS_CONTROL_USED)) {
            lValidContextData.setVisibleTypeId(ACCESS_CONTROL_NOT_USED);
        }

        return lValidContextData;
    }

    /**
     * {@inheritDoc}
     * 
     * @see 
     *      org.topcased.gpm.business.authorization.service.AuthorizationService#
     *      getTransitionAccessControl(java.lang.String,
     */
    @Override
    @SuppressWarnings("unchecked")
    public TransitionAccessControlData getTransitionAccessControl(
            String pRoleToken,
            AccessControlContextData pAccessControlContextData,
            String pTransitionName) throws InvalidTokenException {
        final AccessControlContextData lValidAccessControl =
                validateAccessControlContextData(pRoleToken,
                        pAccessControlContextData, null);
        final String lCacheKey =
                computeAccessControlCacheKey(lValidAccessControl,
                        "TransitionAccessCtl|", "_transition|", pTransitionName);

        // Try to get the access control from the cache.
        Element lAccessControlfromCache = accessControlCache.get(lCacheKey);

        TransitionAccessControlData lTacd = null;

        if (null != lAccessControlfromCache) {
//            if (staticLOGGER.isDebugEnabled()) {
//                staticLOGGER.debug("Cache hit for key=" + lCacheKey);
//
//                staticLOGGER.debug("Current cache stats: "
//                        + accessControlCache.getStatistics());
//            }
            lTacd =
                    (TransitionAccessControlData) lAccessControlfromCache.getValue();
        }
        else {
            Collection<TransitionAccessControl> lTacs;
            lTacs =
                    getTransitionAccessControlDao().getApplicables(
                            lValidAccessControl.getProductName(),
                            lValidAccessControl.getStateName(),
                            lValidAccessControl.getRoleName(),
                            lValidAccessControl.getContainerTypeId(),
                            pTransitionName);

            lTacd = new TransitionAccessControlData();
            fillAccessControlData(lTacd, lValidAccessControl,
                    getBusinessProcessName(pRoleToken));

            //Set default access
            lTacd.setAllowed(TRANSITION_DEFAULT_ALLOWED.getAsBoolean());

            if (lTacs.size() > 1) {
                lTacs = AuthorizationUtils.getSortedAccessControls(lTacs);
            }
            for (TransitionAccessControl lTransitionAccessControl : lTacs) {
                lTacd.setAllowed(lTransitionAccessControl.isAllowed());
                setExtendedAttributes(lTacd, lTransitionAccessControl);
            }
            lTacd.setTransitionName(pTransitionName);
            putInCache(lCacheKey, lTacd);
        }
        return lTacd;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#getTypeAccessControl(java.lang.String,
     *      java.lang.String, java.lang.String, java.lang.String,
     *      java.lang.String)
     * @deprecated Since 1.7
     * @see AuthorizationService#getTypeAccessControl(String,
     *      AccessControlContextData)
     */
    @Override
    public TypeAccessControlData getTypeAccessControl(String pRoleToken,
            String pProcessName, String pProductName, String pStateName,
            String pTypeId) throws InvalidTokenException {
        AccessControlContextData lAccessControlContextData =
                new AccessControlContextData();

        lAccessControlContextData.setRoleName(getRoleNameFromToken(pRoleToken));
        lAccessControlContextData.setProductName(pProductName);
        lAccessControlContextData.setStateName(pStateName);
        lAccessControlContextData.setContainerTypeId(pTypeId);

        return getTypeAccessControl(pRoleToken, lAccessControlContextData);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#getTypeAccessControl(java.lang.String,
     *      org.topcased.gpm.business.authorization.service.AccessControlContextData)
     */
    @Override
    @SuppressWarnings("unchecked")
    public TypeAccessControlData getTypeAccessControl(String pRoleToken,
            final AccessControlContextData pAccessControlContextData)
        throws InvalidTokenException {
        final AccessControlContextData lValidAccessControl =
                validateAccessControlContextData(pRoleToken,
                        pAccessControlContextData, null);
        final String lCacheKey =
                computeAccessControlCacheKey(lValidAccessControl,
                        "TypeAccessCtl|", null, null);

        // Try to get the access control from the cache.
        Element lElem = accessControlCache.get(lCacheKey);

        TypeAccessControlData lStacd;

        if (null != lElem) {
//            if (staticLOGGER.isDebugEnabled()) {
//                staticLOGGER.debug("Cache hit for key=" + lCacheKey);
//                staticLOGGER.debug("Current cache stats: "
//                        + accessControlCache.getStatistics());
//            }
            lStacd = (TypeAccessControlData) lElem.getValue();
        }
        else {
            Collection<TypeAccessControl> lTypeAccessControls =
                    getTypeAccessControlDao().getApplicables(
                            lValidAccessControl.getProductName(),
                            lValidAccessControl.getStateName(),
                            lValidAccessControl.getRoleName(),
                            lValidAccessControl.getContainerTypeId());

            if (lTypeAccessControls.size() > 1) {
                lTypeAccessControls =
                        AuthorizationUtils.getSortedAccessControls(lTypeAccessControls);
            }

//            if (staticLOGGER.isDebugEnabled()) {
//                staticLOGGER.debug("TAC Applicables number = "
//                        + lTypeAccessControls.size());
//                staticLOGGER.debug("TAC Access control context values");
//                staticLOGGER.debug("Product name = "
//                        + lValidAccessControl.getProductName());
//                staticLOGGER.debug("State name = "
//                        + lValidAccessControl.getStateName());
//                staticLOGGER.debug("Rolen name = "
//                        + lValidAccessControl.getRoleName());
//                staticLOGGER.debug("container id = "
//                        + lValidAccessControl.getContainerTypeId());
//            }

            lStacd = new TypeAccessControlData();

            fillAccessControlData(lStacd, lValidAccessControl,
                    getBusinessProcessName(pRoleToken));

            lStacd.setCreatable(Boolean.valueOf(TYPE_DEFAULT_CREATABLE.getAsString()));
            lStacd.setUpdatable(Boolean.valueOf(TYPE_DEFAULT_UPDATABLE.getAsString()));
            lStacd.setDeletable(Boolean.valueOf(TYPE_DEFAULT_DELETABLE.getAsString()));
            lStacd.setConfidential(Boolean.valueOf(TYPE_DEFAULT_CONFIDENTIAL.getAsString()));

            for (TypeAccessControl lTypeAccessControl : lTypeAccessControls) {
                if (lTypeAccessControl.getCreatable() != null) {
                    lStacd.setCreatable(lTypeAccessControl.getCreatable());
                }
                if (lTypeAccessControl.getUpdatable() != null) {
                    lStacd.setUpdatable(lTypeAccessControl.getUpdatable());
                }
                if (lTypeAccessControl.getDeletable() != null) {
                    lStacd.setDeletable(lTypeAccessControl.getDeletable());
                }
                if (lTypeAccessControl.getConfidential() != null) {
                    lStacd.setConfidential(lTypeAccessControl.getConfidential());
                }
                setExtendedAttributes(lStacd, lTypeAccessControl);
            }
            putInCache(lCacheKey, lStacd);
        }
        return lStacd;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#getSheetAccessControl(java.lang.String,
     *      org.topcased.gpm.business.authorization.service.AccessControlContextData,
     *      java.lang.String)
     */
    @Override
    public TypeAccessControlData getSheetAccessControl(final String pRoleToken,
            final AccessControlContextData pAccessControlContextData,
            final String pSheetId) throws InvalidTokenException {
        // Get a copy because a value of the context is modified
        final AccessControlContextData lNewAccessControl =
                new AccessControlContextData(pAccessControlContextData);

        lNewAccessControl.setValuesContainerId(pSheetId);
        return getTypeAccessControl(pRoleToken, lNewAccessControl);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService
     *      #getSheetAccessControl(java.lang.String, java.lang.String)
     */
    @Override
    public TypeAccessControlData getSheetAccessControl(String pRoleToken,
            String pSheetId) {
        final String lRoleName = getRoleNameFromToken(pRoleToken);

        final CacheableSheet lSheetData =
                getSheetService().getCacheableSheet(pSheetId,
                        CacheProperties.IMMUTABLE);
        final AccessControlContextData lAccessControlContextData =
                new AccessControlContextData();

        lAccessControlContextData.setRoleName(lRoleName);
        lAccessControlContextData.setProductName(lSheetData.getProductName());
        lAccessControlContextData.setStateName(lSheetData.getCurrentStateName());
        lAccessControlContextData.setContainerTypeId(lSheetData.getTypeId());
        lAccessControlContextData.setValuesContainerId(pSheetId);

        return getTypeAccessControl(pRoleToken, lAccessControlContextData);
    }

    /**
     * hasRoleOnProduct.
     * 
     * @param pRoleToken
     *            Role session token.
     * @param pProductName
     *            the product name
     * @return if has a role on the product
     */
    // TODO declare in interface
    public boolean hasRoleOnProduct(String pRoleToken, String pProductName) {

        // Assume the 'admin' role has access on all products
        if (hasAdminAccess(pRoleToken)) {
            return true;
        }

        EndUser lUser = getUserFromToken(pRoleToken);
        final String lProcessName =
                getAuthService().getProcessNameFromToken(pRoleToken);
        // tests if has an admin role
        for (Role lAdminRole : lUser.getAdminRoles()) {
            if (lAdminRole.getBusinessProcess().getName().equals(lProcessName)) {
                return true;
            }
        }

        Role lUserCurrentRole = getRole(pRoleToken);
        // tests if has a role on the product
        for (RolesForProduct lRfp : lUser.getRolesForProducts()) {
            if (lRfp.getProduct().getName().equals(pProductName)) {
                for (Role lRole : lRfp.getRoles()) {
                    if (lRole.equals(lUserCurrentRole)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#getApplicationActionAccessControlForType(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public ActionAccessControlData getApplicationActionAccessControlForType(
            String pRoleToken, String pContainerTypeId, String pActionKey) {
        final AccessControlContextData lAccessContext;
        lAccessContext =
                getAccessControlContextData(pRoleToken, null, pContainerTypeId,
                        null, null);
        return getApplicationActionAccessControl(pRoleToken, lAccessContext,
                pActionKey);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#getApplicationActionAccessControl(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    @Override
    public ActionAccessControlData getApplicationActionAccessControl(
            String pRoleToken, String pSheetId, String pActionKey) {
        final AccessControlContextData lAccessContext;

        if (pSheetId == null || StringUtils.isBlank(pSheetId)) {
            lAccessContext =
                    getAccessControlContextData(pRoleToken, null, null, null,
                            pSheetId);
        }
        else {
            final CacheableSheet lCachedSheet =
                    getSheetService().getCacheableSheet(pSheetId,
                            CacheProperties.IMMUTABLE);

            lAccessContext =
                    getAccessControlContextData(pRoleToken,
                            lCachedSheet.getCurrentStateName(),
                            lCachedSheet.getTypeId(), null, pSheetId);
        }

        return getApplicationActionAccessControl(pRoleToken, lAccessContext,
                pActionKey);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#getApplicationActionAccessControl(java.lang.String,
     *      java.lang.String, java.util.List)
     */
    @Override
    public List<ActionAccessControlData> getApplicationActionAccessControl(
            String pRoleToken, String pSheetId, List<String> pActionKeys) {
        final AccessControlContextData lAccessContext;

        if (pSheetId == null || StringUtils.isBlank(pSheetId)) {
            lAccessContext =
                    getAccessControlContextData(pRoleToken, null, null, null,
                            pSheetId);
        }
        else {
            final CacheableSheet lCachedSheet =
                    getSheetService().getCacheableSheet(pSheetId,
                            CacheProperties.IMMUTABLE);

            lAccessContext =
                    getAccessControlContextData(pRoleToken,
                            lCachedSheet.getCurrentStateName(),
                            lCachedSheet.getTypeId(), null, pSheetId);
        }

        return getApplicationActionAccessControl(pRoleToken, lAccessContext,
                pActionKeys);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#getApplicationActionAccessControl(java.lang.String,
     *      org.topcased.gpm.business.authorization.service.AccessControlContextData,
     *      java.util.List)
     */
    @Override
    public List<ActionAccessControlData> getApplicationActionAccessControl(
            String pRoleToken,
            AccessControlContextData pAccessControlContextData,
            List<String> pActionKeys) throws IllegalArgumentException {
        // The blank token can be used in getApplicationActionAccessControl
        final List<ActionAccessControlData> lAccessControlList =
                new ArrayList<ActionAccessControlData>(pActionKeys.size());

        for (String lActionKey : pActionKeys) {
            // Blank role tokens are handled in getApplicationActionAccessControl() method
            lAccessControlList.add(getApplicationActionAccessControl(
                    pRoleToken, pAccessControlContextData, lActionKey));
        }

        return lAccessControlList;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#getApplicationActionAccessControl(java.lang.String,
     *      org.topcased.gpm.business.authorization.service.AccessControlContextData,
     *      java.lang.String)
     */
    @Override
    @SuppressWarnings("unchecked")
    public ActionAccessControlData getApplicationActionAccessControl(
            String pRoleToken,
            AccessControlContextData pAccessControlContextData,
            String pActionKey) throws IllegalArgumentException {
        if (StringUtils.isBlank(pActionKey)) {
            throw new IllegalArgumentException("Argument pActionKey is blank");
        }

        final AccessControlContextData lValidAccessControl =
                validateAccessControlContextData(pRoleToken,
                        pAccessControlContextData, null);
        final String lCacheKey =
                computeAccessControlCacheKey(lValidAccessControl,
                        "ApplicationAccessCtl|", "_actionKey|", pActionKey);

        // Try to get the access control from the cache.
        Element lElem = accessControlCache.get(lCacheKey);

        ActionAccessControlData lAacd;

        if (null != lElem) {
//            if (staticLOGGER.isDebugEnabled()) {
//                staticLOGGER.debug("Cache hit for key=" + lCacheKey);
//
//                staticLOGGER.debug("Current cache stats: "
//                        + accessControlCache.getStatistics());
//            }
            lAacd = (ActionAccessControlData) lElem.getValue();
        }
        else {
            lAacd = new ActionAccessControlData();

            if (StringUtils.isBlank(pRoleToken)) {
                lAacd = new ActionAccessControlData();
                lAacd.setLabelKey(pActionKey);
                lAacd.setEnabled(ACTION_DEFAULT_NOROLE_ENABLED.getAsBoolean());
                lAacd.setConfidential(ACTION_DEFAULT_NOROLE_CONFIDENTIAL.getAsBoolean());
                return lAacd;
            }

            Collection<AppliActionAccessControl> lApplicables =
                    getAppliActionAccessControlDao().getApplicables(
                            lValidAccessControl.getProductName(),
                            lValidAccessControl.getStateName(),
                            lValidAccessControl.getRoleName(),
                            lValidAccessControl.getContainerTypeId(),
                            pActionKey);

            if (lApplicables.size() > 1) {
                lApplicables =
                        AuthorizationUtils.getSortedAccessControls(lApplicables);
            }

            fillAccessControlData(lAacd, lValidAccessControl,
                    getBusinessProcessName(pRoleToken));
            lAacd.setLabelKey(pActionKey);

            // If no specific access control is defined for this action,
            // simply return default values
            lAacd.setEnabled(AccessControlConstant.ACTION_DEFAULT_ENABLED.getAsBoolean());
            lAacd.setConfidential(AccessControlConstant.ACTION_DEFAULT_CONFIDENTIAL.getAsBoolean());

            for (AppliActionAccessControl lAaac : lApplicables) {
                if (null != lAaac.getEnabled()) {
                    lAacd.setEnabled(lAaac.getEnabled());
                }
                if (null != lAaac.getConfidential()) {
                    lAacd.setConfidential(lAaac.getConfidential());
                }
                setExtendedAttributes(lAacd, lAaac);
            }

//            if (staticLOGGER.isDebugEnabled()) {
//                staticLOGGER.debug("Cache miss for key=" + lCacheKey);
//            }
            putInCache(lCacheKey, lAacd);
        }
        return lAacd;
    }

    /**
     * {@inheritDoc} An admin access is define on instance if it is set on a
     * role and an actionKey (no product specified) and if the given role is
     * defined on total instance. (not on a specified product).
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#isAdminAccessDefinedOnInstance(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public boolean isAdminAccessDefinedOnInstance(String pRoleToken, String pActionKey) {
        boolean lResult = false;

        String lRoleName = getRoleNameFromToken(pRoleToken);

        // Check if an admin access is set on role and actionKey
        AccessControlContextData lAccessControlContextData = new AccessControlContextData();
        lAccessControlContextData.setRoleName(lRoleName);

        AdminAccessControlData lAdminAccessControlData =
        		getAdminAccessControl(pRoleToken, lAccessControlContextData, pActionKey);

        // If an admin access has been set, check the role definition (instance or product)
        if (lAdminAccessControlData != null) {
            EndUser lUser = getUserFromToken(getUserSessionFromRoleSession(pRoleToken));
            Collection<Role> lRolesList = lUser.getAdminRoles();
            for (Role lRole : lRolesList) {
                if (lRoleName.equals(lRole.getName())) {
                    lResult = true;
                }
            }
        }

        return lResult;
    }

    /**
     * {@inheritDoc} An admin access is set on a specified product if (there are
     * two cases) : 1. An admin access is defined on role, action and product 2.
     * An admin access is defined on role and action (no product specified) and
     * the role is defined on a product.
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#isAdminAccessDefinedOnProduct(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    @Override
    public boolean isAdminAccessDefinedOnProduct(String pRoleToken,
            String pActionKey, String pProductName) {
       
    	return isAdminAccessDefinedOnProduct(pRoleToken, pActionKey, pProductName, getRoleNameFromToken(pRoleToken));
    }
    
    @Override
    public boolean isAdminAccessDefinedOnProduct(String pRoleToken,
            String pActionKey, String pProductName, String pRoleName) {

        boolean lResult = false;
        // Check if an admin access is set on role, action, product
        AccessControlContextData lAccessControlContextDataOnProduct = new AccessControlContextData();
        lAccessControlContextDataOnProduct.setRoleName(pRoleName);
        lAccessControlContextDataOnProduct.setProductName(pProductName);

        AdminAccessControlData lAdminAccessControlDataOnProduct =
                getAdminAccessControl(pRoleToken, lAccessControlContextDataOnProduct, pActionKey);

        if (lAdminAccessControlDataOnProduct != null) {
            return true;
        }

        // Check if an admin access is set on role, action (without product specified).
        AccessControlContextData lAccessControlContextData = new AccessControlContextData();
        lAccessControlContextData.setRoleName(pRoleName);

        AdminAccessControlData lAdminAccessControlData =
                getAdminAccessControl(pRoleToken, lAccessControlContextData, pActionKey);

        // If an admin access has been set, check the role definition (instance of product)
        if (lAdminAccessControlData != null) {
            EndUser lUser = getUserFromToken(getUserSessionFromRoleSession(pRoleToken));

            // If role is defined on product pProductName
            Collection<RolesForProduct> lRolesForProductsList = lUser.getRolesForProducts();
            for (RolesForProduct lRoleForProduct : lRolesForProductsList) {
                if (lRoleForProduct.getProduct().getName().equals(pProductName)) {
                    for (Role lRole : lRoleForProduct.getRoles()) {
                        if (lRole.getName().equals(pRoleName)) {
                            lResult = true;
                        }
                    }
                }
            }

            // If role is defined on all products
            Collection<Role> lRolesList = lUser.getAdminRoles();
            for (Role lRole : lRolesList) {
                if (pRoleName.equals(lRole.getName())) {
                    lResult = true;
                }
            }
        }

        return lResult;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#getAdminAccessControl(java.lang.String,
     *      org.topcased.gpm.business.authorization.service.AccessControlContextData,
     *      java.lang.String)
     */
    @Override
    @SuppressWarnings("unchecked")
    public AdminAccessControlData getAdminAccessControl(String pRoleToken,
            AccessControlContextData pAccessControlContextData,
            String pActionKey) throws IllegalArgumentException {
        if (StringUtils.isBlank(pActionKey)) {
            throw new IllegalArgumentException("Argument pActionKey is blank");
        }

        final AccessControlContextData lValidAccessControl =
        		validateAccessControlContextData(pRoleToken, pAccessControlContextData, null);
        final String lCacheKey =
        		computeAccessControlCacheKey(lValidAccessControl, "AdminAccessCtl|", "_actionKey|", pActionKey);

        // Try to get the access control from the cache.
        Element lElem = accessControlCache.get(lCacheKey);

        AdminAccessControlData lAacd;

        if (null != lElem) { // Cache hit
            lAacd = (AdminAccessControlData) lElem.getValue();
        }
        else {
            lAacd = new AdminAccessControlData();

            if (StringUtils.isBlank(pRoleToken)) {
                lAacd = new AdminAccessControlData();
                lAacd.setLabelKey(pActionKey);
                return lAacd;
            }

            Collection<AdminAccessControl> lApplicables = getAdminAccessControlDao().getApplicables(
            		lValidAccessControl.getProductName(),
            		lValidAccessControl.getRoleName(), pActionKey);

            if (lApplicables.size() == 0) {
                putInCache(lCacheKey, null);
                return null;
            }

            if (lApplicables.size() > 1) {
                lApplicables = AuthorizationUtils.getSortedAccessControls(lApplicables);
            }

            fillAccessControlData(lAacd, lValidAccessControl, getBusinessProcessName(pRoleToken));
            lAacd.setLabelKey(pActionKey);

            for (AdminAccessControl lAaac : lApplicables) {
                setExtendedAttributes(lAacd, lAaac);
                if (lAaac.getProductControl() == null) {
                    lAacd.getContext().setProductName(null);
                }
                else {
                    lAacd.getContext().setProductName(lAaac.getProductControl().getName());
                }

                if (lAaac.getRoleControl() == null) {
                    lAacd.getContext().setRoleName(null);
                }
                else {
                    lAacd.getContext().setRoleName(lAaac.getRoleControl().getName());
                }
            }

            putInCache(lCacheKey, lAacd);
        }
        return lAacd;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#getFilterAccessControl(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public FilterAccessControl getFilterAccessControl(String pRoleToken,
            String pTypeId) {
        FilterAccessDefinitionKey lFilterAccessDefinitionKey =
                new FilterAccessDefinitionKey(getProcessName(pRoleToken),
                        getRoleNameFromToken(pRoleToken), pTypeId);

        FilterAccessDefinition lFilterAccessDefinition =
                filterAccessManager.getElement(lFilterAccessDefinitionKey);

        return lFilterAccessDefinition.getTypeAccessControl();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#getFilterAccessControl(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    @Override
    public FilterAccessControl getFilterAccessControl(String pRoleToken,
            String pTypeId, String pFieldLabel) {
        FilterAccessDefinitionKey lFilterAccessDefinitionKey =
                new FilterAccessDefinitionKey(getProcessName(pRoleToken),
                        getRoleNameFromToken(pRoleToken), pTypeId);

        FilterAccessDefinition lFilterAccessDefinition =
                filterAccessManager.getElement(lFilterAccessDefinitionKey);

        return lFilterAccessDefinition.getFieldsAccessControl().get(pFieldLabel);
    }

    /**
     * Get an access control object from the DB.
     * 
     * @param pClazz
     *            Actual class of the access control
     * @param pAdditionalCriterion
     *            Additional criterion for the request
     * @param pContextHierarchy
     *            the context hierarchy
     * @param pBusinessProcessName
     *            the business process name
     * @param pAccessControlContextData
     *            the access control context data
     * @return The access ctl object corresponding to the given parameters, or
     *         null
     */
    private <AC extends AccessControl> AC getAccessControl(Class<AC> pClazz,
            String pBusinessProcessName,
            AccessControlContextData pAccessControlContextData,
            Criterion pAdditionalCriterion, boolean pContextHierarchy) {
        AC lAccessControl;
        Session lHibernateSession = GpmSessionFactory.getHibernateSession();
        AccessControlFinder<AC> lAcf =
                new AccessControlFinder<AC>(lHibernateSession);

        BusinessProcess lBusinessProcess = null;
        FieldsContainer lFieldsContainer = null;
        Role lRole = null;
        Product lProduct = null;
        Node lState = null;

        lBusinessProcess = getBusinessProcess(pBusinessProcessName);

        if (StringUtils.isNotBlank(pBusinessProcessName)
                && StringUtils.isNotBlank(pAccessControlContextData.getContainerTypeId())) {

            lFieldsContainer =
                    getFieldsContainer(pAccessControlContextData.getContainerTypeId());

            if (StringUtils.isNotBlank(pAccessControlContextData.getStateName())) {
                lState =
                        getStateNode(
                                pAccessControlContextData.getContainerTypeId(),
                                pAccessControlContextData.getStateName());
                if (null == lState) {
                    throw new InvalidNameException(
                            pAccessControlContextData.getStateName(),
                            "Product named "
                                    + pAccessControlContextData.getStateName()
                                    + " invalid");
                }
            }
        }

        if (StringUtils.isNotBlank(pAccessControlContextData.getRoleName())) {
            lRole =
                    getRoleDao().getRole(
                            pAccessControlContextData.getRoleName(),
                            lBusinessProcess);
            if (null == lRole) {
                throw new InvalidNameException(
                        pAccessControlContextData.getRoleName(), "Role named "
                                + pAccessControlContextData.getRoleName()
                                + " invalid");
            }
        }

        if (StringUtils.isNotBlank(pAccessControlContextData.getProductName())) {
            lProduct =
                    getProductDao().getProduct(pBusinessProcessName,
                            pAccessControlContextData.getProductName());
            if (null == lProduct) {
                throw new InvalidNameException(
                        pAccessControlContextData.getProductName(),
                        "Product named "
                                + pAccessControlContextData.getProductName()
                                + " invalid");
            }
        }

        lAccessControl =
                lAcf.getAccessControl(pClazz, lProduct, lState, lRole,
                        lFieldsContainer, pAdditionalCriterion,
                        pContextHierarchy);

        return lAccessControl;
    }

    /**
     * Get an access control object from the DB.
     * 
     * @param pClazz
     *            Actual class of the access control
     * @param pAccessControl
     *            Access control object containing the search criteria
     * @param pAdditionalCriterion
     *            Criterion to add in the query
     * @param pContextHierarchy
     *            the context hierarchy
     * @return The access ctl object corresponding to the given parameters, or
     *         null
     */
    private <AC extends AccessControl> AC getAccessControl(Class<AC> pClazz,
            AccessControlData pAccessControl, Criterion pAdditionalCriterion,
            boolean pContextHierarchy) {

        return getAccessControl(pClazz,
                pAccessControl.getBusinessProcessName(),
                pAccessControl.getContext(), pAdditionalCriterion,
                pContextHierarchy);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#addRole(java.lang.String,
     *      java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void addRole(String pToken, String pLoginName,
            String pBusinessProcessName, String pRoleName)
        throws InvalidNameException {

        if (!hasGlobalAdminRole(pToken)
                && (roleSessions.isTokenValid(pToken) && !isUserUpdatable(
                        pToken, pLoginName))) {
            throw new AuthorizationException(
                    "Admininistrator role or admin access 'user.modify' required.");
        }
        BusinessProcess lBusinessProc =
                getBusinessProcess(pBusinessProcessName);
        Role lRole = getRoleDao().getRole(pRoleName, lBusinessProc);

        if (null == lRole) {
            throw new InvalidNameException(pRoleName, "Invalid role {0}");
        }

        EndUser lUser = getUserFromLogin(pLoginName);
        lUser.addToRoleList(lRole);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#addRole(java.lang.String,
     *      java.lang.String, java.lang.String, java.lang.String,
     *      java.lang.String)
     */
    @Override
    public void addRole(String pRoleToken, String pLoginName,
            String pBusinessProcessName, String pRoleName, String pProductName)
        throws InvalidNameException {
        if (!isUserUpdatable(pRoleToken, pLoginName)) {
            throw new AuthorizationException(
                    "Admininistrator role or admin access 'user.modify' required.");
        }
        BusinessProcess lBusinessProc =
                getBusinessProcess(pBusinessProcessName);
        Product lProduct =
                getProductDao().getProduct(pBusinessProcessName, pProductName);

        if (null == lProduct) {
            throw new InvalidNameException(pProductName,
                    "Product name {0} invalid");
        }

        Role lRole = getRoleDao().getRole(pRoleName, lBusinessProc);
        if (null == lRole) {
            throw new InvalidNameException(pRoleName, "Invalid role {0}");
        }

        EndUser lUser = getUserFromLogin(pLoginName);
        addRole(lUser, lBusinessProc, lRole, pProductName);
    }

    /**
     * Add a role to a user for a specific product.
     * 
     * @param pUser
     *            User.
     * @param pProcess
     *            Business process
     * @param pRole
     *            Role to give to this user.
     * @param pProductName
     *            Name of the product.
     * @throws InvalidNameException
     *             The product is invalid.
     */
    private void addRole(EndUser pUser, BusinessProcess pProcess, Role pRole,
            String pProductName) {
        Product lProduct =
                getProductDao().getProduct(pProcess.getName(), pProductName);

        if (null == lProduct) {
            throw new InvalidNameException(pProductName,
                    "Product name {0} invalid");
        }

        Query lQuery =
                GpmSessionFactory.getHibernateSession().createFilter(
                        pUser.getRolesForProducts(),
                        "where this.product = :product");
        lQuery.setParameter("product", lProduct);

        RolesForProduct lRfp = (RolesForProduct) lQuery.uniqueResult();

        if (null == lRfp) {
            // If no RolesForProduct exist yet for the product, create it.
            lRfp = RolesForProduct.newInstance();
            lRfp.setProduct(lProduct);
            getRolesForProductDao().create(lRfp);
            pUser.addToRolesForProductList(lRfp);
        }

        lRfp.addToRoleList(pRole);
        clearProductNamesCache();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#createRole(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    @Override
    public void createRole(String pToken, String pRoleName,
            String pBusinessProcessName) {
//        if (staticLOGGER.isInfoEnabled()) {
//            staticLOGGER.info("Create role '" + pRoleName + "' for process '"
//                    + pBusinessProcessName + "'");
//        }
        assertGlobalAdminRole(pToken);

        BusinessProcess lProcess = getBusinessProcess(pBusinessProcessName);
        getOrCreateRole(pRoleName, lProcess);
    }

    /**
     * Get a role in the DB, or create it if needed.
     * 
     * @param pRoleName
     *            Role name
     * @param pBusinessProcess
     *            Business process
     * @return Role object.
     */
    private Role getOrCreateRole(String pRoleName,
            BusinessProcess pBusinessProcess) {
        Role lRole = getRoleDao().getRole(pRoleName, pBusinessProcess);
        if (null == lRole) {
            lRole = Role.newInstance();
            lRole.setBusinessProcess(pBusinessProcess);
            lRole.setName(pRoleName);

            getRoleDao().create(lRole);
        }
        return lRole;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#deleteRole(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    @Override
    public void deleteRole(String pRoleToken, String pRoleName,
            String pBusinessProcessName) throws InvalidNameException,
        ConstraintException {
        assertGlobalAdminRole(pRoleToken);

        BusinessProcess lProcess = getBusinessProcess(pBusinessProcessName);

        Role lRole = getRoleDao().getRole(pRoleName, lProcess);
        if (null == lRole) {
            throw new InvalidNameException(pRoleName, "Role {0} does not exist");
        }
        // Check if the role is currently used or not.
        else if (getRoleDao().isRoleUsed(lRole)) {
            throw new ConstraintException("Role '" + pRoleName + "' used.");
        }
        else {
            // Remove all OverriddenRoles for the role
            authorizationService.deleteOverriddenRolesFromRole(pRoleName);

            // Remove the role
            getRoleDao().remove(lRole);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#getRolesNames(java.lang.String)
     */
    @Override
    public Collection<String> getRolesNames(String pBusinessProcessName) {
        return getRoleDao().getAllRoleNames(pBusinessProcessName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#getRoles(java.lang.String,
     *      java.lang.String)
     */
    @Override
    @SuppressWarnings("unchecked")
    public Collection<RoleData> getRoles(String pLoginName,
            String pBusinessProcessName) {
        EndUser lUser = getUserFromLogin(pLoginName);
        BusinessProcess lBusinessProcess =
                getBusinessProcess(pBusinessProcessName);

        Collection<RoleData> lResult = new HashSet<RoleData>();

        // 'Normal' roles
        Query lRfpQuery =
                GpmSessionFactory.getHibernateSession().createFilter(
                        lUser.getRolesForProducts(),
                        "where this.product.businessProcess = :businessProcess");
        lRfpQuery.setParameter("businessProcess", lBusinessProcess);

        Collection<RolesForProduct> lRolesForProductList = lRfpQuery.list();

        for (RolesForProduct lRfp : lRolesForProductList) {
            for (Role lRole : lRfp.getRoles()) {
                RoleData lRoleData = new RoleData();
                lRoleData.setRoleName(lRole.getName());
                lRoleData.setProductName(lRfp.getProduct().getName());
                lResult.add(lRoleData);
            }
        }

        // Admin roles
        Collection<Role> lRoleAdmin =
                roleDao.getRoles(pLoginName, StringUtils.EMPTY,
                        pBusinessProcessName, true, false);

        for (Role lAdminRole : lRoleAdmin) {
            RoleData lRoleData = new RoleData();
            lRoleData.setRoleName(lAdminRole.getName());
            lRoleData.setProductName(null);
            lResult.add(lRoleData);
        }
        return lResult;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#getRoles(java.lang.String,
     *      java.lang.String, java.lang.String,
     *      org.topcased.gpm.business.authorization.service.RoleProperties)
     */
    @SuppressWarnings("unchecked")
    @Override
    public Collection<RoleData> getRoles(String pUserToken,
            String pProductName, String pBusinessProcessName,
            RoleProperties pRoleProperties) {
        final Collection<RoleData> lResult;
        String lLogin = getLoginFromToken(pUserToken);

        //Getting roleOnProduct
        Collection<Role> lRoles =
                roleDao.getRoles(lLogin, pProductName, pBusinessProcessName,
                        false, true);
        final Collection<RoleData> lRoleOnProduct;
        if (CollectionUtils.isNotEmpty(lRoles)) {
            lRoleOnProduct = new ArrayList<RoleData>(lRoles.size());
            for (Role lRole : lRoles) {
                RoleData lRoleData = new RoleData();
                lRoleData.setRoleName(lRole.getName());
                lRoleData.setProductName(pProductName);
                lRoleOnProduct.add(lRoleData);
            }
        }
        else {
            lRoleOnProduct = Collections.emptySet();
        }

        //Getting adminRoles
        lRoles =
                roleDao.getRoles(lLogin, pProductName, pBusinessProcessName,
                        true, false);
        final Collection<RoleData> lAdminRoles;
        if (CollectionUtils.isNotEmpty(lRoles)) {
            lAdminRoles = new ArrayList<RoleData>(lRoles.size());
            for (Role lAdminRole : lRoles) {
                RoleData lRoleData = new RoleData();
                lRoleData.setRoleName(lAdminRole.getName());
                lRoleData.setProductName(pProductName);
                lAdminRoles.add(lRoleData);
            }
        }
        else {
            lAdminRoles = Collections.emptySet();
        }

        lResult = CollectionUtils.union(lRoleOnProduct, lAdminRoles);
        return lResult;
    }

    /**
     * Can current role update another user roles ? Roles can be updated by
     * global admin role, or by an admin access set on instance. For an admin
     * access set on product, roles can be updated on the product, and not for
     * admin role.
     * 
     * @param pRoleToken
     *            role session token
     * @param pRoles
     *            roles to be updated
     * @return true if user is updatable
     */
    protected boolean isRoleUpdatable(String pRoleToken,
            Collection<RoleData> pRoles) {

        boolean lIsAdmin = hasGlobalAdminRole(pRoleToken);
        if (lIsAdmin) {
            return true;
        }

        boolean lIsAdminAccessOnInstance =
        		isAdminAccessDefinedOnInstance(pRoleToken, AdministrationAction.USER_MODIFY.getActionKey())
                || isAdminAccessDefinedOnInstance(pRoleToken, AdministrationAction.USER_ROLES_MODIFY.getActionKey());
        if (lIsAdminAccessOnInstance) {
            return true;
        }

        String lCurrentProductName = getProductNameFromSessionToken(pRoleToken);
        boolean lIsAdminAccessOnProduct =
                isAdminAccessDefinedOnProduct(pRoleToken, AdministrationAction.USER_MODIFY.getActionKey(), lCurrentProductName)
               || isAdminAccessDefinedOnProduct(pRoleToken, AdministrationAction.USER_ROLES_MODIFY.getActionKey(), lCurrentProductName);
        if (lIsAdminAccessOnProduct) {
            boolean lRolesOk = true;
            for (RoleData lRoleData : pRoles) {
                // Check if role is defined on current product
                if (!lCurrentProductName.equals(lRoleData.getProductName())) {
                    lRolesOk = false;
                }
                // Check if role is not admin
                if (ADMIN_ROLE_NAME.equals(lRoleData.getRoleName())) {
                    lRolesOk = false;
                }
            }
            return lRolesOk;
        }

        return false;

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#setRoles(java.lang.String,
     *      java.lang.String, java.lang.String, java.util.Collection)
     */
    @Override
    public void setRoles(String pRoleToken, String pLoginName,
            String pBusinessProcessName, Collection<RoleData> pOldRoles,
            Collection<RoleData> pNewRoles, Context pContext) {
    	
    	BusinessProcess lBusinessProcess = getBusinessProcess(pBusinessProcessName);
		Map<String, List<String>> lOldRoleMap = new HashMap<String, List<String>>();
		Map<String, List<String>> lNewRoleMap = new HashMap<String, List<String>>();
    	
		// Initialize map of *Product specific only* previous roles
		if (pOldRoles != null) {
    		for (RoleData lOldData : pOldRoles) {
    			Role lRole = getRoleDao().getRole(lOldData.getRoleName(), lBusinessProcess);
                if (lRole == null) {
                    throw new InvalidNameException(lOldData.getRoleName());
                }

                String lProductName = lOldData.getProductName();
                if (!StringUtils.isBlank(lProductName)) {
                	List<String> lProductRoles = lOldRoleMap.get(lProductName);
                	if (lProductRoles == null) {
                		lProductRoles = new ArrayList<String>();
                		lOldRoleMap.put(lProductName, lProductRoles);
                	}
                	lProductRoles.add(lOldData.getRoleName());
                }
    		}
    	}
    	
        removeRoles(pLoginName, pBusinessProcessName);
        EndUser lUser = getUserFromLogin(pLoginName);

        // Create all roles
        for (RoleData lRoleData : pNewRoles) {
        	String lRoleName = lRoleData.getRoleName();
        	String lProductName = lRoleData.getProductName();
            Role lRole = getRoleDao().getRole(lRoleName, lBusinessProcess);
            if (lRole == null) {
                throw new InvalidNameException(lRoleName);
            }

            if (StringUtils.isBlank(lProductName)) {
                // No product name specified, this is a process role.
                lUser.addToRoleList(lRole);
            }
            else {
            	// Product role
            	// If a product role has the USER_ROLES_MODIFY action,
            	// then nobody else shall have this role for the product
                if (!getAdminAccessControlDao().getApplicables(null, lRoleName,
                		AdministrationAction.USER_ROLES_MODIFY.getActionKey()).isEmpty()) {
                	// Remove all <current role> for current product for all users
                	Product lProduct = getProductDao().getProduct(pBusinessProcessName, lProductName);
                	for (RolesForProduct lRfp : getRolesForProductDao().getByProduct(lProduct)) {
                		lRfp.getRoles().remove(lRole); // Remove role if present
                	}
                }
                addRole(lUser, lBusinessProcess, lRole, lProductName);

                if (pOldRoles != null) {
                	List<String> lNewProductRoles = lNewRoleMap.get(lProductName);
                	if (lNewProductRoles == null) {
                		lNewProductRoles = new ArrayList<String>();
                		lNewRoleMap.put(lProductName, lNewProductRoles);
                	}
                	lNewProductRoles.add(lRoleName);
                }
            }
        }

        // Clear the cache
        availableProductsManager.depreciateElement(new AvailableProductsKey(
                lBusinessProcess.getName(), pLoginName));
        
        // Trigger extension points if needed
        if (pOldRoles != null) {
        	for (Map.Entry<String, List<String>> lNewEntry : lNewRoleMap.entrySet()) {
        		String lProductName = lNewEntry.getKey();
        		List<String> lNewRoles = lNewEntry.getValue();
        		List<String> lOldRoles = lOldRoleMap.get(lProductName);
        		if (lOldRoles != null) {
        			lOldRoleMap.remove(lProductName);
        		} else {
        			lOldRoles = new ArrayList<String>();
        		}
        		getProductService().triggerRolesChanged(
        				pRoleToken, lProductName, pLoginName, lOldRoles, lNewRoles, pContext);
        	}
        	
        	// This second loop scans products where there were deleted roles
        	for (Map.Entry<String, List<String>> lOldEntry : lOldRoleMap.entrySet()) {
        		String lProductName = lOldEntry.getKey();
        		List<String> lOldRoles = lOldEntry.getValue();
        		getProductService().triggerRolesChanged(
        				pRoleToken, lProductName, pLoginName, lOldRoles, new ArrayList<String>(), pContext);
        	}
        }
    }

    /**
     * Remove all user's roles.
     * <p>
     * Remove overriden, admin and product roles.
     * 
     * @param pLoginName
     *            User's login
     * @param pProcessName
     *            Process name
     */
    // TODO declare in interface
    public void removeRoles(String pLoginName, String pProcessName) {
        EndUser lUser = getUserFromLogin(pLoginName);
        // Remove all 'overridden' roles.
        List<OverriddenRole> lOverriddenRoles =
                getOverriddenRoleDao().getOverriddenRolesFromUserLogin(
                        pLoginName);
        List<String> lContainerIds = new ArrayList<String>();
        for (OverriddenRole lOverriddenRole : lOverriddenRoles) {
            String lContainerId = lOverriddenRole.getValuesContainer().getId();
            if (!lContainerIds.contains(lContainerId)) {
                lContainerIds.add(lContainerId);
                removeElementFromCache("OverriddenRole" + lContainerId
                        + pLoginName);
            }
        }

        List<RolesForProduct> lRolesForProductToDelete =
                new ArrayList<RolesForProduct>();

        // Remove all 'user' roles.
        for (RolesForProduct lRfp : lUser.getRolesForProducts()) {
            if (lRfp.getProduct().getBusinessProcess().getName().equals(
                    pProcessName)) {
                lRfp.getRoles().clear();
                lRolesForProductToDelete.add(lRfp);
            }
        }
        // delete the RolesForProducts which references no role
        for (RolesForProduct lRfp : lRolesForProductToDelete) {
            lUser.getRolesForProducts().remove(lRfp);
            getRolesForProductDao().remove(lRfp);
        }

        // Remove all 'product-less' roles
        Collection<Role> lAdminRolesToRemove = new ArrayList<Role>();

        for (Role lAdminRole : lUser.getAdminRoles()) {
            if (lAdminRole.getBusinessProcess().getName().equals(pProcessName)) {
                lAdminRolesToRemove.add(lAdminRole);
            }
        }
        if (lAdminRolesToRemove.size() != 0) {
            lUser.getAdminRoles().removeAll(lAdminRolesToRemove);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#addRoleForUsers(java.lang.String,
     *      java.lang.String[], java.lang.String, java.lang.String,
     *      java.lang.String)
     */
    @Override
    public void addRoleForUsers(String pRoleToken, String[] pLoginNames,
            String pRoleName, String pProductName, String pBusinessProcessName) {

        // Check that current user is admin (at least on specified product)
        assertHasAdminRoleOnProduct(pRoleToken, pProductName);

        List<EndUser> lUsers =
                getEndUserDao().getUsersFromLogins(pLoginNames,
                        isLoginCaseSensitive());
        //Get role to set from its name
        Role lRole =
                getRoleDao().getRole(pRoleName,
                        getBusinessProcess(pBusinessProcessName));

        /*
         * Two possibilities :
         * either productName is null, and roles to set are instance roles
         * or roles to set are roles on a product
         */
        if (pProductName == null) { // set instance roles

            // Affect this role to all users
            for (EndUser lCurrentUser : lUsers) {
                lCurrentUser.addToRoleList(lRole);
            }
        }
        else { //set roles for a product
            // Affect this roleForProduct to all users
            // Affect this role to all users
            for (EndUser lCurrentUser : lUsers) {
                // Create the roleForProduct, that is to set to the list of users
                RolesForProduct lRoleForProduct = RolesForProduct.newInstance();
                lRoleForProduct.setProduct(getProduct(pBusinessProcessName,
                        pProductName));
                lRoleForProduct.addToRoleList(lRole);
                lCurrentUser.addToRolesForProductList(lRoleForProduct);
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#removeRoleForUsers(java.lang.String,
     *      java.lang.String[], java.lang.String, java.lang.String,
     *      java.lang.String)
     */
    @Override
    public void removeRoleForUsers(String pRoleToken, String[] pLoginNames,
            String pRoleName, String pProductName, String pBusinessProcessName) {

        // Check that current user is admin (at least on specified product)
        assertHasAdminRoleOnProduct(pRoleToken, pProductName);

        List<EndUser> lUsers =
                getEndUserDao().getUsersFromLogins(pLoginNames,
                        isLoginCaseSensitive());
        //Get role to set from its name
        Role lRole =
                getRoleDao().getRole(pRoleName,
                        getBusinessProcess(pBusinessProcessName));

        /*
         * Two possibilities :
         * either productName is null, and roles to set are instance roles
         * or roles to set are roles on a product
         */
        if (pProductName == null) { // set instance roles

            // Remove this role for all users
            for (EndUser lCurrentUser : lUsers) {
                if (lCurrentUser.getAdminRoles() != null
                        && lCurrentUser.getAdminRoles().contains(lRole)) {
                    lCurrentUser.removeFromRoleList(lRole);
                }
            }
        }
        else { //remove roles for a product
            // Remove this roleForProduct to all users
            // Remove this role to all users
            for (EndUser lCurrentUser : lUsers) {
                // Create the roleForProduct, that is to set to the list of users
                RolesForProduct lRolesForProductToDelete = null;
                for (RolesForProduct lRolesForProduct : lCurrentUser.getRolesForProducts()) {
                    if (pProductName.equals(lRolesForProduct.getProduct().getName())) {
                        if (lRolesForProduct.getRoles() != null
                                && lRolesForProduct.getRoles().contains(lRole)) {
                            lRolesForProduct.removeFromRoleList(lRole);
                        }
                        // If role remove is last role : prepare to remove the rolesForProduct
                        if (lRolesForProduct.getRoles() == null
                                || lRolesForProduct.getRoles().isEmpty()) {
                            lRolesForProductToDelete = lRolesForProduct;
                        }
                    }
                }
                if (lRolesForProductToDelete != null) {
                    lCurrentUser.removeFromRolesForProductList(lRolesForProductToDelete);
                }
            }
        }
    }

    /**
     * Remove the assignement of a user to a role.
     * 
     * @param pRoleToken
     *            Role token
     * @param pLogin
     *            User's login.
     * @param pRoleName
     *            Role's name
     * @param pProductName
     *            Product's name
     * @param pBusinessProcessName
     *            Process name.
     */
    // TODO declare in interface
    public void removeRoleAssignement(String pRoleToken, String pLogin,
            String pRoleName, String pProductName, String pBusinessProcessName) {

        // Check that current user is admin (at least on specified product)
        assertHasAdminRoleOnProduct(pRoleToken, pProductName);

        EndUser lUser = getUserFromLogin(pLogin);

        //Get role to set from its name
        Role lRole =
                getRoleDao().getRole(pRoleName,
                        getBusinessProcess(pBusinessProcessName));

        /*
         * Two possibilities :
         * either productName is null, and roles to set are instance roles
         * or roles to set are roles on a product
         */
        if (StringUtils.isBlank(pProductName)) { // set instance roles
            if (lUser.getAdminRoles() != null
                    && lUser.getAdminRoles().contains(lRole)) {
                lUser.removeFromRoleList(lRole);
            }
        }
        else { //remove roles for a product
            // Remove this roleForProduct to all users
            // Remove this role to all users
            // Create the roleForProduct, that is to set to the list of users
            RolesForProduct lRolesForProductToDelete = null;
            for (RolesForProduct lRolesForProduct : lUser.getRolesForProducts()) {
                if (pProductName.equals(lRolesForProduct.getProduct().getName())) {
                    if (lRolesForProduct.getRoles() != null
                            && lRolesForProduct.getRoles().contains(lRole)) {
                        lRolesForProduct.removeFromRoleList(lRole);
                    }
                    // If role remove is last role : prepare to remove the rolesForProduct
                    if (lRolesForProduct.getRoles() == null
                            || lRolesForProduct.getRoles().isEmpty()) {
                        lRolesForProductToDelete = lRolesForProduct;
                    }
                }
            }
            if (lRolesForProductToDelete != null) {
                lUser.removeFromRolesForProductList(lRolesForProductToDelete);
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService
     *      #hasAdminAccess(java.lang.String)
     */
    @Override
    public boolean hasAdminAccess(String pRoleToken) {
        String lRoleName = getRoleNameFromToken(pRoleToken);
        return lRoleName.equals(ADMIN_ROLE_NAME);
    }

    /**
     * Assert that the role session token corresponds to a user that has an
     * Administrator role either on the specified product or on instance.
     * 
     * @param pRoleToken
     *            the role session token.
     * @param pProductName
     *            the product name (or null if instance)
     */
    // TODO declare in interface
    public void assertHasAdminRoleOnProduct(String pRoleToken,
            String pProductName) {
        String lUserLogin = getUserFromToken(pRoleToken).getLogin();
        if (!getRoleDao().hasAdminRole(lUserLogin, pProductName,
                getBusinessProcessName(pRoleToken))) {
            throw new AuthorizationException(
                    "Administrator role required on product '" + pProductName
                            + "' or instance.");
        }
    }

    /**
     * Assert the role session is an admin role.
     * 
     * @param pRoleToken
     *            Role session token
     */
    // TODO declare in interface
    public void assertAdminRole(String pRoleToken) {
        if (!hasAdminAccess(pRoleToken)) {
            throw new AuthorizationException("Admininistrator role required");
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#hasGlobalAdminRole(java.lang.String)
     */
    @Override
    public final boolean hasGlobalAdminRole(String pToken) {

        Boolean lHasGlobalAdminRole =
                (Boolean) getCached(pToken + "hasGlobalAdminRole");

        if (lHasGlobalAdminRole == null) {
            String lLogin = getLoginFromToken(pToken);
            if (lLogin.equals(ADMIN_LOGIN)) {
                lHasGlobalAdminRole = true;
            }
            else {
                EndUser lUser = getUserFromToken(pToken);
                lHasGlobalAdminRole = getEndUserDao().hasGlobalAdminRole(lUser);
            }
            addCached(pToken + "hasGlobalAdminRole", lHasGlobalAdminRole);
        }

        return lHasGlobalAdminRole.booleanValue();
    }

    /**
     * Tests if a user is connected with the global admin role.
     * 
     * @param pRoleToken
     *            The role token.
     * @return If a user is connected with the global admin role.
     */
    // TODO declare in interface
    public boolean isGlobalAdminRole(final String pRoleToken) {
        return ADMIN_ROLE_NAME.equals(getRoleNameFromToken(pRoleToken))
                && isProcessAccess(pRoleToken);
    }

    /**
     * Assert the user/role session is a global admin role.
     * 
     * @param pToken
     *            User or role session token
     * @throws AuthorizationException
     *             When the given token is not a global admin role.
     */
    // TODO declare in interface
    public final void assertGlobalAdminRole(String pToken) {
        if (!hasGlobalAdminRole(pToken)) {
            throw new AuthorizationException("Admininistrator role required");
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#getAdminUserToken()
     */
    @Override
    public String getAdminUserToken() {
        if (globalAdminUserToken == null) {
            EndUser lAdminUser =
                    getEndUserDao().getUser(ADMIN_LOGIN, isLoginCaseSensitive());

            if (lAdminUser == null) {
                throw new GDMException("Administrator user does not exist");
            }
            globalAdminUserToken = userSessions.createPermanentUser(lAdminUser);
        }
        return globalAdminUserToken;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#getAdminRoleToken(java.lang.String)
     */
    @Override
    public String getAdminRoleToken(String pBusinessProcessName) {
        if (globalAdminRoleTokens == null
                || !globalAdminRoleTokens.containsKey(pBusinessProcessName)) {
            String lAdminRoleToken;
            lAdminRoleToken =
                    selectRole(getAdminUserToken(), ADMIN_ROLE_NAME, null,
                            pBusinessProcessName);
            if (lAdminRoleToken == null) {
                throw new GDMException(
                        "No administrator role available for instance '"
                                + pBusinessProcessName + "'");
            }
            if (globalAdminRoleTokens == null) {
                globalAdminRoleTokens = new HashMap<String, String>();
            }
            globalAdminRoleTokens.put(pBusinessProcessName, lAdminRoleToken);
            return lAdminRoleToken;
        }
        return globalAdminRoleTokens.get(pBusinessProcessName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#setFieldAccessControl(org.topcased.gpm.business.authorization.service.FieldAccessControlData)
     */
    @Override
    public void setFieldAccessControl(FieldAccessControlData pAccessControlData) {
        final Field lField =
                fieldsManager.getField(
                        getFieldsContainer(pAccessControlData.getContext().getContainerTypeId()),
                        pAccessControlData.getFieldName());
        FieldAccessControl lFac =
                getAccessControl(
                        FieldAccessControl.class,
                        pAccessControlData,
                        Restrictions.eq(
                                AccessControlConstant.DB_ATTRIBUTE_FIELD_FIELDCONTROL.getAsString(),
                                lField), false);

        if (null == lFac) {
            lFac = FieldAccessControl.newInstance();

            fillAccessControl(lFac, pAccessControlData);

            lFac.setFieldControl(lField);
            lFac.setConfidential(pAccessControlData.getAccess().getConfidential());
            lFac.setUpdatable(pAccessControlData.getAccess().getUpdatable());
            lFac.setMandatory(pAccessControlData.getAccess().getMandatory());
            lFac.setExportable(pAccessControlData.getAccess().getExportable());
            lFac.setVisibleTypeId(pAccessControlData.getContext().getVisibleTypeId());

            getFieldAccessControlDao().create(lFac);
        }
        else {
            lFac.setConfidential(pAccessControlData.getAccess().getConfidential());
            lFac.setUpdatable(pAccessControlData.getAccess().getUpdatable());
            lFac.setMandatory(pAccessControlData.getAccess().getMandatory());
            lFac.setExportable(pAccessControlData.getAccess().getExportable());
            lFac.setVisibleTypeId(pAccessControlData.getContext().getVisibleTypeId());
            updateAttributesContainer(pAccessControlData, lFac);
        }

        // We must clear the access controls cache
        accessControlCache.removeAll();
        clearFilterCaches();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#setSheetTypeAccessControl(org.topcased.gpm.business.authorization.service.TypeAccessControlData)
     */
    @Override
    public void setSheetTypeAccessControl(
            TypeAccessControlData pAccessControlData) {
        setTypeAccessControl(pAccessControlData);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#setTypeAccessControl(org.topcased.gpm.business.authorization.service.TypeAccessControlData)
     */
    @Override
    public void setTypeAccessControl(TypeAccessControlData pAccessControlData) {

        FieldsContainer lContainer =
                getFieldsContainer(pAccessControlData.getContext().getContainerTypeId());

        TypeAccessControl lTac =
                getAccessControl(
                        TypeAccessControl.class,
                        pAccessControlData,
                        Restrictions.eq(
                                AccessControlConstant.DB_ATTRIBUTE_TYPECONTROL.getAsString(),
                                lContainer), false);

        if (null == lTac) {
            lTac = TypeAccessControl.newInstance();

            fillAccessControl(lTac, pAccessControlData);

            lTac.setConfidential(pAccessControlData.getConfidential());
            lTac.setCreatable(pAccessControlData.getCreatable());
            lTac.setDeletable(pAccessControlData.getDeletable());
            lTac.setUpdatable(pAccessControlData.getUpdatable());

            getTypeAccessControlDao().create(lTac);
        }
        else {
            lTac.setUpdatable(pAccessControlData.getUpdatable());
            updateAttributesContainer(pAccessControlData, lTac);
        }

        // We must clear the access controls cache
        accessControlCache.removeAll();
        clearFilterCaches();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#setApplicationActionAccessControl(org.topcased.gpm.business.authorization.service.ActionAccessControlData)
     */
    @Override
    public void setApplicationActionAccessControl(
            ActionAccessControlData pAccessControlData) {
        AppliActionAccessControl lAaac = null;

        BusinessProcess lProcess =
                getBusinessProcess(pAccessControlData.getBusinessProcessName());

        lAaac =
                getAccessControl(
                        AppliActionAccessControl.class,
                        pAccessControlData,
                        Restrictions.and(
                                Restrictions.eq(
                                        AccessControlConstant.DB_ATTRIBUTE_APPLIACTION_BUSINESSPROCESS.getAsString(),
                                        lProcess),
                                Restrictions.eq(
                                        AccessControlConstant.DB_ATTRIBUTE_APPLIACTION_ACTIONKEY.getAsString(),
                                        pAccessControlData.getLabelKey())),
                        false);

        if (null == lAaac) {
            lAaac = AppliActionAccessControl.newInstance();

            fillAccessControl(lAaac, pAccessControlData);

            lAaac.setBusinessProcess(lProcess);
            lAaac.setActionKey(pAccessControlData.getLabelKey());
            lAaac.setConfidential(pAccessControlData.getConfidential());
            lAaac.setEnabled(pAccessControlData.getEnabled());

            getAppliActionAccessControlDao().create(lAaac);
        }
        else {
            lAaac.setConfidential(pAccessControlData.getConfidential());
            lAaac.setEnabled(pAccessControlData.getEnabled());
            updateAttributesContainer(pAccessControlData, lAaac);
        }

        // We must clear the access controls cache
        accessControlCache.removeAll();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#setAdminAccessControl(org.topcased.gpm.business.authorization.service.AdminAccessControlData)
     */
    @Override
    public void setAdminAccessControl(AdminAccessControlData pAccessControl) {
        AdminAccessControl lAac =
                getAccessControl(
                        AdminAccessControl.class,
                        pAccessControl,
                        Restrictions.eq(
                                AccessControlConstant.DB_ATTRIBUTE_APPLIACTION_ACTIONKEY.getAsString(),
                                pAccessControl.getLabelKey()), false);

        if (null == lAac) {
            lAac = AdminAccessControl.newInstance();

            fillAccessControl(lAac, pAccessControl);

            lAac.setActionKey(pAccessControl.getLabelKey());

            getAdminAccessControlDao().create(lAac);
        }
        else {
            updateAttributesContainer(pAccessControl, lAac);
        }

        // We must clear the access controls cache
        accessControlCache.removeAll();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#setTransitionAccessControl(org.topcased.gpm.business.authorization.service.TransitionAccessControlData)
     */
    @Override
    public void setTransitionAccessControl(
            TransitionAccessControlData pAccessControlData) {
        TransitionAccessControl lTac = null;

        lTac =
                getAccessControl(
                        TransitionAccessControl.class,
                        pAccessControlData,
                        Restrictions.eq(
                                AccessControlConstant.DB_ATTRIBUTE_TRANSITION_TRANSITIONNAME.getAsString(),
                                pAccessControlData.getTransitionName()), false);

        if (null == lTac) {
            lTac = TransitionAccessControl.newInstance();

            fillAccessControl(lTac, pAccessControlData);

            lTac.setTransitionName(pAccessControlData.getTransitionName());
            lTac.setAllowed(pAccessControlData.getAllowed());

            getTransitionAccessControlDao().create(lTac);
        }
        else {
            lTac.setAllowed(pAccessControlData.getAllowed());
            updateAttributesContainer(pAccessControlData, lTac);
        }

        // We must clear the access controls cache
        accessControlCache.removeAll();
    }

    /**
     * Retrieve the user token from a session token.
     * 
     * @param pToken
     *            Session token. This token can be either an user or role
     *            session token
     * @return The user token corresponding to the role session token. If the
     *         given token is already a user token, the same token is simply
     *         returned by this method.
     * @throws InvalidTokenException
     *             When the given token does not exists or is blank / null.
     */
    // TODO declare in interface
    public String getUserToken(String pToken) {
        if (null == pToken) {
            throw new InvalidTokenException("Token is null");
        }
        if (roleSessions.isTokenValid(pToken)) {
            return roleSessions.getContext(pToken).getUserToken();
        }
        else if (userSessions.isTokenValid(pToken)) {
            return pToken;
        }
        else {
            throw new InvalidTokenException(pToken, "Invalid token ''{0}''");
        }
    }

    /**
     * Fill an access control data object.
     * 
     * @param pAccessCtlData
     *            Access control data object to fill. Updated by this method
     * @param pAccessControlContextData
     *            the access control context data
     * @param pProcessName
     *            The name of the business process
     */
    private void fillAccessControlData(AccessControlData pAccessCtlData,
            AccessControlContextData pAccessControlContextData,
            String pProcessName) {
        // Set the business pro. name
        pAccessCtlData.setBusinessProcessName(pProcessName);
        AccessControlContextData lAccessControlContextData =
                new AccessControlContextData();
        if (null != pAccessControlContextData.getRoleName()) {
            lAccessControlContextData.setRoleName(pAccessControlContextData.getRoleName());
        }
        if (null != pAccessControlContextData.getProductName()) {
            lAccessControlContextData.setProductName(pAccessControlContextData.getProductName());
        }
        if (null != pAccessControlContextData.getStateName()) {
            lAccessControlContextData.setStateName(pAccessControlContextData.getStateName());
        }
        if (null != pAccessControlContextData.getContainerTypeId()) {
            lAccessControlContextData.setContainerTypeId(pAccessControlContextData.getContainerTypeId());
        }
        pAccessCtlData.setContext(lAccessControlContextData);
    }

    /**
     * Fill an AccessControl from the values of an accessControlData.
     * 
     * @param pAccessControl
     *            AccessControl to fill
     * @param pAccessControlData
     *            AccessControlData containing actual values
     */
    private void fillAccessControl(AccessControl pAccessControl,
            final AccessControlData pAccessControlData) {
        BusinessProcess lProcess =
                getBusinessProcess(pAccessControlData.getBusinessProcessName());
        Product lProduct = null;
        Role lRole = null;
        Node lState = null;
        FieldsContainer lFieldsContainer = null;
        AttributesContainer lAttributesContainer = null;

        if (!StringUtils.isBlank(pAccessControlData.getContext().getProductName())) {
            lProduct =
                    getProductDao().getProduct(
                            pAccessControlData.getBusinessProcessName(),
                            pAccessControlData.getContext().getProductName());
        }
        pAccessControl.setProductControl(lProduct);

        if (!StringUtils.isBlank(pAccessControlData.getContext().getRoleName())) {
            lRole =
                    getRoleDao().getRole(
                            pAccessControlData.getContext().getRoleName(),
                            lProcess);
        }
        pAccessControl.setRoleControl(lRole);

        //set the state only a container is available.
        if (!StringUtils.isBlank(pAccessControlData.getContext().getContainerTypeId())) {
            if (!StringUtils.isBlank(pAccessControlData.getContext().getStateName())) {
                lState =
                        getStateNode(
                                pAccessControlData.getContext().getContainerTypeId(),
                                pAccessControlData.getContext().getStateName());
            }
            pAccessControl.setStateControl(lState);
        }

        //Get container control
        if (!StringUtils.isBlank(pAccessControlData.getContext().getContainerTypeId())) {
            lFieldsContainer =
                    getFieldsContainer(pAccessControlData.getContext().getContainerTypeId());
        }
        pAccessControl.setTypeControl(lFieldsContainer);

        if (pAccessControlData.getExtendedAttributes() != null) {
            lAttributesContainer =
                    createAccessControlAttributeContainer(pAccessControlData.getExtendedAttributes());
        }
        pAccessControl.setExtendedAttributes(lAttributesContainer);

    }

    /**
     * Get a state node from its name.
     * 
     * @param pTypeId
     *            Sheet type identifier
     * @param pStateName
     *            State name (can be null)
     * @return The state node (or null if pStateName is blank)
     * @throws InvalidNameException
     *             if the state name does not exist
     */
    private Node getStateNode(String pTypeId, String pStateName) {
        if (StringUtils.isBlank(pStateName)) {
            return null;
        }

        Node lState = getSheetTypeDao().getNode(pTypeId, pStateName);
        if (null == lState) {
            throw new InvalidNameException(pStateName,
                    "State name ''{0}'' invalid");
        }
        return lState;
    }

    /**
     * Get an EndUser object from a user or role session.
     * 
     * @param pToken
     *            Session token
     * @return EndUser object from the database
     * @throws InvalidTokenException
     *             The user token is blank.
     * @throws AuthorizationException
     *             The user token is invalid.
     */
    private EndUser getUserFromToken(String pToken) {
        String lUserToken = getUserToken(pToken);
        String lUserId = userSessions.getContext(lUserToken).getEndUserId();
        EndUser lEndUser = (EndUser) getEndUserDao().load(lUserId);

        if (null == lEndUser) {
            throw new AuthorizationException("Invalid user token");
        }

        return lEndUser;
    }

    /**
     * Get an EndUser object from a user login.
     * 
     * @param pLogin
     *            User session token
     * @return EndUser object from the database
     * @throws InvalidNameException
     *             The login is incorrect.
     */
    private EndUser getUserFromLogin(String pLogin) throws InvalidNameException {
        EndUser lUser = getEndUserDao().getUser(pLogin, isLoginCaseSensitive());
        if (null == lUser) {
            throw new InvalidValueException(pLogin, "Invalid login name {0}");
        }
        return lUser;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#getRole(java.lang.String)
     */
    @Override
    public Role getRole(String pRoleToken) {
        return getRoleDao().load(
                roleSessions.getContext(pRoleToken).getRoleId());
    }

    /**
     * Get the role from role session token.
     * 
     * @param pRoleToken
     *            Role session token
     * @return Role or null if not found.
     * @throws InvalidTokenException
     *             The role token is invalid.
     */
    private org.topcased.gpm.business.serialization.data.Role getSerializableRole(
            String pRoleToken) {
        Role lRole = getRole(pRoleToken);
        org.topcased.gpm.business.serialization.data.Role lRes =
                new org.topcased.gpm.business.serialization.data.Role();
        lRes.setName(lRole.getName());
        return lRes;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#getRoleNameFromToken(java.lang.String)
     */
    @Override
    public final String getRoleNameFromToken(String pRoleToken) {
        return roleSessions.getContext(pRoleToken).getRoleName();
    }

    /**
     * Get the process name from role session token.
     * 
     * @param pRoleToken
     *            Role session token
     * @return Role UUID.
     * @throws InvalidTokenException
     *             The role token is invalid.
     */
    // TODO declare in interface
    public final String getProcessNameFromToken(String pRoleToken) {
        return roleSessions.getContext(pRoleToken).getProcessName();
    }

    /**
     * Get the user's login from token.
     * 
     * @param pToken
     *            User session or role session token
     * @return Login of the user
     */
    // TODO declare in interface
    public final String getLoginFromToken(String pToken) {
        String lUserToken = getUserToken(pToken);
        return userSessions.getContext(lUserToken).getLogin();
    }

    /**
     * Get the product from session token.
     * 
     * @param pRoleToken
     *            Role session token
     * @return Product or null if not found.
     * @throws InvalidTokenException
     *             The role token is blank.
     */
    // TODO declare in interface
    public Product getProductFromSessionToken(String pRoleToken) {
        // Note: Role token is checked in getContext() method.
        String lProductId = roleSessions.getContext(pRoleToken).getProductId();
        if (null == lProductId) {
            return null;
        }
        // else
        return (Product) getProductDao().load(lProductId);
    }

    /**
     * Get the product name from a role session token.
     * 
     * @param pRoleToken
     *            Role session token
     * @return Product name or null if not found.
     * @throws InvalidTokenException
     *             The role token is blank.
     */
    // TODO declare in interface
    public final String getProductNameFromSessionToken(String pRoleToken) {
        // Note: Role token is checked in getContext() method.
        return roleSessions.getContext(pRoleToken).getProductName();
    }

    /**
     * Check that the type access control is not confidential and creatable.
     * 
     * @param pRoleToken
     *            the current role token
     * @param pFieldsContainerId
     *            the fields container id
     * @param pFieldsContainerName
     *            the fields container name
     * @param pProcessName
     *            the business process name
     * @param pProductName
     *            the product name
     * @param pStateName
     *            the current sheet state name
     * @throws AuthorizationException
     *             if the fieldsContainer is confidential or not creatable.
     */
    // TODO declare in interface
    public void checkCreatableAccessControl(String pRoleToken,
            String pFieldsContainerId, String pFieldsContainerName,
            String pProcessName, String pProductName, String pStateName)
        throws AuthorizationException {
        //Don't apply these access controls on admin roles
        if (hasAdminAccess(pRoleToken)) {
            return;
        }
        TypeAccessControlData lTypeAccessControl =
                getTypeAccessControl(pRoleToken, pProcessName, pProductName,
                        pStateName, pFieldsContainerId);

        if (lTypeAccessControl == null) {
            return;
        }
        if (lTypeAccessControl.getConfidential()) {
            throw new AuthorizationException("Illegal access. Type '"
                    + pFieldsContainerName + "' is confidential.");
        }
        else if (!lTypeAccessControl.getCreatable()) {
            throw new AuthorizationException("Illegal access. Type '"
                    + pFieldsContainerName + "' is not creatable.");
        }
    }

    /**
     * Assert that the type access control is not confidential and deletable.
     * 
     * @param pRoleToken
     *            the current role token
     * @param pFieldsContainer
     *            the fields container
     * @param pProductName
     *            the product name
     * @param pStateName
     *            the current sheet state name
     * @throws AuthorizationException
     *             if the fieldsContainer is confidential or not deletable.
     */
    // TODO declare in interface
    public void assertDeletable(String pRoleToken,
            CacheableFieldsContainer pFieldsContainer, String pProductName,
            String pStateName) throws AuthorizationException {
        //Don't apply these access controls on admin roles
        if (hasAdminAccess(pRoleToken)) {
            return;
        }
        TypeAccessControlData lTypeAccessControl =
                getTypeAccessControl(pRoleToken,
                        pFieldsContainer.getBusinessProcessName(),
                        pProductName, pStateName, pFieldsContainer.getId());
        if (lTypeAccessControl == null) {
            return;
        }
        if (lTypeAccessControl.getConfidential()) {
            throw new AuthorizationException("Illegal access. Type '"
                    + pFieldsContainer.getName() + "' is confidential.");
        }
        else if (!lTypeAccessControl.getDeletable()) {
            throw new AuthorizationException("Illegal access. Type '"
                    + pFieldsContainer.getName() + "' is not deletable.");
        }
    }

    /**
     * Check that the type access control is not confidential.
     * 
     * @param pRoleToken
     *            the current role token
     * @param pFieldsContainerId
     *            the fields container id
     * @param pFieldsContainerName
     *            the fields container name
     * @param pProcessName
     *            the business process name
     * @param pProductName
     *            the product name
     * @param pStateName
     *            the current sheet state name
     * @throws AuthorizationException
     *             if the fieldsContainer is confidential
     */
    // TODO declare in interface
    public void checkNotConfidentialAccessControl(String pRoleToken,
            String pFieldsContainerId, String pFieldsContainerName,
            String pProcessName, String pProductName, String pStateName)
        throws AuthorizationException {
        //Don't apply these access controls on admin roles
        if (hasAdminAccess(pRoleToken)) {
            return;
        }
        TypeAccessControlData lTypeAccessControl =
                getTypeAccessControl(pRoleToken, pProcessName, pProductName,
                        pStateName, pFieldsContainerId);
        if (lTypeAccessControl != null && lTypeAccessControl.getConfidential()) {
            throw new AuthorizationException("Illegal access. Type '"
                    + pFieldsContainerName + "' is confidential.");
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#getValueOfUserAttribute(org.topcased.gpm.business.authorization.service.EndUserData,
     *      java.lang.String)
     */
    @Override
    public String getValueOfUserAttribute(EndUserData pEndUserData,
            String pAttributeName) {
        if (pEndUserData != null && pAttributeName != null) {
            if (pEndUserData.getUserAttributes() == null) {
                return null;
            }
            for (UserAttributesData lAttribute : pEndUserData.getUserAttributes()) {
                if (lAttribute.getName() != null
                        && lAttribute.getName().equals(pAttributeName)) {
                    return lAttribute.getValue();
                }
            }
        }
        return null;
    }

    /**
     * Add a new user attribute to a user.
     * 
     * @param pEndUserData
     *            the user data
     * @param pAttributeName
     *            the attribute name
     * @param pValue
     *            the attribute value
     */
    private void addUserAttribute(EndUserData pEndUserData,
            String pAttributeName, String pValue) {
        UserAttributesData[] lUserAttributes = pEndUserData.getUserAttributes();
        UserAttributesData lNewUserAttribute =
                new UserAttributesData(pAttributeName, pValue);
        pEndUserData.setUserAttributes((UserAttributesData[]) ArrayUtils.add(
                lUserAttributes, lNewUserAttribute));
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#setValueToUserAttribute(org.topcased.gpm.business.authorization.service.EndUserData,
     *      java.lang.String, java.lang.String)
     */
    @Override
    public void setValueToUserAttribute(EndUserData pEndUserData,
            String pAttributeName, String pValue) {
        if (pEndUserData != null && pAttributeName != null) {
            if (pEndUserData.getUserAttributes() == null) {
                pEndUserData.setUserAttributes(new UserAttributesData[0]);
            }
            else {
                // Check if the attribute already exists
                for (UserAttributesData lAttribute : pEndUserData.getUserAttributes()) {
                    if (lAttribute.getName() != null
                            && lAttribute.getName().equals(pAttributeName)) {
                        // set the new value
                        lAttribute.setValue(pValue);
                        EndUser lEndUser =
                                getEndUserDao().getUser(
                                        pEndUserData.getLogin(),
                                        isLoginCaseSensitive());
                        setUserAttributes(lEndUser, pEndUserData);
                        return;
                    }
                }
            }

            // When the attribute was not found, create it.
            addUserAttribute(pEndUserData, pAttributeName, pValue);

            EndUser lEndUser =
                    getEndUserDao().getUser(pEndUserData.getLogin(),
                            isLoginCaseSensitive());
            setUserAttributes(lEndUser, pEndUserData);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#validateRoleToken(java.lang.String)
     */
    @Override
    public void validateRoleToken(String pToken) {

        if (roleSessions.isTokenValid(pToken)) {
            return;
        }
        else if (userSessions.isTokenValid(pToken)) {
            throw new InvalidTokenException(pToken,
                    "Invalid roleToken : ''{0}'' is a user session token.");
        }
        else {
            throw new InvalidTokenException("Invalid role session token.");
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {

        AuthorizationServiceImpl lAuthorizationServiceImpl =
                (AuthorizationServiceImpl) mainContext.getBean("authorizationServiceImpl");

        // Create a new ArrayList for the UserCredentials.
        List<UserCredentials> lUserCredentialsArrayList =
                new ArrayList<UserCredentials>();

        // Read the userCredentialsMgrDefinition
        String lUserCredentialsDefWithComma =
                lAuthorizationServiceImpl.getUserCredentialsMgrDefinition();
        String[] lUserCredentialsDefList =
                lUserCredentialsDefWithComma.split(",");
        for (String lUserCredentialsDef : lUserCredentialsDefList) {

            UserCredentials lUserCredentials;
            try {
                // Get the bean
                lUserCredentials =
                        (UserCredentials) mainContext.getBean(lUserCredentialsDef.trim());
            }
            catch (NoSuchBeanDefinitionException ex) {
                throw new GDMException("The bean " + lUserCredentialsDef.trim()
                        + " is not defined, consequently it will be ignored.");
            }

            // Add in the list
            lUserCredentialsArrayList.add(lUserCredentials);
        }

        if (lUserCredentialsArrayList.size() == 0) {
            throw new GDMException(
                    "No userCredentialsMgr has been set in properties file, "
                            + "consequently it is impossible to log to the application.");
        }

        // Set the user credentials list
        userCredentialsList = lUserCredentialsArrayList;
    }

    /**
     * get userCredentialsMgrDefinition.
     * 
     * @return the userCredentialsMgrDefinition
     */
    public String getUserCredentialsMgrDefinition() {
        return userCredentialsMgrDefinition;
    }

    /**
     * Check if the token is a valid user or role token.
     * 
     * @param pToken
     *            the token to check
     * @throws InvalidTokenException
     *             If the token is not valid.
     */
    // TODO declare in interface
    public void validateToken(String pToken) throws InvalidTokenException {
        if (roleSessions.isTokenValid(pToken)) {
            return;
        }
        else if (userSessions.isTokenValid(pToken)) {
            return;
        }
        else {
            if (StringUtils.isBlank(pToken)) {
                throw new InvalidTokenException("Authorization token is blank");
            }
            // else
            throw new InvalidTokenException(pToken, "Invalid token ''{0}''");
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#getProductsAsTree(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public List<ProductTreeNode> getProductsAsTree(String pToken,
            String pBusinessProcess) {
        String lUserToken = getAuthService().getUserToken(pToken);

        // First, create the tree node with all the products
        // (if a product is not available for this user,
        // the associated productTreeNode enabled attribute is set to false).
        List<ProductTreeNode> lProductTreeNodeList =
                createCompleteProductTreeNodeList(lUserToken, pBusinessProcess);

        // Then, remove all inaccessible nodes in the product node tree list.
        cleanDisabledProductTreeNodeList(lProductTreeNodeList);

        return lProductTreeNodeList;
    }

    /**
     * Create the complete list of Product root Tree Nodes All ProductTreeNode
     * is filled with all its children. If a ProductTreeNode is not accessible
     * by the current user, the enabled property is set to false
     * 
     * @param pUserToken
     *            Identifying the current user
     * @param pBusinessProcessName
     *            Name of the current process
     * @return A list of all products without parents
     */
    @SuppressWarnings("unchecked")
    protected List<ProductTreeNode> createCompleteProductTreeNodeList(
            String pUserToken, String pBusinessProcessName) {
        List<ProductTreeNode> lProductTreeNodeList =
                new ArrayList<ProductTreeNode>();

        // Get all accessible product names
        List<String> lAvailableProductsName =
                getProductNames(pUserToken, pBusinessProcessName);

        // Get all products
        final BusinessProcess lBusinessProc =
                getBusinessProcess(pBusinessProcessName);
        final Session lSess = GpmSessionFactory.getHibernateSession();
        final List<Product> lAllDomainProducts =
                lSess.createFilter(lBusinessProc.getProducts(),
                        "order by this.name").list();
        List<ProductData> lAllProducts =
                createProductDataList(lAllDomainProducts);

        Map<String, ProductData> lProdMap = new HashMap<String, ProductData>();
        Map<String, List<ProductData>> lChildrenProdMap =
                new HashMap<String, List<ProductData>>();

        for (ProductData lProduct : lAllProducts) {
            lProdMap.put(lProduct.getName(), lProduct);
        }

        for (ProductData lProduct : lAllProducts) {
            // Retrieve the productData for all children of the current product
            List<ProductData> lChildrenList;
            final int lChildrenCount = lProduct.getChildrenNames().length;
            if (lChildrenCount > 0) {
                lChildrenList = new ArrayList<ProductData>(lChildrenCount);
                for (String lChidrenName : lProduct.getChildrenNames()) {
                    lChildrenList.add(lProdMap.get(lChidrenName));
                }
            }
            else {
                lChildrenList = Collections.emptyList();
            }
            lChildrenProdMap.put(lProduct.getName(), lChildrenList);
        }

        // Get products without parents
        List<String> lTopLevelProductNames =
                getProductDao().getProductNames(pBusinessProcessName, true);

        for (String lProductName : lTopLevelProductNames) {
            boolean lNodeEnabled =
                    lAvailableProductsName.contains(lProductName);
            ProductData lProductData = lProdMap.get(lProductName);

            // create the complete node
            ProductTreeNode lProductTreeNode =
                    createProductTreeNode(lProductData, lChildrenProdMap,
                            lNodeEnabled, lAvailableProductsName);

            lProductTreeNodeList.add(lProductTreeNode);
        }

        return lProductTreeNodeList;
    }

    private List<ProductData> createProductDataList(
            Collection<? extends Product> pProducts) {
        final List<ProductData> lProductDataList =
                new ArrayList<ProductData>(pProducts.size());

        for (Product lProduct : pProducts) {
            ProductData lData = new ProductData();

            lData.setName(lProduct.getName());
            lData.setId(lProduct.getId());
            lData.setProcessName(lProduct.getBusinessProcess().getName());
            String[] lEnvNames = new String[lProduct.getEnvironments().size()];
            int i = 0;
            for (Environment lEnv : lProduct.getEnvironments()) {
                lEnvNames[i] = lEnv.getName();
                i++;
            }
            lData.setEnvironmentNames(lEnvNames);
            lData.setProductTypeId(lProduct.getDefinition().getId());
            lData.setProductTypeName(lProduct.getDefinition().getName());

            if (lProduct.getChildren() != null) {
                Collection<String> lChildrenNames =
                        new ArrayList<String>(lProduct.getChildren().size());
                Collection<Product> lProducts =
                        new TreeSet<Product>(new Comparator<Product>() {
                            public int compare(Product pProd1, Product pProd2) {
                                Collator lCollator = Collator.getInstance();
                                return lCollator.compare(pProd1.getName(),
                                        pProd2.getName());
                            };
                        });
                lProducts.addAll(lProduct.getChildren());
                for (Product lChild : lProducts) {
                    lChildrenNames.add(lChild.getName());
                }
                lData.setChildrenNames(lChildrenNames.toArray(new String[0]));
            }

            lProductDataList.add(lData);
        }

        return lProductDataList;
    }

    /**
     * Create a product Tree node from its product data and fill all its
     * children.
     * 
     * @param pProductData
     *            The root product data
     * @param pChildrenMap
     *            Map associating the name of a product with its children
     *            ProductData
     * @param pNodeEnabled
     *            Is this node accessible by the current user
     * @param pAvailableProductsName
     *            The list of accessible products name
     * @return The created product tree node.
     */
    protected ProductTreeNode createProductTreeNode(
            final ProductData pProductData,
            final Map<String, List<ProductData>> pChildrenMap,
            boolean pNodeEnabled, final List<String> pAvailableProductsName) {
        ProductTreeNode lProductTreeNode =
                new ProductTreeNode(pProductData, pNodeEnabled);

        // Get all products data children of pProductData.
        List<ProductData> lChildrenListProductData =
                pChildrenMap.get(pProductData.getName());

        for (ProductData lChildrenProductData : lChildrenListProductData) {
            boolean lChildrenEnabled =
                    pAvailableProductsName.contains(lChildrenProductData.getName());

            ProductTreeNode lChildrenProductTreeNode =
                    createProductTreeNode(lChildrenProductData, pChildrenMap,
                            lChildrenEnabled, pAvailableProductsName);
            lProductTreeNode.add(lChildrenProductTreeNode);
        }
        return lProductTreeNode;
    }

    /**
     * Clean the product tree node list : all inaccessible nodes are removed
     * from the tree.
     * 
     * @param pProductTreeNodeList
     *            The complete product tree node list
     */
    protected void cleanDisabledProductTreeNodeList(
            List<ProductTreeNode> pProductTreeNodeList) {
        ArrayList<ProductTreeNode> lRemoveListNode =
                new ArrayList<ProductTreeNode>();

        for (ProductTreeNode lProductTreeNode : pProductTreeNodeList) {
            boolean lRemoveThisNode =
                    cleanDisabledProductTreeNode(lProductTreeNode);
            if (lRemoveThisNode) {
                lRemoveListNode.add(lProductTreeNode);
            }
        }

        for (ProductTreeNode lRemoveNode : lRemoveListNode) {
            pProductTreeNodeList.remove(lRemoveNode);
        }
    }

    /**
     * Clean a product tree node : all inaccessible nodes leaf are removed from
     * the node.
     * 
     * @param pProductTreeNode
     *            The product tree node to be cleared
     * @return boolean indicating if this node has to be removed from the
     *         product node list
     */
    @SuppressWarnings("unchecked")
    protected boolean cleanDisabledProductTreeNode(
            ProductTreeNode pProductTreeNode) {
        boolean lRemoveThisNode = false;

        // List containing node that will be removed
        List<ProductTreeNode> lProductTreeNodeListToRemove =
                new ArrayList<ProductTreeNode>();

        // Through the children node
        Enumeration<ProductTreeNode> lEnumProductTreeNode =
                pProductTreeNode.depthFirstEnumeration();

        while (lEnumProductTreeNode.hasMoreElements()) {
            ProductTreeNode lProductTreeNode =
                    lEnumProductTreeNode.nextElement();
            // Not enabled leaf are added to the removed list
            if (lProductTreeNode.isLeaf() && !lProductTreeNode.isEnabled()) {
                lProductTreeNodeListToRemove.add(lProductTreeNode);
            }
        }

        if (lProductTreeNodeListToRemove.size() != 0) {
            // Remove all nodes present in the remove list.
            for (ProductTreeNode lProductTreeNodeToRemove : lProductTreeNodeListToRemove) {
                if (!lProductTreeNodeToRemove.isRoot()) {
                    ProductTreeNode lParentNode =
                            (ProductTreeNode) lProductTreeNodeToRemove.getParent();
                    lParentNode.remove(lProductTreeNodeToRemove);
                }
                // For a root node to be removed :
                // this node will be removed from the initial list
                else {
                    lRemoveThisNode = true;
                }
            }

            // If some leafs have been removed, continue the clean.
            if (!lRemoveThisNode) {
                lRemoveThisNode =
                        cleanDisabledProductTreeNode(pProductTreeNode);
            }
        }
        return lRemoveThisNode;
    }

    /**
     * Create the AttributeContainer linked with the AccessControl. Set the
     * access control extended attributes.
     * 
     * @param pAttributeDatas
     *            Extended attributes to set
     * @return the attributes container
     */
    private AttributesContainer createAccessControlAttributeContainer(
            AttributeData[] pAttributeDatas) {
        final AttributesContainer lAttributesContainer =
                AttributesContainer.newInstance();

        getAttributesContainerDao().create(lAttributesContainer);
        getAttributesService().set(lAttributesContainer.getId(),
                pAttributeDatas);

        return lAttributesContainer;
    }

    /**
     * Update Extended attributes of an Access control. Create or update the
     * previous Attributes container. Remove the attributes container if it's no
     * more defined.
     * 
     * @param pAccessControlData
     *            Access control data containing new definition of Access
     *            control.
     * @param pAccessControl
     *            Entity to update.
     */
    private void updateAttributesContainer(
            AccessControlData pAccessControlData, AccessControl pAccessControl) {

        if ((pAccessControlData.getExtendedAttributes() != null)
                && (pAccessControlData.getExtendedAttributes().length > 0)) {
            if (pAccessControl.getExtendedAttributes() != null) {
                getAttributesService().removeAll(
                        pAccessControl.getExtendedAttributes().getId());
                getAttributesService().set(
                        pAccessControl.getExtendedAttributes().getId(),
                        pAccessControlData.getExtendedAttributes());
            }
            else { //No Attributes container, create a new
                AttributesContainer lAttributesContainer =
                        createAccessControlAttributeContainer(pAccessControlData.getExtendedAttributes());
                pAccessControl.setExtendedAttributes(lAttributesContainer);
            }
        }
        else { //No extended attributes, must remove the oldest
            if (pAccessControl.getExtendedAttributes() != null) {
                getAttributesContainerDao().remove(
                        pAccessControl.getExtendedAttributes());
            }
        }
    }

    /**
     * Set extended attributes from an access control entity to an value object.
     * <p>
     * Use the Attributes service. The Access control entity id is the Attribute
     * Container id.
     * 
     * @param pAccessControlData
     *            Access control data to fill.
     * @param pAccessControl
     *            Access control as Attribute Container.
     */
    private void setExtendedAttributes(AccessControlData pAccessControlData,
            AccessControl pAccessControl) {
        AttributeData[] lAttributeDatas = null;
        if (pAccessControl.getExtendedAttributes() != null) {
            String lAttributesContainerId =
                    pAccessControl.getExtendedAttributes().getId();
            if (StringUtils.isNotBlank(lAttributesContainerId)) {
                lAttributeDatas =
                        getAttributesService().getAll(lAttributesContainerId);
            }
        }
        pAccessControlData.setExtendedAttributes(lAttributeDatas);
    }

    /**
     * Put in cache.
     * 
     * @param pKey
     *            the key
     * @param pAccessCtl
     *            the access ctl
     */
    private void putInCache(String pKey, Object pAccessCtl) {
        accessControlCache.put(new Element(pKey,
                CopyUtils.getImmutableCopy(pAccessCtl)));
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#deleteAllAccessControls(java.lang.String,
     *      java.lang.String)
     */
    @Override
    @SuppressWarnings("unchecked")
    public void deleteAllAccessControls(String pRoleToken,
            String pBusinessProcessName) {
        assertGlobalAdminRole(pRoleToken);

        BusinessProcess lProcess = getBusinessProcess(pBusinessProcessName);

        for (AccessControl lAccess : (List<AccessControl>) getAccessControlDao().getAll(
                lProcess)) {
            getAccessControlDao().remove(lAccess);
        }

        // Remove all access controls present in the cache.
        accessControlCache.removeAll();
    }

    /**
     * Get the Role DAO.
     * 
     * @return Data Access Object
     */
    public RoleDao getRoleDao() {
        return roleDao;
    }

    /**
     * Set the Role DAO.
     * 
     * @param pRoleDao
     *            the Role DAO
     */
    public void setRoleDao(RoleDao pRoleDao) {
        roleDao = pRoleDao;
    }

    /**
     * Get the RoleForProduct DAO.
     * 
     * @return Data Access Object
     */
    public RolesForProductDao getRolesForProductDao() {
        return rolesForProductDao;
    }

    /**
     * Sets the roles for product dao.
     * 
     * @param pRolesForProductDao
     *            the new roles for product dao
     */
    public void setRolesForProductDao(RolesForProductDao pRolesForProductDao) {
        rolesForProductDao = pRolesForProductDao;
    }

    /**
     * Delete RolesForProduct to a given product.
     * 
     * @param pProduct
     *            The product
     */
    // TODO declare in interface
    public void deleteRolesForProductByProduct(Product pProduct) {
        List<RolesForProduct> lRolesForProduct =
                getRolesForProductDao().getByProduct(pProduct);

        for (RolesForProduct lRfp : lRolesForProduct) {
            getRolesForProductDao().remove(lRfp);
        }
    }

    /**
     * Get the FieldAccessControl DAO.
     * 
     * @return Data Access Object
     */
    public FieldAccessControlDao getFieldAccessControlDao() {
        return fieldAccessControlDao;
    }

    /**
     * Sets the field access control dao.
     * 
     * @param pFieldAccessControlDao
     *            the new field access control dao
     */
    public void setFieldAccessControlDao(
            FieldAccessControlDao pFieldAccessControlDao) {
        fieldAccessControlDao = pFieldAccessControlDao;
    }

    /**
     * Get the SheetTypeAccessControl DAO.
     * 
     * @return Returns the sheetTypeAccessControl.
     */
    public TypeAccessControlDao getTypeAccessControlDao() {
        return typeAccessControlDao;
    }

    /**
     * setSheetTypeAccessControlDao.
     * 
     * @param pTypeAccessControlDao
     *            The sheetTypeAccessControlDao to set.
     */
    public void setTypeAccessControlDao(
            TypeAccessControlDao pTypeAccessControlDao) {
        typeAccessControlDao = pTypeAccessControlDao;
    }

    /**
     * getAppliActionAccessControlDao.
     * 
     * @return Returns the appliActionAccessControlDao.
     */
    public AppliActionAccessControlDao getAppliActionAccessControlDao() {
        return appliActionAccessControlDao;
    }

    /**
     * setAppliActionAccessControlDao.
     * 
     * @param pAppliActionAccessControlDao
     *            The to set.
     */
    public void setAppliActionAccessControlDao(
            AppliActionAccessControlDao pAppliActionAccessControlDao) {
        appliActionAccessControlDao = pAppliActionAccessControlDao;
    }

    /**
     * get adminAccessControlDao.
     * 
     * @return the adminAccessControlDao
     */
    public AdminAccessControlDao getAdminAccessControlDao() {
        return adminAccessControlDao;
    }

    /**
     * set adminAccessControlDao.
     * 
     * @param pAdminAccessControlDao
     *            the adminAccessControlDao to set
     */
    public void setAdminAccessControlDao(
            AdminAccessControlDao pAdminAccessControlDao) {
        this.adminAccessControlDao = pAdminAccessControlDao;
    }

    /**
     * getTransitionAccessControlDao Get the TransitionAccessControl DAO
     * singleton.
     * 
     * @return Returns the transitionAccessControlDao.
     */
    public TransitionAccessControlDao getTransitionAccessControlDao() {
        return transitionAccessControlDao;
    }

    /**
     * setTransitionAccessControlDao Set the TransitionAccessControl DAO
     * singleton. This method is called automatically by Spring and not intended
     * to be called by user code.
     * 
     * @param pTransitionAccessControlDao
     *            The transitionAccessControlDao to set.
     */
    public void setTransitionAccessControlDao(
            TransitionAccessControlDao pTransitionAccessControlDao) {
        transitionAccessControlDao = pTransitionAccessControlDao;
    }

    /**
     * Define the cache implementation to use for the access controls.
     * 
     * @param pAccessControlCache
     *            the accessControlCache to set
     */
    public final void setAccessControlCache(Cache pAccessControlCache) {
        accessControlCache = pAccessControlCache;
    }

    /** The role dao. */
    protected RoleDao roleDao;

    /** The roles for product dao. */
    protected RolesForProductDao rolesForProductDao;

    /** The field access control dao. */
    protected FieldAccessControlDao fieldAccessControlDao;

    /** The type access control dao. */
    protected TypeAccessControlDao typeAccessControlDao;

    /** The appli action access control dao. */
    protected AppliActionAccessControlDao appliActionAccessControlDao;

    /** The admin access control dao. */
    protected AdminAccessControlDao adminAccessControlDao;

    /** The transition access control dao. */
    protected TransitionAccessControlDao transitionAccessControlDao;

    /** The access control cache. */
    protected Cache accessControlCache;

    /** The field dao. */
    private FieldDao fieldDao;

    /** The attributes container dao. */
    private AttributesContainerDao attributesContainerDao;

    /**
     * getFieldDao.
     * 
     * @return the FieldDao
     */
    public FieldDao getFieldDao() {
        return fieldDao;
    }

    /**
     * setFieldDao.
     * 
     * @param pFieldDao
     *            the FieldDao to set
     */
    public void setFieldDao(FieldDao pFieldDao) {
        fieldDao = pFieldDao;
    }

    /**
     * Gets the attributes container dao.
     * 
     * @return the attributes container dao
     */
    public AttributesContainerDao getAttributesContainerDao() {
        return attributesContainerDao;
    }

    /**
     * Sets the attributes container dao.
     * 
     * @param pAttributesContainerDao
     *            the new attributes container dao
     */
    public void setAttributesContainerDao(
            AttributesContainerDao pAttributesContainerDao) {
        attributesContainerDao = pAttributesContainerDao;
    }

    /**
     * Clean the unused sessions.
     */
    // TODO declare in interface
    public void cleanExpiredSessions() {
        /* Remove all the invalid user context */
        final List<UserContext> lInvalidUsers = new ArrayList<UserContext>();

        for (UserContext lUser : userSessions.getAll()) {
            if (!lUser.isValid()) {
                lInvalidUsers.add(lUser);
            }
        }
        for (UserContext lInvalidUser : lInvalidUsers) {
            userSessions.remove(lInvalidUser.getToken());
            mainContext.publishEvent(new LogoutEvent(this, lInvalidUser));
        }

        /* Remove all the invalid role context */
        final List<RoleContext> lInvalidRoles = new ArrayList<RoleContext>();

        for (RoleContext lRole : roleSessions.getAll()) {
            if (!lRole.isValid()) {
                lInvalidRoles.add(lRole);
            }
        }
        for (RoleContext lInvalidRole : lInvalidRoles) {
            roleSessions.remove(lInvalidRole.getToken());
            mainContext.publishEvent(new LogoutEvent(this, lInvalidRole));
        }
    }

    /** The session max time. */
    private long sessionMaxTime;

    /**
     * Get the sessionMaxTime.
     * 
     * @return the sessionMaxTime
     */
    public long getSessionMaxTime() {
        return sessionMaxTime;
    }

    /**
     * Set the sessionMaxTime.
     * 
     * @param pSessionMaxTime
     *            the new sessionMaxTime
     */
    public void setSessionMaxTime(long pSessionMaxTime) {
        sessionMaxTime = pSessionMaxTime;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#isValidUserToken(java.lang.String)
     */
    @Override
    public boolean isValidUserToken(String pSessionToken) {
        return userSessions.isTokenValid(pSessionToken);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#isValidRoleToken(java.lang.String)
     */
    @Override
    public final boolean isValidRoleToken(String pSessionToken) {
        return roleSessions.isTokenValid(pSessionToken);
    }

    /**
     * Check the validity of a role session token
     * 
     * @param pSessionToken
     *            Role session token to check
     * @throws InvalidTokenException
     *             When the token is invalid
     */
    // TODO declare in interface
    public final void assertValidRoleToken(String pSessionToken) {
        if (!roleSessions.isTokenValid(pSessionToken)) {
            throw new InvalidTokenException(pSessionToken,
                    "Invalid role token ''{0}''");
        }
    }

    /**
     * get loginCaseSensitive.
     * 
     * @return the loginCaseSensitive
     */
    @Override
    public boolean isLoginCaseSensitive() {
        boolean lLoginCaseSensitive = loginCaseSensitive;

        AttributeData[] lAttributeDatas =
                getAttributesService().getGlobalAttributes(
                        new String[] { AttributesService.LOGIN_CASE_SENSITIVE });
        if (lAttributeDatas[0] != null) {
            lLoginCaseSensitive =
                    Boolean.parseBoolean(lAttributeDatas[0].getValues()[0]);
        }
        return lLoginCaseSensitive;
    }

    /**
     * set loginCaseSensitive.
     * 
     * @param pLoginCaseSensitive
     *            the loginCaseSensitive to set
     */
    public void setLoginCaseSensitive(boolean pLoginCaseSensitive) {
        loginCaseSensitive = pLoginCaseSensitive;
    }

    /** The overriddenRole dao. */
    private OverriddenRoleDao overriddenRoleDao;

    /**
     * Gets the overridenRole dao.
     * 
     * @return the overriddenRoleDao
     */
    public OverriddenRoleDao getOverriddenRoleDao() {
        return overriddenRoleDao;
    }

    /**
     * Sets the overridenRole dao.
     * 
     * @param pOverriddenRoleDao
     *            The new overriddenRole dao
     */
    public void setOverriddenRoleDao(OverriddenRoleDao pOverriddenRoleDao) {
        this.overriddenRoleDao = pOverriddenRoleDao;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#
     *      deleteOverriddenRole(java.lang.String, java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    @Override
    public void deleteOverriddenRole(String pRoleToken,
            String pValuesContainerId, String pUserLogin, String pRoleName) {
        assertAdminRole(pRoleToken);
        OverriddenRole lOverriddenRole =
                (OverriddenRole) getOverriddenRoleDao().getOverriddenRole(
                        pValuesContainerId, pUserLogin, pRoleName);
        if (lOverriddenRole != null) {
            deleteOverriddenRole(lOverriddenRole);
        }
    }

    /**
     * Delete an OverriddenRole in DB.
     * 
     * @param pOverriddenRole
     *            OverriddenRole to delete
     */
    private void deleteOverriddenRole(OverriddenRole pOverriddenRole) {

        getOverriddenRoleDao().remove(pOverriddenRole);

        // Invalidate cacheable object
        removeElementFromCache("OverriddenRole"
                + pOverriddenRole.getValuesContainer().getId()
                + pOverriddenRole.getEndUser().getLogin());
    }

    /**
     * Delete all OverriddenRole of a value container (sheet, link and product).
     * 
     * @param pValuesContainerId
     *            The value container ID
     */
    // TODO declare in interface
    public void deleteOverriddenRolesFromContainerId(String pValuesContainerId) {

        List<OverriddenRole> lOverriddenRoles =
                getOverriddenRoleDao().getOverriddenRolesFromContainerId(
                        pValuesContainerId);

        for (OverriddenRole lOverriddenRole : lOverriddenRoles) {
            deleteOverriddenRole(lOverriddenRole);
        }
    }

    /**
     * Delete all OverriddenRoles of an user.
     * 
     * @param pLogin
     *            The user login
     */
    // TODO declare in interface
    public void deleteOverriddenRolesFromLogin(String pLogin) {

        List<OverriddenRole> lOverriddenRoles =
                getOverriddenRoleDao().getOverriddenRolesFromUserLogin(pLogin);

        for (OverriddenRole lOverriddenRole : lOverriddenRoles) {
            deleteOverriddenRole(lOverriddenRole);
        }
    }

    /**
     * Delete all OverriddenRoles of a role.
     * 
     * @param pRoleName
     *            The role name
     */
    private void deleteOverriddenRolesFromRole(String pRoleName) {

        List<OverriddenRole> lOverriddenRoles =
                getOverriddenRoleDao().getOverriddenRolesFromRoleName(pRoleName);

        for (OverriddenRole lOverriddenRole : lOverriddenRoles) {
            deleteOverriddenRole(lOverriddenRole);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#
     *      getOverridenRoleName(java.lang.String, java.lang.String)
     */
    @Override
    public String getOverridenRoleName(String pRoleToken,
            String pValuesContainerId) {
        String lUserLogin = getLoginFromToken(pRoleToken);
        String lRoleName = getRoleNameFromToken(pRoleToken);
        return getOverridenRoleName(pValuesContainerId, lUserLogin, lRoleName);
    }

    /**
     * Get the overridden role name from a specific container, user and role.
     * 
     * @param pValuesContainerId
     *            The values container identifier
     * @param pUserLogin
     *            The user login
     * @param pRoleName
     *            The role name
     * @return the overridden role name if it exists.
     */
    private String getOverridenRoleName(String pValuesContainerId,
            String pUserLogin, String pRoleName) {

        CacheableOverriddenRole lCacheableOverriddenRole =
                getCachedElement(CacheableOverriddenRole.class,
                        CacheableOverriddenRole.PREFIX + pValuesContainerId
                                + pUserLogin, CACHE_IMMUTABLE_OBJECT);
        String lOverriddenRoleName;
        if (lCacheableOverriddenRole != null) {
            lOverriddenRoleName =
                    lCacheableOverriddenRole.getOverriddenRoleName(pRoleName);
            if (lOverriddenRoleName == null) {
                //We get it from DAO
                lCacheableOverriddenRole =
                        CopyUtils.getMutableCopy(lCacheableOverriddenRole);
                lOverriddenRoleName =
                        getOverriddenRoleDao().getOverriddenRoleName(
                                pValuesContainerId, pUserLogin, pRoleName);
                if (lOverriddenRoleName == null) {
                    //No overriddenRole found
                    lOverriddenRoleName = pRoleName;
                }
                lCacheableOverriddenRole.putOverriddenRoleInMap(pRoleName,
                        lOverriddenRoleName);
                addElementInCache(lCacheableOverriddenRole);
            }
        }
        else {
            lCacheableOverriddenRole =
                    new CacheableOverriddenRole(pValuesContainerId, pUserLogin);
            lOverriddenRoleName =
                    getOverriddenRoleDao().getOverriddenRoleName(
                            pValuesContainerId, pUserLogin, pRoleName);
            if (lOverriddenRoleName == null) {
                //No overriddenRole found
                lOverriddenRoleName = pRoleName;
            }
            lCacheableOverriddenRole.putOverriddenRoleInMap(pRoleName,
                    lOverriddenRoleName);
            addElementInCache(lCacheableOverriddenRole);
        }
        return lOverriddenRoleName;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#
     *      setOverriddenRole(java.lang.String, java.lang.String,
     *      java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void setOverriddenRole(String pRoleToken, String pValuesContainerId,
            String pUserLogin, String pOldRoleName, String pNewRoleName) {
        assertAdminRole(pRoleToken);

        String lPreviousOverridenRole =
                getOverridenRoleName(pValuesContainerId, pUserLogin,
                        pOldRoleName);

        if (lPreviousOverridenRole.equals(pOldRoleName)) {
            //Creation
            EndUser lUser = getEndUserDao().getUser(pUserLogin, false);
            ValuesContainer lContainer = getValuesContainer(pValuesContainerId);
            BusinessProcess lBusinessProcess =
                    getBusinessProcess(getBusinessProcessName(pRoleToken));
            Role lOldRole =
                    getRoleDao().getRole(pOldRoleName, lBusinessProcess);
            if (lOldRole == null) {
                throw new InvalidNameException(pOldRoleName,
                        "The role {0} doesn't exist");
            }
            Role lNewRole =
                    getRoleDao().getRole(pNewRoleName, lBusinessProcess);
            if (lNewRole == null) {
                throw new InvalidNameException(pNewRoleName,
                        "The role {0} doesn't exist");
            }
            OverriddenRole lRole = OverriddenRole.newInstance();
            lRole.setEndUser(lUser);
            lRole.setValuesContainer(lContainer);
            lRole.setNewRole(lNewRole);
            lRole.setOldRole(lOldRole);

            getOverriddenRoleDao().create(lRole);
        }
        else if (!lPreviousOverridenRole.equals(pNewRoleName)) {
            // Update
            OverriddenRole lOverriddenRole =
                    (OverriddenRole) getOverriddenRoleDao().getOverriddenRole(
                            pValuesContainerId, pUserLogin, pOldRoleName);
            BusinessProcess lBusinessProcess =
                    getBusinessProcess(getBusinessProcessName(pRoleToken));
            Role lNewRole =
                    getRoleDao().getRole(pNewRoleName, lBusinessProcess);
            if (lNewRole == null) {
                throw new InvalidNameException(pNewRoleName,
                        "The role {0} doesn't exist");
            }
            lOverriddenRole.setNewRole(lNewRole);
        }
        // Invalidate cacheable object
        removeElementFromCache(CacheableOverriddenRole.PREFIX
                + pValuesContainerId + pUserLogin);
    }

    /**
     * Compute access control cache key.
     * 
     * @param pAccessContext
     *            the access context
     * @param pAccessControlName
     *            the access control name
     * @param pSpecificParamName
     *            the specific param name
     * @param pSpecificParamValue
     *            the specific param value
     * @return the string
     */
    private String computeAccessControlCacheKey(
            AccessControlContextData pAccessContext, String pAccessControlName,
            String pSpecificParamName, String pSpecificParamValue) {
        final StringBuilder lCacheKeyBuilder =
                new StringBuilder(pAccessControlName);

        if (StringUtils.isNotBlank(pAccessContext.getStateName())) {
            lCacheKeyBuilder.append("_state|");
            lCacheKeyBuilder.append(pAccessContext.getStateName());
        }

        if (StringUtils.isNotBlank(pAccessContext.getProductName())) {
            lCacheKeyBuilder.append("_product|");
            lCacheKeyBuilder.append(pAccessContext.getProductName());
        }

        if (StringUtils.isNotBlank(pAccessContext.getContainerTypeId())) {
            lCacheKeyBuilder.append("_typeId|");
            lCacheKeyBuilder.append(pAccessContext.getContainerTypeId());
        }

        if (StringUtils.isNotBlank(pAccessContext.getRoleName())) {
            lCacheKeyBuilder.append("_role|");
            lCacheKeyBuilder.append(pAccessContext.getRoleName());
        }

        if (StringUtils.isNotBlank(pAccessContext.getVisibleTypeId())) {
            lCacheKeyBuilder.append("_visibleTypeId|");
            lCacheKeyBuilder.append(pAccessContext.getVisibleTypeId());
        }

        if (StringUtils.isNotBlank(pSpecificParamName)
                && StringUtils.isNotBlank(pSpecificParamValue)) {
            lCacheKeyBuilder.append(pSpecificParamName);
            lCacheKeyBuilder.append(pSpecificParamValue);
        }

        return lCacheKeyBuilder.toString();
    }

    /**
     * Get an initialized access control context data.
     * 
     * @param pRoleToken
     *            The role token
     * @param pStateName
     *            The name of the state
     * @param pFieldsContainerId
     *            The id of the fields container
     * @param pVisibleTypeId
     *            The id of the visible type
     * @param pValuesContainerId
     *            The id of the values container
     * @return The compute access control context data
     */
    // TODO declare in interface
    public AccessControlContextData getAccessControlContextData(
            String pRoleToken, String pStateName, String pFieldsContainerId,
            String pVisibleTypeId, String pValuesContainerId) {
        String lProductName = null;
        String lRoleName = null;

        if (pRoleToken != null) {
            lProductName = getProductNameFromSessionToken(pRoleToken);
            lRoleName = getRoleNameFromToken(pRoleToken);
        }

        return new AccessControlContextData(lProductName, lRoleName,
                pStateName, pFieldsContainerId, pVisibleTypeId,
                pValuesContainerId);
    }

    // TODO declare in interface
    public boolean isRoleExists(String pRoleName, String pBusinessProcessName) {
        return roleDao.isRoleExists(pBusinessProcessName, pRoleName);
    }

    private final static FilterScope[] FILTER_SCOPES =
            new FilterScope[] { FilterScope.INSTANCE_FILTER,
                               FilterScope.PRODUCT_FILTER,
                               FilterScope.USER_FILTER };

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#getExecutableFilterScope(java.lang.String)
     */
    @Override
    public List<FilterScope> getExecutableFilterScope(final String pRoleToken) {
        final List<FilterScope> lScopes = new LinkedList<FilterScope>();

        for (FilterScope lScope : FILTER_SCOPES) {
            if (filterAccessManager.getAccessOnFilterScope(pRoleToken, lScope).getExecutable()) {
                lScopes.add(lScope);
            }
        }

        return lScopes;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#getEditableFilterScope(java.lang.String)
     */
    public List<FilterScope> getEditableFilterScope(final String pRoleToken) {
        final List<FilterScope> lScopes = new LinkedList<FilterScope>();

        for (FilterScope lScope : FILTER_SCOPES) {
            if (filterAccessManager.getAccessOnFilterScope(pRoleToken, lScope).getEditable()) {
                lScopes.add(lScope);
            }
        }

        return lScopes;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#createFilterAccess(org.topcased.gpm.business.serialization.data.FilterAccessCtl)
     */
    @Override
    public void createFilterAccess(String pRoleToken,
            FilterAccessCtl pFilterAccessCtl) throws InvalidNameException,
        IllegalArgumentException, AuthorizationException,
        InvalidIdentifierException {
        if (hasGlobalAdminRole(pRoleToken)) {
            String lProcessName = getProcessNameFromToken(pRoleToken);
            //Check for role name existence
            if (StringUtils.isNotBlank(pFilterAccessCtl.getRoleName())) {
                if (!isRoleExists(pFilterAccessCtl.getRoleName(), lProcessName)) {
                    throw new InvalidNameException(
                            pFilterAccessCtl.getRoleName(),
                            "The role name {0} does not exists.");
                }
            }

            //Check for type name existence
            if (StringUtils.isNotBlank(pFilterAccessCtl.getTypeName())) {
                if (!fieldsContainerServiceImpl.isFieldsContainerExists(
                        lProcessName, pFilterAccessCtl.getTypeName())) {
                    throw new InvalidNameException(
                            pFilterAccessCtl.getTypeName(),
                            "The type name {0} does not exists.");
                }
            }

            //Check for field name existence
            if (StringUtils.isNotBlank(pFilterAccessCtl.getFieldName())) {
                if (StringUtils.isNotBlank(pFilterAccessCtl.getTypeName())) {
                    if (!UsableFieldsManager.SHEET_VIRTUAL_FIELDS.contains(pFilterAccessCtl.getFieldName())
                            && !fieldDao.isFieldExists(
                                    pFilterAccessCtl.getTypeName(),
                                    pFilterAccessCtl.getFieldName())) {
                        throw new InvalidNameException(
                                pFilterAccessCtl.getFieldName(),
                                "The field name {0} does not exists for the type '"
                                        + pFilterAccessCtl.getTypeName() + "'.");
                    }
                }
                else {
                    throw new IllegalArgumentException(
                            "When field name attribute is setting, "
                                    + "the type name attribute must be filled.");
                }
            }
            filterAccessManager.createFilterAccess(pFilterAccessCtl);
        }
        else {
            throw new AuthorizationException(
                    "No rights to create filter access "
                            + "(only global admin can create it).");
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#createFilterAccessConstraint(org.topcased.gpm.business.authorization.impl.filter.FilterAccessContraint)
     */
    @Override
    public void createFilterAccessConstraint(String pRoleToken,
            FilterAccessContraint pFilterAccessContraint)
        throws AuthorizationException {
        if (hasGlobalAdminRole(pRoleToken)) {
            filterAccessManager.createFilterAccessConstraint(pFilterAccessContraint);
        }
        else {
            throw new AuthorizationException(
                    "No rights to create filter access "
                            + "constraint (only global admin can create it).");
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#isFilterAccessConstraintExists(java.lang.String)
     */
    @Override
    public boolean isFilterAccessConstraintExists(String pConstraintName) {
        return filterAccessManager.isFilterAccessConstraintExists(pConstraintName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#getLogin(java.lang.String)
     */
    @Override
    public String getLogin(final String pToken) throws InvalidTokenException {
        // If the token is a role token, get the associated user token
        return userSessions.getContext(getUserToken(pToken)).getLogin();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#getProcessName(java.lang.String)
     */
    @Override
    public String getProcessName(final String pRoleToken)
        throws InvalidTokenException {
        return roleSessions.getContext(pRoleToken).getProcessName();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.service.AuthorizationService#getProductName(java.lang.String)
     */
    @Override
    public String getProductName(final String pRoleToken)
        throws InvalidTokenException {
        return roleSessions.getContext(pRoleToken).getProductName();
    }

    /**
     * Test the existence of a user.
     * <p>
     * Use login case sensitive attribute.
     * </p>
     * 
     * @param pLogin
     *            Login of the user to test (mandatory)
     * @return True if the user exists, false otherwise (login is blank)
     */
    // TODO declare in interface
    public boolean isUserExists(final String pLogin) {
        final boolean lIsExists;
        if (StringUtils.isNotBlank(pLogin)) {
            boolean lIsCaseSensitive = isLoginCaseSensitive();
            lIsExists = getEndUserDao().isExists(pLogin, lIsCaseSensitive);
        }
        else {
            lIsExists = false;
        }
        return lIsExists;
    }

    /**
     * Test if the user is connected with a process role.
     * 
     * @param pRoleToken
     *            The role token.
     * @return If the user is connected with a process role.
     */
    // TODO declare in interface
    public boolean isProcessAccess(final String pRoleToken) {
        final EndUser lEndUser = getUserFromToken(pRoleToken);
        final String lProcessName = getProcessName(pRoleToken);
        final String lRoleName = getRoleNameFromToken(pRoleToken);

        for (Role lAdminRole : lEndUser.getAdminRoles()) {
            if (StringUtils.equals(lProcessName,
                    lAdminRole.getBusinessProcess().getName())
                    && StringUtils.equals(lRoleName, lAdminRole.getName())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Get the user identifier.
     * <p>
     * Use login case sensitive attribute.
     * </p>
     * 
     * @param pLogin
     *            Login of the user to find (mandatory)
     * @return Technical identifier if user exists, Empty string otherwise
     *         (login is blank)
     */
    // TODO declare in interface
    public String getUserId(final String pLogin) {
        final String lUserId;
        if (StringUtils.isNotBlank(pLogin)) {
            boolean lIsCaseSensitive = isLoginCaseSensitive();
            lUserId = getEndUserDao().getId(pLogin, lIsCaseSensitive);
        }
        else {
            lUserId = StringUtils.EMPTY;
        }
        return lUserId;
    }

    /**
     * Retrieve the user's login from its technical identifier.
     * 
     * @param pUserId
     *            Technical identifier of the user.
     * @return User's login if found.
     */
    // TODO declare in interface
    public String getUserLogin(final String pUserId) {
        final String lLogin;
        if (StringUtils.isNotBlank(pUserId)) {
            lLogin = getEndUserDao().getLoginFromId(pUserId);
        }
        else {
            lLogin = StringUtils.EMPTY;
        }
        return lLogin;
    }

    /**
     * Test the role assignement to the user.
     * 
     * @param pUserLogin
     *            User to test.
     * @param pRoleName
     *            Role to test.
     * @return True if the role is assigned to the user, false otherwise.
     * @throws InvalidNameException
     *             The login is incorrect.
     */
    // TODO declare in interface
    public boolean isAssignTo(final String pUserLogin, final String pRoleName)
        throws InvalidNameException {
        boolean lFound = false;
        EndUser lUser = getUserFromLogin(pUserLogin);
        Set<Role> lRoles = lUser.getAdminRoles();
        Iterator<Role> lRoleIt = lRoles.iterator();
        while (!lFound && lRoleIt.hasNext()) {
            Role lRole = lRoleIt.next();
            lFound = lRole.getName().equals(pRoleName);
        }
        return lFound;
    }

    /**
     * Test the role assignement for the product to the user.
     * 
     * @param pUserLogin
     *            User to test
     * @param pRoleName
     *            Role to test.
     * @param pProductName
     *            Product to test.
     * @return True if the role is assigned on the product for the user, false
     *         otherwise.
     * @throws InvalidNameException
     *             The login is incorrect.
     */
    // TODO declare in interface
    public boolean isAssignTo(final String pUserLogin, final String pRoleName,
            final String pProductName) throws InvalidNameException {
        boolean lFound = false;
        EndUser lUser = getUserFromLogin(pUserLogin);
        Set<RolesForProduct> lRolesForProduct = lUser.getRolesForProducts();
        Iterator<RolesForProduct> lRoleIt = lRolesForProduct.iterator();
        while (!lFound && lRoleIt.hasNext()) {
            RolesForProduct lRoleForProduct = lRoleIt.next();
            if (lRoleForProduct.getProduct().getName().equals(pProductName)) {
                Iterator<Role> lRoles = lRoleForProduct.getRoles().iterator();
                while (!lFound && lRoles.hasNext()) {
                    Role lRole = lRoles.next();
                    lFound = lRole.getName().equals(pRoleName);
                }
            }
        }
        return lFound;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<EndUserData> getUsersFromLogins(final List<String> pLogins,
            final boolean pCaseSensitive, final boolean pLightUserData)
        throws GDMException {
        List<EndUser> lUsers =
                getEndUserDao().getUsersFromLogins(
                        (String[]) pLogins.toArray(new String[0]),
                        pCaseSensitive);
        List<EndUserData> lUsersData =
                new ArrayList<EndUserData>();
        for (EndUser lUser : lUsers) {
            EndUserData lEnduserData = createEndUserData(lUser, pLightUserData);
            lUsersData.add(lEnduserData);
        }
        return lUsersData;
    }

    @Override
    public boolean refreshSession(String pUserToken) {
        return userSessions.getContext(pUserToken).refresh();
    }
}
