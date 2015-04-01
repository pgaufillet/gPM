/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.serialization.data;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * A LinkTypeSorter maps the sorts of a link type in gPM and is used for XML
 * marshalling/unmarshalling of a sort
 * 
 * @author tpanuel
 */
@XStreamAlias("sort")
public class LinkTypeSorter implements Serializable {
    private static final long serialVersionUID = 6994955803804474269L;

    @XStreamAsAttribute
    private String filterName;

    @XStreamAsAttribute
    private String sheetType;

    /**
     * Getter on the filterName
     * 
     * @return The filterName
     */
    public String getFilterName() {
        return filterName;
    }

    /**
     * Setter on the filterName
     * 
     * @param pFilterName
     *            The new filterName
     */
    public void setFilterName(String pFilterName) {
        filterName = pFilterName;
    }

    /**
     * Getter on the sheet type (Origin, Destination or Both)
     * 
     * @return The sheet type
     */
    public String getSheetType() {
        return sheetType;
    }

    /**
     * Setter on the sheet type (Origin, Destination or Both)
     * 
     * @param pSheetType
     *            The new sheet type
     */
    public void setSheetType(String pSheetType) {
        sheetType = pSheetType;
    }
}
