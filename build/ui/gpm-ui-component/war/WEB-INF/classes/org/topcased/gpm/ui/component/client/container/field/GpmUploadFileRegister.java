/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.container.field;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Register for upload field.
 * 
 * @author tpanuel
 */
public class GpmUploadFileRegister implements Iterable<GpmUploadFile> {
    private final Set<GpmUploadFile> uploadFiles;

    /**
     * Create a GpmUploadFileRegirster.
     */
    public GpmUploadFileRegister() {
        uploadFiles = new HashSet<GpmUploadFile>();
    }

    /**
     * Register an upload file.
     * 
     * @param pUploadFile
     *            The upload file.
     */
    public void registerUploadFile(final GpmUploadFile pUploadFile) {
        uploadFiles.add(pUploadFile);
    }

    /**
     * Unregister an upload file.
     * 
     * @param pUploadFile
     *            The upload file.
     */
    public void unregisterUploadFile(final GpmUploadFile pUploadFile) {
        uploadFiles.remove(pUploadFile);
    }

    /**
     * Unregister all the upload files.
     */
    public void unregisterAll() {
        uploadFiles.clear();
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<GpmUploadFile> iterator() {
        return uploadFiles.iterator();
    }
}