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

/**
 * Interface for gPM jobs
 * 
 * @author tpanuel
 */
public interface GpmJob {
    /**
     * Execute the job
     */
    public void execute();
}
