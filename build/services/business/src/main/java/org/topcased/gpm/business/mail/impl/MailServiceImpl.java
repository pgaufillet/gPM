/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Szadel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.mail.impl;

import java.util.Date;

import javax.activation.DataHandler;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.lang.StringUtils;
//import org.apache.log4j.Logger;
import org.springframework.mail.javamail.JavaMailSender;
import org.topcased.gpm.business.ServiceImplBase;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.exception.MailException;
import org.topcased.gpm.business.mail.MailData;
import org.topcased.gpm.business.mail.MailDataFile;
import org.topcased.gpm.business.mail.service.MailDataFileSource;
import org.topcased.gpm.business.mail.service.MailService;

/**
 * The implementation of MailService.
 * 
 * @author tszadel
 */
public class MailServiceImpl extends ServiceImplBase implements MailService {
    /** The logger. */
//    private static final Logger LOGGER =
//            Logger.getLogger(MailServiceImpl.class);

    /** Mail sender */
    private JavaMailSender mailSender;

    private String mailFrom;

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.mail.service.
     *      MailService#sendMail(org.topcased.gpm.business.mail.MailData)
     */
    public void sendMail(MailData pData) throws GDMException {
        sendMail(pData, true);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.mail.service.MailService#sendMail(MailData,
     *      boolean)
     */
    @Override
    public void sendMail(MailData pData, boolean pThrowException)
        throws GDMException {
        try {
            doSendMail(pData);
        }
        catch (Throwable e) {
            if (pThrowException) {
                throw new GDMException("Cannot send mail: "
                        + e.getLocalizedMessage(), e);
            }
            else {
                logError(e);
            }
        }

    }

    private void doSendMail(MailData pData) throws Throwable {
//        if (LOGGER.isDebugEnabled()) {
//            LOGGER.debug("Verifying maildata");
//        }
        if (StringUtils.isBlank(pData.getSubject())) {
            throw new MailException("Subject cannot be null or empty.");
        }
        if (StringUtils.isBlank(pData.getMessage())) {
            throw new MailException("Message cannot be null or empty.");
        }
        if (pData.getTo() == null || pData.getTo().length == 0) {
            throw new MailException("No recipient specified.");
        }

        MimeMessage lMessage = mailSender.createMimeMessage();

        InternetAddress[] lAddress = new InternetAddress[pData.getTo().length];
        int i = 0;
        for (String lAddrString : pData.getTo()) {
            lAddress[i++] = new InternetAddress(lAddrString);
        }
        lMessage.setRecipients(Message.RecipientType.TO, lAddress);

        // If ReplyTo has been set
        if (pData.getReplyTo() != null && pData.getReplyTo().length != 0) {
            lAddress = new InternetAddress[pData.getReplyTo().length];
            i = 0;
            for (String lAddrString : pData.getReplyTo()) {
                lAddress[i++] = new InternetAddress(lAddrString);
            }
            lMessage.setReplyTo(lAddress);
        }

        lMessage.setSubject(pData.getSubject());

        if (pData.getMailDataFiles() == null
                || pData.getMailDataFiles().length == 0) {
            // Sending simple mail
//            if (LOGGER.isDebugEnabled()) {
//                LOGGER.debug("Sending simple mail");
//            }
            lMessage.setText(pData.getMessage());
        }
        else {
//            if (LOGGER.isDebugEnabled()) {
//                LOGGER.debug("Sending mail with attachments");
//            }
            // create the Multipart and add its parts to it
            Multipart lMultiPart = new MimeMultipart();
            // create and fill the first message part
            MimeBodyPart lTextPart = new MimeBodyPart();
            lTextPart.setText(pData.getMessage());
            lMultiPart.addBodyPart(lTextPart);

            // create the attached message part
            if (pData.getMailDataFiles() != null) {
                for (MailDataFile lAttachedFile : pData.getMailDataFiles()) {
                    MimeBodyPart lMimeBodyPart = new MimeBodyPart();
                    // attach the file to the message
                    MailDataFileSource lMailDataFileSource =
                            new MailDataFileSource(lAttachedFile);
                    lMimeBodyPart.setDataHandler(new DataHandler(
                            lMailDataFileSource));
                    if (lAttachedFile.getName() == null
                            || lAttachedFile.getName().trim().equals("")) {
                        throw new MailException(
                                "Attached file should have a name.");
                    }
                    lMimeBodyPart.setFileName(lAttachedFile.getName());
                    lMultiPart.addBodyPart(lMimeBodyPart);
                }
            }

            // add the Multipart to the message
            lMessage.setContent(lMultiPart);
        }

        // set the Date: header
        lMessage.setSentDate(new Date());

        // If sender has not been set the default mail sender 
        // from configuration file is used.
        if (pData.getFrom() == null) {
            pData.setFrom(mailFrom);
        }

        InternetAddress lAddressSender = new InternetAddress(pData.getFrom());

        lMessage.setSender(lAddressSender);
        lMessage.setFrom(lAddressSender);

        mailSender.send(lMessage);
    }

    private void logError(Throwable pE) {
        StringBuffer lStackTrace = new StringBuffer();
        for (StackTraceElement lSTE : pE.getStackTrace()) {
            lStackTrace.append(lSTE.toString());
            lStackTrace.append("\n");
        }
//        LOGGER.error("Cannot send mail: " + pE.getLocalizedMessage() + "\n" + lStackTrace);
    }

    /**
     * Sets the Mail Sender.
     * 
     * @param pMailSender
     *            The mail sender.
     */
    public void setMailSender(JavaMailSender pMailSender) {
        mailSender = pMailSender;
    }

    /**
     * Sets the Mail From.
     * 
     * @param pMailFrom
     *            The mail from.
     */
    public void setMailFrom(String pMailFrom) {
        mailFrom = pMailFrom;
    }
}
