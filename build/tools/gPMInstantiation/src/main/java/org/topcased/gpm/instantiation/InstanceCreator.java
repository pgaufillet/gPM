/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Sophian Idjellidaine (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.instantiation;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.topcased.gpm.business.ContextLocator;
import org.topcased.gpm.business.ServiceLocator;
import org.topcased.gpm.business.attributes.AttributeData;
import org.topcased.gpm.business.attributes.service.AttributesService;
import org.topcased.gpm.business.authorization.impl.filter.FilterAccessContraint;
import org.topcased.gpm.business.authorization.service.AccessControlContextData;
import org.topcased.gpm.business.authorization.service.AccessControlData;
import org.topcased.gpm.business.authorization.service.ActionAccessControlData;
import org.topcased.gpm.business.authorization.service.AdminAccessControlData;
import org.topcased.gpm.business.authorization.service.AuthorizationService;
import org.topcased.gpm.business.authorization.service.FieldAccessControlData;
import org.topcased.gpm.business.authorization.service.TransitionAccessControlData;
import org.topcased.gpm.business.authorization.service.TypeAccessControlData;
import org.topcased.gpm.business.display.service.DisplayService;
import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.exception.FieldValidationException;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.exception.ImportException;
import org.topcased.gpm.business.exception.InstantiateException;
import org.topcased.gpm.business.exception.InvalidNameException;
import org.topcased.gpm.business.exception.MandatoryValuesException;
import org.topcased.gpm.business.extensions.service.ActionData;
import org.topcased.gpm.business.extensions.service.CommandData;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.extensions.service.ExtendedActionData;
import org.topcased.gpm.business.extensions.service.ExtensionsService;
import org.topcased.gpm.business.extensions.service.ScriptData;
import org.topcased.gpm.business.facilities.AttachedDisplayHintData;
import org.topcased.gpm.business.facilities.ChoiceFieldDisplayHintData;
import org.topcased.gpm.business.facilities.ChoiceTreeDisplayHintData;
import org.topcased.gpm.business.facilities.DateDisplayHintData;
import org.topcased.gpm.business.facilities.DisplayGroupData;
import org.topcased.gpm.business.facilities.DisplayHintData;
import org.topcased.gpm.business.facilities.GridDisplayHintData;
import org.topcased.gpm.business.facilities.TextFieldDisplayHintData;
import org.topcased.gpm.business.fields.AttachedFieldData;
import org.topcased.gpm.business.fields.AttachedFieldModificationData;
import org.topcased.gpm.business.fields.ChoiceFieldData;
import org.topcased.gpm.business.fields.FieldAccessData;
import org.topcased.gpm.business.fields.FieldSummaryData;
import org.topcased.gpm.business.fields.FieldType;
import org.topcased.gpm.business.fields.FieldTypeData;
import org.topcased.gpm.business.fields.JAppletDisplayHintData;
import org.topcased.gpm.business.fields.MultipleFieldData;
import org.topcased.gpm.business.fields.PointerFieldData;
import org.topcased.gpm.business.fields.SimpleFieldData;
import org.topcased.gpm.business.fields.ValueType;
import org.topcased.gpm.business.fields.service.FieldsService;
import org.topcased.gpm.business.fieldscontainer.service.FieldsContainerService;
import org.topcased.gpm.business.i18n.service.I18nService;
import org.topcased.gpm.business.importation.ElementReport;
import org.topcased.gpm.business.importation.ImportExecutionReport;
import org.topcased.gpm.business.importation.ImportProperties;
import org.topcased.gpm.business.importation.ImportProperties.ImportFlag;
import org.topcased.gpm.business.importation.impl.report.ElementType;
import org.topcased.gpm.business.importation.service.ImportService;
import org.topcased.gpm.business.instance.service.InstanceService;
import org.topcased.gpm.business.lifecycle.service.LifeCycleService;
import org.topcased.gpm.business.link.service.LinkService;
import org.topcased.gpm.business.link.service.LinkTypeData;
import org.topcased.gpm.business.process.service.BusinessProcessData;
import org.topcased.gpm.business.product.service.ProductService;
import org.topcased.gpm.business.product.service.ProductTypeData;
import org.topcased.gpm.business.report.ReportModelData;
import org.topcased.gpm.business.report.service.ReportingService;
import org.topcased.gpm.business.revision.service.RevisionService;
import org.topcased.gpm.business.scalar.ScalarValueData;
import org.topcased.gpm.business.search.criterias.CriteriaData;
import org.topcased.gpm.business.search.criterias.CriteriaFieldData;
import org.topcased.gpm.business.search.criterias.OperationData;
import org.topcased.gpm.business.search.impl.FilterCreator;
import org.topcased.gpm.business.search.impl.query.SqlFunction;
import org.topcased.gpm.business.search.service.SearchService;
import org.topcased.gpm.business.search.service.UsableFieldData;
import org.topcased.gpm.business.serialization.converter.XMLConverter;
import org.topcased.gpm.business.serialization.data.AccessCtl;
import org.topcased.gpm.business.serialization.data.Action;
import org.topcased.gpm.business.serialization.data.ActionAccessCtl;
import org.topcased.gpm.business.serialization.data.AdminAccessCtl;
import org.topcased.gpm.business.serialization.data.AttachedDisplayHint;
import org.topcased.gpm.business.serialization.data.AttachedField;
import org.topcased.gpm.business.serialization.data.AttachedFieldValueData;
import org.topcased.gpm.business.serialization.data.Attribute;
import org.topcased.gpm.business.serialization.data.AttributeValue;
import org.topcased.gpm.business.serialization.data.Category;
import org.topcased.gpm.business.serialization.data.ChoiceDisplayHint;
import org.topcased.gpm.business.serialization.data.ChoiceField;
import org.topcased.gpm.business.serialization.data.ChoiceStringDisplayHint;
import org.topcased.gpm.business.serialization.data.ChoiceTreeDisplayHint;
import org.topcased.gpm.business.serialization.data.Command;
import org.topcased.gpm.business.serialization.data.ContainerTypeAccessCtl;
import org.topcased.gpm.business.serialization.data.CriteriaGroup;
import org.topcased.gpm.business.serialization.data.Criterion;
import org.topcased.gpm.business.serialization.data.DateDisplayHint;
import org.topcased.gpm.business.serialization.data.DisplayGroup;
import org.topcased.gpm.business.serialization.data.DisplayHint;
import org.topcased.gpm.business.serialization.data.Environment;
import org.topcased.gpm.business.serialization.data.ExportType;
import org.topcased.gpm.business.serialization.data.ExtendedAction;
import org.topcased.gpm.business.serialization.data.ExtensionPoint;
import org.topcased.gpm.business.serialization.data.ExtensionsContainer;
import org.topcased.gpm.business.serialization.data.ExternDisplayHint;
import org.topcased.gpm.business.serialization.data.Field;
import org.topcased.gpm.business.serialization.data.FieldAccessCtl;
import org.topcased.gpm.business.serialization.data.FieldRef;
import org.topcased.gpm.business.serialization.data.FieldValueData;
import org.topcased.gpm.business.serialization.data.FieldsContainer;
import org.topcased.gpm.business.serialization.data.Filter;
import org.topcased.gpm.business.serialization.data.FilterAccessConstraint;
import org.topcased.gpm.business.serialization.data.FilterAccessConstraintName;
import org.topcased.gpm.business.serialization.data.FilterAccessCtl;
import org.topcased.gpm.business.serialization.data.Gpm;
import org.topcased.gpm.business.serialization.data.GridDisplayHint;
import org.topcased.gpm.business.serialization.data.InputDataType;
import org.topcased.gpm.business.serialization.data.JAppletDisplayHint;
import org.topcased.gpm.business.serialization.data.Link;
import org.topcased.gpm.business.serialization.data.LinkType;
import org.topcased.gpm.business.serialization.data.LinkTypeSorter;
import org.topcased.gpm.business.serialization.data.Lock;
import org.topcased.gpm.business.serialization.data.Message;
import org.topcased.gpm.business.serialization.data.MultipleField;
import org.topcased.gpm.business.serialization.data.NamedElement;
import org.topcased.gpm.business.serialization.data.Option;
import org.topcased.gpm.business.serialization.data.PointerFieldValueData;
import org.topcased.gpm.business.serialization.data.Product;
import org.topcased.gpm.business.serialization.data.ProductType;
import org.topcased.gpm.business.serialization.data.ReportModel;
import org.topcased.gpm.business.serialization.data.Role;
import org.topcased.gpm.business.serialization.data.Script;
import org.topcased.gpm.business.serialization.data.SheetType;
import org.topcased.gpm.business.serialization.data.SimpleField;
import org.topcased.gpm.business.serialization.data.State;
import org.topcased.gpm.business.serialization.data.TextDisplayHint;
import org.topcased.gpm.business.serialization.data.TransitionAccessCtl;
import org.topcased.gpm.business.serialization.data.Translation;
import org.topcased.gpm.business.serialization.data.TypeMapping;
import org.topcased.gpm.business.serialization.data.User;
import org.topcased.gpm.business.serialization.data.UserRole;
import org.topcased.gpm.business.serialization.data.ValuesContainerData;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.business.sheet.service.SheetService;
import org.topcased.gpm.business.util.GridObjectsUtil;
import org.topcased.gpm.business.values.service.ValuesService;
import org.topcased.gpm.common.extensions.GUIContext;
import org.topcased.gpm.instantiation.fieldaccess.FieldAccess;
import org.topcased.gpm.instantiation.fieldaccess.FieldCompositeAccess;
import org.topcased.gpm.instantiation.fieldaccess.FieldElementAccess;
import org.topcased.gpm.instantiation.fieldaccess.FieldGroupAccess;
import org.topcased.gpm.instantiation.fieldaccess.MultipleFieldAccess;
import org.topcased.gpm.instantiation.fieldaccess.MultipleLineAccess;
import org.topcased.gpm.instantiation.options.InstantiateOptions;
import org.topcased.gpm.util.bean.CacheProperties;
import org.topcased.gpm.util.resources.BasicResourcesLoader;
import org.topcased.gpm.util.resources.IResourcesLoader;
import org.topcased.gpm.util.session.GpmSessionFactory;
import org.topcased.gpm.util.xml.SchemaValidator;

/**
 * The Class InstanceCreator.
 * 
 * @author llatil
 */
public class InstanceCreator {

    /** The Constant ID_PREFIX. */
    private static final String ID_PREFIX = "id:";

    /** The Constant FORMAT_VERSION. */
    private static final String FORMAT_VERSION = "1.2";

    /** The logger for this class. */
    private final Logger logger = Logger.getLogger(InstanceCreator.class);

    /** name of business process. */
    private String processName;

    /** The role token. */
    private final String roleToken;

    /** The Service Locator. */
    private ServiceLocator serviceLocator;

    /** The i18n Service . */
    private I18nService i18nService;

    /** The sheet Service. */
    private SheetService sheetService;

    /** Link service. */
    private LinkService linkService;

    /** The authorization service. */
    private AuthorizationService authorizationService;

    /** The product service. */
    private ProductService productService;

    /** The field service. */
    private FieldsService fieldsService;

    /** The extension service. */
    private ExtensionsService extensionsService;

    /** The display group service. */
    private DisplayService displayGroupService;

    /** The life cycle service. */
    private LifeCycleService lifeCycleService;

    /** The search service. */
    private SearchService searchService;

    /** The reporting service. */
    private ReportingService reportingService;

    /** The attributes service. */
    private AttributesService attributesService;

    /** The fields container service. */
    private FieldsContainerService fieldsContainerService;

    /** The instance service. */
    private InstanceService instanceService;

    private SchemaValidator gpmSchemaValidator;

    private ValuesService valuesService;

    private ImportService importService;

    private ImportProperties importProperties;

    /** The local id2gpm id. */
    private final Map<String, String> localId2gpmId =
            new HashMap<String, String>();

    /** The resources loader. */
    private final IResourcesLoader resourcesLoader = new BasicResourcesLoader();

    /** The converter. */
    private XMLConverter converter;

    private final Context creationCtx;

    private final boolean xmlSyntaxChecked;

    /**
     * Title of display group for product if no display group is defined in XML
     * instantiation file
     */
    private static final String UNIQUE_DISPLAY_GROUP_PRODUCT_TITLE =
            "Product_Fields";

    /**
     * Constructor.
     * 
     * @param pRoleToken
     *            The role session token to use during instance creation.
     * @param pCreationCtx
     *            Execution context to use during instantiation of the elements
     *            (used in gPM service calls)
     * @param pSyntaxChecked
     *            Specify if the XML syntax (XSD) must be checked or not
     */
    public InstanceCreator(String pRoleToken, Context pCreationCtx,
            boolean pSyntaxChecked) {
        roleToken = pRoleToken;
        xmlSyntaxChecked = pSyntaxChecked;
        creationCtx = pCreationCtx;
        importProperties = new ImportProperties();
    }

    /**
     * Load a file or a resource.
     * 
     * @param pURL
     *            The url of a file or a resource.
     * @return The input stream (can be null if not found)
     */
    private InputStream load(String pURL) {
        return load(pURL, pURL);
    }

    /**
     * Load a file or a resource.
     * 
     * @param pURL
     *            The url of a file or a resource.
     * @param pObj
     *            The object requiring the load of the resource (Used to get the
     *            line number in case of error)
     * @return The input stream (can be null if not found)
     */
    private InputStream load(String pURL, Object pObj) {
        InputStream lIs = null;
        lIs = resourcesLoader.getAsStream(pURL);

        if (null == lIs) {
            error("Cannot load resource '" + pURL + "'", pObj);
        }
        return lIs;
    }

    /**
     * Instantiate the data.
     * 
     * @param pProcessName
     *            Name of process to create / update
     * @param pXmlResource
     *            The name of the XML resource that contains the data.
     */
    public void parseXML(String pProcessName, String pXmlResource) {
        processName = pProcessName;

        try {
            if (xmlSyntaxChecked) {
                // Validate the syntax of the document.
                if (null == gpmSchemaValidator) {
                    gpmSchemaValidator = new SchemaValidator();
                }
                gpmSchemaValidator.validate(load(pXmlResource), Boolean.TRUE);
            }

            // Create an XML converter
            converter = new XMLConverter(load(pXmlResource));

            // Convert XML to java objects
            Gpm lGpm = converter.fromXML();

            // Retrieving the different services
            initServices();

            // Check the format version
            String lVersion = lGpm.getVersion();
            if (!lVersion.trim().equals(FORMAT_VERSION)) {
                throw new InstantiateException("Invalid format version. '"
                        + FORMAT_VERSION + "' expected.");
            }

            createGpmAndGlobalAttributes(lGpm);

            initCommands(lGpm.getCommands());

            // Add or update the process to the specified instance
            BusinessProcessData lBusinessProcessData =
                    new BusinessProcessData();
            lBusinessProcessData.setName(processName);

            // Create users
            instantiateUsers(lGpm.getUsers());

            // Create roles
            createRoles(lGpm.getRoles(), processName);

            if (null != lGpm.getDictionary()) {
                // Add or update dictionary to the specified instance
                instantiateCategories(lGpm.getDictionary().getCategories());
                instantiateEnvironments(lGpm.getDictionary().getEnvironments());
            }

            // Add or update I18n to the specified instance
            createTranslations(lGpm.getTranslations());

            // Create Fields Containers
            createOrUpdateProductTypes(lGpm.getProductTypes(), processName);
            createOrUpdateLinkTypes(lGpm.getProductLinkTypes(), processName);
            createOrUpdateSheetTypes(lGpm.getSheetTypes(), processName);
            createOrUpdateLinkTypes(lGpm.getSheetLinkTypes(), processName);

            // Create the type mappings
            createOrUpdateMapping(lGpm.getMapping());

            // Create dynamic model
            generateDynamicDbSchema(lGpm.getProductTypes(),
                    lGpm.getProductLinkTypes(), lGpm.getSheetTypes(),
                    lGpm.getSheetLinkTypes());

            // Create Values Containers
            instantiateProducts(lGpm.getProducts());
            instantiateProductLinks(lGpm.getProductLinks());
            instantiateSheets(lGpm.getSheets());
            instantiateSheetLinks(lGpm.getSheetLinks());

            instantiateFilters(lGpm.getFilters());

            createReports(lGpm.getReports(), processName);

            String lProcessId =
                    instanceService.getBusinessProcessId(processName);

            createOrUpdateExtensionPoints(lGpm.getExtensionPoints());

            createOrUpdateExtendedActions(lGpm.getExtendedActions(), lProcessId);
            createAccessControls(lGpm.getAccessControls(), processName);

            if (lGpm.getFilterAccessConstraints() != null) {
                createFilterAccessControlConstraint(lGpm.getFilterAccessConstraints());
            }

            if (lGpm.getFilterAccessControls() != null) {
                createFilterAccessControls(lGpm.getFilterAccessControls());
            }

            // Assign all roles to users
            instantiateUserRoles(lGpm.getUserRoles());
        }
        catch (InstantiateException e) {
            // Print error
            System.err.println("Error during instantiation of '" + pXmlResource
                    + "'" + "\n" + " On object: " + e.getObjectInError() + "\n"
                    + e.getErrorMessage());
            logger.error(e);
            throw new GDMException(e);
        }
    }

    /**
     * Creates the users.
     * 
     * @param pUsers
     *            the users
     */
    private void instantiateUsers(List<User> pUsers) {
        if (CollectionUtils.isEmpty(pUsers)) {
            return;
        }
        importProperties.setUsersFlag(ImportFlag.CREATE_OR_UPDATE);
        for (User lUser : pUsers) {
            try {
                importService.importUser(roleToken, lUser, importProperties,
                        creationCtx);
            }
            catch (ImportException e) {
                error(e.getMessage(), e.getObject(), e);
            }
        }
    }

    /**
     * Creates the roles.
     * 
     * @param pRoles
     *            the roles
     * @param pProcessName
     *            the process name
     */
    private void createRoles(List<Role> pRoles, String pProcessName) {
        if (null == pRoles) {
            return;
        }
        for (Role lRole : pRoles) {
            authorizationService.createRole(roleToken, lRole.getName(),
                    pProcessName);
        }
    }

    /**
     * Assign user roles.
     * 
     * @param pUserRoles
     *            the user roles
     * @param pProcessName
     *            the process name
     */
    private void instantiateUserRoles(List<UserRole> pUserRoles) {
        if (CollectionUtils.isEmpty(pUserRoles)) {
            return;
        }

        importProperties.setUserRolesFlag(ImportFlag.CREATE_OR_UPDATE);
        for (UserRole lUserRole : pUserRoles) {
            try {
                importService.importUserRole(roleToken, lUserRole,
                        importProperties, creationCtx);
            }
            catch (ImportException e) {
                error(e.getMessage(), e.getObject(), e);
            }
        }
    }

    /**
     * Creates or updates extended actions.
     * 
     * @param pExtendedActions
     *            the extended actions
     * @param pExtensionsContainerId
     *            the extensions container id
     */
    private void createOrUpdateExtendedActions(
            List<ExtendedAction> pExtendedActions, String pExtensionsContainerId) {
        if (null == pExtendedActions) {
            return;
        }
        int index = 0;
        for (ExtendedAction lExtAction : pExtendedActions) {

            ExtendedActionData lExtendedActionData = new ExtendedActionData();
            lExtendedActionData.setName(lExtAction.getName());
            lExtendedActionData.setIndex(++index);
            lExtendedActionData.setExtensionPointName(lExtAction.getExtensionPointName());
            lExtendedActionData.setConfirmationMessage(lExtAction.getConfirmationMessage());
            lExtendedActionData.setMenuEntryName(lExtAction.getMenuEntry().getName());
            lExtendedActionData.setMenuEntryParentName(lExtAction.getMenuEntry().getParentName());
            lExtendedActionData.setExtensionsContainerId(pExtensionsContainerId);
            ExtendedActionData lOldExtendedAction =
                    extensionsService.getExtendedAction(pExtensionsContainerId,
                            lExtAction.getName());
            if (lExtAction.getInputDataType() != null) {
                InputDataType lInputDataType =
                        fieldsService.getInputDataType(roleToken,
                                lExtAction.getInputDataType().getName(),
                                processName);
                String lId = null;
                if (lInputDataType == null) {
                    lId =
                            fieldsService.createInputDataType(roleToken,
                                    processName, lExtAction.getInputDataType());
                    lInputDataType = lExtAction.getInputDataType();
                    updateFieldsContainer(lId, lInputDataType);
                }
                else {
                    lId = lInputDataType.getId();
                    updateFieldsContainer(lInputDataType.getId(),
                            lInputDataType);

                }
                // Create the ext points for the input data type (list of commands ref).
                createOrUpdateExtensionPoints(lInputDataType, lId);
                createDisplayGroups(lInputDataType, lId);

                lExtendedActionData.setInputDataTypeName(lInputDataType.getName());
            }
            else if (lOldExtendedAction != null) {
                // old input data type to remove
                InputDataType lOldInputDataType =
                        fieldsService.getInputDataType(roleToken,
                                lOldExtendedAction.getInputDataTypeName(),
                                processName);
                if (lOldInputDataType != null) {
                    fieldsService.removeInputDataType(roleToken,
                            lOldInputDataType.getId());
                }
            }

            ArrayList<GUIContext> lContexts = new ArrayList<GUIContext>();
            if (lExtAction.getGuiContexts() != null) {

                for (NamedElement lContext : lExtAction.getGuiContexts()) {
                    lContexts.add(GUIContext.fromString(lContext.getName()));
                }
                lExtendedActionData.setContexts(lContexts);
            }
            else {
                lContexts.add(GUIContext.ALWAYS);
                lExtendedActionData.setContexts(lContexts);
            }

            if (lOldExtendedAction == null) {
                extensionsService.createExtendedAction(roleToken, processName,
                        lExtendedActionData);
            }
            else {
                extensionsService.updateExtendedAction(roleToken,
                        lExtendedActionData);
            }
        }
    }

    /**
     * Creates the or update extension points.
     * 
     * @param pExtensionPoints
     *            the extension points
     */
    private void createOrUpdateExtensionPoints(
            List<ExtensionPoint> pExtensionPoints) {
        if (null == pExtensionPoints) {
            return;
        }

        for (ExtensionPoint lExtPoint : pExtensionPoints) {
            List<String> lCmdNames;
            lCmdNames = new ArrayList<String>(lExtPoint.getCommands().size());
            for (NamedElement lCmdRef : lExtPoint.getCommands()) {
                lCmdNames.add(lCmdRef.getName());
            }

            List<AttributeData> lAttributes = null;
            if (lExtPoint.getAttributes() != null) {
                lAttributes =
                        new ArrayList<AttributeData>(
                                lExtPoint.getAttributes().size());
                for (Attribute lAttribute : lExtPoint.getAttributes()) {
                    lAttributes.add(new AttributeData(lAttribute.getName(),
                            lAttribute.getValues()));
                }
            }
            extensionsService.setExtension(roleToken,
                    instanceService.getBusinessProcessId(processName),
                    lExtPoint.getName(), lCmdNames, lAttributes);
        }
    }

    /**
     * Creates or updates extension points.
     * 
     * @param pContainer
     *            the container
     * @param pContainerId
     *            the container id
     */
    private void createOrUpdateExtensionPoints(ExtensionsContainer pContainer,
            String pContainerId) {
        List<ExtensionPoint> lExtPoints = pContainer.getExtensionPoints();
        if (null != lExtPoints) {
            for (ExtensionPoint lExtPoint : lExtPoints) {
                // If the extension point definition has no commands nor attributes,
                // remove the extension point itself.
                if (lExtPoint.getCommands().size() == 0
                        && lExtPoint.getAttributes().size() == 0) {
                    extensionsService.removeExtension(roleToken, pContainerId,
                            lExtPoint.getName());
                }
                else {
                    try {
                        List<String> lCmdNames;
                        lCmdNames =
                                new ArrayList<String>(
                                        lExtPoint.getCommands().size());
                        for (NamedElement lCmdRef : lExtPoint.getCommands()) {
                            lCmdNames.add(lCmdRef.getName());
                        }
                        extensionsService.setExtension(roleToken, pContainerId,
                                lExtPoint.getName(), lCmdNames);
                    }
                    catch (GDMException e) {
                        error(e.getMessage(), lExtPoint, e);
                    }
                }
            }
        }
    }

    /**
     * Creates the access controls.
     * 
     * @param pAccControls
     *            the acc controls
     * @param pProcessName
     *            the process name
     */
    private void createAccessControls(List<AccessCtl> pAccControls,
            String pProcessName) {
        if (null == pAccControls) {
            return;
        }

        for (AccessCtl lAcc : pAccControls) {
            if (lAcc instanceof FieldAccessCtl) {
                FieldAccessControlData lFieldAccessCtlData;
                lFieldAccessCtlData = new FieldAccessControlData();

                FieldAccessCtl lFieldAcc = (FieldAccessCtl) lAcc;
                FieldAccessData lAccess = new FieldAccessData();

                // Set field control access
                lAccess.setUpdatable(lFieldAcc.isUpdatable());
                lAccess.setConfidential(lFieldAcc.isConfidential());
                lAccess.setMandatory(lFieldAcc.isMandatory());
                lAccess.setExportable(lFieldAcc.isExportable());
                lFieldAccessCtlData.setAccess(lAccess);

                lFieldAccessCtlData.setFieldName(lFieldAcc.getFieldKey());
                fillAccessControl(lFieldAccessCtlData, lAcc, pProcessName);

                if (lFieldAccessCtlData.getContext().getContainerTypeId() == null) {
                    error("The type name must be specified.", lAcc);
                }
                authorizationService.setFieldAccessControl(lFieldAccessCtlData);
            }
            else if (lAcc instanceof ContainerTypeAccessCtl) {
                TypeAccessControlData lSheetTypeAccessCtlData;
                lSheetTypeAccessCtlData = new TypeAccessControlData();

                ContainerTypeAccessCtl lSheetTypeAcc =
                        (ContainerTypeAccessCtl) lAcc;
                lSheetTypeAccessCtlData.setUpdatable(lSheetTypeAcc.isUpdatable());
                lSheetTypeAccessCtlData.setConfidential(lSheetTypeAcc.isConfidential());
                lSheetTypeAccessCtlData.setCreatable(lSheetTypeAcc.isCreatable());
                lSheetTypeAccessCtlData.setDeletable(lSheetTypeAcc.isDeletable());
                fillAccessControl(lSheetTypeAccessCtlData, lAcc, pProcessName);

                authorizationService.setSheetTypeAccessControl(lSheetTypeAccessCtlData);
            }
            else if (lAcc instanceof TransitionAccessCtl) {
                TransitionAccessControlData lTransitionAccessCtlData;
                lTransitionAccessCtlData = new TransitionAccessControlData();

                TransitionAccessCtl lTransitionAcc = (TransitionAccessCtl) lAcc;
                lTransitionAccessCtlData.setAllowed(lTransitionAcc.isEnabled());
                lTransitionAccessCtlData.setTransitionName(lTransitionAcc.getTransitionName());
                fillAccessControl(lTransitionAccessCtlData, lAcc, pProcessName);

                authorizationService.setTransitionAccessControl(lTransitionAccessCtlData);
            }
            else if (lAcc instanceof ActionAccessCtl) {
                ActionAccessControlData lActionAccessCtlData;
                lActionAccessCtlData = new ActionAccessControlData();
                ActionAccessCtl lActionAcc = (ActionAccessCtl) lAcc;

                lActionAccessCtlData.setEnabled(lActionAcc.isEnabled());
                lActionAccessCtlData.setConfidential(lActionAcc.isConfidential());
                lActionAccessCtlData.setLabelKey(lActionAcc.getActionKey());
                fillAccessControl(lActionAccessCtlData, lAcc, pProcessName);

                authorizationService.setApplicationActionAccessControl(lActionAccessCtlData);
            }
            else if (lAcc instanceof AdminAccessCtl) {
                AdminAccessControlData lAdminAccessCtlData;
                lAdminAccessCtlData = new AdminAccessControlData();
                AdminAccessCtl lAdminActionAcc = (AdminAccessCtl) lAcc;

                lAdminAccessCtlData.setLabelKey(lAdminActionAcc.getActionKey());
                fillAccessControl(lAdminAccessCtlData, lAcc, pProcessName);

                authorizationService.setAdminAccessControl(lAdminAccessCtlData);
            }
            else {
                error("Invalid access control class:"
                        + lAcc.getClass().getName(), lAcc);
            }
        }
    }

    /**
     * Fill access control.
     * 
     * @param pAccCtlData
     *            the acc ctl data
     * @param pAccess
     *            the access
     * @param pProcessName
     *            the process name
     */
    private void fillAccessControl(AccessControlData pAccCtlData,
            final AccessCtl pAccess, final String pProcessName) {
        pAccCtlData.setBusinessProcessName(pProcessName);
        AccessControlContextData lAccessControlContextData =
                new AccessControlContextData();
        lAccessControlContextData.setProductName(pAccess.getProductName());
        lAccessControlContextData.setStateName(pAccess.getStateName());
        lAccessControlContextData.setRoleName(pAccess.getRole());

        if (StringUtils.isNotBlank(pAccess.getTypeName())) {
            String lFieldsContainerId =
                    serviceLocator.getFieldsContainerService().getFieldsContainerId(
                            roleToken, pAccess.getTypeName());
            lAccessControlContextData.setContainerTypeId(lFieldsContainerId);
        }

        if (pAccess instanceof FieldAccessCtl) {
            FieldAccessCtl lFieldAccess = (FieldAccessCtl) pAccess;
            if (StringUtils.isNotBlank(lFieldAccess.getVisibleType())) {
                String lFacadeContainerId =
                        serviceLocator.getFieldsContainerService().getFieldsContainerId(
                                roleToken, lFieldAccess.getVisibleType());
                lAccessControlContextData.setVisibleTypeId(lFacadeContainerId);
            }
        }
        pAccCtlData.setContext(lAccessControlContextData);
        pAccCtlData.setExtendedAttributes(toAttributeDatas(pAccess.getAttributes()));
    }

    private void createFilterAccessControls(
            List<FilterAccessCtl> pFilterAccessControls) {
        for (FilterAccessCtl lFilterAccessCtl : pFilterAccessControls) {
            //Verifying constraints existence
            if (CollectionUtils.isNotEmpty(lFilterAccessCtl.getConstraints())) {
                for (FilterAccessConstraintName lConstraintName : lFilterAccessCtl.getConstraints()) {
                    if (!authorizationService.isFilterAccessConstraintExists(lConstraintName.getConstraintName())) {
                        throw new InstantiateException(
                                "Invalid constraint name '" + lConstraintName
                                        + "'", lFilterAccessCtl);
                    }
                }
            }
            //Check visibility
            if (StringUtils.isNotBlank(lFilterAccessCtl.getVisibility())) {
                if (StringUtils.isNotBlank(lFilterAccessCtl.getTypeName())
                        || (StringUtils.isNotBlank(lFilterAccessCtl.getFieldName()))) {
                    throw new InstantiateException(
                            "When the visibility is setting, "
                                    + "the type name and/or the field name must be empty",
                            lFilterAccessCtl);
                }
            }

            // If not specified, executable or editable is true
            if (lFilterAccessCtl.getExecutable() == null) {
                lFilterAccessCtl.setExecutable(true);
            }
            if (lFilterAccessCtl.getEditable() == null) {
                lFilterAccessCtl.setEditable(true);
            }
            try {
                authorizationService.createFilterAccess(roleToken,
                        lFilterAccessCtl);
            }
            catch (InvalidNameException e) {
                throw new InstantiateException(
                        "Element not well-formed (see cause).",
                        lFilterAccessCtl, e);
            }
            catch (IllegalArgumentException e) {
                throw new InstantiateException(
                        "Element not well-formed (see cause).",
                        lFilterAccessCtl, e);
            }
            catch (AuthorizationException e) {
                throw new InstantiateException("No rights.", lFilterAccessCtl,
                        e);
            }
        }
    }

    private void createFilterAccessControlConstraint(
            List<FilterAccessConstraint> pFilterAccessConstraint) {
        for (FilterAccessConstraint lConstraint : pFilterAccessConstraint) {
            CriteriaData lCriteriaData =
                    createCriteriaGroups(lConstraint.getCriteria());
            FilterAccessContraint lFilterAccessContraint =
                    new FilterAccessContraint();
            lFilterAccessContraint.setName(lConstraint.getConstraintName());
            lFilterAccessContraint.setDescription(lConstraint.getDescription());
            lFilterAccessContraint.setConstraints(lCriteriaData);
            try {
                authorizationService.createFilterAccessConstraint(roleToken,
                        lFilterAccessContraint);
            }
            catch (AuthorizationException e) {
                throw new InstantiateException(e.getMessage(), lConstraint, e);
            }
        }
    }

    private CriteriaData createCriteriaGroups(
            List<CriteriaGroup> pCriteriaGroups) {
        if (pCriteriaGroups.isEmpty()) {
            return null;
        }
        else if (pCriteriaGroups.size() == 1) {
            return createCriteriaGroup(pCriteriaGroups.get(0));
        }
        else {
            OperationData lOperationData = new OperationData();
            lOperationData.setOperator(FilterCreator.OR);

            List<CriteriaData> lCriteria =
                    new ArrayList<CriteriaData>(pCriteriaGroups.size());
            for (CriteriaGroup lCriteriaGroup : pCriteriaGroups) {
                CriteriaData lCriteriaData =
                        createCriteriaGroup(lCriteriaGroup);
                if (lCriteriaData != null) {
                    lCriteria.add(lCriteriaData);
                }
            }
            if (lCriteria.isEmpty()) {
                return null;
            }
            else {
                lOperationData.setCriteriaDatas(lCriteria.toArray(new CriteriaData[0]));
                return lOperationData;
            }
        }
    }

    private CriteriaData createCriteriaGroup(CriteriaGroup pCriteriaGroup) {
        if (pCriteriaGroup.getCriterionList().isEmpty()) {
            return null;
        }
        else if (pCriteriaGroup.getCriterionList().size() == 1) {
            return createCriterion(pCriteriaGroup.getCriterionList().get(0));
        }
        else {
            OperationData lOperationData = new OperationData();
            lOperationData.setOperator(FilterCreator.AND);

            List<CriteriaData> lCriteria =
                    new ArrayList<CriteriaData>(
                            pCriteriaGroup.getCriterionList().size());
            for (Criterion lCriterion : pCriteriaGroup.getCriterionList()) {
                lCriteria.add(createCriterion(lCriterion));
            }
            lOperationData.setCriteriaDatas(lCriteria.toArray(new CriteriaData[0]));
            return lOperationData;
        }
    }

    private CriteriaFieldData createCriterion(Criterion pCriterion) {
        String lUsableFieldDataId =
                serviceLocator.getSearchService().getUsableFieldDataId(
                        roleToken, processName, pCriterion.getFieldKey());
        UsableFieldData lUsableFieldData =
                searchService.getUsableField(processName, lUsableFieldDataId,
                        new ArrayList<String>(0));

        if (null == lUsableFieldData) {
            String lMsg =
                    MessageFormat.format(
                            "Invalid field name ''{0}'' for criterion",
                            pCriterion.getFieldKey());
            throw new InstantiateException(lMsg, pCriterion);
        }

        ScalarValueData lScalarValueData = null;

        FieldType lFieldType = lUsableFieldData.getFieldType();

        try {
            lScalarValueData =
                    FilterCreator.createScalarValueData(lFieldType,
                            pCriterion.getValue());
        }
        catch (InstantiateException e) {
            throw new InstantiateException("Invalid value '"
                    + pCriterion.getValue() + "' for field '"
                    + pCriterion.getFieldKey() + "'", pCriterion, e);
        }

        String lRealOp = FilterCreator.OPERATORS.get(pCriterion.getOperator());
        Collection<String> lCompatibleOperators =
                searchService.getCompatibleOperators(lUsableFieldData);
        if ((null == lRealOp) || (!lCompatibleOperators.contains(lRealOp))) {
            throw new InstantiateException("Invalid operator '"
                    + pCriterion.getOperator() + "' for field '"
                    + pCriterion.getFieldKey() + "'", pCriterion);
        }

        // Get case sensitivity
        Boolean lCaseSensitive = pCriterion.getCaseSensitive();
        if (lCaseSensitive == null) {
            lCaseSensitive = Boolean.FALSE;
        }

        CriteriaFieldData lCriteriaFieldData =
                new CriteriaFieldData(lRealOp, lCaseSensitive,
                        lScalarValueData, lUsableFieldData);
        return lCriteriaFieldData;
    }

    /**
     * Create the environments for the dictionary.
     * 
     * @param pCategoryList
     *            The list of the category available.
     * @param pBusinessProcessName
     *            the business process name
     */
    private void instantiateCategories(List<Category> pCategoryList) {
        if (logger.isDebugEnabled()) {
            logger.debug("instantiate categories.");
        }

        if (CollectionUtils.isEmpty(pCategoryList)) {
            return;
        }

        if ((creationCtx.get(InstantiateOptions.DELETE_CATEGORY_VALUES_OPTION) != null)
                && ((Boolean) creationCtx.get(InstantiateOptions.DELETE_CATEGORY_VALUES_OPTION))) {
            importProperties.setCategoriesFlag(ImportFlag.ERASE);
        }
        else {
            //Default update the category values
            importProperties.setCategoriesFlag(ImportFlag.CREATE_OR_UPDATE);
        }

        // Retrieving the category
        for (Category lCategory : pCategoryList) {
            try {
                importService.importCategory(roleToken, lCategory,
                        importProperties, creationCtx);
            }
            catch (ImportException e) {
                error(e.getMessage(), e.getObject(), e);
            }
        }
    }

    /**
     * Create the categories for the dictionary.
     * 
     * @param pEnvironmentList
     *            The list of the environment available.
     */
    private void instantiateEnvironments(List<Environment> pEnvironmentList) {
        if (CollectionUtils.isEmpty(pEnvironmentList)) {
            return;
        }
        importProperties.setEnvironmentsFlag(ImportFlag.CREATE_OR_UPDATE);

        // Retrieving the environment
        for (Environment lEnvironment : pEnvironmentList) {
            try {
                importService.importEnvironment(roleToken, lEnvironment,
                        importProperties, creationCtx);
            }
            catch (ImportException e) {
                error(e.getMessage(), e.getObject(), e);
            }
        }
    }

    /**
     * Initialize the ext points commands.
     * 
     * @param pCommandsList
     *            the commands list
     */
    private void initCommands(List<Command> pCommandsList) {
        if (null == pCommandsList || pCommandsList.size() == 0) {
            return;
        }

        CommandData[] lCommands = new CommandData[pCommandsList.size()];
        int i = 0;

        for (Command lCmd : pCommandsList) {
            CommandData lCmdData = null;

            if (lCmd instanceof Script) {
                Script lScript = (Script) lCmd;
                ScriptData lScriptData = new ScriptData();

                // Set script language name
                lScriptData.setLanguage(lScript.getLanguage());

                String lCode;
                if (StringUtils.isBlank(lScript.getCode().getFilename())) {
                    lCode = lScript.getCode().getContent();
                }
                else {
                    StringBuilder lStrBuilder = new StringBuilder();

                    InputStream lIs =
                            load(lScript.getCode().getFilename(),
                                    lScript.getCode());

                    BufferedReader lReader =
                            new BufferedReader(new InputStreamReader(lIs));
                    try {
                        String lLine;
                        while ((lLine = lReader.readLine()) != null) {
                            lStrBuilder.append(lLine);
                            lStrBuilder.append('\n');
                        }
                    }
                    catch (IOException e) {
                        error("Cannot read resource '"
                                + lScript.getCode().getFilename() + "'",
                                lScript.getCode());
                    }
                    lCode = lStrBuilder.toString();
                }
                lScriptData.setCode(lCode);
                lCmdData = lScriptData;
            }
            else if (lCmd instanceof Action) {
                Action lAction = (Action) lCmd;
                ActionData lActionData = new ActionData();

                lActionData.setClassName(lAction.getClassname());
                lCmdData = lActionData;
            }
            else {
                error("Internal error: command type '"
                        + lCmd.getClass().getName() + "' not supported");
            }

            if (null != lCmdData) {
                lCmdData.setName(lCmd.getName());
                lCommands[i++] = lCmdData;
            }
        }
        extensionsService.createCommands(lCommands);
    }

    /**
     * Initialize I18n.
     * 
     * @param pTranslations
     *            the translations
     */
    private void createTranslations(List<Translation> pTranslations) {
        if (null == pTranslations) {
            return;
        }

        Map<String, String> lTranslationMap = new HashMap<String, String>();

        for (Translation lTranslation : pTranslations) {
            if (null != lTranslation.getMessages()) {
                // Define translations
                for (Message lMsg : lTranslation.getMessages()) {
                    lTranslationMap.put(lMsg.getKey(), lMsg.getTranslatedText());
                }
                i18nService.setValues(lTranslation.getLang(), lTranslationMap);
                lTranslationMap.clear();
            }

            if (lTranslation.getImages() != null) {
                for (Message lImg : lTranslation.getImages()) {
                    i18nService.setTypedValue(lImg.getKey(),
                            lTranslation.getLang(), I18nService.TYPE_IMAGE,
                            lImg.getTranslatedText());
                }
            }
        }
    }

    /**
     * Create the fields for product type, sheet type or sheet link type.
     * 
     * @param pContainerId
     *            The id of the productType, sheetType or sheetLinkType.
     * @param pFieldsList
     *            The fields list definition from the XML file.
     */
    private void createFields(String pContainerId, List<Field> pFieldsList) {
        if (null == pFieldsList) {
            return;
        }

        // Retrieve all fields defined in the xml file for the product type, the
        // sheet type, or the sheet link type
        for (Field lField : pFieldsList) {
            try {
                String lFieldId = null;

                if (lField instanceof SimpleField) {
                    SimpleField lSimpleField = (SimpleField) lField;

                    // Create the SimpleField
                    lFieldId = createSimpleField(lSimpleField, pContainerId);
                }
                else if (lField instanceof ChoiceField) {

                    ChoiceField lChoiceField = (ChoiceField) lField;
                    // Create the ChoiceField
                    lFieldId = createChoiceField(lChoiceField, pContainerId);
                }
                else if (lField instanceof AttachedField) {
                    AttachedField lAttachedField = (AttachedField) lField;
                    // Create the AttachedField
                    lFieldId =
                            createAttachedField(lAttachedField, pContainerId);
                }
                else if (lField instanceof MultipleField) {
                    MultipleField lMultipleField = (MultipleField) lField;

                    String lSeparator = lMultipleField.getFieldSeparator();
                    if (null == lSeparator) {
                        lSeparator = FieldsService.DEFAULT_FIELD_SEPARATOR;
                    }
                    // Create the MultipleField
                    lFieldId =
                            createMultipleField(lMultipleField, pContainerId,
                                    lSeparator);
                }
                else {
                    error("Invalid class " + lField.getClass().toString(),
                            lField);
                }

                // Create the extended attributes for the field (if any)
                createAttributes(lFieldId, lField.getAttributes());
            }
            catch (GDMException e) {
                error(e.getMessage(), lField);
            }
        }
    }

    /**
     * Fill and create a SimpleField.
     * 
     * @param pSimpleField
     *            The SimpleField to fill and create.
     * @param pFieldsContainerId
     *            The current id of the product type or sheet type.
     * @return The id of the field created
     */
    private String createSimpleField(SimpleField pSimpleField,
            String pFieldsContainerId) {

        if (pSimpleField.getLabelKey().equals("Childrens")) {
            logger.info("here");

        }
        if (logger.isInfoEnabled()) {
            logger.info("createSimpleField [ " + pSimpleField.getLabelKey()
                    + " ]");
        }

        // Create a SimpleField data.
        SimpleFieldData lSimpleFieldData = new SimpleFieldData();

        fillField(lSimpleFieldData, pSimpleField);
        String lValueTypeStr = pSimpleField.getValueType().toUpperCase();
        lSimpleFieldData.setValueType(ValueType.fromString(lValueTypeStr));
        lSimpleFieldData.setDefaultValue(pSimpleField.getDefaultValue());

        lSimpleFieldData.setMaxSize(pSimpleField.getSizeAsInt());

        String lFieldId =
                fieldsService.createSimpleField(roleToken, pFieldsContainerId,
                        lSimpleFieldData);

        if (logger.isInfoEnabled()) {
            logger.info("Creating successful the SimpleField with Id [ "
                    + lFieldId + " ]");
        }

        return lFieldId;
    }

    /**
     * Fill and create a ChoiceField.
     * 
     * @param pChoiceField
     *            The ChoiceField to fill and create.
     * @param pFieldsContainerId
     *            The current id of the product type or sheet type.
     * @return The id of the field created
     */
    private String createChoiceField(ChoiceField pChoiceField,
            String pFieldsContainerId) {

        if (logger.isInfoEnabled()) {
            logger.info("createChoiceField [ " + pChoiceField.getLabelKey()
                    + " ]");
        }

        // Create a ChoiceField data.
        ChoiceFieldData lChoiceFieldData = new ChoiceFieldData();
        fillField(lChoiceFieldData, pChoiceField);

        lChoiceFieldData.setCategoryName(pChoiceField.getCategoryName());
        lChoiceFieldData.setDefaultValue(pChoiceField.getDefaultValue());

        // FIXME : improve the setValuesSeparator
        lChoiceFieldData.setValuesSeparator(":");

        String lFieldId = null;
        lFieldId =
                fieldsService.createChoiceField(roleToken, pFieldsContainerId,
                        lChoiceFieldData);

        return lFieldId;
    }

    /**
     * Fill and create a AttachedField.
     * 
     * @param pAttachedField
     *            The AttachedField to fill and create.
     * @param pFieldsContainerId
     *            The current id of the product type or sheet type.
     * @return The id of the field created
     */
    private String createAttachedField(AttachedField pAttachedField,
            String pFieldsContainerId) {

        if (logger.isInfoEnabled()) {
            logger.info("fillAndCreateAttachedField [ "
                    + pAttachedField.getLabelKey() + " ]");
        }

        // Create a AttachedField data.
        AttachedFieldData lAttachedFieldData = new AttachedFieldData();
        fillField(lAttachedFieldData, pAttachedField);

        String lFieldId =
                fieldsService.createAttachedField(roleToken,
                        pFieldsContainerId, lAttachedFieldData);

        return lFieldId;
    }

    /**
     * Fill field.
     * 
     * @param pFieldData
     *            the field data
     * @param pField
     *            the field
     */
    private void fillField(FieldTypeData pFieldData, final Field pField) {
        pFieldData.setLabelKey(pField.getLabelKey());
        pFieldData.setDescription(pField.getDescription());

        pFieldData.setMultipleValueSupport(pField.isMultivalued());

        FieldAccessData lFieldAccess = new FieldAccessData();
        lFieldAccess.setMandatory(pField.isMandatory());
        lFieldAccess.setUpdatable(pField.isUpdatable());
        lFieldAccess.setConfidential(pField.isConfidential());
        lFieldAccess.setExportable(pField.isExportable());

        pFieldData.setDefaultAccess(lFieldAccess);

        addPointerField(pFieldData, pField);
    }

    /**
     * Check that field can support pointer fields. Add pointer field and
     * pointer field attributes
     * 
     * @param pFieldData
     *            the field data
     * @param pField
     *            the field
     */
    private void addPointerField(FieldTypeData pFieldData, final Field pField) {

        boolean lPointerField = pField.isPointerField();

        //pointerFields are not authorized for Multiple fields
        if (lPointerField && pField instanceof MultipleField) {
            throw new InstantiateException("Multiple field '"
                    + pField.getLabelKey() + "'cannot be a pointer field. ");
        }

        pFieldData.setPointerField(lPointerField);

        if (lPointerField && pField.getReferencedLinkType() != null
                && pField.getReferencedFieldLabel() != null) {
            pFieldData.setPointerFieldData(new PointerFieldData(
                    pField.getReferencedLinkType(),
                    pField.getReferencedFieldLabel()));
        }
    }

    /**
     * Fill and create a MultipleField.
     * 
     * @param pMultipleField
     *            The MultipleField to fill and create.
     * @param pFieldsContainerId
     *            The current id of the product type or sheet type.
     * @return the string
     */
    private String createMultipleField(MultipleField pMultipleField,
            String pFieldsContainerId, String pFieldSeparator) {
        if (logger.isInfoEnabled()) {
            logger.info("createMultipleOrReferenceField [ "
                    + pMultipleField.getLabelKey() + " ]");
        }

        // Create a MultipleField data.
        MultipleFieldData lMultipleFieldData = new MultipleFieldData();
        fillField(lMultipleFieldData, pMultipleField);

        lMultipleFieldData.setFieldSeparator(pFieldSeparator);

        List<Field> lSubFields = pMultipleField.getFields();
        if (lSubFields == null) {
            lSubFields = new ArrayList<Field>();
        }
        // Create the sub fields in the container
        createFields(pFieldsContainerId, lSubFields);

        // Get the names of the fields and set them in multifield data
        String[] lSubFieldsArray = new String[lSubFields.size()];

        int i = 0;
        for (Field lSubField : lSubFields) {
            //            FIXME AHD : POINTER DM5-ASTR
            //            if (lSubField.isPointerField()) {
            //                //TODO fixme
            //                throw new GDMException(
            //                        "Field '"
            //                                + lSubField.getLabelKey()
            //                                + "' invalid : A field inside a multiple field cannot be defined as a pointer field.");
            //            }
            lSubFieldsArray[i++] = lSubField.getLabelKey();
        }
        lMultipleFieldData.setSubfields(lSubFieldsArray);

        String lFieldId =
                fieldsService.createMultipleField(roleToken,
                        pFieldsContainerId, lMultipleFieldData);

        return lFieldId;
    }

    /**
     * Fill and create a ReferenceField.
     * 
     * @param pReferenceField
     *            The ReferenceField to fill and create.
     * @param pProductTypeIdOrSheetTypeId
     *            The current id of the product type or sheet type.
     */
    private void createReferenceField(MultipleField pReferenceField,
            String pProductTypeIdOrSheetTypeId) {
        if (logger.isInfoEnabled()) {
            logger.info("fillAndCreateReferenceField [ "
                    + pReferenceField.getLabelKey() + " ]");
        }

        if (pReferenceField.getLabelKey() == null) {
            pReferenceField.setLabelKey(FieldsService.REFERENCE_FIELD_NAME);
        }
        String lSeparator = pReferenceField.getFieldSeparator();
        if (null == lSeparator) {
            lSeparator = FieldsService.DEFAULT_FIELD_SEPARATOR;
        }
        String lRefFieldId =
                createMultipleField(pReferenceField,
                        pProductTypeIdOrSheetTypeId, lSeparator);

        sheetService.setSheetTypeReferenceField(roleToken,
                pProductTypeIdOrSheetTypeId, lRefFieldId);
    }

    /**
     * Create or update product list.
     * 
     * @param pProductTypesList
     *            the product types list
     * @param pProcessName
     *            the process name
     */
    private void createOrUpdateProductTypes(
            List<ProductType> pProductTypesList, String pProcessName) {
        if (null != pProductTypesList) {
            for (ProductType lProdType : pProductTypesList) {
                createOrUpdateProductType(lProdType, pProcessName);
            }
        }
    }

    /**
     * Create or update product type.
     * 
     * @param pProdType
     *            the prod type
     * @param pProcessName
     *            the process name
     */
    private void createOrUpdateProductType(ProductType pProdType,
            String pProcessName) {
        ProductTypeData lProductTypeData = null;
        String lProductTypeId;

        lProductTypeData =
                productService.getProductTypeByName(roleToken, pProcessName,
                        pProdType.getName());

        if (null == lProductTypeData) {
            lProductTypeData = new ProductTypeData();
            lProductTypeData.setName(pProdType.getName());
            lProductTypeData.setDescription(pProdType.getDescription());
            lProductTypeId =
                    productService.createProductType(roleToken, pProcessName,
                            lProductTypeData);
        }
        else {
            lProductTypeId = lProductTypeData.getId();
        }

        updateFieldsContainer(lProductTypeId, pProdType);

        //2 solutions to keep compatibility and avoid instances impact:
        // 1 - no display group in instance XML file
        // --> create a display group
        // 2 - display groups defined in instance XML file
        // --> use these display groups

        List<DisplayGroup> lDisplayGroups = pProdType.getDisplayGroups();
        if (lDisplayGroups == null || lDisplayGroups.size() == 0) {
            // create the single display group for the product
            createDisplayGroupForProductType(pProdType, lProductTypeId);
        }
        else {
            // create the display groups for the product
            createDisplayGroups(pProdType, lProductTypeId);
        }

        // Create the ext points for the product type (list of commands ref).
        createOrUpdateExtensionPoints(pProdType, lProductTypeId);
        fieldsContainerService.initDynamicMapping(lProductTypeId);
    }

    /**
     * Create or update product list.
     * 
     * @param pSheetTypesList
     *            the sheet types list
     * @param pProcessName
     *            the process name
     */
    private void createOrUpdateSheetTypes(List<SheetType> pSheetTypesList,
            String pProcessName) {

        if (null != pSheetTypesList) {
            for (SheetType lSheetType : pSheetTypesList) {
                createOrUpdateSheetType(lSheetType, pProcessName);
            }
        }
    }

    /**
     * Creates the display groups.
     * 
     * @param pFieldsContainer
     *            the fields container
     * @param pContainerId
     *            the container id
     */
    private void createDisplayGroups(FieldsContainer pFieldsContainer,
            String pContainerId) {
        List<DisplayGroup> lDisplayGroups = pFieldsContainer.getDisplayGroups();
        if (null != lDisplayGroups) {

            // Remove old display groups. (for migration)
            List<DisplayGroupData> lDisplayGroupsToRemove =
                    displayGroupService.getDisplayGroupList(roleToken,
                            pContainerId);
            if (lDisplayGroupsToRemove != null) {
                for (DisplayGroupData lDisplayGroupData : lDisplayGroupsToRemove) {
                    displayGroupService.removeDisplayGroup(
                            lDisplayGroupData.getLabelKey(), pContainerId);
                }
            }

            int lOrder = 1;
            // Create the display groups.
            for (DisplayGroup lDisplayGroup : lDisplayGroups) {
                try {
                    DisplayGroupData lDisplayGroupData;
                    lDisplayGroupData = new DisplayGroupData();
                    lDisplayGroupData.setContainerId(pContainerId);
                    lDisplayGroupData.setDisplayOrder(lOrder);
                    lDisplayGroupData.setLabelKey(lDisplayGroup.getName());

                    boolean lIsOpened = true;
                    if (lDisplayGroup.getOpened() != null) {
                        lIsOpened = lDisplayGroup.getOpened();
                    }
                    lDisplayGroupData.setOpened(lIsOpened);

                    FieldSummaryData[] lFieldSummaryDataArray;
                    lFieldSummaryDataArray =
                            new FieldSummaryData[lDisplayGroup.getFields().size()];

                    int i = 0;
                    for (FieldRef lFieldRef : lDisplayGroup.getFields()) {
                        FieldSummaryData lFsd = new FieldSummaryData();
                        lFsd.setLabelKey(lFieldRef.getName());
                        lFieldSummaryDataArray[i++] = lFsd;
                    }
                    lDisplayGroupData.setFieldSummaryDatas(lFieldSummaryDataArray);

                    displayGroupService.createDisplayGroup(lDisplayGroupData);
                    lOrder++;
                }
                catch (GDMException e) {
                    error(e.getMessage(), lDisplayGroup, e);
                }
            }
        }
    }

    /**
     * Create or update product type.
     * 
     * @param pSheetType
     *            the sheet type
     * @param pProcessName
     *            the process name
     */
    private void createOrUpdateSheetType(SheetType pSheetType,
            String pProcessName) {
        String lSheetTypeId;

        CacheableSheetType lSheetTypeData =
                sheetService.getCacheableSheetTypeByName(roleToken,
                        pProcessName, pSheetType.getName(),
                        CacheProperties.IMMUTABLE);

        if (null == lSheetTypeData) {
            lSheetTypeData = new CacheableSheetType();
            lSheetTypeData.setName(pSheetType.getName());
            lSheetTypeData.setDescription(pSheetType.getDescription());
            lSheetTypeId =
                    sheetService.createSheetType(roleToken, pProcessName,
                            lSheetTypeData);
        }
        else {
            lSheetTypeId = lSheetTypeData.getId();
        }

        // Create the reference field
        MultipleField lReferenceField = pSheetType.getReferenceField();
        createReferenceField(lReferenceField, lSheetTypeId);

        updateFieldsContainer(lSheetTypeId, pSheetType);

        createSheetTypeAttributes(pSheetType, lSheetTypeId);

        //createThe display groups
        createDisplayGroups(pSheetType, lSheetTypeId);

        // Create the ext points for the sheet type (list of commands ref).
        createOrUpdateExtensionPoints(pSheetType, lSheetTypeId);

        createLifecycle(pProcessName, pSheetType);

        List<State> lStates = pSheetType.getStates();
        if (null != lStates) {
            for (State lState : lStates) {

                String lStateName = lState.getName();

                if (null != lState.getExtensionPoints()) {
                    for (ExtensionPoint lExtPoint : lState.getExtensionPoints()) {
                        try {
                            // Create the ext points for the lifecycle states
                            List<String> lCmdNames;
                            lCmdNames =
                                    new ArrayList<String>(
                                            lExtPoint.getCommands().size());
                            for (NamedElement lCmdRef : lExtPoint.getCommands()) {
                                lCmdNames.add(lCmdRef.getName());
                            }
                            lifeCycleService.setExtension(roleToken,
                                    lSheetTypeId, lStateName,
                                    lExtPoint.getName(), lCmdNames);
                        }
                        catch (GDMException e) {
                            error(e.getMessage(), lExtPoint, e);
                        }
                    }
                }
            }
        }

        createOrUpdateExtendedActions(pSheetType.getExtendedActions(),
                lSheetTypeId);
        fieldsContainerService.initDynamicMapping(lSheetTypeId);
    }

    /**
     * Creates the sheet type attributes:
     * <ul>
     * <li>Versionable
     * <li>Autolocking
     * 
     * @param pSheetType
     *            Sheet type from the XML
     * @param pSheetTypeId
     *            Sheet type id.
     */
    private void createSheetTypeAttributes(SheetType pSheetType,
            String pSheetTypeId) {
        if (pSheetType.isVersionable()) {
            AttributeData lAttrData =
                    new AttributeData(
                            RevisionService.REVISION_ENABLED_ATTRIBUTE_NAME,
                            new String[] { "True" });
            attributesService.set(pSheetTypeId,
                    new AttributeData[] { lAttrData });
        }

        if (StringUtils.isNotBlank(pSheetType.getAutolocking())) {
            String lAutolockingValue = pSheetType.getAutolocking();
            AttributeData lAutlockingAttributeData =
                    new AttributeData(AttributesService.AUTOLOCKING,
                            new String[] { lAutolockingValue });
            attributesService.set(pSheetTypeId,
                    new AttributeData[] { lAutlockingAttributeData });
        }
    }

    /**
     * Creates the lifecycle.
     * 
     * @param pBusinessProcessName
     *            the business process name
     * @param pSheetType
     *            the sheet type
     * @return true, if successful
     */
    private boolean createLifecycle(String pBusinessProcessName,
            SheetType pSheetType) {
        if (pSheetType.getLifecycleResource() == null) {
            return false;
        }

        try {
            InputStream lIs = null;
            try {
                lIs = load(pSheetType.getLifecycleResource(), pSheetType);
                try {
                    lifeCycleService.createProcessDefinition(roleToken,
                            pBusinessProcessName, lIs);

                }
                catch (GDMException e) {
                    error(e.getMessage(), pSheetType, e);
                }
            }
            finally {
                // test if input stream not null to avoid a NPE
                if (lIs != null) {
                    // close stream after use
                    lIs.close();
                }
            }
        }
        catch (IOException e) {
            error("Cannot close lifecycle resource for " + pBusinessProcessName
                    + " and " + pSheetType.getName(), e);
        }
        return true;
    }

    /**
     * Creates the display hints.
     * 
     * @param pFieldsContainerId
     *            the fields container id
     * @param pHints
     *            the hints
     */
    private void createDisplayHints(String pFieldsContainerId,
            List<DisplayHint> pHints) {
        if (null == pHints) {
            return;
        }

        for (DisplayHint lDisplayHint : pHints) {

            try {
                if (lDisplayHint instanceof TextDisplayHint) {
                    TextDisplayHint lTextHint = (TextDisplayHint) lDisplayHint;
                    TextFieldDisplayHintData lHintData;

                    lHintData =
                            new TextFieldDisplayHintData(lTextHint.getWidth(),
                                    lTextHint.getHeight(),
                                    lTextHint.getDisplayType());
                    displayGroupService.setTextFieldDisplayHint(roleToken,
                            pFieldsContainerId, lDisplayHint.getLabelKey(),
                            lHintData);
                }
                else if (lDisplayHint instanceof ChoiceDisplayHint) {
                    ChoiceDisplayHint lChoiceHint =
                            (ChoiceDisplayHint) lDisplayHint;
                    ChoiceFieldDisplayHintData lHintData;
                    lHintData =
                            new ChoiceFieldDisplayHintData(
                                    lChoiceHint.isList(),
                                    lChoiceHint.getImageType());

                    displayGroupService.setChoiceFieldDisplayHint(roleToken,
                            pFieldsContainerId, lDisplayHint.getLabelKey(),
                            lHintData);
                }
                else if (lDisplayHint instanceof ChoiceTreeDisplayHint) {
                    ChoiceTreeDisplayHint lChoiceHint =
                            (ChoiceTreeDisplayHint) lDisplayHint;
                    ChoiceTreeDisplayHintData lHintData;
                    lHintData =
                            new ChoiceTreeDisplayHintData(
                                    lChoiceHint.getSeparator());

                    displayGroupService.setChoiceTreeDisplayHint(roleToken,
                            pFieldsContainerId, lDisplayHint.getLabelKey(),
                            lHintData);
                }
                else if (lDisplayHint instanceof AttachedDisplayHint) {
                    AttachedDisplayHint lAttachedDisplayHint =
                            (AttachedDisplayHint) lDisplayHint;
                    AttachedDisplayHintData lHintData;
                    lHintData =
                            new AttachedDisplayHintData(
                                    lAttachedDisplayHint.getWidth(),
                                    lAttachedDisplayHint.getHeight(),
                                    lAttachedDisplayHint.getDisplayType());

                    displayGroupService.setAttachedDisplayHint(roleToken,
                            pFieldsContainerId, lDisplayHint.getLabelKey(),
                            lHintData);

                }
                else if (lDisplayHint instanceof ExternDisplayHint) {
                    ExternDisplayHint lExternDisplayHint =
                            (ExternDisplayHint) lDisplayHint;

                    DisplayHintData lHintData;
                    lHintData =
                            new DisplayHintData(
                                    lExternDisplayHint.getType(),
                                    getAttributesData(lDisplayHint.getAttributes()));

                    displayGroupService.setDisplayHint(roleToken,
                            pFieldsContainerId, lDisplayHint.getLabelKey(),
                            lHintData);

                }
                else if (lDisplayHint instanceof DateDisplayHint) {
                    DateDisplayHint lDateDisplayHint =
                            (DateDisplayHint) lDisplayHint;
                    DateDisplayHintData lHintData;
                    lHintData =
                            new DateDisplayHintData(
                                    lDateDisplayHint.getFormat(),
                                    lDateDisplayHint.hasTime());

                    displayGroupService.setDateDisplayHint(roleToken,
                            pFieldsContainerId, lDisplayHint.getLabelKey(),
                            lHintData);
                }
                else if (lDisplayHint instanceof GridDisplayHint) {
                    GridDisplayHint lGridDisplayHint =
                            (GridDisplayHint) lDisplayHint;
                    GridDisplayHintData lGridDisplayHintData =
                            GridObjectsUtil.createGridDisplayHintData(lGridDisplayHint);
                    displayGroupService.setGridDisplayHint(roleToken,
                            pFieldsContainerId, lDisplayHint.getLabelKey(),
                            lGridDisplayHintData);

                    // Create the extended attributes for the displayHint (if any)
                    createAttributes(pFieldsContainerId,
                            lDisplayHint.getAttributes());
                }
                else if (lDisplayHint instanceof ChoiceStringDisplayHint) {
                    ChoiceStringDisplayHint lChoiceStringDisplayHint;
                    lChoiceStringDisplayHint =
                            (ChoiceStringDisplayHint) lDisplayHint;

                    final String[] lAttributeValues;
                    lAttributeValues =
                            new String[] {
                                          lChoiceStringDisplayHint.getExtensionPointName(),
                                          Boolean.toString(lChoiceStringDisplayHint.isStrict()) };
                    AttributeData[] lAttributes =
                            new AttributeData[] { new AttributeData(
                                    ChoiceStringDisplayHint.getHintAttributesName(),
                                    lAttributeValues) };
                    DisplayHintData lHintData;
                    lHintData =
                            new DisplayHintData(
                                    ChoiceStringDisplayHint.getHintType(),
                                    lAttributes);

                    displayGroupService.setDisplayHint(roleToken,
                            pFieldsContainerId,
                            lChoiceStringDisplayHint.getLabelKey(), lHintData);
                }
                else if (lDisplayHint instanceof JAppletDisplayHint) {
                    JAppletDisplayHint lJAppletDisplayHint =
                            (JAppletDisplayHint) lDisplayHint;
                    JAppletDisplayHintData lHintData;
                    lHintData =
                            new JAppletDisplayHintData(
                                    lJAppletDisplayHint.getAppletCode(),
                                    lJAppletDisplayHint.getAppletCodeBase(),
                                    lJAppletDisplayHint.getAppletAlter(),
                                    lJAppletDisplayHint.getAppletName(),
                                    lJAppletDisplayHint.getAppletArchive(),
                                    (String[]) lJAppletDisplayHint.getAppletParameterAsStringList().toArray(
                                            new String[lJAppletDisplayHint.getAppletParameterAsStringList().size()]));
                    displayGroupService.setJAppletDisplayHint(roleToken,
                            pFieldsContainerId, lDisplayHint.getLabelKey(),
                            lHintData);
                }
            }
            catch (GDMException e) {
                error("Cannot create display hint.", lDisplayHint, e);
            }
        }
    }

    /**
     * Create or update a list of link types.
     * 
     * @param pLinkTypesList
     *            the link types list
     * @param pProcessName
     *            the process name
     */
    private void createOrUpdateLinkTypes(List<LinkType> pLinkTypesList,
            String pProcessName) {

        if (null != pLinkTypesList) {
            for (LinkType lLinkType : pLinkTypesList) {
                createOrUpdateLinkType(lLinkType, pProcessName);
            }
        }
    }

    /**
     * Create or update link type.
     * 
     * @param pLinkType
     *            the link type
     * @param pProcessName
     *            the process name
     */
    private void createOrUpdateLinkType(LinkType pLinkType, String pProcessName) {
        LinkTypeData lLinkTypeData = null;
        String lLinkTypeId = null;

        lLinkTypeData =
                linkService.getLinkTypeByName(roleToken, pProcessName,
                        pLinkType.getName());

        if (null == lLinkTypeData) {
            lLinkTypeData = new LinkTypeData();
            lLinkTypeData.setName(pLinkType.getName());
            lLinkTypeData.setDescription(pLinkType.getDescription());

            lLinkTypeData.setLowBound(pLinkType.getLowBound());
            lLinkTypeData.setHighBound(pLinkType.getHighBound());
            lLinkTypeData.setUnidirectionalCreation(pLinkType.isUnidirectionalCreation());
            lLinkTypeData.setUnidirectionalNavigation(pLinkType.isUnidirectionalNavigation());

            lLinkTypeData.setOriginType(pLinkType.getOriginType());
            lLinkTypeData.setDestinationType(pLinkType.getDestinationType());
            try {
                lLinkTypeId =
                        linkService.createLinkType(roleToken, pProcessName,
                                lLinkTypeData);

                // Create the ext points for the sheet type (list of commands
                // ref).
                createOrUpdateExtensionPoints(pLinkType, lLinkTypeId);
            }
            catch (GDMException e) {
                error(e.getMessage(), pLinkType);
            }

            // If filters are defined to sort the link type
            if (pLinkType.getSorts() != null) {
                List<AttributeValue> lOriginFilterName = null;
                List<AttributeValue> lDestinationFilterName = null;
                List<AttributeValue> lBothFilterName = null;

                // If several LinkTypeSorter with the same sheet type,
                // only the econd is kept
                for (LinkTypeSorter lSort : pLinkType.getSorts()) {
                    List<AttributeValue> lValues =
                            new ArrayList<AttributeValue>();
                    lValues.add(new AttributeValue(lSort.getFilterName()));

                    if (lSort.getSheetType().equals(
                            LinkType.FILTER_FOR_ORIGIN_SHEET)) {
                        lOriginFilterName = lValues;
                    }
                    else if (lSort.getSheetType().equals(
                            LinkType.FILTER_FOR_DESTINATION_SHEET)) {
                        lDestinationFilterName = lValues;
                    }
                    else {
                        lBothFilterName = lValues;
                    }
                }

                // Add attributes for define the filters associated to sheet link types
                boolean lDifferentLinkedSheetTypes =
                        !pLinkType.getOriginType().equals(
                                pLinkType.getDestinationType());
                List<Attribute> lNewAttributes = new ArrayList<Attribute>();

                // Sheet type priorities
                // The linked sheet have a different sheet type
                if (lDifferentLinkedSheetTypes) {
                    // For the origin sheet type: Origin -> Both -> null
                    if (lOriginFilterName != null) {
                        lNewAttributes.add(new Attribute(
                                LinkType.FILTER_FOR_ORIGIN_SHEET,
                                lOriginFilterName));
                    }
                    else if (lBothFilterName != null) {
                        lNewAttributes.add(new Attribute(
                                LinkType.FILTER_FOR_ORIGIN_SHEET,
                                lBothFilterName));
                    }
                    // For the destination sheet type: Destination -> Both -> null
                    if (lDestinationFilterName != null) {
                        lNewAttributes.add(new Attribute(
                                LinkType.FILTER_FOR_DESTINATION_SHEET,
                                lDestinationFilterName));
                    }
                    else if (lBothFilterName != null) {
                        lNewAttributes.add(new Attribute(
                                LinkType.FILTER_FOR_DESTINATION_SHEET,
                                lBothFilterName));
                    }
                }
                // The linked sheet have the same sheet type
                else {
                    // For the two sheet types: Origin -> Destination -> Both -> null
                    // For the origin sheet type: Origin -> Both -> null
                    if (lOriginFilterName != null) {
                        lNewAttributes.add(new Attribute(
                                LinkType.FILTER_FOR_ORIGIN_SHEET,
                                lOriginFilterName));
                        lNewAttributes.add(new Attribute(
                                LinkType.FILTER_FOR_DESTINATION_SHEET,
                                lOriginFilterName));
                    }
                    else if (lDestinationFilterName != null) {
                        lNewAttributes.add(new Attribute(
                                LinkType.FILTER_FOR_ORIGIN_SHEET,
                                lDestinationFilterName));
                        lNewAttributes.add(new Attribute(
                                LinkType.FILTER_FOR_DESTINATION_SHEET,
                                lDestinationFilterName));
                    }
                    else if (lBothFilterName != null) {
                        lNewAttributes.add(new Attribute(
                                LinkType.FILTER_FOR_ORIGIN_SHEET,
                                lBothFilterName));
                        lNewAttributes.add(new Attribute(
                                LinkType.FILTER_FOR_DESTINATION_SHEET,
                                lBothFilterName));
                    }
                }

                if (pLinkType.getAttributes() == null) {
                    pLinkType.setAttributes(lNewAttributes);
                }
                else {
                    pLinkType.getAttributes().addAll(lNewAttributes);
                }
            }
        }
        else {
            lLinkTypeId = lLinkTypeData.getId();
        }
        updateFieldsContainer(lLinkTypeId, pLinkType);

        fieldsContainerService.initDynamicMapping(lLinkTypeId);
    }

    /**
     * Create or update a fields container.
     * <p>
     * This method fills the fields definitions, display hints, and attributes
     * of a fields container.
     * 
     * @param pContainerId
     *            Unique identifier of the container to update
     * @param pContainer
     *            the container
     */
    void updateFieldsContainer(String pContainerId, FieldsContainer pContainer) {
        createFields(pContainerId, pContainer.getFields());
        createDisplayHints(pContainerId, pContainer.getDisplayHints());
        createAttributes(pContainerId, pContainer.getAttributes());
    }

    /**
     * Create or update a list of products.
     * 
     * @param pProcessName
     *            The current process.
     * @param pProducts
     *            the products
     * @return The id of the product created
     */
    private void instantiateProducts(List<Product> pProducts) {
        if (CollectionUtils.isEmpty(pProducts)) {
            return;
        }
        importProperties.setProductsFlag(ImportFlag.CREATE_OR_UPDATE);
        for (Product lProduct : pProducts) {
            try {
                String lLocalId = lProduct.getId();
                if (!isUUID(lProduct.getId())) {
                    lProduct.setId(null);
                }
                cleanIdentifier(lProduct);
                ImportExecutionReport lReport =
                        importService.importProduct(roleToken, lProduct,
                                importProperties, creationCtx);

                List<ElementReport> lIds =
                        lReport.getElementReport(ElementType.PRODUCT);
                if (lIds.size() == 1) {
                    addGpmId(lLocalId, lIds.get(0).getElementId());
                }
            }
            catch (ImportException e) {
                error(e.getMessage(), lProduct, e);
            }
        }
    }

    /**
     * Create or update a list of sheets.
     * 
     * @param pSheets
     *            The sheets list to create or update.
     * @param pProcessName
     *            The current process.
     */
    private void instantiateSheets(
            List<org.topcased.gpm.business.serialization.data.SheetData> pSheets) {
        if (CollectionUtils.isEmpty(pSheets)) {
            return;
        }
        importProperties.setSheetsFlag(ImportFlag.CREATE_OR_UPDATE);
        for (org.topcased.gpm.business.serialization.data.SheetData lSheet : pSheets) {
            try {
                String lLocalId = lSheet.getId();
                if (!isUUID(lSheet.getId())) {
                    lSheet.setId(StringUtils.EMPTY);
                }
                cleanIdentifier(lSheet);
                importService.importSheet(roleToken, lSheet, importProperties,
                        creationCtx);
                addGpmId(lLocalId, lSheet.getId());
            }
            catch (InvalidNameException e) {
                error("", lSheet, e);
            }
            catch (MandatoryValuesException e) {
                StringBuilder lErrorMessage = new StringBuilder(e.getMessage());
                lErrorMessage.append(" For the fields: ");
                for (org.topcased.gpm.business.exception.MandatoryValuesException.FieldRef lFieldRef : e.getFields()) {
                    lErrorMessage.append(lFieldRef.getLabelKey());
                }
                error(lErrorMessage.toString(), lSheet);
            }
            catch (FieldValidationException e) {
                error(e.getMessage(), lSheet, e);
            }
            catch (ImportException e) {
                error(e.getMessage(), e.getObject(), e);
            }
        }
    }

    /**
     * Transform a serializationContainerLock into a business ContainerLockData
     * structure.
     * 
     * @param pSheetId
     *            the sheet id
     * @param pLock
     *            the lock
     */
    protected void updateLock(String pSheetId, Lock pLock) {
        if (pLock == null) {
            return;
        }
        sheetService.lockSheet(roleToken, pSheetId, pLock);
    }

    /**
     * Create a list of links.
     * 
     * @param pProcessName
     *            The current process.
     * @param pLinks
     *            the links
     */
    private void instantiateSheetLinks(List<Link> pLinks) {
        if (CollectionUtils.isEmpty(pLinks)) {
            return;
        }
        importProperties.setSheetLinksFlag(ImportFlag.CREATE_OR_UPDATE);
        for (Link lLink : pLinks) {
            try {
                String lOriginId = getGpmId(lLink.getOriginId());
                String lDestinationId = getGpmId(lLink.getDestinationId());
                lLink.setOriginId(lOriginId);
                lLink.setDestinationId(lDestinationId);
                cleanIdentifier(lLink);
                importService.importLink(roleToken, lLink, importProperties,
                        creationCtx);
            }
            catch (FieldValidationException e) {
                error(e.getMessage(), lLink, e);
            }
            catch (ImportException e) {
                error(e.getMessage(), e.getObject(), e);
            }
        }
    }

    /**
     * Create a list of product links.
     * 
     * @param pProcessName
     *            The current process.
     * @param pLinks
     *            the links
     */
    private void instantiateProductLinks(List<Link> pLinks) {
        if (CollectionUtils.isEmpty(pLinks)) {
            return;
        }

        importProperties.setProductLinksFlag(ImportFlag.CREATE_OR_UPDATE);
        for (Link lLink : pLinks) {
            try {
                String lOriginId = getGpmId(lLink.getOriginId());
                String lDestinationId = getGpmId(lLink.getDestinationId());
                lLink.setOriginId(lOriginId);
                lLink.setDestinationId(lDestinationId);
                cleanIdentifier(lLink);
                importService.importLink(roleToken, lLink, importProperties,
                        creationCtx);
            }
            catch (FieldValidationException e) {
                error(e.getMessage(), lLink, e);
            }
            catch (ImportException e) {
                error(e.getMessage(), e.getObject(), e);
            }
        }
    }

    /**
     * (re)create the filters.
     * 
     * @param pFilters
     *            the filters
     * @param pProcessName
     *            the process name
     */
    private void instantiateFilters(List<Filter> pFilters) {
        if (CollectionUtils.isEmpty(pFilters)) {
            return;
        }

        importProperties.setFiltersFlag(ImportFlag.CREATE_OR_UPDATE);
        for (Filter lFilter : pFilters) {
            try {
                importService.importFilter(roleToken, lFilter,
                        importProperties, creationCtx);
            }
            catch (ImportException e) {
                error(e.getMessage(), e.getObject(), e);
            }
        }
    }

    /**
     * Creates the reports.
     * 
     * @param pReports
     *            the reports
     * @param pProcessName
     *            the process name
     */
    private void createReports(List<ReportModel> pReports, String pProcessName) {
        if (null == pReports) {
            return;
        }
        for (ReportModel lReportModel : pReports) {

            String[] lContainersIds = null;
            String[] lExportTypes = null;

            // Create container Ids from Container names
            if (lReportModel.getContainers() != null) {
                FilterCreator lFilterCreator =
                        new FilterCreator(roleToken, null, pProcessName,
                                serviceLocator);
                lContainersIds =
                        lFilterCreator.createContainersId(
                                lReportModel.getContainers(), pProcessName);
            }

            // Find ExportTypes
            if (lReportModel.getExportTypes() != null) {
                lExportTypes = new String[lReportModel.getExportTypes().size()];
                int i = 0;
                for (ExportType lExportType : lReportModel.getExportTypes()) {
                    lExportTypes[i] = lExportType.getExportFormat();
                    i++;
                }
            }

            // Create the report model data object.
            ReportModelData lReportModelData =
                    new ReportModelData(lReportModel.getName(),
                            lReportModel.getDescription(),
                            lReportModel.getPath(), lContainersIds,
                            lExportTypes, null);
            // Save the report model in DB.
            reportingService.createOrUpdate(roleToken, processName,
                    lReportModelData);
        }
    }

    /**
     * Update a sheet or a product.
     * 
     * @param pValues
     *            the values
     * @param pValuesContainerAccess
     *            the values container access
     */
    private void updateValues(List<FieldValueData> pValues,
            FieldCompositeAccess pValuesContainerAccess) {
        if (null == pValues) {
            return;
        }

        // Set to store the processed field names
        Set<String> lProcessedFieldNames = new HashSet<String>();

        for (FieldValueData lValue : pValues) {
            updateFieldValue(pValuesContainerAccess, lValue,
                    lProcessedFieldNames);
        }
    }

    /**
     * Update a field.
     * 
     * @param pFieldAccess
     *            The field access.
     * @param pFieldValue
     *            The field element.
     * @param pProcessedFieldnames
     *            the processed fieldnames
     */
    private void updateFieldValue(FieldCompositeAccess pFieldAccess,
            FieldValueData pFieldValue, Set<String> pProcessedFieldnames) {

        FieldAccess lFieldAccess = pFieldAccess.getField(pFieldValue.getName());
        if (lFieldAccess == null) {
            error("Field name '" + pFieldValue.getName() + "' invalid",
                    pFieldValue);
        }

        // Special case when a group and a field have the same name.
        if (lFieldAccess instanceof FieldGroupAccess) {
            FieldGroupAccess lGroupAccess = (FieldGroupAccess) lFieldAccess;
            updateFieldValue(lGroupAccess, pFieldValue, pProcessedFieldnames);
            return;
        }

        boolean lNeedNewLine = false;

        // If this field name is already known, that means we have to create a
        // new line.
        if (pProcessedFieldnames.contains(lFieldAccess.getName())) {
            lNeedNewLine = true;
        }
        else {
            pProcessedFieldnames.add(lFieldAccess.getName());
        }

        if (lFieldAccess instanceof MultipleLineAccess) {
            MultipleLineAccess lMultiLineElement =
                    (MultipleLineAccess) lFieldAccess;

            FieldAccess lElemAccess;
            if (lNeedNewLine) {
                lElemAccess = lMultiLineElement.addLine();
            }
            else {
                lElemAccess = lMultiLineElement.iterator().next();
            }

            if (lElemAccess instanceof FieldElementAccess) {
                updateFieldValue((FieldElementAccess) lElemAccess, pFieldValue,
                        lNeedNewLine);
            }
            else if (lElemAccess instanceof MultipleFieldAccess) {
                MultipleFieldAccess lMultiFieldAccess =
                        (MultipleFieldAccess) lElemAccess;
                updateValues(pFieldValue.getFieldValues(), lMultiFieldAccess);
            }
        }
        else if (lFieldAccess instanceof MultipleFieldAccess) {
            MultipleFieldAccess lMultiFieldAccess =
                    (MultipleFieldAccess) lFieldAccess;
            updateValues(pFieldValue.getFieldValues(), lMultiFieldAccess);
        }
        else if (lFieldAccess instanceof FieldElementAccess) {
            updateFieldValue((FieldElementAccess) lFieldAccess, pFieldValue,
                    lNeedNewLine);
        }
    }

    /**
     * Update field value.
     * 
     * @param pFieldElement
     *            the field element
     * @param pFieldValue
     *            the field value
     * @param pNeedNewLine
     *            the need new line
     */
    private void updateFieldValue(FieldElementAccess pFieldElement,
            FieldValueData pFieldValue, boolean pNeedNewLine) {
        if (pFieldElement.getType().equals("CHOICE_MULTIPLE")) {
            if (pNeedNewLine) {
                String[] lValues = pFieldElement.getValues();
                lValues =
                        (String[]) ArrayUtils.add(lValues,
                                pFieldValue.getValue());
                pFieldElement.setValues(lValues);
            }
            else {
                pFieldElement.setValues(new String[] { pFieldValue.getValue() });
            }
        }
        else if (pFieldElement.getType().equals("FILE")) {
            if (!(pFieldValue instanceof AttachedFieldValueData)) {
                error("The field '" + pFieldElement.getName()
                        + "' requires an " + "attached field value data",
                        pFieldValue);
            }
            AttachedFieldModificationData lAttachedData =
                    new AttachedFieldModificationData();
            AttachedFieldValueData lAttachedFieldValue =
                    (AttachedFieldValueData) pFieldValue;
            InputStream lFileInput =
                    load(lAttachedFieldValue.getFilename(), lAttachedFieldValue);

            ByteArrayOutputStream lContentOutput = new ByteArrayOutputStream();

            try {
                IOUtils.copy(lFileInput, lContentOutput);
            }
            catch (IOException e) {
                error("Cannot read file content '"
                        + lAttachedFieldValue.getFilename() + "'",
                        lAttachedFieldValue, e);
            }

            // Get the 'name' to use for the attached field value.
            // We use the 'value' defined for the XML element, or (if
            // unset), the actual filename, with absolute or relative
            // path expression removed.
            String lName = lAttachedFieldValue.getValue();
            if (StringUtils.isBlank(lName)) {
                lName = lAttachedFieldValue.getFilename();
                lName = lName.substring(lName.lastIndexOf("/") + 1);
            }
            lAttachedData.setName(lName);
            lAttachedData.setMimeType(lAttachedFieldValue.getMimeType());
            lAttachedData.setContent(lContentOutput.toByteArray());

            pFieldElement.setFileValue(lAttachedData);
        }
        else {
            pFieldElement.setValues(new String[] { pFieldValue.getValue() });
        }
    }

    /**
     * Create/set the attributes for an element.
     * 
     * @param pIdent
     *            Identifier of the element containing the attributes
     * @param pAttrs
     *            Attributes list
     */
    private void createAttributes(String pIdent,
            final List<? extends Attribute> pAttrs) {

        if (null == pAttrs) {
            return;
        }
        AttributeData[] lAttrData = getAttributesData(pAttrs);
        attributesService.set(pIdent, lAttrData);
    }

    /**
     * Get an AttributeData array from a list of Create/set the attributes for
     * an element.
     * 
     * @param pAttrs
     *            Attributes list
     * @return the attributes data
     */
    private AttributeData[] getAttributesData(
            final List<? extends Attribute> pAttrs) {
        if (null == pAttrs) {
            return null;
        }

        AttributeData[] lAttrData = new AttributeData[pAttrs.size()];
        int i = 0;

        for (Attribute lAttr : pAttrs) {
            lAttrData[i] =
                    new AttributeData(lAttr.getName(), lAttr.getValues());
            ++i;
        }
        return lAttrData;
    }

    /**
     * Converts the given Attribute list to AttributeData table.
     * 
     * @param pAttrs
     *            List of attribute to convert.
     * @return Table of converted attributes.
     */
    private AttributeData[] toAttributeDatas(
            final List<? extends Attribute> pAttrs) {

        AttributeData[] lAttrData = null;
        if (pAttrs != null) {
            lAttrData = new AttributeData[pAttrs.size()];
            int i = 0;
            for (Attribute lAttr : pAttrs) {
                lAttrData[i] =
                        new AttributeData(lAttr.getName(), lAttr.getValues());
                ++i;
            }

        }
        return lAttrData;
    }

    /**
     * Create the gPM object and the global attributes.
     * 
     * @param pGpm
     *            the gPM object
     */
    private void createGpmAndGlobalAttributes(Gpm pGpm) {
        instanceService.createGpm(roleToken);

        // default authentication is internal.
        String lAuthentication = AttributesService.INTERNAL_AUTHENTICATION;
        Boolean lIsDefaultAuthentication = Boolean.TRUE;
        //default 'filterFieldsMaxDepth' is 1
        String lFilterFieldsMaxDepth =
                AttributesService.FILTER_FIELDS_MAX_DEPTH_DEFAULT_VALUE;
        //default 'autolocking' is WRITE
        String lAutolockingValue = AttributesService.AUTOLOCKING_DEFAULT;
        Boolean lIsDefaultAutolocking = Boolean.TRUE;

        int lEstimatedAttributesCount = 1;

        if (pGpm.getAttributes() != null) {
            lEstimatedAttributesCount += pGpm.getAttributes().size();
        }

        List<AttributeData> lGlobalAttributes =
                new ArrayList<AttributeData>(lEstimatedAttributesCount);
        List<AttributeData> lProcessAttributes =
                new ArrayList<AttributeData>(lEstimatedAttributesCount);

        // If global options are specified.
        if (pGpm.getOptions() != null) {
            Option lOption = pGpm.getOptions();
            if (lOption.getAuthentication() != null) {
                lAuthentication = lOption.getAuthentication();
                lIsDefaultAuthentication = Boolean.FALSE;
            }
            if (lOption.getUserIdParamName() != null) {
                String lUserIdParamName = lOption.getUserIdParamName();
                lGlobalAttributes.add(new AttributeData(
                        AttributesService.USER_ID_PARAMETER_NAME,
                        new String[] { lUserIdParamName }));
            }
            if (lOption.getContactUrl() != null) {
                String lContactUrl = lOption.getContactUrl();
                lGlobalAttributes.add(new AttributeData(
                        AttributesService.CONTACT_URL,
                        new String[] { lContactUrl }));
            }
            if (lOption.getFilterFieldsMaxDepth() != null) {
                lFilterFieldsMaxDepth = lOption.getFilterFieldsMaxDepth();
                lGlobalAttributes.add(new AttributeData(
                        AttributesService.FILTER_FIELDS_MAX_DEPTH,
                        new String[] { lFilterFieldsMaxDepth }));
            }
            if (lOption.getMaxExportableSheets() != null) {
                String lMaxExportableSheets = lOption.getMaxExportableSheets();
                lGlobalAttributes.add(new AttributeData(
                        AttributesService.MAX_EXPORTABLE_SHEETS,
                        new String[] { lMaxExportableSheets }));
            }
            if (lOption.getLoginCaseSensitive() != null) {
                String lLoginCaseSensitive = lOption.getLoginCaseSensitive();
                lGlobalAttributes.add(new AttributeData(
                        AttributesService.LOGIN_CASE_SENSITIVE,
                        new String[] { lLoginCaseSensitive }));
            }
            if (lOption.getHelpContentUrl() != null) {
                String lHelpContentUrl = lOption.getHelpContentUrl();
                lGlobalAttributes.add(new AttributeData(
                        AttributesService.HELP_CONTENT_URL,
                        new String[] { lHelpContentUrl }));
            }
            if (lOption.getAutolocking() != null) {
                lAutolockingValue = lOption.getAutolocking();
                lIsDefaultAutolocking = Boolean.FALSE;
            }
            if (lOption.getSqlFunctionCaseSensitive() != null) {
                String lSqlFunctionCaseSensitive =
                        lOption.getSqlFunctionCaseSensitive();
                lProcessAttributes.add(new AttributeData(
                        SqlFunction.CASE_SENSITIVE.toString(),
                        new String[] { lSqlFunctionCaseSensitive }));
            }
        }

        String[] lGlobalAttributesNames =
                { AttributesService.AUTHENTICATION,
                 AttributesService.AUTOLOCKING };
        AttributeData[] lGlobalAttributesValues =
                attributesService.getGlobalAttributes(lGlobalAttributesNames);

        // Define the 'AUTHENTICATION' attribute if not already defined
        if (lGlobalAttributesValues[0] == null || !lIsDefaultAuthentication) {
            lGlobalAttributes.add(new AttributeData(
                    AttributesService.AUTHENTICATION,
                    new String[] { lAuthentication }));
        }

        // Define the 'AUTOLOCKING' attribute if not already defined
        if (lGlobalAttributesValues[1] == null || !lIsDefaultAutolocking) {
            lGlobalAttributes.add(new AttributeData(
                    AttributesService.AUTOLOCKING,
                    new String[] { lAutolockingValue }));
        }

        // Add other global attributes to the list
        if (pGpm.getAttributes() != null) {
            for (Attribute lAttr : pGpm.getAttributes()) {
                lGlobalAttributes.add(new AttributeData(lAttr.getName(),
                        lAttr.getValues()));
            }
        }

        // Set global attributes
        attributesService.setGlobalAttributes(
                roleToken,
                lGlobalAttributes.toArray(new AttributeData[lGlobalAttributes.size()]));

        // Set process attributes
        attributesService.set(
                instanceService.getBusinessProcessId(processName),
                lProcessAttributes.toArray(new AttributeData[lProcessAttributes.size()]));
    }

    private void createOrUpdateMapping(final List<TypeMapping> pMapping) {
        if (pMapping != null) {
            for (TypeMapping lTypeMapping : pMapping) {
                valuesService.createTypeMapping(roleToken, lTypeMapping);
            }
        }
    }

    /**
     * Generate dynamic data base model
     * 
     * @param pProductTypes
     *            The list of product types
     * @param pProductLinkTypes
     *            The list of product's links types
     * @param pSheetTypes
     *            The list of sheet types
     * @param pSheetLinkTypes
     *            The list of sheet's links types
     */
    private void generateDynamicDbSchema(List<ProductType> pProductTypes,
            List<LinkType> pProductLinkTypes, List<SheetType> pSheetTypes,
            List<LinkType> pSheetLinkTypes) {
        int lNbNewTypes = 0;

        // Product Type Data Model
        if (pProductTypes != null) {
            lNbNewTypes += pProductTypes.size();
        }

        // Product Link Type Data Model
        if (pProductLinkTypes != null) {
            lNbNewTypes += pProductLinkTypes.size();
        }

        // Sheet Type Data Model
        if (pSheetTypes != null) {
            lNbNewTypes += pSheetTypes.size();
        }

        // Sheet Link Type Data Model
        if (pSheetLinkTypes != null) {
            lNbNewTypes += pSheetLinkTypes.size();
        }

        // Generate entities for modeling dynamic schema 
        if (lNbNewTypes > 0) {
            // Recreate session factory if dynamic generation mode
            // is set on the options
            if ((Boolean) creationCtx.get(InstantiateOptions.AUTO_GENERATE_DYNAMIC_MODEL)) {
                // Recreate session factory
                GpmSessionFactory.getInstance().updateDatabaseSchema();
            }
        }
    }

    /**
     * Gets the gpm id.
     * 
     * @param pId
     *            the id
     * @return the gpm id
     */
    private String getGpmId(String pId) {
        if (pId.startsWith(ID_PREFIX)) {
            return pId.substring(ID_PREFIX.length());
        }
        else if (isUUID(pId)) {
            return pId;
        }
        return localId2gpmId.get(pId);
    }

    /**
     * Adds the gpm id.
     * 
     * @param pLocalId
     *            the local id
     * @param pGpmId
     *            the gpm id
     */
    private void addGpmId(String pLocalId, String pGpmId) {
        if (!isUUID(pLocalId)) {
            localId2gpmId.put(pLocalId, pGpmId);
        }
    }

    /**
     * Check if the given string looks like an UUID
     * 
     * @param pId
     *            String to be tested
     * @return True if the string seems to be an UUID.
     */
    private boolean isUUID(String pId) {
        if (StringUtils.isBlank(pId)) {
            return false;
        }
        try {
            // Check if the 'id' looks like an UUID
            UUID.fromString(pId);
            return true;
        }
        catch (IllegalArgumentException e) {
        }
        return false;
    }

    /**
     * Initialize the gPM services.
     */
    private void initServices() {
        serviceLocator = ServiceLocator.instance();
        i18nService = serviceLocator.getI18nService();
        fieldsService = serviceLocator.getFieldsService();
        sheetService = serviceLocator.getSheetService();
        linkService = serviceLocator.getLinkService();
        productService = serviceLocator.getProductService();
        authorizationService = serviceLocator.getAuthorizationService();
        extensionsService = serviceLocator.getExtensionsService();
        displayGroupService = serviceLocator.getDisplayService();
        lifeCycleService = serviceLocator.getLifeCycleService();
        searchService = serviceLocator.getSearchService();
        attributesService = serviceLocator.getAttributesService();
        reportingService = serviceLocator.getReportingService();
        instanceService = serviceLocator.getInstanceService();
        fieldsContainerService = serviceLocator.getFieldsContainerService();
        valuesService =
                (ValuesService) ContextLocator.getContext().getBean(
                        "valuesService");
        importService = serviceLocator.getImportService();
    }

    /**
     * Reports an error message.
     * 
     * @param pMsg
     *            The text of the error message
     * @param pObj
     *            The serialization object (read from the XML file) producing
     *            the error (this is used to get and display the corresponding
     *            line number). This parameter can be null.
     * @param pException
     *            The exception to be displayed in the error report. This
     *            parameter can be null.
     * @throws InstantiateException
     *             An instantiate exception is always thrown here (catched
     *             globally to actually reports the error)
     */
    private void error(String pMsg, Object pObj, Throwable pException)
        throws InstantiateException {
        String lFullMsg = pMsg;

        if (null != pException) {
            if (StringUtils.isBlank(pMsg)) {
                lFullMsg = pException.getMessage();
            }
            else {
                lFullMsg += "\n" + pException.getMessage();
            }
        }
        throw new InstantiateException(lFullMsg, pObj);
    }

    /**
     * Reports an error message.
     * 
     * @param pMsg
     *            The text of the error message
     * @param pObj
     *            The serialization object (read from the XML file) producing
     *            the error (this is used to get and display the corresponding
     *            line number). This parameter can be null.
     * @throws InstantiateException
     *             An instantiate exception is always thrown here (catched
     *             globally to actually reports the error)
     */
    private void error(String pMsg, Object pObj) throws InstantiateException {
        error(pMsg, pObj, null);
    }

    /**
     * Reports an error message.
     * 
     * @param pMsg
     *            The text of the error message
     * @throws InstantiateException
     *             An instantiate exception is always thrown here (catched
     *             globally to actually reports the error)
     */
    private void error(String pMsg) throws InstantiateException {
        error(pMsg, null);
    }

    private void cleanIdentifier(final ValuesContainerData pValuesContainer) {
        //Clean pointer fields identifier.
        if (CollectionUtils.isNotEmpty(pValuesContainer.getFieldValues())) {
            for (FieldValueData lField : pValuesContainer.getFieldValues()) {
                if (lField instanceof PointerFieldValueData) {
                    PointerFieldValueData lPointerField =
                            (PointerFieldValueData) lField;
                    if (StringUtils.isNotBlank(lPointerField.getReferencedContainerId())) {
                        String lContainerId =
                                getGpmId(lPointerField.getReferencedContainerId());
                        lPointerField.setReferencedContainerId(lContainerId);
                    }
                }
            }
        }
    }

    /**
     * Creates the display group for product type if no display group is defined
     * in instance XML file.
     * 
     * @param pFieldsContainer
     *            the fields container
     * @param pContainerId
     *            the container id
     */
    private void createDisplayGroupForProductType(
            FieldsContainer pFieldsContainer, String pContainerId) {

        // Remove old display groups. (for migration)
        List<DisplayGroupData> lDisplayGroupsToRemove =
                displayGroupService.getDisplayGroupList(roleToken, pContainerId);
        if (lDisplayGroupsToRemove != null) {
            for (DisplayGroupData lDisplayGroupData : lDisplayGroupsToRemove) {
                displayGroupService.removeDisplayGroup(
                        lDisplayGroupData.getLabelKey(), pContainerId);
            }
        }

        // Create the display group.
        DisplayGroup lDisplayGroup = new DisplayGroup();
        try {
            //this object DisplayGroupData is necessary to create
            //a display group - used as parameter
            DisplayGroupData lDisplayGroupData;
            lDisplayGroupData = new DisplayGroupData();
            lDisplayGroupData.setContainerId(pContainerId);
            lDisplayGroupData.setDisplayOrder(1);
            //set a default title to display group
            //this title can be changed using translation
            lDisplayGroupData.setLabelKey(UNIQUE_DISPLAY_GROUP_PRODUCT_TITLE);

            boolean lIsOpened = true;
            if (lDisplayGroup.getOpened() != null) {
                lIsOpened = lDisplayGroup.getOpened();
            }
            lDisplayGroupData.setOpened(lIsOpened);

            FieldSummaryData[] lFieldSummaryDataArray;
            if (pFieldsContainer.getFields() != null) {
                lFieldSummaryDataArray =
                        new FieldSummaryData[pFieldsContainer.getFields().size()];

                int i = 0;
                //get the fields from the container (the product)
                //and assign them to DisplayGroupData
                for (Field lField : pFieldsContainer.getFields()) {
                    FieldSummaryData lFsd = new FieldSummaryData();
                    lFsd.setLabelKey(lField.getLabelKey());
                    lFieldSummaryDataArray[i++] = lFsd;
                }
            }
            else {
                lFieldSummaryDataArray = new FieldSummaryData[0];
            }
            lDisplayGroupData.setFieldSummaryDatas(lFieldSummaryDataArray);

            displayGroupService.createDisplayGroup(lDisplayGroupData);
        }
        catch (GDMException e) {
            error(e.getMessage(), lDisplayGroup, e);
        }
    }
}
