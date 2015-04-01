/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Magali Franchet (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.serialization.data;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * AdminAccessCtl
 * 
 * @author mfranche
 */
@XStreamAlias("adminAccess")
public class AdminAccessCtl extends AccessCtl {

    /** The action key. */
    @XStreamAsAttribute
    private String actionKey;

    /**
     * get actionKey
     * 
     * @return the actionKey
     */
    public String getActionKey() {
        return actionKey;
    }

}
