/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien LOUISY (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.server.exportimport;

import java.io.IOException;
import java.io.OutputStream;

import org.topcased.gpm.ui.facade.server.FacadeCommand;

/**
 * ExportFileFacadeCommand
 * 
 * @author jlouisy
 */
public class ExportFileFacadeCommand implements FacadeCommand {

    private byte[] file;

    /**
     * ExportFileFacadeCommand constructor
     * 
     * @param pFile
     *            File to put in cache.
     */
    public ExportFileFacadeCommand(byte[] pFile) {
        file = pFile;
    }

    /**
     * {@inheritDoc}
     * 
     * @throws IOException
     * @see org.topcased.gpm.ui.facade.server.FacadeCommand#execute(java.io.OutputStream)
     */
    @Override
    public void execute(OutputStream pOutputStream) {
        try {
            pOutputStream.write(file);
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
