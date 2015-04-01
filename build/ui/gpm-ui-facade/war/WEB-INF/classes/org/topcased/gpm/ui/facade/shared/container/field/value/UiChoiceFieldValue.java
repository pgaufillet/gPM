/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.shared.container.field.value;

import org.topcased.gpm.business.util.Translation;

/**
 * UiChoiceFieldValue
 * 
 * @author nveillet
 */
public class UiChoiceFieldValue extends Translation {

    /** serialVersionUID */
    private static final long serialVersionUID = 5734149403642450578L;

    private String translatedImageValue;

    /**
     * Empty Constructor
     */
    public UiChoiceFieldValue() {
        super();
    }

    /**
     * Constructor
     * 
     * @param pValue
     *            the value
     * @param pTranslatedValue
     *            the translated text value
     * @param pTranslatedImageValue
     *            the translated image value
     */
    public UiChoiceFieldValue(String pValue, String pTranslatedValue,
            String pTranslatedImageValue) {
        super(pValue, pTranslatedValue);
        translatedImageValue = pTranslatedImageValue;
    }

    /**
     * get translatedImageValue
     * 
     * @return the translatedImageValue
     */
    public String getTranslatedImageValue() {
        return translatedImageValue;
    }

    /**
     * set translatedImageValue
     * 
     * @param pTranslatedImageValue
     *            the translatedImageValue to set
     */
    public void setTranslatedImageValue(String pTranslatedImageValue) {
        translatedImageValue = pTranslatedImageValue;
    }

}
