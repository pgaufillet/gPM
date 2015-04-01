/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.attributes.service;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.topcased.gpm.business.attributes.AttributeData;
import org.topcased.gpm.common.valuesContainer.LockType;

/**
 * Extended attributes management service
 * 
 * @author llatil
 */
@Transactional(propagation = Propagation.REQUIRED)
public interface AttributesService {
    /** Global attribute authentication (external or internal) */
    public static String AUTHENTICATION = "authentication";

    /** One of the possible values for global attribute AUTHENTICATION */
    public static String INTERNAL_AUTHENTICATION = "internal";

    /** One of the possible values for global attribute AUTHENTICATION */
    public static String EXTERNAL_AUTHENTICATION = "external";

    /**
     * Global attribute for the user ID parameter name (coming from external
     * authentication system)
     */
    public static String USER_ID_PARAMETER_NAME = "userIdParameterName";

    /** Global attribute for the url associated to button "contact" in gui. */
    public static String CONTACT_URL = "contactUrl";

    /** Global attribute defining an URL displayed on menu Help/Help Contents */
    public static String HELP_CONTENT_URL = "helpContentUrl";

    /**
     * Global attribute for the maximum depth of the usable field for the filter
     * criteria and summary fields.
     */
    public static final String FILTER_FIELDS_MAX_DEPTH = "filterFieldsMaxDepth";

    /**
     * The default value for the maximum depth of the usable field is 1.
     */
    public static final String FILTER_FIELDS_MAX_DEPTH_DEFAULT_VALUE = "1";

    /**
     * Global attribute for the maximum exportable sheets.
     */
    public static final String MAX_EXPORTABLE_SHEETS = "maxExportableSheets";

    /**
     * Global attribute for autolocking fields container.
     */
    public static final String AUTOLOCKING = "autolocking";

    /**
     * Global attribute for autolocking fields container default value.
     */
    public static final String AUTOLOCKING_DEFAULT = LockType.WRITE.getValue();

    /**
     * Global attribute for the login case sensitive.
     */
    public static final String LOGIN_CASE_SENSITIVE = "loginCaseSensitive";

    public static final Boolean LOGIN_CASE_SENSITIVE_DEFAULT = Boolean.TRUE;

    /**
     * Define a list of attributes to define or modify for an element.
     * <p>
     * The attributes to set are defined in the AttributeData[], using one array
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
     */
    void set(String pElemId, AttributeData[] pAttrs);

    /**
     * Get some attributes of an element.
     * <p>
     * This method is used to retrieve several attributes defined on an element.
     * Result is returned as an array containing whose size is the same as the
     * attribute names array passed as parameter. In the result array, each
     * element is either a valid AttributeData, or null if the corresponding
     * attribute name is not defined.
     * 
     * @param pElemId
     *            Unique identifier of the attributes container.
     * @param pAttrNames
     *            Names of the attributes to retrieve.
     * @return Array containing the attributes. Attributes data array keeps the
     *         same ordering as pAttrNames parameter. Undefined attributes are
     *         represented by a null value in this array.
     */
    AttributeData[] get(String pElemId, String[] pAttrNames);

    /**
     * Get all attributes defined for an element.
     * 
     * @param pElemId
     *            Identifier of the attributes container.
     * @return Array containing the attributes.
     */
    AttributeData[] getAll(String pElemId);

    /**
     * Remove all attributes defined on an element.
     * 
     * @param pElemId
     *            Identifier of the attributes container.
     */
    void removeAll(String pElemId);

    /**
     * Get the names of all extended attributes defined for an element.
     * 
     * @param pElemId
     *            Identifier of the attributes container.
     * @return Array containing the names of all attributes (sorted
     *         alphabetically).
     */
    String[] getAttrNames(String pElemId);

    /**
     * Get the list of global attribute names
     * 
     * @return a list of global attribute names
     */
    public String[] getGlobalAttrNames();

    /**
     * Get a list of global attributes data from their names
     * 
     * @param pAttrNames
     *            the attribute names
     * @return A list of AttributeData objects
     * @see AttributesService#get(String, String[])
     */
    public AttributeData[] getGlobalAttributes(String[] pAttrNames);

    /**
     * Set a list of global attributes
     * 
     * @param pRoleToken
     *            the role token
     * @param pAttributesData
     *            the list of AttributeData
     */
    public void setGlobalAttributes(String pRoleToken,
            AttributeData[] pAttributesData);

    /**
     * Increment atomically an attribute value.
     * <p>
     * This method ensures that the attribute value is changed atomically
     * (thread-safe), and immediately committed in the database.
     * <p>
     * The attribute value (string) is assumed to be formatted as an integer
     * value.
     * 
     * @param pRoleToken
     *            Role session token.
     * @param pElemId
     *            Identifier of the attributes container.
     * @param pAttrName
     *            Name of the attribute to increment.
     * @param pInitial
     *            Initial value to set for the attribute. This value is set if
     *            the attribute does not exist yet, or is not formatted as an
     *            integer.
     * @return Incremented value of the attribute.
     */
    public long atomicIncrement(String pRoleToken, String pElemId,
            String pAttrName, long pInitial);

    /**
     * Set an attribute value if the current value equals to a given value
     * <p>
     * This method ensures that the attribute value is changed atomically
     * (thread-safe), and immediately committed in the database.
     * <p>
     * 
     * @param pRoleToken
     *            Role session token.
     * @param pElementId
     *            Identifier of the attributes container.
     * @param pAttributeName
     *            Name of the attribute to increment.
     * @param pOldValue
     *            The old value tested with the current value
     * @param pNewValue
     *            The new value of the attribute
     * @return The value of the attribute before modification (equals pOldValue
     *         if value has been modified)
     */
    public String atomicTestAndSet(String pRoleToken, String pElementId,
            String pAttributeName, String pOldValue, String pNewValue);

    /**
     * Update attributes of an element.
     * 
     * @param pElemId
     *            Identifier of the attributes container
     * @param pAttrs
     *            Attributes to define
     */
    public void update(String pElemId, AttributeData[] pAttrs);
}