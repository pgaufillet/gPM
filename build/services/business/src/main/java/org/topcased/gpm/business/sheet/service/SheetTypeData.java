/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 ******************************************************************/
package org.topcased.gpm.business.sheet.service;

import org.topcased.gpm.business.sheet.impl.CacheableSheetType;

/**
 * Old sheet type data structure
 * 
 * @author generated
 * @deprecated
 * @since 1.8.3
 * @see CacheableSheetType
 */
public class SheetTypeData implements java.io.Serializable {
    private static final long serialVersionUID = -8707429115480135347L;

    /**
     * Default constructor.
     */
    public SheetTypeData() {
    }

    /**
     * Constructor without default values.
     */
    public SheetTypeData(final String pId, final String pName,
            final String pDescription) {
        id = pId;
        name = pName;
        description = pDescription;
    }

    /**
     * Constructor taking all properties.
     */
    public SheetTypeData(final String pId, final String pName,
            final String pDescription, final boolean pSelectable) {
        id = pId;
        name = pName;
        description = pDescription;
        selectable = pSelectable;
    }

    /**
     * Copies constructor from other SheetTypeData
     */
    public SheetTypeData(final SheetTypeData pOtherBean) {
        if (pOtherBean != null) {
            id = pOtherBean.getId();
            name = pOtherBean.getName();
            description = pOtherBean.getDescription();
            selectable = pOtherBean.isSelectable();
        }
    }

    private java.lang.String id;

    /**
     * 
     */
    public String getId() {
        return id;
    }

    public void setId(final String pId) {
        id = pId;
    }

    private String name;

    /**
     * 
     */
    public String getName() {
        return name;
    }

    public void setName(final String pName) {
        name = pName;
    }

    private String description;

    /**
     * <p>
     * Translated value of description if exists. The description non translated
     * value otherwise.
     * </p>
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(final String pDescription) {
        description = pDescription;
    }

    private boolean selectable = true;

    /**
     * 
     */
    public boolean isSelectable() {
        return selectable;
    }

    public void setSelectable(final boolean pSelectable) {
        selectable = pSelectable;
    }
}