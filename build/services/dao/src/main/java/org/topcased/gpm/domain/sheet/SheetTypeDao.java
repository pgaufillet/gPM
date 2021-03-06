/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 ******************************************************************/
//
// Attention: Generated code! Do not modify by hand!
// Generated by: SpringDao.vsl in andromda-spring-cartridge.
//
package org.topcased.gpm.domain.sheet;

import org.topcased.gpm.domain.process.ProcessDefinition;

/**
 * @see org.topcased.gpm.domain.sheet.SheetType
 * @author Atos
 */
public interface SheetTypeDao
        extends
        org.topcased.gpm.domain.IDao<org.topcased.gpm.domain.sheet.SheetType, java.lang.String> {
    /**
     * <p>
     * Returns the sheet type associated with the sheet type name.
     * </p>
     */
    public org.topcased.gpm.domain.sheet.SheetType getSheetType(
            org.topcased.gpm.domain.businessProcess.BusinessProcess pBusinessProcess,
            java.lang.String pName);

    /**
     * 
     */
    public org.topcased.gpm.domain.process.Node getNode(
            java.lang.String pSheetTypeId, java.lang.String pStateName);

    /**
     * <p>
     * Get the list of sheet types defined for a business process.
     * </p>
     */
    public java.util.List<SheetType> getSheetTypes(
            org.topcased.gpm.domain.businessProcess.BusinessProcess pBusinessProcess);

    /**
     * Retrieves the SheetType ids
     * 
     * @param pBusinessProcName
     * @return java.util.List<String>
     */
    public java.util.List<String> getSheetTypesId(
            java.lang.String pBusinessProcName);

    /**
     * Retrieves sheet Type names
     * 
     * @param pBusinessProcName
     * @return java.util.List<String>
     */
    public java.util.List<String> getSheetTypeNames(
            java.lang.String pBusinessProcName);

    /**
     * Retrieves the Process Definition entity, sheet type id given
     * 
     * @param pSheetTypeId
     * @return process definition entity or null
     */
    public ProcessDefinition getProcessDefinitionBySheetTypeId(
            final String pSheetTypeId);

    /**
     * Checks if all container are instance of sheet Type, ids given.
     * 
     * @param pIds
     *            array of container id
     * @return true if the container ids are sheet type ids and false if one of
     *         the ids is not a sheet type id
     */
    public boolean checkSheetTypeIds(String[] pIds);

}