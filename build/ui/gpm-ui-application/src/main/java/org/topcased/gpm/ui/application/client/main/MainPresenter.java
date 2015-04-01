/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.main;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.customware.gwt.presenter.client.EventBus;
import net.customware.gwt.presenter.client.place.Place;

import org.topcased.gpm.ui.application.client.Application;
import org.topcased.gpm.ui.application.client.admin.AdminPresenter;
import org.topcased.gpm.ui.application.client.command.connection.InitAdminSpaceCommand;
import org.topcased.gpm.ui.application.client.command.connection.LaunchSheetAction;
import org.topcased.gpm.ui.application.client.command.connection.LaunchWorkspaceAction;
import org.topcased.gpm.ui.application.client.command.connection.LogoutCommand;
import org.topcased.gpm.ui.application.client.command.connection.OpenAdminSpaceCommand;
import org.topcased.gpm.ui.application.client.command.connection.OpenUserSpaceCommand;
import org.topcased.gpm.ui.application.client.common.AbstractPresenter;
import org.topcased.gpm.ui.application.client.event.ActionEventHandler;
import org.topcased.gpm.ui.application.client.event.EmptyAction;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.client.user.UserSpacePresenter;
import org.topcased.gpm.ui.application.shared.command.authorization.AbstractConnectionResult;
import org.topcased.gpm.ui.application.shared.command.authorization.ConnectResult;
import org.topcased.gpm.ui.application.shared.command.authorization.LogoutResult;
import org.topcased.gpm.ui.application.shared.command.authorization.OpenAdminModuleResult;
import org.topcased.gpm.ui.application.shared.command.authorization.SelectProcessResult;
import org.topcased.gpm.ui.application.shared.command.authorization.SelectSheetResult;
import org.topcased.gpm.ui.application.shared.command.user.GetUserProfileAction;
import org.topcased.gpm.ui.component.client.resources.i18n.Ui18n;
import org.topcased.gpm.ui.component.client.util.GpmAnchorTarget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ClosingEvent;
import com.google.gwt.user.client.Window.ClosingHandler;
import com.google.inject.Inject;

/**
 * The presenter for the main frame.
 * 
 * @author tpanuel
 */
public class MainPresenter extends AbstractPresenter<MainDisplay> {

    /**
     * The session Id
     */
    private static String sessionId;

    /**
     * Cookie name to indicate the current opened tabs
     */
    private static final String OPEN_TAB_COOKIE =
            GWT.getModuleBaseURL() + "_OPEN_TABS";

    /**
     * Cookie name to indicate the number of opened tabs
     */
    public static final String NUMBER_TABS =
            GWT.getModuleBaseURL() + "_NUMBER_TABS";

    /**
     * Cookie name to indicate the tabs to close
     */
    private static final String FORCE_CLOSE_TABS_COOKIE =
            GWT.getModuleBaseURL() + "_FORCE_CLOSE";

    /**
     * Life time of FLAG_FORCE_LOGOUT cookie, to be removed for next login
     */
    private static final int COOKIE_LIFE_TIME = 8 * 1000;

    /**
     * It must be lower than COOKIE_LIFE_TIME, 3/4 of COOKIE_LIFE_TIME is a good
     * value
     */
    private static final int COOKIE_FLAG_CONTROL_PERIOD =
            (int) (0.75 * COOKIE_LIFE_TIME);

    /**
     * Unique application identifier
     */
    private static final String APPLICATION_ID =
            "#" + new Long(System.currentTimeMillis()).toString();

    /**
     * Indicates if the application was forced to close for multitab reason
     */
    private static boolean staticForceLogoutFlag = false;

    private static Timer staticCookieControlTask;

    private final UserSpacePresenter userSpace;

    private final AdminPresenter adminSpace;

    private final InitAdminSpaceCommand initAdmin;

    private final OpenAdminSpaceCommand openAdmin;

    private final OpenUserSpaceCommand openUser;

    private final LogoutCommand logout;

    private final List<HandlerRegistration> registrations;

    private boolean connected;

    /**
     * The confirmation pop up is displayed twice on IE8, when an user close the
     * window the closed event is sent twice. <code>staticLogOutFlag</code> is
     * used to indicate that the event has been take in consideration and the
     * current user is already logged out, so the pop up should not be displayed
     * again <code>true</code> if the pop up have been displayed and
     * <code>false</code> if not
     */
    private static boolean staticLogOutFlag = false;

    /**
     * Create a MainPresenter.
     * 
     * @param pDisplay
     *            The display.
     * @param pEventBus
     *            The event bus.
     * @param pUserSpace
     *            The user space presenter.
     * @param pAdminSpace
     *            The admin space presenter.
     * @param pInitAdmin
     *            admin init command
     * @param pOpenAdmin
     *            open admin command
     * @param pOpenUser
     *            open user command
     * @param pLogout
     *            The logout command.
     */
    @Inject
    public MainPresenter(final MainDisplay pDisplay, final EventBus pEventBus,
            final UserSpacePresenter pUserSpace,
            final AdminPresenter pAdminSpace,
            final InitAdminSpaceCommand pInitAdmin,
            final OpenAdminSpaceCommand pOpenAdmin,
            final OpenUserSpaceCommand pOpenUser, final LogoutCommand pLogout) {
        super(pDisplay, pEventBus);
        userSpace = pUserSpace;
        adminSpace = pAdminSpace;
        initAdmin = pInitAdmin;
        openAdmin = pOpenAdmin;
        openUser = pOpenUser;
        logout = pLogout;
        connected = false;
        registrations = new ArrayList<HandlerRegistration>();
    }

    /**
     * Set the loading panel visibility.
     * 
     * @param pVisibility
     *            If the loading panel is displayed.
     */
    public void setLoadingPanelVisibility(final boolean pVisibility) {
        getDisplay().setLoadingPanelVisibility(pVisibility);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.presenter.client.BasicPresenter#getPlace()
     */
    @Override
    public Place getPlace() {
        return null;
    }

    /**
     * Initiate a flag depending if it exists or not, and initiate the Timer
     * task to check the cookie value repeatedly
     */
    private void initiateCookieFlagTimer() {
        sessionId = Cookies.getCookie("JSESSIONID");
        // Retrieve Cookie containing number of open tabs.
        String lNumberTab = Cookies.getCookie(NUMBER_TABS + "_" + sessionId);
        if (lNumberTab == null || lNumberTab.equals("")) {
            Cookies.setCookie(NUMBER_TABS + "_" + sessionId, "1");
        }
        else {
            int lCount =
                    Integer.parseInt(Cookies.getCookie(NUMBER_TABS + "_"
                            + sessionId));
            Cookies.setCookie(NUMBER_TABS + "_" + sessionId, ++lCount + "");
        }

        // Initialize Cookie and Cookie Control task if not already initialized
        if (staticCookieControlTask == null) {
            String lFlag = Cookies.getCookie(OPEN_TAB_COOKIE);
            if (lFlag == null) { // Create cookie
                lFlag = "";
            }
            Cookies.setCookie(OPEN_TAB_COOKIE, lFlag + APPLICATION_ID);

            // Schedule a timer to run before end of cookie lifetime
            staticCookieControlTask = new Timer() {
                public void run() {
                    String lFlag = Cookies.getCookie(FORCE_CLOSE_TABS_COOKIE);

                    // If cookie FORCE_LOGOUT contains the app id, force logout
                    if (lFlag != null && lFlag.contains(APPLICATION_ID)) {
                        staticForceLogoutFlag = true;

                        if (!Application.INJECTOR.getLogoutCommand().islastOpenedTab()) {
                            return;
                        }

                        Application.INJECTOR.getConfirmationMessagePresenter().displayQuestion(
                                Ui18n.MESSAGES.confirmationMultiTabForcedReload(),
                                new ClickHandler() {
                                    @Override
                                    public void onClick(ClickEvent pArg0) {
                                        unbind();
                                        forceCloseAndReloadApp();
                                    }
                                });
                    }
                }
            };
            staticCookieControlTask.scheduleRepeating(COOKIE_FLAG_CONTROL_PERIOD);
        }
    }

    /**
     * Force all other windows opened in the same browser on the same base URL
     * to logout, when multiple tabs were opened
     */
    private static void forceCloseAndReloadApp() {
        String lFlag = Cookies.getCookie(OPEN_TAB_COOKIE);

        /**
         * The user decide to quite the application, thus we set the flag to
         * <code>true</code> to said that the <code>onClose</code> event has
         * been sent then the application is going to be redirected on a new URL
         */
        staticLogOutFlag = true;
        if (lFlag != null) {
            // Remove app id from open flags
            lFlag = lFlag.replace(APPLICATION_ID, "");
            Cookies.setCookie(OPEN_TAB_COOKIE, lFlag);

            if (!staticForceLogoutFlag) { // If was not forced
                // Set all other apps to force close
                Cookies.setCookie(FORCE_CLOSE_TABS_COOKIE, lFlag, new Date(
                        new Date().getTime() + COOKIE_LIFE_TIME));
            }
        }
        String lUrl = rebuildApplicationURL();
        Window.Location.replace(lUrl);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.presenter.client.BasicPresenter#onBind()
     */
    @SuppressWarnings("rawtypes")
	@Override
    protected void onBind() {
        addEventHandler(GlobalEvent.CONNECTION.getType(),
                new ActionEventHandler<AbstractConnectionResult>() {
                    @Override
                    public void execute(final AbstractConnectionResult pResult) {
                        onConnection(pResult);
                    }
                });
        addEventHandler(GlobalEvent.INIT_ADMIN_SPACE.getType(),
                new ActionEventHandler<OpenAdminModuleResult>() {
                    @Override
                    public void execute(OpenAdminModuleResult pResult) {
                        adminSpace.initAdminSpace(pResult);
                        adminSpace.bind();
                        getDisplay().setContent(
                                adminSpace.getDisplay().asWidget());
                        getDisplay().setSwitchButtonHandler(openUser);
                    }
                });
        addEventHandler(GlobalEvent.OPEN_ADMIN_SPACE.getType(),
                new ActionEventHandler<EmptyAction>() {
                    @Override
                    public void execute(EmptyAction pResult) {
                        getDisplay().setContent(
                                adminSpace.getDisplay().asWidget());
                        getDisplay().setSwitchButtonHandler(openUser);
                    }
                });
        addEventHandler(GlobalEvent.OPEN_USER_SPACE.getType(),
                new ActionEventHandler<EmptyAction>() {
                    @Override
                    public void execute(EmptyAction pResult) {
                        getDisplay().setContent(
                                userSpace.getDisplay().asWidget());
                        getDisplay().setSwitchButtonHandler(openAdmin);
                    }
                });
        // Bind user space
        userSpace.bind();
    }

    private void onConnection(final AbstractConnectionResult pResult) {
        // Set the version displayed in HMI
        if (pResult.getVersion() != null) {
            getDisplay().setVersion(pResult.getVersion());
        }

        if (pResult instanceof ConnectResult) {
            fireEvent(GlobalEvent.LAUNCH_WORKSPACE.getType(),
                    new LaunchWorkspaceAction((ConnectResult) pResult));
        }
        if (pResult instanceof SelectSheetResult) {
            // Initialize the main view is need
            fireEvent(GlobalEvent.LAUNCH_SHEET.getType(),
                    new LaunchSheetAction(
                            ((SelectSheetResult) pResult).getResult()));
        }
        // The user is connected
        if (pResult.getInitInfo() != null) {
            String lLocale = pResult.getInitInfo().getLocale();

            // Changer the locale
            if (!lLocale.isEmpty()
                    && !LocaleInfo.getCurrentLocale().getLocaleName().equals(
                            lLocale)) {
                final UrlBuilder lUrlBuilder =
                        Window.Location.createUrlBuilder();

                lUrlBuilder.setParameter("locale", lLocale);
                Window.Location.replace(lUrlBuilder.buildString());
            }
            else {
                // Initialize banner if a process is selected
                if (!(pResult instanceof SelectProcessResult)) {

                    // Version
                    final String lLogin = pResult.getInitInfo().getLogin();
                    // User information
                    getDisplay().setLoginAndLanguage(lLogin,
                            pResult.getInitInfo().getLanguage());
                    // Contact URL
                    getDisplay().setContactUrl(
                            pResult.getInitInfo().getContactUrl());
                    // Process Name
                    getDisplay().setProcessName(
                            pResult.getInitInfo().getProcessName());
                    // Go the administration space button
                    if (pResult.getInitInfo().isAdminAccess()) {
                        getDisplay().setSwitchButtonHandler(initAdmin);
                    }
                    // Logout button only if in internal authentication
                    if (!pResult.getInitInfo().isExternalAuthentication()) {
                        getDisplay().addLogoutButton(logout);
                    }
                    addEventHandler(GlobalEvent.LOGOUT.getType(),
                            new ActionEventHandler<LogoutResult>() {
                                @Override
                                public void execute(LogoutResult pResult) {
                                    unbind();
                                    // Set cookie to FORCE_LOGOUT
                                    forceCloseAndReloadApp();
                                }
                            });
                    // Help button
                    if (pResult.getInitInfo().getHelpUrl() != null) {
                        getDisplay().addHelpButton(new ClickHandler() {
                            @Override
                            public void onClick(ClickEvent pEvent) {
                                Window.open(pResult.getInitInfo().getHelpUrl(),
                                        GpmAnchorTarget.BLANK.getValue(), "");
                            }
                        });
                    }
                    getDisplay().addUserProfileEditionHandler(
                            new ClickHandler() {
                                @Override
                                public void onClick(ClickEvent pEvent) {
                                    fireEvent(
                                            GlobalEvent.OPEN_USER_PROFILE_POPUP.getType(),
                                            new GetUserProfileAction());
                                }
                            });
                    displayUserSpace();
                }
                // Add handlers on windows close or refresh
                if (!connected) {
                    initiateCookieFlagTimer();

                    // Add handlers on windows close or refresh
                    registrations.add(Window.addWindowClosingHandler(new ClosingHandler() {
                        @Override
                        public void onWindowClosing(final ClosingEvent pEvent) {
                            // Set cookie to FORCE_LOGOUT
                            /**
                             * Checks if the pop up message has been displayed
                             * before Could not be set to true after the event
                             * because the user can decide to cancel the pop up
                             */
                            if (!staticLogOutFlag) {
                                pEvent.setMessage(Ui18n.MESSAGES.confirmationClose());
                            }
                            else {
                                /**
                                 * Reinitializes the flag to avoid deadlock
                                 */
                                staticLogOutFlag = false;
                            }

                        }
                    }));
                    registrations.add(Window.addCloseHandler(Application.INJECTOR.getLogoutCommand()));
                    registrations.add(Window.addCloseHandler(new CloseHandler<Window>() {
                        @Override
                        public void onClose(CloseEvent<Window> pEvent) {
                            forceCloseAndReloadApp();
                        }
                    }));
                    connected = true;
                }
            }
        }
    }

    /**
     * Rebuild the application URL using GWT base URL, GWT module name and
     * current URL parameters
     * 
     * @return The rebuild URL
     */
    private static String rebuildApplicationURL() {
        String lURL =
                GWT.getModuleBaseURL().substring(
                        0,
                        GWT.getModuleBaseURL().length()
                                - GWT.getModuleName().length() - 1);
        lURL += "?";
        Map<String, List<String>> lParams = Window.Location.getParameterMap();
        for (String lKey : lParams.keySet()) {
            for (String lValue : lParams.get(lKey)) {
                lURL += lKey + "=" + lValue + "&";
            }
        }
        return lURL;
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.presenter.client.BasicPresenter#onUnbind()
     */
    @Override
    protected void onUnbind() {
        userSpace.unbind();
        adminSpace.unbind();
        for (HandlerRegistration lRegistration : registrations) {
            lRegistration.removeHandler();
        }
        registrations.clear();
        connected = false;
    }

    private void displayUserSpace() {
        // By default, the user space is displayed
        getDisplay().setContent(userSpace.getDisplay().asWidget());
    }

    public static String getSessionId() {
        return sessionId;
    }

}