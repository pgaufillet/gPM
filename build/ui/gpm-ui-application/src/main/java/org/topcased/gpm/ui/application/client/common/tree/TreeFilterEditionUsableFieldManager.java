/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien LOUISY (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.common.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.topcased.gpm.ui.component.client.tree.GpmDynamicTreeManager;
import org.topcased.gpm.ui.facade.shared.filter.field.UiFilterUsableField;

/**
 * Manager for filter edition container tree.
 * 
 * @author jlouisy
 */
public abstract class TreeFilterEditionUsableFieldManager extends
        GpmDynamicTreeManager<TreeFilterEditionUsableFieldItem> {

    /** Tree mode. */
    public static enum TreeFilterEditionMode {
        SELECT_RESULT_MODE, SELECT_CRITERIA_MODE, SELECT_SORTING_MODE
    };

    private TreeFilterEditionMode mode;

    private Map<String, UiFilterUsableField> usableFields;

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.tree.GpmDynamicTreeManager#createSubItems(org.topcased.gpm.ui.component.client.tree.GpmDynamicTreeItem)
     */
    @Override
    public List<TreeFilterEditionUsableFieldItem> createSubItems(
            final TreeFilterEditionUsableFieldItem pItem) {

        final List<TreeFilterEditionUsableFieldItem> lSubItems =
                new ArrayList<TreeFilterEditionUsableFieldItem>();

        for (UiFilterUsableField lSubField : usableFields.get(
                pItem.getNode().getName()).getSubFields()) {
            lSubItems.add(new TreeFilterEditionUsableFieldItem(lSubField));
        }

        return lSubItems;
    }

    public void setMode(TreeFilterEditionMode pMode) {
        mode = pMode;
    }

    public TreeFilterEditionMode getMode() {
        return mode;
    }

    public void setUsableFields(Map<String, UiFilterUsableField> pUsableFields) {
        usableFields = pUsableFields;
    }

}