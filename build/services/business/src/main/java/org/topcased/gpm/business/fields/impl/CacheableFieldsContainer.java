/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/

package org.topcased.gpm.business.fields.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.ServiceLocator;
import org.topcased.gpm.business.attributes.AttributeData;
import org.topcased.gpm.business.attributes.service.AttributesService;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.extensions.impl.CacheableExtensionsContainer;
import org.topcased.gpm.business.serialization.data.AppletParameter;
import org.topcased.gpm.business.serialization.data.ChoiceStringDisplayHint;
import org.topcased.gpm.business.serialization.data.DisplayGroup;
import org.topcased.gpm.business.serialization.data.DisplayHint;
import org.topcased.gpm.business.serialization.data.Field;
import org.topcased.gpm.business.serialization.data.FieldRef;
import org.topcased.gpm.business.serialization.data.GridDisplayHint;
import org.topcased.gpm.business.serialization.data.Lock.LockTypeEnumeration;
import org.topcased.gpm.business.util.GridObjectsUtil;
import org.topcased.gpm.domain.facilities.AttachedDisplayHint;
import org.topcased.gpm.domain.facilities.ChoiceFieldDisplayHint;
import org.topcased.gpm.domain.facilities.ChoiceFieldDisplayType;
import org.topcased.gpm.domain.facilities.FieldDisplayHint;
import org.topcased.gpm.domain.facilities.TextFieldDisplayHint;
import org.topcased.gpm.domain.fields.AttachedField;
import org.topcased.gpm.domain.fields.ChoiceField;
import org.topcased.gpm.domain.fields.MultipleField;
import org.topcased.gpm.domain.fields.PointerFieldAttributes;
import org.topcased.gpm.domain.fields.SimpleField;

/**
 * The Class CacheableFieldsContainer.
 * 
 * @author llatil
 * @author phtsaan
 */
public class CacheableFieldsContainer extends CacheableExtensionsContainer {
    /** serialVersionUID */
    private static final long serialVersionUID = -7314545871035495919L;

    /** Comparator between field names */
    private static final Comparator<Field> FIELD_NAMES_COMPARATOR =
            new FieldNameComparator();

    /** Map of fields (field key -> Field definition). */
    protected Map<String, org.topcased.gpm.business.serialization.data.Field> fieldsKeyMap =
            new HashMap<String, org.topcased.gpm.business.serialization.data.Field>();

    /** Set of top-level fields only */
    protected Set<Field> topLevelFields =
            new TreeSet<Field>(FIELD_NAMES_COMPARATOR);

    /**
     * Map of fields (field ID key -> Field definition). All fields are included
     * in this map.
     */
    protected Map<String, org.topcased.gpm.business.serialization.data.Field> fieldsIdMap =
            new HashMap<String, org.topcased.gpm.business.serialization.data.Field>();

    /** Map of fields groups (group label key -> Group definition). */
    private Map<String, DisplayGroup> displayGroupsMap = Collections.emptyMap();

    /** Map of display hints (Field key -> display hint). */
    private Map<String, org.topcased.gpm.business.serialization.data.DisplayHint> displayHintsMap =
            Collections.emptyMap();

    /** Name of this container (type). */
    private String name;

    /**
     * Translated value of description if exists. The description non translated
     * value otherwise.
     */
    private String description;

    /** Name of the gPM business process defining this type. */
    private String businessProcessName;

    /**
     * Creatable access control attribute (indicates if a user can create a
     * values container from this type or not)
     */
    private Boolean creatable;

    /**
     * Updatable access control attribute (indicates if a user can update a
     * values container from this type or not)
     */
    private Boolean updatable;

    /**
     * Confidential access control attribute (indicates if a user can see a
     * values container from this type or not)
     */
    private Boolean confidential;

    /**
     * Deletable access control attribute (indicates if a user can delete a
     * values container from this type or not)
     */
    private Boolean deletable;

    /**
     * All sheets of this type can be locked automatically.
     */
    private boolean autolocking = false;

    /**
     * The type of the lock that can be set.
     */
    private LockTypeEnumeration lockType;

    /**
     * Empty constructor for mutable/immutable transformation
     */
    public CacheableFieldsContainer() {
        super();
    }

    /**
     * Constructs a new FieldsContainer object suitable for caching.
     * 
     * @param pContainerEntity
     *            Container object read from the database
     */
    public CacheableFieldsContainer(
            org.topcased.gpm.domain.fields.FieldsContainer pContainerEntity) {
        super(pContainerEntity.getId());

        name = pContainerEntity.getName();
        setDescription(pContainerEntity.getDescription());

        businessProcessName = pContainerEntity.getBusinessProcess().getName();

        setAutolockingAttribute(pContainerEntity.getId());

        for (org.topcased.gpm.domain.fields.Field lFieldEntity : pContainerEntity.getFields()) {
            addField(lFieldEntity);
        }
    }

    /**
     * Setting the autolocking and lockType attributes.<br />
     * Retrieve information first from the sheet type extended attribute and if
     * not defined, retrieve from the global attribute.
     * 
     * @param pFieldsContainerId
     *            Sheet type identifier.
     */
    private void setAutolockingAttribute(String pFieldsContainerId) {
        AttributesService lAttributesService =
                ServiceLocator.instance().getAttributesService();

        AttributeData[] lAttributeData =
                lAttributesService.get(pFieldsContainerId,
                        new String[] { AttributesService.AUTOLOCKING });
        if (!ArrayUtils.isEmpty(lAttributeData)) {
            AttributeData lAutolockingAttributeData = lAttributeData[0];
            if (lAutolockingAttributeData != null) {
                getAutolockingAttributeValue(lAutolockingAttributeData);
            }
            else { // the attribute is not defined. Maybe in a global
                // attribute.
                AttributeData[] lGlobalAttributeData =
                        lAttributesService.getGlobalAttributes(new String[] { AttributesService.AUTOLOCKING });
                if (!ArrayUtils.isEmpty(lGlobalAttributeData)) {
                    lAutolockingAttributeData = lGlobalAttributeData[0];
                    if (lAutolockingAttributeData != null) {
                        getAutolockingAttributeValue(lAutolockingAttributeData);
                    }
                }
            }
        }
    }

    /**
     * Get the attribute value. <br />
     * set the autolocking and lockType CacheableSheetType attributes.
     * 
     * @param pAutolockingAttributeData
     *            Attribute value. (Cannot be null).
     */
    private void getAutolockingAttributeValue(
            AttributeData pAutolockingAttributeData) {
        // can be locked
        if (!ArrayUtils.isEmpty(pAutolockingAttributeData.getValues())) {
            LockTypeEnumeration lLockType =
                    LockTypeEnumeration.valueOf(pAutolockingAttributeData.getValues()[0]);
            if (!lLockType.equals(LockTypeEnumeration.NONE)) {
                autolocking = true;
                lockType = lLockType;
            }
        }
    }

    /**
     * Constructs a new FieldsContainer object suitable for caching.
     * 
     * @return the fieldsKeyMap
     */
    public Map<String, org.topcased.gpm.business.serialization.data.Field> getFieldsKeyMap() {
        return fieldsKeyMap;
    }

    /**
     * get displayGroupsMap
     * 
     * @return the displayGroupsMap
     */
    public Map<String, DisplayGroup> getDisplayGroupsMap() {
        return displayGroupsMap;
    }

    /**
     * Get a field from its label
     * 
     * @param pFieldLabel
     *            Field label
     * @return The serializable field data.
     */
    public org.topcased.gpm.business.serialization.data.Field getFieldFromLabel(
            String pFieldLabel) {
        return fieldsKeyMap.get(pFieldLabel);
    }

    /**
     * Get a field from its technical identifier.
     * 
     * @param pFieldId
     *            Field identifier
     * @return The serializable field data.
     */
    public org.topcased.gpm.business.serialization.data.Field getFieldFromId(
            String pFieldId) {
        return fieldsIdMap.get(pFieldId);
    }

    /**
     * Get the list of all top-level fields defined in this container.
     * 
     * @return Field definitions list
     */
    public Collection<? extends org.topcased.gpm.business.serialization.data.Field> getFields() {
        return topLevelFields;
    }

    /**
     * Get the list of all fields defined in this container
     * 
     * @return All field definitions list
     */
    public Collection<? extends org.topcased.gpm.business.serialization.data.Field> getAllFields() {
        return fieldsKeyMap.values();
    }

    /**
     * Get the list of display hints defined for the fields of this container.
     * 
     * @return Field definitions list
     */
    public Collection<? extends DisplayHint> getDisplayHints() {
        return displayHintsMap.values();
    }

    /**
     * Retrieve a display hints defined for a given field.
     * 
     * @param pFieldKey
     *            Field key
     * @return Field display hint, or null if not defined
     */
    public org.topcased.gpm.business.serialization.data.DisplayHint getDisplayHint(
            String pFieldKey) {
        return displayHintsMap.get(pFieldKey);
    }

    /**
     * Gets the display group.
     * 
     * @param pGroupName
     *            the group name
     * @return the display group
     */
    public org.topcased.gpm.business.serialization.data.DisplayGroup getDisplayGroup(
            String pGroupName) {
        return displayGroupsMap.get(pGroupName);
    }

    /**
     * Gets the display groups.
     * 
     * @return the display groups
     */
    public Collection<? extends DisplayGroup> getDisplayGroups() {
        return displayGroupsMap.values();
    }

    /**
     * Add a new field entity in this object.
     * 
     * @param pFieldEntity
     *            Field entity to add
     */
    protected void addField(org.topcased.gpm.domain.fields.Field pFieldEntity) {
        if (fieldsIdMap.get(pFieldEntity.getId()) == null) {
            org.topcased.gpm.business.serialization.data.Field lField;
            lField = createSerializableField(pFieldEntity);

            fieldsKeyMap.put(pFieldEntity.getLabelKey(), lField);
            if (!pFieldEntity.isSubfield()) {
                addTopLevelField(lField);
            }
        }
    }

    /**
     * Add a field to the top level field listF
     * 
     * @param pFieldEntity
     *            The field to add
     */
    protected void addTopLevelField(
            org.topcased.gpm.business.serialization.data.Field pFieldEntity) {
        topLevelFields.add(pFieldEntity);
    }

    /**
     * Create a serializable field.
     * 
     * @param pDomainField
     *            The field to serialize.
     * @return The serialized field.
     */
    protected Field createSerializableField(
            final org.topcased.gpm.domain.fields.Field pDomainField) {
        final org.topcased.gpm.business.serialization.data.Field lField;

        if (pDomainField instanceof SimpleField) {
            final org.topcased.gpm.business.serialization.data.SimpleField lSimpleField =
                    new org.topcased.gpm.business.serialization.data.SimpleField();
            final SimpleField lDomainSimpleField = (SimpleField) pDomainField;

            lSimpleField.setValueType(StringUtils.capitalize(lDomainSimpleField.getType().toString()));
            if (lDomainSimpleField.getDefaultValue() != null) {
                lSimpleField.setDefaultValue(lDomainSimpleField.getDefaultValue().getAsString());
            }
            lSimpleField.setSizeAsInt(lDomainSimpleField.getMaxSize());

            lField = lSimpleField;
        }
        else if (pDomainField instanceof ChoiceField) {
            final org.topcased.gpm.business.serialization.data.ChoiceField lChoiceField =
                    new org.topcased.gpm.business.serialization.data.ChoiceField();
            final ChoiceField lDomainChoiceField = (ChoiceField) pDomainField;

            if (lDomainChoiceField.getDefaultValue() != null) {
                lChoiceField.setDefaultValue(lDomainChoiceField.getDefaultValue().getValue());
            }
            lChoiceField.setCategoryName(lDomainChoiceField.getCategory().getName());

            lField = lChoiceField;
        }
        else if (pDomainField instanceof AttachedField) {
            lField =
                    new org.topcased.gpm.business.serialization.data.AttachedField();
        }
        else if (pDomainField instanceof MultipleField) {
            final org.topcased.gpm.business.serialization.data.MultipleField lMultiField =
                    new org.topcased.gpm.business.serialization.data.MultipleField();
            final MultipleField lDomainMultipleField =
                    (MultipleField) pDomainField;
            final List<org.topcased.gpm.business.serialization.data.Field> lSubFields =
                    new ArrayList<org.topcased.gpm.business.serialization.data.Field>();

            for (org.topcased.gpm.domain.fields.Field lSubFieldEntity : lDomainMultipleField.getFields()) {
                final Field lSubField =
                        createSerializableField(lSubFieldEntity);

                lSubField.setMultipleField(lDomainMultipleField.getId());
                lSubFields.add(lSubField);
            }
            lMultiField.setFieldSeparator(lDomainMultipleField.getFieldSeparator());
            lMultiField.setFields(lSubFields);

            lField = lMultiField;
        }
        else {
            throw new GDMException("Invalid field type : "
                    + pDomainField.getClass().getName());
        }

        lField.setId(pDomainField.getId());

        lField.setLabelKey(pDomainField.getLabelKey());
        lField.setDescription(pDomainField.getDescription());

        lField.setConfidential(pDomainField.isConfidential());
        lField.setExportable(pDomainField.isExportable());
        lField.setMandatory(pDomainField.isMandatory());
        lField.setMultivalued(pDomainField.isMultiValued());
        lField.setUpdatable(pDomainField.isUpdatable());

        if (pDomainField.getFieldDisplayHint() != null) {
            final DisplayHint lHint = createDisplayHint(pDomainField);

            if (lHint != null) {
                // Create the actual hints map if needed.
                if (displayHintsMap == Collections.EMPTY_MAP) {
                    displayHintsMap =
                            new HashMap<String, org.topcased.gpm.business.serialization.data.DisplayHint>();
                }
                displayHintsMap.put(pDomainField.getLabelKey(), lHint);
            }
        }

        lField.setPointerField(pDomainField.isPointerField());
        // Add pointer field attributes if necessary
        if (pDomainField.isPointerField()) {
            PointerFieldAttributes lPointerFieldAttributes =
                    pDomainField.getPointerFieldAttributes();
            if (lPointerFieldAttributes != null) {
                lField.setReferencedFieldLabel(lPointerFieldAttributes.getReferencedFieldLabel());
                lField.setReferencedLinkType(lPointerFieldAttributes.getReferencedLinkType());
            }
        }

        fieldsIdMap.put(lField.getId(), lField);
        fieldsKeyMap.put(lField.getLabelKey(), lField);

        return lField;
    }

    /**
     * Creates the display hint.
     * 
     * @param pDisplayHintEntity
     *            the display hint entity
     * @param pLabelKey
     *            Label key of the field attached to this display hint.
     * @return the org.topcased.gpm.business.serialization.data. display hint
     */
    private static org.topcased.gpm.business.serialization.data.DisplayHint createDisplayHint(
            org.topcased.gpm.domain.fields.Field pField) {
        FieldDisplayHint lDisplayHintEntity = pField.getFieldDisplayHint();
        String lLabelKey = pField.getLabelKey();

        if (lDisplayHintEntity instanceof ChoiceFieldDisplayHint) {
            org.topcased.gpm.domain.facilities.ChoiceFieldDisplayHint lChoiceDisplayHintEntity;
            org.topcased.gpm.business.serialization.data.ChoiceDisplayHint lChoiceDisplayHint;

            lChoiceDisplayHintEntity =
                    (org.topcased.gpm.domain.facilities.ChoiceFieldDisplayHint) lDisplayHintEntity;
            lChoiceDisplayHint =
                    new org.topcased.gpm.business.serialization.data.ChoiceDisplayHint();

            boolean lIsList =
                    lChoiceDisplayHintEntity.getDisplayType().equals(
                            ChoiceFieldDisplayType.COMBO)
                            || lChoiceDisplayHintEntity.getDisplayType().equals(
                                    ChoiceFieldDisplayType.LIST);
            lChoiceDisplayHint.setList(lIsList);
            lChoiceDisplayHint.setLabelKey(lLabelKey);

            if (lChoiceDisplayHintEntity.getDisplayType().equals(
                    ChoiceFieldDisplayType.IMAGE)
                    || lChoiceDisplayHintEntity.getDisplayType().equals(
                            ChoiceFieldDisplayType.IMAGE_TEXT)) {
                String lImageType =
                        lChoiceDisplayHintEntity.getDisplayType().getValue();
                lChoiceDisplayHint.setImageType(lImageType);
                lChoiceDisplayHint.setLabelKey(lLabelKey);
            }
            return lChoiceDisplayHint;
        }
        else if (lDisplayHintEntity instanceof TextFieldDisplayHint) {
            org.topcased.gpm.domain.facilities.TextFieldDisplayHint lTextDisplayHintEntity;
            org.topcased.gpm.business.serialization.data.TextDisplayHint lTextDisplayHint;

            lTextDisplayHintEntity =
                    (org.topcased.gpm.domain.facilities.TextFieldDisplayHint) lDisplayHintEntity;
            lTextDisplayHint =
                    new org.topcased.gpm.business.serialization.data.TextDisplayHint();

            lTextDisplayHint.setDisplayType(lTextDisplayHintEntity.getDisplay().toString());
            lTextDisplayHint.setWidth(lTextDisplayHintEntity.getWidth());
            lTextDisplayHint.setHeight(lTextDisplayHintEntity.getHeight());
            lTextDisplayHint.setMultiline(lTextDisplayHintEntity.getHeight() > 1);
            lTextDisplayHint.setLabelKey(lLabelKey);
            return lTextDisplayHint;
        }
        else if (lDisplayHintEntity instanceof AttachedDisplayHint) {
            org.topcased.gpm.domain.facilities.AttachedDisplayHint lAttachedDispHintEntity;
            org.topcased.gpm.business.serialization.data.AttachedDisplayHint lAttachedDispHint;

            lAttachedDispHintEntity =
                    (org.topcased.gpm.domain.facilities.AttachedDisplayHint) lDisplayHintEntity;
            lAttachedDispHint =
                    new org.topcased.gpm.business.serialization.data.AttachedDisplayHint();

            lAttachedDispHint.setDisplayType(lAttachedDispHintEntity.getDisplayType().toString());
            lAttachedDispHint.setWidth(lAttachedDispHintEntity.getWidth());
            lAttachedDispHint.setHeight(lAttachedDispHintEntity.getHeight());
            lAttachedDispHint.setLabelKey(lLabelKey);
            return lAttachedDispHint;
        }
        else if (lDisplayHintEntity instanceof org.topcased.gpm.domain.facilities.DateDisplayHint) {
            org.topcased.gpm.domain.facilities.DateDisplayHint lDateDisplayHintEntity;
            org.topcased.gpm.business.serialization.data.DateDisplayHint lDateDisplayHint;

            lDateDisplayHintEntity =
                    (org.topcased.gpm.domain.facilities.DateDisplayHint) lDisplayHintEntity;
            lDateDisplayHint =
                    new org.topcased.gpm.business.serialization.data.DateDisplayHint();

            boolean lTimeIncluded = false;
            if (lDateDisplayHintEntity.getIncludeTime() != null) {
                lTimeIncluded = lDateDisplayHintEntity.getIncludeTime();
            }
            lDateDisplayHint.setFormat(lDateDisplayHintEntity.getFormat().toString());
            lDateDisplayHint.setIncludeTime(lTimeIncluded);
            lDateDisplayHint.setLabelKey(lLabelKey);
            return lDateDisplayHint;
        }
        else if (lDisplayHintEntity instanceof org.topcased.gpm.domain.facilities.DisplayHint) {
            org.topcased.gpm.domain.facilities.DisplayHint lNewDisplayHintEntity;

            lNewDisplayHintEntity =
                    (org.topcased.gpm.domain.facilities.DisplayHint) lDisplayHintEntity;
            String lDisplayType = lNewDisplayHintEntity.getType();

            // Handle ChoiceStringDisplayHint
            if (lDisplayType.equals(ChoiceStringDisplayHint.getHintType())) {
                AttributesService lAttrsService =
                        ServiceLocator.instance().getAttributesService();
                AttributeData[] lAttrsData =
                        lAttrsService.get(
                                pField.getId(),
                                new String[] { ChoiceStringDisplayHint.getHintAttributesName() });
                if (lAttrsData[0] == null
                        || lAttrsData[0].getValues().length == 0) {
                    // If no attribute present, let assume the ChoiceString
                    // display hint is not
                    // set.
                    return null;
                }

                String lExtensionPointName = lAttrsData[0].getValues()[0];
                boolean lStrict = false;
                if (lAttrsData[0].getValues().length > 1) {
                    lStrict =
                            Boolean.parseBoolean(lAttrsData[0].getValues()[1]);
                }
                ChoiceStringDisplayHint lChoiceStringDisplayHint =
                        new org.topcased.gpm.business.serialization.data.ChoiceStringDisplayHint(
                                lExtensionPointName, lStrict);
                lChoiceStringDisplayHint.setLabelKey(lLabelKey);
                return lChoiceStringDisplayHint;
            }
            // else
            org.topcased.gpm.business.serialization.data.ExternDisplayHint lExternDisplayHint;
            lExternDisplayHint =
                    new org.topcased.gpm.business.serialization.data.ExternDisplayHint();

            lExternDisplayHint.setType(lNewDisplayHintEntity.getType());
            lExternDisplayHint.setLabelKey(lLabelKey);
            return lExternDisplayHint;
        }
        else if (lDisplayHintEntity instanceof org.topcased.gpm.domain.facilities.GridDisplayHint) {
            // convert entity to serialization data
            org.topcased.gpm.domain.facilities.GridDisplayHint lGridDisplayHint =
                    (org.topcased.gpm.domain.facilities.GridDisplayHint) lDisplayHintEntity;

            GridDisplayHint lGridDisplayHintSerializationData =
                    GridObjectsUtil.createGridDisplayHintSerializationData(lGridDisplayHint);
            lGridDisplayHintSerializationData.setLabelKey(lLabelKey);
            return lGridDisplayHintSerializationData;
        }
        else if (lDisplayHintEntity instanceof org.topcased.gpm.domain.facilities.ChoiceTreeDisplayHint) {
            org.topcased.gpm.domain.facilities.ChoiceTreeDisplayHint lChoiceTreeDisplayHintEntity;
            org.topcased.gpm.business.serialization.data.ChoiceTreeDisplayHint lChoiceTreeDisplayHint;

            lChoiceTreeDisplayHintEntity =
                    (org.topcased.gpm.domain.facilities.ChoiceTreeDisplayHint) lDisplayHintEntity;
            lChoiceTreeDisplayHint =
                    new org.topcased.gpm.business.serialization.data.ChoiceTreeDisplayHint();

            lChoiceTreeDisplayHint.setLabelKey(lLabelKey);
            lChoiceTreeDisplayHint.setSeparator(lChoiceTreeDisplayHintEntity.getSeparator());

            return lChoiceTreeDisplayHint;
        }
        else if (lDisplayHintEntity instanceof org.topcased.gpm.domain.facilities.JAppletDisplayHint) {

            org.topcased.gpm.domain.facilities.JAppletDisplayHint lJAppletDisplayHintEntity;
            org.topcased.gpm.business.serialization.data.JAppletDisplayHint lJAppletDisplayHint;

            lJAppletDisplayHintEntity =
                    (org.topcased.gpm.domain.facilities.JAppletDisplayHint) lDisplayHintEntity;
            List<AppletParameter> lList = null;
            if (lJAppletDisplayHintEntity.getAppletParameters().size() > 0) {
                lList =
                        new ArrayList<AppletParameter>(
                                lJAppletDisplayHintEntity.getAppletParameters().size());
                Iterator<org.topcased.gpm.domain.facilities.AppletParameter> lAppletParameter =
                        lJAppletDisplayHintEntity.getAppletParameters().iterator();
                while (lAppletParameter.hasNext()) {
                    lList.add(new AppletParameter(
                            lAppletParameter.next().getParameterName()));
                }
            }
            lJAppletDisplayHint =
                    new org.topcased.gpm.business.serialization.data.JAppletDisplayHint(
                            lJAppletDisplayHintEntity.getAppletCode(),
                            lJAppletDisplayHintEntity.getAppletCodeBase(),
                            lJAppletDisplayHintEntity.getAppletAlter(),
                            lJAppletDisplayHintEntity.getAppletName(),
                            lJAppletDisplayHintEntity.getAppletArchive(), lList);

            lJAppletDisplayHint.setLabelKey(lLabelKey);
            return lJAppletDisplayHint;
        }
        else {
            throw new GDMException("Unsupported display hint type:"
                    + lDisplayHintEntity.getClass().getName());
        }
    }

    /**
     * Get container name.
     * 
     * @return Container name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the description of this object.
     * 
     * @return Translated value of description if exists.<br />
     *         The description non translated value otherwise.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the description of this object.
     * 
     * @param pDescription
     *            New description text
     */
    public void setDescription(String pDescription) {
        description = pDescription;
    }

    /**
     * Get the business process name.
     * 
     * @return Business process name
     */
    public String getBusinessProcessName() {
        return businessProcessName;
    }

    /**
     * set businessProcessName.
     * 
     * @param pBusinessProcessName
     *            the businessProcessName to set
     */
    public void setBusinessProcessName(String pBusinessProcessName) {
        businessProcessName = pBusinessProcessName;
    }

    /**
     * Sets the display groups.
     * 
     * @param pDisplayGroupsEntities
     *            the new display groups
     */
    public void setDisplayGroups(
            List<? extends org.topcased.gpm.domain.facilities.DisplayGroup> pDisplayGroupsEntities) {

        if (pDisplayGroupsEntities.size() != 0) {
            displayGroupsMap = new LinkedHashMap<String, DisplayGroup>();
            for (org.topcased.gpm.domain.facilities.DisplayGroup lDisplayGroupEntity : pDisplayGroupsEntities) {
                org.topcased.gpm.business.serialization.data.DisplayGroup lDisplayGroup;
                lDisplayGroup =
                        new org.topcased.gpm.business.serialization.data.DisplayGroup();
                lDisplayGroup.setName(lDisplayGroupEntity.getName());
                lDisplayGroup.setOpened(lDisplayGroupEntity.isOpened());
                lDisplayGroup.setDescription(null);

                List<FieldRef> lFieldsRef = new ArrayList<FieldRef>();
                for (org.topcased.gpm.domain.fields.Field lFieldEntity : lDisplayGroupEntity.getFields()) {
                    lFieldsRef.add(new FieldRef(lFieldEntity.getLabelKey()));
                }
                lDisplayGroup.setFields(lFieldsRef);

                displayGroupsMap.put(lDisplayGroup.getName(), lDisplayGroup);
            }
        }
    }

    /**
     * set displayHintsMap.
     * 
     * @param pDisplayHintsMap
     *            the displayHintsMap to set
     */
    public void setDisplayHintsMap(
            Map<String, org.topcased.gpm.business.serialization.data.DisplayHint> pDisplayHintsMap) {
        displayHintsMap = pDisplayHintsMap;
    }

    /**
     * Set displayGroupsMap.
     * 
     * @param pDisplayGroupsMap
     *            the display groups map
     */
    public void setDisplayGroupsMap(
            Map<String, org.topcased.gpm.business.serialization.data.DisplayGroup> pDisplayGroupsMap) {
        displayGroupsMap = pDisplayGroupsMap;
    }

    /**
     * set fieldsIdMap.
     * 
     * @return the fieldsIdMap
     */
    public Map<String, org.topcased.gpm.business.serialization.data.Field> getFieldsIdMap() {
        return fieldsIdMap;
    }

    /**
     * set fieldsIdMap.
     * 
     * @param pFieldsIdMap
     *            the fieldsIdMap to set
     */
    public void setFieldsIdMap(
            Map<String, org.topcased.gpm.business.serialization.data.Field> pFieldsIdMap) {
        fieldsIdMap = pFieldsIdMap;
    }

    /**
     * set fieldsKeyMap.
     * 
     * @param pFieldsKeyMap
     *            the fieldsKeyMap to set
     */
    public void setFieldsKeyMap(
            Map<String, org.topcased.gpm.business.serialization.data.Field> pFieldsKeyMap) {
        fieldsKeyMap = pFieldsKeyMap;
    }

    /**
     * set name.
     * 
     * @param pName
     *            the name to set
     */
    public void setName(String pName) {
        name = pName;
    }

    public Boolean getCreatable() {
        return creatable;
    }

    public void setCreatable(Boolean pCreatable) {
        this.creatable = pCreatable;
    }

    public Boolean getUpdatable() {
        return updatable;
    }

    public void setUpdatable(Boolean pUpdatable) {
        this.updatable = pUpdatable;
    }

    public Boolean getConfidential() {
        return confidential;
    }

    public void setConfidential(Boolean pConfidential) {
        this.confidential = pConfidential;
    }

    public Boolean getDeletable() {
        return deletable;
    }

    public void setDeletable(Boolean pDeletable) {
        this.deletable = pDeletable;
    }

    /**
     * Marshal.
     * 
     * @param pSerializedContainer
     *            the serialized container
     */
    public void marshal(
            org.topcased.gpm.business.serialization.data.FieldsContainer pSerializedContainer) {
        super.marshal(pSerializedContainer);

        pSerializedContainer.setName(getName());
        pSerializedContainer.setDescription(getDescription());
        pSerializedContainer.addFields(getFields());
        pSerializedContainer.addDisplayHints(getDisplayHints());
        pSerializedContainer.addDisplayGroups(getDisplayGroups());
        pSerializedContainer.setConfidential(getConfidential());
        pSerializedContainer.setCreatable(getCreatable());
        pSerializedContainer.setDeletable(getDeletable());
        pSerializedContainer.setUpdatable(getUpdatable());
    }

    /**
     * get displayHintsMap
     * 
     * @return the displayHintsMap
     */
    public Map<String, DisplayHint> getDisplayHintsMap() {
        return displayHintsMap;
    }

    /**
     * get topLevelFields
     * 
     * @return the topLevelFields
     */
    public Set<Field> getTopLevelFields() {
        return topLevelFields;
    }

    /**
     * set topLevelFields
     * 
     * @param pTopLevelFields
     *            the new topLevelFields
     */
    public void setTopLevelFields(Set<Field> pTopLevelFields) {
        topLevelFields = pTopLevelFields;
    }

    public boolean isAutolocking() {
        return autolocking;
    }

    public void setAutolocking(boolean pAutolocking) {
        autolocking = pAutolocking;
    }

    public LockTypeEnumeration getLockType() {
        return lockType;
    }

    public void setLockType(LockTypeEnumeration pLockType) {
        lockType = pLockType;
    }

    /**
     * Internal comparator used to sort Field objects according to their label.
     */
    private final static class FieldNameComparator implements
            Comparator<Field>, Serializable {
        /** serialVersionUID */
        private static final long serialVersionUID = 5410406940500657331L;

        /**
         * {@inheritDoc}
         * 
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        public int compare(Field pField1, Field pField2) {
            return pField1.getLabelKey().compareTo(pField2.getLabelKey());
        }
    }
}
