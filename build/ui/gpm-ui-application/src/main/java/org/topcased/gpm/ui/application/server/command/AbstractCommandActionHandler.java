/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.server.command;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.Action;
import net.customware.gwt.dispatch.shared.ActionException;
import net.customware.gwt.dispatch.shared.Result;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.util.FieldsContainerType;
import org.topcased.gpm.business.util.FilterType;
import org.topcased.gpm.ui.application.server.SessionAttributesEnum;
import org.topcased.gpm.ui.application.shared.command.authorization.ConnectResult;
import org.topcased.gpm.ui.application.shared.command.authorization.GetConnectionInformationAction;
import org.topcased.gpm.ui.facade.server.FacadeLocator;
import org.topcased.gpm.ui.facade.server.authorization.AuthorizationFacade;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.authorization.UiUserSession;
import org.topcased.gpm.ui.facade.shared.action.ActionType;
import org.topcased.gpm.ui.facade.shared.action.UiAction;
import org.topcased.gpm.ui.facade.shared.container.field.UiField;
import org.topcased.gpm.ui.facade.shared.container.field.multiple.UiMultipleField;
import org.topcased.gpm.ui.facade.shared.container.field.multivalued.UiMultivaluedField;
import org.topcased.gpm.ui.facade.shared.container.field.simple.UiAttachedField;
import org.topcased.gpm.ui.facade.shared.exception.UiSessionException;

import com.google.inject.Provider;

/**
 * <p>
 * Abstract super-class for {@link ActionHandler} implementations that force the
 * {@link Provider<HttpSession>} object to be passed in as a constructor to the
 * handler.
 * </p>
 * <p>
 * Sub-class must annotate their constructor with {@linkplain @Inject}
 * annotation.
 * </p>
 * 
 * @param <A>
 *            The {@link Action} implementation.
 * @param <R>
 *            The {@link Result} implementation.
 * @author mkargbo
 */
public abstract class AbstractCommandActionHandler<A extends Action<R>, R extends Result>
        implements ActionHandler<A, R> {

    /**
     * get the user session from the a request.
     * 
     * @param pRequest
     *            Request containing the HTTP Session
     * @return the user session
     * @throws UiSessionException
     *             if user session not exist in http session
     */
    public static UiUserSession getUserSession(final HttpServletRequest pRequest) {
        HttpSession lHttpSession = pRequest.getSession();
        UiUserSession lUserSession =
                (UiUserSession) lHttpSession.getAttribute(SessionAttributesEnum.GPM_USER_SESSION_ATTR.name());

        if (lUserSession == null) {
            throw new UiSessionException();
        }

        return lUserSession;
    }

    /**
     * Get user's session associated to this 'productName' from the a request.
     * If the product name parameter is 'empty', this method return the default
     * global session
     * 
     * @param pRequest
     *            Request containing the HTTP Session
     * @param pProductName
     *            Product's name corresponding to the session
     * @return Session associated to this 'productName', null if no session
     *         found or the user is not logged.
     */
    public static UiSession getSession(final HttpServletRequest pRequest,
            final String pProductName) {
        final UiSession lSession;
        HttpSession lHttpSession = pRequest.getSession();
        UiUserSession lUiUserSession =
                (UiUserSession) lHttpSession.getAttribute(SessionAttributesEnum.GPM_USER_SESSION_ATTR.name());
        if (lUiUserSession == null) {
            lSession = null;
        }
        else if (StringUtils.isNotEmpty(pProductName)) {
            lSession = lUiUserSession.getSession(pProductName);
        }
        else {
            lSession = lUiUserSession.getDefaultGlobalSession();
        }
        return lSession;
    }

    protected Provider<HttpSession> httpSession;

    /**
     * Action handler constructor that initialize the httpSession object.
     * 
     * @param pHttpSession
     *            Http session
     */
    public AbstractCommandActionHandler(Provider<HttpSession> pHttpSession) {
        httpSession = pHttpSession;
    }

    /**
     * Clear the HTTPSession. <h4>Attributes removed</h4>
     * <ul>
     * <li>User session</li>
     * <li>authentication system type (external or internal)</li>
     * <li>external authentication user identifier</li>
     * </ul>
     */
    protected void clearSession() {
        HttpSession lHttpSession = getHttpSession();
        lHttpSession.removeAttribute(SessionAttributesEnum.GPM_USER_SESSION_ATTR.name());
        lHttpSession.removeAttribute(SessionAttributesEnum.AUTHENTICATION_SYSTEM_TYPE.name());
        lHttpSession.removeAttribute(SessionAttributesEnum.AUTHENTICATION_SYSTEM_USER_ID.name());
    }

    /**
     * Create a default session associated to this 'productName'
     * 
     * @param pProductName
     *            Product's name corresponding to the default session
     * @return Default session associated to this 'productName'.
     */
    protected UiSession connectDefaultSession(final String pProductName) {
        AuthorizationFacade lAuthorizationFacade =
                getFacadeLocator().getAuthorizationFacade();

        UiSession lSession =
                lAuthorizationFacade.connect(getUserSession(), pProductName,
                        lAuthorizationFacade.getDefaultRole(getUserSession(),
                                pProductName));
        getUserSession().addDefaultSession(pProductName, lSession);

        return lSession;
    }

    /**
     * Create a default session associated to this 'productId'
     * 
     * @param pProductId
     *            Product's identifier corresponding to the default session
     * @return Default session associated to this 'productId'.
     */
    protected UiSession connectDefaultSessionByProductId(final String pProductId) {
        return connectDefaultSession(getFacadeLocator().getProductFacade().getProductName(
                getDefaultSession(), pProductId));
    }

    /**
     * Get the ConnectResult if user don't already connected to the sheet
     * product
     * 
     * @param pCurrentProductName
     *            the current product name
     * @param pSheetProductName
     *            the sheet
     * @param pContext
     *            the execution context
     * @return the connect result or null if the current and sheet's product are
     *         not the same.
     * @throws ActionException
     *             the ActionException
     */
    protected ConnectResult getConnectResult(String pCurrentProductName,
            String pSheetProductName, ExecutionContext pContext)
        throws ActionException {
        // Get the product connection if the sheet is on another product
        ConnectResult lConnectResult = null;
        if (!pCurrentProductName.equals(pSheetProductName)
                && getSession(pSheetProductName) == null) {
            lConnectResult =
                    pContext.execute(new GetConnectionInformationAction(
                            pSheetProductName));
        }

        return lConnectResult;
    }

    /**
     * Get the creatable fields containers
     * 
     * @param pSession
     *            the session
     * @param pFieldsContainerType
     *            the container class
     * @param pFieldsContainerId
     *            For links, the fields container identifier
     * @return the creatable fields containers
     */
    protected List<UiAction> getCreatableFieldsContainers(UiSession pSession,
            FieldsContainerType pFieldsContainerType, String pFieldsContainerId) {

        List<String> lTypeNames = null;
        switch (pFieldsContainerType) {
            case SHEET:
                lTypeNames =
                        getFacadeLocator().getSheetFacade().getCreatableSheetTypes(
                                pSession);
                break;
            case PRODUCT:
                lTypeNames =
                        getFacadeLocator().getProductFacade().getCreatableProductTypes(
                                pSession);
                break;
            case LINK:
            default:
                lTypeNames =
                        getFacadeLocator().getLinkFacade().getCreatableLinkTypes(
                                pSession, pFieldsContainerId);
                break;
        }

        List<UiAction> lSubActions = new ArrayList<UiAction>();
        for (String lTypeName : lTypeNames) {
            lSubActions.add(new UiAction(lTypeName, ActionType.DYNAMIC));
        }

        return lSubActions;
    }

    /**
     * Get user's global default session.
     * 
     * @return Global default session associated, null if no session created.
     */
    protected UiSession getDefaultSession() {
        UiUserSession lUser = getUserSession();
        return lUser.getDefaultGlobalSession();
    }

    /**
     * Get user's default session associated to this 'productName'.
     * 
     * @param pProductName
     *            Product's name corresponding to the default session
     * @return Default session associated to this 'productName', null if no
     *         session found.
     */
    protected UiSession getDefaultSession(final String pProductName) {
        return getUserSession().getDefaultSession(pProductName);
    }

    /**
     * Get user's default session associated to this 'productId'.
     * 
     * @param pProductId
     *            Product's identifier corresponding to the default session
     * @return Default session associated to this 'productId', null if no
     *         session found.
     */
    protected UiSession getDefaultSessionByProductId(final String pProductId) {
        UiUserSession lUserSession = getUserSession();
        return lUserSession.getDefaultSession(getFacadeLocator().getProductFacade().getProductName(
                lUserSession.getDefaultGlobalSession(), pProductId));
    }

    /**
     * get the facade locator
     * 
     * @return the facade locator
     */
    protected FacadeLocator getFacadeLocator() {
        return FacadeLocator.instance();
    }

    protected HttpSession getHttpSession() {
        return httpSession.get();
    }

    /**
     * Get user's session associated to this 'productName'.
     * 
     * @param pProductName
     *            Product's name corresponding to the session
     * @return Session associated to this 'productName', null if no session
     *         found.
     */
    protected UiSession getSession(final String pProductName) {
        UiUserSession lUser = getUserSession();
        return lUser.getSession(pProductName);
    }

    /**
     * Get user's session associated to this product.
     * 
     * @param pProductName
     *            Product's name corresponding to the session
     * @param pValuesContainerId
     *            Current ValuesContainer identifier
     * @param pFieldsContainerType
     *            the fields container type determine the session type
     * @return Session associated to this 'productName', null if no session
     *         found.
     */
    protected UiSession getSession(String pProductName,
            String pValuesContainerId, FieldsContainerType pFieldsContainerType) {
        if (FieldsContainerType.PRODUCT.equals(pFieldsContainerType)) {
            if (pProductName != null) {
                return getDefaultSession(pProductName);
            }
            else {
                return getDefaultSessionByProductId(pValuesContainerId);
            }
        }
        else {
            return getSession(pProductName);
        }
    }

    /**
     * Get user's session associated to this 'productName'.
     * 
     * @param pProductName
     *            Product's name corresponding to the session
     * @param pFilterType
     *            the filter type determine the session type
     * @return Session associated to this 'productName', null if no session
     *         found.
     */
    protected UiSession getSession(final String pProductName,
            final FilterType pFilterType) {
        if (FilterType.PRODUCT.equals(pFilterType)) {
            return getDefaultSession();
        }
        else {
            return getSession(pProductName);
        }
    }

    /**
     * get the user session in http session
     * 
     * @return
     * @throws UiSessionException
     *             if user session not exist in http session
     */
    protected UiUserSession getUserSession() {
        UiUserSession lUserSession =
                (UiUserSession) getHttpSession().getAttribute(
                        SessionAttributesEnum.GPM_USER_SESSION_ATTR.name());

        if (lUserSession == null) {
            throw new UiSessionException();
        }

        return lUserSession;
    }

    protected boolean isUserAuthenticated() {
        return getUserSession() != null;
    }

    /**
     * Add file content.
     * 
     * @param pUserSession
     *            The user session.
     * @param pFields
     *            The fields without file content.
     */
    protected void addFileContent(final UiUserSession pUserSession,
            final List<UiField> pFields) {
        if (pFields != null) {
            for (UiField lField : pFields) {
                addFileContent(pUserSession, lField);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void addFileContent(final UiUserSession pUserSession,
            final UiField pField) {
        if (pField != null) {
            if (pField instanceof UiAttachedField) {
                final UiAttachedField lAttachedField = (UiAttachedField) pField;
                final String lFileName = lAttachedField.getFileName();

                if (lFileName != null && !lFileName.trim().isEmpty()) {
                    lAttachedField.setContent(pUserSession.getTemporaryUploadedFile(lFileName));

                    // Remove file path of the file name
                    int lIndex =
                            Math.max(lFileName.lastIndexOf("/"),
                                    lFileName.lastIndexOf("\\"));
                    if (lIndex >= 0) {
                        lAttachedField.setFileName(lFileName.substring(
                                lIndex + 1, lFileName.length()));
                    }
                }
            }
            else if (pField instanceof UiMultipleField
                    || pField instanceof UiMultivaluedField) {
                for (UiField lSubField : ((Iterable<UiField>) pField)) {
                    addFileContent(pUserSession, lSubField);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#rollback(net.customware.gwt.dispatch.shared.Action,
     *      net.customware.gwt.dispatch.shared.Result,
     *      net.customware.gwt.dispatch.server.ExecutionContext)
     */
    @Override
    public void rollback(A pAction, R pResult, ExecutionContext pContext)
        throws ActionException {
        // Nothing todo
    }
}