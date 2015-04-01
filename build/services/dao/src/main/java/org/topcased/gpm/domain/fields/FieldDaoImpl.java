/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.domain.fields;

import org.hibernate.Query;

/**
 * Dao query for fields.
 * 
 * @author mkargbo
 */
public class FieldDaoImpl
        extends
        org.topcased.gpm.domain.AbstractDao<org.topcased.gpm.domain.fields.Field, java.lang.String>
        implements org.topcased.gpm.domain.fields.FieldDao {
    /**
     * Constructor
     */
    public FieldDaoImpl() {
        super(org.topcased.gpm.domain.fields.Field.class);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.fields.FieldDaoBase#hasValues(org.topcased.gpm.domain.fields.Field)
     */
    public Boolean hasValues(final Field pField) {
        final String lQueryStr =
                "select count(fieldValue) "
                        + " FROM org.topcased.gpm.domain.fields.FieldValue as fieldValue"
                        + " WHERE fieldValue.field.id = :fieldId";

        final Query lQuery = getSession(false).createQuery(lQueryStr);
        lQuery.setParameter("fieldId", pField.getId());

        Long lCount = (Long) lQuery.uniqueResult();

        if (null == lCount || lCount == 0) {
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.fields.FieldDaoBase#getParent(org.topcased.gpm.domain.fields.Field)
     */
    public MultipleField getParent(final Field pField) {
        final String lQueryStr =
                "from " + MultipleField.class.getName() + " multipleField"
                        + " WHERE :field in elements(multipleField.fields)";

        final Query lQuery = getSession(false).createQuery(lQueryStr);
        lQuery.setParameter("field", pField);

        return (MultipleField) lQuery.uniqueResult();
    }

    private static final String IS_FIELD_EXISTS =
            "SELECT field.id FROM "
                    + Field.class.getName()
                    + " field "
                    + "WHERE field.labelKey = :fieldLabelKey AND field.container.name = :typeName";

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.fields.FieldDaoBase#isFieldExists(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public Boolean isFieldExists(String pTypeName, String pFieldLabelKey) {
        final Query lQuery = createQuery(IS_FIELD_EXISTS);
        lQuery.setParameter("fieldLabelKey", pFieldLabelKey);
        lQuery.setParameter("typeName", pTypeName);
        return hasResult(lQuery);
    }

    /**
     * @see org.topcased.gpm.domain.fields.Field#getAllAttrNames(org.topcased.gpm.domain.attributes.AttributesContainer)
     */
    @SuppressWarnings("rawtypes")
    public java.util.List getAllAttrNames(
            final org.topcased.gpm.domain.attributes.AttributesContainer pAttrContainer) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.fields.Field as field where field.attrContainer.id = :attrContainerId";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("attrContainerId", pAttrContainer.getId());
            java.util.List lResults = lQueryObject.list();
            return lResults;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.topcased.gpm.domain.fields.Field#getAttributes(org.topcased.gpm.domain.attributes.AttributesContainer,
     *      java.lang.String[])
     */
    @SuppressWarnings("rawtypes")
    public java.util.List getAttributes(
            final org.topcased.gpm.domain.attributes.AttributesContainer pAttrContainer,
            final java.lang.String[] pAttrNames) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.fields.Field as field where field.attrContainer.id = :attrContainerId and field.attrNames = :attrNames";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("attrContainerId", pAttrContainer.getId());
            lQueryObject.setParameterList("attrNames", pAttrNames);
            java.util.List lResults = lQueryObject.list();
            return lResults;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.topcased.gpm.domain.fields.Field#getAttribute(org.topcased.gpm.domain.attributes.AttributesContainer,
     *      java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public org.topcased.gpm.domain.attributes.Attribute getAttribute(
            final org.topcased.gpm.domain.attributes.AttributesContainer pAttrContainer,
            final java.lang.String pAttrName) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.fields.Field as field where field.attrContainer.id = :attrContainerId and field.attrName = :attrName";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("attrContainerId", pAttrContainer.getId());
            lQueryObject.setParameter("attrName", pAttrName);
            java.util.List lResults = lQueryObject.list();
            org.topcased.gpm.domain.attributes.Attribute lResult = null;
            if (lResults != null) {
                if (lResults.size() > 1) {
                    throw new org.springframework.dao.InvalidDataAccessResourceUsageException(
                            "More than one instance of 'org.topcased.gpm.domain.attributes.Attribute"
                                    + "' was found when executing query --> '"
                                    + lQueryString + "'");
                }
                else if (lResults.size() == 1) {
                    lResult =
                            (org.topcased.gpm.domain.attributes.Attribute) lResults.iterator().next();
                }
            }
            return lResult;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

}