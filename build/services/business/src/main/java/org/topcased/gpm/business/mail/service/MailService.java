/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Szadel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.mail.service;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.mail.MailData;

/**
 * Mail service.
 * 
 * @author tszadel
 */
@Transactional(propagation = Propagation.REQUIRED)
public interface MailService {
    /**
     * Sends a mail.
     * 
     * @param pData
     *            Mail data.
     * @throws GDMException
     *             Exception.
     */
    public void sendMail(MailData pData) throws GDMException;

    /**
     * Sends a mail.</br> If an exception is thrown and the parameter is set to
     * false, then the exception will be logged with log4j.
     * <p>
     * This behavior is set to avoid systematic rollback on an transaction if
     * there is an issue with the mail service used by the instance. Instance
     * can decide if a mailservice error is blocking for a transaction or not.
     * 
     * @param pData
     *            the mail data
     * @param pThrowException
     *            true to throw an exception, false to catch the exception and
     *            put it in a log.
     * @throws GDMException
     *             Exception
     */
    public void sendMail(MailData pData, boolean pThrowException)
        throws GDMException;
}
