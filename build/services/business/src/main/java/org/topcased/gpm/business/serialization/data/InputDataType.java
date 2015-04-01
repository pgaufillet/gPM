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
 * InputDataType.
 * 
 * @author ahaugommard
 */
@XStreamAlias("inputDataType")
public class InputDataType extends FieldsContainer {

    /** Generated UID */
    private static final long serialVersionUID = 4311848886866226202L;

    /** Technical identifier of the sheet. */
    @XStreamAsAttribute
    private String id;

    /**
     * Gets the id.
     * 
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the id.
     * 
     * @param pId
     *            the new id
     */
    public void setId(String pId) {
        this.id = pId;
    }
}
