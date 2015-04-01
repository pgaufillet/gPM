/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.shared.filter.field.sort;

import java.io.Serializable;
import java.util.LinkedList;

import org.topcased.gpm.business.util.FieldType;
import org.topcased.gpm.ui.facade.shared.filter.field.UiFilterFieldName;

/**
 * UiFilterSortingField
 * 
 * @author nveillet
 */
public class UiFilterSortingField implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = -726888047122620533L;

    private FieldType fieldType;

    private String id;

    private LinkedList<UiFilterFieldName> name;

    private UiFilterSorting order;

    private boolean virtualField;

    /**
     * get virtualField
     * 
     * @return the virtualField
     */
    public boolean isVirtualField() {
        return virtualField;
    }

    /**
     * set virtualField
     * 
     * @param pVirtualField
     *            the virtualField to set
     */
    public void setVirtualField(boolean pVirtualField) {
        virtualField = pVirtualField;
    }

    /**
     * Constructor
     */
    public UiFilterSortingField() {
    }

    /**
     * Constructor with values
     * 
     * @param pId
     *            the identifier
     * @param pList
     *            the name
     * @param pOrder
     *            the order
     * @param pFieldType
     *            the field type
     * @param pVirtualField
     *            is virtual field
     */
    public UiFilterSortingField(String pId,
            LinkedList<UiFilterFieldName> pList, UiFilterSorting pOrder,
            FieldType pFieldType, boolean pVirtualField) {
        id = pId;
        name = pList;
        order = pOrder;
        fieldType = pFieldType;
        virtualField = pVirtualField;
    }

    /**
     * get field type
     * 
     * @return the field type
     */
    public FieldType getFieldType() {
        return fieldType;
    }

    /**
     * get identifier
     * 
     * @return the identifier
     */
    public String getId() {
        return id;
    }

    /**
     * get name
     * 
     * @return the name
     */
    public LinkedList<UiFilterFieldName> getName() {
        return name;
    }

    /**
     * get order
     * 
     * @return the order
     */
    public UiFilterSorting getOrder() {
        return order;
    }

    /**
     * set field type
     * 
     * @param pFieldType
     *            field type
     */
    public void setFieldType(FieldType pFieldType) {
        fieldType = pFieldType;
    }

    /**
     * set identifier
     * 
     * @param pId
     *            the identifier to set
     */
    public void setId(String pId) {
        id = pId;
    }

    /**
     * set name
     * 
     * @param pName
     *            the name to set
     */
    public void setName(LinkedList<UiFilterFieldName> pName) {
        name = pName;
    }

    /**
     * set order
     * 
     * @param pOrder
     *            the oder to set
     */
    public void setOrder(UiFilterSorting pOrder) {
        order = pOrder;
    }
}
