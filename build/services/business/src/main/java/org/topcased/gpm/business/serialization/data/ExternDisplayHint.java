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
 * Extern Display Hint
 * 
 * @author mfranche
 */
@XStreamAlias("displayHint")
public class ExternDisplayHint extends DisplayHint {

    /** serialVersionUID */
    private static final long serialVersionUID = 5070966965479034981L;

    @XStreamAsAttribute
    private String type;

    /**
     * get type
     * 
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * set type
     * 
     * @param pType
     *            the type to set
     */
    public void setType(String pType) {
        this.type = pType;
    }

}
