/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.util.job;

import org.topcased.gpm.business.sheet.impl.SheetServiceImpl;

/**
 * Job for cleaning all the session locks
 * 
 * @author tpanuel
 */
public class JobSessionLockCleaner implements GpmJob {
    private SheetServiceImpl sheetServiceImpl;

    /**
     * Get the sheet service implementation
     * 
     * @return The sheet service implementation
     */
    public SheetServiceImpl getSheetServiceImpl() {
        return sheetServiceImpl;
    }

    /**
     * Set the sheet service implementation
     * 
     * @param pSheetServiceImpl
     *            The new sheet service implementation
     */
    public void setSheetServiceImpl(SheetServiceImpl pSheetServiceImpl) {
        sheetServiceImpl = pSheetServiceImpl;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.util.job.GpmJob#execute()
     */
    public void execute() {
        sheetServiceImpl.cleanAllSessionLocks();
    }
}
