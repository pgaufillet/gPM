/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.container.field.multivalued;

import java.util.Iterator;
import java.util.List;

import org.topcased.gpm.business.values.field.BusinessField;
import org.topcased.gpm.business.values.field.multivalued.BusinessMultivaluedField;
import org.topcased.gpm.ui.component.client.container.field.AbstractGpmField;
import org.topcased.gpm.ui.component.client.exception.NotImplementedException;

import com.google.gwt.user.client.ui.Widget;

/**
 * Super class for multivalued fields.
 * 
 * @author tpanuel
 */
public abstract class AbstractGpmMultivaluedField<A extends BusinessField, W extends Widget>
        extends AbstractGpmField<W> implements BusinessMultivaluedField<A> {
    /**
     * Create an abstract multivalued field.
     * 
     * @param pWidget
     */
    public AbstractGpmMultivaluedField(final W pWidget) {
        super(pWidget);
    }

    /**
     * Get the list of business fields.
     * 
     * @return The list of business fields.
     */
    abstract public List<A> getFieldList();

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<A> iterator() {
        return getFieldList().iterator();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.multivalued.BusinessMultivaluedField#size()
     */
    @Override
    public int size() {
        return getFieldList().size();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#getAsString()
     */
    @Override
    public String getAsString() {
        final StringBuilder lRes = new StringBuilder();

        for (final A lField : getFieldList()) {
            String lValue = lField.getAsString();
            if (lValue != null) {
                lRes.append(lValue);
            }
        }

        return lRes.toString();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.multivalued.BusinessMultivaluedField#get(int)
     */
    @Override
    public A get(final int pIndex) {
        final List<A> lFieldList = getFieldList();

        if (pIndex < lFieldList.size()) {
            return lFieldList.get(pIndex);
        }
        else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.multivalued.BusinessMultivaluedField#getFirst()
     */
    @Override
    public A getFirst() {
        return get(0);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#clear()
     */
    @Override
    public void clear() {
        final List<A> lFieldList = getFieldList();
        final int lSize = lFieldList.size();

        if (lSize > 0) {
            for (int i = 1; i < lSize; i++) {
                removeLine();
            }
            //lFieldList.get(0).clear();
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#copy(org.topcased.gpm.business.values.field.BusinessField)
     */
    @SuppressWarnings("unchecked")
    @Override
    public void copy(final BusinessField pOther) {
        final BusinessMultivaluedField<BusinessField> lOther =
                (BusinessMultivaluedField<BusinessField>) pOther;

        // Clear first : just one empty element
        clear();
        // Copy first element
        if (lOther.size() > 0) {
            final BusinessField lFirst = getFirst();
            final BusinessField lOtherFirst = lOther.getFirst();

            if (lFirst != null && lOtherFirst != null) {
                getFirst().copy(lOtherFirst);
            }
        }
        // Copy other elements
        for (int i = 1; i < lOther.size(); i++) {
            addLine().copy(lOther.get(i));
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#hasSameValues(org.topcased.gpm.business.values.field.BusinessField)
     */
    @SuppressWarnings("unchecked")
    @Override
    public boolean hasSameValues(final BusinessField pOther) {
        final BusinessMultivaluedField<A> lOtherMv =
                (BusinessMultivaluedField<A>) pOther;
        final List<A> lElements = getFieldList();

        // The same of number of line is need
        if (lElements.size() != lOtherMv.size()) {
            return false;
        }
        // All elements must have same values
        for (int i = 0; i < lElements.size(); i++) {
            if (!lElements.get(i).hasSameValues(lOtherMv.get(i))) {
                return false;
            }
        }

        return true;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.multivalued.BusinessMultivaluedField#removeLine(int)
     */
    @Override
    public A removeLine(final int pIndex) {
        throw new NotImplementedException("Not implemented method");
    }
}