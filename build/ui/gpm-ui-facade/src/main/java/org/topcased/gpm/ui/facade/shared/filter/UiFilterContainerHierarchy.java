/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.shared.filter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.topcased.gpm.ui.facade.shared.filter.field.UiFilterFieldNameType;

/**
 * UiFilterContainerHierarchy
 * 
 * @author nveillet
 */
public class UiFilterContainerHierarchy implements Serializable {
    /** serialVersionUID */
    private static final long serialVersionUID = -7158122977122158244L;

    private List<String[]> children;

    private String containerId;

    private String containerName;

    private String containerTranslatedName;

    private UiFilterFieldNameType containerType;

    /**
     * Create UiFilterContainerHierarchy
     */
    public UiFilterContainerHierarchy() {
        children = new ArrayList<String[]>();
    }

    /**
     * Create UiFilterContainerHierarchy with values
     * 
     * @param pContainerId
     *            the container identifier
     * @param pContainerName
     *            the container name
     * @param pContainerTranslatedName
     *            the container translated name
     * @param pChildren
     *            the children
     * @param pContainerType
     *            the container type
     */
    public UiFilterContainerHierarchy(String pContainerId,
            String pContainerName, String pContainerTranslatedName,
            List<String[]> pChildren, UiFilterFieldNameType pContainerType) {
        containerId = pContainerId;
        containerName = pContainerName;
        containerTranslatedName = pContainerTranslatedName;
        containerType = pContainerType;
        children = pChildren;
    }

    /**
     * get children
     * 
     * @return the children
     */
    public List<String[]> getChildren() {
        return children;
    }

    /**
     * get container identifier
     * 
     * @return the container identifier
     */
    public String getContainerId() {
        return containerId;
    }

    /**
     * get container name
     * 
     * @return the container name
     */
    public String getContainerName() {
        return containerName;
    }

    /**
     * get container translated name
     * 
     * @return the container translated name
     */
    public String getContainerTranslatedName() {
        return containerTranslatedName;
    }

    /**
     * get container type
     * 
     * @return container type
     */
    public UiFilterFieldNameType getContainerType() {
        return containerType;
    }

    /**
     * set children
     * 
     * @param pChildren
     *            the children to set
     */
    public void setChildren(List<String[]> pChildren) {
        children = pChildren;
    }

    /**
     * set container identifier
     * 
     * @param pContainerId
     *            the container identifier to set
     */
    public void setContainerId(String pContainerId) {
        containerId = pContainerId;
    }

    /**
     * set container name
     * 
     * @param pContainerName
     *            the container name to set
     */
    public void setContainerName(String pContainerName) {
        containerName = pContainerName;
    }

    /**
     * set container translated name
     * 
     * @param pContainerTranslatedName
     *            the container translated name to set
     */
    public void setContainerTranslatedName(String pContainerTranslatedName) {
        containerTranslatedName = pContainerTranslatedName;
    }

    /**
     * set container type
     * 
     * @param pContainerType
     *            container type
     */
    public void setContainerType(UiFilterFieldNameType pContainerType) {
        containerType = pContainerType;
    }

}
