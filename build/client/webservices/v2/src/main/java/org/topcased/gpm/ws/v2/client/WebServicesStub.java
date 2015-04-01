/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Olivier Gehin (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ws.v2.client;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.jws.WebResult;

/**
 * Service Stub Interface.
 * 
 * @author ogehin
 */
public class WebServicesStub {

    /** The service stub instance. */
    protected org.topcased.gpm.ws.v2.client.Services services;

    /**
     * Constructor of the web service stub.
     * <p>
     * This constructor is used to create an instance of a client stub, that is
     * then used to call the gPM remote API. The client stub uses an URL
     * parameter, passed in this constructor, which must be WSDL file address.
     * <p>
     * This URL has the following format: <br>
     * <i>&lt;server_adress&gt;[:&lt;server_port&gt;]/&lt;application_name&gt;/
     * services/v2?wsdl</i>
     * <p>
     * For example, for an application deployed on localhost, listening on 8080
     * port, with default application name: <br>
     * <i>http://localhost:8080/gPM-application/services/v2?wsdl</i>
     * 
     * @param pUrl
     *            URL of the WSDL file.
     * @throws RuntimeException
     *             if the URL string is malformed.
     */
    public WebServicesStub(String pUrl) throws RuntimeException {
        try {
            URL lServicesUrl = new URL(pUrl);
            ServicesImplService lServicesImplService =
                    new ServicesImplService(lServicesUrl);
            services = lServicesImplService.getServicesImplPort();
        }
        catch (MalformedURLException e) {
            throw new RuntimeException("The url parameter is malformed. "
                    + e.getCause());
        }
    }

    /**
     * Constructor of the web service stub.
     * <p>
     * This ctor use an URL object to specify the WSDL file address.
     * 
     * @param pUrl
     *            URL of the WSDL file.
     */
    public WebServicesStub(URL pUrl) {
        ServicesImplService lServicesImplService =
                new ServicesImplService(pUrl);
        services = lServicesImplService.getServicesImplPort();
    }

    /**
     * Login.
     * 
     * @param pUserName
     *            username
     * @param pPassword
     *            password
     * @return User token
     * @throws GDMException_Exception
     *             internal exception.
     */
    public String login(String pUserName, String pPassword)
        throws GDMException_Exception {
        return services.login(pUserName, pPassword);
    }

    /**
     * Logout.
     * 
     * @param pUserToken
     *            userToken.
     * @throws GDMException_Exception
     *             internal exception.
     */
    public void logout(String pUserToken) throws GDMException_Exception {
        services.logout(pUserToken);
    }

    /**
     * Get the list of available business processes for the user
     * 
     * @param pUserToken
     *            Token identifying the user session
     * @return Array of business processes
     * @throws GDMException_Exception
     *             internal exception.
     */
    public List<String> getBusinessProcessNames(String pUserToken)
        throws GDMException_Exception {
        return services.getBusinessProcessNames(pUserToken);
    }

    /**
     * Get the list of product names for a given business process. This list
     * contains only products where the user has a role.
     * 
     * @param pUserToken
     *            Token identifying the user session
     * @param pBusinessProcess
     *            Business process the returned products belong to.
     * @return List of product names.
     * @throws GDMException_Exception
     *             internal exception.
     */
    public List<String> getProductNames(String pUserToken,
            String pBusinessProcess) throws GDMException_Exception {
        return services.getProductNames(pUserToken, pBusinessProcess);
    }

    /**
     * Get the list of products available for a given business process. This
     * list contains only products where given user has a role.
     * 
     * @param pUserToken
     *            Token identifying the user session
     * @param pBusinessProcess
     *            Business process the returned products belong to.
     * @return List of product data.
     * @throws GDMException_Exception
     *             internal exception.
     */
    public List<Product> getProducts(String pUserToken, String pBusinessProcess)
        throws GDMException_Exception {
        return services.getProducts(pUserToken, pBusinessProcess);
    }

    /**
     * Get a product from its name and its business process.
     * 
     * @param pRoleToken
     *            Token identifying the role session.
     * @param pBusinessProcess
     *            Business process the returned product belong to.
     * @param pProductName
     *            The name of the product
     * @return The product data.
     * @throws GDMException_Exception
     *             Internal exception.
     */
    public Product getProduct(String pRoleToken, String pBusinessProcess,
            String pProductName) throws GDMException_Exception {
        return services.getProduct(pRoleToken, pBusinessProcess, pProductName);
    }

    /**
     * Get a product from its id.
     * 
     * @param pRoleToken
     *            Token identifying the role session.
     * @param pProductId
     *            The product id.
     * @return The product data.
     * @throws GDMException_Exception
     *             Internal exception.
     */
    public Product getProductByKey(String pRoleToken, String pProductId)
        throws GDMException_Exception {
        return services.getProductByKey(pRoleToken, pProductId);
    }

    /**
     * Get the list of user role names allowed for a given login
     * 
     * @param pUserToken
     *            Authorization token.
     * @param pBusinessProcessName
     *            Name of business process
     * @param pProductName
     *            Name of the product.
     * @return List of allowed role names.
     * @throws GDMException_Exception
     *             internal exception.
     */
    public List<String> getRolesNames(String pUserToken,
            String pBusinessProcessName, String pProductName)
        throws GDMException_Exception {
        return services.getRolesNames(pUserToken, pBusinessProcessName,
                pProductName);
    }

    /**
     * Get the user role name from the session token
     * 
     * @param pRoleToken
     *            Session token.
     * @return User role name.
     * @throws GDMException_Exception
     *             internal exception.
     */
    public String getRoleName(String pRoleToken) throws GDMException_Exception {
        return services.getRoleName(pRoleToken);
    }

    /**
     * Select role
     * 
     * @param pUserToken
     *            Token identifying the user session
     * @param pRoleName
     *            Role name
     * @param pProductName
     *            Name of product
     * @param pBusinessProcessName
     *            Name of business process
     * @return Role session token.
     * @throws GDMException_Exception
     *             internal exception.
     */
    public String selectRole(String pUserToken, String pRoleName,
            String pProductName, String pBusinessProcessName)
        throws GDMException_Exception {
        return services.selectRole(pUserToken, pRoleName, pProductName,
                pBusinessProcessName);
    }

    /**
     * Get the language to use in internationalization for a user.
     * 
     * @param pToken
     *            Role or user session token
     * @return Language to use
     * @throws GDMException_Exception
     *             internal exception.
     */
    public String getUserLanguage(String pToken) throws GDMException_Exception {
        return services.getUserLanguage(pToken);
    }

    /**
     * Get information on a user from this roleToken. User role list is filled
     * with the only current role.
     * 
     * @param pRoleToken
     *            roleToken
     * @return User object.
     * @throws GDMException_Exception
     *             internal exception.
     */
    public User getUserFromRole(String pRoleToken)
        throws GDMException_Exception {
        return services.getUserFromRole(pRoleToken);
    }

    /**
     * Get values in the user preferred language If a value isn't found in this
     * language, then the default language is used.
     * 
     * @param pToken
     *            User or role session token.
     * @param pKeys
     *            values keys array.
     * @return translated values array.
     * @throws GDMException_Exception
     *             internal exception.
     */
    public List<String> getValuesForUser(String pToken, List<String> pKeys)
        throws GDMException_Exception {
        return services.getValuesForUser(pToken, pKeys);
    }

    /**
     * Getter for sheet types.
     * 
     * @param pRoleToken
     *            roleToken.
     * @param pProcessName
     *            Name of business process.
     * @return SheetType array.
     * @throws GDMException_Exception
     *             internal exception.
     */
    public List<SheetType> getSheetTypes(String pRoleToken, String pProcessName)
        throws GDMException_Exception {
        return services.getSheetTypes(pRoleToken, pProcessName);
    }

    /**
     * Get information on a set of sheet types.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pKeys
     *            List of technical identifiers of sheet types
     * @return List of sheet type infos
     * @throws GDMException_Exception
     *             internal exception.
     */
    public List<SheetType> getSheetTypesByKeys(String pRoleToken,
            List<String> pKeys) throws GDMException_Exception {
        return services.getSheetTypesByKeys(pRoleToken, pKeys);
    }

    /**
     * Get data content for a list of sheets.
     * 
     * @param pRefs
     *            Reference array
     * @param pRoleToken
     *            roleToken
     * @param pProcessName
     *            business process name
     * @param pProductName
     *            product name
     * @return SheetData array.
     * @throws GDMException_Exception
     *             internal exception.
     */
    public List<SheetData> getSheetsByRefs(String pRoleToken,
            String pProcessName, String pProductName, List<String> pRefs)
        throws GDMException_Exception {
        return services.getSheetsByRefs(pRoleToken, pProcessName, pProductName,
                pRefs);
    }

    /**
     * Get data content of a sheet.
     * 
     * @param pRoleToken
     *            roleToken
     * @param pProcessName
     *            business process name
     * @param pProductName
     *            product name
     * @param pSheetReference
     *            Functional sheet reference
     * @return SheetData array.
     * @throws GDMException_Exception
     *             Internal exception.
     */
    public SheetData getSheetByRef(String pRoleToken, String pProcessName,
            String pProductName, String pSheetReference)
        throws GDMException_Exception {
        List<SheetData> lSheets =
                getSheetsByRefs(pRoleToken, pProcessName, pProductName,
                        Collections.singletonList(pSheetReference));
        if (lSheets == null || lSheets.isEmpty()) {
            return null;
        }
        return lSheets.get(0);
    }

    /**
     * Lock the sheet with a specified type.
     * 
     * @param pRoleToken
     *            the session token
     * @param pSheetId
     *            the sheet id
     * @param pLockType
     *            The LockType
     * @throws GDMException_Exception
     *             internal exception.
     */
    public void lockSheet(final String pRoleToken, String pSheetId,
            LockTypeEnumeration pLockType) throws GDMException_Exception {
        services.lockSheet(pRoleToken, pSheetId, pLockType);
    }

    /**
     * Lock the sheet using a specified lock info.
     * <p>
     * This method can be used by an administrator to take lock on behalf on any
     * user.
     * 
     * @param pRoleToken
     *            the session token
     * @param pSheetId
     *            the sheet id
     * @param pLock
     *            lock infos
     * @throws GDMException_Exception
     *             internal exception.
     */
    public void lockSheetForUser(final String pRoleToken, String pSheetId,
            Lock pLock) throws GDMException_Exception {
        services.lockSheetForUser(pRoleToken, pSheetId, pLock);
    }

    /**
     * Get the lock set of a sheet, if any.
     * 
     * @param pRoleToken
     *            the session token
     * @param pSheetId
     *            the sheet id
     * @return Lock set on the sheet, or null if sheet is not locked.
     * @throws GDMException_Exception
     *             internal exception.
     */
    public Lock getLock(final String pRoleToken, final String pSheetId)
        throws GDMException_Exception {
        return services.getLock(pRoleToken, pSheetId);
    }

    /**
     * Getter for sheets.
     * 
     * @param pKeys
     *            key array
     * @param pRoleToken
     *            roleToken
     * @return SheetData array.
     * @throws GDMException_Exception
     *             internal exception.
     */
    public List<SheetData> getSheetsByKeys(String pRoleToken, List<String> pKeys)
        throws GDMException_Exception {
        return services.getSheetsByKeys(pRoleToken, pKeys);
    }

    /**
     * Get an 'empty' sheet corresponding to a given sheet type.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pTypeName
     *            Sheet type name
     * @param pProductName
     *            Product name
     * @param pContext
     *            Array of context parameters (may be null)
     * @return The sheet data pre-filled with the default values.
     * @throws GDMException_Exception
     *             internal exception.
     */
    public SheetData getSheetModel(String pRoleToken, String pTypeName,
            String pProductName, List<ContextParam> pContext)
        throws GDMException_Exception {
        return services.getSheetModel(pRoleToken, pTypeName, pProductName,
                pContext);
    }

    /**
     * Getter for link types.
     * 
     * @param pKeys
     *            key array
     * @param pRoleToken
     *            roleToken
     * @return LinkType array.
     * @throws GDMException_Exception
     *             internal exception.
     */
    public List<LinkType> getLinkTypesByKeys(String pRoleToken,
            List<String> pKeys) throws GDMException_Exception {
        return services.getLinkTypesByKeys(pRoleToken, pKeys);
    }

    /**
     * Getter for links.
     * 
     * @param pContainerId
     *            container identifier.
     * @param pRoleToken
     *            roleToken
     * @return Link array.
     * @throws GDMException_Exception
     *             internal exception.
     */
    public List<Link> getLinks(String pRoleToken, String pContainerId)
        throws GDMException_Exception {
        return services.getLinks(pRoleToken, pContainerId);
    }

    /**
     * Getter for links.
     * 
     * @param pKeys
     *            key array
     * @param pRoleToken
     *            roleTokenF
     * @return Link array.
     * @throws GDMException_Exception
     *             internal exception.
     */
    public List<Link> getLinksByKeys(String pRoleToken, List<String> pKeys)
        throws GDMException_Exception {
        return services.getLinksByKeys(pRoleToken, pKeys);
    }

    /**
     * Get a list of sheet types identifiers possibly linkable to a given sheet
     * type
     * 
     * @param pRoleToken
     *            Role session token
     * @param pSheetTypeId
     *            Identifier of the sheet type
     * @return List of sheet type identifiers
     */
    public List<String> getLinkableSheetTypes(String pRoleToken,
            String pSheetTypeId) {
        return services.getLinkableSheetTypes(pRoleToken, pSheetTypeId);
    }

    /**
     * Getter for product types.
     * 
     * @param pKeys
     *            key array
     * @param pRoleToken
     *            roleToken
     * @return ProductType array.
     * @throws GDMException_Exception
     *             internal exception.
     * @deprecated
     */
    @Deprecated
    public List<ProductType> getProductTypesByKeys(String pRoleToken,
            List<String> pKeys) throws GDMException_Exception {
        return services.getProductTypesByKeys(pRoleToken, pKeys);
    }

    /**
     * Getter for products.
     * 
     * @param pKeys
     *            key array
     * @param pRoleToken
     *            roleToken
     * @return ProductData array.
     * @throws GDMException_Exception
     *             internal exception.
     * @deprecated
     */
    @Deprecated
    public List<Product> getProductsByKeys(String pRoleToken, List<String> pKeys)
        throws GDMException_Exception {
        return services.getProductsByKeys(pRoleToken, pKeys);
    }

    /**
     * Get the sheet types associated with a process a given role can create.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pProcessName
     *            Name of business process
     * @return Array of sheet types data
     * @throws GDMException_Exception
     *             internal exception.
     */
    public List<SheetType> getCreatableSheetTypes(String pRoleToken,
            String pProcessName) throws GDMException_Exception {
        return services.getCreatableSheetTypes(pRoleToken, pProcessName);
    }

    /**
     * Create Sheet.
     * 
     * @param pSheetData
     *            sheetData
     * @param pRoleToken
     *            roleToken
     * @param pProcessName
     *            process Name
     * @param pProductName
     *            product Name
     * @return Identifier of the created sheet.
     * @throws GDMException_Exception
     *             internal exception.
     */
    public String createSheet(String pRoleToken, String pProcessName,
            String pProductName, SheetData pSheetData)
        throws GDMException_Exception {
        return services.createSheet(pRoleToken, pProcessName, pProductName,
                pSheetData);
    }

    /**
     * Create Sheet.
     * 
     * @param pSheetData
     *            sheetData
     * @param pRoleToken
     *            roleToken
     * @param pProcessName
     *            process Name
     * @param pProductName
     *            product Name
     * @param pContext
     *            Execution context
     * @return Identifier of the created sheet.
     * @throws GDMException_Exception
     *             internal exception.
     */
    public String createSheet(String pRoleToken, String pProcessName,
            String pProductName, SheetData pSheetData,
            List<ContextParam> pContext) throws GDMException_Exception {
        return services.createSheetEx(pRoleToken, pProcessName, pProductName,
                pSheetData, pContext);
    }

    /**
     * Update Sheets.
     * 
     * @param pSheetDatas
     *            sheetData array
     * @param pRoleToken
     *            roleToken
     * @param pProcessName
     *            process Name
     * @throws GDMException_Exception
     *             internal exception.
     */
    public void updateSheets(String pRoleToken, String pProcessName,
            List<SheetData> pSheetDatas) throws GDMException_Exception {
        services.updateSheets(pRoleToken, pProcessName, pSheetDatas);
    }

    /**
     * Update Sheets.
     * 
     * @param pRoleToken
     *            roleToken
     * @param pProcessName
     *            process Name
     * @param pSheetDataList
     *            sheetData array
     * @param pContext
     *            Execution context
     * @throws GDMException_Exception
     *             internal exception.
     */
    public void updateSheets(String pRoleToken, String pProcessName,
            List<SheetData> pSheetDataList, List<ContextParam> pContext)
        throws GDMException_Exception {
        services.updateSheets(pRoleToken, pProcessName, pSheetDataList);
    }

    /**
     * Update a sheet content.
     * 
     * @param pRoleToken
     *            roleToken
     * @param pProcessName
     *            process Name
     * @param pSheetData
     *            sheet data
     * @param pContext
     *            Execution context
     * @throws GDMException_Exception
     *             internal exception.
     */
    public void updateSheet(String pRoleToken, String pProcessName,
            SheetData pSheetData, List<ContextParam> pContext)
        throws GDMException_Exception {
        if (null == pSheetData) {
            throw new IllegalArgumentException("Sheet data cannot be null");
        }
        updateSheets(pRoleToken, pProcessName,
                Collections.singletonList(pSheetData), pContext);
    }

    /**
     * Delete sheets.
     * 
     * @param pRoleToken
     *            Token of the caller's role.
     * @param pKeys
     *            Identifier array of sheets to delete in the database.
     * @throws GDMException_Exception
     *             internal exception.
     */
    public void deleteSheets(String pRoleToken, List<String> pKeys)
        throws GDMException_Exception {
        services.deleteSheets(pRoleToken, pKeys);
    }

    /**
     * Delete sheets.
     * 
     * @param pRoleToken
     *            Token of the caller's role.
     * @param pKeys
     *            Identifier array of sheets to delete in the database.
     * @param pContext
     *            Execution context
     * @throws GDMException_Exception
     *             internal exception.
     */
    public void deleteSheets(String pRoleToken, List<String> pKeys,
            List<ContextParam> pContext) throws GDMException_Exception {
        services.deleteSheetsEx(pRoleToken, pKeys, pContext);
    }

    /**
     * Delete a sheet.
     * 
     * @param pRoleToken
     *            Token of the caller's role.
     * @param pSheetKey
     *            Identifier of the sheet to delete in the database.
     * @param pContext
     *            Execution context
     * @throws GDMException_Exception
     *             internal exception.
     */
    public void deleteSheet(String pRoleToken, String pSheetKey,
            List<ContextParam> pContext) throws GDMException_Exception {
        deleteSheets(pRoleToken, Collections.singletonList(pSheetKey), pContext);
    }

    /**
     * Get a list of possible links selectable for a given sheet type.
     * 
     * @param pRoleToken
     *            The role token.
     * @param pSheetTypeId
     *            Identifier of the sheet type.
     * @return Array of sheet link type identifiers
     * @throws GDMException_Exception
     *             internal exception.
     */
    public List<String> getPossibleLinkTypes(String pRoleToken,
            String pSheetTypeId) throws GDMException_Exception {
        return services.getPossibleLinkTypes(pRoleToken, pSheetTypeId);
    }

    /**
     * Create a new link between two sheets.
     * 
     * @param pRoleToken
     *            The role token.
     * @param pLinkTypeName
     *            Name of the link type.
     * @param pSourceId
     *            The source element id.
     * @param pDestId
     *            The destination element id.
     * @return the created link.
     * @throws GDMException_Exception
     *             internal exception.
     */
    public Link createSheetLink(String pRoleToken, String pLinkTypeName,
            String pSourceId, String pDestId) throws GDMException_Exception {
        return services.createSheetLink(pRoleToken, pLinkTypeName, pSourceId,
                pDestId);
    }

    /**
     * Create a new link between two sheets.
     * 
     * @param pRoleToken
     *            The role token.
     * @param pLinkTypeName
     *            Name of the link type.
     * @param pSourceId
     *            The source element id.
     * @param pDestId
     *            The destination element id.
     * @param pContext
     *            Execution context (or null if no context required)
     * @return the created link.
     * @throws GDMException_Exception
     *             internal exception.
     */
    public Link createSheetLink(String pRoleToken, String pLinkTypeName,
            String pSourceId, String pDestId, List<ContextParam> pContext)
        throws GDMException_Exception {
        return services.createSheetLinkEx(pRoleToken, pLinkTypeName, pSourceId,
                pDestId, pContext);
    }

    /**
     * Update an existing link content.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pLinks
     *            link data array.
     * @throws GDMException_Exception
     *             internal exception.
     */
    public void updateLinks(String pRoleToken, List<Link> pLinks)
        throws GDMException_Exception {
        services.updateLinks(pRoleToken, pLinks);
    }

    /**
     * Update an existing link content.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pLinks
     *            link data array.
     * @param pContext
     *            Execution context
     * @throws GDMException_Exception
     *             internal exception.
     */
    public void updateLinks(String pRoleToken, List<Link> pLinks,
            List<ContextParam> pContext) throws GDMException_Exception {
        services.updateLinksEx(pRoleToken, pLinks, pContext);
    }

    /**
     * Delete a link between two sheets.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pKeys
     *            Identifier array of links to remove.
     * @throws GDMException_Exception
     *             internal exception.
     */
    public void deleteLinks(String pRoleToken, List<String> pKeys)
        throws GDMException_Exception {
        services.deleteLinks(pRoleToken, pKeys);
    }

    /**
     * Delete a link between two sheets.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pKeys
     *            Identifier array of links to remove.
     * @param pContext
     *            Execution context
     * @throws GDMException_Exception
     *             internal exception.
     */
    public void deleteLinks(String pRoleToken, List<String> pKeys,
            List<ContextParam> pContext) throws GDMException_Exception {
        services.deleteLinksEx(pRoleToken, pKeys, pContext);
    }

    /**
     * Get the process information regarding the life cycle of sheet array.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pSheetIds
     *            Identifier of sheets
     * @return Process information
     * @throws GDMException_Exception
     *             internal exception.
     */
    public List<ProcessInformation> getSheetProcessInformations(
            String pRoleToken, List<String> pSheetIds)
        throws GDMException_Exception {
        return services.getSheetProcessInformations(pRoleToken, pSheetIds);
    }

    /**
     * Signal a transition on a sheet.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pSheetId
     *            Id of the sheet target.
     * @param pTransition
     *            name of the transition to perform.
     * @throws GDMException_Exception
     *             internal exception.
     */
    public void changeState(String pRoleToken, String pSheetId,
            String pTransition) throws GDMException_Exception {
        services.changeState(pRoleToken, pSheetId, pTransition);
    }

    /**
     * Signal a transition on a sheet.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pSheetId
     *            Id of the sheet target.
     * @param pTransition
     *            name of the transition to perform.
     * @param pContext
     *            execution context
     * @throws GDMException_Exception
     *             internal exception.
     */
    public void changeState(String pRoleToken, String pSheetId,
            String pTransition, Context pContext) throws GDMException_Exception {
        services.changeStateEx(pRoleToken, pSheetId, pTransition, pContext);
    }

    /**
     * Get the history of a sheet.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pSheetId
     *            Identifier of the sheet
     * @return Array containing the history info
     * @throws GDMException_Exception
     *             internal exception.
     */
    public List<SheetHistoryData> getSheetHistory(String pRoleToken,
            String pSheetId) throws GDMException_Exception {
        return services.getSheetHistory(pRoleToken, pSheetId);
    }

    /**
     * Get the list of values of a category defined for an environment.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pBusinessProcessName
     *            Business process name.
     * @param pEnvironmentName
     *            Name of the environment.
     * @param pCategoryName
     *            Name of the category.
     * @return The list of environment data.
     * @throws GDMException_Exception
     *             internal exception.
     */
    public EnvironmentData getEnvironmentCategory(String pRoleToken,
            String pBusinessProcessName, String pEnvironmentName,
            String pCategoryName) throws GDMException_Exception {
        return services.getEnvironmentCategory(pRoleToken,
                pBusinessProcessName, pEnvironmentName, pCategoryName);
    }

    /**
     * Get the list of values of a category defined for an environment.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pCategoryName
     *            Name of the category.
     * @param pProcessName
     *            Business process name.
     * @param pEnvironmentName
     *            Name of the environment.
     * @return the categoryValues array.
     * @throws GDMException_Exception
     *             internal exception.
     */
    public List<String> getCategoryValues(String pRoleToken,
            String pCategoryName, String pProcessName, String pEnvironmentName)
        throws GDMException_Exception {
        return services.getCategoryValues(pRoleToken, pCategoryName,
                pProcessName, pEnvironmentName);
    }

    /**
     * Get all the ExecutableFilterData objects of a certain type , visible for
     * the given businessProcess, product and user.
     * 
     * @param pRoleToken
     *            the role token
     * @param pBusinessProcessName
     *            the current business process
     * @param pProductName
     *            the current product
     * @param pUserLogin
     *            the current user login
     * @param pFilterType
     *            the type of filter we are looking for (QueryLL.SHEET_FILTER,
     *            QueryLL.PRODUCT_FILTER)
     * @param pUsage
     *            the filter usage (TREE_VIEW, TABLE_VIEW or BOTH_VIEWS)
     * @return the visible filter names.
     * @throws GDMException_Exception
     *             internal exception.
     * @see #executeSheetFilterWithScope(String, int, int, String, String,
     *      FilterScope) for a more precise control
     */
    public List<String> getVisibleExecutableFilterNamesByFilterType(
            String pRoleToken, String pBusinessProcessName,
            String pProductName, String pUserLogin, FilterTypeData pFilterType,
            FilterUsageEnum pUsage) throws GDMException_Exception {
        return services.getVisibleExecutableFilterNamesByFilterType(pRoleToken,
                pBusinessProcessName, pProductName, pUserLogin, pFilterType,
                pUsage);
    }

    /**
     * Get all the ExecutableFilterData objects of a certain type , visible for
     * the given businessProcess, product and user, sorted in a map by filter
     * scope (Process, Product, User)
     * 
     * @param pRoleToken
     *            the role token
     * @param pBusinessProcessName
     *            the current business process
     * @param pProductName
     *            the current product
     * @param pUserLogin
     *            the current user login
     * @param pFilterType
     *            the type of filter we are looking for (QueryLL.SHEET_FILTER,
     *            QueryLL.PRODUCT_FILTER)
     * @param pUsage
     *            the filter usage (TREE_VIEW, TABLE_VIEW or BOTH_VIEWS)
     * @return the visible filter names.
     * @throws GDMException_Exception
     *             internal exception.
     */
    public List<String> getExecutableFilterNamesForContext(String pRoleToken,
            String pBusinessProcessName, String pProductName,
            String pUserLogin, FilterTypeData pFilterType,
            FilterUsageEnum pUsage, FilterScope pScope)
        throws GDMException_Exception {
        return services.getExecutableFilterNamesForContext(pRoleToken,
                pBusinessProcessName, pProductName, pUserLogin, pFilterType,
                pUsage, pScope);
    }

    /**
     * Get all the FilterData objects for a certain sheet type, visible for the
     * given businessProcess, product and user.
     * 
     * @param pRoleToken
     *            The role session token
     * @param pSheetTypeName
     *            The sheet type name
     * @param pFilterScope
     *            The filter scope
     * @return the visible filter names.
     */
    public List<String> getVisibleFilterNamesBySheetType(String pRoleToken,
            String pSheetTypeName, FilterScope pFilterScope) {
        return services.getVisibleFilterNamesBySheetType(pRoleToken,
                pSheetTypeName, pFilterScope);
    }

    /**
     * Execute an executable filter data on sheets with a userLogin in a product
     * and a business process, with a limited number of results
     * 
     * @param pRoleToken
     *            the role token
     * @param pBusinessProcessName
     *            the business process name
     * @param pProductName
     *            the product name
     * @param pUserLogin
     *            the user login
     * @param pFilterName
     *            the name of executable filter data
     * @return the array of sheet summary data
     * @throws GDMException_Exception
     *             internal exception.
     * @see #executeSheetFilterWithScope(String, int, int, String, String,
     *      FilterScope) for a more precise control
     */
    public List<SheetSummaryData> executeSheetFilter(String pRoleToken,
            String pBusinessProcessName, String pProductName,
            String pUserLogin, String pFilterName)
        throws GDMException_Exception {
        return services.executeSheetFilter(pRoleToken, pBusinessProcessName,
                pProductName, pUserLogin, pFilterName);
    }

    /**
     * Execute a filter on sheets in a product and current business process,
     * with a limited number of results or not, from its name and its visibility
     * scope
     * 
     * @param pRoleToken
     *            the role token
     * @param pMaxResult
     *            the max number of results to show or -1 to show all results
     * @param pFirstResult
     *            the number of the first result to show (no effect if
     *            pMaxResult = -1)
     * @param pProductName
     *            the product name
     * @param pFilterName
     *            the filter name
     * @param pScope
     *            the visibility scope of the filter. If null, a user filter, a
     *            product filter or an instance filter will be executed with
     *            this order of preference.
     * @return the array of sheet summary data
     * @throws GDMException_Exception
     *             internal exception.
     */
    public List<SheetSummaryData> executeSheetFilterWithScope(
            String pRoleToken, int pMaxResult, int pFirstResult,
            String pProductName, String pFilterName, FilterScope pScope)
        throws GDMException_Exception {
        return services.executeSheetFilterWithScope(pRoleToken, pMaxResult,
                pFirstResult, pProductName, pFilterName, pScope);
    }

    /**
     * Execute an executable filter data on products with a userLogin in a
     * product and a business process, with a limited number of results
     * 
     * @param pRoleToken
     *            the role token
     * @param pBusinessProcessName
     *            the business process name
     * @param pProductName
     *            the product name
     * @param pUserLogin
     *            the user login
     * @param pFilterName
     *            the name of executable filter data
     * @return the array of product summary datas
     * @throws GDMException_Exception
     *             internal exception.
     * @see #executeProductFilterWithScope(String, int, int, String, String,
     *      FilterScope) for a more precise control
     */
    public List<ProductSummaryData> executeProductFilter(String pRoleToken,
            String pBusinessProcessName, String pProductName,
            String pUserLogin, String pFilterName)
        throws GDMException_Exception {
        return services.executeProductFilter(pRoleToken, pBusinessProcessName,
                pProductName, pUserLogin, pFilterName);
    }

    /**
     * Execute a filter on products in a product and current business process,
     * with a limited number of results or not, from its name and its visibility
     * scope
     * 
     * @param pRoleToken
     *            the role token
     * @param pMaxResult
     *            the max number of results to show or -1 to show all results
     * @param pFirstResult
     *            the number of the first result to show (no effect if
     *            pMaxResult = -1)
     * @param pProductName
     *            the product name
     * @param pFilterName
     *            the filter name
     * @param pScope
     *            the visibility scope of the filter. If null, a user filter, a
     *            product filter or an instance filter will be executed with
     *            this order of preference.
     * @return the array of product summary data
     * @throws GDMException_Exception
     *             internal exception.
     */
    public List<ProductSummaryData> executeProductFilterWithScope(
            String pRoleToken, int pMaxResult, int pFirstResult,
            String pProductName, String pFilterName, FilterScope pScope)
        throws GDMException_Exception {
        return services.executeProductFilterWithScope(pRoleToken, pMaxResult,
                pFirstResult, pProductName, pFilterName, pScope);
    }

    /**
     * Get the content of an attached file.
     * 
     * @param pRoleToken
     *            Role token session.
     * @param pAttachedFieldValueId
     *            Identifier of the attached file in the database.
     * @return Byte array containing the file content.
     * @throws GDMException_Exception
     *             internal exception.
     */
    public byte[] getAttachedFileContent(String pRoleToken,
            String pAttachedFieldValueId) throws GDMException_Exception {
        return services.getAttachedFileContent(pRoleToken,
                pAttachedFieldValueId);
    }

    /**
     * Define a list of attributes to define or modify for an element. The
     * attributes to set are defined in the AttributeData[], using one array
     * entry per attribute to define (or remove). Each element defines the
     * attribute name, and the values to set for the attribute as a string
     * array. If the values array is empty for an attribute, the attribute is
     * removed from the container.
     * <p>
     * The attributes already defined which are not present in the AttributeData
     * array are not modified.
     * 
     * @param pElemId
     *            Unique identifier of the attributes container.
     * @param pAttrs
     *            Attributes to define
     * @throws GDMException_Exception
     *             internal exception.
     */
    public void setAttributes(String pElemId, List<AttributeData> pAttrs)
        throws GDMException_Exception {
        services.set(pElemId, pAttrs);
    }

    /**
     * Get some attributes of an element.
     * 
     * @param pElemId
     *            Unique identifier of the attributes container.
     * @param pAttrNames
     *            Names of the attributes to retrieve
     * @return Array containing the attributes.
     * @throws GDMException_Exception
     *             internal exception.
     */
    public List<AttributeData> getAttributes(String pElemId,
            List<String> pAttrNames) throws GDMException_Exception {
        return services.get(pElemId, pAttrNames);
    }

    /**
     * Get all attributes defined for an element.
     * 
     * @param pElemId
     *            Identifier of the attributes container.
     * @return Array containing the attributes.
     * @throws GDMException_Exception
     *             internal exception.
     */
    public List<AttributeData> getAllAttributes(String pElemId)
        throws GDMException_Exception {
        return services.getAll(pElemId);
    }

    /**
     * Remove all attributes defined on an element.
     * 
     * @param pElemId
     *            Identifier of the attributes container.
     * @throws GDMException_Exception
     *             internal exception.
     */
    public void removeAllAttributes(String pElemId)
        throws GDMException_Exception {
        services.removeAll(pElemId);
    }

    /**
     * Get the names of all extended attributes defined for an element.
     * 
     * @param pElemId
     *            Identifier of the attributes container.
     * @return Array containing the names of all attributes (sorted
     *         alphabetically).
     * @throws GDMException_Exception
     *             internal exception.
     */
    public List<String> getAttrNames(String pElemId)
        throws GDMException_Exception {
        return services.getAttrNames(pElemId);
    }

    /**
     * Get the list of global attribute names
     * 
     * @return a list of global attribute names
     * @throws GDMException_Exception
     *             internal exception.
     */
    public List<String> getGlobalAttrNames() throws GDMException_Exception {
        return services.getGlobalAttrNames();
    }

    /**
     * Get a list of global attributes data from their names
     * 
     * @param pAttrNames
     *            the attribute names
     * @return A list of AttributeData objects
     * @throws GDMException_Exception
     *             internal exception.
     */
    public List<AttributeData> getGlobalAttributes(List<String> pAttrNames)
        throws GDMException_Exception {
        return services.getGlobalAttributes(pAttrNames);
    }

    /**
     * Set a list of global attributes
     * 
     * @param pRoleToken
     *            the role token
     * @param pAttributesData
     *            the list of AttributeData
     * @throws GDMException_Exception
     *             internal exception.
     */
    public void setGlobalAttributes(String pRoleToken,
            List<AttributeData> pAttributesData) throws GDMException_Exception {
        services.setGlobalAttributes(pRoleToken, pAttributesData);
    }

    /**
     * Create a new revision on container pContainerId with a revision pLabel.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pContainerId
     *            Container identifier
     * @param pLabel
     *            The revision label
     * @return The revision id
     * @throws GDMException_Exception
     *             internal exception.
     */
    public String createRevision(String pRoleToken, String pContainerId,
            String pLabel) throws GDMException_Exception {
        return services.createRevision(pRoleToken, pContainerId, pLabel);
    }

    /**
     * Execute an extended action, with the current context : a sheet, a sheet
     * list or an input data.
     * 
     * @param pRoleToken
     *            the current user role token.
     * @param pExtendedActionName
     *            the extended action name.
     * @param pSheetId
     *            the current sheet ID (if the action is executed on a sheet).
     * @param pSheetIds
     *            the current sheet IDS (if the action is executed on a filter
     *            result).
     * @param pInputData
     *            the parameter values for action execution.
     * @return an ExtendedActionResult gives parameters according to the
     *         resultingScreen
     * @throws GDMException_Exception
     *             internal exception.
     */
    public ExtendedActionResult executeExtendedAction(String pRoleToken,
            String pExtendedActionName, String pSheetId,
            List<String> pSheetIds, InputData pInputData)
        throws GDMException_Exception {
        return services.executeExtendedAction(pRoleToken, pExtendedActionName,
                pSheetId, pSheetIds, pInputData);
    }

    /**
     * Get properties of a revision having pRevisionId on container pContainerId
     * 
     * @param pRoleToken
     *            Role session token
     * @param pContainerId
     *            Container identifier
     * @param pRevisionId
     *            Revision identifier
     * @throws GDMException_Exception
     *             internal exception.
     * @return The revision data structure
     */
    public RevisionData getRevision(String pRoleToken, String pContainerId,
            String pRevisionId) throws GDMException_Exception {
        return services.getRevision(pRoleToken, pContainerId, pRevisionId);
    }

    /**
     * Get properties of a revision having label pLabel on container
     * pContainerId
     * 
     * @param pRoleToken
     *            Role session token
     * @param pContainerId
     *            Container identifier
     * @param pLabel
     *            Revision label
     * @return The revision data structure
     * @throws GDMException_Exception
     *             internal exception.
     */
    public RevisionData getRevisionByLabel(String pRoleToken,
            String pContainerId, String pLabel) throws GDMException_Exception {
        return services.getRevisionByLabel(pRoleToken, pContainerId, pLabel);
    }

    /**
     * Get a sheet in a specific revision identified by a revision Id and a
     * container Id
     * 
     * @param pRoleToken
     *            The role token
     * @param pContainerId
     *            Container identifier
     * @param pRevisionId
     *            Revision identifier
     * @return The serializable sheet structure
     * @throws GDMException_Exception
     *             internal exception.
     */
    public SheetData getSheetDataInRevision(String pRoleToken,
            String pContainerId, String pRevisionId)
        throws GDMException_Exception {
        return services.getSheetDataInRevision(pRoleToken, pContainerId,
                pRevisionId);
    }

    /**
     * Get a sheet in a specific revision identified by a revision label and a
     * container Id
     * 
     * @param pRoleToken
     *            The role token
     * @param pContainerId
     *            Container identifier
     * @param pRevisionLabel
     *            Revision label
     * @return The sheet
     * @throws GDMException_Exception
     *             internal exception.
     */
    public SheetData getSheetDataByRevisionLabel(String pRoleToken,
            String pContainerId, String pRevisionLabel)
        throws GDMException_Exception {
        return services.getSheetDataByRevisionLabel(pRoleToken, pContainerId,
                pRevisionLabel);
    }

    /**
     * Delete the latest revision on container pContainerId
     * 
     * @param pRoleToken
     *            Role session token
     * @param pContainerId
     *            Container identifier
     * @throws GDMException_Exception
     *             internal exception.
     */
    public void deleteRevision(String pRoleToken, String pContainerId)
        throws GDMException_Exception {
        services.deleteRevision(pRoleToken, pContainerId);
    }

    /**
     * Get the number of revisions on the container
     * 
     * @param pRoleToken
     *            Role session token
     * @param pContainerId
     *            Container identifier
     * @throws GDMException_Exception
     *             internal exception.
     * @return Number of revisions
     */
    public int getRevisionsCount(String pRoleToken, String pContainerId)
        throws GDMException_Exception {
        return services.getRevisionsCount(pRoleToken, pContainerId);
    }

    /**
     * Get a summary of revisions created on a container
     * 
     * @param pRoleToken
     *            The role token
     * @param pContainerId
     *            Container identifier
     * @return Collection containing a summary of each revision
     * @throws GDMException_Exception
     *             internal exception.
     */
    public List<RevisionSummaryData> getRevisionsSummary(String pRoleToken,
            String pContainerId) throws GDMException_Exception {
        return services.getRevisionsSummary(pRoleToken, pContainerId);
    }

    /**
     * Get the list of users defined on the application
     * 
     * @return a list of users
     * @throws GDMException_Exception
     *             internal exception
     */
    public List<User> getAllUsers() throws GDMException_Exception {
        return services.getAllUsers();
    }

    /**
     * Get the sheetState from the sheet ID
     * 
     * @param pSheetId
     *            the sheetID
     * @return the sheet state
     * @throws GDMException_Exception
     *             internal exception
     */
    public String getSheetState(String pSheetId) throws GDMException_Exception {
        return services.getSheetState(pSheetId);
    }

    /**
     * Get the user from its login
     * 
     * @param pLogin
     *            the user login
     * @return the user corresponding to this login
     * @throws GDMException_Exception
     *             internal exception
     */
    public User getUserFromLogin(String pLogin) throws GDMException_Exception {
        return services.getUserFromLogin(pLogin);
    }

    /**
     * Get the users with role.
     * 
     * @param pProcessName
     *            the business process name
     * @param pRoleName
     *            the searched role name
     * @param pProductNames
     *            the list of products in which this role must be found
     * @param pIsInstanceRoleAllowed
     *            true if both product roles and instance roles can be found,
     *            false if limited to product roles
     * @param pIsRoleOnAllProduct
     *            true if role must be presented on all specified products,
     *            false for a role on at least one of the products.
     * @return list of user logins
     * @throws GDMException_Exception
     *             internal exception
     */
    public List<String> getUsersWithRole(String pProcessName, String pRoleName,
            List<String> pProductNames, boolean pIsInstanceRoleAllowed,
            boolean pIsRoleOnAllProduct) throws GDMException_Exception {
        return services.getUsersWithRole(pProcessName, pRoleName,
                pProductNames, pIsInstanceRoleAllowed, pIsRoleOnAllProduct);
    }

    /**
     * Add new roles for a list of users
     * 
     * @param pRoleToken
     *            Session token of the current user
     * @param pUserLogins
     *            List of user logins to modify
     * @param pRoleName
     *            Name of the role to set
     * @param pProductName
     *            Name of the product on which the role must be set
     * @param pProcessName
     *            Current business process name
     * @throws GDMException_Exception
     *             internal exception
     */
    public void addRoleForUsers(String pRoleToken, List<String> pUserLogins,
            String pRoleName, String pProductName, String pProcessName)
        throws GDMException_Exception {
        services.addRoleForUsers(pRoleToken, pUserLogins, pRoleName,
                pProductName, pProcessName);
    }

    /**
     * Remove a role for a list of users
     * 
     * @param pRoleToken
     *            Session token of the current user
     * @param pUserLogins
     *            List of user logins to modify
     * @param pRoleName
     *            Name of the role to remove
     * @param pProductName
     *            Name of the product on which the role must be removed (or null
     *            for instance role)
     * @param pProcessName
     *            Current business process name
     * @throws GDMException_Exception
     *             internal exception
     */
    public void removeRoleForUsers(String pRoleToken, List<String> pUserLogins,
            String pRoleName, String pProductName, String pProcessName)
        throws GDMException_Exception {
        services.removeRoleForUsers(pRoleToken, pUserLogins, pRoleName,
                pProductName, pProcessName);
    }

    /**
     * Execute a filter from its name and get the list of resulting IDs
     * 
     * @param pRoleToken
     *            session token
     * @param pBusinessProcessName
     *            process name
     * @param pProductName
     *            filter product name
     * @param pUserLogin
     *            filter user login
     * @param pFilterName
     *            filter name
     * @return list of Ids obtained as a result of the filter
     * @throws GDMException_Exception
     *             internal exception.
     */
    public List<String> executeFilter(String pRoleToken,
            String pBusinessProcessName, String pProductName,
            String pUserLogin, String pFilterName)
        throws GDMException_Exception {
        return services.executeFilter(pRoleToken, pBusinessProcessName,
                pProductName, pUserLogin, pFilterName);
    }

    /**
     * Get a list of all acceptable values for a 'choiceString' field.
     * <p>
     * The field passed as parameter for this method must be a String field, and
     * should have a 'choiceString' display hint.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pTypeId
     *            Identifier of the values container
     * @param pFieldId
     *            Field identifier.
     * @param pCtxParams
     *            Execution context
     * @return List of values (used as choice field values)
     * @throws GDMException_Exception
     *             internal exception.
     * @deprecated use
     *             {@link WebServicesStub#getChoiceStringData(String, String, String, List)}
     *             instead
     * @since 2.0.5
     */
    public List<String> getChoiceStringList(String pRoleToken, String pTypeId,
            String pFieldId, List<ContextParam> pCtxParams)
        throws GDMException_Exception {
        if (pCtxParams == null) {
            return services.getChoiceStringList(pRoleToken, pTypeId, pFieldId,
                    new ArrayList<ContextParam>(0));
        }
        return services.getChoiceStringList(pRoleToken, pTypeId, pFieldId,
                pCtxParams);
    }

    /**
     * Get the data used by a 'choiceString' field.
     * <p>
     * Data of a 'choiceString' field are :
     * <ul>
     * <li>a list of values (key to use:
     * ExtensionPointParameters.CHOICES_RESULT.getParameterName())
     * <li>an optional default selected value (key to use:
     * ExtensionPointParameters.DEFAULT_VALUE_CHOICES_RESULT.getParameterName())
     * </ul>
     * <p>
     * The field passed as parameter for this method must be a String field, and
     * should have a 'choiceString' display hint. The user can enhance the
     * default context.
     * <p>
     * Default context:
     * <ul>
     * <li>typeName: Name of the choice string field container</li>
     * <li>typeId: Id of the choice string field container</li>
     * <li>fieldName: The choice string field name</li>
     * <li>fieldId: The choice string field id</li>
     * </ul>
     * 
     * @param pRoleToken
     *            Role session token
     * @param pTypeId
     *            Identifier of the values container
     * @param pFieldId
     *            Field identifier.
     * @param pCtxParams
     *            Context of the extension action to get choice string value.
     *            Can be null.
     * @return List of values
     * @throws GDMException_Exception
     *             internal exception.
     */
    public HashMap getChoiceStringData(String pRoleToken, String pTypeId,
            String pFieldId, List<ContextParam> pCtxParams)
        throws GDMException_Exception {
        if (pCtxParams == null) {
            return services.getChoiceStringData(pRoleToken, pTypeId, pFieldId,
                    new ArrayList<ContextParam>(0));
        }
        return services.getChoiceStringData(pRoleToken, pTypeId, pFieldId,
                pCtxParams);
    }

    /**
     * Get the IDs of elements linked to current element with specific link
     * type.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pValuesContainerId
     *            Identifier of the current values container.
     * @param pLinkTypeName
     *            Name of the link type.
     * @return List of linked container IDs
     * @throws GDMException_Exception
     *             internal exception.
     */
    public List<String> getLinkedElementsIds(String pRoleToken,
            String pValuesContainerId, String pLinkTypeName)
        throws GDMException_Exception {
        return services.getLinkedElementsIds(pRoleToken, pValuesContainerId,
                pLinkTypeName);
    }

    /**
     * Get the list of user role names available for a given login
     * 
     * @param pUserToken
     *            Authorization token.
     * @param pBusinessProcessName
     *            Business process name
     * @param pProductName
     *            Product name (or null)
     * @param pRoleProperties
     *            properties indicating if roles found must be instance roles or
     *            roles on the specified product
     * @return List of available role names.
     * @throws GDMException_Exception
     *             internal exception.
     */
    public List<String> getRolesNames(String pUserToken,
            String pBusinessProcessName, String pProductName,
            RoleProperties pRoleProperties) throws GDMException_Exception {
        return services.getRolesNamesWithProperties(pUserToken,
                pBusinessProcessName, pProductName, pRoleProperties);
    }

    /**
     * Get a Sheet type filled with contextual access controls
     * 
     * @param pRoleToken
     *            session token
     * @param pProcessName
     *            process name
     * @param pProductName
     *            current product name (can be null)
     * @param pStateName
     *            current sheet state name(can be null)
     * @param pSheetTypeName
     *            name of the sheet type to find and fill with access controls.
     * @return The sheet type, with access controls, and corresponding extended
     *         attributes.
     * @throws GDMException_Exception
     *             internal exception
     */
    public SheetType getSheetTypeWithAccessControls(String pRoleToken,
            String pProcessName, String pProductName, String pStateName,
            String pSheetTypeName) throws GDMException_Exception {
        return services.getSheetTypeWithAccessControls(pRoleToken,
                pProcessName, pProductName, pStateName, pSheetTypeName);
    }

    /**
     * Test values container existence.
     * 
     * @param pValuesContainerId
     *            Identifier of the values container to test.
     * @return True if the values container exists, false otherwise
     */
    public boolean isValuesContainerExists(String pValuesContainerId) {
        return services.isValuesContainerExists(pValuesContainerId);
    }

    /**
     * Import all the data contains on an XML file.
     * 
     * @param pRoleToken
     *            The role token.
     * @param pInputStream
     *            The input stream reading the XML file.
     * @param pProperties
     *            The import properties.
     * @return The import execution report.
     * @throws GDMException_Exception
     *             Error if the schema is invalid or if the content to import
     *             has unauthorized type (ImportFlag.ERROR)
     * @throws ImportException_Exception
     *             gpm import exception.
     */
    public ImportExecutionReport importData(final String pRoleToken,
            final byte[] pInputStream, final ImportProperties pProperties)
        throws GDMException_Exception, ImportException_Exception {
        return services.importData(pRoleToken, pInputStream, pProperties);
    }

    /**
     * Export the data of an instance on an XML file.
     * 
     * @param pRoleToken
     *            The role token.
     * @param pProperties
     *            The export properties.
     * @return The output stream containing the exported data.
     * @throws GDMException_Exception
     *             the kernel gPM exception.
     */
    public byte[] exportData(final String pRoleToken,
            final ExportProperties pProperties) throws GDMException_Exception {
        return services.exportData(pRoleToken, pProperties);
    }

    /**
     * Generate the sheet report with argument data.
     * 
     * @param pRoleToken
     *            the role token.
     * @param pSheetIds
     *            the sheets id.
     * @param pExportFormat
     *            the export format.
     * @param pReportName
     *            the report name to choose a specific report when there are
     *            several.
     * @return the generate sheet report in byte array.
     * @throws GDMException_Exception
     *             the kernel gPM exception.
     */
    public byte[] generateSheetReport(String pRoleToken,
            List<String> pSheetIds, SheetExportFormat pExportFormat,
            String pReportName) throws GDMException_Exception {
        return services.generateSheetReport(pRoleToken, pSheetIds,
                pExportFormat, pReportName);
    }

    /**
     * Generate the filter result report with argument data.
     * 
     * @param pRoleToken
     *            the role token.
     * @param pExportFormat
     *            the export format.
     * @param pFilterName
     *            the filter name.
     * @param pFilterScope
     *            the filter scope.
     * @return the generate filter result report in byte array.
     * @throws GDMException_Exception
     *             the kernel gPM exception.
     */
    public byte[] generateFilterResultReport(String pRoleToken,
            SheetExportFormat pExportFormat, String pFilterName,
            FilterScope pFilterScope) throws GDMException_Exception {
        return services.generateFilterResultReport(pRoleToken, pExportFormat,
                pFilterName, pFilterScope);
    }

    /**
     * Set the content of an attached file.
     * 
     * @param pRoleToken
     *            Role token session.
     * @param pSheetId
     *            The sheet id
     * @param pAttachedFieldValueId
     *            Identifier of the attached file in the database.
     * @param pAttachedFileContent
     *            Byte array containing the file content.
     * @throws GDMException_Exception
     *             internal exception.
     */
    public void setAttachedFileContent(final String pRoleToken,
            final String pSheetId, final String pAttachedFieldValueId,
            final byte[] pAttachedFileContent) throws GDMException_Exception {
        services.setAttachedFileContentWithSheetId(pRoleToken, pSheetId,
                pAttachedFieldValueId, pAttachedFileContent);
    }

    /**
     * Create or Update a filter. If the ExecutableFilterData contains a
     * non-null ID, makes an update, else create a new Filter.
     * 
     * @param pRoleToken
     *            Role token session.
     * @param pExecutableFilterData
     *            The filter
     */
    public void createOrUpdateExecutableFilter(String pRoleToken,
            ExecutableFilterData pExecutableFilterData) {
        services.createOrUpdateExecutableFilter(pRoleToken,
                pExecutableFilterData);
    }

    /**
     * Delete filter by ID
     * 
     * @param pRoleToken
     *            Role token session.
     * @param pFilterId
     *            Identifier of the filter
     */
    public void deleteExecutableFilter(String pRoleToken, String pFilterId) {
        services.deleteExecutableFilter(pRoleToken, pFilterId);
    }

    /**
     * Executes the filter in argument and returns the results and execution
     * report
     * 
     * @param pRoleToken
     *            Role token session.
     * @param pBusinessProcessName
     *            The process name
     * @param pProductName
     *            The product name (can be null)
     * @param pUserLogin
     *            The user login (can be null)
     * @param pExecutableFilterData
     *            The filter data
     * @return The filter result
     */
    public FilterResult executeFilter(String pRoleToken,
            String pBusinessProcessName, String pProductName,
            String pUserLogin, ExecutableFilterData pExecutableFilterData) {
        return services.executeFilterData(pRoleToken, pBusinessProcessName,
                pProductName, pUserLogin, pExecutableFilterData);
    }

    /**
     * Get the list of scopes of editable filters
     * 
     * @param pRoleToken
     *            Role token session.
     * @return List of the scopes of editable filters
     */
    public List<FilterScope> getEditableFilterScope(String pRoleToken) {
        return services.getEditableFilterScope(pRoleToken);
    }

    /**
     * Get filter by ID
     * 
     * @param pRoleToken
     *            Role token session.
     * @param pFilterId
     *            Identifier of the filter
     * @return The expected filter
     */
    public ExecutableFilterData getExecutableFilter(String pRoleToken,
            String pFilterId) {
        return services.getExecutableFilter(pRoleToken, pFilterId);
    }

    /**
     * Get filter by name
     * 
     * @param pRoleToken
     *            Role token session.
     * @param pProcessName
     *            The process name
     * @param pFilterProductName
     *            The product name (can be null)
     * @param pFilterUserLogin
     *            The user login (can be null)
     * @param pFilterName
     *            The filter name
     * @return The expected filter
     */
    public ExecutableFilterData getExecutableFilterByName(String pRoleToken,
            String pProcessName, String pFilterProductName,
            String pFilterUserLogin, String pFilterName) {
        return services.getExecutableFilterByName(pRoleToken, pProcessName,
                pFilterProductName, pFilterUserLogin, pFilterName);
    }

    /**
     * Get the list of scopes of executable filters
     * 
     * @param pRoleToken
     *            Role token session.
     * @return List of the scopes of executable filters
     */
    public List<FilterScope> getExecutableFilterScope(String pRoleToken) {
        return services.getExecutableFilterScope(pRoleToken);
    }

    /**
     * Get the filters fields maximum depth
     * 
     * @return The filter fields maximum depth
     */
    public int getFilterFieldsMaxDepth() {
        return services.getFilterFieldsMaxDepth();
    }

    /**
     * Get the container names that can be used for the requested filter type.
     * It also checks the user authorization to access the corresponding
     * containers before sending them
     * 
     * @param pRoleToken
     *            Role token session.
     * @param pFilterType
     *            The filter type
     * @return The containers identifiers
     */
    public List<String> getSearcheableContainers(String pRoleToken,
            FilterTypeData pFilterType) {
        return services.getSearcheableContainers(pRoleToken, pFilterType);
    }

    /**
     * Get the field names that can be used for the requested containers,
     * meaning the common fields for the containers in argument. It also checks
     * the user authorizations to access the corresponding fields before sending
     * them
     * 
     * @param pRoleToken
     *            Role token session.
     * @param pContainerIds
     *            The containers identifiers
     * @return The field labels
     */
    public List<String> getSearcheableFieldsLabel(String pRoleToken,
            Collection<String> pContainerIds) {
        List<String> lContainerIds = new ArrayList<String>(pContainerIds);
        return services.getSearcheableFieldsLabel(pRoleToken, lContainerIds);
    }

    /**
     * Get the available extended actions
     * 
     * @param pRoleToken
     *            The role token session.
     * @param pGuiContexts
     *            The GUI context list
     * @param pContainerId
     *            The container identifier
     * @return The list of available extended actions
     * @throws GDMException_Exception
     *             internal exception.
     */
    public List<ExtendedActionSummary> getAvailableExtendedActions(
            final String pRoleToken, final List<GuiContext> pGuiContexts,
            final String pContainerId) throws GDMException_Exception {
        return services.getAvailableExtendedActions(pRoleToken, pGuiContexts,
                pContainerId);
    }

    /**
     * Get a inputData type
     * 
     * @param pRoleToken
     *            The role token session.
     * @param pInputDataTypeId
     *            The identifier
     * @return The inputData type
     * @throws GDMException_Exception
     *             internal exception.
     */
    public InputDataType getInputDataType(final String pRoleToken,
            final String pInputDataTypeId) throws GDMException_Exception {
        return services.getInputDataType(pRoleToken, pInputDataTypeId);
    }

    /**
     * Get an initialized input data
     * 
     * @param pRoleToken
     *            The role token session.
     * @param pInputDataTypeId
     *            The identifier
     * @return The inputData
     * @throws GDMException_Exception
     *             internal exception.
     */
    public InputData getInputDataModel(final String pRoleToken,
            final String pInputDataTypeId) throws GDMException_Exception {
        return services.getInputDataModel(pRoleToken, pInputDataTypeId);
    }

    /**
     * Execute an extended action
     * 
     * @param pRoleToken
     *            The role token session.
     * @param pExtendedActionName
     *            The extended action name
     * @param pExtensionsContainerTypeId
     *            The container type identifier
     * @param pSheetId
     *            The sheet identifier (can be null)
     * @param pSheetIds
     *            The list of sheet identifier (can be null)
     * @param pInputData
     *            The extended action inputData
     * @return The extended action result
     * @throws GDMException_Exception
     *             internal exception
     */
    public ExtendedActionResult executeExtendedAction(final String pRoleToken,
            final String pExtendedActionName,
            final String pExtensionsContainerTypeId, final String pSheetId,
            final List<String> pSheetIds, final InputData pInputData)
        throws GDMException_Exception {
        List<String> lSheetIds = pSheetIds;
        if (lSheetIds == null) {
            lSheetIds = new ArrayList<String>(0);
        }
        return services.executeExtendedActionByContainer(pRoleToken,
                pExtendedActionName, pExtensionsContainerTypeId, pSheetId,
                lSheetIds, pInputData);
    }

    /**
     * Get the available reports model for a given sheet list
     * 
     * @param pRoleToken
     *            The role token session.
     * @param pSheetIds
     *            The list of sheet identifier
     * @param pExportFormat
     *            The export format
     * @return The available reports model
     * @throws GDMException_Exception
     *             internal exception
     */
    public List<String> getAvailableReportModels(final String pRoleToken,
            final List<String> pSheetIds, final SheetExportFormat pExportFormat)
        throws GDMException_Exception {
        return services.getAvailableReportModels(pRoleToken, pSheetIds,
                pExportFormat);
    }

    /**
     * Get the link type with access control
     * 
     * @param pRoleToken
     *            The role token session.
     * @param pLinkTypeId
     *            The link type identifier
     * @param pDisplayedContainerId
     *            The container type identifier from the link is displayed
     * @return The link type
     * @throws GDMException_Exception
     *             internal exception
     */
    @WebResult(name = "linkType")
    public LinkType getLinkType(final String pRoleToken,
            final String pLinkTypeId, final String pDisplayedContainerId)
        throws GDMException_Exception {
        return services.getLinkType(pRoleToken, pLinkTypeId,
                pDisplayedContainerId);
    }

    /**
     * Get access control on actions
     * 
     * @param pRoleToken
     *            The role token session.
     * @param pAccessControlContextData
     *            The access control context. Set access control context
     *            attributes to null for use default values.
     * @param pActionNames
     *            The actions names
     * @return The action access
     * @throws GDMException_Exception
     *             internal exception
     */
    public List<ActionAccessControlData> getActionAccessControls(
            final String pRoleToken,
            final AccessControlContextData pAccessControlContextData,
            final List<String> pActionNames) throws GDMException_Exception {
        return services.getActionAccessControls(pRoleToken,
                pAccessControlContextData, pActionNames);
    }

    /**
     * Get the category values of a product
     * 
     * @param pRoleToken
     *            The role token session.
     * @param pProductName
     *            The product name
     * @param pCategoryName
     *            The category name
     * @return The category values
     * @throws GDMException_Exception
     *             internal exception
     */
    public List<String> getCategoryValues(final String pRoleToken,
            final String pProductName, final String pCategoryName)
        throws GDMException_Exception {
        return services.getCategoryValuesByProduct(pRoleToken, pProductName,
                pCategoryName);
    }

    /**
     * Returns the list of all virtual fields
     * 
     * @return The list of all virtual fields
     */
    public List<String> getAllVirtualFieldTypes() {
        return services.getAllVirtualFieldTypes();
    }

    /**
     * Return the product hierarchy
     * 
     * @param pUserToken
     *            the user token
     * @param pProcessName
     *            the process name
     * @return the product hierarchy
     */
    public List<ProductNode> getProductsAsTree(String pUserToken,
            String pProcessName) {
        return services.getProductsAsTree(pUserToken, pProcessName);
    }

    /**
     * Get all available extended actions depending on Context, and takes in
     * account the CreateSheet case, where no container ID existed but where
     * container type was known.
     * 
     * @param pRoleToken
     *            the user token
     * @param pGuiContexts
     *            the GUI Context
     * @param pExtensionContainerId
     *            the Container Type ID
     * @return All corresponding extended actions
     */
    public List<ExtendedActionSummary> getAvailableExtendedActionsForExtensionContainer(
            String pRoleToken, List<GuiContext> pGuiContexts,
            String pExtensionContainerId) {
        return services.getAvailableExtendedActionsForExtensionContainer(
                pRoleToken, pGuiContexts, pExtensionContainerId);
    }
    
    /**
     * Duplicates a sheet. The created sheet is a copy of the source sheet (same
     * field values).
     * 
     * @param pRoleToken
     *            Token of the caller's role.
     * @param pSheetId
     *            Identifier of the source sheet.
     * @return the data of the copy.
     * @throws GDMException
     *             internal exception.
     */
    public SheetData duplicateSheet(String pRoleToken, String pSheetId)
        throws GDMException_Exception {
        return services.duplicateSheet(pRoleToken, pSheetId);
    }
    
        
    /**
     * Get access controls for all the possible combinations of sheetTypes/sheet
     * states, for a certain role and a certain product.
     * 
     * @param pRoleToken
     *            The role token session.
     * @param pAccessControlContextData
     *            The access control context. Set access control context
     *            attributes to null for use default values.
     * @param pActionNames
     *            The actions names
     * @return The action access
     * @throws GDMException_Exception
     *             internal exception
     */
    public List<ActionAccessControlData> getActionAccessControlsByProductAndRole(
            String pRoleToken,
            AccessControlContextData pAccessControlContextData,
            List<String> pActionNames) throws GDMException_Exception {
        return services.getActionAccessControlsByProductAndRole(pRoleToken,
                pAccessControlContextData, pActionNames);
    }
    
    
    /**
     * Obtain the pointed field value from the pointer field label, and
     * references to pointed field.
     * 
     * @param pRoleToken
     *            session token
     * @param pFieldLabel
     *            pointer field label
     * @param pReferencedContainerId
     *            ID of pointed container
     * @param pReferencedFieldLabel
     *            pointed field label
     * @return the pointed field value object (as a list of
     *         <code>&lt;FieldValueData&gt;</code>) :
     *         <ul>
     *         <li>If the pointed field value is null => returns
     *         <code>null</code></li>
     *         <li>If the pointed field value is a single field value data =>
     *         returns a list with 1 element</li>
     *         <li>If the pointed field value is a list of field value datas =>
     *         returns the list</li>
     *         <li>If the pointed field value is a map of
     *         <code>&lt;String, List&lt;FieldValueData&gt;&gt;</code> (for a
     *         multiple field)=> returns the union of all FieldValueData lists</li>
     *         </ul>
     */
    public List<FieldValueData> getPointedFieldValue(String pRoleToken,
            String pFieldLabel, String pReferencedContainerId,
            String pReferencedFieldLabel) {
        return services.getPointedFieldValue(pRoleToken, pFieldLabel,
                pReferencedContainerId, pReferencedFieldLabel);
    }

    /**
     * Gets a new sheetData, initialized with the values of another sheet.
     * 
     * @param pRoleToken
     *            roleToken
     * @param pResultSheetTypeId
     *            the Id of the sheet type of the sheet that has to be
     *            initialized.
     * @param pSourceSheetId
     *            the Id of the sheet we want to get the values from.
     * @return The sheetData of the initialized sheet.
     * @throws GDMException
     *             internal exception.
     */
    public SheetData initializeSheet(String pRoleToken,
            String pResultSheetTypeId, String pSourceSheetId)
        throws GDMException_Exception {
        return services.initializeSheet(pRoleToken, pResultSheetTypeId,
                pSourceSheetId);
    }
}
