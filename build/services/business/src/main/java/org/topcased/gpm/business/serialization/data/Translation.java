/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Sophian Idjellidaine (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.serialization.data;

import java.util.LinkedList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * The Class Translation.
 * 
 * @author llatil
 */
@XStreamAlias("translation")
public class Translation {

    /** The lang. */
    @XStreamAsAttribute
    private String lang;

    /** The messages. */
    @XStreamImplicit(itemFieldName = "message")
    private List<Message> messages = new LinkedList<Message>();

    /** The images. */
    @XStreamImplicit(itemFieldName = "image")
    private List<Message> images = new LinkedList<Message>();

    /**
     * Gets the language.
     * 
     * @return the language
     */
    public String getLang() {
        return lang;
    }

    /**
     * Sets the language.
     * 
     * @param pLang
     *            the language
     */
    public void setLang(String pLang) {
        this.lang = pLang;
    }

    /**
     * Gets the messages.
     * 
     * @return the messages
     */
    public List<Message> getMessages() {
        return messages;
    }

    /**
     * Sets the messages.
     * 
     * @param pMessages
     *            the messages
     */
    public void setMessages(List<Message> pMessages) {
        messages = pMessages;
    }

    /**
     * Gets the images.
     * 
     * @return the images
     */
    public List<Message> getImages() {
        return images;
    }

    /**
     * Sets the images.
     * 
     * @param pImages
     *            the new images
     */
    public void setImages(List<Message> pImages) {
        images = pImages;
    }
}
