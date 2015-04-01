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

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * MenuEntry.
 * 
 * @author ahaugomm
 */
@XStreamAlias("menuEntry")
public class MenuEntry extends NamedElement {

    /** Generated UID */
    private static final long serialVersionUID = -3955703049458603614L;

    /** The parent name. */
    @XStreamAsAttribute
    private String parentName;

    /**
     * get parentName.
     * 
     * @return the parentName
     */
    public String getParentName() {
        return parentName;
    }

    /**
     * set parentName.
     * 
     * @param pParentName
     *            the parentName to set
     */
    public void setParentName(String pParentName) {
        this.parentName = pParentName;
    }
}
