/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin), Michael KARGBO (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.server.command.authorization;

import java.util.List;

import javax.servlet.http.HttpSession;

import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.exception.InvalidNameException;
import org.topcased.gpm.business.exception.InvalidValueException;
import org.topcased.gpm.business.util.log.GPMActionLogConstants;
import org.topcased.gpm.business.util.log.GPMLogger;
import org.topcased.gpm.ui.application.server.SessionAttributesEnum;
import org.topcased.gpm.ui.application.server.command.AbstractCommandActionHandler;
import org.topcased.gpm.ui.application.shared.command.authorization.AbstractConnectionResult;
import org.topcased.gpm.ui.application.shared.command.authorization.ConnectProductAction;
import org.topcased.gpm.ui.application.shared.command.authorization.ConnectResult;
import org.topcased.gpm.ui.application.shared.command.authorization.LoginAction;
import org.topcased.gpm.ui.application.shared.command.authorization.LoginResult;
import org.topcased.gpm.ui.application.shared.command.authorization.LogoutAction;
import org.topcased.gpm.ui.application.shared.command.authorization.SelectProcessResult;
import org.topcased.gpm.ui.application.shared.command.authorization.SelectProductResult;
import org.topcased.gpm.ui.application.shared.command.authorization.SelectSheetResult;
import org.topcased.gpm.ui.application.shared.command.init.InitInfo;
import org.topcased.gpm.ui.application.shared.command.sheet.GetSheetCreationAction;
import org.topcased.gpm.ui.application.shared.command.sheet.GetSheetEditionAction;
import org.topcased.gpm.ui.application.shared.command.sheet.GetSheetResult;
import org.topcased.gpm.ui.application.shared.command.sheet.GetSheetVisualizationAction;
import org.topcased.gpm.ui.application.shared.exception.ExternalAuthenticationException;
import org.topcased.gpm.ui.application.shared.util.PreSelectionEnum;
import org.topcased.gpm.ui.facade.server.FacadeLocator;
import org.topcased.gpm.ui.facade.server.attribute.AttributeFacade;
import org.topcased.gpm.ui.facade.server.authorization.AuthorizationFacade;
import org.topcased.gpm.ui.facade.server.authorization.UiUserSession;
import org.topcased.gpm.ui.facade.server.i18n.I18nFacade;
import org.topcased.gpm.ui.facade.server.i18n.I18nTranslationManager;
import org.topcased.gpm.ui.facade.server.i18n.impl.I18nFacadeImpl;
import org.topcased.gpm.ui.facade.shared.container.product.UiProduct;
import org.topcased.gpm.ui.facade.shared.container.product.UiProductHierarchy;
import org.topcased.gpm.ui.facade.shared.exception.UiSessionException;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Handles {@link LoginAction}
 * <p>
 * Directly connect the user to a 'process' and a 'product' if no choice is
 * available.
 * </p>
 * <h4>Action chain</h4>
 * <ol>
 * <li>Try authenticate the user</li>
 * <li>Getting process, products and sheet</li>
 * <li>Get elements to initialize the UI application</li>
 * </ol>
 * <h4>Action result</h4>
 * <ul>
 * <li>{@link LoginAction}
 * <dl>
 * <dt>Login</dt>
 * <dd>Try authenticate the user.</dd>
 * <dl>
 * <dt>OnSuccess: user authenticated</dt>
 * <dd>Continue the chain</dd>
 * <dt>OnFailure: user is not authenticated</dt>
 * <dd>Return {@link LoginResult} setting at <code>false</code></dd>
 * </dl>
 * <dt>Getting process</dt>
 * <dd>If one process, this is automatically selected. Otherwise returned an
 * {@link SelectProcessResult} to let the user choice its process.</dd>
 * <dt>Getting product</dt>
 * <dd>If one product, this is automatically selected. Otherwise returned an
 * {@link SelectProductResult} to let the user choice its product.</dd>
 * <dt>Getting sheet</dt>
 * <dd>If one sheet, this is automatically selected. Otherwise returned an
 * {@link SelectSheetResult} to let the user search its sheet.</dd> </dl></li>
 * <li>{@link ConnectProductAction} On Product selected, retrieve the roles (and
 * select the first one), try to open a user's session with the selected
 * process, product and role. Retrieve global actions, extended action and
 * filters.</li>
 * </ul>
 * 
 * @author nveillet
 */
public class LoginHandler extends
        AbstractCommandActionHandler<LoginAction, AbstractConnectionResult> {
    
    /** GPM Logger */
    private GPMLogger gpmLogger = GPMLogger.getLogger(LoginHandler.class);

    
    /**
     * Create the LoginHandler
     * 
     * @param pHttpSession
     *            The http session
     */
    @Inject
    public LoginHandler(Provider<HttpSession> pHttpSession) {
        super(pHttpSession);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#execute(net.customware.gwt.dispatch.shared.Action,
     *      net.customware.gwt.dispatch.server.ExecutionContext)
     */
    @Override
    public AbstractConnectionResult execute(LoginAction pAction,
            ExecutionContext pContext) throws ActionException {
        // Login to the core
        final boolean lLoginResult = login(pAction);
        AbstractConnectionResult lResult = null;

        if (lLoginResult) {
            // get the initialization information
            InitInfo lInitInfo = getInitInformation(pAction);

            // Check the locale
            if (!lInitInfo.getLocale().isEmpty()
                    && !pAction.getLocaleName().equals(lInitInfo.getLocale())) {
                lResult = new LoginResult(true);
                lResult.setInitInfo(lInitInfo);
                return lResult;
            }

            try {
                final AuthorizationFacade lAuthorizationFacade =
                        getFacadeLocator().getAuthorizationFacade();

                // Create local variable
                String lProcessName =
                        pAction.getParameter(PreSelectionEnum.PROCESS.name());
                String lProductName =
                        pAction.getParameter(PreSelectionEnum.PRODUCT.name());
                String lSheetReference =
                        pAction.getParameter(PreSelectionEnum.SHEETREF.name());
                String lSheetId =
                        pAction.getParameter(PreSelectionEnum.SHEETID.name());
                String lSheetDisplayMode =
                        pAction.getParameter(PreSelectionEnum.MODE.name());
                String lSheetType =
                        pAction.getParameter(PreSelectionEnum.SHEETTYPE.name());

                /**
                 * SELECT PROCESS
                 */
                if (lSheetId != null) {
                    lProcessName =
                            getFacadeLocator().getSheetFacade().getProcessName(
                                    getUserSession(), lSheetId);
                }

                if (lProcessName == null) {
                    List<String> lAvailableProcessNames =
                            lAuthorizationFacade.getAvailableProcesses(getUserSession());
                    if (lAvailableProcessNames.size() > 1) {
                        if (lProductName != null || lSheetReference != null) {
                            throw new AuthorizationException(
                                    "Bad URL format: the ''process'' parameter must be specified.");
                        }
                        lResult =
                                new SelectProcessResult(lAvailableProcessNames);
                    }
                    else if (lAvailableProcessNames.size() == 0) {
                        throw new AuthorizationException(
                                "No processes available.");
                    }
                    else {
                        lProcessName = lAvailableProcessNames.get(0);
                    }
                }
                getUserSession().setProcessName(lProcessName);
                lInitInfo.setProcessName(lProcessName);

                /**
                 * SELECT PRODUCT
                 */
                if (lProductName == null && lSheetId != null) {
                    lProductName =
                            getFacadeLocator().getSheetFacade().getProductName(
                                    getUserSession(), lSheetId);
                }

                if (lResult == null && lProductName == null) {
                    // get available products
                    List<UiProduct> lAvailableProductNames =
                            lAuthorizationFacade.getAvailableUiProducts(getUserSession());
                    if (lAvailableProductNames.size() > 1) { // Use the higher role to connect
                        if (lSheetReference != null) {
                            throw new AuthorizationException(
                                    "Bad URL format: the ''product'' parameter must be specified.");
                        }
                        List<UiProductHierarchy> lProductHierarchy =
                                lAuthorizationFacade.getAvailableProductsHierarchy(getUserSession());

                        lResult =
                                new SelectProductResult(lAvailableProductNames,
                                        lProductHierarchy);
                    }
                    else if (lAvailableProductNames.size() == 1) { // Connect on the only product 
                        lProductName = lAvailableProductNames.get(0).getName();
                    }
                }
                if (lResult == null) {
                    if (lProductName != null) { // Product name was set
                        lResult =
                                pContext.execute(new ConnectProductAction(
                                        lProductName));
                    }
                    else { // No product name was set, login without connecting to a product
                        lResult = new LoginResult(true);
                    }
                }

                /**
                 * SELECT SHEET BY REFERENCE
                 */
                if (lResult instanceof ConnectResult && lSheetReference != null) {
                    lSheetId =
                            getFacadeLocator().getSheetFacade().getId(
                                    getSession(lProductName), lSheetReference);

                    if (StringUtils.isBlank(lSheetId)) {
                        throw new InvalidNameException(lSheetReference,
                                "Invalid sheet reference ''{0}''.");
                    }
                }

                /**
                 * SELECT SHEET BY ID
                 */
                if (lResult instanceof ConnectResult && lSheetId != null) {
                    GetSheetResult lSheetResult;
                    if ("modification".equals(lSheetDisplayMode)) {
                        lSheetResult =
                                pContext.execute(new GetSheetEditionAction(
                                        lProductName, lSheetId));
                    }
                    else if (lSheetDisplayMode == null
                            || "visualization".equals(lSheetDisplayMode)) {
                        lSheetResult =
                                pContext.execute(new GetSheetVisualizationAction(
                                        lProductName, lSheetId));
                    }
                    else {
                        throw new InvalidValueException(lSheetDisplayMode,
                                "Invalid ''mode'' parameter value ''{0}''.");
                    }
                    lSheetResult.setConnectResult((ConnectResult) lResult);

                    // Connect result
                    lResult = new SelectSheetResult(lSheetResult);
                }
                else if (lResult instanceof ConnectResult && lSheetId == null) {
                    GetSheetResult lSheetResult;
                    //Open a new sheet in edition mode according to the sheetType
                    if ("creation".equals(lSheetDisplayMode)) {
                        lSheetResult =
                                pContext.execute(new GetSheetCreationAction(
                                        lProductName, lSheetType));
                        lSheetResult.setConnectResult((ConnectResult) lResult);

                        // Connect result
                        lResult = new SelectSheetResult(lSheetResult);
                    }
                }
                // set the initialization information
                if (getUserSession().getProcessName() != null) {
                    lInitInfo.setAdminAccess(lAuthorizationFacade.hasAdministrationModuleAccess(getUserSession()));
                    // Result is a LoginResult means there are no products, and user tries to log
                    // as admin
                    if (lProductName == null && lResult instanceof LoginResult
                            && !lInitInfo.isAdminAccess()) {
                        throw new AuthorizationException(
                                "No available product was found for user.");
                    }
                }
                lResult.setInitInfo(lInitInfo);
            }
            catch (RuntimeException e) {
                pContext.execute(new LogoutAction());
                throw e;
            }
            catch (ActionException e) {
                pContext.execute(new LogoutAction());
                throw e;
            }
        }
        else {
            lResult = new LoginResult(false);
        }

        lResult.setVersion(getFacadeLocator().getAuthorizationFacade().getVersion());

        return lResult;
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    @Override
    public Class<LoginAction> getActionType() {
        return LoginAction.class;
    }

    /**
     * Get the initialization information
     * 
     * @return the initialization information
     */
    private InitInfo getInitInformation(final LoginAction pAction) {
        final I18nFacade lI18nFacade = getFacadeLocator().getI18nFacade();
        final UiUserSession lSession = getUserSession();

        InitInfo lInitInfo = new InitInfo();
        final String lLocale = lSession.getLanguage();
        I18nTranslationManager lTranslationManager =
                lI18nFacade.getTranslationManager(lLocale);
        lInitInfo.setTextValues(lTranslationManager.getAllTextTranslations());
        lInitInfo.setImgValues(lTranslationManager.getAllImageTranslations());
        lInitInfo.setLocale(getLocale(pAction.getAvailableLocale(), lLocale));
        List<String> lAvailableLanguages =
                getFacadeLocator().getI18nFacade().getAvailableLanguages();
        lInitInfo.setLanguage(getLocale(
                lAvailableLanguages.toArray(new String[0]), lLocale));
        if (StringUtils.isBlank(lInitInfo.getLanguage())) {
            lInitInfo.setLanguage(I18nFacadeImpl.DEFAULT_LANGUAGE);
        }
        lInitInfo.setLogin(lSession.getLogin());
        lInitInfo.setHelpUrl(getFacadeLocator().getAttributeFacade().getHelpUrl());
        lInitInfo.setContactUrl(getFacadeLocator().getAttributeFacade().getContactUrl());

        if (getFacadeLocator().getAttributeFacade().getAuthenticationSystem().size() <= 1) {
            lInitInfo.setExternalAuthentication(false);
        }
        else {
            lInitInfo.setExternalAuthentication(true);
        }

        return lInitInfo;
    }

    /**
     * Try to login the user to the core
     * 
     * @param pAction
     *            the action
     * @return if the user is logged
     * @throws ExternalAuthenticationException
     */
    private boolean login(LoginAction pAction)
        throws ExternalAuthenticationException {
        final AuthorizationFacade lAuthorizationFacade =
                getFacadeLocator().getAuthorizationFacade();

        UiUserSession lSession = null;
        try {
            lSession = getUserSession();
        }
        catch (UiSessionException e) {
            // Do nothing because we want login
        }

        final boolean lLogged;

        if (lSession != null) {
            lLogged = true;
        }
        else {
            final HttpSession lHttpSession = getHttpSession();
            final UiUserSession lUserSession;
            SessionAttributesEnum lAuthSystem =
                    (SessionAttributesEnum) getHttpSession().getAttribute(
                            SessionAttributesEnum.AUTHENTICATION_SYSTEM_TYPE.name());

            // Is internal or external authentication -> First connection
            if (lAuthSystem == null) {
                // Need transaction to load attributes : only the first time
                final AttributeFacade lAttributeFacade =
                        FacadeLocator.instance().getAttributeFacade();
                final List<String> lAuth =
                        lAttributeFacade.getAuthenticationSystem();

                if (lAuth.size() <= 1) {
                    // Internal authentication
                    lHttpSession.setAttribute(
                            SessionAttributesEnum.AUTHENTICATION_SYSTEM_TYPE.name(),
                            SessionAttributesEnum.INTERNAL);
                    lAuthSystem = SessionAttributesEnum.INTERNAL;
                }
                else {
                    // External authentication
                    lHttpSession.setAttribute(
                            SessionAttributesEnum.AUTHENTICATION_SYSTEM_TYPE.name(),
                            SessionAttributesEnum.EXTERNAL);
                    lHttpSession.setAttribute(
                            SessionAttributesEnum.AUTHENTICATION_SYSTEM_USER_ID.name(),
                            pAction.getParameter(lAuth.get(1)));
                    lAuthSystem = SessionAttributesEnum.EXTERNAL;
                }
            }

            switch (lAuthSystem) {
                case EXTERNAL:
                    final String lLogin =
                            (String) getHttpSession().getAttribute(
                                    SessionAttributesEnum.AUTHENTICATION_SYSTEM_USER_ID.name());
                    if (StringUtils.isBlank(lLogin)) {
                        clearSession();
                        throw new ExternalAuthenticationException(
                                ExternalAuthenticationException.MISSING_USER_ID);
                    }
                    else {
                        lUserSession = lAuthorizationFacade.login(lLogin, "");
                    }
                    break;
                case INTERNAL:
                    lUserSession =
                            lAuthorizationFacade.login(pAction.getLogin(),
                                    pAction.getPassword());
                    break;
                default:
                    lUserSession = null;
            }
            if (lUserSession == null) {
                lLogged = false;
            }
            else {
                lUserSession.setApplicationUrl(pAction.getApplicationUrl());

                // Create User session
                getHttpSession().setAttribute(
                        SessionAttributesEnum.GPM_USER_SESSION_ATTR.name(),
                        lUserSession);
                lLogged = true;

                // Log connection
                if (pAction.getLogin() != null) {
                    gpmLogger.highInfo(pAction.getLogin(), GPMActionLogConstants.USER_CONNECTION);
                } else {
                    if (lAuthSystem == SessionAttributesEnum.EXTERNAL) {
                        final String lLogin =
                                (String) getHttpSession().getAttribute(
                                        SessionAttributesEnum.AUTHENTICATION_SYSTEM_USER_ID.name());
                        if (lLogin != null) {
                            gpmLogger.highInfo(lLogin, GPMActionLogConstants.USER_CONNECTION);
                        }
                    }
                }
            }
        }
        return lLogged;
    }

    private String getLocale(final String[] pAvailableLocale,
            final String pBusinessLocale) {
        final String lUiLocale;
        String lBusinessLocale = pBusinessLocale;
        if (pBusinessLocale.equals("en") || pBusinessLocale.equals("en_EN")) {
            lBusinessLocale = "en_GB";
        }
        if (lBusinessLocale != null && !lBusinessLocale.isEmpty()) {
            int i = 0;
            boolean lFound = false;
            while (!lFound && i < pAvailableLocale.length) {
                if (lBusinessLocale.contains(pAvailableLocale[i])) {
                    lFound = true;
                }
                else {
                    i++;
                }
            }
            if (lFound) {
                lUiLocale = pAvailableLocale[i];
            }
            else {
                lUiLocale = "";
            }
        }
        else {
            lUiLocale = "";
        }
        return lUiLocale;
    }
}