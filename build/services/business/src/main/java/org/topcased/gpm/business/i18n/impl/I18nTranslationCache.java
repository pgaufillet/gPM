/***************************************************************
 * Copyright (c) 2011 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien Eballard (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.i18n.impl;

import java.util.HashMap;
import java.util.Map;

/**
 * I18nTranslationCache, used to store All translations by language, and type
 * 
 * @author jeballar
 */
public class I18nTranslationCache {
    private static I18nTranslationCache staticInstance =
            new I18nTranslationCache();

    /** Map<Language, Map<Type, Map<Label, Translated Value>>> */
    private final Map<String, Map<String, Map<String, String>>> cache;

    private I18nTranslationCache() {
        cache = new HashMap<String, Map<String, Map<String, String>>>();
    }

    /**
     * Return the corresponding translation when typed
     * 
     * @param pLanguage
     *            The language
     * @param pLabelKey
     *            The searched translation
     * @return The corresponding translation when typed
     */
    public String getTranslation(String pLanguage, String pLabelKey) {
        return getTranslation(pLanguage, pLabelKey, null);
    }

    /**
     * Return the corresponding translation when typed
     * 
     * @param pLanguage
     *            The language
     * @param pLabelKey
     *            The searched translation
     * @param pType
     *            The type of element to translate (can be null)
     * @return The translation or null if not found
     */
    public String getTranslation(String pLanguage, String pLabelKey,
            String pType) {
        synchronized (cache) {
            return getTranslationsMap(pLanguage, pType).get(pLabelKey);
        }
    }

    /**
     * Set a translation value
     * 
     * @param pLang
     *            The language
     * @param pLabelKey
     *            The searched translation
     * @param pType
     *            The type of element to translate (can be null)
     * @param pTranslatedValue
     *            The translated value
     */
    public void setTranslation(String pLang, String pLabelKey, String pType,
            String pTranslatedValue) {
        synchronized (cache) {
            // Put new value
            getTranslationsMap(pLang, pType).put(pLabelKey, pTranslatedValue);
        }
    }

    /**
     * Get the map corresponding to the language and type in parameters
     * 
     * @param pLang
     *            the language
     * @param pType
     *            the type
     * @return the map containing the translations for the language and type in
     *         parameters
     */
    private Map<String, String> getTranslationsMap(String pLang, String pType) {
        Map<String, Map<String, String>> lLanguageCache = cache.get(pLang);

        // Get language Map
        if (lLanguageCache == null) { // Create the language Map if absent
            lLanguageCache = new HashMap<String, Map<String, String>>();
            cache.put(pLang, lLanguageCache);
        }

        // Get language typed Map
        Map<String, String> lTypeCache = lLanguageCache.get(pType);
        if (lTypeCache == null) { // Create the type Map if absent
            lTypeCache = new HashMap<String, String>();
            lLanguageCache.put(pType, lTypeCache);
        }

        // The empty case for Type Map should not happen, because whole translations of a language
        // should be loaded, with every wanted types

        return lTypeCache;
    }

    /**
     * Add new translations
     * 
     * @param pLang
     *            the language to affect
     * @param pTranslations
     *            the association LabelKey/TranslatedValue Map
     * @param pType
     *            the type of the translations
     */
    public void setTranslations(String pLang,
            Map<String, String> pTranslations, String pType) {
        synchronized (cache) {

            Map<String, String> lMap = getTranslationsMap(pLang, pType);

            for (String lKey : pTranslations.keySet()) { // Build keys and insert values
                lMap.put(lKey, pTranslations.get(lKey));
            }
        }
    }

    /**
     * Get the static instance of I18nTranslationCache
     * 
     * @return The static instance of I18nTranslationCache
     */
    public static I18nTranslationCache getInstance() {
        return staticInstance;
    }

    /**
     * Indicates if the language is loaded in cache
     * 
     * @param pLanguage
     *            the language to look for
     * @return <CODE>true</CODE> or <CODE>false</CODE> if the language is loaded
     *         in cache or not
     */
    public boolean isLanguageLoaded(String pLanguage) {
        synchronized (cache) {
            return cache.get(pLanguage) != null;
        }
    }

    /**
     * Return all translations in cache for language and type in parameters
     * 
     * @param pLang
     *            The language
     * @param pType
     *            The type
     * @return All translations in cache for language and type in parameters
     */
    public Map<String, String> getAllTranslations(String pLang, String pType) {
        return getTranslationsMap(pLang, pType);
    }
}
