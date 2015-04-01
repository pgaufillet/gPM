/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien LOUISY (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.serialization.data;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * Choice tree display hint.
 * 
 * @author jlouisy
 */
@XStreamAlias("choiceTreeDisplayHint")
public class ChoiceTreeDisplayHint extends DisplayHint {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 560802317084041717L;

    /** The separator. */
    @XStreamAsAttribute
    private String separator;

    /**
     * Get the separator
     * 
     * @return the separator
     */
    public String getSeparator() {
        return separator;
    }

    /**
     * Specify the separator.
     * 
     * @param pSeparator
     *            the separator.
     */
    public void setSeparator(String pSeparator) {
        separator = pSeparator;
    }

}
