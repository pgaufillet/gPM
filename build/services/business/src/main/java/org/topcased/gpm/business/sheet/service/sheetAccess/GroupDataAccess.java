/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available 
 * under the terms of the Lesser Gnu Public License (LGPL)which 
 * accompanies this distribution, and is available 
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.sheet.service.sheetAccess;

import org.topcased.gpm.business.sheet.service.FieldGroupData;

/**
 * Implementation of the GroupData
 * 
 * @author llatil
 */
public class GroupDataAccess extends FieldContainerAccess implements GroupData {

    GroupDataAccess(final FieldGroupData pFieldGroupData) {
        fieldGroupData = pFieldGroupData;
        initFieldMap(fieldGroupData.getMultipleLineFieldDatas());
    }

    /**
     * Get the group name
     * 
     * @return Group name
     */
    public String getName() {
        return fieldGroupData.getLabelKey();
    }

    private FieldGroupData fieldGroupData;
}
