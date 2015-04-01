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

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * The Class Script.
 * 
 * @author llatil
 */
@XStreamAlias("script")
public class Script extends Command {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 728962853148663752L;

    /** The language. */
    @XStreamAsAttribute
    private String language;

    /** The code. */
    private Code code;

    /**
     * Constructs a new script.
     * 
     * @param pLanguage
     *            the language
     * @param pCode
     *            the code
     */
    public Script(String pLanguage, String pCode) {
        language = pLanguage;
        code = new Code();
        code.setContent(pCode);
    }

    /**
     * Get the script code.
     * 
     * @return Script code.
     */
    public Code getCode() {
        return code;
    }

    /**
     * Get the script language name.
     * 
     * @return Script language name
     */
    public String getLanguage() {
        return language;
    }
}
