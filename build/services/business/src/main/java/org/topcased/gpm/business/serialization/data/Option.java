/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Sophian Idjellidaine (Atos Origin),
 * Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.serialization.data;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Stores gPM global attributes.
 * 
 * @author ahaugommard
 */
@XStreamAlias("option")
public class Option {

    /** The type of authentication to gPM application (internal/external). */
    private String authentication;

    /**
     * The name of the parameter for user ID given by the external
     * authentication system.
     */
    private String userIdParamName;

    /** The url for button "contact" displayed in GUI. */
    private String contactUrl;

    /**
     * The maximum depth of the usable field for the filter criteria and summary
     * fields.
     */
    private String filterFieldsMaxDepth;

    /**
     * The maximum exportable sheets
     */
    private String maxExportableSheets;

    /**
     * The help content url
     */
    private String helpContentUrl;

    /**
     * The autolocking attribute.
     */
    private String autolocking;

    /**
     * The login case sensitive
     */
    private String loginCaseSensitive;

    /**
     * The sql function of case sensitive
     */
    private String sqlFunctionCaseSensitive;

    /**
     * Gets the authentication.
     * 
     * @return the authentication
     */
    public String getAuthentication() {
        return authentication;
    }

    /**
     * Gets the user id param name.
     * 
     * @return the user id param name
     */
    public String getUserIdParamName() {
        return userIdParamName;
    }

    /**
     * Gets the contact url.
     * 
     * @return the contact url
     */
    public String getContactUrl() {
        return contactUrl;
    }

    /**
     * Gets the maximum depth of the usable field for the filter criteria and
     * summary fields.
     * 
     * @return the maximum depth of the usable field for the filter criteria and
     *         summary fields.
     */
    public String getFilterFieldsMaxDepth() {
        return filterFieldsMaxDepth;
    }

    /**
     * Get the maximum exportable sheets
     * 
     * @return the maximum exportable sheets
     */
    public String getMaxExportableSheets() {
        return maxExportableSheets;
    }

    /**
     * get helpContentUrl
     * 
     * @return the helpContentUrl
     */
    public String getHelpContentUrl() {
        return helpContentUrl;
    }

    public String getAutolocking() {
        return autolocking;
    }

    /**
     * get loginCaseSensitive
     * 
     * @return the loginCaseSensitive
     */
    public String getLoginCaseSensitive() {
        return loginCaseSensitive;
    }

    /**
     * get sqlFunctionCaseSensitive
     * 
     * @return the sqlFunctionCaseSensitive
     */
    public String getSqlFunctionCaseSensitive() {
        return sqlFunctionCaseSensitive;
    }
}
