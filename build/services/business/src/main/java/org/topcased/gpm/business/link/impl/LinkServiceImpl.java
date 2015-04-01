/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 ******************************************************************/

package org.topcased.gpm.business.link.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.lang.StringUtils;
//import org.apache.log4j.Logger;
import org.topcased.gpm.business.ServiceImplBase;
import org.topcased.gpm.business.authorization.service.AccessControlContextData;
import org.topcased.gpm.business.authorization.service.TypeAccessControlData;
import org.topcased.gpm.business.cache.AbstractCachedObjectFactory;
import org.topcased.gpm.business.cache.CacheableFactory;
import org.topcased.gpm.business.dynamic.DynamicValuesContainerAccessFactory;
import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.exception.ConstraintException;
import org.topcased.gpm.business.exception.FieldValidationException;
import org.topcased.gpm.business.exception.FilterException;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.exception.InvalidIdentifierException;
import org.topcased.gpm.business.exception.InvalidNameException;
import org.topcased.gpm.business.exception.InvalidTokenException;
import org.topcased.gpm.business.exception.MandatoryValuesException;
import org.topcased.gpm.business.exception.StaleLinkDataException;
import org.topcased.gpm.business.exception.UndeletableElementException;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.extensions.service.ContextBase;
import org.topcased.gpm.business.extensions.service.ContextValueFactory;
import org.topcased.gpm.business.extensions.service.ExtensionPointNames;
import org.topcased.gpm.business.extensions.service.ExtensionPointParameters;
import org.topcased.gpm.business.fields.FieldAccessData;
import org.topcased.gpm.business.fields.FieldData;
import org.topcased.gpm.business.fields.FieldType;
import org.topcased.gpm.business.fields.LineFieldData;
import org.topcased.gpm.business.fields.MultipleLineFieldData;
import org.topcased.gpm.business.fields.impl.CacheableFieldsContainer;
import org.topcased.gpm.business.fields.impl.CacheableValuesContainer;
import org.topcased.gpm.business.fieldscontainer.service.FieldsContainerService;
import org.topcased.gpm.business.link.service.LinkData;
import org.topcased.gpm.business.link.service.LinkService;
import org.topcased.gpm.business.link.service.LinkTypeData;
import org.topcased.gpm.business.product.impl.CacheableProduct;
import org.topcased.gpm.business.scalar.StringValueData;
import org.topcased.gpm.business.search.criterias.CriteriaData;
import org.topcased.gpm.business.search.criterias.CriteriaFieldData;
import org.topcased.gpm.business.search.criterias.OperationData;
import org.topcased.gpm.business.search.criterias.impl.Operators;
import org.topcased.gpm.business.search.impl.fields.UsableFieldCacheKey;
import org.topcased.gpm.business.search.impl.fields.UsableFieldsManager;
import org.topcased.gpm.business.search.impl.query.FilterQueryConfigurator;
import org.topcased.gpm.business.search.impl.query.FilterResultIdIterator;
import org.topcased.gpm.business.search.service.ExecutableFilterData;
import org.topcased.gpm.business.search.service.FilterVisibilityConstraintData;
import org.topcased.gpm.business.serialization.data.FieldValueData;
import org.topcased.gpm.business.serialization.data.PointerFieldValueData;
import org.topcased.gpm.business.sheet.impl.CacheableSheet;
import org.topcased.gpm.business.sheet.impl.CacheableSheetLinksByType;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.business.values.field.virtual.VirtualFieldType;
import org.topcased.gpm.domain.businessProcess.BusinessProcess;
import org.topcased.gpm.domain.dictionary.Environment;
import org.topcased.gpm.domain.extensions.ExtensionPoint;
import org.topcased.gpm.domain.extensions.ExtensionsContainerDao;
import org.topcased.gpm.domain.fields.Field;
import org.topcased.gpm.domain.fields.FieldsContainer;
import org.topcased.gpm.domain.fields.PointerFieldAttributesDao;
import org.topcased.gpm.domain.fields.ValuesContainer;
import org.topcased.gpm.domain.link.Link;
import org.topcased.gpm.domain.link.LinkDao;
import org.topcased.gpm.domain.link.LinkNavigation;
import org.topcased.gpm.domain.link.LinkType;
import org.topcased.gpm.domain.product.Product;
import org.topcased.gpm.domain.sheet.Sheet;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * The Class LinkServiceImpl.
 * 
 * @author llatil
 */
public class LinkServiceImpl extends ServiceImplBase implements LinkService {

    /** The link factory. */
    private final CacheableFactory linkFactory = new CacheableLinkFactory();

    /** The link type factory. */
    private final CacheableFactory linkTypeFactory =
            new CacheableLinkTypeFactory();

    /**
     * Constructs a new link service impl.
     */
    public LinkServiceImpl() {
        registerFactories(linkFactory, linkTypeFactory);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.link.service.LinkService#getLinkedElementsIds(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public List<String> getLinkedElementsIds(final String pValuesContainerId,
            final String pLinkTypeName) {
        return getValuesContainerDao().getLinkedElements(pValuesContainerId,
                pLinkTypeName);
    }

    /**
     * Get the list of identifiers of all links in a given values container.
     * 
     * @param pContainerId
     *            Identifier of the container
     * @return Set of links identifiers
     */
    // TODO declare in superclass
    private Set<String> getLinkIds(final String pContainerId) {
        final Set<String> lResult = new HashSet<String>();

        lResult.addAll(getLinkDao().getLinks(pContainerId));
        return lResult;
    }

    /**
     * Creates a sheetLinkData.
     * 
     * @param pRoleToken
     *            The role token.
     * @param pLinkId
     *            The sheet link identifier.
     * @return The data.
     */
    // TODO declare in superclass
    @Deprecated
    public LinkData createLinkData(final String pRoleToken, final String pLinkId) {
        final CacheableLink lLink =
                getCacheableLink(pLinkId, CacheProperties.IMMUTABLE);

        return dataTransformationServiceImpl.getLinkDataFromCacheableLink(
                pRoleToken, lLink);
    }

    /**
     * Create a LinkData when the current container is containerId. This method
     * is useful to update access controls on link fields with a condition on
     * container type origin or container type destination
     * 
     * @param pRoleToken
     *            The role token
     * @param pLinkId
     *            The sheet link identifier
     * @param pContainerId
     *            The container identifier
     * @return The link data
     */
    // TODO declare in superclass
    public LinkData createLinkDataFromContainer(final String pRoleToken,
            final String pLinkId, final String pContainerId) {

        // Create the link data
        // (in this method, access controls on link fields set on sheet link
        // will be set)
        final LinkData lLinkData = createLinkData(pRoleToken, pLinkId);

        // Get the sheet type associated to the sheet
        final String lLinkTypeId =
                getCacheableLink(pLinkId, CacheProperties.IMMUTABLE).getTypeId();

        // Get the cacheable value container corresponding to the containerId.
        final CacheableValuesContainer lCacheableContainer =
                getCachedValuesContainer(pContainerId,
                        CacheProperties.IMMUTABLE.getCacheFlags());

        // Through link data field
        for (MultipleLineFieldData lMultipleLineFieldData : lLinkData.getMultipleLineFieldDatas()) {
            final AccessControlContextData lAccessControlContextData =
                    getAuthService().getAccessControlContextData(pRoleToken,
                            CacheProperties.DEFAULT_ACCESS_CONTROL_USED,
                            lLinkTypeId, lCacheableContainer.getTypeId(),
                            lLinkData.getId());

            // Get the access control on the multiple link field set on the
            // sheetTypeId.
            FieldAccessData lFieldAccessControlData =
                    getAuthService().getFieldAccess(pRoleToken,
                            lAccessControlContextData,
                            lMultipleLineFieldData.getFieldId());

            // Update the field with found access controls.
            lMultipleLineFieldData.setExportable(lFieldAccessControlData.getExportable());
            lMultipleLineFieldData.setConfidential(lFieldAccessControlData.getConfidential());
            for (LineFieldData lLineFieldData : lMultipleLineFieldData.getLineFieldDatas()) {
                for (FieldData lFieldData : lLineFieldData.getFieldDatas()) {
                    // Get the access control on the field set on the
                    // sheetTypeId.
                    lFieldAccessControlData =
                            getAuthService().getFieldAccess(pRoleToken,
                                    lAccessControlContextData,
                                    lFieldData.getFieldId());
                    // Update the field with found access controls
                    lFieldData.setExportable(lFieldAccessControlData.getExportable());
                    lFieldData.setConfidential(lFieldAccessControlData.getConfidential());
                    lFieldData.setMandatory(lFieldAccessControlData.getMandatory());
                    lFieldData.setUpdatable(lFieldAccessControlData.getUpdatable());
                }
            }
        }
        return lLinkData;
    }

    /**
     * Create a list of sheet link data from a list of Sheet Links.
     * 
     * @param pRoleToken
     *            The role token.
     * @param pLinks
     *            List of Sheet Links
     * @return Newly created list of SheetLinkData
     */
    // TODO declare in superclass
    @Deprecated
    public List<LinkData> createLinkDataList(final String pRoleToken,
            final Collection<String> pLinks) {
        final List<LinkData> lLinkDataList = new ArrayList<LinkData>(pLinks.size());

        for (String lLinkId : pLinks) {
            lLinkDataList.add(createLinkData(pRoleToken, lLinkId));
        }
        return lLinkDataList;
    }

    /**
     * Create a list of sheet link data from a list of Sheet Links.
     * 
     * @param pRoleToken
     *            The role token.
     * @param pLinks
     *            List of Sheet Links
     * @return Newly created list of SheetLinkData
     */
    // TODO declare in superclass
    public List<CacheableLink> createLinkList(final String pRoleToken,
            final List<String> pLinks, final CacheProperties pCacheProperties) {
        List<CacheableLink> lLinkList =
                new ArrayList<CacheableLink>(pLinks.size());

        for (String lLinkId : pLinks) {
            lLinkList.add(getCacheableLink(lLinkId, pCacheProperties));
        }
        return lLinkList;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.link.service.LinkService#createSheetLink(java.lang.String,
     *      java.lang.String, java.lang.String, java.lang.String,
     *      org.topcased.gpm.business.extensions.service.Context)
     */
    @Override
    @Deprecated
    public LinkData createSheetLink(final String pRoleToken,
            final String pLinkTypeName, final String pSourceId,
            final String pDestId, final Context pContext)
        throws AuthorizationException {

        if (!authorizationService.isValidRoleToken(pRoleToken)) {
            throw new InvalidTokenException(pRoleToken,
                    "Invalid role token ''{0}''");
        }

        // Get the source & destination sheets
        final CacheableSheet lSourceSheet =
                getSheetService().getCacheableSheet(pSourceId,
                        CacheProperties.IMMUTABLE);
        final CacheableSheet lDestSheet =
                getSheetService().getCacheableSheet(pDestId,
                        CacheProperties.IMMUTABLE);

        assertLinkCreation(pRoleToken, lSourceSheet, lDestSheet);
        String lProcessName =
                getAuthService().getProcessNameFromToken(pRoleToken);

        return createLink(pRoleToken, lSourceSheet, lDestSheet, lProcessName,
                pLinkTypeName, pContext);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.link.service.LinkService#createCacheableSheetLink(java.lang.String,
     *      java.lang.String, java.lang.String, java.lang.String,
     *      org.topcased.gpm.business.extensions.service.Context)
     */
    @Override
    public CacheableLink createCacheableSheetLink(final String pRoleToken,
            final String pLinkTypeName, final String pSourceId,
            final String pDestId, final Context pContext)
        throws AuthorizationException {

        // gets the sheetType access control for this sheet
        final CacheableSheet lSourceSheet =
                getSheetService().getCacheableSheet(pSourceId,
                        CacheProperties.IMMUTABLE);
        final CacheableSheet lDestSheet =
                getSheetService().getCacheableSheet(pDestId,
                        CacheProperties.IMMUTABLE);

        assertLinkCreation(pRoleToken, lSourceSheet, lDestSheet);

        CacheableLinkType lLinkType =
                getCacheableLinkTypeByName(pRoleToken, pLinkTypeName);
        CacheableLink lLink =
                new CacheableLink(lLinkType, lSourceSheet.getId(),
                        lSourceSheet.getFunctionalReference(),
                        lDestSheet.getId(), lDestSheet.getFunctionalReference());
        return createLink(pRoleToken, lLink, true, pContext);
    }

    /**
     * Assert that a link can be created between those two sheets
     * 
     * @param pRoleToken
     *            Role session
     * @param pSourceSheet
     *            Source sheet
     * @param pDestSheet
     *            Destination sheet
     * @throws AuthorizationException
     *             if the link creation is not authorized
     */
    private void assertLinkCreation(final String pRoleToken,
            final CacheableSheet pSourceSheet, final CacheableSheet pDestSheet) {
        final AccessControlContextData lAccessControlContextData =
                new AccessControlContextData();

        lAccessControlContextData.setRoleName(getAuthService().getRoleNameFromToken(
                pRoleToken));
        lAccessControlContextData.setProductName(pSourceSheet.getProductName());
        lAccessControlContextData.setStateName(pSourceSheet.getCurrentStateName());
        lAccessControlContextData.setContainerTypeId(pSourceSheet.getTypeId());

        final TypeAccessControlData lSourceSheetTypeAccess =
                getAuthService().getSheetAccessControl(pRoleToken,
                        lAccessControlContextData, pSourceSheet.getId());
        // if the user hasn't the right to get this sheet
        if (lSourceSheetTypeAccess.getConfidential()) {
            throw new AuthorizationException("Illegal access to the sheet "
                    + pSourceSheet.getId() + " : the access is confidential.");
        }

        // Check if the source sheet is writable.
        getFieldsContainerServiceImpl().assertValuesContainerIsWritable(
                pRoleToken, pSourceSheet.getId());

        lAccessControlContextData.setProductName(pDestSheet.getProductName());
        lAccessControlContextData.setStateName(pDestSheet.getCurrentStateName());
        lAccessControlContextData.setContainerTypeId(pDestSheet.getTypeId());

        // gets the sheetType access control for the sheet to link
        final TypeAccessControlData lDestSheetTypeAccess =
                getAuthService().getSheetAccessControl(pRoleToken,
                        lAccessControlContextData, pDestSheet.getId());
        // if the user hasn't the right to get this sheet
        if (lDestSheetTypeAccess.getConfidential()) {
            throw new AuthorizationException("Illegal access to the sheet "
                    + pDestSheet.getId() + " : the access is confidential.");
        }

        // Check if the dest sheet is writable.
        getFieldsContainerServiceImpl().assertValuesContainerIsWritable(
                pRoleToken, pDestSheet.getId());
    }

    /**
     * Creates the link.
     * 
     * @param pRoleToken
     *            the role token
     * @param pOriginElement
     *            the origin element
     * @param pDestElement
     *            the dest element
     * @param pBusinessProcessName
     *            the business process name
     * @param pLinkTypeName
     *            the link type name
     * @param pContext
     *            Execution context.
     * @return the link data
     * @throws InvalidIdentifierException
     *             If the origin (or destination) doesn't correspond to the
     *             definition of the link type.
     */
    private LinkData createLink(final String pRoleToken,
            final CacheableValuesContainer pOriginElement,
            final CacheableValuesContainer pDestElement, final String pBusinessProcessName,
            final String pLinkTypeName, final Context pContext) {

        CacheableLinkType lLinkType =
                getCacheableLinkTypeByName(pRoleToken, pLinkTypeName);
        CacheableLink lLink =
                new CacheableLink(lLinkType, pOriginElement.getId(),
                        pOriginElement.getFunctionalReference(),
                        pDestElement.getId(),
                        pDestElement.getFunctionalReference());
        lLink = createLink(pRoleToken, lLink, true, pContext);

        return dataTransformationServiceImpl.getLinkDataFromCacheableLink(
                pRoleToken, lLink);
    }

    private String doCreateLink(String pRoleToken,
            CacheableLink pCacheableLink, CacheableLinkType pLinkType,
            boolean pCreateAutoPointers, Context pContext) {
        final Link lLink;

        if (pCacheableLink.getId() != null
                && linkDao.exist(pCacheableLink.getId())) {
            lLink = linkDao.load(pCacheableLink.getId());
        }
        else {
            // Create the sheet entity
            lLink = linkDao.getNewLink(pLinkType.getId());
            if (pCacheableLink.getId() != null) {
                lLink.setId(pCacheableLink.getId());
            }
        }

        ValuesContainer lOriginElement =
                getValuesContainer(pCacheableLink.getOriginId());
        lLink.setOrigin(lOriginElement);
        ValuesContainer lDestElement =
                getValuesContainer(pCacheableLink.getDestinationId());
        lLink.setDestination(lDestElement);

        lLink.setDefinition(getLinkType(pLinkType.getId()));

        for (Environment lEnv : lOriginElement.getEnvironments()) {
            lLink.addToEnvironmentList(lEnv);
        }

        // Persist this link in DB.
        getLinkDao().create(lLink);

        // Check mandatory fields
        fieldsManager.checkMandoryFields(pRoleToken, pLinkType, pCacheableLink);

        // Create the link field values
        DynamicValuesContainerAccessFactory.getInstance().getAccessor(
                pLinkType.getId()).updateDomainFromBusiness(lLink,
                pCacheableLink, pContext);

        // Create the attributes of the link.
        getAttributesService().set(lLink.getId(),
                pCacheableLink.getAllAttributes());

        // Create the version of the link
        lLink.setVersion(pCacheableLink.getVersion());

        // If automatic pointer fields must be filled with this link type,
        // create them.
        if (pCreateAutoPointers) {
            createAutomaticPointerFieldValues(pRoleToken,
                    pLinkType.getBusinessProcessName(), lLink, pContext);
        }

        // Invalidate the CacheableSheetLinks of the linked sheets
        removeElementFromCache(CacheableSheetLinksByType.getCachedElementId(
                pCacheableLink.getOriginId(), pLinkType.getId()));
        removeElementFromCache(CacheableSheetLinksByType.getCachedElementId(
                pCacheableLink.getDestinationId(), pLinkType.getId()));

        pCacheableLink.setId(lLink.getId());

        return lLink.getId();
    }

    /**
     * Create automatic pointer field values (during link creation)
     * 
     * @param pRoleToken
     *            current session token
     * @param pProcessName
     *            current business process name
     * @param pLink
     *            Link newly created.
     * @param pContext
     *            Execution context.
     */
    private void createAutomaticPointerFieldValues(String pRoleToken,
            String pProcessName, Link pLink, Context pContext) {

        // Get link attributes : origin type and destination type
        String lOriginTypeId = pLink.getOrigin().getDefinition().getId();
        String lDestTypeId = pLink.getDestination().getDefinition().getId();

        // Create automatic pointer field values for link Origin -> Destination
        createAutomaticPointerFieldValues(pRoleToken, pProcessName, pContext,
                pLink.getDefinition().getName(), getCachedFieldsContainer(
                        lOriginTypeId, CACHE_IMMUTABLE_OBJECT),
                pLink.getOrigin().getId(), pLink.getDestination().getId());
        // Create automatic pointer field values for link Destination -> Origin
        createAutomaticPointerFieldValues(pRoleToken, pProcessName, pContext,
                pLink.getDefinition().getName(), getCachedFieldsContainer(
                        lDestTypeId, CACHE_IMMUTABLE_OBJECT),
                pLink.getDestination().getId(), pLink.getOrigin().getId());
    }

    private void createAutomaticPointerFieldValues(String pRoleToken,
            String pProcessName, Context pContext, String pLinkTypeName,
            CacheableFieldsContainer pPointerSheetType, String pPointerSheetId,
            String pPointedSheetId) {

        // Get all pointer fields that reference this link type
        // inside pointer field attributes for this pointer sheet type.
        List<Field> lPointerFields =
                pointerFieldAttributesDao.getPointerFields(pProcessName,
                        pLinkTypeName, pPointerSheetType.getId());

        if (lPointerFields != null && !lPointerFields.isEmpty()) {

            // Get pointer sheet.
            CacheableSheet lPointerSheet =
                    (CacheableSheet) getCachedValuesContainer(pPointerSheetId,
                            CACHE_MUTABLE_OBJECT);

            // Get pointer sheet, sheet type
            CacheableValuesContainer lPointedSheet =
                    getCachedValuesContainer(pPointedSheetId,
                            CACHE_IMMUTABLE_OBJECT);
            CacheableFieldsContainer lPointedSheetType =
                    getCachedFieldsContainer(lPointedSheet.getTypeId(),
                            CACHE_IMMUTABLE_OBJECT);

            // Initialize Map for sub fields values :
            // Map(<<NAME of PARENT FIELD>> -> MAP (<<NAME OF SUB-FIELD>>->
            // <<VALUE>>))
            Map<String, Map<String, Map<String, FieldValueData>>> lSubFieldsValues =
                    new LinkedHashMap<String, Map<String, Map<String, FieldValueData>>>();

            // Create once loop variables;
            String lPointedFieldLabel;
            org.topcased.gpm.business.serialization.data.Field lSerializationPointerField;
            org.topcased.gpm.business.serialization.data.Field lPointedField;
            PointerFieldValueData lPointerFieldValueData;

            // For each pointer field, add automatically a new pointer field
            // value
            for (Field lPointerField : lPointerFields) {

                lPointedFieldLabel =
                        lPointerField.getPointerFieldAttributes().getReferencedFieldLabel();
                lSerializationPointerField =
                        pPointerSheetType.getFieldFromLabel(lPointerField.getLabelKey());

                // Check that pointer field is a pointer
                if (!lPointerField.isPointerField()) {
                    throw new GDMException(
                            "A pointed field value cannot be added "
                                    + "to a field that is not a pointer field.");
                }
                // Get pointed field
                lPointedField =
                        lPointedSheetType.getFieldFromLabel(lPointedFieldLabel);

                // Check that pointed fiel exists
                if (lPointedField == null) {
                    throw new GDMException("Invalid pointed field : '"
                            + lPointedFieldLabel + "'.");
                }
                // check that pointed field is not a pointer
                else if (lPointedField.isPointerField()) {
                    throw new GDMException("The pointed field : '"
                            + lPointedFieldLabel + "' cannot be a pointer.");
                }
                // Check that pointer and pointed fields have same data type
                if (FieldType.valueOf(lSerializationPointerField) != FieldType.valueOf(lPointedField)) {
                    throw new GDMException(
                            "Pointer field and pointed field must have same data type ( pointer: '"
                                    + FieldType.valueOf(lSerializationPointerField)
                                    + "' vs pointed: '"
                                    + FieldType.valueOf(lPointedField) + "').");
                }

                // Create pointer field value
                lPointerFieldValueData =
                        new PointerFieldValueData(lPointerField.getLabelKey());
                lPointerFieldValueData.setReferencedContainerId(pPointedSheetId);
                lPointerFieldValueData.setReferencedFieldLabel(lPointedFieldLabel);

                // If pointer field is not inside the multiple field,
                // add new value directly to the pointer sheet values
                if (lSerializationPointerField.getMultipleField() == null) {
                    // Either adding or replacement of the value
                    // (either for mono or multi valued simple field)
                    if (lPointerField.isMultiValued()) {
                        lPointerSheet.addValue(lPointerFieldValueData);
                    }
                    else {
                        lPointerSheet.setValue(lPointerFieldValueData);
                    }
                }
                else {
                    // Field inside a multiple field
                    // 1. Get parent field
                    org.topcased.gpm.business.serialization.data.Field lParentField =
                            pPointerSheetType.getFieldFromId(lSerializationPointerField.getMultipleField());

//                    if (LOGGER.isDebugEnabled()) {
//                        LOGGER.debug("Pointer field '"
//                                + lPointerField.getLabelKey()
//                                + "' is a subfield of multiple field '"
//                                + lParentField.getLabelKey() + "'.");
//                    }

                    // 2.Add pointer field value into a map of all subFields
                    // values
                    // (used to compute a "cross product" of all sub- and
                    // pointer field values).
                    putPointerFieldValueInMap(lSubFieldsValues,
                            lPointerSheet.getId(), lParentField.getLabelKey(),
                            lPointerFieldValueData);

                    // 3. All values from this map are copied to the pointer
                    // sheet afterwards.

                }
            }
            // Before updating sheets, add values stored in sub field values map
            Map<String, Map<String, FieldValueData>> lParentFieldMap =
                    lSubFieldsValues.get(lPointerSheet.getId());
            if (lParentFieldMap != null) {
                for (Entry<String, Map<String, FieldValueData>> lEntry : lParentFieldMap.entrySet()) {
                    String lParentFieldLabel = lEntry.getKey();
                    // If parent field is not multivalued, remove existing value
                    // before adding new one.
                    if (!pPointerSheetType.getFieldFromLabel(lParentFieldLabel).isMultivalued()) {
                        lPointerSheet.removeFieldValue(lParentFieldLabel);
                    }
                    lPointerSheet.addValue(lParentFieldLabel,
                            lEntry.getValue().values());
                }
            }

            // Update origin and destination sheets skipping extension points
            Context lCtxWithoutExtPt = Context.createContext(pContext);
            lCtxWithoutExtPt.put(Context.GPM_SKIP_EXT_PTS, Boolean.TRUE);
            getSheetService().updateSheet(pRoleToken, lPointerSheet,
                    lCtxWithoutExtPt);
        }
    }

    /**
     * Add a pointer field value in a map containing "cross product" of pointer
     * field values inside multiple fields for a list of containers The map is
     * updated by this method.
     * 
     * @param pMap
     *            map containing "cross product" of pointer field values inside
     *            multiple fields for a list of containers
     * @param pContainerId
     *            Id of container with a pointer field value to add
     * @param pParentFieldLabel
     *            Label of the parent field with a pointer sub field
     * @param pPointerFieldValue
     *            pointer field value to add
     */
    private void putPointerFieldValueInMap(
            Map<String, Map<String, Map<String, FieldValueData>>> pMap,
            String pContainerId, String pParentFieldLabel,
            PointerFieldValueData pPointerFieldValue) {

        Map<String, Map<String, FieldValueData>> lContainerMap =
                pMap.get(pContainerId);
        if (lContainerMap != null) {
            Map<String, FieldValueData> lParentFieldMap =
                    lContainerMap.get(pParentFieldLabel);
            if (lParentFieldMap != null) {
                lParentFieldMap.put(pPointerFieldValue.getName(),
                        pPointerFieldValue);
            }
            else {
                // Add complete map hierarchy from parent field map
                lParentFieldMap = new HashMap<String, FieldValueData>();
                lParentFieldMap.put(pPointerFieldValue.getName(),
                        pPointerFieldValue);
                lContainerMap.put(pParentFieldLabel, lParentFieldMap);
            }

        }
        else {
            // Add complete map hierarchy from container map
            lContainerMap = new HashMap<String, Map<String, FieldValueData>>();
            Map<String, FieldValueData> lParentFieldMap =
                    new HashMap<String, FieldValueData>();
            lParentFieldMap.put(pPointerFieldValue.getName(),
                    pPointerFieldValue);
            lContainerMap.put(pParentFieldLabel, lParentFieldMap);
            pMap.put(pContainerId, lContainerMap);
        }
    }

    /**
     * Delete automatic pointer field values during link deletion
     * 
     * @param pRoleToken
     *            current session token
     * @param pProcessName
     *            current business process name
     * @param pLink
     *            Link to be deleted.
     * @param pContext
     *            Execution context.
     */
    private void deleteAutomaticPointerFieldValues(String pRoleToken,
            String pProcessName, CacheableLink pLink, Context pContext) {

        CacheableValuesContainer lOrigin =
                getCachedValuesContainer(pLink.getOriginId(),
                        CacheProperties.MUTABLE.getCacheFlags());

        CacheableValuesContainer lDest =
                getCachedValuesContainer(pLink.getDestinationId(),
                        CacheProperties.MUTABLE.getCacheFlags());

        // Create automatic pointer field values for link Origin -> Destination
        deleteAutomaticPointerFieldValues(pRoleToken, pProcessName, pContext,
                pLink.getTypeName(), lOrigin, lDest);
        // Create automatic pointer field values for link Destination -> Origin
        deleteAutomaticPointerFieldValues(pRoleToken, pProcessName, pContext,
                pLink.getTypeName(), lDest, lOrigin);
    }

    @SuppressWarnings("unchecked")
    private void deleteAutomaticPointerFieldValues(String pRoleToken,
            String pProcessName, Context pContext, String pLinkTypeName,
            CacheableValuesContainer pPointerContainer,
            CacheableValuesContainer pPointedContainer) {
        // Get all pointer fields that reference this link type inside pointer
        // field attributes
        List<Field> lPointerFields =
                pointerFieldAttributesDao.getPointerFields(pProcessName,
                        pLinkTypeName, pPointerContainer.getTypeId());

        if (lPointerFields != null && !lPointerFields.isEmpty()) {

            // Create once loop variables;
            org.topcased.gpm.business.serialization.data.Field lSerializationPointerField;

            // Get origin and destination, and origin and destination types.
            CacheableFieldsContainer lPointerSheetType =
                    getSheetService().getCacheableSheetType(
                            pPointerContainer.getTypeId(),
                            CacheProperties.IMMUTABLE);

            // For each pointer field, delete automatically previous pointer
            // field value
            for (Field lPointerField : lPointerFields) {
                // Find which link extremity is pointer and pointed
                lSerializationPointerField =
                        lPointerSheetType.getFieldFromLabel(lPointerField.getLabelKey());

                // Create an empty value (all existing values must be replaced
                // by this one.
                PointerFieldValueData lEmptyValue =
                        new PointerFieldValueData(lPointerField.getLabelKey());

                // Current value for pointer field
                Object lCurrentValue = null;

                // Separate case of pointer field inside multiple fields.

                String lParentFieldLabelKey = null; // (not null if a parent
                // exists)
                if (lSerializationPointerField.getMultipleField() != null) {
                    org.topcased.gpm.business.serialization.data.Field lParentField =
                            lPointerSheetType.getFieldFromId(lSerializationPointerField.getMultipleField());
                    lParentFieldLabelKey = lParentField.getLabelKey();

                    if (!lParentField.isMultivalued()) {
                        lCurrentValue =
                                pPointerContainer.getValue(
                                        lParentFieldLabelKey,
                                        lPointerField.getLabelKey());
                    }
                    else {
                        // parent field is multivalued :
                        // 1. get value
                        Object lParentValue =
                                pPointerContainer.getValue(lParentFieldLabelKey);

                        // 2a. when parent field contains multiple lines
                        if (lParentValue instanceof List<?>) {

                            // 3. Check each line, get the subfield value for
                            // each line,
                            // and remove line in which pointed value points on
                            // pointed sheet
                            int lLineIndexToRemove = -1;
                            int lLineIndex = 0;
                            for (Map<String, FieldValueData> lLine : (List<Map<String, FieldValueData>>) lParentValue) {
                                PointerFieldValueData lPointedValue =
                                        (PointerFieldValueData) lLine.get(lPointerField.getLabelKey());
                                if (lPointedValue != null
                                        && pPointedContainer.getId().equals(
                                                lPointedValue.getReferencedContainerId())) {
                                    lLineIndexToRemove = lLineIndex;
                                    break; // Current line index is in
                                    // lLineIndexToRemove
                                }
                                lLineIndex++;

                            }
                            // 4. Remove the line corresponding to current link
                            if (lLineIndexToRemove != -1) {
                                ((List<?>) lParentValue).remove(lLineIndexToRemove);
                            }
                        }
                        // 2b. when parent field contains only one line :
                        // get the only available value
                        else if (lParentValue instanceof Map<?, ?>) {
                            lCurrentValue =
                                    pPointerContainer.getValue(
                                            lParentFieldLabelKey,
                                            lPointerField.getLabelKey());
                        }
                        // ELSE : DO NOTHING : Keep null current value
                    }
                }
                else {
                    lCurrentValue =
                            pPointerContainer.getValue(lPointerField.getLabelKey());
                }

                /*
                 * When current value is found, it can be - null, - a pointer
                 * field value - a list of pointer field values
                 * 
                 * Current value must be removed.
                 * 
                 * Note : In the case of a pointer field inside a multivalued
                 * multiple field, the removal is already done after steps 1.,
                 * 2a., 3., 4.
                 */

                if (lCurrentValue instanceof PointerFieldValueData) {
                    // Only one value is present:
                    // check that it correspond to the other part of the link
                    if (pPointedContainer.getId().equals(
                            ((PointerFieldValueData) lCurrentValue).getReferencedContainerId())) {
                        if (lParentFieldLabelKey != null) {
                            // Pointer field is inside a multiple field
                            pPointerContainer.setValue(lParentFieldLabelKey,
                                    lEmptyValue);
                        }
                        else { // pointer field is not inside a multiple field
                            pPointerContainer.setValue(lEmptyValue);
                        }
                    }
                }
                else if (lCurrentValue instanceof List<?>) {
                    // Multi valued pointer field : check each value
                    // and delete all those referencing this link.
                    List<FieldValueData> lNewValues =
                            new ArrayList<FieldValueData>();
                    for (PointerFieldValueData lPointerFieldValue : (List<PointerFieldValueData>) lCurrentValue) {
                        if (!pPointedContainer.getId().equals(
                                lPointerFieldValue.getReferencedContainerId())) {
                            lNewValues.add(lPointerFieldValue);
                        }
                    }
                    if (lParentFieldLabelKey != null) { // Pointer field is
                        // inside a multiple
                        // field
                        pPointerContainer.removeSubFieldValue(
                                lParentFieldLabelKey,
                                lPointerField.getLabelKey());
                        pPointerContainer.addValue(lParentFieldLabelKey,
                                lNewValues);
                    }
                    else {
                        // Pointer field is not inside a multiple field
                        // replace old values by new one
                        pPointerContainer.getValuesMap().put(
                                lPointerField.getLabelKey(), lNewValues);
                    }
                }
                // ELSE DO NOTHING (nothing to remove because nothing is present
                // or already removed)
            }
            // Update pointer sheet skipping extention points
            Context lCtxWithoutExtPt = Context.createContext(pContext);
            lCtxWithoutExtPt.put(Context.GPM_SKIP_EXT_PTS, Boolean.TRUE);

            if (pPointerContainer instanceof CacheableSheet) {
                getSheetService().updateSheet(pRoleToken,
                        (CacheableSheet) pPointerContainer, lCtxWithoutExtPt);
            }
            else if (pPointerContainer instanceof CacheableProduct) {
                getProductService().updateProduct(pRoleToken,
                        (CacheableProduct) pPointerContainer, lCtxWithoutExtPt);
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.link.service.LinkService#deleteLink(java.lang.String,
     *      java.lang.String,
     *      org.topcased.gpm.business.extensions.service.Context)
     */
    @Override
    public void deleteLink(final String pRoleToken, final String pLinkId,
            final Context pContext) throws AuthorizationException {
        final CacheableLink lLink =
                getCacheableLink(pLinkId, CacheProperties.IMMUTABLE);
        final CacheableLinkType lLinkType =
                getCacheableLinkType(lLink.getTypeId(),
                        CacheProperties.IMMUTABLE);

        // Get the type access control for this sheet
        // (The role token is checked in getTypeAccessControl)
        TypeAccessControlData lTypeAccessControl =
                getAuthService().getTypeAccessControl(
                        pRoleToken,
                        new AccessControlContextData(
                                CacheProperties.DEFAULT_ACCESS_CONTROL_USED,
                                CacheProperties.DEFAULT_ACCESS_CONTROL_USED,
                                CacheProperties.DEFAULT_ACCESS_CONTROL_USED,
                                lLinkType.getOriginTypeId(),
                                CacheProperties.DEFAULT_ACCESS_CONTROL_USED,
                                lLink.getOriginId()));
        if (lTypeAccessControl != null) {
            // if the user hasn't the right to get this sheet
            if (lTypeAccessControl.getConfidential()) {
                throw new AuthorizationException("Illegal access to the sheet "
                        + lLink.getOriginRef()
                        + " : the access is confidential.");
            }
        }

        // Check that the link type is deletable and not confidential
        getAuthService().assertDeletable(pRoleToken, lLinkType,
                lLink.getProductName(), null);

        Link lLinkEntity = getLink(pLinkId);

        // Extension point preDeleteSheetLink or preDelete
        final ExtensionPoint lPreDelete =
                getExecutableExtensionPoint(lLinkType.getId(),
                        ExtensionPointNames.PRE_DELETE, pContext);

        if (lPreDelete != null) {
            final Context lCtx = new ContextBase(pContext);

            lCtx.put(ExtensionPointParameters.LINK_ID, pLinkId);

            getExtensionsService().execute(pRoleToken, lPreDelete, lCtx);
        }

        // Extension point postDeleteSheetLink or postDelete
        final ExtensionPoint lPostDelete =
                getExecutableExtensionPoint(lLinkType.getId(),
                        ExtensionPointNames.POST_DELETE, pContext);
        LinkData lLinkData = null;
        CacheableLink lCacheableLink = null;

        if (lPostDelete != null) {
            // If a post extension point is present, the link data structure
            // must be created.
            // (as we cannot create it *after* link deletion)
            lLinkData = getLinkFromKey(pRoleToken, pLinkId);
            lCacheableLink =
                    getCacheableLink(pLinkId, CacheProperties.IMMUTABLE);
        }

        // Remove all OverriddenRoles of the link
        authorizationService.deleteOverriddenRolesFromContainerId(pLinkId);

        // Remove link from link list in origin and destination
        lLinkEntity.getOrigin().removeFromLinkFromOriginList(lLinkEntity);
        lLinkEntity.getDestination().removeFromLinkFromDestinationList(
                lLinkEntity);

        deleteAutomaticPointerFieldValues(pRoleToken,
                lLinkType.getBusinessProcessName(), lLink, pContext);
        getLinkDao().remove(pLinkId);
        removeElementFromCache(pLinkId);
        // Invalid the CacheableSheetLinks of the linked sheets
        removeElementFromCache(CacheableSheetLinksByType.getCachedElementId(
                lLinkEntity.getOrigin().getId(), lLinkType.getId()));
        removeElementFromCache(CacheableSheetLinksByType.getCachedElementId(
                lLinkEntity.getDestination().getId(), lLinkType.getId()));

        if (lPostDelete != null) {
            final Context lCtx = new ContextBase(pContext);

            lCtx.put(ExtensionPointParameters.LINK_ID, pLinkId);

            lCtx.put(ExtensionPointParameters.LINK, lCacheableLink);

            lCtx.put(ExtensionPointParameters.LINK_DATA, lLinkData);

            getExtensionsService().execute(pRoleToken, lPostDelete, lCtx);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.link.service.LinkService#updateLink(String,
     *      CacheableLink, Context)
     */
    @Override
    public void updateLink(final String pRoleToken,
            CacheableLink pCacheableLink, Context pContext)
        throws AuthorizationException {
        updateLink(pRoleToken, pCacheableLink, pContext, true);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.link.service.LinkService#updateLink(String,
     *      CacheableLink, Context, boolean)
     */
    @Override
    public void updateLink(final String pRoleToken,
            CacheableLink pCacheableLink, Context pContext,
            boolean pIgnoreVersion) throws AuthorizationException {

        Link lLink = getLink(pCacheableLink.getId());
        CacheableLinkType lLinkType =
                getCacheableLinkType(pCacheableLink.getTypeId(),
                        CacheProperties.IMMUTABLE);

        // Check that the link type is not confidential before updating it
        FieldsContainer lFieldsContainer = lLink.getDefinition();
        String lProcessName = lFieldsContainer.getBusinessProcess().getName();
        ValuesContainer lOrigin = lLink.getOrigin();

        // Check the version of the link between database and cache before updating it
        if (!pIgnoreVersion) {
            if (pCacheableLink.getVersion() != lLink.getVersion()) {
                throw new StaleLinkDataException(lLink.getVersion(),
                        pCacheableLink.getVersion());
            }
        }

        // The origin is a sheet
        if (lOrigin instanceof Sheet) {
            getAuthService().checkNotConfidentialAccessControl(pRoleToken,
                    lFieldsContainer.getId(), lFieldsContainer.getName(),
                    lProcessName, lOrigin.getProduct().getName(), null);
        }
        // The origin is a product
        else if (lOrigin instanceof Product) {
            getAuthService().checkNotConfidentialAccessControl(pRoleToken,
                    lFieldsContainer.getId(), lFieldsContainer.getName(),
                    lProcessName, ((Product) lOrigin).getName(), null);
        }
        // Invalid origin type
        else {
            throw new GDMException(lOrigin.getClass()
                    + " is not valid a type for a link's origin element");
        }

        // Extension point preUpdateSheetLink or preUpdate
        final ExtensionPoint lPreUpdate =
                getExecutableExtensionPoint(lLinkType.getId(),
                        ExtensionPointNames.PRE_UPDATE, pContext);

        if (lPreUpdate != null) {
            final ContextBase lCtx = new ContextBase(pContext);
            final LinkDataFactory lLinkDataFactory =
                    new LinkDataFactory(pRoleToken, pCacheableLink);

            lCtx.put(ExtensionPointParameters.LINK, pCacheableLink);

            lCtx.addFactory(
                    ExtensionPointParameters.LINK_DATA.getParameterName(),
                    lLinkDataFactory);
            getExtensionsService().execute(pRoleToken, lPreUpdate, lCtx);

            // Check if the actual value of the LinkData has been created
            // If this is the case, that means the ext point command retrieved
            // its value
            // and we assume it updated it.
            final LinkData lLinkData =
                    (LinkData) lCtx.getValue(ExtensionPointParameters.LINK_DATA.getParameterName());

            if (lLinkData != null) {
                // Recreate the CachedLink from the LinkData (required as the
                // ext
                // point commands may have changed the LinkData).
                pCacheableLink =
                        dataTransformationServiceImpl.getCacheableLinkFromLinkData(
                                pRoleToken, lLinkData);
            }
        }

        // Check mandatory fields
        fieldsManager.checkMandoryFields(pRoleToken, lLinkType, pCacheableLink);

        // Create the link field values
        DynamicValuesContainerAccessFactory.getInstance().getAccessor(
                lLinkType.getId()).updateDomainFromBusiness(lLink,
                pCacheableLink, pContext);

        // Update the attributes of the link.
        getAttributesService().update(lLink.getId(),
                pCacheableLink.getAllAttributes());

        // Increment the version of the link.
        int lPrevVersion = lLink.getVersion();
        lLink.setVersion(lPrevVersion + 1);

        removeElementFromCache(pCacheableLink.getId());
        // Invalid the CacheableSheetLinks of the linked sheets
        removeElementFromCache(CacheableSheetLinksByType.getCachedElementId(
                pCacheableLink.getOriginId(), pCacheableLink.getTypeId()));
        removeElementFromCache(CacheableSheetLinksByType.getCachedElementId(
                pCacheableLink.getDestinationId(), pCacheableLink.getTypeId()));

        // Extension point postUpdateSheetLink or postUpdate
        final ExtensionPoint lPostUpdate =
                getExecutableExtensionPoint(lLinkType.getId(),
                        ExtensionPointNames.POST_UPDATE, pContext);

        if (lPostUpdate != null) {
            final ContextBase lCtx = new ContextBase(pContext);
            final LinkDataFactory lLinkDataFactory =
                    new LinkDataFactory(pRoleToken, pCacheableLink);

            lCtx.put(ExtensionPointParameters.LINK, pCacheableLink);

            lCtx.addFactory(
                    ExtensionPointParameters.LINK_DATA.getParameterName(),
                    lLinkDataFactory);

            getExtensionsService().execute(pRoleToken, lPostUpdate, lCtx);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.link.service.LinkService#updateLink(java.lang.String,
     *      org.topcased.gpm.business.serialization.data.Link,
     *      org.topcased.gpm.business.extensions.service.Context)
     */
    @Override
    public void updateLink(final String pRoleToken,
            final org.topcased.gpm.business.serialization.data.Link pLink,
            Context pContext) throws AuthorizationException {
        CacheableLink lLink =
                getCacheableLink(pLink.getId(), CacheProperties.MUTABLE);
        CacheableLinkType lCacheableLinkType =
                getCacheableLinkType(lLink.getTypeId(),
                        CacheProperties.IMMUTABLE);

        CacheableLink lCacheableLink =
                new CacheableLink(pLink, lCacheableLinkType);
        updateLink(pRoleToken, lCacheableLink, pContext, false);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.link.service.LinkService#getLinkFromKey(java.lang.String,
     *      java.lang.String)
     * @deprecated
     */
    @Deprecated
    @Override
    public LinkData getLinkFromKey(String pRoleToken, String pLinkId) {
        authorizationService.assertValidRoleToken(pRoleToken);
        CacheableLink lLink =
                getLink(pRoleToken, pLinkId, CacheProperties.MUTABLE);
        return getDataTransformationServiceImpl().getLinkDataFromCacheableLink(
                pRoleToken, lLink);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.SheetService#createSheetLinkType(java.lang.String,
     *      java.lang.String,
     *      org.topcased.gpm.business.sheet.service.SheetLinkTypeData)
     * @deprecated
     */
    @Override
    public String createLinkType(final String pRoleToken,
            final String pBusinessProcName, final LinkTypeData pLinkTypeData) {
        final CacheableLinkType lLinkType = getLinkType(pLinkTypeData);
        return createLinkType(pRoleToken, lLinkType);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.link.service.LinkService#getLinkTypeByName(java.lang.String,
     *      java.lang.String, java.lang.String)
     * @deprecated
     */
    @Deprecated
    @Override
    public LinkTypeData getLinkTypeByName(final String pRoleToken,
            final String pProcessName, final String pSheetLinkTypeName) {
        authorizationService.assertValidRoleToken(pRoleToken);

        String lLinkTypeId =
                fieldsContainerServiceImpl.getFieldsContainerId(pRoleToken,
                        pSheetLinkTypeName);

        if (null == lLinkTypeId) {
            return null;
        }

        CacheableLinkType lSheetLinkType =
                getCacheableLinkType(lLinkTypeId, CacheProperties.IMMUTABLE);

        // check that the link type is not confidential
        getAuthService().checkNotConfidentialAccessControl(pRoleToken,
                lSheetLinkType.getId(), lSheetLinkType.getName(),
                lSheetLinkType.getBusinessProcessName(), null, null);

        // check that the origin type is not confidential
        getAuthService().checkNotConfidentialAccessControl(pRoleToken,
                lSheetLinkType.getOriginTypeId(),
                lSheetLinkType.getOriginTypeName(),
                lSheetLinkType.getBusinessProcessName(), null, null);

        // gets the sheetType access control for the destination type
        getAuthService().checkNotConfidentialAccessControl(pRoleToken,
                lSheetLinkType.getDestinationTypeId(),
                lSheetLinkType.getDestinationTypeName(),
                lSheetLinkType.getBusinessProcessName(), null, null);

        return createLinkTypeData(pRoleToken, lSheetLinkType);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.link.service.LinkService#getLinkTypes(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public List<CacheableLinkType> getLinkTypes(final String pRoleToken,
            final String pTypeId) throws AuthorizationException {

        getAuthService().assertValidRoleToken(pRoleToken);

        String lProcessName =
                getAuthService().getProcessNameFromToken(pRoleToken);

        // check that the element type is not confidential
        getAuthService().checkNotConfidentialAccessControl(pRoleToken, pTypeId,
                StringUtils.EMPTY, lProcessName, StringUtils.EMPTY,
                StringUtils.EMPTY);

        List<String> lLinkTypes =
                getLinkTypeDao().getLinkTypesId(pTypeId,
                        LinkNavigation.BIDIRECTIONAL_CREATION);

        List<CacheableLinkType> lRes =
                new ArrayList<CacheableLinkType>(lLinkTypes.size());

        for (String lLinkTypeId : lLinkTypes) {
            CacheableLinkType lLinkType =
                    getCacheableLinkType(lLinkTypeId, CacheProperties.IMMUTABLE);
            lRes.add(lLinkType);
        }
        return lRes;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.link.service.LinkService#getExistingLinkTypes(java.lang.String,
     *      java.lang.String, org.topcased.gpm.util.bean.CacheProperties)
     */
    @Override
    public List<CacheableLinkType> getExistingLinkTypes(String pRoleToken,
            String pValuesContainerId, CacheProperties pProperties)
        throws AuthorizationException {

        List<String> lLinkTypesId =
                linkDao.getExistingLinkTypes(pValuesContainerId);

        List<CacheableLinkType> lLinkTypes = new ArrayList<CacheableLinkType>();

        for (String lLinkTypeId : lLinkTypesId) {
            lLinkTypes.add(getLinkType(pRoleToken, lLinkTypeId, pProperties));
        }

        return lLinkTypes;
    }

    /**
     * Create a SheetLinkTypeData from a SheetTypeLink
     * 
     * @param pTypeLink
     *            source SheetTypeLink
     * @return Newly created SheetTypeData
     * @deprecated {@link LinkServiceImpl#createLinkTypeData(String, CacheableLinkType)}
     */
    // TODO declare in superclass
    public LinkTypeData createLinkTypeData(final CacheableLinkType pTypeLink) {
        return createLinkTypeData(null, pTypeLink);
    }

    /**
     * Create a SheetLinkTypeData from a SheetTypeLink
     * 
     * @param pRoleToken
     *            Role token, for i18nService.
     * @param pTypeLink
     *            source SheetTypeLink
     * @return Newly created SheetTypeData
     */
    // TODO declare in superclass
    public LinkTypeData createLinkTypeData(final String pRoleToken,
            final CacheableLinkType pTypeLink) {
        LinkTypeData lSheetLinkTypeData = new LinkTypeData();

        lSheetLinkTypeData.setId(pTypeLink.getId());
        lSheetLinkTypeData.setName(pTypeLink.getName());

        if ((StringUtils.isNotBlank(pTypeLink.getDescription()))
                && (StringUtils.isNotBlank(pRoleToken))) {
            // For upward compatibility. Previous method signature doesn't have
            // roletoken parameter.
            lSheetLinkTypeData.setDescription(getI18nService().getValueForUser(
                    pRoleToken, pTypeLink.getDescription()));
        }
        else {
            lSheetLinkTypeData.setDescription(pTypeLink.getDescription());
        }
        if (null != pTypeLink.getOriginTypeName()) {
            lSheetLinkTypeData.setOriginType(pTypeLink.getOriginTypeName());
        }
        if (null != pTypeLink.getDestinationTypeName()) {
            lSheetLinkTypeData.setDestinationType(pTypeLink.getDestinationTypeName());
        }
        lSheetLinkTypeData.setLowBound(pTypeLink.getLowBound());
        lSheetLinkTypeData.setHighBound(pTypeLink.getHighBound());

        lSheetLinkTypeData.setUnidirectionalCreation(pTypeLink.isUnidirectionalForCreation());
        lSheetLinkTypeData.setUnidirectionalNavigation(pTypeLink.isUnidirectionalForNavigation());

        return lSheetLinkTypeData;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.SheetService#deleteSheetLinkType(java.lang.String,
     *      java.lang.String, java.lang.String, boolean)
     */
    @Override
    public void deleteLinkType(final String pRoleToken,
            final String pBusinessProcName, final String pSheetLinkTypeName,
            boolean pDeleteLinks) throws AuthorizationException {

        getAuthService().assertAdminRole(pRoleToken);

        LinkType lLinkType = getLinkType(pBusinessProcName, pSheetLinkTypeName);
        if (getValuesContainerDao().getCount(lLinkType.getId()) > 0
                && !pDeleteLinks) {
            throw new UndeletableElementException(pSheetLinkTypeName);
        }

        // FIXME Is it required to delete the sheetLink associated and
        // all associated values?
        getLinkTypeDao().remove(lLinkType);

        // Remove the link type from cache
        removeElementFromCache(lLinkType.getId(), true);
    }

    /**
     * Gets the cacheable link.
     * 
     * @param pLinkId
     *            the link id
     * @param pProperties
     *            the cache properties
     * @return the cacheable link
     */
    // TODO declare in superclass
    public CacheableLink getCacheableLink(String pLinkId,
            CacheProperties pProperties) {
        CacheableLink lCachedLink =
                getCachedElement(CacheableLink.class, pLinkId,
                        pProperties.getCacheFlags());

        if (null == lCachedLink) {
            final Link lLinkEntity = getLink(pLinkId);
            final CacheableLinkType lLinkType =
                    getCacheableLinkType(lLinkEntity.getDefinition().getId(),
                            CacheProperties.IMMUTABLE);

            lCachedLink = new CacheableLink(lLinkEntity, lLinkType);
            addElementInCache(lCachedLink);
        }

        return lCachedLink;
    }

    /**
     * Gets the cacheable link.
     * 
     * @param pLinkEntity
     *            the link entity
     * @param pProperties
     *            the cache properties
     * @return the cacheable link
     */
    // TODO declare in superclass
    public CacheableLink getCacheableLink(Link pLinkEntity,
            CacheProperties pProperties) {
        return getCacheableLink(pLinkEntity.getId(), pProperties);
    }

    /**
     * Get CacheableSheetType without access control. It's not a method of the
     * interface, internal use only
     * 
     * @param pLinkTypeId
     *            The id of the link type
     * @param pProperties
     *            The cache properties
     * @return The cacheableLinkType
     * @throws IllegalArgumentException
     *             If not found.
     */
    // TODO declare in superclass
    public CacheableLinkType getCacheableLinkType(String pLinkTypeId,
            CacheProperties pProperties) throws IllegalArgumentException {
        CacheableLinkType lCachedLinkType =
                getCachedElement(CacheableLinkType.class, pLinkTypeId,
                        pProperties.getCacheFlags());

        if (null == lCachedLinkType) {
            org.topcased.gpm.domain.link.LinkType lLinkTypeEntity =
                    getLinkType(pLinkTypeId);
            lCachedLinkType = new CacheableLinkType(lLinkTypeEntity);
            addElementInCache(lCachedLinkType);
        }

        return lCachedLinkType;
    }

    /**
     * Get link type by name
     * 
     * @param pRoleToken
     *            The role token
     * @param pLinkTypeName
     *            The link type name
     * @return The link type
     */
    // TODO declare in superclass
    public CacheableLinkType getCacheableLinkTypeByName(String pRoleToken,
            String pLinkTypeName) {
        String lInstanceName =
                authorizationService.getProcessNameFromToken(pRoleToken);

        return getCacheableLinkTypeByName(pRoleToken, lInstanceName,
                pLinkTypeName, CacheProperties.IMMUTABLE);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.link.service.LinkService#getCacheableLinkTypeByName(java.lang.String,
     *      java.lang.String, java.lang.String,
     *      org.topcased.gpm.util.bean.CacheProperties)
     */
    @Override
    public CacheableLinkType getCacheableLinkTypeByName(String pRoleToken,
            String pProcessName, String pLinkTypeName,
            CacheProperties pProperties) {
        LinkType lLinkType =
                getLinkTypeDao().getLinkType(pLinkTypeName,
                        getBusinessProcess(pProcessName));

        if (null == lLinkType) {
            throw new InvalidNameException("Type ''{0}'' does not exist",
                    pLinkTypeName);
        }

        return getLinkType(pRoleToken, lLinkType.getId(), pProperties);
    }

    /**
     * Gets the link.
     * 
     * @param pLinkId
     *            the link id
     * @return the link
     */
    // TODO declare in superclass
    private org.topcased.gpm.domain.link.Link getLink(String pLinkId) {
        return fieldsContainerServiceImpl.getValuesContainer(
                org.topcased.gpm.domain.link.Link.class, pLinkId);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.link.service.LinkService#getSerializableLink(java.lang.String)
     */
    @Override
    public org.topcased.gpm.business.serialization.data.Link getSerializableLinkByKey(
            String pRoleToken, String pLinkId) {
        if (StringUtils.isBlank(pRoleToken)) {
            throw new InvalidTokenException("The role token is blank.");
        }

        // TODO add access control
        org.topcased.gpm.business.serialization.data.Link lLink =
                new org.topcased.gpm.business.serialization.data.Link();
        CacheableLink lCacheableLink =
                getCacheableLink(pLinkId, CacheProperties.IMMUTABLE);

        lCacheableLink.marshal(lLink);
        // Product name cannot be filled on marshal
        // Because CacheableLink doesn't know product informations
        if (sheetDao.exist(lCacheableLink.getDestinationId())) {
            // Destination is a sheet
            lLink.setDestinationProductName(sheetDao.getProductName(lCacheableLink.getDestinationId()));
        }
        else {
            // Destination is a product
            lLink.setDestinationProductName(getProductDao().load(
                    lCacheableLink.getDestinationId()).getName());
        }
        if (sheetDao.exist(lCacheableLink.getOriginId())) {
            // Origin is a sheet
            lLink.setOriginProductName(sheetDao.getProductName(lCacheableLink.getOriginId()));
        }
        else {
            // Origin is a product
            lLink.setOriginProductName(getProductDao().load(
                    lCacheableLink.getOriginId()).getName());
        }

        return lLink;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.link.service.LinkService#getSerializableLinks(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public List<org.topcased.gpm.business.serialization.data.Link> getSerializableLinks(
            final String pRoleToken, final String pContainerId)
        throws AuthorizationException {

        // Get the type access control for this sheet
        // (The role token is checked in getTypeAccessControl)
        TypeAccessControlData lTypeAccessControlData =
                getAuthService().getTypeAccessControl(
                        pRoleToken,
                        new AccessControlContextData(
                                CacheProperties.DEFAULT_ACCESS_CONTROL_USED,
                                CacheProperties.DEFAULT_ACCESS_CONTROL_USED,
                                CacheProperties.DEFAULT_ACCESS_CONTROL_USED,
                                CacheProperties.DEFAULT_ACCESS_CONTROL_USED,
                                CacheProperties.DEFAULT_ACCESS_CONTROL_USED,
                                pContainerId));
        if (lTypeAccessControlData != null) {
            // if the user hasn't the right to get this sheet
            if (lTypeAccessControlData.getConfidential()) {
                throw new AuthorizationException("Illegal access to the sheet "
                        + pContainerId + " : the access is confidential.");
            }
        }
        Collection<String> lLinksId;
        lLinksId = getLinkIds(pContainerId);

        List<org.topcased.gpm.business.serialization.data.Link> lLinks =
                new ArrayList<org.topcased.gpm.business.serialization.data.Link>(
                        lLinksId.size());
        for (String lLinkId : lLinksId) {
            lLinks.add(getSerializableLinkByKey(pRoleToken, lLinkId));
        }
        return lLinks;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.link.service.LinkService#getSerializableLinkType(java.lang.String)
     */
    @Override
    public org.topcased.gpm.business.serialization.data.LinkType getSerializableLinkType(
            String pRoleToken, String pLinkTypeId) {
        // Get the type access control for this sheet
        // (The role token is checked in getSheetAccessControl)
        AccessControlContextData lAccessControlContextData =
                new AccessControlContextData();
        String lProductName = getAuthService().getProductName(pRoleToken);
        lAccessControlContextData.setRoleName(getAuthService().getRoleNameFromToken(
                pRoleToken));
        lAccessControlContextData.setProductName(lProductName);
        lAccessControlContextData.setStateName(null);
        lAccessControlContextData.setContainerTypeId(pLinkTypeId);
        TypeAccessControlData lAccessControl =
                getAuthService().getTypeAccessControl(pRoleToken,
                        lAccessControlContextData);

        if (lAccessControl == null || !lAccessControl.getConfidential()) {
            org.topcased.gpm.business.serialization.data.LinkType lLinkType =
                    new org.topcased.gpm.business.serialization.data.LinkType();
            getCacheableLinkType(pLinkTypeId, CacheProperties.IMMUTABLE).marshal(
                    lLinkType);
            return lLinkType;
        }
        return null;
    }

    /** The extensions container DAO. */
    private ExtensionsContainerDao extensionsContainerDao;

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.link.service.LinkService#getSheetLinksByType(java.lang.String,
     *      java.lang.String, java.lang.String, boolean)
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<String> getSheetLinksByType(String pProcessName,
            String pSheetId, String pLinkTypeId, boolean pUseFilter) {
        final CacheableSheet lSheet =
                getSheetService().getCacheableSheet(pSheetId,
                        CacheProperties.IMMUTABLE);
        final String lFilterName;
        List<String> lResult;

        if (pUseFilter) {
            lFilterName =
                    getFilterForLinksDisplaying(pLinkTypeId, lSheet.getTypeId());
        }
        else {
            lFilterName = null;
        }

        if (lFilterName == null | StringUtils.isEmpty(lFilterName)) {
            lResult =
                    new ArrayList<String>(getLinkDao().getSheetLinksByType(
                            pLinkTypeId, pSheetId));
        }
        else {
            final String lAdminRoleToken =
                    getAuthService().getAdminRoleToken(pProcessName);
            final ExecutableFilterData lFilter =
                    getSearchService().getExecutableFilterByName(
                            lAdminRoleToken, pProcessName, lFilterName, null,
                            null);

            if (lFilter == null) {
                throw new FilterException(lFilterName, "Invalid filter name: "
                        + lFilterName);
            }

            CacheableLinkType lLinkType =
                    getCacheableLinkType(pLinkTypeId, CacheProperties.IMMUTABLE);
            // Add constraints on the linked sheets on the filter
            UsableFieldsManager lUsableFieldsManager =
                    getSearchService().getUsableFieldsManager();

            // $ORIGIN_SHEET_REF = $CURRENT_SHEET_REF
            CriteriaFieldData lOriginSheetRef =
                    new CriteriaFieldData(
                            Operators.EQ,
                            Boolean.TRUE,
                            new StringValueData(lSheet.getFunctionalReference()),
                            lUsableFieldsManager.getElement(new UsableFieldCacheKey(
                                    pProcessName,
                                    pLinkTypeId,
                                    VirtualFieldType.$ORIGIN_SHEET_REF.getValue())));
            // $ORIGIN_PRODUCT = $CURRENT_PRODUCT
            CriteriaFieldData lOriginSheetProduct =
                    new CriteriaFieldData(
                            Operators.EQ,
                            Boolean.TRUE,
                            new StringValueData(lSheet.getProductName()),
                            lUsableFieldsManager.getElement(new UsableFieldCacheKey(
                                    pProcessName, pLinkTypeId,
                                    VirtualFieldType.$ORIGIN_PRODUCT.getValue())));
            // ($ORIGIN_SHEET_REF = $CURRENT_SHEET_REF) AND ($ORIGIN_PRODUCT =
            // $CURRENT_PRODUCT)
            OperationData lOriginSheetCriteria =
                    new OperationData(Operators.AND,
                            new CriteriaData[] { lOriginSheetRef,
                                                lOriginSheetProduct });

            OperationData lLinkedSheetsCriteria;

            if (lLinkType.isUnidirectionalForNavigation()) {
                // The link is unidirectionnel, the current sheet is always the
                // origin
                lLinkedSheetsCriteria = lOriginSheetCriteria;
            }
            else {
                // The link is bidirectionnel, the current sheet can be the
                // origin or the destination
                // $DEST_SHEET_REF = $CURRENT_SHEET_REF
                CriteriaFieldData lDestSheetRef =
                        new CriteriaFieldData(
                                Operators.EQ,
                                Boolean.TRUE,
                                new StringValueData(
                                        lSheet.getFunctionalReference()),
                                lUsableFieldsManager.getElement(new UsableFieldCacheKey(
                                        pProcessName,
                                        pLinkTypeId,
                                        VirtualFieldType.$DEST_SHEET_REF.getValue())));
                // $DEST_PRODUCT = $CURRENT_PRODUCT
                CriteriaFieldData lDestSheetProduct =
                        new CriteriaFieldData(
                                Operators.EQ,
                                Boolean.TRUE,
                                new StringValueData(lSheet.getProductName()),
                                lUsableFieldsManager.getElement(new UsableFieldCacheKey(
                                        pProcessName,
                                        pLinkTypeId,
                                        VirtualFieldType.$DEST_PRODUCT.getValue())));
                // ($DEST_SHEET_REF = $CURRENT_SHEET_REF) AND ($DEST_PRODUCT =
                // $CURRENT_PRODUCT)
                OperationData lDestSheetCriteria =
                        new OperationData(Operators.AND,
                                new CriteriaData[] { lDestSheetRef,
                                                    lDestSheetProduct });
                // (($ORIGIN_SHEET_REF = $CURRENT_SHEET_REF) AND
                // ($ORIGIN_PRODUCT = $CURRENT_PRODUCT))
                // OR
                // (($DEST_SHEET_REF = $CURRENT_SHEET_REF) AND ($DEST_PRODUCT =
                // $CURRENT_PRODUCT))
                lLinkedSheetsCriteria =
                        new OperationData(Operators.OR,
                                new CriteriaData[] { lOriginSheetCriteria,
                                                    lDestSheetCriteria });
            }

            // Add the new criteria at the end of the top level criterias
            if (lFilter.getFilterData().getCriteriaData() == null) {
                lFilter.getFilterData().setCriteriaData(lLinkedSheetsCriteria);
            }
            else {
                lFilter.getFilterData().setCriteriaData(
                        new OperationData(
                                Operators.AND,
                                new CriteriaData[] {
                                                    lFilter.getFilterData().getCriteriaData(),
                                                    lLinkedSheetsCriteria }));
            }

            // Execute of the filter
            FilterResultIdIterator lFilterResultIdIterator =
                    getSearchService().executeFilterIdentifier(
                            lAdminRoleToken,
                            lFilter,
                            new FilterVisibilityConstraintData(
                                    getAuthService().getUserFromRole(
                                            lAdminRoleToken).getLogin(),
                                    pProcessName, lSheet.getProductName()),
                            new FilterQueryConfigurator());
            lResult = IteratorUtils.toList(lFilterResultIdIterator);
        }
        return lResult;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.link.service.LinkService#getFilterForLinksDisplaying(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public String getFilterForLinksDisplaying(String pLinkTypeId,
            String pSheetTypeId) {
        CacheableLinkType lLinkType =
                getCacheableLinkType(pLinkTypeId, CacheProperties.IMMUTABLE);
        CacheableSheetType lSheetType =
                getSheetService().getCacheableSheetType(pSheetTypeId,
                        CacheProperties.IMMUTABLE);
        String[] lValues;

        // If the origin and destination types are the same,
        // it's the filter of the origin type that is returned
        if (lLinkType.getOriginTypeName().equals(lSheetType.getName())) {
            lValues =
                    lLinkType.getAttributeValues(org.topcased.gpm.business.serialization.data.LinkType.FILTER_FOR_ORIGIN_SHEET);
        }
        else {
            lValues =
                    lLinkType.getAttributeValues(org.topcased.gpm.business.serialization.data.LinkType.FILTER_FOR_DESTINATION_SHEET);
        }

        // Null, if no filter defined
        if (lValues == null) {
            return null;
        }
        // else
        return lValues[0];
    }

    /**
     * @return Returns the extensionsContainerDao.
     */
    @Override
    public ExtensionsContainerDao getExtensionsContainerDao() {
        return extensionsContainerDao;
    }

    /**
     * setExtensionsContainerDao.
     * 
     * @param pExtensionsContainerDao
     *            The extensionsContainerDao to set.
     */
    @Override
    public void setExtensionsContainerDao(
            final ExtensionsContainerDao pExtensionsContainerDao) {
        extensionsContainerDao = pExtensionsContainerDao;
    }

    /** The link DAO. */
    private LinkDao linkDao;

    /**
     * Gets the link DAO.
     * 
     * @return the link DAO
     */
    public LinkDao getLinkDao() {
        return linkDao;
    }

    /**
     * Sets the link DAO.
     * 
     * @param pLinkDao
     *            the new link DAO
     */
    public void setLinkDao(LinkDao pLinkDao) {
        linkDao = pLinkDao;
    }

    private PointerFieldAttributesDao pointerFieldAttributesDao;

    public void setPointerFieldAttributesDao(
            PointerFieldAttributesDao pPointerFieldAttributesDao) {
        pointerFieldAttributesDao = pPointerFieldAttributesDao;
    }

    /*
     * 
     * A factory for creating LinkType objects.
     */
    private static class CacheableLinkTypeFactory extends
            AbstractCachedObjectFactory {

        /**
         * Constructs a new link type factory.
         */
        public CacheableLinkTypeFactory() {
            super(LinkType.class);
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.topcased.gpm.business.cache.CacheableFactory#createCacheableObject(java.lang.Object)
         */
        public Object createCacheableObject(Object pEntityObject) {
            org.topcased.gpm.domain.link.LinkType lLinkTypeEntity =
                    (LinkType) pEntityObject;
            return new CacheableLinkType(lLinkTypeEntity);
        }
    }

    /**
     * A factory for creating Link objects.
     */
    private class CacheableLinkFactory extends AbstractCachedObjectFactory {

        /**
         * Constructs a new link factory.
         */
        public CacheableLinkFactory() {
            super(Link.class);
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.topcased.gpm.business.cache.CacheableFactory#createCacheableObject(java.lang.Object)
         */
        public Object createCacheableObject(Object pEntityObject) {
            final Link lLinkEntity = (Link) pEntityObject;
            final CacheableLinkType lLinkType =
                    getCacheableLinkType(lLinkEntity.getDefinition().getId(),
                            CacheProperties.IMMUTABLE);

            return new CacheableLink(lLinkEntity, lLinkType);
        }
    }

    /**
     * Link data factory : Construction of a link data from a cacheable link
     * 
     * @deprecated
     * @since 1.8
     * @see CacheableLink
     */
    private class LinkDataFactory implements ContextValueFactory {

        private final CacheableLink link;

        private final String roleToken;

        protected LinkDataFactory(String pRoleToken, CacheableLink pLink) {
            link = pLink;
            roleToken = pRoleToken;
        }

        public Object create(String pName, Class<?> pObjClass) {
            return dataTransformationServiceImpl.getLinkDataFromCacheableLink(
                    roleToken, link);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.link.service.LinkService#createLink(java.lang.String,
     *      org.topcased.gpm.business.link.impl.CacheableLink, boolean,
     *      org.topcased.gpm.business.extensions.service.Context)
     */
    public CacheableLink createLink(String pRoleToken, CacheableLink pLink,
            boolean pCreateAutoPointers, Context pContext)
        throws AuthorizationException, MandatoryValuesException,
        FieldValidationException, GDMException, ConstraintException,
        InvalidIdentifierException {

        // Check that no link of this type exists between the two linked sheets
        // yet.
        if (isLinkExists(pLink)) {
            // The link already exists
            throw new ConstraintException("Link already exists");
        }

        // Find the link type
        CacheableLinkType lLinkType;
        try {
            lLinkType =
                    getCacheableLinkType(pLink.getTypeId(),
                            CacheProperties.IMMUTABLE);
        }
        catch (IllegalArgumentException e) {
            throw new GDMException(
                    "Cannot create a link between these elements, link type '"
                            + pLink.getTypeName() + "' doesn't exist", e);
        }

        //Retrieving origin and destination values container
        CacheableValuesContainer lOrigin =
                fieldsContainerServiceImpl.getValuesContainer(pRoleToken,
                        pLink.getOriginId(), 0, CacheProperties.IMMUTABLE);
        CacheableValuesContainer lDestination =
                fieldsContainerServiceImpl.getValuesContainer(pRoleToken,
                        pLink.getDestinationId(), 0, CacheProperties.IMMUTABLE);

        // Test the compatibility of the given origin and destination
        // with the definition of the link type.
        if (!lLinkType.getOriginTypeId().equals(
                lLinkType.getDestinationTypeId())) {
            if (lLinkType.getDestinationTypeId().equals(lOrigin.getTypeId())) {
                CacheableValuesContainer lOldDestination = lDestination;
                lDestination = lOrigin;
                if (lLinkType.getOriginTypeId().equals(
                        lOldDestination.getTypeId())) {
                    lOrigin = lOldDestination;
                    pLink.setOriginId(lOrigin.getId());
                    pLink.setOriginRef(lOrigin.getFunctionalReference());
                    pLink.setDestinationId(lDestination.getId());
                    pLink.setDestinationRef(lDestination.getFunctionalReference());
                }
                else {
                    throw new InvalidIdentifierException(
                            "The given values container for the destination has not "
                                    + "the same type as the origin defined for the link type");
                }
            }
        }
        if (!lLinkType.getOriginTypeId().equals(lOrigin.getTypeId())) {
            throw new InvalidIdentifierException(
                    "The given values container for the origin has not "
                            + "the same type as the origin defined for the link type");
        }
        if (!(lLinkType.getDestinationTypeId().equals(lDestination.getTypeId()))) {
            throw new InvalidIdentifierException(
                    "The given values container for the destination has not "
                            + "the same type as the destination defined for the link type");
        }

        String lProcessName =
                authorizationService.getProcessNameFromToken(pRoleToken);
        // check that the link type is creatable and not confidential
        authorizationService.checkCreatableAccessControl(pRoleToken,
                lLinkType.getId(), lLinkType.getName(), lProcessName,
                lOrigin.getProductName(), null);

        // Extension point preCreateSheetLink or preCreate
        final ExtensionPoint lPreCreate =
                getExecutableExtensionPoint(lLinkType.getId(),
                        ExtensionPointNames.PRE_CREATE, pContext);

        if (lPreCreate != null) {
            final ContextBase lCtx = new ContextBase(pContext);

            lCtx.put(ExtensionPointParameters.LINK, pLink);

            lCtx.addFactory(
                    ExtensionPointParameters.LINK_DATA.getParameterName(),
                    new LinkDataFactory(pRoleToken, pLink));

            getExtensionsService().execute(pRoleToken, lPreCreate, lCtx);

            // Check if the actual value of the LinkData has been created
            // If this is the case, that means the ext point command retrieved
            // its value and we assume it updated it.
            final LinkData lLinkData =
                    (LinkData) lCtx.getValue(ExtensionPointParameters.LINK_DATA.getParameterName());

            if (lLinkData != null) {
                // Recreate the CachedLink from the LinkData (required as the
                // ext
                // point commands may have changed the LinkData).
                pLink =
                        dataTransformationServiceImpl.getCacheableLinkFromLinkData(
                                pRoleToken, lLinkData);
            }
        }

        String lLinkId =
                doCreateLink(pRoleToken, pLink, lLinkType, pCreateAutoPointers,
                        pContext);

        // Extension point postCreateSheetLink or postCreate
        final ExtensionPoint lPostCreate =
                getExecutableExtensionPoint(lLinkType.getId(),
                        ExtensionPointNames.POST_CREATE, pContext);

        if (lPostCreate != null) {
            final ContextBase lCtx = new ContextBase(pContext);

            lCtx.put(ExtensionPointParameters.LINK_ID, lLinkId);

            getExtensionsService().execute(pRoleToken, lPostCreate, lCtx);
        }

        return pLink;
    }

    /**
     * Test the link existence.
     * <p>
     * A link exists only if:
     * <ol>
     * <li>Identifier exists OR</li>
     * <li>There is a link with the same origin identifier and destination
     * identifier and link's type.</li>
     * </ol>
     * Note: There is a permutation between origin and destination to test
     * bidirectinal links.
     * 
     * @param pLink
     *            Link to test
     * @return True if the link exists, false otherwise
     */
    // TODO declare in superclass
    public boolean isLinkExists(
            final org.topcased.gpm.business.serialization.data.Link pLink) {
        final Boolean lRes;
        if (StringUtils.isNotBlank(pLink.getId())) {
            lRes = linkDao.exist(pLink.getId());
        }
        else {
            lRes =
                    linkDao.isLinkExists(pLink.getType(), pLink.getOriginId(),
                            pLink.getDestinationId());
        }
        return lRes;
    }

    /**
     * Test the link existence.
     * <p>
     * A link exists only if:
     * <ol>
     * <li>Identifier exists OR</li>
     * <li>There is a link with the same origin identifier and destination
     * identifier and link's type.</li>
     * </ol>
     * Note: There is a permutation between origin and destination to test
     * bidirectinal links.
     * 
     * @param pLink
     *            Link to test
     * @return True if the link exists, false otherwise
     */
    // TODO declare in superclass
    public boolean isLinkExists(final CacheableLink pLink) {
        final Boolean lRes;
        if (StringUtils.isNotBlank(pLink.getId())) {
            lRes = linkDao.exist(pLink.getId());
        }
        else {
            lRes =
                    linkDao.isLinkExists(pLink.getTypeName(),
                            pLink.getOriginId(), pLink.getDestinationId());
        }
        return lRes;
    }

    /**
     * Test the existence of a product's link.
     * 
     * @param pTypeName
     *            Name of the link's type.
     * @param pOriginRef
     *            Reference of the origin sheet.
     * @param pDestinationRef
     *            Reference of the destination sheet.
     * @return True if the sheet's link exists, false otherwise (one of
     *         parameters is blank).
     */
    // TODO declare in superclass
    public boolean isSheetLinkExists(final String pTypeName,
            final String pOriginRef, final String pDestinationRef) {
        if (StringUtils.isNotBlank(pTypeName)
                && StringUtils.isNotBlank(pOriginRef)
                && StringUtils.isNotBlank(pDestinationRef)) {
            return linkDao.isSheetLinkExists(pTypeName, pOriginRef,
                    pDestinationRef);
        }
        else {
            return false;
        }
    }

    /**
     * Test the existence of a product's link.
     * 
     * @param pTypeName
     *            Name of the link's type.
     * @param pOriginProductName
     *            Name of the origin product.
     * @param pDestinationProductName
     *            Name of the destination product.
     * @return True if the product's link exists, false otherwise (one of
     *         parameters is blank).
     */
    // TODO declare in superclass
    public boolean isProductLinkExists(final String pTypeName,
            final String pOriginProductName,
            final String pDestinationProductName) {
        if (StringUtils.isNotBlank(pTypeName)
                && StringUtils.isNotBlank(pOriginProductName)
                && StringUtils.isNotBlank(pDestinationProductName)) {
            return linkDao.isProductLinkExists(pTypeName, pOriginProductName,
                    pDestinationProductName);
        }
        else {
            return false;
        }
    }

    /**
     * Retrive the identifier of a link.
     *<p>
     * Use origin identifier, destination identifier and link's type name to
     * find the link.
     * </p>
     * 
     * @param pTypeName
     *            Name of the link's type.
     * @param pOriginId
     *            Technical identifier of the origin
     * @param pDestinationId
     *            Technical identifier of the destination
     * @return Identifier of the link, null otherwise.
     */
    // TODO declare in superclass
    public String getId(final String pTypeName, final String pOriginId,
            final String pDestinationId) {
        return linkDao.getId(pTypeName, pOriginId, pDestinationId);
    }

    /**
     * Get the sheet link id
     * 
     * @param pProcessName
     *            The process name.
     * @param pOriginProductName
     *            The origin product name.
     * @param pOriginSheetReference
     *            The origin sheet reference.
     * @param pDestinationProductName
     *            The destination product name.
     * @param pDestinationSheetReference
     *            The destination sheet reference.
     * @param pTypeName
     *            The type name.
     * @return The sheet link id.
     */
    // TODO declare in superclass
    public String getSheetLinkId(final String pProcessName,
            final String pOriginProductName,
            final String pOriginSheetReference,
            final String pDestinationProductName,
            final String pDestinationSheetReference, final String pTypeName) {
        return linkDao.getSheetLinkId(pProcessName, pOriginProductName,
                pOriginSheetReference, pDestinationProductName,
                pDestinationSheetReference, pTypeName);
    }

    /**
     * Get the product link id
     * 
     * @param pProcessName
     *            The process name.
     * @param pOriginProductName
     *            The origin product name.
     * @param pDestinationProductName
     *            The destination product name.
     * @param pTypeName
     *            The type name.
     * @return The product link id.
     */
    // TODO declare in superclass
    public String getProductLinkId(final String pProcessName,
            final String pOriginProductName,
            final String pDestinationProductName, final String pTypeName) {
        return linkDao.getProductLinkId(pProcessName, pOriginProductName,
                pDestinationProductName, pTypeName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.link.service.LinkService#getLink(java.lang.String,
     *      java.lang.String, org.topcased.gpm.util.bean.CacheProperties)
     */
    @Override
    public CacheableLink getLink(String pRoleToken, String pLinkId,
            CacheProperties pProperties) {
        return (CacheableLink) fieldsContainerServiceImpl.getValuesContainer(
                pRoleToken, pLinkId,
                FieldsContainerService.FIELD_NOT_CONFIDENTIAL, pProperties);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.link.service.LinkService#getLinks(java.lang.String,
     *      java.lang.String, org.topcased.gpm.util.bean.CacheProperties)
     */
    @Override
    public List<CacheableLink> getLinks(String pRoleToken, String pContainerId,
            CacheProperties pCacheProperties) throws AuthorizationException {
        List<String> lLinksId = getLinksId(pRoleToken, pContainerId);
        List<CacheableLink> lLinks =
                new ArrayList<CacheableLink>(lLinksId.size());

        for (String lLinkId : lLinksId) {
            lLinks.add(getCacheableLink(lLinkId, pCacheProperties));
        }
        return lLinks;
    }

    /**
     * Get the link's identifiers of a container
     * 
     * @param pRoleToken
     *            role token
     * @param pContainerId
     *            Identifier of the container
     * @return Identifier of links for the specified container
     * @throws InvalidTokenException
     *             Role token is invalid (expired)
     * @throws AuthorizationException
     *             No right to access to container
     */
    private List<String> getLinksId(String pRoleToken, String pContainerId)
        throws InvalidTokenException, AuthorizationException {
        getAuthService().assertValidRoleToken(pRoleToken);

        if (!authorizationService.hasGlobalAdminRole(pRoleToken)) {
            // gets the type access control for this container
            TypeAccessControlData lTypeAccessControlData =
                    getAuthService().getTypeAccessControl(
                            pRoleToken,
                            new AccessControlContextData(
                                    CacheProperties.DEFAULT_ACCESS_CONTROL_USED,
                                    CacheProperties.DEFAULT_ACCESS_CONTROL_USED,
                                    CacheProperties.DEFAULT_ACCESS_CONTROL_USED,
                                    CacheProperties.DEFAULT_ACCESS_CONTROL_USED,
                                    CacheProperties.DEFAULT_ACCESS_CONTROL_USED,
                                    pContainerId));

            if (lTypeAccessControlData != null) {
                // if the user hasn't the right to get this sheet
                if (lTypeAccessControlData.getConfidential()) {
                    throw new AuthorizationException(
                            "Illegal access to the sheet " + pContainerId
                                    + " : the access is confidential.");
                }
            }
        }
        return getLinkDao().getLinks(pContainerId);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.link.service.LinkService#getLinkType(java.lang.String,
     *      java.lang.String, org.topcased.gpm.util.bean.CacheProperties)
     */
    @Override
    public CacheableLinkType getLinkType(String pRoleToken, String pLinkTypeId,
            CacheProperties pProperties) {
        final CacheableLinkType lLinkType =
                getCacheableLinkType(pLinkTypeId, pProperties);

        if (pProperties.getSpecificAccessControl() == null) {
            return lLinkType;
        }
        // else
        return authorizationService.getCheckedLinksFieldsContainer(pRoleToken,
                pProperties.getSpecificAccessControl(), lLinkType);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.link.service.LinkService#getLinkTypeByName(java.lang.String,
     *      java.lang.String, org.topcased.gpm.util.bean.CacheProperties)
     */
    @Override
    public CacheableLinkType getLinkTypeByName(String pRoleToken,
            String pLinkTypeName, CacheProperties pProperties) {
        final String lProcessName =
                authorizationService.getProcessNameFromToken(pRoleToken);
        LinkType lLinkType =
                getLinkTypeDao().getLinkType(pLinkTypeName,
                        getBusinessProcess(lProcessName));

        if (null == lLinkType) {
            throw new InvalidNameException("Type ''{0}'' does not exist",
                    pLinkTypeName);
        }

        return getLinkType(pRoleToken, lLinkType.getId(), pProperties);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.link.service.LinkService#createLinkType(java.lang.String,
     *      org.topcased.gpm.business.link.impl.CacheableLinkType)
     */
    @Override
    public String createLinkType(String pRoleToken, CacheableLinkType pLinkType) {
        getAuthService().assertAdminRole(pRoleToken);

        // Check input parameters
        if (null == pLinkType) {
            throw new GDMException("LinkType parameter is null");
        }

        if (StringUtils.isBlank(pLinkType.getOriginTypeName())) {
            throw new InvalidNameException("OriginType's name is blank");
        }

        if (StringUtils.isBlank(pLinkType.getDestinationTypeName())) {
            throw new InvalidNameException("DestinationType's name is blank");
        }

        if (StringUtils.isBlank(pLinkType.getName())) {
            throw new InvalidNameException("Name is blank");
        }

        String lProcessName =
                authorizationService.getProcessNameFromToken(pRoleToken);
        BusinessProcess lBusinessProc = getBusinessProcess(lProcessName);
        LinkType lLinkType;

        FieldsContainer lOriginType =
                getFieldsContainer(lProcessName, pLinkType.getOriginTypeName(),
                        false);
        FieldsContainer lDestinationType =
                getFieldsContainer(lProcessName,
                        pLinkType.getDestinationTypeName(), false);
        lLinkType =
                getLinkTypeDao().getLinkType(pLinkType.getName(), lBusinessProc);

        if (null == lLinkType) {
            // Create a new SheetTypeLink
            lLinkType = LinkType.newInstance();
            lLinkType.setName(pLinkType.getName());
            lLinkType.setBusinessProcess(lBusinessProc);
        }

        lLinkType.setDescription(pLinkType.getDescription());
        lLinkType.setLowBound(pLinkType.getLowBound());
        lLinkType.setHighBound(pLinkType.getHighBound());

        lLinkType.setUnidirectionalCreation(pLinkType.isUnidirectionalForCreation());
        lLinkType.setUnidirectionalNavigation(pLinkType.isUnidirectionalForNavigation());

        lLinkType.setOriginType(lOriginType);
        lLinkType.setDestType(lDestinationType);

        getLinkTypeDao().create(lLinkType);
        removeElementFromCache(lLinkType.getId(), true);

        return lLinkType.getId();
    }

    private static CacheableLinkType getLinkType(final LinkTypeData pLinkType) {
        final CacheableLinkType lLinkType;
        if (pLinkType == null) {
            lLinkType = null;
        }
        else {
            lLinkType = new CacheableLinkType();
            lLinkType.setId(pLinkType.getId());
            lLinkType.setName(pLinkType.getName());
            lLinkType.setDescription(pLinkType.getDescription());
            lLinkType.setUnidirectionalForCreation(pLinkType.isUnidirectionalCreation());
            lLinkType.setUnidirectionalForNavigation(pLinkType.isUnidirectionalNavigation());
            lLinkType.setHighBound(pLinkType.getHighBound());
            lLinkType.setLowBound(pLinkType.getLowBound());
            lLinkType.setOriginTypeName(pLinkType.getOriginType());
            lLinkType.setDestinationTypeName(pLinkType.getDestinationType());
        }
        return lLinkType;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.link.service.LinkService#getLinksByType(java.lang.String,
     *      java.lang.String, org.topcased.gpm.util.bean.CacheProperties)
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<CacheableLink> getLinksByType(String pRoleToken,
            String pLinkTypeName, CacheProperties pCacheProperties) {
        authorizationService.assertValidRoleToken(pRoleToken);

        final String lProcessName =
                authorizationService.getProcessNameFromToken(pRoleToken);
        LinkType lSheetLinkType = getLinkType(lProcessName, pLinkTypeName);

        // Check that the link type is not confidential
        getAuthService().checkNotConfidentialAccessControl(pRoleToken,
                lSheetLinkType.getId(), pLinkTypeName, lProcessName, null, null);

        List<Link> lLinks =
                getValuesContainerDao().getAll(lSheetLinkType.getId());
        List<String> lAllowedLinks = new ArrayList<String>(lLinks.size());
        for (Link lSheetLink : lLinks) {
            boolean lOriginAuthorized = false;
            String lOriginId = lSheetLink.getOrigin().getDefinition().getId();
            // gets the sheetType access control for the origin sheetType
            AccessControlContextData lAccessControlContextData =
                    new AccessControlContextData();
            String lProductName = getAuthService().getProductName(pRoleToken);
            lAccessControlContextData.setRoleName(getAuthService().getRoleNameFromToken(
                    pRoleToken));
            lAccessControlContextData.setProductName(lProductName);
            lAccessControlContextData.setStateName(null);
            lAccessControlContextData.setContainerTypeId(lOriginId);
            TypeAccessControlData lTypeAccessControlData =
                    getAuthService().getTypeAccessControl(pRoleToken,
                            lAccessControlContextData);
            // if the user hasn't the right to access this type
            if ((lTypeAccessControlData != null && !lTypeAccessControlData.getConfidential())
                    || lTypeAccessControlData == null) {
                lOriginAuthorized = true;
            }

            String lDestId =
                    lSheetLink.getDestination().getDefinition().getId();
            // gets the sheetType access control for the destination sheetType
            AccessControlContextData lAccessControlContextDestData =
                    new AccessControlContextData();
            lAccessControlContextDestData.setRoleName(getAuthService().getRoleNameFromToken(
                    pRoleToken));
            lAccessControlContextDestData.setProductName(lProductName);
            lAccessControlContextDestData.setStateName(null);
            lAccessControlContextDestData.setContainerTypeId(lDestId);
            lTypeAccessControlData =
                    getAuthService().getTypeAccessControl(pRoleToken,
                            lAccessControlContextDestData);
            // if the user hasn't the right to access this type
            if ((lTypeAccessControlData != null && !lTypeAccessControlData.getConfidential())
                    || lTypeAccessControlData == null) {
                if (lOriginAuthorized) {
                    lAllowedLinks.add(lSheetLink.getId());
                }
            }
        }
        return createLinkList(pRoleToken, lAllowedLinks, pCacheProperties);
    }
}
