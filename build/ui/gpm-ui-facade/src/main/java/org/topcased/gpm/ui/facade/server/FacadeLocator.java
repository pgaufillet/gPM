/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.server;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.topcased.gpm.ui.facade.server.attribute.AttributeFacade;
import org.topcased.gpm.ui.facade.server.authorization.AuthorizationFacade;
import org.topcased.gpm.ui.facade.server.dictionary.DictionaryFacade;
import org.topcased.gpm.ui.facade.server.exportimport.ExportImportFacade;
import org.topcased.gpm.ui.facade.server.extendedaction.ExtendedActionFacade;
import org.topcased.gpm.ui.facade.server.filter.FilterFacade;
import org.topcased.gpm.ui.facade.server.i18n.I18nFacade;
import org.topcased.gpm.ui.facade.server.link.LinkFacade;
import org.topcased.gpm.ui.facade.server.mail.MailFacade;
import org.topcased.gpm.ui.facade.server.product.ProductFacade;
import org.topcased.gpm.ui.facade.server.sheet.SheetFacade;
import org.topcased.gpm.ui.facade.server.user.UserFacade;

/**
 * FacadeLocator
 * 
 * @author nveillet
 */
public final class FacadeLocator {
    private static ApplicationContext staticContext;

    private static FacadeLocator staticFacadeLocator;

    public static synchronized ApplicationContext getContext() {
        if (staticContext == null) {
            staticContext =
                    new ClassPathXmlApplicationContext("facadeContext.xml");
        }
        return staticContext;
    }

    /**
     * Create a new facade locator
     * 
     * @return the newly created facade locator instance.
     */
    public static synchronized FacadeLocator getNewInstance() {
        staticFacadeLocator =
                (FacadeLocator) getContext().getBean("facadeLocator");
        return staticFacadeLocator;
    }

    /**
     * Get the shared instance (singleton) of the facade locator
     * 
     * @return the shared facade locator instance.
     */
    public static synchronized FacadeLocator instance() {
        if (null != staticFacadeLocator) {
            return staticFacadeLocator;
        }
        return getNewInstance();
    }

    public static void setContext(ApplicationContext pContext) {
        staticContext = pContext;
    }

    private AttributeFacade attributeFacade;

    private AuthorizationFacade authorizationFacade;

    private DictionaryFacade dictionaryFacade;

    private ExportImportFacade exportImportFacade;

    private ExtendedActionFacade extendedActionFacade;

    private FilterFacade filterFacade;

    private I18nFacade i18nFacade;

    private LinkFacade linkFacade;

    private MailFacade mailFacade;

    private ProductFacade productFacade;

    private SheetFacade sheetFacade;

    private UserFacade userFacade;

    /**
     * private constructor : singleton
     */
    private FacadeLocator() {
    }

    /**
     * get attributeFacade
     * 
     * @return the attributeFacade
     */
    public AttributeFacade getAttributeFacade() {
        return attributeFacade;
    }

    /**
     * get authorizationFacade
     * 
     * @return the authorizationFacade
     */
    public AuthorizationFacade getAuthorizationFacade() {
        return authorizationFacade;
    }

    /**
     * get dictionaryFacade
     * 
     * @return the dictionaryFacade
     */
    public DictionaryFacade getDictionaryFacade() {
        return dictionaryFacade;
    }

    /**
     * get exportImportFacade
     * 
     * @return the exportImportFacade
     */
    public ExportImportFacade getExportImportFacade() {
        return exportImportFacade;
    }

    /**
     * get extendedActionFacade
     * 
     * @return the extendedActionFacade
     */
    public ExtendedActionFacade getExtendedActionFacade() {
        return extendedActionFacade;
    }

    /**
     * get filterFacade
     * 
     * @return the filterFacade
     */
    public FilterFacade getFilterFacade() {
        return filterFacade;
    }

    public I18nFacade getI18nFacade() {
        return i18nFacade;
    }

    /**
     * get linkFacade
     * 
     * @return the linkFacade
     */
    public LinkFacade getLinkFacade() {
        return linkFacade;
    }

    /**
     * get mailFacade
     * 
     * @return the mailFacade
     */
    public MailFacade getMailFacade() {
        return mailFacade;
    }

    /**
     * get productFacade
     * 
     * @return the productFacade
     */
    public ProductFacade getProductFacade() {
        return productFacade;
    }

    /**
     * get sheetFacade
     * 
     * @return the sheetFacade
     */
    public SheetFacade getSheetFacade() {
        return sheetFacade;
    }

    /**
     * get userFacade
     * 
     * @return the userFacade
     */
    public UserFacade getUserFacade() {
        return userFacade;
    }

    /**
     * set attributeFacade
     * 
     * @param pAttributeFacade
     *            the attributeFacade to set
     */
    public void setAttributeFacade(AttributeFacade pAttributeFacade) {
        attributeFacade = pAttributeFacade;
    }

    /**
     * set authorizationFacade
     * 
     * @param pAuthorizationFacade
     *            the authorizationFacade to set
     */
    public void setAuthorizationFacade(AuthorizationFacade pAuthorizationFacade) {
        authorizationFacade = pAuthorizationFacade;
    }

    /**
     * set dictionaryFacade
     * 
     * @param pDictionaryFacade
     *            the dictionaryFacade to set
     */
    public void setDictionaryFacade(DictionaryFacade pDictionaryFacade) {
        dictionaryFacade = pDictionaryFacade;
    }

    /**
     * set exportImportFacade
     * 
     * @param pExportImportFacade
     *            the exportImportFacade to set
     */
    public void setExportImportFacade(ExportImportFacade pExportImportFacade) {
        exportImportFacade = pExportImportFacade;
    }

    /**
     * set extendedActionFacade
     * 
     * @param pExtendedActionFacade
     *            the extendedActionFacade to set
     */
    public void setExtendedActionFacade(
            ExtendedActionFacade pExtendedActionFacade) {
        extendedActionFacade = pExtendedActionFacade;
    }

    /**
     * set filterFacade
     * 
     * @param pFilterFacade
     *            the filterFacade to set
     */
    public void setFilterFacade(FilterFacade pFilterFacade) {
        filterFacade = pFilterFacade;
    }

    public void setI18nFacade(I18nFacade pI18nFacade) {
        i18nFacade = pI18nFacade;
    }

    /**
     * set linkFacade
     * 
     * @param pLinkFacade
     *            the linkFacade to set
     */
    public void setLinkFacade(LinkFacade pLinkFacade) {
        linkFacade = pLinkFacade;
    }

    /**
     * set mailFacade
     * 
     * @param pMailFacade
     *            the mailFacade to set
     */
    public void setMailFacade(MailFacade pMailFacade) {
        mailFacade = pMailFacade;
    }

    /**
     * set productFacade
     * 
     * @param pProductFacade
     *            the productFacade to set
     */
    public void setProductFacade(ProductFacade pProductFacade) {
        productFacade = pProductFacade;
    }

    /**
     * set sheetFacade
     * 
     * @param pSheetFacade
     *            the sheetFacade to set
     */
    public void setSheetFacade(SheetFacade pSheetFacade) {
        sheetFacade = pSheetFacade;
    }

    /**
     * set userFacade
     * 
     * @param pUserFacade
     *            the userFacade to set
     */
    public void setUserFacade(UserFacade pUserFacade) {
        userFacade = pUserFacade;
    }
}
