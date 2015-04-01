/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Olivier Gehin (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ws.v2;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.topcased.gpm.business.attributes.AttributeData;
import org.topcased.gpm.business.authorization.service.AccessControlContextData;
import org.topcased.gpm.business.authorization.service.ActionAccessControlData;
import org.topcased.gpm.business.authorization.service.RoleProperties;
import org.topcased.gpm.business.dictionary.EnvironmentData;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.exception.ImportException;
import org.topcased.gpm.business.exportation.ExportProperties;
import org.topcased.gpm.business.extensions.service.ExtendedActionResult;
import org.topcased.gpm.business.importation.ImportExecutionReport;
import org.topcased.gpm.business.importation.ImportProperties;
import org.topcased.gpm.business.lifecycle.service.ProcessInformation;
import org.topcased.gpm.business.product.service.ProductSummaryData;
import org.topcased.gpm.business.revision.RevisionSummaryData;
import org.topcased.gpm.business.scalar.BooleanValueData;
import org.topcased.gpm.business.scalar.DateValueData;
import org.topcased.gpm.business.scalar.IntegerValueData;
import org.topcased.gpm.business.scalar.RealValueData;
import org.topcased.gpm.business.scalar.ScalarValueData;
import org.topcased.gpm.business.scalar.StringValueData;
import org.topcased.gpm.business.search.criterias.CriteriaData;
import org.topcased.gpm.business.search.criterias.CriteriaFieldData;
import org.topcased.gpm.business.search.criterias.FilterTypeData;
import org.topcased.gpm.business.search.criterias.OperationData;
import org.topcased.gpm.business.search.criterias.impl.VirtualFieldData;
import org.topcased.gpm.business.search.impl.fields.UsableTypeData;
import org.topcased.gpm.business.search.service.ExecutableFilterData;
import org.topcased.gpm.business.search.service.FilterScope;
import org.topcased.gpm.business.search.service.UsableFieldData;
import org.topcased.gpm.business.serialization.data.AttachedDisplayHint;
import org.topcased.gpm.business.serialization.data.AttachedField;
import org.topcased.gpm.business.serialization.data.AttachedFieldValueData;
import org.topcased.gpm.business.serialization.data.ChoiceDisplayHint;
import org.topcased.gpm.business.serialization.data.ChoiceField;
import org.topcased.gpm.business.serialization.data.ChoiceStringDisplayHint;
import org.topcased.gpm.business.serialization.data.ChoiceTreeDisplayHint;
import org.topcased.gpm.business.serialization.data.FieldValueData;
import org.topcased.gpm.business.serialization.data.InputData;
import org.topcased.gpm.business.serialization.data.InputDataType;
import org.topcased.gpm.business.serialization.data.Link;
import org.topcased.gpm.business.serialization.data.LinkType;
import org.topcased.gpm.business.serialization.data.Lock;
import org.topcased.gpm.business.serialization.data.PointerFieldValueData;
import org.topcased.gpm.business.serialization.data.Product;
import org.topcased.gpm.business.serialization.data.ProductType;
import org.topcased.gpm.business.serialization.data.RevisionData;
import org.topcased.gpm.business.serialization.data.SheetData;
import org.topcased.gpm.business.serialization.data.SheetType;
import org.topcased.gpm.business.serialization.data.SimpleField;
import org.topcased.gpm.business.serialization.data.TextDisplayHint;
import org.topcased.gpm.business.serialization.data.User;
import org.topcased.gpm.business.serialization.data.Lock.LockTypeEnumeration;
import org.topcased.gpm.business.sheet.export.service.SheetExportFormat;
import org.topcased.gpm.business.sheet.service.SheetHistoryData;
import org.topcased.gpm.business.sheet.service.SheetSummaryData;
import org.topcased.gpm.ws.context.BooleanParam;
import org.topcased.gpm.ws.context.ContextParam;
import org.topcased.gpm.ws.context.IntegerParam;
import org.topcased.gpm.ws.context.StringParam;
import org.topcased.gpm.ws.context.StringsListParam;
import org.topcased.gpm.ws.v2.business.ProductNode;
import org.topcased.gpm.ws.v2.extensions.ExtendedActionSummary;
import org.topcased.gpm.ws.v2.extensions.GuiContext;
import org.topcased.gpm.ws.v2.search.FilterResult;
import org.topcased.gpm.ws.v2.util.FilterUsageEnum;

/**
 * SEI.
 * 
 * @author ogehin
 */
@Transactional(propagation = Propagation.REQUIRED)
@WebService
public interface Services {

    /**
     * Login.
     * 
     * @param pUserName
     *            username
     * @param pPassword
     *            password
     * @return Usertoken.
     * @throws GDMException
     *             internal exception.
     */
    @WebResult(name = "userToken")
    public String login(@WebParam(name = "useName") String pUserName,
            @WebParam(name = "password") String pPassword) throws GDMException;

    /**
     * Logout.
     * 
     * @param pUserToken
     *            userToken.
     * @throws GDMException
     *             internal exception.
     */
    public void logout(@WebParam(name = "userToken") String pUserToken)
        throws GDMException;

    /**
     * Get the list of available business processes for the user
     * 
     * @param pUserToken
     *            Token identifying the user session
     * @return Array of business processes
     * @throws GDMException
     *             internal exception.
     */
    @WebResult(name = "businessProcessNames")
    public String[] getBusinessProcessNames(
            @WebParam(name = "userToken") String pUserToken)
        throws GDMException;

    /**
     * Get the list of product names for a given business process. This list
     * contains only products where the user has a role.
     * 
     * @param pUserToken
     *            Token identifying the user session
     * @param pBusinessProcess
     *            Business process the returned products belong to.
     * @return List of product names.
     * @throws GDMException
     *             internal exception.
     */
    @WebResult(name = "productNames")
    public Collection<String> getProductNames(
            @WebParam(name = "userToken") String pUserToken,
            @WebParam(name = "businessProcess") String pBusinessProcess)
        throws GDMException;

    /**
     * Get the list of products available for a given business process. This
     * list contains only products where given user has a role.
     * 
     * @param pUserToken
     *            Token identifying the user session
     * @param pBusinessProcess
     *            Business process the returned products belong to.
     * @return List of product data.
     * @throws GDMException
     *             internal exception.
     */
    @WebResult(name = "products")
    public Product[] getProducts(
            @WebParam(name = "userToken") String pUserToken,
            @WebParam(name = "businessProcess") String pBusinessProcess)
        throws GDMException;

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
     * @throws GDMException
     *             Internal exception.
     */
    @WebResult(name = "product")
    public Product getProduct(@WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "businessProcess") String pBusinessProcess,
            @WebParam(name = "productName") String pProductName)
        throws GDMException;

    /**
     * Get a product from its id.
     * 
     * @param pRoleToken
     *            Token identifying the role session.
     * @param pProductId
     *            The product id.
     * @return The product data.
     * @throws GDMException
     *             Internal exception.
     */
    @WebResult(name = "product")
    public Product getProductByKey(
            @WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "productId") String pProductId)
        throws GDMException;

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
     * @throws GDMException
     *             internal exception.
     */
    @WebResult(name = "rolesNames")
    public String[] getRolesNames(
            @WebParam(name = "userToken") String pUserToken,
            @WebParam(name = "businessProcessName") String pBusinessProcessName,
            @WebParam(name = "productName") String pProductName)
        throws GDMException;

    /**
     * Get the role name corresponding to a session token
     * 
     * @param pRoleToken
     *            Session token.
     * @return The role name.
     * @throws GDMException
     *             internal exception.
     */
    @WebResult(name = "roleName")
    public String getRoleName(@WebParam(name = "roleToken") String pRoleToken)
        throws GDMException;

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
     * @throws GDMException
     *             internal exception.
     */
    @WebResult(name = "roleToken")
    public String selectRole(@WebParam(name = "userToken") String pUserToken,
            @WebParam(name = "roleName") String pRoleName,
            @WebParam(name = "productName") String pProductName,
            @WebParam(name = "businessProcessName") String pBusinessProcessName)
        throws GDMException;

    /**
     * Lock the sheet with a specified type.
     * 
     * @param pRoleToken
     *            the session token
     * @param pSheetId
     *            the sheet id
     * @param pLockType
     *            The lock type
     * @throws GDMException
     *             internal exception.
     */
    public void lockSheet(
            @WebParam(name = "roleToken") final String pRoleToken,
            @WebParam(name = "sheetId") String pSheetId,
            @WebParam(name = "lockType") LockTypeEnumeration pLockType)
        throws GDMException;

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
     * @throws GDMException
     *             internal exception.
     */
    public void lockSheetForUser(
            @WebParam(name = "roleToken") final String pRoleToken,
            @WebParam(name = "sheetId") String pSheetId,
            @WebParam(name = "lock") Lock pLock) throws GDMException;

    /**
     * Get the lock set of a sheet, if any.
     * 
     * @param pRoleToken
     *            the session token
     * @param pSheetId
     *            the sheet id
     * @return Lock set on the sheet, or null if sheet is not locked.
     * @throws GDMException
     *             internal exception.
     */
    public Lock getLock(@WebParam(name = "roleToken") final String pRoleToken,
            @WebParam(name = "sheetId") final String pSheetId)
        throws GDMException;

    /**
     * Getter for sheet types.
     * 
     * @param pRoleToken
     *            roleToken.
     * @param pProcessName
     *            Name of business process.
     * @return SheetType array.
     * @throws GDMException
     *             internal exception.
     */
    @WebResult(name = "sheetTypes")
    public SheetType[] getSheetTypes(
            @WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "businessProcessName") String pProcessName)
        throws GDMException;

    /**
     * Getter for sheet types.
     * 
     * @param pRoleToken
     *            roleToken
     * @param pKeys
     *            Array of technical identifiers of sheet types
     * @return SheetType array.
     * @throws GDMException
     *             internal exception.
     */
    @WebResult(name = "sheetTypes")
    public SheetType[] getSheetTypesByKeys(
            @WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "sheetTypeIds") String[] pKeys)
        throws GDMException;

    /**
     * Getter for sheets.
     * 
     * @param pKeys
     *            key array
     * @param pRoleToken
     *            roleToken
     * @return SheetData array.
     * @throws GDMException
     *             internal exception.
     */
    @WebResult(name = "sheetDatas")
    public SheetData[] getSheetsByKeys(
            @WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "sheetIds") String[] pKeys) throws GDMException;

    /**
     * Get an 'empty' sheet corresponding to a given sheet type.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pTypeName
     *            Sheet type name
     * @param pProductName
     *            Product name
     * @param pCtxParams
     *            Array of context parameters (may be null)
     * @return The sheet data pre-filled with the default values.
     * @throws GDMException
     *             internal exception.
     */
    @WebResult(name = "sheetData")
    public SheetData getSheetModel(
            @WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "typeName") String pTypeName,
            @WebParam(name = "productName") String pProductName,
            @WebParam(name = "context") ContextParam[] pCtxParams)
        throws GDMException;

    /**
     * Getter for sheets.
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
     * @throws GDMException
     *             internal exception.
     */
    @WebResult(name = "sheetDatas")
    public SheetData[] getSheetsByRefs(
            @WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "businessProcessName") String pProcessName,
            @WebParam(name = "productName") String pProductName,
            @WebParam(name = "sheetRefs") String[] pRefs) throws GDMException;

    /**
     * Get information on a set of link types.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pKeys
     *            List of technical identifiers of link types
     * @return LinkType array.
     * @throws GDMException
     *             internal exception.
     */
    @WebResult(name = "linkTypes")
    public LinkType[] getLinkTypesByKeys(
            @WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "linkTypeIds") String[] pKeys) throws GDMException;

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
    @WebResult(name = "sheetTypeIds")
    public Collection<String> getLinkableSheetTypes(
            @WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "sheetTypeId") String pSheetTypeId);

    /**
     * Getter for links.
     * 
     * @param pContainerId
     *            container identifier.
     * @param pRoleToken
     *            roleToken
     * @return Link array.
     * @throws GDMException
     *             internal exception.
     */
    @WebResult(name = "linkDatas")
    public Link[] getLinks(@WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "containerId") String pContainerId)
        throws GDMException;

    /**
     * Getter for links.
     * 
     * @param pKeys
     *            key array
     * @param pRoleToken
     *            roleToken
     * @return Link array.
     * @throws GDMException
     *             internal exception.
     */
    @WebResult(name = "linkDatas")
    public Link[] getLinksByKeys(
            @WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "linkIds") String[] pKeys) throws GDMException;

    /**
     * Getter for product types.
     * 
     * @param pKeys
     *            key array
     * @param pRoleToken
     *            roleToken
     * @return ProductType array.
     * @throws GDMException
     *             internal exception.
     */
    @WebResult(name = "productTypes")
    public ProductType[] getProductTypesByKeys(
            @WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "productTypeIds") String[] pKeys)
        throws GDMException;

    /**
     * Getter for products.
     * 
     * @param pKeys
     *            key array
     * @param pRoleToken
     *            roleToken
     * @return ProductData array.
     * @throws GDMException
     *             internal exception.
     */
    @WebResult(name = "products")
    public Product[] getProductsByKeys(
            @WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "productIds") String[] pKeys) throws GDMException;

    /**
     * Get the sheet types associated with a process a given role can create.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pProcessName
     *            Name of business process
     * @return Array of sheet types data
     * @throws GDMException
     *             internal exception.
     */
    @WebResult(name = "creatableSheetTypes")
    public SheetType[] getCreatableSheetTypes(
            @WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "processName") String pProcessName)
        throws GDMException;

    /**
     * Create Sheets.
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
     * @throws GDMException
     *             internal exception.
     */
    @WebResult(name = "sheetId")
    public String createSheet(@WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "processName") String pProcessName,
            @WebParam(name = "productName") String pProductName,
            @WebParam(name = "sheetData") SheetData pSheetData)
        throws GDMException;

    /**
     * Create Sheets.
     * 
     * @param pRoleToken
     *            roleToken
     * @param pProcessName
     *            process Name
     * @param pProductName
     *            product Name
     * @param pSheetData
     *            sheetData
     * @param pCtxParams
     *            Array of context parameters (may be null)
     * @return Identifier of the created sheet.
     * @throws GDMException
     *             internal exception.
     */
    @WebResult(name = "sheetId")
    public String createSheetEx(
            @WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "processName") String pProcessName,
            @WebParam(name = "productName") String pProductName,
            @WebParam(name = "sheetData") SheetData pSheetData,
            @WebParam(name = "context") ContextParam[] pCtxParams)
        throws GDMException;

    /**
     * Update Sheets.
     * 
     * @param pSheetDatas
     *            sheetData array
     * @param pRoleToken
     *            roleToken
     * @param pProcessName
     *            process Name
     * @throws GDMException
     *             internal exception.
     */
    public void updateSheets(@WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "processName") String pProcessName,
            @WebParam(name = "sheetDatas") SheetData[] pSheetDatas)
        throws GDMException;

    /**
     * Update Sheets.
     * 
     * @param pRoleToken
     *            roleToken
     * @param pProcessName
     *            process Name
     * @param pSheetDatas
     *            sheetData array
     * @param pCtxParams
     *            Array of context parameters (may be null)
     * @throws GDMException
     *             internal exception.
     */
    public void updateSheetsEx(@WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "processName") String pProcessName,
            @WebParam(name = "sheetDatas") SheetData[] pSheetDatas,
            @WebParam(name = "context") ContextParam[] pCtxParams)
        throws GDMException;

    /**
     * Delete sheets.
     * 
     * @param pRoleToken
     *            Token of the caller's role.
     * @param pKeys
     *            Identifier array of sheets to delete in the database.
     * @throws GDMException
     *             internal exception.
     */
    public void deleteSheets(@WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "sheetIds") String[] pKeys) throws GDMException;

    /**
     * Delete sheets.
     * 
     * @param pRoleToken
     *            Token of the caller's role.
     * @param pKeys
     *            Identifier array of sheets to delete in the database.
     * @param pCtxParams
     *            Array of context parameters (may be null)
     * @throws GDMException
     *             internal exception.
     */
    public void deleteSheetsEx(@WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "sheetIds") String[] pKeys,
            @WebParam(name = "context") ContextParam[] pCtxParams)
        throws GDMException;

    /**
     * Get a list of possible links selectable for a given sheet type.
     * 
     * @param pRoleToken
     *            The role token.
     * @param pSheetTypeId
     *            Identifier of the sheet type.
     * @return Array of sheet link type identifiers
     * @throws GDMException
     *             internal exception.
     */
    @WebResult(name = "posibleLinkTypes")
    public String[] getPossibleLinkTypes(
            @WebParam(name = "roleToken") final String pRoleToken,
            @WebParam(name = "sheetTypeId") final String pSheetTypeId)
        throws GDMException;

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
     * @return {@link Link}
     * @throws GDMException
     *             internal exception.
     */
    @WebResult(name = "link")
    public Link createSheetLink(
            @WebParam(name = "roleToken") final String pRoleToken,
            @WebParam(name = "linkTypeName") final String pLinkTypeName,
            @WebParam(name = "sourceId") final String pSourceId,
            @WebParam(name = "destId") final String pDestId)
        throws GDMException;

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
     * @param pCtxParams
     *            Array of context parameters (may be null)
     * @return {@link Link}
     * @throws GDMException
     *             internal exception.
     */
    @WebResult(name = "link")
    public Link createSheetLinkEx(
            @WebParam(name = "roleToken") final String pRoleToken,
            @WebParam(name = "linkTypeName") final String pLinkTypeName,
            @WebParam(name = "sourceId") final String pSourceId,
            @WebParam(name = "destId") final String pDestId,
            @WebParam(name = "context") ContextParam[] pCtxParams)
        throws GDMException;

    /**
     * Update existing links content.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pLinks
     *            link data array.
     * @throws GDMException
     *             internal exception.
     */
    public void updateLinks(@WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "links") Link[] pLinks) throws GDMException;

    /**
     * Update existing links content.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pLinks
     *            link data array.
     * @param pCtxParams
     *            Array of context parameters (may be null)
     * @throws GDMException
     *             internal exception.
     */
    public void updateLinksEx(@WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "links") Link[] pLinks,
            @WebParam(name = "context") ContextParam[] pCtxParams)
        throws GDMException;

    /**
     * Delete a list of sheet links.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pKeys
     *            Identifier array of links to remove.
     * @throws GDMException
     *             internal exception.
     */
    public void deleteLinks(@WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "linkIds") String[] pKeys) throws GDMException;

    /**
     * Delete a list of sheet links.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pKeys
     *            Identifier array of links to remove.
     * @param pCtxParams
     *            Array of context parameters (may be null)
     * @throws GDMException
     *             internal exception.
     */
    public void deleteLinksEx(@WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "linkIds") String[] pKeys,
            @WebParam(name = "context") ContextParam[] pCtxParams)
        throws GDMException;

    /**
     * Get the process information regarding the life cycle of sheet array.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pSheetIds
     *            Identifier of sheets
     * @return Process information
     * @throws GDMException
     *             internal exception.
     */
    @WebResult(name = "processInformation")
    public ProcessInformation[] getSheetProcessInformations(
            @WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "sheetIds") String[] pSheetIds)
        throws GDMException;

    /**
     * Signal a transition on a sheet.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pSheetId
     *            Id of the sheet target.
     * @param pTransition
     *            name of the transition to perform.
     * @throws GDMException
     *             internal exception.
     */
    public void changeState(@WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "sheetId") String pSheetId,
            @WebParam(name = "transition") String pTransition)
        throws GDMException;

    /**
     * Signal a transition on a sheet.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pSheetId
     *            Id of the sheet target.
     * @param pTransition
     *            name of the transition to perform.
     * @param pCtxParams
     *            Execution context parameters
     * @throws GDMException
     *             internal exception.
     */
    public void changeStateEx(@WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "sheetId") String pSheetId,
            @WebParam(name = "transition") String pTransition,
            @WebParam(name = "context") ContextParam[] pCtxParams)
        throws GDMException;

    /**
     * Get the history of a sheet.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pSheetId
     *            Identifier of the sheet
     * @return Array containing the history info
     * @throws GDMException
     *             internal exception.
     */
    @WebResult(name = "sheetHistoryDatas")
    public SheetHistoryData[] getSheetHistory(
            @WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "sheetId") String pSheetId) throws GDMException;

    /**
     * Get the list of all values of a category defined for an environment.
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
     * @throws GDMException
     *             internal exception.
     */
    @WebResult(name = "environmentData")
    public EnvironmentData getEnvironmentCategory(
            @WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "businessProcessName") String pBusinessProcessName,
            @WebParam(name = "environmentName") String pEnvironmentName,
            @WebParam(name = "categoryName") String pCategoryName)
        throws GDMException;

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
     * @throws GDMException
     *             internal exception.
     */
    @WebResult(name = "categoryValues")
    public String[] getCategoryValues(
            @WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "categoryName") String pCategoryName,
            @WebParam(name = "businessProcessName") String pProcessName,
            @WebParam(name = "environmentName") String pEnvironmentName)
        throws GDMException;

    /**
     * Get all the ExecutableFilterData objects of a certain type , visible and
     * executable for the given businessProcess, product and user.
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
     * @throws GDMException
     *             internal exception.
     */
    @WebResult(name = "executableFilterNames")
    public String[] getVisibleExecutableFilterNamesByFilterType(
            @WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "businessProcessName") String pBusinessProcessName,
            @WebParam(name = "productName") String pProductName,
            @WebParam(name = "userLogin") String pUserLogin,
            @WebParam(name = "filterType") FilterTypeData pFilterType,
            @WebParam(name = "usage") FilterUsageEnum pUsage)
        throws GDMException;

    /**
     * Get all the ExecutableFilterData objects of a certain type , visible for
     * the given businessProcess, product and user, sorted by filter scope
     * (INSTANCE, PRODUCT, USER)
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
     * @return the visible filter names by types (Process, Product, User) in a
     *         map (FilterScope, list<filter names>)).
     * @throws GDMException
     *             internal exception.
     */
    @WebResult(name = "executableFilterNamesByContext")
    public List<String> getExecutableFilterNamesForContext(
            @WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "businessProcessName") String pBusinessProcessName,
            @WebParam(name = "productName") String pProductName,
            @WebParam(name = "userLogin") String pUserLogin,
            @WebParam(name = "filterType") FilterTypeData pFilterType,
            @WebParam(name = "usage") FilterUsageEnum pUsage,
            @WebParam(name = "filterScope") FilterScope pFilterScope)
        throws GDMException;

    /**
     * Get all the FilterData objects for a certain sheet type , visible for the
     * given businessProcess, product and user.
     * 
     * @param pRoleToken
     *            The role token
     * @param pSheetTypeName
     *            The sheet type name
     * @param pFilterScope
     *            The FilterScope
     * @return the visible filter ids.
     */
    @WebResult(name = "visibleFilterNames")
    public String[] getVisibleFilterNamesBySheetType(
            @WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "sheetTypeName") String pSheetTypeName,
            @WebParam(name = "filterScope") FilterScope pFilterScope);

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
     * @throws GDMException
     *             internal exception.
     * @see #executeSheetFilterWithScope(String, int, int, String, String,
     *      FilterScope) for a more precise control
     */
    @WebResult(name = "sheetSummaryDatas")
    public SheetSummaryData[] executeSheetFilter(
            @WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "businessProcessName") String pBusinessProcessName,
            @WebParam(name = "productName") String pProductName,
            @WebParam(name = "userLogin") String pUserLogin,
            @WebParam(name = "executableFilterName") String pFilterName)
        throws GDMException;

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
     * @throws GDMException
     *             internal exception.
     */
    @WebResult(name = "sheetSummaryDatas")
    public SheetSummaryData[] executeSheetFilterWithScope(
            @WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "maxResult") int pMaxResult,
            @WebParam(name = "firstResult") int pFirstResult,
            @WebParam(name = "productName") String pProductName,
            @WebParam(name = "executableFilterName") String pFilterName,
            @WebParam(name = "scope") FilterScope pScope) throws GDMException;

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
     * @throws GDMException
     *             internal exception.
     * @see #executeProductFilterWithScope(String, int, int, String, String,
     *      FilterScope) for a more precise control
     */
    @WebResult(name = "productSummaryDatas")
    public ProductSummaryData[] executeProductFilter(
            @WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "businessProcessName") String pBusinessProcessName,
            @WebParam(name = "productName") String pProductName,
            @WebParam(name = "userLogin") String pUserLogin,
            @WebParam(name = "executableFilterName") String pFilterName)
        throws GDMException;

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
     * @throws GDMException
     *             internal exception.
     */
    @WebResult(name = "productSummaryDatas")
    public ProductSummaryData[] executeProductFilterWithScope(
            @WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "maxResult") int pMaxResult,
            @WebParam(name = "firstResult") int pFirstResult,
            @WebParam(name = "productName") String pProductName,
            @WebParam(name = "executableFilterName") String pFilterName,
            @WebParam(name = "scope") FilterScope pScope) throws GDMException;

    /**
     * Get the content of an attached file.
     * 
     * @param pRoleToken
     *            Role token session.
     * @param pAttachedFieldValueId
     *            Identifier of the sheet in the database.
     * @return Byte array containing the file content.
     * @throws GDMException
     *             internal exception.
     */
    @WebResult(name = "attachedFileContent")
    public byte[] getAttachedFileContent(
            @WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "attachedFieldValueId") String pAttachedFieldValueId)
        throws GDMException;

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
     * @throws GDMException
     *             internal exception.
     */
    void set(@WebParam(name = "containerId") String pElemId,
            @WebParam(name = "attributeDatas") AttributeData[] pAttrs)
        throws GDMException;

    /**
     * Get some attributes of an element.
     * 
     * @param pElemId
     *            Unique identifier of the attributes container.
     * @param pAttrNames
     *            Names of the attributes to retrieve
     * @return Array containing the attributes.
     * @throws GDMException
     *             internal exception.
     */
    @WebResult(name = "attributeDatas")
    AttributeData[] get(@WebParam(name = "containerId") String pElemId,
            @WebParam(name = "attributeNames") String[] pAttrNames)
        throws GDMException;

    /**
     * Get all attributes defined for an element.
     * 
     * @param pElemId
     *            Identifier of the attributes container.
     * @return Array containing the attributes.
     * @throws GDMException
     *             internal exception.
     */
    @WebResult(name = "attributeDatas")
    AttributeData[] getAll(@WebParam(name = "containerId") String pElemId)
        throws GDMException;

    /**
     * Remove all attributes defined on an element.
     * 
     * @param pElemId
     *            Identifier of the attributes container.
     * @throws GDMException
     *             internal exception.
     */
    void removeAll(@WebParam(name = "containerId") String pElemId)
        throws GDMException;

    /**
     * Get the names of all extended attributes defined for an element.
     * 
     * @param pElemId
     *            Identifier of the attributes container.
     * @return Array containing the names of all attributes (sorted
     *         alphabetically).
     * @throws GDMException
     *             internal exception.
     */
    @WebResult(name = "attributeNames")
    String[] getAttrNames(@WebParam(name = "containerId") String pElemId)
        throws GDMException;

    /**
     * Get the list of global attribute names
     * 
     * @return a list of global attribute names
     * @throws GDMException
     *             internal exception.
     */
    @WebResult(name = "attributeNames")
    public String[] getGlobalAttrNames() throws GDMException;

    /**
     * Get a list of global attributes data from their names
     * 
     * @param pAttrNames
     *            the attribute names
     * @return A list of AttributeData objects
     * @throws GDMException
     *             internal exception.
     */
    @WebResult(name = "attributeDatas")
    public AttributeData[] getGlobalAttributes(
            @WebParam(name = "attributeNames") String[] pAttrNames)
        throws GDMException;

    /**
     * Set a list of global attributes
     * 
     * @param pRoleToken
     *            the role token
     * @param pAttributesData
     *            the list of AttributeData
     * @throws GDMException
     *             internal exception.
     */
    public void setGlobalAttributes(
            @WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "attributeDatas") AttributeData[] pAttributesData)
        throws GDMException;

    /**
     * Get information on a user from this roleToken.
     * 
     * @param pRoleToken
     *            roleToken
     * @return User object.
     * @throws GDMException
     *             internal exception.
     */
    @WebResult(name = "user")
    public User getUserFromRole(@WebParam(name = "roleToken") String pRoleToken)
        throws GDMException;

    /**
     * Get the list of users with a given role on a given list of products
     * 
     * @param pBusinessProcessName
     *            processName
     * @param pRoleName
     *            roleName
     * @param pProductNames
     *            A list of product names (can be null)
     * @param pIsInstanceRoleAllowed
     *            boolean (true if instance roles are allowed, false if only
     *            productRoles)
     * @param pIsRoleOnAllProducts
     *            true if the resulting users must have the given role on each
     *            product from the list, false if the resulting users must have
     *            the given role on at least one product from the list.
     * @return a list of user logins, with the role on the products
     * @throws GDMException
     *             when roleName is invalid.
     */
    @WebResult(name = "userLogins")
    public String[] getUsersWithRole(
            @WebParam(name = "processName") String pBusinessProcessName,
            @WebParam(name = "roleName") String pRoleName,
            @WebParam(name = "productName") String[] pProductNames,
            @WebParam(name = "isInstanceRoleAllowed") boolean pIsInstanceRoleAllowed,
            @WebParam(name = "isRoleOnAllProduct") boolean pIsRoleOnAllProducts)
        throws GDMException;

    /**
     * Get a user data structure from its login.
     * 
     * @param pLogin
     *            the user login
     * @return the user data
     * @throws GDMException
     *             internal exception.
     */
    @WebResult(name = "user")
    public User getUserFromLogin(@WebParam(name = "login") String pLogin)
        throws GDMException;

    /**
     * Get the sheet state name from the sheet ID
     * 
     * @param pSheetId
     *            the current sheet ID
     * @return the sheet state name
     * @throws GDMException
     *             internal exception.
     */
    @WebResult(name = "stateName")
    public String getSheetState(@WebParam(name = "sheetId") String pSheetId)
        throws GDMException;

    /**
     * Set the defined role on the given product (or on instance if no product)
     * for a list of users.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pLoginNames
     *            Login names of the users
     * @param pRoleName
     *            Name of the role to set
     * @param pProductName
     *            Name of the product on which this role must be set (or null if
     *            instance role)
     * @param pBusinessProcessName
     *            Name of the business process
     * @throws GDMException
     *             internal exception.
     */
    public void addRoleForUsers(
            @WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "logins") String[] pLoginNames,
            @WebParam(name = "roleName") String pRoleName,
            @WebParam(name = "productName") String pProductName,
            @WebParam(name = "processName") String pBusinessProcessName)
        throws GDMException;

    /**
     * Remove the defined role on the given product (or on instance if no
     * product) for a list of users.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pLoginNames
     *            Login names of the users
     * @param pRoleName
     *            Name of the role to remove
     * @param pProductName
     *            Name of the product on which this role must be removed (or
     *            null if instance role)
     * @param pBusinessProcessName
     *            Name of the business process
     * @throws GDMException
     *             internal exception.
     */
    public void removeRoleForUsers(
            @WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "logins") String[] pLoginNames,
            @WebParam(name = "roleName") String pRoleName,
            @WebParam(name = "productName") String pProductName,
            @WebParam(name = "processName") String pBusinessProcessName)
        throws GDMException;

    /**
     * Get a list of all acceptable values for a 'choiceString' field.
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
     * @throws GDMException
     *             internal exception.
     * @deprecated use
     *             {@link Services#getChoiceStringData(String, String, String, ContextParam[])}
     *             instead
     * @since 2.0.5
     */
    @WebResult(name = "choiceStringList")
    public List<String> getChoiceStringList(
            @WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "typeId") String pTypeId,
            @WebParam(name = "fieldId") String pFieldId,
            @WebParam(name = "context") ContextParam[] pCtxParams)
        throws GDMException;

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
     * @throws GDMException
     *             internal exception.
     */
    @WebResult(name = "choiceStringData")
    public HashMap<String, Object> getChoiceStringData(
            @WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "typeId") String pTypeId,
            @WebParam(name = "fieldId") String pFieldId,
            @WebParam(name = "context") ContextParam[] pCtxParams)
        throws GDMException;

    /**
     * Execute an executable filter data, return resulting containers id
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
     *            the filter name
     * @return the collection of containers id
     * @throws GDMException
     *             internal exception.
     */
    @WebResult(name = "filterResultIds")
    public Collection<String> executeFilter(
            @WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "processName") String pBusinessProcessName,
            @WebParam(name = "productName") String pProductName,
            @WebParam(name = "userLogin") String pUserLogin,
            @WebParam(name = "filterName") String pFilterName)
        throws GDMException;

    /**
     * Get the IDs of elements linked to current element with specific link
     * type.
     * 
     * @param pRoleToken
     *            the sesion token
     * @param pValuesContainerId
     *            Identifier of the current values container.
     * @param pLinkTypeName
     *            Name of the link type.
     * @return List of linked container IDs
     * @throws GDMException
     *             internal exception.
     */
    @WebResult(name = "linkedElementsIds")
    public List<String> getLinkedElementsIds(
            @WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "containerId") final String pValuesContainerId,
            @WebParam(name = "linkTypeName") final String pLinkTypeName)
        throws GDMException;

    /**
     * Get the list of user role names allowed for a given user
     * 
     * @param pUserToken
     *            Authorization token.
     * @param pBusinessProcessName
     *            Name of business process
     * @param pProductName
     *            Name of the product.
     * @param pRoleProperties
     *            Properties indicating if the role to find are on instance/on
     *            product/on both
     * @return List of allowed role names.
     * @throws GDMException
     *             internal exception.
     */
    @WebResult(name = "rolesNamesProps")
    public Collection<String> getRolesNamesWithProperties(
            @WebParam(name = "userToken") String pUserToken,
            @WebParam(name = "processName") String pBusinessProcessName,
            @WebParam(name = "productName") String pProductName,
            @WebParam(name = "roleProps") RoleProperties pRoleProperties)
        throws GDMException;

    /**
     * Get information on all users.
     * 
     * @return List of user data for all users.
     * @throws GDMException
     *             internal exception.
     */
    @WebResult(name = "users")
    public User[] getAllUsers() throws GDMException;

    /**
     * Get the language to be used for a user.
     * 
     * @param pToken
     *            Role or user session token
     * @return Language to use
     * @throws GDMException
     *             internal exception.
     */
    String getUserLanguage(String pToken) throws GDMException;

    /**
     * Get values in the user preferred language If a value isn't found in this
     * language, then the default language is used.
     * 
     * @param pToken
     *            User or role session token.
     * @param pKeys
     *            values keys array.
     * @return translated values array.
     * @throws GDMException
     *             internal exception.
     */
    @WebResult(name = "translatedValues")
    public String[] getValuesForUser(@WebParam(name = "token") String pToken,
            @WebParam(name = "valuesId") String[] pKeys) throws GDMException;

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
     * @throws GDMException
     *             internal exception.
     */
    @WebResult(name = "extendedActionResult")
    public ExtendedActionResult executeExtendedAction(
            @WebParam(name = "token") String pRoleToken,
            @WebParam(name = "extendedActionName") String pExtendedActionName,
            @WebParam(name = "sheetId") String pSheetId,
            @WebParam(name = "sheetIds") Collection<String> pSheetIds,
            @WebParam(name = "inputData") InputData pInputData)
        throws GDMException;

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
     * @throws GDMException
     *             internal exception.
     */
    @WebResult(name = "revisionId")
    public String createRevision(
            @WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "containerId") String pContainerId,
            @WebParam(name = "label") String pLabel) throws GDMException;

    /**
     * Get properties of a revision having pRevisionId on container pContainerId
     * 
     * @param pRoleToken
     *            Role session token
     * @param pContainerId
     *            Container identifier
     * @param pRevisionId
     *            Revision identifier
     * @throws GDMException
     *             internal exception.
     * @return The revision data structure
     */
    @WebResult(name = "revisionData")
    public RevisionData getRevision(
            @WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "containerId") String pContainerId,
            @WebParam(name = "revisionId") String pRevisionId)
        throws GDMException;

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
     * @throws GDMException
     *             internal exception.
     */
    @WebResult(name = "revisionData")
    public RevisionData getRevisionByLabel(
            @WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "containerId") String pContainerId,
            @WebParam(name = "label") String pLabel) throws GDMException;

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
     * @return The sheet
     * @throws GDMException
     *             internal exception.
     */
    @WebResult(name = "sheetData")
    public SheetData getSheetDataInRevision(
            @WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "containerId") String pContainerId,
            @WebParam(name = "revisionId") String pRevisionId)
        throws GDMException;

    /**
     * Get a sheet in a specific revision identified by a revision Id and a
     * container Id
     * 
     * @param pRoleToken
     *            The role token
     * @param pContainerId
     *            Container identifier
     * @param pRevisionLabel
     *            Revision label
     * @return The sheet
     * @throws GDMException
     *             internal exception.
     */
    @WebResult(name = "sheetData")
    public SheetData getSheetDataByRevisionLabel(
            @WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "containerId") String pContainerId,
            @WebParam(name = "revisionLabel") String pRevisionLabel)
        throws GDMException;

    /**
     * Delete the latest revision on container pContainerId
     * 
     * @param pRoleToken
     *            Role session token
     * @param pContainerId
     *            Container identifier
     * @throws GDMException
     *             internal exception.
     */
    public void deleteRevision(@WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "containerId") String pContainerId)
        throws GDMException;

    /**
     * Get the number of revisions on the container
     * 
     * @param pRoleToken
     *            Role session token
     * @param pContainerId
     *            Container identifier
     * @throws GDMException
     *             internal exception.
     * @return Number of revisions
     */
    @WebResult(name = "revisionsCount")
    public int getRevisionsCount(
            @WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "containerId") String pContainerId)
        throws GDMException;

    /**
     * Get a summary of revisions created on a container
     * 
     * @param pRoleToken
     *            The role token
     * @param pContainerId
     *            Container identifier
     * @return Collection containing a summary of each revision
     * @throws GDMException
     *             internal exception.
     */
    @WebResult(name = "revisionSummaryDatas")
    public Collection<RevisionSummaryData> getRevisionsSummary(
            @WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "containerId") String pContainerId)
        throws GDMException;

    /**
     * * Empty method created for access to override types.
     * 
     * @param pChoiceField
     *            ChoiceField
     * @param pSimpleField
     *            SimpleField
     * @param pAttachedField
     *            AttachedField
     * @param pAttachedFieldValueData
     *            AttachedFieldValueData
     * @param pTextDisplayHint
     *            TextDisplayHint
     * @param pChoiceTreeDisplayHint
     *            ChoiceTreeDisplayHint
     * @param pChoiceDisplayHint
     *            ChoiceDisplayHint
     * @param pChoiceStringDisplayHint
     *            ChoiceStringDisplayHint
     * @param pAttachedDisplayHint
     *            AttachedDisplayHint
     * @param pStringParam
     *            StringParam
     * @param pBoolParam
     *            BoolParam
     * @param pIntParam
     *            IntParam
     * @param pListStrParam
     *            ListStrParam
     * @param pOperationData
     *            OperationData
     * @param pBooleanValueData
     *            BooleanValueData
     * @param pDateValueData
     *            DateValueData
     * @param pIntegerValueData
     *            IntegerValueData
     * @param pRealValueData
     *            RealValueData
     * @param pStringValueData
     *            StringValueData
     * @param pScalarValueData
     *            ScalarValueData
     * @param pCriteriaData
     *            CriteriaData
     * @param pUsableTypeData
     *            UsableTypeData
     * @param pUsableFieldData
     *            UsableFieldData
     * @param pVirtualFieldData
     *            VirtualFieldData
     * @param pCriteriaFieldData
     *            CriteriaFieldData
     * @param pPointerFieldValueData
     *            PointerFieldValueData
     */
    public void overrideTypes(ChoiceField pChoiceField,
            SimpleField pSimpleField, AttachedField pAttachedField,
            AttachedFieldValueData pAttachedFieldValueData,
            TextDisplayHint pTextDisplayHint,
            ChoiceDisplayHint pChoiceDisplayHint,
            ChoiceTreeDisplayHint pChoiceTreeDisplayHint,
            ChoiceStringDisplayHint pChoiceStringDisplayHint,
            AttachedDisplayHint pAttachedDisplayHint, StringParam pStringParam,
            BooleanParam pBoolParam, IntegerParam pIntParam,
            StringsListParam pListStrParam, OperationData pOperationData,
            BooleanValueData pBooleanValueData, DateValueData pDateValueData,
            IntegerValueData pIntegerValueData, RealValueData pRealValueData,
            StringValueData pStringValueData, ScalarValueData pScalarValueData,
            CriteriaData pCriteriaData, UsableTypeData pUsableTypeData,
            UsableFieldData pUsableFieldData,
            VirtualFieldData pVirtualFieldData,
            CriteriaFieldData pCriteriaFieldData,
            PointerFieldValueData pPointerFieldValueData);

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
     * @throws GDMException
     *             internal exception.
     */
    @WebResult(name = "sheetType")
    public SheetType getSheetTypeWithAccessControls(
            @WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "processName") String pProcessName,
            @WebParam(name = "productName") String pProductName,
            @WebParam(name = "stateName") String pStateName,
            @WebParam(name = "sheetTypeName") String pSheetTypeName)
        throws GDMException;

    /**
     * Test values container existence.
     * 
     * @param pValuesContainerId
     *            Identifier of the values container to test.
     * @return True if the values container exists, false otherwise
     */
    public boolean isValuesContainerExists(
            @WebParam(name = "valuesContainerId") String pValuesContainerId);

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
     * @throws GDMException
     *             Error if the schema is invalid or if the content to import
     *             has unauthorized type (ImportFlag.ERROR)
     * @throws ImportException
     *             service import exception.
     */
    @WebResult(name = "importExecutionReport")
    public ImportExecutionReport importData(
            @WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "inputStream") byte[] pInputStream,
            @WebParam(name = "importProperties") ImportProperties pProperties)
        throws GDMException, ImportException;

    /**
     * Export the data of an instance on an XML file.
     * 
     * @param pRoleToken
     *            The role token.
     * @param pProperties
     *            The export properties.
     * @return The output stream containing the exported data.
     */
    @WebResult(name = "outputStream")
    public byte[] exportData(@WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "exportProperties") ExportProperties pProperties);

    /**
     * Generate the sheet report of selected sheets on a specified export
     * format.
     * 
     * @param pRoleToken
     *            the role token.
     * @param pSheetIds
     *            the sheets id.
     * @param pExportFormat
     *            the export format.
     * @param pReportName
     *            the report name.
     * @return the output stream in byte array.
     * @throws GDMException
     *             internal exception.
     */
    @WebResult(name = "outputStream")
    public byte[] generateSheetReport(
            @WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "sheetIds") List<String> pSheetIds,
            @WebParam(name = "exportFormat") SheetExportFormat pExportFormat,
            @WebParam(name = "reportName") String pReportName)
        throws GDMException;

    /**
     * Generate the filter result report of selected sheets on a specified
     * export format.
     * 
     * @param pRoleToken
     *            the role token.
     * @param pExportFormat
     *            the export format.
     * @param pFilterName
     *            the filter name.
     * @param pFilterScope
     *            the filter scope
     * @return the output stream in byte array.
     * @throws GDMException
     *             internal exception.
     */
    @WebResult(name = "outputStream")
    public byte[] generateFilterResultReport(
            @WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "exportFormat") SheetExportFormat pExportFormat,
            @WebParam(name = "filterName") String pFilterName,
            @WebParam(name = "filterScope") FilterScope pFilterScope)
        throws GDMException;

    /**
     * Set the content of an attached file.
     * 
     * @param pRoleToken
     *            Role token session.
     * @param pSheetId
     *            The sheet id
     * @param pAttachedFieldValueId
     *            Identifier of the sheet in the database.
     * @param pAttachedFileContent
     *            Byte array containing the file content.
     * @throws GDMException
     *             internal exception.
     */
    public void setAttachedFileContentWithSheetId(
            @WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "sheetId") String pSheetId,
            @WebParam(name = "attachedFieldValueId") String pAttachedFieldValueId,
            @WebParam(name = "attachedFileContent") byte[] pAttachedFileContent)
        throws GDMException;

    /**
     * Set the content of an attached file. instead use the method
     * <tt>setAttachedFileContentWithSheetId</tt> with a sheet id
     * 
     * @param pRoleToken
     *            Role token session.
     * @param pAttachedFieldValueId
     *            Identifier of the sheet in the database.
     * @param pAttachedFileContent
     *            Byte array containing the file content.
     * @throws GDMException
     *             internal exception.
     */
    @Deprecated
    public void setAttachedFileContent(
            @WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "attachedFieldValueId") String pAttachedFieldValueId,
            @WebParam(name = "attachedFileContent") byte[] pAttachedFileContent)
        throws GDMException;

    /**
     * Get filter by ID
     * 
     * @param pRoleToken
     *            Role token session.
     * @param pFilterId
     *            Identifier of the filter
     * @return The expected filter
     */
    ExecutableFilterData getExecutableFilter(
            @WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "filterId") String pFilterId);

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
    ExecutableFilterData getExecutableFilterByName(
            @WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "processName") String pProcessName,
            @WebParam(name = "productName") String pFilterProductName,
            @WebParam(name = "userLogin") String pFilterUserLogin,
            @WebParam(name = "filterName") String pFilterName);

    /**
     * Create or Update a filter. If the ExecutableFilterData contains a
     * non-null ID, makes an update, else create a new Filter.
     * 
     * @param pRoleToken
     *            Role token session.
     * @param pExecutableFilterData
     *            The filter
     */
    void createOrUpdateExecutableFilter(
            @WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "executableFilterData") ExecutableFilterData pExecutableFilterData);

    /**
     * Delete filter by ID
     * 
     * @param pRoleToken
     *            Role token session.
     * @param pFilterId
     *            Identifier of the filter
     */
    void deleteExecutableFilter(
            @WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "filterId") String pFilterId);

    /**
     * Get the container names that can be used for the requested filter type.
     * It also checks the user authorization to access the corresponding
     * containers
     * 
     * @param pRoleToken
     *            Role token session.
     * @param pFilterType
     *            The filter type
     * @return The containers identifiers
     */
    List<String> getSearcheableContainers(
            @WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "filterType") FilterTypeData pFilterType);

    /**
     * Get the field names that can be used for the requested containers,
     * meaning the common fields for the containers in argument. It also checks
     * the user authorizations to access the corresponding fields
     * 
     * @param pRoleToken
     *            Role token session.
     * @param pContainerIds
     *            The containers identifiers
     * @return The field labels
     */
    List<String> getSearcheableFieldsLabel(
            @WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "containerIds") List<String> pContainerIds);

    /**
     * Get the filters fields maximum depth
     * 
     * @return The filter fields maximum depth
     */
    int getFilterFieldsMaxDepth();

    /**
     * Get the list of scopes of executable filters
     * 
     * @param pRoleToken
     *            Role token session.
     * @return List of the scopes of executable filters
     */
    List<FilterScope> getExecutableFilterScope(
            @WebParam(name = "roleToken") String pRoleToken);

    /**
     * Get the list of scopes of editable filters
     * 
     * @param pRoleToken
     *            Role token session.
     * @return List of the scopes of editable filters
     */
    List<FilterScope> getEditableFilterScope(
            @WebParam(name = "roleToken") String pRoleToken);

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
    FilterResult executeFilterData(
            @WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "processName") String pBusinessProcessName,
            @WebParam(name = "productName") String pProductName,
            @WebParam(name = "userLogin") String pUserLogin,
            @WebParam(name = "executableFilterData") ExecutableFilterData pExecutableFilterData);

    /**
     * Get the available extended actions
     * 
     * @param pRoleToken
     *            The role token session.
     * @param pGuiContexts
     *            The GUI context list
     * @param pContainerId
     *            The container identifier (use business process if null)
     * @return The list of available extended actions
     * @throws GDMException
     *             internal exception
     */
    @WebResult(name = "extendedActionSummaries")
    public List<ExtendedActionSummary> getAvailableExtendedActions(
            @WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "guiContexts") List<GuiContext> pGuiContexts,
            @WebParam(name = "containerId") String pContainerId)
        throws GDMException;

    /**
     * Get a inputData type
     * 
     * @param pRoleToken
     *            The role token session.
     * @param pInputDataTypeId
     *            The identifier
     * @return The inputData type
     * @throws GDMException
     *             internal exception
     */
    @WebResult(name = "inputDataType")
    public InputDataType getInputDataType(
            @WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "inputDataTypeId") String pInputDataTypeId)
        throws GDMException;

    /**
     * Get an initialized input data
     * 
     * @param pRoleToken
     *            The role token session.
     * @param pInputDataTypeId
     *            The identifier
     * @return The inputData
     * @throws GDMException
     *             internal exception
     */
    @WebResult(name = "inputData")
    public InputData getInputDataModel(
            @WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "inputDataTypeId") String pInputDataTypeId)
        throws GDMException;

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
     *            the current sheet identifier (if the action is executed on a
     *            sheet).
     * @param pSheetIds
     *            the current sheet identifiers (if the action is executed on a
     *            filter result).
     * @param pInputData
     *            The extended action inputData
     * @return The extended action result gives parameters according to the
     *         resultingScreen
     * @throws GDMException
     *             internal exception
     */
    @WebResult(name = "extendedActionResult")
    public ExtendedActionResult executeExtendedActionByContainer(
            @WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "extendedActionName") String pExtendedActionName,
            @WebParam(name = "extensionsContainerTypeId") String pExtensionsContainerTypeId,
            @WebParam(name = "sheetId") String pSheetId,
            @WebParam(name = "sheetIds") List<String> pSheetIds,
            @WebParam(name = "inputData") InputData pInputData)
        throws GDMException;

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
     * @throws GDMException
     *             internal exception
     */
    @WebResult(name = "extendedActionResult")
    public List<String> getAvailableReportModels(
            @WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "sheetIds") Collection<String> pSheetIds,
            @WebParam(name = "exportFormat") SheetExportFormat pExportFormat)
        throws GDMException;

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
     * @throws GDMException
     *             internal exception
     */
    @WebResult(name = "linkType")
    public LinkType getLinkType(
            @WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "linkTypeId") String pLinkTypeId,
            @WebParam(name = "displayedContainerId") String pDisplayedContainerId)
        throws GDMException;

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
     * @throws GDMException
     *             internal exception
     */
    @WebResult(name = "actionAccessControlDatas")
    public List<ActionAccessControlData> getActionAccessControls(
            @WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "accessControlContextData") AccessControlContextData pAccessControlContextData,
            @WebParam(name = "actionNames") List<String> pActionNames)
        throws GDMException;

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
     * @throws GDMException
     *             internal exception
     */
    @WebResult(name = "categoryValues")
    public List<String> getCategoryValuesByProduct(
            @WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "productName") String pProductName,
            @WebParam(name = "categoryName") String pCategoryName)
        throws GDMException;

    /**
     * Returns the list of all virtual fields
     * 
     * @return The list of all virtual fields
     */
    @WebResult(name = "virtualFieldTypes")
    public List<String> getAllVirtualFieldTypes();

    /**
     * Return the list of the root product nodes of the product hierarchy,
     * themselves pointing on their sub product nodes.
     * 
     * @param pUserToken
     *            the user token
     * @param pProcessName
     *            the process name
     * @return the list of the top products
     */
    @WebResult(name = "productNodes")
    public List<ProductNode> getProductsAsTree(
            @WebParam(name = "userToken") String pUserToken,
            @WebParam(name = "processName") String pProcessName);

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
    @WebResult(name = "availableExtendedActions")
    public List<ExtendedActionSummary> getAvailableExtendedActionsForExtensionContainer(
            @WebParam(name = "userToken") String pRoleToken,
            @WebParam(name = "guiContexts") List<GuiContext> pGuiContexts,
            @WebParam(name = "extensionContainerId") String pExtensionContainerId);

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
    @WebResult(name = "initializedSheetData")
    public SheetData initializeSheet(
            @WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "resultSheetTypeId") String pResultSheetTypeId,
            @WebParam(name = "sourceSheetId") String pSourceSheetId)
        throws GDMException;
    
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
    @WebResult(name = "duplicatedSheetData")
    public SheetData duplicateSheet(
            @WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "sheetId") String pSheetId) throws GDMException;

    /**
     * Get all access control data for a product and a role (which means access
     * controls for every (sheet type/sheet state) combination.
     * 
     * @param pRoleToken
     *            The role token session.
     * @param pAccessControlContextData
     *            The access control context. Set access control context
     *            attributes to null for using default values.
     * @param pActionNames
     *            The actions names
     * @return The action access
     * @throws GDMException
     *             internal exception
     */
    @WebResult(name = "actionAccessControlByProductAndRoleDatas")
    public List<ActionAccessControlData> getActionAccessControlsByProductAndRole(
            @WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "accessControlContextData") AccessControlContextData pAccessControlContextData,
            @WebParam(name = "actionNames") List<String> pActionNames)
        throws GDMException;

    
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
    @WebResult(name = "fieldValueDatas")
    public List<FieldValueData> getPointedFieldValue(
            @WebParam(name = "roleToken") String pRoleToken,
            @WebParam(name = "fieldLabel") String pFieldLabel,
            @WebParam(name = "referencedContainerId") String pReferencedContainerId,
            @WebParam(name = "referencedFieldLabel") String pReferencedFieldLabel);
}
