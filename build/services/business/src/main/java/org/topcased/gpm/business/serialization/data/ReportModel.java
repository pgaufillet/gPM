/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Sophian Idjellidaine (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.serialization.data;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * ReportModel.
 * 
 * @author ahaugomm
 */
@XStreamAlias("reportModel")
public class ReportModel extends NamedElement {

    private static final long serialVersionUID = -3311428242298499444L;

    /** The description. */
    @XStreamAsAttribute
    private String description;

    /** The path. */
    @XStreamAsAttribute
    private String path;

    /** The containers. */
    private List<NamedElement> containers;

    /** The export types. */
    private List<ExportType> exportTypes;

    /**
     * get description.
     * 
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * get path.
     * 
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * get containers.
     * 
     * @return the containers
     */
    public List<NamedElement> getContainers() {
        return containers;
    }

    /**
     * get exportTypes.
     * 
     * @return the exportTypes
     */
    public List<ExportType> getExportTypes() {
        return exportTypes;
    }
}
