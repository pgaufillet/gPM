/***************************************************************
 * Copyright (c) 2013 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Romain Ranzato (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.exportation.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.topcased.gpm.business.serialization.data.CategoryValue;
import org.topcased.gpm.business.serialization.data.NamedElement;

public abstract class AbstractNamedElementExportManager<S> extends
        AbstractExportManager<NamedElement> {

    public AbstractNamedElementExportManager(String pNodeName) {
        super(pNodeName);
    }

    /**
     * Obfuscate dictionary values
     * 
     * @param pCategoryValue
     *            the value to obfuscate
     * @return the List of obfuscated CategoryValues
     */
    protected List<CategoryValue> obfuscateElement(String pCategoryValue) {
        final List<CategoryValue> lList = new ArrayList<CategoryValue>();
        if (pCategoryValue != null && !pCategoryValue.isEmpty()) {

            final Set<Entry<String, String>> lEntrySet =
                    ExportationData.getInstance().getProductNames().entrySet();

            // Obfuscate dictionary products name
            String lFinalCategoryValue = pCategoryValue;
            for (Map.Entry<String, String> lEntry : lEntrySet) {
                final String lEntryKey = lEntry.getKey();
                if (pCategoryValue.contains(lEntryKey)) {
                    final String lEntryValue = lEntry.getValue();
                    lFinalCategoryValue =
                            lFinalCategoryValue.replaceAll(lEntryKey,
                                    lEntryValue);
                }
            }
            lList.add(new CategoryValue(lFinalCategoryValue));

            // Add dictionary values to the map, these data will not be obfuscated in sheets
            ExportationData.getInstance().getCategoryValue().add(pCategoryValue);

        }
        return lList;
    }

}
