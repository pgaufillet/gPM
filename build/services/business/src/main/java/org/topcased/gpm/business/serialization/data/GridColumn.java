/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.serialization.data;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * Represents a GridColumn element in the schema
 * 
 * @author mkargbo
 */
@XStreamAlias("gridColumn")
public class GridColumn extends NamedElement {

    /** Serial ID */
    private static final long serialVersionUID = -7825864410279203415L;

    @XStreamAlias("editorType")
    @XStreamAsAttribute
    private String editorType;

    public String getEditorType() {
        return editorType;
    }

    public void setEditorType(String pEditorType) {
        this.editorType = pEditorType;
    }
}
