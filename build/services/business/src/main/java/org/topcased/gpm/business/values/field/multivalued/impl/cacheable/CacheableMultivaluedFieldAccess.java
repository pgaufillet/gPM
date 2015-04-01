/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 *
 ******************************************************************/
package org.topcased.gpm.business.values.field.multivalued.impl.cacheable;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.serialization.data.Field;
import org.topcased.gpm.business.values.field.BusinessField;
import org.topcased.gpm.business.values.field.impl.cacheable.AbstractCacheableFieldAccess;
import org.topcased.gpm.business.values.field.impl.cacheable.CacheableFieldAccessFactory;
import org.topcased.gpm.business.values.field.multiple.impl.cacheable.CacheableMultipleFieldAccess;
import org.topcased.gpm.business.values.field.multivalued.BusinessMultivaluedField;
import org.topcased.gpm.business.values.field.simple.BusinessAttachedField;
import org.topcased.gpm.business.values.impl.cacheable.ICacheableParentAccess;
import org.topcased.gpm.util.bean.GpmPair;
import org.topcased.gpm.util.iterator.GpmPairIterator;

/**
 * Access on a multi valued field.
 * 
 * @author tpanuel
 * @param <A>
 *            The type of field access.
 * @param <F>
 *            The type of field value to access.
 */
public class CacheableMultivaluedFieldAccess<A extends BusinessField, F>
        extends AbstractCacheableFieldAccess<Field, List<F>> implements
        BusinessMultivaluedField<A>, ICacheableParentAccess {
    private final static String DEFAULT_SEPARATOR = " ";

    private final CacheableFieldAccessFactory fieldFactory;

    /**
     * Create an access on an empty multi value field.
     * 
     * @param pMultivaluedField
     *            The multi valued field to access.
     * @param pParentAccess
     *            The parent access.
     */
    public CacheableMultivaluedFieldAccess(final Field pMultivaluedField,
            final ICacheableParentAccess pParentAccess) {
        super(pMultivaluedField, new LinkedList<F>(), pParentAccess, false);
        // Field must be multi valued
        if (!pMultivaluedField.isMultivalued()) {
            throw new GDMException(pMultivaluedField.getLabelKey()
                    + " is not multi valued.");
        }
        fieldFactory = new CacheableFieldAccessFactory(this);
    }

    /**
     * Create an access on a multi value field.
     * 
     * @param pMultivaluedField
     *            The multi valued field to access.
     * @param pValues
     *            The values to access : not a list if only one value.
     * @param pParentAccess
     *            The parent access.
     */
    @SuppressWarnings("unchecked")
    public CacheableMultivaluedFieldAccess(final Field pMultivaluedField,
            final Object pValues, final ICacheableParentAccess pParentAccess) {
        // 
        super(pMultivaluedField, (List<F>) initList(pValues), pParentAccess,
                false);
        // Field must be multi valued
        if (!pMultivaluedField.isMultivalued()) {
            throw new GDMException(pMultivaluedField.getLabelKey()
                    + " is not multi valued.");
        }
        fieldFactory = new CacheableFieldAccessFactory(this);
    }

    /**
     * Initialize the list of values
     * 
     * @param pValues
     *            The values
     * @return The values list
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private final static List initList(final Object pValues) {
        final List lNewValues = new LinkedList();

        if (pValues != null) {
            if (pValues instanceof List) {
                for (Object lValue : (List) pValues) {
                    if (lValue != null) {
                        lNewValues.add(lValue);
                    }
                }
            }
            else {
                lNewValues.add(pValues);
            }
        }

        return lNewValues;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.multivalued.BusinessMultivaluedField#get(int)
     */
    @SuppressWarnings("unchecked")
    public A get(final int pIndex) {
        // If the index superior at the number of value,> add values
        while (pIndex >= size()) {
            read().add(null);
        }

        final AbstractCacheableFieldAccess<?, F> lFieldAccess =
                fieldFactory.getMonovaluedFieldAccess(String.valueOf(pIndex));

        // If the value is not present in the list, add it
        if (read().get(pIndex) == null) {
            read().set(pIndex, lFieldAccess.read());
        }

        return (A) lFieldAccess;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.multivalued.BusinessMultivaluedField#getFirst()
     */
    public A getFirst() {
        return get(0);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.multivalued.BusinessMultivaluedField#addLine()
     */
    public A addLine() {
        write();
        return get(size());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.multivalued.BusinessMultivaluedField#removeLine()
     */
    public A removeLine() {
        final int lLastLineIndex = size() - 1;
        final A lLastAccess = get(lLastLineIndex);

        // Remove access
        write().remove(size() - 1);

        return lLastAccess;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.multivalued.BusinessMultivaluedField#removeLine(int)
     */
    public A removeLine(int pIndex) {
        if (pIndex >= size()) {
            throw new IndexOutOfBoundsException();
        }

        final A lAccess = get(pIndex);

        // Remove access
        write().remove(pIndex);

        return lAccess;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.multivalued.BusinessMultivaluedField#size()
     */
    public int size() {
        final List<F> lValues = read();

        if (lValues == null) {
            return 0;
        }
        else {
            return lValues.size();
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Iterable#iterator()
     */
    public Iterator<A> iterator() {
        return new CacheableMultivaluedFieldAccessIterator<A>(this);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.impl.cacheable.ICacheableParentAccess#addNewValue(org.topcased.gpm.business.values.field.impl.cacheable.AbstractCacheableFieldAccess)
     */
    public void addNewValue(
            final AbstractCacheableFieldAccess<?, ?> pFieldAccess) {
        write();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.impl.cacheable.ICacheableParentAccess#getFieldType(java.lang.String)
     */
    public Field getFieldType(final String pFieldName) {
        return getFieldType();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.impl.cacheable.ICacheableParentAccess#getValue(java.lang.String)
     */
    public Object getValue(final String pFieldName) {
        final int lIndex = Integer.parseInt(pFieldName);

        if (lIndex >= size()) {
            throw new IndexOutOfBoundsException();
        }

        return read().get(lIndex);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#hasSameValues(org.topcased.gpm.business.values.field.BusinessField)
     */
    public boolean hasSameValues(final BusinessField pOther) {
        if (!(pOther instanceof BusinessMultivaluedField)) {
            return false;
        }
        else if (pOther == null || pOther.isEmpty()) {
            return isEmpty();
        }
        else {
            final GpmPairIterator<BusinessField, BusinessField> lPairIterator =
                    new GpmPairIterator<BusinessField, BusinessField>(this,
                            pOther);

            // Test if each access has the same values
            while (lPairIterator.hasNext()) {
                final GpmPair<BusinessField, BusinessField> lCurrentElement =
                        lPairIterator.next();

                if (lCurrentElement.getFirst() == null
                        || !lCurrentElement.getFirst().hasSameValues(
                                lCurrentElement.getSecond())) {
                    return false;
                }
            }

            return true;
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#isEmpty()
     */
    public boolean isEmpty() {
        // Test if each sub access is empty
        for (BusinessField lFieldAccess : this) {
            if (!lFieldAccess.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * All the lines are removed. At the end this multivalued field access will
     * have no more field access. If a field access is requested a new one will
     * be created
     * 
     * @see CacheableMultivaluedFieldAccess#get(int) {@inheritDoc}
     * @see org.topcased.gpm.business.values.field.BusinessField#clear()
     */
    public void clear() {
        if (size() > 0) {
            // Remove all values 
            while (size() > 0) {
                removeLine();
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#reset()
     */
    public void reset() {
        if (size() > 0) {
            // Remove all values except the last one
            while (size() > 1) {
                // Remove last line
                removeLine();
            }
            get(0).reset();
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#copy(org.topcased.gpm.business.values.field.BusinessField)
     */
    public void copy(final BusinessField pOther) {
        if (pOther != null) {
            if (pOther instanceof BusinessMultivaluedField<?>) {
                final BusinessMultivaluedField<?> lOther =
                        (BusinessMultivaluedField<?>) pOther;

                clear();
                // Copy all sub values
                for (int i = 0; i < lOther.size(); i++) {
                    if (lOther.get(i) instanceof BusinessAttachedField) {
                        BusinessAttachedField lAttachedField =
                                (BusinessAttachedField) lOther.get(i);
                        if (!StringUtils.isBlank(lAttachedField.getFileName())
                                || lAttachedField.getContent() != null) {
                            get(i).copy(lOther.get(i));
                        }
                    }
                    else {
                        if (get(i) instanceof CacheableMultipleFieldAccess) {
                            if (((CacheableMultipleFieldAccess) get(i)).verifyIfErase(
                                    lOther.get(i))) {
                                removeLine(i);
                            }
                            else {
                                get(i).copy(lOther.get(i));
                            }
                        }
                        else {
                            get(i).copy(lOther.get(i));
                        }
                    }
                }
            }
            else {
                throw new GDMException("Cannot copy "
                        + pOther.getClass().getName() + " on "
                        + getClass().getName() + ".");
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final Iterator<A> lFields = iterator();
        final StringBuffer lStringValue = new StringBuffer();
        final String lFieldSeparator = DEFAULT_SEPARATOR;

        while (lFields.hasNext()) {
            lStringValue.append(lFields.next().toString());
            if (lFields.hasNext()) {
                lStringValue.append(lFieldSeparator);
            }
        }

        return lStringValue.toString();
    }

}