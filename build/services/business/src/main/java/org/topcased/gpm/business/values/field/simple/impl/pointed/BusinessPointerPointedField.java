/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.values.field.simple.impl.pointed;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.ServiceLocator;
import org.topcased.gpm.business.exception.MethodNotImplementedException;
import org.topcased.gpm.business.fields.service.FieldsService;
import org.topcased.gpm.business.values.field.BusinessField;
import org.topcased.gpm.business.values.field.impl.pointed.AbstractBusinessPointedField;
import org.topcased.gpm.business.values.field.simple.BusinessPointerField;

/**
 * BusinessPointerPointedField
 * 
 * @author nveillet
 */
public class BusinessPointerPointedField extends AbstractBusinessPointedField
        implements BusinessPointerField {

    final private String pointedContainerId;

    private BusinessField pointedField;

    final private String pointedFieldName;

    final private String roleToken;

    /**
     * Constructor
     * 
     * @param pRoleToken
     *            the user role token
     * @param pFieldName
     *            The field name
     * @param pFieldDescription
     *            The field description
     * @param pPointedContainerId
     *            The pointed values container identifier
     * @param pPointedFieldName
     *            The pointed field name
     */
    public BusinessPointerPointedField(String pFieldName,
            String pFieldDescription, String pRoleToken,
            String pPointedContainerId, String pPointedFieldName) {
        super(pFieldName, pFieldDescription);
        roleToken = pRoleToken;
        pointedContainerId = pPointedContainerId;
        pointedFieldName = pPointedFieldName;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.impl.pointed.AbstractBusinessPointedField#getAsString()
     */
    @Override
    public String getAsString() {
        BusinessField lPointedField = getPointedField();

        if (lPointedField != null) {
            return lPointedField.getAsString();
        }

        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessPointerField#getPointedContainerId()
     */
    public String getPointedContainerId() {
        return pointedContainerId;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessPointerField#getPointedField()
     */
    public BusinessField getPointedField() {
        if (pointedField == null) {
            FieldsService lFieldsService =
                    ServiceLocator.instance().getFieldsService();
            pointedField =
                    lFieldsService.getPointedField(roleToken, getFieldName(),
                            getPointedContainerId(), getPointedFieldName());
        }
        return pointedField;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessPointerField#getPointedFieldName()
     */
    public String getPointedFieldName() {
        return pointedFieldName;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#hasSameValues(org.topcased.gpm.business.values.field.BusinessField)
     */
    public boolean hasSameValues(BusinessField pOther) {
        final BusinessPointerField lOtherPointer =
                (BusinessPointerField) pOther;
        String lPointedContainerId1 = pointedContainerId;
        if (lPointedContainerId1 == null) {
            lPointedContainerId1 = StringUtils.EMPTY;
        }
        String lPointedContainerId2 = lOtherPointer.getPointedContainerId();
        if (lPointedContainerId2 == null) {
            lPointedContainerId2 = StringUtils.EMPTY;
        }

        String lPointedFieldName1 = pointedFieldName;
        if (lPointedFieldName1 == null) {
            lPointedFieldName1 = StringUtils.EMPTY;
        }
        String lPointedFieldName2 = lOtherPointer.getPointedFieldName();
        if (lPointedFieldName2 == null) {
            lPointedFieldName2 = StringUtils.EMPTY;
        }

        return lPointedContainerId1.equals(lPointedContainerId2)
                && lPointedFieldName1.equals(lPointedFieldName2);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessPointerField#setPointedContainerId(java.lang.String)
     */
    public void setPointedContainerId(String pPointedContainerId) {
        throw new MethodNotImplementedException("setPointedContainerId");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessPointerField#setPointedFieldName(java.lang.String)
     */
    public void setPointedFieldName(String pPointedContainerId) {
        throw new MethodNotImplementedException("setPointedFieldName");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.impl.pointed.AbstractBusinessPointedField#toString()
     */
    @Override
    public String toString() {
        return getPointedField().getAsString();
    }

}
