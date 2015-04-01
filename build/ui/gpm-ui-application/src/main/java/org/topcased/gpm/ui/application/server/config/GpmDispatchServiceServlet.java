/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.server.config;

import javax.servlet.http.HttpSession;

import net.customware.gwt.dispatch.server.Dispatch;
import net.customware.gwt.dispatch.server.service.DispatchServiceServlet;
import net.customware.gwt.dispatch.shared.Action;
import net.customware.gwt.dispatch.shared.ActionException;
import net.customware.gwt.dispatch.shared.Result;

import org.apache.commons.lang.text.StrSubstitutor;
import org.springframework.orm.hibernate3.HibernateTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.topcased.gpm.business.exception.BusinessException;
import org.topcased.gpm.business.exception.ExtensionException;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.exception.InvalidTokenException;
import org.topcased.gpm.business.exception.MandatoryValuesException;
import org.topcased.gpm.business.exception.MandatoryValuesException.FieldRef;
import org.topcased.gpm.business.util.log.GPMLogger;
import org.topcased.gpm.ui.application.server.SessionAttributesEnum;
import org.topcased.gpm.ui.application.server.command.AbstractCommandActionHandler;
import org.topcased.gpm.ui.application.shared.exception.ExternalAuthenticationException;
import org.topcased.gpm.ui.facade.server.FacadeLocator;
import org.topcased.gpm.ui.facade.server.authorization.UiUserSession;
import org.topcased.gpm.ui.facade.server.i18n.I18nTranslationManager;
import org.topcased.gpm.ui.facade.shared.exception.UiBusinessException;
import org.topcased.gpm.ui.facade.shared.exception.UiException;
import org.topcased.gpm.ui.facade.shared.exception.UiExtensionException;
import org.topcased.gpm.ui.facade.shared.exception.UiSessionException;
import org.topcased.gpm.ui.facade.shared.exception.UiUnexpectedException;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * GpmDispatchServiceServlet
 * 
 * @author nveillet
 */
@Singleton
public class GpmDispatchServiceServlet extends DispatchServiceServlet {

    /** serialVersionUID */
    private static final long serialVersionUID = 3525101771981699981L;
    
    /** GPM Logger */
    protected GPMLogger gpmLogger = GPMLogger.getLogger(GpmDispatchServiceServlet.class);

    /**
     * Constructor
     * 
     * @param pDispatch
     *            the dispatch
     */
    @Inject
    public GpmDispatchServiceServlet(final Dispatch pDispatch) {
        super(pDispatch);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.service.DispatchServiceServlet#execute(net.customware.gwt.dispatch.shared.Action)
     */
    @Override
    public Result execute(Action<?> pAction) throws ActionException {
        // Start transaction
        final HibernateTransactionManager lTransactionManager =
                (HibernateTransactionManager) FacadeLocator.getContext().getBean(
                        "transactionManager");
        final TransactionStatus lTransactionStatus =
                lTransactionManager.getTransaction(new DefaultTransactionAttribute(
                        TransactionDefinition.PROPAGATION_REQUIRED));

        try {

        	Result lResult = super.execute(pAction);
        	
        	HttpSession lHttpSession = getThreadLocalRequest().getSession();
            UiUserSession lUserSession =
                    (UiUserSession) lHttpSession.getAttribute(SessionAttributesEnum.GPM_USER_SESSION_ATTR.name());
            if (lUserSession != null) {
                boolean lRefresh =
                        FacadeLocator.getNewInstance().getAuthorizationFacade().refreshSession(
                                lUserSession);
                if (!lRefresh) {
                    throw new InvalidTokenException("Session expired");
                }
            }

            // Commit
            lTransactionManager.commit(lTransactionStatus);

            return lResult;
        }

        //  Roll back on exception and convert exception 

        // Extensions points exceptions
        // The message can be specified in the extension point
        catch (ExtensionException e) {
        	gpmLogger.logException(e);
        	lTransactionManager.rollback(lTransactionStatus);
            if (e.getCause() == null) {
                throw new UiBusinessException(e.getUserMessage());
            }
            else if (e.getCause() instanceof GDMException) {
                I18nTranslationManager lTranslationManager =
                        getTranslationManager();

                StrSubstitutor lSubstitutor =
                        new StrSubstitutor(
                                lTranslationManager.getAllTextTranslations());
                String lI18nMsg =
                        lSubstitutor.replace(e.getCause().getMessage());

                throw new UiExtensionException(lI18nMsg);
            }
            else {
                throw new UiUnexpectedException(e.getCause());
            }
        }
        /* functional Exceptions = no technical information needed */
        // Mandatory fields exception
        catch (MandatoryValuesException e) {
            lTransactionManager.rollback(lTransactionStatus);
            I18nTranslationManager lTranslationManager =
                    getTranslationManager();
            String lMessage = e.getMessage() + "\n";
            String lNext = "";
            for (FieldRef lField : ((MandatoryValuesException) e).getFields()) {
                lMessage +=
                        lNext
                                + lTranslationManager.getTextTranslation(lField.getName());
                lNext = ", ";
            }
            gpmLogger.logException(e);
            throw new UiBusinessException(lMessage);
        }
        // other functional exceptions
        catch (BusinessException e) {
        	gpmLogger.logException(e);
			lTransactionManager.rollback(lTransactionStatus);
        	throw new UiBusinessException(e.getUserMessage());
        }

        // Session exceptions
        catch (InvalidTokenException e) {
        	gpmLogger.logException(e);
        	lTransactionManager.rollback(lTransactionStatus);
            throw new UiSessionException(e.getUserMessage());
        }
        catch (UiSessionException e) {
        	gpmLogger.logException(e);
        	lTransactionManager.rollback(lTransactionStatus);
            throw e;
        }

        // Business exceptions
        catch (GDMException e) {
        	gpmLogger.logException(e);
        	lTransactionManager.rollback(lTransactionStatus);
            throw new UiUnexpectedException(e.getUserMessage(), e);
        }
        // Facade exceptions
        catch (UiException e) {
        	gpmLogger.logException(e);
        	lTransactionManager.rollback(lTransactionStatus);
            throw e;
        }
        // Others Runtime exceptions
        catch (RuntimeException e) {
        	gpmLogger.logException(e);
        	lTransactionManager.rollback(lTransactionStatus);
            throw new UiUnexpectedException(e);
        }
        // Action Exceptions
        // External Authentication
        catch (ExternalAuthenticationException e) {
        	gpmLogger.logException(e);
        	lTransactionManager.rollback(lTransactionStatus);
            throw new UiBusinessException(e.getMessage());
        }
        // other action exceptions
        catch (ActionException e) {
        	gpmLogger.logException(e);
        	lTransactionManager.rollback(lTransactionStatus);
            throw new UiUnexpectedException(e);
        }
    }

    /**
     * get the translation manager
     * 
     * @return The translation manager
     */
    private I18nTranslationManager getTranslationManager() {
        UiUserSession lUserSession =
                AbstractCommandActionHandler.getUserSession(getThreadLocalRequest());
        I18nTranslationManager lTranslationManager =
                FacadeLocator.instance().getI18nFacade().getTranslationManager(
                        lUserSession.getLanguage());
        return lTranslationManager;
    }
}