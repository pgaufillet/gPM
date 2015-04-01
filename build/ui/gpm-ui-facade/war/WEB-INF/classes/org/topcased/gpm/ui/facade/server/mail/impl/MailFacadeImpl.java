/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.server.mail.impl;

import java.util.List;

import org.topcased.gpm.business.mail.MailData;
import org.topcased.gpm.business.mail.MailDataFile;
import org.topcased.gpm.ui.facade.server.AbstractFacade;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.mail.MailFacade;

/**
 * MailFacade
 * 
 * @author nveillet
 */
public class MailFacadeImpl extends AbstractFacade implements MailFacade {

    private static final String ATTACHED_FILE_NAME = "sheetExport.pdf";

    /**
     * Send a mail
     * 
     * @param pSession
     *            the session
     * @param pDestinationUsers
     *            the destination user mail addresses
     * @param pSubject
     *            the mail subject
     * @param pBody
     *            the mail body
     * @param pAttachedFile
     *            the mail attached file
     * @param pOutputStream
     */
    public void sendMail(UiSession pSession, List<String> pDestinationUsers,
            String pSubject, String pBody, byte[] pAttachedFile) {
        String[] lTo = pDestinationUsers.toArray(new String[0]);

        MailDataFile lMailDataFile = new MailDataFile();
        lMailDataFile.setAttachedFile(pAttachedFile);
        lMailDataFile.setName(ATTACHED_FILE_NAME);
        MailDataFile[] lMailDataFileList = { lMailDataFile };

        MailData lMailData =
                new MailData(lTo, pBody, pSubject, lMailDataFileList);

        getMailService().sendMail(lMailData);
    }
}
