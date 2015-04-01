/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Atos Origin
 ******************************************************************/
package org.topcased.gpm.business.serialization.data;

import org.topcased.gpm.business.extensions.service.ExtensionPointParameters;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * The Class ChoiceStringDisplayHint.
 * 
 * @author llatil
 */
@XStreamAlias("choiceStringDisplayHint")
public class ChoiceStringDisplayHint extends DisplayHint {

    /** Generated serial UID. */
    private static final long serialVersionUID = 9197149931922035734L;

    /** Default value of 'strict' option. */
    private static final boolean STRICT_DEFAULT = false;

    /** The 'strict' option. */
    @XStreamAlias("strict")
    @XStreamAsAttribute
    private Boolean strict;

    /** The extension point name. */
    @XStreamAlias("extensionPoint")
    @XStreamAsAttribute
    private String extensionPointName;

    /** Type of display hint. */
    public static final String HINT_TYPE = "gPM.ChoiceString";

    /** Name of the attribute used to store the hint arguments */
    public static final String HINT_TYPE_ATTRIBUTES =
            "gPM.ChoiceStringAttributes";

    /**
     * Name of the context variable containing the list of acceptable values
     * 
     * @deprecated
     * @since 1.8
     * @see ExtensionPointParameters.CHOICES_RESULT
     */
    public static final String CTX_RESULT_NAME = "choices";

    /**
     * Constructor for mutable / immutable switch
     */
    public ChoiceStringDisplayHint() {
        super();
    }

    /**
     * Constructs a new choice string display hint.
     * 
     * @param pExtensionPoint
     *            Name of the extension point
     */
    public ChoiceStringDisplayHint(String pExtensionPoint) {
        extensionPointName = pExtensionPoint;
        strict = STRICT_DEFAULT;
    }

    /**
     * Constructs a new choice string display hint.
     * 
     * @param pExtensionPoint
     *            Name of the extension point
     * @param pStrict
     *            Specify if the choice list is strict or not (allow the user to
     *            type in values not present in the choice list).
     */
    public ChoiceStringDisplayHint(String pExtensionPoint, boolean pStrict) {
        extensionPointName = pExtensionPoint;
        strict = pStrict;
    }

    /**
     * Checks if is strict.
     * 
     * @return true, if is strict
     */
    public boolean isStrict() {
        if (null != strict) {
            return strict.booleanValue();
        }
        return STRICT_DEFAULT;
    }

    /**
     * Set the strict
     * 
     * @param pSrict
     *            the new strict
     */
    public void setStrict(boolean pSrict) {
        strict = pSrict;
    }

    /**
     * Gets the extension point name.
     * 
     * @return the extension point name
     */
    public String getExtensionPointName() {
        return extensionPointName;
    }

    /**
     * Set the extensionPointName
     * 
     * @param pExtensionPointName
     *            the new extensionPointName
     */
    public void setExtensionPointName(String pExtensionPointName) {
        extensionPointName = pExtensionPointName;
    }

    /**
     * Gets the hint type.
     * 
     * @return the hint type
     */
    public static String getHintType() {
        return HINT_TYPE;
    }

    /**
     * Gets the name of the attribute storing the hint parameters.
     * <p>
     * This attribute is currently attached to the field.
     * 
     * @return name of the attribute
     */
    public static String getHintAttributesName() {
        return HINT_TYPE_ATTRIBUTES;
    }
}
