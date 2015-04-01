/***************************************************************
 * Copyright (c) 2012 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Cyril Marchive (Atos)
 ******************************************************************/
package org.topcased.gpm.business.serialization.data;

import java.util.LinkedList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Class mapping a sheets content.
 * 
 * @author cmarchive
 */
@XStreamAlias("sheets")
public class SheetsData extends ValuesContainerData {

    /**
     * Default ID
     */
    private static final long serialVersionUID = -6234319915307878462L;

    /**
     * Individual values of the reference (used to define / update the
     * reference.
     */
    @XStreamAlias(value = "sheet", impl = LinkedList.class)
    private List<SheetData> sheets;

    public List<SheetData> getSheets() {
        return sheets;
    }

    public void setSheets(List<SheetData> pSheets) {
        this.sheets = pSheets;
    }
}
