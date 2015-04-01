/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.extensions.service;

import java.util.Collection;
import java.util.List;

import org.topcased.gpm.business.ServiceLocator;
import org.topcased.gpm.business.fields.impl.CacheableFieldsContainer;
import org.topcased.gpm.business.fields.impl.CacheableInputData;
import org.topcased.gpm.business.fields.impl.CacheableValuesContainer;
import org.topcased.gpm.business.link.impl.CacheableLink;
import org.topcased.gpm.business.link.impl.CacheableLinkType;
import org.topcased.gpm.business.link.service.LinkData;
import org.topcased.gpm.business.product.impl.CacheableProduct;
import org.topcased.gpm.business.product.impl.CacheableProductType;
import org.topcased.gpm.business.revision.RevisionData;
import org.topcased.gpm.business.serialization.data.Lock;
import org.topcased.gpm.business.sheet.impl.CacheableSheet;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.business.values.BusinessContainer;
import org.topcased.gpm.business.values.field.BusinessField;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Name of the different parameters used on extension points
 * 
 * @author tpanuel
 */
@SuppressWarnings("unchecked")
public class ExtensionPointParameters {
    /* GLOBAL */
    /** The name of the current process */
    public final static ContextParameter<String> PROCESS_NAME =
            new ContextParameter<String>("processName", String.class);

    /** The name of the current extension point */
    public final static ContextParameter<String> EXTENSION_POINT_NAME =
            new ContextParameter<String>("extensionPointName", String.class);

    /** The service locator */
    public final static ContextParameter<ServiceLocator> SERVICE_LOCATOR =
            new ContextParameter<ServiceLocator>("serviceLocator",
                    ServiceLocator.class);

    /** The role token of the current session */
    public final static ContextParameter<String> ROLE_TOKEN =
            new ContextParameter<String>("roleToken", String.class);

    /** The user token of the current session */
    public final static ContextParameter<String> USER_TOKEN =
            new ContextParameter<String>("userToken", String.class);

    /** The name of the current role */
    public final static ContextParameter<String> ROLE_NAME =
            new ContextParameter<String>("roleName", String.class);

    /** The role token of the administrator */
    public final static ContextParameter<String> ADMIN_ROLE_TOKEN =
            new ContextParameter<String>("adminRoleToken", String.class);

    /** The name of the current product */
    public final static ContextParameter<String> PRODUCT_NAME =
            new ContextParameter<String>("productName", String.class);

    /* VALUES CONTAINER */
    /** The id current values container */
    public final static ContextParameter<String> VALUES_CONTAINER_ID =
            new ContextParameter<String>("valuesContainerId", String.class);

    /** The current values container */
    public final static ContextParameter<CacheableValuesContainer> VALUES_CONTAINER =
            new ContextParameter<CacheableValuesContainer>("valuesContainer",
                    CacheableValuesContainer.class);

    /** The current fields container */
    public final static ContextParameter<CacheableFieldsContainer> FIELDS_CONTAINER =
            new ContextParameter<CacheableFieldsContainer>("fieldsContainer",
                    CacheableFieldsContainer.class);

    /** The current fields container id */
    public final static ContextParameter<String> FIELDS_CONTAINER_ID =
            new ContextParameter<String>("fieldsContainerId", String.class);

    /** The current fields container name */
    public final static ContextParameter<String> FIELDS_CONTAINER_NAME =
            new ContextParameter<String>("fieldsContainerName", String.class);

    /**
     * The values container used like source for the current duplication /
     * initialization
     */
    public final static ContextParameter<CacheableValuesContainer> SOURCE_VALUES_CONTAINER =
            new ContextParameter<CacheableValuesContainer>(
                    "sourceValuesContainer", CacheableValuesContainer.class);

    /** The business container */
    public final static ContextParameter<BusinessContainer> BUSINESS_CONTAINER =
            new ContextParameter<BusinessContainer>("container",
                    BusinessContainer.class);

    /**
     * The business container used like source for the current initialization
     */
    public final static ContextParameter<BusinessContainer> SOURCE_BUSINESS_CONTAINER =
            new ContextParameter<BusinessContainer>("sourceContainer",
                    BusinessContainer.class);

    /* SHEET */
    /** The id of the current sheet */
    public final static ContextParameter<String> SHEET_ID =
            new ContextParameter<String>("valuesContainerId", String.class);

    /** The current sheet */
    public final static ContextParameter<CacheableSheet> SHEET =
            new ContextParameter<CacheableSheet>("valuesContainer",
                    CacheableSheet.class);

    /** The type of the current sheet */
    public final static ContextParameter<CacheableSheetType> SHEET_TYPE =
            new ContextParameter<CacheableSheetType>("fieldsContainer",
                    CacheableSheetType.class);

    /** The sheet used like source for the current duplication / initialization */
    public final static ContextParameter<CacheableSheet> SOURCE_SHEET =
            new ContextParameter<CacheableSheet>("sourceValuesContainer",
                    CacheableSheet.class);

    /* LINK */
    /** The id of the current link */
    public final static ContextParameter<String> LINK_ID =
            new ContextParameter<String>("valuesContainerId", String.class);

    /** The current link */
    public final static ContextParameter<CacheableLink> LINK =
            new ContextParameter<CacheableLink>("valuesContainer",
                    CacheableLink.class);

    /** The type of the current link */
    public final static ContextParameter<CacheableLinkType> LINK_TYPE =
            new ContextParameter<CacheableLinkType>("fieldsContainer",
                    CacheableLinkType.class);

    /* PRODUCT */
    /** The id of the current product */
    public final static ContextParameter<String> PRODUCT_ID =
            new ContextParameter<String>("valuesContainerId", String.class);

    /** The current product */
    public final static ContextParameter<CacheableProduct> PRODUCT =
            new ContextParameter<CacheableProduct>("valuesContainer",
                    CacheableProduct.class);

    /** The type of the current product */
    public final static ContextParameter<CacheableProductType> PRODUCT_TYPE =
            new ContextParameter<CacheableProductType>("fieldsContainer",
                    CacheableProductType.class);

    /* FIELD */
    /** The Id of the current field */
    public final static ContextParameter<String> FIELD_ID =
            new ContextParameter<String>("fieldId", String.class);

    /** The name of the current field */
    public final static ContextParameter<String> FIELD_NAME =
            new ContextParameter<String>("fieldName", String.class);

    /** The business field */
    public final static ContextParameter<BusinessField> BUSINESS_FIELD =
            new ContextParameter<BusinessField>("field", BusinessField.class);

    /**
     * The business field used like source for the current field initialization
     */
    public final static ContextParameter<BusinessField> SOURCE_BUSINESS_FIELD =
            new ContextParameter<BusinessField>("sourceField",
                    BusinessField.class);

    /* REVISION */
    /** The id of the current revision */
    public final static ContextParameter<String> REVISION_ID =
            new ContextParameter<String>("revisionId", String.class);

    /** The current revision */
    public final static ContextParameter<RevisionData> REVISION_DATA =
            new ContextParameter<RevisionData>("revisionData",
                    RevisionData.class);

    /* STATE */
    /** The name of the current sheet's transition */
    public final static ContextParameter<String> TRANSITION_NAME =
            new ContextParameter<String>("transitionName", String.class);

    /** The name of the current sheet's state */
    public final static ContextParameter<String> STATE_NAME =
            new ContextParameter<String>("stateName", String.class);

    /* LOCK */
    /** The current sheet's lock */
    public final static ContextParameter<Lock> LOCK =
            new ContextParameter<Lock>("lock", Lock.class);

    /* EXPORT */
    /** The current XML document */
    public final static ContextParameter<Document> XML_DOCUMENT =
            new ContextParameter<Document>("xmlDocument", Document.class);

    /** The sheet node of the current XML document */
    public final static ContextParameter<Node> XML_SHEET_NODE =
            new ContextParameter<Node>("xmlSheetNode", Node.class);

    /* REPORTING */
    /** The current report model name */
    public final static ContextParameter<String> REPORT_MODEL_NAME =
            new ContextParameter<String>("reportModelName", String.class);

    /* CHOICE STRING */
    /** The result list of a choice string field */
    @SuppressWarnings("rawtypes")
	public final static ContextParameter<Collection<String>> CHOICES_RESULT =
            (ContextParameter<Collection<String>>) new ContextParameter(
                    "choices", Collection.class);

    /** The default selected value of a choice string field */
    public final static ContextParameter<String> CHOICES_RESULT_DEFAULT_VALUE =
            (ContextParameter<String>) new ContextParameter<String>(
                    "choicesDefaultValue", String.class);

    /* EXTENDED ACTION */
    /** The input data of an extended action */
    @SuppressWarnings("rawtypes")
	public final static ContextParameter<CacheableInputData> INPUT_DATA =
            (ContextParameter<CacheableInputData>) new ContextParameter(
                    "inputData", CacheableInputData.class);

    /** The list of values container identifiers checked on filter results */
    @SuppressWarnings("rawtypes")
	public final static ContextParameter<List<String>> SHEET_IDS =
            (ContextParameter<List<String>>) new ContextParameter(
                    "valuesContainerIds", List.class);

    /** The list of values container identifiers checked on filter results */
    @SuppressWarnings("rawtypes")
	public final static ContextParameter<List<String>> VALUES_CONTAINER_IDS =
            (ContextParameter<List<String>>) new ContextParameter(
                    "valuesContainerIds", List.class);
    
    /** The list of updated cacheable links  */
    @SuppressWarnings("rawtypes")
	public final static ContextParameter<List<CacheableLink>> CURRENT_UPDATED_LINKS =
            (ContextParameter<List<CacheableLink>>) new ContextParameter(
                    "currentEditedLinks", List.class);
    
    /** User login */
    @SuppressWarnings("rawtypes")
	public final static ContextParameter<String> USER_LOGIN =
            (ContextParameter<String>) new ContextParameter(
                    "userLogin", String.class);

    /** The list of previous user roles */
    @SuppressWarnings("rawtypes")
	public final static ContextParameter<List<String>> PREVIOUS_ROLES =
            (ContextParameter<List<String>>) new ContextParameter(
                    "previousRoles", List.class);
    
    /** The list of new roles */
    @SuppressWarnings("rawtypes")
	public final static ContextParameter<List<String>> NEW_ROLES =
            (ContextParameter<List<String>>) new ContextParameter(
                    "newRoles", List.class);
    /* DEPRECATED */
    /**
     * The current sheet : org.topcased.gpm.business.sheet.service.SheetData or
     * org.topcased.gpm.business.sheet.impl.CacheableSheet
     * 
     * @deprecated
     * @since 1.8
     * @see ExtensionPointParameters.SHEET or
     *      ExtensionPointParameters.VALUES_CONTAINER
     */
    public final static ContextParameter<Object> SHEET_DATA =
            new ContextParameter<Object>("sheetData", Object.class);

    /**
     * The current link
     * 
     * @deprecated
     * @since 1.8
     * @see ExtensionPointParameters.LINK or
     *      ExtensionPointParameters.VALUES_CONTAINER
     */
    public final static ContextParameter<LinkData> LINK_DATA =
            new ContextParameter<LinkData>("linkData", LinkData.class);

}