/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Joël GIAUFER (Atos Origin)
 ******************************************************************/

package org.topcased.gpm.ui.component.client.container.field.multivalued;

import java.util.List;

import org.topcased.gpm.ui.component.client.container.field.AbstractGpmField;
import org.topcased.gpm.ui.component.client.field.multivalued.GpmMultivaluedWidget;

/**
 * The GpmMultivaluedField to be instantiate to display an UIMultivaluedField
 * element
 * 
 * @author jgiaufer
 * @param <A>
 *            The type of abstract field.
 */
public class GpmMultivaluedField<A extends AbstractGpmField<?>> extends
        AbstractGpmMultivaluedField<A, GpmMultivaluedWidget<A>> {
    /**
     * Generic constructor
     * 
     * @param pGpmField
     *            The GpmField first value
     * @param pAddAvailable
     *            add action must be available in field
     * @param pRemoveAvailable
     *            delete line action must be available
     * @param pMoveAvailable
     *            move fields up and down must be available
     */
    public GpmMultivaluedField(final A pGpmField, boolean pAddAvailable,
            boolean pRemoveAvailable, boolean pMoveAvailable) {
        super(new GpmMultivaluedWidget<A>(pGpmField, pAddAvailable,
                pRemoveAvailable, pMoveAvailable));
    }

    /**
     * Get the template field.
     * 
     * @return The template field.
     */
    public A getTemplateField() {
        return getWidget().getTemplateField();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.multivalued.BusinessMultivaluedField#addLine()
     */
    @Override
    public A addLine() {
        return getWidget().addLine();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.multivalued.BusinessMultivaluedField#removeLine()
     */
    @Override
    public A removeLine() {
        final int lSize = size();

        if (lSize > 0) {
            final A lFirst = getFirst();

            getWidget().remove(size() - 1);

            return lFirst;
        }
        else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.field.multivalued.AbstractGpmMultivaluedField#getFieldList()
     */
    @Override
    public List<A> getFieldList() {
        return getWidget().getAvailableLines();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.field.AbstractGpmField#getEmptyClone()
     */
    @SuppressWarnings("unchecked")
    @Override
    public GpmMultivaluedField<A> getEmptyClone() {
        final GpmMultivaluedField<A> lClone =
                new GpmMultivaluedField<A>(
                        (A) getWidget().getTemplateField().getEmptyClone(),
                        getWidget().isAddButtonAvailable(),
                        getWidget().isRemoveButtonAvailable(),
                        getWidget().isMoveButtonsAvailable());

        initEmptyClone(lClone);

        return lClone;
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
     * @see org.topcased.gpm.business.values.field.BusinessField#isUpdatable()
     */
    @Override
    public boolean isUpdatable() {
        return getWidget().isAddButtonAvailable()
                || getWidget().isRemoveButtonAvailable()
                || getWidget().isMoveButtonsAvailable()
                || getTemplateField().isUpdatable();
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
}