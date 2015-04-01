/***************************************************************
 * Copyright (c) 2011 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien Eballard (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.topcased.gpm.ui.application.server.SessionAttributesEnum;
import org.topcased.gpm.ui.application.shared.exception.ExternalAuthenticationException;
import org.topcased.gpm.ui.facade.server.FacadeLocator;
import org.topcased.gpm.ui.facade.server.attribute.AttributeFacade;
import org.topcased.gpm.ui.facade.server.authorization.AuthorizationFacade;
import org.topcased.gpm.ui.facade.server.authorization.UiUserSession;
import org.topcased.gpm.ui.facade.shared.exception.UiSessionException;

/**
 * This servlet is the entry point of user requests for the application. When in
 * External Authentication, it logs the user in and redirects him directly to
 * its language GWT permutation by adding the language parameter in redirect
 * URL.
 * 
 * @author jeballar
 */
public class RedirectServlet extends HttpServlet {

    /** serialVersionUID */
    private static final long serialVersionUID = -1339710688919926692L;

    private static final String INDEX_PAGE = "index.html";

    private static final String LOCALE_PARAM = "locale";

    private Map<String, String[]> requestArguments =
            new HashMap<String, String[]>();

    private HttpServletRequest request = null;

    private String userIdParamName;

    /**
     * {@inheritDoc}
     * 
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doPost(HttpServletRequest pReq, HttpServletResponse pResp)
        throws ServletException, IOException {
        doGet(pReq, pResp);
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    public void doGet(HttpServletRequest pRequest, HttpServletResponse pResponse)
        throws IOException, ServletException {
        if (!pRequest.getRequestURI().endsWith("/gpm/index.html")) {
            String lUrl = pRequest.getContextPath() + "/gpm/index.html";
            String lParameters = "";
            if (!pRequest.getParameterMap().isEmpty()) {
                lParameters = "?";
            }
            for (Object lObject : pRequest.getParameterMap().keySet()) {
                String lKey = (String) lObject;
                String lValue = pRequest.getParameter(lKey);
                lParameters += lKey + "=" + lValue + "&";
            }
            if (!pRequest.getParameterMap().isEmpty()) {
                lParameters = lParameters.substring(0, lParameters.length() - 1);
            }
            pResponse.sendRedirect(lUrl + lParameters);
            return;
        }
        ClassPathResource lPath = new ClassPathResource(INDEX_PAGE);
        InputStream lIn = lPath.getInputStream();
        ServletOutputStream lOut = pResponse.getOutputStream();

        StreamConnector lPipe = new StreamConnector(lIn, lOut);
        pResponse.setContentType("text/html");

        lPipe.pipe();

        request = pRequest;
        buildGETParametersFromRequest();

        UiUserSession lSession = null;

        // Build redirect string
        StringBuilder lRedirectBuilder =
                new StringBuilder(pRequest.getRequestURL());
        lRedirectBuilder.append(INDEX_PAGE);

        try {
            lSession = attemptLogin();
            if (lSession != null) {
                getHttpSession().setAttribute(
                        SessionAttributesEnum.GPM_USER_SESSION_ATTR.name(),
                        lSession);
            }
        }
        catch (ExternalAuthenticationException lException) {
            // Let it redirect
        }

        if (lSession != null) {
            setParameter(LOCALE_PARAM, lSession.getLanguage());
        }
    }

    /**
     * Get all GET parameters in URL and set them in the map
     */
    @SuppressWarnings("unchecked")
    private void buildGETParametersFromRequest() {
        requestArguments =
                new HashMap<String, String[]>(request.getParameterMap());
    }

    /**
     * Set the value of a parameter in the Map. The request must not have been
     * built yet
     * 
     * @param pParamName
     *            the parameter name
     * @param pValue
     *            the desired value
     */
    private void setParameter(String pParamName, String pValue) {
        requestArguments.put(pParamName, new String[] { pValue });
    }

    /**
     * Get the first GET param value for the parameter name in argument
     * 
     * @param pName
     *            the parameter name
     * @return the value or <code>null</code> if not found
     */
    @SuppressWarnings("unchecked")
    public String getRequestParameter(String pName) {
        if (request != null) {
            Map<String, String[]> lMap =
                    (Map<String, String[]>) request.getParameterMap();
            if (request.getParameterMap() != null) {
                String[] lValues = lMap.get(pName);
                if (lValues != null && lValues.length > 0) {
                    return lValues[0];
                }
            }
        }
        return null;
    }

    /**
     * Try to login the user to the core
     * 
     * @return if the user is logged
     * @throws ExternalAuthenticationException
     */
    private UiUserSession attemptLogin() throws ExternalAuthenticationException {
        final AuthorizationFacade lAuthorizationFacade =
                getFacadeLocator().getAuthorizationFacade();

        // Get existing user session
        UiUserSession lSession = null;
        try {
            lSession = getUserSession();
        }
        catch (UiSessionException e) {
            // Do nothing because we want login
        }

        // If no session opened :
        if (lSession == null) {
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
                    userIdParamName = lAuth.get(1);
                    lHttpSession.setAttribute(
                            SessionAttributesEnum.AUTHENTICATION_SYSTEM_TYPE.name(),
                            SessionAttributesEnum.EXTERNAL);
                    // Get USER-ID from HTTP Header
                    String lLogin = request.getHeader(userIdParamName);
                    if (StringUtils.isBlank(lLogin)) { // Else get USER-ID from GET parameters
                        lLogin = getRequestParameter(userIdParamName);
                    }

                    lHttpSession.setAttribute(
                            SessionAttributesEnum.AUTHENTICATION_SYSTEM_USER_ID.name(),
                            lLogin);
                    lAuthSystem = SessionAttributesEnum.EXTERNAL;
                }
            }

            switch (lAuthSystem) {
                case EXTERNAL:
                    // Get USER-ID from HTTP Header
                    final String lLogin =
                            (String) getHttpSession().getAttribute(
                                    SessionAttributesEnum.AUTHENTICATION_SYSTEM_USER_ID.name());
                    if (StringUtils.isBlank(lLogin)) {
                        // If no login, clear all session infos
                        clearSession();
                        throw new ExternalAuthenticationException(
                                ExternalAuthenticationException.MISSING_USER_ID);
                    }
                    else {
                        lUserSession = lAuthorizationFacade.login(lLogin, "");
                        lUserSession.setApplicationUrl(request.getRequestURL().toString());
                    }
                    break;
                default:
                    lUserSession = null;
            }
            return lUserSession;
        }
        return lSession;
    }

    private void clearSession() {
        HttpSession lHttpSession = getHttpSession();
        lHttpSession.removeAttribute(SessionAttributesEnum.GPM_USER_SESSION_ATTR.name());
        lHttpSession.removeAttribute(SessionAttributesEnum.AUTHENTICATION_SYSTEM_TYPE.name());
        lHttpSession.removeAttribute(SessionAttributesEnum.AUTHENTICATION_SYSTEM_USER_ID.name());
    }

    /**
     * Get HTTP Session
     * 
     * @return HTTP Session
     */
    private HttpSession getHttpSession() {
        return request.getSession();
    }

    /**
     * Get User Session
     * 
     * @return User Session
     */
    private UiUserSession getUserSession() {
        HttpSession lHttpSession = getHttpSession();
        String lUserSessionAttr =
                SessionAttributesEnum.GPM_USER_SESSION_ATTR.name();
        UiUserSession lUserSession =
                (UiUserSession) lHttpSession.getAttribute(lUserSessionAttr);
        if (lUserSession == null) {
            throw new UiSessionException();
        }
        return lUserSession;
    }

    /**
     * Get a FacadeLocator
     * 
     * @return a FacadeLocator
     */
    private FacadeLocator getFacadeLocator() {
        return FacadeLocator.instance();
    }

    /**
     * Connect directly to streams, read from input and write in output as long
     * as possible.
     */
    private final class StreamConnector {

        final static private int BUF_LENGTH = 1024;

        final private InputStream in;

        final private OutputStream out;

        private StreamConnector(InputStream pIn, OutputStream pOut) {
            in = pIn;
            out = pOut;
        }

        public void pipe() {
            byte[] lBuf = new byte[BUF_LENGTH];
            int lRead = 0;
            try {
                while ((lRead = in.read(lBuf)) != -1) {
                    out.write(lBuf, 0, lRead);
                }
            }
            catch (IOException lException) {
                lException.printStackTrace();

            }
        }
    }
}