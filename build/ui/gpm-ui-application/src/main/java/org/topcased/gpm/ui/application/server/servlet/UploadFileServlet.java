/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.server.servlet;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.ui.application.server.SessionAttributesEnum;
import org.topcased.gpm.ui.facade.server.authorization.UiUserSession;

import com.google.inject.Singleton;

/**
 * UploadFileServlet
 * 
 * @author tpanuel
 */
@Singleton
public class UploadFileServlet extends HttpServlet {
    private static final long serialVersionUID = 3266802243933902341L;

    /**
     * {@inheritDoc}
     * 
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @SuppressWarnings("unchecked")
	@Override
    protected void doPost(final HttpServletRequest pReq,
            final HttpServletResponse pResp) throws ServletException,
        IOException {
        pReq.setCharacterEncoding("UTF-8");
        try {
            UiUserSession lUiUserSession = (UiUserSession) pReq.getSession()
            		.getAttribute(SessionAttributesEnum.GPM_USER_SESSION_ATTR.name());
            FileItemFactory lFactory = new DiskFileItemFactory();
            List<FileItem> lItems = new ServletFileUpload(lFactory).parseRequest(pReq);

            for (FileItem lItem : lItems) {
                String lName = new String(lItem.getName());
                final InputStream lInput = new BufferedInputStream(lItem.getInputStream());

                // Read file content
                final byte[] lResult = new byte[(int) lItem.getSize()];
                final int lSize = lResult.length;
                int c;
                for (int i=0; i<lSize; i++) {
                	c = lInput.read();
                	if (c == -1) {
                		throw new GDMException("Could not upload file " + lName + ": unexpected end of file.");
                	}
                	lResult[i] = (byte) c;
                }

                // Add the file on the upload file map
                lUiUserSession.addTemporaryUploadedFile(lName, lResult);
                lInput.close();
            }
        }
        catch (FileUploadException e) {
            throw new GDMException(e);
        }
    }
}