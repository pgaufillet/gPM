/***************************************************************
 * Copyright (c) 2011 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Pierre Hubert TSAAN (ATOS)
 ******************************************************************/
package org.topcased.gpm.business.serialization.data;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * Applet parameter
 * 
 * @author phtsaan
 */
@XStreamAlias("appletParameter")
public class AppletParameter extends SimpleField implements Serializable {

    /**
     * Default ID 
     */
    private static final long serialVersionUID = 1L;

    /* parameter Name */
    @XStreamAsAttribute
    private String appletParamName;

    /**
     * Constructor
     * 
     * @param pAppletParamName applet parameter name
     */
    public AppletParameter(String pAppletParamName) {
        super();
        this.appletParamName = pAppletParamName;
    }

    /**
     * Retrieves parameter Name
     * 
     * @return paramName
     */
    public String getParamName() {
        return appletParamName;
    }

    /**
     * Set parameter name
     * 
     * @param pParamName parameter name
     */
    public void setParamName(String pParamName) {
        this.appletParamName = pParamName;
    }
}
