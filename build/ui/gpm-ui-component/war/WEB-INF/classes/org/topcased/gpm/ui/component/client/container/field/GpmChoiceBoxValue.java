/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.container.field;

import java.util.ArrayList;
import java.util.List;

import org.topcased.gpm.business.util.Translation;

/**
 * Class handling the value/displayed value behavior of ChoiceBox
 * 
 * @author nveillet
 */
public class GpmChoiceBoxValue {

    /**
     * Utility method to build a ChoiceBox list with single values.
     * 
     * @param pValues
     *            the values to transform
     * @return the adapted list of GpmChoiceBoxValue
     */
    public static List<GpmChoiceBoxValue> buildFromStrings(List<String> pValues) {
        List<GpmChoiceBoxValue> lValues = new ArrayList<GpmChoiceBoxValue>();
        for (String lValue : pValues) {
            lValues.add(new GpmChoiceBoxValue(lValue, lValue));
        }
        return lValues;
    }

    /**
     * Utility method to build a ChoiceBox list with translation values.
     * 
     * @param pValues
     *            the values to transform
     * @return the adapted list of GpmChoiceBoxValue
     */
    public static List<GpmChoiceBoxValue> buildFromTranslations(
            List<Translation> pValues) {
        List<GpmChoiceBoxValue> lValues = new ArrayList<GpmChoiceBoxValue>();
        for (Translation lValue : pValues) {
            lValues.add(new GpmChoiceBoxValue(lValue.getValue(),
                    lValue.getTranslatedValue()));
        }
        return lValues;
    }

    private final String displayedImage;

    private final String displayedValue;

    private final String value;

    /**
     * Constructor building a ChoiceBox value with its string value and
     * displayed value
     * 
     * @param pValue
     *            String Value
     * @param pDisplayedValue
     *            Displayed String
     */
    public GpmChoiceBoxValue(String pValue, String pDisplayedValue) {
        value = pValue;
        displayedValue = pDisplayedValue;
        displayedImage = null;
    }

    /**
     * Constructor building a ChoiceBox value with its string value, a displayed
     * value and an image URL
     * 
     * @param pValue
     *            String Value
     * @param pDisplayedValue
     *            Displayed String
     * @param pDisplayedImage
     *            the image URL to display
     */
    public GpmChoiceBoxValue(String pValue, String pDisplayedValue,
            String pDisplayedImage) {
        value = pValue;
        displayedValue = pDisplayedValue;
        displayedImage = pDisplayedImage;
    }

    /**
     * Displayed image
     * 
     * @return the displayedImage
     */
    public String getDisplayedImage() {
        return displayedImage;
    }

    /**
     * Representing String
     * 
     * @return Representing String
     */
    public String getDisplayedValue() {
        return displayedValue;
    }

    /**
     * String value
     * 
     * @return String value
     */
    public String getValue() {
        return value;
    }
}