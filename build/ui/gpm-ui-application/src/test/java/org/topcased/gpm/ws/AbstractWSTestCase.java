/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Olivier Gehin (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ws;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.topcased.gpm.ws.v2.client.GDMException_Exception;
import org.topcased.gpm.ws.v2.client.WebServicesStub;

/**
 * Abstract class for all webservice tests. It initializes a new db and logs the
 * user.
 * 
 * @author ogehin
 */
public class AbstractWSTestCase extends TestCase {
    /** Logger. */
    private static final Logger LOGGER =
            Logger.getLogger(AbstractWSTestCase.class);

    private static WsProperties staticWsProperties;

    /** The default login with its password. */
    protected static final String[] DEFAULT_LOGIN = { "admin", "admin" };

    /** The default role. */
    protected static final String DEFAULT_ROLE = "admin";

    /** The default type product name. */
    private static final String DEFAULT_PRODUCT_TYPE_NAME = "store";

    /** The default product name. */
    protected static final String DEFAULT_PRODUCT_NAME = "Bernard's store";

    /** The default business process name. */
    protected static final String DEFAULT_PROCESS_NAME = "PET STORE";

    /** Login parameters to use for the 'user2' user. */
    protected static final String[] USER2_LOGIN = { "user2", "pwd2" };

    /** The 'normal' role name. */
    private static final String USER2_ROLE_NAME = "notadmin";

    /** The service stub instance. */
    protected static WebServicesStub staticServices;

    /** The user token. */
    protected String userToken;

    /** The Role session token. */
    protected String roleToken;

    /** The secondary user token. */
    protected String secondaryUserToken;

    /** The secondary Role session token. */
    protected String secondaryRoleToken;

    /**
     * Default constructor.
     */
    public AbstractWSTestCase() {
        super();
        if (staticWsProperties == null) {
            try {
                staticWsProperties = new WsProperties();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Custom setup method.
     * 
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() {
        if (staticServices == null) {
            String lUrl =
                    new String(staticWsProperties.getUrl() + "/"
                            + staticWsProperties.getServletName() + "/v2?wsdl");
            try {
                staticServices = new WebServicesStub(lUrl);
            }
            catch (Exception e) { //RuntimeException
                e.printStackTrace();
            }
        }

        String[] lLogin = getLogin();
        String[] lSecondaryLogin = getLogin();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("\tDefault Login : " + lLogin[0]);
            LOGGER.debug("\tDefault Password : " + lLogin[1]);
            LOGGER.debug("\tDefault Role : " + getRole());
            LOGGER.debug("\tDefault Product Name : " + getProductName());
            LOGGER.debug("\tDefault Process Name : " + getProcessName());
            LOGGER.debug("\tUser Login : " + lSecondaryLogin[0]);
            LOGGER.debug("\tUser Password : " + lSecondaryLogin[1]);
            LOGGER.debug("\tUser Role : " + getSecondaryRole());
        }

        try {
            userToken = staticServices.login(lLogin[0], lLogin[1]);
            roleToken =
                    staticServices.selectRole(userToken, getRole(),
                            getProductName(), getProcessName());
            secondaryUserToken = staticServices.login(lLogin[0], lLogin[1]);
            secondaryRoleToken =
                    staticServices.selectRole(userToken, getRole(),
                            getProductName(), getProcessName());
        }
        catch (GDMException_Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Returns the login that should be used for logging.
     * 
     * @return The login and its password.
     */
    protected String[] getLogin() {
        return DEFAULT_LOGIN;
    }

    /**
     * Returns the secondary login that should be used for logging.
     * 
     * @return The secondary login and its password.
     */
    protected String[] getSecondaryLogin() {
        return USER2_LOGIN;
    }

    /**
     * Returns the role of the user.
     * 
     * @return The role of the user.
     */
    protected String getRole() {
        return DEFAULT_ROLE;
    }

    /**
     * Returns the role of the secondary user.
     * 
     * @return The role of the secondary user.
     */
    protected String getSecondaryRole() {
        return USER2_ROLE_NAME;
    }

    /**
     * Returns the name of the product type.
     * 
     * @return The name of the product type.
     */
    public static String getProductTypeName() {
        return DEFAULT_PRODUCT_TYPE_NAME;
    }

    /**
     * Returns the name of the product.
     * 
     * @return The name of the product.
     */
    public static String getProductName() {
        return DEFAULT_PRODUCT_NAME;
    }

    /**
     * Returns the name of the process.
     * 
     * @return The name of the process.
     */
    public static String getProcessName() {
        return DEFAULT_PROCESS_NAME;
    }

    /**
     * Generate a new List containing an element or add an element to an
     * existing list.
     * 
     * @param pElement
     *            the element to add
     * @param pList
     *            the initial list
     * @return The list containing a new element.
     */
    public List<String> generateList(String pElement, List<String> pList) {
        if (pList == null) {
            List<String> lList = new ArrayList<String>();
            lList.add(pElement);
            return lList;
        }
        pList.add(pElement);
        return pList;
    }
}
