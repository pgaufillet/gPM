/***************************************************************
 * Copyright (c) 2011 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Pierre Hubert TSAAN (Atos)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.util;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.ui.HTML;

/**
 * GpmAppletBuilder.
 * <p>
 * Build an Applet tag
 * </p>
 * 
 * @author phtsaan
 */
public class GpmAppletBuilder {

    public static enum AppletHTMLTag {
        APPLET_OPEN_CLAUSE("<applet"), CLOSE_TAG(">"), APPLET_END_CLAUSE(
                "</applet>"), APPLET_CODE("code="), APPLET_CODE_BASE(
                "codebase="), APPLET_ARCHIVE("archive="), APPLET_ALTER("alt="), APPLET_NAME(
                "name="), APPLET_MYSCRIPT("myscript="), PARAM_OPEN_CLAUSE(
                "<param"), PARAM_NANE("name="), PARAM_VALUE("value="), BLANK(
                " "), SHEET_ID("sheetId");
        private final String lValue;

        AppletHTMLTag(String pValue) {
            this.lValue = pValue;
        }

        public String toString() {
            return lValue;
        }

        public String getValue() {
            return this.lValue;

        }
    };

    private static final int INIT_SIZE = 100;

    /**
     * Applet Name
     */
    private String appletName;

    /**
     * Applet alternatif message
     */
    private String appleAlter;

    /**
     * Applet archive
     */
    private String appleArchive;

    /**
     * Applet code
     */
    private String appletCode;

    /**
     * Applet code base
     */
    private String appletCodeBase;

    /**
     * Applet Parameters Map<paramName, ParamValue>
     */
    private Map<String, String> listParamMap = new HashMap<String, String>();

    /**
     * constructor
     * 
     * @param pAppletName
     * @param pAppleAlter
     * @param pAppleArchive
     * @param pAppletCode
     * @param pAppletCodeBase
     * @param pListParamMap
     */
    public GpmAppletBuilder(String pAppletName, String pAppleAlter,
            String pAppleArchive, String pAppletCode, String pAppletCodeBase,
            Map<String, String> pListParamMap) {
        super();
        this.appletName = pAppletName;
        this.appleAlter = pAppleAlter;
        this.appleArchive = pAppleArchive;
        this.appletCode = pAppletCode;
        this.appletCodeBase = pAppletCodeBase;
        this.listParamMap = pListParamMap;
    }

    /**
     * constructor
     * 
     * @param pAppletName
     * @param pAppleAlter
     * @param pAppleArchive
     * @param pAppletCode
     * @param pAppletCodeBase
     */
    public GpmAppletBuilder(String pAppletName, String pAppleAlter,
            String pAppleArchive, String pAppletCode, String pAppletCodeBase) {
        super();
        this.appletName = pAppletName;
        this.appleAlter = pAppleAlter;
        this.appleArchive = pAppleArchive;
        this.appletCode = pAppletCode;
        this.appletCodeBase = pAppletCodeBase;
    }

    public GpmAppletBuilder() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * get applet byte code
     * 
     * @return appletTag
     */
    public String getAppletCode() {
        return appletCode;
    }

    /**
     * set the applet code base
     * 
     * @param pAppletCode
     */
    public void setAppletCode(String pAppletCode) {
        this.appletCode = pAppletCode;
    }

    /**
     * get Applet parameters Map
     * 
     * @return listParamMap
     */
    public Map<String, String> getListParamMap() {
        return listParamMap;
    }

    /**
     * set the Applet parameter map
     * 
     * @param pListParamMap
     */
    public void setListParamMap(Map<String, String> pListParamMap) {
        this.listParamMap = pListParamMap;
    }

    /**
     * Build the Applet HTML tag and return it as an encoded string.
     * 
     * @return The encoded HTML string.
     */
    public StringBuilder buildAppletHTMLString() {

        // build parameter tags
        final StringBuilder lParam = paramsBuilder(listParamMap);

        // build attribute
        final StringBuilder lAttribut =
                getAppletAttribute(appletName, appleAlter, appleArchive,
                        appletCode, appletCodeBase);

        // build HTML TAG
        final StringBuilder lAppletHTMLTag = new StringBuilder(INIT_SIZE * 2);

        lAppletHTMLTag.append(lAttribut.toString());
        lAppletHTMLTag.append(lParam.toString());
        lAppletHTMLTag.append(AppletHTMLTag.APPLET_END_CLAUSE.getValue());
        return lAppletHTMLTag;
    }

    private StringBuilder paramsBuilder(Map<String, String> pParamList) {
        // Buffer to handle String
        StringBuilder lParamString = new StringBuilder(INIT_SIZE);

        if (pParamList != null) {
            for (Map.Entry<String, String> lEntry : pParamList.entrySet()) {
                lParamString.append(AppletHTMLTag.PARAM_OPEN_CLAUSE.getValue());
                lParamString.append(AppletHTMLTag.BLANK.getValue());

                lParamString.append(AppletHTMLTag.PARAM_NANE.getValue());
                lParamString.append(lEntry.getKey());
                lParamString.append(AppletHTMLTag.BLANK.getValue());

                lParamString.append(AppletHTMLTag.PARAM_VALUE.getValue());
                lParamString.append(lEntry.getValue());
                lParamString.append(AppletHTMLTag.BLANK.getValue());

                lParamString.append(AppletHTMLTag.CLOSE_TAG.getValue());
            }
        }
        return lParamString;
    }

    private StringBuilder getAppletAttribute(String pAppletName,
            String pAppleAlter, String pAppleArchive, String pAppletCode,
            String pAppletCodeBase) {
        StringBuilder lAttr = new StringBuilder(INIT_SIZE);
        lAttr.append(AppletHTMLTag.APPLET_OPEN_CLAUSE.getValue());
        lAttr.append(AppletHTMLTag.BLANK.getValue());

        lAttr.append(AppletHTMLTag.APPLET_MYSCRIPT.getValue());
        lAttr.append(true);
        lAttr.append(AppletHTMLTag.BLANK.getValue());

        lAttr.append(AppletHTMLTag.APPLET_CODE.getValue());
        lAttr.append(pAppletCode);
        lAttr.append(AppletHTMLTag.BLANK.getValue());

        lAttr.append(AppletHTMLTag.APPLET_NAME.getValue());
        lAttr.append(pAppletName);
        lAttr.append(AppletHTMLTag.BLANK.getValue());

        if (pAppletCodeBase != null && !pAppletCodeBase.equals("")) {
            lAttr.append(AppletHTMLTag.APPLET_CODE_BASE.getValue());
            lAttr.append(pAppletCodeBase);
            lAttr.append(AppletHTMLTag.BLANK.getValue());
        }

        if (pAppleAlter != null) {
            lAttr.append(AppletHTMLTag.APPLET_ALTER.getValue());
            lAttr.append(pAppleAlter);
            lAttr.append(AppletHTMLTag.BLANK.getValue());
        }

        if (pAppleArchive != null) {
            lAttr.append(AppletHTMLTag.APPLET_ARCHIVE.getValue());
            lAttr.append(pAppleArchive);
            lAttr.append(AppletHTMLTag.BLANK.getValue());
        }
        lAttr.append(AppletHTMLTag.CLOSE_TAG.getValue());
        return lAttr;
    }

    public String getAppletName() {
        return appletName;
    }

    public void setAppletName(String pAppletName) {
        this.appletName = pAppletName;
    }

    public String getAppleAlter() {
        return appleAlter;
    }

    public void setAppleAlter(String pAppleAlter) {
        this.appleAlter = pAppleAlter;
    }

    public String getAppleArchive() {
        return appleArchive;
    }

    public void setAppleArchive(String pAppleArchive) {
        this.appleArchive = pAppleArchive;
    }

    public String getAppletCodeBase() {
        return appletCodeBase;
    }

    public void setAppletCodeBase(String pAppletCodeBase) {
        this.appletCodeBase = pAppletCodeBase;
    }

    public HTML getHTML() {
        return new HTML(this.buildAppletHTMLString().toString());
    }
}
