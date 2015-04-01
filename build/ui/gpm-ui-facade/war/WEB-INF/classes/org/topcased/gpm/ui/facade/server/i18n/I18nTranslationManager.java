/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.server.i18n;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.topcased.gpm.business.i18n.service.I18nService;

/**
 * I18nTranslationManager
 * 
 * @author nveillet
 */
public class I18nTranslationManager {

    private boolean allTranslationLoaded;

    private final I18nService i18nService;

    private final Map<String, String> imageTranslations;

    private final String lang;

    private final Map<String, String> textTranslations;

    /**
     * Constructor
     * 
     * @param pLang
     *            the lang
     * @param pI18nService
     *            the internationalization service
     */
    public I18nTranslationManager(String pLang, I18nService pI18nService) {
        lang = pLang;
        textTranslations = new HashMap<String, String>();
        imageTranslations = new HashMap<String, String>();
        i18nService = pI18nService;
        allTranslationLoaded = false;
    }

    /**
     * Get all images translations
     * 
     * @return the translations
     */
    public Map<String, String> getAllImageTranslations() {
        if (!allTranslationLoaded) {
            loadAllTranslations();
        }
        return imageTranslations;
    }

    /**
     * Get all text translations
     * 
     * @return the translations
     */
    public Map<String, String> getAllTextTranslations() {
        if (!allTranslationLoaded) {
            loadAllTranslations();
        }
        return textTranslations;
    }

    /**
     * Get a image translation
     * 
     * @param pKey
     *            the key to translate
     * @return the value translated
     */
    public String getImageTranslation(String pKey) {
        String lValue = null;
        if (pKey != null) {
            if (!imageTranslations.containsKey(pKey)) {
                lValue =
                        i18nService.getTypedValue(pKey, lang,
                                I18nService.TYPE_IMAGE);
                imageTranslations.put(pKey, lValue);
            }
            else {
                lValue = imageTranslations.get(pKey);
            }
        }
        return lValue;
    }

    /**
     * get lang
     * 
     * @return the lang
     */
    public String getLang() {
        return lang;
    }

    /**
     * Get a text translation
     * 
     * @param pKey
     *            the key to translate
     * @return the value translated
     */
    public String getTextTranslation(String pKey) {
        String lValue = null;
        if (pKey != null) {
            if (!textTranslations.containsKey(pKey)) {
                lValue = i18nService.getValue(pKey, lang);
                textTranslations.put(pKey, lValue);
            }
            else {
                lValue = textTranslations.get(pKey);
            }
        }
        return lValue;
    }

    /**
     * Load all translations
     */
    private void loadAllTranslations() {
        List<Map<String, String>> lAllTranslations = i18nService.getValue(lang);
        textTranslations.putAll(lAllTranslations.get(0));
        imageTranslations.putAll(lAllTranslations.get(1));

        allTranslationLoaded = true;
    }
}
