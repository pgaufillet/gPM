/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Thomas Szadel
 * (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.i18n.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
//import org.apache.log4j.Logger;
import org.topcased.gpm.business.ServiceImplBase;
import org.topcased.gpm.business.authorization.service.EndUserData;
import org.topcased.gpm.business.exception.InvalidNameException;
import org.topcased.gpm.business.exception.InvalidTokenException;
import org.topcased.gpm.business.i18n.service.I18nService;
import org.topcased.gpm.domain.i18n.I18nValue;
import org.topcased.gpm.domain.i18n.I18nValueDao;
import org.topcased.gpm.util.session.GpmSessionFactory;

/**
 * The implementation of I18nService.
 * 
 * @author tszadel
 */
public class I18nServiceImpl extends ServiceImplBase implements I18nService {

    /** The name of the user attribute used to store the default language. */
    public static final String ATTR_USER_DEFAULT_LANGUAGE = "defaultLanguage";

    /** The default language. */
    public static final String DEFAULT_LANGUAGE = "en";

    /** The key for the language. */
    private static final String LANGUAGE_KEY = "lang";

    /**
     * The string to use in database to specify an 'empty' language. In this
     * case the value is used as default (the one selected if no language
     * redefines it) Note: This string is used to avoid using a problematic SQL
     * 'null' value or empty string.
     */
    private static final String EMPTY_LANGUAGE_STRING = " ";

    /** The Dao. */
    private I18nValueDao i18nValueDao;

    /** Translation texts cache. */
    private final I18nTranslationCache translationsCache =
            I18nTranslationCache.getInstance();

    /**
     * Define the user's preferred language.
     * 
     * @param pUserToken
     *            The user token (see Authorization)
     * @param pLang
     *            The language (en_EN, fr_FR...).
     */
    final public void setPreferredLanguage(String pUserToken, String pLang) {
        String lLang = pLang;

        // If no language is specified, use the default one.
        if (StringUtils.isBlank(lLang)) {
            lLang = DEFAULT_LANGUAGE;
        }
        getAuthService().setSessionAttribute(pUserToken, LANGUAGE_KEY, lLang);
    }

    /**
     * Returns the user's preferred language.
     * 
     * @param pToken
     *            The user or role token (see Authorization)
     * @return The user's preferred language or default one if not defined.
     */
    final public String getPreferredLanguage(String pToken) {
        String lToken = getAuthService().getUserToken(pToken);
        String lLang =
                (String) getAuthService().getSessionAttribute(lToken,
                        LANGUAGE_KEY);

        if (StringUtils.isBlank(lLang)) {
            EndUserData lUser =
                    getAuthService().getUserData(
                            getAuthService().getLoginFromToken(lToken));
            lLang =
                    getAuthService().getValueOfUserAttribute(lUser,
                            ATTR_USER_DEFAULT_LANGUAGE);

            if (StringUtils.isBlank(lLang)) {
                lLang = DEFAULT_LANGUAGE;
            }
            getAuthService().setSessionAttribute(lToken, LANGUAGE_KEY, lLang);
        }
        return lLang;
    }

    /**
     * Gets a value in the specified language.
     * 
     * @param pLabelKey
     *            The label key.
     * @param pLang
     *            The language.
     * @return The translated text. If no translation exists, the label key is
     *         returned.
     * @throws InvalidNameException
     *             The label key is blank.
     */
    final public String getValue(String pLabelKey, String pLang) {
        return getTypedValue(pLabelKey, pLang, null);
    }

    /**
     * Gets a value of a given type in the specified language.
     * 
     * @param pLabelKey
     *            The label key.
     * @param pLang
     *            The language.
     * @param pType
     *            Type.
     * @return The translated text. If no translation exists, the label key is
     *         returned.
     * @throws InvalidNameException
     *             The label key is blank.
     */
    final public String getTypedValue(String pLabelKey, String pLang,
            String pType) {
        if (pLabelKey == null || pLang == null) {
            throw new IllegalArgumentException();
        }

        // If the cache for the language is not loaded, load it
        if (!translationsCache.isLanguageLoaded(pLang)) {
            getValue(pLang);
        }

        // Lookup the translation in cache
        String lValue =
                translationsCache.getTranslation(pLang, pLabelKey, pType);
        if (null != lValue) {
            return lValue;
        }

        if (StringUtils.isBlank(pLang)) {
            // Use the default language.
            pLang = DEFAULT_LANGUAGE;
        }

        // Maybe the language is en_EN
        // (composed of the country and the variant, only get the country)

        // If the cache for the language is not loaded, load it
        String lShortLanguage = getCountry(pLang);
        if (!translationsCache.isLanguageLoaded(lShortLanguage)) {
            getValue(pLang);
        }

        lValue =
                translationsCache.getTranslation(lShortLanguage, pLabelKey,
                        pType);

        // If really no translation found, use labelKey
        if (lValue == null) {
            lValue = pLabelKey;
        }

        // Store it in cache
        translationsCache.setTranslation(pLang, pLabelKey, pType, lValue);
        return lValue;
    }

    /**
     * Gets the country.
     * 
     * @param pLang
     *            the lang
     * @return the country
     */
    private String getCountry(String pLang) {
        String[] lLang = pLang.split("_");
        return lLang[0];
    }

    /**
     * Gets a value in the user preferred language.
     * <p>
     * If the value isn't found in this language, then the default language is
     * used.
     * 
     * @param pToken
     *            User session token.
     * @param pLabelKey
     *            The label key.
     * @return The translated text. If no translation exists, the label key is
     *         returned.
     * @throws InvalidTokenException
     *             The user token is blank.
     */
    final public String getValueForUser(String pToken, String pLabelKey) {
        String lPreferredLang = getPreferredLanguage(pToken);
        return getValue(pLabelKey, lPreferredLang);
    }

    /**
     * Gets a value in the user preferred language. If the value isn't found in
     * this language, then the default language is used.
     * 
     * @param pToken
     *            User or role session token.
     * @param pLabelKey
     *            The label key.
     * @param pType
     *            Type
     * @return The translated text. If no translation exists, the label key is
     *         returned.
     * @throws InvalidTokenException
     *             The user token is blank.
     */
    final public String getTypedValueForUser(String pToken, String pLabelKey,
            String pType) {
        String lUserToken = getAuthService().getUserToken(pToken);

        String lPreferredLang = getPreferredLanguage(lUserToken);
        return getTypedValue(pLabelKey, lPreferredLang, pType);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.i18n.service.I18nService#getValuesForUser(java.lang.String,
     *      java.util.Collection)
     */
    public final Map<String, String> getValuesForUser(String pToken,
            java.util.Collection<String> pKeys) {
        Map<String, String> lMap = new HashMap<String, String>(pKeys.size());
        String lLang = getPreferredLanguage(pToken);

        for (String lKey : pKeys) {
            lMap.put(lKey, getValue(lKey, lLang));
            String lTypedValue = getTypedValue(lKey, lLang, "IMAGE");
            if (!lTypedValue.equals(lKey)) {
                lMap.put(lKey + "_IMAGE", lTypedValue);
            }
        }
        return lMap;
    }

    /**
     * {@inheritDoc}
     * 
     * @author yntsama
     * @deprecated This method should not be used because the behavior is
     *             already implemented in get translations methods
     * @see I18nService#getValue(String)
     * @see I18nService#getTypedValue(String, String, String)
     * @see I18nService#getTypedValueForUser(String, String, String)
     * @see org.topcased.gpm.business.i18n.service.I18nService#getValuesForUser(java.lang.String,
     *      java.util.Collection)
     */
    public final Map<String, String> getSimpleValuesForUser(String pLoginToken,
            ArrayList<String> pKeys) {
        Map<String, String> lMap = new HashMap<String, String>(pKeys.size());
        String lLang = getPreferredLanguage(pLoginToken);

        for (String lKey : pKeys) {
            lMap.put(lKey, getValue(lKey, lLang));
        }
        return lMap;
    }

    /**
     * Define a new translation in the specified language. If the language is
     * not specified, the value is defined as default translation.
     * 
     * @param pLabelKey
     *            The label key.
     * @param pLang
     *            The language (use null for 'default' translation)
     * @param pTranslation
     *            The translated text. If no translation exists, the label key
     *            is returned.
     * @throws InvalidNameException
     *             The label key is blank or the translation is null.
     */
    public final void setValue(String pLabelKey, String pLang,
            String pTranslation) {
        setTypedValue(pLabelKey, pLang, null, pTranslation);
    }

    /**
     * Define a new translation in the specified language.
     * 
     * @param pLabelKey
     *            The label key.
     * @param pLang
     *            The language (use null for 'default' translation).
     * @param pType
     *            Type of the element (ex: "img" for image URL).
     * @param pTranslation
     *            The translated text. If no translation exists, the label key
     *            is returned.
     * @throws InvalidNameException
     *             The label key is blank or the translation is null.
     */
    public final void setTypedValue(String pLabelKey, String pLang,
            String pType, String pTranslation) {

        if (StringUtils.isBlank(pLabelKey)) {
            throw new IllegalArgumentException("The label key is blank.");
        }
        if (StringUtils.isBlank(pTranslation)) {
            throw new IllegalArgumentException("The translated text is blank.");
        }

        // Use the 'empty language' string if no language is specified.
        if (StringUtils.isBlank(pLang)) {
            pLang = EMPTY_LANGUAGE_STRING;
        }

        // Set the new value
        internalSetValue(pLabelKey, pLang, pType, pTranslation);

        GpmSessionFactory.getHibernateSession().flush();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.i18n.service.I18nService#setValues(java.lang.String,
     *      java.util.Map)
     */
    public final void setValues(String pLang, Map<String, String> pTranslations) {
        setTypedValues(pLang, null, pTranslations);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.i18n.service.I18nService#setTypedValues(java.lang.String,
     *      java.lang.String, java.util.Map)
     */
    public final void setTypedValues(String pLang, String pType,
            Map<String, String> pTranslations) {
        if (null == pTranslations) {
            throw new IllegalArgumentException("Translations map is null");
        }

        // Use the 'empty language' string if no language is specified.
        if (StringUtils.isBlank(pLang)) {
            pLang = EMPTY_LANGUAGE_STRING;
        }

        // Set All values
        for (Map.Entry<String, String> lEntry : pTranslations.entrySet()) {
            internalSetValue(lEntry.getKey(), pLang, pType, lEntry.getValue());
        }

        GpmSessionFactory.getHibernateSession().flush();
    }

    /**
     * Internal set value. Sets the value in the database And in cache.
     * 
     * @param pLabelKey
     *            the label key
     * @param pLang
     *            the lang
     * @param pType
     *            the type
     * @param pTranslation
     *            the translation
     */
    private void internalSetValue(String pLabelKey, String pLang, String pType,
            String pTranslation) {
        I18nValue lI18nValue;
        if (null == pType) {
            lI18nValue = i18nValueDao.get(pLabelKey, pLang);
        }
        else {
            lI18nValue = i18nValueDao.getTyped(pLabelKey, pLang, pType);
        }

        if (null != lI18nValue) {
            if (StringUtils.isBlank(pTranslation)) {
                pTranslation = EMPTY_LANGUAGE_STRING;
            }
            lI18nValue.setValue(pTranslation);
        }
        else {
            lI18nValue = I18nValue.newInstance();
            lI18nValue.setLabelKey(pLabelKey);
            lI18nValue.setLang(pLang);
            lI18nValue.setValue(pTranslation);
            lI18nValue.setType(pType);
            i18nValueDao.create(lI18nValue);
        }

        translationsCache.setTranslation(pLang, pLabelKey, pType, pTranslation);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.i18n.service.I18nService#getAvailableLanguages()
     */
    public String[] getAvailableLanguages() {
        return i18nValueDao.getAvailableLanguages();
    }

    /**
     * Sets the I18N dao.
     * 
     * @param pDao
     *            The dao.
     */
    public void setI18nValueDao(I18nValueDao pDao) {
        i18nValueDao = pDao;
    }

    /**
     * Gets the I18N dao.
     * 
     * @return The dao.
     */
    protected I18nValueDao getI18nValueDao() {
        return i18nValueDao;
    }

    /**
     * {@inheritDoc} Get all translations directly from DAO bypassing caches. If
     * language can be shortened (use base language), it loads the corresponding
     * translations too.<br />
     * Example: en_EN > base language = en <br />
     * <b>IMPORTANT NOTE: </b>This method never reloads translations from
     * database, it could be wise to implement a cache lifetime.
     * 
     * @see org.topcased.gpm.business.i18n.service.I18nService#getValue(java.lang.String)
     */
    @Override
    public List<Map<String, String>> getValue(String pLang) {
        // TODO : See method comment about improving cache use

        // Store into 2 Map<String, String>
        Map<String, String> lTextValue = null;
        Map<String, String> lImgValue = null;

        if (translationsCache.isLanguageLoaded(pLang)) {
            lTextValue = translationsCache.getAllTranslations(pLang, null);
            lImgValue = translationsCache.getAllTranslations(pLang, TYPE_IMAGE);
            final List<Map<String, String>> lTranslations =
                    new ArrayList<Map<String, String>>(2);
            lTranslations.add(lTextValue);
            lTranslations.add(lImgValue);
            return lTranslations;
        }

        // If not in cache :

        boolean lInstantiateMaps = true;

        // First, get values from Main language ("en" for "en_EN") if different from current
        if (!getCountry(pLang).equals(pLang)) {
            final List<Map<String, String>> lTranslations =
                    getValue(getCountry(pLang));
            lTextValue = lTranslations.get(0);
            lImgValue = lTranslations.get(1);
        }
        else {
            lInstantiateMaps = false;
        }

        // Then get Text translations from DAO
        List<Object[]> lValues = i18nValueDao.getValues(pLang, null);
        if (!lInstantiateMaps) { // If map not instantiated
            lTextValue = new HashMap<String, String>(lValues.size());
        }
        // And iterate
        for (Object[] lValue : lValues) {
            lTextValue.put(lValue[0].toString(), lValue[1].toString());
        }

        // Get Image translations from DAO
        lValues = i18nValueDao.getValues(pLang, TYPE_IMAGE);
        lImgValue = new HashMap<String, String>();
        for (Object[] lValue : lValues) {
            lImgValue.put(lValue[0].toString(), lValue[1].toString());
        }

        // Put in return List
        final List<Map<String, String>> lTranslations =
                new ArrayList<Map<String, String>>(2);
        lTranslations.add(lTextValue);
        lTranslations.add(lImgValue);

        I18nTranslationCache.getInstance().setTranslations(pLang, lTextValue,
                null);
        I18nTranslationCache.getInstance().setTranslations(pLang, lTextValue,
                TYPE_IMAGE);

        return lTranslations;
    }
}
