/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.serialization.data;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * FilterAccessCtl
 * 
 * @author mkargbo
 */
@XStreamAlias("filterAccess")
public class FilterAccessCtl {

    @XStreamAsAttribute
    private String roleName;

    @XStreamAsAttribute
    private String visibility;

    @XStreamAsAttribute
    private String typeName;

    @XStreamAsAttribute
    private String fieldName;

    @XStreamAsAttribute
    private Boolean executable;

    @XStreamAsAttribute
    private Boolean editable;

    @XStreamImplicit(itemFieldName = "filterAccessConstraintName")
    private List<FilterAccessConstraintName> constraints;

    /**
     * Constructor 
     */
    public FilterAccessCtl() {
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String pRoleName) {
        roleName = pRoleName;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String pVisibility) {
        visibility = pVisibility;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String pTypeName) {
        typeName = pTypeName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String pFieldName) {
        fieldName = pFieldName;
    }

    public Boolean getExecutable() {
        return executable;
    }

    public void setExecutable(Boolean pExecutable) {
        executable = pExecutable;
    }

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean pEditable) {
        editable = pEditable;
    }

    public List<FilterAccessConstraintName> getConstraints() {
        return constraints;
    }

    public void setConstraints(List<FilterAccessConstraintName> pConstraints) {
        constraints = pConstraints;
    }
}
