/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien LOUISY, Olivier JUIN (Atos)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.container.field;

import java.util.List;

import org.topcased.gpm.business.values.field.BusinessField;

import com.google.gwt.user.client.ui.DeckPanel;

/**
 * Generic field to use in filter edition popup.
 * 
 * @author jlouisy
 */
public class GpmGenericField extends AbstractGpmField<DeckPanel> implements
        BusinessField {

    private List<AbstractGpmField<?>> fieldList;

    /**
     * Constructor
     */
    public GpmGenericField() {
        super(new DeckPanel());
    }

    /**
     * Set all fields that can be displayed
     * 
     * @param pFieldList
     *            all fields, only one will be displayed at the same time
     */
    public void setFieldList(List<AbstractGpmField<?>> pFieldList) {
        fieldList = pFieldList;
        for (AbstractGpmField<?> lField : fieldList) {
            getWidget().add(lField.getWidget());
            lField.getWidget().setWidth("");
            lField.getWidget().setHeight("");
        }
        if (!fieldList.isEmpty()) {
            getWidget().showWidget(0);
        }
    }

    /**
     * Return the selected field (the visible one if there is none)
     * 
     * @return the selected field
     */
    public AbstractGpmField<?> getSelectedField() {
        for (AbstractGpmField<?> lField : fieldList) {
            if (lField.getWidget().isVisible()) {
                return lField;
            }
        }
        return null; // Should not happen
    }

    /**
     * Create a clone
     * 
     * @return a clone
     */
    @Override
    public AbstractGpmField<DeckPanel> getEmptyClone() {
        GpmGenericField lClone = new GpmGenericField();
        lClone.setFieldDescription(this.getFieldDescription());
        lClone.setFieldName(this.getFieldName());
        return lClone;
    }

    /**
     * Enable: unused
     * 
     * @param pEnabled
     *            unused
     */
    @Override
    public void setEnabled(boolean pEnabled) {
    }

    /**
     * Copy: unused
     * 
     * @param pOther
     *            unused
     */
    @Override
    public void copy(BusinessField pOther) {
    }

    @Override
    public String getAsString() {
        return null;
    }

    /**
     * Always false
     * 
     * @param pOther
     *            any Business field
     * @return false
     */
    @Override
    public boolean hasSameValues(BusinessField pOther) {
        return false;
    }

    @Override
    public boolean isUpdatable() {
        return true;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.field.AbstractGpmField#isEnabled()
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

}