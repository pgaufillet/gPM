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

import java.util.List;

import org.topcased.gpm.business.values.field.BusinessField;
import org.topcased.gpm.business.values.field.simple.BusinessChoiceField;
import org.topcased.gpm.ui.component.client.container.field.AbstractGpmField;
import org.topcased.gpm.ui.component.client.field.tree.GpmMonovaluedTreeWidget;
import org.topcased.gpm.ui.component.client.field.tree.GpmTreeChoiceFieldItem;

/**
 * GpmTreeChoiceField.
 * 
 * @author jeballar
 */
public final class GpmTreeChoiceField extends
        AbstractGpmField<GpmMonovaluedTreeWidget> implements
        BusinessChoiceField {

    /** Static counter that indexes Items to differentiate them */
    private static int staticItemsEvolutiveIndex = 0;

    /**
     * Use this constructor to build a multivalued TreeChoiceField
     * 
     * @param pEnabled
     *            Indicate if the widget is editable by the user or not (for
     *            edition or display use)
     */
    public GpmTreeChoiceField(boolean pEnabled) {
        super(new GpmMonovaluedTreeWidget(pEnabled));
    }

    /**
     * Add root items to the Tree selectable values
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
    public AbstractGpmField<GpmMonovaluedTreeWidget> getEmptyClone() {
        GpmTreeChoiceField lField = null;

        if (getWidget() instanceof GpmMonovaluedTreeWidget) {
            lField = new GpmTreeChoiceField(getWidget().isEnabled());
            initEmptyClone(lField);
            lField.addTreeRootValues(getWidget().getRootItems());
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
    @Override
    public void copy(BusinessField pOther) {
        if (pOther instanceof BusinessChoiceField) {
            setCategoryValue(((BusinessChoiceField) pOther).getCategoryValue());
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#getAsString()
     */
    @Override
    public String getAsString() {
        return getCategoryValue();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#hasSameValues(org.topcased.gpm.business.values.field.BusinessField)
     */
    @Override
    public boolean hasSameValues(BusinessField pOther) {
        if (pOther instanceof BusinessChoiceField) {
            return ((BusinessChoiceField) pOther).getCategoryValue().equals(
                    getCategoryValue());
        }
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#isUpdatable()
     */
    @Override
    public boolean isUpdatable() {
        return true;
    }

    @Override
    public String getCategoryValue() {
        return getWidget().getValue();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessChoiceField#setCategoryValue(java.lang.String)
     */
    @Override
    public void setCategoryValue(String pValue) {
        getWidget().selectValue(pValue);
    }
}
