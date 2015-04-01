/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien Eballard (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.field;

import java.util.ArrayList;
import java.util.List;

import org.topcased.gpm.business.values.field.BusinessField;
import org.topcased.gpm.business.values.field.multivalued.BusinessMultivaluedField;
import org.topcased.gpm.business.values.field.simple.BusinessChoiceField;
import org.topcased.gpm.ui.component.client.container.field.AbstractGpmField;
import org.topcased.gpm.ui.component.client.container.field.multivalued.AbstractGpmMultivaluedChoiceField;
import org.topcased.gpm.ui.component.client.field.tree.GpmMultivaluedTreeWidget;
import org.topcased.gpm.ui.component.client.field.tree.GpmTreeChoiceFieldItem;

/**
 * GpmMultivaluedTreeChoiceField
 * 
 * @author jeballar
 */
public final class GpmMultivaluedTreeChoiceField extends
        AbstractGpmMultivaluedChoiceField<GpmMultivaluedTreeWidget> {

    /** Static counter that indexes Items to differentiate and compare them */
    private static int staticItemsEvolutiveIndex = 0;

    /**
     * Use this constructor to build a multivalued TreeChoiceField
     */
    public GpmMultivaluedTreeChoiceField() {
        super(new GpmMultivaluedTreeWidget());
    }

    /**
     * Set the root items of the Tree selectable values
     * 
     * @param pValues
     *            the root items to add
     */
    public void addTreeRootValues(List<GpmTreeChoiceFieldItem> pValues) {
        for (GpmTreeChoiceFieldItem lItem : pValues) {
            getWidget().addRootItem(lItem);
            indexRecursive(lItem);
        }
    }

    /**
     * Assign a unique index to every element of the tree
     * 
     * @param pItem
     *            the item to index and recurse
     */
    private void indexRecursive(GpmTreeChoiceFieldItem pItem) {
        pItem.setIndex(staticItemsEvolutiveIndex++);
        for (GpmTreeChoiceFieldItem lItem : pItem.getSubItems()) {
            indexRecursive(lItem);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.field.AbstractGpmField#getEmptyClone()
     */
    @Override
    public AbstractGpmField<GpmMultivaluedTreeWidget> getEmptyClone() {
        GpmMultivaluedTreeChoiceField lField = null;
        if (getWidget() instanceof GpmMultivaluedTreeWidget) {
            lField = new GpmMultivaluedTreeChoiceField();
            initEmptyClone(lField);
            addTreeRootValues(getWidget().getRootItems());
        }
        return lField;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.field.AbstractGpmField#isEnabled()
     */
    @Override
    public boolean isEnabled() {
        return getWidget().isEnabled();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.field.AbstractGpmField#setEnabled(boolean)
     */
    @Override
    public void setEnabled(boolean pEnabled) {
        getWidget().setEnabled(pEnabled);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#copy(org.topcased.gpm.business.values.field.BusinessField)
     */
    @SuppressWarnings("unchecked")
    @Override
    public void copy(BusinessField pOther) {
        if (pOther instanceof BusinessMultivaluedField) {
            BusinessMultivaluedField<BusinessChoiceField> lOther =
                    (BusinessMultivaluedField<BusinessChoiceField>) pOther;
            for (BusinessChoiceField lField : lOther) {
                setWidgetValueSelected(lField.getCategoryValue(), true);
            }
        }
        if (pOther instanceof BusinessChoiceField) {
            for (String lValue : getSelectedValues()) {
                setWidgetValueSelected(lValue, true);
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.field.multivalued.AbstractGpmMultivaluedChoiceField#setWidgetValueSelected(java.lang.String,
     *      boolean)
     */
    @Override
    public void setWidgetValueSelected(String pValue, boolean pSelected) {
        getWidget().setValueSelected(pValue, pSelected);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.field.multivalued.AbstractGpmMultivaluedChoiceField#getSelectedValues()
     */
    @Override
    protected List<String> getSelectedValues() {
        List<String> lValues = new ArrayList<String>();
        for (GpmTreeChoiceFieldItem lItem : getWidget().getSelectedItems()) {
            lValues.add(lItem.getValue());
        }
        return lValues;
    }
}
