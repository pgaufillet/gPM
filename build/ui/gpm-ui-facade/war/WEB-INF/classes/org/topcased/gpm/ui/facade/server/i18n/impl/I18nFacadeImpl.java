/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.server.i18n.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.topcased.gpm.business.util.Translation;
import org.topcased.gpm.ui.facade.server.AbstractFacade;
import org.topcased.gpm.ui.facade.server.authorization.UiUserSession;
import org.topcased.gpm.ui.facade.server.i18n.I18nFacade;
import org.topcased.gpm.ui.facade.server.i18n.I18nTranslationManager;

/**
 * gPM i18n facade.
 * <p>
 * Only one method.
 * </p>
 * 
 * @author mkargbo
 */
public class I18nFacadeImpl extends AbstractFacade implements I18nFacade {

    /** The default language. */
    public static final String DEFAULT_LANGUAGE = "en";

    private final static Map<String, I18nTranslationManager> TRANSLATION_MANAGERS;

    static {
        TRANSLATION_MANAGERS = new HashMap<String, I18nTranslationManager>();
    }

    /**
     * Get available languages.
     * 
     * @return Available languages.
     */
    public List<String> getAvailableLanguages() {
        List<String> lResultList = new ArrayList<String>();
        String[] lAvailableLanguages = getI18nService().getAvailableLanguages();
        for (int i = 0; i < lAvailableLanguages.length; i++) {
            lResultList.add(lAvailableLanguages[i]);
        }
        return lResultList;
    }

    /**
     * Get translation
     * 
     * @param pSession
     *            the session
     * @param pKey
     *            the key
     * @return the translated value
     */
    public Translation getTranslation(UiUserSession pSession, String pKey) {
        I18nTranslationManager lTranslationManager =
                getTranslationManager(pSession.getLanguage());

        return new Translation(pKey,
                lTranslationManager.getTextTranslation(pKey));

    }

    /**
     * Get a translation manager
     * 
     * @param pLang
     *            the lang
     * @return the value translated
     */
    public I18nTranslationManager getTranslationManager(final String pLang) {

        I18nTranslationManager lTranslationManager =
                TRANSLATION_MANAGERS.get(pLang);

        if (lTranslationManager == null) {
            lTranslationManager =
                    new I18nTranslationManager(pLang, getI18nService());
            TRANSLATION_MANAGERS.put(pLang, lTranslationManager);
        }

        return lTranslationManager;
    }

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
            Collection<String> pKeys) {
        I18nTranslationManager lTranslationManager =
                getTranslationManager(pSession.getLanguage());

        List<Translation> lValues = new ArrayList<Translation>();
        for (String lKey : pKeys) {
            lValues.add(new Translation(lKey,
                    lTranslationManager.getTextTranslation(lKey)));
        }
        return lValues;
    }

    /**
     * Get user's language.
     * 
     * @param pUserSession
     *            the user session
     * @return User's language if specified, default language otherwise.
     */
    public String getUserLanguage(final UiUserSession pUserSession) {
        return getI18nService().getPreferredLanguage(pUserSession.getToken());
    }
}