/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael KARGBO (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.authorization.impl;

import org.topcased.gpm.business.fieldscontainer.service.FieldsContainerService;

/**
 * FieldAccess
 * <p>
 * Needed to limit methods arguments.
 * </p>
 * 
 * @author mkargbo
 */
class FieldAccess {

    private boolean notConfidential;

    private boolean mandatory;

    private boolean notMandatory;

    private boolean updatable;

    private boolean exportable;

    public FieldAccess() {
    }

    /**
     * Construct a field's access.
     * 
     * @param pNotConfidential
     *            Not confidential
     * @param pMandatory
     *            Mandatory
     * @param pNotMandatory
     *            Not mandatory
     * @param pUpdatable
     *            Updatable
     * @param pExportable
     *            Exportable
     */
    public FieldAccess(boolean pNotConfidential, boolean pMandatory,
            boolean pNotMandatory, boolean pUpdatable, boolean pExportable) {
        notConfidential = pNotConfidential;
        mandatory = pMandatory;
        notMandatory = pNotMandatory;
        updatable = pUpdatable;
        exportable = pExportable;
    }

    public boolean isNotConfidential() {
        return notConfidential;
    }

    public void setNotConfidential(boolean pNotConfidential) {
        notConfidential = pNotConfidential;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean pMandatory) {
        mandatory = pMandatory;
    }

    public boolean isNotMandatory() {
        return notMandatory;
    }

    public void setNotMandatory(boolean pNotMandatory) {
        notMandatory = pNotMandatory;
    }

    public boolean isUpdatable() {
        return updatable;
    }

    public void setUpdatable(boolean pUpdatable) {
        updatable = pUpdatable;
    }

    public boolean isExportable() {
        return exportable;
    }

    public void setExportable(boolean pExportable) {
        exportable = pExportable;
    }

    /**
     * Compute access flag
     * 
     * @param pAccessFlag
     *            Access flag
     * @return Field access object according the flag.
     */
    public static FieldAccess getFieldAccess(final long pAccessFlag) {
        boolean lNotConfidential =
                ((pAccessFlag & FieldsContainerService.FIELD_NOT_CONFIDENTIAL) != 0);
        boolean lMandatory =
                ((pAccessFlag & FieldsContainerService.FIELD_MANDATORY) != 0);
        boolean lUpdate =
                ((pAccessFlag & FieldsContainerService.FIELD_UPDATE) != 0);
        boolean lExport =
                ((pAccessFlag & FieldsContainerService.FIELD_EXPORT) != 0);
        boolean lNotMandatory =
                ((pAccessFlag & FieldsContainerService.FIELD_NOT_MANDATORY) != 0);
        return new FieldAccess(lNotConfidential, lMandatory, lNotMandatory,
                lUpdate, lExport);
    }
}