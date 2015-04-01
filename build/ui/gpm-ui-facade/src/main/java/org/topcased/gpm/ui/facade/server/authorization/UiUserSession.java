/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.server.authorization;

import java.util.HashMap;
import java.util.Map;

/**
 * UiUserSession represents a gPM user that has been connected.
 * <p>
 * login and token have been set at the creation. processName later.
 * </p>
 * <p>
 * A user can have many session on gPM (one session per product). (
 * {@link UiSession})
 * </p>
 * 
 * @author mkargbo
 */
public class UiUserSession {
    private String applicationUrl;

    private UiSession defaultGlobalSession;

    private final Map<String, UiSession> defaultProductSession;

    private String language;

    private String login;

    private String processName;

    private final Map<String, UiSession> session;

    private Boolean keepTemporaryFiles;

    private final Map<String, byte[]> temporaryUploadedFile;

    private String token;

    /**
     * Default constructor
     */
    public UiUserSession() {
        session = new HashMap<String, UiSession>();
        defaultProductSession = new HashMap<String, UiSession>();
        temporaryUploadedFile = new HashMap<String, byte[]>();
        keepTemporaryFiles = false;
    }

    /**
     * Add a new user's default session for the specified product.
     * <p>
     * Replace the previous existed session for this product.
     * </p>
     * 
     * @param pProductName
     *            Product's name of the session
     * @param pSession
     *            User's session
     */
    public void addDefaultSession(String pProductName, UiSession pSession) {
        pSession.setParent(this);
        defaultProductSession.put(pProductName, pSession);
    }

    /**
     * Add a new user's session for the specified product.
     * <p>
     * Replace the previous existed session for this product.
     * </p>
     * 
     * @param pProductName
     *            Product's name of the session
     * @param pSession
     *            User's session
     */
    public void addSession(String pProductName, UiSession pSession) {
        pSession.setParent(this);
        session.put(pProductName, pSession);
    }

    /**
     * Store a temporary uploaded file.
     * 
     * @param pFileName
     *            The file name.
     * @param pFileContent
     *            The file content.
     */
    public void addTemporaryUploadedFile(final String pFileName,
            final byte[] pFileContent) {
        temporaryUploadedFile.put(getShortUploadedFileName(pFileName),
                pFileContent);
    }

    /**
     * Clear all temporary uploaded files.
     */
    public void clearTemporaryUploadedFile() {
        temporaryUploadedFile.clear();
    }

    /**
     * get applicationUrl
     * 
     * @return the applicationUrl
     */
    public String getApplicationUrl() {
        return applicationUrl;
    }

    /**
     * get default global session
     * 
     * @return the default global session
     */
    public UiSession getDefaultGlobalSession() {
        return defaultGlobalSession;
    }

    /**
     * Retrieve a default session per product's name.
     * 
     * @param pProductName
     *            Name of the product for the default session.
     * @return Session if exists, null otherwise.
     */
    public UiSession getDefaultSession(String pProductName) {
        return defaultProductSession.get(pProductName);
    }

    /**
     * get language
     * 
     * @return the language
     */
    public String getLanguage() {
        return language;
    }

    public String getLogin() {
        return login;
    }

    public String getProcessName() {
        return processName;
    }

    /**
     * Retrieve a session per product's name.
     * 
     * @param pProductName
     *            Name of the product for the session.
     * @return Session if exists, null otherwise.
     */
    public UiSession getSession(String pProductName) {
        return session.get(pProductName);
    }

    /**
     * Get a temporary uploaded file.
     * 
     * @param pFileName
     *            The file name.
     * @return The file content.
     */
    public byte[] getTemporaryUploadedFile(final String pFileName) {
        return temporaryUploadedFile.get(getShortUploadedFileName(pFileName));
    }

    public String getToken() {
        return token;
    }

    /**
     * Remove a default session
     * 
     * @param pProductName
     *            Name of the product for the default session.
     * @return The deleted session
     */
    public UiSession removeDefaultSession(String pProductName) {
        return defaultProductSession.remove(pProductName);
    }

    /**
     * Remove a session
     * 
     * @param pProductName
     *            Name of the product for the session.
     * @return The deleted session
     */
    public UiSession removeSession(String pProductName) {
        return session.remove(pProductName);
    }

    /**
     * set applicationUrl
     * 
     * @param pApplicationUrl
     *            the applicationUrl to set
     */
    public void setApplicationUrl(String pApplicationUrl) {
        applicationUrl = pApplicationUrl;
    }

    /**
     * set default global session
     * 
     * @param pDefaultGlobalSession
     *            the default global session to set
     */
    public void setDefaultGlobalSession(UiSession pDefaultGlobalSession) {
        defaultGlobalSession = pDefaultGlobalSession;
    }

    /**
     * set language
     * 
     * @param pLanguage
     *            the language to set
     */
    public void setLanguage(String pLanguage) {
        language = pLanguage;
    }

    public void setLogin(String pLogin) {
        login = pLogin;
    }

    public void setProcessName(String pProcessName) {
        processName = pProcessName;
    }

    public void setToken(String pToken) {
        token = pToken;
    }

    public Boolean getKeepTemporaryFiles() {
        return keepTemporaryFiles;
    }

    public void setKeepTemporaryFiles(Boolean pKeepTemporaryFiles) {
        this.keepTemporaryFiles = pKeepTemporaryFiles;
    }

    public Map<String, UiSession> getSession() {
        return session;
    }

    /**
     * get the short file name, without path.
     * 
     * @param pFileName
     *            The file name
     * @return the file name without path
     */
    private String getShortUploadedFileName(final String pFileName) {
        String lFileName = pFileName;
        int lIndex =
                Math.max(pFileName.lastIndexOf("/"),
                        pFileName.lastIndexOf("\\"));
        if (lIndex >= 0) {
            lFileName = pFileName.substring(lIndex + 1, pFileName.length());
        }
        return lFileName;
    }

}
