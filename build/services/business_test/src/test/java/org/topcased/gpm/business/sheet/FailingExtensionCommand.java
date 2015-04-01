/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas Samson (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.sheet;

import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.extensions.service.GDMExtension;

/**
 * Extension point command that always fails It throws a NullPointerException in
 * execute(). The purpose of this class is to validate the behavior of an
 * extension point execution when one of the associated commands fails.
 * 
 * @author ahaugomm
 */
public class FailingExtensionCommand implements GDMExtension {

    /**
     * Command execution.
     * <p>
     * The implementation fails deliberately.
     * 
     * @param pContext
     *            Execution context
     * @return N/A (this method never returns)
     * @throws NullPointerException
     *             Exception thrown in all cases
     */
    public boolean execute(Context pContext) {
        throw new NullPointerException();
    }
}
