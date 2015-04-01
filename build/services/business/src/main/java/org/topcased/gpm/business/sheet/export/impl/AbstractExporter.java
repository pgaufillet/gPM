/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Szadel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.sheet.export.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.topcased.gpm.business.ServiceLocator;
import org.topcased.gpm.business.authorization.service.AccessControlContextData;
import org.topcased.gpm.business.authorization.service.AuthorizationService;
import org.topcased.gpm.business.i18n.service.I18nService;
import org.topcased.gpm.business.serialization.data.Field;
import org.topcased.gpm.business.serialization.data.SimpleField;
import org.topcased.gpm.business.sheet.impl.CacheableSheet;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.business.sheet.service.SheetData;

/**
 * Abstract Class for all exporters.
 * 
 * @author tszadel
 */
public abstract class AbstractExporter {

    /** The field type for date. */
    protected static final String DATE_VALUE_TYPE = "DATE";

    /** The field type for date. */
    protected static final String DATE_TYPE = "Date";

    /** The separator for different fields of a multiple field. */
    protected static final String MULTIPLE_SEPARATOR = "::";

    /** The warning message when filter limit is reached. */
    protected static final String LIMIT_WARNING =
            "WARNING: this generated file doesn't contain "
                    + "all filter results because filter limit was reached";

    /** The I18n Service. */
    protected I18nService i18nService =
            ServiceLocator.instance().getI18nService();

    protected AuthorizationService authorizationService =
            ServiceLocator.instance().getAuthorizationService();

    /**
     * Export sheets into single format.
     * 
     * @param pRoleToken
     *            Role token
     * @param pSheetDatas
     *            The sheets.
     * @return The content.
     * @throws IOException
     *             Error.
     * @deprecated Since 1.7
     * @see #exportSheets(String, OutputStream, List)
     */
    public abstract byte[] exportSheets(String pRoleToken,
            List<SheetData> pSheetDatas) throws IOException;

    /**
     * Export sheets into single format.
     * 
     * @param pRoleToken
     *            Role token
     * @param pOutputStream
     *            The outputstream to be write
     * @param pSheetIds
     *            The sheets ids
     * @throws IOException
     *             Error.
     */
    public abstract void exportSheets(String pRoleToken,
            OutputStream pOutputStream, List<String> pSheetIds)
        throws IOException;

    /**
     * Export sheets into single format.
     * 
     * @param pRoleToken
     *            Role token
     * @param pOutputStream
     *            The outputstream to be write
     * @param pSheetIds
     *            The sheets ids
     * @param pExportedFieldsLabel
     *            The exported fields label
     * @throws IOException
     *             Error.
     */
    public abstract void exportSheets(String pRoleToken,
            OutputStream pOutputStream, List<String> pSheetIds,
            List<String> pExportedFieldsLabel) throws IOException;

    /**
     * Get the field value
     * 
     * @param pCacheableSheetType
     *            Cacheable sheet type
     * @param pFieldValueData
     *            field value data
     * @return the field value
     */
    protected String getFieldValue(
            CacheableSheetType pCacheableSheetType,
            org.topcased.gpm.business.serialization.data.FieldValueData pFieldValueData) {
        String lValue = pFieldValueData.getValue();
        if (lValue != null) {
            final Field lField =
                    pCacheableSheetType.getFieldFromLabel(pFieldValueData.getName());
            if (lField instanceof SimpleField) {
                SimpleField lSimpleField = (SimpleField) lField;
                if (lSimpleField.getValueType().compareTo(DATE_VALUE_TYPE) == 0) {
                    lValue = getDateValue(lValue);
                }
            }
        }
        return lValue;
    }

    /**
     * Returns a date.
     * 
     * @param pValue
     *            The string value (can be a long or a string representation).
     * @return The date or the value if the date cannot be converted.
     */
    protected String getDateValue(String pValue) {
        Date lDate;
        DateFormat lDateFormat =
                DateFormat.getDateTimeInstance(DateFormat.LONG,
                        DateFormat.MEDIUM);
        try {
            lDate = new Date(Long.parseLong(pValue));
        }
        catch (NumberFormatException e) {
            // Trying a string representation
            try {
                lDate = lDateFormat.parse(pValue);
            }
            catch (ParseException e1) {
                return pValue;
            }
        }
        return lDateFormat.format(lDate.getTime());
    }

    /**
     * Get the list of field value data in the cacheable sheet
     * 
     * @param pCacheableSheet
     *            The cacheable sheet
     * @param pValue
     *            The multiple field name
     * @param pSimpleFieldName
     *            The simple field name
     * @return The list of field value data
     */
    @SuppressWarnings("unchecked")
    protected List<org.topcased.gpm.business.serialization.data.FieldValueData> getFieldValueData(
            CacheableSheet pCacheableSheet, Object pValue,
            String pSimpleFieldName) {
        List<org.topcased.gpm.business.serialization.data.FieldValueData> lListFieldValueData =
                new ArrayList<org.topcased.gpm.business.serialization.data.FieldValueData>();

        if (pValue != null) {
            if (pValue instanceof org.topcased.gpm.business.serialization.data.FieldValueData) {
                lListFieldValueData.add((org.topcased.gpm.business.serialization.data.FieldValueData) pValue);
            }
            else if (pValue instanceof Map) {
                Map<String, ?> lMap = (Map<String, ?>) pValue;
                Object lSimpleVal = lMap.get(pSimpleFieldName);
                List<org.topcased.gpm.business.serialization.data.FieldValueData> lSubResultTab =
                        getFieldValueData(pCacheableSheet, lSimpleVal,
                                pSimpleFieldName);
                for (org.topcased.gpm.business.serialization.data.FieldValueData lFieldValueData : lSubResultTab) {
                    lListFieldValueData.add(lFieldValueData);
                }
            }
            else if (pValue instanceof List) {
                List<?> lList = (List<?>) pValue;
                for (Object lListValue : lList) {
                    List<org.topcased.gpm.business.serialization.data.FieldValueData> lSubResultTab;
                    lSubResultTab =
                            getFieldValueData(pCacheableSheet, lListValue,
                                    pSimpleFieldName);
                    for (org.topcased.gpm.business.serialization.data.FieldValueData lFieldValueData : lSubResultTab) {
                        lListFieldValueData.add(lFieldValueData);
                    }
                }

            }
        }
        return lListFieldValueData;
    }

    /**
     * Transform an usable field data identifier in field identifier.
     * 
     * @param pLabelKey
     *            usable field data identifier
     * @return The field identifier
     */
    public static String retrieveFieldLabelKey(String pLabelKey) {
        String[] labelKeyTab = pLabelKey.split("\\|");
        return labelKeyTab[labelKeyTab.length - 1];
    }

    /**
     * Get access control context for the values container.
     * <p>
     * Set the roleName, productName, typeId and valuesContainerId
     * </p>
     * 
     * @param pRoleToken
     *            Role token
     * @param pElement
     *            Element to test
     * @return True if the type of the element is not confidential, false
     *         otherwise.
     */
    protected AccessControlContextData getAccessControlContext(
            String pRoleToken, CacheableSheet pElement) {
        final AccessControlContextData lAccessControlContext =
                new AccessControlContextData();
        String lRoleName =
                authorizationService.getRoleNameFromToken(pRoleToken);
        lAccessControlContext.setRoleName(lRoleName);
        lAccessControlContext.setProductName(pElement.getProductName());
        lAccessControlContext.setContainerTypeId(pElement.getTypeId());
        lAccessControlContext.setValuesContainerId(pElement.getId());
        return lAccessControlContext;
    }
}
