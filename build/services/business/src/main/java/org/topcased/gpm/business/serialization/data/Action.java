/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/

package org.topcased.gpm.business.serialization.data;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * The Class Action.
 * 
 * @author llatil
 */
@XStreamAlias("action")
public class Action extends Command {

    private static final long serialVersionUID = -9149281326112534610L;

    /** The name of the class implementing the action. */
    @XStreamAsAttribute
    private String classname;

    /**
     * Get the name of the class implementing the action.
     * 
     * @return the name of the class implementing the action.
     */
    public final String getClassname() {
        return classname;
    }

    /**
     * Set the name of the class implementing the action.
     * 
     * @param pClassname
     *            the name of the class implementing the action.
     */
    public final void setClassname(String pClassname) {
        classname = pClassname;
    }
}
