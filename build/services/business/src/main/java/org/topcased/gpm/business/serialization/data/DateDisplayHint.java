/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Magali Franchet (Neo-Soft on behalf of Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.serialization.data;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * DateDisplayHint
 * 
 * @author mfranche
 */
@XStreamAlias("dateDisplayHint")
public class DateDisplayHint extends DisplayHint {

    /** serialVersionUID */
    private static final long serialVersionUID = -7115785230009258000L;

    /** The format */
    @XStreamAsAttribute
    private String format;

    /** Include time in the date format */
    @XStreamAsAttribute
    private Boolean includeTime;

    /**
     * get format
     * 
     * @return the format
     */
    public String getFormat() {
        return format;
    }

    /**
     * set format
     * 
     * @param pFormat
     *            the format to set
     */
    public void setFormat(String pFormat) {
        format = pFormat;
    }

    /**
     * Check if the time must be included in date format.
     * 
     * @return true if the date format must display the time.
     */
    public boolean hasTime() {
        return (includeTime != null && includeTime);
    }

    public Boolean getIncludeTime() {
        return includeTime;
    }

    public void setIncludeTime(Boolean pIncludeTime) {
        includeTime = pIncludeTime;
    }
}
