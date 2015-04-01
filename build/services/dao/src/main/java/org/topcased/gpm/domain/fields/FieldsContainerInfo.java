/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.domain.fields;

/**
 * The Class FieldsContainerInfo.
 * 
 * @author llatil
 */
public class FieldsContainerInfo {

    /** The name. */
    private String name;

    /** The description. */
    private String description;

    /** The id. */
    private String id;

    /**
     * Constructs a new FieldContainerValueInfo.
     * 
     * @param pId
     *            Identifier of the fields container
     * @param pName
     *            Name
     * @param pDescription
     *            Description
     */
    public FieldsContainerInfo(String pId, String pName, String pDescription) {
        id = pId;
        description = pDescription;
        name = pName;
    }

    /**
     * Gets the name.
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the description.
     * 
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the id.
     * 
     * @return the id
     */
    public String getId() {
        return id;
    }

}
