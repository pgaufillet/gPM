/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas Samson (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.mail;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.ServiceLocator;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.mail.service.MailService;

/**
 * Tests the method <CODE>sendMail<CODE> of the Mail Service.
 * 
 * @author nsamson
 */
public class TestSendMailService extends AbstractBusinessServiceTestCase {

    /** The Mail Service. */
    private MailService mailService;

    /** The mail from. */
    private static final String MAIL_FROM = "test-mail@test.fr";

    /** The mail from. */
    private static final String[] MAIL_TO = { "test-mail@test.fr" };

    /** The mail from. */
    private static final String[] MAIL_REPLY_TO = { "test-mail@test.fr" };

    /** The mail message. */
    private static final String MAIL_MESSAGE = "This is a test message.";

    /** The mail subject. */
    private static final String MAIL_SUBJECT = "Test message";

    /**
     * Tests the sendMail method.
     */
    public void testNormalCase() {
        // Gets the mail service.
        mailService = ServiceLocator.instance().getMailService();

        // Send mail
        MailData lMailData =
                new MailData(MAIL_FROM, MAIL_TO, MAIL_REPLY_TO, MAIL_MESSAGE,
                        MAIL_SUBJECT, new MailDataFile[0]);

        try {
            mailService.sendMail(lMailData);
        }
        catch (GDMException e) {
            fail(e);
        }
    }

    /**
     * Send a mail with a null subject
     */
    public void testWithNullSubject() {
        mailService = ServiceLocator.instance().getMailService();

        // Send mail
        MailData lMailData = new MailData();

        lMailData.setFrom(MAIL_FROM);
        lMailData.setTo(MAIL_TO);
        lMailData.setReplyTo(MAIL_REPLY_TO);
        lMailData.setMessage(MAIL_MESSAGE);
        lMailData.setSubject(null);
        lMailData.setMailDataFiles(new MailDataFile[0]);

        try {
            mailService.sendMail(lMailData);
            fail("The mail should not have been sent with null object.");
        }
        catch (GDMException e) {
        }
    }

    /**
     * Send a mail with a null message
     */
    public void testWithNullMessage() {
        mailService = ServiceLocator.instance().getMailService();

        // Send mail
        MailData lMailData =
                new MailData(MAIL_FROM, MAIL_TO, MAIL_REPLY_TO, null,
                        MAIL_SUBJECT, new MailDataFile[0]);

        try {
            mailService.sendMail(lMailData);
            fail("The mail should not have been sent with null message.");
        }
        catch (GDMException e) {
        }

    }

    /**
     * Send a mail with a null sender
     */
    public void testWithNullSender() {
        mailService = ServiceLocator.instance().getMailService();

        // Send mail
        MailData lMailData =
                new MailData(null, MAIL_TO, null, MAIL_MESSAGE, MAIL_SUBJECT,
                        new MailDataFile[0]);

        // The mail should be send without problem

        try {
            mailService.sendMail(lMailData);
        }
        catch (Exception e) {
            fail(e);
        }
    }

    /**
     * Send a mail to a null receiver.
     */
    public void testWithNullReceiver() {
        mailService = ServiceLocator.instance().getMailService();

        // Send mail
        MailData lMailData =
                new MailData(MAIL_FROM, null, MAIL_REPLY_TO, MAIL_MESSAGE,
                        MAIL_SUBJECT, new MailDataFile[0]);

        try {
            mailService.sendMail(lMailData);
            fail("The mail should not have been sent with null receiver.");
        }
        catch (GDMException e) {
        }
    }

    /**
     * Create a mail with attached file and send it.
     */
    public void testWithAttachedFile() {
        mailService = ServiceLocator.instance().getMailService();

        // Send mail
        try {
            InputStream lIs =
                    this.getClass().getClassLoader().getResourceAsStream(
                            "attachedFile.jpg");
            byte[] lByteArray = IOUtils.toByteArray(lIs);

            MailDataFile[] lMailFiles = new MailDataFile[1];

            lMailFiles[0] = new MailDataFile();
            lMailFiles[0].setAttachedFile(lByteArray);
            lMailFiles[0].setMimetype("image/jpeg");
            lMailFiles[0].setName("attachedFile.jpg");

            MailData lMailData =
                    new MailData(MAIL_FROM, MAIL_TO, MAIL_REPLY_TO,
                            MAIL_MESSAGE, MAIL_SUBJECT, lMailFiles);

            mailService.sendMail(lMailData);
        }
        catch (IOException e) {
            fail(e);
        }
        catch (GDMException e) {
            fail(e);
        }
    }

    /**
     * Create a mail with an incorrect attached file
     */
    public void testWithIncorrectAttachedFile() {
        mailService = ServiceLocator.instance().getMailService();

        // Send mail
        try {
            InputStream lIs =
                    this.getClass().getClassLoader().getResourceAsStream(
                            "attachedFile.jpg");
            byte[] lByteArray = IOUtils.toByteArray(lIs);

            MailDataFile[] lMailFiles = new MailDataFile[1];

            // Create a Mail Data file without name
            lMailFiles[0] = new MailDataFile(null, lByteArray, "image/jpeg");

            MailData lMailData =
                    new MailData(MAIL_FROM, MAIL_TO, MAIL_REPLY_TO,
                            MAIL_MESSAGE, MAIL_SUBJECT, lMailFiles);

            mailService.sendMail(lMailData);
            fail("The exception has not been thrown.");

        }
        catch (IOException ex) {
            fail("A IOException has been thrown." + ex.getMessage());
        }
        catch (GDMException lGDMException) {
            // ok
        }
        catch (Throwable e) {

            fail("The exception thrown is not a GDMException.");
        }
    }

    public void testWithIncorrectMailAddress() {
        // Gets the mail service.
        mailService = ServiceLocator.instance().getMailService();

        // Send mail
        String[] lIncorrectAddresses = { "test@" };
        MailData lMailData =
                new MailData(MAIL_FROM, lIncorrectAddresses, MAIL_REPLY_TO,
                        MAIL_MESSAGE, MAIL_SUBJECT, new MailDataFile[0]);

        try {
            mailService.sendMail(lMailData);
            fail("The exception has not been thrown.");
        }
        catch (GDMException e) {
            // ok
        }
        catch (Throwable e) {

            fail("The exception thrown is not a GDMException.");
        }
    }
}
