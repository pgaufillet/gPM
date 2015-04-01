/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.server;

import org.topcased.gpm.ui.facade.server.authorization.UiUserSession;

/**
 * Session's attributes
 * 
 * @author mkargbo
 */
public enum SessionAttributesEnum {

    /** {@link UiUserSession} attribute */
    GPM_USER_SESSION_ATTR(),

    /**
     * {@link SessionAttributesEnum#INTERNAL} or
     * {@link SessionAttributesEnum#EXTERNAL} attribute
     */
    AUTHENTICATION_SYSTEM_TYPE(),

    /** String of the user identifier. */
    AUTHENTICATION_SYSTEM_USER_ID(),

    /**
     * Value to the attribute
     * {@link SessionAttributesEnum#AUTHENTICATION_SYSTEM_TYPE}
     */
    INTERNAL(),

    /**
     * Value to the attribute
     * {@link SessionAttributesEnum#AUTHENTICATION_SYSTEM_TYPE}
     */
    EXTERNAL();

}
