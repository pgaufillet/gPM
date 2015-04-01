/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Thomas Szadel
 * (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.i18n.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * The I18n Service.
 * 
 * @author tszadel
 */
@Transactional(propagation = Propagation.REQUIRED)
public interface I18nService {

    public static final String TYPE_IMAGE = "IMAGE";

    /**
     * Define the user's preferred language.
     * 
     * @param pUserToken
     *            The user token (see Authorization)
     * @param pLang
     *            The language (en_EN, fr_FR...).
     */
    public void setPreferredLanguage(String pUserToken, String pLang);

    /**
     * Returns the user's preferred language.
     * 
     * @param pToken
     *            The user/role session token (see Authorization)
     * @return The user's preferred language or default one if not defined.
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public String getPreferredLanguage(String pToken);

    /**
     * Gets a value in the specified language. If the value isn't found in this
     * language, then the default language is used.
     * 
     * @param pLabelKey
     *            The label key.
     * @param pLang
     *            The language.
     * @return The translated text. If no translation exists, the label key is
     *         returned.
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public String getValue(String pLabelKey, String pLang);

    /**
     * Gets a value in the specified language. If the value isn't found in this
     * language, then the default language is used.
     * 
     * @param pLabelKey
     *            The label key.
     * @param pLang
     *            The language.
     * @param pType
     *            Type of the element (ex: "img" for image URL)
     * @return The translated text. If no translation exists, the label key is
     *         returned.
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public String getTypedValue(String pLabelKey, String pLang, String pType);

    /**
     * Define a new translation in the specified language.
     * 
     * @param pLabelKey
     *            The label key.
     * @param pLang
     *            The language.
     * @param pTranslation
     *            The translated text. If no translation exists, the label key
     *            is returned.
     */
    public void setValue(String pLabelKey, String pLang, String pTranslation);

    /**
     * Define a new translation in the specified language.
     * 
     * @param pLang
     *            The language.
     * @param pTranslations
     *            A map defined all translation to add/update (map contains
     *            <key, message> entries.
     */
    public void setValues(String pLang, Map<String, String> pTranslations);

    /**
     * Define a new translation item in the specified language.
     * 
     * @param pLabelKey
     *            The label key.
     * @param pLang
     *            The language.
     * @param pType
     *            Type of the element (ex: "img" for image URL)
     * @param pTranslation
     *            The translated text. If no translation exists, the label key
     *            is returned.
     */
    public void setTypedValue(String pLabelKey, String pLang, String pType,
            String pTranslation);

    /**
     * Define a new translation item in the specified language.
     * 
     * @param pLang
     *            The language.
     * @param pType
     *            Type of the element (ex: "img" for image URL)
     * @param pTranslations
     *            A map defined all translation to add/update (map contains
     *            <key, message> entries).
     */
    public void setTypedValues(String pLang, String pType,
            Map<String, String> pTranslations);

    /**
     * Gets a value in the user preferred language. If the value isn't found in
     * this language, then the default language is used.
     * 
     * @param pToken
     *            User or role session token.
     * @param pLabelKey
     *            The label key.
     * @return The translated text. If no translation exists, the label key is
     *         returned.
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public String getValueForUser(String pToken, String pLabelKey);

    /**
     * Gets a value in the user preferred language. If the value isn't found in
     * this language, then the default language is used.
     * 
     * @param pToken
     *            User or role session token.
     * @param pLabelKey
     *            The label key.
     * @param pType
     *            Type of the element (ex: "img" for image URL)
     * @return The translated text. If no translation exists, the label key is
     *         returned.
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public String getTypedValueForUser(String pToken, String pLabelKey,
            String pType);

    /**
     * Gets a value in the user preferred language. If the value isn't found in
     * this language, then the default language is used.
     * 
     * @param pToken
     *            User or role session token.
     * @param pKeys
     *            List of keys to translate.
     * @deprecated This method should not be used because the behavior is
     *             already implemented in get translations methods
     * @see I18nService#getValue(String)
     * @see I18nService#getTypedValue(String, String, String)
     * @see I18nService#getTypedValueForUser(String, String, String)
     * @return The translated text. If no translation exists, the label key is
     *         returned.
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Map<String, String> getValuesForUser(String pToken,
            Collection<String> pKeys);

    /**
     * Gets in the preferred language of a just-logged-in user
     * 
     * @param pLoginToken
     *            User token.
     * @param pKeys
     *            List of keys to translate.
     * @return The translated text. If no translation exists, the label key is
     *         returned.
     * @author yntsama
     */
    public Map<String, String> getSimpleValuesForUser(String pLoginToken,
            ArrayList<String> pKeys);

    /**
     * Get the list of available languages in GUI
     * 
     * @return an array of locale names.
     */
    public String[] getAvailableLanguages();

    /**
     * Get all values for the specified language.
     * <p>
     * The first list contains the text values and second contains the img
     * value.
     * </p>
     * 
     * @param pLang
     *            Language of translation to retrieve
     * @return Translations according to the specified language.
     */
    public List<Map<String, String>> getValue(final String pLang);
}
