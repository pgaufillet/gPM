/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Magali Franchet (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.help.service;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * HelpService
 * 
 * @author mfranche
 */
@Transactional(propagation = Propagation.REQUIRED)
public interface HelpService {

    /**
     * Get value of "helpContentUrl" property
     * 
     * @return Url of help content.
     */
    public String getHelpContentUrl();
}
