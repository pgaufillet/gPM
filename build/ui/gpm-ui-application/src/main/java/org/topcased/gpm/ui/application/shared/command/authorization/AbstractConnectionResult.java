/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.shared.command.authorization;

import net.customware.gwt.dispatch.shared.Result;

import org.topcased.gpm.ui.application.shared.command.init.InitInfo;

/**
 * AbstractConnectionResult
 * 
 * @author nveillet
 */
public abstract class AbstractConnectionResult implements Result {
    /** serialVersionUID */
    private static final long serialVersionUID = -2127904138674485880L;

    private InitInfo init;

    private String version;

    /**
     * set the info to initialize
     * 
     * @param pInit
     *            the initialization info
     */
    public void setInitInfo(final InitInfo pInit) {
        init = pInit;
    }

    /**
     * get the info to initialize : null if initialization is not need
     * 
     * @return the initialization info
     */
    public InitInfo getInitInfo() {
        return init;
    }

    /**
     * Set the version to be displayed in HMI
     * 
     * @param pVersion
     *            The version to be displayed in HMI
     */
    public void setVersion(String pVersion) {
        version = pVersion;
    }

    /**
     * get version
     * 
     * @return the version
     */
    public String getVersion() {
        return version;
    }
}