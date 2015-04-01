/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.server.i18n;

import java.util.Collection;
import java.util.List;

import org.topcased.gpm.business.util.Translation;
import org.topcased.gpm.ui.facade.server.authorization.UiUserSession;

/**
 * gPM i18n facade.
 * <p>
 * Only one method.
 * </p>
 * 
 * @author mkargbo
 */
public interface I18nFacade {

    /**
     * Get available languages.
     * 
     * @return Available languages.
     */
    public List<String> getAvailableLanguages();

    /**
     * Get translation
     * 
     * @param pSession
     *            the session
     * @param pKey
     *            the key
     * @return the translated value
     */
    public Translation getTranslation(UiUserSession pSession, String pKey);

    /**
     * Get a translation manager
     * 
     * @param pLang
     *            the lang
     * @return the value translated
     */
    public I18nTranslationManager getTranslationManager(final String pLang);

    /**
     * Get translations
     * 
     * @param pSession
     *            the session
     * @param pKeys
     *            the keys list
     * @return the translated value list
     */
    public List<Translation> getTranslations(UiUserSession pSession,
            Collection<String> pKeys);

    /**
     * Get user's language.
     * 
     * @param pUserSession
     *            the user session
     * @return User's language if specified, default language otherwise.
     */
    public String getUserLanguage(final UiUserSession pUserSession);
}