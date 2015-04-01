/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available 
 * under the terms of the Lesser Gnu Public License (LGPL)which 
 * accompanies this distribution, and is available 
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Sophian Idjellidaine (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.serialization.data;

/**
 * A message maps to a Message in gPM and is used for XML
 * marshalling/unmarshalling. Here a message is just composed with a key and
 * label.
 * 
 * @author sidjelli
 */
public class Message {

    /** The key. */
    private String key;

    /** The translated text. */
    private String translatedText;

    /**
     * Gets the key.
     * 
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * Sets the key.
     * 
     * @param pKey
     *            the key
     */
    public void setKey(String pKey) {
        this.key = pKey;
    }

    /**
     * Gets the translated text.
     * 
     * @return the translated text
     */
    public String getTranslatedText() {
        return org.topcased.gpm.util.lang.StringUtils.normalizeString(translatedText.trim());
    }

    /**
     * Sets the translated text.
     * 
     * @param pTranslatedText
     *            the translated text
     */
    public void setTranslatedText(String pTranslatedText) {
        this.translatedText = pTranslatedText;
    }
}
