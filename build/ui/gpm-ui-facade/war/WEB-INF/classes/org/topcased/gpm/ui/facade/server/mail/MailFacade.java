/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.server.mail;

import java.util.List;

import org.topcased.gpm.ui.facade.server.authorization.UiSession;

/**
 * MailFacade
 * 
 * @author nveillet
 */
public interface MailFacade {

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
            String pSubject, String pBody, byte[] pAttachedFile);
}
